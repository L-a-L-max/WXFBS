package com.demo.wxchat.mapper;

import com.demo.wxchat.domain.AiChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI对话消息Mapper
 */
@Mapper
public interface AiChatMessageMapper {

    /**
     * 根据消息唯一标识查询（用于去重）
     */
    AiChatMessage selectByMessageId(@Param("messageId") String messageId);

    /**
     * 查询会话的未读消息
     */
    List<AiChatMessage> selectUnreadMessages(@Param("sessionId") String sessionId);

    /**
     * 插入消息
     */
    int insert(AiChatMessage message);

    /**
     * 标记消息为已读
     */
    int markAsRead(@Param("sessionId") String sessionId, @Param("ids") List<Long> ids);
}
