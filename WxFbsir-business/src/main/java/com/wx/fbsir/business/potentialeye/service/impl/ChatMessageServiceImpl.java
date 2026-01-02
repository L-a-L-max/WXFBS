package com.wx.fbsir.business.potentialeye.service.impl;

import com.wx.fbsir.business.potentialeye.domain.ChatMessage;
import com.wx.fbsir.business.potentialeye.mapper.ChatMessageMapper;
import com.wx.fbsir.business.potentialeye.service.IChatMessageService;
import com.wx.fbsir.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author wxfbsir
 * @date 2025-12-27
 */
@Service
public class ChatMessageServiceImpl implements IChatMessageService
{
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param chatId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public ChatMessage selectChatMessageByChatId(Long chatId)
    {
        return chatMessageMapper.selectChatMessageByChatId(chatId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param chatMessage 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ChatMessage> selectChatMessageList(ChatMessage chatMessage)
    {
        return chatMessageMapper.selectChatMessageList(chatMessage);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param chatMessage 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertChatMessage(ChatMessage chatMessage)
    {
        return chatMessageMapper.insertChatMessage(chatMessage);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param chatMessage 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateChatMessage(ChatMessage chatMessage)
    {
        chatMessage.setUpdateTime(DateUtils.getNowDate());
        return chatMessageMapper.updateChatMessage(chatMessage);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param chatIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteChatMessageByChatIds(Long[] chatIds)
    {
        return chatMessageMapper.deleteChatMessageByChatIds(chatIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param chatId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteChatMessageByChatId(Long chatId)
    {
        return chatMessageMapper.deleteChatMessageByChatId(chatId);
    }

    @Override
    public List<ChatMessage> selectUnreadMessages(String sessionId) {
        return chatMessageMapper.selectUnreadMessages(sessionId);
    }

    @Override
    public void markAsRead(String sessionId, List<Long> ids) {
        chatMessageMapper.markAsRead(sessionId,ids);
    }

    @Override
    public ChatMessage selectChatMessageByMessageId(String messageId) {
        return chatMessageMapper.selectChatMessageByMessageId(messageId);
    }
}
