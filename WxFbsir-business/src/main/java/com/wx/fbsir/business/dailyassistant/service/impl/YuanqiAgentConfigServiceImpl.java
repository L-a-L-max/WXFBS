package com.wx.fbsir.business.dailyassistant.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.wx.fbsir.business.dailyassistant.domain.YuanqiAgentConfig;
import com.wx.fbsir.business.dailyassistant.mapper.YuanqiAgentConfigMapper;
import com.wx.fbsir.business.dailyassistant.service.IYuanqiAgentConfigService;
import com.wx.fbsir.common.utils.AesEncryptUtils;

/**
 * 腾讯元器智能体配置Service业务层处理
 *
 * @author wxfbsir
 * @date 2025-12-05
 */
@Service
public class YuanqiAgentConfigServiceImpl implements IYuanqiAgentConfigService
{
    private static final Logger log = LoggerFactory.getLogger(YuanqiAgentConfigServiceImpl.class);
    
    @Autowired
    private YuanqiAgentConfigMapper yuanqiAgentConfigMapper;
    
    // 脱敏标记，表示字段已加密存储
    private static final String MASKED_VALUE = "***已加密***";

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
     * 根据用户ID查询启用的配置（返回脱敏数据）
     *
     * @param userId 用户ID
     * @return 腾讯元器智能体配置（敏感字段已脱敏）
     */
    @Override
    public YuanqiAgentConfig selectActiveConfigByUserId(Long userId)
    {
        YuanqiAgentConfig config = yuanqiAgentConfigMapper.selectActiveConfigByUserId(userId);
        if (config != null) {
            // 脱敏处理：不返回真实的agentId和apiKey
            if (StringUtils.hasText(config.getAgentId())) {
                config.setAgentId(MASKED_VALUE);
            }
            if (StringUtils.hasText(config.getApiKey())) {
                config.setApiKey(MASKED_VALUE);
            }
        }
        return config;
    }

    /**
     * 根据用户ID查询启用的配置（返回解密数据，仅供内部业务逻辑使用）
     * 注意：此方法返回真实的敏感信息，不应暴露给前端
     *
     * @param userId 用户ID
     * @return 腾讯元器智能体配置（敏感字段已解密）
     */
    @Override
    public YuanqiAgentConfig selectActiveConfigByUserIdDecrypted(Long userId)
    {
        YuanqiAgentConfig config = yuanqiAgentConfigMapper.selectActiveConfigByUserId(userId);
        if (config != null) {
            // 解密敏感字段
            return decryptSensitiveFields(config);
        }
        return null;
    }

    /**
     * 新增腾讯元器智能体配置（加密敏感信息）
     *
     * @param yuanqiAgentConfig 腾讯元器智能体配置
     * @return 结果
     */
    @Override
    public int insertYuanqiAgentConfig(YuanqiAgentConfig yuanqiAgentConfig)
    {
        // 加密敏感信息
        encryptSensitiveFields(yuanqiAgentConfig);
        return yuanqiAgentConfigMapper.insertYuanqiAgentConfig(yuanqiAgentConfig);
    }

    /**
     * 修改腾讯元器智能体配置（加密敏感信息）
     *
     * @param yuanqiAgentConfig 腾讯元器智能体配置
     * @return 结果
     */
    @Override
    public int updateYuanqiAgentConfig(YuanqiAgentConfig yuanqiAgentConfig)
    {
        // 如果前端传入的是脱敏标记，说明用户没有修改该字段，需要保留原值
        if (MASKED_VALUE.equals(yuanqiAgentConfig.getAgentId()) || 
            MASKED_VALUE.equals(yuanqiAgentConfig.getApiKey())) {
            // 查询原有配置
            YuanqiAgentConfig oldConfig = yuanqiAgentConfigMapper.selectYuanqiAgentConfigById(yuanqiAgentConfig.getId());
            if (oldConfig != null) {
                // 保留原有的加密值
                if (MASKED_VALUE.equals(yuanqiAgentConfig.getAgentId())) {
                    yuanqiAgentConfig.setAgentId(oldConfig.getAgentId());
                }
                if (MASKED_VALUE.equals(yuanqiAgentConfig.getApiKey())) {
                    yuanqiAgentConfig.setApiKey(oldConfig.getApiKey());
                }
            }
        } else {
            // 用户修改了敏感字段，需要加密新值
            encryptSensitiveFields(yuanqiAgentConfig);
        }
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
    
    /**
     * 加密敏感字段（智能体ID和API密钥）
     *
     * @param config 配置对象
     */
    private void encryptSensitiveFields(YuanqiAgentConfig config)
    {
        try {
            // 加密智能体ID
            if (StringUtils.hasText(config.getAgentId()) && !MASKED_VALUE.equals(config.getAgentId())) {
                String encryptedAgentId = AesEncryptUtils.encrypt(config.getAgentId());
                config.setAgentId(encryptedAgentId);
            }
            
            // 加密API密钥
            if (StringUtils.hasText(config.getApiKey()) && !MASKED_VALUE.equals(config.getApiKey())) {
                String encryptedApiKey = AesEncryptUtils.encrypt(config.getApiKey());
                config.setApiKey(encryptedApiKey);
            }
        } catch (Exception e) {
            log.error("[智能体配置] 加密失败");
            throw new RuntimeException("配置加密失败，请检查输入数据");
        }
    }
    
    /**
     * 解密敏感字段（供内部调用使用）
     * 注意：此方法仅供业务逻辑内部使用，不应暴露给前端
     *
     * @param config 配置对象
     * @return 解密后的配置对象
     */
    public YuanqiAgentConfig decryptSensitiveFields(YuanqiAgentConfig config)
    {
        if (config == null) {
            return null;
        }
        
        try {
            // 解密智能体ID
            if (StringUtils.hasText(config.getAgentId())) {
                String decryptedAgentId = AesEncryptUtils.decrypt(config.getAgentId());
                config.setAgentId(decryptedAgentId);
            }
            
            // 解密API密钥
            if (StringUtils.hasText(config.getApiKey())) {
                String decryptedApiKey = AesEncryptUtils.decrypt(config.getApiKey());
                config.setApiKey(decryptedApiKey);
            }
            
            return config;
        } catch (Exception e) {
            log.error("[智能体配置] 解密失败");
            throw new RuntimeException("配置解密失败，可能是历史数据问题，请重新配置");
        }
    }
}
