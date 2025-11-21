package com.cube.system.mapper;

import com.cube.common.core.domain.entity.AiAgent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI智能体Mapper接口
 *
 * @author cube
 * @date 2025-01-14
 */
@Mapper
public interface AiAgentMapper
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
     * @param roleIds 角色ID列表
     * @return AI智能体集合
     */
    public List<AiAgent> selectUserAvailableAgents(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

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
     * 删除AI智能体
     *
     * @param id AI智能体主键
     * @return 结果
     */
    public int deleteAiAgentById(Long id);

    /**
     * 批量删除AI智能体
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiAgentByIds(Long[] ids);

    /**
     * 校验智能体代码是否唯一
     *
     * @param agentCode 智能体代码
     * @return 结果
     */
    public AiAgent checkAgentCodeUnique(String agentCode);

    /**
     * 更新AI在线状态
     *
     * @param agentCode 智能体代码
     * @param onlineStatus 在线状态
     * @return 结果
     */
    public int updateOnlineStatus(@Param("agentCode") String agentCode, @Param("onlineStatus") Integer onlineStatus);

    /**
     * 批量更新AI在线状态
     *
     * @param agentCodes 智能体代码列表
     * @param onlineStatus 在线状态
     * @return 结果
     */
    public int batchUpdateOnlineStatus(@Param("agentCodes") List<String> agentCodes, @Param("onlineStatus") Integer onlineStatus);
}
