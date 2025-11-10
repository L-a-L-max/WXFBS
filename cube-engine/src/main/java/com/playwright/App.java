package com.playwright;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.net.ServerSocket;
import java.util.Scanner;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class App {
    public static void main(String[] args) {

        // 1. 启动前提示用户输入IP
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入地址：");
        String serverIp = scanner.nextLine().trim();

        if (serverIp.isEmpty()) {
            System.err.println("地址不能为空！");
            scanner.close();
            return;
        }

        // 2. 让用户选择是否启用HTTPS/WSS加密协议
        boolean sslEnabled = false;
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

        // 3. 检测可用端口
        int availablePort = findAvailablePort();
        if (availablePort == -1) {
            System.err.println("❌ 无法找到可用端口，应用启动失败！");
            scanner.close();
            return;
        }

        // 4. 根据用户选择设置协议和端口
        String protocol = sslEnabled ? "https" : "http";
        String wsProtocol = sslEnabled ? "wss" : "ws";

        System.setProperty("protocol", protocol);           // 注入http/https
        System.setProperty("wsProtocol", wsProtocol);       // 注入ws/wss
        System.setProperty("SERVER_PORT", String.valueOf(availablePort)); // 设置应用端口
        System.setProperty("ssl.enabled", String.valueOf(sslEnabled)); // 设置SSL启用状态

        // 5. 输入引擎ID
        System.out.print("请输入主机引擎ID：");
        String containerId = scanner.nextLine().trim();
        if (containerId.isEmpty()) {
            System.err.println("主机引擎ID不能为空！");
            scanner.close();
            return;
        }

        // 6. 输入CPU核心数
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

        // 将输入的IP设置为系统变量
        System.setProperty("server.ip", serverIp);
        System.setProperty("CONTAINER_ID", containerId);

        // 输出配置信息
        System.out.println("\n========================================");
        System.out.println("🚀 启动配置信息");
        System.out.println("========================================");
        System.out.printf("📡 服务器地址: %s%n", serverIp);
        System.out.printf("🔐 协议: %s/%s%n", protocol, wsProtocol);
        System.out.printf("📦 主机ID: %s%n", containerId);
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
}
