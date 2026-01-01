package com.wx.fbsir.business.potentialeye.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wx.fbsir.business.potentialeye.config.WeWorkConfig;
import com.wx.fbsir.business.potentialeye.domain.SysUserWx;
import com.wx.fbsir.business.potentialeye.domain.WeWorkLoginResult;
import com.wx.fbsir.business.potentialeye.service.ISysUserWxService;
import com.wx.fbsir.common.core.domain.entity.SysUser;
import com.wx.fbsir.common.core.domain.model.LoginUser;
import com.wx.fbsir.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

/**
 * 企业微信认证服务，初始化用户信息和积分
 */
@Service
public class WeWorkAuthService {

    private static final Logger log = LoggerFactory.getLogger(WeWorkAuthService.class);

    private static final String WEWORK_API_BASE = "https://qyapi.weixin.qq.com/cgi-bin";
    private static final String WEWORK_OAUTH_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize";

    @Autowired
    private WeWorkConfig weWorkConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private WxTokenService wxTokenService;

    @Autowired
    private ISysUserWxService userWxService;

    /**
     * 构建OAuth授权URL
     */
    public String buildOAuthAuthorizeUrl(String redirectUri) {
        String corpId = weWorkConfig.getCorpId();
        String encodedRedirect = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        
        String authUrl = WEWORK_OAUTH_AUTHORIZE
                + "?appid=" + corpId
                + "&redirect_uri=" + encodedRedirect
                + "&response_type=code"
                + "&scope=snsapi_base"
                + "#wechat_redirect";
        
        log.info("[企业微信授权] 生成授权URL - corpId: {}", corpId);
        return authUrl;
    }

    /**
     * 处理OAuth回调 - 获取用户ID
     * 不保存成员信息，直接返回token和userId
     */
    public WeWorkLoginResult handleOAuthCallback(String code) {
        try {
            log.info("[企业微信授权] 开始处理回调 - code: {}", code);
            
            // 获取access_token
            String accessToken = getAccessToken();
            log.info("[企业微信授权] 获取access_token成功");

            // 通过code获取用户ID
            String weWorkUserId = getUserIdByCode(accessToken, code);
            log.info("[企业微信授权] 获取用户ID成功 - userId: {}", weWorkUserId);
            // 构建返回结果
            WeWorkLoginResult result = new WeWorkLoginResult();
            result.setWeWorkUserId(weWorkUserId);
            //判断当前用户是否存在
            SysUserWx userWx = userWxService.selectUserWxBySessionId(weWorkUserId);
            if (ObjectUtil.isNull(userWx)) {
                log.warn("未找到session_id对应的微信用户信息: {}", weWorkUserId);
                return result;
            }
            SysUser user = userService.selectUserById(userWx.getUserId());
            if (Objects.isNull(user)) {
                log.warn("找到微信用户但未找到对应的系统用户: wxId={}, userId={}",
                        userWx.getWxId(), userWx.getUserId());
                return result;
            }
            //创建ruoyi登录的token信息
            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(user.getUserId());
            loginUser.setLoginTime(new Date().getTime());
            loginUser.setUser(user);
            String token = wxTokenService.createToken(loginUser);
            result.setToken(token);
            log.info("[企业微信授权] 授权成功 - userId: {}, token: {}", weWorkUserId, result.getToken());
            return result;
        } catch (Exception e) {
            log.error("[企业微信授权] 授权失败", e);
            throw new RuntimeException("企业微信授权失败: " + e.getMessage());
        }
    }

    /**
     * 获取access_token
     */
    private String getAccessToken() {
        String corpId = weWorkConfig.getCorpId();
        String agentSecret = weWorkConfig.getAgentSecret();
        String url = WEWORK_API_BASE + "/gettoken?corpid=" + corpId + "&corpsecret=" + agentSecret;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject result = JSON.parseObject(response.getBody());

            if (result.getInteger("errcode") == 0) {
                return result.getString("access_token");
            } else {
                throw new RuntimeException("获取access_token失败: " + result.getString("errmsg"));
            }
        } catch (Exception e) {
            log.error("[企业微信授权] 获取access_token异常", e);
            throw new RuntimeException("获取access_token异常: " + e.getMessage());
        }
    }

    /**
     * 通过code获取用户ID
     */
    private String getUserIdByCode(String accessToken, String code) {
        String url = WEWORK_API_BASE + "/auth/getuserinfo?access_token=" + accessToken + "&code=" + code;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject result = JSON.parseObject(response.getBody());

            if (result.getInteger("errcode") == 0) {
                return result.getString("userid");
            } else {
                throw new RuntimeException("获取用户信息失败: " + result.getString("errmsg"));
            }
        } catch (Exception e) {
            log.error("[企业微信授权] 获取用户信息异常", e);
            throw new RuntimeException("获取用户信息异常: " + e.getMessage());
        }
    }

    /**
     * 生成token
     */
    private String generateToken(String weWorkUserId) {
        return "token_" + weWorkUserId + "_" + System.currentTimeMillis();
    }
}
