package com.playwright.utils.ai;

import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import com.playwright.entity.UserInfoRequest;
import com.playwright.utils.common.*;
import com.playwright.websocket.WebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.playwright.utils.common.ScreenshotUtil.uploadFile;

/**
 * DeepSeek AI平台工具类
 * @author 优立方
 * @version JDK 17
 * &#064;date  2025年06月15日 10:33
 */
@Component
public class DeepSeekUtil {

    @Autowired
    private LogMsgUtil logInfo;
    
    @Autowired
    private WebSocketClientService webSocketClientService;
    
    @Autowired
    private ClipboardLockManager clipboardLockManager;
    
    @Value("${cube.url}")
    private String url;
    
    @Autowired
    private ScreenshotUtil screenshotUtil;

    /**
     * 检查DeepSeek登录状态
     * 🔥 增强版：修复部分设备上浏览器无法正常调用的问题
     * @param page Playwright页面对象
     * @param navigate 是否需要先导航到DeepSeek页面
     * @return 登录状态，如枟已登录则返回用户名，否则返回"false"
     */
    public String checkLoginStatus(Page page, boolean navigate) {
        if (navigate) {
            try {
                page.navigate("https://chat.deepseek.com/");
                page.waitForLoadState();
                page.waitForTimeout(1500);
            } catch (Exception e) {
                System.err.println("❌ [DeepSeek] 导航失败: " + e.getMessage());
            }
        }

        try {
            // 检测登录按钮，如果存在则未登录
            Locator loginBtn = page.locator("button:has-text('登录'), button:has-text('Login')").first();
            if (loginBtn.count() > 0 && loginBtn.isVisible()) {
                return "false";
            }
        } catch (Exception e) {
            // 登录按钮检测失败，继续其他检测
        }

        try {
            // 直接检测用户信息
            Locator userInfoDiv = page.locator("div._2afd28d");
            if (userInfoDiv.count() > 0) {
                Locator userNameDiv = page.locator("div._2afd28d div._9d8da05");
                if (userNameDiv.count() > 0) {
                    String userName = userNameDiv.textContent();
                    if (userName != null && !userName.trim().isEmpty() && !userName.contains("未登录")) {
                        return userName.trim();
                    }
                }
            }

            // 检测侧边栏区域内的用户信息
            Locator sidebarArea = page.locator("div.ca6d4be1._5a20a69");
            if (sidebarArea.count() > 0) {
                Locator userInfoInSidebar = sidebarArea.locator("div._2afd28d div._9d8da05");
                if (userInfoInSidebar.count() > 0) {
                    String userName = userInfoInSidebar.textContent();
                    if (userName != null && !userName.trim().isEmpty() && !userName.contains("未登录")) {
                        return userName.trim();
                    }
                }
                
                // 尝试点击侧边栏展开按钮
                try {
                    Locator sidebarToggle = sidebarArea.locator("div._4f3769f.ds-icon-button").first();
                    if (sidebarToggle.count() > 0 && sidebarToggle.isVisible()) {
                        sidebarToggle.click();
                        page.waitForTimeout(2000);
                        
                        Locator userInfoAfterExpand = page.locator("div._2afd28d div._9d8da05");
                        if (userInfoAfterExpand.count() > 0) {
                            String userName = userInfoAfterExpand.textContent();
                            if (userName != null && !userName.trim().isEmpty() && !userName.contains("未登录")) {
                                return userName.trim();
                            }
                        }
                    }
                } catch (Exception toggleEx) {
                    // 静默处理侧边栏切换失败
                }
            }

        } catch (Exception e) {
            // 静默处理检测异常
        }

        // 最后尝试：检测页面中是否有用户相关信息
        try {
            Locator chatInterface = page.locator(".chat-interface, .conversation-area, [data-testid='chat-input']");
            if (chatInterface.count() > 0) {
                return "已登录用户";
            }
        } catch (Exception e) {
            // 通用检测失败
        }

        return "false";
    }

    /**
     * 导航到DeepSeek登录页面并等待二维码加载
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @return 是否成功导航并加载二维码
     */
    public boolean navigateToLoginPage(Page page, String userId) {
        try {
            // 🔥 方案1：直接导航到登录页面（主要方案）
            page.navigate("https://chat.deepseek.com/sign_in", new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.NETWORKIDLE)
                .setTimeout(30000));
            
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            logInfo.sendTaskLog("页面基本加载完成，等待二维码加载", userId, "DeepSeek");
            
            // 🔥 等待二维码加载（增加等待时间和检测机制）
            boolean qrCodeLoaded = false;
            for (int i = 0; i < 10; i++) { // 最多等待10秒
                page.waitForTimeout(1000);
                
                // 检测微信登录区域
                Locator wechatLoginBlock = page.locator(".ds-sign-in-with-wechat-block");
                if (wechatLoginBlock.count() > 0) {
                    logInfo.sendTaskLog("检测到微信登录区域", userId, "DeepSeek");
                    qrCodeLoaded = true;
                    break;
                }
                
                // 检测 iframe二维码
                Locator qrIframe = page.locator("iframe[src*='open.weixin.qq.com']");
                if (qrIframe.count() > 0) {
                    logInfo.sendTaskLog("检测到微信二维码iframe", userId, "DeepSeek");
                    qrCodeLoaded = true;
                    break;
                }
                
                logInfo.sendTaskLog("第" + (i + 1) + "次检测二维码，继续等待...", userId, "DeepSeek");
            }
            
            if (!qrCodeLoaded) {
                logInfo.sendTaskLog("二维码加载超时，尝试备用方案", userId, "DeepSeek");
                return tryFallbackNavigation(page, userId);
            } else {
                // 二维码加载成功，额外等待一下确保完全显示
                page.waitForTimeout(2000);
                logInfo.sendTaskLog("二维码加载完成，准备截图", userId, "DeepSeek");
                return true;
            }
            
        } catch (Exception e) {
            logInfo.sendTaskLog("直接导航失败，尝试备用方案: " + e.getMessage(), userId, "DeepSeek");
            return tryFallbackNavigation(page, userId);
        }
    }
    
    /**
     * 备用导航方案：从主页点击登录按钮
     */
    private boolean tryFallbackNavigation(Page page, String userId) {
        try {
            // 先导航到主页
            page.navigate("https://chat.deepseek.com/", new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.NETWORKIDLE)
                .setTimeout(30000));
            
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.waitForTimeout(2000);
            
            // 查找并点击登录按钮
            Locator loginButton = page.locator("button:has-text('登录'), button:has-text('Login')").first();
            if (loginButton.count() > 0 && loginButton.isVisible()) {
                logInfo.sendTaskLog("找到登录按钮，正在点击", userId, "DeepSeek");
                loginButton.click();
                page.waitForTimeout(3000);
                
                // 备用方案也需要等待二维码加载
                logInfo.sendTaskLog("已跳转到登录页面，等待二维码加载", userId, "DeepSeek");
                for (int j = 0; j < 8; j++) {
                    page.waitForTimeout(1000);
                    Locator wechatBlock = page.locator(".ds-sign-in-with-wechat-block");
                    Locator iframe = page.locator("iframe[src*='open.weixin.qq.com']");
                    if (wechatBlock.count() > 0 || iframe.count() > 0) {
                        logInfo.sendTaskLog("备用方案检测到二维码", userId, "DeepSeek");
                        page.waitForTimeout(2000);
                        return true;
                    }
                }
                
                logInfo.sendTaskLog("备用方案二维码加载超时", userId, "DeepSeek");
                return false;
            } else {
                logInfo.sendTaskLog("未找到登录按钮", userId, "DeepSeek");
                return false;
            }
            
        } catch (Exception fallbackException) {
            logInfo.sendTaskLog("备用方案也失败: " + fallbackException.getMessage(), userId, "DeepSeek");
            return false;
        }
    }

    /**
     * 等待并获取DeepSeek登录二维码
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param screenshotUtil 截图工具
     * @return 二维码截图URL
     * @deprecated 使用 navigateToLoginPage + 直接截图 替代
     */
    public String waitAndGetQRCode(Page page, String userId, ScreenshotUtil screenshotUtil) throws Exception {
        logInfo.sendTaskLog("正在获取DeepSeek登录二维码", userId, "DeepSeek");

        // 🔥 【已废弃】这个方法已被 navigateToLoginPage + 直接截图 替代
        // 但为了向后兼容，保留这个方法
        logInfo.sendTaskLog("使用旧版waitAndGetQRCode方法，建议使用navigateToLoginPage", userId, "DeepSeek");
        
        boolean success = navigateToLoginPage(page, userId);
        if (!success) {
            return "false";
        }
        
        // 🔥 最终截图（无论上面哪种情况）
        try {
            String url = screenshotUtil.screenshotAndUpload(page, "checkDeepSeekLogin.png");
            
            if (url != null && !url.trim().isEmpty()) {
                logInfo.sendTaskLog("DeepSeek二维码获取成功，URL: " + url, userId, "DeepSeek");
                System.out.println("📱 [DeepSeek] 二维码截图成功: " + url);
                return url;
            } else {
                logInfo.sendTaskLog("DeepSeek二维码截图失败，返回URL为空", userId, "DeepSeek");
                System.err.println("❌ [DeepSeek] 截图失败，返回URL为空");
                return "false";
            }
        } catch (Exception screenshotError) {
            logInfo.sendTaskLog("DeepSeek二维码截图异常: " + screenshotError.getMessage(), userId, "DeepSeek");
            System.err.println("❌ [DeepSeek] 截图异常: " + screenshotError.getMessage());
            return "false";
        }
    }

    /**
     * 等待DeepSeek AI回答完成并提取内容
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param aiName AI名称
     * @param roles 角色信息，用于判断是否为深度思考模式
     * @return 获取的回答内容
     */
    public String waitDeepSeekResponse(Page page, String userId, String aiName, String roles) {
        try {
            // 等待页面内容稳定
            String currentContent = "";
            String lastContent = "";
            int stableCount = 0;
            int emptyCount = 0;
            int noChangeCount = 0;
            int contentLengthHistory[] = new int[3]; // 记录最近三次内容长度
            boolean hasCompletionMarkers = false; // 是否检测到完成标记
  
            long startTime = System.currentTimeMillis();

            // 添加初始延迟，确保页面完全加载
            page.waitForTimeout(500);
            
            // 判断是否为深度思考或联网模式
            boolean isDeepThinkingMode = roles != null && roles.contains("ds-sdsk");
            boolean isWebSearchMode = roles != null && roles.contains("ds-lwss");
            
            // 根据不同模式设置不同的超时和稳定参数
            long maxTimeout = 300000; // 默认5分钟
            int requiredStableCount = 1; // 默认稳定次数
            int checkInterval = 200; // 默认检查间隔
            
            if (isDeepThinkingMode && isWebSearchMode) {
                maxTimeout = 1800000; // 深度思考+联网模式30分钟 (延长50%: 1200000 -> 1800000)
                requiredStableCount = 2; // 需要更多的稳定确认
                checkInterval = 450; // 增加检查间隔 (延长50%: 300 -> 450)
                logInfo.sendTaskLog("启用深度思考+联网模式监听，等待时间可能较长", userId, aiName);
            } else if (isDeepThinkingMode) {
                maxTimeout = 1350000; // 深度思考模式22.5分钟 (延长50%: 900000 -> 1350000)
                requiredStableCount = 2; // 需要更多的稳定确认
                checkInterval = 375; // 增加检查间隔 (延长50%: 250 -> 375)
                logInfo.sendTaskLog("启用深度思考模式监听，等待时间可能较长", userId, aiName);
            } else if (isWebSearchMode) {
                maxTimeout = 900000; // 联网模式15分钟 (延长50%: 600000 -> 900000)
                requiredStableCount = 2; // 需要更多的稳定确认
                checkInterval = 375; // 增加检查间隔 (延长50%: 250 -> 375)
                logInfo.sendTaskLog("启用联网搜索模式监听", userId, aiName);
            }

            // 等待消息发出后4秒开始检测
            page.waitForTimeout(4000);
            logInfo.sendTaskLog("开始检测DeepSeek回复完成状态", userId, aiName);

            // 添加定期截图变量
            long lastScreenshotTime = System.currentTimeMillis();
            int screenshotInterval = 6000; // 6秒截图一次
            boolean hasEverHadContent = false; // 记录是否曾经有过内容

            // 进入循环，直到内容不再变化或者超时
            while (true) {
                // 🔥 优先检查页面是否关闭
                if (page.isClosed()) {
                    logInfo.sendTaskLog("❌ 页面已关闭，停止监听", userId, aiName);
                    throw new RuntimeException("页面在监控过程中被关闭");
                }
                
                // 检查是否超时
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > maxTimeout) {
                    logInfo.sendTaskLog("超时，AI未完成回答或回答时间过长！", userId, aiName);
                    break;
                }

                // 定期截图（每6秒一次）- 无论什么状态都截图
                if (System.currentTimeMillis() - lastScreenshotTime >= screenshotInterval) {
                    try {
                        screenshotUtil.screenshotAndUpload(page, userId + aiName + "执行过程截图" + ((int)(elapsedTime/1000/6) + 1) + ".png");
                        lastScreenshotTime = System.currentTimeMillis();
                    } catch (Exception e) {
                        // 截图失败不影响主流程，静默处理
                    }
                }

                // 检测和处理刷新按钮
                try {
                    checkAndClickRefreshButton(page, userId, aiName);
                } catch (Exception e) {
                    // 刷新按钮检测失败不影响主流程，静默处理
                }

                // 获取最新AI回答内容 - 使用新的检测逻辑
                Map<String, Object> responseData = getLatestDeepSeekResponseWithCompletion(page);
                currentContent = (String) responseData.getOrDefault("content", "");
                String textContent = (String) responseData.getOrDefault("textContent", "");
                // 🔥 安全地获取 hasActionButtons，避免 NullPointerException
                Object hasActionButtonsObj = responseData.get("hasActionButtons");
                boolean hasActionButtons = hasActionButtonsObj != null ? (Boolean) hasActionButtonsObj : false;
                int contentLength = 0;
                if (responseData.containsKey("length")) {
                    contentLength = ((Number) responseData.get("length")).intValue();
                }

                // 如果成功获取到内容
                if (currentContent != null && !currentContent.trim().isEmpty()) {
                    // 标记曾经有过内容
                    hasEverHadContent = true;
                    // 重置空内容计数
                    emptyCount = 0;
                    
                    // 更新内容长度历史
                    for (int i = contentLengthHistory.length - 1; i > 0; i--) {
                        contentLengthHistory[i] = contentLengthHistory[i-1];
                    }
                    contentLengthHistory[0] = contentLength;
                    
                    // 检查内容是否稳定
                    if (currentContent.equals(lastContent)) {
                        stableCount++;
                        
                        // 检查是否有"正在思考"或类似的提示
                        boolean isThinking = checkIfGenerating(page);
                        
                        // 智能判断完成条件
                        boolean isComplete = false;
                        
                        // 条件1: 如果检测到完成按钮组（最重要的判断条件）
                        if (hasActionButtons) {
                            logInfo.sendTaskLog("检测到完成按钮组，回复已完成", userId, aiName);
                            isComplete = true;
                        }
                        // 条件2: 内容稳定且不再生成
                        else if (stableCount >= requiredStableCount && !isThinking) {
                            // 检查内容长度，如果内容较长，可以更快结束等待
                            if (contentLength > 1000) {
                                // 对于很长的内容，只要稳定就可以提前结束
                                logInfo.sendTaskLog("长内容已稳定，准备提取", userId, aiName);
                                isComplete = true;
                            }
                            else if (contentLength > 500) {
                                noChangeCount++;
                                // 如果长内容连续多次没有变化，可以提前结束
                                if (noChangeCount >= 2) {
                                    logInfo.sendTaskLog("内容稳定，准备提取", userId, aiName);
                                    isComplete = true;
                                }
                            } 
                            // 检查内容增长是否已经停止
                            else if (isContentGrowthStopped(contentLengthHistory) && stableCount >= requiredStableCount) {
                                logInfo.sendTaskLog("内容增长已停止，准备提取", userId, aiName);
                                isComplete = true;
                            }
                            // 对于短内容，需要更多的稳定确认
                            else if (stableCount >= requiredStableCount + 1) {
                                logInfo.sendTaskLog("短内容已稳定，准备提取", userId, aiName);
                                isComplete = true;
                            }
                        }
                        
                        if (isComplete) {
                            logInfo.sendTaskLog("DeepSeek回答完成，正在自动提取内容", userId, aiName);
                            break;
                        }
                    } else {
                        // 内容发生变化，重置稳定计数和无变化计数
                        stableCount = 0;
                        noChangeCount = 0;
                        lastContent = currentContent;
                    }
                } else {
                    // 内容为空，增加空内容计数
                    emptyCount++;
                    
                    // 如果连续多次获取到空内容，检查是否有错误
                    if (emptyCount > 8) {
                        // 检查页面是否有错误提示
                        try {
                            Object errorResult = page.evaluate("""
                                () => {
                                    const errorElements = document.querySelectorAll('.error-message, .ds-error, [class*="error"]');
                                    for (const el of errorElements) {
                                        if (el.innerText && el.innerText.trim() && 
                                            window.getComputedStyle(el).display !== 'none') {
                                            return el.innerText.trim();
                                        }
                                    }
                                    return null;
                                }
                            """);
                            
                            if (errorResult instanceof String && !((String)errorResult).isEmpty()) {
                                logInfo.sendTaskLog("DeepSeek返回错误: " + errorResult, userId, aiName);
                                return "DeepSeek错误: " + errorResult;
                            }
                        } catch (Exception e) {
                            // 页面错误检测失败，静默处理
                        }
                        
                        // 只有在从未有过内容且等待很长时间的情况下才报错
                        if (!hasEverHadContent && emptyCount > 100) { // 约60秒才输出一次
                            logInfo.sendTaskLog("长时间未检测到回复，但继续等待...", userId, aiName);
                            // 不要返回错误，继续等待
                        }
                        
                        // 减少"内容暂时为空"的日志输出频率
                        if (hasEverHadContent && emptyCount == 10) { // 只在刚开始为空时输出一次
                            logInfo.sendTaskLog("内容暂时为空，继续等待...", userId, aiName);
                        }
                    }
                }

                // 根据不同模式使用不同的检查间隔
                page.waitForTimeout(checkInterval);
                
                // 动态调整检查间隔，随着等待时间增加而增加，避免频繁检查
                if (elapsedTime > 30000) { // 30秒后
                    checkInterval = Math.min(800, checkInterval + 50); // 逐渐增加到最多800ms
                }
            }

            // 尝试通过复制按钮获取纯回答内容（过滤思考过程）
            AtomicReference<String> finalContentRef = new AtomicReference<>();
            clipboardLockManager.runWithClipboardLock(()->{
                String finalContent = clickCopyButtonAndGetAnswer(page, userId);
                finalContentRef.set(finalContent);
            });
            String finalContent=finalContentRef.get();
            // 如果复制按钮方法失败，回退到原来的方法
            if (finalContent == null || finalContent.trim().isEmpty()) {
                logInfo.sendTaskLog("复制按钮方法失败，回退到DOM提取方法", userId, aiName);
                finalContent = getLastConversationContent(page, userId);
            }
            
            // 如果最终仍然没有内容，但页面正常，可能是网络问题或正在处理中
            if ((finalContent == null || finalContent.trim().isEmpty()) && !hasEverHadContent) {
                logInfo.sendTaskLog("超时未获取到回复内容，可能是网络问题或账号限制", userId, aiName);
                return "DeepSeek超时未返回内容，请检查网络或账号状态";
            }
            
            // 🔥 检测是否包含AI思考过程内容
            if (finalContent != null && detectThinkingContent(finalContent)) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "内容检测", 
                    "⚠️ 检测到可能包含AI思考过程的内容，建议检查是否为最终答案。" +
                    "\n提示：如果内容以\"让我\"、\"首先\"、\"接下来\"等开头，可能是思考过程而非最终答案。" +
                    "\n💡 解决方案：请重新生成或手动编辑内容。", 
                    url + "/saveLogInfo");
                logInfo.sendTaskLog("⚠️ 内容包含疑似思考过程，请检查", userId, aiName);
            }
            
            logInfo.sendTaskLog("DeepSeek内容已自动提取完成", userId, aiName);
            return finalContent;

        } catch (Exception e) {
            // 异常向上抛出，由AOP统一处理
            throw e;
        }
    }

    /**
     * 检查是否仍在生成内容
     */
    private boolean checkIfGenerating(Page page) {
        try {
            // 使用更可靠的方法检查生成状态
            Object generatingStatus = page.evaluate("""
            () => {
                try {
                    // 检查停止指示器
                    const thinkingIndicators = document.querySelectorAll(
                        '.generating-indicator, .loading-indicator, .thinking-indicator, ' +
                        '.ds-typing-container, .ds-loading-dots, .loading-container, ' +
                        '[class*="loading"], [class*="typing"], [class*="generating"]'
                    );
                    
                    for (const indicator of thinkingIndicators) {
                        if (indicator && 
                            window.getComputedStyle(indicator).display !== 'none' && 
                            window.getComputedStyle(indicator).visibility !== 'hidden') {
                            return true;
                        }
                    }
                    
                    // 检查停止生成按钮
                    const stopButtons = document.querySelectorAll(
                        'button:contains("停止生成"), button:contains("Stop"), ' +
                        '[title="停止生成"], [title="Stop generating"], ' +
                        '.stop-generating-button, [class*="stop"]'
                    );
                    
                    for (const btn of stopButtons) {
                        if (btn && 
                            window.getComputedStyle(btn).display !== 'none' && 
                            window.getComputedStyle(btn).visibility !== 'hidden') {
                            return true;
                        }
                    }
                    
                    // 检查光标闪烁
                    const blinkingElements = document.querySelectorAll(
                        '[class*="cursor"], [class*="blink"]'
                    );
                    
                    for (const el of blinkingElements) {
                        if (el && 
                            window.getComputedStyle(el).display !== 'none' && 
                            window.getComputedStyle(el).visibility !== 'hidden') {
                            // 检查是否在最后一个回复中
                            const responses = document.querySelectorAll('.ds-markdown');
                            if (responses.length > 0) {
                                const lastResponse = responses[responses.length - 1];
                                if (lastResponse.contains(el)) {
                                    return true;
                                }
                            }
                        }
                    }
                    
                    return false;
                } catch (e) {
                    console.error('检查生成状态时出错:', e);
                    return false;
                }
            }
            """);

            return generatingStatus instanceof Boolean ? (Boolean) generatingStatus : false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查内容增长是否已经停止
     * @param contentLengthHistory 内容长度历史记录
     * @return 如果内容增长已停止返回true
     */
    private boolean isContentGrowthStopped(int[] contentLengthHistory) {
        // 检查最近三次内容长度是否相同或几乎相同
        if (contentLengthHistory[0] > 0 && 
            Math.abs(contentLengthHistory[0] - contentLengthHistory[1]) <= 5 && 
            Math.abs(contentLengthHistory[1] - contentLengthHistory[2]) <= 5) {
            return true;
        }
        return false;
    }

    /**
     * 检查页面是否有完成标记
     * @param page Playwright页面对象
     * @return 如果检测到完成标记返回true
     */
    private boolean checkForCompletionMarkers(Page page) {
        try {
            Object result = page.evaluate("""
                () => {
                    try {
                        // 检查是否有完成标记
                        const lastMessage = document.querySelector('.ds-markdown:last-child');
                        if (!lastMessage) return false;
                        
                        // 检查是否有代码块闭合
                        const codeBlocks = lastMessage.querySelectorAll('pre, code');
                        if (codeBlocks.length > 0) {
                            // 检查代码块是否都已闭合
                            const openCodeTags = lastMessage.textContent.match(/```[^`]*/g) || [];
                            // 如果开标签数量为奇数，说明有未闭合的代码块
                            if (openCodeTags.length % 2 !== 0) return false;
                        }
                        
                        // 检查是否有未闭合的括号或引号
                        const text = lastMessage.textContent;
                        const brackets = { '(': ')', '[': ']', '{': '}' };
                        const stack = [];
                        
                        for (let i = 0; i < text.length; i++) {
                            const char = text[i];
                            if (char === '(' || char === '[' || char === '{') {
                                stack.push(char);
                            } else if (char === ')' || char === ']' || char === '}') {
                                const lastOpen = stack.pop();
                                if (brackets[lastOpen] !== char) {
                                    // 括号不匹配，可能是文本中的括号，忽略
                                }
                            }
                        }
                        
                        // 如果栈不为空，说明有未闭合的括号
                        if (stack.length > 0) return false;
                        
                        // 检查是否有常见的结束标记
                        const commonEndMarkers = [
                            /希望这对你有所帮助/,
                            /如果你有任何其他问题/,
                            /如有任何疑问/,
                            /祝你好运/,
                            /希望能够解决你的问题/,
                            /希望对你有帮助/,
                            /Have a great day/,
                            /Hope this helps/,
                            /Let me know if/,
                            /感谢使用/,
                            /Thank you for using/
                        ];
                        
                        for (const marker of commonEndMarkers) {
                            if (marker.test(text)) {
                                return true;
                            }
                        }
                        
                        // 检查是否有完整的句子结束（以句号、问号或感叹号结束）
                        const lastChar = text.trim().slice(-1);
                        if (['.', '。', '!', '！', '?', '？'].includes(lastChar)) {
                            // 检查最近500ms是否有新内容
                            const timestamp = lastMessage.getAttribute('data-timestamp');
                            if (timestamp && (Date.now() - parseInt(timestamp)) > 500) {
                                return true;
                            }
                        }
                        
                        return false;
                    } catch (e) {
                        console.error('检查完成标记时出错:', e);
                        return false;
                    }
                }
            """);
            
            return result instanceof Boolean ? (Boolean) result : false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取AI最新的回答内容，并返回详细信息
     * @param page Playwright页面对象
     * @return 包含内容和元数据的Map
     */
    private Map<String, Object> getLatestAiResponseWithDetails(Page page) {
        try {
            Object jsResult = page.evaluate("""
            () => {
                try {
                    // 获取所有包含AI回答的消息
                    const markdownElements = document.querySelectorAll('.ds-markdown');
                    if (markdownElements.length === 0) {
                        // 尝试其他可能的选择器
                        const alternativeElements = document.querySelectorAll(
                            '.markdown-body, .ai-response, .message-content, [class*="markdown"]'
                        );
                        
                        if (alternativeElements.length > 0) {
                            const latestAlt = alternativeElements[alternativeElements.length - 1];
                            const textContent = latestAlt.textContent || '';
                            return {
                                content: latestAlt.innerHTML,
                                textContent: textContent,
                                length: textContent.trim().length,
                                source: 'alternative-selector',
                                timestamp: Date.now()
                            };
                        }
                        
                        return {
                            content: '',
                            textContent: '',
                            length: 0,
                            source: 'no-markdown-elements',
                            timestamp: Date.now()
                        };
                    }
                    
                    // 获取最新的Markdown内容
                    const latestMarkdown = markdownElements[markdownElements.length - 1];
                    
                    // 为元素添加时间戳以便后续检查
                    if (!latestMarkdown.hasAttribute('data-timestamp')) {
                        latestMarkdown.setAttribute('data-timestamp', Date.now().toString());
                    }
                    
                    // 克隆内容以避免修改原DOM
                    const contentClone = latestMarkdown.cloneNode(true);
                    
                    // 移除头像图标和其他无关元素
                    const iconsToRemove = contentClone.querySelectorAll(
                        '._7eb2358, ._58dfa60, .ds-icon, svg, ' +
                        '.avatar, .user-avatar, .ai-avatar, ' +
                        '.ds-button, button, [role="button"], ' +
                        '[class*="loading"], [class*="typing"], [class*="cursor"]'
                    );
                    iconsToRemove.forEach(icon => icon.remove());
                    
                    // 移除空的div容器
                    const emptyDivs = contentClone.querySelectorAll('div:empty');
                    emptyDivs.forEach(div => div.remove());
                    
                    // 检查内容长度
                    const textContent = contentClone.textContent || '';
                    const contentLength = textContent.trim().length;
                    
                    return {
                        content: contentClone.innerHTML,
                        textContent: textContent,
                        length: contentLength,
                        hasCodeBlocks: contentClone.querySelectorAll('pre, code').length > 0,
                        source: 'latest-markdown',
                        timestamp: Date.now()
                    };
                } catch (e) {
                    return {
                        content: '',
                        textContent: '',
                        length: 0,
                        source: 'error',
                        error: e.toString(),
                        timestamp: Date.now()
                    };
                }
            }
            """);

            if (jsResult instanceof Map) {
                return (Map<String, Object>) jsResult;
            }
        } catch (Exception e) {
            System.err.println("获取AI回答时出错: " + e.getMessage());
        }

        return new HashMap<>();
    }

    /**
     * 获取AI最新的回答内容
     * @param page Playwright页面对象
     * @return 最新的AI回答内容
     */
    private String getLatestAiResponse(Page page) {
        Map<String, Object> responseData = getLatestAiResponseWithDetails(page);
        return (String) responseData.getOrDefault("content", "");
    }


    /**
     * 发送消息到DeepSeek并等待回复
     * @param page Playwright页面实例
     * @param userPrompt 用户提示文本
     * @param userId 用户ID
     * @param roles 角色标识
     * @param chatId 会话ID，如果不为空则使用此会话继续对话
     * @return 处理完成后的结果
     */
    public String handleDeepSeekAI(Page page, String userPrompt, String userId, String roles, String chatId, String aiName) throws InterruptedException {
        try {
            long startProcessTime = System.currentTimeMillis(); // 记录开始处理时间
            
            // 重置刷新按钮点击标志（每次新对话都重新检测）
            hasClickedRefreshButton = false;
            
            // 设置页面错误处理
            page.onPageError(error -> {
            });
            
            // 监听请求失败
            page.onRequestFailed(request -> {
            });
            
            boolean navigationSucceeded = false;
            int retries = 0;
            final int MAX_RETRIES = 2; // 🔥 优化：降低重试次数，加快失败返回（从3降至2）
            
            // 如果有会话ID，则直接导航到该会话
            if (chatId != null && !chatId.isEmpty()) {
                // 这个日志保留，与豆包一致
                
                while (!navigationSucceeded && retries < MAX_RETRIES) {
                    try {
                        // 增加导航选项，提高稳定性
                        page.navigate("https://chat.deepseek.com/a/chat/s/" + chatId, 
                            new Page.NavigateOptions()
                            .setTimeout(10000) // 增加超时时间
                            .setWaitUntil(WaitUntilState.LOAD)); // 使用LOAD而不是DOMCONTENTLOADED，确保页面完全加载
                        
                        // 等待页面稳定 - 使用更可靠的方式
                        try {
                            // 首先等待页面加载完成
                            page.waitForLoadState(LoadState.DOMCONTENTLOADED, new Page.WaitForLoadStateOptions().setTimeout(15000));
                            
                            // 使用JavaScript检查页面是否已准备好，而不是依赖选择器
                            boolean pageReady = false;
                            for (int attempt = 0; attempt < 10 && !pageReady; attempt++) {
                                try {
                                    Object result = page.evaluate("() => { return document.readyState === 'complete' || document.readyState === 'interactive'; }");
                                    if (result instanceof Boolean && (Boolean) result) {
                                        pageReady = true;
                                    } else {
                                        Thread.sleep(500); // 等待500毫秒再次检查
                                    }
                                } catch (Exception evalEx) {
                                    // 忽略评估错误，继续尝试
                                    Thread.sleep(500);
                                }
                            }
                            
                            // 如果页面已准备好，尝试等待网络空闲，但不强制要求
                            if (pageReady) {
                                try {
                                    page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(5000));
                                } catch (Exception networkEx) {
                                    // 忽略网络空闲等待错误
                                }
                            }
                        } catch (Exception e) {
                            // 忽略等待错误，继续执行
                        }
                        
                        navigationSucceeded = true;
                    } catch (Exception e) {
                        retries++;
            
                        if (retries >= MAX_RETRIES) {
                            try {
                                page.navigate("https://chat.deepseek.com/");
                                Thread.sleep(1000); // 给页面充足的加载时间
                            } catch (Exception ex) {
                            }
                        }
                        
                        // 短暂等待后重试
                        Thread.sleep(2000); // 增加等待时间
                    }
                }
            } else {
                try {
                    page.navigate("https://chat.deepseek.com/", 
                        new Page.NavigateOptions()
                        .setTimeout(10000)
                        .setWaitUntil(WaitUntilState.LOAD)); // 使用LOAD而不是DOMCONTENTLOADED
                    
                    // 等待页面稳定 - 使用更可靠的方式
                    try {
                        // 首先等待页面加载完成
                        page.waitForLoadState(LoadState.DOMCONTENTLOADED, new Page.WaitForLoadStateOptions().setTimeout(15000));
                        
                        // 使用JavaScript检查页面是否已准备好，而不是依赖选择器
                        boolean pageReady = false;
                        for (int attempt = 0; attempt < 10 && !pageReady; attempt++) {
                            try {
                                Object result = page.evaluate("() => { return document.readyState === 'complete' || document.readyState === 'interactive'; }");
                                if (result instanceof Boolean && (Boolean) result) {
                                    pageReady = true;
                                } else {
                                    Thread.sleep(500); // 等待500毫秒再次检查
                                }
                            } catch (Exception evalEx) {
                                // 忽略评估错误，继续尝试
                                Thread.sleep(500);
                            }
                        }
                        
                        // 如果页面已准备好，尝试等待网络空闲，但不强制要求
                        if (pageReady) {
                            try {
                                page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(5000));
                            } catch (Exception networkEx) {
                                // 忽略网络空闲等待错误
                            }
                        }
                    } catch (Exception e) {
                        // 忽略等待错误，继续执行
                    }
                } catch (Exception e) {
                }
            }
            
            // 等待页面加载完成
            try {
                // 使用更可靠的等待方式，但缩短超时时间
                Thread.sleep(1000); // 给页面充足的渲染时间
                logInfo.sendTaskLog("DeepSeek页面打开完成", userId, aiName);
            } catch (Exception e) {
            }
            
            // 先处理深度思考和联网搜索按钮的状态
            boolean needDeepThink = roles.contains("ds-sdsk");
            boolean needWebSearch = roles.contains("ds-lwss");
            // 只要有一个没选中就点亮，否则如果都没选则全部关闭
            if (needDeepThink || needWebSearch) {
                if (needDeepThink) {
                    toggleButtonIfNeeded(page, userId, "深度思考", true, logInfo, aiName);
                    // 日志已在toggleButtonIfNeeded方法中发送
                } else {
                    toggleButtonIfNeeded(page, userId, "深度思考", false, logInfo, aiName);
                }
                if (needWebSearch) {
                    toggleButtonIfNeeded(page, userId, "联网搜索", true, logInfo,aiName);
                } else {
                    toggleButtonIfNeeded(page, userId, "联网搜索", false, logInfo,aiName);
                }
            } else {
                // 如果都不需要，全部关闭
                toggleButtonIfNeeded(page, userId, "深度思考", false, logInfo,aiName);
                toggleButtonIfNeeded(page, userId, "联网搜索", false, logInfo,aiName);
            }
            
            // 定位并填充输入框 - 使用新的定位方式
            try {
                Locator inputBox = null;
                boolean inputFound = false;
                
                // 尝试多种输入框定位方式
                String[] inputSelectors = {
                    "textarea[placeholder*='给 DeepSeek 发送消息']",
                    "textarea[placeholder*='Send a message']", 
                    "textarea.ds-scroll-area",
                    "textarea._27c9245",
                    "#chat-input",
                    ".chat-input",
                    "textarea[rows='2']"
                };
                
                // 循环尝试不同的选择器
                for (String selector : inputSelectors) {
                    try {
                        inputBox = page.locator(selector).first();
                        if (inputBox.count() > 0 && inputBox.isVisible()) {
                            inputFound = true;
                            logInfo.sendTaskLog("使用选择器找到输入框: " + selector, userId, aiName);
                            break;
                        }
                    } catch (Exception e) {
                        // 继续尝试下一个选择器
                    }
                }
                
                // 如果还是找不到，使用JavaScript查找
                if (!inputFound) {
                    try {
                        Object jsResult = page.evaluate("""
                            () => {
                                const textareas = document.querySelectorAll('textarea');
                                for (const textarea of textareas) {
                                    if (textarea.placeholder && 
                                        (textarea.placeholder.includes('DeepSeek') || 
                                         textarea.placeholder.includes('发送消息') ||
                                         textarea.placeholder.includes('Send a message'))) {
                                        textarea.setAttribute('data-ai-input', 'true');
                                        return true;
                                    }
                                }
                                return false;
                            }
                        """);
                        
                        if (Boolean.TRUE.equals(jsResult)) {
                            inputBox = page.locator("textarea[data-ai-input='true']").first();
                            if (inputBox.count() > 0 && inputBox.isVisible()) {
                                inputFound = true;
                                logInfo.sendTaskLog("通过JavaScript找到输入框", userId, aiName);
                            }
                        }
                    } catch (Exception e) {
                        // JavaScript方法也失败了
                    }
                }
                
                if (inputFound && inputBox != null) {
                    // 点击输入框获得焦点
                    inputBox.click();
                    Thread.sleep(500); // 等待焦点切换
                    
                    // 清空输入框
                    inputBox.fill("");
                    Thread.sleep(200);
                    
                    // 使用模拟人工输入方式
//                    simulateHumanTyping(page, inputBox, userPrompt, userId);
                    inputBox.fill(userPrompt);
                    logInfo.sendTaskLog("用户指令已自动输入完成", userId, aiName);
                    
                    // 等待发送按钮可用并点击
//                    boolean sendSuccess = clickSendButton(page, userId);
                    int times = 3;
                    String inputText = inputBox.textContent();
                    while (inputText != null && !inputText.isEmpty()) {
                        inputBox.press("Enter");
                        inputText = inputBox.textContent();
                        Thread.sleep(1000);
                        if(times-- < 0) {
                            throw new RuntimeException("指令输入失败");
                        }
                    }
                } else {
                    return "获取内容失败：未找到输入框";
                }
            } catch (Exception e) {
                // 发送消息失败，抛出异常由AOP处理
                throw new RuntimeException("发送消息失败: " + e.getMessage(), e);
            }
            
            // 等待回答完成并获取内容
            logInfo.sendTaskLog("开启自动监听任务，持续监听DeepSeek回答中", userId, aiName);
            String content = waitDeepSeekResponse(page, userId, aiName, roles);
            
            // 返回内容
            return content;
            
        } catch (Exception e) {
            // 异常向上抛出，由AOP统一处理
            throw e;
        }
    }

    /**
     * 模拟人工输入文本
     * @param page Playwright页面
     * @param inputBox 输入框元素
     * @param text 要输入的文本
     * @param userId 用户ID
     */
    private void simulateHumanTyping(Page page, Locator inputBox, String text, String userId) throws InterruptedException {
        try {
            // 先尝试逐字符输入
            for (int i = 0; i < text.length(); i++) {
                String currentChar = String.valueOf(text.charAt(i));
                inputBox.type(currentChar, new Locator.TypeOptions().setDelay(50 + (int)(Math.random() * 100))); // 50-150ms延迟
                
                // 每输入几个字符检查一下是否成功
                if (i % 10 == 0) {
                    Thread.sleep(100);
                    String currentValue = (String) inputBox.evaluate("el => el.value");
                    if (currentValue == null || !currentValue.contains(text.substring(0, Math.min(i + 1, text.length())))) {
                        // 如果检测到输入有问题，重新设置焦点并继续
                        inputBox.click();
                        Thread.sleep(200);
                    }
                }
            }
            
            // 验证输入是否完成
            String finalValue = (String) inputBox.evaluate("el => el.value");
            if (finalValue == null || !finalValue.contains(text.substring(0, Math.min(50, text.length())))) {
                // 模拟输入失败，直接填充
                inputBox.fill(text);
            } else {
                logInfo.sendTaskLog("模拟人工输入成功", userId, "DeepSeek");
            }
            
        } catch (Exception e) {
            // 模拟输入出错，回退到直接填充
            inputBox.fill(text);
        }
    }

    /**
     * 点击发送按钮
     * @param page Playwright页面
     * @param userId 用户ID
     * @return 是否发送成功
     */
    private boolean clickSendButton(Page page, String userId) throws InterruptedException {
        try {
            // 等待发送按钮可用
            boolean buttonReady = false;
            int waitCount = 0;
            final int MAX_WAIT = 50; // 最多等待5秒
            
            while (!buttonReady && waitCount < MAX_WAIT) {
                try {
                    // 检查发送按钮是否可用
                    Object buttonStatus = page.evaluate("""
                        () => {
                            // 查找发送按钮
                            const selectors = [
                                '._7436101',
                                'button[aria-disabled="false"]',
                                '.send-button:not([disabled])',
                                'button:not([aria-disabled="true"]):not([disabled])'
                            ];
                            
                            for (const selector of selectors) {
                                const button = document.querySelector(selector);
                                if (button && 
                                    button.getAttribute('aria-disabled') !== 'true' &&
                                    !button.disabled &&
                                    window.getComputedStyle(button).display !== 'none') {
                                    return { found: true, selector: selector };
                                }
                            }
                            
                            return { found: false };
                        }
                    """);
                    
                    if (buttonStatus instanceof Map) {
                        Map<String, Object> status = (Map<String, Object>) buttonStatus;
                        if (Boolean.TRUE.equals(status.get("found"))) {
                            buttonReady = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    // 继续等待
                }
                
                Thread.sleep(100);
                waitCount++;
            }
            
            if (!buttonReady) {
                logInfo.sendTaskLog("发送按钮等待超时，尝试强制发送", userId, "DeepSeek");
            }
            
            // 尝试点击发送按钮
            boolean clicked = false;
            
            // 方法1: 使用特定选择器
            try {
                Locator sendButton = page.locator("._7436101").first();
                if (sendButton.count() > 0) {
                    // 等待按钮变为可用状态
                    for (int i = 0; i < 10 && !clicked; i++) {
                        try {
                            String ariaDisabled = sendButton.getAttribute("aria-disabled");
                            if (!"true".equals(ariaDisabled)) {
                                sendButton.click(new Locator.ClickOptions().setForce(true).setTimeout(3000));
                                clicked = true;
                                logInfo.sendTaskLog("指令已自动发送成功", userId, "DeepSeek");
                                break;
                            }
                        } catch (Exception e) {
                            // 继续尝试
                        }
                        Thread.sleep(500);
                    }
                }
            } catch (Exception e) {
                // 方法1失败，尝试其他方法
            }
            
            // 方法2: 尝试其他发送按钮选择器
            if (!clicked) {
                String[] sendButtonSelectors = {
                    "button[aria-disabled='false']",
                    "button:not([aria-disabled='true']):not([disabled])",
                    ".send-button:not([disabled])",
                    "button.ds-button--primary:not([disabled])",
                    "[role='button']:not([aria-disabled='true'])"
                };
                
                for (String selector : sendButtonSelectors) {
                    try {
                        Locator button = page.locator(selector).first();
                        if (button.count() > 0 && button.isVisible()) {
                            button.click(new Locator.ClickOptions().setForce(true).setTimeout(3000));
                            clicked = true;
                            logInfo.sendTaskLog("使用备用选择器发送成功: " + selector, userId, "DeepSeek");
                            break;
                        }
                    } catch (Exception e) {
                        // 继续尝试下一个选择器
                    }
                }
            }
            
            // 方法3: 使用JavaScript强制点击
            if (!clicked) {
                try {
                    Object result = page.evaluate("""
                        () => {
                            // 设置消息发送时间戳
                            window._deepseekMessageSentTime = Date.now();
                            
                            // 尝试多种发送方式
                            const selectors = [
                                '._7436101',
                                'button[aria-disabled="false"]',
                                '.send-button:not([disabled])',
                                'button:not([aria-disabled="true"]):not([disabled])'
                            ];
                            
                            for (const selector of selectors) {
                                const button = document.querySelector(selector);
                                if (button && window.getComputedStyle(button).display !== 'none') {
                                    try {
                                        button.click();
                                        return { method: selector, success: true };
                                    } catch (e) {
                                        continue;
                                    }
                                }
                            }
                            
                            // 尝试按Enter键
                            const textareas = document.querySelectorAll('textarea');
                            for (const textarea of textareas) {
                                if (textarea.value && textarea.value.trim()) {
                                    const event = new KeyboardEvent('keydown', {
                                        key: 'Enter',
                                        code: 'Enter',
                                        keyCode: 13,
                                        bubbles: true
                                    });
                                    textarea.dispatchEvent(event);
                                    return { method: 'Enter键', success: true };
                                }
                            }
                            
                            return { method: '所有方法', success: false };
                        }
                    """);
                    
                    if (result instanceof Map) {
                        Map<String, Object> jsResult = (Map<String, Object>) result;
                        if (Boolean.TRUE.equals(jsResult.get("success"))) {
                            clicked = true;
                            logInfo.sendTaskLog("JavaScript发送成功: " + jsResult.get("method"), userId, "DeepSeek");
                        }
                    }
                } catch (Exception e) {
                    // JavaScript方法也失败了
                }
            }
            
            // 方法4: 最后尝试按Enter键
            if (!clicked) {
                try {
                    page.keyboard().press("Enter");
                    clicked = true;
                    logInfo.sendTaskLog("使用Enter键发送", userId, "DeepSeek");
                } catch (Exception e) {
                    // 最后的方法也失败了
                }
            }
            
            if (clicked) {
                // 设置发送时间戳
                try {
                    page.evaluate("() => { window._deepseekMessageSentTime = Date.now(); }");
                } catch (Exception e) {
                    // 忽略错误
                }
                
                // 等待确保消息已发送
                Thread.sleep(1000);
                return true;
            } else {
                // 所有发送方法都失败
                return false;
            }
            
        } catch (Exception e) {
            // 发送按钮点击失败，静默处理
            return false;
        }
    }

    /**
     * 处理DeepSeek内容并保存到稿库
     * 只保存AI回答的内容，不以问答形式展现
     * @param page Playwright页面实例
     * @param userInfoRequest 用户信息请求
     * @param roleType 角色类型
     * @param userId 用户ID
     * @param content 已获取的内容
     * @return 处理后的内容
     */
    public String saveDeepSeekContent(Page page, UserInfoRequest userInfoRequest, String roleType, String userId, String content) throws Exception{
        try {
            long startTime = System.currentTimeMillis(); // 记录开始时间
            
            // 1. 从URL提取会话ID和分享链接
            String shareUrl = "";
            String chatId = "";
            try {
                String currentUrl = page.url();
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("/chat/s/([^/]+)");
                java.util.regex.Matcher matcher = pattern.matcher(currentUrl);
                if (matcher.find()) {
                    chatId = matcher.group(1);
                    shareUrl = "https://chat.deepseek.com/a/chat/s/" + chatId;
                    userInfoRequest.setDeepseekChatId(chatId);
                    // 🔥 通知用户会话ID已保存
                    logInfo.sendTaskLog("已获取DeepSeek会话ID: " + chatId + "，下次可继续使用此会话", userId, "DeepSeek");
                    JSONObject chatData = new JSONObject();
                    chatData.put("type", "RETURN_DEEPSEEK_CHATID");
                    chatData.put("chatId", chatId);
                    chatData.put("userId", userId);
                    webSocketClientService.sendMessage(chatData.toJSONString());
                }
            } catch (Exception e) {
                // URL提取失败，静默处理
            }
            
            // 2. 生成最后一组对话的长截图（参考百度的处理方案）
            String shareImgUrl = null;
            try {
                shareImgUrl = captureLastConversationScreenshot(page, userId);
                logInfo.sendTaskLog("成功生成对话截图", userId, "DeepSeek");
            } catch (Exception e) {
                // 截图失败，静默处理
            }
            
            // 3. 只保留AI内容，不加对话包装
            String cleanedContent = cleanDeepSeekContent(content, userId);
            String displayContent = cleanedContent;
            if (cleanedContent == null || cleanedContent.trim().isEmpty()) {
                displayContent = content;
            }
            
            // 4. 设置AI名称
            String aiName = "DeepSeek";
            if (roleType != null) {
                boolean hasDeepThinking = roleType.contains("ds-sdsk");
                boolean hasWebSearch = roleType.contains("ds-lwss");
                if (hasDeepThinking && hasWebSearch) {
                    aiName = "DeepSeek-思考联网";
                } else if (hasDeepThinking) {
                    aiName = "DeepSeek-深度思考";
                } else if (hasWebSearch) {
                    aiName = "DeepSeek-联网搜索";
                }
            }
            
            // 5. 发送内容到前端
            logInfo.sendResData(displayContent, userId, "DeepSeek", "RETURN_DEEPSEEK_RES", shareUrl, shareImgUrl, userInfoRequest.getTaskId());
            
            // 6. 保存内容到稿库
            userInfoRequest.setDraftContent(displayContent);
            userInfoRequest.setAiName(aiName);
            userInfoRequest.setShareUrl(shareUrl);
            userInfoRequest.setShareImgUrl(shareImgUrl);
            Object response = RestUtils.post(url + "/saveDraftContent", userInfoRequest);
            logInfo.sendTaskLog("执行完成", userId, "DeepSeek");
            return displayContent;
        } catch (Exception e) {
            // 异常向上抛出，由AOP统一处理
            throw e;
        }
    }


    /**
     * 通用方法：根据目标激活状态切换按钮（深度思考/联网搜索）
     * @param page Playwright页面
     * @param userId 用户ID
     * @param buttonText 按钮文本（如"深度思考"、"联网搜索"）
     * @param shouldActive 期望激活(true)还是关闭(false)
     * @param logInfo 日志工具
     */
    private void toggleButtonIfNeeded(Page page, String userId, String buttonText, boolean shouldActive, LogMsgUtil logInfo,String aiName) {
        try {
            // 使用更简单的选择器
            String buttonSelector = String.format("button:has-text('%s'), div[role='button']:has-text('%s')", buttonText, buttonText);

            // 增加超时时间并等待按钮可交互
            Locator button = page.locator(buttonSelector).first();
            button.waitFor(new Locator.WaitForOptions().setTimeout(10000)); // 增加到10秒

            if (!button.isVisible()) {
                logInfo.sendTaskLog(buttonText + "按钮不可见", userId, aiName);
                return;
            }

            // 获取按钮的完整类名
            String currentClasses = (String) button.evaluate("el => el.className");

            // 检查当前状态：是否包含 _76f196b 类
            boolean isCurrentlyActive = currentClasses.contains("_76f196b");

            // 只在状态不符时点击
            if (isCurrentlyActive != shouldActive) {
                // 使用强制点击避免被其他元素遮挡
                button.click(new Locator.ClickOptions().setTimeout(5000).setForce(true));

                // 等待状态变化
                boolean stateChanged = false;
                for (int i = 0; i < 10; i++) { // 🔥 优化：降低重试次数（从15降至10），加快响应
                    page.waitForTimeout(200);

                    String newClasses = (String) button.evaluate("el => el.className");
                    boolean isNowActive = newClasses.contains("_76f196b");

                    if (isNowActive == shouldActive) {
                        stateChanged = true;
                        break;
                    }
                }

                if (stateChanged) {
                    logInfo.sendTaskLog((shouldActive ? "已启动" : "已关闭") + buttonText + "模式", userId, aiName);
                }
            } else {
                logInfo.sendTaskLog(buttonText + "模式已经是" + (shouldActive ? "开启" : "关闭") + "状态", userId, aiName);
            }
        } catch (Exception e) {
            // 模式切换失败，静默处理
        }
    }


    /**
     * 清理DeepSeek内容中的图标和其他不需要的元素
     * @param content 原始内容
     * @param userId 用户ID，用于记录日志
     * @return 清理后的内容
     */
    private String cleanDeepSeekContent(String content, String userId) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        
        try {
            // 清理DeepSeek头像图标和其他不需要的元素
            String cleaned = content;
            
            // 1. 清理DeepSeek头像图标容器（多种模式匹配）
            cleaned = cleaned.replaceAll("<div class=\"[^\"]*_7eb2358[^\"]*\"[^>]*>.*?</div>", "");
            cleaned = cleaned.replaceAll("<div class=\"[^\"]*_58dfa60[^\"]*\"[^>]*>.*?</div>", "");
            
            // 2. 清理SVG图标及其容器
            cleaned = cleaned.replaceAll("<div[^>]*>\\s*<svg[^>]*>.*?</svg>\\s*</div>", "");
            cleaned = cleaned.replaceAll("<svg[^>]*>.*?</svg>", "");
            
            // 3. 清理其他可能的头像或图标容器
            cleaned = cleaned.replaceAll("<div class=\"[^\"]*avatar[^\"]*\"[^>]*>.*?</div>", "");
            cleaned = cleaned.replaceAll("<div class=\"[^\"]*icon[^\"]*\"[^>]*>.*?</div>", "");
            
            // 4. 清理空的div标签
            cleaned = cleaned.replaceAll("<div[^>]*>\\s*</div>", "");
            
            // 5. 清理连续的空白字符
            cleaned = cleaned.replaceAll("\\s{2,}", " ");
            
            // 如果内容被完全清空或只剩下少量HTML标签，返回原始内容
            String textOnly = cleaned.replaceAll("<[^>]+>", "").trim();
            if (textOnly.isEmpty() || textOnly.length() < 10) {
                return content;
            }
            
            logInfo.sendTaskLog("已清理HTML内容中的头像图标和交互元素，保留原始格式", userId, "DeepSeek");
            return cleaned;
        } catch (Exception e) {
            // 出现异常时记录日志并返回原始内容
            return content;
        }
    }

    /**
     * 获取最新的DeepSeek回答内容，并检查是否包含完成按钮组
     * @param page Playwright页面对象
     * @return 包含内容和完成状态的Map
     */
    private Map<String, Object> getLatestDeepSeekResponseWithCompletion(Page page) {
        try {
            Object jsResult = page.evaluate("""
            () => {
                try {
                    // 查找包含特定class的最新回复区域
                    const responseContainers = document.querySelectorAll('div._4f9bf79.d7dc56a8._43c05b5');
                    if (responseContainers.length === 0) {
                        return {
                            content: '',
                            textContent: '',
                            length: 0,
                            hasActionButtons: false,
                            source: 'no-response-containers',
                            timestamp: Date.now()
                        };
                    }
                    
                    // 获取最后一个回复容器（最新的回复）
                    const latestContainer = responseContainers[responseContainers.length - 1];
                    
                    // 检查是否包含操作按钮组
                    const actionButtonsSelector = 'div.ds-flex._0a3d93b[style*="align-items: center; gap: 10px"] div.ds-flex._965abe9._54866f7';
                    const hasActionButtons = latestContainer.querySelector(actionButtonsSelector) !== null;
                    
                    // 🔥 优先提取.ds-markdown-html（不含深度思考的纯回答内容）
                    let markdownElement = latestContainer.querySelector('.ds-markdown-html');
                    let isHtmlContent = true;
                    
                    // 如果没有找到.ds-markdown-html，回退到.ds-markdown
                    if (!markdownElement) {
                        markdownElement = latestContainer.querySelector('.ds-markdown');
                        isHtmlContent = false;
                    }
                    
                    if (!markdownElement) {
                        return {
                            content: '',
                            textContent: '',
                            length: 0,
                            hasActionButtons: hasActionButtons,
                            source: 'no-markdown-in-container',
                            timestamp: Date.now()
                        };
                    }
                    
                    // 克隆内容以避免修改原DOM
                    const contentClone = markdownElement.cloneNode(true);
                    
                    // 移除不需要的元素
                    const elementsToRemove = contentClone.querySelectorAll(
                        'svg, .ds-icon, button, [role="button"], ' +
                        '[class*="loading"], [class*="typing"], [class*="cursor"], ' +
                        '.md-code-block-banner, .code-info-button-text'
                    );
                    elementsToRemove.forEach(el => el.remove());
                    
                    // 获取文本内容
                    const textContent = contentClone.textContent || '';
                    const contentLength = textContent.trim().length;
                    
                    return {
                        content: contentClone.innerHTML,
                        textContent: textContent,
                        length: contentLength,
                        hasActionButtons: hasActionButtons,
                        source: isHtmlContent ? 'ds-markdown-html-only' : 'ds-markdown-full',
                        timestamp: Date.now()
                    };
                } catch (e) {
                    return {
                        content: '',
                        textContent: '',
                        length: 0,
                        hasActionButtons: false,
                        source: 'error',
                        error: e.toString(),
                        timestamp: Date.now()
                    };
                }
            }
            """);

            if (jsResult instanceof Map) {
                return (Map<String, Object>) jsResult;
            }
        } catch (Exception e) {
            System.err.println("获取DeepSeek回答时出错: " + e.getMessage());
        }

        return new HashMap<>();
    }

    /**
     * 获取最后一组对话内容（参考百度的处理方案）
     * @param page Playwright页面对象
     * @param userId 用户ID
     * @return 最后一组对话的完整内容
     */
    private String getLastConversationContent(Page page, String userId) {
        try {
            logInfo.sendTaskLog("开始获取最后一组对话内容", userId, "DeepSeek");
            
            Object jsResult = page.evaluate("""
            () => {
                try {
                    // 查找所有回复容器
                    const responseContainers = document.querySelectorAll('div._4f9bf79.d7dc56a8._43c05b5');
                    if (responseContainers.length === 0) {
                        return { content: '', source: 'no-containers' };
                    }
                    
                    // 获取最后一个回复容器（最新的回复）
                    const latestContainer = responseContainers[responseContainers.length - 1];
                    
                    // 克隆容器以避免修改原DOM
                    const containerClone = latestContainer.cloneNode(true);
                    
                    // 移除不需要的交互元素，但保留结构
                    const elementsToRemove = containerClone.querySelectorAll(
                        'button, [role="button"], ' +
                        '[class*="loading"], [class*="typing"], [class*="cursor"], ' +
                        '.code-info-button-text, ._17e543b'
                    );
                    elementsToRemove.forEach(el => el.remove());
                    
                    // 清理空的div容器
                    const emptyDivs = containerClone.querySelectorAll('div:empty');
                    emptyDivs.forEach(div => div.remove());
                    
                    // 获取清理后的HTML内容
                    const cleanedContent = containerClone.innerHTML;
                    
                    return {
                        content: cleanedContent,
                        source: 'last-conversation-cleaned',
                        timestamp: Date.now()
                    };
                } catch (e) {
                    return {
                        content: '',
                        source: 'error',
                        error: e.toString()
                    };
                }
            }
            """);

            if (jsResult instanceof Map) {
                Map<String, Object> result = (Map<String, Object>) jsResult;
                String content = (String) result.getOrDefault("content", "");
                if (!content.trim().isEmpty()) {
                    logInfo.sendTaskLog("成功获取最后一组对话内容", userId, "DeepSeek");
                    return content;
                }
            }
            
            // 如果上述方法失败，回退到原有方法
            logInfo.sendTaskLog("回退到原有内容获取方法", userId, "DeepSeek");
            return getLatestAiResponse(page);
            
        } catch (Exception e) {
            // 获取失败，回退到原方法，静默处理
            return getLatestAiResponse(page);
        }
    }

    /**
     * 截取最后一组对话的长截图（参考百度的处理方案）
     * @param page Playwright页面对象
     * @param userId 用户ID
     * @return 截图URL
     */
    private String captureLastConversationScreenshot(Page page, String userId) throws Exception {
        try {
            logInfo.sendTaskLog("开始截取最后一组对话截图", userId, "DeepSeek");
            
            // 等待页面稳定
            page.waitForTimeout(1000);
            
            // 使用JavaScript定位最后一组对话区域并截图
            Object screenshotResult = page.evaluate("""
                () => {
                    try {
                        // 查找所有回复容器
                        const responseContainers = document.querySelectorAll('div._4f9bf79.d7dc56a8._43c05b5');
                        if (responseContainers.length === 0) {
                            return { success: false, message: 'no-containers' };
                        }
                        
                        // 获取最后一个回复容器（最新的回复）
                        const latestContainer = responseContainers[responseContainers.length - 1];
                        
                        // 滚动到该容器顶部
                        latestContainer.scrollIntoView({ behavior: 'smooth', block: 'start' });
                        
                        // 获取容器的边界信息
                        const rect = latestContainer.getBoundingClientRect();
                        
                        return {
                            success: true,
                            x: Math.max(0, rect.left),
                            y: Math.max(0, rect.top),
                            width: Math.min(rect.width, window.innerWidth),
                            height: Math.min(rect.height, window.innerHeight)
                        };
                    } catch (e) {
                        return { success: false, message: e.toString() };
                    }
                }
            """);
            
            if (screenshotResult instanceof Map) {
                Map<String, Object> result = (Map<String, Object>) screenshotResult;
                if (Boolean.TRUE.equals(result.get("success"))) {
                    // 等待滚动完成
                    page.waitForTimeout(1500);
                    
                    // 进行完整页面截图（因为对话区域可能很长）
                    String screenshotPath = "deepseek_conversation_" + System.currentTimeMillis() + ".png";
                    
                    // 使用全页面截图，确保捕获完整内容
                    page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get(screenshotPath))
                        .setFullPage(true)
                        .setType(com.microsoft.playwright.options.ScreenshotType.PNG)
                    );
                    
                                         // 上传截图并返回URL
                     String uploadedUrl = uploadFile(screenshotUtil.uploadUrl, screenshotPath);
                     logInfo.sendTaskLog("对话截图已生成并上传", userId, "DeepSeek");
                     
                     return uploadedUrl;
                }
            }
            
            // 如果上述方法失败，使用简单的全页面截图
            logInfo.sendTaskLog("使用备用截图方案", userId, "DeepSeek");
            String fallbackPath = "deepseek_fallback_" + System.currentTimeMillis() + ".png";
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(fallbackPath))
                .setFullPage(true)
                .setType(com.microsoft.playwright.options.ScreenshotType.PNG)
            );
            
                         return uploadFile(screenshotUtil.uploadUrl, fallbackPath);
            
        } catch (Exception e) {
            // 异常向上抛出，由AOP统一处理
            throw e;
        }
    }

    /**
     * 点击复制按钮并获取纯回答内容（过滤思考过程）
     * @param page Playwright页面对象
     * @param userId 用户ID
     * @return 过滤后的回答内容
     */
    private String clickCopyButtonAndGetAnswer(Page page, String userId) {
        try {
            logInfo.sendTaskLog("正在点击复制按钮获取回答内容", userId, "DeepSeek");
            
            // 等待并点击复制按钮
            Object result = page.evaluate("""
                () => {
                    try {
                        // 查找最新的回复容器
                        const responseContainers = document.querySelectorAll('div._4f9bf79.d7dc56a8._43c05b5');
                        if (responseContainers.length === 0) {
                            return { success: false, error: 'no-response-containers' };
                        }
                        
                        // 获取最后一个回复容器（最新的回复）
                        const latestContainer = responseContainers[responseContainers.length - 1];
                        
                        // 🔥 改进：使用更精确的复制按钮定位方式
                        // 方法1：通过操作按钮组容器查找
                        const actionButtonsContainer = latestContainer.querySelector('div.ds-flex._965abe9._54866f7[style*="align-items: center; gap: 10px"]');
                        
                        if (actionButtonsContainer) {
                            // 查找所有按钮
                            const buttons = actionButtonsContainer.querySelectorAll('div.ds-icon-button.db183363[role="button"]');
                            
                            // 遍历按钮，找到包含复制图标的按钮
                            for (let button of buttons) {
                                const copyIcon = button.querySelector('svg path[d*="M6.14926 4.02039"]');
                                if (copyIcon) {
                                    // 找到复制按钮，点击它
                                    button.click();
                                    return { success: true, message: 'copy-button-clicked-by-icon' };
                                }
                            }
                        }
                        
                        // 方法2：回退方案 - 直接查找包含复制图标的按钮
                        const allCopyButtons = latestContainer.querySelectorAll('div.ds-icon-button[role="button"]');
                        for (let button of allCopyButtons) {
                            const copyIcon = button.querySelector('svg path[d*="M6.14926 4.02039"]');
                            if (copyIcon) {
                                button.click();
                                return { success: true, message: 'copy-button-clicked-fallback' };
                            }
                        }
                        
                        return { success: false, error: 'copy-button-not-found' };
                    } catch (e) {
                        return { success: false, error: e.toString() };
                    }
                }
                """);
            
            if (result instanceof Map) {
                Map<String, Object> resultMap = (Map<String, Object>) result;
                Boolean success = (Boolean) resultMap.get("success");
                
                if (success != null && success) {
                    // 🔒 使用剪贴板锁保护剪贴板操作
                    AtomicReference<String> contentRef = new AtomicReference<>();
                    
                    clipboardLockManager.runWithClipboardLock(() -> {
                        try {
                            // 等待剪贴板更新
                            Thread.sleep(2000);
                            
                            // 获取剪贴板内容
                            String clipboardContent = (String) page.evaluate("navigator.clipboard.readText()");
                            
                            if (clipboardContent != null && !clipboardContent.trim().isEmpty()) {
                                // 过滤思考内容，只保留回答部分
                                String filteredContent = filterThinkingContent(clipboardContent, userId);
                                contentRef.set(filteredContent);
                                logInfo.sendTaskLog("成功获取并过滤回答内容", userId, "DeepSeek");
                            } else {
                                logInfo.sendTaskLog("剪贴板内容为空", userId, "DeepSeek");
                                contentRef.set("");
                            }
                        } catch (Exception e) {
                            logInfo.sendTaskLog("剪贴板读取失败: " + e.getMessage(), userId, "DeepSeek");
                            contentRef.set("");
                        }
                    });
                    
                    return contentRef.get();
                } else {
                    // 复制按钮点击失败，返回空字符串
                    return "";
                }
            }
            
            return "";
        } catch (Exception e) {
            // 点击复制按钮失败，静默处理
            return "";
        }
    }
    
    /**
     * 过滤思考内容，只保留回答部分
     * @param content 原始复制的内容
     * @param userId 用户ID
     * @return 过滤后的内容
     */
    private String filterThinkingContent(String content, String userId) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }
        
        try {
            // 移除思考标记开始到结束的内容
            // DeepSeek的思考内容通常包含在特定的标记中
            String filtered = content;
            
            // 1. 移除思考过程标记块（常见的思考标记）
            filtered = filtered.replaceAll("(?s)<thinking>.*?</thinking>", "");
            filtered = filtered.replaceAll("(?s)```thinking.*?```", "");
            filtered = filtered.replaceAll("(?s)\\*\\*思考过程：\\*\\*.*?\\*\\*回答：\\*\\*", "**回答：**");
            filtered = filtered.replaceAll("(?s)思考过程：.*?回答：", "");
            filtered = filtered.replaceAll("(?s)【思考】.*?【回答】", "");
            
            // 2. 移除常见的思考提示词
            String[] thinkingPatterns = {
                "让我想想...",
                "让我思考一下...",
                "我需要仔细考虑...",
                "让我分析一下...",
                "首先，我需要理解...",
                "思考过程：",
                "分析过程：",
                "推理步骤：",
                "解题思路：",
                "我的思考：",
                "分析如下：",
                "让我逐步分析：",
                "步骤分析："
            };
            
            // 移除这些思考提示及其后的内容直到第一个实质性回答
            for (String pattern : thinkingPatterns) {
                // 如果内容以思考提示开始，尝试找到实际回答的开始
                if (filtered.toLowerCase().startsWith(pattern.toLowerCase())) {
                    // 查找可能的回答开始标记
                    String[] answerMarkers = {
                        "回答：", "答案：", "结论：", "总结：", "因此，", "所以，", 
                        "综上，", "最终答案：", "我的回答是：", "答："
                    };
                    
                    int bestIndex = -1;
                    for (String marker : answerMarkers) {
                        int index = filtered.indexOf(marker);
                        if (index > 0 && (bestIndex == -1 || index < bestIndex)) {
                            bestIndex = index;
                        }
                    }
                    
                    if (bestIndex > 0) {
                        filtered = filtered.substring(bestIndex);
                        break;
                    }
                }
            }
            
            // 3. 移除段落开头的思考性语句
            String[] lines = filtered.split("\n");
            StringBuilder result = new StringBuilder();
            boolean foundMainContent = false;
            
            for (String line : lines) {
                String trimmedLine = line.trim();
                
                // 跳过空行
                if (trimmedLine.isEmpty()) {
                    result.append(line).append("\n");
                    continue;
                }
                
                // 检查是否是思考性语句
                boolean isThinkingLine = false;
                for (String pattern : thinkingPatterns) {
                    if (trimmedLine.toLowerCase().startsWith(pattern.toLowerCase())) {
                        isThinkingLine = true;
                        break;
                    }
                }
                
                // 如果不是思考性语句，或者已经找到了主要内容，则保留
                if (!isThinkingLine || foundMainContent) {
                    result.append(line).append("\n");
                    if (!isThinkingLine) {
                        foundMainContent = true;
                    }
                }
            }
            
            // 4. 清理多余的空行和空白字符
            String finalResult = result.toString().trim();
            finalResult = finalResult.replaceAll("\n{3,}", "\n\n"); // 最多保留两个连续换行
            
            // 如果过滤后内容为空或过短，返回原内容
            if (finalResult.isEmpty() || finalResult.length() < 10) {
                logInfo.sendTaskLog("过滤后内容过短，返回原内容", userId, "DeepSeek");
                return content;
            }
            
            logInfo.sendTaskLog("成功过滤思考内容，保留回答部分", userId, "DeepSeek");
            return finalResult;
            
        } catch (Exception e) {
            // 过滤失败，返回原内容，静默处理
            return content;
        }
    }

    // 用于记录是否已点击过刷新按钮，避免重复点击
    private boolean hasClickedRefreshButton = false;
    
    /**
     * 检测并点击DeepSeek的刷新按钮
     * 刷新按钮通常出现在用户消息的左侧，class为"_001e3bb"
     * 注意：需要排除侧边栏开关按钮（父元素包含_4f3769f）
     * @param page Playwright页面对象
     * @param userId 用户ID
     * @param aiName AI名称
     */
    private void checkAndClickRefreshButton(Page page, String userId, String aiName) {
        try {
            // 如果已经点击过刷新按钮，不再重复点击
            if (hasClickedRefreshButton) {
                return;
            }
            
            // 使用JavaScript进行更精确的检测，排除侧边栏按钮
            Object result = page.evaluate("""
                () => {
                    try {
                        // 查找所有包含 _001e3bb 的div
                        const potentialButtons = document.querySelectorAll('div._001e3bb');
                        
                        for (const innerDiv of potentialButtons) {
                            const parentButton = innerDiv.parentElement;
                            
                            // 检查父元素是否是按钮
                            if (!parentButton || parentButton.getAttribute('role') !== 'button') {
                                continue;
                            }
                            
                            // 排除侧边栏按钮（包含_4f3769f class）
                            if (parentButton.className.includes('_4f3769f')) {
                                continue; // 这是侧边栏按钮，跳过
                            }
                            
                            // 检查按钮是否可见
                            const styles = window.getComputedStyle(parentButton);
                            if (styles.display === 'none' || styles.visibility === 'hidden') {
                                continue;
                            }
                            
                            // 检查附近是否有用户消息（真正的刷新按钮应该在用户消息附近）
                            const userMessages = document.querySelectorAll('div.fbb737a4');
                            if (userMessages.length > 0) {
                                // 找到真正的刷新按钮，标记位置以便后续点击
                                parentButton.setAttribute('data-refresh-button', 'true');
                                return { found: true, message: '找到刷新按钮' };
                            }
                        }
                        
                        return { found: false, message: '未找到刷新按钮' };
                    } catch (e) {
                        return { found: false, message: e.toString() };
                    }
                }
            """);
            
            if (result instanceof Map) {
                Map<String, Object> resultMap = (Map<String, Object>) result;
                Boolean found = (Boolean) resultMap.get("found");
                
                if (found != null && found) {
                    // 找到刷新按钮，先滚动到可见区域再点击
                    Locator refreshButton = page.locator("[data-refresh-button='true']").first();
                    if (refreshButton.count() > 0) {
                        try {
                            logInfo.sendTaskLog("检测到刷新按钮，准备点击", userId, aiName);
                            refreshButton.scrollIntoViewIfNeeded();
                            page.waitForTimeout(500);
                            refreshButton.click(new Locator.ClickOptions().setForce(true).setTimeout(3000));
                            hasClickedRefreshButton = true;
                            logInfo.sendTaskLog("已点击刷新按钮，重新生成回答", userId, aiName);
                            page.waitForTimeout(1000);
                        } catch (Exception clickEx) {
                            // 点击失败，标记已尝试避免重复，静默处理
                            hasClickedRefreshButton = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 刷新按钮检测失败，静默处理不抛出异常
        }
    }
    
    /**
     * 🔥 检测文本内容是否包含AI思考过程
     * 
     * 识别特征：
     * - 以"让我"、"首先"、"接下来"开头
     * - 包含步骤描述词："然后"、"最后"、"需要"
     * - 包含元认知词汇："我需要"、"应该"、"要"
     * 
     * @param content 文本内容
     * @return true表示可能包含思考过程，false表示正常内容
     */
    private static boolean detectThinkingContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        
        // 先提取纯文本（去除HTML标签）
        String text = content.replaceAll("<[^>]+>", "").trim();
        
        // 🔥 强特征：开头包含明显的思考过程标识
        String[] strongStartPatterns = {
            "用户让我", "让我", "我需要先", "首先，我需要", "首先，需要",
            "我需要把", "我要把", "需要先", "应该先"
        };
        
        for (String pattern : strongStartPatterns) {
            if (text.startsWith(pattern)) {
                return true;
            }
        }
        
        // 🔥 中等特征：前100字符内包含多个思考过程关键词
        String prefix = text.length() > 100 ? text.substring(0, 100) : text;
        int thinkingKeywordCount = 0;
        String[] thinkingKeywords = {
            "首先，", "接下来，", "然后", "最后", "需要", "应该", 
            "我需要", "要先", "接着", "之后", "确定", "处理", "转换"
        };
        
        for (String keyword : thinkingKeywords) {
            if (prefix.contains(keyword)) {
                thinkingKeywordCount++;
            }
        }
        
        // 如果前100字符内出现3个以上思考关键词，判定为思考过程
        if (thinkingKeywordCount >= 3) {
            return true;
        }
        
        // 🔥 弱特征：内容过于结构化（像步骤说明）
        // 检查是否包含大量的步骤描述
        boolean hasFirstStep = prefix.contains("第一") || prefix.contains("1.") || prefix.contains("一、");
        boolean hasSecondStep = prefix.contains("第二") || prefix.contains("2.") || prefix.contains("二、");
        boolean hasThirdStep = prefix.contains("第三") || prefix.contains("3.") || prefix.contains("三、");
        
        // 如果前100字符包含明确的步骤序号，可能是思考过程
        if (hasFirstStep && hasSecondStep && hasThirdStep) {
            return true;
        }
        
        return false;
    }
} 