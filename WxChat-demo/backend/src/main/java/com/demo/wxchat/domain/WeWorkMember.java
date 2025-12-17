package com.demo.wxchat.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业微信成员实体
 */
public class WeWorkMember implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String weWorkUserId;
    private Long teamId;
    private String memberName;
    private String department;
    private String mobile;
    private String email;
    private String avatar;
    private Integer inGroup;
    private String status;
    private Date lastVerifyTime;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeWorkUserId() {
        return weWorkUserId;
    }

    public void setWeWorkUserId(String weWorkUserId) {
        this.weWorkUserId = weWorkUserId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getInGroup() {
        return inGroup;
    }

    public void setInGroup(Integer inGroup) {
        this.inGroup = inGroup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastVerifyTime() {
        return lastVerifyTime;
    }

    public void setLastVerifyTime(Date lastVerifyTime) {
        this.lastVerifyTime = lastVerifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
