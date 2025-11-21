package com.cube.common.core.domain.entity;

import com.cube.common.annotation.Excel;
import com.cube.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * AI智能体权限对象 ai_agent_permission
 *
 * @author cube
 * @date 2025-01-14
 */
public class AiAgentPermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    private Long id;

    /** 智能体ID */
    @Excel(name = "智能体ID")
    @NotNull(message = "智能体ID不能为空")
    private Long agentId;

    /** 权限类型：1-角色权限，2-用户权限 */
    @Excel(name = "权限类型", readConverterExp = "1=角色权限,2=用户权限")
    @NotNull(message = "权限类型不能为空")
    private Integer permissionType;

    /** 目标ID（角色ID或用户ID） */
    @Excel(name = "目标ID")
    @NotNull(message = "目标ID不能为空")
    @Size(min = 0, max = 64, message = "目标ID长度不能超过64个字符")
    private String targetId;

    /** 权限动作：0-禁止，1-允许 */
    @Excel(name = "权限动作", readConverterExp = "0=禁止,1=允许")
    @NotNull(message = "权限动作不能为空")
    private Integer permissionAction;

    /** 智能体名称（关联查询字段） */
    @Excel(name = "智能体名称")
    private String agentName;

    /** 目标名称（关联查询字段） */
    @Excel(name = "目标名称")
    private String targetName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setAgentId(Long agentId)
    {
        this.agentId = agentId;
    }

    public Long getAgentId()
    {
        return agentId;
    }

    public void setPermissionType(Integer permissionType)
    {
        this.permissionType = permissionType;
    }

    public Integer getPermissionType()
    {
        return permissionType;
    }

    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }

    public String getTargetId()
    {
        return targetId;
    }

    public void setPermissionAction(Integer permissionAction)
    {
        this.permissionAction = permissionAction;
    }

    public Integer getPermissionAction()
    {
        return permissionAction;
    }

    public void setAgentName(String agentName)
    {
        this.agentName = agentName;
    }

    public String getAgentName()
    {
        return agentName;
    }

    public void setTargetName(String targetName)
    {
        this.targetName = targetName;
    }

    public String getTargetName()
    {
        return targetName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("agentId", getAgentId())
            .append("permissionType", getPermissionType())
            .append("targetId", getTargetId())
            .append("permissionAction", getPermissionAction())
            .append("agentName", getAgentName())
            .append("targetName", getTargetName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
