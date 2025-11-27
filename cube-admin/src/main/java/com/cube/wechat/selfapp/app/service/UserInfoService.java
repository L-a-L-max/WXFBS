package com.cube.wechat.selfapp.app.service;

import com.cube.wechat.selfapp.app.domain.*;
import com.cube.wechat.selfapp.app.domain.query.ArtPromptQuery;
import com.cube.wechat.selfapp.app.domain.query.IdeaPromptQuery;
import com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery;
import com.cube.wechat.selfapp.corpchat.util.ResultBody;

import java.util.List;
import java.util.Map;

public interface UserInfoService {
    ResultBody getOfficeAccountByUserId(String userId);

    /**
     * 查询个人中心统计数据
     * */
    ResultBody getUserCount(String userId);

    /**
     * 获取积分明细
     * */
    ResultBody getUserPointsRecord(Map map);


    ResultBody saveAIChatHistory(AIParam aiParam);

    ResultBody saveAINodeLog(AINodeLog aiNodeLog);

    /*
     * 查询用户AI历史会话列表
     * */
    ResultBody getUserChatHistoryList(String userId,String title);

    ResultBody getChatHistoryDetail(String conversationId);

    ResultBody deleteUserChatHistory(List<String> list);

    ResultBody saveChromeData(Map map);



    ResultBody updateChatTitle(Map map);

    ResultBody pushOffice(List<String> ids, String userName);

    ResultBody authChecker(String userName);

    ResultBody changePoint(String userId,String method);


    ResultBody pushAutoOneOffice(Map map);

    ResultBody pushViewAutoOffice(String taskId);

    ResultBody saveOfficeAccount(WcOfficeAccount wcOfficeAccount);


    ResultBody getAgentBind(Long userId);
    ResultBody getSpaceInfoByUserId(Long userId);

    ResultBody getJsPromptByName(String templateName);


    ResultBody saveAgentBind(Map map);
    ResultBody saveUserFlowId(Map map);

    ResultBody saveSpaceBind(Map map);



    ResultBody saveChromeTaskData(String taskId,String userid,String corpId);

    ResultBody getTaskStatus(String taskId);

    ResultBody getUserPromptTem(String userId,String taskId);

    ResultBody getPromptTem(Integer type,String userId);

    ResultBody updateUserPromptTem(Map map);

    ResultBody getIsChangeByCorpId(String corpId);


    ResultBody getOfficeAccount(Long userId);

    List<PromptTemplate> getScorePromptList(ScorePromptQuery scorePromptQuery);

    ResultBody getScorePrompt(Long id);

    ResultBody saveScorePrompt(PromptTemplate promptTemplate);

    ResultBody updateScorePrompt(PromptTemplate promptTemplate);

    ResultBody deleteScorePrompt(Long[] ids);

    ResultBody getAllScorePrompt();

    List<ArtTemplate> getArtPromptList(ArtPromptQuery artPromptQuery);

    ResultBody getArtPrompt(Long id);

    ResultBody saveArtPrompt(ArtTemplate artTemplate);

    ResultBody updateArtPrompt(ArtTemplate artTemplate);

    ResultBody deleteArtPrompt(Long[] ids);

    ResultBody getAllArtPrompt();

    List<IdeaTemplate> getIdeaPromptList(IdeaPromptQuery ideaPromptQuery);

    ResultBody getIdeaPrompt(Long id);

    ResultBody saveIdeaPrompt(IdeaTemplate ideaTemplate);

    ResultBody updateIdeaPrompt(IdeaTemplate ideaTemplate);

    ResultBody deleteIdeaPrompt(Long[] ids);

    ResultBody getAllIdeaPrompt();

    /**
     * 上架评分模板并定价
     *
     * @param id 模板ID
     * @param price 定价
     * @return 操作结果
     */
    ResultBody publishScorePrompt(Long id, java.math.BigDecimal price);

    /**
     * 下架评分模板
     *
     * @param id 模板ID
     * @return 操作结果
     */
    ResultBody unpublishScorePrompt(Long id);

    /**
     * 设置评分模板为公共/私有
     *
     * @param id 模板ID
     * @param isCommon 是否公共(0:私有 1:公共)
     * @return 操作结果
     */
    ResultBody setScorePromptCommon(Long id, Integer isCommon);

    /**
     * 查询我创建的评分模板（模板类型：2-评分模板）
     *
     * @param queryParams 查询参数
     * @return 模板列表
     */
    com.cube.common.core.page.TableDataInfo getMyCreatedTemplates(com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery queryParams);

    /**
     * 查询我购买的评分模板（模板类型：2-评分模板）
     *
     * @param queryParams 查询参数
     * @return 模板列表
     */
    com.cube.common.core.page.TableDataInfo getMyPurchasedTemplates(com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery queryParams);

    /**
     * 查询市场中的评分模板（已上架的评分模板，模板类型：2-评分模板）
     *
     * @param queryParams 查询参数
     * @return 模板列表
     */
    com.cube.common.core.page.TableDataInfo getMarketTemplates(com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery queryParams);

    /**
     * 购买评分模板（模板类型：2-评分模板）
     * 注意：此方法仅用于购买评分模板，提示词模板的购买需要使用其他方法
     *
     * @param templateId 评分模板ID
     * @return 操作结果
     */
    ResultBody purchaseTemplate(Long templateId);

    /**
     * 获取我的积分
     *
     * @return 积分数量
     */
    ResultBody getMyPoints();

    /**
     * 查询我创建的提示词模板（模板类型：1-提示词模板）
     *
     * @param queryParams 查询参数
     * @return 模板列表
     */
    com.cube.common.core.page.TableDataInfo getMyCreatedCallWords(com.cube.wechat.selfapp.app.domain.query.CallWordQuery queryParams);

    /**
     * 查询我购买的提示词模板（模板类型：1-提示词模板）
     *
     * @param queryParams 查询参数
     * @return 模板列表
     */
    com.cube.common.core.page.TableDataInfo getMyPurchasedCallWords(com.cube.wechat.selfapp.app.domain.query.CallWordQuery queryParams);

    /**
     * 查询市场中的提示词模板（已上架的提示词模板，模板类型：1-提示词模板）
     *
     * @param queryParams 查询参数
     * @return 模板列表
     */
    com.cube.common.core.page.TableDataInfo getMarketCallWords(com.cube.wechat.selfapp.app.domain.query.CallWordQuery queryParams);

    /**
     * 购买提示词模板（模板类型：1-提示词模板）
     * 注意：此方法仅用于购买提示词模板，评分模板的购买需要使用其他方法
     *
     * @param platformId 提示词模板platformId
     * @return 操作结果
     */
    ResultBody purchaseCallWord(String platformId);

}
