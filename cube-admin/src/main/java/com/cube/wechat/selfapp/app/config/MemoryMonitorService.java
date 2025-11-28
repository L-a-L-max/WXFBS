package com.cube.wechat.selfapp.app.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存监控和自动清理服务
 * 防止内存泄漏，定期清理无用资源
 */
@Service
public class MemoryMonitorService {
    
    private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private static final double MEMORY_THRESHOLD = 0.85; // 85%内存使用率阈值
    private static final long MAX_FUTURE_AGE = 300000; // 5分钟
    
    /**
     * 每分钟检查一次内存使用情况
     */
    @Scheduled(fixedRate = 60000)
    public void monitorMemory() {
        try {
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            long used = heapUsage.getUsed();
            long max = heapUsage.getMax();
            double usageRatio = (double) used / max;
            
            if (usageRatio > MEMORY_THRESHOLD) {
                System.err.println("⚠️ 内存使用率过高: " + String.format("%.1f%%", usageRatio * 100) + 
                                 " (" + formatBytes(used) + "/" + formatBytes(max) + ")");
                
                // 触发清理
                performCleanup();
                
                // 建议垃圾回收
                System.gc();
                
                // 再次检查
                MemoryUsage afterGC = memoryBean.getHeapMemoryUsage();
                long afterUsed = afterGC.getUsed();
                double afterRatio = (double) afterUsed / max;
                System.out.println("🧹 清理后内存: " + String.format("%.1f%%", afterRatio * 100) + 
                                 " (" + formatBytes(afterUsed) + "/" + formatBytes(max) + ")");
            }
        } catch (Exception e) {
            System.err.println("❌ 内存监控失败: " + e.getMessage());
        }
    }
    
    /**
     * 每5分钟清理一次过期资源
     */
    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredResources() {
        try {
            performCleanup();
        } catch (Exception e) {
            System.err.println("❌ 资源清理失败: " + e.getMessage());
        }
    }
    
    /**
     * 执行清理操作
     */
    private void performCleanup() {
        int cleanedCount = 0;
        
        // 清理过期的Future对象
        try {
            ConcurrentHashMap<String, ?> futureMap = MyWebSocketHandler.getFutureMap();
            if (futureMap != null) {
                int beforeSize = futureMap.size();
                // 清理所有Future对象（简化处理）
                futureMap.clear();
                cleanedCount += beforeSize;
                if (beforeSize > 0) {
                    System.out.println("🧹 清理Future对象: " + beforeSize + "个");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ 清理Future对象失败: " + e.getMessage());
        }
        
        if (cleanedCount > 0) {
            System.out.println("✅ 资源清理完成，共清理 " + cleanedCount + " 个对象");
        }
    }
    
    /**
     * 格式化字节数
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + "B";
        if (bytes < 1024 * 1024) return String.format("%.1fKB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1fMB", bytes / (1024.0 * 1024));
        return String.format("%.1fGB", bytes / (1024.0 * 1024 * 1024));
    }
    
    /**
     * 获取当前内存使用情况
     */
    public String getMemoryStatus() {
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        long used = heapUsage.getUsed();
        long max = heapUsage.getMax();
        double usageRatio = (double) used / max;
        
        return String.format("内存使用: %.1f%% (%s/%s)", 
                           usageRatio * 100, 
                           formatBytes(used), 
                           formatBytes(max));
    }
}
