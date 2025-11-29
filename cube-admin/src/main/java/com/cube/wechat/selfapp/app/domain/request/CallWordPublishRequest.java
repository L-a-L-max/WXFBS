package com.cube.wechat.selfapp.app.domain.request;

import java.math.BigDecimal;

/**
 * 上架模板请求
 */
public class CallWordPublishRequest {

    private String platformId;

    private BigDecimal price;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

