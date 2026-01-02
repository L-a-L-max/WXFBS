package com.wx.fbsir.business.potentialeye.service.impl;

import com.wx.fbsir.business.potentialeye.domain.SysUserWx;
import com.wx.fbsir.business.potentialeye.mapper.SysUserWxMapper;
import com.wx.fbsir.business.potentialeye.service.ISysUserWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ISysUserWxServiceImpl implements ISysUserWxService {

    @Autowired
    private SysUserWxMapper userWxMapper;

    @Override
    public SysUserWx selectUserWxBySessionId(String weWorkUserId) {
        return userWxMapper.selectUserWxBySessionId(weWorkUserId);
    }

    @Override
    public int insert(SysUserWx sysUserWx) {
        return userWxMapper.insertSysUserWx(sysUserWx);
    }
}
