package com.playwright.aop;

import com.playwright.entity.LogInfo;
import com.playwright.entity.UserInfoRequest;
import com.playwright.entity.mcp.McpResult;
import com.playwright.utils.common.LogMsgUtil;
import com.playwright.utils.common.LoginSessionManager;
import com.playwright.utils.common.RestUtils;
import com.playwright.utils.common.UserLogUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * dateStart 2024/8/4 9:34
 * dateNow   2025/8/8 10:11
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    @Value("${cube.url}")
    private String url;
    @Autowired
    private LogMsgUtil logMsgUtil;
    @Autowired
    private LoginSessionManager loginSessionManager;

    @Pointcut("execution(* com.playwright.controller..*(..))")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = 0;
        LogInfo logInfo = new LogInfo();
        logInfo.setUserId("");
        String description = "无";
        try {
            start = System.currentTimeMillis();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        方法名
            Method method = signature.getMethod();
            String methodName = method.getName();
            logInfo.setMethodName(methodName);
            
            // 移除"进入方法"日志，避免日志冗余
//        获取Operation注解上的summary
            description = "";
            if (method.isAnnotationPresent(Operation.class)) {
                Operation operation = method.getAnnotation(Operation.class);
                description = operation.summary();
                logInfo.setDescription(description);
            }
//        参数
            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                Object arg = args[0];
                if (arg instanceof UserInfoRequest userInfoRequest) {
                    logInfo.setUserId(userInfoRequest.getUserId());
                } else if(arg instanceof Map map) {
                    logInfo.setUserId(map.get("userId").toString());
                }
                else {
                    String str = (String) arg;
                    logInfo.setUserId(str);
                }
            }
            logInfo.setMethodParams(Arrays.toString(args));
        } catch (Exception e) {
            UserLogUtil.sendExceptionLog("无", "aop异常", "logAround", e, url + "/saveLogInfo");
        }
        Object result = null;
        // 最多重试1次（i=0首次执行，i=1重试1次）
        for(int i = 0; i <= 1; i++) {
            try {
                result = joinPoint.proceed();
//            执行成功
                if(result == null) {
                    return null;
                }
                McpResult mcpResult = null;
                String resultStr = "";
                if(result instanceof McpResult) {
                    mcpResult = (McpResult) result;
                } else {
                    resultStr = result.toString();
                }
                if(resultStr.contains("false") || mcpResult != null && mcpResult.getCode() == 204) {
//            如果是登录方法，跳过检测
                    if (logInfo.getMethodName().contains("check")) {
                        break;
                    }

                    if(i < 1) {
                        // 🔥 重试前检查会话状态（防止用户已切换或关闭）
                        String aiName = extractAINameFromMethod(logInfo.getMethodName());
                        if (!aiName.isEmpty()) {
                            String sessionKey = logInfo.getUserId() + "-" + aiName;
                            if (!loginSessionManager.isSessionActive(sessionKey)) {
                                // 会话失效或超时，不再重试
                                break;
                            }
                        }
                        
                        // 记录详细的错误原因
                        String errorDetail = mcpResult != null ? mcpResult.getResult() : resultStr;
                        log.warn("{}执行失败，准备重试 [第{}次失败，错误详情: {}]", logInfo.getMethodName(), i + 1, errorDetail);
                        
                        // 发送详细的错误日志
                        UserLogUtil.sendAIBusinessLog(
                            logInfo.getUserId(), 
                            extractAIName(description), 
                            logInfo.getMethodName(), 
                            "执行失败，准备重试 - 错误详情: " + errorDetail, 
                            System.currentTimeMillis(), 
                            url + "/saveLogInfo"
                        );
                        
                        sendTaskLog(description, logInfo.getUserId(), ",尝试重试");
                        continue;
                    }
                }
                
                // 如果是登录检查方法，输出统一的登录成功日志
                if (logInfo.getMethodName().contains("check") && (logInfo.getMethodName().contains("Login") || logInfo.getMethodName().contains("login"))) {
                    // 判断登录是否成功（返回结果不是"false"且不为空）
                    String loginResult = resultStr;
                    if (mcpResult != null) {
                        loginResult = mcpResult.getResult();
                    }
                    if (loginResult != null && !loginResult.contains("false") && !loginResult.trim().isEmpty()) {
                        // 提取AI名称
                        String aiName = extractAIName(description);
                        if (aiName.equals("未知")) {
                            // 从方法名推断AI名称
                            if (logInfo.getMethodName().contains("YB") || logInfo.getMethodName().contains("YuanBao")) {
                                aiName = "腾讯元宝";
                            } else if (logInfo.getMethodName().contains("Baidu")) {
                                aiName = "百度AI";
                            } else if (logInfo.getMethodName().contains("DeepSeek")) {
                                aiName = "DeepSeek";
                            } else if (logInfo.getMethodName().contains("DouBao")) {
                                aiName = "豆包";
                            } else if (logInfo.getMethodName().contains("Metaso")) {
                                aiName = "秘塔";
                            } else if (logInfo.getMethodName().contains("TongYi")) {
                                aiName = "通义千问";
                            } else if (logInfo.getMethodName().contains("ZHZD") || logInfo.getMethodName().contains("Zhihu")) {
                                aiName = "知乎直答";
                            } else if (logInfo.getMethodName().contains("Tencent")) {
                                aiName = "腾讯元宝";
                            }
                        }
                        // 只记录登录成功的情况，包含用户信息
                        if (!loginResult.contains("未登录")) {
                            String userId = logInfo.getUserId();
                            String userName = loginResult.length() > 10 ? loginResult.substring(0, 10) + "..." : loginResult;
                            System.out.println(String.format("✅ %s登录成功 | 用户:%s | 账号:%s", aiName, userId, userName));
                        }
                    }
                    break;
                }
                
                if(mcpResult != null) {
                    logInfo.setExecutionResult(mcpResult.getResult());
                } else {
                    logInfo.setExecutionResult(resultStr);
                }
                logInfo.setIsSuccess(1);
                logInfo.setExecutionTimeMillis(System.currentTimeMillis() - start);
                RestUtils.post(url + "/saveLogInfo", logInfo);
                break;
            } catch (Throwable e) {
                if(i < 1) {
//              如果是登录方法，跳过检测
                    if (logInfo.getMethodName().contains("check")) {
                        return "false";
                    }
                    
                    // 记录详细的异常信息
                    log.warn("{}执行异常，准备重试 [第{}次失败，异常类型: {}, 异常信息: {}]", 
                        logInfo.getMethodName(), i + 1, e.getClass().getSimpleName(), e.getMessage());
                    
                    // 发送详细的异常日志
                    Exception exception = (e instanceof Exception) ? (Exception) e : new RuntimeException(e);
                    UserLogUtil.sendAIExceptionLog(
                        logInfo.getUserId(), 
                        extractAIName(description), 
                        logInfo.getMethodName(), 
                        exception,
                        System.currentTimeMillis(),
                        "执行失败，准备重试（第" + (i + 1) + "次失败）",
                        url + "/saveLogInfo"
                    );
                    
                    sendTaskLog(description, logInfo.getUserId(), ",尝试重试");
                    continue;
                }
//            执行失败（重试后仍然失败）
                logInfo.setExecutionResult(e.getMessage());
                logInfo.setIsSuccess(0);
                logInfo.setExecutionTimeMillis(System.currentTimeMillis() - start);
                log.error("{}方法执行失败（已重试），详情: {}, 用户ID: {}, 异常: {}", 
                    logInfo.getMethodName(), logInfo.getDescription(), logInfo.getUserId(), e.getMessage());
                
                // 记录最终失败的日志
                RestUtils.post(url + "/saveLogInfo", logInfo);
                
                // 发送最终失败的异常日志
                Exception finalException = (e instanceof Exception) ? (Exception) e : new RuntimeException(e);
                UserLogUtil.sendAIExceptionLog(
                    logInfo.getUserId(), 
                    extractAIName(description), 
                    logInfo.getMethodName(), 
                    finalException,
                    System.currentTimeMillis(),
                    "执行最终失败（已重试1次仍失败）",
                    url + "/saveLogInfo"
                );
                
                //             传递不同ai的错误信息
                sendTaskLog(description, logInfo.getUserId(), "");
                
                // 🔥 检查方法返回类型，避免ClassCastException
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Class<?> returnType = signature.getReturnType();
                
                // 如果返回类型是String，返回"false"字符串
                if (returnType == String.class) {
                    return "false";
                }
                
                // 否则返回McpResult
                if (description.contains("检查")) {
                    return McpResult.fail("用户id:" + logInfo.getUserId()  + description + "未登录", "");
                } else if (description.contains("投递")) {
                    return McpResult.fail("用户id:" + logInfo.getUserId() + description + "失败", "");
                } else {
                    return McpResult.fail("用户id:" + logInfo.getUserId() + description + "失败", "");
                }
            }
        }
        // 移除"返回结果"日志，避免日志冗余（结果已在LogMsgUtil.sendResData中统一输出）
        return result;
    }

    private void sendTaskLog(String description, String userId, String isTryAgain) {
        //             传递不同ai的错误信息
        if (description.contains("DeepSeek")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "DeepSeek");
        }
        if (description.contains("豆包")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "豆包");
        }
        if (description.contains("MiniMax")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "MiniMax");
        }
        if (description.contains("秘塔")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "秘塔");
        }
        if (description.contains("KiMi")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "KiMi");
        }
        if (description.contains("通义千问")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "通义千问");
        }
        if (description.contains("百度AI")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "百度AI");
        }
        if (description.contains("腾讯元宝T1")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "腾讯元宝T1");
        }
        if (description.contains("腾讯元宝DS")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "腾讯元宝DS");
        }
        if (description.contains("知乎直答")) {
            logMsgUtil.sendTaskLog(description + "执行失败" + isTryAgain, userId, "知乎直答");
        }
    }

    /**
     * 从描述中提取AI名称
     * @param description 方法描述
     * @return AI名称，如果没有匹配则返回"未知"
     */
    private String extractAIName(String description) {
        if (description == null) {
            return "未知";
        }
        if (description.contains("DeepSeek")) return "DeepSeek";
        if (description.contains("豆包")) return "豆包";
        if (description.contains("MiniMax")) return "MiniMax";
        if (description.contains("秘塔")) return "秘塔";
        if (description.contains("KiMi")) return "KiMi";
        if (description.contains("通义千问")) return "通义千问";
        if (description.contains("百度AI")) return "百度AI";
        if (description.contains("腾讯元宝T1")) return "腾讯元宝T1";
        if (description.contains("腾讯元宝DS")) return "腾讯元宝DS";
        if (description.contains("知乎直答") || description.contains("知乎")) return "知乎直答";
        return "未知";
    }

    /**
     * 从方法名中提取AI名称（用于会话键构建）
     * 必须与LoginSessionManager中使用的AI名称保持一致
     * @param methodName 方法名
     * @return AI名称，如果无法提取则返回空字符串
     */
    private String extractAINameFromMethod(String methodName) {
        if (methodName == null) {
            return "";
        }
        // 获取登录二维码方法：getBaiduQrCode, getDBQrCode, getDSQrCode等
        if (methodName.contains("Baidu")) return "Baidu";
        if (methodName.contains("DB") && methodName.contains("QrCode")) return "Doubao";
        if (methodName.contains("DS") && methodName.contains("QrCode")) return "DeepSeek";
        if (methodName.contains("YB") && methodName.contains("QrCode")) return "YuanBao";
        if (methodName.contains("Metaso")) return "Metaso";
        if (methodName.contains("TongYi") || methodName.contains("QW")) return "TongYi";
        if (methodName.contains("Zhihu") || methodName.contains("ZHZD")) return "知乎直答";
        return "";
    }

}
