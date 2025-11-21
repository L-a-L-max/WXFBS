package com.cube.system.mapper;

import com.cube.common.core.domain.entity.AiAgentPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI智能体权限Mapper接口
 *
 * @author cube
 * @date 2025-01-14
 */
@Mapper
public interface AiAgentPermissionMapper
{
    /**
     * 查询AI智能体权限
     *
     * @param id AI智能体权限主键
     * @return AI智能体权限
     */
    public AiAgentPermission selectAiAgentPermissionById(Long id);

    /**
     * 查询AI智能体权限列表
     *
     * @param aiAgentPermission AI智能体权限
     * @return AI智能体权限集合
     */
    public List<AiAgentPermission> selectAiAgentPermissionList(AiAgentPermission aiAgentPermission);

    /**
     * 查询用户对某个AI的权限
     *
     * @param agentId 智能体ID
     * @param userId 用户ID
     * @return 权限动作（0-禁止，1-允许，null-无记录）
     */
    public Integer selectUserPermission(@Param("agentId") Long agentId, @Param("userId") String userId);

    /**
     * 查询角色对某个AI的权限
     *
     * @param agentId 智能体ID
     * @param roleIds 角色ID列表
     * @return 权限动作（0-禁止，1-允许，null-无记录）
     */
    public Integer selectRolePermission(@Param("agentId") Long agentId, @Param("roleIds") List<String> roleIds);

    /**
     * 查询用户的所有AI权限
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return AI权限列表
     */
    public List<AiAgentPermission> selectUserAllPermissions(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

    /**
     * 新增AI智能体权限
     *
     * @param aiAgentPermission AI智能体权限
     * @return 结果
     */
    public int insertAiAgentPermission(AiAgentPermission aiAgentPermission);

    /**
     * 修改AI智能体权限
     *
     * @param aiAgentPermission AI智能体权限
     * @return 结果
     */
    public int updateAiAgentPermission(AiAgentPermission aiAgentPermission);

    /**
     * 删除AI智能体权限
     *
     * @param id AI智能体权限主键
     * @return 结果
     */
    public int deleteAiAgentPermissionById(Long id);

    /**
     * 批量删除AI智能体权限
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiAgentPermissionByIds(Long[] ids);

    /**
     * 根据智能体ID删除权限
     *
     * @param agentId 智能体ID
     * @return 结果
     */
    public int deletePermissionByAgentId(Long agentId);

    /**
     * 批量插入权限
     *
     * @param permissions 权限列表
     * @return 结果
     */
    public int batchInsertPermissions(@Param("permissions") List<AiAgentPermission> permissions);

    /**
     * 设置用户权限（存在则更新，不存在则插入）
     *
     * @param agentId 智能体ID
     * @param userId 用户ID
     * @param permissionAction 权限动作
     * @param createBy 创建者
     * @return 结果
     */
    public int setUserPermission(@Param("agentId") Long agentId, @Param("userId") String userId, 
                                @Param("permissionAction") Integer permissionAction, @Param("createBy") String createBy);

    /**
     * 设置角色权限（存在则更新，不存在则插入）
     *
     * @param agentId 智能体ID
     * @param roleId 角色ID
     * @param permissionAction 权限动作
     * @param createBy 创建者
     * @return 结果
     */
    public int setRolePermission(@Param("agentId") Long agentId, @Param("roleId") String roleId, 
                                @Param("permissionAction") Integer permissionAction, @Param("createBy") String createBy);
}
