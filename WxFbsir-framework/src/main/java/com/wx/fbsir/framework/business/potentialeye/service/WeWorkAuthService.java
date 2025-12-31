package com.wx.fbsir.framework.business.potentialeye.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wx.fbsir.business.potentialeye.config.WeWorkConfig;
import com.wx.fbsir.business.potentialeye.domain.WeWorkLoginResult;
import com.wx.fbsir.common.core.domain.entity.SysUser;
import com.wx.fbsir.common.core.domain.model.LoginUser;
import com.wx.fbsir.common.utils.SecurityUtils;
import com.wx.fbsir.framework.web.service.TokenService;
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
    private TokenService tokenService;

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
            //判断当前用户是否存在
            SysUser user = userService.selectUserByUserName(weWorkUserId);
            if (Objects.isNull(user)) {
                //获取用户详细信息
                String url = WEWORK_API_BASE + "/user/get?access_token=" + accessToken + "&USERID=" + weWorkUserId;
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                JSONObject jsonObject = JSON.parseObject(response.getBody());
                //创建普通的用户
                user = new SysUser();
                user.setUserName(weWorkUserId);
                user.setNickName(jsonObject.getString("name")!=null?jsonObject.getString("name"):weWorkUserId);
                user.setPoints(1000); //设置初始用户登录后的积分
                user.setPassword(SecurityUtils.encryptPassword("123456")); //密码加密处理
                user.setCreateBy(weWorkUserId);
                user.setCreateTime(new Date());
                userService.insertUser(user);
            }
            //创建ruoyi登录的token信息
            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(user.getUserId());
            loginUser.setLoginTime(new Date().getTime());
            loginUser.setUser(user);
            String token = tokenService.createToken(loginUser);
            // 构建返回结果
            WeWorkLoginResult result = new WeWorkLoginResult();
            result.setToken(token);
            result.setWeWorkUserId(weWorkUserId);
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
