package com.wx.fbsir.business.potentialeye.mapper;

import com.wx.fbsir.business.potentialeye.domain.SysUserWx;

import java.util.List;

/**
 * 企业微信登录 sys_user_wx
 * 
 * @author wxfbsir
 */
public interface SysUserWxMapper {


    public SysUserWx selectSysUserWxByWxId(Long wxId);


    public List<SysUserWx> selectSysUserWxList(SysUserWx SysUserWx);


    public int insertSysUserWx(SysUserWx SysUserWx);


    public int updateSysUserWx(SysUserWx SysUserWx);


    public int deleteSysUserWxByWxId(Long wxId);


    public int deleteSysUserWxByWxIds(Long[] wxIds);

    SysUserWx selectUserWxBySessionId(String weWorkUserId);
}
