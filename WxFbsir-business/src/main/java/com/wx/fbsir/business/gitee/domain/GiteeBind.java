package com.wx.fbsir.business.gitee.domain;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GiteeBind implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long bindId;
    private Long userId;
    private String giteeUserId;
    private String giteeUsername;
    private String giteeAvatar;
    private Date bindTime;

    public Long getBindId() {
        return bindId;
    }

    public void setBindId(Long bindId) {
        this.bindId = bindId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGiteeUserId() {
        return giteeUserId;
    }

    public void setGiteeUserId(String giteeUserId) {
        this.giteeUserId = giteeUserId;
    }

    public String getGiteeUsername() {
        return giteeUsername;
    }

    public void setGiteeUsername(String giteeUsername) {
        this.giteeUsername = giteeUsername;
    }

    public String getGiteeAvatar() {
        return giteeAvatar;
    }

    public void setGiteeAvatar(String giteeAvatar) {
        this.giteeAvatar = giteeAvatar;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("bindId", getBindId())
            .append("userId", getUserId())
            .append("giteeUserId", getGiteeUserId())
            .append("giteeUsername", getGiteeUsername())
            .append("giteeAvatar", getGiteeAvatar())
            .append("bindTime", getBindTime())
            .toString();
    }
}
