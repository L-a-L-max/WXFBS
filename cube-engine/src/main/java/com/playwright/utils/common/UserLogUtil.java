package com.playwright.utils.common;

import com.playwright.entity.LogInfo;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户日志工具类，提供详细的日志记录功能
 * @author muyou
 * description: 增强版日志记录工具，支持智能体错误信息记录、日志去重
 * dateStart 2024/8/4 9:34
 * dateNow   2025/10/23 16:00
 */
public class UserLogUtil {
    
    // 日志去重缓存：key = userId_aiName_operation_message, value = 上次记录时间戳
    private static final Map<String, Long> LOG_DEDUP_CACHE = new ConcurrentHashMap<>();
    
    // 默认去重时间间隔（30秒）
    private static final long DEFAULT_DEDUP_INTERVAL_MS = 30000;
    
    /**
     * 检查是否应该记录日志（去重机制）
     * @param userId 用户ID
     * @param aiName AI名称
     * @param operation 操作名称
     * @param message 日志消息
     * @param intervalMs 去重时间间隔（毫秒）
     * @return true表示应该记录，false表示重复需跳过
     */
    private static boolean shouldLog(String userId, String aiName, String operation, String message, long intervalMs) {
        String key = String.format("%s_%s_%s_%s", userId, aiName, operation, message);
        long now = System.currentTimeMillis();
        Long lastLogTime = LOG_DEDUP_CACHE.get(key);
        
        if (lastLogTime == null || (now - lastLogTime) > intervalMs) {
            LOG_DEDUP_CACHE.put(key, now);
            return true;
        }
        return false;
    }
    
    /**
     * 清理过期的去重缓存（可选，定期调用）
     */
    public static void cleanExpiredDedupCache() {
        long now = System.currentTimeMillis();
        LOG_DEDUP_CACHE.entrySet().removeIf(entry -> 
            (now - entry.getValue()) > 300000); // 清理5分钟前的记录
    }
    
    /**
     * 发送日志的核心方法
     */
    private static void sendLog(String userId, String description, String methodName, Exception e, Integer isSuccess, Long startTime, String result, String url) {
        try {
            LogInfo logInfo = new LogInfo();
            logInfo.setUserId(StringUtils.hasText(userId) ? userId : "未知用户");
            logInfo.setMethodName(StringUtils.hasText(methodName) ? methodName : "未知方法");
            logInfo.setDescription(StringUtils.hasText(description) ? description : "无描述");
            
            if(e != null) {
                String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                
                // 🔥 增强：添加详细的堆栈信息
                StringBuilder detailedError = new StringBuilder();
                detailedError.append(String.format("错误类型：%s | 错误信息：%s | 发生时间：%s", 
                    e.getClass().getSimpleName(), errorMessage, LocalDateTime.now()));
                
                // 添加堆栈跟踪信息（取前5层，避免过长）
                StackTraceElement[] stackTrace = e.getStackTrace();
                if (stackTrace != null && stackTrace.length > 0) {
                    detailedError.append("\n【堆栈信息】：");
                    int maxStackLines = Math.min(5, stackTrace.length);
                    for (int i = 0; i < maxStackLines; i++) {
                        StackTraceElement element = stackTrace[i];
                        detailedError.append(String.format("\n  → %s.%s(%s:%d)", 
                            element.getClassName(),
                            element.getMethodName(),
                            element.getFileName(),
                            element.getLineNumber()));
                    }
                    if (stackTrace.length > maxStackLines) {
                        detailedError.append(String.format("\n  ... 还有 %d 层堆栈", stackTrace.length - maxStackLines));
                    }
                }
                
                // 添加原因链（如果存在）
                Throwable cause = e.getCause();
                if (cause != null) {
                    detailedError.append(String.format("\n【根本原因】：%s - %s", 
                        cause.getClass().getSimpleName(), 
                        cause.getMessage()));
                }
                
                // 🔥 添加可能的解决方案提示
                String possibleSolution = getPossibleSolution(e);
                if (possibleSolution != null) {
                    detailedError.append("\n【可能原因】：").append(possibleSolution);
                }
                
                logInfo.setExecutionResult(detailedError.toString());
            } else {
                logInfo.setExecutionResult(StringUtils.hasText(result) ? result : "执行成功");
            }
            
            long executionTime = startTime != null ? System.currentTimeMillis() - startTime : 0;
            logInfo.setExecutionTimeMillis(executionTime);
            logInfo.setExecutionTime(LocalDateTime.now());
            logInfo.setMethodParams("通过UserLogUtil记录");
            logInfo.setIsSuccess(isSuccess);
            
            RestUtils.post(url, logInfo);
        } catch (Exception ex) {
            // 避免日志记录本身出现异常影响主流程
            System.err.println("UserLogUtil记录日志失败: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * 根据异常类型提供可能的解决方案
     */
    private static String getPossibleSolution(Exception e) {
        String exceptionType = e.getClass().getSimpleName();
        String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        
        // 超时相关
        if (exceptionType.contains("Timeout") || message.contains("timeout")) {
            if (message.contains("selector") || message.contains("选择器")) {
                return "页面元素未在预期时间内出现，可能是：1) 页面加载过慢 2) 选择器不正确 3) 元素被隐藏或未渲染 4) 网络延迟";
            }
            return "操作超时，可能是：1) 网络连接不稳定 2) AI响应时间过长 3) 页面加载缓慢 4) 服务器压力大";
        }
        
        // 元素相关
        if (message.contains("element") || message.contains("元素")) {
            if (message.contains("not visible") || message.contains("不可见")) {
                return "元素存在但不可见，可能是：1) 元素被其他元素遮挡 2) CSS样式隐藏了元素 3) 元素在可视区域外 4) 页面结构发生变化";
            }
            if (message.contains("not found") || message.contains("未找到")) {
                return "元素未找到，可能是：1) 页面结构已更新 2) 选择器表达式错误 3) 页面未完全加载 4) 需要登录或权限";
            }
        }
        
        // 页面相关
        if (message.contains("page") || message.contains("页面")) {
            if (message.contains("closed") || message.contains("关闭")) {
                return "页面已关闭，可能是：1) 用户手动关闭了浏览器 2) 会话超时 3) 程序异常导致页面关闭 4) 浏览器崩溃";
            }
        }
        
        // 网络相关
        if (exceptionType.contains("Network") || message.contains("network") || message.contains("网络")) {
            return "网络错误，可能是：1) 网络连接中断 2) DNS解析失败 3) 代理服务器问题 4) 目标服务器不可达";
        }
        
        // WebSocket相关
        if (message.contains("websocket") || message.contains("ws://") || message.contains("wss://")) {
            return "WebSocket连接问题，可能是：1) 浏览器连接断开 2) 网络不稳定 3) 服务端主动断开 4) 防火墙阻止连接";
        }
        
        // 剪贴板相关
        if (message.contains("clipboard") || message.contains("剪贴板")) {
            return "剪贴板操作失败，可能是：1) 浏览器权限限制 2) 内容为空 3) JavaScript执行失败 4) 系统剪贴板被占用";
        }
        
        // 运行时异常
        if (exceptionType.equals("RuntimeException")) {
            if (message.contains("等待") && message.contains("超时")) {
                return "等待操作超时，可能是：1) AI思考时间超过预期（深度思考模式需要更长时间）2) 页面响应缓慢 3) 网络延迟 4) 页面卡死";
            }
        }
        
        return null;
    }
    
    /**
     * 记录异常日志（不包含开始时间）
     */
    public static void sendExceptionLog(String userId, String description, String methodName, Exception e, String url) {
        sendLog(userId, description, methodName, e, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录异常日志（包含开始时间）
     */
    public static void sendExceptionLog(String userId, String description, String methodName, Exception e, Long startTime, String url) {
        sendLog(userId, description, methodName, e, 0, startTime, null, url);
    }

    /**
     * 记录正常执行日志
     */
    public static void sendNormalLog(String userId, String description, String methodName, Long startTime, String result, String url) {
        sendLog(userId, description, methodName, null, 1, startTime, result, url);
    }
    
    /**
     * 记录智能体特定的异常日志
     */
    public static void sendAIExceptionLog(String userId, String aiName, String methodName, Exception e, Long startTime, String additionalInfo, String url) {
        String description = String.format("智能体异常 | AI：%s | 附加信息：%s", 
            StringUtils.hasText(aiName) ? aiName : "未知AI", 
            StringUtils.hasText(additionalInfo) ? additionalInfo : "无");
        sendLog(userId, description, methodName, e, 0, startTime, null, url);
    }
    
    /**
     * 记录智能体操作超时异常
     */
    public static void sendAITimeoutLog(String userId, String aiName, String operation, Exception e, String elementInfo, String url) {
        String description = String.format("智能体操作超时 | AI：%s | 操作：%s", aiName, operation);
        sendLog(userId, description, "AI操作", e, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录智能体元素不可见异常
     */
    public static void sendElementNotVisibleLog(String userId, String aiName, String selector, String pageUrl, String url) {
        Exception elementException = new Exception(String.format(
            "元素不可见 | AI：%s | 选择器：%s | 页面URL：%s", 
            aiName, selector, pageUrl));
        String description = String.format("智能体元素异常 | AI：%s | 元素不可见", aiName);
        sendLog(userId, description, "元素操作", elementException, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录智能体登录状态异常
     */
    public static void sendLoginStatusLog(String userId, String aiName, String statusInfo, String url) {
        Exception loginException = new Exception(String.format(
            "登录状态异常 | AI：%s | 状态信息：%s", 
            aiName, statusInfo));
        String description = String.format("智能体登录异常 | AI：%s", aiName);
        sendLog(userId, description, "登录检查", loginException, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录智能体业务执行异常
     */
    public static void sendAIBusinessLog(String userId, String aiName, String businessType, String errorInfo, Long startTime, String url) {
        Exception businessException = new Exception(String.format(
            "业务执行异常 | AI：%s | 业务类型：%s | 错误信息：%s", 
            aiName, businessType, errorInfo));
        String description = String.format("智能体业务异常 | AI：%s | 业务：%s", aiName, businessType);
        sendLog(userId, description, businessType, businessException, 0, startTime, null, url);
    }
    
    /**
     * 记录智能体成功执行日志
     */
    public static void sendAISuccessLog(String userId, String aiName, String operation, String result, Long startTime, String url) {
        String description = String.format("智能体执行成功 | AI：%s | 操作：%s", aiName, operation);
        String detailedResult = String.format("成功执行 | AI：%s | 操作：%s | 结果：%s", aiName, operation, result);
        sendLog(userId, description, operation, null, 1, startTime, detailedResult, url);
    }
    
    /**
     * 记录智能体警告日志（原版，无去重）
     */
    public static void sendAIWarningLog(String userId, String aiName, String operation, String warningMessage, String url) {
        sendAIWarningLogWithDedup(userId, aiName, operation, warningMessage, url, 0); // 0表示不去重
    }
    
    /**
     * 记录智能体警告日志（带去重机制）
     * @param dedupIntervalMs 去重时间间隔（毫秒），0表示不去重
     */
    public static void sendAIWarningLogWithDedup(String userId, String aiName, String operation, String warningMessage, String url, long dedupIntervalMs) {
        // 如果启用去重且不应记录，则跳过
        if (dedupIntervalMs > 0 && !shouldLog(userId, aiName, operation, warningMessage, dedupIntervalMs)) {
            return;
        }
        
        Exception warningException = new Exception(String.format(
            "警告信息 | AI：%s | 操作：%s | 警告：%s", 
            aiName, operation, warningMessage));
        String description = String.format("智能体警告 | AI：%s | 操作：%s", aiName, operation);
        sendLog(userId, description, operation, warningException, 1, System.currentTimeMillis(), null, url);
    }
    
    // ==================== 技术错误和警告专用日志方法 ====================
    
    /**
     * 记录页面相关错误日志
     * 包括：页面关闭、页面导航失败、页面加载超时
     */
    public static void sendPageErrorLog(String userId, String aiName, String pageOperation, String errorDetails, String url) {
        Exception pageException = new Exception(String.format(
            "页面错误 | AI：%s | 操作：%s | 详情：%s", 
            aiName, pageOperation, errorDetails));
        String description = String.format("页面操作错误 | AI：%s | 操作：%s", aiName, pageOperation);
        sendLog(userId, description, "页面操作", pageException, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录页面相关警告日志（原版，无去重）
     */
    public static void sendPageWarningLog(String userId, String aiName, String pageOperation, String warningDetails, String url) {
        sendPageWarningLogWithDedup(userId, aiName, pageOperation, warningDetails, url, 0);
    }
    
    /**
     * 记录页面相关警告日志（带去重机制）
     * @param dedupIntervalMs 去重时间间隔（毫秒），0表示不去重
     */
    public static void sendPageWarningLogWithDedup(String userId, String aiName, String pageOperation, String warningDetails, String url, long dedupIntervalMs) {
        if (dedupIntervalMs > 0 && !shouldLog(userId, aiName, pageOperation, warningDetails, dedupIntervalMs)) {
            return;
        }
        
        Exception pageWarning = new Exception(String.format(
            "页面警告 | AI：%s | 操作：%s | 详情：%s", 
            aiName, pageOperation, warningDetails));
        String description = String.format("页面操作警告 | AI：%s | 操作：%s", aiName, pageOperation);
        sendLog(userId, description, "页面操作", pageWarning, 1, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录元素相关错误日志
     * 包括：元素未找到、元素不可见、元素不可点击
     */
    public static void sendElementErrorLog(String userId, String aiName, String elementOperation, String selector, String errorDetails, String url) {
        Exception elementException = new Exception(String.format(
            "元素错误 | AI：%s | 操作：%s | 选择器：%s | 详情：%s", 
            aiName, elementOperation, selector, errorDetails));
        String description = String.format("元素操作错误 | AI：%s | 操作：%s", aiName, elementOperation);
        sendLog(userId, description, "元素操作", elementException, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录元素相关警告日志
     */
    public static void sendElementWarningLog(String userId, String aiName, String elementOperation, String selector, String warningDetails, String url) {
        Exception elementWarning = new Exception(String.format(
            "元素警告 | AI：%s | 操作：%s | 选择器：%s | 详情：%s", 
            aiName, elementOperation, selector, warningDetails));
        String description = String.format("元素操作警告 | AI：%s | 操作：%s", aiName, elementOperation);
        sendLog(userId, description, "元素操作", elementWarning, 1, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录网络相关错误日志
     * 包括：网络超时、请求失败
     */
    public static void sendNetworkErrorLog(String userId, String aiName, String networkOperation, String errorDetails, String url) {
        Exception networkException = new Exception(String.format(
            "网络错误 | AI：%s | 操作：%s | 详情：%s", 
            aiName, networkOperation, errorDetails));
        String description = String.format("网络操作错误 | AI：%s | 操作：%s", aiName, networkOperation);
        sendLog(userId, description, "网络操作", networkException, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录网络相关警告日志
     */
    public static void sendNetworkWarningLog(String userId, String aiName, String networkOperation, String warningDetails, String url) {
        Exception networkWarning = new Exception(String.format(
            "网络警告 | AI：%s | 操作：%s | 详情：%s", 
            aiName, networkOperation, warningDetails));
        String description = String.format("网络操作警告 | AI：%s | 操作：%s", aiName, networkOperation);
        sendLog(userId, description, "网络操作", networkWarning, 1, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录内容相关错误日志（原版，无去重）
     * 包括：内容为空、内容格式错误
     */
    public static void sendContentErrorLog(String userId, String aiName, String contentOperation, String errorDetails, String url) {
        sendContentErrorLogWithDedup(userId, aiName, contentOperation, errorDetails, url, 0);
    }
    
    /**
     * 记录内容相关错误日志（带去重机制）
     * @param dedupIntervalMs 去重时间间隔（毫秒），0表示不去重
     */
    public static void sendContentErrorLogWithDedup(String userId, String aiName, String contentOperation, String errorDetails, String url, long dedupIntervalMs) {
        if (dedupIntervalMs > 0 && !shouldLog(userId, aiName, contentOperation, errorDetails, dedupIntervalMs)) {
            return;
        }
        
        Exception contentException = new Exception(String.format(
            "内容错误 | AI：%s | 操作：%s | 详情：%s", 
            aiName, contentOperation, errorDetails));
        String description = String.format("内容处理错误 | AI：%s | 操作：%s", aiName, contentOperation);
        sendLog(userId, description, "内容处理", contentException, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录内容相关警告日志
     */
    public static void sendContentWarningLog(String userId, String aiName, String contentOperation, String warningDetails, String url) {
        Exception contentWarning = new Exception(String.format(
            "内容警告 | AI：%s | 操作：%s | 详情：%s", 
            aiName, contentOperation, warningDetails));
        String description = String.format("内容处理警告 | AI：%s | 操作：%s", aiName, contentOperation);
        sendLog(userId, description, "内容处理", contentWarning, 1, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录剪贴板相关错误日志
     * 包括：剪贴板读取失败
     */
    public static void sendClipboardErrorLog(String userId, String aiName, String clipboardOperation, String errorDetails, String url) {
        Exception clipboardException = new Exception(String.format(
            "剪贴板错误 | AI：%s | 操作：%s | 详情：%s", 
            aiName, clipboardOperation, errorDetails));
        String description = String.format("剪贴板操作错误 | AI：%s | 操作：%s", aiName, clipboardOperation);
        sendLog(userId, description, "剪贴板操作", clipboardException, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录剪贴板相关警告日志
     */
    public static void sendClipboardWarningLog(String userId, String aiName, String clipboardOperation, String warningDetails, String url) {
        Exception clipboardWarning = new Exception(String.format(
            "剪贴板警告 | AI：%s | 操作：%s | 详情：%s", 
            aiName, clipboardOperation, warningDetails));
        String description = String.format("剪贴板操作警告 | AI：%s | 操作：%s", aiName, clipboardOperation);
        sendLog(userId, description, "剪贴板操作", clipboardWarning, 1, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录JavaScript执行错误日志
     * 包括：JS evaluate失败
     */
    public static void sendJavaScriptErrorLog(String userId, String aiName, String jsOperation, String jsCode, String errorDetails, String url) {
        Exception jsException = new Exception(String.format(
            "JavaScript错误 | AI：%s | 操作：%s | 代码：%s | 详情：%s", 
            aiName, jsOperation, jsCode, errorDetails));
        String description = String.format("JavaScript执行错误 | AI：%s | 操作：%s", aiName, jsOperation);
        sendLog(userId, description, "JavaScript执行", jsException, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录JavaScript执行警告日志
     */
    public static void sendJavaScriptWarningLog(String userId, String aiName, String jsOperation, String jsCode, String warningDetails, String url) {
        Exception jsWarning = new Exception(String.format(
            "JavaScript警告 | AI：%s | 操作：%s | 代码：%s | 详情：%s", 
            aiName, jsOperation, jsCode, warningDetails));
        String description = String.format("JavaScript执行警告 | AI：%s | 操作：%s", aiName, jsOperation);
        sendLog(userId, description, "JavaScript执行", jsWarning, 1, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录WebSocket相关错误日志
     */
    public static void sendWebSocketErrorLog(String userId, String aiName, String wsOperation, String errorDetails, String url) {
        Exception wsException = new Exception(String.format(
            "WebSocket错误 | AI：%s | 操作：%s | 详情：%s", 
            aiName, wsOperation, errorDetails));
        String description = String.format("WebSocket操作错误 | AI：%s | 操作：%s", aiName, wsOperation);
        sendLog(userId, description, "WebSocket操作", wsException, 0, System.currentTimeMillis(), null, url);
    }
    
    /**
     * 记录WebSocket相关警告日志
     */
    public static void sendWebSocketWarningLog(String userId, String aiName, String wsOperation, String warningDetails, String url) {
        Exception wsWarning = new Exception(String.format(
            "WebSocket警告 | AI：%s | 操作：%s | 详情：%s", 
            aiName, wsOperation, warningDetails));
        String description = String.format("WebSocket操作警告 | AI：%s | 操作：%s", aiName, wsOperation);
        sendLog(userId, description, "WebSocket操作", wsWarning, 1, System.currentTimeMillis(), null, url);
    }
}
