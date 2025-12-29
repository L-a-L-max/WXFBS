package com.wx.fbsir.business.websocket.server;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wx.fbsir.business.websocket.message.EngineMessage;
import com.wx.fbsir.business.websocket.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Engine 消息路由器（主节点）
 * 
 * 职责：
 *   1. 处理 Engine 发来的系统消息（注册、心跳等）
 *   2. 将 Engine 的业务响应转发给前端
 * 
 * 消息流向：
 *   Engine → Admin(EngineMessageRouter) → 前端(ClientMessageRouter)
 *
 * @author wxfbsir
 * @date 2025-12-15
 */
@Component
public class EngineMessageRouter {

    private static final Logger log = LoggerFactory.getLogger(EngineMessageRouter.class);

    private final Map<MessageType, MessageHandler> handlers = new ConcurrentHashMap<>();
    private final ClientMessageRouter clientMessageRouter;
    private final com.wx.fbsir.business.websocket.controller.EngineRequestController engineRequestController;

    public EngineMessageRouter(ClientMessageRouter clientMessageRouter,
                                com.wx.fbsir.business.websocket.controller.EngineRequestController engineRequestController) {
        this.clientMessageRouter = clientMessageRouter;
        this.engineRequestController = engineRequestController;
    }

    @PostConstruct
    public void init() {
        log.info("[消息路由] 初始化完成 - Engine 响应将自动转发给前端");
    }

    /**
     * 注册消息处理器
     *
     * @param type    消息类型
     * @param handler 处理器
     */
    public void registerHandler(MessageType type, MessageHandler handler) {
        handlers.put(type, handler);
        log.debug("[消息路由] 注册处理器 - 类型: {}, 处理器: {}", 
            type.getCode(), handler.getClass().getSimpleName());
    }

    /**
     * 路由 Engine 消息（完整转发payload）
     * 
     * 所有 Engine 发来的业务响应都会转发给对应的前端用户
     * Admin不对payload做任何处理，完整透传
     *
     * @param session 会话对象
     * @param message 消息对象
     */
    public void route(EngineSession session, EngineMessage message) {
        if (message == null) {
            log.warn("[消息路由] 收到空消息，已忽略");
            return;
        }

        String type = message.getType();
        String userId = message.getUserId();
        
        // 🔴 关键修复：根据请求来源区分响应目标
        // 提取 requestId 和 sourceType
        String requestId = message.getPayloadValue("requestId");
        String sourceType = message.getPayloadValue("sourceType");
        
        log.debug("[Router] 收到Engine响应: {} - 类型: {}, 用户: {}, 请求ID: {}", 
            session.getEngineId(), type, userId, requestId);
        
        // 检查是否是单次返回结果（_RESULT后缀）
        boolean isResultMessage = type != null && type.endsWith("_RESULT");
        if (isResultMessage && requestId != null && !requestId.isEmpty()) {
            // 根据来源类型路由
            if ("HTTP".equals(sourceType)) {
                // HTTP 请求 → 仅完成 HTTP 响应
                java.util.Map<String, Object> resultData = new java.util.HashMap<>();
                if (message.getPayload() != null) {
                    resultData.putAll(message.getPayload());
                }
                engineRequestController.completeRequest(requestId, resultData);
                log.debug("[Router] HTTP响应完成 - 请求ID: {}, 类型: {}", requestId, type);
                return; // 不转发给 WebSocket
                
            } else if ("WEBSOCKET".equals(sourceType)) {
                // WebSocket 请求 → 仅转发给 WebSocket 客户端（完整转发payload）
                if (userId != null && !userId.isEmpty()) {
                    String jsonMessage = message.toJson();
                    clientMessageRouter.routeToClient(userId, jsonMessage);
                    log.debug("[Router] WebSocket响应已转发 - 请求ID: {}, 类型: {}", requestId, type);
                    return;
                }
            } else {
                // 未知来源或旧版本消息，兼容处理（双路转发）
                log.warn("[Router] 未知来源类型: {}, 请求ID: {}, 执行兼容路由", sourceType, requestId);
                
                // 尝试完成 HTTP 请求
                java.util.Map<String, Object> resultData = new java.util.HashMap<>();
                if (message.getPayload() != null) {
                    resultData.putAll(message.getPayload());
                }
                engineRequestController.completeRequest(requestId, resultData);
                
                // 尝试转发给 WebSocket
                if (userId != null && !userId.isEmpty()) {
                    String jsonMessage = message.toJson();
                    clientMessageRouter.routeToClient(userId, jsonMessage);
                }
                return;
            }
        }
        
        // 非 _RESULT 消息（进度消息等），正常转发给客户端
        if (userId != null && !userId.isEmpty()) {
            String jsonMessage = message.toJson();
            clientMessageRouter.routeToClient(userId, jsonMessage);
            log.debug("[Router] 转发进度消息: {} - 用户: {}", type, userId);
            return;
        }
        
        // 检查是否有注册的 Handler
        MessageType messageType = message.getMessageType();
        MessageHandler handler = handlers.get(messageType);
        if (handler != null) {
            try {
                handler.handle(session, message);
            } catch (Exception e) {
                log.error("[消息路由] 处理异常 - 类型: {}, 错误: {}", type, e.getMessage());
            }
            return;
        }
        
        log.warn("[消息路由] 未知消息类型且无 userId - 类型: {}, EngineID: {}", 
            type, session.getEngineId());
    }

    /**
     * 直接转发原始消息到前端
     * 
     * 用于 Engine 直接发送的响应消息
     */
    public void forwardToClient(String userId, String rawMessage) {
        if (userId != null && !userId.isEmpty()) {
            clientMessageRouter.routeToClient(userId, rawMessage);
        }
    }

    /**
     * 消息处理器接口
     */
    public interface MessageHandler {
        void handle(EngineSession session, EngineMessage message);
    }
}
