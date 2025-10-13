package com.cube.wechat.miniapp.service;

import com.cube.wechat.miniapp.domain.MiniAppLoginBody;

/**
 * 微信小程序登录服务接口
 */
public interface MiniAppLoginService {

    /**
     * 微信小程序登录
     * 
     * @param loginBody 登录请求参数
     * @return token
     */
    String login(MiniAppLoginBody loginBody);
}

