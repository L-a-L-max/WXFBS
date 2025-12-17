package com.demo.wxchat.domain;

import java.io.Serializable;

/**
 * 企业微信登录结果
 */
public class WeWorkLoginResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private Long teamId;
    private String weWorkUserId;
    private String memberName;
    private String groupChatId;
    private Boolean inGroup;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getWeWorkUserId() {
        return weWorkUserId;
    }

    public void setWeWorkUserId(String weWorkUserId) {
        this.weWorkUserId = weWorkUserId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }

    public Boolean getInGroup() {
        return inGroup;
    }

    public void setInGroup(Boolean inGroup) {
        this.inGroup = inGroup;
    }
}
