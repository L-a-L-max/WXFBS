package com.wx.fbsir.engine.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统性能监控工具类
 * 
 * 用于获取系统的硬件信息和实时性能数据
 * 包括：CPU型号、内存容量、磁盘空间、CPU使用率、内存使用率等
 *
 * @author wxfbsir
 * @date 2025-12-23
 */
public class SystemPerformanceMonitor {

    private static final Logger log = LoggerFactory.getLogger(SystemPerformanceMonitor.class);
    
    // 缓存硬件信息（这些信息不会变化）
    private static String cachedCpuModel = null;
    private static Integer cachedCpuCores = null;
    private static Long cachedTotalMemory = null;
    private static Long cachedTotalDiskSpace = null;

    /**
     * 获取完整的硬件信息（静态信息，适合注册时使用）
     * 
     * @return 硬件信息Map
     */
    public static Map<String, Object> getHardwareInfo() {
        Map<String, Object> info = new HashMap<>();
        
        try {
            // CPU信息
            info.put("cpuModel", getCpuModel());
            info.put("cpuCores", getCpuCores());
            info.put("cpuLogicalCores", Runtime.getRuntime().availableProcessors());
            
            // 内存信息（字节 → GB）
            long totalMemory = getTotalMemory();
            info.put("totalMemoryMB", totalMemory / (1024 * 1024));
            info.put("totalMemoryGB", String.format("%.2f", totalMemory / (1024.0 * 1024 * 1024)));
            
            // 磁盘信息（字节 → GB）
            long totalDisk = getTotalDiskSpace();
            info.put("totalDiskMB", totalDisk / (1024 * 1024));
            info.put("totalDiskGB", String.format("%.2f", totalDisk / (1024.0 * 1024 * 1024)));
            
        } catch (Exception e) {
            log.warn("[性能监控] 获取硬件信息失败: {}", e.getMessage());
        }
        
        return info;
    }

    /**
     * 获取实时性能数据（动态信息，适合心跳时使用）
     * 
     * @return 性能数据Map
     */
    public static Map<String, Object> getPerformanceData() {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // CPU使用率（0-100的百分比）
            double cpuUsage = getCpuUsage();
            data.put("cpuUsagePercent", String.format("%.1f", cpuUsage));
            
            // 内存使用情况
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = 
                    (com.sun.management.OperatingSystemMXBean) osBean;
                
                // 总内存和空闲内存（字节）
                long totalMemory = sunOsBean.getTotalPhysicalMemorySize();
                long freeMemory = sunOsBean.getFreePhysicalMemorySize();
                long usedMemory = totalMemory - freeMemory;
                
                // 计算内存使用率
                double memoryUsage = (usedMemory * 100.0) / totalMemory;
                
                data.put("usedMemoryMB", usedMemory / (1024 * 1024));
                data.put("freeMemoryMB", freeMemory / (1024 * 1024));
                data.put("memoryUsagePercent", String.format("%.1f", memoryUsage));
            }
            
            // JVM内存使用情况
            Runtime runtime = Runtime.getRuntime();
            long jvmTotalMemory = runtime.totalMemory();
            long jvmFreeMemory = runtime.freeMemory();
            long jvmUsedMemory = jvmTotalMemory - jvmFreeMemory;
            long jvmMaxMemory = runtime.maxMemory();
            
            data.put("jvmUsedMemoryMB", jvmUsedMemory / (1024 * 1024));
            data.put("jvmMaxMemoryMB", jvmMaxMemory / (1024 * 1024));
            data.put("jvmUsagePercent", String.format("%.1f", (jvmUsedMemory * 100.0) / jvmMaxMemory));
            
            // 系统负载（1分钟、5分钟、15分钟平均负载）
            double systemLoad = osBean.getSystemLoadAverage();
            if (systemLoad >= 0) {
                data.put("systemLoad", String.format("%.2f", systemLoad));
            }
            
            // 磁盘使用情况（当前工作目录所在磁盘）
            java.io.File root = new java.io.File("/");
            long totalDisk = root.getTotalSpace();
            long freeDisk = root.getFreeSpace();
            long usedDisk = totalDisk - freeDisk;
            double diskUsage = (usedDisk * 100.0) / totalDisk;
            
            data.put("usedDiskGB", String.format("%.2f", usedDisk / (1024.0 * 1024 * 1024)));
            data.put("freeDiskGB", String.format("%.2f", freeDisk / (1024.0 * 1024 * 1024)));
            data.put("diskUsagePercent", String.format("%.1f", diskUsage));
            
            // 当前时间戳
            data.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.warn("[性能监控] 获取性能数据失败: {}", e.getMessage());
        }
        
        return data;
    }

    /**
     * 获取CPU型号
     * 
     * @return CPU型号字符串，获取失败返回 "Unknown"
     */
    private static String getCpuModel() {
        if (cachedCpuModel != null) {
            return cachedCpuModel;
        }

        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command;
            
            if (os.contains("mac")) {
                // macOS: 使用 sysctl 命令
                command = "sysctl -n machdep.cpu.brand_string";
            } else if (os.contains("linux")) {
                // Linux: 读取 /proc/cpuinfo
                command = "cat /proc/cpuinfo | grep 'model name' | head -1";
            } else if (os.contains("win")) {
                // Windows: 使用 wmic 命令
                command = "wmic cpu get name";
            } else {
                return "Unknown";
            }
            
            Process process = Runtime.getRuntime().exec(command);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.toLowerCase().contains("name")) {
                        // Linux需要去掉 "model name : " 前缀
                        if (line.contains(":")) {
                            line = line.substring(line.indexOf(":") + 1).trim();
                        }
                        cachedCpuModel = line;
                        return cachedCpuModel;
                    }
                }
            }
            
        } catch (Exception e) {
            log.debug("[性能监控] 获取CPU型号失败: {}", e.getMessage());
        }
        
        cachedCpuModel = "Unknown";
        return cachedCpuModel;
    }

    /**
     * 获取CPU物理核心数
     * 
     * @return CPU核心数
     */
    private static int getCpuCores() {
        if (cachedCpuCores != null) {
            return cachedCpuCores;
        }

        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command;
            
            if (os.contains("mac")) {
                command = "sysctl -n hw.physicalcpu";
            } else if (os.contains("linux")) {
                command = "lscpu | grep '^Core(s) per socket:' | awk '{print $4}'";
            } else {
                // Windows或其他系统，使用逻辑处理器数的一半作为估算
                cachedCpuCores = Runtime.getRuntime().availableProcessors() / 2;
                return cachedCpuCores;
            }
            
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null && !line.isEmpty()) {
                    cachedCpuCores = Integer.parseInt(line.trim());
                    return cachedCpuCores;
                }
            }
            
        } catch (Exception e) {
            log.debug("[性能监控] 获取CPU核心数失败: {}", e.getMessage());
        }
        
        // 降级：使用逻辑处理器数
        cachedCpuCores = Runtime.getRuntime().availableProcessors();
        return cachedCpuCores;
    }

    /**
     * 获取系统总内存（字节）
     * 
     * @return 总内存字节数
     */
    private static long getTotalMemory() {
        if (cachedTotalMemory != null) {
            return cachedTotalMemory;
        }

        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = 
                    (com.sun.management.OperatingSystemMXBean) osBean;
                cachedTotalMemory = sunOsBean.getTotalPhysicalMemorySize();
                return cachedTotalMemory;
            }
        } catch (Exception e) {
            log.debug("[性能监控] 获取总内存失败: {}", e.getMessage());
        }
        
        // 降级：使用JVM最大内存
        cachedTotalMemory = Runtime.getRuntime().maxMemory();
        return cachedTotalMemory;
    }

    /**
     * 获取磁盘总容量（字节）
     * 
     * @return 磁盘总容量字节数
     */
    private static long getTotalDiskSpace() {
        if (cachedTotalDiskSpace != null) {
            return cachedTotalDiskSpace;
        }

        try {
            java.io.File root = new java.io.File("/");
            cachedTotalDiskSpace = root.getTotalSpace();
            return cachedTotalDiskSpace;
        } catch (Exception e) {
            log.debug("[性能监控] 获取磁盘容量失败: {}", e.getMessage());
            cachedTotalDiskSpace = 0L;
            return cachedTotalDiskSpace;
        }
    }

    /**
     * 获取CPU使用率（0-100的百分比）
     * 
     * @return CPU使用率
     */
    private static double getCpuUsage() {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = 
                    (com.sun.management.OperatingSystemMXBean) osBean;
                
                // getSystemCpuLoad() 返回 0.0-1.0 之间的值
                double cpuLoad = sunOsBean.getSystemCpuLoad();
                if (cpuLoad >= 0) {
                    return cpuLoad * 100.0;
                }
            }
        } catch (Exception e) {
            log.debug("[性能监控] 获取CPU使用率失败: {}", e.getMessage());
        }
        
        return -1.0; // 无法获取
    }

    /**
     * 清除缓存（用于测试）
     */
    public static void clearCache() {
        cachedCpuModel = null;
        cachedCpuCores = null;
        cachedTotalMemory = null;
        cachedTotalDiskSpace = null;
    }
}
