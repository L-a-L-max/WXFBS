package com.cube.system.service;

import com.cube.common.core.domain.entity.AiAgentPermission;

import java.util.List;

/**
 * AI智能体权限Service接口
 *
 * @author cube
 * @date 2025-01-14
 */
public interface IAiAgentPermissionService
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
     * 检查用户对某个AI的权限
     *
     * @param agentId 智能体ID
     * @param userId 用户ID
     * @return 是否有权限
     */
    public boolean checkUserPermission(Long agentId, String userId);

    /**
     * 查询用户的所有AI权限
     *
     * @param userId 用户ID
     * @return AI权限列表
     */
    public List<AiAgentPermission> selectUserAllPermissions(String userId);

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
     * 批量删除AI智能体权限
     *
     * @param ids 需要删除的AI智能体权限主键集合
     * @return 结果
     */
    public int deleteAiAgentPermissionByIds(Long[] ids);

    /**
     * 删除AI智能体权限信息
     *
     * @param id AI智能体权限主键
     * @return 结果
     */
    public int deleteAiAgentPermissionById(Long id);

    /**
     * 设置用户权限
     *
     * @param agentId 智能体ID
     * @param userId 用户ID
     * @param permissionAction 权限动作（0-禁止，1-允许）
     * @return 结果
     */
    public int setUserPermission(Long agentId, String userId, Integer permissionAction);

    /**
     * 设置角色权限
     *
     * @param agentId 智能体ID
     * @param roleId 角色ID
     * @param permissionAction 权限动作（0-禁止，1-允许）
     * @return 结果
     */
    public int setRolePermission(Long agentId, String roleId, Integer permissionAction);

    /**
     * 批量设置权限
     *
     * @param permissions 权限列表
     * @return 结果
     */
    public int batchSetPermissions(List<AiAgentPermission> permissions);

    /**
     * 删除智能体的所有权限
     *
     * @param agentId 智能体ID
     * @return 结果
     */
    public int deletePermissionByAgentId(Long agentId);

    /**
     * 刷新权限缓存
     *
     * @param userId 用户ID，为空则刷新所有用户缓存
     */
    public void refreshPermissionCache(String userId);

    /**
     * 清除权限缓存
     *
     * @param userId 用户ID，为空则清除所有用户缓存
     */
    public void clearPermissionCache(String userId);
}
