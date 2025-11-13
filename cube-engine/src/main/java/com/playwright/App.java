package com.playwright;

import com.playwright.config.CustomServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
@EnableConfigurationProperties(CustomServerConfig.class)
public class App {
    public static void main(String[] args) {

        // 1. 首先尝试读取配置文件中的预设配置
        System.out.println("🔍 检查配置文件中的预设配置...");
        
        String serverIp;
        String containerId;
        boolean sslEnabled;
        Scanner scanner = new Scanner(System.in);
        
        // 直接读取配置文件来检查是否有预设配置
        CustomServerConfig customConfig = readConfigFromFile();
        
        if (customConfig != null && customConfig.isConfigured()) {
            // 使用配置文件中的预设配置
            System.out.println("✅ 发现预设配置: " + customConfig.getConfigSummary());
            serverIp = customConfig.getAddress();
            containerId = customConfig.getHostId();
            sslEnabled = customConfig.isSslEnabled();
            
            System.out.println("📋 使用配置文件中的预设配置，直接启动...");
        } else {
            // 配置文件中没有预设配置，提示用户输入
            System.out.println("⚠️ 配置文件中未找到预设配置，请手动输入服务器信息");
            System.out.println("💡 提示: 你可以在 application.yaml 中的 custom.server 区域预设配置信息");
            
            // 2. 提示用户输入IP
            System.out.print("请输入地址：");
            serverIp = scanner.nextLine().trim();

            if (serverIp.isEmpty()) {
                System.err.println("地址不能为空！");
                scanner.close();
                return;
            }

            // 3. 让用户选择是否启用HTTPS/WSS加密协议
            sslEnabled = false;
            while (true) {
                System.out.print("是否启用HTTPS/WSS？(y/n，默认n)：");
                String sslInput = scanner.nextLine().trim().toLowerCase();

                if (sslInput.isEmpty() || sslInput.equals("n") || sslInput.equals("no")) {
                    break;
                } else if (sslInput.equals("y") || sslInput.equals("yes")) {
                    sslEnabled = true;
                    break;
                } else {
                    System.err.println("请输入 y 或 n（yes/no）");
                }
            }

            // 4. 输入引擎ID
            System.out.print("请输入主机引擎ID：");
            containerId = scanner.nextLine().trim();
            if (containerId.isEmpty()) {
                System.err.println("主机引擎ID不能为空！");
                scanner.close();
                return;
            }
        }

        // 5. 检测可用端口
        int availablePort = findAvailablePort();
        if (availablePort == -1) {
            System.err.println("❌ 无法找到可用端口，应用启动失败！");
            scanner.close();
            return;
        }

        // 6. 根据配置设置协议和端口
        String protocol = sslEnabled ? "https" : "http";
        String wsProtocol = sslEnabled ? "wss" : "ws";

        System.setProperty("protocol", protocol);           // 注入http/https
        System.setProperty("wsProtocol", wsProtocol);       // 注入ws/wss
        System.setProperty("SERVER_PORT", String.valueOf(availablePort)); // 设置应用端口
        System.setProperty("ssl.enabled", String.valueOf(sslEnabled)); // 设置SSL启用状态

        // 6. CPU和线程配置
        if (customConfig != null && customConfig.isConfigured()) {
            // 使用预设配置时，自动使用系统最大CPU和线程数
            int systemCores = Runtime.getRuntime().availableProcessors();
            int maxThreads = systemCores * 2;
            
            System.setProperty("CPU_CORES", String.valueOf(systemCores));
            System.setProperty("MAX_THREADS", String.valueOf(maxThreads));
            
            System.out.println("🔧 自动配置: CPU核心数=" + systemCores + ", 最大线程数=" + maxThreads);
        } else {
            // 手动输入时，允许用户选择CPU核心数
            String cpuCoresInput = "";
            while (true) {
                System.out.print("请输入CPU核心数（默认使用系统可用核心数，直接回车跳过）：");
                cpuCoresInput = scanner.nextLine().trim();

                if (cpuCoresInput.isEmpty()) {
                    break; // 用户选择使用默认值
                }

                try {
                    int cpuCores = Integer.parseInt(cpuCoresInput);
                    if (cpuCores <= 0) {
                        System.err.println("CPU核心数必须大于0，请重新输入！");
                        continue;
                    }
                    System.setProperty("CPU_CORES", cpuCoresInput);
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("CPU核心数格式错误，请输入有效的数字");
                    continue;
                }
            }

            // 7. 输入最大线程数
            String maxThreadsInput = "";
            while (true) {
                System.out.print("请输入最大线程数（默认使用系统可用处理器数*2，直接回车跳过）：");
                maxThreadsInput = scanner.nextLine().trim();

                if (maxThreadsInput.isEmpty()) {
                    break; // 用户选择使用默认值
                }

                try {
                    int maxThreads = Integer.parseInt(maxThreadsInput);
                    if (maxThreads <= 0) {
                        System.err.println("最大线程数必须大于0，请重新输入！");
                        continue;
                    }
                    System.setProperty("MAX_THREADS", maxThreadsInput);
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("最大线程数格式错误，请输入有效的数字！");
                    continue;
                }
            }
        }

        // 将配置设置为系统变量
        System.setProperty("server.ip", serverIp);
        System.setProperty("CONTAINER_ID", containerId);

        // 输出最终配置信息
        System.out.println("\n========================================");
        System.out.println("🚀 启动配置信息");
        System.out.println("========================================");
        System.out.printf("📡 服务器地址: %s%n", serverIp);
        System.out.printf("🔐 协议: %s/%s%n", protocol, wsProtocol);
        System.out.printf("📦 主机ID: %s%n", containerId);
        System.out.printf("🔧 端口: %d%n", availablePort);
        System.out.println("========================================");

        // 启动Spring Boot应用
        SpringApplication.run(App.class, args);

        scanner.close();
    }

    /**
     * 在指定范围内查找可用端口
     *
     * @return 可用端口号，如果找不到返回-1
     */
    private static int findAvailablePort() {
        for (int port = 8083; port <= 9999; port++) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                // 如果能成功创建ServerSocket，说明端口可用
                System.out.println("✅ 检测到可用端口：" + port);
                return port;
            } catch (Exception e) {
                // 端口被占用，继续检测下一个端口
                System.out.println("⚠️ 端口 " + port + " 被占用，继续检测...");
            }
        }
        System.err.println("❌ 在端口范围 " + 8083 + "-" + 9999 + " 内找不到可用端口！");
        return -1;
    }

    /**
     * 直接读取配置文件中的自定义服务器配置
     */
    private static CustomServerConfig readConfigFromFile() {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = App.class.getClassLoader().getResourceAsStream("application.yaml");
            
            if (inputStream == null) {
                return null;
            }
            
            Map<String, Object> data = yaml.load(inputStream);
            inputStream.close();
            
            // 提取custom.server配置
            Map<String, Object> custom = (Map<String, Object>) data.get("custom");
            if (custom == null) {
                return null;
            }
            
            Map<String, Object> server = (Map<String, Object>) custom.get("server");
            if (server == null) {
                return null;
            }
            
            // 创建CustomServerConfig对象
            CustomServerConfig config = new CustomServerConfig();
            
            Object address = server.get("address");
            if (address != null) {
                config.setAddress(address.toString());
            }
            
            Object hostId = server.get("hostId");
            if (hostId != null) {
                config.setHostId(hostId.toString());
            }
            
            Object sslEnabled = server.get("sslEnabled");
            if (sslEnabled != null) {
                config.setSslEnabled(Boolean.parseBoolean(sslEnabled.toString()));
            }
            
            return config;
            
        } catch (Exception e) {
            System.err.println("⚠️ 读取配置文件失败: " + e.getMessage());
            return null;
        }
    }
}
