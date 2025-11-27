package com.cube.wechat.selfapp.app.controller;

import com.cube.common.annotation.Log;
import com.cube.common.annotation.RateLimiter;
import com.cube.common.core.controller.BaseController;
import com.cube.common.core.domain.AjaxResult;
import com.cube.common.core.page.TableDataInfo;
import com.cube.common.enums.BusinessType;
import com.cube.common.utils.StringUtils;
import com.cube.wechat.selfapp.app.config.MyWebSocketHandler;
import com.cube.wechat.selfapp.app.domain.*;
import com.cube.wechat.selfapp.app.domain.query.ArtPromptQuery;
import com.cube.wechat.selfapp.app.domain.query.IdeaPromptQuery;
import com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery;
import com.cube.wechat.selfapp.app.mapper.ArtTemplateMapper;
import com.cube.wechat.selfapp.app.mapper.IdeaTemplateMapper;
import com.cube.wechat.selfapp.app.mapper.PromptTemplateMapper;
import com.cube.wechat.selfapp.app.service.AIGCService;
import com.cube.wechat.selfapp.app.service.UserInfoService;
import com.cube.wechat.selfapp.app.service.WechatMpService;
import com.cube.wechat.selfapp.corpchat.util.ResultBody;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author AspireLife
 * @version JDK 1.8
 * @date 2024年09月04日 09:26
 */

@RestController
@RequestMapping("/mini")
public class UserInfoController extends BaseController {


    @Autowired
    private UserInfoService userInfoService;


    @Autowired
    private MyWebSocketHandler myWebSocketHandler;
    @Autowired
    private WechatMpService wechatMpService;
    @Autowired
    private AIGCService aigcService;
    @Autowired
    private PromptTemplateMapper promptTemplateMapper;
    @Autowired
    private ArtTemplateMapper artTemplateMapper;
    @Autowired
    private IdeaTemplateMapper ideaTemplateMapper;

    @GetMapping("/getOfficeAccount")
    public ResultBody getOfficeAccount(){
        return userInfoService.getOfficeAccount(getUserId());
    }

    @GetMapping("/getUserCount")
    public ResultBody getUserCount(@ApiParam("用户ID") String userId){
        return userInfoService.getUserCount(userId);
    };

    @PostMapping("/getUserPointsRecord")
    public ResultBody getUserPointsRecord(@RequestBody Map map){
        return userInfoService.getUserPointsRecord(map);
    };

    @PostMapping("/saveAIChatHistory")
    public ResultBody saveAIChatHistory(@RequestBody AIParam aiParam){
        return userInfoService.saveAIChatHistory(aiParam);
    }

    @PostMapping("/saveAINodeLog")
    public ResultBody saveAINodeLog(@RequestBody AINodeLog AINodeLog){
        return userInfoService.saveAINodeLog(AINodeLog);
    }



    @GetMapping("/getUserChatHistory")
    public ResultBody getUserChatHistory(String userId,String title){
        return userInfoService.getUserChatHistoryList(userId,title);
    }

    @GetMapping("/getChatHistoryDetail")
    public ResultBody getChatHistoryDetail(String conversationId){
        return userInfoService.getChatHistoryDetail(conversationId);
    }

    @PostMapping("/updateChatTitle")
    public ResultBody updateChatTitle(@RequestBody Map map){
        return userInfoService.updateChatTitle(map);
    }

    @PostMapping("/deleteUserChatHistory")
    public ResultBody deleteUserChatHistory(@RequestBody List<String> list){
        return userInfoService.deleteUserChatHistory(list);
    }

    @PostMapping("/saveChromeData")
    public ResultBody saveChromeData(@RequestBody Map map){
        if(map!=null){
            if(map.get("answer")!=null && !map.get("answer").equals("")){
                map.put("answer",map.get("answer").toString().replaceAll("<[^>]+>", "").trim());
            }
            map.put("promptNum",map.get("prompt").toString().length());
            map.put("answerNum",map.get("answer").toString().length());
            map.put("username",map.get("username").toString().trim());
            userInfoService.saveChromeData(map);
        }

        return ResultBody.success("");
    }


    @PostMapping("/pushOffice")
    public ResultBody pushOffice(@RequestBody List<String> ids){
        String userName = getUsername();
//        String userName = "o3lds67b1zyFvifHTC_32epnmzqM";
        return userInfoService.pushOffice(ids,userName);
    }

    @RateLimiter(time = 60, count = 5)
    @GetMapping("/authChecker")
    public ResultBody checkAuth(String username){
        return userInfoService.authChecker(username);
    }

    @RateLimiter(time = 60, count = 10)
    @GetMapping("/changePoint")
    public ResultBody changePoint(String userId,String method){
        return userInfoService.changePoint(userId,method);
    }


    @PostMapping("/pushAutoOffice")
    public ResultBody pushAutoOffice(@RequestBody Map map){
//        return userInfoService.pushAutoOneOffice(map);
        try {
            String userId = map.get("userId") + "";
            String contentText = map.get("contentText") + "";

            // 验证内容不为空
            if (contentText == null || contentText.trim().isEmpty() || contentText.equals("null")) {
                throw new RuntimeException("投递内容为空，请先进行AI排版生成内容");
            }

            String unionId = aigcService.getUnionIdByUserId(userId);

            // 检查用户是否绑定公众号
            ResultBody officeAccountResult = userInfoService.getOfficeAccountByUserId(userId);
            if (officeAccountResult.getCode() != 200 || officeAccountResult.getData() == null) {
                throw new RuntimeException("未绑定公众号，请先在系统中绑定公众号后再进行投递");
            }

            WcOfficeAccount woa = (WcOfficeAccount) officeAccountResult.getData();

//            获取素材信息
            int first = contentText.indexOf("《");
            int second = contentText.indexOf("》", first + 1);

            if (first == -1 || second == -1 || first >= second) {
                throw new RuntimeException("内容格式错误，标题格式应为《标题》，请检查AI排版结果");
            }

            String title = contentText.substring(first + 1, second);

            // 安全地提取内容部分
            int lastGtIndex = contentText.lastIndexOf(">");
            if (lastGtIndex != -1 && lastGtIndex > second) {
                contentText = contentText.substring(second + 1, lastGtIndex + 1);
            } else {
                // 如果没有找到合适的结束位置，就取标题之后的所有内容
                contentText = contentText.substring(second + 1);
            }

            contentText = contentText.replaceAll("\r\n\r\n", "");
            map.put("userId",userId);
            map.put("title",title);
            map.put("contentText", contentText);
            map.put("unionId", unionId);
            map.put("thumbMediaId", woa.getMediaId());
            return wechatMpService.publishToOffice(map);
        } catch (RuntimeException e) {
            // 保持原有的业务异常消息
            throw e;
        } catch (Exception e) {
            System.err.println("投递到公众号失败：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("投递失败：" + e.getMessage());
        }
    }

    @GetMapping("/getViewAutoOffice")
    public ResultBody getViewAutoOffice(String taskId){
        return userInfoService.pushViewAutoOffice(taskId);
    }

    @GetMapping("/getAgentBind")
    public ResultBody getAgentBind(){
        return userInfoService.getAgentBind(getUserId());
    }

    @RateLimiter(time = 60, count = 10)
    @GetMapping("/getSpaceInfoByUserId")
    public ResultBody getSpaceInfoByUserId(String userId){
        if(StringUtils.isNotEmpty(userId)){
            return userInfoService.getSpaceInfoByUserId(Long.valueOf(userId));
        }else{
            return userInfoService.getSpaceInfoByUserId(getUserId());
        }
    }
    @RateLimiter(time = 60, count = 10)
    @GetMapping("/getJsPromptByName")
    public ResultBody getJsPromptByName(String templateName){
        return userInfoService.getJsPromptByName(templateName);
    }
    @RateLimiter(time = 60, count = 10)
    @PostMapping("/bindUserFlowId")
    public ResultBody bindUserFlowId(@RequestBody Map map){
        return userInfoService.saveUserFlowId(map);
    }

    @PostMapping("/saveAgentBind")
    public ResultBody saveAgentBind(@RequestBody Map map){
        map.put("userId",getUserId());
        return userInfoService.saveAgentBind(map);
    }
    @PostMapping("/saveSpaceBind")
    public ResultBody saveSpaceBind(@RequestBody Map map){
        map.put("userId",getUserId());
        return userInfoService.saveSpaceBind(map);
    }


    @PostMapping("/saveWcOfficeAccount")
    public ResultBody saveWcOfficeAccount(@RequestBody WcOfficeAccount wcOfficeAccount){
        if (wcOfficeAccount == null) {
            return ResultBody.error(201, "绑定失败：参数为空");
        }

        wcOfficeAccount.setUserId(getUserId());
        wcOfficeAccount.setUserName(getUsername());
        return userInfoService.saveOfficeAccount(wcOfficeAccount);
    }

    @PostMapping("/receiveKeyword")
    public ResultBody receiveKeyword(@RequestBody Map<String, String> request) {
        String keyword = request.get("keyword");
        String userid = request.get("userid");
        String corpId = request.get("corpId");
        String taskId = request.get("taskId");
        String username = request.get("username");
        try {
            userInfoService.saveChromeTaskData(taskId,userid,corpId);
            myWebSocketHandler.sendMessageToClient(userid,keyword,taskId,corpId,username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将关键词发送给 WebSocket 服务
        return ResultBody.success("发送成功");
    }

    @GetMapping("/checkClentStatus")
    public ResultBody checkClentStatus(String corpId) {
        try {


            String status = myWebSocketHandler.sendMessageToClient(corpId, "heartbeat","taskId",null,null);
            return ResultBody.success(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将关键词发送给 WebSocket 服务
        return ResultBody.success("发送成功");
    }


    @GetMapping("/getUserPromptTem")
    public ResultBody getUserPromptTem(String userId,String agentId) {
        return userInfoService.getUserPromptTem(userId,agentId);
    }

    @GetMapping("/getPromptTem")
    public ResultBody getPromptTem(Integer type,String userId){
        return userInfoService.getPromptTem(type,userId);
    }

    @PostMapping("/updateUserPromptTem")
    public ResultBody updateUserPromptTem(@RequestBody Map map) {


        return userInfoService.updateUserPromptTem(map);
    }



    @GetMapping("/getTaskStatus")
    public ResultBody getTaskStatus(String taskId){
        return userInfoService.getTaskStatus(taskId);
    };

    @GetMapping("/getIsChangeByCorpId")
    public ResultBody getIsChangeByCorpId(String corpId){ return userInfoService.getIsChangeByCorpId(corpId);
    };

    @GetMapping("/getScorePrompt/{id}")
    public ResultBody getScorePrompt(@PathVariable Long id){
        return userInfoService.getScorePrompt(id);
    }

    @GetMapping("/getScorePromptList")
    public TableDataInfo getScorePromptList(ScorePromptQuery scorePromptQuery){
        startPage();
        List<PromptTemplate> list = userInfoService.getScorePromptList(scorePromptQuery);
        return getDataTable(list);
    }

    //获取当前用户的所有评分提示词
    @GetMapping("/getAllScorePrompt")
    public ResultBody getAllScorePrompt(){
        return userInfoService.getAllScorePrompt();
    }

    @PostMapping("/saveScorePrompt")
    public ResultBody saveScorePrompt(@RequestBody PromptTemplate promptTemplate){
        return userInfoService.saveScorePrompt(promptTemplate);
    }

    @PutMapping("/updateScorePrompt")
    public ResultBody updateScorePrompt(@RequestBody PromptTemplate promptTemplate){
        return userInfoService.updateScorePrompt(promptTemplate);
    }

    @DeleteMapping("/deleteScorePrompt")
    public ResultBody deleteScorePrompt(@RequestBody Long[] ids){
        return userInfoService.deleteScorePrompt(ids);
    }

    @GetMapping("/getIdeaPrompt/{id}")
    public ResultBody getIdeaPrompt(@PathVariable Long id){
        return userInfoService.getIdeaPrompt(id);
    }

    @GetMapping("/getIdeaPromptList")
    public TableDataInfo getIdeaPromptList(IdeaPromptQuery ideaPromptQuery){
        startPage();
        List<IdeaTemplate> list = userInfoService.getIdeaPromptList(ideaPromptQuery);
        return getDataTable(list);
    }

    //获取当前用户的所有评分提示词
    @GetMapping("/getAllIdeaPrompt")
    public ResultBody getAllIdeaPrompt(){
        return userInfoService.getAllIdeaPrompt();
    }

    @PostMapping("/saveIdeaPrompt")
    public ResultBody saveIdeaPrompt(@RequestBody IdeaTemplate ideaTemplate){
        return userInfoService.saveIdeaPrompt(ideaTemplate);
    }

    @PutMapping("/updateIdeaPrompt")
    public ResultBody updateIdeaPrompt(@RequestBody IdeaTemplate ideaTemplate){
        return userInfoService.updateIdeaPrompt(ideaTemplate);
    }

    @DeleteMapping("/deleteIdeaPrompt")
    public ResultBody deleteIdeaPrompt(@RequestBody Long[] ids){
        return userInfoService.deleteIdeaPrompt(ids);
    }

    @GetMapping("/getArtPrompt/{id}")
    public ResultBody getArtPrompt(@PathVariable Long id){
        return userInfoService.getArtPrompt(id);
    }

    @GetMapping("/getArtPromptList")
    public TableDataInfo getArtPromptList(ArtPromptQuery artPromptQuery){
        startPage();
        List<ArtTemplate> list = userInfoService.getArtPromptList(artPromptQuery);
        return getDataTable(list);
    }

    //获取当前用户的所有艺术提示词
    @GetMapping("/getAllArtPrompt")
    public ResultBody getAllArtPrompt(){
        return userInfoService.getAllArtPrompt();
    }

    @PostMapping("/saveArtPrompt")
    public ResultBody saveArtPrompt(@RequestBody ArtTemplate artTemplate){
        return userInfoService.saveArtPrompt(artTemplate);
    }

    @PutMapping("/updateArtPrompt")
    public ResultBody updateArtPrompt(@RequestBody ArtTemplate artTemplate){
        return userInfoService.updateArtPrompt(artTemplate);
    }

    @DeleteMapping("/deleteArtPrompt")
    public ResultBody deleteArtPrompt(@RequestBody Long[] ids){
        return userInfoService.deleteArtPrompt(ids);
    }

    /**
     * 设置评分模板为公共/私有（仅管理员）
     *
     * @param id 模板ID
     * @param isCommon 是否公共(0:私有 1:公共)
     * @return 操作结果
     */
    @Log(title = "设置公共评分模板", businessType = BusinessType.UPDATE)
    @PutMapping("/setScorePromptCommon/{id}/{isCommon}")
    public AjaxResult setScorePromptCommon(@PathVariable Long id, @PathVariable Integer isCommon) {
        try {
            // 手动检查权限：只有admin或common角色可以设置公共模板
            if (!com.cube.common.utils.SecurityUtils.hasRole("admin") && 
                !com.cube.common.utils.SecurityUtils.hasRole("common")) {
                return AjaxResult.error("您没有权限执行此操作");
            }
            
            ResultBody result = userInfoService.setScorePromptCommon(id, isCommon);
            if (result.getCode() == 200) {
                return AjaxResult.success(result.getMessages());
            } else {
                return AjaxResult.error(result.getMessages());
            }
        } catch (Exception e) {
            logger.error("设置公共评分模板失败", e);
            return AjaxResult.error("设置失败：" + e.getMessage());
        }
    }

    /**
     * 上架评分模板并定价
     *
     * @param request 上架请求
     * @return 操作结果
     */
    @Log(title = "上架评分模板", businessType = BusinessType.UPDATE)
    @PutMapping("/publishScorePrompt")
    public AjaxResult publishScorePrompt(@RequestBody com.cube.wechat.selfapp.app.domain.request.ScorePromptPublishRequest request) {
        if (request == null || request.getId() == null) {
            return AjaxResult.error("模板ID不能为空");
        }
        ResultBody result = userInfoService.publishScorePrompt(request.getId(), request.getPrice());
        if (result.getCode() == 200) {
            return AjaxResult.success(result.getMessages());
        } else {
            return AjaxResult.error(result.getMessages());
        }
    }

    /**
     * 下架评分模板
     *
     * @param id 模板ID
     * @return 操作结果
     */
    @Log(title = "下架评分模板", businessType = BusinessType.UPDATE)
    @PutMapping("/unpublishScorePrompt/{id}")
    public AjaxResult unpublishScorePrompt(@PathVariable Long id) {
        ResultBody result = userInfoService.unpublishScorePrompt(id);
        if (result.getCode() == 200) {
            return AjaxResult.success(result.getMessages());
        } else {
            return AjaxResult.error(result.getMessages());
        }
    }

    /**
     * 查询我创建的评分模板（模板类型：2-评分模板）
     * 注意：此接口仅查询评分模板，不包含提示词模板
     */
    @GetMapping("/getMyCreatedTemplates")
    public TableDataInfo getMyCreatedTemplates(ScorePromptQuery queryParams) {
        com.cube.common.core.page.TableDataInfo dataTable = userInfoService.getMyCreatedTemplates(queryParams);
        return dataTable;
    }

    /**
     * 查询我购买的评分模板（模板类型：2-评分模板）
     * 注意：此接口仅查询评分模板的购买记录，不包含提示词模板
     */
    @GetMapping("/getMyPurchasedTemplates")
    public TableDataInfo getMyPurchasedTemplates(ScorePromptQuery queryParams) {
        com.cube.common.core.page.TableDataInfo dataTable = userInfoService.getMyPurchasedTemplates(queryParams);
        return dataTable;
    }

    /**
     * 查询市场中的评分模板（已上架的评分模板，模板类型：2-评分模板）
     * 注意：此接口仅查询评分模板，不包含提示词模板
     */
    @GetMapping("/getMarketTemplates")
    public TableDataInfo getMarketTemplates(ScorePromptQuery queryParams) {
        com.cube.common.core.page.TableDataInfo dataTable = userInfoService.getMarketTemplates(queryParams);
        return dataTable;
    }

    /**
     * 购买评分模板（模板类型：2-评分模板）
     * 注意：此接口仅用于购买评分模板，提示词模板的购买需要使用其他接口
     */
    @Log(title = "购买评分模板", businessType = BusinessType.INSERT)
    @PostMapping("/purchaseTemplate/{templateId}")
    public AjaxResult purchaseTemplate(@PathVariable Long templateId) {
        ResultBody result = userInfoService.purchaseTemplate(templateId);
        if (result.getCode() == 200) {
            return AjaxResult.success(result.getMessages());
        } else {
            return AjaxResult.error(result.getMessages());
        }
    }

    /**
     * 查询我创建的提示词模板（模板类型：1-提示词模板）
     */
    @GetMapping("/getMyCreatedCallWords")
    public TableDataInfo getMyCreatedCallWords(com.cube.wechat.selfapp.app.domain.query.CallWordQuery queryParams) {
        com.cube.common.core.page.TableDataInfo dataTable = userInfoService.getMyCreatedCallWords(queryParams);
        return dataTable;
    }

    /**
     * 查询我购买的提示词模板（模板类型：1-提示词模板）
     */
    @GetMapping("/getMyPurchasedCallWords")
    public TableDataInfo getMyPurchasedCallWords(com.cube.wechat.selfapp.app.domain.query.CallWordQuery queryParams) {
        com.cube.common.core.page.TableDataInfo dataTable = userInfoService.getMyPurchasedCallWords(queryParams);
        return dataTable;
    }

    /**
     * 查询市场中的提示词模板（已上架的提示词模板，模板类型：1-提示词模板）
     */
    @GetMapping("/getMarketCallWords")
    public TableDataInfo getMarketCallWords(com.cube.wechat.selfapp.app.domain.query.CallWordQuery queryParams) {
        com.cube.common.core.page.TableDataInfo dataTable = userInfoService.getMarketCallWords(queryParams);
        return dataTable;
    }

    /**
     * 购买提示词模板（模板类型：1-提示词模板）
     */
    @Log(title = "购买提示词模板", businessType = BusinessType.INSERT)
    @PostMapping("/purchaseCallWord/{platformId}")
    public AjaxResult purchaseCallWord(@PathVariable String platformId) {
        ResultBody result = userInfoService.purchaseCallWord(platformId);
        if (result.getCode() == 200) {
            return AjaxResult.success(result.getMessages());
        } else {
            return AjaxResult.error(result.getMessages());
        }
    }

    /**
     * 获取我的积分
     */
    @GetMapping("/getMyPoints")
    public AjaxResult getMyPoints() {
        ResultBody result = userInfoService.getMyPoints();
        if (result.getCode() == 200) {
            return AjaxResult.success("获取成功", result.getData());
        } else {
            return AjaxResult.error(result.getMessages());
        }
    }

    /**
     * 设置文章模板为公共/私有（仅管理员）
     *
     * @param id 模板ID
     * @param isCommon 是否公共(0:私有 1:公共)
     * @return 操作结果
     */
    @Log(title = "设置公共文章模板", businessType = BusinessType.UPDATE)
    @PutMapping("/setArtPromptCommon/{id}/{isCommon}")
    public AjaxResult setArtPromptCommon(@PathVariable Long id, @PathVariable Integer isCommon) {
        try {
            // 手动检查权限：只有admin或common角色可以设置公共模板
            if (!com.cube.common.utils.SecurityUtils.hasRole("admin") && 
                !com.cube.common.utils.SecurityUtils.hasRole("common")) {
                return AjaxResult.error("您没有权限执行此操作");
            }
            
            ArtTemplate artTemplate = artTemplateMapper.getArtPromptById(id);
            if (artTemplate == null) {
                return AjaxResult.error("模板不存在");
            }
            artTemplate.setIsCommon(isCommon);
            int result = artTemplateMapper.updateArtTemplate(artTemplate);
            return result > 0 ? AjaxResult.success("设置成功") : AjaxResult.error("设置失败");
        } catch (Exception e) {
            logger.error("设置公共文章模板失败", e);
            return AjaxResult.error("设置失败：" + e.getMessage());
        }
    }

    /**
     * 设置思路模板为公共/私有（仅管理员）
     *
     * @param id 模板ID
     * @param isCommon 是否公共(0:私有 1:公共)
     * @return 操作结果
     */
    @Log(title = "设置公共思路模板", businessType = BusinessType.UPDATE)
    @PutMapping("/setIdeaPromptCommon/{id}/{isCommon}")
    public AjaxResult setIdeaPromptCommon(@PathVariable Long id, @PathVariable Integer isCommon) {
        try {
            // 手动检查权限：只有admin或common角色可以设置公共模板
            if (!com.cube.common.utils.SecurityUtils.hasRole("admin") && 
                !com.cube.common.utils.SecurityUtils.hasRole("common")) {
                return AjaxResult.error("您没有权限执行此操作");
            }
            
            IdeaTemplate ideaTemplate = ideaTemplateMapper.getIdeaPromptById(id);
            if (ideaTemplate == null) {
                return AjaxResult.error("模板不存在");
            }
            ideaTemplate.setIsCommon(isCommon);
            int result = ideaTemplateMapper.updateIdeaTemplate(ideaTemplate);
            return result > 0 ? AjaxResult.success("设置成功") : AjaxResult.error("设置失败");
        } catch (Exception e) {
            logger.error("设置公共思路模板失败", e);
            return AjaxResult.error("设置失败：" + e.getMessage());
        }
    }

}

