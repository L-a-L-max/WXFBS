package com.wx.fbsir.business.websocket.server;

import com.wx.fbsir.business.websocket.config.WebSocketProperties;
import com.wx.fbsir.business.websocket.message.EngineMessage;
import com.wx.fbsir.business.websocket.message.MessageType;
import com.wx.fbsir.business.websocket.service.ConnectionLogService;
import com.wx.fbsir.business.websocket.service.WhitelistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Engine WebSocket 处理器
 * 
 * 处理 Engine 节点的 WebSocket 连接和消息
 * 解决老项目的以下问题：
 * - 单一 onMessage 方法 500+ 行 if-else
 * - 消息处理没有异常隔离
 * - 缺乏消息类型校验
 *
 * @author wxfbsir
 * @date 2025-12-15
 */
@Component
public class EngineWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(EngineWebSocketHandler.class);

    private final EngineSessionManager sessionManager;
    private final WebSocketProperties properties;
    private final EngineMessageRouter messageRouter;
    private final WhitelistService whitelistService;
    private final ConnectionLogService connectionLogService;

    public EngineWebSocketHandler(EngineSessionManager sessionManager,
                                   WebSocketProperties properties,
                                   EngineMessageRouter messageRouter,
                                   WhitelistService whitelistService,
                                   ConnectionLogService connectionLogService) {
        this.sessionManager = sessionManager;
        this.properties = properties;
        this.messageRouter = messageRouter;
        this.whitelistService = whitelistService;
        this.connectionLogService = connectionLogService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        String remoteIp = getRemoteIp(session);
        Integer remotePort = getRemotePort(session);
        
        log.debug("[WebSocket] 新连接 - IP: {}", remoteIp);
        
        // 记录连接请求
        connectionLogService.logConnection(sessionId, remoteIp, remotePort, "/ws/engine");
        
        // 检查IP黑名单（连接阶段只检查IP，主机ID在注册时验证）
        WhitelistService.ValidationResult ipResult = whitelistService.checkIpBlacklist(remoteIp);
        if (!ipResult.isValid()) {
            log.warn("[WebSocket] IP被拒绝 - IP: {}, 原因: {}", remoteIp, ipResult.getMessage());
            connectionLogService.updateRejected(sessionId, null, 
                ConnectionLogService.STATUS_REJECT_BLACKLIST, ipResult.getMessage());
            
            sendErrorAndClose(session, ipResult.getMessage(), ipResult.getCode(), CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        
        EngineSession engineSession = sessionManager.addSession(session);
        if (engineSession == null) {
            // 连接数超限，拒绝连接
            log.warn("[WebSocket] 连接数超限 - IP: {}", remoteIp);
            connectionLogService.updateRejected(sessionId, null,
                ConnectionLogService.STATUS_REJECT_WHITELIST, "连接数超限");
            
            sendErrorAndClose(session, "服务器连接数已达上限，请稍后重试", 
                "CONNECTION_LIMIT_EXCEEDED", CloseStatus.SERVICE_OVERLOAD);
            return;
        }
        
        // 设置消息大小限制
        session.setTextMessageSizeLimit(properties.getMaxMessageSize());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = session.getId();
        String payload = message.getPayload();
        
        log.debug("[WebSocket] 收到消息 - SessionID: {}, 长度: {} 字符", sessionId, payload.length());
        
        // 解析消息
        EngineMessage engineMessage = EngineMessage.fromJson(payload);
        if (engineMessage == null) {
            log.warn("[WebSocket] 消息解析失败 - SessionID: {}, 原始消息: {}", 
                sessionId, payload.length() > 200 ? payload.substring(0, 200) + "..." : payload);
            sendError(session, "消息格式错误", "INVALID_MESSAGE_FORMAT");
            return;
        }
        
        // 更新会话活跃时间
        EngineSession engineSession = sessionManager.getSession(sessionId);
        if (engineSession != null) {
            engineSession.updateActiveTime();
        }
        
        // 处理系统消息
        if (handleSystemMessage(session, engineMessage)) {
            return;
        }
        
        // 检查会话是否已注册
        if (engineSession == null || engineSession.getStatus() != EngineSession.SessionStatus.REGISTERED) {
            log.warn("[WebSocket] 会话未注册，拒绝处理业务消息 - SessionID: {}", sessionId);
            sendError(session, "请先发送注册消息", "NOT_REGISTERED");
            return;
        }
        
        // 路由到业务处理器
        messageRouter.route(engineSession, engineMessage);
    }

    /**
     * 处理系统消息
     *
     * @return true 已处理，false 需要继续处理
     */
    private boolean handleSystemMessage(WebSocketSession session, EngineMessage message) {
        MessageType type = message.getMessageType();
        String sessionId = session.getId();
        
        switch (type) {
            case ENGINE_REGISTER:
                return handleRegister(session, message);
                
            case HEARTBEAT_PING:
                return handleHeartbeat(session, message);
                
            case ENGINE_UNREGISTER:
                return handleUnregister(session, message);
                
            default:
                return false;
        }
    }

    /**
     * 处理 Engine 注册（包含白名单验证）
     */
    private boolean handleRegister(WebSocketSession session, EngineMessage message) {
        String sessionId = session.getId();
        String remoteIp = getRemoteIp(session);
        
        // 获取注册信息（hostId 从 engineId 字段获取）
        String hostId = message.getEngineId();
        String version = message.getPayloadValue("version");
        String deviceId = message.getPayloadValue("deviceId");
        Object capabilitiesObj = message.getPayloadValue("capabilities");
        
        // 获取副节点上报的IP信息
        String clientLocalIp = message.getPayloadValue("localIp");
        String clientPublicIp = message.getPayloadValue("publicIp");
        
        // 确定用于黑名单验证的有效IP（优先使用副节点上报的公网IP）
        String effectiveIp = determineEffectiveIp(remoteIp, clientPublicIp, clientLocalIp);
        
        // 收集设备信息用于记录
        Map<String, Object> deviceInfo = new HashMap<>();
        deviceInfo.put("hostname", message.getPayloadValue("hostname"));
        deviceInfo.put("osName", message.getPayloadValue("osName"));
        deviceInfo.put("osVersion", message.getPayloadValue("osVersion"));
        deviceInfo.put("javaVersion", message.getPayloadValue("javaVersion"));
        deviceInfo.put("macAddress", message.getPayloadValue("macAddress"));
        deviceInfo.put("localIp", clientLocalIp);
        deviceInfo.put("publicIp", clientPublicIp);
        deviceInfo.put("connectionIp", remoteIp);  // 连接来源IP（可能是代理IP）
        
        log.debug("[WebSocket] 注册请求 - HostID: {}, 连接IP: {}, 公网IP: {}, 有效IP: {}", 
            hostId, remoteIp, clientPublicIp, effectiveIp);
        
        // 1. 验证主机ID是否为空
        if (hostId == null || hostId.trim().isEmpty()) {
            log.warn("[WebSocket] 拒绝: 主机ID为空 - IP: {}", remoteIp);
            connectionLogService.updateRejected(sessionId, hostId, 
                ConnectionLogService.STATUS_REJECT_WHITELIST, "主机ID为空");
            sendError(session, "主机ID不能为空，请向管理员申请主机ID", "EMPTY_HOST_ID");
            return true;
        }
        
        // 2. 验证白名单（使用有效IP进行黑名单检查）
        WhitelistService.ValidationResult whitelistResult = whitelistService.validateHostId(hostId, effectiveIp);
        if (!whitelistResult.isValid()) {
            log.warn("[拒绝] {} - {}", whitelistResult.getCode(), hostId);
            connectionLogService.updateRejected(sessionId, hostId, 
                ConnectionLogService.STATUS_REJECT_WHITELIST, whitelistResult.getMessage());
            sendError(session, whitelistResult.getMessage(), whitelistResult.getCode());
            return true;
        }
        
        // 3. 检查是否已有连接（单连接限制）
        WhitelistService.ValidationResult duplicateResult = whitelistService.checkDuplicateConnection(hostId);
        if (!duplicateResult.isValid()) {
            log.warn("[WebSocket] 拒绝: 重复连接 - HostID: {}", hostId);
            connectionLogService.updateRejected(sessionId, hostId, 
                ConnectionLogService.STATUS_REJECT_DUPLICATE, duplicateResult.getMessage());
            sendError(session, duplicateResult.getMessage(), duplicateResult.getCode());
            return true;
        }
        
        // 4. 解析能力列表（必须是 Map 格式）
        List<Map<String, Object>> capabilities = new java.util.ArrayList<>();
        if (capabilitiesObj instanceof List<?> rawList) {
            for (Object item : rawList) {
                if (item instanceof Map<?, ?> mapItem) {
                    Map<String, Object> cap = new HashMap<>();
                    mapItem.forEach((k, v) -> cap.put(String.valueOf(k), v));
                    capabilities.add(cap);
                }
            }
        }
        
        // 5. 注册 Engine
        boolean success = sessionManager.registerEngine(sessionId, hostId, version, capabilities);
        
        if (success) {
            // 更新设备信息到会话
            sessionManager.updateDeviceInfo(sessionId, deviceInfo);
            
            // 记录注册成功（在线状态存储在内存EngineSessionManager中，不写数据库）
            connectionLogService.updateRegistered(sessionId, hostId, deviceId, version, deviceInfo);
            
            // 发送注册确认
            EngineMessage ackMsg = EngineMessage.builder()
                .type(MessageType.ENGINE_REGISTER_ACK)
                .engineId(hostId)
                .payload("message", "注册成功")
                .payload("serverTime", System.currentTimeMillis())
                .build();
            
            try {
                session.sendMessage(new TextMessage(ackMsg.toJson()));
                log.info("[Engine] ========================================");
                log.info("[Engine] 节点已注册 - ID: {}, 版本: {}", hostId, version);
                log.info("[Engine] ========================================");
            } catch (Exception e) {
                log.error("[WebSocket] 发送注册确认失败 - HostID: {}, 错误: {}", hostId, e.getMessage());
            }
        } else {
            connectionLogService.updateRejected(sessionId, hostId, 
                ConnectionLogService.STATUS_REJECT_DUPLICATE, "EngineID已被占用");
            sendError(session, "主机ID [" + hostId + "] 已有活跃连接，请勿重复连接", "DUPLICATE_CONNECTION");
            // 关闭新连接
            try {
                session.close(new CloseStatus(4008, "EngineID已被占用"));
            } catch (Exception e) {
                log.warn("[WebSocket] 关闭重复连接失败: {}", e.getMessage());
            }
        }
        
        return true;
    }

    /**
     * 处理心跳（只更新内存中的统计，不写数据库）
     */
    private boolean handleHeartbeat(WebSocketSession session, EngineMessage message) {
        String sessionId = session.getId();
        EngineSession engineSession = sessionManager.getSession(sessionId);
        
        if (engineSession != null) {
            engineSession.updateHeartbeatTime();
        }
        
        // 发送心跳响应
        EngineMessage pongMsg = EngineMessage.builder()
            .type(MessageType.HEARTBEAT_PONG)
            .payload("serverTime", System.currentTimeMillis())
            .build();
        
        try {
            session.sendMessage(new TextMessage(pongMsg.toJson()));
        } catch (Exception e) {
            log.warn("[WebSocket] 发送心跳响应失败 - SessionID: {}", sessionId);
        }
        
        return true;
    }

    /**
     * 处理 Engine 注销
     */
    private boolean handleUnregister(WebSocketSession session, EngineMessage message) {
        String sessionId = session.getId();
        String hostId = message.getEngineId();
        
        log.debug("[WebSocket] 注销 - {}", hostId);
        
        // 更新连接记录（在线状态由 sessionManager.removeSession 自动清理内存）
        connectionLogService.updateDisconnected(sessionId, 1000, "主动注销", true);
        
        sessionManager.removeSession(sessionId);
        
        return true;
    }

    /**
     * 发送错误消息
     */
    private void sendError(WebSocketSession session, String message, String code) {
        try {
            EngineMessage errorMsg = EngineMessage.builder()
                .type(MessageType.ERROR)
                .payload("message", message)
                .payload("code", code)
                .build();
            session.sendMessage(new TextMessage(errorMsg.toJson()));
        } catch (Exception e) {
            log.error("[WebSocket] 发送错误消息失败 - 错误: {}", e.getMessage());
        }
    }

    /**
     * 发送错误消息并关闭连接
     */
    private void sendErrorAndClose(WebSocketSession session, String message, String code, CloseStatus closeStatus) {
        try {
            EngineMessage errorMsg = EngineMessage.builder()
                .type(MessageType.ERROR)
                .payload("message", message)
                .payload("code", code)
                .build();
            session.sendMessage(new TextMessage(errorMsg.toJson()));
            session.close(closeStatus);
        } catch (Exception e) {
            log.error("[WebSocket] 发送错误消息并关闭连接失败 - 错误: {}", e.getMessage());
            try {
                session.close(closeStatus);
            } catch (Exception ex) {
                log.error("[WebSocket] 关闭连接失败 - 错误: {}", ex.getMessage());
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String sessionId = session.getId();
        
        // 转换为友好的错误原因
        String friendlyReason = getFriendlyErrorReason(exception);
        log.error("[WebSocket] 传输错误 - SessionID: {}, 原因: {}", sessionId, friendlyReason);
        
        // 获取会话信息，记录统计并清理
        EngineSession engineSession = sessionManager.getSession(sessionId);
        if (engineSession != null) {
            engineSession.recordError(friendlyReason);
            
            if (engineSession.getEngineId() != null && !engineSession.getEngineId().startsWith("pending-")) {
                // 断开时统一写入统计信息
                connectionLogService.updateDisconnectedWithStats(
                    sessionId, null, friendlyReason,
                    ConnectionLogService.STATUS_ABNORMAL_CLOSE,
                    engineSession.getMessageSent(), engineSession.getMessageReceived(),
                    engineSession.getHeartbeatCount(), engineSession.getErrorCount(),
                    friendlyReason
                );
            }
        }
        
        sessionManager.removeSession(sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        
        // 获取会话信息，记录统计并清理
        EngineSession engineSession = sessionManager.getSession(sessionId);
        if (engineSession != null && engineSession.getEngineId() != null 
            && !engineSession.getEngineId().startsWith("pending-")) {
            
            // 根据状态码判断断开类型和原因
            int dbStatus;
            String closeReason = getFriendlyCloseReason(status, engineSession.getLastError());
            
            if (status.getCode() == 4007) {
                // 管理员断开
                dbStatus = ConnectionLogService.STATUS_ADMIN_DISCONNECT;
            } else if (status.getCode() == CloseStatus.NORMAL.getCode()) {
                // 正常断开
                dbStatus = ConnectionLogService.STATUS_NORMAL_CLOSE;
            } else {
                // 异常断开
                dbStatus = ConnectionLogService.STATUS_ABNORMAL_CLOSE;
            }
            
            // 断开时统一写入统计信息（在线状态由 sessionManager 内存管理）
            connectionLogService.updateDisconnectedWithStats(
                sessionId, status.getCode(), closeReason, dbStatus,
                engineSession.getMessageSent(), engineSession.getMessageReceived(),
                engineSession.getHeartbeatCount(), engineSession.getErrorCount(),
                engineSession.getLastError()
            );
            
            log.info("[Engine] ========================================");
            log.info("[Engine] 主机 {} 已断开 - {}", engineSession.getEngineId(), closeReason);
            log.info("[Engine] ========================================");
        }
        
        sessionManager.removeSession(sessionId);
    }

    /**
     * 获取客户端IP地址
     * 支持反向代理场景，优先从HTTP头获取真实IP
     */
    private String getRemoteIp(WebSocketSession session) {
        try {
            // 1. 尝试从HTTP头获取真实IP（反向代理场景）
            String realIp = getHeaderValue(session, "X-Real-IP");
            if (realIp != null && !realIp.isEmpty() && !"unknown".equalsIgnoreCase(realIp)) {
                return realIp;
            }
            
            // 2. 尝试从X-Forwarded-For获取（可能有多个IP，取第一个）
            String forwardedFor = getHeaderValue(session, "X-Forwarded-For");
            if (forwardedFor != null && !forwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(forwardedFor)) {
                // X-Forwarded-For格式: client, proxy1, proxy2
                String[] ips = forwardedFor.split(",");
                if (ips.length > 0) {
                    return ips[0].trim();
                }
            }
            
            // 3. 尝试其他常见代理头
            String[] proxyHeaders = {"Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
            for (String header : proxyHeaders) {
                String ip = getHeaderValue(session, header);
                if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                    return ip;
                }
            }
            
            // 4. 最后使用直连IP
            if (session.getRemoteAddress() != null) {
                return session.getRemoteAddress().getAddress().getHostAddress();
            }
        } catch (Exception e) {
            log.warn("[WebSocket] 获取客户端IP失败: {}", e.getMessage());
        }
        return "unknown";
    }
    
    /**
     * 从WebSocket会话的握手头中获取指定header值
     */
    private String getHeaderValue(WebSocketSession session, String headerName) {
        try {
            if (session.getHandshakeHeaders() != null) {
                return session.getHandshakeHeaders().getFirst(headerName);
            }
        } catch (Exception e) {
            // 忽略
        }
        return null;
    }
    
    /**
     * 确定用于黑名单验证的有效IP
     * 
     * 优先级：
     * 1. 副节点上报的公网IP（最可靠，是副节点真实出口IP）
     * 2. 连接来源IP（如果不是内网IP或代理IP）
     * 3. 副节点上报的内网IP（最后备选）
     * 
     * @param connectionIp 连接来源IP（可能是代理服务器IP）
     * @param clientPublicIp 副节点上报的公网IP
     * @param clientLocalIp 副节点上报的内网IP
     * @return 用于黑名单验证的有效IP
     */
    private String determineEffectiveIp(String connectionIp, String clientPublicIp, String clientLocalIp) {
        // 1. 优先使用副节点上报的公网IP（最可靠）
        if (isValidPublicIp(clientPublicIp)) {
            return clientPublicIp;
        }
        
        // 2. 如果连接IP是有效的公网IP，使用连接IP
        if (isValidPublicIp(connectionIp)) {
            return connectionIp;
        }
        
        // 3. 如果副节点上报了内网IP，使用内网IP
        if (clientLocalIp != null && !clientLocalIp.isEmpty() && !"unknown".equals(clientLocalIp)) {
            return clientLocalIp;
        }
        
        // 4. 最后使用连接IP（即使是127.0.0.1）
        return connectionIp != null ? connectionIp : "unknown";
    }
    
    /**
     * 判断是否为有效的公网IP
     * 排除内网IP、回环IP、未知IP
     */
    private boolean isValidPublicIp(String ip) {
        if (ip == null || ip.isEmpty() || "unknown".equals(ip)) {
            return false;
        }
        
        // 排除回环地址
        if (ip.startsWith("127.") || "localhost".equals(ip)) {
            return false;
        }
        
        // 排除内网地址
        // 10.0.0.0 - 10.255.255.255
        if (ip.startsWith("10.")) {
            return false;
        }
        // 172.16.0.0 - 172.31.255.255
        if (ip.startsWith("172.")) {
            try {
                int second = Integer.parseInt(ip.split("\\.")[1]);
                if (second >= 16 && second <= 31) {
                    return false;
                }
            } catch (Exception e) {
                // 解析失败，认为是公网IP
            }
        }
        // 192.168.0.0 - 192.168.255.255
        if (ip.startsWith("192.168.")) {
            return false;
        }
        // 169.254.0.0 - 169.254.255.255 (链路本地地址)
        if (ip.startsWith("169.254.")) {
            return false;
        }
        
        return true;
    }

    /**
     * 获取客户端端口
     */
    private Integer getRemotePort(WebSocketSession session) {
        try {
            if (session.getRemoteAddress() != null) {
                return session.getRemoteAddress().getPort();
            }
        } catch (Exception e) {
            log.warn("[WebSocket] 获取客户端端口失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取友好的错误原因说明
     */
    private String getFriendlyErrorReason(Throwable exception) {
        if (exception == null) {
            return "未知错误";
        }
        
        String className = exception.getClass().getSimpleName();
        String message = exception.getMessage();
        
        // 根据异常类型返回友好说明
        return switch (className) {
            case "ClosedChannelException" -> "连接通道已关闭（主节点重启或网络中断）";
            case "IOException" -> {
                if (message != null && message.contains("Connection reset")) {
                    yield "连接被重置（网络异常或对端关闭）";
                } else if (message != null && message.contains("Broken pipe")) {
                    yield "连接管道断裂（网络中断）";
                }
                yield "IO异常: " + (message != null ? message : "未知");
            }
            case "SocketTimeoutException" -> "连接超时";
            case "ConnectException" -> "无法建立连接";
            case "EOFException" -> "连接意外终止（对端关闭）";
            default -> className + ": " + (message != null ? message : "无详细信息");
        };
    }

    /**
     * 获取友好的关闭原因说明
     */
    private String getFriendlyCloseReason(CloseStatus status, String lastError) {
        int code = status.getCode();
        String reason = status.getReason();
        
        // 如果有原始原因，优先使用
        if (reason != null && !reason.isEmpty()) {
            return reason;
        }
        
        // 根据状态码返回友好说明
        return switch (code) {
            case 1000 -> "正常关闭";
            case 1001 -> "端点离开（页面关闭或服务重启）";
            case 1002 -> "协议错误";
            case 1003 -> "不支持的数据类型";
            case 1005 -> "无状态码（异常关闭）";
            case 1006 -> {
                // 1006 是异常关闭，尝试从 lastError 获取更多信息
                if (lastError != null && !lastError.isEmpty()) {
                    if (lastError.contains("ClosedChannelException") || lastError.contains("通道已关闭")) {
                        yield "主节点重启或网络中断";
                    }
                    yield lastError;
                }
                yield "连接异常关闭（可能是主节点重启或网络中断）";
            }
            case 1007 -> "消息格式错误";
            case 1008 -> "策略违规";
            case 1009 -> "消息过大";
            case 1010 -> "客户端期望的扩展未被支持";
            case 1011 -> "服务器内部错误";
            case 1012 -> "服务重启";
            case 1013 -> "服务过载，稍后重试";
            case 1015 -> "TLS握手失败";
            case 4007 -> "管理员断开";
            default -> "关闭码: " + code;
        };
    }
}
