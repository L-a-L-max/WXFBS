package com.cube.system.service.impl;

import com.cube.common.constant.CacheConstants;
import com.cube.common.core.domain.entity.AiAgent;
import com.cube.common.core.domain.entity.SysRole;
import com.cube.common.core.redis.RedisCache;
import com.cube.common.utils.SecurityUtils;
import com.cube.common.utils.StringUtils;
import com.cube.system.mapper.AiAgentMapper;
import com.cube.system.mapper.AiAgentPermissionMapper;
import com.cube.system.mapper.SysRoleMapper;
import com.cube.system.service.IAiAgentService;
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
 * AI智能体Service业务层处理
 *
 * @author cube
 * @date 2025-01-14
 */
@Service
public class AiAgentServiceImpl implements IAiAgentService
{
    private static final Logger log = LoggerFactory.getLogger(AiAgentServiceImpl.class);

    @Autowired
    private AiAgentMapper aiAgentMapper;

    @Autowired
    private AiAgentPermissionMapper aiAgentPermissionMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询AI智能体
     *
     * @param id AI智能体主键
     * @return AI智能体
     */
    @Override
    public AiAgent selectAiAgentById(Long id)
    {
        return aiAgentMapper.selectAiAgentById(id);
    }

    /**
     * 根据智能体代码查询AI智能体
     *
     * @param agentCode 智能体代码
     * @return AI智能体
     */
    @Override
    public AiAgent selectAiAgentByCode(String agentCode)
    {
        // 开发阶段暂时禁用缓存
        // String cacheKey = "ai:agent:" + agentCode;
        // AiAgent cachedAgent = redisCache.getCacheObject(cacheKey);
        // if (cachedAgent != null)
        // {
        //     return cachedAgent;
        // }

        // 从数据库查询
        AiAgent agent = aiAgentMapper.selectAiAgentByCode(agentCode);
        // 开发阶段暂时禁用缓存
        // if (agent != null)
        // {
        //     redisCache.setCacheObject(cacheKey, agent, 30, TimeUnit.MINUTES);
        // }
        return agent;
    }

    /**
     * 查询AI智能体列表
     *
     * @param aiAgent AI智能体
     * @return AI智能体
     */
    @Override
    public List<AiAgent> selectAiAgentList(AiAgent aiAgent)
    {
        return aiAgentMapper.selectAiAgentList(aiAgent);
    }

    /**
     * 查询用户可用的AI智能体列表
     *
     * @param userId 用户ID
     * @return AI智能体集合
     */
    @Override
    public List<AiAgent> selectUserAvailableAgents(String userId)
    {
        // 启用Redis缓存，提升查询性能
        String cacheKey = "ai:available:user:" + userId;
        List<AiAgent> cachedAgents = redisCache.getCacheObject(cacheKey);
        if (cachedAgents != null)
        {
            return cachedAgents;
        }

        // 获取用户角色
        List<SysRole> roles = roleMapper.selectRolePermissionByUserId(Long.valueOf(userId));
        List<String> roleIds = roles.stream().map(role -> String.valueOf(role.getRoleId())).collect(Collectors.toList());

        // 查询用户可用的AI
        List<AiAgent> agents = aiAgentMapper.selectUserAvailableAgents(userId, roleIds);

        // 缓存1小时
        redisCache.setCacheObject(cacheKey, agents, 1, TimeUnit.HOURS);
        return agents;
    }

    /**
     * 查询所有上架的AI智能体列表（按排序顺序）
     *
     * @return AI智能体集合
     */
    @Override
    public List<AiAgent> selectActiveAgentList()
    {
        // 开发阶段暂时禁用缓存
        // String cacheKey = "ai:agent:list";
        // List<AiAgent> cachedAgents = redisCache.getCacheObject(cacheKey);
        // if (cachedAgents != null)
        // {
        //     return cachedAgents;
        // }

        // 从数据库查询
        List<AiAgent> agents = aiAgentMapper.selectActiveAgentList();

        // 开发阶段暂时禁用缓存
        // redisCache.setCacheObject(cacheKey, agents, 30, TimeUnit.MINUTES);
        return agents;
    }

    /**
     * 新增AI智能体
     *
     * @param aiAgent AI智能体
     * @return 结果
     */
    @Override
    @Transactional
    public int insertAiAgent(AiAgent aiAgent)
    {
        aiAgent.setCreateBy(SecurityUtils.getUsername());
        int result = aiAgentMapper.insertAiAgent(aiAgent);
        if (result > 0)
        {
            // 清除相关缓存
            clearCache(null);
        }
        return result;
    }

    /**
     * 修改AI智能体
     *
     * @param aiAgent AI智能体
     * @return 结果
     */
    @Override
    @Transactional
    public int updateAiAgent(AiAgent aiAgent)
    {
        aiAgent.setUpdateBy(SecurityUtils.getUsername());
        int result = aiAgentMapper.updateAiAgent(aiAgent);
        if (result > 0)
        {
            // 清除相关缓存
            clearCache(aiAgent.getAgentCode());
        }
        return result;
    }

    /**
     * 批量删除AI智能体
     *
     * @param ids 需要删除的AI智能体主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteAiAgentByIds(Long[] ids)
    {
        // 先删除相关权限
        for (Long id : ids)
        {
            aiAgentPermissionMapper.deletePermissionByAgentId(id);
        }
        
        int result = aiAgentMapper.deleteAiAgentByIds(ids);
        if (result > 0)
        {
            // 清除所有缓存
            clearCache(null);
        }
        return result;
    }

    /**
     * 删除AI智能体信息
     *
     * @param id AI智能体主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteAiAgentById(Long id)
    {
        // 先删除相关权限
        aiAgentPermissionMapper.deletePermissionByAgentId(id);
        
        int result = aiAgentMapper.deleteAiAgentById(id);
        if (result > 0)
        {
            // 清除所有缓存
            clearCache(null);
        }
        return result;
    }

    /**
     * 校验智能体代码是否唯一
     *
     * @param aiAgent AI智能体信息
     * @return 结果
     */
    @Override
    public boolean checkAgentCodeUnique(AiAgent aiAgent)
    {
        Long agentId = StringUtils.isNull(aiAgent.getId()) ? -1L : aiAgent.getId();
        AiAgent info = aiAgentMapper.checkAgentCodeUnique(aiAgent.getAgentCode());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != agentId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 更新AI在线状态
     *
     * @param agentCode 智能体代码
     * @param onlineStatus 在线状态
     * @return 结果
     */
    @Override
    public int updateOnlineStatus(String agentCode, Integer onlineStatus)
    {
        int result = aiAgentMapper.updateOnlineStatus(agentCode, onlineStatus);
        if (result > 0)
        {
            // 开发阶段暂时禁用缓存
            // String cacheKey = "ai:status:online:" + agentCode;
            // redisCache.setCacheObject(cacheKey, onlineStatus, 1, TimeUnit.HOURS);
            
            // 清除相关缓存
            clearCache(agentCode);
        }
        return result;
    }

    /**
     * 批量更新AI在线状态
     *
     * @param agentCodes 智能体代码列表
     * @param onlineStatus 在线状态
     * @return 结果
     */
    @Override
    public int batchUpdateOnlineStatus(List<String> agentCodes, Integer onlineStatus)
    {
        int result = aiAgentMapper.batchUpdateOnlineStatus(agentCodes, onlineStatus);
        if (result > 0)
        {
            // 开发阶段暂时禁用缓存
            // for (String agentCode : agentCodes)
            // {
            //     String cacheKey = "ai:status:online:" + agentCode;
            //     redisCache.setCacheObject(cacheKey, onlineStatus, 1, TimeUnit.HOURS);
            // }
            
            // 清除相关缓存
            clearCache(null);
        }
        return result;
    }

    /**
     * 检查用户是否有权限使用某个AI
     * 
     * 权限检查优先级：
     * 1. AI状态检查：AI必须上架（agent_status=1）
     * 2. 用户级别权限检查：如果有用户级权限记录，则按用户权限决定
     * 3. 角色级别权限检查：如果有角色级权限记录，则按角色权限决定
     * 4. 全局可用性：如果没有明确权限记录，则按全局可用性决定
     * 5. 默认拒绝：如果不是全局可用且没有明确权限，则拒绝
     *
     * @param userId 用户ID
     * @param agentCode 智能体代码
     * @return 是否有权限
     */
    @Override
    public boolean checkUserPermission(String userId, String agentCode)
    {
        // 1. 获取AI信息并检查AI状态
        AiAgent agent = selectAiAgentByCode(agentCode);
        if (agent == null || agent.getAgentStatus() == 0)
        {
            // AI不存在或已下架，拒绝访问
            return false;
        }

        // 2. 检查用户级别权限（优先级最高）
        Integer userPermission = aiAgentPermissionMapper.selectUserPermission(agent.getId(), userId);
        if (userPermission != null)
        {
            // 有明确的用户级权限记录，按用户权限决定
            return userPermission == 1;
        }

        // 3. 检查角色级别权限（优先级次高）
        List<SysRole> roles = roleMapper.selectRolePermissionByUserId(Long.valueOf(userId));
        List<String> roleIds = roles.stream().map(role -> String.valueOf(role.getRoleId())).collect(Collectors.toList());
        
        if (!roleIds.isEmpty())
        {
            Integer rolePermission = aiAgentPermissionMapper.selectRolePermission(agent.getId(), roleIds);
            if (rolePermission != null)
            {
                // 有明确的角色级权限记录，按角色权限决定
                return rolePermission == 1;
            }
        }

        // 4. 检查全局可用性（如果没有明确权限记录）
        // 如果是全局可用，则允许；否则拒绝
        return agent.getIsGlobal() == 1;
    }

    /**
     * 刷新AI智能体缓存
     */
    @Override
    public void refreshCache()
    {
        // 清除所有缓存
        clearCache(null);
        
        // 预热缓存
        selectActiveAgentList();
    }

    /**
     * 清除AI智能体缓存
     *
     * @param agentCode 智能体代码，为空则清除所有缓存
     */
    @Override
    public void clearCache(String agentCode)
    {
        if (StringUtils.isNotEmpty(agentCode))
        {
            // 清除特定AI的缓存
            redisCache.deleteObject("ai:agent:" + agentCode);
            redisCache.deleteObject("ai:status:online:" + agentCode);
        }
        else
        {
            // 清除所有AI相关缓存
            log.info("开始清除所有AI相关缓存...");
            
            // 1. 清除AI智能体列表缓存
            redisCache.deleteObject("ai:agent:list");
            
            // 2. 清除所有用户权限缓存
            Collection<String> userPermKeys = redisCache.keys("ai:permission:user:*");
            if (!userPermKeys.isEmpty()) {
                redisCache.deleteObject(userPermKeys);
                log.info("清除用户权限缓存: {} 个", userPermKeys.size());
            }
            
            // 3. 清除所有角色权限缓存
            Collection<String> roleKeys = redisCache.keys("ai:permission:role:*");
            if (!roleKeys.isEmpty()) {
                redisCache.deleteObject(roleKeys);
                log.info("清除角色权限缓存: {} 个", roleKeys.size());
            }
            
            // 4. 清除所有用户可用AI列表缓存
            Collection<String> availableKeys = redisCache.keys("ai:available:user:*");
            if (!availableKeys.isEmpty()) {
                redisCache.deleteObject(availableKeys);
                log.info("清除用户可用AI列表缓存: {} 个", availableKeys.size());
            }
            
            // 5. 清除所有AI智能体详情缓存
            Collection<String> agentKeys = redisCache.keys("ai:agent:*");
            if (!agentKeys.isEmpty()) {
                redisCache.deleteObject(agentKeys);
                log.info("清除AI智能体详情缓存: {} 个", agentKeys.size());
            }
            
            // 6. 清除所有AI在线状态缓存
            Collection<String> statusKeys = redisCache.keys("ai:status:online:*");
            if (!statusKeys.isEmpty()) {
                redisCache.deleteObject(statusKeys);
                log.info("清除AI在线状态缓存: {} 个", statusKeys.size());
            }
            
            log.info("所有AI相关缓存清除完成");
        }
    }
}
