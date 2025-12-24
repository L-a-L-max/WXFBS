package com.wx.fbsir.engine.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

/**
 * 系统性能检测器
 * 
 * 智能检测 CPU 和内存性能，兼容多种主机类型：
 * 1. 高性能：M1/M2/M3 等多核高频 CPU
 * 2. 标准性能：Intel i5/i7 桌面机
 * 3. 低性能CPU高内存：N100/N95 等低频 CPU + 16GB 内存
 * 4. 移动端：单核强多核弱
 * 5. 极低性能：树莓派等
 *
 * @author wxfbsir
 * @date 2025-12-22
 */
public class SystemPerformanceDetector {

    private static final Logger log = LoggerFactory.getLogger(SystemPerformanceDetector.class);

    /**
     * 性能等级
     */
    public enum PerformanceLevel {
        HIGH,           // 高性能（M系列、高端桌面）
        MEDIUM,         // 中等性能（标准桌面、笔记本）
        LOW_CPU,        // CPU弱但内存足（N100/N95 + 16GB）
        MOBILE,         // 移动端（单核强多核弱）
        VERY_LOW        // 极低性能（树莓派、2核2GB）
    }

    /**
     * 性能配置
     */
    public static class PerformanceConfig {
        private final PerformanceLevel level;
        private final int coreThreads;
        private final int maxThreads;
        private final int queueCapacity;
        private final String reason;

        public PerformanceConfig(PerformanceLevel level, int coreThreads, int maxThreads, 
                                  int queueCapacity, String reason) {
            this.level = level;
            this.coreThreads = coreThreads;
            this.maxThreads = maxThreads;
            this.queueCapacity = queueCapacity;
            this.reason = reason;
        }

        public PerformanceLevel getLevel() { return level; }
        public int getCoreThreads() { return coreThreads; }
        public int getMaxThreads() { return maxThreads; }
        public int getQueueCapacity() { return queueCapacity; }
        public String getReason() { return reason; }
    }

    /**
     * 检测系统性能并返回推荐配置
     */
    public static PerformanceConfig detectPerformance() {
        int processors = Runtime.getRuntime().availableProcessors();
        long maxMemoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        
        String cpuModel = detectCPUModel();
        boolean isLowFrequencyCPU = isLowFrequencyCPU(cpuModel, processors);
        boolean isAppleSilicon = isAppleSilicon(cpuModel);
        boolean isMobileCPU = isMobileCPU(cpuModel);
        
        log.info("[性能检测] CPU核心: {}, 内存: {}MB, 型号: {}", processors, maxMemoryMB, cpuModel);
        
        // 1. 极低性能：2核心或内存<3GB
        if (processors <= 2 || maxMemoryMB < 3072) {
            return new PerformanceConfig(
                PerformanceLevel.VERY_LOW,
                2,
                2,
                500,
                String.format("极低性能 - %d核/%dMB内存", processors, maxMemoryMB)
            );
        }
        
        // 2. 低频CPU（N100/N95/Celeron）+ 内存足够
        if (isLowFrequencyCPU && maxMemoryMB >= 8192) {
            int coreThreads = Math.max(2, processors / 2);
            int maxThreads = Math.max(4, processors);
            return new PerformanceConfig(
                PerformanceLevel.LOW_CPU,
                coreThreads,
                maxThreads,
                1000,
                String.format("低频CPU高内存 - %s (%d核/%dGB)", cpuModel, processors, maxMemoryMB / 1024)
            );
        }
        
        // 3. Apple Silicon（M1/M2/M3）- 高性能多核
        if (isAppleSilicon) {
            return new PerformanceConfig(
                PerformanceLevel.HIGH,
                processors,
                processors * 2,
                1000,
                String.format("Apple Silicon - %s (%d核)", cpuModel, processors)
            );
        }
        
        // 4. 移动端CPU（单核强多核弱）
        if (isMobileCPU && processors >= 4) {
            return new PerformanceConfig(
                PerformanceLevel.MOBILE,
                Math.min(4, processors),
                Math.min(8, processors * 2),
                1000,
                String.format("移动端CPU - %s (%d核)", cpuModel, processors)
            );
        }
        
        // 5. 低内存（<4GB）
        if (maxMemoryMB < 4096) {
            int coreThreads = Math.max(2, processors / 2);
            int maxThreads = Math.max(4, processors);
            return new PerformanceConfig(
                PerformanceLevel.VERY_LOW,
                coreThreads,
                maxThreads,
                500,
                String.format("低内存 - %dMB", maxMemoryMB)
            );
        }
        
        // 6. 标准/高性能（默认）
        PerformanceLevel level = processors >= 8 ? PerformanceLevel.HIGH : PerformanceLevel.MEDIUM;
        return new PerformanceConfig(
            level,
            processors,
            processors * 2,
            1000,
            String.format("%s - %d核/%dGB", level == PerformanceLevel.HIGH ? "高性能" : "标准性能", 
                processors, maxMemoryMB / 1024)
        );
    }

    /**
     * 检测CPU型号
     */
    private static String detectCPUModel() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command;
            
            if (os.contains("mac")) {
                command = "sysctl -n machdep.cpu.brand_string";
            } else if (os.contains("linux")) {
                command = "cat /proc/cpuinfo | grep 'model name' | head -1";
            } else if (os.contains("win")) {
                command = "wmic cpu get name";
            } else {
                return "Unknown";
            }
            
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            
            if (line != null && !line.trim().isEmpty()) {
                // 清理输出
                line = line.replaceAll("model name\\s*:\\s*", "").trim();
                if (line.toLowerCase().contains("name")) {
                    line = reader.readLine(); // Windows第二行才是CPU名称
                }
                return line != null ? line.trim() : "Unknown";
            }
        } catch (Exception e) {
            log.debug("[性能检测] CPU型号检测失败: {}", e.getMessage());
        }
        return "Unknown";
    }

    /**
     * 判断是否为低频CPU（N100/N95/Celeron/Atom等）
     */
    private static boolean isLowFrequencyCPU(String cpuModel, int processors) {
        if (cpuModel == null || cpuModel.equals("Unknown")) {
            return false;
        }
        
        String model = cpuModel.toLowerCase();
        
        // Intel 低性能系列
        if (model.contains("n100") || model.contains("n95") || model.contains("n97") ||
            model.contains("n200") || model.contains("n305") ||
            model.contains("celeron") || model.contains("atom") || 
            model.contains("pentium silver") || model.contains("pentium gold")) {
            return true;
        }
        
        // AMD 低性能系列
        if (model.contains("athlon") && model.contains("silver") ||
            model.contains("3020e") || model.contains("3015e")) {
            return true;
        }
        
        // 4核心 + 低频特征词
        if (processors == 4 && (model.contains("1.0 ghz") || model.contains("1.1 ghz") || 
            model.contains("1.2 ghz") || model.contains("1.3 ghz"))) {
            return true;
        }
        
        return false;
    }

    /**
     * 判断是否为 Apple Silicon（M1/M2/M3）
     */
    private static boolean isAppleSilicon(String cpuModel) {
        if (cpuModel == null || cpuModel.equals("Unknown")) {
            // macOS 上再尝试检测架构
            String arch = System.getProperty("os.arch");
            return arch != null && arch.toLowerCase().contains("aarch64");
        }
        
        String model = cpuModel.toLowerCase();
        return model.contains("apple m1") || model.contains("apple m2") || 
               model.contains("apple m3") || model.contains("apple m4");
    }

    /**
     * 判断是否为移动端CPU（U系列/H系列但单核强）
     */
    private static boolean isMobileCPU(String cpuModel) {
        if (cpuModel == null || cpuModel.equals("Unknown")) {
            return false;
        }
        
        String model = cpuModel.toLowerCase();
        
        // Intel 移动端标识
        if (model.matches(".*i[357]-\\d{4,5}u.*") ||  // 如 i5-1135G7U
            model.matches(".*i[357]-\\d{4,5}y.*")) {  // 如 i7-1165Y
            return true;
        }
        
        // AMD 移动端低功耗
        if (model.contains("ryzen") && (model.contains("u") || model.contains("15w"))) {
            return true;
        }
        
        return false;
    }

    /**
     * 获取系统负载（如果可用）
     */
    public static double getSystemLoad() {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            return osBean.getSystemCpuLoad();
        } catch (Exception e) {
            return -1;
        }
    }
}
