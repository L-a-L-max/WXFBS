package com.wx.fbsir.engine.controller.yuanqi;

import com.microsoft.playwright.Page;
import com.wx.fbsir.engine.capability.annotation.StreamCapability;
import com.wx.fbsir.engine.capability.base.StreamTaskHelper;
import com.wx.fbsir.engine.playwright.pool.BrowserPoolManager;
import com.wx.fbsir.engine.playwright.session.BrowserSession;
import com.wx.fbsir.engine.utils.yuanqi.YuanQiLoginUtil;
import com.wx.fbsir.engine.utils.yuanqi.YuanQiNodeUtil;
import com.wx.fbsir.engine.utils.yuanqi.YuanQiWorkflowUtil;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 元器（YuanQi）节点操作控制器
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 功能概述
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 1. 编辑大模型节点 - 定位节点、填充参数（模型、Temperature、TopP、MaxTokens）、配置提示词
 * 2. 调试工作流 - 点击调试按钮、填入测试参数、执行调试并返回结果
 * 3. 发布工作流 - 点击发布按钮、确认发布、返回发布状态
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 消息类型
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * - YUANQI_EDIT_NODE: 编辑大模型节点
 * - YUANQI_DEBUG_WORKFLOW: 调试工作流
 * - YUANQI_PUBLISH_WORKFLOW: 发布工作流
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 安全机制
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * - 所有操作前进行参数校验
 * - 敏感信息（如提示词内容）不记录到日志
 * - 操作失败时提供详细错误信息便于排查
 * - 浏览器会话使用后及时销毁释放资源
 * 
 * @author wxfbsir
 * @date 2025-01-10
 */
@Controller
public class YuanQiNodeController extends StreamTaskHelper {

    @Autowired
    private YuanQiLoginUtil loginUtil;
    
    @Autowired
    private YuanQiWorkflowUtil workflowUtil;
    
    @Autowired
    private YuanQiNodeUtil nodeUtil;
    
    @Autowired
    private BrowserPoolManager browserPool;
    
    @Autowired
    @Lazy
    private com.wx.fbsir.engine.websocket.client.WebSocketClientManager webSocketClientManager;
    
    @Autowired
    private com.wx.fbsir.engine.playwright.util.ScreenshotUploadClient uploadClient;

    /**
     * 编辑大模型节点（流式返回）
     * 
     * 请求JSON示例：
     * {
     *   "type": "YUANQI_EDIT_NODE",
     *   "engineId": "engine-001",
     *   "payload": {
     *     "requestId": "uuid",
     *     "spaceName": "福帮手开源",
     *     "agentName": "123",
     *     "workflowName": "分析助手-高优先级-多模型",
     *     "nodeName": "精调大模型",
     *     "config": {
     *       "modelName": "hunyuan-2.0-instruct-20251111",
     *       "temperature": 0.7,
     *       "topP": 0.6,
     *       "maxTokens": 4000,
     *       "prompt": "以【{{article_topic}}】为核心，生成800-1000字公众号营销文章..."
     *     }
     *   }
     * }
     * 
     * 操作流程：
     * 1. 导航到元器首页
     * 2. 导航到工作流编辑页面（复用现有功能）
     * 3. 定位并编辑指定节点
     * 4. 填充配置参数和提示词
     * 5. 保存配置并截图返回
     */
    @StreamCapability(
        type = "YUANQI_EDIT_NODE",
        description = "编辑元器工作流大模型节点",
        timeout = 120000L,
        progressInterval = 3000
    )
    public void handleEditNode(EngineMessage message) {
        String userId = message.getUserId();
        String requestId = message.getPayloadValue("requestId");
        String spaceName = message.getPayloadValue("spaceName");
        String agentName = message.getPayloadValue("agentName");
        String workflowName = message.getPayloadValue("workflowName");
        String nodeName = message.getPayloadValue("nodeName");
        Map<String, Object> config = message.getPayloadValue("config");
        
        log.info("[元器节点编辑] 开始 - 用户: {}, 请求: {}, 空间: {}, 智能体: {}, 工作流: {}, 节点: {}", 
            userId, requestId, spaceName, agentName, workflowName, nodeName);
        
        StreamTask task = startStreamTask(userId, requestId, 3000);
        BrowserSession session = null;
        
        try {
            // 参数校验
            if (spaceName == null || spaceName.isEmpty()) {
                task.sendError("参数错误: spaceName 不能为空");
                return;
            }
            if (agentName == null || agentName.isEmpty()) {
                task.sendError("参数错误: agentName 不能为空");
                return;
            }
            if (workflowName == null || workflowName.isEmpty()) {
                task.sendError("参数错误: workflowName 不能为空");
                return;
            }
            if (nodeName == null || nodeName.isEmpty()) {
                task.sendError("参数错误: nodeName 不能为空");
                return;
            }
            if (config == null || config.isEmpty()) {
                task.sendError("参数错误: config 不能为空");
                return;
            }
            
            task.sendLog("正在打开浏览器...");
            
            // 获取持久化浏览器会话
            session = browserPool.acquirePersistent(userId, "yuanqi", false);
            Page page = session.getOrCreatePage();
            
            // 步骤1：导航到元器首页
            task.sendLog("正在导航到元器首页...");
            if (!loginUtil.navigateToHomePage(page)) {
                task.sendError("导航到元器首页失败，请检查网络连接");
                return;
            }
            
            // 检查登录状态
            String loginStatus = loginUtil.checkLoginStatus(page, false);
            if ("false".equals(loginStatus)) {
                task.sendError("未登录元器，请先使用 YUANQI_SCAN_LOGIN 进行扫码登录");
                return;
            }
            task.sendLog("已登录元器，用户: " + loginStatus);
            
            // 步骤2：导航到工作流编辑页面
            task.sendLog("正在导航到工作流编辑页面...");
            task.sendLog("空间: " + spaceName + ", 智能体: " + agentName + ", 工作流: " + workflowName);
            
            YuanQiWorkflowUtil.WorkflowNavigationResult navResult = 
                workflowUtil.navigateToWorkflowEdit(page, spaceName, agentName, workflowName);
            
            if (!navResult.isSuccess()) {
                task.sendError("导航失败: " + navResult.getMessage());
                String errorScreenshot = captureAndUpload(page, userId, "yuanqi_nav_error_" + System.currentTimeMillis());
                if (errorScreenshot != null) {
                    task.sendScreenshot(errorScreenshot);
                }
                return;
            }
            
            Page workflowPage = navResult.getNewPage();
            task.sendLog("已进入工作流编辑页面");
            
            // 截图当前状态
            String navScreenshot = captureAndUpload(workflowPage, userId, "yuanqi_workflow_" + System.currentTimeMillis());
            if (navScreenshot != null) {
                task.sendScreenshot(navScreenshot);
            }
            
            // 步骤3：编辑节点
            task.sendLog("正在定位节点: " + nodeName);
            YuanQiNodeUtil.NodeEditResult editResult = nodeUtil.editLLMNode(workflowPage, nodeName, config);
            
            if (editResult.isSuccess()) {
                task.sendLog("节点配置已保存");
                
                // 截图编辑结果
                String resultScreenshot = captureAndUpload(workflowPage, userId, "yuanqi_edit_result_" + System.currentTimeMillis());
                if (resultScreenshot != null) {
                    task.sendScreenshot(resultScreenshot);
                }
                
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("message", "节点配置已保存");
                resultData.put("nodeName", nodeName);
                resultData.put("workflowName", workflowName);
                resultData.put("screenshotUrl", resultScreenshot);
                
                task.sendSuccess("编辑成功", resultData);
                log.info("[元器节点编辑] 成功 - 用户: {}, 节点: {}", userId, nodeName);
            } else {
                task.sendError("节点编辑失败: " + editResult.getMessage());
                
                // 截图错误状态
                String errorScreenshot = captureAndUpload(workflowPage, userId, "yuanqi_edit_error_" + System.currentTimeMillis());
                if (errorScreenshot != null) {
                    task.sendScreenshot(errorScreenshot);
                }
                
                log.warn("[元器节点编辑] 失败 - 用户: {}, 节点: {}, 原因: {}", userId, nodeName, editResult.getMessage());
            }
            
        } catch (Exception e) {
            log.error("[元器节点编辑] 失败 - 用户: {}, 请求: {}", userId, requestId, e);
            task.sendError("编辑失败: " + e.getMessage());
        } finally {
            task.stop();
            
            if (session != null) {
                try {
                    session.destroy();
                    log.debug("[元器节点编辑] 已销毁会话释放资源 - 用户: {}", userId);
                } catch (Exception e) {
                    log.warn("[元器节点编辑] 销毁会话失败 - 用户: {}, 错误: {}", userId, e.getMessage());
                }
            }
        }
    }
    
    /**
     * 调试工作流（流式返回）
     * 
     * 请求JSON示例：
     * {
     *   "type": "YUANQI_DEBUG_WORKFLOW",
     *   "engineId": "engine-001",
     *   "payload": {
     *     "requestId": "uuid",
     *     "spaceName": "福帮手开源",
     *     "agentName": "123",
     *     "workflowName": "分析助手-高优先级-多模型",
     *     "debugInput": {
     *       "article_topic": "人工智能发展趋势"
     *     }
     *   }
     * }
     * 
     * 操作流程：
     * 1. 导航到工作流编辑页面
     * 2. 点击调试按钮
     * 3. 填入调试输入参数
     * 4. 执行调试并等待结果
     * 5. 截图返回调试结果
     */
    @StreamCapability(
        type = "YUANQI_DEBUG_WORKFLOW",
        description = "调试元器工作流",
        timeout = 180000L,
        progressInterval = 5000
    )
    public void handleDebugWorkflow(EngineMessage message) {
        String userId = message.getUserId();
        String requestId = message.getPayloadValue("requestId");
        String spaceName = message.getPayloadValue("spaceName");
        String agentName = message.getPayloadValue("agentName");
        String workflowName = message.getPayloadValue("workflowName");
        Map<String, Object> debugInput = message.getPayloadValue("debugInput");
        
        log.info("[元器工作流调试] 开始 - 用户: {}, 请求: {}, 空间: {}, 智能体: {}, 工作流: {}", 
            userId, requestId, spaceName, agentName, workflowName);
        
        StreamTask task = startStreamTask(userId, requestId, 5000);
        BrowserSession session = null;
        
        try {
            // 参数校验
            if (spaceName == null || spaceName.isEmpty()) {
                task.sendError("参数错误: spaceName 不能为空");
                return;
            }
            if (agentName == null || agentName.isEmpty()) {
                task.sendError("参数错误: agentName 不能为空");
                return;
            }
            if (workflowName == null || workflowName.isEmpty()) {
                task.sendError("参数错误: workflowName 不能为空");
                return;
            }
            
            task.sendLog("正在打开浏览器...");
            
            // 获取持久化浏览器会话
            session = browserPool.acquirePersistent(userId, "yuanqi", false);
            Page page = session.getOrCreatePage();
            
            // 导航到元器首页
            task.sendLog("正在导航到元器首页...");
            if (!loginUtil.navigateToHomePage(page)) {
                task.sendError("导航到元器首页失败");
                return;
            }
            
            // 检查登录状态
            String loginStatus = loginUtil.checkLoginStatus(page, false);
            if ("false".equals(loginStatus)) {
                task.sendError("未登录元器，请先使用 YUANQI_SCAN_LOGIN 进行扫码登录");
                return;
            }
            task.sendLog("已登录元器，用户: " + loginStatus);
            
            // 导航到工作流编辑页面
            task.sendLog("正在导航到工作流编辑页面...");
            YuanQiWorkflowUtil.WorkflowNavigationResult navResult = 
                workflowUtil.navigateToWorkflowEdit(page, spaceName, agentName, workflowName);
            
            if (!navResult.isSuccess()) {
                task.sendError("导航失败: " + navResult.getMessage());
                return;
            }
            
            Page workflowPage = navResult.getNewPage();
            task.sendLog("已进入工作流编辑页面");
            
            // 执行调试
            task.sendLog("正在执行调试...");
            YuanQiNodeUtil.DebugResult debugResult = nodeUtil.debugWorkflow(workflowPage, debugInput);
            
            // 截图调试结果
            String debugScreenshot = captureAndUpload(workflowPage, userId, "yuanqi_debug_" + System.currentTimeMillis());
            if (debugScreenshot != null) {
                task.sendScreenshot(debugScreenshot);
            }
            
            if (debugResult.isSuccess()) {
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("message", debugResult.getMessage());
                resultData.put("output", debugResult.getOutput());
                resultData.put("workflowName", workflowName);
                resultData.put("screenshotUrl", debugScreenshot);
                
                task.sendSuccess("调试完成", resultData);
                log.info("[元器工作流调试] 成功 - 用户: {}, 工作流: {}", userId, workflowName);
            } else {
                task.sendError("调试失败: " + debugResult.getMessage());
                log.warn("[元器工作流调试] 失败 - 用户: {}, 工作流: {}, 原因: {}", userId, workflowName, debugResult.getMessage());
            }
            
        } catch (Exception e) {
            log.error("[元器工作流调试] 失败 - 用户: {}, 请求: {}", userId, requestId, e);
            task.sendError("调试失败: " + e.getMessage());
        } finally {
            task.stop();
            
            if (session != null) {
                try {
                    session.destroy();
                    log.debug("[元器工作流调试] 已销毁会话释放资源 - 用户: {}", userId);
                } catch (Exception e) {
                    log.warn("[元器工作流调试] 销毁会话失败 - 用户: {}, 错误: {}", userId, e.getMessage());
                }
            }
        }
    }
    
    /**
     * 发布工作流（流式返回）
     * 
     * 请求JSON示例：
     * {
     *   "type": "YUANQI_PUBLISH_WORKFLOW",
     *   "engineId": "engine-001",
     *   "payload": {
     *     "requestId": "uuid",
     *     "spaceName": "福帮手开源",
     *     "agentName": "123",
     *     "workflowName": "分析助手-高优先级-多模型"
     *   }
     * }
     * 
     * 操作流程：
     * 1. 导航到工作流编辑页面
     * 2. 点击发布按钮
     * 3. 确认发布
     * 4. 等待发布完成并截图返回
     */
    @StreamCapability(
        type = "YUANQI_PUBLISH_WORKFLOW",
        description = "发布元器工作流",
        timeout = 60000L,
        progressInterval = 3000
    )
    public void handlePublishWorkflow(EngineMessage message) {
        String userId = message.getUserId();
        String requestId = message.getPayloadValue("requestId");
        String spaceName = message.getPayloadValue("spaceName");
        String agentName = message.getPayloadValue("agentName");
        String workflowName = message.getPayloadValue("workflowName");
        
        log.info("[元器工作流发布] 开始 - 用户: {}, 请求: {}, 空间: {}, 智能体: {}, 工作流: {}", 
            userId, requestId, spaceName, agentName, workflowName);
        
        StreamTask task = startStreamTask(userId, requestId, 3000);
        BrowserSession session = null;
        
        try {
            // 参数校验
            if (spaceName == null || spaceName.isEmpty()) {
                task.sendError("参数错误: spaceName 不能为空");
                return;
            }
            if (agentName == null || agentName.isEmpty()) {
                task.sendError("参数错误: agentName 不能为空");
                return;
            }
            if (workflowName == null || workflowName.isEmpty()) {
                task.sendError("参数错误: workflowName 不能为空");
                return;
            }
            
            task.sendLog("正在打开浏览器...");
            
            // 获取持久化浏览器会话
            session = browserPool.acquirePersistent(userId, "yuanqi", false);
            Page page = session.getOrCreatePage();
            
            // 导航到元器首页
            task.sendLog("正在导航到元器首页...");
            if (!loginUtil.navigateToHomePage(page)) {
                task.sendError("导航到元器首页失败");
                return;
            }
            
            // 检查登录状态
            String loginStatus = loginUtil.checkLoginStatus(page, false);
            if ("false".equals(loginStatus)) {
                task.sendError("未登录元器，请先使用 YUANQI_SCAN_LOGIN 进行扫码登录");
                return;
            }
            task.sendLog("已登录元器，用户: " + loginStatus);
            
            // 导航到工作流编辑页面
            task.sendLog("正在导航到工作流编辑页面...");
            YuanQiWorkflowUtil.WorkflowNavigationResult navResult = 
                workflowUtil.navigateToWorkflowEdit(page, spaceName, agentName, workflowName);
            
            if (!navResult.isSuccess()) {
                task.sendError("导航失败: " + navResult.getMessage());
                return;
            }
            
            Page workflowPage = navResult.getNewPage();
            task.sendLog("已进入工作流编辑页面");
            
            // 执行发布
            task.sendLog("正在发布工作流...");
            YuanQiNodeUtil.PublishResult publishResult = nodeUtil.publishWorkflow(workflowPage);
            
            // 截图发布结果
            String publishScreenshot = captureAndUpload(workflowPage, userId, "yuanqi_publish_" + System.currentTimeMillis());
            if (publishScreenshot != null) {
                task.sendScreenshot(publishScreenshot);
            }
            
            if (publishResult.isSuccess()) {
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("message", publishResult.getMessage());
                resultData.put("workflowName", workflowName);
                resultData.put("screenshotUrl", publishScreenshot);
                
                task.sendSuccess("发布成功", resultData);
                log.info("[元器工作流发布] 成功 - 用户: {}, 工作流: {}", userId, workflowName);
            } else {
                task.sendError("发布失败: " + publishResult.getMessage());
                log.warn("[元器工作流发布] 失败 - 用户: {}, 工作流: {}, 原因: {}", userId, workflowName, publishResult.getMessage());
            }
            
        } catch (Exception e) {
            log.error("[元器工作流发布] 失败 - 用户: {}, 请求: {}", userId, requestId, e);
            task.sendError("发布失败: " + e.getMessage());
        } finally {
            task.stop();
            
            if (session != null) {
                try {
                    session.destroy();
                    log.debug("[元器工作流发布] 已销毁会话释放资源 - 用户: {}", userId);
                } catch (Exception e) {
                    log.warn("[元器工作流发布] 销毁会话失败 - 用户: {}, 错误: {}", userId, e.getMessage());
                }
            }
        }
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
