package com.wx.fbsir.business.websocket.server;

import com.wx.fbsir.business.websocket.config.WebSocketProperties;
import com.wx.fbsir.business.websocket.message.EngineMessage;
import com.wx.fbsir.business.websocket.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Engine 会话管理器
 * 
 * 统一管理所有 Engine 连接，解决老项目的以下问题：
 * - 会话未及时清理导致内存泄漏
 * - 连接状态管理混乱
 * - 缺乏心跳超时检测
 *
 * @author wxfbsir
 * @date 2025-12-15
 */
@Component
public class EngineSessionManager {

    private static final Logger log = LoggerFactory.getLogger(EngineSessionManager.class);

    /**
     * Session ID -> EngineSession 映射
     * <p>线程安全：使用ConcurrentHashMap保证并发访问安全
     * <p>生命周期：Session从连接建立到断开的完整生命周期
     */
    private final Map<String, EngineSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * Engine ID -> Session ID 映射（支持快速查找）
     * <p>设计目的：避免遍历sessionMap查找特定Engine
     * <p>一致性：与sessionMap保持同步更新
     */
    private final Map<String, String> engineIdToSessionId = new ConcurrentHashMap<>();

    private final WebSocketProperties properties;
    private final AsyncMessageSender asyncMessageSender;
    
    /**
     * 连接数信号量（并发控制，防止资源耗尽）
     * <p>设计原理：
     * <ul>
     *   <li>限制最大并发连接数，保护服务器资源</li>
     *   <li>使用tryAcquire带超时，避免无限等待</li>
     *   <li>每个连接占用1个permit，断开时释放</li>
     * </ul>
     * <p>⚠️ 关键：必须确保release()在所有异常路径中都被调用
     */
    private final Semaphore connectionSemaphore;
    
    /**
     * 获取连接槽位超时时间（毫秒）
     * <p>超时后拒绝新连接，返回503错误
     */
    private static final long ACQUIRE_TIMEOUT_MS = 5000;

    public EngineSessionManager(WebSocketProperties properties, AsyncMessageSender asyncMessageSender) {
        this.properties = properties;
        this.asyncMessageSender = asyncMessageSender;
        this.connectionSemaphore = new Semaphore(properties.getMaxConnections());
        log.info("[会话管理] 初始化完成 - 最大连接数: {}, 信号量已初始化", properties.getMaxConnections());
    }

    /**
     * 添加会话（使用Semaphore控制并发）
     *
     * @param session WebSocket 会话
     * @return EngineSession 对象，如果获取槽位失败则返回null
     */
    public EngineSession addSession(WebSocketSession session) {
        // 使用Semaphore控制连接数，带超时机制
        boolean acquired = false;
        try {
            acquired = connectionSemaphore.tryAcquire(ACQUIRE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!acquired) {
                log.warn("[会话管理] 连接数已达上限: {}, 拒绝新连接", properties.getMaxConnections());
                return null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[会话管理] 获取连接槽位被中断");
            return null;
        }

        String sessionId = session.getId();
        // 临时 engineId，注册后会更新
        String tempEngineId = "pending-" + sessionId;
        
        EngineSession engineSession = new EngineSession(tempEngineId, session);
        sessionMap.put(sessionId, engineSession);
        
        log.debug("[会话] 新连接 - SessionID: {}, 剩余槽位: {}", sessionId, connectionSemaphore.availablePermits());
        
        return engineSession;
    }

    /**
     * 注册 Engine（更新 engineId）
     *
     * @param sessionId  会话ID
     * @param engineId   Engine ID
     * @param version    版本
     * @param capabilities 能力列表
     * @return 注册是否成功
     */
    public boolean registerEngine(String sessionId, String engineId, String version, List<Map<String, Object>> capabilities) {
        EngineSession session = sessionMap.get(sessionId);
        if (session == null) {
            log.warn("[会话管理] 注册失败，会话不存在 - SessionID: {}", sessionId);
            return false;
        }

        // 检查是否已有同 engineId 的连接
        String existingSessionId = engineIdToSessionId.get(engineId);
        if (existingSessionId != null && !existingSessionId.equals(sessionId)) {
            EngineSession existingSession = sessionMap.get(existingSessionId);
            // 检查旧连接是否真的还活着
            if (existingSession != null && existingSession.getSession() != null 
                    && existingSession.getSession().isOpen()) {
                // 旧连接仍然有效，拒绝新连接
                log.warn("[会话管理] EngineID 已被占用，拒绝新连接 - EngineID: {}, 已有SessionID: {}, 新SessionID: {}", 
                    engineId, existingSessionId, sessionId);
                return false;
            } else {
                // 旧连接已失效，清理后允许新连接
                log.info("[会话管理] 清理失效的旧连接 - EngineID: {}, 旧SessionID: {}", engineId, existingSessionId);
                sessionMap.remove(existingSessionId);
                engineIdToSessionId.remove(engineId);
            }
        }

        // 更新会话信息
        session.setVersion(version);
        session.setCapabilities(capabilities);
        session.setStatus(EngineSession.SessionStatus.REGISTERED);
        session.updateHeartbeatTime();

        // 更新映射
        engineIdToSessionId.put(engineId, sessionId);
        
        // 创建新的 EngineSession 对象（因为 engineId 是 final）
        EngineSession newSession = new EngineSession(engineId, session.getSession());
        newSession.setVersion(version);
        newSession.setCapabilities(capabilities);
        newSession.setStatus(EngineSession.SessionStatus.REGISTERED);
        newSession.updateHeartbeatTime();
        sessionMap.put(sessionId, newSession);
        
        return true;
    }

    /**
     * 更新设备信息
     */
    public void updateDeviceInfo(String sessionId, Map<String, Object> deviceInfo) {
        EngineSession session = sessionMap.get(sessionId);
        if (session != null) {
            session.setDeviceInfo(deviceInfo);
        }
    }

    /**
     * 移除会话
     *
     * @param sessionId 会话ID
     */
    public void removeSession(String sessionId) {
        removeSession(sessionId, CloseStatus.NORMAL);
    }

    /**
     * 移除会话（带关闭状态码，释放信号量）
     * <p>执行步骤：
     * <ol>
     *   <li>从sessionMap移除</li>
     *   <li>从engineIdToSessionId移除（保持映射一致性）</li>
     *   <li>设置Session状态为DISCONNECTED</li>
     *   <li>关闭底层WebSocketSession</li>
     *   <li>释放Semaphore槽位</li>
     *   <li>清理AsyncMessageSender资源</li>
     * </ol>
     * <p>⚠️ 关键：确保Semaphore在所有路径中都被释放
     *
     * @param sessionId   会话ID
     * @param closeStatus 关闭状态码
     */
    public void removeSession(String sessionId, CloseStatus closeStatus) {
        EngineSession session = sessionMap.remove(sessionId);
        if (session != null) {
            try {
                // 移除 engineId 映射（保持一致性）
                String engineId = session.getEngineId();
                if (engineId != null && !engineId.startsWith("pending-")) {
                    engineIdToSessionId.remove(engineId);
                }
                
                session.setStatus(EngineSession.SessionStatus.DISCONNECTED);
                
                // 关闭 WebSocket 连接
                try {
                    if (session.getSession() != null && session.getSession().isOpen()) {
                        session.getSession().close(closeStatus);
                    }
                } catch (IOException e) {
                    log.warn("[会话管理] 关闭会话异常 - SessionID: {}, 错误: {}", sessionId, e.getMessage());
                }
                
                log.debug("[会话] 已移除 - EngineID: {}, 剩余槽位: {}", engineId, connectionSemaphore.availablePermits());
                
            } finally {
                // 确保Semaphore在所有异常路径中都被释放
                connectionSemaphore.release();
            }
        }
    }

    /**
     * 根据 Session ID 获取会话
     */
    public EngineSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    /**
     * 根据 Engine ID 获取会话
     */
    public EngineSession getSessionByEngineId(String engineId) {
        String sessionId = engineIdToSessionId.get(engineId);
        if (sessionId == null) {
            return null;
        }
        return sessionMap.get(sessionId);
    }

    /**
     * 检查 Engine 是否在线
     */
    public boolean isEngineOnline(String engineId) {
        EngineSession session = getSessionByEngineId(engineId);
        return session != null && session.isValid() 
            && session.getStatus() == EngineSession.SessionStatus.REGISTERED;
    }

    /**
     * 获取所有在线 Engine ID 列表
     */
    public List<String> getOnlineEngineIds() {
        return getRegisteredSessions().stream()
            .map(EngineSession::getEngineId)
            .collect(Collectors.toList());
    }

    /**
     * 获取所有已注册的会话
     */
    public List<EngineSession> getRegisteredSessions() {
        return sessionMap.values().stream()
            .filter(s -> s.getStatus() == EngineSession.SessionStatus.REGISTERED)
            .collect(Collectors.toList());
    }

    /**
     * 发送消息给指定 Engine
     *
     * @param engineId Engine ID
     * @param message  消息对象
     * @return 是否发送成功
     */
    public boolean sendMessage(String engineId, EngineMessage message) {
        EngineSession session = getSessionByEngineId(engineId);
        if (session == null || !session.isValid()) {
            log.warn("[会话管理] 发送消息失败，会话不存在或无效 - EngineID: {}", engineId);
            return false;
        }

        return sendMessage(session, message);
    }

    /**
     * 发送消息给指定会话（异步发送，避免锁竞争）
     *
     * @param session 会话对象
     * @param message 消息对象
     * @return 是否加入发送队列成功
     */
    public boolean sendMessage(EngineSession session, EngineMessage message) {
        if (session == null || !session.isValid()) {
            return false;
        }

        boolean queued = asyncMessageSender.sendMessage(session.getSession(), message);
        if (queued) {
            session.incrementMessageSent();
        }
        return queued;
    }

    /**
     * 广播消息给所有已注册的 Engine
     *
     * @param message 消息对象
     * @return 成功发送的数量
     */
    public int broadcast(EngineMessage message) {
        int successCount = 0;
        for (EngineSession session : getRegisteredSessions()) {
            if (sendMessage(session, message)) {
                successCount++;
            }
        }
        log.debug("[会话管理] 广播消息完成 - 类型: {}, 成功数: {}/{}", 
            message.getType(), successCount, getRegisteredSessions().size());
        return successCount;
    }

    /**
     * 定时清理过期会话
     * <p>清理逻辑：
     * <ol>
     *   <li>检查会话有效性（底层WebSocketSession是否open）</li>
     *   <li>检查心跳超时（仅针对已注册会话）</li>
     *   <li>移除过期会话并释放Semaphore</li>
     * </ol>
     * <p>⚠️ 并发安全：使用ArrayList避免ConcurrentModificationException
     * <p>🔴 P0修复：缩短清理间隔至30秒，与心跳间隔一致，及时检测断线
     */
    @Scheduled(fixedDelayString = "${wxfbsir.websocket.session-cleanup-interval:30}000")
    public void cleanupSessions() {
        int heartbeatTimeout = properties.getHeartbeatInterval() + properties.getHeartbeatTimeout();
        
        // 收集需要清理的sessionId，避免在遍历时修改Map
        java.util.List<String> toRemove = new java.util.ArrayList<>();

        for (Map.Entry<String, EngineSession> entry : sessionMap.entrySet()) {
            EngineSession session = entry.getValue();
            
            // 检查会话是否有效
            if (!session.isValid()) {
                toRemove.add(entry.getKey());
                continue;
            }

            // 检查心跳超时（只检查已注册的会话）
            if (session.getStatus() == EngineSession.SessionStatus.REGISTERED 
                && session.isHeartbeatTimeout(heartbeatTimeout)) {
                log.warn("[会话管理] 心跳超时，移除会话 - EngineID: {}", session.getEngineId());
                toRemove.add(entry.getKey());
            }
        }
        
        // 批量移除
        for (String sessionId : toRemove) {
            removeSession(sessionId);
        }

        if (!toRemove.isEmpty()) {
            log.debug("[会话] 清理过期: {}", toRemove.size());
        }
    }

    /**
     * 获取连接统计信息
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalConnections", sessionMap.size());
        stats.put("registeredConnections", getRegisteredSessions().size());
        stats.put("maxConnections", properties.getMaxConnections());
        stats.put("availableSlots", connectionSemaphore.availablePermits());
        
        // 按状态统计
        Map<EngineSession.SessionStatus, Long> statusCount = sessionMap.values().stream()
            .collect(Collectors.groupingBy(EngineSession::getStatus, Collectors.counting()));
        stats.put("statusCount", statusCount);
        
        return stats;
    }

    /**
     * 优雅关闭所有连接
     */
    @PreDestroy
    public void shutdown() {
        log.debug("[会话] 开始关闭...");
        
        // 发送关闭通知
        EngineMessage shutdownMsg = EngineMessage.builder()
            .type(MessageType.ERROR)
            .payload("message", "Server is shutting down")
            .payload("code", "SERVER_SHUTDOWN")
            .build();
        
        broadcast(shutdownMsg);
        
        // 关闭所有会话
        for (String sessionId : new ArrayList<>(sessionMap.keySet())) {
            removeSession(sessionId);
        }
        
        log.debug("[会话] 关闭完成");
    }
}
