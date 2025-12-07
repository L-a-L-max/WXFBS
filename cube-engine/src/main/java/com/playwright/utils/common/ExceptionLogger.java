package com.playwright.utils.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一的异常日志记录工具
 * 确保所有异常都被完整记录，包含上下文信息和堆栈跟踪
 * 
 * @author U3W
 * @version 1.0
 */
public class ExceptionLogger {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 记录异常 - 完整版（包含堆栈跟踪）
     * @param operation 操作名称
     * @param userId 用户ID
     * @param aiName AI名称
     * @param exception 异常对象
     */
    public static void logException(String operation, String userId, String aiName, Exception exception) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String exceptionType = exception.getClass().getSimpleName();
        String message = exception.getMessage() != null ? exception.getMessage() : "无错误消息";
        
        // 压缩错误消息到100字以内
        String shortMessage = message.length() > 100 ? message.substring(0, 100) + "..." : message;
        
        // 终端显示简洁错误
        System.err.println(String.format("❌ [异常] %s失败 | AI:%s | 用户:%s | 时间:%s", 
            operation, 
            aiName != null ? aiName : "无", 
            userId != null ? userId : "无", 
            timestamp));
        System.err.println(String.format("   类型:%s | 消息:%s", exceptionType, shortMessage));
        
        // 获取堆栈跟踪（前10层）
        String stackTrace = getStackTrace(exception, 10);
        System.err.println(String.format("   堆栈:%s", stackTrace));
    }
    
    /**
     * 记录异常 - 简化版（不包含AI名称）
     */
    public static void logException(String operation, String userId, Exception exception) {
        logException(operation, userId, null, exception);
    }
    
    /**
     * 记录异常 - 最简版（只有操作名称）
     */
    public static void logException(String operation, Exception exception) {
        logException(operation, null, null, exception);
    }
    
    /**
     * 记录致命异常（会导致任务完全失败的异常）
     */
    public static void logFatalException(String operation, String userId, String aiName, Exception exception) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String exceptionType = exception.getClass().getSimpleName();
        String message = exception.getMessage() != null ? exception.getMessage() : "无错误消息";
        
        System.err.println("═══════════════════════════════════════");
        System.err.println(String.format("🔥 [致命异常] %s严重失败", operation));
        System.err.println(String.format("   AI: %s", aiName != null ? aiName : "无"));
        System.err.println(String.format("   用户: %s", userId != null ? userId : "无"));
        System.err.println(String.format("   时间: %s", timestamp));
        System.err.println(String.format("   类型: %s", exceptionType));
        System.err.println(String.format("   消息: %s", message));
        System.err.println("   完整堆栈:");
        
        // 打印完整堆栈（最多30层）
        String fullStackTrace = getStackTrace(exception, 30);
        System.err.println(fullStackTrace);
        System.err.println("═══════════════════════════════════════");
    }
    
    /**
     * 记录WebSocket异常
     */
    public static void logWebSocketException(String messageType, String userId, String aiName, Exception exception) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String exceptionType = exception.getClass().getSimpleName();
        String message = exception.getMessage() != null ? exception.getMessage() : "无错误消息";
        String shortMessage = message.length() > 80 ? message.substring(0, 80) + "..." : message;
        
        System.err.println(String.format("⚠️ [WebSocket异常] 消息发送失败 | 类型:%s | AI:%s | 用户:%s", 
            messageType, 
            aiName != null ? aiName : "无", 
            userId != null ? userId : "无"));
        System.err.println(String.format("   时间:%s | 异常:%s | 消息:%s", 
            timestamp, exceptionType, shortMessage));
        
        // WebSocket异常通常不影响数据完整性，只记录前5层堆栈
        String stackTrace = getStackTrace(exception, 5);
        System.err.println(String.format("   堆栈:%s", stackTrace));
    }
    
    /**
     * 记录数据库异常
     */
    public static void logDatabaseException(String tableName, String operation, String userId, 
                                           String aiName, Exception exception) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String exceptionType = exception.getClass().getSimpleName();
        String message = exception.getMessage() != null ? exception.getMessage() : "无错误消息";
        
        System.err.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.err.println(String.format("💾 [数据库异常] %s操作失败 | 表:%s", operation, tableName));
        System.err.println(String.format("   AI:%s | 用户:%s | 时间:%s", 
            aiName != null ? aiName : "无", 
            userId != null ? userId : "无", 
            timestamp));
        System.err.println(String.format("   异常类型:%s", exceptionType));
        System.err.println(String.format("   错误消息:%s", message));
        
        // 数据库异常需要详细堆栈（15层）
        String stackTrace = getStackTrace(exception, 15);
        System.err.println(String.format("   堆栈跟踪:%s", stackTrace));
        System.err.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    /**
     * 记录AI处理异常
     */
    public static void logAIProcessException(String aiName, String userId, String stage, Exception exception) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String exceptionType = exception.getClass().getSimpleName();
        String message = exception.getMessage() != null ? exception.getMessage() : "无错误消息";
        String shortMessage = message.length() > 120 ? message.substring(0, 120) + "..." : message;
        
        System.err.println(String.format("🤖 [AI处理异常] %s | 阶段:%s | 用户:%s | 时间:%s", 
            aiName, stage, 
            userId != null ? userId : "无", 
            timestamp));
        System.err.println(String.format("   异常:%s | 消息:%s", exceptionType, shortMessage));
        
        // AI处理异常记录中等堆栈（10层）
        String stackTrace = getStackTrace(exception, 10);
        System.err.println(String.format("   堆栈:%s", stackTrace));
    }
    
    /**
     * 记录截图异常
     */
    public static void logScreenshotException(String imageName, String userId, Exception exception) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String exceptionType = exception.getClass().getSimpleName();
        String message = exception.getMessage() != null ? exception.getMessage() : "无错误消息";
        String shortMessage = message.length() > 60 ? message.substring(0, 60) + "..." : message;
        
        System.err.println(String.format("📷 [截图异常] 截图失败 | 文件:%s | 用户:%s | 时间:%s", 
            imageName, 
            userId != null ? userId : "无", 
            timestamp));
        System.err.println(String.format("   异常:%s | 消息:%s", exceptionType, shortMessage));
    }
    
    /**
     * 记录网络请求异常
     */
    public static void logNetworkException(String url, String method, String userId, Exception exception) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String exceptionType = exception.getClass().getSimpleName();
        String message = exception.getMessage() != null ? exception.getMessage() : "无错误消息";
        String shortMessage = message.length() > 80 ? message.substring(0, 80) + "..." : message;
        
        System.err.println(String.format("🌐 [网络异常] 请求失败 | 方法:%s | URL:%s", method, url));
        System.err.println(String.format("   用户:%s | 时间:%s", 
            userId != null ? userId : "无", 
            timestamp));
        System.err.println(String.format("   异常:%s | 消息:%s", exceptionType, shortMessage));
    }
    
    /**
     * 记录文件操作异常
     */
    public static void logFileException(String operation, String filePath, Exception exception) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String exceptionType = exception.getClass().getSimpleName();
        String message = exception.getMessage() != null ? exception.getMessage() : "无错误消息";
        
        System.err.println(String.format("📁 [文件异常] %s失败 | 文件:%s | 时间:%s", 
            operation, filePath, timestamp));
        System.err.println(String.format("   异常:%s | 消息:%s", exceptionType, message));
    }
    
    /**
     * 获取异常堆栈跟踪（限制层数）
     * @param exception 异常对象
     * @param maxLines 最大行数
     * @return 格式化的堆栈字符串
     */
    private static String getStackTrace(Exception exception, int maxLines) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        
        String fullStackTrace = sw.toString();
        String[] lines = fullStackTrace.split("\n");
        
        StringBuilder result = new StringBuilder();
        int lineCount = Math.min(lines.length, maxLines);
        
        for (int i = 0; i < lineCount; i++) {
            if (i > 0) result.append("\n      "); // 缩进
            result.append(lines[i].trim());
        }
        
        if (lines.length > maxLines) {
            result.append("\n      ... (还有 ").append(lines.length - maxLines).append(" 行)");
        }
        
        return result.toString();
    }
    
    /**
     * 判断异常是否为致命异常
     */
    public static boolean isFatalException(Throwable throwable) {
        return throwable instanceof NullPointerException
            || throwable instanceof OutOfMemoryError
            || throwable instanceof StackOverflowError
            || (throwable.getCause() != null && throwable.getCause() instanceof OutOfMemoryError);
    }
    
    /**
     * 判断异常是否为网络相关异常
     */
    public static boolean isNetworkException(Exception exception) {
        String className = exception.getClass().getName();
        String message = exception.getMessage() != null ? exception.getMessage().toLowerCase() : "";
        
        return className.contains("SocketException")
            || className.contains("ConnectException")
            || className.contains("TimeoutException")
            || className.contains("UnknownHostException")
            || message.contains("connection")
            || message.contains("timeout")
            || message.contains("network");
    }
    
    /**
     * 判断异常是否为数据库相关异常
     */
    public static boolean isDatabaseException(Exception exception) {
        String className = exception.getClass().getName();
        String message = exception.getMessage() != null ? exception.getMessage().toLowerCase() : "";
        
        return className.contains("SQLException")
            || className.contains("DataAccessException")
            || message.contains("database")
            || message.contains("sql")
            || message.contains("connection pool");
    }
}
