package com.wx.fbsir.engine.websocket.client;

import com.wx.fbsir.engine.capability.EngineCapabilityManager;
import com.wx.fbsir.engine.config.EngineProperties;
import com.wx.fbsir.engine.websocket.handler.MessageRouter;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * WebSocket 客户端管理器
 * 
 * 负责 WebSocket 客户端的生命周期管理
 * 解决老项目的资源管理问题
 *
 * @author wxfbsir
 * @date 2025-12-15
 */
@Component
public class WebSocketClientManager {

    private static final Logger log = LoggerFactory.getLogger(WebSocketClientManager.class);

    private final EngineProperties properties;
    private final ThreadPoolTaskScheduler scheduler;
    private final MessageRouter messageRouter;

    /**
     * 能力管理器（延迟注入避免循环依赖）
     */
    @Autowired
    @Lazy
    private EngineCapabilityManager capabilityManager;

    private EngineWebSocketClient client;
    private volatile boolean initialized = false;

    public WebSocketClientManager(EngineProperties properties,
                                   @Qualifier("taskScheduler") ThreadPoolTaskScheduler scheduler,
                                   MessageRouter messageRouter) {
        this.properties = properties;
        this.scheduler = scheduler;
        this.messageRouter = messageRouter;
    }

    /**
     * 应用启动完成后自动连接主节点
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("[ClientManager] 应用启动完成，准备连接主节点");
        
        // 注入 WebSocketClientManager 到 CapabilityManager（解决循环依赖）
        if (capabilityManager != null) {
            capabilityManager.setWebSocketClientManager(this);
        }
        
        connect();
    }

    /**
     * 连接主节点
     */
    public synchronized void connect() {
        if (client != null && client.isOpen()) {
            log.warn("[ClientManager] 已存在活跃连接，请先断开");
            return;
        }

        String wsUrl = properties.getAdmin().getWsUrl();
        log.info("[ClientManager] 开始连接主节点 - 地址: {}", wsUrl);

        try {
            URI serverUri = new URI(wsUrl);
            ScheduledExecutorService schedulerService = scheduler.getScheduledExecutor();

            client = new EngineWebSocketClient(
                serverUri,
                properties,
                schedulerService,
                this::handleMessage
            );

            // 设置能力列表（连接时上报给 Admin）
            if (capabilityManager != null) {
                List<Map<String, Object>> capabilities = capabilityManager.getCapabilityList();
                client.setCapabilities(capabilities);
                log.info("[ClientManager] 已加载 {} 个能力", capabilities.size());
            }

            // 设置最大消息大小（支持大文本传输）
            int maxMessageSize = properties.getConnection().getMaxMessageSize();
            client.setConnectionLostTimeout(properties.getConnection().getHeartbeatTimeout());
            
            // 异步连接
            client.connect();
            
            log.info("[ClientManager] 最大消息大小: {}MB", maxMessageSize / 1024 / 1024);
            initialized = true;
            
            log.info("[ClientManager] 连接请求已发送，等待建立连接...");

        } catch (Exception e) {
            log.error("[ClientManager] 连接初始化失败 - 错误类型: {}, 错误信息: {}",
                e.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * 处理接收到的消息
     */
    private void handleMessage(EngineMessage message) {
        try {
            messageRouter.route(message);
        } catch (Exception e) {
            log.error("[ClientManager] 消息处理异常 - 消息类型: {}, 错误: {}",
                message.getType(), e.getMessage());
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息对象
     * @return true 发送成功，false 发送失败
     */
    public boolean sendMessage(EngineMessage message) {
        if (client == null || !client.isOpen()) {
            log.warn("[ClientManager] 连接未建立，无法发送消息");
            return false;
        }

        try {
            client.sendMessage(message);
            return true;
        } catch (Exception e) {
            log.error("[ClientManager] 消息发送失败 - 错误: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 断开连接
     */
    public synchronized void disconnect() {
        if (client != null) {
            client.closeGracefully();
            client = null;
        }
    }

    /**
     * 重新连接
     */
    public void reconnect() {
        log.info("[ClientManager] 重新连接主节点");
        disconnect();
        connect();
    }

    /**
     * 获取连接状态
     */
    public boolean isConnected() {
        return client != null && client.isOpen();
    }

    /**
     * 获取详细连接状态
     */
    public EngineWebSocketClient.ConnectionStatus getConnectionStatus() {
        if (client == null) {
            return new EngineWebSocketClient.ConnectionStatus(
                false, false, 0, 0, 0
            );
        }
        return client.getStatus();
    }

    /**
     * 优雅关闭
     */
    @PreDestroy
    public void shutdown() {
        disconnect();
    }
}
