package com.playwright.utils.common;

import com.playwright.entity.UserInfoRequest;
import com.playwright.entity.mcp.McpResult;
import com.playwright.service.AiResultPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AI结果处理助手
 * 提供简单易用的API来保存AI结果
 * 
 * @author 优立方
 * @date 2025-01-21
 */
@Component
public class AiResultHelper {
    
    @Autowired
    private AiResultPersistenceService persistenceService;
    
    /**
     * 保存AI结果（核心方法）
     * 一次调用完成数据库保存和前端通知
     * 
     * @param userId 用户ID
     * @param aiName AI名称（如"百度AI"、"豆包"等）
     * @param content 生成的内容
     * @param shareUrl 分享链接（可选）
     * @param shareImgUrl 截图链接（可选）
     * @param chatId 会话ID（可选）
     * @param request 原始请求对象（可选）
     * @return McpResult 包含成功状态和结果数据
     */
    public McpResult saveAiResult(String userId, String aiName, String content,
                                 String shareUrl, String shareImgUrl, String chatId,
                                 UserInfoRequest request) {
        try {
            // 调用持久化服务
            boolean success = persistenceService.saveAndSendResult(
                userId, aiName, content, shareUrl, shareImgUrl, chatId, request
            );
            
            if (success) {
                return McpResult.success(content, shareUrl);
            } else {
                return McpResult.fail(
                    String.format("%s结果保存失败，但已加入重试队列", aiName), 
                    null
                );
            }
            
        } catch (Exception e) {
            System.err.println(String.format("❌ [结果保存异常] %s | 用户:%s | 错误:%s", 
                aiName, userId, e.getMessage()));
            
            return McpResult.fail(
                String.format("%s结果保存异常: %s", aiName, e.getMessage()), 
                null
            );
        }
    }
    
    /**
     * 快速保存（只需要必需参数）
     */
    public McpResult saveAiResult(String userId, String aiName, String content, 
                                 UserInfoRequest request) {
        return saveAiResult(userId, aiName, content, null, null, null, request);
    }
    
    /**
     * 带分享链接的保存
     */
    public McpResult saveAiResultWithShareUrl(String userId, String aiName, String content,
                                             String shareUrl, UserInfoRequest request) {
        return saveAiResult(userId, aiName, content, shareUrl, null, null, request);
    }
    
    /**
     * 带会话ID的保存
     */
    public McpResult saveAiResultWithChatId(String userId, String aiName, String content,
                                           String chatId, UserInfoRequest request) {
        return saveAiResult(userId, aiName, content, null, null, chatId, request);
    }
    
    /**
     * 完整保存（所有参数）
     */
    public McpResult saveAiResultFull(String userId, String aiName, String content,
                                     String shareUrl, String shareImgUrl, String chatId,
                                     UserInfoRequest request) {
        return saveAiResult(userId, aiName, content, shareUrl, shareImgUrl, chatId, request);
    }
}
