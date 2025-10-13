package com.cube.wechat.miniapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "miniapp")
public class MiniAppConfig {

    /**
     * 小程序AppID
     */
    private String appId;

    /**
     * 小程序AppSecret
     */
    private String appSecret;
}

