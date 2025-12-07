package com.playwright.config;

import com.playwright.service.AiResultPersistenceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AI执行配置类
 * 启用AOP和相关服务
 * 
 * @author 优立方
 * @date 2025-01-21
 */
@Configuration
@EnableAspectJAutoProxy
public class AiExecutionConfig {
    
    /**
     * AI类型注册表Bean
     */
    @Bean
    public AITypeRegistry aiTypeRegistry() {
        return new AITypeRegistry();
    }
    
    /**
     * 确保UserLogUtil是Spring Bean
     */
    @Bean
    public com.playwright.utils.common.UserLogUtil userLogUtil() {
        return new com.playwright.utils.common.UserLogUtil();
    }
}
