package com.cube.wechat.selfapp.app.service.impl;

import com.cube.common.core.domain.AjaxResult;
import com.cube.common.utils.SecurityUtils;
import com.cube.point.controller.PointsSystem;
import com.cube.wechat.selfapp.app.domain.CallWord;
import com.cube.wechat.selfapp.app.domain.query.CallWordQuery;
import com.cube.wechat.selfapp.app.mapper.CallWordMapper;
import com.cube.wechat.selfapp.app.mapper.TemplateAuthorMapper;
import com.cube.wechat.selfapp.app.domain.TemplateAuthor;
import com.cube.wechat.selfapp.app.service.CallWordService;
import com.cube.wechat.selfapp.app.service.helper.TemplateAuthorIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 提示词服务实现类
 * 
 * @author fuchen
 * @version 1.0
 * @date 2025-01-14
 */
@Service
public class CallWordServiceImpl implements CallWordService {

    @Autowired
    private CallWordMapper callWordMapper;

    @Autowired
    private TemplateAuthorIdentityManager templateAuthorIdentityManager;

    @Autowired
    private TemplateAuthorMapper templateAuthorMapper;

    @Autowired
    private PointsSystem pointsSystem;

    @Override
    public CallWord getCallWord(String platformId) {
        try {
            CallWord callWord = callWordMapper.getCallWordById(platformId);
            if (callWord == null) {
                callWord = new CallWord();
                callWord.setPlatformId(platformId);
                callWord.setWordContent(getBackupPrompt(platformId));
                callWord.setUpdateTime(LocalDateTime.now());
                callWord.setStatus(0);
                callWord.setPrice(BigDecimal.ZERO);
                callWord.setSalesCount(0);
                callWord.setIncomeTotal(BigDecimal.ZERO);
            }
            return callWord;
        } catch (Exception e) {
            CallWord callWord = new CallWord();
            callWord.setPlatformId(platformId);
            callWord.setWordContent(getBackupPrompt(platformId));
            callWord.setUpdateTime(LocalDateTime.now());
            callWord.setStatus(0);
            callWord.setPrice(BigDecimal.ZERO);
            callWord.setSalesCount(0);
            callWord.setIncomeTotal(BigDecimal.ZERO);
            return callWord;
        }
    }

    @Override
    public AjaxResult saveOrUpdateCallWord(CallWord callWord) {
        try {
            CallWord existing = callWordMapper.getCallWordById(callWord.getPlatformId());
            
            // 如果是更新操作且是公共模板，检查权限
            if (existing != null && existing.getIsCommon() != null && existing.getIsCommon() == 1) {
                if (!com.cube.common.utils.SecurityUtils.hasRole("admin")) {
                    return AjaxResult.error("只有管理员可以修改公共模板");
                }
            }
            
            if (existing != null) {
                callWordMapper.updateCallWord(callWord);
            } else {
                if (callWord.getUserId() == null) {
                    try {
                        callWord.setUserId(SecurityUtils.getUserId());
                    } catch (Exception ignore) {
                    }
                }
                if (callWord.getIsCommon() == null) {
                    callWord.setIsCommon(0);
                }
                if (callWord.getStatus() == null) {
                    callWord.setStatus(0);
                }
                if (callWord.getPrice() == null) {
                    callWord.setPrice(BigDecimal.ZERO);
                }
                if (callWord.getSalesCount() == null) {
                    callWord.setSalesCount(0);
                }
                if (callWord.getIncomeTotal() == null) {
                    callWord.setIncomeTotal(BigDecimal.ZERO);
                }
                callWordMapper.insertCallWord(callWord);
            }
            
            return AjaxResult.success("保存成功");
        } catch (Exception e) {
            return AjaxResult.error("保存失败：" + e.getMessage());
        }
    }

    @Override
    public AjaxResult updateDraftZhihuStatus(Long draftId, boolean isPushed) {
        try {
            callWordMapper.updateDraftZhihuStatus(draftId, isPushed ? 1 : 0);
            return AjaxResult.success("状态更新成功");
        } catch (Exception e) {
            return AjaxResult.error("状态更新失败：" + e.getMessage());
        }
    }

    @Override
    public String generateZhihuTitle(String aiName, String userId, int num) {
        try {
            String template = getCallWord("zhihu_title").getWordContent();
            if (template == null || template.trim().isEmpty()) {
                template = "{aiName}创作-{date}-{num}";
            }
            
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return template.replace("{aiName}", aiName)
                          .replace("{userId}", userId)
                          .replace("{date}", date)
                          .replace("{num}", String.format("%03d", num));
        } catch (Exception e) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return String.format("%s创作-%s-%03d", aiName, date, num);
        }
    }

    @Override
    public AjaxResult deleteCallWord(String[] platformIds) {
        try {
            // 检查是否包含公共模板，如果是只有管理员可以删除
            for (String platformId : platformIds) {
                CallWord callWord = callWordMapper.getCallWordById(platformId);
                if (callWord != null && callWord.getIsCommon() != null && callWord.getIsCommon() == 1) {
                    if (!com.cube.common.utils.SecurityUtils.hasRole("admin")) {
                        return AjaxResult.error("只有管理员可以删除公共模板");
                    }
                }
            }
            
            int count = callWordMapper.deleteCallWordByPlatformIds(platformIds);
            return count > 0 ? AjaxResult.success("删除成功") : AjaxResult.success("删除内容不存在");
        } catch (Exception e) {
            return AjaxResult.error("删除失败：" + e.getMessage());
        }
    }

    @Override
    public List<CallWord> getCallWordList(CallWordQuery callWordQuery) {
        return callWordMapper.getCallWordList(callWordQuery);
    }

    @Override
    public AjaxResult setCallWordCommon(String platformId, Integer isCommon) {
        try {
            CallWord callWord = callWordMapper.selectByPlatformId(platformId);
            if (callWord == null) {
                return AjaxResult.error("提示词不存在");
            }
            boolean wasPublished = callWord.getStatus() != null && callWord.getStatus() == 1;
            boolean willBePublic = isCommon != null && isCommon == 1;
            if (willBePublic && wasPublished) {
                forceUnpublish(callWord);
                wasPublished = false;
            }
            CallWord update = new CallWord();
            update.setPlatformId(platformId);
            update.setIsCommon(isCommon);
            if (willBePublic) {
                update.setStatus(1);
                update.setPrice(BigDecimal.ZERO);
            } else {
                update.setStatus(0);
            }
            int rows = callWordMapper.updateCallWord(update);
            if (rows > 0) {
                Long ownerId = callWord.getUserId();
                if (willBePublic) {
                    templateAuthorIdentityManager.grantAuthorRole(ownerId);
                    incrementReleaseCount(ownerId, 1);
                } else if (wasPublished) {
                    incrementReleaseCount(ownerId, -1);
                }
            }
            return rows > 0 ? AjaxResult.success("设置成功") : AjaxResult.error("设置失败");
        } catch (Exception e) {
            return AjaxResult.error("设置失败：" + e.getMessage());
        }
    }

    @Override
    public AjaxResult publishCallWord(String platformId, BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.error("请输入大于0的价格");
        }
        CallWord callWord = callWordMapper.getCallWordById(platformId);
        if (callWord == null) {
            return AjaxResult.error("模板不存在");
        }
        if (callWord.getIsCommon() != null && callWord.getIsCommon() == 1) {
            return AjaxResult.error("公共模板默认已上架，无需重复操作");
        }
        Long operatorId = null;
        try {
            operatorId = SecurityUtils.getUserId();
        } catch (Exception ignore) {
        }
        Long ownerId = callWord.getUserId();
        boolean isOwner = ownerId != null && ownerId.equals(operatorId);
        boolean isAdmin = false;
        try {
            isAdmin = SecurityUtils.hasRole("admin");
        } catch (Exception ignore) {
        }
        if (!isOwner && !isAdmin) {
            return AjaxResult.error("只能上架自己的模板");
        }
        CallWord update = new CallWord();
        update.setPlatformId(platformId);
        update.setPrice(price);
        update.setStatus(1);
        callWordMapper.updateCallWord(update);
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
        
        return AjaxResult.success("上架成功");
    }

    @Override
    public AjaxResult unpublishCallWord(String platformId) {
        CallWord callWord = callWordMapper.getCallWordById(platformId);
        if (callWord == null) {
            return AjaxResult.error("模板不存在");
        }
        if (callWord.getIsCommon() != null && callWord.getIsCommon() == 1) {
            return AjaxResult.error("公共模板无需下架");
        }
        Long operatorId = null;
        try {
            operatorId = SecurityUtils.getUserId();
        } catch (Exception ignore) {
        }
        Long ownerId = callWord.getUserId();
        boolean isOwner = ownerId != null && ownerId.equals(operatorId);
        boolean isAdmin = false;
        try {
            isAdmin = SecurityUtils.hasRole("admin");
        } catch (Exception ignore) {
        }
        if (!isOwner && !isAdmin) {
            return AjaxResult.error("只能操作自己的模板");
        }
        CallWord update = new CallWord();
        update.setPlatformId(platformId);
        update.setStatus(0);
        callWordMapper.updateCallWord(update);
        Long targetUser = ownerId != null ? ownerId : operatorId;
        templateAuthorIdentityManager.grantAuthorRole(targetUser);
        incrementReleaseCount(targetUser, -1);
        return AjaxResult.success("模板已下架");
    }

    /**
     * 获取备用提示词（当数据库查询失败时使用）
     */
    private String getBackupPrompt(String platformId) {
        switch (platformId) {
            case "wechat_layout":
                return "请将以下内容整理为适合微信公众号发布的HTML格式文章。要求：\n" +
                       "1. 使用适当的HTML标签进行格式化\n" +
                       "2. 重要信息使用<strong>加粗</strong>标记\n" +
                       "3. 代码块使用<pre><code>标记\n" +
                       "4. 列表使用<ul><li>或<ol><li>格式\n" +
                       "5. 段落使用<p>标记，确保良好的可读性\n" +
                       "6. 目标是用于微信公众号\"草稿箱接口\"的 content 字段\n" +
                       "7. 删除不必要的格式标记，保持内容简洁\n\n" +
                       "请对以下内容进行排版：";
            
            case "zhihu_layout":
                return "请将以下内容整理为适合知乎发布的Markdown格式文章。要求：\n" +
                       "1. 保持内容的专业性和可读性\n" +
                       "2. 使用合适的标题层级（## ### #### 等）\n" +
                       "3. 代码块使用```标记，并指定语言类型\n" +
                       "4. 重要信息使用**加粗**标记\n" +
                       "5. 列表使用- 或1. 格式\n" +
                       "6. 删除不必要的格式标记\n" +
                       "7. 确保内容适合知乎的阅读习惯\n" +
                       "8. 文章结构清晰，逻辑连贯\n" +
                       "9. 目标是作为一篇专业文章投递到知乎草稿箱\n\n" +
                       "请对以下内容进行排版：";
            
            default:
                return "请对以下内容进行排版：";
        }
    }

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
                author.setIncomeTotal(BigDecimal.ZERO);
                author.setReleaseTemplateCount(Math.max(0, delta));
                author.setLevel(1);
                templateAuthorMapper.insertTemplateAuthor(author);
            }
        }
    }

    private void forceUnpublish(CallWord callWord) {
        if (callWord == null || callWord.getStatus() == null || callWord.getStatus() == 0) {
            return;
        }
        CallWord update = new CallWord();
        update.setPlatformId(callWord.getPlatformId());
        update.setStatus(0);
        callWordMapper.updateCallWord(update);
        incrementReleaseCount(callWord.getUserId(), -1);
        callWord.setStatus(0);
    }
} 