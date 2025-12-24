package com.wx.fbsir.engine.playwright.pool;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.wx.fbsir.engine.playwright.config.PlaywrightProperties;
import com.wx.fbsir.engine.playwright.core.PlaywrightManager;
import com.wx.fbsir.engine.playwright.session.BrowserSession;
import com.wx.fbsir.engine.playwright.util.ClipboardManager;
import com.wx.fbsir.engine.playwright.util.ScreenshotUtil;
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
    private final ClipboardManager clipboardManager;
    private final ScreenshotUtil screenshotUtil;
    private final GlobalBrowserPool globalBrowserPool;

    /**
     * 持久化会话池（按 userId+name 索引）
     * <p>⚠️ 并发场景：
     * <ul>
     *   <li>场景1：用户A同时打开2个标签页访问同一能力 → 需要复用同一Session</li>
     *   <li>场景2：用户A访问能力1，用户B同时访问能力2 → 完全隔离</li>
     *   <li>场景3：用户A长时间未操作，Session被清理，此时重新访问 → 创建新Session</li>
     * </ul>
     * <p>🔒 线程安全：ConcurrentHashMap + computeIfAbsent确保原子性
     */
    private final ConcurrentHashMap<String, BrowserSession> persistentSessions = new ConcurrentHashMap<>();

    /**
     * 临时会话池（按 sessionId 索引）
     * <p>⚠️ 并发场景：
     * <ul>
     *   <li>场景1：批量任务同时创建100个临时Session → Semaphore限流</li>
     *   <li>场景2：Session用完立即销毁 → 快速回收资源</li>
     * </ul>
     * <p>🔒 线程安全：ConcurrentHashMap保证put/remove原子性
     */
    private final ConcurrentHashMap<String, BrowserSession> temporarySessions = new ConcurrentHashMap<>();

    /**
     * 并发控制信号量（防止资源耗尽）
     * <p>⚠️ 边界场景：
     * <ul>
     *   <li>场景1：100个并发请求同时到达 → Semaphore排队，超时拒绝</li>
     *   <li>场景2：Session创建失败 → 必须release()，否则永久泄漏</li>
     * </ul>
     * <p>🔒 关键：acquire和release必须成对出现，使用try-finally确保
     */
    private Semaphore semaphore;

    /**
     * 当前活跃会话数（监控用）
     * <p>📊 用途：实时监控资源使用情况，与Semaphore配合判断负载
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

    public BrowserPoolManager(PlaywrightManager playwrightManager, PlaywrightProperties properties,
                               ClipboardManager clipboardManager, ScreenshotUtil screenshotUtil,
                               GlobalBrowserPool globalBrowserPool) {
        this.playwrightManager = playwrightManager;
        this.properties = properties;
        this.clipboardManager = clipboardManager;
        this.screenshotUtil = screenshotUtil;
        this.globalBrowserPool = globalBrowserPool;
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
     * <p>⚠️ 关键并发场景分析：
     * <pre>
     * 场景1：用户A同时发起2个请求访问baidu能力
     *   线程1: acquire("userA", "baidu", null, true, true)
     *   线程2: acquire("userA", "baidu", null, true, true)
     *   期望：线程1创建Session，线程2复用
     *   实现：computeIfAbsent原子性保证只创建一次
     * 
     * 场景2：用户A访问baidu，用户B访问google
     *   线程1: acquire("userA", "baidu", ...)
     *   线程2: acquire("userB", "google", ...)
     *   期望：完全隔离，互不影响
     *   实现：不同key，ConcurrentHashMap保证隔离
     * 
     * 场景3：同一用户多实例隔离（如多开微信）
     *   线程1: acquire("userA", "wechat", "instance1", ...)
     *   线程2: acquire("userA", "wechat", "instance2", ...)
     *   期望：两个独立Session，互不干扰
     *   实现：instanceId区分不同实例
     * 
     * 场景4：100个并发请求同时到达
     *   期望：Semaphore限流，超过最大值的请求等待或拒绝
     *   实现：tryAcquire(timeout) + 清晰的错误提示
     * </pre>
     * 
     * @param userId 用户ID（用于用户隔离）
     * @param name 会话名称（能力名称，如"baidu", "wechat"）
     * @param instanceId 实例ID（同一用户多实例隔离，可为null）
     * @param persistent 是否持久化（true=数据保存到磁盘，false=无痕模式）
     * @param headless 是否无头模式（true=后台运行，false=显示窗口）
     * @return BrowserSession
     */
    public BrowserSession acquire(String userId, String name, String instanceId, boolean persistent, boolean headless) {
        if (shutdown) {
            throw new IllegalStateException("BrowserPoolManager 已关闭");
        }
        
        // 构建会话键：支持实例ID隔离
        // 格式：userId:name 或 userId:name:instanceId
        String key = instanceId != null ? buildKey(userId, name, instanceId) : buildKey(userId, name);
        
        // 🔴 P0修复：持久化会话复用存在竞态条件
        // 旧问题：线程1 get()检查通过，线程2 put()覆盖，线程1返回旧Session → Session泄漏
        // 新方案：使用computeIfAbsent原子性操作
        if (persistent) {
            // 先尝试原子获取现有Session
            BrowserSession existing = persistentSessions.computeIfPresent(key, (k, session) -> {
                // 在computeIfPresent的lambda中，持有锁，线程安全
                if (session.isValid() && session.acquire(name)) {
                    log.debug("[浏览器池] 复用持久化会话: {}", key);
                    return session; // 保留现有Session
                }
                // Session无效或无法获取，返回null让外层重新创建
                return null;
            });
            
            if (existing != null) {
                // 成功复用
                return wrapSession(existing);
            }
            // existing == null，说明没有可用的现有Session，继续创建新的
        }
        
        // 获取信号量（等待可用槽位）
        boolean semaphoreAcquired = false;
        BrowserSession session = null;
        boolean sessionCreated = false;
        
        try {
            semaphoreAcquired = semaphore.tryAcquire(properties.getPool().getAcquireTimeout(), TimeUnit.MILLISECONDS);
            if (!semaphoreAcquired) {
                int available = semaphore.availablePermits();
                log.error("[浏览器池] 资源耗尽 - 活跃会话: {}, 最大: {}, 可用槽位: {}, 等待超时: {}ms | 建议: 1) 稍后重试 2) 增加浏览器池大小",
                    activeCount.get(), properties.getPool().getMaxSize(), available, properties.getPool().getAcquireTimeout());
                throw new RuntimeException("浏览器池繁忙，当前" + activeCount.get() + "个活跃会话，请稍后重试");
            }
            
            // 创建新会话（使用全局Browser池）
            session = createSession(userId, name, instanceId, persistent, headless);
            sessionCreated = true;
            activeCount.incrementAndGet();
            totalCreatedCount.incrementAndGet();
            
            // 🔴 P0修复：加入池时存在竞态条件
            // 旧问题：线程1和线程2同时创建Session，put()时后者覆盖前者 → 资源泄漏
            // 新方案：使用putIfAbsent原子性操作
            if (persistent) {
                BrowserSession finalSession = session;
                BrowserSession existing = persistentSessions.putIfAbsent(key, session);
                if (existing != null) {
                    // 另一个线程已经放入了Session，销毁当前创建的
                    log.warn("[浏览器池] 并发创建冲突，销毁多余Session: {}", key);
                    destroySessionQuietly(session);
                    activeCount.decrementAndGet();
                    // 使用已存在的Session
                    session = existing;
                    sessionCreated = false; // 标记未成功创建
                    if (!session.acquire(name)) {
                        // 极端情况：existing也无法获取，抛异常让外层重试
                        throw new RuntimeException("持久化会话被其他线程占用: " + key);
                    }
                }
            } else {
                // 临时Session使用唯一sessionId，无并发冲突
                temporarySessions.put(session.getSessionId(), session);
            }
            
            session.acquire(name);
            log.debug("[浏览器池] 创建新会话: {} (持久化={}, 无头={}, 总创建={})", 
                key, persistent, headless, totalCreatedCount.get());
            
            return wrapSession(session);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            cleanupFailedAcquisition(semaphoreAcquired, sessionCreated, session);
            log.error("[浏览器池] 获取会话被中断 - 用户: {}, 会话: {}", userId, name);
            throw new RuntimeException("获取浏览器会话被中断", e);
        } catch (Exception e) {
            cleanupFailedAcquisition(semaphoreAcquired, sessionCreated, session);
            log.error("[浏览器池] 创建会话失败 - 用户: {}, 错误: {}", userId, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 清理失败的会话获取（确保资源不泄漏）
     */
    private void cleanupFailedAcquisition(boolean semaphoreAcquired, boolean sessionCreated, BrowserSession session) {
        if (sessionCreated && session != null) {
            try {
                session.destroy();
                activeCount.decrementAndGet();
            } catch (Exception e) {
                log.warn("[浏览器池] 清理失败会话异常: {}", e.getMessage());
            }
        }
        if (semaphoreAcquired) {
            semaphore.release();
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
                
                log.debug("[浏览器池] 创建会话成功 - 用户: {}, 名称: {}, 尝试次数: {}", 
                    userId, name, attempt);
                
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
                // 最后一次失败时输出完整堆栈，其他次只记录错误信息
                if (attempt == maxRetries) {
                    log.error("[浏览器池] 创建会话失败 - 用户: {}, 尝试: {}/{}, 错误: {}", 
                        userId, attempt, maxRetries, e.getMessage(), e);
                } else {
                    log.warn("[浏览器池] 创建会话失败 - 用户: {}, 尝试: {}/{}, 错误: {}", 
                        userId, attempt, maxRetries, e.getMessage());
                }
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
        
        // 所有重试均失败，这是严重错误，必须告知用户
        String errorMsg = String.format(
            "无法创建浏览器会话 - 用户: %s, 已重试 %d 次\n" +
            "可能原因: 1) Playwright 未正确安装 2) 系统资源不足 3) 浏览器进程崩溃\n" +
            "建议: 1) 检查 Playwright 安装 2) 重启 Engine 服务 3) 检查系统资源",
            userId, maxRetries);
        log.error("{}", errorMsg, lastException);
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
            
            // 🟠 P1修复：设置超时，防止页面加载卡死
            BrowserContext context = browserType.launchPersistentContext(userDataPath, 
                new BrowserType.LaunchPersistentContextOptions()
                    .setHeadless(headless)
                    .setTimeout(browserConfig.getLaunchTimeout())
                    .setViewportSize(browserConfig.getViewportWidth(), browserConfig.getViewportHeight()));
            
            // 设置默认超时：30秒
            context.setDefaultTimeout(30000);
            // 设置导航超时：60秒
            context.setDefaultNavigationTimeout(60000);
            
            // 持久化上下文不返回 Browser（由 Playwright 内部管理）
            return new BrowserContextResult(null, context);
        } else {
            // 临时上下文：从全局Browser池获取Browser（性能优化）
            Browser browser = globalBrowserPool.acquireBrowser();
            
            try {
                // 🟠 P1修复：设置超时，防止页面加载卡死
                BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(browserConfig.getViewportWidth(), browserConfig.getViewportHeight()));
                
                // 设置默认超时：30秒
                context.setDefaultTimeout(30000);
                // 设置导航超时：60秒
                context.setDefaultNavigationTimeout(60000);
                
                // 返回 Browser 和 Context，确保两者都能被正确关闭
                return new BrowserContextResult(browser, context);
            } catch (Exception e) {
                // 创建Context失败，归还Browser到池
                globalBrowserPool.releaseBrowser(browser);
                throw e;
            }
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
     * 包装会话（设置自动清理回调）
     */
    private BrowserSession wrapSession(BrowserSession session) {
        session.setOnClose(() -> release(session));
        // 设置页面关闭回调，自动清理剪贴板锁和截图锁
        session.setOnPageClose(page -> {
            if (clipboardManager != null) {
                clipboardManager.cleanupPageLock(page);
            }
            if (screenshotUtil != null) {
                screenshotUtil.cleanupPageLock(page);
            }
        });
        
        // 设置Browser归还回调（临时会话destroy时归还Browser到全局池）
        if (!session.isPersistent()) {
            session.setBrowserReleaseCallback(browser -> {
                try {
                    globalBrowserPool.releaseBrowser(browser);
                    log.debug("[会话] Browser已归还到全局池 - 用户: {}", session.getUserId());
                } catch (Exception e) {
                    log.warn("[会话] Browser归还失败: {}", e.getMessage());
                }
            });
        }
        
        return session;
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

    /**
     * 安全地销毁Session（静默处理异常）
     */
    private void destroySessionQuietly(BrowserSession session) {
        if (session == null) {
            return;
        }
        try {
            session.destroy();
        } catch (Exception e) {
            log.warn("[浏览器池] 销毁Session异常: {}", e.getMessage());
        }
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
