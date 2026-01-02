package com.wx.fbsir.business.gitee.mapper;

import com.wx.fbsir.business.gitee.domain.GiteeBind;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GiteeBindMapper {
    GiteeBind selectByGiteeUserId(@Param("giteeUserId") String giteeUserId);

    GiteeBind selectByUserId(@Param("userId") Long userId);

    List<GiteeBind> selectByUserIds(@Param("userIds") List<Long> userIds);

    int countAll();

    int countNewBindByRange(@Param("startTime") java.util.Date startTime, @Param("endTime") java.util.Date endTime);

    int insertGiteeBind(GiteeBind bind);

    int updateGiteeBind(GiteeBind bind);

    int deleteByUserId(@Param("userId") Long userId);
}
