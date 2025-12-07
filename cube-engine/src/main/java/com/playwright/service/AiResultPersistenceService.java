package com.playwright.service;

import com.playwright.config.AITypeRegistry;
import com.playwright.entity.AiResult;
import com.playwright.entity.UserInfoRequest;
import com.playwright.utils.common.ExceptionLogger;
import com.playwright.utils.common.LogMsgUtil;
import com.playwright.utils.common.RestUtils;
import com.playwright.utils.common.UserLogUtil;
import com.playwright.websocket.WebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * AI结果持久化服务
 * 确保所有AI结果都被可靠保存到数据库和稿库
 * 
 * @author 优立方
 * @date 2025-01-21
 */
@Service
public class AiResultPersistenceService {
    
    @Autowired
    private LogMsgUtil logMsgUtil;
    
    @Autowired
    private WebSocketClientService webSocketClientService;
    
    @Value("${cube.url}")
    private String apiUrl;
    
    private final AITypeRegistry aiRegistry = new AITypeRegistry();
    
    // 持久化重试线程池
    private final ExecutorService persistenceExecutor = Executors.newFixedThreadPool(5);
    
    // 持久化任务队列（防止丢失）
    private final BlockingQueue<PersistenceTask> taskQueue = new LinkedBlockingQueue<>(1000);

    /**
     * 持久化任务
     */
    private static class PersistenceTask {
        String userId;
        String aiName;
        String content;
        String shareUrl;
        String shareImgUrl;
        String chatId;
        UserInfoRequest request;
        int retryCount;
        boolean dbAlreadySaved = false; // 🔥 标记数据库是否已保存（用于WebSocket重试）
        
        PersistenceTask(String userId, String aiName, String content, String shareUrl, 
                       String shareImgUrl, String chatId, UserInfoRequest request) {
            this.userId = userId;
            this.aiName = aiName;
            this.content = content;
            this.shareUrl = shareUrl;
            this.shareImgUrl = shareImgUrl;
            this.chatId = chatId;
            this.request = request;
            this.retryCount = 0;
        }
    }

    /**
     * 保存AI结果并发送消息（核心方法）
     * 🔥 企业级保证：数据库保存和前端通知都成功，失败自动重试
     * 
     * @param userId 用户ID
     * @param aiName AI名称
     * @param content 生成内容
     * @param shareUrl 分享链接
     * @param shareImgUrl 截图链接
     * @param chatId 会话ID
     * @param request 原始请求
     * @return 是否保存成功
     */
    public boolean saveAndSendResult(String userId, String aiName, String content, 
                                    String shareUrl, String shareImgUrl, String chatId,
                                    UserInfoRequest request) {
        // 🔥 参数校验：确保必要参数不为空
        if (userId == null || userId.isEmpty()) {
            System.err.println("❌ [参数错误] userId不能为空");
            return false;
        }
        if (aiName == null || aiName.isEmpty()) {
            System.err.println("❌ [参数错误] aiName不能为空");
            return false;
        }
        if (content == null || content.isEmpty()) {
            System.err.println("❌ [参数错误] content不能为空");
            return false;
        }
        
        try {
            // 1. 🔥🔥 立即保存到数据库（最高优先级，必须成功）
            boolean dbSaved = saveToDatabase(userId, aiName, content, shareUrl, shareImgUrl, chatId, request);
            
            // 🔥 数据库保存失败是严重错误，必须重试
            if (!dbSaved) {
                System.err.println(String.format("❌ [数据库失败] %s | 用户:%s | 立即加入重试队列", aiName, userId));
                PersistenceTask task = new PersistenceTask(userId, aiName, content, shareUrl, 
                                                           shareImgUrl, chatId, request);
                boolean queued = taskQueue.offer(task);
                
                if (queued) {
                    persistenceExecutor.submit(() -> retryPersistence(task));
                    System.out.println(String.format("🔄 [已加入重试] %s | 用户:%s | 将重试数据库最多3次", aiName, userId));
                } else {
                    System.err.println(String.format("❌❌ [队列满] %s | 用户:%s | 无法加入重试队列，数据丢失风险！", aiName, userId));
                }
                return false; // 数据库失败直接返回false
            }
            
            // 2. 🔥 发送WebSocket消息到前端（尽力传输，失败也要重试）
            boolean wsSent = sendToWebSocket(userId, aiName, content, shareUrl, shareImgUrl, chatId, request);
            
            // 3. 🔥 WebSocket失败也加入重试队列，尽量让用户实时看到
            if (!wsSent) {
                System.err.println(String.format("⚠️ [WebSocket失败] %s | 用户:%s | 加入重试队列，尽力传输给用户", 
                    aiName, userId));
                
                PersistenceTask wsTask = new PersistenceTask(userId, aiName, content, shareUrl, 
                                                             shareImgUrl, chatId, request);
                wsTask.dbAlreadySaved = true; // 标记数据库已保存，只需重试WebSocket
                
                boolean queued = taskQueue.offer(wsTask);
                if (queued) {
                    persistenceExecutor.submit(() -> retryWebSocketOnly(wsTask));
                    System.out.println(String.format("🔄 [WebSocket加入重试] %s | 用户:%s | 将重试最多3次", aiName, userId));
                } else {
                    System.err.println(String.format("⚠️ [队列满] %s | 用户:%s | WebSocket无法加入重试队列", aiName, userId));
                }
            }
            
            // 🔥 只要数据库成功，就算成功（WebSocket尽力而为）
            return dbSaved;
            
        } catch (Exception e) {
            // 🔥 捕获所有未预期的异常
            System.err.println(String.format("❌ [保存异常] %s | 用户:%s | 错误:%s", 
                aiName, userId, e.getMessage()));
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存到数据库（wc_playwright_draft 和 wc_chat_history）
     * 🔥 关键：每个环节失败都记录详细日志
     */
    private boolean saveToDatabase(String userId, String aiName, String content, 
                                   String shareUrl, String shareImgUrl, String chatId,
                                   UserInfoRequest request) {
        boolean draftSuccess = false;
        boolean chatSuccess = false;
        
        // 1. 🔥 保存到稿库表 wc_playwright_draft（最关键）
        try {
            Map<String, Object> draftData = new HashMap<>();
            draftData.put("taskId", request != null ? request.getTaskId() : "");
            draftData.put("keyWord", ""); // keyWord字段为空
            draftData.put("userPrompt", request != null ? request.getUserPrompt() : "");
            draftData.put("draftContent", content);
            draftData.put("aiName", aiName);
            draftData.put("userId", userId);
            draftData.put("shareUrl", shareUrl != null ? shareUrl : "");
            draftData.put("shareImgUrl", shareImgUrl != null ? shareImgUrl : "");
            
            Object draftResult = RestUtils.post(apiUrl + "/saveDraftContent", draftData);
            draftSuccess = true;
            
        } catch (Exception e) {
            // 🔥 使用ExceptionLogger记录数据库异常
            ExceptionLogger.logDatabaseException("wc_playwright_draft", "保存稿库内容", userId, aiName, e);
            
            // 🔥 记录详细错误日志到 wc_log_info
            logErrorToDatabase(
                userId, 
                aiName, 
                "wc_playwright_draft保存失败",
                e.getMessage(),
                e,
                request
            );
        }
        
        // 2. 🔥 保存到会话历史表 wc_chat_history
        if (request != null) {
            try {
                Map<String, Object> chatData = new HashMap<>();
                chatData.put("userId", userId);
                chatData.put("userPrompt", request.getUserPrompt());
                // ❌ 不要覆盖data字段！前端已经保存了完整的JSON数据
                // chatData.put("data", content); 
                chatData.put("chatId", request.getChatId());
                
                // 设置各个AI的chatId
                if (chatId != null && !chatId.isEmpty()) {
                    if (aiName.contains("腾讯元宝T1") || aiName.contains("腾讯元宝")) {
                        chatData.put("toneChatId", chatId);
                    } else if (aiName.contains("腾讯元宝DS")) {
                        chatData.put("ybDsChatId", chatId);
                    } else if (aiName.contains("豆包")) {
                        chatData.put("dbChatId", chatId);
                    } else if (aiName.contains("通义")) {
                        chatData.put("tyChatId", chatId);
                    } else if (aiName.contains("DeepSeek")) {
                        chatData.put("deepseekChatId", chatId);
                    } else if (aiName.contains("秘塔")) {
                        chatData.put("metasoChatId", chatId);
                    } else if (aiName.contains("Kimi")) {
                        chatData.put("kimiChatId", chatId);
                    } else if (aiName.contains("百度")) {
                        chatData.put("baiduChatId", chatId);
                    } else if (aiName.contains("知乎")) {
                        chatData.put("zhzdChatId", chatId);
                    }
                }
                
                Object chatResult = RestUtils.post(apiUrl + "/saveUserChatData", chatData);
                chatSuccess = true;
                
            } catch (Exception e) {
                // 🔥 使用ExceptionLogger记录数据库异常
                ExceptionLogger.logDatabaseException("wc_chat_history", "保存会话历史", userId, aiName, e);
                
                // 🔥 记录详细错误日志到 wc_log_info
                logErrorToDatabase(
                    userId, 
                    aiName, 
                    "wc_chat_history保存失败",
                    e.getMessage(),
                    e,
                    request
                );
            }
        }
        
        // 🔥🔥 严格要求：稿库表和会话表必须全部成功！
        // 稿库表：存储AI生成的内容，必须成功
        // 会话表：关系到用户体验和历史记录，必须成功
        if (draftSuccess && chatSuccess) {
            // ✅ 两个表都保存成功，记录一条完整的结果日志
            recordSuccessLog(userId, aiName, content, shareUrl, shareImgUrl, chatId, request);
            return true;
        } else {
            // ❌ 任何一个表失败都不可接受
            if (!draftSuccess && !chatSuccess) {
                System.err.println(String.format("❌❌ [严重错误] 稿库+会话全部失败 | %s | 用户:%s", aiName, userId));
                System.err.println("🚨 [紧急] 两个表都保存失败，数据完全丢失，请立即检查数据库连接和API状态！");
            } else if (!draftSuccess) {
                System.err.println(String.format("❌ [稿库失败] 稿库表保存失败 | %s | 用户:%s", aiName, userId));
                System.err.println("🚨 [紧急] 稿库数据丢失，请检查wc_playwright_draft表和/saveDraftContent接口！");
            } else {
                // !chatSuccess
                System.err.println(String.format("❌ [会话失败] 会话表保存失败 | %s | 用户:%s", aiName, userId));
                System.err.println("🚨 [紧急] 会话数据丢失，用户无法查看历史记录，请检查wc_chat_history表和/saveUserChatData接口！");
            }
            return false;
        }
    }

    /**
     * 发送到WebSocket
     * 🔥 企业级保证：WebSocket失败不影响数据完整性（数据库已保存）
     */
    private boolean sendToWebSocket(String userId, String aiName, String content, 
                                   String shareUrl, String shareImgUrl, String chatId,
                                   UserInfoRequest request) {
        try {
            // 🔥 参数校验
            if (logMsgUtil == null) {
                System.err.println("❌ [WebSocket] logMsgUtil未初始化");
                return false;
            }
            
            // 获取AI配置
            AITypeRegistry.AIConfig aiConfig = aiRegistry.getByName(aiName);
            if (aiConfig == null) {
                System.err.println(String.format("❌ [WebSocket发送] 未找到AI配置: %s", aiName));
                return false;
            }
            
            // 🔥 修复：按照正确的参数顺序发送结果消息
            // 参数顺序：content, userId, aiName, type, shareUrl, shareImgUrl, taskId
            try {
                logMsgUtil.sendResData(
                    content,                                      // copiedText
                    userId,                                       // userId
                    aiName,                                       // aiName
                    aiConfig.getMessageType(),                    // type (RETURN_BAIDU_RES等)
                    shareUrl != null ? shareUrl : "",             // shareUrl
                    shareImgUrl != null ? shareImgUrl : "",       // shareImgUrl
                    request != null ? request.getTaskId() : ""    // taskId
                );
            } catch (Exception sendError) {
                System.err.println(String.format("❌ [发送结果失败] %s | 用户:%s | 错误:%s", 
                    aiName, userId, sendError.getMessage()));
                throw sendError; // 继续抛出，让外层捕获
            }
            
            // 发送会话ID消息（如果有）
            if (chatId != null && !chatId.isEmpty()) {
                try {
                    // 🔥 修复：按照正确的参数顺序发送chatId消息
                    // 参数顺序：content, userId, aiName, type, shareUrl, shareImgUrl, taskId
                    logMsgUtil.sendResData(
                        chatId,                                       // copiedText (chatId内容)
                        userId,                                       // userId
                        aiName,                                       // aiName
                        aiConfig.getChatIdType(),                     // type (RETURN_BAIDU_CHATID等)
                        "",                                           // shareUrl (chatId消息不需要)
                        "",                                           // shareImgUrl (chatId消息不需要)
                        request != null ? request.getTaskId() : ""    // taskId
                    );
                } catch (Exception chatIdError) {
                    // chatId发送失败不影响结果消息（降级处理）
                    System.err.println(String.format("⚠️ [chatId发送失败] %s | 用户:%s | 错误:%s", 
                        aiName, userId, chatIdError.getMessage()));
                }
            }
            
            return true;
            
        } catch (Exception e) {
            // 🔥 WebSocket失败：终端警告 + 数据库记录
            System.err.println(String.format("⚠️ [WebSocket失败] %s | 用户:%s | 错误:%s", 
                aiName, userId, e.getMessage()));
            System.err.println(String.format("ℹ️ [提示] %s WebSocket失败不影响数据完整性（数据库已保存），用户可从稿库查看", aiName));
            
            // 记录到数据库日志表
            try {
                UserLogUtil.sendExceptionLogWithId(
                    userId,
                    String.format("%s - WebSocket消息发送失败", aiName),
                    "sendToWebSocket",
                    e,
                    apiUrl + "/saveLogInfo"
                );
            } catch (Exception logError) {
                System.err.println("❌ 记录WebSocket异常到数据库失败: " + logError.getMessage());
            }
            
            // 🔥 WebSocket失败可以接受（数据库已保存）
            return false;
        }
    }

    /**
     * 重试持久化任务
     * 🔥 只重试数据库保存，WebSocket失败不重试
     */
    private void retryPersistence(PersistenceTask task) {
        int maxRetries = 3;
        long retryDelay = 2000; // 2秒
        
        while (task.retryCount < maxRetries) {
            try {
                Thread.sleep(retryDelay * (task.retryCount + 1));
                
                task.retryCount++;
                System.out.println(String.format("🔄 [重试数据库] %s | 用户:%s | 第%d次重试", 
                    task.aiName, task.userId, task.retryCount));
                
                // 🔥 只重试数据库保存（稿库表+会话表必须成功）
                boolean dbSaved = saveToDatabase(task.userId, task.aiName, task.content, 
                                                task.shareUrl, task.shareImgUrl, task.chatId, task.request);
                
                if (dbSaved) {
                    System.out.println(String.format("✅ [数据库重试成功] %s | 用户:%s | 第%d次重试成功", 
                        task.aiName, task.userId, task.retryCount));
                    
                    // 尝试发送WebSocket（失败也无所谓，数据库已保存）
                    try {
                        sendToWebSocket(task.userId, task.aiName, task.content, 
                                       task.shareUrl, task.shareImgUrl, task.chatId, task.request);
                    } catch (Exception wsError) {
                        System.err.println(String.format("⚠️ [WebSocket仍失败] %s | 用户:%s | 但数据库已保存", 
                            task.aiName, task.userId));
                    }
                    return; // 数据库成功就返回
                }
                
            } catch (Exception e) {
                System.err.println(String.format("❌ [数据库重试失败] %s | 用户:%s | 第%d次重试 | 错误:%s", 
                    task.aiName, task.userId, task.retryCount, e.getMessage()));
            }
        }
        
        // 最终失败，记录到数据库
        System.err.println(String.format("❌ [持久化最终失败] %s | 用户:%s | 已重试%d次", 
            task.aiName, task.userId, maxRetries));
        
        UserLogUtil.sendExceptionLogWithId(task.userId, 
            String.format("%s结果持久化失败", task.aiName), 
            "retryPersistence", 
            new Exception(String.format("经过%d次重试仍然失败", maxRetries)), 
            apiUrl + "/saveLogInfo");
    }

    /**
     * 🔥 WebSocket专用重试方法
     * 数据库已保存，只重试WebSocket发送
     */
    private void retryWebSocketOnly(PersistenceTask task) {
        int maxRetries = 3;
        long retryDelay = 1000; // WebSocket重试间隔短一些，1秒
        
        while (task.retryCount < maxRetries) {
            try {
                Thread.sleep(retryDelay * (task.retryCount + 1));
                
                task.retryCount++;
                System.out.println(String.format("🔄 [重试WebSocket] %s | 用户:%s | 第%d次重试", 
                    task.aiName, task.userId, task.retryCount));
                
                // 🔥 只重试WebSocket发送
                boolean wsSent = sendToWebSocket(task.userId, task.aiName, task.content, 
                                                task.shareUrl, task.shareImgUrl, task.chatId, task.request);
                
                if (wsSent) {
                    System.out.println(String.format("✅ [WebSocket重试成功] %s | 用户:%s | 第%d次重试成功，用户可实时看到结果", 
                        task.aiName, task.userId, task.retryCount));
                    return; // 成功就返回
                }
                
            } catch (Exception e) {
                System.err.println(String.format("⚠️ [WebSocket重试失败] %s | 用户:%s | 第%d次重试 | 错误:%s", 
                    task.aiName, task.userId, task.retryCount, e.getMessage()));
            }
        }
        
        // WebSocket最终失败（数据库已保存，用户可从稿库查看）
        System.err.println(String.format("⚠️ [WebSocket最终失败] %s | 用户:%s | 已重试%d次", 
            task.aiName, task.userId, maxRetries));
        System.err.println(String.format("ℹ️ [提示] 数据已保存到数据库，用户可从稿库查看，只是无法实时显示", task.aiName));
        
        // 记录到数据库日志（作为警告，不是错误）
        try {
            UserLogUtil.sendExceptionLogWithId(task.userId, 
                String.format("%s - WebSocket重试3次后仍失败（数据已保存）", task.aiName), 
                "retryWebSocketOnly", 
                new Exception(String.format("WebSocket经过%d次重试仍然失败，但数据库已保存", maxRetries)), 
                apiUrl + "/saveLogInfo");
        } catch (Exception logError) {
            System.err.println("记录WebSocket重试日志失败: " + logError.getMessage());
        }
    }

    /**
     * 🔥 记录错误日志到 wc_log_info 表
     * 包含详细的错误信息、堆栈、参数等
     */
    private void logErrorToDatabase(String userId, String aiName, String errorType, 
                                   String methodName, Exception e, UserInfoRequest request) {
        try {
            // 构建详细的错误信息
            StringBuilder errorDetail = new StringBuilder();
            errorDetail.append(String.format("【错误类型】%s\n", errorType));
            errorDetail.append(String.format("【AI名称】%s\n", aiName));
            errorDetail.append(String.format("【用户ID】%s\n", userId));
            errorDetail.append(String.format("【异常类型】%s\n", e.getClass().getName()));
            errorDetail.append(String.format("【异常信息】%s\n", e.getMessage()));
            
            // 添加堆栈信息（前5行）
            StackTraceElement[] stackTrace = e.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                errorDetail.append("【堆栈信息】\n");
                int maxLines = Math.min(5, stackTrace.length);
                for (int i = 0; i < maxLines; i++) {
                    errorDetail.append(String.format("  %d. %s\n", i+1, stackTrace[i].toString()));
                }
            }
            
            // 添加请求参数
            if (request != null) {
                errorDetail.append("【请求参数】\n");
                errorDetail.append(String.format("  taskId: %s\n", request.getTaskId()));
                errorDetail.append(String.format("  userPrompt: %s\n", 
                    request.getUserPrompt() != null && request.getUserPrompt().length() > 100 
                    ? request.getUserPrompt().substring(0, 100) + "..." 
                    : request.getUserPrompt()));
            }
            
            // 添加时间戳
            errorDetail.append(String.format("【发生时间】%s\n", LocalDateTime.now()));
            
            // 保存到 wc_log_info
            Map<String, Object> logData = new HashMap<>();
            logData.put("methodName", methodName);
            logData.put("methodParams", String.format("userId=%s, aiName=%s", userId, aiName));
            logData.put("executionResult", errorDetail.toString());
            logData.put("executionTimeMillis", 0L);
            logData.put("isSuccess", 0);
            logData.put("userId", userId);
            logData.put("description", errorType);
            
            RestUtils.post(apiUrl + "/saveLogInfo", logData);
            
            System.out.println(String.format("✅ [错误日志已记录] wc_log_info | %s | %s | 用户:%s", 
                errorType, aiName, userId));
            
        } catch (Exception logEx) {
            System.err.println(String.format("❌ [错误日志记录失败] %s | 用户:%s | 日志错误:%s", 
                errorType, userId, logEx.getMessage()));
            logEx.printStackTrace();
        }
    }
    
    /**
     * 🔥 记录一条完整的成功日志到数据库
     * 包含：截图链接、会话ID、文本内容前100字
     */
    private void recordSuccessLog(String userId, String aiName, String content, 
                                  String shareUrl, String shareImgUrl, String chatId,
                                  UserInfoRequest request) {
        try {
            // 提取内容前100字
            String contentPreview = "";
            if (content != null && !content.trim().isEmpty()) {
                String plainText = content.replaceAll("<[^>]+>", "").replaceAll("\\s+", " ").trim();
                contentPreview = plainText.length() > 100 ? plainText.substring(0, 100) + "..." : plainText;
            }
            
            // 构建完整的日志描述
            StringBuilder logDesc = new StringBuilder();
            logDesc.append(String.format("%s内容生成成功", aiName));
            if (shareUrl != null && !shareUrl.isEmpty()) {
                logDesc.append(String.format("\n分享链接: %s", shareUrl));
            }
            if (shareImgUrl != null && !shareImgUrl.isEmpty()) {
                logDesc.append(String.format("\n截图链接: %s", shareImgUrl));
            }
            if (chatId != null && !chatId.isEmpty()) {
                logDesc.append(String.format("\n会话ID: %s", chatId));
            }
            if (!contentPreview.isEmpty()) {
                logDesc.append(String.format("\n内容预览: %s", contentPreview));
            }
            
            // 发送到数据库
            Map<String, Object> logData = new HashMap<>();
            logData.put("taskId", request != null ? request.getTaskId() : "");
            logData.put("methodName", "AI内容生成");
            logData.put("methodParams", String.format("aiName=%s, userId=%s", aiName, userId));
            logData.put("executionResult", logDesc.toString());
            logData.put("executionTimeMillis", 0L);
            logData.put("isSuccess", 1); // 成功
            logData.put("userId", userId);
            logData.put("description", String.format("%s - 内容生成完成", aiName));
            
            RestUtils.post(apiUrl + "/saveLogInfo", logData);
            
        } catch (Exception e) {
            // 日志记录失败不影响主流程
            System.err.println(String.format("⚠️ [日志记录失败] %s | 用户:%s | 错误:%s", 
                aiName, userId, e.getMessage()));
        }
    }
    
    /**
     * 获取待处理任务数量
     */
    public int getPendingTaskCount() {
        return taskQueue.size();
    }

    /**
     * 关闭服务
     */
    public void shutdown() {
        persistenceExecutor.shutdown();
        try {
            if (!persistenceExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                persistenceExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            persistenceExecutor.shutdownNow();
        }
    }
}
