package com.cube.wechat.miniapp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cube.common.core.domain.entity.SysUser;
import com.cube.common.core.domain.model.LoginUser;
import com.cube.common.utils.DateUtils;
import com.cube.common.utils.StringUtils;
import com.cube.framework.web.service.TokenService;
import com.cube.point.controller.PointsSystem;
import com.cube.system.mapper.SysUserMapper;
import com.cube.wechat.miniapp.config.MiniAppConfig;
import com.cube.wechat.miniapp.domain.MiniAppLoginBody;
import com.cube.wechat.miniapp.service.MiniAppLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * 微信小程序登录服务实现
 */
@Service
public class MiniAppLoginServiceImpl implements MiniAppLoginService {

    @Autowired
    private MiniAppConfig miniAppConfig;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private PointsSystem pointsSystem;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String login(MiniAppLoginBody loginBody) {
        String code = loginBody.getCode();
        
        // 调用微信接口获取openid和session_key
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            miniAppConfig.getAppId(), 
            miniAppConfig.getAppSecret(), 
            code
        );
        
        String response = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(response);
        
        // 检查是否有错误
        if (jsonObject.containsKey("errcode") && jsonObject.getInteger("errcode") != 0) {
            throw new RuntimeException("微信授权失败: " + jsonObject.getString("errmsg"));
        }
        
        String openId = jsonObject.getString("openid");
        String unionId = jsonObject.getString("unionid");
        
        // 如果没有unionid，使用openid作为唯一标识
        if (StringUtils.isEmpty(unionId)) {
            unionId = openId;
        }
        
        String nickName = loginBody.getNickName();
        String avatar = loginBody.getAvatar();
        
        // 根据openid查询用户信息
        SysUser wxUser = userMapper.selectWxUserByOpenId(openId, unionId);
        
        SysUser user;
        if (wxUser == null) {
            // 新用户注册
            user = new SysUser();
            user.setUserName(unionId);
            
            // 处理昵称
            if (StringUtils.isEmpty(nickName)) {
                nickName = "小程序用户";
            }
            user.setNickName(nickName + getStringRandom(6));
            
            // 处理头像
            if (StringUtils.isEmpty(avatar)) {
                user.setAvatar("chatfile/avatar/head.png");
            } else {
                user.setAvatar(avatar);
            }
            
            // 设置微信相关字段
            user.setOpenId(openId);
            user.setUnionId(unionId);
            
            // 设置用户基本信息
            user.setType(0);  // 系统用户
            user.setSex("2");  // 未知
            user.setStatus("0");  // 正常状态
            user.setDelFlag("0");  // 未删除
            user.setPassword("");  // 小程序用户无需密码
            user.setEmail("");
            user.setPhonenumber("");
            user.setCreateTime(DateUtils.getNowDate());
            user.setCreateBy("微信小程序用户");
            user.setPoints(200);
            
            // 新增用户
            userMapper.insertUser(user);
            SysUser newUser = userMapper.selectWxUserByOpenId(openId, unionId);
            
            // 为用户分配百家谱用户角色
            userMapper.saveUserRoleBJP(newUser.getUserId());
            
            // 注册积分账户并赠送首次登录积分
            pointsSystem.registerAccount(newUser.getUserId());
            pointsSystem.setUserPoint(
                String.valueOf(newUser.getUserId()),
                "首次登录",
                null,
                "0x2edc4228a84d672affe8a594033cb84a029bcafc",
                "f34f737203aa370f53ef0e041c1bff36bf59db8eb662cdb447f01d9634374dd"
            );
            
            user = newUser;
        } else {
            // 老用户登录
            user = wxUser;
            user.setUpdateTime(DateUtils.getNowDate());
            
            // 检查每日登录积分
            Integer isFirst = pointsSystem.checkPointIsOk("每日优立方登录", String.valueOf(wxUser.getUserId()), 1);
            if (isFirst == 0) {
                pointsSystem.setUserPoint(
                    String.valueOf(wxUser.getUserId()),
                    "每日优立方登录",
                    null,
                    "0x2edc4228a84d672affe8a594033cb84a029bcafc",
                    "f34f737203aa370f53ef0e041c1bff36bf59db8eb662cdb447f01d9634374dd"
                );
            }
            
            userMapper.updateUser(user);
        }
        
        // 组装token信息
        LoginUser loginUser = new LoginUser();
        loginUser.setOpenId(openId);
        loginUser.setUser(user);
        loginUser.setUserId(user.getUserId());
        
        // 生成token
        return tokenService.createToken(loginUser);
    }
    
    /**
     * 生成随机字符串
     */
    private String getStringRandom(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            val.append(random.nextInt(10));
        }
        return val.toString();
    }
}

