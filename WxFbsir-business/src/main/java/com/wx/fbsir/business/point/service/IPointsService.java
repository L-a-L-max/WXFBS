package com.wx.fbsir.business.point.service;

import java.util.List;
import java.util.Map;
import com.wx.fbsir.common.core.domain.AjaxResult;
import com.wx.fbsir.common.core.page.TableDataInfo;
import com.wx.fbsir.business.point.domain.PointsRecord;

/**
 * 积分Service接口
 * 
 * @author wxfbsir
 * @date 2025-12-08
 */
public interface IPointsService {
    /**
     * 积分发放/扣减
     * 
     * @param userId 用户ID
     * @param ruleCode 规则编码（唯一标识）
     * @param changeAmount 积分变动值（可选，不传则使用规则默认值）
     * @return 结果
     */
    public AjaxResult changePoints(Long userId, String ruleCode, Integer changeAmount);
    
    /**
     * 查询用户积分余额
     * 
     * @param userId 用户ID
     * @return 积分余额
     */
    public Integer getUserPoints(Long userId);
    
    /**
     * 查询积分明细记录
     * 
     * @param pointsRecord 积分记录查询条件
     * @return 积分记录列表
     */
    public List<PointsRecord> getPointsRecord(PointsRecord pointsRecord);
    
    /**
     * 获取积分概览
     * 
     * @param userId 用户ID
     * @return 积分概览信息
     */
    public Map<String, Object> getPointsSummary(Long userId);
    
    /**
     * 获取积分任务清单
     * 
     * @param userId 用户ID（可选，用于判断任务完成状态）
     * @return 积分任务列表
     */
    public List<Map<String, Object>> getPointsTaskList(Long userId);
}

