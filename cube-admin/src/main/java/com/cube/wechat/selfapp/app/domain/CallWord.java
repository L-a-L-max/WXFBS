package com.cube.wechat.selfapp.app.domain;

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

    @Override
    public String toString() {
        return "CallWord{" +
                "platformId='" + platformId + '\'' +
                ", wordContent='" + wordContent + '\'' +
                ", isCommon=" + isCommon +
                ", userId=" + userId +
                ", updateTime=" + updateTime +
                '}';
    }
} 