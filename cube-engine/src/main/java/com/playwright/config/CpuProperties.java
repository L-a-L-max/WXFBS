package com.playwright.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CPU配置属性
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "cpu")
public class CpuProperties {

    /**
     * CPU核心数，默认使用系统可用核心数
     */
    private Integer cores = Runtime.getRuntime().availableProcessors();

    /**
     * 最大线程数，默认使用系统可用处理器数*2
     */
    private Integer maxThreads = Runtime.getRuntime().availableProcessors() * 2;

}