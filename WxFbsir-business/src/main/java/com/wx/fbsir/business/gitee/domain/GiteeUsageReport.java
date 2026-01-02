package com.wx.fbsir.business.gitee.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wx.fbsir.common.annotation.Excel;
import com.wx.fbsir.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GiteeUsageReport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long reportId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计日期", dateFormat = "yyyy-MM-dd")
    private Date reportDate;

    @Excel(name = "当日新增绑定用户数")
    private Integer newBindCount;

    @Excel(name = "当日评测总次数")
    private Integer dailyEvaluationCount;

    @Excel(name = "当日活跃评测用户数")
    private Integer dailyActiveUserCount;

    @Excel(name = "累计绑定用户数")
    private Integer totalBindCount;

    @Excel(name = "评分区间分布")
    private String scoreDistribution;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getNewBindCount() {
        return newBindCount;
    }

    public void setNewBindCount(Integer newBindCount) {
        this.newBindCount = newBindCount;
    }

    public Integer getDailyEvaluationCount() {
        return dailyEvaluationCount;
    }

    public void setDailyEvaluationCount(Integer dailyEvaluationCount) {
        this.dailyEvaluationCount = dailyEvaluationCount;
    }

    public Integer getDailyActiveUserCount() {
        return dailyActiveUserCount;
    }

    public void setDailyActiveUserCount(Integer dailyActiveUserCount) {
        this.dailyActiveUserCount = dailyActiveUserCount;
    }

    public Integer getTotalBindCount() {
        return totalBindCount;
    }

    public void setTotalBindCount(Integer totalBindCount) {
        this.totalBindCount = totalBindCount;
    }

    public String getScoreDistribution() {
        return scoreDistribution;
    }

    public void setScoreDistribution(String scoreDistribution) {
        this.scoreDistribution = scoreDistribution;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("reportId", getReportId())
            .append("reportDate", getReportDate())
            .append("newBindCount", getNewBindCount())
            .append("dailyEvaluationCount", getDailyEvaluationCount())
            .append("dailyActiveUserCount", getDailyActiveUserCount())
            .append("totalBindCount", getTotalBindCount())
            .append("scoreDistribution", getScoreDistribution())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
