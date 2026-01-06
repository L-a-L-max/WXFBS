package com.wx.fbsir.engine.controller.yuanqi;

import com.microsoft.playwright.Page;
import com.wx.fbsir.engine.capability.annotation.OnceCapability;
import com.wx.fbsir.engine.capability.base.StreamTaskHelper;
import com.wx.fbsir.engine.playwright.pool.BrowserPoolManager;
import com.wx.fbsir.engine.playwright.session.BrowserSession;
import com.wx.fbsir.engine.utils.yuanqi.YuanQiLoginUtil;
import com.wx.fbsir.engine.utils.yuanqi.YuanQiWorkflowUtil;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import com.wx.fbsir.engine.websocket.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 元器（YuanQi）工作流控制器
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 功能概述
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 1. 工作流导航 - 导航到指定工作流编辑页面
 * 2. 工作流管理 - 工作流相关的操作和管理
 * 3. 智能体管理 - 智能体相关功能
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 消息类型
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * - YUANQI_NAVIGATE_WORKFLOW: 导航到工作流编辑页面
 * 
 * @author wxfbsir
 * @date 2025-01-06
 */
@Controller
public class YuanQiWorkflowController extends StreamTaskHelper {

    @Autowired
    private YuanQiLoginUtil loginUtil;
    
    @Autowired
    private YuanQiWorkflowUtil workflowUtil;
    
    @Autowired
    private BrowserPoolManager browserPool;
    
    @Autowired
    @Lazy
    private com.wx.fbsir.engine.websocket.client.WebSocketClientManager webSocketClientManager;
    
    @Autowired
    private com.wx.fbsir.engine.playwright.util.ScreenshotUploadClient uploadClient;

    /**
     * 导航到工作流编辑页面（单次返回）
     * 
     * 请求JSON示例：
     * {
     *   "type": "YUANQI_NAVIGATE_WORKFLOW",
     *   "engineId": "engine-001",
     *   "payload": {
     *     "spaceName": "福帮手开源",
     *     "agentName": "123",
     *     "workflowName": "分析助手-高优先级-多模型"
     *   }
     * }
     * 
     * 导航流程：
     * 1. 打开元器首页
     * 2. 点击"个人空间"按钮，展开空间/团队选择弹窗
     * 3. 点击指定的空间或团队
     * 4. 点击"我的智能体"或"团队智能体"展开智能体列表
     * 5. 在智能体列表中，点击指定的智能体卡片
     * 6. 点击"工作流管理"标签页
     * 7. 在工作流列表中，找到指定工作流并点击"编辑"按钮
     * 8. 截图新打开的窗口并返回结果
     * 
     * 返回数据：
     * - success: 导航是否成功（boolean）
     * - message: 导航结果信息
     * - screenshotUrl: 工作流编辑页面截图URL
     * - currentUrl: 当前页面URL
     * - spaceName: 空间/团队名称
     * - agentName: 智能体名称
     * - workflowName: 工作流名称
     */
    @OnceCapability(
        type = "YUANQI_NAVIGATE_WORKFLOW",
        description = "导航到元器工作流编辑页面",
        timeout = 60000L
    )
    public void handleNavigateToWorkflow(EngineMessage message) {
        String userId = message.getUserId();
        String requestId = message.getPayloadValue("requestId");
        String spaceName = message.getPayloadValue("spaceName");
        String agentName = message.getPayloadValue("agentName");
        String workflowName = message.getPayloadValue("workflowName");
        
        log.info("[元器工作流导航] 开始 - 用户: {}, 请求: {}, 空间: {}, 智能体: {}, 工作流: {}", 
            userId, requestId, spaceName, agentName, workflowName);
        
        BrowserSession session = null;
        
        try {
            // 参数校验
            if (spaceName == null || spaceName.isEmpty()) {
                sendErrorResult(userId, requestId, "参数错误: spaceName 不能为空");
                return;
            }
            if (agentName == null || agentName.isEmpty()) {
                sendErrorResult(userId, requestId, "参数错误: agentName 不能为空");
                return;
            }
            if (workflowName == null || workflowName.isEmpty()) {
                sendErrorResult(userId, requestId, "参数错误: workflowName 不能为空");
                return;
            }
            
            // 获取持久化浏览器会话
            session = browserPool.acquirePersistent(userId, "yuanqi", false);
            Page page = session.getOrCreatePage();
            
            // 先导航到元器首页
            log.debug("[元器工作流导航] 导航到元器首页");
            if (!loginUtil.navigateToHomePage(page)) {
                sendErrorResult(userId, requestId, "导航到元器首页失败");
                return;
            }
            
            // 执行工作流导航
            YuanQiWorkflowUtil.WorkflowNavigationResult navigationResult = 
                workflowUtil.navigateToWorkflowEdit(page, spaceName, agentName, workflowName);
            
            boolean success = navigationResult.isSuccess();
            String resultMessage = navigationResult.getMessage();
            
            // 截图新打开的页面（如果导航成功）
            String screenshotUrl = null;
            String currentUrl = null;
            
            if (success && navigationResult.getNewPage() != null) {
                Page newPage = navigationResult.getNewPage();
                screenshotUrl = captureAndUpload(newPage, userId, "yuanqi_workflow_" + System.currentTimeMillis());
                currentUrl = newPage.url();
            } else {
                // 如果失败，截图当前页面
                screenshotUrl = captureAndUpload(page, userId, "yuanqi_workflow_error_" + System.currentTimeMillis());
                currentUrl = page.url();
            }
            
            // 构建返回结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("success", success);
            resultData.put("message", resultMessage);
            resultData.put("screenshotUrl", screenshotUrl);
            resultData.put("currentUrl", currentUrl);
            resultData.put("spaceName", spaceName);
            resultData.put("agentName", agentName);
            resultData.put("workflowName", workflowName);
            resultData.put("timestamp", System.currentTimeMillis());
            
            if (success) {
                sendResult(userId, requestId, resultData);
                log.info("[元器工作流导航] 成功 - 用户: {}, 请求: {}, URL: {}", userId, requestId, currentUrl);
            } else {
                sendErrorResult(userId, requestId, resultMessage);
                log.warn("[元器工作流导航] 失败 - 用户: {}, 请求: {}, 原因: {}", userId, requestId, resultMessage);
            }
            
        } catch (Exception e) {
            log.error("[元器工作流导航] 失败 - 用户: {}, 请求: {}", userId, requestId, e);
            sendErrorResult(userId, requestId, "导航失败: " + e.getMessage());
        } finally {
            if (session != null) {
                try {
                    session.destroy();
                    log.debug("[元器工作流导航] 已销毁会话释放资源 - 用户: {}", userId);
                } catch (Exception e) {
                    log.warn("[元器工作流导航] 销毁会话失败 - 用户: {}, 错误: {}", userId, e.getMessage());
                }
            }
        }
    }
    
    /**
     * 发送成功结果
     */
    private void sendResult(String userId, String requestId, Map<String, Object> data) {
        EngineMessage result = EngineMessage.builder()
            .type(MessageType.TASK_RESULT.getCode())
            .userId(userId)
            .payload("requestId", requestId)
            .payload("success", true)
            .payload("data", data)
            .payload("timestamp", System.currentTimeMillis())
            .build();
        
        webSocketClientManager.sendMessage(result);
        log.debug("[元器工作流] 发送结果 - 用户: {}, 请求: {}", userId, requestId);
    }
    
    /**
     * 发送错误结果
     */
    private void sendErrorResult(String userId, String requestId, String errorMessage) {
        EngineMessage result = EngineMessage.builder()
            .type(MessageType.TASK_RESULT.getCode())
            .userId(userId)
            .payload("requestId", requestId)
            .payload("success", false)
            .payload("errorCode", "TASK_ERROR")
            .payload("errorMessage", errorMessage)
            .payload("timestamp", System.currentTimeMillis())
            .build();
        
        webSocketClientManager.sendMessage(result);
        log.error("[元器工作流] 发送错误 - 用户: {}, 请求: {}, 错误: {}", userId, requestId, errorMessage);
    }
    
    /**
     * 截图并上传到 Admin 服务器
     */
    private String captureAndUpload(Page page, String userId, String fileName) {
        try {
            // 截图获取字节数组
            byte[] screenshotBytes = page.screenshot();
            
            // 上传到 Admin 服务器
            com.wx.fbsir.engine.playwright.util.ScreenshotUploadClient.UploadResult result = 
                uploadClient.uploadScreenshot(userId, fileName, screenshotBytes);
            
            if (result.isSuccess()) {
                String uploadedUrl = result.getUrl();
                log.info("[元器截图] 上传成功 - URL: {}", uploadedUrl);
                return uploadedUrl;
            } else {
                log.error("[元器截图] 上传失败 - 错误: {}", result.getErrorMessage());
                return null;
            }
        } catch (Exception e) {
            log.error("[元器截图] 截图失败 - 错误: {}", e.getMessage(), e);
            return null;
        }
    }
}
