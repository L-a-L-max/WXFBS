package com.wx.fbsir.business.potentialeye.controller;

import com.wx.fbsir.business.point.service.IPointsService;
import com.wx.fbsir.business.potentialeye.domain.RegisterParam;
import com.wx.fbsir.business.potentialeye.domain.SysUserWx;
import com.wx.fbsir.business.potentialeye.service.ISysUserWxService;
import com.wx.fbsir.common.annotation.Anonymous;
import com.wx.fbsir.common.constant.UserConstants;
import com.wx.fbsir.common.core.domain.AjaxResult;
import com.wx.fbsir.common.core.domain.entity.SysUser;
import com.wx.fbsir.common.utils.DateUtils;
import com.wx.fbsir.common.utils.SecurityUtils;
import com.wx.fbsir.common.utils.StringUtils;
import com.wx.fbsir.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class SysRegisterUserWxController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysUserWxService userWxService;

    @Autowired
    private IPointsService pointsService;

    @Anonymous
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterParam registerParam){
        String msg = "";
        SysUser sysUser = new SysUser();
        if (StringUtils.isEmpty(registerParam.getUsername()))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(registerParam.getPassword()))
        {
            msg = "用户密码不能为空";
        }
        else if (registerParam.getUsername().length() < UserConstants.USERNAME_MIN_LENGTH
                || registerParam.getUsername().length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (registerParam.getPassword().length() < UserConstants.PASSWORD_MIN_LENGTH
                || registerParam.getPassword().length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "保存用户'" + registerParam.getUsername() + "'失败，注册账号已存在";
        }
        else
        {
            sysUser.setUserName(registerParam.getUsername());
            sysUser.setNickName(registerParam.getUsername());
            sysUser.setPwdUpdateDate(DateUtils.getNowDate());
            sysUser.setPassword(SecurityUtils.encryptPassword(registerParam.getPassword()));
            boolean flag = userService.checkUserNameUnique(sysUser);
            if (!flag) {
                msg = "用户名已经存在";
                return AjaxResult.error(msg);
            }else {
                boolean regFlag = userService.registerUser(sysUser);
                if (!regFlag)
                {
                    msg = "注册失败,请联系系统管理人员";
                }
                else
                {
                    Long userId = sysUser.getUserId();
                    //初始化用户积分信息
                    pointsService.grantPointsByAdmin(userId,1000,"初始登录用户获得的积分");
                    SysUserWx sysUserWx = new SysUserWx();
                    sysUserWx.setSessionId(registerParam.getSessionId());
                    sysUserWx.setUserId(userId);
                    userWxService.insert(sysUserWx);
                }
            }
        }
        return AjaxResult.success(msg);
    }


}
