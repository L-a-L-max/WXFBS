package com.cube.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 日志优化配置
 */
@Component
@ConfigurationProperties(prefix = "logging.optimization")
public class LogOptimizationConfig {
    
    /**
     * 是否启用日志优化
     */
    private boolean enabled = true;
    
    /**
     * 是否简化HTTP解析错误
     */
    private boolean simplifyHttpErrors = true;
    
    /**
     * 是否简化连接重置错误
     */
    private boolean simplifyConnectionErrors = true;
    
    /**
     * 是否简化MCP客户端日志
     */
    private boolean simplifyMcpLogs = true;
    
    /**
     * 日志统计间隔（分钟）
     */
    private int statisticsInterval = 5;

    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSimplifyHttpErrors() {
        return simplifyHttpErrors;
    }

    public void setSimplifyHttpErrors(boolean simplifyHttpErrors) {
        this.simplifyHttpErrors = simplifyHttpErrors;
    }

    public boolean isSimplifyConnectionErrors() {
        return simplifyConnectionErrors;
    }

    public void setSimplifyConnectionErrors(boolean simplifyConnectionErrors) {
        this.simplifyConnectionErrors = simplifyConnectionErrors;
    }

    public boolean isSimplifyMcpLogs() {
        return simplifyMcpLogs;
    }

    public void setSimplifyMcpLogs(boolean simplifyMcpLogs) {
        this.simplifyMcpLogs = simplifyMcpLogs;
    }

    public int getStatisticsInterval() {
        return statisticsInterval;
    }

    public void setStatisticsInterval(int statisticsInterval) {
        this.statisticsInterval = statisticsInterval;
    }
}
