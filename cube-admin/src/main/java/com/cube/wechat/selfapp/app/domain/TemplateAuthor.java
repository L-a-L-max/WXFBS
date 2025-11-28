package com.cube.wechat.selfapp.app.domain;

import java.math.BigDecimal;

/**
 * 模板作者实体
 */
public class TemplateAuthor {

    private Long userId;

    private BigDecimal incomeTotal;

    private Integer releaseTemplateCount;

    private Integer level;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public Integer getReleaseTemplateCount() {
        return releaseTemplateCount;
    }

    public void setReleaseTemplateCount(Integer releaseTemplateCount) {
        this.releaseTemplateCount = releaseTemplateCount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}

