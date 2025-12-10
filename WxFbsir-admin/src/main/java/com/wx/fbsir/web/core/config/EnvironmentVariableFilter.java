package com.wx.fbsir.web.core.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 环境变量过滤器
 * 只允许读取指定前缀的环境变量，阻止其他环境变量被Spring Boot读取
 * 
 * @author WxFbsir
 * @date 2025-12-10
 */
public class EnvironmentVariableFilter implements EnvironmentPostProcessor, Ordered {
    
    // 允许的环境变量前缀
    private static final String ALLOWED_PREFIX = "WXFBSIR_";
    
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 获取所有的属性源
        MutablePropertySources propertySources = environment.getPropertySources();
        
        // 查找系统环境变量属性源
        PropertySource<?> systemEnvironment = propertySources.get("systemEnvironment");
        
        if (systemEnvironment != null) {
            // 创建过滤后的环境变量Map
            Map<String, Object> filteredEnv = new HashMap<>();
            
            // 获取所有环境变量
            Map<String, Object> originalEnv = (Map<String, Object>) systemEnvironment.getSource();
            
            // 只保留WXFBSIR_前缀的环境变量
            for (Map.Entry<String, Object> entry : originalEnv.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith(ALLOWED_PREFIX)) {
                    filteredEnv.put(key, entry.getValue());
                }
            }
            
            // 替换原有的系统环境变量属性源
            propertySources.replace("systemEnvironment", 
                new MapPropertySource("systemEnvironment", filteredEnv));
        }
    }
    
    @Override
    public int getOrder() {
        // 设置为最高优先级，确保在其他处理器之前执行
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
