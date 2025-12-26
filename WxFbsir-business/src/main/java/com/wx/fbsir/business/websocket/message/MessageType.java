package com.wx.fbsir.business.websocket.message;

/**
 * WebSocket 消息类型枚举
 * 
 * 说明：由Engine处理的业务功能，需在CapabilityRegistry注册
 */
public enum MessageType {

    // ==========================================================================
    // 业务消息（Business Messages）
    // ==========================================================================
    
    /**
     * Playwright自动化测试（流式输出）
     * 
     * Controller: PlaywrightTestController
     * 注册方式: stream() - 支持多次进度返回
     * 
     * 请求示例：
     * {"type":"PLAYWRIGHT_TEST","engineId":"engine-001","payload":{"requestId":"xxx"}}
     * 
     * 响应消息：
     * - TASK_PROGRESS: 进度通知（多次）
     * - TASK_RESULT: 最终结果（一次）
     */
    PLAYWRIGHT_TEST("PLAYWRIGHT_TEST", "Playwright测试"),
    
    /**
     * 健康检查
     * 
     * 请求消息：
     * - HEALTH_CHECK: 健康检查请求
     * 
     * 响应消息：
     * - HEALTH_CHECK_RESULT: 健康数据
     */
    HEALTH_CHECK("HEALTH_CHECK", "健康检查"),
    
    /** 简单流式示例 | 新手学习参考 */
    SIMPLE_STREAM_DEMO("SIMPLE_STREAM_DEMO", "简单流式示例"),
    
    /** DeepSeek登录检测 | once() | 请求: {"type":"DEEPSEEK_CHECK_LOGIN","engineId":"engine-001"} */
    DEEPSEEK_CHECK_LOGIN("DEEPSEEK_CHECK_LOGIN", "DeepSeek登录检测"),
    
    /** DeepSeek扫码登录 | stream() | 请求: {"type":"DEEPSEEK_SCAN_LOGIN","engineId":"engine-001"} */
    DEEPSEEK_SCAN_LOGIN("DEEPSEEK_SCAN_LOGIN", "DeepSeek扫码登录"),
    
    /** DeepSeek AI咨询 | stream() | 请求: {"type":"DEEPSEEK_QUERY","engineId":"engine-001","payload":{"query":"你好"}} */
    DEEPSEEK_AI_CONSULT("DEEPSEEK_AI_CONSULT", "DeepSeek AI咨询"),
    
    // ---------- 通用响应消息 ----------
    
    /** 任务日志 | Engine主动发送 | 执行状态文本消息 | 前端显示在 progressLogs 中 */
    TASK_LOG("TASK_LOG", "任务日志"),
    
    /** 任务截图 | Engine主动发送 | 截图URL消息 | 前端显示在 screenshots 轮播区 */
    TASK_SCREENSHOT("TASK_SCREENSHOT", "任务截图"),
    
    /** 任务进度通知 | Engine主动发送 | 流式输出中间进度 | 已废弃，请使用 TASK_LOG */
    @Deprecated
    TASK_PROGRESS("TASK_PROGRESS", "任务进度"),
    
    /** 任务结果 | Engine主动发送 | 流式输出最终结果 */
    TASK_RESULT("TASK_RESULT", "任务结果"),
    
    // ==========================================================================
    // 系统消息（System Messages）
    // 说明：框架内部使用，由EngineWebSocketHandler处理
    // ==========================================================================
    
    /**
     * 心跳请求（Engine → Admin）
     */
    HEARTBEAT_PING("HEARTBEAT_PING", "心跳请求"),
    
    /**
     * 心跳响应（Admin → Engine）
     */
    HEARTBEAT_PONG("HEARTBEAT_PONG", "心跳响应"),
    
    /**
     * Engine注册请求（Engine → Admin）
     */
    ENGINE_REGISTER("ENGINE_REGISTER", "Engine注册"),
    
    /**
     * Engine注册确认（Admin → Engine）
     */
    ENGINE_REGISTER_ACK("ENGINE_REGISTER_ACK", "Engine注册确认"),
    
    /**
     * Engine注销（Engine → Admin）
     */
    ENGINE_UNREGISTER("ENGINE_UNREGISTER", "Engine注销"),
    
    /**
     * 管理员断开连接（Admin → Engine）
     */
    ADMIN_DISCONNECT("ADMIN_DISCONNECT", "管理员断开"),
    
    /**
     * 错误消息（双向）
     */
    ERROR("ERROR", "错误消息"),
    
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
