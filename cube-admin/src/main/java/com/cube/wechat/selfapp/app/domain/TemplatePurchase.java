package com.cube.wechat.selfapp.app.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 模板购买记录实体
 */
public class TemplatePurchase {
    
    private Long id;
    
    /** 模板类型：1-提示词模板 2-评分模板 */
    private Integer templateType;
    
    /** 模板ID（提示词模板用platformId，评分模板用id） */
    private String templateId;
    
    /** 模板名称 */
    private String templateName;
    
    private Long userId;
    
    /** 模板作者ID */
    private Long authorId;
    
    private BigDecimal purchasePrice;
    
    /** 作者分成积分 */
    private BigDecimal authorIncome;
    
    /** 平台抽成积分 */
    private BigDecimal platformFee;
    
    private LocalDateTime purchaseTime;
    
    private LocalDateTime updateTime;
    
    private Integer status;
    
    /** 交易备注 */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getAuthorIncome() {
        return authorIncome;
    }

    public void setAuthorIncome(BigDecimal authorIncome) {
        this.authorIncome = authorIncome;
    }

    public BigDecimal getPlatformFee() {
        return platformFee;
    }

    public void setPlatformFee(BigDecimal platformFee) {
        this.platformFee = platformFee;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

