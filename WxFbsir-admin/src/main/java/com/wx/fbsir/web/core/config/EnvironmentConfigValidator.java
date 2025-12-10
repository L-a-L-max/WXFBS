package com.wx.fbsir.web.core.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * 环境变量配置验证器
 * 在应用启动前验证环境变量配置的完整性
 * 
 * @author WxFbsir
 * @date 2025-12-10
 */
public class EnvironmentConfigValidator implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
    
    // 环境变量前缀
    private static final String ENV_PREFIX = "WXFBSIR_";
    
    // 必需的环境变量列表
    private static final String[] REQUIRED_ENV_VARS = {
        "WXFBSIR_MYSQL_URL",
        "WXFBSIR_MYSQL_USERNAME", 
        "WXFBSIR_MYSQL_PASSWORD",
        "WXFBSIR_REDIS_HOST",
        "WXFBSIR_REDIS_PORT",
        "WXFBSIR_REDIS_DATABASE",
        "WXFBSIR_AES_SECRET_KEY",
        "WXFBSIR_TOKEN_SECRET",
        "WXFBSIR_DOMAIN",
        "WXFBSIR_FILE_PATH",
        "WXFBSIR_DRUID_USERNAME",
        "WXFBSIR_DRUID_PASSWORD"
    };
    
    // 可选的环境变量
    private static final String[] OPTIONAL_ENV_VARS = {
        "WXFBSIR_REDIS_PASSWORD"
    };
    
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        try {
            validateEnvironmentConfig();
        } catch (IllegalStateException e) {
            System.err.println("\n" +
                "=======================================================\n" +
                "  环境变量配置检测失败，应用启动终止\n" +
                "=======================================================\n" +
                e.getMessage() + "\n" +
                "=======================================================");
            System.exit(1);
        }
    }
    
    /**
     * 验证环境变量配置
     */
    private void validateEnvironmentConfig() {
        List<String> existingVars = new ArrayList<>();
        List<String> missingVars = new ArrayList<>();
        
        // 检查所有必需的环境变量
        for (String varName : REQUIRED_ENV_VARS) {
            String value = System.getenv(varName);
            if (value != null && !value.trim().isEmpty()) {
                existingVars.add(varName);
            } else {
                missingVars.add(varName);
            }
        }
        
        // 判断配置模式
        if (existingVars.isEmpty()) {
            // 完全使用配置文件
            logUsingDefaultConfig();
        } else if (!missingVars.isEmpty()) {
            // 环境变量不完整，启动失败
            throwIncompleteConfigError(existingVars, missingVars);
        } else {
            // 完全使用环境变量
            logUsingEnvironmentConfig();
        }
    }
    
    /**
     * 输出使用默认配置的日志
     */
    private void logUsingDefaultConfig() {
        System.out.println("\n" +
            "=======================================================\n" +
            "  配置模式: 使用配置文件默认值\n" +
            "  未检测到环境变量配置，将使用 application.yml 中的默认配置\n" +
            "  生产环境建议使用环境变量配置\n" +
            "=======================================================");
    }
    
    /**
     * 输出使用环境变量配置的日志
     */
    private void logUsingEnvironmentConfig() {
        System.out.println("\n" +
            "=======================================================\n" +
            "  配置模式: 使用环境变量配置\n" +
            "  已成功加载所有必需的环境变量\n" +
            "=======================================================");
    }
    
    /**
     * 抛出配置不完整错误
     */
    private void throwIncompleteConfigError(List<String> existingVars, List<String> missingVars) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("\n环境变量配置不完整！\n\n");
        errorMsg.append("已配置的环境变量:\n");
        for (String var : existingVars) {
            errorMsg.append("  ✓ ").append(var).append("\n");
        }
        errorMsg.append("\n缺少的环境变量:\n");
        for (String var : missingVars) {
            errorMsg.append("  ✗ ").append(var).append("\n");
        }
        errorMsg.append("\n请完整配置所有环境变量，或删除现有环境变量使用配置文件默认值。\n");
        errorMsg.append("环境变量配置示例请查看项目文档。\n");
        
        throw new IllegalStateException(errorMsg.toString());
    }
    
    @Override
    public int getOrder() {
        // 设置最高优先级，确保在其他配置加载前执行
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
