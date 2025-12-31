package com.wx.fbsir.business.potentialeye.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wx.fbsir.common.annotation.Excel;
import com.wx.fbsir.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 【请填写功能名称】对象 chat_message
 */
public class ChatMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 对话信息ID */
    private Long chatId;

    /** 消息ID，用于去重 */
    @Excel(name = "消息ID，用于去重")
    private String messageId;

    /** 会话ID（用户标识） */
    @Excel(name = "会话ID", readConverterExp = "用=户标识")
    private String sessionId;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String content;

    /** 角色"谁发的消息" */
    @Excel(name = "角色")
    private String role;

    /** 是否已读：0-未读, 1-已读 */
    @Excel(name = "是否已读：0-未读, 1-已读")
    private Integer isRead;

    /** 对话消耗的积分 */
    @Excel(name = "对话消耗的积分")
    private Integer pointCost;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    public void setChatId(Long chatId) 
    {
        this.chatId = chatId;
    }

    public Long getChatId() 
    {
        return chatId;
    }

    public void setMessageId(String messageId) 
    {
        this.messageId = messageId;
    }

    public String getMessageId() 
    {
        return messageId;
    }

    public void setSessionId(String sessionId) 
    {
        this.sessionId = sessionId;
    }

    public String getSessionId() 
    {
        return sessionId;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setRole(String role) 
    {
        this.role = role;
    }

    public String getRole() 
    {
        return role;
    }

    public void setIsRead(Integer isRead) 
    {
        this.isRead = isRead;
    }

    public Integer getIsRead() 
    {
        return isRead;
    }

    public void setPointCost(Integer pointCost)
    {
        this.pointCost = pointCost;
    }

    public Integer getPointCost()
    {
        return pointCost;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("chatId", getChatId())
            .append("messageId", getMessageId())
            .append("sessionId", getSessionId())
            .append("content", getContent())
            .append("role", getRole())
            .append("isRead", getIsRead())
            .append("pointCost", getPointCost())
            .append("createdTime", getCreatedTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
