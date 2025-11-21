package com.cube.system.service;

import com.cube.common.core.domain.entity.AiAgent;

import java.util.List;

/**
 * AI智能体Service接口
 *
 * @author cube
 * @date 2025-01-14
 */
public interface IAiAgentService
{
    /**
     * 查询AI智能体
     *
     * @param id AI智能体主键
     * @return AI智能体
     */
    public AiAgent selectAiAgentById(Long id);

    /**
     * 根据智能体代码查询AI智能体
     *
     * @param agentCode 智能体代码
     * @return AI智能体
     */
    public AiAgent selectAiAgentByCode(String agentCode);

    /**
     * 查询AI智能体列表
     *
     * @param aiAgent AI智能体
     * @return AI智能体集合
     */
    public List<AiAgent> selectAiAgentList(AiAgent aiAgent);

    /**
     * 查询用户可用的AI智能体列表
     *
     * @param userId 用户ID
     * @return AI智能体集合
     */
    public List<AiAgent> selectUserAvailableAgents(String userId);

    /**
     * 查询所有上架的AI智能体列表（按排序顺序）
     *
     * @return AI智能体集合
     */
    public List<AiAgent> selectActiveAgentList();

    /**
     * 新增AI智能体
     *
     * @param aiAgent AI智能体
     * @return 结果
     */
    public int insertAiAgent(AiAgent aiAgent);

    /**
     * 修改AI智能体
     *
     * @param aiAgent AI智能体
     * @return 结果
     */
    public int updateAiAgent(AiAgent aiAgent);

    /**
     * 批量删除AI智能体
     *
     * @param ids 需要删除的AI智能体主键集合
     * @return 结果
     */
    public int deleteAiAgentByIds(Long[] ids);

    /**
     * 删除AI智能体信息
     *
     * @param id AI智能体主键
     * @return 结果
     */
    public int deleteAiAgentById(Long id);

    /**
     * 校验智能体代码是否唯一
     *
     * @param aiAgent AI智能体信息
     * @return 结果
     */
    public boolean checkAgentCodeUnique(AiAgent aiAgent);

    /**
     * 更新AI在线状态
     *
     * @param agentCode 智能体代码
     * @param onlineStatus 在线状态
     * @return 结果
     */
    public int updateOnlineStatus(String agentCode, Integer onlineStatus);

    /**
     * 批量更新AI在线状态
     *
     * @param agentCodes 智能体代码列表
     * @param onlineStatus 在线状态
     * @return 结果
     */
    public int batchUpdateOnlineStatus(List<String> agentCodes, Integer onlineStatus);

    /**
     * 检查用户是否有权限使用某个AI
     *
     * @param userId 用户ID
     * @param agentCode 智能体代码
     * @return 是否有权限
     */
    public boolean checkUserPermission(String userId, String agentCode);

    /**
     * 刷新AI智能体缓存
     */
    public void refreshCache();

    /**
     * 清除AI智能体缓存
     *
     * @param agentCode 智能体代码，为空则清除所有缓存
     */
    public void clearCache(String agentCode);
}
