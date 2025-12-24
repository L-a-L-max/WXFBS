package com.wx.fbsir.engine.controller;

import com.wx.fbsir.engine.capability.annotation.StreamCapability;
import com.wx.fbsir.engine.controller.base.BaseStreamController;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单流式输出示例Controller
 * 
 * 功能：演示如何使用BaseStreamController实现完整的流式任务
 * 
 * 流式输出 = 任务执行过程中多次返回进度信息
 * - 自动处理requestId传递
 * - 统一的进度发送方法
 * - 自动使用TASK_PROGRESS和TASK_RESULT
 * - 防止资源泄漏
 * - 全局异常处理
 *
 * @author wxfbsir
 * @date 2025-12-23
 */
@Controller
public class SimpleStreamDemoController extends BaseStreamController {

    /**
     * 处理简单流式任务（流式返回）
     * 
     * 演示流式输出的完整流程：
     * 1. 提取requestId（由Admin生成）
     * 2. 创建StreamTask（可配置推送间隔）
     * 3. 多次发送进度（自动使用TASK_PROGRESS）
     * 4. 发送最终结果（自动使用TASK_RESULT）
     * 5. 确保资源清理
     * 
     * @param message 消息对象（包含userId和requestId）
     */
    @StreamCapability(
        type = "SIMPLE_STREAM_DEMO",
        description = "简单流式输出示例",
        progressInterval = 3000
    )
    public void handleSimpleStreamDemo(EngineMessage message) {
        // ━━━━━━━━━━ 步骤1: 提取参数 ━━━━━━━━━━
        // requestId由Admin强制生成，全链路唯一
        String userId = message.getUserId();
        String requestId = extractRequestId(message);
        
        log.info("[简单流式示例] 任务开始 - 用户: {}, 请求ID: {}", userId, requestId);
        
        // ━━━━━━━━━━ 步骤2: 创建StreamTask ━━━━━━━━━━
        // 参数说明：
        // - userId: 用户ID
        // - requestId: 请求ID（Admin生成）
        // - 3000: 推送间隔（毫秒），这里设置为3秒
        //         可以改成5000（5秒）或10000（10秒）
        StreamTask task = startStreamTask(userId, requestId, 3000);
        
        try {
            // ━━━━━━━━━━ 步骤3: 执行业务逻辑并发送进度 ━━━━━━━━━━
            
            // 💡 发送进度的3种方式：
            
            // 方式1: 简单文本进度（最常用）
            task.sendProgress("开始初始化...");
            Thread.sleep(2000); // 模拟耗时操作
            
            // 方式2: 带百分比的进度（推荐）
            // 参数：消息文本, 当前步骤, 总步骤数
            task.sendProgress("正在加载数据...", 1, 5);
            Thread.sleep(2000);
            
            task.sendProgress("正在处理数据...", 2, 5);
            Thread.sleep(2000);
            
            task.sendProgress("正在计算结果...", 3, 5);
            Thread.sleep(2000);
            
            task.sendProgress("正在生成报告...", 4, 5);
            Thread.sleep(2000);
            
            // 方式3: 带额外数据的进度（高级用法）
            // 例如：附带截图链接、文件URL等
            Map<String, Object> extraData = new HashMap<>();
            extraData.put("current", 5);
            extraData.put("total", 5);
            extraData.put("processingTime", "10秒");
            extraData.put("memoryUsage", "50MB");
            task.sendProgress("数据处理完成", extraData);
            Thread.sleep(1000);
            
            // ━━━━━━━━━━ 步骤4: 发送最终结果 ━━━━━━━━━━
            // 构建结果数据
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("processedCount", 100);
            resultData.put("successCount", 95);
            resultData.put("failCount", 5);
            resultData.put("totalTime", "12秒");
            resultData.put("resultUrl", "http://example.com/result/123");
            
            // 发送成功结果
            // task.sendSuccess会：
            // 1. 自动使用TASK_RESULT消息类型
            // 2. 自动携带requestId
            // 3. 自动停止StreamTask
            task.sendSuccess("任务执行成功！", resultData);
            
            log.info("[简单流式示例] 任务完成 - 用户: {}, 请求ID: {}", userId, requestId);
            
        } catch (InterruptedException e) {
            // 任务被中断
            Thread.currentThread().interrupt();
            log.error("[简单流式示例] 任务被中断 - 用户: {}, 请求ID: {}", userId, requestId, e);
            task.sendError("任务被中断");
            
        } catch (Exception e) {
            // 其他异常
            log.error("[简单流式示例] 任务失败 - 用户: {}, 请求ID: {}, 错误: {}", 
                userId, requestId, e.getMessage(), e);
            
            // 发送错误结果
            // task.sendError会：
            // 1. 自动使用TASK_RESULT消息类型
            // 2. 设置success=false
            // 3. 自动停止StreamTask
            task.sendError("任务执行失败：" + e.getMessage());
            
        } finally {
            // ━━━━━━━━━━ 步骤5: 确保资源清理 ━━━━━━━━━━
            // 停止StreamTask（防止内存泄漏）
            // 如果已经调用了sendSuccess或sendError，会自动停止，这里是双重保险
            task.stop();
        }
    }
    
    /**
     * 演示自动进度推送（流式返回）
     * 
     * 长时间运行的任务可使用自动进度推送功能
     */
    @StreamCapability(
        type = "LONG_RUNNING_TASK",
        description = "长时间运行任务示例",
        progressInterval = 5000
    )
    public void handleLongRunningTask(EngineMessage message) {
        String userId = message.getUserId();
        String requestId = extractRequestId(message);
        
        StreamTask task = startStreamTask(userId, requestId, 5000);
        
        try {
            // 启动自动进度推送（每5秒推送一次）
            // 参数是一个函数，接收当前计数，返回进度消息
            task.startAutoProgress(count -> "任务进行中，已运行 " + (count * 5) + " 秒...");
            
            // 执行长时间任务
            Thread.sleep(30000); // 模拟30秒的任务
            
            // 发送最终结果（会自动停止自动进度推送）
            task.sendSuccess("长时间任务完成", null);
            
        } catch (Exception e) {
            task.sendError("任务失败: " + e.getMessage());
        } finally {
            task.stop();
        }
    }
}
