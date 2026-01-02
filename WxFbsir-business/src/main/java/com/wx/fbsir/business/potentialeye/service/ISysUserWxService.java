package com.wx.fbsir.business.potentialeye.service;

import com.wx.fbsir.business.potentialeye.domain.SysUserWx;

public interface ISysUserWxService {

    SysUserWx selectUserWxBySessionId(String weWorkUserId);

    int insert(SysUserWx sysUserWx);
}
