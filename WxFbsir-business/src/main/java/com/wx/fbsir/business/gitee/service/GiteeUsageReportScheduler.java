package com.wx.fbsir.business.gitee.service;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GiteeUsageReportScheduler {
    private static final Logger log = LoggerFactory.getLogger(GiteeUsageReportScheduler.class);

    @Autowired
    private GiteeUsageReportService giteeUsageReportService;

    @Scheduled(cron = "0 5 0 * * ?")
    public void generateDailyReport() {
        LocalDate reportDate = LocalDate.now().minusDays(1);
        try {
            giteeUsageReportService.generateDailyReport(reportDate);
        } catch (Exception ex) {
            log.error("Failed to generate gitee usage report for {}", reportDate, ex);
        }
    }
}
