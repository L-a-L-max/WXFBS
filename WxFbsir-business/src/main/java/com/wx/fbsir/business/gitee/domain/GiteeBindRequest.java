package com.wx.fbsir.business.gitee.domain;

public class GiteeBindRequest {
    private String bindToken;
    private String username;
    private String password;

    public String getBindToken() {
        return bindToken;
    }

    public void setBindToken(String bindToken) {
        this.bindToken = bindToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
