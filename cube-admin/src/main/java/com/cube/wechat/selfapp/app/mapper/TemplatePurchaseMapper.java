package com.cube.wechat.selfapp.app.mapper;

import com.cube.wechat.selfapp.app.domain.TemplatePurchase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 模板购买记录Mapper
 */
@Mapper
public interface TemplatePurchaseMapper {

    /**
     * 插入购买记录
     */
    int insertTemplatePurchase(TemplatePurchase purchase);

    /**
     * 查询用户是否已购买模板
     */
    TemplatePurchase selectByTemplateIdAndUserId(@Param("templateType") Integer templateType,
                                                  @Param("templateId") String templateId, 
                                                  @Param("userId") Long userId);

    /**
     * 查询用户购买的模板列表（指定模板类型）
     */
    List<Map<String, Object>> selectPurchasedTemplatesByUserId(@Param("templateType") Integer templateType,
                                                               @Param("userId") Long userId, 
                                                               @Param("name") String name,
                                                               @Param("offset") Integer offset,
                                                               @Param("limit") Integer limit);

    /**
     * 统计用户购买的模板数量（指定模板类型）
     */
    int countPurchasedTemplatesByUserId(@Param("templateType") Integer templateType,
                                        @Param("userId") Long userId, 
                                        @Param("name") String name);
}

