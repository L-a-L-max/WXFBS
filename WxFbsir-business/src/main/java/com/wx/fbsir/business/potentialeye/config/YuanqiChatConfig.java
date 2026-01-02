package com.wx.fbsir.business.potentialeye.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 元器AI对话配置
 * 如果corp-id或agent-id未配置，将自动使用企业微信的配置
 */
@Configuration
@ConfigurationProperties(prefix = "yuanqi")
public class YuanqiChatConfig {

    @Autowired
    private WeWorkConfig weWorkConfig;

    private String token;
    private String encodingAesKey;
    private String targetUrl;
    private String corpId;
    private String agentId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    /**
     * 获取企业ID，如果未配置则使用企业微信的配置
     */
    public String getCorpId() {
        if (corpId == null || corpId.trim().isEmpty()) {
            return weWorkConfig.getCorpId();
        }
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    /**
     * 获取应用ID，如果未配置则使用企业微信的配置
     */
    public String getAgentId() {
        if (agentId == null || agentId.trim().isEmpty()) {
            return weWorkConfig.getAgentId();
        }
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
