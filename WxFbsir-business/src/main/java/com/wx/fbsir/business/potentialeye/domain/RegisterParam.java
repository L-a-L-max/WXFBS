package com.wx.fbsir.business.potentialeye.domain;

import com.wx.fbsir.common.core.domain.model.LoginBody;

public class RegisterParam extends LoginBody {

    private String sessionId;

    public RegisterParam() {
    }

    public RegisterParam(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "RegisterParam{" +
                "sessionId='" + sessionId + '\'' +
                '}';
    }
}
