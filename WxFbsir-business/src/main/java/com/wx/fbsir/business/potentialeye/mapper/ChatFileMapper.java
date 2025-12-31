package com.wx.fbsir.business.potentialeye.mapper;

import com.wx.fbsir.business.potentialeye.domain.ChatFile;

import java.util.List;


/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author wxfbsir
 * @date 2025-12-27
 */
public interface ChatFileMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param fileId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public ChatFile selectChatFileByFileId(Long fileId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param chatFile 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ChatFile> selectChatFileList(ChatFile chatFile);

    /**
     * 新增【请填写功能名称】
     * 
     * @param chatFile 【请填写功能名称】
     * @return 结果
     */
    public int insertChatFile(ChatFile chatFile);

    /**
     * 修改【请填写功能名称】
     * 
     * @param chatFile 【请填写功能名称】
     * @return 结果
     */
    public int updateChatFile(ChatFile chatFile);

    /**
     * 删除【请填写功能名称】
     * 
     * @param fileId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteChatFileByFileId(Long fileId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param fileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatFileByFileIds(Long[] fileIds);
}
