package com.cube.system.service.impl;

import com.cube.common.core.domain.entity.AiAgentPermission;
import com.cube.common.core.domain.entity.SysRole;
import com.cube.common.core.redis.RedisCache;
import com.cube.common.utils.SecurityUtils;
import com.cube.common.utils.StringUtils;
import com.cube.system.mapper.AiAgentPermissionMapper;
import com.cube.system.mapper.SysRoleMapper;
import com.cube.system.service.IAiAgentPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * AI智能体权限Service业务层处理
 *
 * @author cube
 * @date 2025-01-14
 */
@Service
public class AiAgentPermissionServiceImpl implements IAiAgentPermissionService
{
    private static final Logger log = LoggerFactory.getLogger(AiAgentPermissionServiceImpl.class);

    @Autowired
    private AiAgentPermissionMapper aiAgentPermissionMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询AI智能体权限
     *
     * @param id AI智能体权限主键
     * @return AI智能体权限
     */
    @Override
    public AiAgentPermission selectAiAgentPermissionById(Long id)
    {
        return aiAgentPermissionMapper.selectAiAgentPermissionById(id);
    }

    /**
     * 查询AI智能体权限列表
     *
     * @param aiAgentPermission AI智能体权限
     * @return AI智能体权限
     */
    @Override
    public List<AiAgentPermission> selectAiAgentPermissionList(AiAgentPermission aiAgentPermission)
    {
        return aiAgentPermissionMapper.selectAiAgentPermissionList(aiAgentPermission);
    }

    /**
     * 检查用户对某个AI的权限
     * 
     * 权限检查优先级：
     * 1. 用户级别权限检查：如果有用户级权限记录，则按用户权限决定
     * 2. 角色级别权限检查：如果有角色级权限记录，则按角色权限决定
     * 3. 默认拒绝：如果没有明确权限记录，则拒绝
     *
     * @param agentId 智能体ID
     * @param userId 用户ID
     * @return 是否有权限
     */
    @Override
    public boolean checkUserPermission(Long agentId, String userId)
    {
        // 1. 检查用户级别权限（优先级最高）
        Integer userPermission = aiAgentPermissionMapper.selectUserPermission(agentId, userId);
        if (userPermission != null)
        {
            // 有明确的用户级权限记录，按用户权限决定
            return userPermission == 1;
        }

        // 2. 检查角色级别权限（优先级次高）
        List<SysRole> roles = roleMapper.selectRolePermissionByUserId(Long.valueOf(userId));
        List<String> roleIds = roles.stream().map(role -> String.valueOf(role.getRoleId())).collect(Collectors.toList());
        
        if (!roleIds.isEmpty())
        {
            Integer rolePermission = aiAgentPermissionMapper.selectRolePermission(agentId, roleIds);
            if (rolePermission != null)
            {
                // 有明确的角色级权限记录，按角色权限决定
                return rolePermission == 1;
            }
        }

        // 3. 默认拒绝（如果没有明确的权限设置）
        return false;
    }

    /**
     * 查询用户的所有AI权限
     *
     * @param userId 用户ID
     * @return AI权限列表
     */
    @Override
    public List<AiAgentPermission> selectUserAllPermissions(String userId)
    {
        // 启用Redis缓存，提升查询性能
        String cacheKey = "ai:permission:user:" + userId + ":all";
        List<AiAgentPermission> cachedPermissions = redisCache.getCacheObject(cacheKey);
        if (cachedPermissions != null)
        {
            return cachedPermissions;
        }

        // 获取用户角色
        List<SysRole> roles = roleMapper.selectRolePermissionByUserId(Long.valueOf(userId));
        List<String> roleIds = roles.stream().map(role -> String.valueOf(role.getRoleId())).collect(Collectors.toList());

        // 查询用户的所有权限
        List<AiAgentPermission> permissions = aiAgentPermissionMapper.selectUserAllPermissions(userId, roleIds);

        // 缓存1小时
        redisCache.setCacheObject(cacheKey, permissions, 1, TimeUnit.HOURS);
        return permissions;
    }

    /**
     * 新增AI智能体权限
     *
     * @param aiAgentPermission AI智能体权限
     * @return 结果
     */
    @Override
    @Transactional
    public int insertAiAgentPermission(AiAgentPermission aiAgentPermission)
    {
        aiAgentPermission.setCreateBy(SecurityUtils.getUsername());
        int result = aiAgentPermissionMapper.insertAiAgentPermission(aiAgentPermission);
        if (result > 0)
        {
            // 清除相关缓存
            clearPermissionCache(aiAgentPermission.getTargetId());
        }
        return result;
    }

    /**
     * 修改AI智能体权限
     *
     * @param aiAgentPermission AI智能体权限
     * @return 结果
     */
    @Override
    @Transactional
    public int updateAiAgentPermission(AiAgentPermission aiAgentPermission)
    {
        aiAgentPermission.setUpdateBy(SecurityUtils.getUsername());
        int result = aiAgentPermissionMapper.updateAiAgentPermission(aiAgentPermission);
        if (result > 0)
        {
            // 清除相关缓存
            clearPermissionCache(aiAgentPermission.getTargetId());
        }
        return result;
    }

    /**
     * 批量删除AI智能体权限
     *
     * @param ids 需要删除的AI智能体权限主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteAiAgentPermissionByIds(Long[] ids)
    {
        int result = aiAgentPermissionMapper.deleteAiAgentPermissionByIds(ids);
        if (result > 0)
        {
            // 清除所有权限缓存
            clearPermissionCache(null);
        }
        return result;
    }

    /**
     * 删除AI智能体权限信息
     *
     * @param id AI智能体权限主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteAiAgentPermissionById(Long id)
    {
        int result = aiAgentPermissionMapper.deleteAiAgentPermissionById(id);
        if (result > 0)
        {
            // 清除所有权限缓存
            clearPermissionCache(null);
        }
        return result;
    }

    /**
     * 设置用户权限
     *
     * @param agentId 智能体ID
     * @param userId 用户ID
     * @param permissionAction 权限动作（0-禁止，1-允许）
     * @return 结果
     */
    @Override
    @Transactional
    public int setUserPermission(Long agentId, String userId, Integer permissionAction)
    {
        int result = aiAgentPermissionMapper.setUserPermission(agentId, userId, permissionAction, SecurityUtils.getUsername());
        if (result > 0)
        {
            // 清除用户权限缓存
            clearPermissionCache(userId);
        }
        return result;
    }

    /**
     * 设置角色权限
     *
     * @param agentId 智能体ID
     * @param roleId 角色ID
     * @param permissionAction 权限动作（0-禁止，1-允许）
     * @return 结果
     */
    @Override
    @Transactional
    public int setRolePermission(Long agentId, String roleId, Integer permissionAction)
    {
        int result = aiAgentPermissionMapper.setRolePermission(agentId, roleId, permissionAction, SecurityUtils.getUsername());
        if (result > 0)
        {
            // 清除角色权限缓存
            String cacheKey = "ai:permission:role:" + roleId;
            redisCache.deleteObject(cacheKey);
            
            // 清除所有用户权限缓存（因为角色权限变更会影响用户权限）
            clearPermissionCache(null);
        }
        return result;
    }

    /**
     * 批量设置权限
     *
     * @param permissions 权限列表
     * @return 结果
     */
    @Override
    @Transactional
    public int batchSetPermissions(List<AiAgentPermission> permissions)
    {
        if (permissions == null || permissions.isEmpty())
        {
            return 0;
        }

        // 设置创建者
        String username = SecurityUtils.getUsername();
        for (AiAgentPermission permission : permissions)
        {
            permission.setCreateBy(username);
        }

        int result = aiAgentPermissionMapper.batchInsertPermissions(permissions);
        if (result > 0)
        {
            // 清除所有权限缓存
            clearPermissionCache(null);
        }
        return result;
    }

    /**
     * 删除智能体的所有权限
     *
     * @param agentId 智能体ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePermissionByAgentId(Long agentId)
    {
        int result = aiAgentPermissionMapper.deletePermissionByAgentId(agentId);
        if (result > 0)
        {
            // 清除所有权限缓存
            clearPermissionCache(null);
        }
        return result;
    }

    /**
     * 刷新权限缓存
     *
     * @param userId 用户ID，为空则刷新所有用户缓存
     */
    @Override
    public void refreshPermissionCache(String userId)
    {
        if (StringUtils.isNotEmpty(userId))
        {
            // 清除特定用户的权限缓存
            clearPermissionCache(userId);
            
            // 预热缓存
            selectUserAllPermissions(userId);
        }
        else
        {
            // 清除所有权限缓存
            clearPermissionCache(null);
        }
    }

    /**
     * 清除权限缓存
     *
     * @param userId 用户ID，为空则清除所有用户缓存
     */
    @Override
    public void clearPermissionCache(String userId)
    {
        if (StringUtils.isNotEmpty(userId))
        {
            // 清除特定用户的权限缓存
            redisCache.deleteObject("ai:permission:user:" + userId);
            redisCache.deleteObject("ai:permission:user:" + userId + ":all");
            // 清除用户可用AI列表缓存
            redisCache.deleteObject("ai:available:user:" + userId);
        }
        else
        {
            // 清除所有权限缓存
            Collection<String> userKeys = redisCache.keys("ai:permission:user:*");
            if (!userKeys.isEmpty()) {
                redisCache.deleteObject(userKeys);
            }
            
            Collection<String> roleKeys = redisCache.keys("ai:permission:role:*");
            if (!roleKeys.isEmpty()) {
                redisCache.deleteObject(roleKeys);
            }
            
            // 清除所有用户可用AI列表缓存
            Collection<String> availableKeys = redisCache.keys("ai:available:user:*");
            if (!availableKeys.isEmpty()) {
                redisCache.deleteObject(availableKeys);
            }
        }
    }
}
