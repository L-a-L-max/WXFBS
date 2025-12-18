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

    public EngineMessageRouter(ClientMessageRouter clientMessageRouter) {
        this.clientMessageRouter = clientMessageRouter;
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
     * 路由 Engine 消息
     * 
     * 所有 Engine 发来的业务响应都会转发给对应的前端用户
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
        
        // 如果消息包含 userId，转发给前端
        if (userId != null && !userId.isEmpty()) {
            // 将 EngineMessage 转换为 JSON 字符串转发
            String jsonMessage = message.toJson();
            clientMessageRouter.routeToClient(userId, jsonMessage);
            log.debug("[消息路由] 转发到前端 - 类型: {}, 用户: {}", type, userId);
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
