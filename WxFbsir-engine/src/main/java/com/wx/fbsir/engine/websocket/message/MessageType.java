package com.wx.fbsir.engine.websocket.message;

/**
 * WebSocket 消息类型枚举
 * 
 * 设计原则：
 * 1. 业务消息在上，系统消息在下
 * 2. 每个消息类型必须有对应的Controller处理
 * 3. 消息类型与CapabilityRegistry注册一致
 * 
 * 添加新消息类型的步骤：
 * 步骤1：在下方业务消息区添加枚举（参考PLAYWRIGHT_TEST示例）
 * 步骤2：在Engine模块创建Controller类（参考PlaywrightTestController）
 * 步骤3：在CapabilityRegistry注册处理器（stream或once）
 * 步骤4：更新测试文档，添加请求/响应示例
 * 
 * 示例：
 * <pre>
 * // 1. 流式输出消息（多次返回）
 * PLAYWRIGHT_TEST("PLAYWRIGHT_TEST", "Playwright测试"),  // 请求
 * TASK_PROGRESS("TASK_PROGRESS", "任务进度"),        // 响应（多次）
 * TASK_RESULT("TASK_RESULT", "任务结果"),            // 响应（最终）
 * 
 * // 2. 单次返回消息
 * HEALTH_CHECK("HEALTH_CHECK", "健康检查"),          // 请求
 * HEALTH_CHECK_RESULT("", ""),                        // 响应（自动生成，无需定义）
 * </pre>
 *
 * @author wxfbsir
 * @date 2025-12-15
 */
public enum MessageType {

    // ==========================================================================
    // 业务消息（Business Messages）
    // 说明：由Engine处理的业务功能，需在CapabilityRegistry注册
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
     * 健康检查（单次返回）
     * 
     * Controller: HealthCheckController
     * 注册方式: once() - 单次返回结果
     * 
     * 请求示例：
     * {"type":"HEALTH_CHECK","engineId":"engine-001","payload":{"requestId":"xxx"}}
     * 
     * 响应消息：
     * - HEALTH_CHECK_RESULT: 健康数据（type后缀自动加_RESULT）
     */
    HEALTH_CHECK("HEALTH_CHECK", "健康检查"),
    
    /**
     * 简单流式示例
     * 
     * 用于演示流式输出的完整流程，新手学习参考
     * 
     * 请求消息：
     * - SIMPLE_STREAM_DEMO: 简单流式示例请求（由Admin转发）
     * 
     * 响应消息：
     * - TASK_PROGRESS: 任务进度（多次）
     * - TASK_RESULT: 任务结果（一次）
     */
    SIMPLE_STREAM_DEMO("SIMPLE_STREAM_DEMO", "简单流式示例"),
    
    // ---------- 通用响应消息 ----------
    
    /**
     * 任务进度通知（由Engine主动发送）
     * 用于流式输出的中间进度反馈
     */
    TASK_PROGRESS("TASK_PROGRESS", "任务进度"),
    
    /**
     * 任务结果（由Engine主动发送）
     * 用于流式输出的最终结果
     */
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
