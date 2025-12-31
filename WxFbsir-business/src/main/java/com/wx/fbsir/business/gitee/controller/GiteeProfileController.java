package com.wx.fbsir.business.gitee.controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.wx.fbsir.business.gitee.domain.GiteeAuthState;
import com.wx.fbsir.business.gitee.util.GiteeCacheKeyUtil;
import com.wx.fbsir.business.gitee.util.GiteeOauthUtil;
import com.wx.fbsir.common.core.domain.AjaxResult;
import com.wx.fbsir.common.core.redis.RedisCache;
import com.wx.fbsir.common.utils.SecurityUtils;
import com.wx.fbsir.common.utils.uuid.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/gitee")
public class GiteeProfileController {
    private static final Logger log = LoggerFactory.getLogger(GiteeProfileController.class);
    private static final int AUTH_STATE_EXPIRE_MINUTES = 10;
    private static final String DEFAULT_REDIRECT_PATH = "/user/profile/gitee";

    @Value("${gitee.oauth.client-id:}")
    private String clientId;

    @Value("${gitee.oauth.callback-url:http://localhost:8080/auth}")
    private String callbackUrl;

    @Autowired
    private RedisCache redisCache;

    @GetMapping("/status")
    public AjaxResult status() {
        Long userId = SecurityUtils.getUserId();
        String token = redisCache.getCacheObject(GiteeCacheKeyUtil.getAccessTokenKey(userId));
        Map<String, Object> data = Map.of("authorized", StringUtils.isNotBlank(token));
        return AjaxResult.success(data);
    }

    @GetMapping("/authorize")
    public AjaxResult authorize(@RequestParam(value = "redirect", required = false) String redirect) {
        String resolvedClientId = StringUtils.isNotBlank(clientId)
            ? clientId
            : GiteeOauthUtil.DEFAULT_CLIENT_ID;
        if (StringUtils.isBlank(resolvedClientId)) {
            return AjaxResult.error("gitee clientId未配置");
        }

        String state = IdUtils.fastUUID();
        GiteeAuthState authState = new GiteeAuthState();
        authState.setUserId(SecurityUtils.getUserId());
        authState.setRedirect(normalizeRedirect(redirect));
        redisCache.setCacheObject(GiteeCacheKeyUtil.getAuthStateKey(state), authState,
            AUTH_STATE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        String authorizeUrl = GiteeOauthUtil.buildAuthorizeUrl(resolvedClientId, callbackUrl, state);
        Map<String, Object> data = Map.of("url", authorizeUrl);
        return AjaxResult.success(data);
    }

    @GetMapping("/profile")
    public AjaxResult profile() {
        String token = getAccessToken();
        if (token == null) {
            return AjaxResult.error("请先完成Gitee授权");
        }
        try {
            JsonNode profile = GiteeOauthUtil.fetchUserProfileJson(token);
            return AjaxResult.success(profile);
        } catch (Exception ex) {
            log.error("获取Gitee用户资料失败", ex);
            return AjaxResult.error(ex.getMessage());
        }
    }

    @GetMapping("/repos")
    public AjaxResult repos(@RequestParam Map<String, String> params) {
        String token = getAccessToken();
        if (token == null) {
            return AjaxResult.error("请先完成Gitee授权");
        }
        try {
            JsonNode repos = GiteeOauthUtil.fetchUserRepos(token, params);
            return AjaxResult.success(repos);
        } catch (Exception ex) {
            log.error("获取Gitee仓库失败", ex);
            return AjaxResult.error(ex.getMessage());
        }
    }

    @GetMapping("/issues")
    public AjaxResult issues(@RequestParam Map<String, String> params) {
        String token = getAccessToken();
        if (token == null) {
            return AjaxResult.error("请先完成Gitee授权");
        }
        try {
            JsonNode issues = GiteeOauthUtil.fetchUserIssues(token, params);
            return AjaxResult.success(issues);
        } catch (Exception ex) {
            log.error("获取Gitee Issues失败", ex);
            return AjaxResult.error(ex.getMessage());
        }
    }

    @GetMapping("/notifications")
    public AjaxResult notifications(@RequestParam Map<String, String> params) {
        String token = getAccessToken();
        if (token == null) {
            return AjaxResult.error("请先完成Gitee授权");
        }
        try {
            JsonNode notifications = GiteeOauthUtil.fetchNotifications(token, params);
            return AjaxResult.success(notifications);
        } catch (Exception ex) {
            log.error("获取Gitee通知失败", ex);
            return AjaxResult.error(ex.getMessage());
        }
    }

    private String getAccessToken() {
        Long userId = SecurityUtils.getUserId();
        String token = redisCache.getCacheObject(GiteeCacheKeyUtil.getAccessTokenKey(userId));
        return StringUtils.isBlank(token) ? null : token;
    }

    private String normalizeRedirect(String redirect) {
        if (StringUtils.isBlank(redirect)) {
            return DEFAULT_REDIRECT_PATH;
        }
        if (redirect.startsWith("http") || redirect.startsWith("//")) {
            return DEFAULT_REDIRECT_PATH;
        }
        if (!redirect.startsWith("/")) {
            return "/" + redirect;
        }
        return redirect;
    }
}
