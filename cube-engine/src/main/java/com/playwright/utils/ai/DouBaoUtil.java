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
                
                // 记录成功日志
                UserLogUtil.sendAIBusinessLog(userId, "豆包", "超能模式", "成功点击试一试按钮进入超能模式", System.currentTimeMillis(), url + "/saveLogInfo");
            }
        } catch (Exception e) {
            // 如果按钮不存在或点击失败，记录但不抛出异常，不影响后续流程
            UserLogUtil.sendAIBusinessLog(userId, "豆包", "超能模式检测", "超能模式按钮检测或点击失败：" + e.getMessage(), System.currentTimeMillis(), url + "/saveLogInfo");
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
            System.out.println("\n🔄 ==================== AI模式切换开始 ====================");
            System.out.println("👤 用户ID: " + userId);
            System.out.println("🧠 需要深度思考: " + needDeepThinking);
            System.out.println("🌐 当前页面URL: " + page.url());
            
            // 等待页面加载完成，给足够时间让按钮渲染
            System.out.println("⏳ 等待模式切换按钮渲染...");
            page.waitForTimeout(2000);  // 增加等待时间到2秒
            
            // 尝试等待至少一个模式按钮出现（最多等待5秒）
            try {
                page.locator(".switch-button-qHPwBT").first().waitFor(new Locator.WaitForOptions().setTimeout(5000));
                System.out.println("✅ 模式切换按钮已渲染");
            } catch (Exception e) {
                System.err.println("⚠️  警告：5秒内未检测到模式切换按钮，可能页面加载异常");
            }
            
            // 定位所有模式按钮
            Locator speedModeButton = page.locator(".switch-button-qHPwBT:has-text(\"极速\")").first();
            Locator thinkModeButton = page.locator(".switch-button-qHPwBT:has-text(\"思考\")");
            Locator superModeButton = page.locator("[data-testid='super-agent-mode-switch']");
            
            boolean hasSuperMode = superModeButton.count() > 0;
            System.out.println("🔍 检测到的按钮数量:");
            System.out.println("   - 极速模式按钮: " + speedModeButton.count());
            System.out.println("   - 思考模式按钮: " + thinkModeButton.count());
            System.out.println("   - 超能模式按钮: " + superModeButton.count());
            System.out.println("   - 是否有超能权限: " + hasSuperMode);
            
            if (hasSuperMode) {
                // ========== 内测用户（有超能权限）==========
                System.out.println("\n📍 用户类型: 内测用户（有超能权限）");
                logInfo.sendTaskLog("检测到超能模式，当前为内测用户", userId, "豆包");
                
                if (needDeepThinking) {
                    // 需要深度思考：使用超能模式
                    boolean superActive = isModeActive(superModeButton);
                    System.out.println("🎯 目标模式: 超能模式（深度思考）");
                    System.out.println("📊 当前超能模式激活状态: " + superActive);
                    
                    if (!superActive) {
                        // 超能模式未激活，需要切换
                        System.out.println("🔄 执行操作: 切换到超能模式");
                        logInfo.sendTaskLog("任务需要深度思考，正在切换到超能模式", userId, "豆包");
                        superModeButton.click();
                        page.waitForTimeout(500);
                        System.out.println("✅ 超能模式切换成功");
                        logInfo.sendTaskLog("✓ 已启用超能模式", userId, "豆包");
                        UserLogUtil.sendAIBusinessLog(userId, "豆包", "模式切换", "已切换到超能模式（深度思考）", System.currentTimeMillis(), url + "/saveLogInfo");
                    } else {
                        System.out.println("✅ 超能模式已经激活，无需切换");
                        logInfo.sendTaskLog("✓ 超能模式已启用（无需切换）", userId, "豆包");
                    }
                } else {
                    // 不需要深度思考：必须使用极速模式
                    boolean superActive = isModeActive(superModeButton);
                    boolean speedActive = speedModeButton.count() > 0 && isModeActive(speedModeButton);
                    
                    System.out.println("🎯 目标模式: 极速模式（无需深度思考）");
                    System.out.println("📊 当前模式状态:");
                    System.out.println("   - 超能模式激活: " + superActive);
                    System.out.println("   - 极速模式激活: " + speedActive);
                    
                    if (superActive) {
                        // 当前是超能模式，需要切换到极速模式
                        System.out.println("⚠️  检测到当前为超能模式，需要切换到极速模式");
                        logInfo.sendTaskLog("当前为超能模式，但任务无需深度思考，正在切换到极速模式", userId, "豆包");
                        if (speedModeButton.count() > 0) {
                            System.out.println("🔄 执行操作: 点击极速模式按钮");
                            speedModeButton.click();
                            page.waitForTimeout(500);
                            System.out.println("✅ 成功从超能模式切换到极速模式");
                            logInfo.sendTaskLog("✓ 已从超能模式切换到极速模式", userId, "豆包");
                            UserLogUtil.sendAIBusinessLog(userId, "豆包", "模式切换", "从超能模式切换到极速模式", System.currentTimeMillis(), url + "/saveLogInfo");
                        }
                    } else if (!speedActive && speedModeButton.count() > 0) {
                        // 既不是超能也不是极速，切换到极速
                        System.out.println("🔄 执行操作: 切换到极速模式");
                        logInfo.sendTaskLog("正在切换到极速模式", userId, "豆包");
                        speedModeButton.click();
                        page.waitForTimeout(500);
                        System.out.println("✅ 极速模式切换成功");
                        logInfo.sendTaskLog("✓ 已启用极速模式", userId, "豆包");
                        UserLogUtil.sendAIBusinessLog(userId, "豆包", "模式切换", "已切换到极速模式", System.currentTimeMillis(), url + "/saveLogInfo");
                    } else {
                        System.out.println("✅ 极速模式已经激活，无需切换");
                        logInfo.sendTaskLog("✓ 极速模式已启用（无需切换）", userId, "豆包");
                    }
                }
            } else {
                // ========== 普通用户（无超能权限）==========
                System.out.println("\n📍 用户类型: 普通用户（无超能权限）");
                
                if (needDeepThinking) {
                    // 需要深度思考：使用思考模式
                    boolean thinkActive = thinkModeButton.count() > 0 && isModeActive(thinkModeButton);
                    System.out.println("🎯 目标模式: 思考模式（深度思考）");
                    System.out.println("📊 当前思考模式激活状态: " + thinkActive);
                    
                    if (thinkModeButton.count() > 0 && !thinkActive) {
                        System.out.println("🔄 执行操作: 切换到思考模式");
                        logInfo.sendTaskLog("任务需要深度思考，正在切换到思考模式", userId, "豆包");
                        thinkModeButton.click();
                        page.waitForTimeout(500);
                        System.out.println("✅ 思考模式切换成功");
                        logInfo.sendTaskLog("✓ 已启用思考模式", userId, "豆包");
                        UserLogUtil.sendAIBusinessLog(userId, "豆包", "模式切换", "已切换到思考模式（深度思考）", System.currentTimeMillis(), url + "/saveLogInfo");
                    } else {
                        System.out.println("✅ 思考模式已经激活，无需切换");
                        logInfo.sendTaskLog("✓ 思考模式已启用（无需切换）", userId, "豆包");
                    }
                } else {
                    // 不需要深度思考：使用极速模式
                    boolean speedActive = speedModeButton.count() > 0 && isModeActive(speedModeButton);
                    System.out.println("🎯 目标模式: 极速模式（无需深度思考）");
                    System.out.println("📊 当前极速模式激活状态: " + speedActive);
                    
                    if (speedModeButton.count() > 0 && !speedActive) {
                        System.out.println("🔄 执行操作: 切换到极速模式");
                        logInfo.sendTaskLog("任务无需深度思考，正在切换到极速模式", userId, "豆包");
                        speedModeButton.click();
                        page.waitForTimeout(500);
                        System.out.println("✅ 极速模式切换成功");
                        logInfo.sendTaskLog("✓ 已启用极速模式", userId, "豆包");
                        UserLogUtil.sendAIBusinessLog(userId, "豆包", "模式切换", "已切换到极速模式", System.currentTimeMillis(), url + "/saveLogInfo");
                    } else {
                        System.out.println("✅ 极速模式已经激活，无需切换");
                        logInfo.sendTaskLog("✓ 极速模式已启用（无需切换）", userId, "豆包");
                    }
                }
            }
            System.out.println("🏁 ==================== AI模式切换结束 ====================\n");
        } catch (Exception e) {
            // 如果模式切换失败，记录但不抛出异常，不影响后续流程
            System.err.println("❌ AI模式切换失败: " + e.getMessage());
            e.printStackTrace();
            UserLogUtil.sendAIBusinessLog(userId, "豆包", "模式切换", "AI模式切换失败：" + e.getMessage(), System.currentTimeMillis(), url + "/saveLogInfo");
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
            long timeout = 600000; // 10分钟超时
            long operationStartTime = System.currentTimeMillis();

            while (true) {
                long elapsedTime = System.currentTimeMillis() - operationStartTime;
                if (elapsedTime > timeout) {
                    // 记录超时异常
                    UserLogUtil.sendAITimeoutLog(userId, "豆包", "评分内容等待", new TimeoutException("豆包运行超时"), "等待评分结果生成", url + "/saveLogInfo");
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
            UserLogUtil.sendAISuccessLog(userId, "豆包", "评分任务", "成功完成评分并提取结果", startTime, url + "/saveLogInfo");

        } catch (TimeoutError e) {
            // 记录超时异常
            UserLogUtil.sendAITimeoutLog(userId, "豆包", "评分任务", e, "复制按钮等待或点击操作", url + "/saveLogInfo");
            throw e;
        } catch (Exception e) {
            // 记录其他异常
            UserLogUtil.sendAIExceptionLog(userId, "豆包", "waitAndClickDBScoreCopyButton", e, startTime, "评分任务执行失败", url + "/saveLogInfo");
            throw e;
        }
    }

    public String waitAndClickDBCopyButton(Page page, String userId, String roles) throws InterruptedException {
        try {
            // 等待页面内容稳定
            String currentContent = "";
            String lastContent = "";
            long timeout = 600000; // 10分钟超时
            long startTime = System.currentTimeMillis();

            while (true) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > timeout) {
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
            String copiedText = "";
            // 等待复制按钮出现
            Locator locator = page.locator("//*[@id=\"root\"]/div[1]/div/div[3]/div[1]/div[1]/div/div/div[2]/div/div[2]/div/div/div");

            if (locator.count() > 0 && locator.isVisible()) {
                locator.click(new Locator.ClickOptions().setForce(true));
            } else {
            }


            page.waitForSelector("[data-testid='message_action_copy']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(600000));  // 600秒超时
            logInfo.sendTaskLog("豆包回答完成，正在自动提取内容", userId, "豆包");
            // 点击复制按钮
            page.locator("[data-testid='message_action_copy']").last()  // 获取最后一个复制按钮
                    .click();
            Thread.sleep(2000);
            copiedText = (String) page.evaluate("navigator.clipboard.readText()");
            logInfo.sendTaskLog("豆包内容已自动提取完成", userId, "豆包");

            // 记录成功日志
            UserLogUtil.sendAISuccessLog(userId, "豆包", "内容复制", "成功提取豆包回答内容", System.currentTimeMillis(), url + "/saveLogInfo");
            return copiedText;
        } catch (TimeoutError e) {
            // 记录超时异常
            UserLogUtil.sendAITimeoutLog(userId, "豆包", "内容复制", e, "等待复制按钮或内容提取", url + "/saveLogInfo");
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
        try {
            // 等待聊天框的内容稳定
            String currentContent = "";
            String lastContent = "";
            String rightCurrentContent = "";
            String rightLastContent = "";
            String textContent = "";
            String rightTextContent = "";
            boolean isRight = false;
            // 设置最大等待时间（单位：毫秒），比如 10 分钟
            long timeout = 600000; // 10 分钟
            long startTime = System.currentTimeMillis();  // 获取当前时间戳

            // 进入循环，直到内容不再变化或者超时
            while (true) {
                // 检查是否是代码生成
                Locator chatHis = page.locator("//div[@class='canvas-header-Bc97DC']");
                if (chatHis.count() > 0) {
                    isRight = true;
                } else {
                    isRight = false;
                }
                Locator changeTypeLocator = page.locator("text=改用对话直接回答");
                if (changeTypeLocator.isVisible()) {
                    changeTypeLocator.click();
                }
                // 获取当前时间戳
                long elapsedTime = System.currentTimeMillis() - startTime;

                // 如果超时，退出循环
                if (elapsedTime > timeout) {
                    break;
                }
                // 获取最新内容
                if (currentContent.contains("改用对话直接回答") && !isRight) {
                    page.locator("//*[@id=\"root\"]/div[1]/div/div[3]/div/main/div/div/div[2]/div/div[1]/div/div/div[2]/div[2]/div/div/div/div/div/div/div[1]/div/div/div[2]/div[1]/div/div").click();
                    isRight = true;
                }

                if (isRight) {
                    Locator outputLocator = page.locator("//div[@role='textbox']");
                    rightCurrentContent = outputLocator.innerHTML();
                    rightTextContent = outputLocator.textContent();
                }
                Locator outputLocator = page.locator(".flow-markdown-body").last();
                currentContent = outputLocator.innerHTML();
                textContent = outputLocator.textContent();
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
            UserLogUtil.sendAISuccessLog(userId, aiName, "HTML内容提取", "成功提取并处理HTML内容", System.currentTimeMillis(), url + "/saveLogInfo");
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
            // 设置最大等待时间（单位：毫秒），比如 10 分钟
            long timeout = 600000; // 10 分钟
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
                            // 获取所有复制按钮的 SVG 元素（通过 xlink:href 属性定位）
                            if (page.locator("[data-testid='code-block-copy']").count() > 0) {
                                page.locator("[data-testid='code-block-copy']").last()  // 获取最后一个复制按钮
                                        .click();
                            } else {
                                page.locator("[data-testid='message_action_copy']").last()  // 获取最后一个复制按钮
                                        .click();
                            }

                            String text = (String) page.evaluate("navigator.clipboard.readText()");
                            textRef.set(text);
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
            UserLogUtil.sendAISuccessLog(userId, aiName, "排版代码提取", "成功提取排版代码内容", System.currentTimeMillis(), url + "/saveLogInfo");
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
