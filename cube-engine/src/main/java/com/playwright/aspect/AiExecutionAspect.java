package com.playwright.aspect;

import com.playwright.config.AITypeRegistry;
import com.playwright.entity.UserInfoRequest;
import com.playwright.service.AiResultPersistenceService;
import com.playwright.utils.common.LogMsgUtil;
import com.playwright.utils.common.UserLogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * AI执行统一异常处理切面
 * 捕获所有AI执行异常，记录详细日志
 * @author 优立方
 * @date 2025-01-21
 */
@Aspect
@Component
public class AiExecutionAspect {

    @Autowired
    private UserLogUtil userLogUtil;
    
    @Autowired
    private LogMsgUtil logMsgUtil;
    
    @Autowired
    private AiResultPersistenceService persistenceService;
    
    @Value("${cube.url}")
    private String url;
    
    private final AITypeRegistry aiRegistry = new AITypeRegistry();
    
    /**
     * 切入点：AIGCController中所有以start开头的方法
     */
    @Pointcut("execution(* com.playwright.controller.ai.AIGCController.start*(..))")
    public void aiExecutionMethods() {}
    
    /**
     * 切入点：所有AI工具类的保存方法
     */
    @Pointcut("execution(* com.playwright.utils.ai.*.save*(..))")
    public void aiSaveMethods() {}
    
    /**
     * 切入点：结果持久化服务
     */
    @Pointcut("execution(* com.playwright.service.AiResultPersistenceService.saveAndSendResult(..))")
    public void persistenceMethods() {}
    
    /**
     * 环绕通知：统一处理AI执行异常
     */
    @Around("aiExecutionMethods()")
    public Object handleAiExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        long startTime = System.currentTimeMillis();
        
        // 提取AI名称
        String aiName = extractAiName(methodName);
        String userId = extractUserId(joinPoint.getArgs());
        UserInfoRequest request = extractRequest(joinPoint.getArgs());
        
        System.out.println(String.format("▶️ [AI执行开始] %s | 用户:%s | 方法:%s", 
            aiName, userId, methodName));
        
        try {
            // 执行原方法
            Object result = joinPoint.proceed();
            
            long duration = System.currentTimeMillis() - startTime;
            System.out.println(String.format("✅ [AI执行成功] %s | 用户:%s | 耗时:%dms", 
                aiName, userId, duration));
            
            // 🔥 不再记录成功日志到数据库，由AiResultPersistenceService统一记录最终结果
            
            return result;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            
            // 详细的异常信息
            String errorMessage = formatErrorMessage(e);
            String userFriendlyMessage = getUserFriendlyErrorMessage(aiName, e);
            
            System.err.println(String.format("❌ [AI执行失败] %s | 用户:%s | 耗时:%dms | 错误:%s", 
                aiName, userId, duration, errorMessage));
            
            // 1. 保存详细错误到数据库
            String dbId = UserLogUtil.sendExceptionLogWithId(
                userId,
                String.format("%s执行失败", aiName),
                methodName,
                e,
                url + "/saveLogInfo"
            );
            
            System.out.println(String.format("📝 [错误已记录] 数据库ID:%s", dbId));
            
            // 2. 发送友好的错误消息到前端
            try {
                AITypeRegistry.AIConfig aiConfig = aiRegistry.getByMethodName(methodName);
                if (aiConfig != null) {
                    // 🔥 修复：按照正确的参数顺序发送错误消息
                    // 参数顺序：content, userId, aiName, type, shareUrl, shareImgUrl, taskId
                    logMsgUtil.sendResData(
                        String.format("❌ %s\n\n详细错误已记录，错误ID: %s\n请联系技术支持或稍后重试。", 
                            userFriendlyMessage, dbId),          // content
                        userId,                                   // userId
                        aiName,                                   // aiName
                        aiConfig.getMessageType(),                // type
                        "",                                       // shareUrl
                        "",                                       // shareImgUrl
                        request != null ? request.getTaskId() : "" // taskId
                    );
                }
            } catch (Exception sendError) {
                System.err.println("发送错误消息失败: " + sendError.getMessage());
            }
            
            // 3. 尝试保存部分结果（如果有）
            tryToSavePartialResult(userId, aiName, request, e);
            
            // 重新抛出异常（让上层知道失败了）
            throw e;
        }
    }
    
    /**
     * 环绕通知：确保持久化一定成功
     */
    @Around("persistenceMethods()")
    public Object ensurePersistence(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // 持久化失败是严重问题，记录详细日志
            System.err.println("❌ [持久化失败] 错误: " + e.getMessage());
            
            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                String userId = args[0].toString();
                UserLogUtil.sendExceptionLogWithId(
                    userId,
                    "AI结果持久化失败",
                    "ensurePersistence",
                    e,
                    url + "/saveLogInfo"
                );
            }
            
            throw e;
        }
    }
    
    /**
     * 从方法名提取AI名称
     */
    private String extractAiName(String methodName) {
        AITypeRegistry.AIConfig config = aiRegistry.getByMethodName(methodName);
        return config != null ? config.getName() : "未知AI";
    }
    
    /**
     * 从参数中提取用户ID
     */
    private String extractUserId(Object[] args) {
        if (args == null || args.length == 0) {
            return "未知用户";
        }
        
        for (Object arg : args) {
            if (arg instanceof UserInfoRequest) {
                return ((UserInfoRequest) arg).getUserId();
            }
        }
        
        return "未知用户";
    }
    
    /**
     * 从参数中提取请求对象
     */
    private UserInfoRequest extractRequest(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        
        for (Object arg : args) {
            if (arg instanceof UserInfoRequest) {
                return (UserInfoRequest) arg;
            }
        }
        
        return null;
    }
    
    /**
     * 格式化错误消息
     */
    private String formatErrorMessage(Exception e) {
        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            return e.getMessage().length() > 100 
                ? e.getMessage().substring(0, 100) + "..." 
                : e.getMessage();
        }
        return e.getClass().getSimpleName();
    }
    
    /**
     * 获取用户友好的错误消息
     */
    private String getUserFriendlyErrorMessage(String aiName, Exception e) {
        String errorType = e.getClass().getSimpleName();
        String errorMsg = e.getMessage() != null ? e.getMessage() : "";
        
        // 根据异常类型返回友好提示
        if (errorType.contains("Timeout") || errorMsg.contains("timeout")) {
            return String.format("%s响应超时，请稍后重试", aiName);
        } else if (errorType.contains("Network") || errorMsg.contains("network")) {
            return String.format("%s网络连接失败，请检查网络后重试", aiName);
        } else if (errorType.contains("Login") || errorMsg.contains("login")) {
            return String.format("%s登录状态失效，请重新登录", aiName);
        } else if (errorType.contains("TargetClosed") || errorMsg.contains("closed")) {
            return String.format("%s页面已关闭，请重新尝试", aiName);
        } else {
            return String.format("%s执行过程中出现异常", aiName);
        }
    }
    
    /**
     * 尝试保存部分结果（容错机制）
     */
    private void tryToSavePartialResult(String userId, String aiName, UserInfoRequest request, Exception error) {
        try {
            // 即使失败，也要记录用户的请求和失败信息
            persistenceService.saveAndSendResult(
                userId,
                aiName,
                String.format("执行失败: %s", formatErrorMessage(error)),
                null,
                null,
                null,
                request
            );
        } catch (Exception e) {
            System.err.println("保存部分结果失败: " + e.getMessage());
        }
    }
}
