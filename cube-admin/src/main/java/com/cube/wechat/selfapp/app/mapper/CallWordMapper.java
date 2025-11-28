package com.cube.wechat.selfapp.app.mapper;

import com.cube.wechat.selfapp.app.domain.CallWord;
import com.cube.wechat.selfapp.app.domain.query.CallWordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 提示词配置Mapper接口
 * 
 * @author fuchen
 * @version 1.0
 * @date 2025-01-14
 */
@Mapper
public interface CallWordMapper {

    /**
     * 根据平台ID查询提示词
     * 
     * @param platformId 平台标识
     * @return 提示词配置
     */
    CallWord getCallWordById(@Param("platformId") String platformId);

    /**
     * 插入新的提示词配置
     * 
     * @param callWord 提示词配置
     * @return 影响行数
     */
    int insertCallWord(CallWord callWord);

    /**
     * 更新提示词配置
     * 
     * @param callWord 提示词配置
     * @return 影响行数
     */
    int updateCallWord(CallWord callWord);

    /**
     * 更新草稿的知乎投递状态
     * 
     * @param draftId 草稿ID
     * @param isPushed 是否已投递（0-未投递，1-已投递）
     * @return 影响行数
     */
    int updateDraftZhihuStatus(@Param("draftId") Long draftId, @Param("isPushed") Integer isPushed);

    int deleteCallWordByPlatformIds(String[] platformIds);

    List<CallWord> getCallWordList(CallWordQuery callWordQuery);

    /**
     * 根据平台ID查询提示词（用于设置公共模板）
     * 
     * @param platformId 平台标识
     * @return 提示词配置
     */
    CallWord selectByPlatformId(@Param("platformId") String platformId);

    /**
     * 仅更新提示词的公共状态
     *
     * @param platformId 平台标识
     * @param isCommon 是否公共
     * @return 影响行数
     */
    int updateCallWordCommon(@Param("platformId") String platformId, @Param("isCommon") Integer isCommon);

    /**
     * 查询我创建的提示词模板
     *
     * @param userId 用户ID
     * @param platformId 平台ID（可选）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 提示词列表
     */
    List<CallWord> getMyCreatedCallWords(@Param("userId") Long userId,
                                         @Param("platformId") String platformId,
                                         @Param("offset") Integer offset,
                                         @Param("limit") Integer limit);

    /**
     * 统计我创建的提示词模板数量
     *
     * @param userId 用户ID
     * @param platformId 平台ID（可选）
     * @return 数量
     */
    int countMyCreatedCallWords(@Param("userId") Long userId, @Param("platformId") String platformId);

    /**
     * 查询市场中的提示词模板（已上架的）
     *
     * @param platformId 平台ID（可选）
     * @param minPrice 最低价格（可选）
     * @param maxPrice 最高价格（可选）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 提示词列表
     */
    List<java.util.Map<String, Object>> getMarketCallWords(@Param("platformId") String platformId,
                                                          @Param("minPrice") java.math.BigDecimal minPrice,
                                                          @Param("maxPrice") java.math.BigDecimal maxPrice,
                                                          @Param("offset") Integer offset,
                                                          @Param("limit") Integer limit);

    /**
     * 统计市场中的提示词模板数量
     *
     * @param platformId 平台ID（可选）
     * @param minPrice 最低价格（可选）
     * @param maxPrice 最高价格（可选）
     * @return 数量
     */
    int countMarketCallWords(@Param("platformId") String platformId,
                            @Param("minPrice") java.math.BigDecimal minPrice,
                            @Param("maxPrice") java.math.BigDecimal maxPrice);
} 