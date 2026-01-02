package com.wx.fbsir.business.potentialeye.domain;

import com.wx.fbsir.common.annotation.Excel;
import com.wx.fbsir.common.annotation.Excel.ColumnType;
import com.wx.fbsir.common.annotation.Excel.Type;
import com.wx.fbsir.common.core.domain.BaseEntity;

/**
 * 企业微信登录 sys_user_wx
 * 
 * @author wxfbsir
 */
public class SysUserWx extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户序号")
    private Long wxId;

    @Excel(name = "企业微信ID")
    private String sessionId;

    @Excel(name = "用户ID")
    private Long userId;

    public SysUserWx() {
    }

    public SysUserWx(Long wxId, String sessionId, Long userId) {
        this.wxId = wxId;
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public Long getWxId() {
        return wxId;
    }

    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SysUserWx{" +
                "wxId=" + wxId +
                ", sessionId='" + sessionId + '\'' +
                ", userId=" + userId +
                '}';
    }
}
