package com.wx.fbsir.engine.playwright.util;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 截图工具类
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 核心职责
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 1. 提供页面截图功能
 * 2. 支持全页面截图和元素截图
 * 3. 支持保存到文件或返回 Base64
 * 4. 支持截图命名和目录管理
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 使用方式
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * ```java
 * @Autowired
 * private ScreenshotUtil screenshotUtil;
 * 
 * // 截图保存到文件
 * Path path = screenshotUtil.capture(page, "login-qrcode");
 * 
 * // 截图返回 Base64
 * String base64 = screenshotUtil.captureAsBase64(page);
 * 
 * // 截取元素
 * String base64 = screenshotUtil.captureElementAsBase64(page, "#qrcode");
 * ```
 * 
 * @author wxfbsir
 * @date 2025-12-16
 */
@Component
public class ScreenshotUtil {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);

    /**
     * 默认截图保存目录
     */
    private static final String DEFAULT_SCREENSHOT_DIR = System.getProperty("java.io.tmpdir") + "/playwright-screenshots";

    /**
     * 日期时间格式化器
     */
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    /**
     * 每个页面的截图操作锁（解决多线程截图冲突）
     */
    private final ConcurrentHashMap<String, ReentrantLock> pageLocks = new ConcurrentHashMap<>();
    
    /**
     * 锁等待超时时间（秒）
     */
    private static final int LOCK_TIMEOUT_SECONDS = 30;

    /**
     * 截取页面并保存到文件
     * 
     * @param page Playwright 页面
     * @param name 截图名称（不含扩展名）
     * @return 截图文件路径
     */
    public Path capture(Page page, String name) {
        return capture(page, name, false);
    }

    /**
     * 截取页面并保存到文件（线程安全）
     * 
     * @param page Playwright 页面
     * @param name 截图名称（不含扩展名）
     * @param fullPage 是否截取整个页面（包括滚动区域）
     * @return 截图文件路径
     */
    public Path capture(Page page, String name, boolean fullPage) {
        if (page == null) {
            log.error("[截图] 保存失败 - Page 为 null");
            throw new IllegalArgumentException("[截图] Page 不能为 null");
        }
        if (name == null || name.isEmpty()) {
            log.error("[截图] 保存失败 - 名称为空");
            throw new IllegalArgumentException("[截图] 名称不能为空");
        }
        
        String pageId = getPageId(page);
        ReentrantLock lock = getPageLock(pageId);
        
        try {
            if (!lock.tryLock(LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                log.error("[截图] 保存失败 - 获取锁超时 ({}s)，可能存在截图冲突", LOCK_TIMEOUT_SECONDS);
                throw new RuntimeException("[截图] 获取截图锁超时");
            }
            
            try {
                // 确保目录存在
                Path dir = Paths.get(DEFAULT_SCREENSHOT_DIR);
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir);
                    log.debug("[截图] 创建截图目录: {}", dir);
                }

                // 生成文件名
                String timestamp = LocalDateTime.now().format(DATETIME_FORMATTER);
                String filename = String.format("%s_%s.png", name, timestamp);
                Path filePath = dir.resolve(filename);

                // 截图
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(filePath)
                        .setFullPage(fullPage)
                        .setType(ScreenshotType.PNG));

                log.debug("[截图] 保存成功 - 页面: {}, 文件: {}", pageId, filePath);
                return filePath;
            } catch (com.microsoft.playwright.TimeoutError e) {
                log.error("[截图] 截图超时 - 页面: {}, 名称: {}, 错误: {}", pageId, name, e.getMessage());
                throw new RuntimeException("[截图] 截图超时", e);
            } catch (IOException e) {
                log.error("[截图] IO异常 - 页面: {}, 名称: {}, 错误: {}", pageId, name, e.getMessage());
                throw new RuntimeException("[截图] 文件保存失败", e);
            } catch (Exception e) {
                log.error("[截图] 保存失败 - 页面: {}, 名称: {}, 错误类型: {}, 错误: {}", 
                    pageId, name, e.getClass().getSimpleName(), e.getMessage());
                throw new RuntimeException("[截图] 截图保存失败", e);
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[截图] 截图被中断 - 页面: {}, 名称: {}", pageId, name);
            throw new RuntimeException("[截图] 截图操作被中断", e);
        }
    }

    /**
     * 截取页面并返回 Base64 编码
     * 
     * @param page Playwright 页面
     * @return Base64 编码的图片
     */
    public String captureAsBase64(Page page) {
        return captureAsBase64(page, false);
    }

    /**
     * 截取页面并返回 Base64 编码（线程安全）
     * 
     * @param page Playwright 页面
     * @param fullPage 是否截取整个页面
     * @return Base64 编码的图片
     */
    public String captureAsBase64(Page page, boolean fullPage) {
        if (page == null) {
            log.error("[截图] Base64截图失败 - Page 为 null");
            throw new IllegalArgumentException("[截图] Page 不能为 null");
        }
        
        String pageId = getPageId(page);
        ReentrantLock lock = getPageLock(pageId);
        
        try {
            if (!lock.tryLock(LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                log.error("[截图] Base64截图失败 - 获取锁超时 ({}s)", LOCK_TIMEOUT_SECONDS);
                throw new RuntimeException("[截图] 获取截图锁超时");
            }
            
            try {
                byte[] bytes = page.screenshot(new Page.ScreenshotOptions()
                        .setFullPage(fullPage)
                        .setType(ScreenshotType.PNG));
                String base64 = Base64.getEncoder().encodeToString(bytes);
                log.debug("[截图] Base64生成成功 - 页面: {}, 大小: {} 字节", pageId, bytes.length);
                return base64;
            } catch (com.microsoft.playwright.TimeoutError e) {
                log.error("[截图] Base64截图超时 - 页面: {}, 错误: {}", pageId, e.getMessage());
                throw new RuntimeException("[截图] 截图超时", e);
            } catch (Exception e) {
                log.error("[截图] Base64生成失败 - 页面: {}, 错误类型: {}, 错误: {}", 
                    pageId, e.getClass().getSimpleName(), e.getMessage());
                throw new RuntimeException("[截图] 截图生成失败", e);
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[截图] Base64截图被中断 - 页面: {}", pageId);
            throw new RuntimeException("[截图] 截图操作被中断", e);
        }
    }

    /**
     * 截取页面并返回 Data URL（可直接用于 img src）
     * 
     * @param page Playwright 页面
     * @return Data URL 格式的图片
     */
    public String captureAsDataUrl(Page page) {
        String base64 = captureAsBase64(page);
        return "data:image/png;base64," + base64;
    }

    /**
     * 截取指定元素（线程安全）
     * 
     * @param page Playwright 页面
     * @param selector 元素选择器
     * @param name 截图名称
     * @return 截图文件路径
     */
    public Path captureElement(Page page, String selector, String name) {
        if (page == null) {
            log.error("[截图] 元素截图失败 - Page 为 null");
            throw new IllegalArgumentException("[截图] Page 不能为 null");
        }
        if (selector == null || selector.isEmpty()) {
            log.error("[截图] 元素截图失败 - 选择器为空");
            throw new IllegalArgumentException("[截图] 选择器不能为空");
        }
        
        String pageId = getPageId(page);
        ReentrantLock lock = getPageLock(pageId);
        
        try {
            if (!lock.tryLock(LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                log.error("[截图] 元素截图失败 - 获取锁超时 ({}s)", LOCK_TIMEOUT_SECONDS);
                throw new RuntimeException("[截图] 获取截图锁超时");
            }
            
            try {
                Path dir = Paths.get(DEFAULT_SCREENSHOT_DIR);
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir);
                }

                String timestamp = LocalDateTime.now().format(DATETIME_FORMATTER);
                String filename = String.format("%s_%s.png", name, timestamp);
                Path filePath = dir.resolve(filename);

                page.locator(selector).screenshot(new com.microsoft.playwright.Locator.ScreenshotOptions()
                        .setPath(filePath)
                        .setType(ScreenshotType.PNG));

                log.debug("[截图] 元素截图成功 - 页面: {}, 选择器: {}, 文件: {}", pageId, selector, filePath);
                return filePath;
            } catch (com.microsoft.playwright.TimeoutError e) {
                log.error("[截图] 元素截图超时 - 页面: {}, 选择器: {}, 错误: {}", pageId, selector, e.getMessage());
                throw new RuntimeException("[截图] 元素截图超时", e);
            } catch (Exception e) {
                log.error("[截图] 元素截图失败 - 页面: {}, 选择器: {}, 错误类型: {}, 错误: {}", 
                    pageId, selector, e.getClass().getSimpleName(), e.getMessage());
                throw new RuntimeException("[截图] 元素截图失败", e);
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[截图] 元素截图被中断 - 页面: {}, 选择器: {}", pageId, selector);
            throw new RuntimeException("[截图] 截图操作被中断", e);
        }
    }

    /**
     * 截取指定元素并返回 Base64（线程安全）
     * 
     * @param page Playwright 页面
     * @param selector 元素选择器
     * @return Base64 编码的图片
     */
    public String captureElementAsBase64(Page page, String selector) {
        if (page == null) {
            log.error("[截图] 元素Base64截图失败 - Page 为 null");
            throw new IllegalArgumentException("[截图] Page 不能为 null");
        }
        if (selector == null || selector.isEmpty()) {
            log.error("[截图] 元素Base64截图失败 - 选择器为空");
            throw new IllegalArgumentException("[截图] 选择器不能为空");
        }
        
        String pageId = getPageId(page);
        ReentrantLock lock = getPageLock(pageId);
        
        try {
            if (!lock.tryLock(LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                log.error("[截图] 元素Base64截图失败 - 获取锁超时 ({}s)", LOCK_TIMEOUT_SECONDS);
                throw new RuntimeException("[截图] 获取截图锁超时");
            }
            
            try {
                byte[] bytes = page.locator(selector).screenshot(
                        new com.microsoft.playwright.Locator.ScreenshotOptions()
                                .setType(ScreenshotType.PNG));
                String base64 = Base64.getEncoder().encodeToString(bytes);
                log.debug("[截图] 元素Base64截图成功 - 页面: {}, 选择器: {}, 大小: {} 字节", 
                    pageId, selector, bytes.length);
                return base64;
            } catch (com.microsoft.playwright.TimeoutError e) {
                log.error("[截图] 元素Base64截图超时 - 页面: {}, 选择器: {}, 错误: {}", 
                    pageId, selector, e.getMessage());
                throw new RuntimeException("[截图] 元素截图超时", e);
            } catch (Exception e) {
                log.error("[截图] 元素Base64截图失败 - 页面: {}, 选择器: {}, 错误类型: {}, 错误: {}", 
                    pageId, selector, e.getClass().getSimpleName(), e.getMessage());
                throw new RuntimeException("[截图] 元素截图失败", e);
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[截图] 元素Base64截图被中断 - 页面: {}, 选择器: {}", pageId, selector);
            throw new RuntimeException("[截图] 截图操作被中断", e);
        }
    }

    /**
     * 截取指定元素并返回 Data URL
     * 
     * @param page Playwright 页面
     * @param selector 元素选择器
     * @return Data URL 格式的图片
     */
    public String captureElementAsDataUrl(Page page, String selector) {
        String base64 = captureElementAsBase64(page, selector);
        return "data:image/png;base64," + base64;
    }

    /**
     * 截取二维码区域（常用于登录场景，线程安全）
     * 
     * @param page Playwright 页面
     * @param qrSelector 二维码元素选择器
     * @return Base64 编码的二维码图片
     */
    public String captureQrCode(Page page, String qrSelector) {
        if (page == null) {
            log.error("[截图] 二维码截图失败 - Page 为 null");
            throw new IllegalArgumentException("[截图] Page 不能为 null");
        }
        if (qrSelector == null || qrSelector.isEmpty()) {
            log.error("[截图] 二维码截图失败 - 选择器为空");
            throw new IllegalArgumentException("[截图] 选择器不能为空");
        }
        
        String pageId = getPageId(page);
        
        try {
            // 等待二维码元素出现
            page.locator(qrSelector).waitFor();
            String result = captureElementAsBase64(page, qrSelector);
            log.debug("[截图] 二维码截图成功 - 页面: {}, 选择器: {}", pageId, qrSelector);
            return result;
        } catch (com.microsoft.playwright.TimeoutError e) {
            log.warn("[截图] 二维码元素等待超时 - 页面: {}, 选择器: {}, 尝试全页面截图", pageId, qrSelector);
            // 降级为全页面截图
            return captureAsBase64(page);
        } catch (Exception e) {
            log.warn("[截图] 二维码截图失败 - 页面: {}, 选择器: {}, 错误: {}，尝试全页面截图", 
                pageId, qrSelector, e.getMessage());
            // 降级为全页面截图
            return captureAsBase64(page);
        }
    }

    /**
     * 清理过期的截图文件
     * 
     * @param maxAgeHours 最大保留时间（小时）
     * @return 清理的文件数量
     */
    public int cleanupOldScreenshots(int maxAgeHours) {
        if (maxAgeHours <= 0) {
            log.warn("[截图] 清理参数无效 - maxAgeHours: {}", maxAgeHours);
            return 0;
        }
        
        int cleaned = 0;
        int failed = 0;
        try {
            Path dir = Paths.get(DEFAULT_SCREENSHOT_DIR);
            if (!Files.exists(dir)) {
                log.debug("[截图] 截图目录不存在，跳过清理");
                return 0;
            }

            long cutoffTime = System.currentTimeMillis() - (maxAgeHours * 3600 * 1000L);

            for (Path file : Files.newDirectoryStream(dir, "*.png")) {
                try {
                    if (Files.getLastModifiedTime(file).toMillis() < cutoffTime) {
                        Files.delete(file);
                        cleaned++;
                    }
                } catch (IOException e) {
                    failed++;
                    log.debug("[截图] 删除文件失败 - 文件: {}, 错误: {}", file.getFileName(), e.getMessage());
                }
            }

            if (cleaned > 0 || failed > 0) {
                log.info("[截图] 清理完成 - 成功: {} 个, 失败: {} 个", cleaned, failed);
            }
        } catch (Exception e) {
            log.error("[截图] 清理失败 - 错误类型: {}, 错误: {}", e.getClass().getSimpleName(), e.getMessage());
        }
        return cleaned;
    }

    /**
     * 获取截图保存目录
     */
    public String getScreenshotDir() {
        return DEFAULT_SCREENSHOT_DIR;
    }
    
    /**
     * 获取页面的唯一标识
     */
    private String getPageId(Page page) {
        try {
            return String.valueOf(System.identityHashCode(page));
        } catch (Exception e) {
            return "unknown";
        }
    }
    
    /**
     * 获取或创建页面的截图锁
     */
    private ReentrantLock getPageLock(String pageId) {
        return pageLocks.computeIfAbsent(pageId, k -> new ReentrantLock());
    }
    
    /**
     * 清理页面锁（当页面关闭时调用）
     * 
     * @param page Playwright 页面
     */
    public void cleanupPageLock(Page page) {
        if (page != null) {
            String pageId = getPageId(page);
            ReentrantLock removed = pageLocks.remove(pageId);
            if (removed != null) {
                log.debug("[截图] 清理页面锁 - 页面: {}", pageId);
            }
        }
    }
    
    /**
     * 获取当前持有的锁数量（用于监控）
     */
    public int getLockCount() {
        return pageLocks.size();
    }
    
    /**
     * 清理所有页面锁（在应用关闭时调用）
     */
    public void clearAllLocks() {
        int count = pageLocks.size();
        pageLocks.clear();
        if (count > 0) {
            log.info("[截图] 清理所有页面锁 - 数量: {}", count);
        }
    }
    
    /**
     * 检查是否存在锁泄漏（用于监控）
     * @param maxExpectedLocks 最大预期锁数量
     * @return true 如果可能存在泄漏
     */
    public boolean hasLockLeak(int maxExpectedLocks) {
        int currentLocks = pageLocks.size();
        if (currentLocks > maxExpectedLocks) {
            log.warn("[截图] 可能存在锁泄漏 - 当前锁数: {}, 最大预期: {}", currentLocks, maxExpectedLocks);
            return true;
        }
        return false;
    }
}
