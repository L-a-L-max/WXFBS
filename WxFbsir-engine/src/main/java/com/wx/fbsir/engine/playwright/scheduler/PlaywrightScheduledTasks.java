package com.wx.fbsir.engine.playwright.scheduler;

import com.wx.fbsir.engine.playwright.config.PlaywrightProperties;
import com.wx.fbsir.engine.playwright.core.PlaywrightManager;
import com.wx.fbsir.engine.playwright.util.ScreenshotUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Playwright 定时任务
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 核心职责
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 1. 定期清理过期截图文件
 * 2. 定期清理僵尸进程
 * 3. 定期清理浏览器锁文件
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 解决老项目问题
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * - 截图文件堆积：定期清理超过24小时的截图
 * - 僵尸进程：定期检测并清理
 * - 锁文件残留：定期清理
 * 
 * @author wxfbsir
 * @date 2025-12-18
 */
@Component
public class PlaywrightScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightScheduledTasks.class);

    private final PlaywrightProperties properties;
    private final PlaywrightManager playwrightManager;
    private final ScreenshotUtil screenshotUtil;

    public PlaywrightScheduledTasks(PlaywrightProperties properties,
                                     PlaywrightManager playwrightManager,
                                     ScreenshotUtil screenshotUtil) {
        this.properties = properties;
        this.playwrightManager = playwrightManager;
        this.screenshotUtil = screenshotUtil;
    }

    /**
     * 清理过期截图（每小时执行）
     */
    @Scheduled(fixedRate = 3600000)
    public void cleanupOldScreenshots() {
        if (!properties.isEnabled()) {
            return;
        }
        
        try {
            int cleaned = screenshotUtil.cleanupOldScreenshots(24);
            if (cleaned > 0) {
                log.info("[定时任务] 清理过期截图完成 - 清理: {} 个文件", cleaned);
            }
        } catch (Exception e) {
            log.error("[定时任务] 清理截图失败 - 错误类型: {}, 错误: {}", 
                e.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * 清理僵尸进程（每10分钟执行）
     */
    @Scheduled(fixedRate = 600000)
    public void cleanupZombieProcesses() {
        if (!properties.isEnabled()) {
            return;
        }
        
        try {
            playwrightManager.cleanupZombieProcesses();
        } catch (Exception e) {
            log.error("[定时任务] 清理僵尸进程失败 - 错误类型: {}, 错误: {}", 
                e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
