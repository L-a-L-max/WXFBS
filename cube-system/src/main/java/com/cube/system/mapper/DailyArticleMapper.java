package com.cube.system.mapper;

import java.util.List;
import com.cube.system.domain.DailyArticle;
import org.apache.ibatis.annotations.Param;

/**
 * 日更助手文章Mapper接口
 *
 * @author cube
 * @date 2025-11-26
 */
public interface DailyArticleMapper
{
    /**
     * 查询日更助手文章
     *
     * @param id 日更助手文章ID
     * @return 日更助手文章
     */
    public DailyArticle selectDailyArticleById(Long id);

    /**
     * 查询日更助手文章列表
     *
     * @param dailyArticle 日更助手文章
     * @return 日更助手文章集合
     */
    public List<DailyArticle> selectDailyArticleList(DailyArticle dailyArticle);

    /**
     * 根据用户ID查询日更助手文章列表
     *
     * @param userId 用户ID
     * @return 日更助手文章集合
     */
    public List<DailyArticle> selectDailyArticleListByUserId(Long userId);

    /**
     * 新增日更助手文章
     *
     * @param dailyArticle 日更助手文章
     * @return 结果
     */
    public int insertDailyArticle(DailyArticle dailyArticle);

    /**
     * 修改日更助手文章
     *
     * @param dailyArticle 日更助手文章
     * @return 结果
     */
    public int updateDailyArticle(DailyArticle dailyArticle);

    /**
     * 更新文章处理状态
     *
     * @param id 文章ID
     * @param processStatus 处理状态
     * @return 结果
     */
    public int updateProcessStatus(@Param("id") Long id, @Param("processStatus") Integer processStatus);

    /**
     * 更新优化后的文章内容
     *
     * @param id 文章ID
     * @param optimizedContent 优化后的内容
     * @return 结果
     */
    public int updateOptimizedContent(@Param("id") Long id, @Param("optimizedContent") String optimizedContent);

    /**
     * 更新大模型文章内容
     *
     * @param dailyArticle 日更助手文章
     * @return 结果
     */
    public int updateModelContent(DailyArticle dailyArticle);

    /**
     * 删除日更助手文章
     *
     * @param id 日更助手文章ID
     * @return 结果
     */
    public int deleteDailyArticleById(Long id);

    /**
     * 批量删除日更助手文章
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDailyArticleByIds(Long[] ids);
}
