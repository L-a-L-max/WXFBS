package com.wx.fbsir.business.websocket.server;

import com.wx.fbsir.business.websocket.config.WebSocketProperties;
import com.wx.fbsir.business.websocket.message.EngineMessage;
import com.wx.fbsir.business.websocket.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
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
     */
    private final Map<String, EngineSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * Engine ID -> Session ID 映射（支持快速查找）
     */
    private final Map<String, String> engineIdToSessionId = new ConcurrentHashMap<>();

    private final WebSocketProperties properties;

    public EngineSessionManager(WebSocketProperties properties) {
        this.properties = properties;
        log.info("[会话管理] 初始化完成 - 最大连接数: {}", properties.getMaxConnections());
    }

    /**
     * 添加会话
     *
     * @param session WebSocket 会话
     * @return EngineSession 对象
     */
    public EngineSession addSession(WebSocketSession session) {
        // 检查连接数限制
        if (sessionMap.size() >= properties.getMaxConnections()) {
            log.warn("[会话管理] 连接数已达上限: {}, 拒绝新连接", properties.getMaxConnections());
            return null;
        }

        String sessionId = session.getId();
        // 临时 engineId，注册后会更新
        String tempEngineId = "pending-" + sessionId;
        
        EngineSession engineSession = new EngineSession(tempEngineId, session);
        sessionMap.put(sessionId, engineSession);
        
        log.debug("[会话] 新连接 - SessionID: {}", sessionId);
        
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
     * 移除会话（带关闭状态码）
     *
     * @param sessionId   会话ID
     * @param closeStatus 关闭状态码
     */
    public void removeSession(String sessionId, CloseStatus closeStatus) {
        EngineSession session = sessionMap.remove(sessionId);
        if (session != null) {
            // 移除 engineId 映射
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
            
            log.debug("[会话] 已移除 - EngineID: {}", engineId);
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
     * 发送消息给指定 Engine（String 格式）
     */
    public void sendToEngine(String engineId, String message) {
        EngineSession session = getSessionByEngineId(engineId);
        if (session == null || !session.isValid()) {
            log.warn("[会话管理] Engine 不在线 - EngineID: {}", engineId);
            return;
        }

        try {
            synchronized (session.getSession()) {
                session.getSession().sendMessage(new TextMessage(message));
            }
            log.debug("[会话管理] 消息发送成功 - EngineID: {}", engineId);
        } catch (IOException e) {
            log.error("[会话管理] 消息发送失败 - EngineID: {}, 错误: {}", engineId, e.getMessage());
        }
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
     * 发送消息给指定会话
     *
     * @param session 会话对象
     * @param message 消息对象
     * @return 是否发送成功
     */
    public boolean sendMessage(EngineSession session, EngineMessage message) {
        if (session == null || !session.isValid()) {
            return false;
        }

        try {
            String json = message.toJson();
            synchronized (session.getSession()) {
                session.getSession().sendMessage(new TextMessage(json));
            }
            log.debug("[会话管理] 消息发送成功 - EngineID: {}, 类型: {}", 
                session.getEngineId(), message.getType());
            return true;
        } catch (IOException e) {
            log.error("[会话管理] 消息发送失败 - EngineID: {}, 错误: {}", 
                session.getEngineId(), e.getMessage());
            return false;
        }
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
     */
    @Scheduled(fixedDelayString = "${wxfbsir.websocket.session-cleanup-interval:60}000")
    public void cleanupSessions() {
        int heartbeatTimeout = properties.getHeartbeatInterval() + properties.getHeartbeatTimeout();
        int cleanedCount = 0;

        for (Map.Entry<String, EngineSession> entry : sessionMap.entrySet()) {
            EngineSession session = entry.getValue();
            
            // 检查会话是否有效
            if (!session.isValid()) {
                removeSession(entry.getKey());
                cleanedCount++;
                continue;
            }

            // 检查心跳超时（只检查已注册的会话）
            if (session.getStatus() == EngineSession.SessionStatus.REGISTERED 
                && session.isHeartbeatTimeout(heartbeatTimeout)) {
                log.warn("[会话管理] 心跳超时，移除会话 - EngineID: {}", session.getEngineId());
                removeSession(entry.getKey());
                cleanedCount++;
            }
        }

        if (cleanedCount > 0) {
            log.debug("[会话] 清理过期: {}", cleanedCount);
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
