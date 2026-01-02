package com.wx.fbsir.business.gitee.domain;

import java.util.List;

public class GiteeUserSummaryRequest {
    private List<Long> userIds;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
