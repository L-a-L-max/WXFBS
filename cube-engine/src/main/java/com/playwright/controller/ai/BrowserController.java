package com.playwright.controller.ai;

import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
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
 */
@RestController
@RequestMapping("/api/browser")
@Tag(name = "浏览器控制器", description = "处理浏览器相关操作")
@Slf4j
public class BrowserController {

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
        String key = userId + "-mt";
        if (loginMap.containsKey(key)) {
            JSONObject jsonObjectTwo = new JSONObject();
            jsonObjectTwo.put("status", loginMap.get(key));
            jsonObjectTwo.put("userId", userId);
            jsonObjectTwo.put("type", "RETURN_METASO_STATUS");
            webSocketClientService.sendMessage(jsonObjectTwo.toJSONString());
            return loginMap.get(key);
        }
        
        String sessionKey = userId + "-Metaso";
        
        // 🔥 智能会话复用：检查是否已有活跃会话（连续点击同一个AI）
        LoginSessionManager.LoginSession existingSession = loginSessionManager.getSession(sessionKey);
        if (existingSession != null) {
            // 复用现有会话，直接重新截图
            try {
                Page page = existingSession.getPage();
                String url = screenshotUtil.screenshotAndUpload(page, "checkMetasoLogin.png");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", url);
                jsonObject.put("userId", userId);
                jsonObject.put("type", "RETURN_PC_METASO_QRURL");
                webSocketClientService.sendMessage(jsonObject.toJSONString());
                return url;
            } catch (Exception e) {
                // 如果复用失败，继续创建新会话
                System.err.println("⚠️ [Metaso登录] 复用会话失败，创建新会话: " + e.getMessage());
            }
        }
        
        // 创建新会话
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "metaso")) {
            Page page = browserUtil.getOrCreatePage(context);
            
            // 注册新的登录会话
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
                    // 检查会话是否仍然活跃
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
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("url", url);
                    jsonObject.put("userId", userId);
                    jsonObject.put("type", "RETURN_PC_METASO_QRURL");
                    webSocketClientService.sendMessage(jsonObject.toJSONString());
                    
                    for (int i = 0; i < 10; i++) {
                        // 检查会话是否仍然活跃
                        if (!loginSessionManager.isSessionActive(sessionKey)) {
                            return "session_terminated";
                        }
                        
                        Thread.sleep(2000);
                        String userName = metasoUtil.checkLogin(page, userId);
                        if (userName != null) {
                            loginMap.put(key, userName);
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
                loginMap.put(key, s);
                loginSessionManager.endLoginSession(sessionKey);
                return s;
            }
        } catch (Exception e) {
            System.err.println("❌ [Metaso登录] 获取登录二维码失败: " + e.getMessage());
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
        String sessionKey = userId + "-TongYi";
        
        // 🔥 智能会话复用：检查是否已有活跃会话（连续点击同一个AI）
        LoginSessionManager.LoginSession existingSession = loginSessionManager.getSession(sessionKey);
        if (existingSession != null) {
            // 复用现有会话，直接重新截图
            try {
                Page page = existingSession.getPage();
                String url = screenshotUtil.screenshotAndUpload(page, "checkTongYiLogin.png");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", url);
                jsonObject.put("userId", userId);
                jsonObject.put("type", "RETURN_PC_QW_QRURL");
                webSocketClientService.sendMessage(jsonObject.toJSONString());
                return url;
            } catch (Exception e) {
                System.err.println("⚠️ [通义千问登录] 复用会话失败，创建新会话: " + e.getMessage());
            }
        }
        
        // 创建新会话
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "ty")) {
            Page page = browserUtil.getOrCreatePage(context);
            
            // 🔥 注册新的登录会话
            sessionKey = loginSessionManager.startLoginSession(userId, "TongYi", context, page);
            
            page.navigate("https://www.tongyi.com/");
            page.waitForTimeout(3000);
            
            Locator loginButton = page.locator("(//span[contains(text(),'立即登录')])[1]");
            if (loginButton.count() > 0 && loginButton.isVisible()) {
                loginButton.click();
                page.waitForTimeout(2000);
                page.locator("div[class*='qrcodeWrapper']").last().waitFor(new Locator.WaitForOptions().setTimeout(10000));

                String url = screenshotUtil.screenshotAndUpload(page, "checkTongYiLogin.png");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", url);
                jsonObject.put("userId", userId);
                jsonObject.put("type", "RETURN_PC_QW_QRURL");
                webSocketClientService.sendMessage(jsonObject.toJSONString());

                // 🔥 检查会话状态并等待登录
                Locator userAvatarArea = page.locator(".popupUser");
                try {
                    userAvatarArea.waitFor(new Locator.WaitForOptions().setTimeout(60000));
                    
                    // 🔥 再次检查会话是否仍然活跃
                    if (!loginSessionManager.isSessionActive(sessionKey)) {
                        return "session_terminated";
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
                    System.err.println("❌ [通义千问登录] 等待登录超时: " + waitException.getMessage());
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
            // 如果当前用户正在处理，则返回"处理中"
            return loginMap.get(key);
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
        String sessionKey = userId + "-DeepSeek";
        
        // 🔥 智能会话复用：检查是否已有活跃会话（连续点击同一个AI）
        LoginSessionManager.LoginSession existingSession = loginSessionManager.getSession(sessionKey);
        if (existingSession != null) {
            // 复用现有会话，直接重新截图
            try {
                Page page = existingSession.getPage();
                String url = deepSeekUtil.waitAndGetQRCode(page, userId, screenshotUtil);
                if (!"false".equals(url)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("url", url);
                    jsonObject.put("userId", userId);
                    jsonObject.put("type", "RETURN_PC_DEEPSEEK_QRURL");
                    webSocketClientService.sendMessage(jsonObject.toJSONString());
                    return url;
                }
            } catch (Exception e) {
                System.err.println("⚠️ [DeepSeek登录] 复用会话失败，创建新会话: " + e.getMessage());
            }
        }
        
        // 创建新会话
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "deepseek")) {
            Page page = browserUtil.getOrCreatePage(context);
            
            // 🔥 注册新的登录会话
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

                // 结束会话
                loginSessionManager.endLoginSession(sessionKey);
                // 截图返回当前页面
                return screenshotUtil.screenshotAndUpload(page, "deepseekLoggedIn.png");
            }

            // 未登录，获取二维码截图URL
            String url = deepSeekUtil.waitAndGetQRCode(page, userId, screenshotUtil);

            if (!"false".equals(url)) {
                // 🔥 添加延迟确保截图完成
                Thread.sleep(1000);
                
                // 发送二维码URL到WebSocket
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", url);
                jsonObject.put("userId", userId);
                jsonObject.put("type", "RETURN_PC_DEEPSEEK_QRURL");
                webSocketClientService.sendMessage(jsonObject.toJSONString());

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
                            JSONObject qrUpdateObject = new JSONObject();
                            qrUpdateObject.put("url", url);
                            qrUpdateObject.put("userId", userId);
                            qrUpdateObject.put("type", "RETURN_PC_DEEPSEEK_QRURL");
                            webSocketClientService.sendMessage(qrUpdateObject.toJSONString());
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
            loginSessionManager.endLoginSession(sessionKey);
        } catch (Exception e) {
            System.err.println("❌ [DeepSeek登录] 获取登录二维码失败: " + e.getMessage());
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
                log.info("已有其他线程检测,等待登录状态变化");
                // 等待其他线程检测登录状态
                for (int i = 0; i < 10; i++) {
                    if (loginMap.get(key) != null) {
                        if (loginMap.get(key).contains("未登录")) {
                            log.info("检测到未登录");
                            return "false";
                        } else {
                            log.info("检测到已登录");
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
        try {
            UnPersisBrowserContextInfo browserContextInfo = BrowserContextFactory.getBrowserContext(userId, 2);
            BrowserContext context = null;
            if (browserContextInfo != null) {
                context = browserContextInfo.getBrowserContext();
            }
            Page page = context.pages().get(0);
            page.navigate("https://yuanbao.tencent.com/chat/naQivTmsDa");
            page.locator("//span[contains(text(),'登录')]").click();

            // 短暂等待确保页面开始加载
            Thread.sleep(2000);

            // 立即获取并发送二维码
            String url = screenshotUtil.screenshotAndUpload(page, "checkYBLogin.png");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);
            jsonObject.put("userId", userId);
            jsonObject.put("type", "RETURN_PC_YB_QRURL");
            webSocketClientService.sendMessage(jsonObject.toJSONString());

            // 尝试处理账号类型选择弹窗（初始检查）
            handleAccountTypeSelection(page);

            boolean isLogin = false;
            Locator phone = page.locator("//p[@class='nick-info-name']");
            String phoneText = phone.textContent();

            for (int i = 0; i < 6; i++) {
                if (phoneText.contains("未登录")) {
                    Thread.sleep(10000);
                    // 刷新二维码截图
                    url = screenshotUtil.screenshotAndUpload(page, "checkYBLogin.png");
                    jsonObject.put("url", url);
                    webSocketClientService.sendMessage(jsonObject.toJSONString());

                    // 再次尝试处理账号类型选择弹窗
                    handleAccountTypeSelection(page);
                } else {
                    break;
                }
                phoneText = phone.textContent();
            }

            if (phone.count() > 0) {
                JSONObject jsonObjectTwo = new JSONObject();
                if (phoneText.contains("未登录")) {
                    jsonObjectTwo.put("status", "false");
                } else {
                    isLogin = true;
                    jsonObjectTwo.put("status", phoneText);
                }
                jsonObjectTwo.put("userId", userId);
                jsonObjectTwo.put("type", "RETURN_YB_STATUS");
                webSocketClientService.sendMessage(jsonObjectTwo.toJSONString());
            }
            return isLogin ? phoneText : "false";
        } catch (Exception e) {
            log.error("获取元宝二维码失败", e);
            throw e;
        }
    }

    // 提取账号选择处理为独立方法，增强异常处理
    private void handleAccountTypeSelection(Page page) {
        try {
            Locator accountTypeModal = page.locator(".choose-content:has-text('选择账号类型')");
            // 使用较短的超时时间
            accountTypeModal.waitFor(new Locator.WaitForOptions().setTimeout(3000));

            if (accountTypeModal.count() > 0 && accountTypeModal.isVisible()) {
                Locator personalAccountBtn = page.locator(".ybc-login-account-list_personal");
                if (personalAccountBtn.count() > 0 && !isElementDisabled(personalAccountBtn)) {
                    log.info("找到个人账号按钮，准备点击");
                    personalAccountBtn.click();
                    log.info("点击操作完成");
                    Thread.sleep(2000);
                } else {
                    log.warn("未找到个人账号按钮，尝试使用文本选择器");
                    Locator textBasedBtn = page.locator("//span[contains(text(),'个人账号')]");
                    if (textBasedBtn.count() > 0) {
                        textBasedBtn.click();
                        Thread.sleep(2000);
                    }
                }
            }
        } catch (Exception e) {
            // 不抛出异常，仅记录日志
            log.debug("账号类型选择弹窗处理失败或未出现: " + e.getMessage());
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
            Thread.sleep(5000);
            Locator locator = page.locator("[data-testid='to_login_button']");
            if (locator.count() > 0 && locator.isVisible()) {
                return "false";
            } else {
                Thread.sleep(500);
                page.locator("[data-testid=\"chat_header_avatar_button\"]").click();
                Thread.sleep(500);
                page.locator("[data-testid=\"chat_header_setting_button\"]").click();
//                Thread.sleep(1500);
                Locator phone = page.locator(".nickName-cIcGuG");
                phone.waitFor(new Locator.WaitForOptions().setTimeout(3000));
                if (phone.count() > 0) {
                    String phoneText = phone.textContent();
                    loginMap.put(key, phoneText);
                    return phoneText;
                } else {
                    return "false";
                }
            }
        } catch (Exception e) {
            throw e;
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
        String sessionKey = userId + "-Doubao";
        
        // 🔥 智能会话复用：检查是否已有活跃会话（连续点击同一个AI）
        LoginSessionManager.LoginSession existingSession = loginSessionManager.getSession(sessionKey);
        if (existingSession != null) {
            // 复用现有会话，直接重新截图
            try {
                Page page = existingSession.getPage();
                String url = screenshotUtil.screenshotAndUpload(page, "checkDBLogin.png");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", url);
                jsonObject.put("userId", userId);
                jsonObject.put("type", "RETURN_PC_DB_QRURL");
                webSocketClientService.sendMessage(jsonObject.toJSONString());
                return url;
            } catch (Exception e) {
                System.err.println("⚠️ [豆包登录] 复用会话失败，创建新会话: " + e.getMessage());
            }
        }
        
        // 创建新会话
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "db")) {
            Page page = browserUtil.getOrCreatePage(context);
            
            // 🔥 注册新的登录会话
            sessionKey = loginSessionManager.startLoginSession(userId, "Doubao", context, page);
            
            page.navigate("https://www.doubao.com/chat/");
            Locator locator = page.locator("[data-testid='to_login_button']");
            Thread.sleep(2000);
            
            if (locator.count() > 0 && locator.isVisible()) {
                locator.click();
                page.locator("[data-testid='qrcode_switcher']").evaluate("el => el.click()");

                Thread.sleep(3000);
                String url = screenshotUtil.screenshotAndUpload(page, "checkDBLogin.png");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", url);
                jsonObject.put("userId", userId);
                jsonObject.put("type", "RETURN_PC_DB_QRURL");
                webSocketClientService.sendMessage(jsonObject.toJSONString());
                
                try {
                    Locator login = page.getByText("登录成功");
                    login.waitFor(new Locator.WaitForOptions().setTimeout(60000));
                    
                    // 🔥 检查会话是否仍然活跃
                    if (!loginSessionManager.isSessionActive(sessionKey)) {
                        return "session_terminated";
                    }
                    
                    Thread.sleep(5000);
                    page.locator("[data-testid=\"chat_header_avatar_button\"]").click();
                    Thread.sleep(1000);
                    page.locator("[data-testid=\"chat_header_setting_button\"]").click();
                    Thread.sleep(1000);
                    Locator phone = page.locator(".nickName-cIcGuG");
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
                } catch (Exception loginException) {
                    System.err.println("❌ [豆包登录] 等待登录超时: " + loginException.getMessage());
                }
                
                loginSessionManager.endLoginSession(sessionKey);
            } else {
                System.err.println("❌ [豆包登录] 登录按钮未找到");
                loginSessionManager.endLoginSession(sessionKey);
            }
        } catch (Exception e) {
            System.err.println("❌ [豆包登录] 获取登录二维码失败: " + e.getMessage());
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
        String sessionKey = userId + "-Baidu";
        
        // 🔥 智能会话复用：检查是否已有活跃会话（连续点击同一个AI）
        LoginSessionManager.LoginSession existingSession = loginSessionManager.getSession(sessionKey);
        if (existingSession != null) {
            // 复用现有会话，直接重新截图
            try {
                Page page = existingSession.getPage();
                String url = baiduUtil.waitAndGetQRCode(page, userId);
                if (url != null && !url.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("url", url);
                    jsonObject.put("userId", userId);
                    jsonObject.put("type", "RETURN_PC_BAIDU_QRURL");
                    webSocketClientService.sendMessage(jsonObject.toJSONString());
                    return url;
                }
            } catch (Exception e) {
                System.err.println("⚠️ [百度AI登录] 复用会话失败，创建新会话: " + e.getMessage());
            }
        }
        
        // 创建新会话
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "baidu")) {
            Page page = browserUtil.getOrCreatePage(context);
            
            // 🔥 注册新的登录会话
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
                // 发送二维码截图
                JSONObject qrUpdateObject = new JSONObject();
                qrUpdateObject.put("url", url);
                qrUpdateObject.put("userId", userId);
                qrUpdateObject.put("type", "RETURN_PC_BAIDU_QRURL");
                webSocketClientService.sendMessage(qrUpdateObject.toJSONString());

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
                            JSONObject qrRefreshObject = new JSONObject();
                            qrRefreshObject.put("url", newUrl);
                            qrRefreshObject.put("userId", userId);
                            qrRefreshObject.put("type", "RETURN_PC_BAIDU_QRURL");
                            webSocketClientService.sendMessage(qrRefreshObject.toJSONString());
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
            System.err.println("❌ [百度AI登录] 获取登录二维码失败: " + e.getMessage());
            // 发送异常消息到前端
            JSONObject errorObject = new JSONObject();
            errorObject.put("url", "");
            errorObject.put("userId", userId);
            errorObject.put("type", "RETURN_PC_BAIDU_QRURL");
            errorObject.put("error", "获取二维码异常");
            webSocketClientService.sendMessage(errorObject.toJSONString());
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
        String sessionKey = userId + "-知乎直答";
        
        // 🔥 智能会话复用：检查是否已有活跃会话（连续点击同一个AI）
        LoginSessionManager.LoginSession existingSession = loginSessionManager.getSession(sessionKey);
        if (existingSession != null) {
            // 复用现有会话，直接重新截图
            try {
                Page page = existingSession.getPage();
                String url = screenshotUtil.screenshotAndUpload(page, "zhihu_qrcode_" + userId);
                if (url != null && !url.isEmpty() && !"false".equals(url)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("url", url);
                    jsonObject.put("userId", userId);
                    jsonObject.put("type", "RETURN_PC_ZHZD_QRURL");
                    webSocketClientService.sendMessage(jsonObject.toJSONString());
                    return url;
                }
            } catch (Exception e) {
                System.err.println("⚠️ [知乎登录] 复用会话失败，创建新会话: " + e.getMessage());
            }
        }
        
        // 创建新会话
        try (BrowserContext context = browserUtil.createPersistentBrowserContext(false, userId, "zhzd")) {
            Page page = browserUtil.getOrCreatePage(context);
            
            // 🔥 注册新的登录会话
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
                            
                            // 🔥 参考豆包的做法，立即发送WebSocket消息
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("url", screenshotPath);
                            jsonObject.put("userId", userId);
                            jsonObject.put("type", "RETURN_PC_ZHZD_QRURL");
                            webSocketClientService.sendMessage(jsonObject.toJSONString());
                            
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