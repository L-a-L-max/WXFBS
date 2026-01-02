package com.wx.fbsir.business.gitee.service;

import com.alibaba.fastjson2.JSON;
import com.wx.fbsir.business.gitee.domain.GiteeScoreRangeCount;
import com.wx.fbsir.business.gitee.domain.GiteeUsageReport;
import com.wx.fbsir.business.gitee.mapper.GiteeAnalysisReportMapper;
import com.wx.fbsir.business.gitee.mapper.GiteeBindMapper;
import com.wx.fbsir.business.gitee.mapper.GiteeUsageReportMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GiteeUsageReportService {

    @Autowired
    private GiteeBindMapper giteeBindMapper;

    @Autowired
    private GiteeAnalysisReportMapper giteeAnalysisReportMapper;

    @Autowired
    private GiteeUsageReportMapper giteeUsageReportMapper;

    public List<GiteeUsageReport> selectGiteeUsageReportList(GiteeUsageReport report) {
        refreshTodayReportIfNeeded(report);
        return giteeUsageReportMapper.selectGiteeUsageReportList(report);
    }

    public void generateDailyReport(LocalDate reportDate) {
        if (reportDate == null) {
            return;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        Date startTime = Date.from(reportDate.atStartOfDay(zoneId).toInstant());
        Date endTime = Date.from(reportDate.plusDays(1).atStartOfDay(zoneId).toInstant());
        Date reportDateValue = java.sql.Date.valueOf(reportDate);

        int newBindCount = giteeBindMapper.countNewBindByRange(startTime, endTime);
        int totalBindCount = giteeBindMapper.countAll();
        int dailyEvaluationCount = giteeAnalysisReportMapper.countByRange(startTime, endTime);
        int dailyActiveUserCount = giteeAnalysisReportMapper.countDistinctUserByRange(startTime, endTime);
        List<GiteeScoreRangeCount> scoreRanges = giteeAnalysisReportMapper.selectScoreDistributionByRange(startTime, endTime);
        String scoreDistribution = JSON.toJSONString(scoreRanges);

        GiteeUsageReport existing = giteeUsageReportMapper.selectByReportDate(reportDateValue);
        if (existing == null) {
            GiteeUsageReport report = new GiteeUsageReport();
            report.setReportDate(reportDateValue);
            report.setNewBindCount(newBindCount);
            report.setDailyEvaluationCount(dailyEvaluationCount);
            report.setDailyActiveUserCount(dailyActiveUserCount);
            report.setTotalBindCount(totalBindCount);
            report.setScoreDistribution(scoreDistribution);
            report.setCreateTime(new Date());
            giteeUsageReportMapper.insertGiteeUsageReport(report);
            return;
        }

        existing.setReportDate(reportDateValue);
        existing.setNewBindCount(newBindCount);
        existing.setDailyEvaluationCount(dailyEvaluationCount);
        existing.setDailyActiveUserCount(dailyActiveUserCount);
        existing.setTotalBindCount(totalBindCount);
        existing.setScoreDistribution(scoreDistribution);
        existing.setUpdateTime(new Date());
        giteeUsageReportMapper.updateGiteeUsageReport(existing);
    }

    private void refreshTodayReportIfNeeded(GiteeUsageReport report) {
        LocalDate today = LocalDate.now();
        LocalDate reportDate = toLocalDate(report != null ? report.getReportDate() : null);
        if (reportDate != null) {
            if (today.equals(reportDate)) {
                generateDailyReport(today);
            }
            return;
        }

        Map<String, Object> params = report != null ? report.getParams() : null;
        LocalDate begin = toLocalDate(params != null ? params.get("beginTime") : null);
        LocalDate end = toLocalDate(params != null ? params.get("endTime") : null);
        if (begin != null && end != null) {
            if (!today.isBefore(begin) && !today.isAfter(end)) {
                generateDailyReport(today);
            }
            return;
        }

        generateDailyReport(today);
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (value instanceof String) {
            String text = ((String) value).trim();
            if (!StringUtils.hasText(text)) {
                return null;
            }
            if (text.length() > 10) {
                text = text.substring(0, 10);
            }
            try {
                return LocalDate.parse(text);
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }
}
