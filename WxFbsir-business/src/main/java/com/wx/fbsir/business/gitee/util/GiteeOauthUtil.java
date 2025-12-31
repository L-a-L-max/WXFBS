package com.wx.fbsir.business.gitee.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public final class GiteeOauthUtil {
    public static final String DEFAULT_CLIENT_ID = "57d5b862a86bt";
    public static final String AUTHORIZE_URL = "https://gitee.com/oauth/authorize";
    public static final String TOKEN_URL = "https://gitee.com/oauth/token";
    public static final String USER_URL = "https://gitee.com/api/v5/user";
    public static final String USER_ISSUES_URL = "https://gitee.com/api/v5/user/issues";
    public static final String USER_REPOS_URL = "https://gitee.com/api/v5/user/repos";
    public static final String NOTIFICATIONS_URL = "https://gitee.com/api/v5/notifications/threads";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private GiteeOauthUtil() {
    }

    public static String buildAuthorizeUrl(String clientId, String callbackUrl) {
        String encoded = URLEncoder.encode(callbackUrl, StandardCharsets.UTF_8);
        return AUTHORIZE_URL
            + "?response_type=code"
            + "&client_id=" + clientId
            + "&redirect_uri=" + encoded;
    }

    public static String buildAuthorizeUrl(String clientId, String callbackUrl, String state) {
        String encoded = URLEncoder.encode(callbackUrl, StandardCharsets.UTF_8);
        String encodedState = URLEncoder.encode(state, StandardCharsets.UTF_8);
        return AUTHORIZE_URL
            + "?response_type=code"
            + "&client_id=" + clientId
            + "&redirect_uri=" + encoded
            + "&state=" + encodedState;
    }

    public static GiteeOauthToken exchangeCodeForToken(String clientId, String clientSecret, String callbackUrl, String code)
        throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", callbackUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        String response = restTemplate.postForObject(TOKEN_URL, entity, String.class);
        if (response == null || response.isBlank()) {
            throw new Exception("gitee token响应为空");
        }

        JsonNode jsonNode = OBJECT_MAPPER.readTree(response);
        if (!jsonNode.has("access_token")) {
            String error = jsonNode.has("error") ? jsonNode.get("error").asText() : "unknown";
            String desc = jsonNode.has("error_description") ? jsonNode.get("error_description").asText() : "";
            throw new Exception("gitee获取token失败: " + error + (desc.isBlank() ? "" : (": " + desc)));
        }

        GiteeOauthToken token = new GiteeOauthToken();
        token.setAccessToken(jsonNode.get("access_token").asText());
        token.setTokenType(jsonNode.has("token_type") ? jsonNode.get("token_type").asText() : "");
        token.setRefreshToken(jsonNode.has("refresh_token") ? jsonNode.get("refresh_token").asText() : "");
        token.setExpiresIn(jsonNode.has("expires_in") ? jsonNode.get("expires_in").asLong() : 0L);
        token.setScope(jsonNode.has("scope") ? jsonNode.get("scope").asText() : "");
        token.setCreatedAt(jsonNode.has("created_at") ? jsonNode.get("created_at").asLong() : 0L);
        return token;
    }

    public static GiteeUserProfile fetchUserProfile(String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = USER_URL + "?access_token=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
        String response = restTemplate.getForObject(url, String.class);
        if (response == null || response.isBlank()) {
            throw new Exception("gitee用户信息响应为空");
        }
        JsonNode jsonNode = OBJECT_MAPPER.readTree(response);
        if (!jsonNode.has("id")) {
            String message = jsonNode.has("message") ? jsonNode.get("message").asText() : "unknown";
            throw new Exception("gitee获取用户信息失败: " + message);
        }

        GiteeUserProfile profile = new GiteeUserProfile();
        profile.setId(jsonNode.get("id").asText());
        profile.setLogin(jsonNode.has("login") ? jsonNode.get("login").asText() : "");
        profile.setName(jsonNode.has("name") ? jsonNode.get("name").asText() : "");
        profile.setAvatarUrl(jsonNode.has("avatar_url") ? jsonNode.get("avatar_url").asText() : "");
        profile.setEmail(jsonNode.has("email") ? jsonNode.get("email").asText() : "");
        return profile;
    }

    public static JsonNode fetchUserProfileJson(String accessToken) throws Exception {
        return fetchJson(USER_URL, accessToken, Collections.emptyMap());
    }

    public static JsonNode fetchUserRepos(String accessToken, Map<String, String> params) throws Exception {
        return fetchJson(USER_REPOS_URL, accessToken, params);
    }

    public static JsonNode fetchUserIssues(String accessToken, Map<String, String> params) throws Exception {
        Map<String, String> resolvedParams = new LinkedHashMap<>();
        if (params != null && !params.isEmpty()) {
            resolvedParams.putAll(params);
        }
        resolvedParams.put("filter", "all");
        resolvedParams.put("state", "all");
        return fetchJson(USER_ISSUES_URL, accessToken, resolvedParams);
    }

    public static JsonNode fetchNotifications(String accessToken, Map<String, String> params) throws Exception {
        return fetchJson(NOTIFICATIONS_URL, accessToken, params);
    }

    private static JsonNode fetchJson(String baseUrl, String accessToken, Map<String, String> params) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = buildUrl(baseUrl, accessToken, params);
        String response = restTemplate.getForObject(url, String.class);
        if (response == null || response.isBlank()) {
            throw new Exception("gitee响应为空");
        }
        return OBJECT_MAPPER.readTree(response);
    }

    private static String buildUrl(String baseUrl, String accessToken, Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl);
        builder.append("?access_token=").append(URLEncoder.encode(accessToken, StandardCharsets.UTF_8));
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key == null || key.isBlank() || value == null) {
                    continue;
                }
                if ("access_token".equalsIgnoreCase(key)) {
                    continue;
                }
                builder.append("&")
                    .append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            }
        }
        return builder.toString();
    }

    public static final class GiteeOauthToken {
        private String accessToken;
        private String tokenType;
        private String refreshToken;
        private long expiresIn;
        private String scope;
        private long createdAt;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        @Override
        public String toString() {
            return "GiteeOauthToken{" +
                "accessToken='" + mask(accessToken) + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", refreshToken='" + mask(refreshToken) + '\'' +
                ", expiresIn=" + expiresIn +
                ", scope='" + scope + '\'' +
                ", createdAt=" + createdAt +
                '}';
        }
    }

    public static final class GiteeUserProfile {
        private String id;
        private String login;
        private String name;
        private String avatarUrl;
        private String email;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    private static String mask(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        if (value.length() <= 6) {
            return "******";
        }
        return value.substring(0, 3) + "****" + value.substring(value.length() - 3);
    }
}
