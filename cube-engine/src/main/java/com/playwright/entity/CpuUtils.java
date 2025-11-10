package com.playwright.entity;

import com.playwright.config.CpuProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CPU工具类
 */
@Component
public class CpuUtils {

    @Autowired
    private CpuProperties cpuProperties;

    /**
     * 获取配置的CPU核心数
     */
    public int getConfiguredCores() {
        return cpuProperties.getCores();
    }

    /**
     * 获取配置的最大线程数
     */
    public int getConfiguredMaxThreads() {
        return cpuProperties.getMaxThreads();
    }

}
