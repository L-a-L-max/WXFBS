package com.wx.fbsir.engine.websocket.client;

import com.wx.fbsir.engine.config.EngineProperties;
import com.wx.fbsir.engine.util.DeviceInfoUtil;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import com.wx.fbsir.engine.websocket.message.MessageType;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Engine WebSocket 客户端
 * 
 * 企业级 WebSocket 客户端实现，解决老项目的以下问题：
 * - 心跳机制不完善
 * - 重连逻辑存在竞态条件
 * - 连接状态管理混乱
 *
 * @author wxfbsir
 * @date 2025-12-15
 */
public class EngineWebSocketClient extends WebSocketClient {

    private static final Logger log = LoggerFactory.getLogger(EngineWebSocketClient.class);

    private final EngineProperties properties;
    private final ScheduledExecutorService scheduler;
    private final Consumer<EngineMessage> messageHandler;

    /**
     * 心跳定时任务
     */
    private ScheduledFuture<?> heartbeatTask;

    /**
     * 是否正在重连中（避免竞态条件）
     */
    private final AtomicBoolean reconnecting = new AtomicBoolean(false);

    /**
     * 是否主动关闭（区分异常断开和主动关闭）
     */
    private final AtomicBoolean intentionalClose = new AtomicBoolean(false);

    /**
     * 重连次数
     */
    private final AtomicInteger reconnectCount = new AtomicInteger(0);

    /**
     * 最后一次收到消息的时间
     */
    private final AtomicLong lastMessageTime = new AtomicLong(System.currentTimeMillis());

    /**
     * 最后一次发送心跳的时间
     */
    private final AtomicLong lastHeartbeatTime = new AtomicLong(0);

    /**
     * 第一次断开的时间（用于1分钟超时检查）
     */
    private final AtomicLong firstDisconnectTime = new AtomicLong(0);

    /**
     * 能力列表（连接时上报给 Admin）
     */
    private volatile java.util.List<java.util.Map<String, Object>> capabilities;

    /**
     * 构造器
     *
     * @param serverUri      服务器地址
     * @param properties     配置属性
     * @param scheduler      调度器
     * @param messageHandler 消息处理器
     */
    public EngineWebSocketClient(URI serverUri,
                                  EngineProperties properties,
                                  ScheduledExecutorService scheduler,
                                  Consumer<EngineMessage> messageHandler) {
        super(serverUri);
        this.properties = properties;
        this.scheduler = scheduler;
        this.messageHandler = messageHandler;
        
        // 设置连接超时
        this.setConnectionLostTimeout(properties.getConnection().getHeartbeatTimeout());
        
        log.info("[Engine] 初始化完成 - 目标: {}", serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        // 重置状态
        reconnectCount.set(0);
        reconnecting.set(false);
        firstDisconnectTime.set(0);
        lastMessageTime.set(System.currentTimeMillis());
        
        // 发送注册消息
        sendRegisterMessage();
        
        // 启动心跳检测
        startHeartbeat();
    }

    @Override
    public void onMessage(String message) {
        lastMessageTime.set(System.currentTimeMillis());
        
        if (message == null || message.isEmpty()) {
            log.warn("[WebSocket] 收到空消息，已忽略");
            return;
        }
        
        log.debug("[WebSocket] 收到消息 - 长度: {} 字符", message.length());
        
        try {
            EngineMessage engineMessage = EngineMessage.fromJson(message);
            if (engineMessage == null) {
                log.warn("[WebSocket] 消息解析失败 - 原始消息: {}", 
                    message.length() > 200 ? message.substring(0, 200) + "..." : message);
                return;
            }
            
            // 处理系统消息（心跳响应、注册确认等）
            if (handleSystemMessage(engineMessage)) {
                return;
            }
            
            // 业务消息交给处理器
            if (messageHandler != null) {
                messageHandler.accept(engineMessage);
            }
            
        } catch (Exception e) {
            log.error("[WebSocket] 消息处理异常 - 错误类型: {}, 错误信息: {}", 
                e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // 停止心跳
        stopHeartbeat();
        
        // 判断是否曾经成功连接过（用于区分黑名单拒绝和主节点宕机）
        boolean everConnected = lastMessageTime.get() > 0;
        
        // 从reason中解析HTTP状态码（握手失败时reason包含HTTP响应信息）
        int httpStatusCode = parseHttpStatusCode(reason);
        
        // ==================== 明确拒绝场景（立即退出，不重试） ====================
        
        // 1. 服务器主动拒绝（认证/授权失败）
        if (code == 1011 || code == 1008 || code == 1003) {
            intentionalClose.set(true);
            printFatalError("服务器拒绝连接", 
                "服务器主动关闭了连接\n" +
                "1. 检查主机ID是否在白名单中\n" +
                "2. 检查IP是否被封禁\n" +
                "3. 检查主节点服务是否正常\n" +
                "4. 状态码: " + code + ", 原因: " + (reason != null ? reason : "未知"));
            return;
        }
        
        // 2. 重复连接被拒绝（4008）
        if (code == 4008) {
            intentionalClose.set(true);
            printFatalError("重复连接被拒绝", 
                "主机ID已有活跃连接，不允许重复连接\n" +
                "1. 可能有另一个相同主机ID的实例正在运行\n" +
                "2. 请检查是否有重复启动的进程\n" +
                "3. 如果确认没有其他实例，请等待旧连接超时后重试\n" +
                "4. 主机ID: " + properties.getHostId());
            return;
        }
        
        // 3. 管理员断开（4007）- 立即退出，不重试
        if (code == 4007) {
            intentionalClose.set(true);
            printFatalError("被管理员断开", 
                "管理员主动断开了您的连接\n" +
                "1. 请联系管理员了解原因\n" +
                "2. 主机ID: " + properties.getHostId());
            return;
        }
        
        // 4. 服务过载（1013）- 立即退出，不重试
        if (code == 1013) {
            intentionalClose.set(true);
            printFatalError("服务器过载", 
                "主节点连接数已达上限，无法接受新连接\n" +
                "1. 请稍后再试\n" +
                "2. 如果持续出现，请联系管理员扩容\n" +
                "3. 主节点地址: " + properties.getAdmin().getWsUrl());
            return;
        }
        
        // 5. HTTP 403 - 黑名单拒绝（立即退出，不重试）
        if (httpStatusCode == 403) {
            intentionalClose.set(true);
            printFatalError("IP被黑名单拒绝", 
                "您的IP地址已被加入黑名单，无法连接主节点\n" +
                "1. 请联系管理员解除黑名单\n" +
                "2. 您的IP: " + getLocalIpForLog() + "\n" +
                "3. 主节点地址: " + properties.getAdmin().getWsUrl());
            return;
        }
        
        // 6. HTTP 429 - 限流拒绝（立即退出，不重试）
        if (httpStatusCode == 429) {
            intentionalClose.set(true);
            printFatalError("连接被限流拒绝", 
                "连接请求过于频繁，已被限流\n" +
                "1. 请稍后再试\n" +
                "2. 如果持续出现，请联系管理员\n" +
                "3. 您的IP: " + getLocalIpForLog());
            return;
        }
        
        // 7. 握手阶段被拒绝（其他4xx错误）- 从未成功连接过
        if (!everConnected && code == 1002 && httpStatusCode >= 400 && httpStatusCode < 500) {
            intentionalClose.set(true);
            printFatalError("连接被服务器拒绝", 
                "WebSocket握手失败，服务器拒绝连接\n" +
                "1. HTTP状态码: " + httpStatusCode + "\n" +
                "2. 请检查主机ID是否正确配置\n" +
                "3. 请联系管理员检查您的IP: " + getLocalIpForLog() + "\n" +
                "4. 主节点地址: " + properties.getAdmin().getWsUrl());
            return;
        }
        
        // ==================== 可重试场景（主节点宕机/网络问题） ====================
        // 只有曾经成功连接过，才认为是主节点宕机，允许重试
        
        if (!intentionalClose.get() && properties.getReconnect().isEnabled() && everConnected) {
            // 记录第一次断开时间
            if (firstDisconnectTime.get() == 0) {
                firstDisconnectTime.set(System.currentTimeMillis());
                log.warn("[Engine] 连接断开（曾成功连接），尝试重连... (状态码: {}, HTTP: {}, 原因: {})", 
                    code, httpStatusCode > 0 ? httpStatusCode : "N/A", reason != null ? reason : "未知");
            }
            
            // 检查是否超过1分钟
            long disconnectDuration = System.currentTimeMillis() - firstDisconnectTime.get();
            if (disconnectDuration > 60000) {
                intentionalClose.set(true);
                printFatalError("重连超时", 
                    "已尝试重连1分钟，仍无法连接主节点\n" +
                    "1. 检查主节点是否已启动\n" +
                    "2. 检查网络连接是否正常\n" +
                    "3. 检查主节点地址配置: " + properties.getAdmin().getWsUrl());
                return;
            }
            
            log.warn("[Engine] 准备重连...");
            scheduleReconnect();
            return;
        }
        
        // ==================== 首次连接失败（非黑名单，可能是主节点未启动） ====================
        // 从未成功连接过，但不是明确的拒绝错误（可能是连接超时、网关超时等）
        if (!everConnected && !intentionalClose.get() && properties.getReconnect().isEnabled()) {
            // 记录第一次断开时间
            if (firstDisconnectTime.get() == 0) {
                firstDisconnectTime.set(System.currentTimeMillis());
                log.warn("[Engine] 首次连接失败，尝试重连... (状态码: {}, HTTP: {}, 原因: {})", 
                    code, httpStatusCode > 0 ? httpStatusCode : "N/A", reason != null ? reason : "未知");
            }
            
            // 首次连接失败也允许重试1分钟（可能是主节点还没启动）
            long disconnectDuration = System.currentTimeMillis() - firstDisconnectTime.get();
            if (disconnectDuration > 60000) {
                intentionalClose.set(true);
                printFatalError("连接失败", 
                    "尝试连接1分钟，仍无法连接主节点\n" +
                    "1. 检查主节点是否已启动\n" +
                    "2. 检查网络连接是否正常\n" +
                    "3. 检查主节点地址配置: " + properties.getAdmin().getWsUrl() + "\n" +
                    "4. 状态码: " + code + (httpStatusCode > 0 ? ", HTTP: " + httpStatusCode : ""));
                return;
            }
            
            log.warn("[Engine] 准备重连...");
            scheduleReconnect();
        }
    }
    
    /**
     * 从关闭原因中解析HTTP状态码
     * 格式示例: "Invalid status code received: 403 Status line: HTTP/1.1 403 Forbidden"
     */
    private int parseHttpStatusCode(String reason) {
        if (reason == null || reason.isEmpty()) {
            return 0;
        }
        try {
            // 尝试匹配 "status code received: XXX" 格式
            if (reason.contains("status code received:")) {
                String[] parts = reason.split("status code received:");
                if (parts.length > 1) {
                    String codePart = parts[1].trim().split(" ")[0];
                    return Integer.parseInt(codePart);
                }
            }
            // 尝试匹配 "HTTP/1.1 XXX" 格式
            if (reason.contains("HTTP/")) {
                int httpIndex = reason.indexOf("HTTP/");
                String httpPart = reason.substring(httpIndex);
                String[] parts = httpPart.split(" ");
                if (parts.length >= 2) {
                    return Integer.parseInt(parts[1]);
                }
            }
        } catch (Exception e) {
            // 解析失败，返回0
        }
        return 0;
    }
    
    /**
     * 获取本机IP用于日志显示
     */
    private String getLocalIpForLog() {
        try {
            return DeviceInfoUtil.getLocalIp();
        } catch (Exception e) {
            return "unknown";
        }
    }

    @Override
    public void onError(Exception ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        
        // 连接被拒绝或超时时，允许重连而不是立即退出
        // 只有在明确的认证/授权失败时才退出
        log.warn("[Engine] 连接错误: {}", msg);
    }
    
    /**
     * 打印致命错误并退出
     */
    private void printFatalError(String title, String details) {
        System.err.println();
        System.err.println("╔════════════════════════════════════════════════════");
        System.err.println("║  Engine 启动失败");
        System.err.println("╠════════════════════════════════════════════════════");
        System.err.println("║  错误: " + title);
        System.err.println("╠════════════════════════════════════════════════════");
        System.err.println("║  解决方案:");
        for (String line : details.split("\n")) {
            System.err.println("║    " + line);
        }
        System.err.println("╚════════════════════════════════════════════════════");
        System.exit(1);
    }

    /**
     * 设置能力列表（连接前调用）
     */
    public void setCapabilities(java.util.List<java.util.Map<String, Object>> capabilities) {
        this.capabilities = capabilities;
    }

    /**
     * 发送注册消息（包含主机ID、设备信息和能力列表）
     */
    private void sendRegisterMessage() {
        // 获取设备信息
        java.util.Map<String, String> deviceInfo = DeviceInfoUtil.getDeviceInfo();
        String deviceId = DeviceInfoUtil.getDeviceId();
        
        // 获取IP地址信息
        String localIp = DeviceInfoUtil.getLocalIp();
        String publicIp = DeviceInfoUtil.getPublicIp();
        
        EngineMessage registerMsg = EngineMessage.builder()
            .type(MessageType.ENGINE_REGISTER)
            .engineId(properties.getHostId())  // 使用主机ID作为engineId
            .payload("hostId", properties.getHostId())
            .payload("version", properties.getVersion())
            .payload("deviceId", deviceId)
            .payload("localIp", localIp)  // 内网IP
            .payload("publicIp", publicIp != null ? publicIp : "unknown")  // 公网出口IP
            .payload("macAddress", deviceInfo.getOrDefault("macAddress", "unknown"))
            .payload("hostname", deviceInfo.getOrDefault("hostname", "unknown"))
            .payload("osName", deviceInfo.getOrDefault("osName", "unknown"))
            .payload("osVersion", deviceInfo.getOrDefault("osVersion", "unknown"))
            .payload("javaVersion", deviceInfo.getOrDefault("javaVersion", "unknown"))
            .payload("capabilities", capabilities != null ? capabilities : java.util.Collections.emptyList())
            .build();
        
        sendMessage(registerMsg);
        log.info("[Engine] 发送注册 - 主机ID: {}, 能力数量: {}", 
            properties.getHostId(), capabilities != null ? capabilities.size() : 0);
    }

    /**
     * 处理系统消息
     *
     * @param message 消息
     * @return true 如果是系统消息已处理，false 需要继续处理
     */
    private boolean handleSystemMessage(EngineMessage message) {
        MessageType type = message.getMessageType();
        
        switch (type) {
            case HEARTBEAT_PONG:
                log.debug("[WebSocket] 收到心跳响应");
                return true;
                
            case ENGINE_REGISTER_ACK:
                log.info("[Engine] ========================================");
                log.info("[Engine] 连接成功 - ID: {}", properties.getHostId());
                log.info("[Engine] ========================================");
                return true;
                
            case ERROR:
                handleErrorMessage(message);
                return true;
                
            default:
                return false;
        }
    }

    /**
     * 处理错误消息
     * 根据错误类型决定是否需要终止程序，并给出友好提示
     */
    private void handleErrorMessage(EngineMessage message) {
        String errorCode = message.getPayloadValue("code");
        String errorMsg = message.getPayloadValue("message");
        
        // 获取友好的错误说明
        String friendlyTip = getFriendlyErrorTip(errorCode);
        
        if (friendlyTip != null) {
            // 致命错误：打印友好提示后终止程序
            intentionalClose.set(true);
            
            // 关闭连接
            try {
                close();
            } catch (Exception e) {
                log.debug("[WebSocket] 关闭连接时发生异常 - 错误: {}", e.getMessage());
            }
            
            // 输出友好的错误提示
            System.err.println();
            System.err.println("╔════════════════════════════════════════════════════════════╗");
            System.err.println("║                    Engine 启动失败                          ║");
            System.err.println("╠════════════════════════════════════════════════════════════╣");
            System.err.println("║ 错误: " + padRight(errorMsg, 53) + "║");
            System.err.println("╠════════════════════════════════════════════════════════════╣");
            System.err.println("║ 解决方案:                                                   ║");
            for (String line : friendlyTip.split("\n")) {
                System.err.println("║   " + padRight(line, 57) + "║");
            }
            System.err.println("╚════════════════════════════════════════════════════════════╝");
            
            System.exit(1);
        } else {
            // 非致命错误：仅记录日志
            log.warn("[Engine] 警告: {} - {}", errorCode, errorMsg);
        }
    }

    /**
     * 获取友好的错误提示
     * @return 提示信息，null表示非致命错误
     */
    private String getFriendlyErrorTip(String errorCode) {
        if (errorCode == null) {
            return null;
        }
        
        return switch (errorCode) {
            case "EMPTY_HOST_ID" -> 
                "主机ID未配置\n" +
                "1. 编辑 application.yml\n" +
                "2. 设置 wxfbsir.engine.host-id 为有效的主机ID\n" +
                "3. 主机ID需向管理员申请";
                
            case "HOST_NOT_IN_WHITELIST" -> 
                "主机ID未授权\n" +
                "1. 检查 application.yml 中的 host-id 是否正确\n" +
                "2. 联系管理员将此主机ID添加到白名单\n" +
                "3. 当前配置的主机ID: " + properties.getHostId();
                
            case "HOST_DISABLED" -> 
                "主机ID已被禁用\n" +
                "1. 联系管理员了解禁用原因\n" +
                "2. 请求管理员重新启用此主机ID\n" +
                "3. 或申请新的主机ID";
                
            case "HOST_EXPIRED" -> 
                "主机ID已过期\n" +
                "1. 联系管理员续期此主机ID\n" +
                "2. 或申请新的主机ID";
                
            case "IP_NOT_ALLOWED" -> 
                "当前IP不在允许列表中\n" +
                "1. 联系管理员将当前IP添加到允许列表\n" +
                "2. 或从允许的IP地址启动Engine";
                
            case "IP_BLOCKED" -> 
                "当前IP已被封禁\n" +
                "1. 联系管理员了解封禁原因\n" +
                "2. 请求解除封禁或更换IP地址";
                
            case "DUPLICATE_CONNECTION" -> 
                "此主机ID已有其他Engine连接\n" +
                "1. 检查是否有其他Engine使用相同的主机ID\n" +
                "2. 停止其他Engine或使用不同的主机ID\n" +
                "3. 一个主机ID只能有一个Engine连接";
                
            case "ADMIN_DISCONNECT" -> 
                "被管理员主动断开\n" +
                "1. 联系管理员了解断开原因\n" +
                "2. 解决问题后重新启动";
                
            default -> null;
        };
    }
    
    /**
     * 字符串右填充（用于格式化输出）
     */
    private static String padRight(String s, int n) {
        if (s == null) s = "";
        if (s.length() >= n) return s.substring(0, n);
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < n) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /**
     * 发送消息
     *
     * @param message 消息对象
     */
    public void sendMessage(EngineMessage message) {
        if (!isOpen()) {
            log.warn("[WebSocket] 连接未建立，消息发送失败 - 消息类型: {}", message.getType());
            return;
        }
        
        try {
            String json = message.toJson();
            send(json);
            log.debug("[WebSocket] 消息发送成功 - 类型: {}, 长度: {} 字符", 
                message.getType(), json.length());
        } catch (Exception e) {
            log.error("[WebSocket] 消息发送失败 - 错误: {}", e.getMessage());
        }
    }

    /**
     * 启动心跳检测
     */
    private void startHeartbeat() {
        if (heartbeatTask != null && !heartbeatTask.isCancelled()) {
            heartbeatTask.cancel(false);
        }
        
        int interval = properties.getConnection().getHeartbeatInterval();
        
        heartbeatTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                if (!isOpen()) {
                    return;
                }
                
                // 检查心跳超时
                long lastMsg = lastMessageTime.get();
                int timeout = properties.getConnection().getHeartbeatTimeout();
                if (System.currentTimeMillis() - lastMsg > (interval + timeout) * 1000L) {
                    log.warn("[WebSocket] 心跳超时，主动断开连接");
                    close();
                    return;
                }
                
                // 发送心跳
                EngineMessage heartbeat = EngineMessage.builder()
                    .type(MessageType.HEARTBEAT_PING)
                    .engineId(properties.getHostId())
                    .build();
                
                sendMessage(heartbeat);
                lastHeartbeatTime.set(System.currentTimeMillis());
                log.debug("[WebSocket] 发送心跳");
                
            } catch (Exception e) {
                log.error("[WebSocket] 心跳发送异常 - 错误: {}", e.getMessage());
            }
        }, interval, interval, TimeUnit.SECONDS);
        
        log.debug("[Engine] 心跳已启动 - 间隔: {}秒", interval);
    }

    /**
     * 停止心跳检测
     */
    private void stopHeartbeat() {
        if (heartbeatTask != null && !heartbeatTask.isCancelled()) {
            heartbeatTask.cancel(false);
            heartbeatTask = null;
            log.debug("[WebSocket] 心跳检测已停止");
        }
    }

    /**
     * 调度重连（指数退避策略）
     */
    private void scheduleReconnect() {
        // 避免重复调度重连
        if (!reconnecting.compareAndSet(false, true)) {
            log.debug("[WebSocket] 已在重连中，跳过本次调度");
            return;
        }
        
        int maxRetries = properties.getReconnect().getMaxRetries();
        int currentRetry = reconnectCount.incrementAndGet();
        
        // 检查重试次数
        if (maxRetries > 0 && currentRetry > maxRetries) {
            log.error("[WebSocket] 重连次数已达上限: {}, 停止重连", maxRetries);
            reconnecting.set(false);
            return;
        }
        
        // 计算重连延迟（指数退避）
        int initialDelay = properties.getReconnect().getInitialDelay();
        int maxDelay = properties.getReconnect().getMaxDelay();
        double multiplier = properties.getReconnect().getBackoffMultiplier();
        
        long delay = (long) (initialDelay * Math.pow(multiplier, currentRetry - 1));
        delay = Math.min(delay, maxDelay);
        
        log.warn("[Engine] {}秒后第{}次重连", delay, currentRetry);
        
        scheduler.schedule(() -> {
            try {
                if (intentionalClose.get()) {
                    log.debug("[WebSocket] 检测到主动关闭，取消重连");
                    return;
                }
                
                log.info("[Engine] 重连中...");
                reconnect();
                
            } catch (Exception e) {
                log.error("[WebSocket] 重连异常 - 错误: {}", e.getMessage());
            } finally {
                reconnecting.set(false);
            }
        }, delay, TimeUnit.SECONDS);
    }

    /**
     * 主动关闭连接
     */
    public void closeGracefully() {
        log.info("[Engine] 关闭中...");
        intentionalClose.set(true);
        stopHeartbeat();
        
        // 发送注销消息
        if (isOpen()) {
            try {
                EngineMessage unregisterMsg = EngineMessage.builder()
                    .type(MessageType.ENGINE_UNREGISTER)
                    .engineId(properties.getHostId())
                    .build();
                sendMessage(unregisterMsg);
                
                // 等待消息发送完成
                Thread.sleep(500);
            } catch (Exception e) {
                log.warn("[WebSocket] 发送注销消息失败 - 错误: {}", e.getMessage());
            }
        }
        
        close();
        log.info("[Engine] 已关闭");
    }

    /**
     * 获取连接状态信息
     */
    public ConnectionStatus getStatus() {
        return new ConnectionStatus(
            isOpen(),
            reconnecting.get(),
            reconnectCount.get(),
            lastMessageTime.get(),
            lastHeartbeatTime.get()
        );
    }

    /**
     * 连接状态信息
     */
    public record ConnectionStatus(
        boolean connected,
        boolean reconnecting,
        int reconnectCount,
        long lastMessageTime,
        long lastHeartbeatTime
    ) {}
}
