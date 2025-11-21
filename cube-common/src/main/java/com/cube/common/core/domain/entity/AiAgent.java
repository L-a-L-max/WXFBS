package com.cube.common.core.domain.entity;

import com.cube.common.annotation.Excel;
import com.cube.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;

/**
 * AI智能体对象 ai_agent
 *
 * @author cube
 * @date 2025-01-14
 */
public class AiAgent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 智能体ID */
    private Long id;

    /** AI名称（显示名称） */
    @Excel(name = "AI名称")
    @NotBlank(message = "AI名称不能为空")
    @Size(min = 0, max = 100, message = "AI名称长度不能超过100个字符")
    private String agentName;

    /** AI标识符（后端调用标识，如：baidu、kimi、doubao） */
    @Excel(name = "AI标识符")
    @NotBlank(message = "AI标识符不能为空")
    @Size(min = 0, max = 50, message = "AI标识符长度不能超过50个字符")
    private String agentCode;

    /** AI图标URL */
    @Excel(name = "AI图标URL")
    @Size(min = 0, max = 500, message = "AI图标URL长度不能超过500个字符")
    private String agentIcon;

    /** 后端接口标识（如：startBaidu、startKimi） */
    @Excel(name = "后端接口标识")
    @Size(min = 0, max = 100, message = "后端接口标识长度不能超过100个字符")
    private String backendInterface;

    /** 登录检测API路径（如：/api/browser/checkBaiduLogin） */
    @Excel(name = "登录检测API路径")
    @Size(min = 0, max = 200, message = "登录检测API路径长度不能超过200个字符")
    private String loginCheckApi;

    /** 获取登录二维码API路径（如：/api/browser/getBaiduQrCode） */
    @Excel(name = "获取登录二维码API路径")
    @Size(min = 0, max = 200, message = "获取登录二维码API路径长度不能超过200个字符")
    private String loginQrcodeApi;

    /** AI咨询API路径（如：/api/browser/startBaidu） */
    @Excel(name = "AI咨询API路径")
    @Size(min = 0, max = 200, message = "AI咨询API路径长度不能超过200个字符")
    private String chatApi;

    /** WebSocket登录检测消息类型（如：PLAY_CHECK_BAIDU_LOGIN） */
    @Excel(name = "WebSocket登录检测消息类型")
    @Size(min = 0, max = 100, message = "WebSocket登录检测消息类型长度不能超过100个字符")
    private String websocketCheckType;

    /** WebSocket二维码消息类型（如：PLAY_GET_BAIDU_QRCODE） */
    @Excel(name = "WebSocket二维码消息类型")
    @Size(min = 0, max = 100, message = "WebSocket二维码消息类型长度不能超过100个字符")
    private String websocketQrcodeType;

    /** WebSocket聊天消息类型（如：START_BAIDU） */
    @Excel(name = "WebSocket聊天消息类型")
    @Size(min = 0, max = 100, message = "WebSocket聊天消息类型长度不能超过100个字符")
    private String websocketChatType;

    /** AI状态：0-下架，1-上架 */
    @Excel(name = "AI状态", readConverterExp = "0=下架,1=上架")
    private Integer agentStatus;

    /** 在线状态：0-离线，1-在线 */
    @Excel(name = "在线状态", readConverterExp = "0=离线,1=在线")
    private Integer onlineStatus;

    /** 排序顺序（数字越小越靠前） */
    @Excel(name = "排序顺序")
    private Integer sortOrder;

    /** 是否全局可用：0-否，1-是 */
    @Excel(name = "是否全局可用", readConverterExp = "0=否,1=是")
    private Integer isGlobal;

    /** 额外配置（JSON格式，包含选项框和按钮配置） */
    private String configJson;

    /** AI描述 */
    @Excel(name = "AI描述")
    @Size(min = 0, max = 500, message = "AI描述长度不能超过500个字符")
    private String description;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setAgentName(String agentName)
    {
        this.agentName = agentName;
    }

    public String getAgentName()
    {
        return agentName;
    }

    public void setAgentCode(String agentCode)
    {
        this.agentCode = agentCode;
    }

    public String getAgentCode()
    {
        return agentCode;
    }

    public void setAgentIcon(String agentIcon)
    {
        this.agentIcon = agentIcon;
    }

    public String getAgentIcon()
    {
        return agentIcon;
    }

    public void setBackendInterface(String backendInterface)
    {
        this.backendInterface = backendInterface;
    }

    public String getBackendInterface()
    {
        return backendInterface;
    }

    public void setLoginCheckApi(String loginCheckApi)
    {
        this.loginCheckApi = loginCheckApi;
    }

    public String getLoginCheckApi()
    {
        return loginCheckApi;
    }

    public void setLoginQrcodeApi(String loginQrcodeApi)
    {
        this.loginQrcodeApi = loginQrcodeApi;
    }

    public String getLoginQrcodeApi()
    {
        return loginQrcodeApi;
    }

    public void setChatApi(String chatApi)
    {
        this.chatApi = chatApi;
    }

    public String getChatApi()
    {
        return chatApi;
    }

    public void setWebsocketCheckType(String websocketCheckType)
    {
        this.websocketCheckType = websocketCheckType;
    }

    public String getWebsocketCheckType()
    {
        return websocketCheckType;
    }

    public void setWebsocketQrcodeType(String websocketQrcodeType)
    {
        this.websocketQrcodeType = websocketQrcodeType;
    }

    public String getWebsocketQrcodeType()
    {
        return websocketQrcodeType;
    }

    public void setWebsocketChatType(String websocketChatType)
    {
        this.websocketChatType = websocketChatType;
    }

    public String getWebsocketChatType()
    {
        return websocketChatType;
    }

    public void setAgentStatus(Integer agentStatus)
    {
        this.agentStatus = agentStatus;
    }

    public Integer getAgentStatus()
    {
        return agentStatus;
    }

    public void setOnlineStatus(Integer onlineStatus)
    {
        this.onlineStatus = onlineStatus;
    }

    public Integer getOnlineStatus()
    {
        return onlineStatus;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setIsGlobal(Integer isGlobal)
    {
        this.isGlobal = isGlobal;
    }

    public Integer getIsGlobal()
    {
        return isGlobal;
    }

    public void setConfigJson(String configJson)
    {
        this.configJson = configJson;
    }

    public String getConfigJson()
    {
        return configJson;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("agentName", getAgentName())
            .append("agentCode", getAgentCode())
            .append("agentIcon", getAgentIcon())
            .append("backendInterface", getBackendInterface())
            .append("loginCheckApi", getLoginCheckApi())
            .append("loginQrcodeApi", getLoginQrcodeApi())
            .append("chatApi", getChatApi())
            .append("websocketCheckType", getWebsocketCheckType())
            .append("websocketQrcodeType", getWebsocketQrcodeType())
            .append("websocketChatType", getWebsocketChatType())
            .append("agentStatus", getAgentStatus())
            .append("onlineStatus", getOnlineStatus())
            .append("sortOrder", getSortOrder())
            .append("isGlobal", getIsGlobal())
            .append("configJson", getConfigJson())
            .append("description", getDescription())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
