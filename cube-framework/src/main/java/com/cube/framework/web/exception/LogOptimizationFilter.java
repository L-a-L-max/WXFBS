package com.cube.framework.web.exception;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * 日志优化过滤器
 * 简化冗长的异常堆栈输出，只保留关键信息
 */
public class LogOptimizationFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        String loggerName = event.getLoggerName();
        
        // 优化HTTP请求解析错误 - 简化为一行
        if (isHttpParsingError(message, loggerName)) {
            return FilterReply.DENY; // 拒绝原始日志，后续会输出简化版本
        }
        
        // 优化连接重置错误 - 简化为一行
        if (isConnectionResetError(message, loggerName)) {
            return FilterReply.DENY; // 拒绝原始日志，后续会输出简化版本
        }
        
        // 优化异常处理器失败 - 简化为一行
        if (isExceptionHandlerFailure(message, loggerName)) {
            return FilterReply.DENY; // 拒绝原始日志，后续会输出简化版本
        }
        
        // 优化MCP客户端频繁初始化 - 降低日志级别
        if (isMcpClientInit(message, loggerName)) {
            return FilterReply.DENY; // 拒绝原始日志，后续会输出简化版本
        }
        
        return FilterReply.NEUTRAL;
    }
    
    /**
     * 检查是否为HTTP请求解析错误
     */
    private boolean isHttpParsingError(String message, String loggerName) {
        return loggerName.contains("Http11Processor") && 
               message.contains("Error parsing HTTP request header") &&
               message.contains("Invalid character found in method name");
    }
    
    /**
     * 检查是否为连接重置错误
     */
    private boolean isConnectionResetError(String message, String loggerName) {
        return (message.contains("Connection reset by peer") || 
                message.contains("AsyncRequestNotUsableException")) &&
               (loggerName.contains("GlobalExceptionHandler") || 
                loggerName.contains("ExceptionHandlerExceptionResolver"));
    }
    
    /**
     * 检查是否为异常处理器失败
     */
    private boolean isExceptionHandlerFailure(String message, String loggerName) {
        return message.contains("HttpMessageNotWritableException") &&
               message.contains("No converter for") &&
               loggerName.contains("ExceptionHandlerExceptionResolver");
    }
    
    /**
     * 检查是否为MCP客户端初始化
     */
    private boolean isMcpClientInit(String message, String loggerName) {
        return loggerName.contains("McpAsyncServer") &&
               message.contains("Client initialize request");
    }
}
