package com.cube.wechat.selfapp.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cube.common.utils.SecurityUtils;
import com.cube.common.utils.StringUtils;
import com.cube.point.controller.PointsSystem;
import com.cube.wechat.selfapp.app.domain.*;
import com.cube.wechat.selfapp.app.domain.query.ArtPromptQuery;
import com.cube.wechat.selfapp.app.domain.query.IdeaPromptQuery;
import com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery;
import com.cube.wechat.selfapp.app.mapper.ArtTemplateMapper;
import com.cube.wechat.selfapp.app.mapper.IdeaTemplateMapper;
import com.cube.wechat.selfapp.app.mapper.PromptTemplateMapper;
import com.cube.wechat.selfapp.app.mapper.TemplateAuthorMapper;
import com.cube.wechat.selfapp.app.mapper.UserInfoMapper;
import com.cube.wechat.selfapp.app.service.helper.TemplateAuthorIdentityManager;
import com.cube.wechat.selfapp.app.service.UserInfoService;
import com.cube.wechat.selfapp.app.util.RestUtils;
import com.cube.wechat.selfapp.corpchat.util.RedisUtil;
import com.cube.wechat.selfapp.corpchat.util.ResultBody;
import com.cube.wechat.selfapp.corpchat.util.WeChatApiUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author AspireLife
 * @version JDK 1.8
 * @date 2024年10月23日 10:28
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PromptTemplateMapper promptTemplateMapper;

    @Autowired
    private PointsSystem pointsSystem;

    @Autowired
    private WeChatApiUtils weChatApiUtils;


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ArtTemplateMapper artTemplateMapper;

    @Autowired
    private IdeaTemplateMapper ideaTemplateMapper;

    @Autowired
    private TemplateAuthorIdentityManager templateAuthorIdentityManager;

    @Autowired
    private TemplateAuthorMapper templateAuthorMapper;

    @Autowired
    private com.cube.wechat.selfapp.app.mapper.CallWordMapper callWordMapper;

    @Autowired
    private com.cube.wechat.selfapp.app.mapper.TemplatePurchaseMapper templatePurchaseMapper;

    /**
     * 查询个人中心统计数据
     */
    @Override
    public ResultBody getUserCount(String userId) {
        return ResultBody.success(userInfoMapper.getUserCount(userId));
    }

    /**
     * 获取积分明细
     */
    @Override
    public ResultBody getUserPointsRecord(Map map) {
        PageHelper.startPage((int) map.get("pageIndex"), (int) map.get("pageSize"));
        List<Map> list = userInfoMapper.getUserPointsRecord(map);
        PageInfo pageInfo = new PageInfo(list);
        return ResultBody.success(pageInfo);
    }


    @Override
    public ResultBody saveAIChatHistory(AIParam aiParam) {
        userInfoMapper.saveAIChatHistory(aiParam);
        return ResultBody.success("保存成功");
    }

    @Override
    public ResultBody saveAINodeLog(AINodeLog aiNodeLog) {
        userInfoMapper.saveAINodeLog(aiNodeLog);
        return ResultBody.success("保存成功");
    }


    @Override
    public ResultBody getUserChatHistoryList(String userId, String title) {
        return ResultBody.success(userInfoMapper.getUserChatHistoryList(userId, title));
    }

    @Override
    public ResultBody getChatHistoryDetail(String conversationId) {
        String chatHisroty = userInfoMapper.getChatHistoryDetail(conversationId);
        JSONArray contentArray = JSONArray.parseArray(chatHisroty);
        return ResultBody.success(contentArray);
    }

    @Override
    public ResultBody deleteUserChatHistory(List<String> list) {
        for (String s : list) {
            redisUtil.delete("dudu." + s);
            userInfoMapper.deleteUserChatHistory(s);
        }
        return ResultBody.success("删除成功");
    }

    @Override
    public ResultBody saveChromeData(Map map) {
        userInfoMapper.saveChromeData(map);
        return ResultBody.success("成功");
    }

    @Override
    public ResultBody updateChatTitle(Map map) {
        userInfoMapper.updateChatTitle(map);
        return ResultBody.success("成功");
    }

    @Override
    public ResultBody pushOffice(List<String> ids, String userName) {
        WcOfficeAccount woa = userInfoMapper.getOfficeAccountByUserName(userName);
        List<Map> list = userInfoMapper.getPushOfficeData(ids, userName);
        if (list.isEmpty()) {
            return ResultBody.error(300, "暂无素材可被收录");
        }
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter); // 格式化日期

        String title = woa.getOfficeAccountName() + "今日速递-" + formattedDate;
//        使用 Map 聚合数据
        Map<String, List<String>> groupedData = new LinkedHashMap<>();
        for (Map map : list) {
            String linkUrl = map.get("answer") + "";
            String keyWord = map.get("prompt") + "";
            String author = map.get("author") + "";
            String liTitle = "<a href ='" + linkUrl + "' style='color:#576b95' >" + author + "：" + map.get("title") + "</a>";
            String str = map.get("summary").toString().replace("；;", "；");
            str = str.replace(";", "；");
            String[] summarys = str.split("；");
            String summary = "<p style='font-family: 'Arial''>";
            int i = 1;
            for (String s : summarys) {

                if (i == 1) {
                    summary = summary + i + ". " + s + "。<br><br>";
                } else if (i == 2) {
                    summary = summary + i + "." + s + "。<br><br>";
                } else {
                    summary = summary + i + "." + s + "<br><br>";
                }
                i++;
            }
            summary = summary + "</p>";
            String content = liTitle + "<br><br>" + summary + "<br><br>";
            groupedData.computeIfAbsent(keyWord, k -> new ArrayList<>()).add(content);
        }

        // 输出结果
        String res = "";
        for (Map.Entry<String, List<String>> entry : groupedData.entrySet()) {
            String keyWord = "<p style='font-weight: normal;color:red;'>" + entry.getKey() + "</p>";
            String content = String.join("", entry.getValue());
            res = res + keyWord + "<br>" + content;
        }
//        V4PNB2XjrprWdg1sJxs7jpoxWs9YhZy8zYH38cbZSl3JzYw_liIxBesx7PuQ7-jV

        System.out.println(res);
        //String assessToken = weChatApiUtils.getOfficeAccessToken("wx4461361a058d608b","8e97c88d040ac7248f5f6240e578a1f3");
        String assessToken = weChatApiUtils.getOfficeAccessToken(woa.getAppId(), woa.getAppSecret());
        String url = "https://api.weixin.qq.com/cgi-bin/draft/add?access_token=" + assessToken;

        List<JSONObject> paramlist = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("author", woa.getOfficeAccountName());
        jsonObject.put("content", res);
        //jsonObject.put("thumb_media_id","V4PNB2XjrprWdg1sJxs7jpoxWs9YhZy8zYH38cbZSl3JzYw_liIxBesx7PuQ7-jV");
        jsonObject.put("thumb_media_id", woa.getMediaId());
        paramlist.add(jsonObject);
        JSONObject param = new JSONObject();
        param.put("articles", paramlist);
        try {
            RestUtils.post(url, param);
        } catch (Exception e) {

        }
        userInfoMapper.updateLinkStatus(list);
        return ResultBody.success("上传成功！");
    }

    public ResultBody authChecker(String userName) {
        Integer num = userInfoMapper.getUserCountByUserName(userName);
        if (num > 0) {
            return ResultBody.success("上传成功！");
        }
        return ResultBody.error(404, "用户不存在");
    }

    public ResultBody changePoint(String userId, String method) {
        Integer points = pointsSystem.getUserPoints(userId);
        if (points < 1) {
            return ResultBody.error(201, "积分余额不足，请明日再来或者联系客服充值");
        }
        com.cube.point.util.ResultBody pointResult = pointsSystem.setUserPoint(userId, method, null, "0x2edc4228a84d672affe8a594033cb84a029bcafc", "f34f737203aa370f53ef0e041c1bff36bf59db8eb662cdb447f01d9634374dd");
        if (pointResult == null || pointResult.getCode() != 200) {
            return ResultBody.error(500, "积分扣减失败: " + (pointResult != null ? pointResult.getMessages() : "未知错误"));
        }
        return ResultBody.success("执行成功！");
    }

    @Override
    public ResultBody getOfficeAccountByUserId(String userId) {
        WcOfficeAccount woa = userInfoMapper.getOfficeAccountByUserName(userId);
        if (woa != null) {
            return ResultBody.success(woa);
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody getOfficeAccount(Long userId) {
        WcOfficeAccount woa = userInfoMapper.getOfficeAccountByUserId(userId);
        if (woa != null) {
            return ResultBody.success(woa);
        }
        return ResultBody.FAIL;
    }

    @Override
    public List<PromptTemplate> getScorePromptList(ScorePromptQuery scorePromptQuery) {
        Long userId = SecurityUtils.getUserId();
        return promptTemplateMapper.getScorePromptList(scorePromptQuery, userId);
    }

    @Override
    public ResultBody getScorePrompt(Long id) {
        PromptTemplate promptTemplate = promptTemplateMapper.getScorePromptById(id);
        if (promptTemplate != null) {
            return ResultBody.success(promptTemplate);
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody saveScorePrompt(PromptTemplate promptTemplate) {
        promptTemplate.setType(3L);
        promptTemplate.setUserId(SecurityUtils.getUserId());
        // 初始化上架相关字段
        if (promptTemplate.getStatus() == null) {
            promptTemplate.setStatus(0);
        }
        if (promptTemplate.getPrice() == null) {
            promptTemplate.setPrice(java.math.BigDecimal.ZERO);
        }
        if (promptTemplate.getSalesCount() == null) {
            promptTemplate.setSalesCount(0);
        }
        if (promptTemplate.getIncomeTotal() == null) {
            promptTemplate.setIncomeTotal(java.math.BigDecimal.ZERO);
        }
        int count = promptTemplateMapper.savePromptTemplate(promptTemplate);
        if (count > 0) {
            return ResultBody.success("添加成功");
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody updateScorePrompt(PromptTemplate promptTemplate) {
        // 检查是否是公共模板，只有管理员可以修改
        PromptTemplate existing = promptTemplateMapper.getScorePromptById(promptTemplate.getId());
        if (existing != null && existing.getIsCommon() != null && existing.getIsCommon() == 1) {
            if (!SecurityUtils.hasRole("admin")) {
                return ResultBody.error(403, "只有管理员可以修改公共模板");
            }
        }
        
        promptTemplate.setType(3L);
        promptTemplate.setUserId(SecurityUtils.getUserId());
        int count = promptTemplateMapper.updatePromptTemplate(promptTemplate);
        if (count > 0) {
            return ResultBody.success("修改成功");
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody deleteScorePrompt(Long[] ids) {
        // 检查是否包含公共模板，只有管理员可以删除
        for (Long id : ids) {
            PromptTemplate promptTemplate = promptTemplateMapper.getScorePromptById(id);
            if (promptTemplate != null && promptTemplate.getIsCommon() != null && promptTemplate.getIsCommon() == 1) {
                if (!SecurityUtils.hasRole("admin")) {
                    return ResultBody.error(403, "只有管理员可以删除公共模板");
                }
            }
        }
        
        int count = promptTemplateMapper.deletePromptTemplateByIds(ids);
        if (count > 0) {
            return ResultBody.success("删除成功");
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody getAllScorePrompt() {
        Long userId = SecurityUtils.getUserId();
        List<PromptTemplate> list = promptTemplateMapper.getAllScorePrompt(userId);
        if (list != null) {
            return ResultBody.success(list);
        }
        return ResultBody.FAIL;
    }

    @Override
    public List<ArtTemplate> getArtPromptList(ArtPromptQuery artPromptQuery) {
        Long userId = SecurityUtils.getUserId();
        return artTemplateMapper.getArtPromptList(artPromptQuery, userId);
    }

    @Override
    public ResultBody getArtPrompt(Long id) {
        ArtTemplate artTemplate = artTemplateMapper.getArtPromptById(id);
        if (artTemplate != null) {
            return ResultBody.success(artTemplate);
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody saveArtPrompt(ArtTemplate artTemplate) {
        artTemplate.setType(3L);
        artTemplate.setUserId(SecurityUtils.getUserId());
        int count = artTemplateMapper.saveArtTemplate(artTemplate);
        if (count > 0) {
            return ResultBody.success("添加成功");
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody updateArtPrompt(ArtTemplate artTemplate) {
        // 检查是否是公共模板，只有管理员可以修改
        ArtTemplate existing = artTemplateMapper.getArtPromptById(artTemplate.getId());
        if (existing != null && existing.getIsCommon() != null && existing.getIsCommon() == 1) {
            if (!SecurityUtils.hasRole("admin")) {
                return ResultBody.error(403, "只有管理员可以修改公共模板");
            }
        }
        
        artTemplate.setType(3L);
        artTemplate.setUserId(SecurityUtils.getUserId());
        int count = artTemplateMapper.updateArtTemplate(artTemplate);
        if (count > 0) {
            return ResultBody.success("修改成功");
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody deleteArtPrompt(Long[] ids) {
        // 检查是否包含公共模板，只有管理员可以删除
        for (Long id : ids) {
            ArtTemplate artTemplate = artTemplateMapper.getArtPromptById(id);
            if (artTemplate != null && artTemplate.getIsCommon() != null && artTemplate.getIsCommon() == 1) {
                if (!SecurityUtils.hasRole("admin")) {
                    return ResultBody.error(403, "只有管理员可以删除公共模板");
                }
            }
        }
        
        int count = artTemplateMapper.deleteArtTemplateByIds(ids);
        if (count > 0) {
            return ResultBody.success("删除成功");
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody getAllArtPrompt() {
        Long userId = SecurityUtils.getUserId();
        List<ArtTemplate> list = artTemplateMapper.getAllArtPrompt(userId);
        if (list != null) {
            return ResultBody.success(list);
        }
        return ResultBody.FAIL;
    }
    @Override
    public List<IdeaTemplate> getIdeaPromptList(IdeaPromptQuery ideaPromptQuery) {
        Long userId = SecurityUtils.getUserId();
        return ideaTemplateMapper.getIdeaPromptList(ideaPromptQuery, userId);
    }

    @Override
    public ResultBody getIdeaPrompt(Long id) {
        IdeaTemplate ideaTemplate = ideaTemplateMapper.getIdeaPromptById(id);
        if (ideaTemplate != null) {
            return ResultBody.success(ideaTemplate);
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody saveIdeaPrompt(IdeaTemplate ideaTemplate) {
        ideaTemplate.setType(3L);
        ideaTemplate.setUserId(SecurityUtils.getUserId());
        int count = ideaTemplateMapper.saveIdeaTemplate(ideaTemplate);
        if (count > 0) {
            return ResultBody.success("添加成功");
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody updateIdeaPrompt(IdeaTemplate ideaTemplate) {
        // 检查是否是公共模板，只有管理员可以修改
        IdeaTemplate existing = ideaTemplateMapper.getIdeaPromptById(ideaTemplate.getId());
        if (existing != null && existing.getIsCommon() != null && existing.getIsCommon() == 1) {
            if (!SecurityUtils.hasRole("admin")) {
                return ResultBody.error(403, "只有管理员可以修改公共模板");
            }
        }
        
        ideaTemplate.setType(3L);
        ideaTemplate.setUserId(SecurityUtils.getUserId());
        int count = ideaTemplateMapper.updateIdeaTemplate(ideaTemplate);
        if (count > 0) {
            return ResultBody.success("修改成功");
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody deleteIdeaPrompt(Long[] ids) {
        // 检查是否包含公共模板，只有管理员可以删除
        for (Long id : ids) {
            IdeaTemplate ideaTemplate = ideaTemplateMapper.getIdeaPromptById(id);
            if (ideaTemplate != null && ideaTemplate.getIsCommon() != null && ideaTemplate.getIsCommon() == 1) {
                if (!SecurityUtils.hasRole("admin")) {
                    return ResultBody.error(403, "只有管理员可以删除公共模板");
                }
            }
        }
        
        int count = ideaTemplateMapper.deleteIdeaTemplateByIds(ids);
        if (count > 0) {
            return ResultBody.success("删除成功");
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody getAllIdeaPrompt() {
        Long userId = SecurityUtils.getUserId();
        List<IdeaTemplate> list = ideaTemplateMapper.getAllIdeaPrompt(userId);
        if (list != null) {
            return ResultBody.success(list);
        }
        return ResultBody.FAIL;
    }

    @Override
    public ResultBody publishScorePrompt(Long id, java.math.BigDecimal price) {
        if (price == null || price.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            return ResultBody.error(400, "请输入大于0的价格");
        }
        PromptTemplate promptTemplate = promptTemplateMapper.selectScorePromptById(id);
        if (promptTemplate == null) {
            return ResultBody.error(404, "模板不存在");
        }
        if (promptTemplate.getIsCommon() != null && promptTemplate.getIsCommon() == 1) {
            return ResultBody.error(400, "公共模板默认已上架，无需重复操作");
        }
        Long operatorId = null;
        try {
            operatorId = SecurityUtils.getUserId();
        } catch (Exception ignore) {
        }
        Long ownerId = promptTemplate.getUserId();
        boolean isOwner = ownerId != null && ownerId.equals(operatorId);
        boolean isAdmin = false;
        try {
            isAdmin = SecurityUtils.hasRole("admin");
        } catch (Exception ignore) {
        }
        if (!isOwner && !isAdmin) {
            return ResultBody.error(403, "只能上架自己的模板");
        }
        PromptTemplate update = new PromptTemplate();
        update.setId(id);
        update.setPrice(price);
        update.setStatus(1);
        promptTemplateMapper.updatePromptTemplate(update);
        templateAuthorIdentityManager.grantAuthorRole(ownerId != null ? ownerId : operatorId);
        incrementReleaseCount(ownerId != null ? ownerId : operatorId, 1);
        
        // 发放模板上架奖励（遵循数据库中的限频配置）
        Long authorUserId = ownerId != null ? ownerId : operatorId;
        if (authorUserId != null) {
            // 调用积分系统，传入null让系统从规则配置中获取积分值和限频规则
            // 系统会自动根据数据库中的限频配置进行校验
            com.cube.point.util.ResultBody result = pointsSystem.setUserPoint(
                authorUserId.toString(),
                "模板上架奖励",
                null, // 传入null，让系统从规则配置中获取积分值
                null,
                null
            );
            if (result.getCode() != 200) {
                System.err.println("模板上架奖励发放失败：" + result.getMessages());
            }
        }
        
        return ResultBody.success("上架成功");
    }

    @Override
    public ResultBody unpublishScorePrompt(Long id) {
        PromptTemplate promptTemplate = promptTemplateMapper.selectScorePromptById(id);
        if (promptTemplate == null) {
            return ResultBody.error(404, "模板不存在");
        }
        if (promptTemplate.getIsCommon() != null && promptTemplate.getIsCommon() == 1) {
            return ResultBody.error(400, "公共模板无需下架");
        }
        Long operatorId = null;
        try {
            operatorId = SecurityUtils.getUserId();
        } catch (Exception ignore) {
        }
        Long ownerId = promptTemplate.getUserId();
        boolean isOwner = ownerId != null && ownerId.equals(operatorId);
        boolean isAdmin = false;
        try {
            isAdmin = SecurityUtils.hasRole("admin");
        } catch (Exception ignore) {
        }
        if (!isOwner && !isAdmin) {
            return ResultBody.error(403, "只能操作自己的模板");
        }
        PromptTemplate update = new PromptTemplate();
        update.setId(id);
        update.setStatus(0);
        promptTemplateMapper.updatePromptTemplate(update);
        Long targetUser = ownerId != null ? ownerId : operatorId;
        templateAuthorIdentityManager.grantAuthorRole(targetUser);
        incrementReleaseCount(targetUser, -1);
        return ResultBody.success("模板已下架");
    }

    @Override
    public ResultBody setScorePromptCommon(Long id, Integer isCommon) {
        try {
            PromptTemplate promptTemplate = promptTemplateMapper.selectScorePromptById(id);
            if (promptTemplate == null) {
                return ResultBody.error(404, "模板不存在");
            }
            
            // 参考 CallWord 的实现逻辑
            boolean wasPublished = promptTemplate.getStatus() != null && promptTemplate.getStatus() == 1;
            boolean willBePublic = isCommon != null && isCommon == 1;
            
            // 如果设置为公共模板且之前已上架，需要先下架
            if (willBePublic && wasPublished) {
                // 先下架
                PromptTemplate unpublishUpdate = new PromptTemplate();
                unpublishUpdate.setId(id);
                unpublishUpdate.setStatus(0);
                promptTemplateMapper.updatePromptTemplate(unpublishUpdate);
                wasPublished = false;
            }
            
            PromptTemplate update = new PromptTemplate();
            update.setId(id);
            update.setIsCommon(isCommon);
            
            // 如果设置为公共模板，自动上架并设置价格为0
            if (willBePublic) {
                update.setStatus(1);
                update.setPrice(java.math.BigDecimal.ZERO);
            }
            // 取消公共模板时，保持原有状态（不自动下架），这样用户自己创建的模板即使取消公共也能看到
            
            int result = promptTemplateMapper.updatePromptTemplate(update);
            
            if (result > 0) {
                Long ownerId = promptTemplate.getUserId();
                if (ownerId != null) {
                    if (willBePublic) {
                        templateAuthorIdentityManager.grantAuthorRole(ownerId);
                        incrementReleaseCount(ownerId, 1);
                    } else if (wasPublished) {
                        // 取消公共模板且之前已上架，减少发布计数
                        incrementReleaseCount(ownerId, -1);
                    }
                }
            }
            
            return result > 0 ? ResultBody.success("设置成功") : ResultBody.error(500, "设置失败");
        } catch (Exception e) {
            return ResultBody.error(500, "设置失败：" + e.getMessage());
        }
    }

    /**
     * 查询我创建的评分模板（模板类型：2-评分模板）
     * 注意：此方法仅查询评分模板（type=3），不包含提示词模板
     */
    @Override
    public com.cube.common.core.page.TableDataInfo getMyCreatedTemplates(com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery queryParams) {
        Long userId = SecurityUtils.getUserId();
        if (queryParams.getPageNum() == null) {
            queryParams.setPageNum(1);
        }
        if (queryParams.getPageSize() == null) {
            queryParams.setPageSize(10);
        }
        PageHelper.startPage(queryParams.getPageNum(), queryParams.getPageSize());
        List<PromptTemplate> list = promptTemplateMapper.getMyCreatedTemplates(queryParams, userId);
        PageInfo<PromptTemplate> pageInfo = new PageInfo<>(list);
        com.cube.common.core.page.TableDataInfo dataTable = new com.cube.common.core.page.TableDataInfo();
        dataTable.setCode(200);
        dataTable.setRows(list);
        dataTable.setTotal(pageInfo.getTotal());
        return dataTable;
    }

    /**
     * 查询我购买的评分模板（模板类型：2-评分模板）
     * 注意：此方法仅查询评分模板的购买记录，不包含提示词模板
     */
    @Override
    public com.cube.common.core.page.TableDataInfo getMyPurchasedTemplates(com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery queryParams) {
        Long userId = SecurityUtils.getUserId();
        if (queryParams.getPageNum() == null) {
            queryParams.setPageNum(1);
        }
        if (queryParams.getPageSize() == null) {
            queryParams.setPageSize(10);
        }
        // 模板类型2：评分模板
        Integer templateType = 2;
        List<Map<String, Object>> list = templatePurchaseMapper.selectPurchasedTemplatesByUserId(
            templateType,
            userId, 
            queryParams.getName(),
            (queryParams.getPageNum() - 1) * queryParams.getPageSize(),
            queryParams.getPageSize()
        );
        int total = templatePurchaseMapper.countPurchasedTemplatesByUserId(templateType, userId, queryParams.getName());
        com.cube.common.core.page.TableDataInfo dataTable = new com.cube.common.core.page.TableDataInfo();
        dataTable.setCode(200);
        dataTable.setRows(list);
        dataTable.setTotal(total);
        return dataTable;
    }

    /**
     * 查询市场中的评分模板（已上架的评分模板，模板类型：2-评分模板）
     * 注意：此方法仅查询评分模板（type=3），不包含提示词模板
     */
    @Override
    public com.cube.common.core.page.TableDataInfo getMarketTemplates(com.cube.wechat.selfapp.app.domain.query.ScorePromptQuery queryParams) {
        if (queryParams.getPageNum() == null) {
            queryParams.setPageNum(1);
        }
        if (queryParams.getPageSize() == null) {
            queryParams.setPageSize(12);
        }
        
        java.math.BigDecimal minPrice = queryParams.getMinPrice() != null ? 
            new java.math.BigDecimal(queryParams.getMinPrice()) : null;
        java.math.BigDecimal maxPrice = queryParams.getMaxPrice() != null ? 
            new java.math.BigDecimal(queryParams.getMaxPrice()) : null;
        
        List<Map<String, Object>> list = promptTemplateMapper.getMarketTemplates(
            queryParams,
            minPrice,
            maxPrice,
            (queryParams.getPageNum() - 1) * queryParams.getPageSize(),
            queryParams.getPageSize()
        );
        int total = promptTemplateMapper.countMarketTemplates(queryParams, minPrice, maxPrice);
        
        // 标记已购买的模板（模板类型2：评分模板）
        Long userId = SecurityUtils.getUserId();
        Integer templateType = 2;
        if (userId != null) {
            for (Map<String, Object> template : list) {
                Object idObj = template.get("id");
                if (idObj != null) {
                    String templateId = idObj.toString();
                    com.cube.wechat.selfapp.app.domain.TemplatePurchase purchase = 
                        templatePurchaseMapper.selectByTemplateIdAndUserId(templateType, templateId, userId);
                    template.put("isPurchased", purchase != null);
                } else {
                    template.put("isPurchased", false);
                }
            }
        }
        
        com.cube.common.core.page.TableDataInfo dataTable = new com.cube.common.core.page.TableDataInfo();
        dataTable.setCode(200);
        dataTable.setRows(list);
        dataTable.setTotal(total);
        return dataTable;
    }

    /**
     * 购买评分模板（模板类型：2-评分模板）
     * 注意：此方法仅用于购买评分模板，提示词模板的购买需要使用其他方法
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultBody purchaseTemplate(Long templateId) {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId == null) {
                return ResultBody.error(401, "用户未登录");
            }
            
            // 检查模板是否存在且已上架
            PromptTemplate template = promptTemplateMapper.selectScorePromptById(templateId);
            if (template == null) {
                return ResultBody.error(404, "模板不存在");
            }
            if (template.getStatus() == null || template.getStatus() != 1) {
                return ResultBody.error(400, "模板未上架，无法购买");
            }
            if (template.getIsCommon() != null && template.getIsCommon() == 1) {
                return ResultBody.error(400, "公共模板免费使用，无需购买");
            }
            
            // 检查是否已购买（模板类型2：评分模板）
            Integer templateType = 2;
            com.cube.wechat.selfapp.app.domain.TemplatePurchase existing = 
                templatePurchaseMapper.selectByTemplateIdAndUserId(templateType, templateId.toString(), userId);
            if (existing != null) {
                return ResultBody.error(400, "您已购买过该模板");
            }
            
            // 检查是否是自己的模板
            if (template.getUserId() != null && template.getUserId().equals(userId)) {
                return ResultBody.error(400, "不能购买自己创建的模板");
            }
            
            // 检查积分是否足够
            java.math.BigDecimal price = template.getPrice();
            if (price == null || price.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                return ResultBody.error(400, "模板价格无效");
            }
            
            Integer userPoints = pointsSystem.getUserPoints(userId.toString());
            if (userPoints == null || userPoints < price.intValue()) {
                return ResultBody.error(400, "积分不足，无法购买");
            }
            
            // 扣除购买者积分
            com.cube.point.util.ResultBody deductResult = pointsSystem.setUserPoint(
                userId.toString(),
                "购买模板",
                -price.intValue(),
                null,
                null
            );
            if (deductResult.getCode() != 200) {
                throw new RuntimeException("积分扣除失败：" + deductResult.getMessages()
                );
            }
            
            // 给作者发放积分奖励（作者分成80%，平台抽成20%）
            Long authorId = template.getUserId();
            int authorRewardAmount = (int) (price.intValue() * 0.8); // 作者获得80%分成
            int platformFeeAmount = price.intValue() - authorRewardAmount; // 平台抽成20%
            
            if (authorId != null && authorRewardAmount > 0) {
                com.cube.point.util.ResultBody rewardResult = pointsSystem.setUserPoint(
                    authorId.toString(),
                    "模板销售分成",
                    authorRewardAmount,
                    null,
                    null
                );
                if (rewardResult.getCode() != 200) {
                    // 如果给作者发放积分失败，记录日志但不回滚购买操作（避免影响购买者）
                    System.err.println("给作者发放积分失败，authorId=" + authorId + ", reward=" + authorRewardAmount + ", error=" + rewardResult.getMessages());
                }
            }
            
            // 将平台抽成存入平台账户（userid=0）
            if (platformFeeAmount > 0) {
                com.cube.point.util.ResultBody platformResult = pointsSystem.setUserPoint(
                    "0",
                    "平台抽成",
                    platformFeeAmount,
                    null,
                    null
                );
                if (platformResult.getCode() != 200) {
                    // 如果给平台账户发放积分失败，记录日志但不回滚购买操作
                    System.err.println("给平台账户发放积分失败，platformFee=" + platformFeeAmount + ", error=" + platformResult.getMessages());
                }
            }
            
            // 增加作者收益和销量
            PromptTemplate update = new PromptTemplate();
            update.setId(templateId);
            update.setSalesCount((template.getSalesCount() == null ? 0 : template.getSalesCount()) + 1);
            if (template.getIncomeTotal() == null) {
                update.setIncomeTotal(price);
            } else {
                update.setIncomeTotal(template.getIncomeTotal().add(price));
            }
            promptTemplateMapper.updatePromptTemplate(update);
            
            // 记录购买记录（模板类型2：评分模板）
            // authorRewardAmount 和 platformFeeAmount 已在上面计算
            
            com.cube.wechat.selfapp.app.domain.TemplatePurchase purchase = 
                new com.cube.wechat.selfapp.app.domain.TemplatePurchase();
            purchase.setTemplateType(templateType);
            purchase.setTemplateId(templateId.toString());
            purchase.setTemplateName(template.getName());
            purchase.setUserId(userId);
            purchase.setAuthorId(authorId);
            purchase.setPurchasePrice(price);
            purchase.setAuthorIncome(new java.math.BigDecimal(authorRewardAmount));
            purchase.setPlatformFee(new java.math.BigDecimal(platformFeeAmount));
            purchase.setStatus(1);
            purchase.setRemark("购买评分模板");
            templatePurchaseMapper.insertTemplatePurchase(purchase);
            
            return ResultBody.success("购买成功");
        } catch (Exception e) {
            return ResultBody.error(500, "购买失败：" + e.getMessage());
        }
    }

    /**
     * 查询我创建的提示词模板（模板类型：1-提示词模板）
     */
    @Override
    public com.cube.common.core.page.TableDataInfo getMyCreatedCallWords(com.cube.wechat.selfapp.app.domain.query.CallWordQuery queryParams) {
        Long userId = SecurityUtils.getUserId();
        if (queryParams.getPageNum() == null) {
            queryParams.setPageNum(1);
        }
        if (queryParams.getPageSize() == null) {
            queryParams.setPageSize(10);
        }
        List<com.cube.wechat.selfapp.app.domain.CallWord> list = callWordMapper.getMyCreatedCallWords(
            userId,
            queryParams.getPlatformId(),
            (queryParams.getPageNum() - 1) * queryParams.getPageSize(),
            queryParams.getPageSize()
        );
        int total = callWordMapper.countMyCreatedCallWords(userId, queryParams.getPlatformId());
        com.cube.common.core.page.TableDataInfo dataTable = new com.cube.common.core.page.TableDataInfo();
        dataTable.setCode(200);
        dataTable.setRows(list);
        dataTable.setTotal(total);
        return dataTable;
    }

    /**
     * 查询我购买的提示词模板（模板类型：1-提示词模板）
     */
    @Override
    public com.cube.common.core.page.TableDataInfo getMyPurchasedCallWords(com.cube.wechat.selfapp.app.domain.query.CallWordQuery queryParams) {
        Long userId = SecurityUtils.getUserId();
        if (queryParams.getPageNum() == null) {
            queryParams.setPageNum(1);
        }
        if (queryParams.getPageSize() == null) {
            queryParams.setPageSize(10);
        }
        // 模板类型1：提示词模板
        Integer templateType = 1;
        List<Map<String, Object>> list = templatePurchaseMapper.selectPurchasedTemplatesByUserId(
            templateType,
            userId,
            queryParams.getPlatformId(),
            (queryParams.getPageNum() - 1) * queryParams.getPageSize(),
            queryParams.getPageSize()
        );
        int total = templatePurchaseMapper.countPurchasedTemplatesByUserId(templateType, userId, queryParams.getPlatformId());
        com.cube.common.core.page.TableDataInfo dataTable = new com.cube.common.core.page.TableDataInfo();
        dataTable.setCode(200);
        dataTable.setRows(list);
        dataTable.setTotal(total);
        return dataTable;
    }

    /**
     * 查询市场中的提示词模板（已上架的提示词模板，模板类型：1-提示词模板）
     */
    @Override
    public com.cube.common.core.page.TableDataInfo getMarketCallWords(com.cube.wechat.selfapp.app.domain.query.CallWordQuery queryParams) {
        if (queryParams.getPageNum() == null) {
            queryParams.setPageNum(1);
        }
        if (queryParams.getPageSize() == null) {
            queryParams.setPageSize(12);
        }
        
        List<Map<String, Object>> list = callWordMapper.getMarketCallWords(
            queryParams.getPlatformId(),
            queryParams.getMinPrice(),
            queryParams.getMaxPrice(),
            (queryParams.getPageNum() - 1) * queryParams.getPageSize(),
            queryParams.getPageSize()
        );
        int total = callWordMapper.countMarketCallWords(
            queryParams.getPlatformId(),
            queryParams.getMinPrice(),
            queryParams.getMaxPrice()
        );
        
        // 标记已购买的模板（模板类型1：提示词模板）
        Long userId = SecurityUtils.getUserId();
        Integer templateType = 1;
        if (userId != null) {
            for (Map<String, Object> template : list) {
                Object idObj = template.get("id");
                if (idObj != null) {
                    String templateId = idObj.toString();
                    com.cube.wechat.selfapp.app.domain.TemplatePurchase purchase = 
                        templatePurchaseMapper.selectByTemplateIdAndUserId(templateType, templateId, userId);
                    template.put("isPurchased", purchase != null);
                } else {
                    template.put("isPurchased", false);
                }
            }
        }
        
        com.cube.common.core.page.TableDataInfo dataTable = new com.cube.common.core.page.TableDataInfo();
        dataTable.setCode(200);
        dataTable.setRows(list);
        dataTable.setTotal(total);
        return dataTable;
    }

    /**
     * 购买提示词模板（模板类型：1-提示词模板）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultBody purchaseCallWord(String platformId) {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId == null) {
                return ResultBody.error(401, "用户未登录");
            }
            
            // 检查模板是否存在且已上架
            com.cube.wechat.selfapp.app.domain.CallWord callWord = callWordMapper.getCallWordById(platformId);
            if (callWord == null) {
                return ResultBody.error(404, "模板不存在");
            }
            if (callWord.getStatus() == null || callWord.getStatus() != 1) {
                return ResultBody.error(400, "模板未上架，无法购买");
            }
            if (callWord.getIsCommon() != null && callWord.getIsCommon() == 1) {
                return ResultBody.error(400, "公共模板免费使用，无需购买");
            }
            
            // 检查是否已购买（模板类型1：提示词模板）
            Integer templateType = 1;
            com.cube.wechat.selfapp.app.domain.TemplatePurchase existing = 
                templatePurchaseMapper.selectByTemplateIdAndUserId(templateType, platformId, userId);
            if (existing != null) {
                return ResultBody.error(400, "您已购买过该模板");
            }
            
            // 检查是否是自己的模板
            if (callWord.getUserId() != null && callWord.getUserId().equals(userId)) {
                return ResultBody.error(400, "不能购买自己创建的模板");
            }
            
            // 检查积分是否足够
            java.math.BigDecimal price = callWord.getPrice();
            if (price == null || price.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                return ResultBody.error(400, "模板价格无效");
            }
            
            Integer userPoints = pointsSystem.getUserPoints(userId.toString());
            if (userPoints == null || userPoints < price.intValue()) {
                return ResultBody.error(400, "积分不足，无法购买");
            }
            
            // 扣除购买者积分
            com.cube.point.util.ResultBody deductResult = pointsSystem.setUserPoint(
                userId.toString(),
                "购买提示词模板",
                -price.intValue(),
                null,
                null
            );
            if (deductResult.getCode() != 200) {
                throw new RuntimeException("积分扣除失败：" + deductResult.getMessages());
            }
            
            // 给作者发放积分奖励（作者分成80%，平台抽成20%）
            Long authorId = callWord.getUserId();
            int authorRewardAmount = (int) (price.intValue() * 0.8); // 作者获得80%分成
            int platformFeeAmount = price.intValue() - authorRewardAmount; // 平台抽成20%
            
            if (authorId != null && authorRewardAmount > 0) {
                com.cube.point.util.ResultBody rewardResult = pointsSystem.setUserPoint(
                    authorId.toString(),
                    "模板销售分成",
                    authorRewardAmount,
                    null,
                    null
                );
                if (rewardResult.getCode() != 200) {
                    // 如果给作者发放积分失败，记录日志但不回滚购买操作（避免影响购买者）
                    System.err.println("给作者发放积分失败，authorId=" + authorId + ", reward=" + authorRewardAmount + ", error=" + rewardResult.getMessages());
                }
            }
            
            // 将平台抽成存入平台账户（userid=0）
            if (platformFeeAmount > 0) {
                com.cube.point.util.ResultBody platformResult = pointsSystem.setUserPoint(
                    "0",
                    "平台抽成",
                    platformFeeAmount,
                    null,
                    null
                );
                if (platformResult.getCode() != 200) {
                    // 如果给平台账户发放积分失败，记录日志但不回滚购买操作
                    System.err.println("给平台账户发放积分失败，platformFee=" + platformFeeAmount + ", error=" + platformResult.getMessages());
                }
            }
            
            // 增加作者收益和销量
            com.cube.wechat.selfapp.app.domain.CallWord update = new com.cube.wechat.selfapp.app.domain.CallWord();
            update.setPlatformId(platformId);
            update.setSalesCount((callWord.getSalesCount() == null ? 0 : callWord.getSalesCount()) + 1);
            if (callWord.getIncomeTotal() == null) {
                update.setIncomeTotal(price);
            } else {
                update.setIncomeTotal(callWord.getIncomeTotal().add(price));
            }
            callWordMapper.updateCallWord(update);
            
            // 记录购买记录（模板类型1：提示词模板）
            // authorRewardAmount 和 platformFeeAmount 已在上面计算
            
            com.cube.wechat.selfapp.app.domain.TemplatePurchase purchase = 
                new com.cube.wechat.selfapp.app.domain.TemplatePurchase();
            purchase.setTemplateType(templateType);
            purchase.setTemplateId(platformId);
            purchase.setTemplateName(platformId);
            purchase.setUserId(userId);
            purchase.setAuthorId(authorId);
            purchase.setPurchasePrice(price);
            purchase.setAuthorIncome(new java.math.BigDecimal(authorRewardAmount));
            purchase.setPlatformFee(new java.math.BigDecimal(platformFeeAmount));
            purchase.setStatus(1);
            purchase.setRemark("购买提示词模板");
            templatePurchaseMapper.insertTemplatePurchase(purchase);
            
            return ResultBody.success("购买成功");
        } catch (Exception e) {
            return ResultBody.error(500, "购买失败：" + e.getMessage());
        }
    }

    @Override
    public ResultBody getMyPoints() {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId == null) {
                return ResultBody.error(401, "用户未登录");
            }
            Integer points = pointsSystem.getUserPoints(userId.toString());
            return ResultBody.success(points != null ? points : 0);
        } catch (Exception e) {
            return ResultBody.error(500, "获取积分失败：" + e.getMessage());
        }
    }

    @Override
    public ResultBody getMyPointsSummary() {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId == null) {
                return ResultBody.error(401, "用户未登录");
            }
            Map<String, Object> summary = pointsSystem.buildUserPointsSummary(userId.toString());
            return ResultBody.success(summary);
        } catch (Exception e) {
            return ResultBody.error(500, "获取积分概览失败：" + e.getMessage());
        }
    }

    /**
     * 增加/减少作者发布模板数量
     */
    private void incrementReleaseCount(Long userId, int delta) {
        if (userId == null || delta == 0) {
            return;
        }
        int affected = templateAuthorMapper.increaseReleaseTemplateCount(userId, delta);
        if (affected == 0) {
            TemplateAuthor author = templateAuthorMapper.selectByUserId(userId);
            if (author == null) {
                author = new TemplateAuthor();
                author.setUserId(userId);
                author.setIncomeTotal(java.math.BigDecimal.ZERO);
                author.setReleaseTemplateCount(Math.max(0, delta));
                author.setLevel(1);
                templateAuthorMapper.insertTemplateAuthor(author);
            }
        }
    }

    /**
     * 清洗HTML内容，确保符合微信草稿箱API要求
     * 1. 移除Markdown代码块标记
     * 2. 移除不支持的HTML标签
     * 3. 移除HTML属性（class、id、style等）
     * 4. 确保段落分明
     */
    private String sanitizeHtmlContent(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        
        // 1. 移除Markdown代码块标记（```）及其内容，转换为普通段落
        content = content.replaceAll("```[\\s\\S]*?```", "");
        content = content.replaceAll("`([^`]+)`", "$1");
        
        // 2. 移除<pre>和<code>标签，保留内容
        content = content.replaceAll("<pre[^>]*>", "");
        content = content.replaceAll("</pre>", "");
        content = content.replaceAll("<code[^>]*>", "");
        content = content.replaceAll("</code>", "");
        
        // 3. 移除不支持的标签：div、span等容器标签
        content = content.replaceAll("<div[^>]*>", "");
        content = content.replaceAll("</div>", "");
        content = content.replaceAll("<span[^>]*>", "");
        content = content.replaceAll("</span>", "");
        
        // 4. 移除所有HTML标签的属性（保留标签本身）
        // 匹配 <标签名 属性="值"> 并替换为 <标签名>
        content = content.replaceAll("<(p|h[1-6]|ul|ol|li|strong|em|b|i|br|img)\\s+[^>]*>", "<$1>");
        
        // 5. 特殊处理img标签，保留src、data-ratio、data-w属性
        content = content.replaceAll("<img\\s+[^>]*src=\"([^\"]+)\"[^>]*>", 
            "<img src=\"$1\" data-ratio=\"0.75\" data-w=\"800\">");
        
        // 6. 移除Markdown语法残留
        content = content.replaceAll("^#{1,6}\\s+", ""); // 移除标题标记
        content = content.replaceAll("\\*\\*([^*]+)\\*\\*", "<strong>$1</strong>"); // 转换加粗
        content = content.replaceAll("\\*([^*]+)\\*", "<em>$1</em>"); // 转换斜体
        
        // 7. 清理多余的空白和换行
        content = content.replaceAll("\\r\\n\\r\\n\\r\\n+", "\r\n\r\n"); // 移除多余的空行
        content = content.replaceAll("\\n\\n\\n+", "\n\n");
        
        // 8. 确保段落标签正确闭合
        content = content.replaceAll("<p>\\s*</p>", ""); // 移除空段落
        
        // 9. 移除开头和结尾的空白
        content = content.trim();
        
        return content;
    }

    @Override
    public ResultBody pushAutoOneOffice(Map map) {
        WcOfficeAccount woa = userInfoMapper.getOfficeAccountByUserName(map.get("userId") + "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(new Date()); // 格式化日期

        String assessToken = weChatApiUtils.getOfficeAccessToken(woa.getAppId(), woa.getAppSecret());
        if (assessToken == null) {
            return ResultBody.FAIL;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/draft/add?access_token=" + assessToken;
        String detailUrl = "https://api.weixin.qq.com/cgi-bin/draft/get?access_token=" + assessToken;
        String contentText = map.get("contentText").toString();

        // 提取标题和内容
        String title = "标题待定";
        int first = contentText.indexOf("《");
        int second = contentText.indexOf("》", first + 1);
        if (first >= 0 && second > first) {
            title = contentText.substring(first + 1, second);
            // 移除标题行，保留从《》后面开始的内容
            contentText = contentText.substring(second + 1).trim();
            // 移除开头的换行
            while (contentText.startsWith("\r\n") || contentText.startsWith("\n")) {
                contentText = contentText.replaceFirst("^[\r\n]+", "");
            }
        }
        
        // 清洗HTML内容，确保符合微信草稿箱API要求
        contentText = sanitizeHtmlContent(contentText);
        
        // 清理多余的换行
        contentText = contentText.replaceAll("\r\n\r\n", "");
        
        // 添加原文链接
        if (map.get("shareUrl") != null && !map.get("shareUrl").equals("")) {
            String shareUrl = "<p>原文链接：" + map.get("shareUrl") + "</p>";
            contentText = shareUrl + contentText;
        }
        List<JSONObject> paramlist = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);

        jsonObject.put("author", woa.getOfficeAccountName());
        jsonObject.put("content", contentText);
        jsonObject.put("thumb_media_id", woa.getMediaId());
        jsonObject.put("content_source_url", map.get("shareUrl"));
        paramlist.add(jsonObject);
        JSONObject param = new JSONObject();
        param.put("articles", paramlist);
        try {
            JSONObject result = RestUtils.post(url, param);
            String mediaId = result.get("media_id").toString();
            JSONObject postJson = new JSONObject();
            postJson.put("media_id", mediaId);
            JSONObject detail = RestUtils.post(detailUrl, postJson);
            Object o = detail.get("news_item");
            List<Map<String, Object>> newsItemArray = (List<Map<String, Object>>) o;
            if (newsItemArray == null || newsItemArray.isEmpty()) {
                throw new RuntimeException("news_item 数组为空");
            }
            Map<String, Object> newsItem = newsItemArray.get(0);
            String tempUrl = (String) newsItem.get("url");
            System.out.println(tempUrl);
            return ResultBody.success(tempUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultBody.error(300, "推送失败");
    }

    @Override
    public ResultBody pushViewAutoOffice(String taskId) {


        List<Map> list = userInfoMapper.getPushViewOfficeData(taskId);
        if (list.size() == 0) {
            return ResultBody.error(300, "暂无素材可被收录");
        }

//        使用 Map 聚合数据
        Map<String, List<String>> groupedData = new LinkedHashMap<>();
        for (Map map : list) {
            String linkUrl = map.get("answer") + "";
            String keyWord = map.get("prompt") + "";
            String author = map.get("author") + "";
            String summary = map.get("summary").toString();
            String liTitle = "<a href ='" + linkUrl + "' style='color:#576b95' >" + author + "：" + map.get("title") + "</a>";
            summary = "<p style='font-family: 'Arial''>" + summary;
            summary = summary + "</p>";
            String content = liTitle + "<br><br>" + summary + "<br><br>";
            groupedData.computeIfAbsent(keyWord, k -> new ArrayList<>()).add(content);
        }
        // 输出结果
        String res = "";
        for (Map.Entry<String, List<String>> entry : groupedData.entrySet()) {
            String keyWord = "<p style='font-weight: normal;color:red;'>" + entry.getKey() + "</p>";
            String content = String.join("", entry.getValue());
            res = res + keyWord + "<br>" + content;
        }
        System.out.println(res);
        return ResultBody.success(res);
    }

    private void downloadFile(String fileUrl, Path targetPath) throws IOException {
//        fileUrl = "https://u3w.com/chatfile/logo.jpg";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String encodedUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/") + 1) + encodedFileName;
        URL url = new URL(encodedUrl);
        try (InputStream in = url.openStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private String uploadImageToWeChat(Path file, WcOfficeAccount woa) throws IOException, InterruptedException {
        String accessToken = weChatApiUtils.getOfficeAccessToken(woa.getAppId(), woa.getAppSecret());
        //String accessToken = "87_EubpBd6h4bK2WJZ7ylMiGyBKgI0I-yAVIDmbmhY8-5fSPiEyLj35ki7hPtLchmJEqFyQ_zUhHqkQFZwipddCAsr4gRP-MOUHWhaoRaMTUrTNFoE66Gs71iT-ZRMFEQbAAAIHT";
        String uploadUrl = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + accessToken + "&type=image";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "curl", uploadUrl, "-F", "media=@" + file.toAbsolutePath()
        );

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            JSONObject jsonResponse = JSONObject.parseObject(response.toString());
            return jsonResponse.getString("media_id");
        } else {
            throw new IOException("上传失败: " + response.toString());
        }
    }

    @Override
    public ResultBody saveOfficeAccount(WcOfficeAccount wcOfficeAccount) {
        // 验证输入字段的长度和格式
        if (!isValidInput(wcOfficeAccount.getAppId(), wcOfficeAccount.getAppSecret(), wcOfficeAccount.getOfficeAccountName())) {
            return ResultBody.error(201, "绑定失败：参数不能为空");
        }
        try {
            if (wcOfficeAccount.getPicUrl() != null) {
                // 1. 下载图片到本地临时文件
                Path tempFile = Files.createTempFile("temp", ".jpg");
                String picUrl = wcOfficeAccount.getPicUrl();
                if (picUrl == null) {
                    throw new RuntimeException("请先上传图片");
                }
                downloadFile(picUrl, tempFile);

                // 2. 上传图片到微信服务器
                String mediaId = uploadImageToWeChat(tempFile, wcOfficeAccount);
                wcOfficeAccount.setMediaId(mediaId);
                // 3. 删除临时文件
                Files.deleteIfExists(tempFile);
            }
        } catch (Exception e) {
            return ResultBody.error(500, e.getMessage());
        }

        try {
            // 先查询用户是否绑定公众号
            WcOfficeAccount woa = userInfoMapper.getOfficeAccountByUserId(wcOfficeAccount.getUserId());
            if (woa != null) {
                // 修改绑定的公众号
                wcOfficeAccount.setId(woa.getId());
                userInfoMapper.updateOfficeAccount(wcOfficeAccount);
            } else {
                // 添加操作
                userInfoMapper.saveOfficeAccount(wcOfficeAccount);
            }
            return ResultBody.success("绑定成功！");
        } catch (Exception e) {
            return ResultBody.error(500, "系统内部错误");
        }
    }

    private boolean isValidInput(String appId, String appSecret, String officeAccountName) {
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret) || StringUtils.isEmpty(officeAccountName)) {
            return false;
        }
        // 验证
        return true;
    }


    @Override
    public ResultBody getAgentBind(Long userId) {
        Map woc = userInfoMapper.getAgentTokenByUserId(userId + "");
        return ResultBody.success(woc);
    }

    @Override
    public ResultBody getSpaceInfoByUserId(Long userId) {
        Map woc = userInfoMapper.getSpaceInfoByUserId(userId + "");
        return ResultBody.success(woc);
    }

    @Override
    public ResultBody getJsPromptByName(String templateName) {
        Map woc = userInfoMapper.getJsPromptByName(templateName);
        return ResultBody.success(woc);
    }

    @Override
    public ResultBody saveAgentBind(Map map) {
        userInfoMapper.saveAgentBind(map);
        return ResultBody.success("成功");
    }

    @Override
    public ResultBody saveUserFlowId(Map map) {
        userInfoMapper.saveUserFlowId(map);
        return ResultBody.success("成功");
    }

    @Override
    public ResultBody saveSpaceBind(Map map) {
        userInfoMapper.saveSpaceBind(map);
        return ResultBody.success("成功");
    }

    @Override
    public ResultBody saveChromeTaskData(String taskId, String userid, String corpId) {
        List<Map> list = new ArrayList<>();

        Map oneMap = new HashMap();
        oneMap.put("taskId", taskId);
        oneMap.put("taskName", "主题生成");
        oneMap.put("status", "running");
        oneMap.put("planTime", "40秒");
        oneMap.put("userid", userid);
        oneMap.put("corpId", corpId);
        list.add(oneMap);

        Map twoMap = new HashMap();
        twoMap.put("taskId", taskId);
        twoMap.put("taskName", "素材搜集");
        twoMap.put("status", "waiting");
        twoMap.put("planTime", "3分钟");
        twoMap.put("userid", userid);
        twoMap.put("corpId", corpId);
        list.add(twoMap);

        Map threeMap = new HashMap();
        threeMap.put("taskId", taskId);
        threeMap.put("taskName", "内容生成");
        threeMap.put("status", "waiting");
        threeMap.put("planTime", "5分钟");
        threeMap.put("userid", userid);
        threeMap.put("corpId", corpId);
        list.add(threeMap);

        Map fourMap = new HashMap();
        fourMap.put("taskId", taskId);
        fourMap.put("taskName", "发布预览");
        fourMap.put("status", "waiting");
        fourMap.put("planTime", "1分钟");
        fourMap.put("userid", userid);
        fourMap.put("corpId", corpId);
        list.add(fourMap);


        userInfoMapper.saveChromeTaskData(list);
        return ResultBody.success("成功");
    }

    @Override
    public ResultBody getTaskStatus(String taskId) {
        List<Map> list = userInfoMapper.getTaskStatus(taskId);
        return ResultBody.success(list);
    }

    @Override
    public ResultBody getUserPromptTem(String userId, String agentId) {
        String promptTem = userInfoMapper.getUserPromptTem(userId, agentId);
        return ResultBody.success(promptTem);
    }

    @Override
    public ResultBody getPromptTem(Integer type, String userId) {
        if (type == 1) {
            List<Map> list = userInfoMapper.getPromptTem();
            return ResultBody.success(list);
        } else {
            String prompt = userInfoMapper.getTaskPromptById("desc", userId);
            return ResultBody.success(prompt);
        }
    }

    @Override
    public ResultBody updateUserPromptTem(Map map) {

        Integer points = pointsSystem.getUserPoints(map.get("userId") + "");
        if (points < 1) {
            return ResultBody.error(201, "积分余额不足，请明日再来或者联系客服充值");
        }

        com.cube.point.util.ResultBody pointResult;
        if (map.get("agentId").equals("desc")) {
            pointResult = pointsSystem.setUserPoint(map.get("userId") + "", "记忆修改", null, "0x2edc4228a84d672affe8a594033cb84a029bcafc", "f34f737203aa370f53ef0e041c1bff36bf59db8eb662cdb447f01d9634374dd");
        } else {
            pointResult = pointsSystem.setUserPoint(map.get("userId") + "", "模板配置", null, "0x2edc4228a84d672affe8a594033cb84a029bcafc", "f34f737203aa370f53ef0e041c1bff36bf59db8eb662cdb447f01d9634374dd");
        }
        if (pointResult == null || pointResult.getCode() != 200) {
            return ResultBody.error(500, "积分扣减失败: " + (pointResult != null ? pointResult.getMessages() : "未知错误"));
        }

        if (map.get("isAllSel").equals(true)) {
            //全选状态，直接全删全增
            userInfoMapper.delTaskPromptByUserId(map.get("userId") + "");
            userInfoMapper.saveAllTaskPromptByUserId(map.get("promptTemplate") + "", map.get("userId") + "");
        } else {
            String prompt = userInfoMapper.getTaskPromptById(map.get("agentId") + "", map.get("userId") + "");
            if (prompt != null) {
                userInfoMapper.updateTaskPromptByUserId(map.get("agentId") + "", map.get("promptTemplate") + "", map.get("userId") + "");
            } else {
                userInfoMapper.saveTaskPromptByUserId(map.get("agentId") + "", map.get("promptTemplate") + "", map.get("userId") + "");
            }
        }


        return ResultBody.success("更新成功");
    }

    @Override
    public ResultBody getIsChangeByCorpId(String corpId) {
// 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 获取10分钟前的时间
        LocalDateTime tenMinutesBefore = now.minusMinutes(10);

        // 定义日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化时间
        String currentTime = now.format(formatter);
        String timeTenMinutesBefore = tenMinutesBefore.format(formatter);

        int num = userInfoMapper.getIsChangeByCorpId(corpId, currentTime, timeTenMinutesBefore);

        return ResultBody.success(num);
    }


}
