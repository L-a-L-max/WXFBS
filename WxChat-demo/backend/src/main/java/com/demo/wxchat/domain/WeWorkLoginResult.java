package com.demo.wxchat.domain;

import java.io.Serializable;

/**
 * 企业微信登录结果（简化版）
 * 只包含必要的token和userId，不依赖数据库表
 */
public class WeWorkLoginResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private String weWorkUserId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWeWorkUserId() {
        return weWorkUserId;
    }

    public void setWeWorkUserId(String weWorkUserId) {
        this.weWorkUserId = weWorkUserId;
    }
}
