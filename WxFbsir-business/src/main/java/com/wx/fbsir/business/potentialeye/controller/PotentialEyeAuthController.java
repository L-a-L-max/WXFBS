package com.wx.fbsir.business.potentialeye.controller;

import cn.hutool.core.util.ObjectUtil;
import com.wx.fbsir.business.potentialeye.domain.WeWorkLoginResult;
import com.wx.fbsir.business.potentialeye.service.impl.WeWorkAuthService;
import com.wx.fbsir.common.annotation.Anonymous;
import com.wx.fbsir.common.core.domain.AjaxResult;
import com.wx.fbsir.common.core.redis.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 企业微信认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class PotentialEyeAuthController {

    private static final Logger log = LoggerFactory.getLogger(PotentialEyeAuthController.class);

    @Autowired
    private WeWorkAuthService weWorkSimpleAuthService;

    @Autowired
    private RedisCache redisCache;
    /**
     * 获取企业微信授权URL（API方式）
     */
    @Anonymous
    @GetMapping("/authorize-url")
    public AjaxResult getAuthorizeUrl(@RequestParam String redirectUri) {
        try {
            log.info("[授权] 获取授权URL - redirectUri: {}", redirectUri);
            String authUrl = weWorkSimpleAuthService.buildOAuthAuthorizeUrl(redirectUri);
            return AjaxResult.success(Map.of("authUrl", authUrl));
        } catch (Exception e) {
            log.error("[授权] 获取授权URL失败", e);
            return AjaxResult.error("获取授权URL失败: " + e.getMessage());
        }
    }

    /**
     * 发起企业微信授权（直接302跳转）
     * 兼容旧版接口，支持直接访问跳转
     */
    @Anonymous
    @GetMapping("/authorize")
    public void authorize(@RequestParam(value = "teamId", required = false) Long teamId,
                          @RequestParam(value = "redirect", required = false) String redirect,
                          jakarta.servlet.http.HttpServletRequest request,
                          jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        try {
            // 构建回调地址
            String redirectUri = redirect;
            if (redirectUri == null || redirectUri.isBlank()) {
                String scheme = request.getHeader("X-Forwarded-Proto");
                if (scheme == null || scheme.isBlank()) {
                    scheme = request.getScheme();
                }
                String host = request.getHeader("Host");
                //if (host.equals("localhost:8080")) host = "43.139.254.160:8080";
                if (host.equals("localhost:8080") || host.equals("73327d6b.r9.vip.cpolar.cn")) host = "mcs.u3w.com"; //本地启动需要配置
                redirectUri = scheme + "://" + host + "/api/auth/callback";
            }

            log.info("[授权] 发起授权跳转 - redirectUri: {}", redirectUri);
            
            // 获取授权URL
            String authUrl = weWorkSimpleAuthService.buildOAuthAuthorizeUrl(redirectUri);
            // 302跳转到企业微信授权页面
            response.sendRedirect(authUrl);
        } catch (Exception e) {
            log.error("[授权] 发起授权失败", e);
            response.sendError(500, "授权失败: " + e.getMessage());
        }
    }

    /**
     * 企业微信OAuth回调处理
     * 返回HTML页面，自动跳转到聊天页面并传递token
     */
    /**
     * 企业微信OAuth回调处理
     * 返回HTML页面，自动跳转到聊天页面或注册页面
     */
    @Anonymous
    @GetMapping("/callback")
    public void handleCallback(@RequestParam("code") String code,
                               jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        try {
            log.info("[授权] 处理回调 - code: {}", code);
            WeWorkLoginResult result = weWorkSimpleAuthService.handleOAuthCallback(code);
            String userId = result.getWeWorkUserId();
            // 判断token是否为空，为空则跳转到注册页面
            if (ObjectUtil.isNull(result.getToken())) {
                // 当前用户不存在于数据库中，跳转到注册页面
                log.info("[授权] 用户未注册，跳转到注册页面 - weWorkUserId: {}", userId);
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write(
                        "<!DOCTYPE html>" +
                                "<html>" +
                                "<head>" +
                                "  <meta charset=\"UTF-8\">" +
                                "  <title>登录成功</title>" +
                                "  <style>" +
                                "    body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }" +
                                "    .success { color: #67C23A; font-size: 24px; margin-bottom: 20px; }" +
                                "    .loading { color: #909399; font-size: 16px; }" +
                                "  </style>" +
                                "</head>" +
                                "<body>" +
                                "  <div class=\"success\">✓ 登录失败，没有用户信息</div>" +
                                "  <div class=\"loading\">正在跳转到注册页面...</div>" +
                                "  <script>" +
                                "    localStorage.setItem('sessionId', '" + userId + "');" +
                                "    sessionStorage.setItem('wework_login', JSON.stringify({" +
                                "      weWorkUserId: '" + userId + "'," +
                                "      sessionId: '" + userId + "'" +
                                "    }));" +
                                "    setTimeout(function() {" +
                                "      window.location.href = '/register';" +
                                "    }, 500);" +
                                "  </script>" +
                                "</body>" +
                                "</html>"
                );
            }else {
                // 用户已存在，正常处理登录逻辑
                // 将这个信息存储在redis中
                redisCache.setCacheObject("loginResult", result);
                String token = result.getToken();
                // 返回HTML页面，使用JavaScript保存token并跳转
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write(
                        "<!DOCTYPE html>" +
                                "<html>" +
                                "<head>" +
                                "  <meta charset=\"UTF-8\">" +
                                "  <title>登录成功</title>" +
                                "  <style>" +
                                "    body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }" +
                                "    .success { color: #67C23A; font-size: 24px; margin-bottom: 20px; }" +
                                "    .loading { color: #909399; font-size: 16px; }" +
                                "  </style>" +
                                "</head>" +
                                "<body>" +
                                "  <div class=\"success\">✓ 登录成功</div>" +
                                "  <div class=\"loading\">正在跳转到对话页面...</div>" +
                                "  <script>" +
                                "    localStorage.setItem('token', '" + token + "');" +
                                "    localStorage.setItem('sessionId', '" + userId + "');" +
                                "    sessionStorage.setItem('token', '" + token + "');" +
                                "    sessionStorage.setItem('wework_login', JSON.stringify({" +
                                "      weWorkUserId: '" + userId + "'," +
                                "      sessionId: '" + userId + "'" +
                                "    }));" +
                                "    setTimeout(function() {" +
                                "      window.location.href = '/chat';" +
                                "    }, 500);" +
                                "  </script>" +
                                "</body>" +
                                "</html>"
                );
            }
        } catch (Exception e) {
            log.error("[授权] 企业微信登录失败", e);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "  <meta charset=\"UTF-8\">" +
                            "  <title>登录失败</title>" +
                            "  <style>" +
                            "    body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }" +
                            "    .error { color: #F56C6C; font-size: 24px; margin-bottom: 20px; }" +
                            "    .message { color: #909399; font-size: 16px; margin-bottom: 30px; }" +
                            "    .btn { display: inline-block; padding: 10px 20px; background: #409EFF; color: white; text-decoration: none; border-radius: 4px; }" +
                            "  </style>" +
                            "</head>" +
                            "<body>" +
                            "  <div class=\"error\">✗ 登录失败</div>" +
                            "  <div class=\"message\">" + e.getMessage() + "</div>" +
                            "  <a href=\"/\" class=\"btn\">返回首页</a>" +
                            "</body>" +
                            "</html>"
            );
        }
    }
}
