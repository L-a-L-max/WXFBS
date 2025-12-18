package com.wx.fbsir.engine.playwright.monitor;

import com.wx.fbsir.engine.playwright.pool.BrowserPoolManager;
import com.wx.fbsir.engine.playwright.util.ClipboardManager;
import com.wx.fbsir.engine.playwright.util.ScreenshotUtil;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Playwright 资源监控器
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 核心职责
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 1. 监控浏览器池资源使用情况
 * 2. 检测锁泄漏（剪贴板锁、截图锁）
 * 3. 监控线程数量，预防僵尸线程堆积
 * 4. 监控内存使用，预防OOM
 * 5. 定期输出资源状态日志
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 解决老项目问题
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * - 僵尸线程堆积：监控线程数量，异常增长时告警
 * - 用户锁堆积：监控锁数量，超过阈值时告警并清理
 * - 内存泄漏：监控内存使用率，超过阈值时告警
 * - 资源未释放：监控会话创建/销毁差异
 * 
 * @author wxfbsir
 * @date 2025-12-18
 */
@Component
public class ResourceMonitor {

    private static final Logger log = LoggerFactory.getLogger(ResourceMonitor.class);

    private final BrowserPoolManager browserPoolManager;
    private final ClipboardManager clipboardManager;
    private final ScreenshotUtil screenshotUtil;

    /**
     * 基线线程数（启动时记录）
     */
    private int baselineThreadCount = 0;

    /**
     * 最大允许的锁数量（超过则视为泄漏）
     */
    private static final int MAX_EXPECTED_LOCKS = 50;

    /**
     * 线程增长告警阈值
     */
    private static final int THREAD_GROWTH_THRESHOLD = 100;

    /**
     * 内存使用率告警阈值
     */
    private static final double MEMORY_USAGE_THRESHOLD = 0.85;

    /**
     * 告警计数器（避免重复告警）
     */
    private final AtomicInteger alertCount = new AtomicInteger(0);

    public ResourceMonitor(BrowserPoolManager browserPoolManager,
                           ClipboardManager clipboardManager,
                           ScreenshotUtil screenshotUtil) {
        this.browserPoolManager = browserPoolManager;
        this.clipboardManager = clipboardManager;
        this.screenshotUtil = screenshotUtil;
        
        // 记录基线线程数
        this.baselineThreadCount = ManagementFactory.getThreadMXBean().getThreadCount();
        log.info("[资源监控] 初始化完成 - 基线线程数: {}", baselineThreadCount);
    }

    /**
     * 定期资源检查（每5分钟）
     */
    @Scheduled(fixedRate = 300000)
    public void checkResources() {
        try {
            // 检查锁泄漏
            checkLockLeak();
            
            // 检查线程泄漏
            checkThreadLeak();
            
            // 检查内存使用
            checkMemoryUsage();
            
            // 检查浏览器池状态
            checkBrowserPool();
            
        } catch (Exception e) {
            log.error("[资源监控] 检查异常 - 错误类型: {}, 错误信息: {}", 
                e.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * 检查锁泄漏
     */
    private void checkLockLeak() {
        int clipboardLocks = clipboardManager.getLockCount();
        int screenshotLocks = screenshotUtil.getLockCount();
        int totalLocks = clipboardLocks + screenshotLocks;
        
        if (totalLocks > MAX_EXPECTED_LOCKS) {
            log.warn("[资源监控] 可能存在锁泄漏 - 剪贴板锁: {}, 截图锁: {}, 总计: {}, 阈值: {}", 
                clipboardLocks, screenshotLocks, totalLocks, MAX_EXPECTED_LOCKS);
            
            // 如果锁数量过多，尝试清理
            if (totalLocks > MAX_EXPECTED_LOCKS * 2) {
                log.warn("[资源监控] 锁数量严重超标，执行强制清理");
                clipboardManager.clearAllLocks();
                screenshotUtil.clearAllLocks();
            }
        } else {
            log.debug("[资源监控] 锁状态正常 - 剪贴板锁: {}, 截图锁: {}", 
                clipboardLocks, screenshotLocks);
        }
    }

    /**
     * 检查线程泄漏
     */
    private void checkThreadLeak() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        int currentThreadCount = threadBean.getThreadCount();
        int peakThreadCount = threadBean.getPeakThreadCount();
        int threadGrowth = currentThreadCount - baselineThreadCount;
        
        if (threadGrowth > THREAD_GROWTH_THRESHOLD) {
            log.warn("[资源监控] 线程数异常增长 - 当前: {}, 基线: {}, 增长: {}, 峰值: {}", 
                currentThreadCount, baselineThreadCount, threadGrowth, peakThreadCount);
            alertCount.incrementAndGet();
        } else {
            log.debug("[资源监控] 线程状态正常 - 当前: {}, 基线: {}, 峰值: {}", 
                currentThreadCount, baselineThreadCount, peakThreadCount);
        }
    }

    /**
     * 检查内存使用
     */
    private void checkMemoryUsage() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        
        long usedMB = heapUsage.getUsed() / (1024 * 1024);
        long maxMB = heapUsage.getMax() / (1024 * 1024);
        double usageRatio = (double) heapUsage.getUsed() / heapUsage.getMax();
        
        if (usageRatio > MEMORY_USAGE_THRESHOLD) {
            log.warn("[资源监控] 内存使用率过高 - 已用: {}MB, 最大: {}MB, 使用率: {:.1%}", 
                usedMB, maxMB, usageRatio);
            alertCount.incrementAndGet();
            
            // 建议执行GC
            if (usageRatio > 0.9) {
                log.warn("[资源监控] 内存使用率超过90%，建议检查是否存在内存泄漏");
            }
        } else {
            log.debug("[资源监控] 内存状态正常 - 已用: {}MB, 最大: {}MB, 使用率: {:.1%}", 
                usedMB, maxMB, usageRatio);
        }
    }

    /**
     * 检查浏览器池状态
     */
    private void checkBrowserPool() {
        Map<String, Object> poolStatus = browserPoolManager.getStatus();
        String leakInfo = browserPoolManager.getResourceLeakInfo();
        
        log.debug("[资源监控] 浏览器池状态 - 活跃: {}, 持久化: {}, 临时: {}, 可用槽位: {}, 资源: {}", 
            poolStatus.get("activeCount"),
            poolStatus.get("persistentCount"),
            poolStatus.get("temporaryCount"),
            poolStatus.get("availableSlots"),
            leakInfo);
    }

    /**
     * 获取资源状态摘要
     */
    public Map<String, Object> getResourceStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // 锁状态
        status.put("clipboardLocks", clipboardManager.getLockCount());
        status.put("screenshotLocks", screenshotUtil.getLockCount());
        
        // 线程状态
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        status.put("currentThreads", threadBean.getThreadCount());
        status.put("baselineThreads", baselineThreadCount);
        status.put("peakThreads", threadBean.getPeakThreadCount());
        
        // 内存状态
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        status.put("heapUsedMB", heapUsage.getUsed() / (1024 * 1024));
        status.put("heapMaxMB", heapUsage.getMax() / (1024 * 1024));
        status.put("heapUsageRatio", (double) heapUsage.getUsed() / heapUsage.getMax());
        
        // 浏览器池状态
        status.putAll(browserPoolManager.getStatus());
        status.put("browserPoolLeakInfo", browserPoolManager.getResourceLeakInfo());
        
        // 告警计数
        status.put("alertCount", alertCount.get());
        
        return status;
    }

    /**
     * 强制清理所有资源锁
     */
    public void forceCleanupLocks() {
        log.info("[资源监控] 执行强制锁清理");
        clipboardManager.clearAllLocks();
        screenshotUtil.clearAllLocks();
    }

    /**
     * 重置基线线程数
     */
    public void resetBaselineThreadCount() {
        this.baselineThreadCount = ManagementFactory.getThreadMXBean().getThreadCount();
        log.info("[资源监控] 重置基线线程数: {}", baselineThreadCount);
    }

    /**
     * 获取告警计数
     */
    public int getAlertCount() {
        return alertCount.get();
    }

    /**
     * 重置告警计数
     */
    public void resetAlertCount() {
        alertCount.set(0);
    }

    @PreDestroy
    public void cleanup() {
        log.info("[资源监控] 关闭中，执行最终清理...");
        forceCleanupLocks();
        
        // 输出最终资源状态
        Map<String, Object> finalStatus = getResourceStatus();
        log.info("[资源监控] 最终资源状态 - 线程: {}, 内存: {}MB, 告警次数: {}", 
            finalStatus.get("currentThreads"),
            finalStatus.get("heapUsedMB"),
            finalStatus.get("alertCount"));
    }
}
