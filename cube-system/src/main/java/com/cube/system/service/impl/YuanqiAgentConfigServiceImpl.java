package com.cube.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cube.system.domain.YuanqiAgentConfig;
import com.cube.system.mapper.YuanqiAgentConfigMapper;
import com.cube.system.service.IYuanqiAgentConfigService;

/**
 * 腾讯元器智能体配置Service业务层处理
 *
 * @author cube
 * @date 2025-11-26
 */
@Service
public class YuanqiAgentConfigServiceImpl implements IYuanqiAgentConfigService
{
    @Autowired
    private YuanqiAgentConfigMapper yuanqiAgentConfigMapper;

    /**
     * 查询腾讯元器智能体配置
     *
     * @param id 腾讯元器智能体配置ID
     * @return 腾讯元器智能体配置
     */
    @Override
    public YuanqiAgentConfig selectYuanqiAgentConfigById(Long id)
    {
        return yuanqiAgentConfigMapper.selectYuanqiAgentConfigById(id);
    }

    /**
     * 查询腾讯元器智能体配置列表
     *
     * @param yuanqiAgentConfig 腾讯元器智能体配置
     * @return 腾讯元器智能体配置
     */
    @Override
    public List<YuanqiAgentConfig> selectYuanqiAgentConfigList(YuanqiAgentConfig yuanqiAgentConfig)
    {
        return yuanqiAgentConfigMapper.selectYuanqiAgentConfigList(yuanqiAgentConfig);
    }

    /**
     * 根据用户ID查询启用的配置
     *
     * @param userId 用户ID
     * @return 腾讯元器智能体配置
     */
    @Override
    public YuanqiAgentConfig selectActiveConfigByUserId(Long userId)
    {
        return yuanqiAgentConfigMapper.selectActiveConfigByUserId(userId);
    }

    /**
     * 新增腾讯元器智能体配置
     *
     * @param yuanqiAgentConfig 腾讯元器智能体配置
     * @return 结果
     */
    @Override
    public int insertYuanqiAgentConfig(YuanqiAgentConfig yuanqiAgentConfig)
    {
        return yuanqiAgentConfigMapper.insertYuanqiAgentConfig(yuanqiAgentConfig);
    }

    /**
     * 修改腾讯元器智能体配置
     *
     * @param yuanqiAgentConfig 腾讯元器智能体配置
     * @return 结果
     */
    @Override
    public int updateYuanqiAgentConfig(YuanqiAgentConfig yuanqiAgentConfig)
    {
        return yuanqiAgentConfigMapper.updateYuanqiAgentConfig(yuanqiAgentConfig);
    }

    /**
     * 批量删除腾讯元器智能体配置
     *
     * @param ids 需要删除的腾讯元器智能体配置ID
     * @return 结果
     */
    @Override
    public int deleteYuanqiAgentConfigByIds(Long[] ids)
    {
        return yuanqiAgentConfigMapper.deleteYuanqiAgentConfigByIds(ids);
    }

    /**
     * 删除腾讯元器智能体配置信息
     *
     * @param id 腾讯元器智能体配置ID
     * @return 结果
     */
    @Override
    public int deleteYuanqiAgentConfigById(Long id)
    {
        return yuanqiAgentConfigMapper.deleteYuanqiAgentConfigById(id);
    }
}
