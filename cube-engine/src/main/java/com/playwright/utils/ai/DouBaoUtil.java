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
                    // 需要深度思考：使用超能模式
                    boolean superActive = isModeActive(superModeButton);
                    
                    if (!superActive) {
                        // 超能模式未激活，需要切换
                        logInfo.sendTaskLog("任务需要深度思考，正在切换到超能模式", userId, "豆包");
                        superModeButton.click();
                        page.waitForTimeout(500);
                        logInfo.sendTaskLog("✓ 已启用超能模式", userId, "豆包");
                    } else {
                        logInfo.sendTaskLog("✓ 超能模式已启用（无需切换）", userId, "豆包");
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
     * html片段获取（核心监控方法）
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
            
            // 等待聊天框的内容稳定
            String currentContent = "";
            String lastContent = "";
            String rightCurrentContent = "";
            String rightLastContent = "";
            String textContent = "";
            String rightTextContent = "";
            boolean isRight = false;
            // 设置最大等待时间（单位：毫秒），延长到 15 分钟以适应深度思考模式
            long timeout = 900000; // 15 分钟
            long startTime = System.currentTimeMillis();  // 获取当前时间戳
            
            // 用于去重警告日志的计数器
            int warningCount = 0;
            long lastWarningTime = 0;

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
                    UserLogUtil.sendAITimeoutLog(userId, aiName, "HTML内容监控", timeoutEx, "等待.flow-markdown-body元素内容稳定", url + "/saveLogInfo");
                    break;
                }
                // 获取最新内容
                if (currentContent.contains("改用对话直接回答") && !isRight) {
                    page.locator("//*[@id=\"root\"]/div[1]/div/div[3]/div/main/div/div/div[2]/div/div[1]/div/div/div[2]/div[2]/div/div/div/div/div/div/div[1]/div/div/div[2]/div[1]/div/div").click();
                    isRight = true;
                }

                try {
                    if (isRight) {
                        Locator outputLocator = page.locator("//div[@role='textbox']");
                        // 增加超时控制，避免无限等待
                        outputLocator.waitFor(new Locator.WaitForOptions().setTimeout(5000).setState(WaitForSelectorState.ATTACHED));
                        rightCurrentContent = outputLocator.innerHTML();
                        rightTextContent = outputLocator.textContent();
                        
                        // 检查内容是否为空（限制警告频率：每60秒最多1次）
                        if ((rightCurrentContent == null || rightCurrentContent.trim().isEmpty()) && 
                            (rightTextContent == null || rightTextContent.trim().isEmpty())) {
                            long now = System.currentTimeMillis();
                            if (now - lastWarningTime > 60000) {
                                UserLogUtil.sendAIWarningLog(userId, aiName, "HTML内容监控", "代码生成模式下获取到空内容", url + "/saveLogInfo");
                                lastWarningTime = now;
                                warningCount++;
                            }
                        }
                    }
                    // 增加超时控制，确保元素存在
                    Locator outputLocator = page.locator(".flow-markdown-body").last();
                    outputLocator.waitFor(new Locator.WaitForOptions().setTimeout(5000).setState(WaitForSelectorState.ATTACHED));
                    currentContent = outputLocator.innerHTML();
                    textContent = outputLocator.textContent();
                    
                    // 检查内容是否为空（限制警告频率：每60秒最多1次）
                    if ((currentContent == null || currentContent.trim().isEmpty()) && 
                        (textContent == null || textContent.trim().isEmpty())) {
                        long now = System.currentTimeMillis();
                        if (now - lastWarningTime > 60000) {
                            UserLogUtil.sendAIWarningLog(userId, aiName, "HTML内容监控", "对话模式下获取到空内容", url + "/saveLogInfo");
                            lastWarningTime = now;
                            warningCount++;
                        }
                    }
                } catch (TimeoutError e) {
                    // 如果选择器超时，记录但继续尝试（限制重试次数）
                    long remainingTime = timeout - elapsedTime;
                    
                    if (remainingTime <= 10000) {
                        // 接近总超时时间，记录详细异常
                        TimeoutException timeoutEx = new TimeoutException("选择器等待超时：.flow-markdown-body");
                        UserLogUtil.sendAITimeoutLog(userId, aiName, "HTML内容监控", 
                            timeoutEx, 
                            "无法找到或等待豆包回复内容元素（总等待时间: " + (elapsedTime/1000) + "秒）", 
                            url + "/saveLogInfo");
                        throw new RuntimeException("等待豆包回复元素超时", e);
                    }
                    
                    // 限制警告频率：每30秒最多记录一次
                    long now = System.currentTimeMillis();
                    if (now - lastWarningTime > 30000) {
                        UserLogUtil.sendAIWarningLog(userId, aiName, "HTML内容监控", 
                            "元素未找到，准备重试（已等待: " + (elapsedTime/1000) + "秒，剩余: " + (remainingTime/1000) + "秒）", 
                            url + "/saveLogInfo");
                        lastWarningTime = now;
                        warningCount++;
                    }
                    page.waitForTimeout(2000);
                    continue;
                } catch (com.microsoft.playwright.impl.TargetClosedError e) {
                    // 页面目标关闭
                    UserLogUtil.sendAIWarningLog(userId, aiName, "HTML内容监控", "页面目标已关闭，WebSocket可能断联", url + "/saveLogInfo");
                    throw new RuntimeException("页面目标已关闭", e);
                }
                
                // 🔥 优化：检测是否有 message-action-bar 按钮组（最可靠的完成标志）
                boolean hasActionBar = false;
                try {
                    Locator actionBar = page.locator(".message-action-bar-ghR0JC").last();
                    hasActionBar = actionBar.count() > 0 && actionBar.isVisible();
                    
                    if (hasActionBar) {
                        // 进一步检查是否包含核心按钮（复制、重新生成等）
                        Locator copyButton = page.locator("[data-testid='message_action_copy']").last();
                        boolean hasCopyButton = copyButton.count() > 0;
                        
                        if (hasCopyButton) {
                            logInfo.sendTaskLog("检测到完整的操作按钮组，" + aiName + "回答已完成", userId, aiName);
                            // 按钮组已出现，说明回复真正完成
                            break;
                        }
                    }
                } catch (Exception e) {
                    // 按钮组检测失败不影响主流程
                }
                
                // 如果当前内容和上次内容相同，认为 AI 已经完成回答，退出循环
                if (!currentContent.isEmpty() && currentContent.equals(lastContent)) {
                    if(isRight) {
                        if(!rightCurrentContent.isEmpty() && rightCurrentContent.equals(rightLastContent)) {
                            logInfo.sendTaskLog(aiName + "回答完成，正在自动提取内容", userId, aiName);
                            break;
                        }
                    } else {
                        logInfo.sendTaskLog(aiName + "回答完成，正在自动提取内容", userId, aiName);
                        break;
                    }
                }
                if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
                    if(isRight) {
                        webSocketClientService.sendMessage(userInfoRequest, McpResult.success(rightTextContent, ""), "db-stream");
                    } else {
                        webSocketClientService.sendMessage(userInfoRequest, McpResult.success(textContent, ""), "db-stream");
                    }
                }
                // 更新上次内容为当前内容
                lastContent = currentContent;
                rightLastContent = rightCurrentContent;
                page.waitForTimeout(5000);  // 等待10秒再次检查
            }
            if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
//                延迟3秒结束，确保剩余内容全部输出
                Thread.sleep(3000);
                webSocketClientService.sendMessage(userInfoRequest, McpResult.success("END", ""), "db-stream");
            }
            logInfo.sendTaskLog(aiName + "内容已自动提取完成", userId, aiName);

            String regex = "<span>\\s*<span[^>]*?>\\d+</span>\\s*</span>";
            if(isRight) {
                currentContent = rightCurrentContent;
            }
            currentContent = currentContent.replaceAll(regex, "");
            currentContent = currentContent.replaceAll("撰写任何内容...", "");

            // 记录成功日志
            // 不再记录成功日志，按照用户要求
            return AiResult.success(currentContent, textContent);

        } catch (TimeoutError e) {
            // 记录超时异常
            UserLogUtil.sendAITimeoutLog(userId, aiName, "HTML内容监控", e, "等待内容生成完成", url + "/saveLogInfo");
            throw e;
        } catch (Exception e) {
            // 记录其他异常
            UserLogUtil.sendAIExceptionLog(userId, aiName, "waitDBHtmlDom", e, System.currentTimeMillis(), "HTML内容提取失败", url + "/saveLogInfo");
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
            boolean isRight = false;
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
