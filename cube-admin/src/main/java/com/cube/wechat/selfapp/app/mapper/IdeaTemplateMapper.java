package com.cube.wechat.selfapp.app.mapper;

import com.cube.wechat.selfapp.app.domain.IdeaTemplate;
import com.cube.wechat.selfapp.app.domain.query.IdeaPromptQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: IdeaTemplateMapper
 * Package: com.cube.wechat.selfapp.app.mapper
 * Description: 创意模板Mapper接口
 *
 * @Author pupil
 * @Create 2025/9/20 13:21
 * @Version 1.0
 */
@Mapper
public interface IdeaTemplateMapper {
    /**
     * 查询创意模板配置
     *
     * @param id 创意模板配置主键
     * @return 创意模板配置
     */
    public IdeaTemplate getIdeaTemplateById(Long id);

    /**
     * 查询创意模板配置列表
     *
     * @param ideaPromptQuery 创意模板搜索参数
     * @return 创意模板配置集合
     */
    public List<IdeaTemplate> getIdeaTemplateList(IdeaPromptQuery ideaPromptQuery);

    /**
     * 新增创意模板配置
     *
     * @param ideaTemplate 创意模板配置
     * @return 结果
     */
    public int saveIdeaTemplate(IdeaTemplate ideaTemplate);

    /**
     * 修改创意模板配置
     *
     * @param ideaTemplate 创意模板配置
     * @return 结果
     */
    public int updateIdeaTemplate(IdeaTemplate ideaTemplate);

    /**
     * 删除创意模板配置
     *
     * @param id 创意模板配置主键
     * @return 结果
     */
    public int deleteIdeaTemplateById(Long id);

    /**
     * 批量删除创意模板配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteIdeaTemplateByIds(Long[] ids);

    public IdeaTemplate getIdeaPromptById(@Param("id") Long id);

    public List<IdeaTemplate> getIdeaPromptList(@Param("ideaPromptQuery") IdeaPromptQuery ideaPromptQuery,
                                                @Param("userId") Long userId);

    public List<IdeaTemplate> getAllIdeaPrompt(@Param("userId") Long userId);

    IdeaTemplate getIdeaWord();
}
