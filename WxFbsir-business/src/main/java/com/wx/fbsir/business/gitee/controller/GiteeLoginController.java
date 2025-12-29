package com.wx.fbsir.business.gitee.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.wx.fbsir.business.gitee.domain.GiteeBindInfo;
import com.wx.fbsir.business.gitee.domain.GiteeBindRequest;
import com.wx.fbsir.business.gitee.domain.GiteeCreateRequest;
import com.wx.fbsir.business.gitee.util.GiteeOauthUtil;
import com.wx.fbsir.common.annotation.Anonymous;
import com.wx.fbsir.common.core.domain.AjaxResult;
import com.wx.fbsir.common.core.domain.entity.SysUser;
import com.wx.fbsir.common.enums.UserStatus;
import com.wx.fbsir.common.utils.SecurityUtils;
import com.wx.fbsir.common.utils.uuid.IdUtils;
import com.wx.fbsir.common.core.redis.RedisCache;
import com.wx.fbsir.system.service.ISysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GiteeLoginController {
    private static final Logger log = LoggerFactory.getLogger(GiteeLoginController.class);
    private static final String BIND_TOKEN_KEY_PREFIX = "gitee:bind:token:";
    private static final String BIND_USER_KEY_PREFIX = "gitee:bind:user:";
    private static final int BIND_TOKEN_EXPIRE_MINUTES = 10;

    @Value("${gitee.oauth.client-id:}")
    private String clientId;

    @Value("${gitee.oauth.client-secret:}")
    private String clientSecret;

    @Value("${gitee.oauth.callback-url:http://localhost:8080/auth}")
    private String callbackUrl;

    @Value("${gitee.oauth.frontend-url:http://localhost}")
    private String frontendUrl;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisCache redisCache;

    @Anonymous
    @GetMapping("/gitlogin")
    public void gitlogin(HttpServletResponse response) throws IOException {
        String resolvedClientId = StringUtils.isNotBlank(clientId)
            ? clientId
            : GiteeOauthUtil.DEFAULT_CLIENT_ID;

        if (StringUtils.isBlank(resolvedClientId)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "gitee clientId未配置");
            return;
        }

        String authorizeUrl = GiteeOauthUtil.buildAuthorizeUrl(resolvedClientId, callbackUrl);
        response.sendRedirect(authorizeUrl);
    }

    @Anonymous
    @GetMapping("/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Gitee OAuth callback received, code={}, error={}", request.getParameter("code"), request.getParameter("error"));
        try {
            String error = request.getParameter("error");
            if (StringUtils.isNotBlank(error)) {
                redirectWithError(response, "gitee授权失败: " + error);
                return;
            }

            String code = request.getParameter("code");
            if (StringUtils.isBlank(code)) {
                redirectWithError(response, "缺少授权码");
                return;
            }

            String resolvedClientId = StringUtils.isNotBlank(clientId)
                ? clientId
                : GiteeOauthUtil.DEFAULT_CLIENT_ID;
            if (StringUtils.isBlank(resolvedClientId)) {
                redirectWithError(response, "gitee clientId未配置");
                return;
            }
            if (StringUtils.isBlank(clientSecret)) {
                redirectWithError(response, "gitee clientSecret未配置");
                return;
            }

            log.info("Gitee OAuth: exchanging code for token");
            GiteeOauthUtil.GiteeOauthToken token = GiteeOauthUtil.exchangeCodeForToken(
                resolvedClientId, clientSecret, callbackUrl, code);

            log.info("Gitee OAuth: fetching user profile");
            GiteeOauthUtil.GiteeUserProfile profile = GiteeOauthUtil.fetchUserProfile(token.getAccessToken());
            if (StringUtils.isBlank(profile.getId())) {
                redirectWithError(response, "gitee用户信息不完整");
                return;
            }

            String boundUsername = redisCache.getCacheObject(getBindUserKey(profile.getId()));
            if (StringUtils.isNotBlank(boundUsername)) {
                SysUser boundUser = userService.selectUserByUserName(boundUsername);
                if (boundUser != null
                    && !UserStatus.DELETED.getCode().equals(boundUser.getDelFlag())
                    && !UserStatus.DISABLE.getCode().equals(boundUser.getStatus())) {
                    log.info("Gitee OAuth: bound user found, redirecting to login");
                    response.sendRedirect(buildFrontendBoundRedirect(boundUsername));
                    return;
                }
            }

            String bindToken = IdUtils.fastUUID();
            GiteeBindInfo bindInfo = new GiteeBindInfo();
            bindInfo.setGiteeId(profile.getId());
            bindInfo.setLogin(profile.getLogin());
            bindInfo.setName(StringUtils.isNotBlank(profile.getName()) ? profile.getName() : profile.getLogin());
            bindInfo.setAvatarUrl(profile.getAvatarUrl());
            bindInfo.setEmail(profile.getEmail());
            bindInfo.setAccessToken(token.getAccessToken());
            bindInfo.setTokenType(token.getTokenType());
            bindInfo.setCreatedAt(token.getCreatedAt());

            redisCache.setCacheObject(getBindTokenKey(bindToken), bindInfo, BIND_TOKEN_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("Gitee OAuth: bind token created, redirecting to login");
            response.sendRedirect(buildFrontendBindRedirect(bindToken));
        } catch (Exception ex) {
            log.error("Gitee OAuth callback failed", ex);
            redirectWithError(response, ex.getMessage());
        }
    }

    @Anonymous
    @PostMapping("/gitee/bind")
    public AjaxResult bindGitee(@RequestBody GiteeBindRequest request) {
        if (request == null || StringUtils.isBlank(request.getBindToken())) {
            return AjaxResult.error("缺少绑定信息");
        }
        if (StringUtils.isBlank(request.getUsername()) || StringUtils.isBlank(request.getPassword())) {
            return AjaxResult.error("请输入账号和密码");
        }

        GiteeBindInfo bindInfo = redisCache.getCacheObject(getBindTokenKey(request.getBindToken()));
        if (bindInfo == null) {
            return AjaxResult.error("绑定信息已过期，请重新授权");
        }

        SysUser user = userService.selectUserByUserName(request.getUsername());
        if (user == null) {
            return AjaxResult.error("账号不存在");
        }
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            return AjaxResult.error("账号已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            return AjaxResult.error("账号已被停用");
        }
        if (!SecurityUtils.matchesPassword(request.getPassword(), user.getPassword())) {
            return AjaxResult.error("账号或密码错误");
        }

        redisCache.setCacheObject(getBindUserKey(bindInfo.getGiteeId()), user.getUserName());
        redisCache.deleteObject(getBindTokenKey(request.getBindToken()));

        return AjaxResult.success();
    }

    @Anonymous
    @PostMapping("/gitee/create")
    public AjaxResult createGiteeUser(@RequestBody GiteeCreateRequest request) {
        if (request == null || StringUtils.isBlank(request.getBindToken())) {
            return AjaxResult.error("缺少绑定信息");
        }

        GiteeBindInfo bindInfo = redisCache.getCacheObject(getBindTokenKey(request.getBindToken()));
        if (bindInfo == null) {
            return AjaxResult.error("绑定信息已过期，请重新授权");
        }

        SysUser user = new SysUser();
        String baseUsername = StringUtils.isNotBlank(bindInfo.getLogin()) ? bindInfo.getLogin() : "gitee_user";
        String uniqueUsername = buildUniqueUsername(baseUsername);
        user.setUserName(uniqueUsername);
        user.setNickName(StringUtils.isNotBlank(bindInfo.getName()) ? bindInfo.getName() : uniqueUsername);
        user.setEmail(bindInfo.getEmail());
        user.setAvatar(bindInfo.getAvatarUrl());
        user.setStatus("0");
        user.setDelFlag("0");
        String tempPassword = IdUtils.fastSimpleUUID().substring(0, 12);
        user.setPassword(SecurityUtils.encryptPassword(tempPassword));
        user.setRemark("gitee:" + bindInfo.getGiteeId());

        boolean created = userService.registerUser(user);
        if (!created) {
            return AjaxResult.error("创建用户失败");
        }

        redisCache.setCacheObject(getBindUserKey(bindInfo.getGiteeId()), user.getUserName());
        redisCache.deleteObject(getBindTokenKey(request.getBindToken()));

        AjaxResult result = AjaxResult.success();
        result.put("username", user.getUserName());
        result.put("password", tempPassword);
        return result;
    }

    private String buildFrontendBoundRedirect(String username) {
        String base = normalizeFrontendUrl(frontendUrl);
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        return base + "/login?redirect=/index&giteeBound=1&giteeUsername=" + encodedUsername;
    }

    private String buildFrontendBindRedirect(String bindToken) {
        String base = normalizeFrontendUrl(frontendUrl);
        String encodedToken = URLEncoder.encode(bindToken, StandardCharsets.UTF_8);
        return base + "/login?redirect=/index&giteeBindToken=" + encodedToken;
    }

    private String buildFrontendErrorRedirect(String message) {
        String base = normalizeFrontendUrl(frontendUrl);
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return base + "/login?redirect=/index&giteeError=" + encodedMessage;
    }

    private void redirectWithError(HttpServletResponse response, String message) {
        log.warn("Gitee OAuth error: {}", message);
        try {
            response.sendRedirect(buildFrontendErrorRedirect(message));
        } catch (IOException ex) {
            log.error("Gitee OAuth redirect failed", ex);
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write(message);
            } catch (IOException ignore) {
                // ignore secondary failure
            }
        }
    }

    private String normalizeFrontendUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return "http://localhost";
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    private String getBindTokenKey(String token) {
        return BIND_TOKEN_KEY_PREFIX + token;
    }

    private String getBindUserKey(String giteeId) {
        return BIND_USER_KEY_PREFIX + giteeId;
    }

    private String buildUniqueUsername(String baseUsername) {
        String candidate = "gitee_" + baseUsername;
        if (userService.selectUserByUserName(candidate) == null) {
            return candidate;
        }
        String fallback = candidate;
        for (int i = 0; i < 5; i++) {
            fallback = candidate + "_" + IdUtils.fastSimpleUUID().substring(0, 4);
            if (userService.selectUserByUserName(fallback) == null) {
                return fallback;
            }
        }
        return candidate + "_" + IdUtils.fastSimpleUUID().substring(0, 6);
    }
}
