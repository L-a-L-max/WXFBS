package com.wx.fbsir.engine.playwright.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Playwright 浏览器自动检测和安装
 * 
 * 在应用启动时自动检测 Playwright 浏览器是否已安装：
 * - 如果已安装：显示版本号
 * - 如果未安装：自动安装
 *
 * @author wxfbsir
 * @date 2025-12-18
 */
@Component
public class PlaywrightBrowserChecker {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightBrowserChecker.class);

    /**
     * Playwright 浏览器默认安装路径
     */
    private static final String[] BROWSER_PATHS = {
        System.getProperty("user.home") + "/.cache/ms-playwright",           // Linux/Mac
        System.getProperty("user.home") + "/Library/Caches/ms-playwright",   // Mac alternative
        System.getenv("LOCALAPPDATA") + "\\ms-playwright"                    // Windows
    };

    /**
     * 浏览器信息
     */
    private String chromiumVersion = null;
    private String browserPath = null;
    private boolean browserInstalled = false;

    /**
     * 检查并安装浏览器（在应用启动时调用）
     */
    public void checkAndInstallBrowser() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📦 Playwright 浏览器检测");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // 1. 检测浏览器是否已安装
        BrowserInfo info = detectInstalledBrowser();
        
        if (info != null) {
            this.browserInstalled = true;
            this.chromiumVersion = info.version;
            this.browserPath = info.path;
            
            log.info("✅ Chromium 浏览器已安装");
            log.info("   版本: {}", chromiumVersion);
            log.info("   路径: {}", browserPath);
        } else {
            log.warn("⚠️ 未检测到 Playwright 浏览器，开始自动安装...");
            
            boolean installed = installBrowser();
            if (installed) {
                // 重新检测
                info = detectInstalledBrowser();
                if (info != null) {
                    this.browserInstalled = true;
                    this.chromiumVersion = info.version;
                    this.browserPath = info.path;
                    
                    log.info("✅ Chromium 浏览器安装成功");
                    log.info("   版本: {}", chromiumVersion);
                    log.info("   路径: {}", browserPath);
                }
            } else {
                log.error("❌ Chromium 浏览器安装失败，请手动执行以下命令安装：");
                log.error("   mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args=\"install chromium\"");
            }
        }
        
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * 检测已安装的浏览器
     */
    private BrowserInfo detectInstalledBrowser() {
        for (String basePath : BROWSER_PATHS) {
            if (basePath == null) continue;
            
            Path path = Paths.get(basePath);
            if (!Files.exists(path)) continue;
            
            try {
                // 查找 chromium-* 目录
                var chromiumDirs = Files.list(path)
                    .filter(Files::isDirectory)
                    .filter(p -> p.getFileName().toString().startsWith("chromium-"))
                    .toList();
                
                if (!chromiumDirs.isEmpty()) {
                    Path chromiumDir = chromiumDirs.get(0);
                    String dirName = chromiumDir.getFileName().toString();
                    
                    // 从目录名提取版本号 (chromium-1234)
                    String version = dirName.replace("chromium-", "");
                    
                    // 查找可执行文件
                    String executablePath = findChromiumExecutable(chromiumDir);
                    if (executablePath != null) {
                        return new BrowserInfo(version, executablePath);
                    }
                }
            } catch (Exception e) {
                log.debug("检测路径 {} 时出错: {}", basePath, e.getMessage());
            }
        }
        
        return null;
    }

    /**
     * 查找 Chromium 可执行文件
     */
    private String findChromiumExecutable(Path chromiumDir) {
        String os = System.getProperty("os.name").toLowerCase();
        
        Path executable;
        if (os.contains("mac")) {
            executable = chromiumDir.resolve("chrome-mac/Chromium.app/Contents/MacOS/Chromium");
        } else if (os.contains("win")) {
            executable = chromiumDir.resolve("chrome-win/chrome.exe");
        } else {
            executable = chromiumDir.resolve("chrome-linux/chrome");
        }
        
        if (Files.exists(executable)) {
            return executable.toString();
        }
        
        // 尝试查找任何 chrome 可执行文件
        try {
            var files = Files.walk(chromiumDir, 3)
                .filter(Files::isExecutable)
                .filter(p -> {
                    String name = p.getFileName().toString().toLowerCase();
                    return name.equals("chrome") || name.equals("chromium") || 
                           name.equals("chrome.exe") || name.equals("chromium.exe");
                })
                .toList();
            
            if (!files.isEmpty()) {
                return files.get(0).toString();
            }
        } catch (Exception e) {
            log.debug("查找可执行文件时出错: {}", e.getMessage());
        }
        
        return null;
    }

    /**
     * 自动安装浏览器
     */
    private boolean installBrowser() {
        try {
            log.info("正在安装 Chromium 浏览器，请稍候...");
            
            // 使用 Playwright CLI 安装
            ProcessBuilder pb = new ProcessBuilder();
            
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                pb.command("cmd", "/c", "mvn", "exec:java", "-e", 
                    "-Dexec.mainClass=com.microsoft.playwright.CLI",
                    "-Dexec.args=install chromium");
            } else {
                pb.command("sh", "-c", 
                    "mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args='install chromium'");
            }
            
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            // 读取输出
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Downloading") || line.contains("Installing") || 
                        line.contains("chromium") || line.contains("SUCCESS")) {
                        log.info("  {}", line);
                    }
                }
            }
            
            boolean finished = process.waitFor(5, TimeUnit.MINUTES);
            if (!finished) {
                process.destroyForcibly();
                log.error("安装超时（5分钟）");
                return false;
            }
            
            int exitCode = process.exitValue();
            return exitCode == 0;
            
        } catch (Exception e) {
            log.error("安装过程出错: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取浏览器版本
     */
    public String getChromiumVersion() {
        return chromiumVersion;
    }

    /**
     * 获取浏览器路径
     */
    public String getBrowserPath() {
        return browserPath;
    }

    /**
     * 浏览器是否已安装
     */
    public boolean isBrowserInstalled() {
        return browserInstalled;
    }

    /**
     * 浏览器信息
     */
    private static class BrowserInfo {
        final String version;
        final String path;

        BrowserInfo(String version, String path) {
            this.version = version;
            this.path = path;
        }
    }
}
