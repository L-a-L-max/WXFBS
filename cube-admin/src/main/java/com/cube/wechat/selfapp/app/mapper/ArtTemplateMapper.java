package com.cube.wechat.selfapp.app.mapper;

import com.cube.wechat.selfapp.app.domain.ArtTemplate;
import com.cube.wechat.selfapp.app.domain.query.ArtPromptQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: ArtTemplateMapper
 * Package: com.cube.wechat.selfapp.app.mapper
 * Description: 艺术模板Mapper接口
 *
 * @Author pupil
 * @Create 2025/9/20 13:21
 * @Version 1.0
 */
@Mapper
public interface ArtTemplateMapper {
    /**
     * 查询艺术模板配置
     *
     * @param id 艺术模板配置主键
     * @return 艺术模板配置
     */
    public ArtTemplate getArtTemplateById(Long id);

    /**
     * 查询艺术模板配置列表
     *
     * @param artPromptQuery 艺术模板搜索参数
     * @return 艺术模板配置集合
     */
    public List<ArtTemplate> getArtTemplateList(ArtPromptQuery artPromptQuery);

    /**
     * 新增艺术模板配置
     *
     * @param artTemplate 艺术模板配置
     * @return 结果
     */
    public int saveArtTemplate(ArtTemplate artTemplate);

    /**
     * 修改艺术模板配置
     *
     * @param artTemplate 艺术模板配置
     * @return 结果
     */
    public int updateArtTemplate(ArtTemplate artTemplate);

    /**
     * 删除艺术模板配置
     *
     * @param id 艺术模板配置主键
     * @return 结果
     */
    public int deleteArtTemplateById(Long id);

    /**
     * 批量删除艺术模板配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteArtTemplateByIds(Long[] ids);

    public ArtTemplate getArtPromptById(@Param("id") Long id);

    public List<ArtTemplate> getArtPromptList(@Param("artPromptQuery") ArtPromptQuery artPromptQuery,
                                              @Param("userId") Long userId);

    public List<ArtTemplate> getAllArtPrompt(@Param("userId") Long userId);

    ArtTemplate getArtWord();
}
