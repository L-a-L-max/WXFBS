package com.cube.framework.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 简化日志处理器
 * 对频繁出现的无关紧要错误进行统计和简化输出
 */
@Component
public class SimpleLogHandler {
    
    private static final Logger log = LoggerFactory.getLogger(SimpleLogHandler.class);
    
    // 错误统计
    private final ConcurrentHashMap<String, AtomicInteger> errorCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> lastLogTime = new ConcurrentHashMap<>();
    
    // 日志输出间隔（毫秒）
    private static final long LOG_INTERVAL = 300000; // 5分钟
    
    /**
     * 处理HTTP解析错误
     */
    public void handleHttpParsingError(String clientInfo) {
        String key = "HTTP_PARSING_ERROR";
        incrementAndLogIfNeeded(key, 
            String.format("🔧 [HTTP] SSL/TLS握手数据误识别为HTTP请求 | 客户端:%s | 已忽略", 
                clientInfo != null ? clientInfo : "未知"));
    }
    
    /**
     * 处理连接重置错误
     */
    public void handleConnectionResetError(String uri) {
        String key = "CONNECTION_RESET";
        incrementAndLogIfNeeded(key, 
            String.format("🔧 [连接] 客户端主动断开连接 | URI:%s | 已处理", 
                uri != null ? uri : "/"));
    }
    
    /**
     * 处理MCP客户端初始化
     */
    public void handleMcpClientInit(String clientName, String protocol) {
        String key = "MCP_CLIENT_INIT";
        incrementAndLogIfNeeded(key, 
            String.format("🔧 [MCP] 客户端重连 | %s | 协议:%s", 
                clientName != null ? clientName : "未知客户端", 
                protocol != null ? protocol : "未知"));
    }
    
    /**
     * 处理异常处理器失败
     */
    public void handleExceptionHandlerFailure(String exceptionType) {
        String key = "EXCEPTION_HANDLER_FAILURE";
        incrementAndLogIfNeeded(key, 
            String.format("🔧 [异常处理] 响应转换失败 | %s | 客户端已断开", 
                exceptionType != null ? exceptionType : "未知异常"));
    }
    
    /**
     * 增加计数并在需要时输出日志
     */
    private void incrementAndLogIfNeeded(String key, String message) {
        AtomicInteger count = errorCounts.computeIfAbsent(key, k -> new AtomicInteger(0));
        AtomicLong lastTime = lastLogTime.computeIfAbsent(key, k -> new AtomicLong(0));
        
        int currentCount = count.incrementAndGet();
        long currentTime = System.currentTimeMillis();
        long lastTimeValue = lastTime.get();
        
        // 第一次出现或超过间隔时间时输出日志
        if (lastTimeValue == 0 || (currentTime - lastTimeValue) > LOG_INTERVAL) {
            if (currentCount == 1) {
                log.info(message);
            } else {
                log.info("{} | 过去5分钟内共{}次", message, currentCount);
                count.set(0); // 重置计数
            }
            lastTime.set(currentTime);
        }
    }
    
    /**
     * 获取错误统计信息
     */
    public String getErrorStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("📊 [错误统计] ");
        
        errorCounts.forEach((key, count) -> {
            if (count.get() > 0) {
                stats.append(String.format("%s:%d次 ", key, count.get()));
            }
        });
        
        return stats.toString();
    }
}
