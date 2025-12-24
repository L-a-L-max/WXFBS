package com.wx.fbsir.engine.controller;

import com.microsoft.playwright.Page;
import com.wx.fbsir.engine.capability.annotation.StreamCapability;
import com.wx.fbsir.engine.controller.base.BaseStreamController;
import com.wx.fbsir.engine.playwright.util.ScreenshotUploadClient;
import com.wx.fbsir.engine.playwright.core.PlaywrightTaskExecutor;
import com.wx.fbsir.engine.playwright.session.BrowserSession;
import com.wx.fbsir.engine.playwright.util.ScreenshotUtil;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Playwright浏览器自动化测试控制器
 * 
 * 功能：
 * - 打开百度搜索引擎页面
 * - 截图3次，每隔3秒
 * - 获取页面所有文字内容
 * - 返回截图链接和文字内容给前端
 * 
 * 演示BaseStreamController的使用方式：
 * 1. 继承BaseStreamController获得流式能力
 * 2. 使用StreamTask管理任务生命周期
 * 3. 自动处理requestId传递和消息发送
 *
 * @author wxfbsir
 * @date 2025-12-19
 */
@Controller
public class PlaywrightTestController extends BaseStreamController {

    private final PlaywrightTaskExecutor taskExecutor;
    private final ScreenshotUtil screenshotUtil;
    private final ScreenshotUploadClient uploadClient;

    public PlaywrightTestController(PlaywrightTaskExecutor taskExecutor,
                                     ScreenshotUtil screenshotUtil,
                                     ScreenshotUploadClient uploadClient) {
        this.taskExecutor = taskExecutor;
        this.screenshotUtil = screenshotUtil;
        this.uploadClient = uploadClient;
    }

    /**
     * 处理Playwright浏览器自动化测试请求（流式返回）
     * 
     * @param message 消息对象（包含userId和requestId）
     */
    @StreamCapability(
        type = "PLAYWRIGHT_TEST",
        description = "Playwright浏览器自动化测试",
        progressInterval = 3000
    )
    public void handlePlaywrightTest(EngineMessage message) {
        // 1. 提取参数（requestId由Admin强制生成，确保全链路唯一）
        String userId = message.getUserId();
        String requestId = extractRequestId(message);
        
        log.info("[Playwright测试] 开始 - 用户: {}, 请求ID: {}", userId, requestId);
        
        // 2. 创建流式任务（默认5秒推送间隔，可自定义）
        StreamTask task = startStreamTask(userId, requestId, 3000);
        
        try {
            // 3. 使用临时会话执行测试（persist=false, headless=false）
            // 临时会话会在任务完成后自动关闭
            taskExecutor.execute(userId, "baidu-test", false, false, session -> {
                return executeTest(session, task);
            });
            
        } catch (Exception e) {
            log.error("[Playwright测试] 执行失败 - 用户: {}, 错误: {}", userId, e.getMessage(), e);
            task.sendError("测试执行失败: " + e.getMessage());
        } finally {
            // 4. 确保停止定时任务（防止资源泄漏）
            task.stop();
        }
    }

    /**
     * 执行测试逻辑
     * 
     * 使用StreamTask统一管理进度推送：
     * - task.sendProgress(message, current, total) - 发送进度（带百分比）
     * - task.sendProgress(message, extraData) - 发送进度（带额外数据）
     * - task.sendSuccess(message, data) - 发送成功结果
     * - task.sendError(errorMessage) - 发送错误结果
     * 
     * @param session 浏览器会话
     * @param task 流式任务包装对象
     */
    private Void executeTest(BrowserSession session, StreamTask task) {
        List<String> screenshotUrls = new ArrayList<>();
        String pageText = "";
        
        try {
            Page page = session.getOrCreatePage();
            
            // 1. 打开百度
            log.debug("[Playwright测试] 正在打开百度...");
            page.navigate("https://www.baidu.com");
            page.waitForLoadState();
            
            // 发送进度通知（1/4）
            task.sendProgress("已打开百度首页", 1, 4);
            
            // 2. 第一次截图
            log.debug("[Playwright测试] 第1次截图...");
            String screenshot1 = captureAndUpload(page, task.getUserId(), "baidu_test_1");
            if (screenshot1 != null) {
                screenshotUrls.add(screenshot1);
                // 发送进度（附带截图链接）
                Map<String, Object> extraData = new HashMap<>();
                extraData.put("current", 2);
                extraData.put("total", 4);
                extraData.put("screenshotUrl", screenshot1);
                task.sendProgress("第1张截图完成", extraData);
            }
            
            // 等待3秒
            Thread.sleep(3000);
            
            // 3. 第二次截图
            log.debug("[Playwright测试] 第2次截图...");
            String screenshot2 = captureAndUpload(page, task.getUserId(), "baidu_test_2");
            if (screenshot2 != null) {
                screenshotUrls.add(screenshot2);
                // 发送进度（附带截图链接）
                Map<String, Object> extraData = new HashMap<>();
                extraData.put("current", 3);
                extraData.put("total", 4);
                extraData.put("screenshotUrl", screenshot2);
                task.sendProgress("第2张截图完成", extraData);
            }
            
            // 等待3秒
            Thread.sleep(3000);
            
            // 4. 第三次截图
            log.debug("[Playwright测试] 第3次截图...");
            String screenshot3 = captureAndUpload(page, task.getUserId(), "baidu_test_3");
            if (screenshot3 != null) {
                screenshotUrls.add(screenshot3);
            }
            
            // 5. 获取页面所有文字
            log.debug("[Playwright测试] 获取页面文字...");
            pageText = (String) page.evaluate("() => document.body.innerText");
            
            // 6. 发送最终结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("screenshotUrls", screenshotUrls);
            resultData.put("pageText", pageText);
            resultData.put("screenshotCount", screenshotUrls.size());
            
            task.sendSuccess("Playwright测试完成，共截图" + screenshotUrls.size() + "张", resultData);
            
            log.info("[Playwright测试] 完成 - 用户: {}, 截图数: {}", task.getUserId(), screenshotUrls.size());
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[Playwright测试] 测试被中断 - 用户: {}, 请求ID: {}", task.getUserId(), task.getRequestId(), e);
            task.sendError("测试被中断");
        } catch (Exception e) {
            log.error("[Playwright测试] 执行失败 - 用户: {}, 请求ID: {}, 错误: {}", 
                task.getUserId(), task.getRequestId(), e.getMessage(), e);
            // 向用户发送友好的错误消息
            String userMessage = "测试执行失败: " + e.getMessage();
            if (e.getCause() != null) {
                userMessage += " (原因: " + e.getCause().getMessage() + ")";
            }
            task.sendError(userMessage);
        }
        
        return null;
    }

    /**
     * 截图并上传
     * 
     * @param page Playwright页面对象
     * @param userId 用户ID
     * @param fileName 文件名
     * @return 上传成功返回URL，失败返回null
     */
    private String captureAndUpload(Page page, String userId, String fileName) {
        try {
            // 截图获取字节数组
            byte[] screenshotBytes = page.screenshot();
            
            // 上传到Admin服务器
            ScreenshotUploadClient.UploadResult result = uploadClient.uploadScreenshot(userId, fileName, screenshotBytes);
            if (result.isSuccess()) {
                return result.getUrl();
            } else {
                log.error("[Playwright测试] 截图上传失败 - 用户: {}, 文件: {}, 错误: {}", 
                    userId, fileName, result.getErrorMessage());
                return null;
            }
        } catch (Exception e) {
            log.error("[Playwright测试] 截图失败 - 用户: {}, 文件: {}, 错误: {}", 
                userId, fileName, e.getMessage(), e);
            return null;
        }
    }
}
