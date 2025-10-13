package com.cube.wechat.miniapp.controller;

import com.cube.common.annotation.Anonymous;
import com.cube.common.constant.Constants;
import com.cube.common.core.domain.AjaxResult;
import com.cube.wechat.miniapp.domain.MiniAppLoginBody;
import com.cube.wechat.miniapp.service.MiniAppLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序登录控制器
 */
@Api(tags = "微信小程序登录")
@RestController
@RequestMapping("/wechat/miniapp")
public class MiniAppLoginController {

    @Autowired
    private MiniAppLoginService miniAppLoginService;

    /**
     * 微信小程序登录
     */
    @Anonymous
    @ApiOperation("微信小程序登录")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody MiniAppLoginBody loginBody) {
        try {
            // 执行登录
            String token = miniAppLoginService.login(loginBody);
            
            // 返回结果
            AjaxResult ajax = AjaxResult.success();
            ajax.put(Constants.TOKEN, token);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error("登录失败: " + e.getMessage());
        }
    }
}

