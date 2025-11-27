package com.cube.wechat.selfapp.app.mapper;

import com.cube.wechat.selfapp.app.domain.PromptTemplate;
import com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ClassName: PromptTemplateMapper
 * Package: com.cube.wechat.selfapp.app.mapper
 * Description:
 *
 * @Author pupil
 * @Create 2025/9/20 13:21
 * @Version 1.0
 */
@Mapper
public interface PromptTemplateMapper {
    /**
     * 查询评分模板配置
     *
     * @param id 评分模板配置主键
     * @return 评分模板配置
     */
    public PromptTemplate getPromptTemplateById(Long id);

    /**
     * 查询评分模板配置列表
     *
     * @param scorePromptQuery 评分模板搜索参数
     * @return 评分模板配置集合
     */
    public List<PromptTemplate> getPromptTemplateList(ScorePromptQuery scorePromptQuery);

    /**
     * 新增评分模板配置
     *
     * @param PromptTemplate 评分模板配置
     * @return 结果
     */
    public int savePromptTemplate(PromptTemplate PromptTemplate);

    /**
     * 修改评分模板配置
     *
     * @param PromptTemplate 评分模板配置
     * @return 结果
     */
    public int updatePromptTemplate(PromptTemplate PromptTemplate);

    /**
     * 删除评分模板配置
     *
     * @param id 评分模板配置主键
     * @return 结果
     */
    public int deletePromptTemplateById(Long id);

    /**
     * 批量删除评分模板配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePromptTemplateByIds(Long[] ids);

    public PromptTemplate getScorePromptById(@Param("id") Long id);

    public List<PromptTemplate> getScorePromptList(@Param("scorePromptQuery") ScorePromptQuery scorePromptQuery,
                                                   @Param("userId") Long userId);

    public List<PromptTemplate> getAllScorePrompt(@Param("userId") Long userId);

    PromptTemplate getScoreWord();

    /**
     * 根据ID查询评分模板（用于上架/下架）
     *
     * @param id 模板ID
     * @return 评分模板
     */
    PromptTemplate selectScorePromptById(@Param("id") Long id);

    /**
     * 查询我创建的模板列表
     */
    List<PromptTemplate> getMyCreatedTemplates(@Param("scorePromptQuery") ScorePromptQuery scorePromptQuery,
                                               @Param("userId") Long userId);

    /**
     * 查询市场模板列表（已上架且非公共模板）
     */
    List<Map<String, Object>> getMarketTemplates(@Param("scorePromptQuery") ScorePromptQuery scorePromptQuery,
                                                 @Param("minPrice") java.math.BigDecimal minPrice,
                                                 @Param("maxPrice") java.math.BigDecimal maxPrice,
                                                 @Param("offset") Integer offset,
                                                 @Param("limit") Integer limit);

    /**
     * 统计市场模板数量
     */
    int countMarketTemplates(@Param("scorePromptQuery") ScorePromptQuery scorePromptQuery,
                             @Param("minPrice") java.math.BigDecimal minPrice,
                             @Param("maxPrice") java.math.BigDecimal maxPrice);
}
