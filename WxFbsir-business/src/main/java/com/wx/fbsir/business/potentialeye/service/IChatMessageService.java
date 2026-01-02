package com.wx.fbsir.business.potentialeye.service;

import com.wx.fbsir.business.potentialeye.domain.ChatMessage;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author wxfbsir
 * @date 2025-12-27
 */
public interface IChatMessageService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param chatId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public ChatMessage selectChatMessageByChatId(Long chatId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param chatMessage 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ChatMessage> selectChatMessageList(ChatMessage chatMessage);

    /**
     * 新增【请填写功能名称】
     * 
     * @param chatMessage 【请填写功能名称】
     * @return 结果
     */
    public int insertChatMessage(ChatMessage chatMessage);

    /**
     * 修改【请填写功能名称】
     * 
     * @param chatMessage 【请填写功能名称】
     * @return 结果
     */
    public int updateChatMessage(ChatMessage chatMessage);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param chatIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteChatMessageByChatIds(Long[] chatIds);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param chatId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteChatMessageByChatId(Long chatId);

    /**
     * 查询会话的未读消息
     */
    List<ChatMessage> selectUnreadMessages(String sessionId);

    /**
     * AI回复后，将消息处理成为已读
     * @param sessionId
     * @param ids
     */
    void markAsRead(String sessionId, List<Long> ids);

    /**
     * 根据消息唯一标识查询（用于去重）
     */
    ChatMessage selectChatMessageByMessageId(String messageId);
}
