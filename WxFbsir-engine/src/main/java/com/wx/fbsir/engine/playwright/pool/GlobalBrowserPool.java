package com.wx.fbsir.engine.playwright.pool;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.wx.fbsir.engine.playwright.config.PlaywrightProperties;
import com.wx.fbsir.engine.playwright.core.PlaywrightManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 全局Browser池（解决Browser未池化的性能问题）
 * 
 * 【架构改进】
 * - 旧架构：每个Session创建一个Browser → 启动慢（1-2秒）、资源浪费
 * - 新架构：全局维护2个Browser实例，复用BrowserContext → 启动快、资源高效
 * 
 * 【设计原则】
 * - Browser全局复用（重量级资源，启动慢）
 * - BrowserContext按需创建（轻量级，毫秒级）
 * - Page生命周期由BrowserSession管理
 * 
 * @author wxfbsir - Senior Architect
 * @date 2025-12-22
 */
@Component
public class GlobalBrowserPool {

    private static final Logger log = LoggerFactory.getLogger(GlobalBrowserPool.class);

    private final PlaywrightManager playwrightManager;
    private final PlaywrightProperties properties;

    /**
     * Browser池大小（动态配置，默认CPU核心数）
     * <p>⚠️ 设计考量：
     * <ul>
     *   <li>默认值：Math.max(2, CPU核心数)，适应不同硬件</li>
     *   <li>不宜过多：Browser是重量级进程，占用大量内存（~200MB/个）</li>
     * </ul>
     * <p>🟠 P1修复：改为可配置，支持动态扩容
     */
    private int poolSize;
    
    /**
     * Browser启动超时（60秒）
     * <p>首次启动需要下载Chromium，可能较慢
     */
    private static final long BROWSER_LAUNCH_TIMEOUT_MS = 60000;
    
    /**
     * 获取Browser超时（30秒）
     * <p>⚠️ 边界场景：所有Browser都在使用中，新请求需等待
     */
    private static final long ACQUIRE_TIMEOUT_MS = 30000;

    /**
     * 可用Browser队列
     * <p>⚠️ 并发场景：
     * <ul>
     *   <li>场景1：线程1 poll()取出Browser，线程2同时poll() → ConcurrentLinkedQueue保证线程安全</li>
     *   <li>场景2：线程1归还Browser(offer)，线程2同时取出 → 无竞态条件</li>
     * </ul>
     */
    private final ConcurrentLinkedQueue<Browser> availableBrowsers = new ConcurrentLinkedQueue<>();
    
    /**
     * Browser数量信号量（限制最多创建poolSize个）
     * <p>🔒 关键：防止并发创建超过poolSize个Browser
     */
    private Semaphore browserSemaphore;
    
    /**
     * 已创建的Browser总数（监控用）
     */
    private final AtomicInteger totalBrowsers = new AtomicInteger(0);
    
    /**
     * 是否已关闭（防止关闭后继续使用）
     */
    private volatile boolean shutdown = false;

    public GlobalBrowserPool(PlaywrightManager playwrightManager, PlaywrightProperties properties) {
        this.playwrightManager = playwrightManager;
        this.properties = properties;
        // 🟠 P1修复：动态计算Browser池大小
        int cpuCores = Runtime.getRuntime().availableProcessors();
        this.poolSize = Math.max(2, Math.min(cpuCores, 8)); // 最小2个，最大8个
        this.browserSemaphore = new Semaphore(poolSize);
        log.info("[Browser池] 动态配置池大小: {} (CPU核心数: {})", poolSize, cpuCores);
    }

    @PostConstruct
    public void init() {
        if (!properties.isEnabled()) {
            log.info("[Browser池] Playwright已禁用，跳过初始化");
            return;
        }

        int successCount = 0;
        for (int i = 0; i < poolSize; i++) {
            try {
                Browser browser = createBrowser();
                availableBrowsers.offer(browser);
                totalBrowsers.incrementAndGet();
                successCount++;
            } catch (Exception e) {
                log.error("[Browser池] Browser #{} 创建失败: {}", i + 1, e.getMessage(), e);
            }
        }
        
        log.info("[Browser池] 初始化完成 - 可用Browser: {}/{}", successCount, poolSize);
    }

    /**
     * 获取Browser实例（带超时控制）
     * 
     * <p>⚠️ 关键并发场景分析：
     * <pre>
     * 场景1：100个并发请求同时到达
     *   线程1-100同时调用acquireBrowser()
     *   期望：最多创建2个Browser，其他线程等待或超时
     *   实现：Semaphore限制为2，超时拒绝
     * 
     * 场景2：Browser意外断开连接
     *   线程1 poll()到已断开的Browser
     *   期望：重新创建可用的Browser
     *   实现：检查isConnected()，断开则关闭并重建
     * 
     * 场景3：池已空但未达上限
     *   线程1 poll()返回null，需创建新Browser
     *   线程2同时poll()也返回null
     *   期望：都能创建成功（因为总数<POOL_SIZE）
     *   实现：Semaphore保证不超限
     * </pre>
     * 
     * <p>🔴 P0风险：Semaphore泄漏会导致永久无法获取Browser
     * <p>🔒 保证：使用try-finally确保异常时release()
     */
    public Browser acquireBrowser() {
        if (shutdown) {
            throw new IllegalStateException("Browser池已关闭");
        }

        boolean semaphoreAcquired = false;
        Browser browser = null;
        
        try {
            semaphoreAcquired = browserSemaphore.tryAcquire(ACQUIRE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!semaphoreAcquired) {
                throw new RuntimeException("Browser池繁忙，等待超时: " + ACQUIRE_TIMEOUT_MS + "ms");
            }

            // 尝试从池中获取可用Browser
            browser = availableBrowsers.poll();
            if (browser != null && browser.isConnected()) {
                log.debug("[Browser池] 复用Browser实例 - 可用数: {}", availableBrowsers.size());
                return browser;
            }

            // Browser断开连接，关闭并重建
            if (browser != null && !browser.isConnected()) {
                log.warn("[Browser池] 检测到断开的Browser，重新创建");
                try {
                    browser.close();
                } catch (Exception e) {
                    log.debug("[Browser池] 关闭断开的Browser异常: {}", e.getMessage());
                }
                browser = null; // 清空，避免后续误用
            }

            log.debug("[Browser池] 创建新Browser实例 - 总数: {}/{}", totalBrowsers.get() + 1, poolSize);
            browser = createBrowser();
            totalBrowsers.incrementAndGet();
            return browser;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 🔴 P0修复：中断时必须释放Semaphore
            if (semaphoreAcquired) {
                browserSemaphore.release();
                log.debug("[Browser池] 中断，释放Semaphore");
            }
            throw new RuntimeException("获取Browser被中断", e);
        } catch (Exception e) {
            // 🔴 P0修复：异常时必须释放Semaphore
            if (semaphoreAcquired) {
                browserSemaphore.release();
                log.error("[Browser池] 创建失败，释放Semaphore - 错误: {}", e.getMessage());
            }
            throw new RuntimeException("获取Browser失败: " + e.getMessage(), e);
        }
    }

    /**
     * 归还Browser实例
     * 
     * <p>⚠️ 关键并发场景：
     * <pre>
     * 场景1：Session使用完Browser后归还
     *   线程1 releaseBrowser(browser1)
     *   线程2同时 acquireBrowser() → 可能获取到browser1
     *   实现：ConcurrentLinkedQueue保证线程安全
     * 
     * 场景2：Browser在使用过程中断开连接
     *   期望：不归还到池中，直接释放Semaphore
     *   实现：检查isConnected()，断开则不offer()
     * 
     * 场景3：归还null（编程错误）
     *   期望：不影响池状态，但必须释放Semaphore
     *   实现：null检查 + release()
     * </pre>
     * 
     * <p>🔴 P0风险：归还时忘记release()会永久泄漏Semaphore
     * <p>🔒 保证：任何情况下都必须release()
     */
    public void releaseBrowser(Browser browser) {
        try {
            if (browser == null) {
                log.warn("[Browser池] 归还null Browser，可能存在编程错误");
                return;
            }

            if (!browser.isConnected()) {
                log.warn("[Browser池] Browser已断开连接，不归还到池中");
                try {
                    browser.close();
                } catch (Exception e) {
                    log.debug("[Browser池] 关闭断开的Browser异常: {}", e.getMessage());
                }
                return;
            }

            // 归还到池中
            availableBrowsers.offer(browser);
            log.debug("[Browser池] Browser已归还 - 可用数: {}", availableBrowsers.size());
            
        } finally {
            // 🔴 P0修复：确保任何情况下都释放Semaphore
            browserSemaphore.release();
        }
    }

    /**
     * 创建Browser实例
     */
    private Browser createBrowser() {
        Playwright playwright = playwrightManager.getPlaywright();
        
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
            .setHeadless(properties.isHeadless())
            .setTimeout(properties.getBrowser().getLaunchTimeout());

        if (properties.getBrowser().isDisableGpu()) {
            options.setArgs(java.util.Arrays.asList("--disable-gpu"));
        }

        return playwright.chromium().launch(options);
    }

    @PreDestroy
    public void destroy() {
        shutdown = true;
        log.info("[Browser池] 关闭中，释放{}个Browser实例...", availableBrowsers.size());

        Browser browser;
        int closedCount = 0;
        while ((browser = availableBrowsers.poll()) != null) {
            try {
                browser.close();
                closedCount++;
            } catch (Exception e) {
                log.warn("[Browser池] Browser关闭失败: {}", e.getMessage());
            }
        }

        log.info("[Browser池] 已关闭{}个Browser实例", closedCount);
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public int getAvailableCount() {
        return availableBrowsers.size();
    }

    public int getTotalBrowsers() {
        return totalBrowsers.get();
    }
}
