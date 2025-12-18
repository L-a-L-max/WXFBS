package com.wx.fbsir.engine.websocket.message;

/**
 * WebSocket 消息类型枚举
 * 
 * 替代老项目的字符串魔法值匹配，提高代码可维护性
 *
 * @author wxfbsir
 * @date 2025-12-15
 */
public enum MessageType {

    // ========== 系统消息 ==========
    
    /**
     * 心跳请求
     */
    HEARTBEAT_PING("HEARTBEAT_PING", "心跳请求"),
    
    /**
     * 心跳响应
     */
    HEARTBEAT_PONG("HEARTBEAT_PONG", "心跳响应"),
    
    /**
     * Engine 注册请求
     */
    ENGINE_REGISTER("ENGINE_REGISTER", "Engine 注册"),
    
    /**
     * Engine 注册响应
     */
    ENGINE_REGISTER_ACK("ENGINE_REGISTER_ACK", "Engine 注册确认"),
    
    /**
     * Engine 注销
     */
    ENGINE_UNREGISTER("ENGINE_UNREGISTER", "Engine 注销"),
    
    /**
     * 错误消息
     */
    ERROR("ERROR", "错误消息"),

    // ========== 业务消息 ==========
    
    /**
     * AI 对话请求
     */
    AI_CHAT_REQUEST("AI_CHAT_REQUEST", "AI对话请求"),
    
    /**
     * AI 对话响应
     */
    AI_CHAT_RESPONSE("AI_CHAT_RESPONSE", "AI对话响应"),
    
    /**
     * AI 对话流式响应
     */
    AI_CHAT_STREAM("AI_CHAT_STREAM", "AI对话流式响应"),
    
    /**
     * 任务请求
     */
    TASK_REQUEST("TASK_REQUEST", "任务请求"),
    
    /**
     * 任务进度
     */
    TASK_PROGRESS("TASK_PROGRESS", "任务进度"),
    
    /**
     * 任务结果
     */
    TASK_RESULT("TASK_RESULT", "任务结果"),
    
    /**
     * 管理员断开连接
     */
    ADMIN_DISCONNECT("ADMIN_DISCONNECT", "管理员断开连接"),
    
    /**
     * 未知类型
     */
    UNKNOWN("UNKNOWN", "未知类型");

    private final String code;
    private final String description;

    MessageType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据 code 获取消息类型
     *
     * @param code 消息类型代码
     * @return 消息类型枚举，未找到返回 UNKNOWN
     */
    public static MessageType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return UNKNOWN;
        }
        for (MessageType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
