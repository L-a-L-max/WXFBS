package com.wx.fbsir.engine.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * 
 * 统一管理线程池，避免线程泄漏
 * 支持优雅关闭，确保任务执行完成
 *
 * @author wxfbsir
 * @date 2025-12-15
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);

    private ThreadPoolTaskExecutor messageExecutor;
    private ThreadPoolTaskScheduler taskScheduler;

    /**
     * 消息处理线程池
     * 
     * 用于处理 WebSocket 接收到的消息
     */
    @Bean("messageExecutor")
    public ThreadPoolTaskExecutor messageExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        int processors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors * 2);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(300);
        executor.setThreadNamePrefix("msg-handler-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        
        this.messageExecutor = executor;
        log.info("[线程池] 消息处理线程池初始化完成 - 核心线程: {}, 最大线程: {}", 
            processors, processors * 2);
        
        return executor;
    }

    /**
     * 定时任务调度器
     * 
     * 用于心跳检测、重连调度等
     */
    @Bean("taskScheduler")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("scheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(30);
        scheduler.setErrorHandler(throwable -> 
            log.error("[定时任务] 执行异常 - 错误: {}", throwable.getMessage())
        );
        scheduler.initialize();
        
        this.taskScheduler = scheduler;
        log.info("[线程池] 定时任务调度器初始化完成");
        
        return scheduler;
    }

    /**
     * 优雅关闭线程池
     */
    @PreDestroy
    public void shutdown() {
        if (messageExecutor != null) {
            messageExecutor.shutdown();
        }
        if (taskScheduler != null) {
            taskScheduler.shutdown();
        }
    }
}
