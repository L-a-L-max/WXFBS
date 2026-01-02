package com.wx.fbsir.business.potentialeye.domain;

import com.wx.fbsir.common.annotation.Excel;
import com.wx.fbsir.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 chat_file
 * 
 * @author wxfbsir
 * @date 2025-12-27
 */
public class ChatFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件ID */
    private Long fileId;

    /** 消息ID */
    @Excel(name = "消息ID")
    private String messageId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件的存储路径 */
    @Excel(name = "文件的存储路径")
    private String filePath;

    /** 存储后的链接 */
    @Excel(name = "存储后的链接")
    private String fileUrl;

    public void setFileId(Long fileId) 
    {
        this.fileId = fileId;
    }

    public Long getFileId() 
    {
        return fileId;
    }

    public void setMessageId(String messageId) 
    {
        this.messageId = messageId;
    }

    public String getMessageId() 
    {
        return messageId;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }

    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fileId", getFileId())
            .append("messageId", getMessageId())
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
            .append("fileUrl", getFileUrl())
            .append("createTime", getCreateTime())
            .toString();
    }
}
