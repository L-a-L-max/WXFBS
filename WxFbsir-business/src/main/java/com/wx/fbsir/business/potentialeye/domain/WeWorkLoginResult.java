package com.wx.fbsir.business.potentialeye.domain;

import java.io.Serializable;

/**
 * 企业微信登录结果
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
