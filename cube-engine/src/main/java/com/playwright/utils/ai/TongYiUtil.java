package com.playwright.utils.ai;

import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.playwright.entity.UserInfoRequest;
import com.playwright.entity.mcp.McpResult;
import com.playwright.utils.common.ElementSelectorUtil;
import com.playwright.utils.common.LogMsgUtil;
import com.playwright.utils.common.UserLogUtil;
import com.playwright.websocket.WebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通义千问AI工具类
 * 提供与通义千问AI交互的自动化操作功能
 *
 * @author 优立方
 * @version JDK 17
 * @date 2025年05月27日 10:33
 */
@Component
public class TongYiUtil {

    @Autowired
    private LogMsgUtil logInfo;

    @Autowired
    private WebSocketClientService webSocketClientService;

    @Autowired
    private ElementSelectorUtil elementSelectorUtil;

    @Value("${cube.url}")
    private String url;

    /**
     * 处理通义千问的特殊模式切换（深度思考/联网搜索）
     *
     * @param page   Playwright页面实例
     * @param roles  用户选择的角色字符串
     * @param userId 用户ID
     * @param aiName AI名称
     */
    private void handleCapabilitySwitch(Page page, String roles, String userId, String aiName) {
        long startTime = System.currentTimeMillis();
        try {
            // 检查页面是否关闭
            if (page.isClosed()) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "模式切换", "页面已关闭，无法切换模式", url + "/saveLogInfo");
                throw new RuntimeException("页面已关闭");
            }
            
            String desiredMode = "";
            if (roles.contains("ty-qw-sdsk")) {
                desiredMode = "深度思考";
            }/* else if (roles.contains("ty-qw-lwss")) {
                desiredMode = "联网搜索";
            }*/

            // 🔥 优化：检查当前是否已有激活的模式，增加超时控制
            try {
                // 检查深度思考模式是否已激活（通过selected--IDtqLZFo class判断）
                String[] activeModeSelectors = {
                    "div.tagBtn--jji85P_L.selected--IDtqLZFo",  // 新的激活模式选择器
                    "span[class*='closeIcon--']",               // 旧的关闭按钮选择器（备用）
                    ".selected--IDtqLZFo"                       // 通用激活状态选择器
                };
                
                Locator activeModeElement = null;
                String activeModeText = "";
                
                for (String selector : activeModeSelectors) {
                    try {
                        Locator tempElement = page.locator(selector);
                        tempElement.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                        if (tempElement.count() > 0 && tempElement.isVisible()) {
                            activeModeElement = tempElement;
                            activeModeText = tempElement.textContent().trim();
                            break;
                        }
                    } catch (TimeoutError te) {
                        // 继续尝试下一个选择器
                    }
                }

                if (activeModeElement != null && activeModeElement.isVisible()) {
                    // 如果模式不同则先关闭当前模式
                    if (!activeModeText.contains(desiredMode)) {
                        // 查找关闭按钮（SVG图标）
                        String[] closeButtonSelectors = {
                            "div.tagBtn--jji85P_L.selected--IDtqLZFo .size-\\[20px\\] svg",  // 新的关闭按钮
                            "span[class*='closeIcon--']",                                    // 旧的关闭按钮
                            ".selected--IDtqLZFo svg[class*='anticon']"                     // 通用SVG关闭按钮
                        };
                        
                        boolean closed = false;
                        for (String closeSelector : closeButtonSelectors) {
                            try {
                                Locator closeButton = page.locator(closeSelector);
                                if (closeButton.count() > 0 && closeButton.isVisible()) {
                                    closeButton.click(new Locator.ClickOptions().setTimeout(15000));
                                    page.waitForTimeout(2000);
                                    closed = true;
                                    break;
                                }
                            } catch (Exception ce) {
                                // 继续尝试下一个关闭按钮选择器
                            }
                        }
                        
                        if (!closed) {
                            UserLogUtil.sendAIWarningLog(userId, aiName, "模式切换", "无法找到可用的关闭按钮", url + "/saveLogInfo");
                        }
                    } else {
                        // 记录模式已正确
                        return; // 不记录成功日志，按照用户要求
                    }
                }
            } catch (Exception e) {
                // 如果没有找到激活模式，说明没有激活模式，继续处理
                UserLogUtil.sendAIWarningLog(userId, aiName, "模式切换", "未检测到已激活的模式，将尝试开启新模式", url + "/saveLogInfo");
            }

            // 🔥 优化：开启目标模式，增加超时控制和重试机制
            if (!desiredMode.isEmpty()) {
                try {
                    Locator buttonContainer = page.locator(".operateLine--gpbLU2Fi");
                    buttonContainer.waitFor(new Locator.WaitForOptions().setTimeout(20000));

                    Locator modeButton = buttonContainer.getByText(desiredMode);
                    if (modeButton.count() == 0) {
                        // 🔥 优化：添加页面状态信息并使用去重机制
                        UserLogUtil.sendAIWarningLogWithDedup(userId, aiName, "模式切换", 
                            "未找到模式按钮元素：" + desiredMode + " | 页面URL：" + page.url(), 
                            url + "/saveLogInfo", 30000);
                        return;
                    }
                    
                    try {
                        modeButton.click(new Locator.ClickOptions().setTimeout(15000));
                    } catch (Exception e) {
                        UserLogUtil.sendAIWarningLog(userId, aiName, "模式切换", "模式按钮不可点击：" + e.getMessage(), url + "/saveLogInfo");
                        throw e;
                    }
                    
                    page.waitForTimeout(2000); // 增加等待时间

                    // 不再记录成功日志，按照用户要求
                } catch (TimeoutError e) {
                    // 如果找不到模式按钮，记录警告但不抛出异常
                    UserLogUtil.sendAIWarningLog(userId, aiName, "模式切换", "等待模式按钮超时：" + desiredMode, url + "/saveLogInfo");
                }
            }
        } catch (com.microsoft.playwright.impl.TargetClosedError e) {
            // 页面目标关闭
            UserLogUtil.sendAIWarningLog(userId, aiName, "模式切换", "页面目标已关闭，WebSocket可能断联", url + "/saveLogInfo");
            logInfo.sendTaskLog("切换特殊模式时页面目标已关闭", userId, aiName);
            throw e;
        } catch (TimeoutError e) {
            // 记录模式切换超时
            UserLogUtil.sendAITimeoutLog(userId, aiName, "模式切换", e, "等待模式按钮或切换操作", url + "/saveLogInfo");
            logInfo.sendTaskLog("切换特殊模式时发生超时", userId, aiName);
            throw e;
        } catch (Exception e) {
            // 记录模式切换异常
            UserLogUtil.sendAIBusinessLog(userId, aiName, "模式切换", "切换特殊模式时发生错误：" + e.getMessage(), startTime, url + "/saveLogInfo");
            logInfo.sendTaskLog("切换特殊模式时发生严重错误", userId, aiName);
            throw e;
        }
    }

    /**
     * 提取出的通义千问请求核心处理方法
     *
     * @param page            Playwright页面实例
     * @param userInfoRequest 包含所有请求信息的对象
     * @return 包含处理结果的Map
     */
    public Map<String, String> processQianwenRequest(Page page, UserInfoRequest userInfoRequest) throws InterruptedException, TimeoutException {
        String userId = userInfoRequest.getUserId();
        String aiName = "通义千问";
        Map<String, String> resultMap = new HashMap<>();
        long startTime = System.currentTimeMillis();

        try {
            // 检查页面是否关闭
            if (page.isClosed()) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "请求处理", "页面已关闭，无法处理请求", url + "/saveLogInfo");
                throw new RuntimeException("页面已关闭");
            }
            
            // 切换特殊模式
            handleCapabilitySwitch(page, userInfoRequest.getRoles(), userId, aiName);

            // 🔥 智能输入框定位策略 - 支持多种placeholder文本
            Locator inputBox = null;
            String[] inputSelectors = {
                    "//textarea[@placeholder='遇事不决问通义']",
                    "//textarea[@placeholder='Enter 发送，Ctrl+Enter 换行，点击放大按钮可全屏输入']",
                    "//textarea[@placeholder='基于Qwen3推理模型，支持自动联网搜索']",
                    "//textarea[contains(@class,'textarea--FEdqShqI')]",
                    "//textarea[contains(@class,'ant-input')]",
                    "//div[@class='chatTextarea--RVTXJYOh']//textarea",
                    "//div[@class='inputContainer--HIOhfxuo']//textarea"
            };

            // 尝试找到可见的输入框
            for (String selector : inputSelectors) {
                try {
                    Locator tempBox = page.locator(selector);
                    if (tempBox.count() > 0 && tempBox.isVisible()) {
                        inputBox = tempBox;
                        logInfo.sendTaskLog("找到输入框，使用选择器: " + selector, userId, aiName);
                        break;
                    }
                } catch (Exception e) {
                    // 继续尝试下一个选择器
                }
            }

            if (inputBox == null) {
                UserLogUtil.sendElementWarningLog(userId, aiName, "请求处理", "input, textarea", "未找到可用的输入框元素", url + "/saveLogInfo");
                throw new RuntimeException("未找到可用的输入框");
            }
//            inputBox.click();
            page.waitForTimeout(500);
////            模拟键盘输入
//            page.keyboard().type(userInfoRequest.getUserPrompt(), new Keyboard.TypeOptions()
//                    .setDelay(100)); // 每个字符之间延迟100ms，更接近真人输入
            try {
                inputBox.fill(userInfoRequest.getUserPrompt());
                logInfo.sendTaskLog("用户指令已自动输入完成", userId, aiName);
            } catch (Exception e) {
                UserLogUtil.sendElementWarningLog(userId, aiName, "请求处理", "input, textarea", "输入框填充失败：" + e.getMessage(), url + "/saveLogInfo");
                throw e;
            }
            
            page.waitForTimeout(500);

            // 🔥 优化：使用增强的安全点击方法，带有重试机制和多选择器策略
            boolean sendSuccess = false;
            try {
                sendSuccess = elementSelectorUtil.safeClickTongYiSendButton(page, "发送按钮点击", 3);
            } catch (Exception e) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "请求处理", "发送按钮点击异常：" + e.getMessage(), url + "/saveLogInfo");
            }
            
            if (!sendSuccess) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "请求处理", "发送按钮点击失败或不可见", url + "/saveLogInfo");
                throw new TimeoutException("发送按钮点击失败，尝试了多种选择器和重试策略仍无法成功");
            }

            // 🔥 增强：验证发送是否成功，等待停止按钮出现
            boolean messageSent = false;
            try {
                messageSent = elementSelectorUtil.waitForTongYiStopButton(page, 15000);
            } catch (Exception e) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "请求处理", "等待停止按钮异常：" + e.getMessage(), url + "/saveLogInfo");
            }

            if (messageSent) {
                logInfo.sendTaskLog("指令已自动发送成功，已开始生成回答", userId, aiName);
            } else {
                UserLogUtil.sendAIWarningLog(userId, aiName, "请求处理", "未检测到停止按钮，可能发送失败或页面结构变化", url + "/saveLogInfo");
                logInfo.sendTaskLog("指令可能发送成功，但未检测到停止按钮，继续处理", userId, aiName);
            }
            logInfo.sendTaskLog("开启自动监听任务，持续监听" + aiName + "回答中", userId, aiName);

            // 获取原始回答HTML
            String rawHtmlContent = waitTongYiHtmlDom(page, userId, aiName, userInfoRequest);
            resultMap.put("rawHtmlContent", rawHtmlContent);

            // 捕获当前会话的 sessionId
            String currentUrl = page.url();
            Pattern pattern = Pattern.compile("sessionId=([a-zA-Z0-9\\-]+)");
            Matcher matcher = pattern.matcher(currentUrl);
            if (matcher.find()) {
                String sessionId = matcher.group(1);
                resultMap.put("sessionId", sessionId);
            } else {
                resultMap.put("sessionId", "");
                logInfo.sendTaskLog("未能在URL中捕获会话ID", userId, aiName);
            }

            // 记录处理成功
            // 不再记录成功日志，按照用户要求
            return resultMap;

        } catch (TimeoutError e) {
            // 记录处理超时
            UserLogUtil.sendAITimeoutLog(userId, aiName, "请求处理", e, "整个请求处理流程", url + "/saveLogInfo");
            logInfo.sendTaskLog("处理通义千问请求时发生超时", userId, aiName);
            resultMap.put("rawHtmlContent", "获取内容失败：超时");
            throw e;
        } catch (Exception e) {
            // 记录处理异常
            UserLogUtil.sendAIExceptionLog(userId, aiName, "processQianwenRequest", e, startTime, "处理通义千问请求失败", url + "/saveLogInfo");
            logInfo.sendTaskLog("处理通义千问请求时发生错误", userId, aiName);
            resultMap.put("rawHtmlContent", "获取内容失败");
            throw e;
        }
    }

    /**
     * 复制通义千问的回答内容到剪贴板
     * 根据新的HTML结构定位复制按钮：
     * 1. 定位最后一个回答容器 .containerWrap--r2_gRwLP
     * 2. 在工具栏中找到复制按钮（SVG图标 #tongyi-copy-line）
     * 
     * @param page   Playwright页面实例
     * @param userId 用户ID
     * @param aiName AI名称
     * @return 复制是否成功
     */
    public boolean copyTongYiContent(Page page, String userId, String aiName) {
        try {
            // 检查页面是否关闭
            if (page.isClosed()) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "内容复制", "页面已关闭，无法复制内容", url + "/saveLogInfo");
                return false;
            }

            logInfo.sendTaskLog("正在定位复制按钮...", userId, aiName);

            // 🔥 策略1：基于最后一个回答容器 + SVG图标定位
            String[] copyButtonSelectors = {
                // 新版选择器：定位到最后一个回答容器内的复制按钮
                ".containerWrap--r2_gRwLP:last-child .tools--JSWHLNPm svg[href='#tongyi-copy-line']",
                ".containerWrap--r2_gRwLP:last-child use[xlink\\:href='#tongyi-copy-line']",
                
                // 通过父元素定位
                ".containerWrap--r2_gRwLP:last-child .btn--YtZqkWMA:has(svg use[xlink\\:href='#tongyi-copy-line'])",
                
                // 备用：通过rightArea定位
                ".containerWrap--r2_gRwLP:last-child .rightArea--rL5UNOps .btn--YtZqkWMA:has(svg)",
                
                // 通过工具栏定位第5个按钮（赞、踩、分享、刷新、复制）
                ".containerWrap--r2_gRwLP:last-child .tools--JSWHLNPm .btn--YtZqkWMA:nth-child(5)",
                
                // 更通用的选择器
                ".containerWrap--r2_gRwLP:last-child [class*='btn--']:has(svg[href*='copy'])"
            };

            boolean copySuccess = false;
            
            for (int attempt = 0; attempt < copyButtonSelectors.length; attempt++) {
                try {
                    String selector = copyButtonSelectors[attempt];
                    logInfo.sendTaskLog("尝试选择器 " + (attempt + 1) + ": " + selector, userId, aiName);
                    
                    Locator copyButton = page.locator(selector);
                    
                    // 等待按钮出现（最多5秒）
                    copyButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                    
                    if (copyButton.count() > 0 && copyButton.isVisible()) {
                        // 找到可见的复制按钮
                        logInfo.sendTaskLog("找到复制按钮，准备点击", userId, aiName);
                        copyButton.click(new Locator.ClickOptions().setTimeout(3000));
                        page.waitForTimeout(1000); // 等待剪贴板更新
                        
                        copySuccess = true;
                        logInfo.sendTaskLog("✓ 内容已成功复制到剪贴板", userId, aiName);
                        break;
                    }
                } catch (TimeoutError e) {
                    // 当前选择器超时，尝试下一个
                    logInfo.sendTaskLog("选择器 " + (attempt + 1) + " 未找到复制按钮，尝试下一个", userId, aiName);
                } catch (Exception e) {
                    UserLogUtil.sendAIWarningLog(userId, aiName, "内容复制", 
                        "选择器 " + (attempt + 1) + " 点击失败: " + e.getMessage(), url + "/saveLogInfo");
                }
            }

            if (!copySuccess) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "内容复制", 
                    "所有复制按钮选择器均失败 | 页面URL: " + page.url(), url + "/saveLogInfo");
            }

            return copySuccess;

        } catch (Exception e) {
            UserLogUtil.sendAIExceptionLog(userId, aiName, "copyTongYiContent", e, 
                System.currentTimeMillis(), "复制通义内容失败", url + "/saveLogInfo");
            return false;
        }
    }

    /**
     * 获取通义千问的分享链接
     * 新方案：直接使用当前页面URL作为分享链接
     * 通义千问对话页面URL本身就包含sessionId，可直接用于分享访问
     * 
     * @param page   Playwright页面实例
     * @param userId 用户ID
     * @param aiName AI名称
     * @return 分享链接（当前页面URL），失败返回null
     */
    public String getTongYiShareLink(Page page, String userId, String aiName) {
        try {
            // 检查页面是否关闭
            if (page.isClosed()) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "分享链接", "页面已关闭，无法获取分享链接", url + "/saveLogInfo");
                return null;
            }

            // 🔥 新方案：直接使用当前页面URL作为分享链接
            // 通义千问的对话URL本身就包含sessionId，可以直接分享
            logInfo.sendTaskLog("正在获取分享链接...", userId, aiName);
            
            String currentUrl = page.url();
            
            // 验证URL格式（确保包含sessionId）
            if (!currentUrl.contains("sessionId=")) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "分享链接", 
                    "URL格式异常，未包含sessionId | URL: " + currentUrl, url + "/saveLogInfo");
                logInfo.sendTaskLog("⚠ URL格式异常，无法生成分享链接", userId, aiName);
                return null;
            }
            
            logInfo.sendTaskLog("✓ 已获取分享链接: " + currentUrl, userId, aiName);
            
            return currentUrl;

        } catch (Exception e) {
            UserLogUtil.sendAIExceptionLog(userId, aiName, "getTongYiShareLink", e, 
                System.currentTimeMillis(), "获取分享链接失败", url + "/saveLogInfo");
            return null;
        }
    }

    /**
     * 等待通义AI的回答内容稳定，并获取HTML片段
     *
     * @param page   Playwright页面实例
     * @param userId 用户ID
     * @param aiName 智能体名称
     */
    public String waitTongYiHtmlDom(Page page, String userId, String aiName, UserInfoRequest userInfoRequest) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        try {
            // 检查页面是否关闭
            if (page.isClosed()) {
                UserLogUtil.sendAIWarningLog(userId, aiName, "内容提取", "页面已关闭，无法提取内容", url + "/saveLogInfo");
                throw new RuntimeException("页面已关闭");
            }
            
            String currentContent = "";
            String lastContent = "";
            String textContent = "";

            long timeout = 900000; // 15分钟 (延长50%: 600000 -> 900000)
            long operationStartTime = System.currentTimeMillis();

            Thread.sleep(3000);
            boolean isEnd = false;
            while (true) {
                // 定期检查页面状态
                if (page.isClosed()) {
                    UserLogUtil.sendAIWarningLog(userId, aiName, "内容提取", "页面在提取过程中被关闭", url + "/saveLogInfo");
                    throw new RuntimeException("页面在提取过程中被关闭");
                }
                
                long elapsedTime = System.currentTimeMillis() - operationStartTime;

                if (elapsedTime > timeout) {
                    // 记录等待超时
                    UserLogUtil.sendAITimeoutLog(userId, aiName, "内容等待", new TimeoutException("通义千问超时"), "等待AI回答完成", url + "/saveLogInfo");
                    logInfo.sendTaskLog("AI回答超时，任务中断", userId, aiName);
                    break;
                }

                Locator container = null;
                Locator outputLocator = null;
                
                try {
                    container = page.locator(".containerWrap--r2_gRwLP").last();
                    outputLocator = container.locator(".tongyi-markdown");
                } catch (Exception e) {
                    UserLogUtil.sendAIWarningLog(userId, aiName, "内容提取", "定位内容元素失败：" + e.getMessage(), url + "/saveLogInfo");
                    page.waitForTimeout(2000);
                    continue;
                }

                try {
                    if (!page.locator("//div[@class='operateBtn--qMhYIdIu stop--P_jcrPFo']").isVisible()) {
                        isEnd = true;
                    }
                } catch (Exception e) {
                    // 停止按钮不存在或检查失败，假设已结束
                    isEnd = true;
                }
                
                // 🔥 修复：减少重复的警告日志输出
                if (outputLocator.count() == 0) {
                    // 静默等待，不输出警告日志
                    page.waitForTimeout(2000);
                    continue;
                }

//                currentContent = outputLocator.innerHTML();
                try {
                    currentContent = outputLocator.innerText();
                    textContent = outputLocator.innerText();
                } catch (Exception e) {
                    UserLogUtil.sendAIWarningLog(userId, aiName, "内容提取", "读取元素文本失败：" + e.getMessage(), url + "/saveLogInfo");
                    page.waitForTimeout(2000);
                    continue;
                }
                
                // 🔥 优化：不再记录内容为空的警告（可能是AI正常响应或还在生成）
                // 只在真正出错时才记录
                
                if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
                    try {
                        webSocketClientService.sendMessage(userInfoRequest, McpResult.success(textContent, ""), userInfoRequest.getAiName());
                    } catch (Exception e) {
                        UserLogUtil.sendAIWarningLog(userId, aiName, "内容提取", "WebSocket发送消息失败：" + e.getMessage(), url + "/saveLogInfo");
                    }
                }
                
                if (isEnd && currentContent.equals(lastContent)) {
                    if (currentContent.isEmpty()) {
                        // 🔥 优化：添加更多上下文信息
                        UserLogUtil.sendAIWarningLogWithDedup(userId, aiName, "内容提取", 
                            "回答内容为空，页面可能出现异常 | 页面URL：" + page.url() + " | 已检测到结束标志", 
                            url + "/saveLogInfo", 30000);
                        page.close(); //遇到问题直接关闭页面
                        throw new TimeoutError("未检测到回答");
                    } else {
                        logInfo.sendTaskLog(aiName + "回答完成，正在自动提取内容", userId, aiName);
                        break;
                    }
                }

                lastContent = currentContent;
                page.waitForTimeout(2000);
            }
            logInfo.sendTaskLog(aiName + "内容已自动提取完成", userId, aiName);
            if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
                try {
                    webSocketClientService.sendMessage(userInfoRequest, McpResult.success("END", ""), userInfoRequest.getAiName());
                } catch (Exception e) {
                    UserLogUtil.sendAIWarningLog(userId, aiName, "内容提取", "WebSocket发送结束消息失败：" + e.getMessage(), url + "/saveLogInfo");
                }
            }
            // 记录内容提取成功
            // 不再记录成功日志，按照用户要求
            return currentContent;

        } catch (com.microsoft.playwright.impl.TargetClosedError e) {
            // 页面目标关闭
            UserLogUtil.sendAIWarningLog(userId, aiName, "内容提取", "页面目标已关闭，WebSocket可能断联", url + "/saveLogInfo");
            throw e;
        } catch (TimeoutError e) {
            // 记录内容提取超时
            UserLogUtil.sendAITimeoutLog(userId, aiName, "内容提取", e, "等待内容稳定", url + "/saveLogInfo");
            throw e;
        } catch (Exception e) {
            // 记录内容提取异常
            UserLogUtil.sendAIExceptionLog(userId, aiName, "waitTongYiHtmlDom", e, startTime, "内容提取失败", url + "/saveLogInfo");
            throw e;
        }
    }
}