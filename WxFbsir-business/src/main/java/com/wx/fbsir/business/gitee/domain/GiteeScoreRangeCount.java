package com.wx.fbsir.business.gitee.domain;

import java.io.Serializable;

public class GiteeScoreRangeCount implements Serializable {
    private static final long serialVersionUID = 1L;

    private String scoreRange;
    private Integer userCount;

    public String getScoreRange() {
        return scoreRange;
    }

    public void setScoreRange(String scoreRange) {
        this.scoreRange = scoreRange;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}
