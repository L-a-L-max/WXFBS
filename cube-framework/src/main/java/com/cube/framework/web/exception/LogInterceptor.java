package com.cube.framework.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.regex.Pattern;

/**
 * 日志拦截器
 * 拦截并简化特定的错误日志输出
 */
@Component
public class LogInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    
    @Autowired
    private SimpleLogHandler simpleLogHandler;
    
    // 正则表达式模式
    private static final Pattern HTTP_PARSING_ERROR_PATTERN = 
        Pattern.compile("Invalid character found in method name.*HTTP method names must be tokens");
    
    private static final Pattern CONNECTION_RESET_PATTERN = 
        Pattern.compile("Connection reset by peer");
    
    private static final Pattern MCP_CLIENT_INIT_PATTERN = 
        Pattern.compile("Client initialize request.*Protocol.*Capabilities.*Implementation");
    
    @PostConstruct
    public void init() {
        log.info("🔧 [日志优化] 日志拦截器已启动，将简化冗长的错误输出");
    }
    
    /**
     * 拦截并处理HTTP解析错误
     */
    public boolean interceptHttpParsingError(String logMessage) {
        if (HTTP_PARSING_ERROR_PATTERN.matcher(logMessage).find()) {
            // 提取客户端信息（如果有）
            String clientInfo = extractClientInfo(logMessage);
            simpleLogHandler.handleHttpParsingError(clientInfo);
            return true; // 表示已处理，不需要输出原始日志
        }
        return false;
    }
    
    /**
     * 拦截并处理连接重置错误
     */
    public boolean interceptConnectionResetError(String logMessage, String uri) {
        if (CONNECTION_RESET_PATTERN.matcher(logMessage).find()) {
            simpleLogHandler.handleConnectionResetError(uri);
            return true; // 表示已处理，不需要输出原始日志
        }
        return false;
    }
    
    /**
     * 拦截并处理MCP客户端初始化
     */
    public boolean interceptMcpClientInit(String logMessage) {
        if (MCP_CLIENT_INIT_PATTERN.matcher(logMessage).find()) {
            String clientName = extractMcpClientName(logMessage);
            String protocol = extractMcpProtocol(logMessage);
            simpleLogHandler.handleMcpClientInit(clientName, protocol);
            return true; // 表示已处理，不需要输出原始日志
        }
        return false;
    }
    
    /**
     * 提取客户端信息
     */
    private String extractClientInfo(String logMessage) {
        // 尝试从错误消息中提取IP或其他客户端标识
        if (logMessage.contains("175.178.154.216")) {
            return "175.178.154.216";
        }
        return "未知客户端";
    }
    
    /**
     * 提取MCP客户端名称
     */
    private String extractMcpClientName(String logMessage) {
        if (logMessage.contains("lke-mcp-client")) {
            return "lke-mcp-client";
        }
        return "未知MCP客户端";
    }
    
    /**
     * 提取MCP协议版本
     */
    private String extractMcpProtocol(String logMessage) {
        if (logMessage.contains("2024-11-05")) {
            return "2024-11-05";
        }
        return "未知协议";
    }
    
    /**
     * 检查是否为需要拦截的日志
     */
    public boolean shouldIntercept(String loggerName, String logMessage) {
        // HTTP解析错误
        if (loggerName.contains("Http11Processor") && 
            logMessage.contains("Error parsing HTTP request header")) {
            return interceptHttpParsingError(logMessage);
        }
        
        // MCP客户端初始化
        if (loggerName.contains("McpAsyncServer") && 
            logMessage.contains("Client initialize request")) {
            return interceptMcpClientInit(logMessage);
        }
        
        // 连接重置错误
        if (logMessage.contains("Connection reset by peer")) {
            return interceptConnectionResetError(logMessage, "/");
        }
        
        return false;
    }
}
