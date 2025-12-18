package com.wx.fbsir.engine.capability;

import com.wx.fbsir.engine.websocket.client.WebSocketClientManager;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import com.wx.fbsir.engine.websocket.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.*;

/**
 * 消息处理管理器
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 核心职责（参考 cube-engine 设计）
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 调用链路（简化版）：
 *   WebSocket → EngineCapabilityManager → CapabilityRegistry → Controller → Utils
 * 
 * ⚠️ 重要：只支持精准匹配消息类型，避免误调用
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *
 * @author wxfbsir
 * @date 2025-12-18
 */
@Component
public class EngineCapabilityManager {

    private static final Logger log = LoggerFactory.getLogger(EngineCapabilityManager.class);

    private final CapabilityRegistry registry;
    private final ThreadPoolTaskExecutor taskExecutor;

    private WebSocketClientManager webSocketClientManager;

    public EngineCapabilityManager(CapabilityRegistry registry,
                                    @Qualifier("messageExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.registry = registry;
        this.taskExecutor = taskExecutor;
        log.info("[消息管理] EngineCapabilityManager 初始化完成");
    }

    public void setWebSocketClientManager(WebSocketClientManager manager) {
        this.webSocketClientManager = manager;
    }

    @PostConstruct
    public void init() {
        // CapabilityRegistry 通过 @PostConstruct 自动注册，无需手动调用
        log.info("[消息管理] 初始化完成 - 当前注册能力数: {}", registry.size());
    }

    /**
     * 处理消息请求（只支持精准匹配，避免误调用）
     */
    public void handleMessage(EngineMessage message) {
        String type = message.getType();
        
        // 只支持精准匹配，避免 yb_deepseek 误匹配 deepseek 等问题
        CapabilityRegistry.MessageHandler handler = registry.getHandler(type);
        
        if (handler == null) {
            log.warn("[消息] 无处理器（精准匹配）: {}", type);
            sendNotFoundError(message, type);
            return;
        }

        // 异步执行
        taskExecutor.execute(() -> executeHandler(handler, message));
    }

    private void executeHandler(CapabilityRegistry.MessageHandler handler, EngineMessage message) {
        long startTime = System.currentTimeMillis();
        String type = handler.type();

        try {
            handler.handle(message);
            long costTime = System.currentTimeMillis() - startTime;
            log.debug("[{}] 完成 - 耗时: {}ms", type, costTime);

        } catch (Exception e) {
            log.error("[{}] 异常: {}", type, e.getMessage(), e);
            sendErrorResult(message, type, e.getMessage());
        }
    }

    private void sendNotFoundError(EngineMessage message, String type) {
        if (webSocketClientManager == null || !webSocketClientManager.isConnected()) {
            return;
        }

        EngineMessage response = EngineMessage.builder()
            .type(MessageType.TASK_RESULT.getCode())
            .userId(message.getUserId())
            .payload("requestId", message.getPayloadValue("requestId"))
            .payload("success", false)
            .payload("errorCode", "HANDLER_NOT_FOUND")
            .payload("errorMessage", "当前主机没有 [" + type + "] 消息处理能力，需要更新主机或联系管理员处理")
            .build();

        webSocketClientManager.sendMessage(response);
    }

    private void sendErrorResult(EngineMessage message, String type, String errorMsg) {
        if (webSocketClientManager == null || !webSocketClientManager.isConnected()) {
            return;
        }

        EngineMessage response = EngineMessage.builder()
            .type(MessageType.TASK_RESULT.getCode())
            .userId(message.getUserId())
            .payload("requestId", message.getPayloadValue("requestId"))
            .payload("success", false)
            .payload("errorCode", "EXECUTION_ERROR")
            .payload("errorMessage", "处理 [" + type + "] 时发生错误: " + errorMsg)
            .build();

        webSocketClientManager.sendMessage(response);
    }

    public List<Map<String, Object>> getCapabilityList() {
        return registry.getCapabilityList();
    }

    public boolean hasCapability(String code) {
        return registry.hasHandler(code);
    }

    public int getCapabilityCount() {
        return registry.size();
    }

    /**
     * 处理能力请求（供 MessageRouter 调用）
     */
    public void handleCapabilityRequest(EngineMessage message) {
        handleMessage(message);
    }
}
