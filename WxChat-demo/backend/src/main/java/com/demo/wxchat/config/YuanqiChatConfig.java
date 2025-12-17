package com.demo.wxchat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 元器AI对话配置
 */
@Configuration
@ConfigurationProperties(prefix = "yuanqi.chat")
public class YuanqiChatConfig {

    /** 消息验证Token */
    private String token;

    /** AES加密密钥（43位） */
    private String encodingAesKey;

    /** 企业ID */
    private String corpId;

    /** 应用AgentId */
    private String agentId;

    /** 元器API目标地址 */
    private String targetUrl;

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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
