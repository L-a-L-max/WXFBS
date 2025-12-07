package com.playwright.utils.ai;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.playwright.entity.AiResult;
import com.playwright.entity.UserInfoRequest;
import com.playwright.entity.mcp.McpResult;
import com.playwright.utils.common.AiResultHelper;
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
    
    @Autowired
    private AiResultHelper aiResultHelper;

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
     * 🔥 增强版：支持多种场景的"试一试"按钮检测
     * 适用场景：登录检测、扫码登录、开始咨询等各个阶段
     *
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param scenario 场景描述（用于日志记录）
     */
    public void checkAndClickSuperModeButton(Page page, String userId, String scenario) {
        try {
            // 等待页面稳定
            page.waitForTimeout(1500);

            // 🔥 优化：检测到"试一试"弹窗时，点击关闭按钮而不是点击"试一试"
            // 根据用户提供的DOM结构，关闭按钮是：<span role="img" class="semi-icon semi-icon-default close-btn-GElrfj">

            // 首先检测是否存在超能模式介绍弹窗
            Locator modalContainer = page.locator("div.intro-LoENFx");
            Locator modalTitle = page.locator("div:has-text(\"向你介绍超能模式\")");

            boolean hasModal = modalContainer.count() > 0 || modalTitle.count() > 0;

            if (!hasModal) {
                // 如果没有检测到弹窗，直接返回
                return;
            }

            System.out.println("🔍 [豆包超能模式] 检测到超能模式介绍弹窗 [" + scenario + "]");

            // 🔥 修复：使用正确的关闭按钮定位方式
            // 方案1：通过精确的class和role属性定位关闭按钮（最准确）
            Locator closeButtonExact = page.locator("span[role=\"img\"].close-btn-GElrfj");

            // 方案2：通过父容器定位关闭按钮（备用方案1）
            Locator closeButtonInModal = page.locator("div.intro-LoENFx span.close-btn-GElrfj");

            // 方案3：通过SVG路径定位关闭按钮（备用方案2）
            Locator closeButtonBySvg = page.locator("div.intro-LoENFx svg path[d*=\"20.307\"]").first();

            // 方案4：通过完整的DOM结构定位（最后备用方案）
            Locator closeButtonByStructure = page.locator("div:has-text(\"向你介绍超能模式\") span.semi-icon.close-btn-GElrfj");

            boolean buttonClicked = false;

            // 优先使用最精确的方案
            if (closeButtonExact.count() > 0 && closeButtonExact.isVisible()) {
                System.out.println("✅ [豆包超能模式] 使用精确定位点击关闭按钮 [" + scenario + "]");
                closeButtonExact.click();
                buttonClicked = true;
            }
            // 备用方案1：通过父容器定位
            else if (closeButtonInModal.count() > 0 && closeButtonInModal.isVisible()) {
                System.out.println("✅ [豆包超能模式] 使用父容器定位点击关闭按钮 [" + scenario + "]");
                closeButtonInModal.click();
                buttonClicked = true;
            }
            // 备用方案2：通过SVG路径定位
            else if (closeButtonBySvg.count() > 0 && closeButtonBySvg.isVisible()) {
                System.out.println("✅ [豆包超能模式] 使用SVG路径定位点击关闭按钮 [" + scenario + "]");
                closeButtonBySvg.click();
                buttonClicked = true;
            }
            // 备用方案3：通过完整结构定位
            else if (closeButtonByStructure.count() > 0 && closeButtonByStructure.isVisible()) {
                System.out.println("✅ [豆包超能模式] 使用结构定位点击关闭按钮 [" + scenario + "]");
                closeButtonByStructure.click();
                buttonClicked = true;
            }

            if (buttonClicked) {
                // 等待点击完成和页面响应
                page.waitForTimeout(2000);
                System.out.println("✅ [豆包超能模式] 关闭按钮点击成功，弹窗已关闭 [" + scenario + "]");

                // 🔥 验证弹窗是否真的关闭了
                page.waitForTimeout(1000);
                boolean modalStillExists = page.locator("div.intro-LoENFx").count() > 0;
                if (modalStillExists) {
                    System.out.println("⚠️ [豆包超能模式] 弹窗仍然存在，尝试其他方式关闭 [" + scenario + "]");
                    // 尝试按ESC键关闭
                    page.keyboard().press("Escape");
                    page.waitForTimeout(1000);
                } else {
                    System.out.println("✅ [豆包超能模式] 弹窗已完全关闭 [" + scenario + "]");
                }
            } else {
                System.out.println("❌ [豆包超能模式] 未找到可点击的关闭按钮 [" + scenario + "]");
                // 尝试按ESC键关闭弹窗
                System.out.println("🔄 [豆包超能模式] 尝试使用ESC键关闭弹窗 [" + scenario + "]");
                page.keyboard().press("Escape");
                page.waitForTimeout(1000);
            }

        } catch (Exception e) {
            // 如果按钮不存在或点击失败，记录但不抛出异常，不影响后续流程
            System.err.println("❌ [豆包超能模式] 关闭按钮检测或点击失败 [" + scenario + "]: " + e.getMessage());
            UserLogUtil.sendElementWarningLog(userId, "豆包", "超能模式检测[" + scenario + "]", ".close-btn-GElrfj", "关闭按钮检测或点击失败：" + e.getMessage(), url + "/saveLogInfo");
        }
    }

    /**
     * 兼容性方法：保持原有调用方式
     */
    public void checkAndClickSuperModeButton(Page page, String userId) {
        checkAndClickSuperModeButton(page, userId, "默认场景");
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
            
            // 🔥 修复：普通用户的深度思考按钮使用不同的选择器
            // 普通用户深度思考按钮：data-testid="use-deep-thinking-switch-btn"
            Locator deepThinkingButton = page.locator("[data-testid='use-deep-thinking-switch-btn']");
            // 超能用户的模式切换按钮
            Locator speedModeButton = page.locator(".switch-button-iHFX3k:has-text(\"极速\"), .switch-button-qHPwBT:has-text(\"极速\")").first();
            Locator thinkModeButton = page.locator(".switch-button-iHFX3k:has-text(\"思考\"), .switch-button-qHPwBT:has-text(\"思考\")").first();
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
                        try {
                            thinkModeButton.scrollIntoViewIfNeeded();
                            Thread.sleep(300);
                            thinkModeButton.click();
                            page.waitForTimeout(500);
                            logInfo.sendTaskLog("✓ 已启用思考模式", userId, "豆包");
                        } catch (Exception e) {
                            logInfo.sendTaskLog("⚠️ 思考模式点击失败，尝试备选方案：" + e.getMessage(), userId, "豆包");
                        }
                    } else if (thinkActive) {
                        logInfo.sendTaskLog("✓ 思考模式已启用（无需切换）", userId, "豆包");
                    } else {
                        logInfo.sendTaskLog("⚠️ 思考模式按钮不可用", userId, "豆包");
                    }
                } else {
                    // 不需要深度思考：必须使用极速模式，永不使用超能模式
                    boolean superActive = isModeActive(superModeButton);
                    boolean speedActive = speedModeButton.count() > 0 && isModeActive(speedModeButton);
                    
                    if (superActive) {
                        // 当前是超能模式，必须切换到极速模式
                        logInfo.sendTaskLog("当前为超能模式，但任务无需深度思考，正在切换到极速模式", userId, "豆包");
                        if (speedModeButton.count() > 0) {
                            try {
                                speedModeButton.scrollIntoViewIfNeeded();
                                Thread.sleep(300);
                                speedModeButton.click();
                                page.waitForTimeout(500);
                                logInfo.sendTaskLog("✓ 已从超能模式切换到极速模式", userId, "豆包");
                            } catch (Exception e) {
                                logInfo.sendTaskLog("⚠️ 极速模式切换失败：" + e.getMessage(), userId, "豆包");
                            }
                        }
                    } else if (!speedActive && speedModeButton.count() > 0) {
                        // 既不是超能也不是极速，切换到极速
                        logInfo.sendTaskLog("正在切换到极速模式", userId, "豆包");
                        try {
                            speedModeButton.scrollIntoViewIfNeeded();
                            Thread.sleep(300);
                            speedModeButton.click();
                            page.waitForTimeout(500);
                            logInfo.sendTaskLog("✓ 已启用极速模式", userId, "豆包");
                        } catch (Exception e) {
                            logInfo.sendTaskLog("⚠️ 极速模式启用失败：" + e.getMessage(), userId, "豆包");
                        }
                    } else {
                        logInfo.sendTaskLog("✓ 极速模式已启用（无需切换）", userId, "豆包");
                    }
                }
            } else {
                // ========== 普通用户（无超能权限）==========
                logInfo.sendTaskLog("当前为普通用户，使用深度思考按钮", userId, "豆包");
                
                if (needDeepThinking) {
                    // 🔥 修复：普通用户使用深度思考按钮，检查是否已激活
                    if (deepThinkingButton.count() > 0) {
                        // 检查深度思考按钮是否已激活 (data-checked="true")
                        String isChecked = deepThinkingButton.getAttribute("data-checked");
                        if (!"true".equals(isChecked)) {
                            logInfo.sendTaskLog("任务需要深度思考，正在启用深度思考按钮", userId, "豆包");
                            deepThinkingButton.scrollIntoViewIfNeeded();
                            deepThinkingButton.click();
                            page.waitForTimeout(500);
                            logInfo.sendTaskLog("✓ 已启用深度思考模式", userId, "豆包");
                        } else {
                            logInfo.sendTaskLog("✓ 深度思考模式已启用（无需切换）", userId, "豆包");
                        }
                    } else {
                        logInfo.sendTaskLog("⚠️ 未找到深度思考按钮", userId, "豆包");
                    }
                } else {
                    // 不需要深度思考：确保深度思考按钮未激活
                    if (deepThinkingButton.count() > 0) {
                        String isChecked = deepThinkingButton.getAttribute("data-checked");
                        if ("true".equals(isChecked)) {
                            logInfo.sendTaskLog("任务无需深度思考，正在关闭深度思考按钮", userId, "豆包");
                            deepThinkingButton.scrollIntoViewIfNeeded();
                            deepThinkingButton.click();
                            page.waitForTimeout(500);
                            logInfo.sendTaskLog("✓ 已关闭深度思考模式", userId, "豆包");
                        } else {
                            logInfo.sendTaskLog("✓ 深度思考模式已关闭（无需切换）", userId, "豆包");
                        }
                    } else {
                        logInfo.sendTaskLog("✓ 普通模式（未找到深度思考按钮）", userId, "豆包");
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

            page.waitForSelector("[data-testid='message_action_copy']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(1800000));  // 延长到1800秒（30分钟）
            logInfo.sendTaskLog("豆包回答完成，正在自动提取内容", userId, "豆包");
            
            // 点击复制按钮
            // 🔒 使用剪贴板锁保护剪贴板操作
            AtomicReference<String> copiedTextRef = new AtomicReference<>();
            
            clipboardLockManager.runWithClipboardLock(() -> {
                try {
                    // 🔥 确保页面获得焦点（剪贴板操作必需）
                    page.bringToFront();
                    Thread.sleep(300);  // 延长到300ms，确保焦点切换完成
                    
            // 🔥 确保定位到 AI 回答消息的复制按钮，而不是用户提问的复制按钮
            Locator aiMessage = page.locator("[data-testid='receive_message']").last();
            if (aiMessage.count() == 0) {
                UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "未找到AI回答消息", url + "/saveLogInfo");
                throw new RuntimeException("未找到AI回答消息");
            }
            
            Locator copyButton = aiMessage.locator("[data-testid='message_action_copy']").first();
            if (copyButton.count() == 0) {
                UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "未找到复制按钮", url + "/saveLogInfo");
                throw new RuntimeException("未找到复制按钮");
            }
            
            try {
                // 🔥 使用 JavaScript 点击以避免元素被遮挡的问题
                page.evaluate("document.querySelectorAll('[data-testid=\"receive_message\"]')[document.querySelectorAll('[data-testid=\"receive_message\"]').length - 1].querySelector('[data-testid=\"message_action_copy\"]').click()");
            } catch (Exception e) {
                // 如果 JavaScript 点击失败，尝试强制点击
                try {
                    copyButton.click(new Locator.ClickOptions().setForce(true));
                } catch (Exception e2) {
                    UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "复制按钮不可点击：" + e2.getMessage(), url + "/saveLogInfo");
                    throw e2;
                }
            }
            
            Thread.sleep(3000);  // 延长到3秒，确保复制完成
            
            // 读取剪贴板
            try {
                        String text = (String) page.evaluate("navigator.clipboard.readText()");
                        copiedTextRef.set(text);
                        if (text == null || text.trim().isEmpty()) {
                    UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "剪贴板读取内容为空", url + "/saveLogInfo");
                        }
            } catch (Exception e) {
                UserLogUtil.sendAIWarningLog(userId, "豆包", "内容复制", "JavaScript执行失败：剪贴板读取失败 - " + e.getMessage(), url + "/saveLogInfo");
                throw e;
            }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            
            copiedText = copiedTextRef.get();
            
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
            // 🔥 关键修复：检测豆包的"中断生成"按钮
            // 当AI正在生成时，会出现一个带有 data-testid="chat_input_local_break_button" 的按钮
            // 这是一个圆形按钮，里面有一个方块图标，用于中断生成
            Locator breakButton = page.locator("[data-testid='chat_input_local_break_button']");
            
            int buttonCount = breakButton.count();
            
            // 如果中断按钮存在且可见，说明AI正在生成
            if (buttonCount > 0) {
                try {
                    boolean isVisible = breakButton.first().isVisible();
                    if (isVisible) {
                        return true;
                    }
                } catch (Exception e) {
                    // 按钮存在但检测可见性失败，保守判断为正在生成
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            System.out.println("❌ [DouBao] checkDouBaoGenerating异常: " + e.getMessage());
            // 检测失败，保守判断为未在生成（避免永远卡住）
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
                        
                        // 🔥 优先提取 [data-testid="message_text_content"]（不含思考内容的纯回答）
                        let markdownElement = latestMessage.querySelector('[data-testid="message_text_content"]');
                        let isTextContent = true;
                        
                        // 如果没有找到，回退到 .flow-markdown-body
                        if (!markdownElement) {
                            markdownElement = latestMessage.querySelector('.flow-markdown-body');
                            isTextContent = false;
                        }
                        
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
                                source: isTextContent ? 'message_text_content_only' : 'receive_message',
                                timestamp: Date.now()
                            };
                        }
                    }
                    
                    // 🔥 方案2：回退到直接查找最新的回复内容
                    // 2.1 优先查找所有 [data-testid="message_text_content"]（只含正文，不含思考）
                    const textContentElements = document.querySelectorAll('[data-testid="message_text_content"]');
                    if (textContentElements.length > 0) {
                        const latestTextContent = textContentElements[textContentElements.length - 1];
                        
                        // 检查是否在用户消息中（排除用户输入）
                        const isInUserMessage = latestTextContent.closest('[data-testid="send_message"]') !== null;
                        if (!isInUserMessage) {
                            const contentClone = latestTextContent.cloneNode(true);
                            const elementsToRemove = contentClone.querySelectorAll(
                                'svg, button, [role="button"], ' +
                                '[class*="loading"], [class*="typing"], [class*="cursor"]'
                            );
                            elementsToRemove.forEach(el => el.remove());
                            
                            const textContent = contentClone.textContent || '';
                            const contentLength = textContent.trim().length;
                            
                            // 检查附近是否有可见的操作按钮组
                            const parentContainer = latestTextContent.closest('[data-testid="message-block-container"]');
                            let hasActionButtons = false;
                            
                            if (parentContainer) {
                                const copyBtn = parentContainer.querySelector('[data-testid="message_action_copy"]');
                                const regenBtn = parentContainer.querySelector('[data-testid="message_action_regenerate"]');
                                const isCopyVisible = copyBtn && copyBtn.offsetParent !== null;
                                const isRegenVisible = regenBtn && regenBtn.offsetParent !== null;
                                hasActionButtons = isCopyVisible && isRegenVisible;
                                if (!hasActionButtons) {
                                    hasActionButtons = copyBtn && copyBtn.offsetParent !== null;
                                }
                            }
                            
                            return {
                                content: contentClone.innerHTML,
                                textContent: textContent,
                                length: contentLength,
                                hasActionButtons: hasActionButtons,
                                source: 'message_text_content_fallback',
                                timestamp: Date.now()
                            };
                        }
                    }
                    
                    // 2.2 如果没有 message_text_content，回退到 .flow-markdown-body
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
     * 🔥 重大优化：统一使用复制按钮获取内容，不再从DOM提取
     *
     * @param page Playwright页面实例
     */
    public AiResult waitDBHtmlDom(Page page, String userId, String aiName, UserInfoRequest userInfoRequest) throws InterruptedException {
        long methodStartTime = System.currentTimeMillis();
        try {
            // 检查页面是否关闭
            if (page.isClosed()) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "内容获取", "页面已关闭，无法获取内容", url + "/saveLogInfo");
                throw new RuntimeException("页面已关闭");
            }
            
            // 🔥 日志标识：使用复制按钮获取内容
            logInfo.sendTaskLog("📋 豆包内容获取方式：复制按钮（唯一方式，不再从DOM提取）", userId, aiName);
            
            // 设置最大等待时间（单位：毫秒），延长到 30 分钟以适应超能模式深度思考
            long timeout = 1800000; // 30 分钟
            long startTime = System.currentTimeMillis();  // 获取当前时间戳
            
            // ========== 阶段1：等待AI开始回复（检测新消息出现） ==========
            logInfo.sendTaskLog("等待AI开始生成新回复...", userId, aiName);
            int initialMessageCount = page.locator("[data-testid='receive_message']").count();
            logInfo.sendTaskLog("当前历史消息数量：" + initialMessageCount, userId, aiName);
            
            boolean aiStartedReply = false;
            while (!aiStartedReply) {
                if (page.isClosed()) {
                    UserLogUtil.sendAIWarningLog(userId, aiName, "等待AI回复", "页面已关闭", url + "/saveLogInfo");
                    throw new RuntimeException("页面已关闭");
                }
                
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > timeout) {
                    TimeoutException timeoutEx = new TimeoutException("等待AI开始回复超时，已等待：" + (elapsedTime/1000) + "秒");
                    UserLogUtil.sendAITimeoutLog(userId, aiName, "等待AI开始回复", timeoutEx, "AI未开始回复", url + "/saveLogInfo");
                    throw new RuntimeException("超时：AI未开始回复");
                }
                
                // 检测是否有新消息出现
                int currentMessageCount = page.locator("[data-testid='receive_message']").count();
                if (currentMessageCount > initialMessageCount) {
                    logInfo.sendTaskLog("✅ 检测到AI开始回复（消息数：" + initialMessageCount + " → " + currentMessageCount + "）", userId, aiName);
                    aiStartedReply = true;
                    break;
                }
                
                // 检测AI错误标识
                try {
                    Locator errorIcon = page.locator("[data-testid='message_box_failed_icon']").last();
                    if (errorIcon.count() > 0) {
                        logInfo.sendTaskLog("❌ 检测到AI错误标识", userId, aiName);
                        return AiResult.success("<p>AI拒绝处理或遇到错误</p>", "AI拒绝处理或遇到错误");
                    }
                } catch (Exception e) {
                    // 忽略
                }
                
                page.waitForTimeout(2000);  // 每2秒检查一次
            }
            
            // ========== 阶段2：等待AI生成完成（检测生成状态变化） ==========
            logInfo.sendTaskLog("开始监听" + aiName + "生成状态...", userId, aiName);
            
            boolean hasDetectedGenerating = false;  // 是否检测到过生成中状态
            int stableCount = 0;  // 稳定计数（连续多次检测到完成状态）
            int lastContentLength = 0;  // 上次内容长度
            
            while (true) {
                if (page.isClosed()) {
                    UserLogUtil.sendAIWarningLog(userId, aiName, "内容获取", "页面在监控过程中被关闭", url + "/saveLogInfo");
                    throw new RuntimeException("页面在监控过程中被关闭");
                }
                
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > timeout) {
                    TimeoutException timeoutEx = new TimeoutException("等待豆包生成完成超时，已等待：" + (elapsedTime/1000) + "秒");
                    UserLogUtil.sendAITimeoutLog(userId, aiName, "等待生成完成", timeoutEx, "AI生成未完成", url + "/saveLogInfo");
                    logInfo.sendTaskLog("❌ 等待生成完成超时", userId, aiName);
                    throw new RuntimeException("超时未完成生成");
                }

                // 检测AI错误标识
                try {
                    Locator errorIcon = page.locator("[data-testid='message_box_failed_icon']").last();
                    if (errorIcon.count() > 0) {
                        logInfo.sendTaskLog("❌ 检测到AI错误标识", userId, aiName);
                        return AiResult.success("<p>AI拒绝处理或遇到错误</p>", "AI拒绝处理或遇到错误");
                    }
                } catch (Exception e) {
                    // 忽略
                }

                // 检测生成状态
                boolean isCurrentlyGenerating = checkDouBaoGenerating(page);
                logInfo.sendTaskLog(String.format("🔍 检测生成状态: %s (已检测到生成: %s, 稳定次数: %d)", 
                    isCurrentlyGenerating ? "正在生成" : "未生成", hasDetectedGenerating, stableCount), userId, aiName);
                
                if (isCurrentlyGenerating) {
                    // 检测到正在生成
                    if (!hasDetectedGenerating) {
                        logInfo.sendTaskLog("🔄 检测到AI正在生成中...", userId, aiName);
                        hasDetectedGenerating = true;
                    }
                    stableCount = 0;  // 重置稳定计数
                } else {
                    // 没有检测到生成中状态
                    if (!hasDetectedGenerating) {
                        // 如果从未检测到生成状态，可能是思考阶段或者刚开始
                        // 继续等待
                        logInfo.sendTaskLog("⏳ 等待AI开始生成...", userId, aiName);
                    } else {
                        // 曾经检测到生成，现在停止了
                        // 需要进一步验证：检测复制按钮 + 内容稳定性 + 发送按钮状态
                        
                        // 🔥 保底判断：检测发送按钮状态（从中止按钮变回灰色发送按钮）
                        boolean sendButtonDisabled = false;
                        try {
                            Locator sendButton = page.locator("[data-testid='chat_input_send_button']");
                            if (sendButton.count() > 0) {
                                // 检查按钮是否被禁用（灰色发送按钮状态）
                                String disabledAttr = sendButton.getAttribute("aria-disabled");
                                sendButtonDisabled = "true".equals(disabledAttr);
                            }
                        } catch (Exception e) {
                            // 忽略
                        }
                        
                        // 1. 检测复制按钮
                        boolean hasCopyButton = false;
                        try {
                            Locator copyButton = page.locator("[data-testid='message_action_copy']").last();
                            hasCopyButton = copyButton.count() > 0 && copyButton.isVisible();
                        } catch (Exception e) {
                            // 忽略
                        }
                        
                        // 2. 检测内容长度稳定性
                        int currentContentLength = 0;
                        try {
                            java.util.Map<String, Object> responseData = getLatestDouBaoResponseWithCompletion(page);
                            currentContentLength = (int) responseData.getOrDefault("length", 0);
                        } catch (Exception e) {
                            // 忽略
                        }
                        
                        boolean contentStable = (currentContentLength > 0 && currentContentLength == lastContentLength);
                        lastContentLength = currentContentLength;
                        
                        logInfo.sendTaskLog(String.format("📊 验证完成状态 - 发送按钮: %s, 复制按钮: %s, 内容长度: %d, 内容稳定: %s", 
                            sendButtonDisabled ? "已禁用" : "可用", hasCopyButton ? "存在" : "不存在", currentContentLength, contentStable ? "是" : "否"), userId, aiName);
                        
                        // 判断是否真正完成
                        if (hasCopyButton && contentStable) {
                            stableCount++;
                            logInfo.sendTaskLog(String.format("✅ 完成验证通过 (稳定次数: %d/1)", stableCount), userId, aiName);
                            if (stableCount >= 1) {
                                // 检测到完成状态（复制按钮存在 + 内容稳定）
                                logInfo.sendTaskLog("✅ AI生成已完成（复制按钮出现 + 内容稳定）", userId, aiName);
                                break;
                            }
                        } else if (hasCopyButton && currentContentLength > 0) {
                            // 即使内容不稳定，但复制按钮存在且有内容，也认为可能完成
                            stableCount++;
                            logInfo.sendTaskLog(String.format("⚠️ 复制按钮存在但内容未稳定 (尝试次数: %d/1)", stableCount), userId, aiName);
                            if (stableCount >= 1) {
                                logInfo.sendTaskLog("✅ AI生成已完成（复制按钮出现，内容长度: " + currentContentLength + "）", userId, aiName);
                                break;
                            }
                        } else if (sendButtonDisabled && currentContentLength > 0) {
                            // 🔥 保底条件：发送按钮已禁用（从中止变回灰色）且有内容，说明生成完成
                            stableCount++;
                            logInfo.sendTaskLog(String.format("🛡️ 保底判断：发送按钮已禁用，内容长度: %d (尝试次数: %d/1)", currentContentLength, stableCount), userId, aiName);
                            if (stableCount >= 1) {
                                logInfo.sendTaskLog("✅ AI生成已完成（发送按钮状态确认）", userId, aiName);
                                break;
                            }
                        } else {
                            stableCount = 0;  // 重置稳定计数
                        }
                    }
                }
                
                // 流式输出支持
                if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
                    try {
                        java.util.Map<String, Object> streamData = getLatestDouBaoResponseWithCompletion(page);
                        String streamText = (String) streamData.getOrDefault("textContent", "");
                        if (streamText != null && !streamText.trim().isEmpty()) {
                            webSocketClientService.sendMessage(userInfoRequest, McpResult.success(streamText, ""), "db-stream");
                        }
                    } catch (Exception e) {
                        // 流式输出失败不影响主流程
                    }
                }
                
                page.waitForTimeout(3000);  // 每3秒检查一次
            }
            
            // 🔥 流式输出结束标志
            if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
                Thread.sleep(3000);  // 延长到3秒，确保流式输出完整
                webSocketClientService.sendMessage(userInfoRequest, McpResult.success("END", ""), "db-stream");
            }
            
            // 🔥🔥🔥 通过复制按钮获取内容（唯一方式，不再从DOM提取）
            logInfo.sendTaskLog("📋 正在通过复制按钮获取内容...", userId, aiName);
            
            AtomicReference<String> finalContentRef = new AtomicReference<>();
            
            clipboardLockManager.runWithClipboardLock(() -> {
                try {
                    // 🔥 确保页面获得焦点（剪贴板操作必需）
                    page.bringToFront();
                    Thread.sleep(300);  // 延长到300ms，确保焦点切换完成
                    
                    // 1. 清空剪贴板
                    page.evaluate("navigator.clipboard.writeText('')");
                    Thread.sleep(500);  // 延长到500ms，确保剪贴板清空完成
                    
                    // 2. 检测是否存在文本编辑框（写作助手面板）
                    Locator writeCanvasPanel = page.locator("div[data-testid='write_canvas_panel']");
                    if (writeCanvasPanel.count() > 0) {
                        logInfo.sendTaskLog("检测到文本编辑框，使用编辑框内的复制按钮", userId, aiName);
                        
                        // 点击编辑框内的复制按钮
                        Locator editorCopyButton = page.locator("div[data-testid='container_inner_copy_btn']");
                        if (editorCopyButton.count() > 0) {
                            editorCopyButton.click();
                            logInfo.sendTaskLog("已点击编辑框复制按钮", userId, aiName);
                            Thread.sleep(1500);
                        }
                        
                        // 点击关闭按钮
                        Locator closeButton = page.locator("div[data-testid='container_inner_close_btn']");
                        if (closeButton.count() > 0) {
                            closeButton.click();
                            logInfo.sendTaskLog("已点击关闭按钮", userId, aiName);
                            Thread.sleep(800);
                        }
                    } else {
                        // 使用常规方式：点击AI回答消息的复制按钮
                        Locator aiMessage = page.locator("[data-testid='receive_message']").last();
                        if (aiMessage.count() == 0) {
                            throw new RuntimeException("未找到AI回答消息");
                        }
                        
                        Locator copyButton = aiMessage.locator("[data-testid='message_action_copy']").first();
                        if (copyButton.count() == 0) {
                            throw new RuntimeException("未找到复制按钮");
                        }
                        
                        // 🔥 使用 JavaScript 点击以避免元素被遮挡的问题
                        try {
                            page.evaluate("document.querySelectorAll('[data-testid=\"receive_message\"]')[document.querySelectorAll('[data-testid=\"receive_message\"]').length - 1].querySelector('[data-testid=\"message_action_copy\"]').click()");
                        } catch (Exception e) {
                            // 如果 JavaScript 点击失败，尝试强制点击
                            copyButton.click(new Locator.ClickOptions().setForce(true));
                        }
                        logInfo.sendTaskLog("已点击复制按钮，等待剪贴板内容...", userId, aiName);
                        Thread.sleep(2000); // 延长到2秒，确保复制完成
                    }
                    
                    // 3. 从剪贴板读取
                    Object clipboardContent = page.evaluate("navigator.clipboard.readText()");
                    String copiedText = clipboardContent != null ? clipboardContent.toString() : "";
                    
                    if (copiedText == null || copiedText.trim().isEmpty()) {
                        throw new RuntimeException("剪贴板内容为空");
                    }
                    
                    finalContentRef.set(copiedText);
                    logInfo.sendTaskLog("✅ 成功从复制按钮获取内容，长度：" + copiedText.trim().length(), userId, aiName);
                    
                } catch (Exception e) {
                    logInfo.sendTaskLog("❌ 复制按钮获取失败：" + e.getMessage(), userId, aiName);
                    throw new RuntimeException("复制按钮获取失败: " + e.getMessage(), e);
                }
            });
            
            String finalContent = finalContentRef.get();
            
            if (finalContent == null || finalContent.trim().isEmpty()) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "内容获取", 
                    "❌ 无法通过复制按钮获取内容", url + "/saveLogInfo");
                throw new RuntimeException("内容获取失败");
            }
            
            // 🔥 检测是否包含AI思考过程内容
            if (detectThinkingContent(finalContent)) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "内容检测", 
                    "⚠️ 检测到可能包含AI思考过程的内容，建议检查是否为最终答案。" +
                    "\n提示：如果内容以\"让我\"、\"首先\"、\"接下来\"等开头，可能是思考过程而非最终答案。" +
                    "\n💡 解决方案：请重新生成或手动编辑内容。", 
                    url + "/saveLogInfo");
                logInfo.sendTaskLog("⚠️ 内容包含疑似思考过程，请检查", userId, aiName);
            }
            
            long totalTime = System.currentTimeMillis() - methodStartTime;
            logInfo.sendTaskLog("📊 提取完成 - 内容长度: " + finalContent.length() + " 字符，耗时: " + (totalTime/1000) + " 秒", userId, aiName);

            // 将纯文本转换为HTML格式
            String htmlContent = convertTextToHtml(finalContent);
            
            return AiResult.success(htmlContent, finalContent);

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
            // 设置最大等待时间（单位：毫秒），延长到 30 分钟以适应超能模式深度思考
            long timeout = 1800000; // 30 分钟
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
                            
                            // 🔥 确保页面获得焦点（剪贴板操作必需）
                            page.bringToFront();
                            Thread.sleep(300);  // 延长到300ms，确保焦点切换完成
                            
                            // 获取AI回答消息的复制按钮
                            boolean buttonFound = false;
                            
                            // 🔥 先尝试定位AI回答消息
                            Locator aiMessage = page.locator("[data-testid='receive_message']").last();
                            
                            // 🔥 使用 JavaScript 点击以避免元素被遮挡的问题
                            if (aiMessage.count() > 0 && aiMessage.locator("[data-testid='code-block-copy']").count() > 0) {
                                try {
                                    page.evaluate("document.querySelectorAll('[data-testid=\"receive_message\"]')[document.querySelectorAll('[data-testid=\"receive_message\"]').length - 1].querySelector('[data-testid=\"code-block-copy\"]').click()");
                                    buttonFound = true;
                                } catch (Exception e) {
                                    // 如果 JavaScript 点击失败，尝试强制点击
                                    aiMessage.locator("[data-testid='code-block-copy']").first()
                                            .click(new Locator.ClickOptions().setForce(true));
                                    buttonFound = true;
                                }
                            } else if (aiMessage.count() > 0 && aiMessage.locator("[data-testid='message_action_copy']").count() > 0) {
                                try {
                                    page.evaluate("document.querySelectorAll('[data-testid=\"receive_message\"]')[document.querySelectorAll('[data-testid=\"receive_message\"]').length - 1].querySelector('[data-testid=\"message_action_copy\"]').click()");
                                    buttonFound = true;
                                } catch (Exception e) {
                                    // 如果 JavaScript 点击失败，尝试强制点击
                                    aiMessage.locator("[data-testid='message_action_copy']").first()
                                            .click(new Locator.ClickOptions().setForce(true));
                                    buttonFound = true;
                                }
                            }
                            
                            if (!buttonFound) {
                                UserLogUtil.sendAIWarningLog(userId, aiName, "剪贴板操作", "未找到复制按钮，元素可能不存在", url + "/saveLogInfo");
                                throw new RuntimeException("未找到复制按钮");
                            }
                            
                            // 延长等待时间，确保复制完成
                            Thread.sleep(2000);

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

    /**
     * 将纯文本转换为HTML格式
     * 简单地将文本内容包裹在<p>标签中，保留换行符
     * 
     * @param text 纯文本内容
     * @return HTML格式内容
     */
    private String convertTextToHtml(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "<p></p>";
        }
        
        // 将文本按行分割，每行包裹在<p>标签中
        String[] lines = text.split("\\n");
        StringBuilder html = new StringBuilder();
        
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                html.append("<p><br></p>");
            } else {
                // 转义HTML特殊字符
                String escapedLine = line
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
                html.append("<p>").append(escapedLine).append("</p>");
            }
        }
        
        return html.toString();
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
        
        String text = content.trim();
        
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
