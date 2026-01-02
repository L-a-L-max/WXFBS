package com.wx.fbsir.business.potentialeye.mapper;

import com.wx.fbsir.business.potentialeye.domain.ChatMessage;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author wxfbsir
 * @date 2025-12-27
 */
public interface ChatMessageMapper 
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
     * 删除【请填写功能名称】
     * 
     * @param chatId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteChatMessageByChatId(Long chatId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param chatIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatMessageByChatIds(Long[] chatIds);

    /**
     * 查询未读消息
     * @param sessionId
     * @return
     */
    List<ChatMessage> selectUnreadMessages(String sessionId);

    /**
     * 标记消息为已读
     */
    void markAsRead(String sessionId, List<Long> ids);

    ChatMessage selectChatMessageByMessageId(String messageId);
}
