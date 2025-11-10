package com.playwright.controller;

/**
 * @author 优立方
 * @version JDK 17
 * @date 2025年02月06日 14:52
 */
import com.playwright.controller.ai.BrowserController;
import com.playwright.entity.CpuUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class StartupRunner {

    @Autowired
    private BrowserController browserController;

    @Autowired
    private CpuUtils cpuUtils;

    private RestTemplate restTemplate;

    private String checkUrl;

    /**
     * 配置RestTemplate bean
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);  // 连接超时5秒
        factory.setReadTimeout(10000);    // 读取超时10秒
        return new RestTemplate(factory);
    }

    @Autowired
    public void HostIdChecker(RestTemplate restTemplate,
                              @Value("${host.check.url}") String checkUrl) {
        this.restTemplate = restTemplate;
        this.checkUrl = checkUrl;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws InterruptedException {
        // 输出系统基本信息
        printSystemInfo();
//         原有的登录检查（已注释）
    }

    private void printSystemInfo() {
        Runtime runtime = Runtime.getRuntime();
        int systemProcessors = runtime.availableProcessors();
        int configuredCores = cpuUtils.getConfiguredCores();
        int configuredMaxThreads = cpuUtils.getConfiguredMaxThreads();
        long maxMemory = runtime.maxMemory() / 1024 / 1024; // MB
        long totalMemory = runtime.totalMemory() / 1024 / 1024; // MB
        long freeMemory = runtime.freeMemory() / 1024 / 1024; // MB
        long usedMemory = totalMemory - freeMemory;

        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");

        // 获取容器ID
        String containerId = System.getProperty("CONTAINER_ID");
        if (containerId == null || containerId.trim().isEmpty()) {
            containerId = "未设置";
        }

        System.out.println("========================================");
        System.out.println("🚀 U3W Cube Engine 系统信息");
        System.out.println("========================================");
        System.out.printf("📦 主机ID: %s%n", containerId);  // 输出主机ID
        System.out.printf("💻 系统: %s (%s)%n", osName, osArch);
        System.out.printf("☕ Java: %s%n", javaVersion);
        System.out.printf("🔧 系统CPU核心数: %d%n", systemProcessors);
        System.out.printf("🔧 配置CPU核心数: %d | 配置最大线程数: %d%n", configuredCores, configuredMaxThreads);
        System.out.printf("💾 内存: 已用 %dMB / 总计 %dMB / 最大 %dMB%n", usedMemory, totalMemory, maxMemory);
        System.out.println("========================================");
        System.out.println("✅ 应用启动完成，准备处理AI任务");
        System.out.println("========================================");
        checkHostIdOnStartup(containerId);
    }

    // 应用启动就绪后发送请求
    public void checkHostIdOnStartup(String containerId) {

        // 2. 拼接完整 URL（附加 hostId 参数）
        String fullUrl = checkUrl + "?hostId=" + containerId;
        try {
            // 3. 发送带容器 ID 的 GET 请求
            String response = restTemplate.getForObject(fullUrl, String.class);
        } catch (HttpClientErrorException e) {
            //System.err.println("❌ 客户端错误：" + e.getStatusCode() + "，响应：" + e.getResponseBodyAsString());
        }
    }
}
