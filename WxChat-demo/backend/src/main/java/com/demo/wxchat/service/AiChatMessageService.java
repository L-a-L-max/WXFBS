package com.demo.wxchat.service;

import com.demo.wxchat.domain.AiChatMessage;
import com.demo.wxchat.mapper.AiChatMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * AI对话消息服务
 */
@Service
public class AiChatMessageService {

    private static final Logger log = LoggerFactory.getLogger(AiChatMessageService.class);

    @Autowired
    private AiChatMessageMapper aiChatMessageMapper;

    /**
     * 保存用户消息
     */
    public AiChatMessage saveUserMessage(String sessionId, String content) {
        AiChatMessage message = new AiChatMessage();
        message.setSessionId(sessionId);
        message.setRole("user");
        message.setContent(content);
        message.setMessageId(UUID.randomUUID().toString());
        message.setIsRead(1);  // 用户消息默认已读
        message.setCreateTime(new Date());

        aiChatMessageMapper.insert(message);
        log.info("[AI对话消息] 保存用户消息 - sessionId: {}, 内容长度: {}", sessionId, content.length());

        return message;
    }

    /**
     * 保存AI回复消息（供工作流回调使用）
     */
    public AiChatMessage saveAssistantMessage(String sessionId, String content, String messageId) {
        // 检查是否已存在（去重）
        if (messageId != null) {
            AiChatMessage existing = aiChatMessageMapper.selectByMessageId(messageId);
            if (existing != null) {
                log.info("[AI对话消息] 消息已存在，跳过 - messageId: {}", messageId);
                return existing;
            }
        }

        AiChatMessage message = new AiChatMessage();
        message.setSessionId(sessionId);
        message.setRole("assistant");
        message.setContent(content);
        message.setMessageId(messageId != null ? messageId : UUID.randomUUID().toString());
        message.setIsRead(0);  // AI回复默认未读
        message.setCreateTime(new Date());

        aiChatMessageMapper.insert(message);
        log.info("[AI对话消息] 保存AI回复 - sessionId: {}, 内容长度: {}", sessionId, content.length());

        return message;
    }

    /**
     * 获取未读的AI回复消息
     */
    public List<AiChatMessage> getUnreadMessages(String sessionId) {
        return aiChatMessageMapper.selectUnreadMessages(sessionId);
    }

    /**
     * 标记消息为已读
     */
    public void markAsRead(String sessionId, List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            aiChatMessageMapper.markAsRead(sessionId, ids);
            log.debug("[AI对话消息] 标记已读 - sessionId: {}, 数量: {}", sessionId, ids.size());
        }
    }
}
