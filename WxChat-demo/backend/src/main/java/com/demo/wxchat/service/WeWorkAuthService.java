package com.demo.wxchat.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.demo.wxchat.domain.WeWorkConfig;
import com.demo.wxchat.domain.WeWorkLoginResult;
import com.demo.wxchat.domain.WeWorkMember;
import com.demo.wxchat.mapper.WeWorkConfigMapper;
import com.demo.wxchat.mapper.WeWorkMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 企业微信认证服务
 */
@Service
public class WeWorkAuthService {

    private static final Logger log = LoggerFactory.getLogger(WeWorkAuthService.class);

    private static final String WEWORK_API_BASE = "https://qyapi.weixin.qq.com/cgi-bin";
    private static final String WEWORK_OAUTH_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize";

    @Autowired
    private WeWorkConfigMapper weWorkConfigMapper;

    @Autowired
    private WeWorkMemberMapper weWorkMemberMapper;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 构建OAuth授权URL
     */
    public String buildOAuthAuthorizeUrl(Long teamId, String redirectUri) {
        WeWorkConfig config = weWorkConfigMapper.selectByTeamId(teamId);
        if (config == null) {
            throw new RuntimeException("团队企业微信配置不存在");
        }

        String encodedRedirect = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        return WEWORK_OAUTH_AUTHORIZE
                + "?appid=" + config.getCorpId()
                + "&redirect_uri=" + encodedRedirect
                + "&response_type=code"
                + "&scope=snsapi_base"
                + "&state=" + teamId
                + "#wechat_redirect";
    }

    /**
     * 处理OAuth回调
     */
    public WeWorkLoginResult handleOAuthCallback(String code, Long teamId) {
        WeWorkConfig config = weWorkConfigMapper.selectByTeamId(teamId);
        if (config == null) {
            throw new RuntimeException("团队企业微信配置不存在");
        }

        // 获取access_token
        String accessToken = getAccessToken(config.getCorpId(), config.getAgentSecret());

        // 通过code获取用户ID
        String weWorkUserId = getUserIdByCode(accessToken, code);

        // 获取或创建成员
        WeWorkMember member = getOrCreateMember(weWorkUserId, teamId, accessToken);

        // 构建返回结果
        WeWorkLoginResult result = new WeWorkLoginResult();
        result.setToken(generateToken(member));
        result.setTeamId(teamId);
        result.setWeWorkUserId(weWorkUserId);
        result.setMemberName(member.getMemberName());
        result.setGroupChatId(config.getGroupChatId());
        result.setInGroup(true);

        return result;
    }

    private String getAccessToken(String corpId, String corpSecret) {
        String url = WEWORK_API_BASE + "/gettoken?corpid=" + corpId + "&corpsecret=" + corpSecret;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject result = JSON.parseObject(response.getBody());

            if (result.getInteger("errcode") == 0) {
                return result.getString("access_token");
            } else {
                throw new RuntimeException("获取access_token失败: " + result.getString("errmsg"));
            }
        } catch (Exception e) {
            log.error("获取access_token异常", e);
            throw new RuntimeException("获取access_token异常: " + e.getMessage());
        }
    }

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
            log.error("获取用户信息异常", e);
            throw new RuntimeException("获取用户信息异常: " + e.getMessage());
        }
    }

    private WeWorkMember getOrCreateMember(String weWorkUserId, Long teamId, String accessToken) {
        WeWorkMember member = weWorkMemberMapper.selectByWeWorkUserId(weWorkUserId);

        if (member == null) {
            JSONObject userInfo = getUserInfo(accessToken, weWorkUserId);

            member = new WeWorkMember();
            member.setWeWorkUserId(weWorkUserId);
            member.setTeamId(teamId);
            member.setMemberName(userInfo.getString("name"));

            Object deptObj = userInfo.get("department");
            if (deptObj instanceof JSONArray) {
                member.setDepartment(((JSONArray) deptObj).toJSONString());
            } else if (deptObj != null) {
                member.setDepartment(String.valueOf(deptObj));
            }

            member.setMobile(userInfo.getString("mobile"));
            member.setEmail(userInfo.getString("email"));
            member.setAvatar(userInfo.getString("avatar"));
            member.setStatus("1");
            member.setInGroup(1);
            member.setCreateTime(new Date());

            weWorkMemberMapper.insert(member);
        }

        return member;
    }

    private JSONObject getUserInfo(String accessToken, String userId) {
        String url = WEWORK_API_BASE + "/user/get?access_token=" + accessToken + "&userid=" + userId;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject result = JSON.parseObject(response.getBody());

            if (result.getInteger("errcode") == 0) {
                return result;
            } else {
                throw new RuntimeException("获取用户详细信息失败: " + result.getString("errmsg"));
            }
        } catch (Exception e) {
            log.error("获取用户详细信息异常", e);
            throw new RuntimeException("获取用户详细信息异常: " + e.getMessage());
        }
    }

    private String generateToken(WeWorkMember member) {
        return "token_" + member.getWeWorkUserId() + "_" + System.currentTimeMillis();
    }
}
