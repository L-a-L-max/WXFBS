package com.wx.fbsir.business.dailyassistant.mapper;

import java.util.List;
import com.wx.fbsir.business.dailyassistant.domain.YuanqiAgentConfig;

/**
 * 腾讯元器智能体配置Mapper接口
 *
 * @author wxfbsir
 * @date 2025-12-05
 */
public interface YuanqiAgentConfigMapper
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
     * 删除腾讯元器智能体配置
     *
     * @param id 腾讯元器智能体配置ID
     * @return 结果
     */
    public int deleteYuanqiAgentConfigById(Long id);

    /**
     * 批量删除腾讯元器智能体配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteYuanqiAgentConfigByIds(Long[] ids);
}
