package com.wx.fbsir.business.gitee.mapper;

import com.wx.fbsir.business.gitee.domain.GiteeUsageReport;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GiteeUsageReportMapper {
    GiteeUsageReport selectByReportDate(@Param("reportDate") Date reportDate);

    List<GiteeUsageReport> selectGiteeUsageReportList(GiteeUsageReport report);

    int insertGiteeUsageReport(GiteeUsageReport report);

    int updateGiteeUsageReport(GiteeUsageReport report);
}
