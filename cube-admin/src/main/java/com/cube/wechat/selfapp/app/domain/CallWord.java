package com.cube.wechat.selfapp.app.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提示词配置实体类
 * 
 * @author fuchen
 * @version 1.0
 * @date 2025-01-14
 */
public class CallWord {
    
    /** 平台标识 */
    private String platformId;
    
    /** 提示词内容 */
    private String wordContent;
    
    /** 是否为公共模板(0:个人 1:公共) */
    private Integer isCommon;
    
    /** 创建用户ID */
    private Long userId;
    
    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 上架状态：0-草稿 1-已上架 */
    private Integer status;

    /** 模板价格 */
    private BigDecimal price;

    /** 销量 */
    private Integer salesCount;

    /** 累计收益 */
    private BigDecimal incomeTotal;

    public CallWord() {
    }

    public CallWord(String platformId, String wordContent) {
        this.platformId = platformId;
        this.wordContent = wordContent;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getWordContent() {
        return wordContent;
    }

    public void setWordContent(String wordContent) {
        this.wordContent = wordContent;
    }

    public Integer getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Integer isCommon) {
        this.isCommon = isCommon;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Integer salesCount) {
        this.salesCount = salesCount;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    @Override
    public String toString() {
        return "CallWord{" +
                "platformId='" + platformId + '\'' +
                ", wordContent='" + wordContent + '\'' +
                ", isCommon=" + isCommon +
                ", userId=" + userId +
                ", updateTime=" + updateTime +
                ", status=" + status +
                ", price=" + price +
                ", salesCount=" + salesCount +
                ", incomeTotal=" + incomeTotal +
                '}';
    }
} 