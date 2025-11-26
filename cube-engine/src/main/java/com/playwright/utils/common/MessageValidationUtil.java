package com.playwright.utils.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * 消息验证工具类，用于统一前后端消息处理逻辑
 * @author 优立方
 * @version JDK 17
 * @date 2025年11月12日
 */
@Component
public class MessageValidationUtil {

    /**
     * 验证消息是否应该被处理
     * @param messageTaskId 消息中的任务ID
     * @param currentTaskId 当前任务ID
     * @param messageUserId 消息中的用户ID
     * @param currentUserId 当前用户ID
     * @param messageType 消息类型
     * @return true表示应该处理，false表示应该忽略
     */
    public static boolean shouldProcessMessage(String messageTaskId, String currentTaskId, 
                                             String messageUserId, String currentUserId, 
                                             String messageType) {
        
        // 🔥 用户ID验证：如果消息包含用户ID，必须匹配当前用户
        if (messageUserId != null && !messageUserId.trim().isEmpty() && 
            currentUserId != null && !currentUserId.trim().isEmpty() && 
            !messageUserId.equals(currentUserId)) {
            System.out.println(String.format("⚠️ [消息过滤] 用户ID不匹配 - 消息用户:%s, 当前用户:%s, 消息类型:%s", 
                messageUserId, currentUserId, messageType));
            return false;
        }
        
        // 🔥 任务ID验证：只有当消息明确包含taskId且与当前任务不匹配时才忽略
        // 如果消息没有taskId或taskId为空，则允许通过（兼容旧版本）
        if (messageTaskId != null && messageTaskId.trim().length() > 0 && 
            currentTaskId != null && currentTaskId.trim().length() > 0 && 
            !messageTaskId.equals(currentTaskId)) {
            System.out.println(String.format("⚠️ [消息过滤] 任务ID不匹配 - 消息任务:%s, 当前任务:%s, 消息类型:%s", 
                messageTaskId, currentTaskId, messageType));
            return false;
        }
        
        System.out.println(String.format("✅ [消息验证] 消息通过验证 - 任务ID:%s, 用户ID:%s, 消息类型:%s", 
            messageTaskId != null ? messageTaskId : "无", 
            messageUserId != null ? messageUserId : "无", 
            messageType));
        return true;
    }
    
    /**
     * 增强消息对象，添加必要的字段用于调试
     * @param message 原始消息JSON对象
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return 增强后的消息对象
     */
    public static JSONObject enhanceMessage(JSONObject message, String userId, String taskId) {
        if (message == null) {
            message = new JSONObject();
        }
        
        // 确保消息包含用户ID
        if (userId != null && !userId.trim().isEmpty()) {
            message.put("userId", userId);
        }
        
        // 确保消息包含任务ID
        if (taskId != null && !taskId.trim().isEmpty()) {
            message.put("taskId", taskId);
        }
        
        // 添加时间戳
        message.put("timestamp", System.currentTimeMillis());
        
        // 添加消息唯一标识
        String messageId = message.getString("type") + "_" + System.currentTimeMillis() + "_" + Math.random();
        message.put("messageId", messageId);
        
        return message;
    }
    
    /**
     * 记录消息发送日志
     * @param messageType 消息类型
     * @param userId 用户ID
     * @param taskId 任务ID
     * @param aiName AI名称
     * @param content 消息内容预览
     */
    public static void logMessageSent(String messageType, String userId, String taskId, 
                                    String aiName, String content) {
        // 只记录AI结果消息，其他消息完全静默
        if (messageType.contains("_RES")) {
            logAIResult(messageType, userId, aiName, content);
        }
        // 所有过程日志完全静默，减少终端噪音
    }
    
    /**
     * 记录AI结果到终端和数据库
     */
    private static void logAIResult(String messageType, String userId, String aiName, String content) {
        // 这个方法现在需要从调用处获取更多信息，暂时保持简单格式
        try {
            // 提取内容前20字
            String contentPreview = "";
            if (content != null && !content.trim().isEmpty()) {
                String plainText = content.replaceAll("<[^>]+>", "").replaceAll("\\s+", " ").trim();
                contentPreview = plainText.length() > 20 ? plainText.substring(0, 20) + "..." : plainText;
            }
            
            // 生成数据库日志ID（模拟）
            String logId = "LOG_" + System.currentTimeMillis();
            
            // 终端显示简洁结果
            System.out.println(String.format("✅ %s完成 | 用户:%s | 内容:%s | 已存储:数据库ID[%s]", 
                aiName != null ? aiName : "AI", 
                userId != null ? userId : "未知",
                contentPreview,
                logId));
                
        } catch (Exception e) {
            System.err.println("❌ 记录AI结果失败: " + e.getMessage());
        }
    }
    
    /**
     * 记录完整的AI结果信息（包含分享链接和截图）
     */
    public static void logCompleteAIResult(String userId, String aiName, String content, 
                                         String shareUrl, String shareImgUrl, String chatId) {
        try {
            // 提取内容前20字
            String contentPreview = "";
            if (content != null && !content.trim().isEmpty()) {
                String plainText = content.replaceAll("<[^>]+>", "").replaceAll("\\s+", " ").trim();
                contentPreview = plainText.length() > 20 ? plainText.substring(0, 20) + "..." : plainText;
            }
            
            // 构建附加信息
            StringBuilder extraInfo = new StringBuilder();
            if (shareUrl != null && !shareUrl.isEmpty()) {
                extraInfo.append(" | 分享链接:").append(shareUrl);
            }
            if (shareImgUrl != null && !shareImgUrl.isEmpty()) {
                extraInfo.append(" | 截图:").append(shareImgUrl.substring(shareImgUrl.lastIndexOf("/") + 1));
            }
            if (chatId != null && !chatId.isEmpty()) {
                extraInfo.append(" | 会话ID:").append(chatId);
            }
            
            // 终端显示完整结果（移除数据库ID显示）
            System.out.println(String.format("✅ %s完成 | 用户:%s | 内容:%s%s", 
                aiName != null ? aiName : "AI", 
                userId != null ? userId : "未知",
                contentPreview,
                extraInfo.toString()));
                
        } catch (Exception e) {
            // 简化错误日志显示（移除数据库ID相关）
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.length() > 50) {
                errorMsg = errorMsg.substring(0, 50) + "...";
            }
            System.err.println("❌ 记录AI结果失败: " + errorMsg);
        }
    }
    
    
    /**
     * 判断是否为重要的任务日志
     */
    private static boolean isImportantTaskLog(String content) {
        if (content == null) return false;
        return content.contains("执行完成") || 
               content.contains("生成完成") || 
               content.contains("分享链接") ||
               content.contains("截图") ||
               content.contains("失败") ||
               content.contains("错误");
    }
    
    /**
     * 记录消息接收日志
     * @param messageType 消息类型
     * @param userId 用户ID
     * @param taskId 任务ID
     * @param aiName AI名称
     * @param messageId 消息ID
     */
    public static void logMessageReceived(String messageType, String userId, String taskId, 
                                        String aiName, String messageId) {
        System.out.println(String.format("📨 [消息接收] 类型:%s | AI:%s | 用户:%s | 任务:%s | 消息ID:%s", 
            messageType, aiName != null ? aiName : "无", 
            userId != null ? userId : "无", 
            taskId != null ? taskId : "无", 
            messageId != null ? messageId : "无"));
    }
}
