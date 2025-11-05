package com.playwright.utils.ai;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.playwright.entity.AiResult;
import com.playwright.entity.UserInfoRequest;
import com.playwright.entity.mcp.McpResult;
import com.playwright.utils.common.ClipboardLockManager;
import com.playwright.utils.common.LogMsgUtil;
import com.playwright.utils.common.UserLogUtil;
import com.playwright.websocket.WebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 豆包AI工具类
 * 提供与豆包AI交互的自动化操作功能
 *
 * @author 优立方
 * @version JDK 17
 * @date 2025年05月27日 10:33
 */
@Component
public class DouBaoUtil {

    @Autowired
    private LogMsgUtil logInfo;

    @Autowired
    private ClipboardLockManager clipboardLockManager;

    @Autowired
    private WebSocketClientService webSocketClientService;

    @Value("${cube.url}")
    private String url;

    /**
     * 🔥 安全地点击最新消息的分享按钮
     * 优先使用简单可靠的方案，确保一次成功
     * 
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param aiName AI名称
     * @param timeoutMs 超时时间（毫秒）
     * @return 是否成功点击
     */
    public boolean clickLatestShareButtonSafely(Page page, String userId, String aiName, long timeoutMs) {
        try {
            logInfo.sendTaskLog("正在定位最新消息的分享按钮...", userId, aiName);
            
            // 🔥 策略1（优先）：使用 Playwright 的 last() 选择器直接定位最后一个分享按钮
            // 这是最简单可靠的方案，成功率最高
            try {
                Locator shareButton = page.locator("button[data-testid='message_action_share']").last();
                if (shareButton.count() > 0) {
                    // 滚动到按钮位置
                    shareButton.scrollIntoViewIfNeeded();
                    Thread.sleep(500);
                    
                    shareButton.click();
                    Thread.sleep(1000); // 等待分享面板完全打开
                    logInfo.sendTaskLog("✅ 已成功点击最新消息的分享按钮", userId, aiName);
                    return true;
                }
            } catch (Exception e) {
                logInfo.sendTaskLog("⚠️ 方案1失败，尝试方案2...", userId, aiName);
            }
            
            // 🔥 策略2（备用）：使用 JavaScript 查找并点击最新消息的分享按钮
            // 可以绕过 Playwright 的可见性检查，直接操作 DOM
            try {
                // 先等待消息块容器出现
                page.locator("[data-testid='message-block-container']").last().waitFor(
                    new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(5000)
                );
                
                // 滚动到最新消息块
                page.evaluate("document.querySelector('[data-testid=\"message-block-container\"]:last-of-type')?.scrollIntoView({behavior: 'smooth', block: 'center'})");
                Thread.sleep(1000);
                
                boolean clicked = (boolean) page.evaluate(
                    "() => {" +
                    "  const receiveMessages = Array.from(document.querySelectorAll('[data-testid=\"receive_message\"]'));" +
                    "  if (receiveMessages.length === 0) return false;" +
                    "  const lastMessage = receiveMessages[receiveMessages.length - 1];" +
                    "  const shareButton = lastMessage.querySelector('button[data-testid=\"message_action_share\"]');" +
                    "  if (!shareButton) return false;" +
                    "  shareButton.scrollIntoView({behavior: 'smooth', block: 'center'});" +
                    "  const mouseoverEvent = new MouseEvent('mouseover', { bubbles: true });" +
                    "  lastMessage.dispatchEvent(mouseoverEvent);" +
                    "  shareButton.click();" +
                    "  return true;" +
                    "}"
                );
                
                if (clicked) {
                    Thread.sleep(1000);
                    logInfo.sendTaskLog("✅ 已成功点击最新消息的分享按钮（方案2）", userId, aiName);
                    return true;
                }
            } catch (Exception e) {
                logInfo.sendTaskLog("⚠️ 方案2也失败: " + e.getMessage(), userId, aiName);
            }
            
            logInfo.sendTaskLog("❌ 所有方案均失败，未找到分享按钮", userId, aiName);
            return false;
            
        } catch (Exception e) {
            UserLogUtil.sendAIWarningLog(userId, aiName, "分享按钮点击", 
                "点击最新消息分享按钮失败: " + e.getMessage(), url + "/saveLogInfo");
            return false;
        }
    }

    /**
     * 检测并点击超能模式的"试一试"按钮
     * 如果登录后出现超能模式提示，自动点击试一试按钮
     *
     * @param page Playwright页面实例
     * @param userId 用户ID
     */
    public void checkAndClickSuperModeButton(Page page, String userId) {
        try {
            // 等待一下，确保页面加载完成
            page.waitForTimeout(2000);
            
            // 通过文本内容定位"试一试"按钮
            Locator tryButton = page.locator("button:has-text(\"试一试\")");
            
            // 检查按钮是否存在且可见
            if (tryButton.count() > 0 && tryButton.isVisible()) {
                logInfo.sendTaskLog("检测到超能模式提示，正在自动点击试一试", userId, "豆包");
                tryButton.click();
                page.waitForTimeout(1000); // 等待点击完成
                logInfo.sendTaskLog("已成功进入超能模式", userId, "豆包");
                
                 // 不再记录成功日志，按照用户要求
            }
        } catch (Exception e) {
            // 如果按钮不存在或点击失败，记录但不抛出异常，不影响后续流程
            UserLogUtil.sendElementWarningLog(userId, "豆包", "超能模式检测", ".switch-button-qHPwBT", "超能模式按钮检测或点击失败：" + e.getMessage(), url + "/saveLogInfo");
        }
    }

    /**
     * 智能切换AI模式（极速/思考/超能）
     * 根据是否需要深度思考以及是否为超能内测用户来决定使用哪个模式
     *
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param needDeepThinking 是否需要深度思考
     */
    public void switchAIMode(Page page, String userId, boolean needDeepThinking) {
        try {
            // 检查页面是否关闭
            if (page.isClosed()) {
                UserLogUtil.sendPageWarningLog(userId, "豆包", "模式切换", "页面已关闭，无法切换AI模式", url + "/saveLogInfo");
                throw new RuntimeException("页面已关闭");
            }
            
            // 等待页面加载完成，给足够时间让按钮渲染
            page.waitForTimeout(2000);  // 增加等待时间到2秒
            
            // 🔥 修复：尝试等待至少一个模式按钮出现（最多等待5秒），减少不必要的警告日志
            try {
                page.locator(".switch-button-qHPwBT").first().waitFor(new Locator.WaitForOptions().setTimeout(5000));
            } catch (TimeoutError e) {
                // 按钮未找到可能是正常情况（比如页面结构变化），降低日志级别
                System.err.println("⚠️  提示：5秒内未检测到模式切换按钮（可能页面结构已更新，功能仍可正常使用）");
                // 不再发送警告日志，避免重复警告
            } catch (Exception e) {
                // 其他异常也降低日志级别
                System.err.println("⚠️  提示：检测模式切换按钮时发生异常，将继续执行");
                // 不再发送警告日志，避免重复警告
            }
            
            // 定位所有模式按钮
            Locator speedModeButton = page.locator(".switch-button-qHPwBT:has-text(\"极速\")").first();
            Locator thinkModeButton = page.locator(".switch-button-qHPwBT:has-text(\"思考\")");
            Locator superModeButton = page.locator("[data-testid='super-agent-mode-switch']");
            
            boolean hasSuperMode = superModeButton.count() > 0;
            
            if (hasSuperMode) {
                // ========== 内测用户（有超能权限）==========
                logInfo.sendTaskLog("检测到超能模式，当前为内测用户", userId, "豆包");
                
                if (needDeepThinking) {
                    // 🔥 优化：需要深度思考时，优先使用思考模式而不是超能模式
                    boolean thinkActive = thinkModeButton.count() > 0 && isModeActive(thinkModeButton);
                    
                    if (thinkModeButton.count() > 0 && !thinkActive) {
                        logInfo.sendTaskLog("任务需要深度思考，正在切换到思考模式", userId, "豆包");
                        thinkModeButton.click();
                        page.waitForTimeout(500);
                        logInfo.sendTaskLog("✓ 已启用思考模式", userId, "豆包");
                    } else if (thinkActive) {
                        logInfo.sendTaskLog("✓ 思考模式已启用（无需切换）", userId, "豆包");
                    } else {
                        // 如果思考模式按钮不存在，则使用超能模式作为备选
                        boolean superActive = isModeActive(superModeButton);
                        if (!superActive) {
                            logInfo.sendTaskLog("思考模式不可用，切换到超能模式", userId, "豆包");
                            superModeButton.click();
                            page.waitForTimeout(500);
                            logInfo.sendTaskLog("✓ 已启用超能模式（备选）", userId, "豆包");
                        } else {
                            logInfo.sendTaskLog("✓ 超能模式已启用（无需切换）", userId, "豆包");
                        }
                    }
                } else {
                    // 不需要深度思考：必须使用极速模式
                    boolean superActive = isModeActive(superModeButton);
                    boolean speedActive = speedModeButton.count() > 0 && isModeActive(speedModeButton);
                    
                    if (superActive) {
                        // 当前是超能模式，需要切换到极速模式
                        logInfo.sendTaskLog("当前为超能模式，但任务无需深度思考，正在切换到极速模式", userId, "豆包");
                        if (speedModeButton.count() > 0) {
                            speedModeButton.click();
                            page.waitForTimeout(500);
                            logInfo.sendTaskLog("✓ 已从超能模式切换到极速模式", userId, "豆包");
                        }
                    } else if (!speedActive && speedModeButton.count() > 0) {
                        // 既不是超能也不是极速，切换到极速
                        logInfo.sendTaskLog("正在切换到极速模式", userId, "豆包");
                        speedModeButton.click();
                        page.waitForTimeout(500);
                        logInfo.sendTaskLog("✓ 已启用极速模式", userId, "豆包");
                    } else {
                        logInfo.sendTaskLog("✓ 极速模式已启用（无需切换）", userId, "豆包");
                    }
                }
            } else {
                // ========== 普通用户（无超能权限）==========
                
                if (needDeepThinking) {
                    // 需要深度思考：使用思考模式
                    boolean thinkActive = thinkModeButton.count() > 0 && isModeActive(thinkModeButton);
                    
                    if (thinkModeButton.count() > 0 && !thinkActive) {
                        logInfo.sendTaskLog("任务需要深度思考，正在切换到思考模式", userId, "豆包");
                        thinkModeButton.click();
                        page.waitForTimeout(500);
                        logInfo.sendTaskLog("✓ 已启用思考模式", userId, "豆包");
                    } else {
                        logInfo.sendTaskLog("✓ 思考模式已启用（无需切换）", userId, "豆包");
                    }
                } else {
                    // 不需要深度思考：使用极速模式
                    boolean speedActive = speedModeButton.count() > 0 && isModeActive(speedModeButton);
                    
                    if (speedModeButton.count() > 0 && !speedActive) {
                        logInfo.sendTaskLog("任务无需深度思考，正在切换到极速模式", userId, "豆包");
                        speedModeButton.click();
                        page.waitForTimeout(500);
                        logInfo.sendTaskLog("✓ 已启用极速模式", userId, "豆包");
                    } else {
                        logInfo.sendTaskLog("✓ 极速模式已启用（无需切换）", userId, "豆包");
                    }
                }
            }
        } catch (com.microsoft.playwright.impl.TargetClosedError e) {
            // 页面目标关闭
            System.err.println("❌ AI模式切换失败: 页面目标已关闭");
            UserLogUtil.sendWebSocketWarningLog(userId, "豆包", "模式切换", "页面目标已关闭，WebSocket可能断联", url + "/saveLogInfo");
        } catch (TimeoutError e) {
            // 超时错误
            System.err.println("❌ AI模式切换超时: " + e.getMessage());
            UserLogUtil.sendElementErrorLog(userId, "豆包", "模式切换", ".switch-button-qHPwBT", "等待模式按钮或切换操作超时: " + e.getMessage(), url + "/saveLogInfo");
        } catch (Exception e) {
            // 如果模式切换失败，记录但不抛出异常，不影响后续流程
            System.err.println("❌ AI模式切换失败: " + e.getMessage());
            e.printStackTrace();
            UserLogUtil.sendElementErrorLog(userId, "豆包", "模式切换", ".switch-button-qHPwBT", "AI模式切换失败：" + e.getMessage(), url + "/saveLogInfo");
        }
    }

    /**
     * 检查按钮是否处于激活状态
     *
     * @param button 按钮定位器
     * @return 如果按钮激活返回true，否则返回false
     */
    private boolean isModeActive(Locator button) {
        try {
            String dataActive = button.getAttribute("data-active");
            return "true".equals(dataActive);
        } catch (Exception e) {
            return false;
        }
    }

    public void waitAndClickDBScoreCopyButton(Page page, String userId) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        try {
            // 等待页面内容稳定
            String currentContent = "";
            String lastContent = "";
            long timeout = 900000; // 15分钟超时（与思考模式保持一致）
            long operationStartTime = System.currentTimeMillis();

            while (true) {
                long elapsedTime = System.currentTimeMillis() - operationStartTime;
                if (elapsedTime > timeout) {
                    // 记录内容等待超时
                    UserLogUtil.sendContentErrorLog(userId, "豆包", "评分内容等待", "等待评分结果生成超时", url + "/saveLogInfo");
                    break;
                }

                Locator outputLocator = page.locator(".flow-markdown-body").last();
                currentContent = outputLocator.innerHTML();

                if (!currentContent.isEmpty() && currentContent.equals(lastContent)) {
                    break;
                }

                lastContent = currentContent;
                page.waitForTimeout(5000); // 每5秒检查一次
            }

            Locator locator = page.locator("//*[@id=\"root\"]/div[1]/div/div[3]/div[1]/div[1]/div/div/div[2]/div/div[2]/div/div/div");
            locator.waitFor(new Locator.WaitForOptions().setTimeout(20000));
            locator.click();

            // 等待复制按钮出现
            page.waitForSelector("[data-testid='message_action_copy']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(600000));  // 600秒超时
            logInfo.sendTaskLog("评分完成，正在自动获取评分内容", userId, "智能评分");
            Thread.sleep(2000);  // 额外等待确保按钮可点击

            // 点击复制按钮
            page.locator("[data-testid='message_action_copy']").last()  // 获取最后一个复制按钮
                    .click();
            logInfo.sendTaskLog("评分结果已自动提取完成", userId, "豆包");

            // 确保点击操作完成
            Thread.sleep(1000);

            // 记录成功日志
            // 不再记录成功日志，按照用户要求

        } catch (TimeoutError e) {
            // 记录元素操作超时异常
            UserLogUtil.sendElementErrorLog(userId, "豆包", "评分任务", ".copy-button", "复制按钮等待或点击操作超时: " + e.getMessage(), url + "/saveLogInfo");
            throw e;
        } catch (Exception e) {
            // 记录其他异常
            UserLogUtil.sendAIExceptionLog(userId, "豆包", "waitAndClickDBScoreCopyButton", e, startTime, "评分任务执行失败", url + "/saveLogInfo");
            throw e;
        }
    }

    public String waitAndClickDBCopyButton(Page page, String userId, String roles) throws InterruptedException {
        try {
            // 检查页面是否关闭
            if (page.isClosed()) {
                UserLogUtil.sendPageWarningLog(userId, "豆包", "内容复制", "页面已关闭，无法复制内容", url + "/saveLogInfo");
                throw new RuntimeException("页面已关闭");
            }
            
            // 等待页面内容稳定
            String currentContent = "";
            String lastContent = "";
            long timeout = 900000; // 15分钟超时（与思考模式保持一致）
            long startTime = System.currentTimeMillis();
            
            // 用于去重警告日志
            long lastWarningTime = 0;

            while (true) {
                // 定期检查页面状态
                if (page.isClosed()) {
                    UserLogUtil.sendPageWarningLog(userId, "豆包", "内容复制", "页面在等待过程中被关闭", url + "/saveLogInfo");
                    throw new RuntimeException("页面在等待过程中被关闭");
                }
                
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > timeout) {
                    UserLogUtil.sendContentErrorLog(userId, "豆包", "内容复制", "等待.flow-markdown-body内容稳定超时", url + "/saveLogInfo");
                    break;
                }

                try {
                    Locator outputLocator = page.locator(".flow-markdown-body").last();
                    currentContent = outputLocator.innerHTML();
                } catch (TimeoutError e) {
                    // 限制警告频率：每30秒最多记录一次
                    long now = System.currentTimeMillis();
                    if (now - lastWarningTime > 30000) {
                        UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "元素未找到：.flow-markdown-body", url + "/saveLogInfo");
                        lastWarningTime = now;
                    }
                    page.waitForTimeout(5000);
                    continue;
                } catch (Exception e) {
                    // 限制警告频率：每30秒最多记录一次
                    long now = System.currentTimeMillis();
                    if (now - lastWarningTime > 30000) {
                        UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "获取内容失败：" + e.getMessage(), url + "/saveLogInfo");
                        lastWarningTime = now;
                    }
                    page.waitForTimeout(5000);
                    continue;
                }

                if (!currentContent.isEmpty() && currentContent.equals(lastContent)) {
                    break;
                }
                lastContent = currentContent;
                page.waitForTimeout(5000); // 每5秒检查一次
            }
            
            String copiedText = "";
            // 等待复制按钮出现
            Locator locator = page.locator("//*[@id=\"root\"]/div[1]/div/div[3]/div[1]/div[1]/div/div/div[2]/div/div[2]/div/div/div");

            try {
                if (locator.count() > 0 && locator.isVisible()) {
                    locator.click(new Locator.ClickOptions().setForce(true));
                }
            } catch (Exception e) {
                UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "点击辅助按钮失败（非关键错误）：" + e.getMessage(), url + "/saveLogInfo");
            }

            page.waitForSelector("[data-testid='message_action_copy']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(600000));  // 600秒超时
            logInfo.sendTaskLog("豆包回答完成，正在自动提取内容", userId, "豆包");
            
            // 点击复制按钮
            Locator copyButton = page.locator("[data-testid='message_action_copy']").last();
            if (copyButton.count() == 0) {
                UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "未找到复制按钮", url + "/saveLogInfo");
                throw new RuntimeException("未找到复制按钮");
            }
            
            try {
                copyButton.click();
            } catch (Exception e) {
                UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "复制按钮不可点击：" + e.getMessage(), url + "/saveLogInfo");
                throw e;
            }
            
            Thread.sleep(2000);
            
            // 读取剪贴板
            try {
                copiedText = (String) page.evaluate("navigator.clipboard.readText()");
                if (copiedText == null || copiedText.trim().isEmpty()) {
                    UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "剪贴板读取内容为空", url + "/saveLogInfo");
                }
            } catch (Exception e) {
                UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "JavaScript执行失败：剪贴板读取失败 - " + e.getMessage(), url + "/saveLogInfo");
                throw e;
            }
            
            logInfo.sendTaskLog("豆包内容已自动提取完成", userId, "豆包");

            // 记录成功日志
            // 不再记录成功日志，按照用户要求
            return copiedText;
        } catch (TimeoutError e) {
            // 记录超时异常
            UserLogUtil.sendAITimeoutLog(userId, "豆包", "内容复制", e, "等待复制按钮或内容提取", url + "/saveLogInfo");
            throw e;
        } catch (com.microsoft.playwright.impl.TargetClosedError e) {
            // 页面目标关闭
            UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "页面目标已关闭，WebSocket可能断联", url + "/saveLogInfo");
            throw e;
        } catch (Exception e) {
            // 记录其他异常
            UserLogUtil.sendAIExceptionLog(userId, "豆包", "waitAndClickDBCopyButton", e, System.currentTimeMillis(), "内容复制失败", url + "/saveLogInfo");
            throw e;
        }
    }

    /**
     * 检查豆包是否仍在生成内容
     * 参考DeepSeek的实现，检测生成指示器、停止按钮等
     */
    private boolean checkDouBaoGenerating(Page page) {
        try {
            Object generatingStatus = page.evaluate("""
            () => {
                try {
                    // 检查是否有生成中的指示器
                    const generatingIndicators = document.querySelectorAll(
                        '.generating-indicator, .loading-indicator, .typing-indicator, ' +
                        '[class*="loading"], [class*="typing"], [class*="generating"], ' +
                        '[class*="cursor-"]'
                    );
                    
                    for (const indicator of generatingIndicators) {
                        if (indicator && 
                            window.getComputedStyle(indicator).display !== 'none' && 
                            window.getComputedStyle(indicator).visibility !== 'hidden') {
                            return true;
                        }
                    }
                    
                    // 检查是否有停止生成按钮
                    const stopButtons = document.querySelectorAll(
                        'button:has-text("停止生成"), button:has-text("Stop"), ' +
                        '[data-testid*="stop"], [class*="stop-button"]'
                    );
                    
                    for (const btn of stopButtons) {
                        if (btn && 
                            window.getComputedStyle(btn).display !== 'none' && 
                            window.getComputedStyle(btn).visibility !== 'hidden') {
                            return true;
                        }
                    }
                    
                    return false;
                } catch (e) {
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
     * @param contentLengthHistory 内容长度历史记录（最近3次）
     * @return 如果内容增长已停止返回true
     */
    private boolean isContentGrowthStopped(int[] contentLengthHistory) {
        // 检查最近三次内容长度是否相同或几乎相同（允许±5的误差）
        if (contentLengthHistory[0] > 0 && 
            Math.abs(contentLengthHistory[0] - contentLengthHistory[1]) <= 5 && 
            Math.abs(contentLengthHistory[1] - contentLengthHistory[2]) <= 5) {
            return true;
        }
        return false;
    }

    /**
     * 获取豆包最新的回复内容及元数据
     * 🔥 关键优化：只获取最新的AI回复，避免混入历史消息或提示词
     * @param page Playwright页面对象
     * @return 包含内容、文本、长度、是否有按钮组等信息的Map
     */
    @SuppressWarnings("unchecked")
    private java.util.Map<String, Object> getLatestDouBaoResponseWithCompletion(Page page) {
        try {
            Object jsResult = page.evaluate("""
            () => {
                try {
                    // 🔥 方案1：优先查找 [data-testid='receive_message'] 最后一个（最新的AI回复）
                    const receiveMessages = document.querySelectorAll('[data-testid="receive_message"]');
                    
                    if (receiveMessages.length > 0) {
                        const latestMessage = receiveMessages[receiveMessages.length - 1];
                        
                        // 🔥🔥 检查是否有可见的操作按钮组（必须是完整的、可见的按钮）
                        let hasActionButtons = false;
                        
                        // 方法1：检测完整的 message_action_bar 容器
                        const actionBar = latestMessage.querySelector('[data-testid="message_action_bar"]');
                        if (actionBar) {
                            // 确保至少有复制和重新生成按钮（核心按钮）
                            const copyBtn = latestMessage.querySelector('[data-testid="message_action_copy"]');
                            const regenBtn = latestMessage.querySelector('[data-testid="message_action_regenerate"]');
                            
                            // 检查按钮是否可见（通过 offsetParent 和 display 样式）
                            const isCopyVisible = copyBtn && copyBtn.offsetParent !== null;
                            const isRegenVisible = regenBtn && regenBtn.offsetParent !== null;
                            
                            hasActionButtons = isCopyVisible && isRegenVisible;
                        }
                        
                        // 方法2：回退方案 - 只检测复制按钮
                        if (!hasActionButtons) {
                            const copyBtn = latestMessage.querySelector('[data-testid="message_action_copy"]');
                            hasActionButtons = copyBtn && copyBtn.offsetParent !== null;
                        }
                        
                        // 获取markdown内容
                        const markdownElement = latestMessage.querySelector('.flow-markdown-body');
                        if (markdownElement) {
                            const contentClone = markdownElement.cloneNode(true);
                            
                            // 移除不需要的元素
                            const elementsToRemove = contentClone.querySelectorAll(
                                'svg, button, [role="button"], ' +
                                '[class*="loading"], [class*="typing"], [class*="cursor"]'
                            );
                            elementsToRemove.forEach(el => el.remove());
                            
                            const textContent = contentClone.textContent || '';
                            const contentLength = textContent.trim().length;
                            
                            return {
                                content: contentClone.innerHTML,
                                textContent: textContent,
                                length: contentLength,
                                hasActionButtons: hasActionButtons,
                                source: 'receive_message',
                                timestamp: Date.now()
                            };
                        }
                    }
                    
                    // 🔥 方案2：回退到 .flow-markdown-body.last()
                    const markdownElements = document.querySelectorAll('.flow-markdown-body');
                    if (markdownElements.length > 0) {
                        const latestMarkdown = markdownElements[markdownElements.length - 1];
                        
                        // 检查是否在用户消息中（排除用户输入）
                        const isInUserMessage = latestMarkdown.closest('[data-testid="send_message"]') !== null;
                        if (isInUserMessage) {
                            // 这是用户的消息，不是AI回复，返回空
                            return {
                                content: '',
                                textContent: '',
                                length: 0,
                                hasActionButtons: false,
                                source: 'user-message-skipped',
                                timestamp: Date.now()
                            };
                        }
                        
                        const contentClone = latestMarkdown.cloneNode(true);
                        const elementsToRemove = contentClone.querySelectorAll(
                            'svg, button, [role="button"], ' +
                            '[class*="loading"], [class*="typing"], [class*="cursor"]'
                        );
                        elementsToRemove.forEach(el => el.remove());
                        
                        const textContent = contentClone.textContent || '';
                        const contentLength = textContent.trim().length;
                        
                        // 🔥 检查附近是否有可见的操作按钮组
                        const parentContainer = latestMarkdown.closest('[data-testid="message-block-container"]');
                        let hasActionButtons = false;
                        
                        if (parentContainer) {
                            // 方法1：检测完整按钮组（复制 + 重新生成）
                            const copyBtn = parentContainer.querySelector('[data-testid="message_action_copy"]');
                            const regenBtn = parentContainer.querySelector('[data-testid="message_action_regenerate"]');
                            
                            // 确保按钮可见
                            const isCopyVisible = copyBtn && copyBtn.offsetParent !== null;
                            const isRegenVisible = regenBtn && regenBtn.offsetParent !== null;
                            
                            hasActionButtons = isCopyVisible && isRegenVisible;
                            
                            // 方法2：回退方案 - 只检测复制按钮
                            if (!hasActionButtons) {
                                hasActionButtons = copyBtn && copyBtn.offsetParent !== null;
                            }
                        }
                        
                        return {
                            content: contentClone.innerHTML,
                            textContent: textContent,
                            length: contentLength,
                            hasActionButtons: hasActionButtons,
                            source: 'flow-markdown-body',
                            timestamp: Date.now()
                        };
                    }
                    
                    return {
                        content: '',
                        textContent: '',
                        length: 0,
                        hasActionButtons: false,
                        source: 'no-content-found',
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

            if (jsResult instanceof java.util.Map) {
                return (java.util.Map<String, Object>) jsResult;
            }
        } catch (Exception e) {
            // 静默处理，返回空Map
        }

        return new java.util.HashMap<>();
    }

    /**
     * html片段获取（核心监控方法）
     * 🔥 重大优化：参考DeepSeek实现，添加完善的生成状态检测和错误处理
     *
     * @param page Playwright页面实例
     */
    public AiResult waitDBHtmlDom(Page page, String userId, String aiName, UserInfoRequest userInfoRequest) throws InterruptedException {
        long methodStartTime = System.currentTimeMillis();
        try {
            // 检查页面是否关闭
            if (page.isClosed()) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "HTML内容监控", "页面已关闭，无法监控内容", url + "/saveLogInfo");
                throw new RuntimeException("页面已关闭");
            }
            
            // 🔥 关键修复：等待AI开始生成新回复，避免获取到历史消息
            logInfo.sendTaskLog("等待AI开始生成新回复...", userId, aiName);
            Thread.sleep(3000);  // 缩短到3秒，提高响应速度
            logInfo.sendTaskLog("开始监听" + aiName + "回复内容", userId, aiName);
            
            // 等待聊天框的内容稳定
            String currentContent = "";
            String lastContent = "";
            String rightCurrentContent = "";
            String textContent = "";
            String rightTextContent = "";
            boolean isRight = false;
            
            // 🔥 新增：内容稳定性检测
            int stableCount = 0;  // 内容稳定次数
            int requiredStableCount = 1;  // 需要的稳定次数
            int emptyCount = 0;  // 空内容计数
            int[] contentLengthHistory = new int[3];  // 记录最近三次内容长度
            boolean hasEverHadContent = false;  // 是否曾经有过内容
            
            // 设置最大等待时间（单位：毫秒），延长到 15 分钟以适应深度思考模式
            long timeout = 900000; // 15 分钟
            long startTime = System.currentTimeMillis();  // 获取当前时间戳
            
            // 用于去重警告日志的时间戳
            long lastWarningTime = 0;
            int checkInterval = 3000;  // 检查间隔，初始3秒

            // 进入循环，直到内容不再变化或者超时
            while (true) {
                // 定期检查页面状态
                if (page.isClosed()) {
                    UserLogUtil.sendAIWarningLog(userId, aiName, "HTML内容监控", "页面在监控过程中被关闭", url + "/saveLogInfo");
                    throw new RuntimeException("页面在监控过程中被关闭");
                }
                
                // 检查是否是代码生成
                Locator chatHis = page.locator("//div[@class='canvas-header-Bc97DC']");
                if (chatHis.count() > 0) {
                    isRight = true;
                } else {
                    isRight = false;
                }
                
                try {
                    Locator changeTypeLocator = page.locator("text=改用对话直接回答");
                    if (changeTypeLocator.isVisible()) {
                        changeTypeLocator.click();
                    }
                } catch (TimeoutError e) {
                    // 切换按钮不存在或不可见，继续
                } catch (Exception e) {
                    // 限制警告日志频率：每30秒最多记录一次
                    long now = System.currentTimeMillis();
                    if (now - lastWarningTime > 30000) {
                        UserLogUtil.sendAIWarningLog(userId, aiName, "HTML内容监控", "切换对话模式按钮操作失败：" + e.getMessage(), url + "/saveLogInfo");
                        lastWarningTime = now;
                    }
                }
                
                // 获取当前时间戳
                long elapsedTime = System.currentTimeMillis() - startTime;

                // 如果超时，退出循环
                if (elapsedTime > timeout) {
                    TimeoutException timeoutEx = new TimeoutException("等待豆包HTML内容超时，已等待：" + (elapsedTime/1000) + "秒");
                    UserLogUtil.sendAITimeoutLog(userId, aiName, "HTML内容监控", timeoutEx, "等待AI回复内容稳定", url + "/saveLogInfo");
                    logInfo.sendTaskLog("⚠️ 等待超时，强制提取当前内容", userId, aiName);
                    break;
                }

                // 🔥🔥🔥 优先检测AI拒绝处理的错误DOM（必须在等待.flow-markdown-body之前检测！）
                try {
                    // 检测是否存在错误图标（豆包拒绝处理标志）
                    Locator errorIcon = page.locator("[data-testid='message_box_failed_icon']").last();
                    int errorIconCount = errorIcon.count();
                    
                    if (errorIconCount > 0) {
                        // 检查是否有有效的回复内容（多种选择器）
                        boolean hasValidContent = false;
                        
                        try {
                            // 方法1：检查 .flow-markdown-body 的文本内容
                            Locator markdownBody = page.locator(".flow-markdown-body").last();
                            if (markdownBody.count() > 0) {
                                String markdownText = markdownBody.textContent();
                                if (markdownText != null && markdownText.trim().length() > 10) {
                                    hasValidContent = true;
                                }
                            }
                            
                            // 方法2：检查 .ds-markdown 的文本内容（豆包深度思考模式）
                            if (!hasValidContent) {
                                Locator dsMarkdown = page.locator(".ds-markdown").last();
                                if (dsMarkdown.count() > 0) {
                                    String dsText = dsMarkdown.textContent();
                                    if (dsText != null && dsText.trim().length() > 10) {
                                        hasValidContent = true;
                                    }
                                }
                            }
                            
                            // 方法3：检查 currentContent 是否有内容
                            if (!hasValidContent && currentContent.trim().length() > 10) {
                                hasValidContent = true;
                            }
                            
                            // 方法4：检查整个消息容器的文本内容（最后手段）
                            if (!hasValidContent) {
                                Locator messageContent = page.locator("[data-testid='message_content']").last();
                                if (messageContent.count() > 0) {
                                    String messageText = messageContent.textContent();
                                    // 排除只有空白字符的情况，排除SVG等非文本内容
                                    String cleanText = messageText != null ? messageText.replaceAll("\\s", "") : "";
                                    if (cleanText.length() > 20) {
                                        hasValidContent = true;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // 内容检测失败，默认认为没有有效内容
                            hasValidContent = false;
                        }
                        
                        // 如果确认只有错误DOM，没有其他有效内容 → 立即终止等待
                        if (!hasValidContent) {
                            logInfo.sendTaskLog("❌ 检测到AI错误标识且无有效内容，立即终止等待", userId, aiName);
                            
                            UserLogUtil.sendAIWarningLog(userId, aiName, "AI处理失败", 
                                "豆包返回错误DOM（message_box_failed_icon），检测到AI拒绝处理或发生错误，无有效内容，已自动终止等待", 
                                url + "/saveLogInfo");
                            
                            // 返回固定的错误消息，立即终止
                            return AiResult.success("<p>AI拒绝处理或遇到错误</p>", "AI拒绝处理或遇到错误");
                        } else {
                            // 有部分内容，记录警告但继续等待
                            long now = System.currentTimeMillis();
                            if (now - lastWarningTime > 30000) {
                                UserLogUtil.sendAIWarningLog(userId, aiName, "AI部分失败", 
                                    "检测到错误标识，但有部分有效内容，继续等待", 
                                    url + "/saveLogInfo");
                                lastWarningTime = now;
                            }
                        }
                    }
                } catch (Exception e) {
                    // 错误检测模块异常，记录但不影响主流程
                    long now = System.currentTimeMillis();
                    if (now - lastWarningTime > 60000) {
                        UserLogUtil.sendAIWarningLog(userId, aiName, "错误检测", 
                            "错误DOM检测异常：" + e.getMessage(), 
                            url + "/saveLogInfo");
                        lastWarningTime = now;
                    }
                }

                // 🔥 新方法：使用统一的内容获取方法
                java.util.Map<String, Object> responseData = getLatestDouBaoResponseWithCompletion(page);
                String newContent = (String) responseData.getOrDefault("content", "");
                String newTextContent = (String) responseData.getOrDefault("textContent", "");
                // 🔥 安全地获取 hasActionButtons，避免 NullPointerException
                Object hasActionButtonsObj = responseData.get("hasActionButtons");
                boolean hasActionButtons = hasActionButtonsObj != null ? (Boolean) hasActionButtonsObj : false;
                int contentLength = 0;
                if (responseData.containsKey("length")) {
                    contentLength = ((Number) responseData.get("length")).intValue();
                }

                // 🔥 处理代码生成模式（右侧textbox）
                try {
                    if (isRight) {
                        Locator outputLocator = page.locator("//div[@role='textbox']");
                        if (outputLocator.count() > 0) {
                            // 增加超时控制，避免无限等待
                            outputLocator.waitFor(new Locator.WaitForOptions().setTimeout(5000).setState(WaitForSelectorState.ATTACHED));
                            rightCurrentContent = outputLocator.innerHTML();
                            rightTextContent = outputLocator.textContent();
                            
                            // 使用代码模式的内容
                            newContent = rightCurrentContent;
                            newTextContent = rightTextContent;
                            contentLength = rightTextContent != null ? rightTextContent.trim().length() : 0;
                        }
                    }
                } catch (Exception e) {
                    // 代码模式获取失败，使用普通模式内容
                }

                // 赋值给当前内容变量
                currentContent = newContent;
                textContent = newTextContent;

                // 🔥 内容有效性检查
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
                    
                    // 🔥 检查内容是否稳定
                    if (currentContent.equals(lastContent)) {
                        stableCount++;
                        
                        // 🔥 检查是否仍在生成
                        boolean isGenerating = checkDouBaoGenerating(page);
                        
                        // 🔥 智能判断完成条件（参考DeepSeek）
                        boolean isComplete = false;
                        
                        // 条件1: 检测到按钮组 + 内容已稳定（双重确认，避免过早截断）
                        if (hasActionButtons && stableCount >= 2) {
                            // 🔥 关键修复：即使检测到按钮，也要确保内容至少稳定2次
                            // 防止豆包在生成过程中动态显示/隐藏按钮导致过早结束
                            logInfo.sendTaskLog("✅ 检测到操作按钮组且内容已稳定(" + stableCount + "次)，" + aiName + "回复已完成", userId, aiName);
                            isComplete = true;
                        }
                        // 条件1.5: 检测到按钮但内容还未稳定（继续等待）
                        else if (hasActionButtons && stableCount < 2) {
                            // 按钮已出现但内容可能还在变化，继续等待内容稳定
                            if (lastWarningTime == 0 || (System.currentTimeMillis() - lastWarningTime > 10000)) {
                                logInfo.sendTaskLog("⏳ 检测到操作按钮组，等待内容稳定(当前" + stableCount + "次，需要2次)...", userId, aiName);
                                lastWarningTime = System.currentTimeMillis();
                            }
                        }
                        // 条件2: 内容稳定且不再生成（无按钮情况的回退方案）
                        else if (stableCount >= requiredStableCount && !isGenerating) {
                            // 长内容可以更快结束
                            if (contentLength > 1000) {
                                logInfo.sendTaskLog("✅ 长内容已稳定，" + aiName + "回复已完成", userId, aiName);
                                isComplete = true;
                            }
                            else if (contentLength > 500 && stableCount >= 2) {
                                logInfo.sendTaskLog("✅ 内容已稳定，" + aiName + "回复已完成", userId, aiName);
                                isComplete = true;
                            }
                            // 检查内容增长是否已停止
                            else if (isContentGrowthStopped(contentLengthHistory) && stableCount >= requiredStableCount) {
                                logInfo.sendTaskLog("✅ 内容增长已停止，" + aiName + "回复已完成", userId, aiName);
                                isComplete = true;
                            }
                            // 短内容需要更多稳定确认
                            else if (stableCount >= requiredStableCount + 2) {
                                logInfo.sendTaskLog("✅ 短内容已稳定，" + aiName + "回复已完成", userId, aiName);
                                isComplete = true;
                            }
                        }
                        
                        if (isComplete) {
                            break;
                        }
                    } else {
                        // 内容发生变化，重置稳定计数
                        stableCount = 0;
                        lastContent = currentContent;
                    }
                } else {
                    // 内容为空，增加空内容计数
                    emptyCount++;
                    
                    // 🔥 空内容异常检测
                    if (emptyCount > 10 && !hasEverHadContent) {
                        // 检查是否有页面错误
                        try {
                            Object errorResult = page.evaluate("""
                                () => {
                                    const errorElements = document.querySelectorAll('.error-message, [class*="error"]');
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
                                UserLogUtil.sendAIWarningLog(userId, aiName, "页面错误", 
                                    "检测到页面错误：" + errorResult, 
                                    url + "/saveLogInfo");
                            }
                        } catch (Exception ex) {
                            // 错误检测失败，静默处理
                        }
                        
                        // 限制日志频率
                        if (emptyCount % 20 == 0) {
                            UserLogUtil.sendAIWarningLog(userId, aiName, "内容为空", 
                                "长时间未检测到回复内容（" + (emptyCount * checkInterval / 1000) + "秒），继续等待...", 
                                url + "/saveLogInfo");
                        }
                    }
                }
                // 🔥 流式输出支持
                if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
                    if(isRight && rightTextContent != null && !rightTextContent.isEmpty()) {
                        webSocketClientService.sendMessage(userInfoRequest, McpResult.success(rightTextContent, ""), "db-stream");
                    } else if (textContent != null && !textContent.isEmpty()) {
                        webSocketClientService.sendMessage(userInfoRequest, McpResult.success(textContent, ""), "db-stream");
                    }
                }
                
                // 🔥 动态调整检查间隔
                if (elapsedTime > 30000) { // 30秒后逐渐增加间隔
                    checkInterval = Math.min(5000, checkInterval + 200);
                }
                
                page.waitForTimeout(checkInterval);
            }
            
            // 🔥 流式输出结束标志
            if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
                Thread.sleep(2000);  // 确保最后的内容发送完毕
                webSocketClientService.sendMessage(userInfoRequest, McpResult.success("END", ""), "db-stream");
            }
            
            logInfo.sendTaskLog(aiName + "内容已自动提取完成", userId, aiName);

            // 🔥 内容清理
            String finalContent = isRight ? rightCurrentContent : currentContent;
            if (finalContent == null || finalContent.trim().isEmpty()) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "内容提取", 
                    "最终提取的内容为空，可能发生异常", 
                    url + "/saveLogInfo");
                finalContent = "<p>内容提取失败</p>";
            }
            
            // 清理多余内容
            String regex = "<span>\\s*<span[^>]*?>\\d+</span>\\s*</span>";
            finalContent = finalContent.replaceAll(regex, "");
            finalContent = finalContent.replaceAll("撰写任何内容...", "");
            
            // 🔥 记录内容长度统计
            int finalLength = textContent != null ? textContent.trim().length() : 0;
            long totalTime = System.currentTimeMillis() - methodStartTime;
            logInfo.sendTaskLog("📊 提取完成 - 内容长度: " + finalLength + " 字符，耗时: " + (totalTime/1000) + " 秒", userId, aiName);

            return AiResult.success(finalContent, textContent);

        } catch (TimeoutError e) {
            // 记录超时异常
            long totalTime = System.currentTimeMillis() - methodStartTime;
            UserLogUtil.sendAITimeoutLog(userId, aiName, "HTML内容监控", e, 
                "等待内容生成完成超时（耗时: " + (totalTime/1000) + "秒）", 
                url + "/saveLogInfo");
            logInfo.sendTaskLog("❌ 内容提取超时失败", userId, aiName);
            throw e;
        } catch (com.microsoft.playwright.impl.TargetClosedError e) {
            // 页面目标关闭
            long totalTime = System.currentTimeMillis() - methodStartTime;
            UserLogUtil.sendAIWarningLog(userId, aiName, "HTML内容监控", 
                "页面目标已关闭，WebSocket可能断联（耗时: " + (totalTime/1000) + "秒）", 
                url + "/saveLogInfo");
            logInfo.sendTaskLog("❌ 页面已关闭，内容提取中断", userId, aiName);
            throw new RuntimeException("页面目标已关闭", e);
        } catch (RuntimeException e) {
            // 运行时异常（包括页面关闭等）
            long totalTime = System.currentTimeMillis() - methodStartTime;
            if (e.getMessage() != null && e.getMessage().contains("页面已关闭")) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "HTML内容监控", 
                    "页面在监控过程中被关闭（耗时: " + (totalTime/1000) + "秒）", 
                    url + "/saveLogInfo");
                logInfo.sendTaskLog("❌ 页面在监控过程中被关闭", userId, aiName);
            } else {
                UserLogUtil.sendAIExceptionLog(userId, aiName, "waitDBHtmlDom", e, methodStartTime, 
                    "运行时异常：" + e.getMessage(), 
                    url + "/saveLogInfo");
                logInfo.sendTaskLog("❌ 发生异常：" + e.getMessage(), userId, aiName);
            }
            throw e;
        } catch (Exception e) {
            // 记录其他异常
            long totalTime = System.currentTimeMillis() - methodStartTime;
            UserLogUtil.sendAIExceptionLog(userId, aiName, "waitDBHtmlDom", e, methodStartTime, 
                "HTML内容提取失败（耗时: " + (totalTime/1000) + "秒）", 
                url + "/saveLogInfo");
            logInfo.sendTaskLog("❌ 内容提取失败：" + e.getMessage(), userId, aiName);
            throw e;
        }
    }


    /**
     * 排版代码获取（核心监控方法）
     *
     * @param page Playwright页面实例
     */
    public String waitPBCopy(Page page, String userId, String aiName) {
        try {
            // 等待聊天框的内容稳定
            String currentContent = "";
            String lastContent = "";
            // 设置最大等待时间（单位：毫秒），延长到 15 分钟
            long timeout = 900000; // 15 分钟
            long startTime = System.currentTimeMillis();  // 获取当前时间戳
            AtomicReference<String> textRef = new AtomicReference<>();
            // 进入循环，直到内容不再变化或者超时
            while (true) {
                // 获取当前时间戳
                long elapsedTime = System.currentTimeMillis() - startTime;

                // 如果超时，退出循环
                if (elapsedTime > timeout) {
                    break;
                }

                Locator outputLocator = page.locator(".flow-markdown-body").last();
                currentContent = outputLocator.innerHTML();
                // 如果当前内容和上次内容相同，认为 AI 已经完成回答，退出循环
                if (currentContent.equals(lastContent)) {
                    logInfo.sendTaskLog(aiName + "回答完成，正在自动提取内容", userId, aiName);

                    clipboardLockManager.runWithClipboardLock(() -> {
                        try {
                            // 检查页面是否关闭
                            if (page.isClosed()) {
                                UserLogUtil.sendAIWarningLog(userId, aiName, "剪贴板操作", "页面已关闭，无法复制内容", url + "/saveLogInfo");
                                throw new RuntimeException("页面已关闭");
                            }
                            
                            // 获取所有复制按钮的 SVG 元素（通过 xlink:href 属性定位）
                            boolean buttonFound = false;
                            if (page.locator("[data-testid='code-block-copy']").count() > 0) {
                                page.locator("[data-testid='code-block-copy']").last()  // 获取最后一个复制按钮
                                        .click();
                                buttonFound = true;
                            } else if (page.locator("[data-testid='message_action_copy']").count() > 0) {
                                page.locator("[data-testid='message_action_copy']").last()  // 获取最后一个复制按钮
                                        .click();
                                buttonFound = true;
                            }
                            
                            if (!buttonFound) {
                                UserLogUtil.sendAIWarningLog(userId, aiName, "剪贴板操作", "未找到复制按钮，元素可能不存在", url + "/saveLogInfo");
                                throw new RuntimeException("未找到复制按钮");
                            }

                            String text = (String) page.evaluate("navigator.clipboard.readText()");
                            if (text == null || text.trim().isEmpty()) {
                                UserLogUtil.sendAIWarningLog(userId, aiName, "剪贴板操作", "剪贴板读取内容为空", url + "/saveLogInfo");
                            }
                            textRef.set(text);
                        } catch (com.microsoft.playwright.PlaywrightException e) {
                            // JavaScript执行错误
                            if (e.getMessage().contains("evaluate")) {
                                UserLogUtil.sendAIWarningLog(userId, aiName, "剪贴板操作", "JavaScript执行失败：剪贴板读取失败 - " + e.getMessage(), url + "/saveLogInfo");
                            } else {
                                UserLogUtil.sendAIWarningLog(userId, aiName, "剪贴板操作", "复制按钮点击失败：" + e.getMessage(), url + "/saveLogInfo");
                            }
                            e.printStackTrace();
                        } catch (Exception e) {
                            // 记录剪贴板操作异常
                            UserLogUtil.sendAIBusinessLog(userId, aiName, "剪贴板操作", "复制内容到剪贴板失败：" + e.getMessage(), System.currentTimeMillis(), url + "/saveLogInfo");
                            e.printStackTrace();
                        }
                    });
                    break;
                }
                // 更新上次内容为当前内容
                lastContent = currentContent;
                page.waitForTimeout(10000);  // 等待10秒再次检查
            }
            logInfo.sendTaskLog(aiName + "内容已自动提取完成", userId, aiName);

            currentContent = textRef.get();

            // 记录成功日志
            // 不再记录成功日志，按照用户要求
            return currentContent;

        } catch (TimeoutError e) {
            // 记录超时异常
            UserLogUtil.sendAITimeoutLog(userId, aiName, "排版代码提取", e, "等待代码生成完成", url + "/saveLogInfo");
            throw e;
        } catch (Exception e) {
            // 记录其他异常
            UserLogUtil.sendAIExceptionLog(userId, aiName, "waitPBCopy", e, System.currentTimeMillis(), "排版代码提取失败", url + "/saveLogInfo");
            throw e;
        }
    }


}
