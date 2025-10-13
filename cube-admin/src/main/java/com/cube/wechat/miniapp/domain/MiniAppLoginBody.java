package com.cube.wechat.miniapp.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信小程序登录请求参数
 */
@Data
@ApiModel(value = "MiniAppLoginBody", description = "微信小程序登录信息")
public class MiniAppLoginBody {

    @ApiModelProperty(value = "微信授权code", required = true)
    private String code;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String avatar;
}

