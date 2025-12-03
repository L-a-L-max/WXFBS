package com.cube.system.service;

import java.util.List;
import com.cube.system.domain.YuanqiAgentConfig;

/**
 * 腾讯元器智能体配置Service接口
 *
 * @author cube
 * @date 2025-11-26
 */
public interface IYuanqiAgentConfigService
{
    /**
     * 查询腾讯元器智能体配置
     *
     * @param id 腾讯元器智能体配置ID
     * @return 腾讯元器智能体配置
     */
    public YuanqiAgentConfig selectYuanqiAgentConfigById(Long id);

    /**
     * 查询腾讯元器智能体配置列表
     *
     * @param yuanqiAgentConfig 腾讯元器智能体配置
     * @return 腾讯元器智能体配置集合
     */
    public List<YuanqiAgentConfig> selectYuanqiAgentConfigList(YuanqiAgentConfig yuanqiAgentConfig);

    /**
     * 根据用户ID查询启用的配置
     *
     * @param userId 用户ID
     * @return 腾讯元器智能体配置
     */
    public YuanqiAgentConfig selectActiveConfigByUserId(Long userId);

    /**
     * 新增腾讯元器智能体配置
     *
     * @param yuanqiAgentConfig 腾讯元器智能体配置
     * @return 结果
     */
    public int insertYuanqiAgentConfig(YuanqiAgentConfig yuanqiAgentConfig);

    /**
     * 修改腾讯元器智能体配置
     *
     * @param yuanqiAgentConfig 腾讯元器智能体配置
     * @return 结果
     */
    public int updateYuanqiAgentConfig(YuanqiAgentConfig yuanqiAgentConfig);

    /**
     * 批量删除腾讯元器智能体配置
     *
     * @param ids 需要删除的腾讯元器智能体配置ID
     * @return 结果
     */
    public int deleteYuanqiAgentConfigByIds(Long[] ids);

    /**
     * 删除腾讯元器智能体配置信息
     *
     * @param id 腾讯元器智能体配置ID
     * @return 结果
     */
    public int deleteYuanqiAgentConfigById(Long id);
}
