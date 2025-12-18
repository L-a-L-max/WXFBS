package com.wx.fbsir.business.websocket.server;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Client 消息路由器
 * 
 * 负责在前端和 Engine 之间路由消息
 * 
 * 消息流向：
 *   前端 → Admin → Engine（请求）
 *   Engine → Admin → 前端（响应，可能多次）
 *
 * @author wxfbsir
 * @date 2025-12-18
 */
@Component
public class ClientMessageRouter {

    private static final Logger log = LoggerFactory.getLogger(ClientMessageRouter.class);

    private final ClientSessionManager clientSessionManager;
    private final EngineSessionManager engineSessionManager;

    public ClientMessageRouter(ClientSessionManager clientSessionManager,
                                EngineSessionManager engineSessionManager) {
        this.clientSessionManager = clientSessionManager;
        this.engineSessionManager = engineSessionManager;
    }

    /**
     * 路由消息到 Engine
     * 
     * ⚠️ 必须在请求中指定 engineId，不支持自动选择
     */
    public void routeToEngine(String clientId, String message) {
        try {
            JSONObject json = JSON.parseObject(message);
            String type = json.getString("type");
            String userId = extractUserId(clientId);
            
            // ━━━━━━━━━━ 获取 engineId（必须指定）━━━━━━━━━━
            String engineId = json.getString("engineId");
            
            // 检查是否指定了 engineId
            if (engineId == null || engineId.isEmpty()) {
                sendError(clientId, type, "ENGINE_NOT_SPECIFIED", "必须指定 engineId 参数");
                log.warn("[Router] 未指定 engineId - 用户: {}, 类型: {}", userId, type);
                return;
            }
            
            // 检查 Engine 是否在线
            if (!engineSessionManager.isEngineOnline(engineId)) {
                sendError(clientId, type, "ENGINE_OFFLINE", "指定的 Engine [" + engineId + "] 不在线");
                log.warn("[Router] Engine 不在线: {} - 用户: {}, 类型: {}", engineId, userId, type);
                return;
            }
            
            // 检查 Engine 是否具有请求的能力
            EngineSession engineSession = engineSessionManager.getSessionByEngineId(engineId);
            if (engineSession != null && !engineSession.hasCapability(type)) {
                sendError(clientId, type, "CAPABILITY_NOT_FOUND", 
                    "Engine [" + engineId + "] 没有 [" + type + "] 能力，请确认后再次尝试");
                log.warn("[Router] Engine 无此能力: {} - Engine: {}, 用户: {}", type, engineId, userId);
                return;
            }
            
            // 确保消息包含必要字段
            json.put("userId", userId);
            json.put("sourceClientId", clientId);
            
            // 转发给 Engine
            engineSessionManager.sendToEngine(engineId, json.toJSONString());
            log.debug("[Router] 转发到 Engine: {} -> {} (用户: {})", type, engineId, userId);
            
        } catch (Exception e) {
            log.error("[Router] 处理客户端消息失败", e);
            sendError(clientId, "ERROR", "PARSE_ERROR", "消息解析失败");
        }
    }

    /**
     * 路由 Engine 响应到前端
     */
    public void routeToClient(String userId, String message) {
        try {
            JSONObject json = JSON.parseObject(message);
            String type = json.getString("type");
            
            // 根据消息类型前缀决定发送目标
            if (type != null) {
                if (type.contains("PC_") || type.startsWith("RETURN_PC_")) {
                    // PC 专用消息
                    clientSessionManager.sendToClient("web-" + userId, message);
                    clientSessionManager.sendToClient("mypc-" + userId, message);
                } else if (type.contains("MINI_")) {
                    // 小程序专用消息
                    clientSessionManager.sendToClient("mini-" + userId, message);
                } else {
                    // 通用消息，发送给所有端
                    clientSessionManager.sendToUser(userId, message);
                }
            } else {
                // 无类型，发送给所有端
                clientSessionManager.sendToUser(userId, message);
            }
            
            log.debug("[Router] 转发到用户: {} -> {}", type, userId);
            
        } catch (Exception e) {
            log.error("[Router] 处理 Engine 响应失败", e);
        }
    }

    /**
     * 发送错误消息给客户端
     */
    private void sendError(String clientId, String type, String errorCode, String errorMessage) {
        JSONObject error = new JSONObject();
        error.put("type", type != null ? type + "_ERROR" : "ERROR");
        error.put("success", false);
        error.put("errorCode", errorCode);
        error.put("errorMessage", errorMessage);
        error.put("timestamp", System.currentTimeMillis());
        clientSessionManager.sendToClient(clientId, error.toJSONString());
    }

    /**
     * 从 clientId 提取 userId
     */
    private String extractUserId(String clientId) {
        if (clientId.startsWith("web-")) {
            return clientId.substring(4);
        } else if (clientId.startsWith("mypc-")) {
            return clientId.substring(5);
        } else if (clientId.startsWith("mini-")) {
            return clientId.substring(5);
        }
        return clientId;
    }
}
