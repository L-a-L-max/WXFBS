package com.wx.fbsir.engine.playwright.session;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 浏览器会话抽象
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 核心职责
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 1. 封装 BrowserContext 和相关资源
 * 2. 管理会话生命周期（创建时间、最后活跃时间、过期检查）
 * 3. 提供页面管理功能（获取、创建、关闭页面）
 * 4. 实现 AutoCloseable 接口，支持 try-with-resources
 * 5. 追踪会话状态（活跃、空闲、已关闭）
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 使用方式
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * ```java
 * // 方式1：通过 try-with-resources 自动释放
 * try (BrowserSession session = browserPool.acquire("userId", "taskName")) {
 *     Page page = session.getOrCreatePage();
 *     page.navigate("https://example.com");
 *     // 执行操作...
 * } // 自动调用 close() 归还到池
 * 
 * // 方式2：手动管理
 * BrowserSession session = browserPool.acquire("userId", "taskName");
 * try {
 *     session.touch(); // 更新活跃时间
 *     Page page = session.getOrCreatePage();
 *     // 执行操作...
 * } finally {
 *     browserPool.release(session);
 * }
 * ```
 * 
 * @author wxfbsir
 * @date 2025-12-16
 */
public class BrowserSession implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(BrowserSession.class);

    /**
     * 会话唯一标识
     */
    private final String sessionId;

    /**
     * 关联的用户ID
     */
    private final String userId;

    /**
     * 会话名称/用途
     */
    private final String name;

    /**
     * 浏览器实例（仅临时会话使用，需要单独关闭）
     */
    private final Browser browser;
    
    /**
     * 浏览器上下文
     */
    private final BrowserContext context;
    
    /**
     * 实例ID（支持同一用户多浏览器实例隔离）
     */
    private final String instanceId;

    /**
     * 是否为持久化会话（数据会保存到磁盘）
     */
    private final boolean persistent;

    /**
     * 是否为无头模式
     */
    private final boolean headless;

    /**
     * 创建时间
     */
    private final Instant createTime;

    /**
     * 最后活跃时间
     */
    private final AtomicLong lastActiveTime;

    /**
     * 会话超时时间（毫秒）
     */
    private final long timeoutMillis;

    /**
     * 是否已关闭
     * <p>⚠️ 并发场景：
     * <ul>
     *   <li>场景1：多个线程同时调用destroy() → AtomicBoolean保证只执行一次</li>
     *   <li>场景2：Session使用中，定时任务检测到超时关闭 → 需要原子性检查</li>
     * </ul>
     */
    private final AtomicBoolean closed = new AtomicBoolean(false);

    /**
     * 是否正在使用中（防止并发获取同一Session）
     * <p>⚠️ 关键并发场景：
     * <pre>
     * 场景1：用户A同时打开2个标签页访问同一能力
     *   线程1: acquire("userA", "baidu", ...) → 复用Session
     *   线程2同时: acquire("userA", "baidu", ...) → 尝试复用
     *   期望：线程1成功获取，线程2等待或创建新Session
     *   实现：inUse.compareAndSet(false, true) 原子性检查
     * 
     * 场景2：Session使用完毕，但未归还
     *   线程1使用完毕，但忘记调用release()
     *   线程2尝试复用 → acquire()失败
     *   保护：防止Session被多个线程同时使用
     * </pre>
     */
    private final AtomicBoolean inUse = new AtomicBoolean(false);

    /**
     * 当前任务描述
     */
    private final AtomicReference<String> currentTask = new AtomicReference<>();

    /**
     * 关闭回调（用于归还到池）
     */
    private Runnable onClose;
    
    /**
     * 页面锁清理回调（用于自动清理剪贴板锁和截图锁）
     */
    private java.util.function.Consumer<Page> onPageClose;
    
    /**
     * Browser归还回调（用于归还Browser到全局池）
     * <p>⚠️ 关键：仅临时会话需要归还Browser，持久化会话不需要
     * <p>设计原理：
     * <ul>
     *   <li>持久化会话：Browser由Playwright内部管理，Context关闭时自动关闭Browser</li>
     *   <li>临时会话：Browser从全局池获取，用完必须归还</li>
     * </ul>
     */
    private java.util.function.Consumer<Browser> browserReleaseCallback;
    
    /**
     * 创建的页面计数（用于资源监控）
     * <p>📊 用途：监控单个Session的资源使用，及时发现Page泄漏
     */
    private final AtomicInteger pageCreatedCount = new AtomicInteger(0);
    
    /**
     * 关闭的页面计数（用于资源监控）
     */
    private final AtomicInteger pageClosedCount = new AtomicInteger(0);

    /**
     * 构造函数（持久化会话）
     */
    public BrowserSession(String userId, String name, BrowserContext context, 
                          boolean persistent, boolean headless, long timeoutMillis) {
        this(userId, name, null, context, null, persistent, headless, timeoutMillis);
    }
    
    /**
     * 构造函数（临时会话，包含 Browser 对象）
     */
    public BrowserSession(String userId, String name, Browser browser, BrowserContext context,
                          boolean persistent, boolean headless, long timeoutMillis) {
        this(userId, name, browser, context, null, persistent, headless, timeoutMillis);
    }
    
    /**
     * 构造函数（完整参数，支持实例ID）
     */
    public BrowserSession(String userId, String name, Browser browser, BrowserContext context, 
                          String instanceId, boolean persistent, boolean headless, long timeoutMillis) {
        this.sessionId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        this.userId = userId;
        this.name = name;
        this.browser = browser;
        this.context = context;
        this.instanceId = instanceId != null ? instanceId : this.sessionId;
        this.persistent = persistent;
        this.headless = headless;
        this.createTime = Instant.now();
        this.lastActiveTime = new AtomicLong(System.currentTimeMillis());
        this.timeoutMillis = timeoutMillis;
    }

    // ==================== 页面管理 ====================

    /**
     * 获取或创建页面
     * 优先复用空白页面，否则创建新页面
     * 
     * @return Page 页面对象
     */
    public Page getOrCreatePage() {
        checkNotClosed();
        touch();
        
        // 尝试复用空白页面
        List<Page> pages = context.pages();
        for (Page page : pages) {
            try {
                String url = page.url();
                if ("about:blank".equals(url) || url.isEmpty()) {
                    return page;
                }
            } catch (Exception e) {
                // 页面可能已关闭，跳过
            }
        }
        
        // 复用第一个页面
        if (!pages.isEmpty()) {
            return pages.get(0);
        }
        
        // 创建新页面
        return context.newPage();
    }

    /**
     * Page数量上限（防止泄漏导致OOM）
     * <p>🔴 P0修复：限制单个Session最多创建10个Page
     */
    private static final int MAX_PAGES_PER_SESSION = 10;
    
    /**
     * 创建新页面
     * 
     * @return Page 新页面对象
     */
    public Page newPage() {
        checkNotClosed();
        touch();
        
        // 🔴 P0修复：检查Page数量上限，防止泄漏
        int currentPageCount = context.pages().size();
        if (currentPageCount >= MAX_PAGES_PER_SESSION) {
            log.warn("[会话] Page数量已达上限 - 会话ID: {}, 当前: {}, 上限: {}, 自动关闭最旧的Page",
                sessionId, currentPageCount, MAX_PAGES_PER_SESSION);
            // 自动关闭最旧的Page（第一个）
            List<Page> pages = context.pages();
            if (!pages.isEmpty()) {
                closePage(pages.get(0));
            }
        }
        
        Page page = context.newPage();
        pageCreatedCount.incrementAndGet();
        log.debug("[会话] 创建新页面 - 会话ID: {}, 已创建页面数: {}", sessionId, pageCreatedCount.get());
        return page;
    }

    /**
     * 获取所有页面
     * 
     * @return 页面列表
     */
    public List<Page> getPages() {
        checkNotClosed();
        return context.pages();
    }

    /**
     * 关闭指定页面
     * 自动调用页面锁清理回调（清理剪贴板锁和截图锁）
     * 
     * @param page 要关闭的页面
     */
    public void closePage(Page page) {
        if (page != null && !page.isClosed()) {
            try {
                // 先调用页面锁清理回调
                if (onPageClose != null) {
                    try {
                        onPageClose.accept(page);
                    } catch (Exception e) {
                        log.debug("[会话] 页面锁清理回调执行失败 - 会话ID: {}, 错误: {}", sessionId, e.getMessage());
                    }
                }
                
                page.close();
                pageClosedCount.incrementAndGet();
                log.debug("[会话] 关闭页面 - 会话ID: {}, 已关闭页面数: {}", sessionId, pageClosedCount.get());
            } catch (Exception e) {
                log.warn("[会话] 关闭页面失败 - 会话ID: {}, 错误: {}", sessionId, e.getMessage());
            }
        }
    }

    /**
     * 关闭所有页面（保留一个空白页）
     */
    public void closeAllPages() {
        List<Page> pages = context.pages();
        for (Page page : pages) {
            closePage(page);
        }
    }

    // ==================== 生命周期管理 ====================

    /**
     * 更新最后活跃时间（心跳）
     */
    public void touch() {
        lastActiveTime.set(System.currentTimeMillis());
    }

    /**
     * 检查会话是否已过期
     * 
     * @return true 如果已过期
     */
    public boolean isExpired() {
        if (timeoutMillis <= 0) {
            return false; // 永不过期
        }
        return System.currentTimeMillis() - lastActiveTime.get() > timeoutMillis;
    }

    /**
     * 检查会话是否有效（未关闭且未过期）
     * 
     * @return true 如果有效
     */
    public boolean isValid() {
        return !closed.get() && !isExpired();
    }

    /**
     * 标记为使用中
     * 
     * @param task 任务描述
     * @return true 如果成功标记
     */
    public boolean acquire(String task) {
        if (inUse.compareAndSet(false, true)) {
            currentTask.set(task);
            touch();
            return true;
        }
        return false;
    }

    /**
     * 释放使用状态
     */
    public void release() {
        currentTask.set(null);
        inUse.set(false);
        touch();
    }

    /**
     * 关闭会话
     */
    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            log.debug("[会话] 关闭会话 - 会话ID: {}, 用户: {}, 运行时长: {}ms", 
                sessionId, userId, getRunningTime());
            // 先执行回调（归还到池）
            if (onClose != null) {
                try {
                    onClose.run();
                } catch (Exception e) {
                    log.warn("[会话] 关闭回调执行失败 - 会话ID: {}, 错误: {}", sessionId, e.getMessage());
                }
            }
        }
    }

    /**
     * 强制销毁会话（关闭浏览器上下文和Browser实例）
     * 
     * 资源释放顺序：Page -> BrowserContext -> Browser
     * 确保所有资源都被正确释放，不留下僵尸进程
     * 
     * 🔴 P0修复：检测Page泄漏并告警
     */
    public void destroy() {
        if (closed.compareAndSet(false, true)) {
            long startTime = System.currentTimeMillis();
            int pageCount = 0;
            int closedPages = 0;
            boolean contextClosed = false;
            boolean browserClosed = false;
            
            log.debug("[会话] 开始销毁 - 会话ID: {}, 用户: {}, 实例ID: {}", sessionId, userId, instanceId);
            
            // 🔴 P0修复：检测Page泄漏
            int created = pageCreatedCount.get();
            int closed = pageClosedCount.get();
            int leaked = created - closed;
            if (leaked > 3) {
                log.warn("[会话] 检测到Page泄漏 - 会话ID: {}, 创建: {}, 关闭: {}, 泄漏: {}",
                    sessionId, created, closed, leaked);
            }
            
            try {
                // 第1步：关闭所有页面
                try {
                    List<Page> pages = context.pages();
                    pageCount = pages.size();
                    for (Page page : pages) {
                        try {
                            if (!page.isClosed()) {
                                page.close();
                                closedPages++;
                                pageClosedCount.incrementAndGet();
                            }
                        } catch (Exception e) {
                            log.debug("[会话] 关闭页面失败 - 会话ID: {}, 错误: {}", sessionId, e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    log.warn("[会话] 获取页面列表失败 - 会话ID: {}, 错误: {}", sessionId, e.getMessage());
                }
                
                // 第2步：短暂等待页面关闭完成
                Thread.sleep(100);
                
                // 第3步：关闭 BrowserContext
                try {
                    context.close();
                    contextClosed = true;
                } catch (Exception e) {
                    log.warn("[会话] 关闭上下文失败 - 会话ID: {}, 错误: {}", sessionId, e.getMessage());
                }
                
                // 第4步：归还/关闭 Browser（仅临时会话）
                if (browser != null && !persistent) {
                    try {
                        if (browserReleaseCallback != null) {
                            // 归还到全局Browser池（性能优化）
                            browserReleaseCallback.accept(browser);
                            browserClosed = true;
                            log.debug("[会话] Browser已归还到池 - 会话ID: {}", sessionId);
                        } else {
                            // 旧逻辑：直接关闭（兼容性）
                            if (browser.isConnected()) {
                                browser.close();
                            }
                            browserClosed = true;
                        }
                    } catch (Exception e) {
                        log.warn("[会话] Browser归还/关闭失败 - 会话ID: {}, 错误: {}", sessionId, e.getMessage());
                    }
                }
                
                long duration = System.currentTimeMillis() - startTime;
                log.info("[会话] 销毁完成 - 会话ID: {}, 页面: {}/{}, 上下文: {}, 浏览器: {}, 耗时: {}ms", 
                    sessionId, closedPages, pageCount, contextClosed ? "已关闭" : "未关闭", 
                    browser != null ? (browserClosed ? "已关闭" : "未关闭") : "无", duration);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[会话] 销毁被中断 - 会话ID: {}", sessionId);
            } catch (Exception e) {
                log.error("[会话] 销毁异常 - 会话ID: {}, 错误类型: {}, 错误信息: {}", 
                    sessionId, e.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    /**
     * 检查是否已关闭，如果已关闭则抛出异常
     */
    private void checkNotClosed() {
        if (closed.get()) {
            throw new IllegalStateException("BrowserSession 已关闭: " + sessionId);
        }
    }

    // ==================== Getters ====================

    public String getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public BrowserContext getContext() {
        checkNotClosed();
        return context;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public boolean isHeadless() {
        return headless;
    }
    
    public String getInstanceId() {
        return instanceId;
    }
    
    public Browser getBrowser() {
        return browser;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public long getLastActiveTime() {
        return lastActiveTime.get();
    }

    public boolean isClosed() {
        return closed.get();
    }

    public boolean isInUse() {
        return inUse.get();
    }

    public String getCurrentTask() {
        return currentTask.get();
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }
    
    /**
     * 设置页面关闭回调（用于自动清理页面锁）
     * 
     * @param onPageClose 页面关闭时的回调，参数为被关闭的Page对象
     */
    public void setOnPageClose(java.util.function.Consumer<Page> onPageClose) {
        this.onPageClose = onPageClose;
    }
    
    /**
     * 设置Browser归还回调
     */
    public void setBrowserReleaseCallback(java.util.function.Consumer<Browser> callback) {
        this.browserReleaseCallback = callback;
    }

    /**
     * 获取会话运行时长（毫秒）
     */
    public long getRunningTime() {
        return System.currentTimeMillis() - createTime.toEpochMilli();
    }

    /**
     * 获取空闲时长（毫秒）
     */
    public long getIdleTime() {
        return System.currentTimeMillis() - lastActiveTime.get();
    }
    
    /**
     * 获取创建的页面数
     */
    public int getPageCreatedCount() {
        return pageCreatedCount.get();
    }
    
    /**
     * 获取关闭的页面数
     */
    public int getPageClosedCount() {
        return pageClosedCount.get();
    }
    
    /**
     * 获取资源泄漏检测信息
     * 如果创建的页面数 > 关闭的页面数，可能存在泄漏
     */
    public String getResourceLeakInfo() {
        int created = pageCreatedCount.get();
        int closed = pageClosedCount.get();
        int currentPages = 0;
        try {
            currentPages = context.pages().size();
        } catch (Exception e) {
            log.debug("[会话] 获取当前页面数失败 - 会话ID: {}, 错误: {}", sessionId, e.getMessage());
        }
        
        return String.format("创建:%d, 关闭:%d, 当前:%d", created, closed, currentPages);
    }

    @Override
    public String toString() {
        return "BrowserSession{" +
                "sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", persistent=" + persistent +
                ", headless=" + headless +
                ", inUse=" + inUse.get() +
                ", instanceId='" + instanceId + '\'' +
                ", closed=" + closed.get() +
                ", hasBrowser=" + (browser != null) +
                ", runningTime=" + getRunningTime() + "ms" +
                ", idleTime=" + getIdleTime() + "ms" +
                ", pages=" + getResourceLeakInfo() +
                '}';
    }
}
