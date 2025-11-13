package com.playwright.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义服务器配置
 * 用于读取用户预设的服务器配置信息
 */
@Component
@ConfigurationProperties(prefix = "custom.server")
public class CustomServerConfig {
    
    /**
     * 服务器地址 (IP:端口，例如: 192.168.0.1:8081)
     */
    private String address = "";
    
    /**
     * 主机ID (例如: zpy001)
     */
    private String hostId = "";
    
    /**
     * 是否启用SSL
     */
    private boolean sslEnabled = false;
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getHostId() {
        return hostId;
    }
    
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
    
    public boolean isSslEnabled() {
        return sslEnabled;
    }
    
    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }
    
    /**
     * 检查是否已配置完整的服务器信息
     */
    public boolean isConfigured() {
        return address != null && !address.trim().isEmpty() 
            && hostId != null && !hostId.trim().isEmpty();
    }
    
    /**
     * 获取配置摘要信息
     */
    public String getConfigSummary() {
        if (isConfigured()) {
            String protocol = sslEnabled ? "https" : "http";
            return String.format("服务器: %s://%s, 主机ID: %s", protocol, address, hostId);
        }
        return "未配置";
    }
}
