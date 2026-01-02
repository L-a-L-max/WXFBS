package com.wx.fbsir.business.gitee.domain;

import java.io.Serializable;

public class GiteeBindInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String giteeId;
    private String login;
    private String name;
    private String avatarUrl;
    private String email;
    private String accessToken;
    private String tokenType;
    private Long createdAt;

    public String getGiteeId() {
        return giteeId;
    }

    public void setGiteeId(String giteeId) {
        this.giteeId = giteeId;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
