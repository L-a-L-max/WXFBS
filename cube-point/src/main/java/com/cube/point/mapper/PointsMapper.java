package com.cube.point.mapper;

import com.cube.point.domain.Points;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface PointsMapper {

    /*
    * 修改用户积分
    * */
    int updateUserPoints(Points points);

    /**
     * 后台保存用户积分记录
     * */
    int saveUserPointsRecord(Points points);

    /**
     * 获取用户积分记录
     * */
    List<Map> getUserPointsRecord(Points points);

    /**
     * 获取用户积分
     * */
    Integer getUserPoints(String userId);

    /**
     * 获取积分规则
     * */
    Integer getPointRuleVal(String changeType);

    /**
     * 获取积分规则配置（包括remark字段）
     * */
    Map<String, Object> getPointRuleConfig(String changeType);

    /**
     * 设置用户积分
     * */
    int setUserPoints(@Param("userId") String userId,@Param("changeType") String changeType,@Param("changeAmount") Integer changeAmount);

    /**
     * 保存用户积分明细
     * */
    int setUserPointRecord(@Param("userId") String userId,@Param("changeType") String changeType,@Param("changeAmount") Integer changeAmount);


    /**
    * 获取用户规则
    * */
    int getPointRule(String changeType);

    /**
    * 校验积分是否允许设置
    * */
    int checkPointIsOk(@Param("changeType") String changeType,@Param("userId") String userId,@Param("isToday") Integer isToday);

    /**
     * 获取积分任务
     * */
    List<Map> getPointTask();

    /**
     * 获取积分任务清单（包含详细信息）
     * */
    List<Map<String, Object>> getPointTaskList();

    /**
     * 检查用户是否完成过某个任务（查询积分记录表）
     * @param userId 用户ID
     * @param changeType 任务类型
     * @return 完成次数
     */
    int checkUserTaskCompleted(@Param("userId") String userId, @Param("changeType") String changeType);

    /**
     * 统计从指定时间开始的积分获取总额（仅统计正向积分）
     */
    Integer sumUserPointsChangeSince(@Param("userId") String userId, @Param("startTime") LocalDateTime startTime);

    /**
     * 获取最近一次积分变动记录
     */
    Map<String, Object> getLastPointChange(@Param("userId") String userId);

    /**
     * 保存用户账号上链
     * */
    int saveUserGethAccount(Map map);


    /**
     * 保存私链交易记录
     * */
    int saveUserGethRecord(Map map);

    /**
     * 查询出没有上链的用户
     * */
    List<Map> getNoGethUserId();



}
