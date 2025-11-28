package com.cube.wechat.selfapp.app.controller;

import com.cube.common.annotation.Log;
import com.cube.common.core.controller.BaseController;
import com.cube.common.core.domain.AjaxResult;
import com.cube.common.core.page.TableDataInfo;
import com.cube.common.enums.BusinessType;
import com.cube.wechat.selfapp.app.domain.CallWord;
import com.cube.wechat.selfapp.app.domain.PromptTemplate;
import com.cube.wechat.selfapp.app.domain.request.CallWordPublishRequest;
import com.cube.wechat.selfapp.app.domain.query.CallWordQuery;
import com.cube.wechat.selfapp.app.mapper.PromptTemplateMapper;
import com.cube.wechat.selfapp.app.service.CallWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 媒体管理控制器
 * 处理媒体相关的业务逻辑
 * 
 * @author fuchen
 * @version 1.0
 * @date 2025-01-14
 */
@RestController
@RequestMapping("/media")
public class MediaController extends BaseController {

    @Autowired
    private CallWordService callWordService;
    @Autowired
    private PromptTemplateMapper promptTemplateMapper;

    /**
     * 获取指定媒体平台的提示词
     *
     * @param platformId 平台标识（如：wechat_layout, zhihu_layout）
     * @return 提示词内容
     */
    @GetMapping("/getCallWord/{platformId}")
    public AjaxResult getCallWord(@PathVariable String platformId) {
        try {
            CallWord callWord = callWordService.getCallWord(platformId);
            return AjaxResult.success("获取成功", callWord);
        } catch (Exception e) {
            logger.error("获取提示词失败", e);
            return AjaxResult.error("获取提示词失败：" + e.getMessage());
        }
    }

    /**
     * 获取评分拼接提示词
     *
     * @return 提示词内容
     */
    @GetMapping("/getScoreWord")
    public AjaxResult getScoreWord() {
        try {
            PromptTemplate promptTemplate = promptTemplateMapper.getScoreWord();
            if(promptTemplate == null){
                return AjaxResult.success("获取成功", "初稿：\n");
            }
            return AjaxResult.success("获取成功", promptTemplate.getPrompt());
        } catch (Exception e) {
            logger.error("获取提示词失败", e);
            return AjaxResult.error("获取提示词失败：" + e.getMessage());
        }
    }

    /**
     * 获取指定媒体平台的提示词列表
     *
     * @return 提示词内容
     */
    @GetMapping("/getCallWordList")
    public TableDataInfo getCallWordList(CallWordQuery callWordQuery)
    {
        startPage();
        List<CallWord> list = callWordService.getCallWordList(callWordQuery);
        return getDataTable(list);
    }

    /**
     * 更新指定媒体平台的提示词
     *
     * @param callWord 平台提示词
     * @return 操作结果
     */
    @PutMapping("/updateCallWord")
    public AjaxResult updateCallWord(@RequestBody CallWord callWord) {
        try {
            AjaxResult result = callWordService.saveOrUpdateCallWord(callWord);
            return result;
        } catch (Exception e) {
            logger.error("更新提示词失败", e);
            return AjaxResult.error("更新提示词失败：" + e.getMessage());
        }
    }

    /**
     * 删除指定媒体平台的提示词
     *
     * @param platformIds 平台标识集合
     * @return 提示词内容
     */
    @DeleteMapping("/deleteCallWord")
    public AjaxResult deleteCallWord(@RequestBody String[] platformIds) {
        try {
            AjaxResult result = callWordService.deleteCallWord(platformIds);
            return result;
        } catch (Exception e) {
            logger.error("删除提示词失败", e);
            return AjaxResult.error("删除提示词失败：" + e.getMessage());
        }
    }

    /**
     * 设置平台提示词为公共/私有（仅管理员和普通角色）
     *
     * @param platformId 平台标识
     * @param isCommon 是否公共(0:私有 1:公共)
     * @return 操作结果
     */
    @Log(title = "设置公共提示词", businessType = BusinessType.UPDATE)
    @PutMapping("/setCallWordCommon/{platformId}/{isCommon}")
    public AjaxResult setCallWordCommon(@PathVariable String platformId, @PathVariable Integer isCommon) {
        try {
            // 手动检查权限：只有admin或common角色可以设置公共模板
            if (!com.cube.common.utils.SecurityUtils.hasRole("admin") && 
                !com.cube.common.utils.SecurityUtils.hasRole("common")) {
                return AjaxResult.error("您没有权限执行此操作");
            }
            
            return callWordService.setCallWordCommon(platformId, isCommon);
        } catch (Exception e) {
            logger.error("设置公共提示词失败", e);
            return AjaxResult.error("设置失败：" + e.getMessage());
        }
    }

    /**
     * 模板上架市场
     */
    @Log(title = "模板上架", businessType = BusinessType.UPDATE)
    @PutMapping("/publishCallWord")
    public AjaxResult publishCallWord(@RequestBody CallWordPublishRequest request) {
        if (request == null || request.getPlatformId() == null) {
            return AjaxResult.error("模板标识不能为空");
        }
        return callWordService.publishCallWord(request.getPlatformId(), request.getPrice());
    }

    /**
     * 模板下架
     */
    @Log(title = "模板下架", businessType = BusinessType.UPDATE)
    @PutMapping("/unpublishCallWord/{platformId}")
    public AjaxResult unpublishCallWord(@PathVariable String platformId) {
        return callWordService.unpublishCallWord(platformId);
    }

} 