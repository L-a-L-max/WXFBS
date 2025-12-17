package com.demo.wxchat.mapper;

import com.demo.wxchat.domain.WeWorkConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 企业微信配置Mapper
 */
@Mapper
public interface WeWorkConfigMapper {

    /**
     * 根据团队ID查询配置
     */
    WeWorkConfig selectByTeamId(@Param("teamId") Long teamId);

    /**
     * 插入配置
     */
    int insert(WeWorkConfig config);

    /**
     * 更新配置
     */
    int update(WeWorkConfig config);
}
