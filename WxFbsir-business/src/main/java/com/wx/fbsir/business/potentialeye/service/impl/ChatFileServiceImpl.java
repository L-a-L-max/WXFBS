package com.wx.fbsir.business.potentialeye.service.impl;

import com.wx.fbsir.business.potentialeye.domain.ChatFile;
import com.wx.fbsir.business.potentialeye.mapper.ChatFileMapper;
import com.wx.fbsir.business.potentialeye.service.IChatFileService;
import com.wx.fbsir.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author wxfbsir
 * @date 2025-12-27
 */
@Service
public class ChatFileServiceImpl implements IChatFileService
{
    @Autowired
    private ChatFileMapper chatFileMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param fileId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public ChatFile selectChatFileByFileId(Long fileId)
    {
        return chatFileMapper.selectChatFileByFileId(fileId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param chatFile 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ChatFile> selectChatFileList(ChatFile chatFile)
    {
        return chatFileMapper.selectChatFileList(chatFile);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param chatFile 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertChatFile(ChatFile chatFile)
    {
        chatFile.setCreateTime(DateUtils.getNowDate());
        return chatFileMapper.insertChatFile(chatFile);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param chatFile 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateChatFile(ChatFile chatFile)
    {
        return chatFileMapper.updateChatFile(chatFile);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param fileIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteChatFileByFileIds(Long[] fileIds)
    {
        return chatFileMapper.deleteChatFileByFileIds(fileIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param fileId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteChatFileByFileId(Long fileId)
    {
        return chatFileMapper.deleteChatFileByFileId(fileId);
    }
}
