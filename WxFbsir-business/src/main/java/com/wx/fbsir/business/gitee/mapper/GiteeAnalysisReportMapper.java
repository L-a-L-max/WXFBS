package com.wx.fbsir.business.gitee.mapper;

import com.wx.fbsir.business.gitee.domain.GiteeAnalysisReport;
import com.wx.fbsir.business.gitee.domain.GiteeScoreRangeCount;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GiteeAnalysisReportMapper {
    int insertGiteeAnalysisReport(GiteeAnalysisReport report);

    List<GiteeAnalysisReport> selectLatestByUserIds(@Param("userIds") List<Long> userIds);

    int countByRange(@Param("startTime") java.util.Date startTime, @Param("endTime") java.util.Date endTime);

    int countDistinctUserByRange(@Param("startTime") java.util.Date startTime, @Param("endTime") java.util.Date endTime);

    List<GiteeScoreRangeCount> selectScoreDistributionByRange(@Param("startTime") java.util.Date startTime, @Param("endTime") java.util.Date endTime);
}
