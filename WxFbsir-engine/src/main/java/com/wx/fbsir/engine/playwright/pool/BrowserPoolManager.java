package com.wx.fbsir.engine.playwright.pool;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.wx.fbsir.engine.playwright.config.PlaywrightProperties;
import com.wx.fbsir.engine.playwright.core.PlaywrightManager;
import com.wx.fbsir.engine.playwright.session.BrowserSession;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 浏览器池管理器
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 核心职责
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 1. 管理浏览器会话池（创建、复用、回收）
 * 2. 支持持久化会话和临时会话
 * 3. 支持有头模式和无头模式
 * 4. 自动清理过期会话
 * 5. 并发控制，防止资源耗尽
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 设计亮点（解决 cube-engine 问题）
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 1. 并发安全：使用 ConcurrentHashMap 和 Semaphore
 * 2. 资源限制：最大会话数限制，防止 OOM
 * 3. 自动清理：定时任务清理过期会话
 * 4. 优雅关闭：PreDestroy 确保资源释放
 * 5. 简化重试：移除复杂的重试逻辑，失败快速返回
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 使用方式
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * ```java
 * @Autowired
 * private BrowserPoolManager browserPool;
 * 
 * // 获取持久化会话（数据保存到磁盘）
 * try (BrowserSession session = browserPool.acquirePersistent("userId", "baidu")) {
 *     Page page = session.getOrCreatePage();
 *     page.navigate("https://baidu.com");
 * }
 * 
 * // 获取临时会话（无痕模式）
 * try (BrowserSession session = browserPool.acquireTemporary("taskId")) {
 *     Page page = session.getOrCreatePage();
 *     page.navigate("https://example.com");
 * }
 * 
 * // 指定无头/有头模式
 * try (BrowserSession session = browserPool.acquire("userId", "task", true, true)) {
 *     // persistent=true, headless=true
 * }
 * ```
 * 
 * @author wxfbsir
 * @date 2025-12-16
 */
@Component
public class BrowserPoolManager {

    private static final Logger log = LoggerFactory.getLogger(BrowserPoolManager.class);

    private final PlaywrightManager playwrightManager;
    private final PlaywrightProperties properties;

    /**
     * 持久化会话池（按 userId+name 索引）
     */
    private final ConcurrentHashMap<String, BrowserSession> persistentSessions = new ConcurrentHashMap<>();

    /**
     * 临时会话池
     */
    private final ConcurrentHashMap<String, BrowserSession> temporarySessions = new ConcurrentHashMap<>();

    /**
     * 并发控制信号量
     */
    private Semaphore semaphore;

    /**
     * 当前活跃会话数
     */
    private final AtomicInteger activeCount = new AtomicInteger(0);
    
    /**
     * 总创建会话数（用于资源监控）
     */
    private final AtomicInteger totalCreatedCount = new AtomicInteger(0);
    
    /**
     * 总销毁会话数（用于资源监控）
     */
    private final AtomicInteger totalDestroyedCount = new AtomicInteger(0);

    /**
     * 是否已关闭
     */
    private volatile boolean shutdown = false;

    public BrowserPoolManager(PlaywrightManager playwrightManager, PlaywrightProperties properties) {
        this.playwrightManager = playwrightManager;
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        if (!properties.isEnabled()) {
            log.info("[浏览器池] 已禁用");
            return;
        }
        
        // 初始化信号量
        this.semaphore = new Semaphore(properties.getPool().getMaxSize());
        
        // 确保数据目录存在
        try {
            Path dataDir = Paths.get(properties.getDataDir());
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (Exception e) {
            log.warn("[浏览器池] 创建数据目录失败: {}", e.getMessage());
        }
        
        log.info("[浏览器池] 初始化完成 - 最大会话数: {}, 数据目录: {}", 
            properties.getPool().getMaxSize(), properties.getDataDir());
    }

    // ==================== 会话获取方法 ====================

    /**
     * 获取持久化会话
     * 会话数据保存到磁盘，下次启动时可复用（如登录状态）
     * 
     * @param userId 用户ID
     * @param name 会话名称（如 "baidu", "deepseek"）
     * @return BrowserSession
     */
    public BrowserSession acquirePersistent(String userId, String name) {
        return acquire(userId, name, true, properties.isHeadless());
    }

    /**
     * 获取持久化会话（指定无头模式）
     */
    public BrowserSession acquirePersistent(String userId, String name, boolean headless) {
        return acquire(userId, name, true, headless);
    }

    /**
     * 获取临时会话（无痕模式）
     * 会话关闭后数据不保留
     * 
     * @param taskId 任务ID
     * @return BrowserSession
     */
    public BrowserSession acquireTemporary(String taskId) {
        return acquire(taskId, "temp", false, properties.isHeadless());
    }

    /**
     * 获取临时会话（指定无头模式）
     */
    public BrowserSession acquireTemporary(String taskId, boolean headless) {
        return acquire(taskId, "temp", false, headless);
    }

    /**
     * 获取浏览器会话（完整参数）
     * 
     * @param userId 用户ID
     * @param name 会话名称
     * @param persistent 是否持久化
     * @param headless 是否无头模式
     * @return BrowserSession
     */
    public BrowserSession acquire(String userId, String name, boolean persistent, boolean headless) {
        return acquire(userId, name, null, persistent, headless);
    }
    
    /**
     * 获取浏览器会话（支持实例ID，用于同一用户多浏览器实例隔离）
     * 
     * @param userId 用户ID
     * @param name 会话名称
     * @param instanceId 实例ID（可为null，将使用userId:name作为默认key）
     * @param persistent 是否持久化
     * @param headless 是否无头模式
     * @return BrowserSession
     */
    public BrowserSession acquire(String userId, String name, String instanceId, boolean persistent, boolean headless) {
        if (shutdown) {
            throw new IllegalStateException("BrowserPoolManager 已关闭");
        }
        
        // 构建会话键：支持实例ID隔离
        String key = instanceId != null ? buildKey(userId, name, instanceId) : buildKey(userId, name);
        
        // 持久化会话：尝试复用
        if (persistent) {
            BrowserSession existing = persistentSessions.get(key);
            if (existing != null && existing.isValid() && existing.acquire(name)) {
                log.debug("[浏览器池] 复用持久化会话: {}", key);
                return wrapSession(existing);
            }
        }
        
        // 获取信号量（等待可用槽位）
        boolean acquired = false;
        try {
            acquired = semaphore.tryAcquire(properties.getPool().getAcquireTimeout(), TimeUnit.MILLISECONDS);
            if (!acquired) {
                throw new RuntimeException("获取浏览器会话超时，当前活跃会话数: " + activeCount.get());
            }
            
            // 创建新会话
            BrowserSession session = createSession(userId, name, instanceId, persistent, headless);
            activeCount.incrementAndGet();
            totalCreatedCount.incrementAndGet();
            
            // 加入池
            if (persistent) {
                // 关闭旧的持久化会话
                BrowserSession old = persistentSessions.put(key, session);
                if (old != null && old != session) {
                    destroySessionQuietly(old);
                }
            } else {
                temporarySessions.put(session.getSessionId(), session);
            }
            
            session.acquire(name);
            log.info("[浏览器池] 创建新会话: {} (持久化={}, 无头={}, 总创建={})", 
                key, persistent, headless, totalCreatedCount.get());
            
            return wrapSession(session);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (acquired) {
                semaphore.release();
            }
            throw new RuntimeException("获取浏览器会话被中断", e);
        } catch (Exception e) {
            if (acquired) {
                semaphore.release();
            }
            throw e;
        }
    }

    /**
     * 释放会话（归还到池）
     */
    public void release(BrowserSession session) {
        if (session == null) return;
        
        session.release();
        
        // 临时会话：立即销毁
        if (!session.isPersistent()) {
            temporarySessions.remove(session.getSessionId());
            destroySessionQuietly(session);
            activeCount.decrementAndGet();
            semaphore.release();
        }
        // 持久化会话：保留在池中
    }

    /**
     * 强制关闭指定会话
     */
    public void closeSession(String userId, String name) {
        String key = buildKey(userId, name);
        BrowserSession session = persistentSessions.remove(key);
        if (session != null) {
            destroySessionQuietly(session);
            activeCount.decrementAndGet();
            semaphore.release();
            log.info("[浏览器池] 关闭会话: {}", key);
        }
    }

    // ==================== 内部方法 ====================

    /**
     * 创建浏览器会话
     * 
     * 包含重试机制，确保在短暂故障时能够恢复
     * 
     * @param userId 用户ID
     * @param name 会话名称
     * @param instanceId 实例ID
     * @param persistent 是否持久化
     * @param headless 是否无头模式
     * @return BrowserSession
     */
    private BrowserSession createSession(String userId, String name, String instanceId, boolean persistent, boolean headless) {
        PlaywrightProperties.BrowserConfig browserConfig = properties.getBrowser();
        int maxRetries = browserConfig.getMaxRetries();
        long retryInterval = browserConfig.getRetryInterval();
        
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            Browser browser = null;
            BrowserContext context = null;
            
            try {
                // 创建浏览器上下文（可能返回 Browser 和 Context）
                BrowserContextResult result = doCreateBrowserContext(userId, name, instanceId, persistent, headless);
                browser = result.browser;
                context = result.context;
                
                // 授予剪贴板权限
                try {
                    context.grantPermissions(Arrays.asList("clipboard-read", "clipboard-write"));
                } catch (Exception e) {
                    log.debug("[浏览器池] 授予剪贴板权限失败 - 用户: {}, 错误: {}", userId, e.getMessage());
                }
                
                log.debug("[浏览器池] 创建会话成功 - 用户: {}, 名称: {}, 尝试次数: {}, 有Browser: {}", 
                    userId, name, attempt, browser != null);
                
                // 返回包含 Browser 的会话（仅临时会话）
                return new BrowserSession(userId, name, browser, context, instanceId, persistent, headless, 
                    properties.getPool().getSessionTimeout());
                    
            } catch (com.microsoft.playwright.TimeoutError e) {
                lastException = e;
                log.warn("[浏览器池] 创建会话超时 - 用户: {}, 尝试: {}/{}, 错误: {}", 
                    userId, attempt, maxRetries, e.getMessage());
                // 清理失败时创建的资源
                cleanupFailedResources(browser, context);
            } catch (com.microsoft.playwright.impl.TargetClosedError e) {
                lastException = e;
                log.warn("[浏览器池] 浏览器目标已关闭 - 用户: {}, 尝试: {}/{}, 错误: {}", 
                    userId, attempt, maxRetries, e.getMessage());
                // 清理失败时创建的资源
                cleanupFailedResources(browser, context);
                // 清理可能的锁文件
                cleanupBrowserLockFiles(userId, name);
            } catch (Exception e) {
                lastException = e;
                log.error("[浏览器池] 创建会话失败 - 用户: {}, 尝试: {}/{}, 错误类型: {}, 错误信息: {}", 
                    userId, attempt, maxRetries, e.getClass().getSimpleName(), e.getMessage());
                // 清理失败时创建的资源
                cleanupFailedResources(browser, context);
            }
            
            // 如果不是最后一次尝试，等待后重试
            if (attempt < maxRetries) {
                try {
                    Thread.sleep(retryInterval * attempt); // 递增等待时间
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("[浏览器池] 创建会话被中断 - 用户: " + userId, ie);
                }
            }
        }
        
        // 所有重试均失败
        String errorMsg = String.format("[浏览器池] 创建会话失败 - 用户: %s, 已重试 %d 次", userId, maxRetries);
        log.error("{}, 最后错误: {}", errorMsg, lastException != null ? lastException.getMessage() : "未知");
        throw new RuntimeException(errorMsg, lastException);
    }
    
    /**
     * 浏览器上下文创建结果（包含Browser和Context）
     */
    private static class BrowserContextResult {
        final Browser browser;  // 仅临时会话有值
        final BrowserContext context;
        
        BrowserContextResult(Browser browser, BrowserContext context) {
            this.browser = browser;
            this.context = context;
        }
    }
    
    /**
     * 实际创建浏览器上下文
     * 返回 Browser 和 Context，确保临时会话的 Browser 能被跟踪和关闭
     */
    private BrowserContextResult doCreateBrowserContext(String userId, String name, String instanceId, 
                                                         boolean persistent, boolean headless) {
        Playwright playwright = playwrightManager.getPlaywright();
        BrowserType browserType = playwright.chromium();
        
        PlaywrightProperties.BrowserConfig browserConfig = properties.getBrowser();
        List<String> args = buildBrowserArgs(headless);
        
        if (persistent) {
            // 持久化上下文：支持实例ID隔离
            Path userDataPath;
            if (instanceId != null) {
                // 有实例ID：每个实例独立目录
                userDataPath = Paths.get(properties.getDataDir(), name, userId, instanceId);
            } else {
                // 无实例ID：传统方式
                userDataPath = Paths.get(properties.getDataDir(), name, userId);
            }
            
            try {
                if (!Files.exists(userDataPath)) {
                    Files.createDirectories(userDataPath);
                    log.debug("[浏览器池] 创建用户数据目录: {}", userDataPath);
                }
            } catch (Exception e) {
                log.warn("[浏览器池] 创建用户数据目录失败 - 路径: {}, 错误: {}", userDataPath, e.getMessage());
            }
            
            BrowserContext context = browserType.launchPersistentContext(userDataPath, 
                new BrowserType.LaunchPersistentContextOptions()
                    .setHeadless(headless)
                    .setTimeout(browserConfig.getLaunchTimeout())
                    .setViewportSize(browserConfig.getViewportWidth(), browserConfig.getViewportHeight())
                    .setArgs(args));
            
            // 持久化上下文不返回 Browser（由 Playwright 内部管理）
            return new BrowserContextResult(null, context);
        } else {
            // 临时上下文：返回 Browser 以便跟踪和关闭
            Browser browser = browserType.launch(new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setTimeout(browserConfig.getLaunchTimeout())
                .setArgs(args));
            
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(browserConfig.getViewportWidth(), browserConfig.getViewportHeight()));
            
            // 返回 Browser 和 Context，确保两者都能被正确关闭
            return new BrowserContextResult(browser, context);
        }
    }
    
    /**
     * 清理创建失败时的资源
     * 确保不留下僵尸进程
     */
    private void cleanupFailedResources(Browser browser, BrowserContext context) {
        if (context != null) {
            try {
                context.close();
            } catch (Exception e) {
                log.debug("[浏览器池] 清理失败的Context异常: {}", e.getMessage());
            }
        }
        if (browser != null) {
            try {
                if (browser.isConnected()) {
                    browser.close();
                }
            } catch (Exception e) {
                log.debug("[浏览器池] 清理失败的Browser异常: {}", e.getMessage());
            }
        }
    }
    
    /**
     * 清理浏览器锁文件
     * 当浏览器异常退出时，可能留下锁文件导致无法重新启动
     */
    private void cleanupBrowserLockFiles(String userId, String name) {
        try {
            Path userDataPath = Paths.get(properties.getDataDir(), name, userId);
            if (!Files.exists(userDataPath)) {
                return;
            }
            
            // 清理 SingletonLock 文件
            Path singletonLock = userDataPath.resolve("SingletonLock");
            if (Files.exists(singletonLock)) {
                Files.delete(singletonLock);
                log.info("[浏览器池] 清理锁文件: {}", singletonLock);
            }
            
            // 清理其他锁文件
            try (var stream = Files.list(userDataPath)) {
                stream.filter(p -> p.getFileName().toString().contains("Lock") || 
                                   p.getFileName().toString().endsWith(".lock"))
                      .forEach(lockFile -> {
                          try {
                              Files.delete(lockFile);
                              log.debug("[浏览器池] 清理锁文件: {}", lockFile.getFileName());
                          } catch (Exception e) {
                              log.debug("[浏览器池] 清理锁文件失败: {}", e.getMessage());
                          }
                      });
            }
        } catch (Exception e) {
            log.debug("[浏览器池] 清理锁文件异常: {}", e.getMessage());
        }
    }

    /**
     * 构建浏览器启动参数
     */
    private List<String> buildBrowserArgs(boolean headless) {
        List<String> args = new ArrayList<>();
        
        // 基础参数
        args.add("--no-sandbox");
        args.add("--disable-dev-shm-usage");
        args.add("--disable-extensions");
        args.add("--disable-plugins");
        
        // GPU 设置
        if (properties.getBrowser().isDisableGpu()) {
            args.add("--disable-gpu");
        }
        
        // 图片加载设置
        if (properties.getBrowser().isDisableImages()) {
            args.add("--disable-images");
        }
        
        // 性能优化参数
        args.add("--disable-background-timer-throttling");
        args.add("--disable-backgrounding-occluded-windows");
        args.add("--disable-renderer-backgrounding");
        args.add("--disable-background-networking");
        args.add("--disable-sync");
        args.add("--no-first-run");
        args.add("--disable-default-apps");
        
        // 内存优化
        args.add("--memory-pressure-off");
        args.add("--max_old_space_size=256");
        
        return args;
    }

    /**
     * 包装会话，设置关闭回调
     */
    private BrowserSession wrapSession(BrowserSession session) {
        session.setOnClose(() -> release(session));
        return session;
    }

    /**
     * 静默销毁会话
     */
    private void destroySessionQuietly(BrowserSession session) {
        try {
            session.destroy();
            totalDestroyedCount.incrementAndGet();
        } catch (Exception e) {
            log.warn("[浏览器池] 销毁会话失败: {}", e.getMessage());
        }
    }

    /**
     * 构建会话键
     */
    private String buildKey(String userId, String name) {
        return userId + ":" + name;
    }
    
    /**
     * 构建会话键（包含实例ID）
     */
    private String buildKey(String userId, String name, String instanceId) {
        return userId + ":" + name + ":" + instanceId;
    }

    // ==================== 定时清理 ====================

    /**
     * 定时清理过期会话
     */
    @Scheduled(fixedDelayString = "${engine.playwright.pool.cleanup-interval:300000}")
    public void cleanupExpiredSessions() {
        if (shutdown || !properties.isEnabled()) return;
        
        int cleaned = 0;
        
        // 清理过期的持久化会话
        Iterator<Map.Entry<String, BrowserSession>> it = persistentSessions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, BrowserSession> entry = it.next();
            BrowserSession session = entry.getValue();
            
            if (session.isExpired() && !session.isInUse()) {
                it.remove();
                destroySessionQuietly(session);
                activeCount.decrementAndGet();
                semaphore.release();
                cleaned++;
            }
        }
        
        // 清理过期的临时会话
        Iterator<Map.Entry<String, BrowserSession>> tempIt = temporarySessions.entrySet().iterator();
        while (tempIt.hasNext()) {
            Map.Entry<String, BrowserSession> entry = tempIt.next();
            BrowserSession session = entry.getValue();
            
            if (session.isExpired() && !session.isInUse()) {
                tempIt.remove();
                destroySessionQuietly(session);
                activeCount.decrementAndGet();
                semaphore.release();
                cleaned++;
            }
        }
        
        if (cleaned > 0) {
            log.info("[浏览器池] 清理过期会话: {} 个，当前活跃: {}", cleaned, activeCount.get());
        }
        
        // 资源泄漏检测：创建数和销毁数差异过大时警告
        int created = totalCreatedCount.get();
        int destroyed = totalDestroyedCount.get();
        int diff = created - destroyed - activeCount.get();
        if (diff > 5) {
            log.warn("[浏览器池] 可能存在资源泄漏 - 创建: {}, 销毁: {}, 活跃: {}, 差异: {}", 
                created, destroyed, activeCount.get(), diff);
        }
    }

    // ==================== 关闭方法 ====================

    @PreDestroy
    public void shutdown() {
        if (shutdown) return;
        shutdown = true;
        
        log.info("[浏览器池] 正在关闭...");
        
        // 关闭所有持久化会话
        for (BrowserSession session : persistentSessions.values()) {
            destroySessionQuietly(session);
        }
        persistentSessions.clear();
        
        // 关闭所有临时会话
        for (BrowserSession session : temporarySessions.values()) {
            destroySessionQuietly(session);
        }
        temporarySessions.clear();
        
        log.info("[浏览器池] 已关闭");
    }

    // ==================== 状态查询 ====================

    /**
     * 获取当前活跃会话数
     */
    public int getActiveCount() {
        return activeCount.get();
    }

    /**
     * 获取持久化会话数
     */
    public int getPersistentCount() {
        return persistentSessions.size();
    }

    /**
     * 获取临时会话数
     */
    public int getTemporaryCount() {
        return temporarySessions.size();
    }

    /**
     * 获取可用槽位数
     */
    public int getAvailableSlots() {
        return semaphore != null ? semaphore.availablePermits() : 0;
    }

    /**
     * 获取池状态
     */
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("enabled", properties.isEnabled());
        status.put("activeCount", activeCount.get());
        status.put("persistentCount", persistentSessions.size());
        status.put("temporaryCount", temporarySessions.size());
        status.put("maxSize", properties.getPool().getMaxSize());
        status.put("availableSlots", getAvailableSlots());
        status.put("shutdown", shutdown);
        status.put("totalCreated", totalCreatedCount.get());
        status.put("totalDestroyed", totalDestroyedCount.get());
        return status;
    }
    
    /**
     * 获取资源泄漏检测信息
     */
    public String getResourceLeakInfo() {
        int created = totalCreatedCount.get();
        int destroyed = totalDestroyedCount.get();
        int active = activeCount.get();
        int diff = created - destroyed - active;
        
        return String.format("创建:%d, 销毁:%d, 活跃:%d, 差异:%d%s", 
            created, destroyed, active, diff, diff > 0 ? "(可能泄漏)" : "");
    }
}
