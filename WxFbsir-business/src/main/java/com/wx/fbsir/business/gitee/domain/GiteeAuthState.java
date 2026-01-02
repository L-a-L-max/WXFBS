package com.wx.fbsir.business.gitee.domain;

import java.io.Serializable;

public class GiteeAuthState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String redirect;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
