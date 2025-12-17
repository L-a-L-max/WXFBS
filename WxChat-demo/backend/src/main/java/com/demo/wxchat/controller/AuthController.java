package com.demo.wxchat.controller;

import com.demo.wxchat.domain.AjaxResult;
import com.demo.wxchat.domain.WeWorkLoginResult;
import com.demo.wxchat.service.WeWorkAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 企业微信认证控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private WeWorkAuthService weWorkAuthService;

    /**
     * 发起企业微信OAuth授权（302跳转）
     */
    @GetMapping("/authorize")
    public void authorize(@RequestParam("teamId") Long teamId,
                          @RequestParam(value = "redirect", required = false) String redirect,
                          HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String redirectUri = redirect;
        if (redirectUri == null || redirectUri.isBlank()) {
            String scheme = request.getHeader("X-Forwarded-Proto");
            if (scheme == null || scheme.isBlank()) {
                scheme = request.getScheme();
            }
            String host = request.getHeader("Host");
            redirectUri = scheme + "://" + host + "/entry";
        }

        String url = weWorkAuthService.buildOAuthAuthorizeUrl(teamId, redirectUri);
        response.sendRedirect(url);
    }

    /**
     * 企业微信OAuth回调处理
     */
    @GetMapping("/callback")
    public AjaxResult handleCallback(@RequestParam("code") String code,
                                     @RequestParam("state") Long teamId) {
        try {
            WeWorkLoginResult result = weWorkAuthService.handleOAuthCallback(code, teamId);
            return AjaxResult.success("登录成功", result);
        } catch (Exception e) {
            log.error("企业微信登录失败", e);
            return AjaxResult.error("登录失败: " + e.getMessage());
        }
    }
}
