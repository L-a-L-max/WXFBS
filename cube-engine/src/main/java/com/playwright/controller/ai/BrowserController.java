package com.playwright.controller.ai;

import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.playwright.entity.UnPersisBrowserContextInfo;
import com.playwright.utils.ai.*;
import com.playwright.utils.common.*;
import com.playwright.websocket.WebSocketClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 浏览器控制器
 *
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 【新增AI接入指南】如何将新的AI集成到登录管理系统
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *
 * 🎯 核心流程（三步走）：
 *
 * 第1步：准备登录会话
 * ├─ 调用：loginSessionManager.prepareLoginSession(userId, "AI名称")
 * ├─ 作用：强制清理该用户的所有旧会话，防止串码
 * └─ 返回：sessionKey（非null）
 *
 * 第2步：创建浏览器并注册会话
 * ├─ 创建BrowserContext和Page
 * ├─ 调用：loginSessionManager.startLoginSession(userId, "AI名称", context, page)
 * └─ 作用：注册新会话，开始30秒超时计时
 *
 * 第3步：发送二维码（带验证）
 * ├─ 获取二维码URL
 * ├─ 调用：sendQrCodeWithValidation(userId, "AI名称", url, "RETURN_PC_XXX_QRURL")
 * └─ 作用：验证会话有效性并发送，防止超时或串码
 *
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *
 * 💡 完整示例代码：
 *
 * {@code
 * @GetMapping("/getNewAiQrCode")
 * public String getNewAiQrCode(@RequestParam("userId") String userId) throws Exception {
 *     // 🔥 第1步：准备登录会话（强制清理旧会话）
 *     String sessionKey = loginSessionManager.prepareLoginSession(userId, "NewAI");
 *
 *     // 🔥 第2步：创建新的BrowserContext并注册会话
 *     try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "newai")) {
 *         Page page = browserUtil.getOrCreatePage(context);
 *
 *         // 注册新的登录会话（开始30秒超时计时）
 *         sessionKey = loginSessionManager.startLoginSession(userId, "NewAI", context, page);
 *
 *         // 导航到AI登录页面
 *         page.navigate("https://newai.example.com/login");
 *         Thread.sleep(2000);
 *
 *         // 获取二维码URL（使用工具类或截图）
 *         String url = screenshotUtil.screenshotAndUpload(page, "newai_qrcode.png");
 *
 *         // 🔥 第3步：使用带验证的方法发送二维码
 *         String result = sendQrCodeWithValidation(userId, "NewAI", url, "RETURN_PC_NEWAI_QRURL");
 *         if (result == null) {
 *             // 会话验证失败（超时或用户已切换），终止流程
 *             return "SERVICE_UNAVAILABLE";
 *         }
 *
 *         // 🔥 第4步：循环检查登录状态（推荐方式）
 *         for (int i = 0; i < 30; i++) {
 *             // ⚠️ 每次循环都要检查会话是否仍然活跃
 *             if (!loginSessionManager.isSessionActive(sessionKey)) {
 *                 return "session_terminated";
 *             }
 *
 *             Thread.sleep(2000);
 *
 *             // 检查是否登录成功
 *             String loginStatus = checkNewAiLogin(page);
 *             if (!"false".equals(loginStatus)) {
 *                 // 登录成功，发送状态消息
 *                 JSONObject statusObject = new JSONObject();
 *                 statusObject.put("status", loginStatus);
 *                 statusObject.put("userId", userId);
 *                 statusObject.put("type", "RETURN_NEWAI_STATUS");
 *                 webSocketClientService.sendMessage(statusObject.toJSONString());
 *
 *                 // 结束会话
 *                 loginSessionManager.endLoginSession(sessionKey);
 *                 return loginStatus;
 *             }
 *         }
 *
 *         // 超时未登录
 *         loginSessionManager.endLoginSession(sessionKey);
 *         return "false";
 *
 *     } catch (Exception e) {
 *         // 🔥 检查是否是严重错误
 *         String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
 *         if (errorMsg.contains("个人资料") || errorMsg.contains("crashed")) {
 *             handleCriticalErrorAndCleanup(userId, "NewAI", e.getMessage());
 *         }
 *         throw e;
 *     } finally {
 *         // 🔥 确保清理会话
 *         if (sessionKey != null) {
 *             loginSessionManager.endLoginSession(sessionKey);
 *         }
 *     }
 * }
 * }
 *
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *
 * ⚠️ 关键注意事项：
 *
 * 1. 会话超时机制
 *    └─ 登录会话默认30秒后自动失效
 *    └─ 必须在循环中调用 isSessionActive() 检查会话状态
 *    └─ 会话失效后立即返回 "session_terminated"
 *
 * 2. 防串码机制
 *    └─ prepareLoginSession 强制清理旧会话
 *    └─ sendQrCodeWithValidation 二次验证会话有效性
 *    └─ 确保用户只能看到当前AI的二维码
 *
 * 3. 资源清理
 *    └─ try-with-resources 自动关闭BrowserContext
 *    └─ finally块确保调用 endLoginSession
 *    └─ 异常时调用 handleCriticalErrorAndCleanup 清理所有会话
 *
 * 4. 前端配置
 *    └─ 在数据库表 sys_aiagent 中添加新AI记录
 *    └─ 配置 websocketQrcodeType：PLAY_GET_NEWAI_QRCODE
 *    └─ 配置 websocketCheckType：PLAY_CHECK_NEWAI_LOGIN
 *    └─ 前端会自动识别并调用对应接口
 *
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *
 * 📖 参考现有实现：
 * - 豆包（Doubao）：getDBQrCode() - 标准流程
 * - 百度AI（Baidu）：getBaiduQrCode() - 包含状态检查
 * - DeepSeek：getDSQrCode() - 二维码刷新逻辑
 * - 秘塔（Metaso）：getMetasoQrCode() - 模态框处理
 *
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */
@RestController
@RequestMapping("/api/browser")
@Tag(name = "浏览器控制器", description = "处理浏览器相关操作")
@Slf4j
public class BrowserController {

    /**
     * 🔥 【核心方法】统一的二维码发送方法（带身份验证和防串码）
     *
     * 📌 核心作用：
     *   1. 验证当前用户的活跃会话是否匹配该AI
     *   2. 防止重试逻辑发送错误AI的二维码（防串码）
     *   3. 确保用户看到的二维码与当前操作的AI一致
     *   4. 验证失败时返回错误提示，让用户重新操作
     *
     * 📌 防串码机制：
     *   - prepareLoginSession已经强制清理了旧会话
     *   - 此方法再次验证确保会话一致性
     *   - 双重保障，确保100%不串码
     *
     * 📌 验证逻辑：
     *   - 检查会话是否活跃
     *   - 检查用户是否只有当前AI的会话
     *   - 如果验证失败，说明用户已经切换或关闭了窗口
     *
     * 📌 错误处理：
     *   - 验证失败时，不清理会话（已经由prepareLoginSession清理）
     *   - 返回null表示验证失败，调用方应终止流程
     *   - 发送友好错误提示给前端
     *
     * 📌 使用方法（添加新AI时参考）：
     *   ```java
     *   // 1. 准备会话（强制清理旧会话）
     *   String sessionKey = loginSessionManager.prepareLoginSession(userId, "AI名称");
     *
     *   // 2. 创建BrowserContext并注册会话
     *   ...
     *
     *   // 3. 获取二维码URL
     *   String url = xxxUtil.getQRCode(...);
     *
     *   // 4. 使用此方法发送（带二次验证）
     *   String result = sendQrCodeWithValidation(userId, "AI名称", url, "RETURN_PC_XXX_QRURL");
     *   if (result == null) {
     *       // 验证失败，终止流程
     *       return "SERVICE_UNAVAILABLE";
     *   }
     *   // 验证通过，继续后续逻辑
     *   ```
     *
     * @param userId 用户ID
     * @param aiType AI类型（Baidu、Doubao、DeepSeek、TongYi、Metaso、知乎直答等）
     * @param url 二维码URL
     * @param messageType WebSocket消息类型（如RETURN_PC_BAIDU_QRURL）
     * @return 如果验证通过返回URL，否则返回null
     */
    private String sendQrCodeWithValidation(String userId, String aiType, String url, String messageType) {
        // 关键验证：确保当前用户的活跃会话是这个AI（包含30秒超时检查）
        if (!loginSessionManager.validateCurrentSession(userId, aiType)) {
            // 会话失效或超时，发送错误提示
            JSONObject errorObject = new JSONObject();
            errorObject.put("type", messageType);
            errorObject.put("userId", userId);
            errorObject.put("url", "");
            errorObject.put("error", "timeout");
            webSocketClientService.sendMessage(errorObject.toJSONString());
            return null;
        }

        // 验证通过，发送二维码
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("userId", userId);
        jsonObject.put("type", messageType);
        webSocketClientService.sendMessage(jsonObject.toJSONString());

        return url;
    }

    @Autowired
    private MetasoUtil metasoUtil;
    // 浏览器操作工具类
    @Autowired
    private ScreenshotUtil screenshotUtil;

    @Autowired
    private WebSocketClientService webSocketClientService;

    @Value("${cube.url}")
    private String logUrl;

    @Autowired
    private LogMsgUtil logMsgUtil;

    @Autowired
    private BrowserUtil browserUtil;

    @Autowired
    private BaiduUtil baiduUtil;

    @Autowired
    private DeepSeekUtil deepSeekUtil;

    @Autowired
    private ZhiHuUtil zhiHuUtil;
    @Autowired
    private TongYiUtil tongYiUtil;

    @Autowired
    private DouBaoUtil douBaoUtil;

    @Autowired
    private LoginSessionManager loginSessionManager;

    @Value("${cube.url}")
    private String url;
    public static final ConcurrentHashMap<String, String> loginMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Integer> lockMap = new ConcurrentHashMap<>();


    /**
     * 获取秘塔登录二维码
     *
     * @param userId 用户唯一标识
     * @return 二维码图片URL 或 "false"表示失败
     */
    @Operation(summary = "获取秘塔登录二维码", description = "返回二维码截图 URL 或 false 表示失败")
    @GetMapping("/getMetasoQrCode")
    public String getMetasoQrCode(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws Exception {
        // 🔥 第1步：准备登录会话（强制清理旧会话）
        String sessionKey = loginSessionManager.prepareLoginSession(userId, "Metaso");

        // 🔥 第2步：创建新的BrowserContext
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "metaso")) {
            Page page = browserUtil.getOrCreatePage(context);

            // 🔥 第3步：注册新的登录会话
            sessionKey = loginSessionManager.startLoginSession(userId, "Metaso", context, page);

            page.navigate("https://metaso.cn/");
            Thread.sleep(2000);

            // 🔥 修复：先关闭可能存在的模态框（如"打开个人资料"弹窗）
            try {
                // 查找模态框的关闭按钮或背景遮罩
                Locator modalBackdrop = page.locator(".MuiBackdrop-root, .MuiModal-backdrop");
                if (modalBackdrop.count() > 0) {
                    // 点击背景遮罩关闭模态框，或按ESC键
                    page.keyboard().press("Escape");
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                // 如果没有模态框，忽略错误
            }

            String s = metasoUtil.checkLogin(page, userId);

            // 未登录
            if (s == null) {
                // 每20秒刷新一次二维码
                for (int j = 0; j < 3; j++) {
                    // 🔥 检查会话是否仍然活跃
                    if (!loginSessionManager.isSessionActive(sessionKey)) {
                        return "session_terminated";
                    }

                    // 🔥 修复：使用更健壮的点击方式，强制点击
                    try {
                        Locator loginLocator = page.locator("//button[contains(text(),'登录/注册')]");
                        // 等待按钮可见
                        loginLocator.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                        // 使用JavaScript强制点击，绕过遮挡检查
                        loginLocator.evaluate("el => el.click()");
                        Thread.sleep(3000);
                    } catch (Exception clickException) {
                        System.err.println("❌ [Metaso登录] 点击登录按钮失败，尝试备用方案: " + clickException.getMessage());
                        // 备用方案：直接导航到登录页面
                        page.navigate("https://metaso.cn/login");
                        Thread.sleep(3000);
                    }
                    String url = screenshotUtil.screenshotAndUpload(page, "checkMetasoLogin.png");

                    // 🔥 【重要】返回前进行身份验证
                    // 确保返回的二维码属于当前用户正在操作的Metaso
                    String result = sendQrCodeWithValidation(userId, "Metaso", url, "RETURN_PC_METASO_QRURL");
                    if (result == null) {
                        // 已清空所有登录会话，返回友好提示
                        return "SERVICE_UNAVAILABLE";
                    }

                    for (int i = 0; i < 10; i++) {
                        // 🔥 检查会话是否仍然活跃
                        if (!loginSessionManager.isSessionActive(sessionKey)) {
                            return "session_terminated";
                        }

                        Thread.sleep(2000);
                        String userName = metasoUtil.checkLogin(page, userId);
                        if (userName != null) {
                            loginSessionManager.endLoginSession(sessionKey);
                            return userName;
                        }
                    }
                }
                loginSessionManager.endLoginSession(sessionKey);
            } else {
                JSONObject jsonObjectTwo = new JSONObject();
                jsonObjectTwo.put("status", s);
                jsonObjectTwo.put("userId", userId);
                jsonObjectTwo.put("type", "RETURN_METASO_STATUS");
                webSocketClientService.sendMessage(jsonObjectTwo.toJSONString());
                loginSessionManager.endLoginSession(sessionKey);
                return s;
            }
        } catch (Exception e) {
            // 🔥 检查错误类型
            String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

            // 静默处理TargetClosedError和"页面已关闭"错误（会话已清理，页面关闭是正常的）
            boolean isTargetClosed = (errorMsg.contains("target") && errorMsg.contains("closed")) ||
                                      errorMsg.contains("页面已关闭");

            if (!isTargetClosed) {
                // 非TargetClosedError才打印日志
                System.err.println("❌ [Metaso登录] 获取登录二维码失败: " + e.getMessage());

                // 检查是否是严重错误（如个人资料错误、页面崩溃等）
                if (errorMsg.contains("个人资料") || errorMsg.contains("profile") ||
                    errorMsg.contains("crashed") || errorMsg.contains("崩溃") ||
                    errorMsg.contains("context") && errorMsg.contains("closed")) {
                    // 遇到严重错误，强制清理该用户的所有会话（保留元宝持久化）
                    handleCriticalErrorAndCleanup(userId, "秘塔", e.getMessage());
                }
            }

            throw e;
        } finally {
            // 🔥 确保无论如何都清理会话记录
            if (sessionKey != null) {
                loginSessionManager.endLoginSession(sessionKey);
            }
        }
        return "false";
    }


    /**
     * 检查秘塔登录状态
     *
     * @param userId 用户唯一标识
     * @return 登录状态："false"表示未登录，手机号表示已登录
     */
    @Operation(summary = "检查秘塔登录状态", description = "返回登录表示已登录，false 表示未登录")
    @GetMapping("/checkMetasoLogin")
    public String checkMetasoLogin(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws Exception {
        String key = userId + "-mt";
        if (loginMap.containsKey(key)) {
            // 如果当前用户正在处理，则返回"处理中"
            return loginMap.get(key);
        }
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "metaso")) {
            Page page = browserUtil.getOrCreatePage(context);
            page.navigate("https://metaso.cn/");
            Thread.sleep(5000);
            String s = metasoUtil.checkLogin(page, userId);
            if (s == null) {
                return "false";
            }
            loginMap.put(key, s);
            return s;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 检查通义AI登录状态
     *
     * @param userId 用户唯一标识
     * @return 登录状态："false"表示未登录，加密的用户名/手机号表示已登录
     */
    @Operation(summary = "检查通义AI登录状态", description = "返回用户名/手机号表示已登录，false 表示未登录")
    @GetMapping("/checkTongYiLogin")
    public String checkTongYiLogin(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) {
        String key = userId + "-ty";
        if (loginMap.containsKey(key)) {
            // 如果当前用户正在处理，则返回"处理中"
            return loginMap.get(key);
        }
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "ty")) {
            Page page = browserUtil.getOrCreatePage(context);
            page.navigate("https://www.tongyi.com/");
            page.waitForTimeout(5000);

            Locator loginButton = page.locator("//*[@id=\"new-nav-tab-wrapper\"]/div[2]/li");

            if (loginButton.count() > 0 && loginButton.isVisible()) {
                // 如果找到“登录”按钮，说明未登录
                return "false";
            } else {
                Locator userAvatarArea = page.locator(".popupUser");
                if (userAvatarArea.count() > 0) {
                    userAvatarArea.hover();
                    page.waitForTimeout(1000);

                    Locator userNameElement = page.locator(".userName");
                    if (userNameElement.count() > 0 && userNameElement.isVisible()) {
                        loginMap.put(key, userNameElement.textContent());
                        // 返回获取到的用户名
                        return userNameElement.textContent();
                    }
                }
                return "false";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取通义千问登录二维码
     *
     * @param userId 用户唯一标识
     * @return 二维码图片URL 或 "false"表示失败
     */
    @Operation(summary = "获取通义千问登录二维码", description = "返回二维码截图 URL 或 false 表示失败")
    @GetMapping("/getTongYiQrCode")
    public String getTongYiQrCode(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws IOException {
        // 🔥 第1步：准备登录会话（强制清理旧会话）
        // prepareLoginSession现在总是返回非null值，会强制清理所有旧会话（包括同一个AI的旧会话）
        String sessionKey = loginSessionManager.prepareLoginSession(userId, "TongYi");

        // 🔥 第2步：创建新的BrowserContext
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "ty")) {
            Page page = browserUtil.getOrCreatePage(context);

            // 🔥 第3步：注册新的登录会话
            sessionKey = loginSessionManager.startLoginSession(userId, "TongYi", context, page);

            page.navigate("https://www.tongyi.com/");
            page.waitForTimeout(3000);

            Locator loginButton = page.locator("(//span[contains(text(),'立即登录')])[1]");
            if (loginButton.count() > 0 && loginButton.isVisible()) {
                loginButton.click();
                page.waitForTimeout(2000);
                page.locator("div[class*='qrcodeWrapper']").last().waitFor(new Locator.WaitForOptions().setTimeout(10000));

                String url = screenshotUtil.screenshotAndUpload(page, "checkTongYiLogin.png");

                // 🔥 【重要】返回前进行身份验证
                // 确保返回的二维码属于当前用户正在操作的TongYi
                String result = sendQrCodeWithValidation(userId, "TongYi", url, "RETURN_PC_QW_QRURL");
                if (result == null) {
                    // 已清空所有登录会话，返回友好提示
                    return "SERVICE_UNAVAILABLE";
                }

                // 🔥 使用循环检查登录状态，而不是直接wait 60秒
                Locator userAvatarArea = page.locator(".popupUser");
                try {
                    boolean loginSuccess = false;
                    for (int i = 0; i < 30; i++) { // 30次 x 2秒 = 60秒
                        // 🔥 每次循环都检查会话是否活跃
                        if (!loginSessionManager.isSessionActive(sessionKey)) {
                            return "session_terminated";
                        }

                        page.waitForTimeout(2000);
                        if (userAvatarArea.count() > 0 && userAvatarArea.isVisible()) {
                            loginSuccess = true;
                            break;
                        }
                    }

                    if (!loginSuccess) {
                        System.err.println("⚠️ [通义千问登录] 等待登录超时");
                        loginSessionManager.endLoginSession(sessionKey);
                        return "false";
                    }

                    page.waitForTimeout(3000);

                    if (userAvatarArea.count() > 0) {
                        userAvatarArea.hover();
                        page.waitForTimeout(1000);

                        Locator userNameElement = page.locator(".userName");
                        if (userNameElement.count() > 0 && userNameElement.isVisible()) {
                            JSONObject jsonObjectTwo = new JSONObject();
                            jsonObjectTwo.put("status", userNameElement.textContent());
                            jsonObjectTwo.put("userId", userId);
                            jsonObjectTwo.put("type", "RETURN_TY_STATUS");
                            webSocketClientService.sendMessage(jsonObjectTwo.toJSONString());

                            loginSessionManager.endLoginSession(sessionKey);
                            return userNameElement.textContent();
                        }
                    }
                } catch (Exception waitException) {
                    System.err.println("❌ [通义千问登录] 登录异常: " + waitException.getMessage());
                    loginSessionManager.endLoginSession(sessionKey);
                    return "false";
                }

                loginSessionManager.endLoginSession(sessionKey);
            }
        } catch (Exception e) {
            System.err.println("❌ [通义千问登录] 获取登录二维码失败: " + e.getMessage());
            throw e;
        } finally {
            // 🔥 确保无论如何都清理会话记录
            if (sessionKey != null) {
                loginSessionManager.endLoginSession(sessionKey);
            }
        }
        return "false";
    }


    /**
     * 检查DeepSeek登录状态
     *
     * @param userId 用户唯一标识
     * @return 登录状态："false"表示未登录，手机号表示已登录
     */
    @Operation(summary = "检查DeepSeek登录状态", description = "返回手机号表示已登录，false 表示未登录")
    @GetMapping("/checkDSLogin")
    public String checkDSLogin(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws InterruptedException {

        String key = userId + "-ds";
        if (loginMap.containsKey(key)) {
            String cachedStatus = loginMap.get(key);
            return cachedStatus;
        }

        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "deepseek")) {
            Page page = browserUtil.getOrCreatePage(context);

            // 导航到DeepSeek页面并确保完全加载
            page.navigate("https://chat.deepseek.com/");
            page.waitForLoadState();
            page.waitForTimeout(1500); // 额外等待1.5秒确保页面完全渲染

            // 先使用工具类方法检测
            String loginStatus = deepSeekUtil.checkLoginStatus(page, false);

            // 如果检测到已登录，直接返回
            if (!"false".equals(loginStatus) && !"未登录".equals(loginStatus)) {
                loginMap.put(key, loginStatus);
                return loginStatus;
            }

            // 所有尝试都失败，返回未登录状态
            return "false";
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取DeepSeek登录二维码
     *
     * @param userId 用户唯一标识
     * @return 二维码图片URL 或 "false"表示失败
     */
    @Operation(summary = "获取DeepSeek登录二维码", description = "返回二维码截图 URL 或 false 表示失败")
    @GetMapping("/getDSQrCode")
    public String getDSQrCode(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws Exception, IOException {
        // 🔥 第1步：准备登录会话（强制清理旧会话）
        // prepareLoginSession现在总是返回非null值，会强制清理所有旧会话（包括同一个AI的旧会话）
        String sessionKey = loginSessionManager.prepareLoginSession(userId, "DeepSeek");

        // 🔥 第2步：创建新的BrowserContext
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "deepseek")) {
            Page page = browserUtil.getOrCreatePage(context);

            // 🔥 第3步：注册新的登录会话
            sessionKey = loginSessionManager.startLoginSession(userId, "DeepSeek", context, page);

            // 首先检查当前登录状态
            String currentStatus = deepSeekUtil.checkLoginStatus(page, true);
            if (!"false".equals(currentStatus)) {
                // 已经登录，直接返回状态
                JSONObject statusObject = new JSONObject();
                statusObject.put("status", currentStatus);
                statusObject.put("userId", userId);
                statusObject.put("type", "RETURN_DEEPSEEK_STATUS");
                webSocketClientService.sendMessage(statusObject.toJSONString());

                // 截图返回当前页面
                String url = screenshotUtil.screenshotAndUpload(page, "deepseekLoggedIn.png");
                JSONObject qrUpdateObject = new JSONObject();
                qrUpdateObject.put("url", url);
                qrUpdateObject.put("userId", userId);
                qrUpdateObject.put("type", "RETURN_PC_DEEPSEEK_QRURL");
                webSocketClientService.sendMessage(qrUpdateObject.toJSONString());

                loginSessionManager.endLoginSession(sessionKey);
                return url;
            }

            // 未登录，获取二维码截图URL
            System.out.println("📱 [DeepSeek] 开始获取二维码...");
            
            // 🔥 【速度优化】快速导航到登录页面，减少等待时间
            try {
                System.out.println("📱 [DeepSeek] 开始快速导航...");
                page.navigate("https://chat.deepseek.com/sign_in");
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                System.out.println("📱 [DeepSeek] 页面基本加载完成");
                
                // 🔥 减少等待时间，从5秒减少到2秒
                Thread.sleep(2000);
                System.out.println("📱 [DeepSeek] 快速等待完成，准备截图");
                
            } catch (Exception navError) {
                System.err.println("❌ [DeepSeek] 导航失败: " + navError.getMessage());
                loginSessionManager.endLoginSession(sessionKey);
                return "false";
            }
            
            // 🔥 【简化修复】直接截图，参考豆包的实现方式
            String url = screenshotUtil.screenshotAndUpload(page, "checkDeepSeekLogin.png");
            System.out.println("📱 [DeepSeek] 直接截图结果: " + url);

            if (url != null && !url.trim().isEmpty() && !"false".equals(url)) {
                // 🔥 【简化修复】直接发送二维码，不使用会话验证，参考豆包实现
                System.out.println("📱 [DeepSeek] 准备发送二维码到前端: " + url);
                
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", url);
                jsonObject.put("userId", userId);
                jsonObject.put("type", "RETURN_PC_DEEPSEEK_QRURL");
                webSocketClientService.sendMessage(jsonObject.toJSONString());
                
                System.out.println("✅ [DeepSeek] 二维码已成功发送到前端");
                logMsgUtil.sendTaskLog("DeepSeek二维码已发送到前端", userId, "DeepSeek");

                // 实时监测登录状态 - 最多等待60秒
                int maxAttempts = 30; // 30次尝试
                for (int i = 0; i < maxAttempts; i++) {
                    // 🔥 检查会话是否仍然活跃
                    if (!loginSessionManager.isSessionActive(sessionKey)) {
                        return "session_terminated";
                    }

                    // 每2秒检查一次登录状态（不刷新页面）
                    Thread.sleep(2000);

                    // 检查当前页面登录状态
                    String loginStatus = deepSeekUtil.checkLoginStatus(page, false);

                    if (!"false".equals(loginStatus)) {
                        // 登录成功，发送状态到WebSocket
                        JSONObject jsonObjectTwo = new JSONObject();
                        jsonObjectTwo.put("status", loginStatus);
                        jsonObjectTwo.put("userId", userId);
                        jsonObjectTwo.put("type", "RETURN_DEEPSEEK_STATUS");
                        webSocketClientService.sendMessage(jsonObjectTwo.toJSONString());

                        // 登录成功，结束会话
                        loginSessionManager.endLoginSession(sessionKey);
                        break;
                    }

                    // 每5次尝试重新截图一次，可能二维码已更新
                    if (i % 5 == 4) {
                        try {
                            // 🔥 再次检查会话状态
                            if (!loginSessionManager.isSessionActive(sessionKey)) {
                                return "session_terminated";
                            }

                            url = screenshotUtil.screenshotAndUpload(page, "checkDeepSeekLogin.png");
                            JSONObject qrRefreshObject = new JSONObject();
                            qrRefreshObject.put("url", url);
                            qrRefreshObject.put("userId", userId);
                            qrRefreshObject.put("type", "RETURN_PC_DEEPSEEK_QRURL");
                            webSocketClientService.sendMessage(qrRefreshObject.toJSONString());
                        } catch (Exception e) {
                            UserLogUtil.sendExceptionLog(userId, "deepSeek获取二维码截图失败", "checkDeepSeekLogin", e, logUrl + "/saveLogInfo");
                        }
                    }
                }

                // 监测结束，清理会话
                loginSessionManager.endLoginSession(sessionKey);
                return url;
            }

            // 获取二维码失败，清理会话
            System.err.println("❌ [DeepSeek] 二维码获取失败，返回值: " + url);
            loginSessionManager.endLoginSession(sessionKey);
        } catch (Exception e) {
            System.err.println("❌ [DeepSeek登录] 获取登录二维码失败: " + e.getMessage());

            // 🔥 检查是否是严重错误（如个人资料错误、页面崩溃等）
            String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (errorMsg.contains("个人资料") || errorMsg.contains("profile") ||
                errorMsg.contains("crashed") || errorMsg.contains("崩溃") ||
                errorMsg.contains("context") && errorMsg.contains("closed")) {
                // 遇到严重错误，强制清理该用户的所有会话（保留元宝持久化）
                handleCriticalErrorAndCleanup(userId, "DeepSeek", e.getMessage());
            }

            throw e;
        } finally {
            // 🔥 确保无论如何都清理会话记录
            if (sessionKey != null) {
                loginSessionManager.endLoginSession(sessionKey);
            }
        }
        return "false";
    }


    /**
     * 检查元宝主站登录状态
     *
     * @param userId 用户唯一标识
     * @return 登录状态："false"表示未登录，手机号表示已登录
     */
    @Operation(summary = "检查元宝登录状态", description = "返回手机号表示已登录，false 表示未登录")
    @GetMapping("/checkLogin")
    public String checkYBLogin(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws InterruptedException {
        try {
            String key = userId + "-yb";
//            加锁，同一个用户只能有一个检查
            if ((loginMap.get(key) == null || loginMap.get(key).contains("未登录")) && lockMap.get(key) == null) {
                loginMap.remove(key);
                lockMap.put(key, 1);
                UnPersisBrowserContextInfo browserContextInfo = BrowserContextFactory.getBrowserContext(userId, 2);
                BrowserContext browserContext = null;
                if (browserContextInfo != null) {
                    browserContext = browserContextInfo.getBrowserContext();
                }
                Page page = browserContext.pages().get(0);
                page.navigate("https://yuanbao.tencent.com/chat/naQivTmsDa/");
                page.waitForLoadState(LoadState.LOAD);
                Thread.sleep(3000);

                // 🔥 新增：页面加载后检测账号类型选择弹窗
                handleAccountTypeSelection(page);

                Locator phone = page.locator("//p[@class='nick-info-name']");
                if (phone.count() > 0) {
                    String phoneText = phone.textContent();
                    if (phoneText.equals("未登录")) {
                        loginMap.put(key, "未登录");
                        lockMap.remove(key);
                        return "false";
                    }
                    loginMap.put(key, phoneText);
                    lockMap.remove(key);
                    return phoneText;
                } else {
                    loginMap.put(key, "未登录");
                    lockMap.remove(key);
                    return "false";
                }
            } else {
                // 等待其他线程检测登录状态
                for (int i = 0; i < 10; i++) {
                    if (loginMap.get(key) != null) {
                        if (loginMap.get(key).contains("未登录")) {
                            return "false";
                        } else {
                            return loginMap.get(key);
                        }
                    }
                    Thread.sleep(3000);
                }
                log.info("检测超时");
                return "false";
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 获取代理版元宝登录二维码
     *
     * @param userId 用户唯一标识
     * @return 二维码图片URL 或 "false"表示失败
     */
    @GetMapping("/getYBQrCode")
    @Operation(summary = "获取代理版元宝登录二维码", description = "返回二维码截图 URL 或 false 表示失败")
    public String getYBQrCode(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws InterruptedException, IOException {

        // 🔥 第1步：准备登录会话（强制清理旧会话）
        String sessionKey = loginSessionManager.prepareLoginSession(userId, "YuanBao");

        try {
            UnPersisBrowserContextInfo browserContextInfo = BrowserContextFactory.getBrowserContext(userId, 2);
            BrowserContext context = null;
            if (browserContextInfo != null) {
                context = browserContextInfo.getBrowserContext();
            }

            if (context == null) {
                return "false";
            }

            // 🔥 确保页面存在且未关闭，如果不存在或已关闭则创建新页面
            Page page = null;
            try {
                boolean needNewPage = true;
                if (!context.pages().isEmpty()) {
                    Page existingPage = context.pages().get(0);
                    // 检查页面是否已关闭
                    if (!existingPage.isClosed()) {
                        page = existingPage;
                        needNewPage = false;
                    } else {
                    }
                }

                if (needNewPage) {
                    page = context.newPage();
                }
            } catch (Exception e) {
                loginSessionManager.endLoginSession(sessionKey);
                return "false";
            }

            // 🔥 第2步：注册新的登录会话（元宝使用持久化BrowserContext，设置为true）
            loginSessionManager.startLoginSession(userId, "YuanBao", context, page, true);

            // 🔥 关键修复：等待页面完全稳定后再操作，避免旧的异步操作干扰
            try {
                // 先等待当前页面的所有待处理操作完成
                page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(3000));
            } catch (Exception e) {
                // 如果超时或失败，继续执行（页面可能已经是空白状态）
            }

            // 导航到元宝登录页面
            page.navigate("https://yuanbao.tencent.com/chat/naQivTmsDa", new Page.NavigateOptions().setTimeout(15000));
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            
            // 等待并点击登录按钮
            try {
                Locator loginButton = page.locator("//span[contains(text(),'登录')]");
                loginButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
                loginButton.click();
                
                // 🔥 修复：点击登录后，等待二维码页面加载完成
                Thread.sleep(2000);
                
                // 🔥 修复：显式等待二维码容器出现，确保页面已加载
                try {
                    // 等待二维码相关元素出现（元宝的二维码通常在iframe或特定容器中）
                    page.locator(".qrcode, canvas, img[src*='qr'], iframe").first()
                        .waitFor(new Locator.WaitForOptions().setTimeout(5000));
                    System.out.println("✅ [元宝登录] 二维码页面已加载");
                } catch (Exception qrWaitError) {
                    // 如果特定元素未找到，继续执行（可能页面结构不同）
                    System.out.println("⚠️ [元宝登录] 未检测到二维码元素，继续截图");
                }
                
            } catch (Exception clickError) {
                // 如果已经在登录页面，忽略点击错误
                System.out.println("⚠️ [元宝登录] 登录按钮点击失败或已在登录页: " + clickError.getMessage());
            }

            // 🔥 修复：再等待1秒确保页面完全渲染
            Thread.sleep(1000);

            // 🔥 检查会话是否仍然活跃
            if (!loginSessionManager.isSessionActive(sessionKey)) {
                loginSessionManager.endLoginSession(sessionKey);
                return "session_terminated";
            }

            // 🔥 【修复】先检查用户是否已经登录，避免已登录用户收到二维码截图
            String currentLoginStatus = "未登录";
            try {
                Locator phone = page.locator("//p[@class='nick-info-name']");
                phone.waitFor(new Locator.WaitForOptions().setTimeout(3000));
                currentLoginStatus = phone.textContent();
            } catch (Exception e) {
                // 如果检测失败，默认为未登录
                currentLoginStatus = "未登录";
            }
            
            // 如果用户已经登录，直接返回登录状态，不发送二维码
            if (!currentLoginStatus.contains("未登录")) {
                System.out.println("🎉 [元宝登录] 用户已登录，直接返回状态: " + currentLoginStatus);
                
                // 发送登录状态给前端
                JSONObject statusObject = new JSONObject();
                statusObject.put("status", currentLoginStatus);
                statusObject.put("userId", userId);
                statusObject.put("type", "RETURN_YB_STATUS");
                webSocketClientService.sendMessage(statusObject.toJSONString());
                
                // 结束会话并返回
                loginSessionManager.endLoginSession(sessionKey);
                return currentLoginStatus;
            }
            
            // 用户未登录，发送二维码截图
            System.out.println("📱 [元宝登录] 用户未登录，发送二维码截图");
            String url = screenshotUtil.screenshotAndUpload(page, "checkYBLogin.png");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);
            jsonObject.put("userId", userId);
            jsonObject.put("type", "RETURN_PC_YB_QRURL");
            webSocketClientService.sendMessage(jsonObject.toJSONString());

            // 尝试处理账号类型选择弹窗（初始检查）
            handleAccountTypeSelection(page);

            boolean isLogin = false;
            String phoneText = currentLoginStatus; // 🔥 使用上面已经检测到的状态
            
            // 🔥 注意：如果程序运行到这里，说明用户是未登录状态，需要等待扫码

            // 🔥 优化：将检测循环从6次x10秒改为60次x1秒，确保用户登录后2秒内响应
            for (int i = 0; i < 60; i++) {
                // 🔥 每次循环检查会话是否仍然活跃
                if (!loginSessionManager.isSessionActive(sessionKey)) {
                    loginSessionManager.endLoginSession(sessionKey);
                    return "session_terminated";
                }

                if (phoneText.contains("未登录")) {
                    Thread.sleep(1000); // 🔥 优化：从10秒减少到1秒，大幅提升响应速度

                    // 🔥 再次检查会话（等待后可能已切换）
                    if (!loginSessionManager.isSessionActive(sessionKey)) {
                        loginSessionManager.endLoginSession(sessionKey);
                        return "session_terminated";
                    }

                    // 🔥 优化：每10秒刷新一次二维码（每10次循环）
                    if (i % 10 == 9) {
                        // 🔥 【修复】刷新二维码前再次检查登录状态，避免已登录用户收到额外截图
                        try {
                            Locator phoneCheck = page.locator("//p[@class='nick-info-name']");
                            phoneCheck.waitFor(new Locator.WaitForOptions().setTimeout(1000));
                            String checkStatus = phoneCheck.textContent();
                            if (!checkStatus.contains("未登录")) {
                                // 用户已登录，不再刷新二维码
                                System.out.println("📱 [元宝登录] 检测到用户已登录，停止刷新二维码");
                                phoneText = checkStatus; // 更新状态以退出循环
                                break;
                            }
                        } catch (Exception checkError) {
                            // 检查失败，继续刷新二维码
                        }
                        
                        url = screenshotUtil.screenshotAndUpload(page, "checkYBLogin.png");
                        jsonObject.put("url", url);
                        webSocketClientService.sendMessage(jsonObject.toJSONString());
                        System.out.println("🔄 [元宝登录] 已刷新二维码截图");
                        
                        // 再次尝试处理账号类型选择弹窗
                        handleAccountTypeSelection(page);
                    }
                    
                    // 🔥 新增：每5次循环检测一次账号类型选择弹窗（更频繁的检测）
                    if (i % 5 == 0) {
                        handleAccountTypeSelection(page);
                    }
                } else {
                    break;
                }
                
                // 🔥 关键修复：使用try-catch包装textContent调用
                try {
                    Locator phone = page.locator("//p[@class='nick-info-name']");
                    phone.waitFor(new Locator.WaitForOptions().setTimeout(1000)); // 🔥 优化：从3秒减少到1秒
                    phoneText = phone.textContent();
                } catch (Exception e) {
                    phoneText = "未登录";
                }
            }

            // 发送最终登录状态
            JSONObject jsonObjectTwo = new JSONObject();
            if (phoneText.contains("未登录")) {
                jsonObjectTwo.put("status", "false");
            } else {
                isLogin = true;
                jsonObjectTwo.put("status", phoneText);
                System.out.println("🎉 [元宝登录] 最终状态：已登录 - " + phoneText);
            }
            jsonObjectTwo.put("userId", userId);
            jsonObjectTwo.put("type", "RETURN_YB_STATUS");
            webSocketClientService.sendMessage(jsonObjectTwo.toJSONString());

            // 🔥 第3步：登录完成，结束会话
            loginSessionManager.endLoginSession(sessionKey);

            return isLogin ? phoneText : "false";
        } catch (Exception e) {
            System.err.println("❌ [元宝登录] 获取元宝二维码失败: " + e.getMessage());

            // 🔥 检查是否是严重错误（如个人资料错误、页面崩溃等）
            String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            
            // 🔥 静默处理"Object doesn't exist"错误（持久化Context的正常现象）
            boolean isObjectNotExist = errorMsg.contains("object") && errorMsg.contains("doesn't exist");
            
            if (!isObjectNotExist && (errorMsg.contains("个人资料") || errorMsg.contains("profile") ||
                errorMsg.contains("crashed") || errorMsg.contains("崩溃") ||
                errorMsg.contains("context") && errorMsg.contains("closed"))) {
                // 遇到严重错误，强制清理该用户的所有会话
                handleCriticalErrorAndCleanup(userId, "元宝", e.getMessage());
            } else {
                // 普通错误或"Object doesn't exist"，只结束当前会话
                loginSessionManager.endLoginSession(sessionKey);
            }

            log.error("获取元宝二维码失败", e);
            throw e;
        }
    }

    /**
     * 🔥 【紧急清理】处理严重错误时清理该用户的所有会话
     *
     * 📌 使用场景：
     *   - 遇到"打开您的个人资料时出了点问题"
     *   - 页面崩溃、Context损坏等严重错误
     *   - 需要强制清理所有资源重新开始
     *
     * 📌 清理策略：
     *   - 清理该用户的所有登录会话
     *   - 持久化AI（元宝）：只标记失效，保持Page和Context开启
     *   - 非持久化AI：完全关闭Page和Context
     *   - 释放该用户的所有锁
     *
     * @param userId 用户ID
     * @param aiType 当前AI类型（用于日志）
     * @param errorMsg 错误信息
     */
    private void handleCriticalErrorAndCleanup(String userId, String aiType, String errorMsg) {
        System.out.println("🚨 [紧急清理] " + aiType + "遇到严重错误，开始清理用户" + userId + "的所有会话");
        System.out.println("   错误信息: " + errorMsg);

        try {
            // 🔥 强制清理该用户的所有登录会话（持久化AI会保留Page和Context）
            loginSessionManager.clearAllUserLoginSessions(userId);
        } catch (Exception e) {
            System.err.println("❌ [紧急清理] 清理失败: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // 提取账号选择处理为独立方法，增强异常处理
    private void handleAccountTypeSelection(Page page) {
        try {
            System.out.println("🔍 [元宝登录] 开始检测账号类型选择弹窗");
            
            // 🔥 修复：增加多种弹窗检测方式，适应不同的页面结构
            // 方案1：检测包含"选择账号类型"文本的弹窗
            Locator accountTypeModal1 = page.locator(".choose-content:has-text('选择账号类型')");
            // 方案2：检测包含"个人账号"和"团队账号"的弹窗容器
            Locator accountTypeModal2 = page.locator("div:has-text('个人账号'):has-text('团队账号')");
            // 方案3：检测包含账号选择按钮的容器
            Locator accountTypeModal3 = page.locator(".ybc-login-account-list_personal").locator("xpath=ancestor::div[contains(@class,'modal') or contains(@class,'dialog') or contains(@class,'choose')]");
            
            boolean hasModal = false;
            Locator activeModal = null;
            
            // 检测哪种弹窗存在
            try {
                if (accountTypeModal1.count() > 0 && accountTypeModal1.isVisible()) {
                    hasModal = true;
                    activeModal = accountTypeModal1;
                    System.out.println("✅ [元宝登录] 检测到账号类型选择弹窗 (方案1)");
                } else if (accountTypeModal2.count() > 0 && accountTypeModal2.isVisible()) {
                    hasModal = true;
                    activeModal = accountTypeModal2;
                    System.out.println("✅ [元宝登录] 检测到账号类型选择弹窗 (方案2)");
                } else if (accountTypeModal3.count() > 0 && accountTypeModal3.isVisible()) {
                    hasModal = true;
                    activeModal = accountTypeModal3;
                    System.out.println("✅ [元宝登录] 检测到账号类型选择弹窗 (方案3)");
                }
            } catch (Exception e) {
                System.out.println("⚠️ [元宝登录] 弹窗检测异常: " + e.getMessage());
            }

            if (!hasModal) {
                System.out.println("ℹ️ [元宝登录] 未检测到账号类型选择弹窗，可能已选择或不需要选择");
                return;
            }

            System.out.println("🎯 [元宝登录] 开始选择个人账号");
            
            // 🔥 修复：增加多种个人账号按钮定位方式
            boolean buttonClicked = false;
            
            // 方案1：通过class定位个人账号按钮
            try {
                Locator personalAccountBtn = page.locator(".ybc-login-account-list_personal");
                if (personalAccountBtn.count() > 0 && personalAccountBtn.isVisible() && !isElementDisabled(personalAccountBtn)) {
                    System.out.println("✅ [元宝登录] 使用class选择器点击个人账号按钮");
                    personalAccountBtn.click();
                    buttonClicked = true;
                }
            } catch (Exception e) {
                System.out.println("⚠️ [元宝登录] class选择器失败: " + e.getMessage());
            }
            
            // 方案2：通过文本定位个人账号按钮
            if (!buttonClicked) {
                try {
                    Locator textBasedBtn = page.locator("//span[contains(text(),'个人账号')]");
                    if (textBasedBtn.count() > 0 && textBasedBtn.isVisible()) {
                        System.out.println("✅ [元宝登录] 使用文本选择器点击个人账号按钮");
                        textBasedBtn.click();
                        buttonClicked = true;
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ [元宝登录] 文本选择器失败: " + e.getMessage());
                }
            }
            
            // 方案3：通过更宽泛的文本匹配
            if (!buttonClicked) {
                try {
                    Locator personalBtn = page.locator("*:has-text('个人账号'), *:has-text('个人'), button:has-text('个人')");
                    if (personalBtn.count() > 0) {
                        System.out.println("✅ [元宝登录] 使用宽泛选择器点击个人账号按钮");
                        personalBtn.first().click();
                        buttonClicked = true;
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ [元宝登录] 宽泛选择器失败: " + e.getMessage());
                }
            }
            
            // 方案4：通过按钮索引选择（通常个人账号是第一个）
            if (!buttonClicked) {
                try {
                    Locator allButtons = page.locator("button, .btn, [role='button']").locator("visible=true");
                    if (allButtons.count() >= 2) {
                        System.out.println("✅ [元宝登录] 使用索引选择器点击第一个按钮（通常是个人账号）");
                        allButtons.first().click();
                        buttonClicked = true;
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ [元宝登录] 索引选择器失败: " + e.getMessage());
                }
            }
            
            if (buttonClicked) {
                System.out.println("✅ [元宝登录] 个人账号选择成功，等待页面响应");
                Thread.sleep(2000); // 等待页面响应
                
                // 🔥 验证弹窗是否已关闭
                try {
                    boolean modalStillExists = page.locator(".choose-content:has-text('选择账号类型'), div:has-text('个人账号'):has-text('团队账号')").count() > 0;
                    if (!modalStillExists) {
                        System.out.println("✅ [元宝登录] 账号类型选择弹窗已关闭");
                    } else {
                        System.out.println("⚠️ [元宝登录] 弹窗仍然存在，可能需要额外操作");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ [元宝登录] 弹窗关闭验证失败: " + e.getMessage());
                }
            } else {
                System.out.println("❌ [元宝登录] 未能找到可点击的个人账号按钮");
            }
            
        } catch (Exception e) {
            // 不抛出异常，仅记录日志
            System.err.println("❌ [元宝登录] 账号类型选择弹窗处理失败: " + e.getMessage());
        }
    }

    // 辅助方法：检查元素是否被禁用
    private boolean isElementDisabled(Locator locator) {
        try {
            return locator.getAttribute("class").contains("ybc-login-account-list_disable");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查豆包登录状态
     *
     * @param userId 用户唯一标识
     * @return 登录状态："false"表示未登录，手机号表示已登录
     */
    @Operation(summary = "检查豆包登录状态", description = "返回手机号表示已登录，false 表示未登录")
    @GetMapping("/checkDBLogin")
    public String checkDBLogin(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws InterruptedException {
        String key = userId + "-db";
        if (loginMap.containsKey(key)) {
            // 如果当前用户正在处理，则返回"处理中"
            return loginMap.get(key);
        }
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "db")) {
            Page page = browserUtil.getOrCreatePage(context);
            page.navigate("https://www.doubao.com/chat/");
            
            // 🔥 优化：智能等待页面加载 - 等待登录按钮或头像按钮出现
            // 这样可以适配不同性能的机器：快的机器会快速返回，慢的机器会等待足够时间
            Locator loginButton = page.locator("[data-testid='to_login_button']");
            Locator avatarButton = page.locator("[data-testid=\"chat_header_avatar_button\"]");
            
            boolean pageReady = false;
            for (int i = 0; i < 20; i++) { // 最多等待20秒（20 x 1秒）
                Thread.sleep(1000);
                try {
                    // 检查登录按钮或头像按钮是否出现
                    if ((loginButton.count() > 0 && loginButton.isVisible()) || 
                        (avatarButton.count() > 0 && avatarButton.isVisible())) {
                        pageReady = true;
                        break;
                    }
                } catch (Exception e) {
                    // 页面还在加载，继续等待
                    continue;
                }
            }
            
            if (!pageReady) {
                // 页面加载失败
                return "false";
            }
            
            // 🔥 新增：页面加载完成后检测超能模式弹窗
            douBaoUtil.checkAndClickSuperModeButton(page, userId, "登录状态检测");
            
            // 🔥 优化：检测登录状态
            if (loginButton.count() > 0 && loginButton.isVisible()) {
                // 未登录：直接返回
                return "false";
            }
            
            // 🔥 优化：已登录状态，获取用户信息
            if (avatarButton.count() == 0 || !avatarButton.isVisible()) {
                // 页面异常，返回未登录
                return "false";
            }
            
            // 🔥 新增：获取用户信息前检测超能模式弹窗
            douBaoUtil.checkAndClickSuperModeButton(page, userId, "获取用户信息前");
            
            try {
                avatarButton.click();
                Thread.sleep(500); // 等待下拉菜单展开
                page.locator("[data-testid=\"chat_header_setting_button\"]").click();
                Thread.sleep(500); // 等待设置页面打开
                
                Locator phone = page.locator(".nickName-cIcGuG");
                phone.waitFor(new Locator.WaitForOptions().setTimeout(5000)); // 🔥 优化：增加到5秒，适配低性能主机
                if (phone.count() > 0) {
                    String phoneText = phone.textContent();
                    loginMap.put(key, phoneText);
                    return phoneText;
                }
            } catch (Exception e) {
                // 🔥 优化：获取用户信息失败时直接返回false
                return "false";
            }
            
            return "false";
        } catch (Exception e) {
            // 🔥 优化：异常时返回false
            return "false";
        }
    }

    /**
     * 获取豆包登录二维码
     *
     * @param userId 用户唯一标识
     * @return 二维码图片URL 或 "false"表示失败
     */
    @Operation(summary = "获取豆包登录二维码", description = "返回二维码截图 URL 或 false 表示失败")
    @GetMapping("/getDBQrCode")
    public String getDBQrCode(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws InterruptedException, IOException {
        // 🔥 第1步：准备登录会话（强制清理旧会话）
        // prepareLoginSession现在总是返回非null值，会强制清理所有旧会话（包括同一个AI的旧会话）
        String sessionKey = loginSessionManager.prepareLoginSession(userId, "Doubao");

        // 🔥 第2步：创建新的BrowserContext
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "db")) {
            Page page = browserUtil.getOrCreatePage(context);

            // 🔥 第3步：注册新的登录会话
            sessionKey = loginSessionManager.startLoginSession(userId, "Doubao", context, page);

            page.navigate("https://www.doubao.com/chat/");
            
            // 🔥 优化：智能等待登录按钮出现 - 适配低性能主机
            // 不是固定等待，而是等待元素出现或超时
            Locator locator = page.locator("[data-testid='to_login_button']");
            boolean loginButtonFound = false;
            
            for (int i = 0; i < 30; i++) { // 最多等待30秒
                Thread.sleep(1000);
                try {
                    if (locator.count() > 0 && locator.isVisible()) {
                        loginButtonFound = true;
                        break;
                    }
                } catch (Exception e) {
                    // 页面还在加载，继续等待
                    continue;
                }
            }
            
            if (!loginButtonFound) {
                // 🔥 优化：登录按钮未找到，可能已经登录过了，直接尝试检测头像区域
                System.out.println("⚠️ [豆包登录] 登录按钮未找到，尝试检测是否已登录...");
                
                Locator avatarButton = page.locator("[data-testid=\"chat_header_avatar_button\"]");
                try {
                    if (avatarButton.count() > 0 && avatarButton.isVisible()) {
                        // 已登录，直接获取用户信息
                        System.out.println("✅ [豆包登录] 检测到已登录（头像按钮出现），直接获取用户信息");
                        
                        // 🔥 新增：已登录状态下检测超能模式弹窗
                        douBaoUtil.checkAndClickSuperModeButton(page, userId, "已登录状态检测");
                        
                        try {
                            avatarButton.click();
                            Thread.sleep(800); // 等待下拉菜单展开
                            page.locator("[data-testid=\"chat_header_setting_button\"]").click();
                            Thread.sleep(800); // 等待设置页面打开
                            
                            Locator phone = page.locator(".nickName-cIcGuG");
                            phone.waitFor(new Locator.WaitForOptions().setTimeout(8000));
                            
                            if (phone.count() > 0) {
                                String phoneText = phone.textContent();
                                JSONObject jsonObjectTwo = new JSONObject();
                                jsonObjectTwo.put("status", phoneText);
                                jsonObjectTwo.put("userId", userId);
                                jsonObjectTwo.put("type", "RETURN_DB_STATUS");
                                webSocketClientService.sendMessage(jsonObjectTwo.toJSONString());
                                
                                loginSessionManager.endLoginSession(sessionKey);
                                return phoneText;
                            }
                        } catch (Exception e) {
                            System.err.println("⚠️ [豆包登录] 获取用户信息失败: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ [豆包登录] 检测已登录状态失败: " + e.getMessage());
                }
                
                // 页面加载失败或无法获取用户信息
                loginSessionManager.endLoginSession(sessionKey);
                return "false";
            }
            
            // 🔥 新增：扫码登录开始时检测"试一试"按钮并关闭
            Thread.sleep(1000);
            douBaoUtil.checkAndClickSuperModeButton(page, userId, "扫码登录开始");

            // 🔥 点击登录按钮进入扫码页面
            locator.click();
            page.locator("[data-testid='qrcode_switcher']").evaluate("el => el.click()");

            Thread.sleep(3000);
            String url = screenshotUtil.screenshotAndUpload(page, "checkDBLogin.png");

            // 🔥 【重要】返回前进行身份验证
            // 目的：确保返回的二维码属于当前用户正在操作的AI
            // 步骤：
            //   1. validateCurrentSession检查用户是否只有Doubao这一个活跃会话
            //   2. 如果用户已切换到其他AI，拒绝发送此二维码
            //   3. 终止登录流程，释放资源
            String result = sendQrCodeWithValidation(userId, "Doubao", url, "RETURN_PC_DB_QRURL");
            if (result == null) {
                // 已清空所有登录会话，返回友好提示
                loginSessionManager.endLoginSession(sessionKey);
                return "SERVICE_UNAVAILABLE";
            }

            try {
                // 🔥 优化：智能登录检测 - 适配低性能主机
                // 检测逻辑：头像按钮出现 = 已登录并跳转到聊天页面
                boolean loginSuccess = false;
                Locator avatarButton = page.locator("[data-testid=\"chat_header_avatar_button\"]");
                Locator loginButton = page.locator("[data-testid='to_login_button']"); // 登录按钮（未登录时显示）
                
                // 🔥 优化：改为500ms检测间隔，总等待时间120秒（240次 x 500ms）
                // 低性能主机可能需要更长时间加载页面
                for (int i = 0; i < 240; i++) {
                    // 🔥 每次循环都检查会话是否活跃
                    if (!loginSessionManager.isSessionActive(sessionKey)) {
                        return "session_terminated";
                    }

                    Thread.sleep(500); // 🔥 优化：500ms检测间隔
                    
                    // 🔥 新增：每10次循环检测一次超能模式弹窗（避免过于频繁）
                    if (i % 10 == 0) {
                        douBaoUtil.checkAndClickSuperModeButton(page, userId, "登录检测过程");
                    }
                    
                    // 🔥 关键检测：头像按钮出现且登录按钮消失 = 已登录
                    try {
                        if (avatarButton.count() > 0 && avatarButton.isVisible() && 
                            (loginButton.count() == 0 || !loginButton.isVisible())) {
                            loginSuccess = true;
                            System.out.println("✅ [豆包登录] 检测到已登录（头像按钮出现）");
                            break;
                        }
                    } catch (Exception e) {
                        // 🔥 优化：页面导航时执行上下文被销毁，继续等待
                        String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
                        if (errorMsg.contains("execution context") || errorMsg.contains("destroyed")) {
                            // 页面正在导航，继续循环
                            continue;
                        }
                        // 其他异常才抛出
                        throw e;
                    }
                }

                if (!loginSuccess) {
                    System.err.println("⚠️ [豆包登录] 等待登录超时");
                    loginSessionManager.endLoginSession(sessionKey);
                    return "false";
                }

                // 🔥 已检测到登录，获取用户信息
                Thread.sleep(1000); // 等待页面稳定
                
                // 🔥 新增：登录成功后检测超能模式弹窗
                douBaoUtil.checkAndClickSuperModeButton(page, userId, "登录成功后");
                
                try {
                    avatarButton.click();
                    Thread.sleep(800); // 等待下拉菜单展开
                    page.locator("[data-testid=\"chat_header_setting_button\"]").click();
                    Thread.sleep(800); // 等待设置页面打开
                    
                    Locator phone = page.locator(".nickName-cIcGuG");
                    phone.waitFor(new Locator.WaitForOptions().setTimeout(8000)); // 🔥 优化：增加到8秒，适配低性能主机
                    
                    if (phone.count() > 0) {
                        String phoneText = phone.textContent();
                        JSONObject jsonObjectTwo = new JSONObject();
                        jsonObjectTwo.put("status", phoneText);
                        jsonObjectTwo.put("userId", userId);
                        jsonObjectTwo.put("type", "RETURN_DB_STATUS");
                        webSocketClientService.sendMessage(jsonObjectTwo.toJSONString());

                        loginSessionManager.endLoginSession(sessionKey);
                        return phoneText;
                    }
                } catch (Exception e) {
                    // 🔥 优化：超时或异常时直接返回false
                    if (e instanceof TimeoutError) {
                        System.err.println("⚠️ [豆包登录] 获取用户信息超时（页面加载慢或元素不存在）");
                    } else {
                        String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
                        if (!errorMsg.contains("target") || !errorMsg.contains("closed")) {
                            System.err.println("❌ [豆包登录] 获取用户信息异常: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception loginException) {
                // 🔥 静默处理TargetClosedError（会话已清理，页面关闭是正常的）
                String errorMsg = loginException.getMessage() != null ? loginException.getMessage().toLowerCase() : "";
                if (!errorMsg.contains("target") || !errorMsg.contains("closed")) {
                    // 非TargetClosedError才打印日志
                    System.err.println("❌ [豆包登录] 登录异常: " + loginException.getMessage());
                }
                loginSessionManager.endLoginSession(sessionKey);
                return "false";
            }

            // 🔥 所有流程都失败，返回false
            loginSessionManager.endLoginSession(sessionKey);
            return "false";
        } catch (Exception e) {
            // 🔥 检查错误类型
            String errorMsg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

            // 静默处理TargetClosedError（会话已清理，页面关闭是正常的）
            boolean isTargetClosed = errorMsg.contains("target") && errorMsg.contains("closed");

            if (!isTargetClosed) {
                // 非TargetClosedError才打印日志
                System.err.println("❌ [豆包登录] 获取登录二维码失败: " + e.getMessage());

                // 检查是否是严重错误（如个人资料错误、页面崩溃等）
                if (errorMsg.contains("个人资料") || errorMsg.contains("profile") ||
                    errorMsg.contains("crashed") || errorMsg.contains("崩溃") ||
                    errorMsg.contains("context") && errorMsg.contains("closed")) {
                    // 遇到严重错误，强制清理该用户的所有会话（保留元宝持久化）
                    handleCriticalErrorAndCleanup(userId, "豆包", e.getMessage());
                }
            }

            throw e;
        } finally {
            // 🔥 确保无论如何都清理会话记录
            if (sessionKey != null) {
                loginSessionManager.endLoginSession(sessionKey);
            }
        }
    }

    /**
     * 退出腾讯元宝
     */
    @Operation(summary = "退出腾讯元宝登录状态", description = "执行退出操作，返回true表示成功")
    @GetMapping("/loginOut")
    public boolean loginOut(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws InterruptedException {
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "yb")) {
            Page page = browserUtil.getOrCreatePage(context);
            page.navigate("https://yuanbao.tencent.com/chat/naQivTmsDa");
            page.click("span.icon-yb-setting");
            page.click("text=退出登录");
            page.locator("//*[@id=\"hunyuan-bot\"]/div[2]/div/div[2]/div/div/div[3]/button[2]").click();
            Thread.sleep(3000);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Operation(summary = "检查百度AI登录状态", description = "返回用户名/手机号表示已登录，false 表示未登录")
    @GetMapping("/checkBaiduLogin")
    public String checkBaiduLogin(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws Exception {
        String key = userId + "-bd";
        if (loginMap.containsKey(key)) {
            // 如果当前用户正在处理，则返回"处理中"
            return loginMap.get(key);
        }
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "baidu")) {
            Page page = browserUtil.getOrCreatePage(context);
            // 使用BaiduUtil检查登录状态
            String loginStatus = baiduUtil.checkBaiduLogin(page, true);

            if (!"false".equals(loginStatus) && !"未登录".equals(loginStatus)) {
                loginMap.put(key, loginStatus);
                return loginStatus; // 返回用户名或登录状态
            } else {
                return "false"; // 未登录
            }

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取百度登录二维码
     *
     * @param userId
     */
    @Operation(summary = "获取百度登录二维码", description = "返回二维码截图 URL 或 false 表示失败")
    @GetMapping("/getBaiduQrCode")
    public String getBaiduQrCode(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) {
        // 🔥 第1步：准备登录会话（强制清理旧会话）
        // prepareLoginSession现在总是返回非null值，会强制清理所有旧会话（包括同一个AI的旧会话）
        String sessionKey = loginSessionManager.prepareLoginSession(userId, "Baidu");

        // 🔥 第2步：创建新的BrowserContext
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "baidu")) {
            Page page = browserUtil.getOrCreatePage(context);

            // 🔥 第3步：注册新的登录会话
            sessionKey = loginSessionManager.startLoginSession(userId, "Baidu", context, page);

            // 首先检查当前登录状态
            String currentStatus = baiduUtil.checkBaiduLogin(page, true);
            if (!"false".equals(currentStatus)) {
                // 已经登录，直接返回状态
                JSONObject statusObject = new JSONObject();
                statusObject.put("status", currentStatus);
                statusObject.put("userId", userId);
                statusObject.put("type", "RETURN_BAIDU_STATUS");
                webSocketClientService.sendMessage(statusObject.toJSONString());

                // 截图返回当前页面
                String url = screenshotUtil.screenshotAndUpload(page, "getBaiduLoggedIn.png");
                JSONObject qrUpdateObject = new JSONObject();
                qrUpdateObject.put("url", url);
                qrUpdateObject.put("userId", userId);
                qrUpdateObject.put("type", "RETURN_PC_BAIDU_QRURL");
                webSocketClientService.sendMessage(qrUpdateObject.toJSONString());

                loginSessionManager.endLoginSession(sessionKey);
                return url;
            }

            // 未登录，使用BaiduUtil获取二维码
            String url = baiduUtil.waitAndGetQRCode(page, userId);

            if (url != null && !url.trim().isEmpty()) {
                // 🔥 带身份验证的二维码发送
                String result = sendQrCodeWithValidation(userId, "Baidu", url, "RETURN_PC_BAIDU_QRURL");
                if (result == null) {
                    // 已清空所有登录会话，返回友好提示
                    return "SERVICE_UNAVAILABLE";
                }

                // 实时监测登录状态 - 最多等待60秒
                int maxAttempts = 30; // 30次尝试，每次2秒
                for (int i = 0; i < maxAttempts; i++) {
                    // 🔥 检查会话是否仍然活跃
                    if (!loginSessionManager.isSessionActive(sessionKey)) {
                        return "session_terminated";
                    }

                    Thread.sleep(2000);

                    // 检查当前页面登录状态
                    String loginStatus = baiduUtil.checkBaiduLogin(page, false);

                    if (!"false".equals(loginStatus)) {
                        // 登录成功，发送状态到WebSocket
                        JSONObject statusSuccessObject = new JSONObject();
                        statusSuccessObject.put("status", loginStatus);
                        statusSuccessObject.put("userId", userId);
                        statusSuccessObject.put("type", "RETURN_BAIDU_STATUS");
                        webSocketClientService.sendMessage(statusSuccessObject.toJSONString());

                        loginSessionManager.endLoginSession(sessionKey);
                        break;
                    }

                    // 每5次尝试重新截图一次，可能二维码已更新
                    if (i % 5 == 4) {
                        try {
                            // 🔥 再次检查会话状态
                            if (!loginSessionManager.isSessionActive(sessionKey)) {
                                return "session_terminated";
                            }

                            String newUrl = screenshotUtil.screenshotAndUpload(page, "getBaiduQrCode_refresh.png");

                            // 🔥 带身份验证的二维码发送
                            String refreshResult = sendQrCodeWithValidation(userId, "Baidu", newUrl, "RETURN_PC_BAIDU_QRURL");
                            if (refreshResult == null) {
                                // 已清空所有登录会话，返回友好提示
                                return "SERVICE_UNAVAILABLE";
                            }
                            // 刷新二维码也是已初始化状态，无需重复标记
                        } catch (Exception e) {
                            System.err.println("❌ [百度AI登录] 刷新二维码失败: " + e.getMessage());
                        }
                    }
                }

                loginSessionManager.endLoginSession(sessionKey);
                return url;
            } else {
                // 发送失败消息到前端
                JSONObject errorObject = new JSONObject();
                errorObject.put("url", "");
                errorObject.put("userId", userId);
                errorObject.put("type", "RETURN_PC_BAIDU_QRURL");
                errorObject.put("error", "获取二维码失败");
                webSocketClientService.sendMessage(errorObject.toJSONString());

                loginSessionManager.endLoginSession(sessionKey);
                return "false";
            }

        } catch (Exception e) {
            // 静默处理会话失效导致的错误
            String errorMsg = e.getMessage();
            boolean isSessionClosed = errorMsg != null && (errorMsg.contains("closed") || errorMsg.contains("doesn't exist"));
            return "false";
        } finally {
            // 🔥 确保无论如何都清理会话记录
            if (sessionKey != null) {
                loginSessionManager.endLoginSession(sessionKey);
            }
        }
    }

    /**
     * 获取知乎登录二维码
     *
     * @param userId 用户唯一标识
            }
        } catch (Exception e) {
            System.err.println("❌ [知乎登录] 切换扫码标签页失败: " + e.getMessage());
        }

        // 等待二维码加载
        try {
            Locator qrCodeArea = page.locator(".Qrcode, .qrcode, canvas, img[src*='qr']");
            if (qrCodeArea.count() > 0) {
                qrCodeArea.first().waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000));
     * 检查知乎登录状态
     *
     * @param userId 用户唯一标识
     * @return 登录状态："false"表示未登录，用户名表示已登录
     */
    @Operation(summary = "检查知乎登录状态", description = "返回用户名表示已登录，false 表示未登录")
    @GetMapping("/checkZhihuLogin")
    public String checkZhihuLogin(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws Exception {
        String key = userId + "-zhzd";
        if (loginMap.containsKey(key)) {
            // 如果当前用户正在处理，则返回"处理中"
            return loginMap.get(key);
        }
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "zhzd")) {
            Page page = browserUtil.getOrCreatePage(context);

            // 先导航到知乎首页而不是登录页面，这样能更好地检测登录状态
            page.navigate("https://www.zhihu.com/");
            page.waitForLoadState();
            Thread.sleep(3000);

            // 检查当前URL是否跳转到登录页面
            String currentUrl = page.url();
            if (currentUrl.contains("signin") || currentUrl.contains("login")) {
                return "false";
            }

            // 检测登录状态
            String userName = zhiHuUtil.checkLoginStatus(page);

            if (!"false".equals(userName) && !"未登录".equals(userName)) {
                loginMap.put(key, userName);
                return userName;
            }

            return "false";

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取知乎登录二维码
     *
     * @param userId 用户唯一标识
     * @return 二维码图片URL 或 "false"表示失败
     */
    @Operation(summary = "获取知乎登录二维码", description = "返回二维码截图 URL 或 false 表示失败")
    @GetMapping("/getZhihuQrCode")
    public String getZhihuQrCode(@Parameter(description = "用户唯一标识") @RequestParam("userId") String userId) throws Exception {
        // 🔥 第1步：准备登录会话（强制清理旧会话）
        // prepareLoginSession现在总是返回非null值，会强制清理所有旧会话（包括同一个AI的旧会话）
        String sessionKey = loginSessionManager.prepareLoginSession(userId, "知乎直答");

        // 🔥 第2步：创建新的BrowserContext
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "zhzd")) {
            Page page = browserUtil.getOrCreatePage(context);

            // 🔥 第3步：注册新的登录会话
            sessionKey = loginSessionManager.startLoginSession(userId, "知乎直答", context, page);

            try {
                // 导航到知乎登录页面
                page.navigate("https://www.zhihu.com/signin");
                page.waitForLoadState();
                Thread.sleep(3000); // 增加等待时间

                // 🔥 在关键步骤检查会话是否仍然活跃
                if (!loginSessionManager.isSessionActive(sessionKey)) {
                    return "session_terminated";
                }

                // 尝试点击扫码登录选项卡（如果存在）
                try {
                    Locator qrTab = page.locator("div[role='tab']:has-text('扫码登录'), .SignFlow-tab:has-text('扫码'), [data-testid='qr-tab']").first();
                    if (qrTab.count() > 0 && qrTab.isVisible()) {
                        qrTab.click();
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    // 继续查找二维码
                }

                // 查找二维码区域 - 使用更全面的选择器
                String[] qrSelectors = {
                    ".SignFlow-qrcode img",
                    ".qr-code img",
                    "[class*='qrcode'] img",
                    ".SignFlow-qrcode canvas",
                    ".qr-code canvas",
                    "[class*='qrcode'] canvas",
                    "img[alt*='二维码']",
                    "canvas[class*='qr']",
                    ".signin-qr img",
                    ".signin-qr canvas"
                };

                Locator qrCodeArea = null;
                String usedSelector = "";

                for (String selector : qrSelectors) {
                    Locator element = page.locator(selector).first();
                    if (element.count() > 0) {
                        qrCodeArea = element;
                        usedSelector = selector;
                        break;
                    }
                }

                if (qrCodeArea != null && qrCodeArea.count() > 0) {
                    try {
                        // 等待二维码加载
                        qrCodeArea.waitFor(new Locator.WaitForOptions()
                                .setState(WaitForSelectorState.VISIBLE)
                                .setTimeout(10000));

                        // 🔥 再次检查会话状态
                        if (!loginSessionManager.isSessionActive(sessionKey)) {
                            return "session_terminated";
                        }

                        // 截图整个页面（参考其他AI的做法）
                        String screenshotPath = screenshotUtil.screenshotAndUpload(page, "zhzd_qrcode_" + userId + ".png");

                        if (screenshotPath != null && !screenshotPath.isEmpty() && !"null".equals(screenshotPath)) {

                            // 🔥 【重要】返回前进行身份验证
                            // 确保返回的二维码属于当前用户正在操作的知乎直答
                            String result = sendQrCodeWithValidation(userId, "知乎直答", screenshotPath, "RETURN_PC_ZHZD_QRURL");
                            if (result == null) {
                                // 已清空所有登录会话，返回友好提示
                                return "SERVICE_UNAVAILABLE";
                            }

                            // 🔥 参考豆包的做法，等待登录状态变化
                            try {
                                // 等待登录成功或页面跳转 (60秒超时)
                                boolean loginSuccess = false;
                                long startTime = System.currentTimeMillis();
                                long timeout = 60000; // 60秒超时

                                while (System.currentTimeMillis() - startTime < timeout) {
                                    // 🔥 检查会话是否仍然活跃
                                    if (!loginSessionManager.isSessionActive(sessionKey)) {
                                        return "session_terminated";
                                    }

                                    // 检查是否已经跳转到知乎主页或其他页面
                                    String currentUrl = page.url();
                                    if (!currentUrl.contains("signin") && !currentUrl.contains("login")) {
                                        loginSuccess = true;
                                        break;
                                    }

                                    // 检查是否有登录成功的元素
                                    try {
                                        if (page.locator(".Avatar.AppHeader-profileAvatar, [class*='Avatar'][class*='AppHeader-profileAvatar']").count() > 0) {
                                            loginSuccess = true;
                                            break;
                                        }
                                    } catch (Exception e) {
                                        // 继续等待
                                    }

                                    Thread.sleep(2000); // 每2秒检查一次
                                }

                                if (loginSuccess) {
                                    // 获取用户信息
                                    String userName = zhiHuUtil.checkLoginStatus(page);
                                    if (!"false".equals(userName) && !"未登录".equals(userName)) {

                                        // 发送登录状态消息
                                        JSONObject statusObject = new JSONObject();
                                        statusObject.put("status", userName);
                                        statusObject.put("userId", userId);
                                        statusObject.put("type", "RETURN_ZHZD_STATUS");
                                        webSocketClientService.sendMessage(statusObject.toJSONString());

                                        loginSessionManager.endLoginSession(sessionKey);
                                        return userName;
                                    }
                                }

                            } catch (Exception loginException) {
                                System.err.println("❌ [知乎登录] 等待登录异常: " + loginException.getMessage());
                            }

                            // 🔥 登录完成或超时后结束会话
                            loginSessionManager.endLoginSession(sessionKey);
                            return screenshotPath;
                        } else {
                            System.err.println("❌ [知乎登录] 页面截图失败，返回: " + screenshotPath);
                        }
                    } catch (Exception e) {
                        System.err.println("❌ [知乎登录] 二维码截图异常: " + e.getMessage());
                    }
                } else {
                    System.err.println("❌ [知乎登录] 未找到二维码元素，尝试截图整个页面进行调试");
                    // 调试：截图整个页面
                    try {
                        screenshotUtil.screenshotAndUpload(page, "zhzd_debug_" + userId);
                    } catch (Exception e) {
                        // 静默处理调试截图失败
                    }
                }

                // 🔥 登录失败也要结束会话
                loginSessionManager.endLoginSession(sessionKey);
                return "false";

            } catch (Exception e) {
                // 🔥 异常时也要结束会话
                loginSessionManager.endLoginSession(sessionKey);
                System.err.println("❌ [知乎登录] 获取登录二维码失败: " + e.getMessage());
                throw e;
            }

        } catch (Exception e) {
            System.err.println("❌ [知乎登录] 获取登录二维码失败: " + e.getMessage());
            throw e;
        } finally {
            // 🔥 确保无论如何都清理会话记录
            if (sessionKey != null) {
                loginSessionManager.endLoginSession(sessionKey);
            }
        }
    }
}