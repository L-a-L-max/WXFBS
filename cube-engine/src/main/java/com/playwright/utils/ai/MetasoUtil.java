package com.playwright.utils.ai;

import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.playwright.entity.UserInfoRequest;
import com.playwright.entity.mcp.McpResult;
import com.playwright.utils.common.AiResultHelper;
import com.playwright.utils.common.ClipboardLockManager;
import com.playwright.utils.common.LogMsgUtil;
import com.playwright.websocket.WebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.ViewportSize;
import com.playwright.utils.common.ScreenshotUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class MetasoUtil {

    @Autowired
    private LogMsgUtil logInfo;

    @Autowired
    private ClipboardLockManager clipboardLockManager;

    @Autowired
    private WebSocketClientService webSocketClientService;

    @Autowired
    private ScreenshotUtil screenshotUtil;
    
    @Autowired
    private AiResultHelper aiResultHelper;

    //    检查登录
    public String checkLogin(Page page, String userId) throws Exception {
        // 检查页面是否已关闭
        if (page.isClosed()) {
            throw new RuntimeException("页面已关闭，无法检查登录状态");
        }

        try {
        Locator loginLocator = page.locator("//button[contains(text(),'登录/注册')]");
            if (!loginLocator.isVisible()) {
            String userName = page.locator("(//span[@class='MuiTypography-root MuiTypography-body1 css-1tyjpe7'])[1]").textContent();
            JSONObject jsonObjectTwo = new JSONObject();
                jsonObjectTwo.put("status", userName);
                jsonObjectTwo.put("userId", userId);
                jsonObjectTwo.put("type", "RETURN_METASO_STATUS");
            webSocketClientService.sendMessage(jsonObjectTwo.toJSONString());
            return userName;
        }
        return null;
        } catch (com.microsoft.playwright.impl.TargetClosedError e) {
            throw new RuntimeException("页面目标已关闭", e);
        } catch (TimeoutError e) {
            throw new RuntimeException("检查登录状态超时", e);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 监控Metaso回答并提取HTML内容
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param aiName AI名称
     * @return 提取的HTML内容
     */
    public String waitMetasoHtmlDom(Page page, String userId, String aiName, UserInfoRequest userInfoRequest) throws Exception {
        // 检查页面是否已关闭
        if (page.isClosed()) {
            throw new RuntimeException("页面已关闭，无法等待Metaso回答");
        }

        try {
            String currentContent = "";
            String lastContent = "";
            String textContent = "";
            long timeout = 270000; //  4.5分钟超时设置 (延长50%: 180000 -> 270000)
            long startTime = System.currentTimeMillis();

            while (true) {
                // 检查页面是否已关闭
                if (page.isClosed()) {
                    throw new RuntimeException("页面在等待回答过程中已关闭");
                }

                // 检查超时
                if (System.currentTimeMillis() - startTime > timeout) {
                    break;
                }

                // 搜索额度用尽弹窗判断
                if (page.getByText("今日搜索额度已用尽").isVisible()) {
                    return "今日搜索额度已用尽";
                }

                // 获取最新回答内容
                Locator contentLocator = page.locator("div.MuiBox-root .markdown-body").last();
                // 设置 20 分钟超时时间获取 innerHTML
                currentContent = contentLocator.innerHTML(new Locator.InnerHTMLOptions()
                        .setTimeout(1800000) // 30分钟 = 1800000毫秒 (延长50%: 1200000 -> 1800000)
                );
                textContent = contentLocator.textContent();
                
                
                // 内容稳定且已完成回答时退出循环
                if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
                    webSocketClientService.sendMessage(userInfoRequest, McpResult.success(textContent, ""), userInfoRequest.getAiName());
                }
                
                if (!currentContent.isEmpty() && currentContent.equals(lastContent)) {
                    logInfo.sendTaskLog(aiName + "回答完成，正在提取内容", userId, aiName);
                    break;
                }
                lastContent = currentContent;
                page.waitForTimeout(2000); // 2秒检查一次
            }
            
            logInfo.sendTaskLog(aiName + "内容已提取完成", userId, aiName);
            
            if (userInfoRequest.getAiName() != null && userInfoRequest.getAiName().contains("stream")) {
                webSocketClientService.sendMessage(userInfoRequest, McpResult.success("END", ""), userInfoRequest.getAiName());
            }
            
            return currentContent;
            
        } catch (com.microsoft.playwright.impl.TargetClosedError e) {
            throw new RuntimeException("页面目标在等待Metaso回答时已关闭", e);
        } catch (TimeoutError e) {
            throw new RuntimeException("等待Metaso回答超时", e);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 安全获取秘塔分享链接
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param aiName AI名称
     * @return 分享链接
     */
    public String getMetasoShareUrlSafely(Page page, String userId, String aiName) {
        try {
            // 检查页面是否已关闭
            if (page.isClosed()) {
                logInfo.sendTaskLog("页面已关闭，无法获取分享链接", userId, aiName);
                return null;
            }

            // 🔥 多策略复制链接按钮选择器
            String[] shareButtonSelectors = {
                // 基于角色和文本的选择器（最稳定）
                "button:has-text('复制链接')",
                "[role='button']:has-text('复制链接')", 
                "//button[contains(text(),'复制')]",
                
                // 基于SVG图标的选择器
                "//svg[contains(@class,'share') or contains(@class,'copy')]//ancestor::button",
                "//use[contains(@xlink:href,'share') or contains(@xlink:href,'copy')]//ancestor::*[@role='button' or local-name()='button']",
                
                // 基于位置的选择器（作为备用）
                "(//*[name()='svg'])[26]",
                "(//button[@type='button'])[24]",
                
                // 通过DOM结构定位
                "//div[contains(@class,'toolbar') or contains(@class,'action')]//button[last()]",
                "//div[contains(@class,'option') or contains(@class,'menu')]//button[contains(@class,'copy') or contains(text(),'复制')]"
            };

            AtomicReference<String> shareUrlRef = new AtomicReference<>();
            boolean clickSuccess = false;

            // 策略1：尝试所有选择器进行复制链接操作
            for (int i = 0; i < shareButtonSelectors.length && !clickSuccess; i++) {
                try {
                    String selector = shareButtonSelectors[i];
                    Locator shareButton = page.locator(selector);
                    
                    if (shareButton.count() > 0) {
                        // 等待按钮可见并点击
                        shareButton.waitFor(new Locator.WaitForOptions()
                            .setTimeout(5000)
                            .setState(WaitForSelectorState.VISIBLE));
                        
                        // 🔒 使用剪贴板锁保护剪贴板操作
                        final int selectorIndex = i;
                        clipboardLockManager.runWithClipboardLock(() -> {
                            try {
                                shareButton.click();
                                Thread.sleep(1000);
                                
                                // 从剪贴板读取链接
                                String url = (String) page.evaluate("navigator.clipboard.readText()");
                                if (url != null && url.contains("http")) {
                                    shareUrlRef.set(url);
                                    logInfo.sendTaskLog("通过选择器 " + (selectorIndex+1) + " 成功获取分享链接: " + url, userId, aiName);
                                }
                            } catch (Exception clipEx) {
                                logInfo.sendTaskLog("第 " + (selectorIndex+1) + " 个选择器点击成功但读取剪贴板失败", userId, aiName);
                            }
                        });
                        
                        String shareUrl = shareUrlRef.get();
                        if (shareUrl != null && shareUrl.contains("http")) {
                            clickSuccess = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    // 继续尝试下一个选择器
                    if (i == shareButtonSelectors.length - 1) {
                        logInfo.sendTaskLog("所有复制链接选择器都失败", userId, aiName);
                    }
                }
            }
            
            String shareUrl = shareUrlRef.get();

            // 策略2：如果复制链接失败，尝试从URL或页面中直接提取
            if (!clickSuccess || shareUrl == null || !shareUrl.contains("http")) {
                logInfo.sendTaskLog("复制链接失败，尝试从页面直接获取链接", userId, aiName);
                
                // 方法2.1：从当前页面URL中提取或构建分享链接
                String currentUrl = page.url();
                if (currentUrl.contains("metaso.cn")) {
                    // 构建标准的秘塔分享链接格式
                    if (currentUrl.contains("/search/")) {
                        shareUrl = currentUrl;
                        logInfo.sendTaskLog("从页面URL获取分享链接: " + shareUrl, userId, aiName);
                    }
                }
                
                // 方法2.2：搜索页面中是否有分享链接元素
                if (shareUrl == null || !shareUrl.contains("http")) {
                    try {
                        Locator linkElements = page.locator("a[href*='metaso.cn'], input[value*='metaso.cn'], span:has-text('http')");
                        if (linkElements.count() > 0) {
                            for (int i = 0; i < linkElements.count(); i++) {
                                try {
                                    String linkText = linkElements.nth(i).textContent();
                                    String linkHref = linkElements.nth(i).getAttribute("href");
                                    String linkValue = linkElements.nth(i).getAttribute("value");
                                    
                                    String potentialUrl = linkHref != null ? linkHref : 
                                                         linkValue != null ? linkValue : linkText;
                                    
                                    if (potentialUrl != null && potentialUrl.contains("http") && potentialUrl.contains("metaso")) {
                                        shareUrl = potentialUrl;
                                        logInfo.sendTaskLog("从页面元素中找到分享链接: " + shareUrl, userId, aiName);
                                        break;
                                    }
                                } catch (Exception ex) {
                                    continue;
                                }
                            }
                        }
                    } catch (Exception e) {
                        logInfo.sendTaskLog("搜索页面链接元素失败: " + e.getMessage(), userId, aiName);
                    }
                }
                
                // 方法2.3：最后备用方案 - 使用当前URL作为分享链接
                if (shareUrl == null || !shareUrl.contains("http")) {
                    shareUrl = currentUrl;
                    logInfo.sendTaskLog("使用当前页面URL作为分享链接: " + shareUrl, userId, aiName);
                }
            }

            // 🔥 新增：清理URL，只保留数字ID部分（支持 /search/ 和 /search-v2/）
            if (shareUrl != null && (shareUrl.contains("metaso.cn/search/") || shareUrl.contains("metaso.cn/search-v2/"))) {
                shareUrl = cleanMetasoUrl(shareUrl);
                logInfo.sendTaskLog("已清理秘塔URL，保留数字ID: " + shareUrl, userId, aiName);
            }
            
            return shareUrl;

        } catch (Exception e) {
            logInfo.sendTaskLog("获取秘塔分享链接时发生异常: " + e.getMessage(), userId, aiName);
            // 返回当前页面URL作为备用
            try {
                String backupUrl = page.url();
                if (backupUrl != null && (backupUrl.contains("metaso.cn/search/") || backupUrl.contains("metaso.cn/search-v2/"))) {
                    backupUrl = cleanMetasoUrl(backupUrl);
                }
                return backupUrl;
            } catch (Exception urlEx) {
                return null;
            }
        }
    }

    /**
     * 清理秘塔URL，只保留数字ID部分
     * 例如：https://metaso.cn/search-v2/8646763915575853056?q=xxx -> https://metaso.cn/search-v2/8646763915575853056
     * @param url 原始URL
     * @return 清理后的URL
     */
    private String cleanMetasoUrl(String url) {
        if (url == null || (!url.contains("metaso.cn/search/") && !url.contains("metaso.cn/search-v2/"))) {
            return url;
        }
        
        try {
            // 查找数字ID的位置（支持 /search/ 和 /search-v2/）
            int searchIndex = url.indexOf("metaso.cn/search-v2/");
            String searchPath = "metaso.cn/search-v2/";
            
            if (searchIndex == -1) {
                searchIndex = url.indexOf("metaso.cn/search/");
                searchPath = "metaso.cn/search/";
            }
            
            if (searchIndex == -1) {
                return url;
            }
            
            // 提取基础路径
            String basePath = url.substring(0, searchIndex + searchPath.length());
            
            // 提取数字ID部分
            String remaining = url.substring(searchIndex + searchPath.length());
            
            // 查找第一个非数字字符的位置（通常是?或#）
            int endIndex = 0;
            for (int i = 0; i < remaining.length(); i++) {
                char c = remaining.charAt(i);
                if (!Character.isDigit(c)) {
                    endIndex = i;
                    break;
                }
            }
            
            // 如果全部都是数字，则保留全部
            if (endIndex == 0) {
                endIndex = remaining.length();
            }
            
            String numberId = remaining.substring(0, endIndex);
            return basePath + numberId;
            
        } catch (Exception e) {
            // 如果解析失败，返回原URL
            return url;
        }
    }

    /**
     * 点击复制按钮并获取内容（从剪贴板）
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param aiName AI名称
     * @return 复制的内容
     */
    public String clickCopyButtonAndGetContent(Page page, String userId, String aiName) {
        try {
            // 检查页面是否已关闭
            if (page.isClosed()) {
                logInfo.sendTaskLog("页面已关闭，无法点击复制按钮", userId, aiName);
                return "";
            }

            // 定位复制按钮 - 在 <div class="flex gap-2! mt-3 items-center"> 中的第一个按钮
            // 根据用户提供的DOM结构，复制按钮是第一个按钮，包含SVG图标
            String[] copyButtonSelectors = {
                // 策略1: 通过父容器定位第一个按钮
                "div.flex.gap-2\\!.mt-3.items-center > button:first-child",
                "div[class*='flex'][class*='gap-2'][class*='mt-3'] > button:first-child",
                // 策略2: 通过SVG图标定位（复制按钮的SVG包含特定的path）
                "button:has(svg path[d*='M7.5 3h7.1'])",
                "//div[contains(@class,'flex') and contains(@class,'gap-2') and contains(@class,'mt-3')]//button[1]",
                // 策略3: 通过search-content-container定位
                "div[id^='search-content-container'] ~ div button:first-child",
                "//div[starts-with(@id,'search-content-container')]/following-sibling::div//div[contains(@class,'flex') and contains(@class,'gap-2')]//button[1]"
            };

            AtomicReference<String> contentRef = new AtomicReference<>("");

            // 尝试所有选择器
            for (int i = 0; i < copyButtonSelectors.length; i++) {
                try {
                    Locator copyButton = page.locator(copyButtonSelectors[i]);
                    
                    if (copyButton.count() > 0) {
                        // 等待按钮可见
                        copyButton.waitFor(new Locator.WaitForOptions()
                            .setTimeout(5000)
                            .setState(WaitForSelectorState.VISIBLE));
                        
                        // 🔒 使用剪贴板锁保护剪贴板操作
                        final int selectorIndex = i;
                        clipboardLockManager.runWithClipboardLock(() -> {
                            try {
                                // 点击复制按钮
                                copyButton.click();
                                Thread.sleep(2000); // 等待剪贴板更新
                                
                                // 从剪贴板读取内容
                                String clipboardContent = (String) page.evaluate("navigator.clipboard.readText()");
                                
                                if (clipboardContent != null && !clipboardContent.trim().isEmpty()) {
                                    contentRef.set(clipboardContent);
                                    logInfo.sendTaskLog("通过选择器 " + (selectorIndex+1) + " 成功获取复制内容", userId, aiName);
                                } else {
                                    logInfo.sendTaskLog("剪贴板内容为空", userId, aiName);
                                }
                            } catch (Exception clipEx) {
                                logInfo.sendTaskLog("第 " + (selectorIndex+1) + " 个选择器点击成功但读取剪贴板失败: " + clipEx.getMessage(), userId, aiName);
                            }
                        });
                        
                        String content = contentRef.get();
                        if (content != null && !content.trim().isEmpty()) {
                            return content;
                        }
                    }
                } catch (Exception e) {
                    // 继续尝试下一个选择器
                    if (i == copyButtonSelectors.length - 1) {
                        logInfo.sendTaskLog("所有复制按钮选择器都失败", userId, aiName);
                    }
                }
            }
            
            // 如果所有选择器都失败，返回空字符串
            logInfo.sendTaskLog("无法找到复制按钮或获取内容失败", userId, aiName);
            return "";
            
        } catch (Exception e) {
            logInfo.sendTaskLog("点击复制按钮时发生异常: " + e.getMessage(), userId, aiName);
            return "";
        }
    }

    /**
     * 针对秘塔的内容区域进行拼接截图
     * 参考DeepSeek的实现，但针对秘塔的DOM结构
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param aiName AI名称
     * @return 截图URL
     */
    public String captureMetasoContentScreenshot(Page page, String userId, String aiName) {
        Path finalScreenshotPath = null;
        ViewportSize originalViewport = null;

        try {
            // 保存原始视口大小
            originalViewport = page.viewportSize();

            // 隐藏可能遮挡内容的固定元素
            hideFixedElements(page);

            // 查找包含提问和回答的完整容器
            @SuppressWarnings("unchecked")
            Map<String, Object> containerInfo = (Map<String, Object>) page.evaluate("""
                () => {
                    try {
                        // 查找所有search-content-container
                        const containers = document.querySelectorAll('div[id^="search-content-container"]');
                        if (containers.length === 0) {
                            return { success: false, message: 'no-containers-found' };
                        }
                        
                        // 获取最后一个容器（最新的回复）
                        const lastContainer = containers[containers.length - 1];
                        
                        // 向上查找包含提问标题的父容器
                        let mainContainer = lastContainer;
                        let parent = lastContainer.parentElement;
                        while (parent && parent !== document.body) {
                            // 检查是否包含提问标题
                            if (parent.querySelector('.search-title_result-title__Qtgg4, .resultTitle')) {
                                mainContainer = parent;
                                break;
                            }
                            // 检查是否是目标父容器（flex flex-col min-h-[calc(100vh-272px)]）
                            if (parent.classList && parent.classList.contains('flex') && 
                                parent.classList.contains('flex-col') &&
                                parent.className.includes('min-h-[calc(100vh-272px)]')) {
                                mainContainer = parent;
                                break;
                            }
                            parent = parent.parentElement;
                        }
                        
                        // 滚动到容器顶部，确保完全可见
                        mainContainer.scrollIntoView({ behavior: 'auto', block: 'start' });
                        
                        // 等待滚动完成
                        setTimeout(() => {}, 500);
                        
                        // 获取容器的完整尺寸信息（包括滚动内容）
                        const rect = mainContainer.getBoundingClientRect();
                        const scrollHeight = mainContainer.scrollHeight;
                        const scrollWidth = mainContainer.scrollWidth;
                        
                        // 确保获取完整的内容区域，添加适当边距
                        const padding = 20;
                        const bottomMargin = 50;
                        
                        return {
                            success: true,
                            x: Math.max(0, rect.x - padding),
                            y: Math.max(0, rect.y - padding),
                            width: Math.max(rect.width, scrollWidth) + padding * 2,
                            height: Math.max(rect.height, scrollHeight) + bottomMargin + padding,
                            scrollHeight: scrollHeight,
                            scrollWidth: scrollWidth,
                            containerCount: containers.length,
                            actualHeight: rect.height,
                            actualWidth: rect.width
                        };
                    } catch (e) {
                        return { success: false, message: e.toString() };
                    }
                }
            """);

            if (!Boolean.TRUE.equals(containerInfo.get("success"))) {
                System.err.println("查找search-content-container失败: " + containerInfo.get("message"));
                return captureFullPageScreenshot(page, userId, aiName);
            }

            // 使用完整容器截图方法
            return captureCompleteContainerScreenshot(page, containerInfo, originalViewport, userId, aiName);

        } catch (Exception e) {
            System.err.println("截取秘塔内容容器失败: " + e.getMessage());
            e.printStackTrace();
            return captureFullPageScreenshot(page, userId, aiName);
        } finally {
            // 恢复原始视口大小
            if (originalViewport != null) {
                page.setViewportSize(originalViewport.width, originalViewport.height);
            }
            // 恢复被隐藏的元素
            restoreFixedElements(page);
            // 清理临时文件
            if (finalScreenshotPath != null) {
                try {
                    Files.deleteIfExists(finalScreenshotPath);
                } catch (IOException e) {
                    System.err.println("清理临时文件失败: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 完整截图捕获整个容器
     */
    private String captureCompleteContainerScreenshot(Page page, Map<String, Object> containerInfo, 
            ViewportSize originalViewport, String userId, String aiName) {
        try {
            double containerX = getDoubleValue(containerInfo, "x");
            double containerY = getDoubleValue(containerInfo, "y");
            double containerWidth = getDoubleValue(containerInfo, "width");
            double containerHeight = getDoubleValue(containerInfo, "height");


            // 设置足够大的视口以容纳整个内容
            int viewportWidth = Math.max(1920, (int) Math.ceil(containerWidth) + 200);
            int viewportHeight = Math.max(1080, (int) Math.ceil(containerHeight) + 200);
            
            page.setViewportSize(viewportWidth, viewportHeight);
            page.waitForTimeout(800); // 等待视口调整完成

            // 确保容器完全可见并展开所有内容
            page.evaluate("""
                () => {
                    try {
                        const containers = document.querySelectorAll('div[id^="search-content-container"]');
                        if (containers.length > 0) {
                            const lastContainer = containers[containers.length - 1];
                            
                            // 向上查找包含提问标题的父容器
                            let mainContainer = lastContainer;
                            let parent = lastContainer.parentElement;
                            while (parent && parent !== document.body) {
                                if (parent.querySelector('.search-title_result-title__Qtgg4, .resultTitle') ||
                                    (parent.classList && parent.classList.contains('flex') && 
                                     parent.classList.contains('flex-col') &&
                                     parent.className.includes('min-h-[calc(100vh-272px)]'))) {
                                    mainContainer = parent;
                                    break;
                                }
                                parent = parent.parentElement;
                            }
                            
                            // 移除任何高度限制，确保内容完全展开
                            mainContainer.style.height = 'auto';
                            mainContainer.style.maxHeight = 'none';
                            mainContainer.style.overflow = 'visible';
                            
                            // 展开所有可能的折叠内容
                            const expandButtons = mainContainer.querySelectorAll('[data-testid="expand-button"], .expand-btn, .more-btn');
                            expandButtons.forEach(btn => {
                                try { btn.click(); } catch(e) {}
                            });
                            
                            // 滚动到容器顶部
                            mainContainer.scrollIntoView({ behavior: 'auto', block: 'start' });
                            
                            // 确保页面滚动到合适位置
                            const rect = mainContainer.getBoundingClientRect();
                            if (rect.top < 50) {
                                window.scrollBy(0, rect.top - 50);
                            }
                        }
                    } catch (e) {
                        console.error('展开容器内容失败:', e);
                    }
                }
            """);
            
            page.waitForTimeout(1000); // 等待内容展开和滚动完成

            // 重新获取展开后的容器尺寸
            @SuppressWarnings("unchecked")
            Map<String, Object> updatedContainerInfo = (Map<String, Object>) page.evaluate("""
                () => {
                    try {
                        const containers = document.querySelectorAll('div[id^="search-content-container"]');
                        if (containers.length === 0) return null;
                        
                        const lastContainer = containers[containers.length - 1];
                        
                        // 向上查找包含提问标题的父容器
                        let mainContainer = lastContainer;
                        let parent = lastContainer.parentElement;
                        while (parent && parent !== document.body) {
                            if (parent.querySelector('.search-title_result-title__Qtgg4, .resultTitle') ||
                                (parent.classList && parent.classList.contains('flex') && 
                                 parent.classList.contains('flex-col') &&
                                 parent.className.includes('min-h-[calc(100vh-272px)]'))) {
                                mainContainer = parent;
                                break;
                            }
                            parent = parent.parentElement;
                        }
                        
                        const rect = mainContainer.getBoundingClientRect();
                        
                        // 获取页面边界
                        const pageWidth = Math.max(document.documentElement.scrollWidth, window.innerWidth);
                        const pageHeight = Math.max(document.documentElement.scrollHeight, window.innerHeight);
                        
                        const padding = 20;
                        
                        return {
                            x: Math.max(0, rect.x - padding),
                            y: Math.max(0, rect.y - padding),
                            width: Math.min(rect.width + padding * 2, pageWidth),
                            height: Math.min(rect.height + padding * 2, pageHeight - rect.y + padding),
                            pageWidth: pageWidth,
                            pageHeight: pageHeight
                        };
                    } catch (e) {
                        return null;
                    }
                }
            """);

            if (updatedContainerInfo != null) {
                containerX = getDoubleValue(updatedContainerInfo, "x");
                containerY = getDoubleValue(updatedContainerInfo, "y");
                containerWidth = getDoubleValue(updatedContainerInfo, "width");
                containerHeight = getDoubleValue(updatedContainerInfo, "height");
            }

            // 验证截图参数
            if (containerWidth <= 0 || containerHeight <= 0) {
                System.err.println("截图参数无效，使用全屏截图");
                return captureFullPageScreenshot(page, userId, aiName);
            }


            // 创建截图路径
            Path screenshotPath = Paths.get(System.getProperty("java.io.tmpdir"),
                    "metaso_complete_container_" + UUID.randomUUID() + ".png");

            // 执行完整容器截图
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setClip(containerX, containerY, containerWidth, containerHeight));

            // 上传并获取URL
            String result = ScreenshotUtil.uploadFile(screenshotUtil.uploadUrl, screenshotPath.toString());
            JSONObject jsonObject = JSONObject.parseObject(result);
            String shareImgUrl = jsonObject.getString("url");

            // 清理临时文件
            Files.deleteIfExists(screenshotPath);

            return shareImgUrl;

        } catch (Exception e) {
            System.err.println("完整容器截图失败: " + e.getMessage());
            e.printStackTrace();
            return captureFullPageScreenshot(page, userId, aiName);
        }
    }

    /**
     * 隐藏可能遮挡内容的固定元素（如输入框）
     */
    private void hideFixedElements(Page page) {
        try {
            page.evaluate("""
                () => {
                    // 保存原始样式以便恢复
                    window._originalFixedElementStyles = {};
                    
                    // 查找所有可能遮挡内容的固定定位元素
                    const fixedElements = document.querySelectorAll('[class*="fixed"], [class*="sticky"], [style*="fixed"], [style*="sticky"]');
                    
                    fixedElements.forEach((el, index) => {
                        // 检查元素是否在底部（可能是输入框）
                        const rect = el.getBoundingClientRect();
                        if (rect.bottom > window.innerHeight - 100) { // 底部100像素内的元素
                            window._originalFixedElementStyles[`element_${index}`] = {
                                element: el,
                                display: el.style.display,
                                visibility: el.style.visibility,
                                position: el.style.position
                            };
                            
                            // 隐藏元素
                            el.style.display = 'none';
                            el.style.visibility = 'hidden';
                        }
                    });
                    
                    return Object.keys(window._originalFixedElementStyles).length;
                }
            """);
        } catch (Exception e) {
            System.err.println("隐藏固定元素失败: " + e.getMessage());
        }
    }

    /**
     * 恢复被隐藏的固定元素
     */
    private void restoreFixedElements(Page page) {
        try {
            page.evaluate("""
                () => {
                    if (window._originalFixedElementStyles) {
                        Object.values(window._originalFixedElementStyles).forEach(styleInfo => {
                            if (styleInfo.element && styleInfo.element.style) {
                                styleInfo.element.style.display = styleInfo.display;
                                styleInfo.element.style.visibility = styleInfo.visibility;
                                styleInfo.element.style.position = styleInfo.position;
                            }
                        });
                        delete window._originalFixedElementStyles;
                    }
                }
            """);
        } catch (Exception e) {
            System.err.println("恢复固定元素失败: " + e.getMessage());
        }
    }

    /**
     * 安全地从 Map 中获取 double 值，处理 Integer 和 Double 类型
     */
    private double getDoubleValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        double result = 0.0;
        
        if (value instanceof Integer) {
            result = ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            result = (Double) value;
        } else if (value instanceof Number) {
            result = ((Number) value).doubleValue();
        } else {
            throw new IllegalArgumentException("无法将值转换为 double: " + value);
        }
        
        // 检查并处理 NaN 和无穷大值
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            System.err.println("警告: 检测到无效数值 " + key + "=" + result + "，使用默认值 0.0");
            return 0.0;
        }
        
        return result;
    }

    /**
     * 全屏截图作为备用方案
     */
    private String captureFullPageScreenshot(Page page, String userId, String aiName) {
        Path screenshotPath = null;

        try {
            // 先隐藏可能遮挡内容的元素
            hideFixedElements(page);

            screenshotPath = Paths.get(System.getProperty("java.io.tmpdir"),
                    "metaso_fullpage_" + UUID.randomUUID() + ".png");

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(true));

            String result = ScreenshotUtil.uploadFile(screenshotUtil.uploadUrl, screenshotPath.toString());
            JSONObject jsonObject = JSONObject.parseObject(result);
            return jsonObject.getString("url");

        } catch (Exception e) {
            System.err.println("全屏截图也失败了: " + e.getMessage());
            return "";
        } finally {
            // 恢复被隐藏的元素
            restoreFixedElements(page);
            if (screenshotPath != null) {
                try {
                    Files.deleteIfExists(screenshotPath);
                } catch (IOException e) {
                    System.err.println("删除临时文件失败: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 获取右上角分享按钮并点击获取分享链接
     * @param page Playwright页面实例
     * @param userId 用户ID
     * @param aiName AI名称
     * @return 分享链接
     */
    public String getMetasoShareUrlFromTopRight(Page page, String userId, String aiName) {
        try {
            // 检查页面是否已关闭
            if (page.isClosed()) {
                logInfo.sendTaskLog("页面已关闭，无法获取分享链接", userId, aiName);
                return null;
            }

            // 定位右上角分享按钮 - 根据用户提供的DOM结构
            // 分享按钮在 <button class="MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeMedium css-1phoeln">
            String[] shareButtonSelectors = {
                // 策略1: 通过class定位
                "button.MuiIconButton-root.css-1phoeln",
                "button[class*='MuiIconButton-root'][class*='css-1phoeln']",
                // 策略2: 通过SVG图标定位（分享按钮的SVG包含特定的path）
                "button:has(svg path[d*='M17.326 10.506'])",
                // 策略3: 通过位置定位（右上角）
                "//button[contains(@class,'MuiIconButton-root')][last()]",
                // 策略4: 通过父容器定位
                "//div[contains(@class,'flex') and contains(@class,'items-center')]//button[contains(@class,'MuiIconButton-root')]"
            };

            AtomicReference<String> shareUrlRef = new AtomicReference<>();
            boolean clickSuccess = false;

            // 尝试所有选择器
            for (int i = 0; i < shareButtonSelectors.length && !clickSuccess; i++) {
                try {
                    String selector = shareButtonSelectors[i];
                    Locator shareButton = page.locator(selector);
                    
                    if (shareButton.count() > 0) {
                        // 等待按钮可见并点击
                        shareButton.waitFor(new Locator.WaitForOptions()
                            .setTimeout(5000)
                            .setState(WaitForSelectorState.VISIBLE));
                        
                        // 🔒 使用剪贴板锁保护剪贴板操作
                        final int selectorIndex = i;
                        clipboardLockManager.runWithClipboardLock(() -> {
                            try {
                                shareButton.click();
                                Thread.sleep(1500); // 等待弹窗或剪贴板更新
                                
                                // 从剪贴板读取链接
                                String url = (String) page.evaluate("navigator.clipboard.readText()");
                                if (url != null && url.contains("http")) {
                                    shareUrlRef.set(url);
                                    logInfo.sendTaskLog("通过选择器 " + (selectorIndex+1) + " 成功获取分享链接: " + url, userId, aiName);
                                } else {
                                    // 如果剪贴板没有，可能弹出了分享菜单，尝试查找分享链接元素
                                    logInfo.sendTaskLog("剪贴板未获取到链接，尝试查找分享菜单", userId, aiName);
                                }
                            } catch (Exception clipEx) {
                                logInfo.sendTaskLog("第 " + (selectorIndex+1) + " 个选择器点击成功但读取剪贴板失败", userId, aiName);
                            }
                        });
                        
                        String shareUrl = shareUrlRef.get();
                        if (shareUrl != null && shareUrl.contains("http")) {
                            clickSuccess = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    // 继续尝试下一个选择器
                    if (i == shareButtonSelectors.length - 1) {
                        logInfo.sendTaskLog("所有分享按钮选择器都失败", userId, aiName);
                    }
                }
            }
            
            String shareUrl = shareUrlRef.get();

            // 如果复制链接失败，尝试从URL或页面中直接提取
            if (!clickSuccess || shareUrl == null || !shareUrl.contains("http")) {
                logInfo.sendTaskLog("分享按钮点击失败，尝试从页面直接获取链接", userId, aiName);
                
                // 从当前页面URL中提取或构建分享链接
                String currentUrl = page.url();
                if (currentUrl.contains("metaso.cn")) {
                    if (currentUrl.contains("/search/") || currentUrl.contains("/search-v2/")) {
                        shareUrl = currentUrl;
                        logInfo.sendTaskLog("从页面URL获取分享链接: " + shareUrl, userId, aiName);
                    }
                }
                
                // 最后备用方案
                if (shareUrl == null || !shareUrl.contains("http")) {
                    shareUrl = currentUrl;
                    logInfo.sendTaskLog("使用当前页面URL作为分享链接: " + shareUrl, userId, aiName);
                }
            }

            // 清理URL，只保留数字ID部分（支持 /search/ 和 /search-v2/）
            if (shareUrl != null && (shareUrl.contains("metaso.cn/search/") || shareUrl.contains("metaso.cn/search-v2/"))) {
                shareUrl = cleanMetasoUrl(shareUrl);
                logInfo.sendTaskLog("已清理秘塔URL，保留数字ID: " + shareUrl, userId, aiName);
            }
            
            return shareUrl;

        } catch (Exception e) {
            logInfo.sendTaskLog("获取秘塔分享链接时发生异常: " + e.getMessage(), userId, aiName);
            // 返回当前页面URL作为备用
            try {
                String backupUrl = page.url();
                if (backupUrl != null && (backupUrl.contains("metaso.cn/search/") || backupUrl.contains("metaso.cn/search-v2/"))) {
                    backupUrl = cleanMetasoUrl(backupUrl);
                }
                return backupUrl;
            } catch (Exception urlEx) {
                return null;
            }
        }
    }

    /**
     * 判断是否为模型相关问题，返回标准答案
     * @param userPrompt 用户提示词
     * @return 如果是模型相关问题，返回标准答案；否则返回null
     */
    public String checkAndAnswerModelQuestion(String userPrompt) {
        if (userPrompt == null || userPrompt.trim().isEmpty()) {
            return null;
        }
        
        String prompt = userPrompt.toLowerCase().trim();
        
        // 检查是否包含模型相关问题关键词
        boolean isModelQuestion = prompt.contains("什么模型") || 
                                  prompt.contains("哪个模型") ||
                                  prompt.contains("你是谁") ||
                                  prompt.contains("你是什么") ||
                                  prompt.contains("你是谁") ||
                                  prompt.contains("what model") ||
                                  prompt.contains("who are you") ||
                                  prompt.contains("what are you");
        
        if (isModelQuestion) {
            return "您好，我是依托claude-4.5-sonnet-thinking模型的智能助手，在Cursor IDE中为您提供代码编写和问题解答服务，你可以直接告诉我你的需求。";
        }
        
        return null;
    }
}