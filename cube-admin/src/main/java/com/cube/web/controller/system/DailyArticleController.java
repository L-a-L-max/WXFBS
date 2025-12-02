package com.cube.web.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.cube.common.annotation.Log;
import com.cube.common.core.controller.BaseController;
import com.cube.common.core.domain.AjaxResult;
import com.cube.common.core.page.TableDataInfo;
import com.cube.common.enums.BusinessType;
import com.cube.common.utils.poi.ExcelUtil;
import com.cube.system.domain.DailyArticle;
import com.cube.system.service.IDailyArticleService;
import com.cube.wechat.selfapp.app.mapper.UserInfoMapper;
import com.cube.wechat.selfapp.app.service.WechatMpService;
import com.cube.wechat.selfapp.corpchat.util.ResultBody;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 日更助手文章Controller
 *
 * @author cube
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/system/daily-article")
public class DailyArticleController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(DailyArticleController.class);

    @Autowired
    private IDailyArticleService dailyArticleService;

    @Autowired
    private WechatMpService wechatMpService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 查询日更助手文章列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DailyArticle dailyArticle)
    {
        startPage();
        List<DailyArticle> list = dailyArticleService.selectDailyArticleList(dailyArticle);
        return getDataTable(list);
    }

    /**
     * 查询当前用户的日更助手文章列表
     */
    @GetMapping("/myList")
    public AjaxResult myList()
    {
        Long userId = getUserId();
        List<DailyArticle> list = dailyArticleService.selectDailyArticleListByUserId(userId);
        return success(list);
    }

    /**
     * 导出日更助手文章列表
     */
    @Log(title = "日更助手文章", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DailyArticle dailyArticle)
    {
        List<DailyArticle> list = dailyArticleService.selectDailyArticleList(dailyArticle);
        ExcelUtil<DailyArticle> util = new ExcelUtil<DailyArticle>(DailyArticle.class);
        util.exportExcel(response, list, "日更助手文章数据");
    }

    /**
     * 获取日更助手文章详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dailyArticleService.selectDailyArticleById(id));
    }

    /**
     * 查询文章处理状态（用于前端轮询）
     * 返回轻量级状态信息，不包含大字段内容
     */
    @GetMapping("/status/{id}")
    public AjaxResult getArticleStatus(@PathVariable("id") Long id)
    {
        DailyArticle article = dailyArticleService.selectDailyArticleById(id);
        if (article == null) {
            return error("文章不存在");
        }
        
        // 只返回状态相关字段，减少数据传输
        Map<String, Object> status = new HashMap<>();
        status.put("id", article.getId());
        status.put("articleTitle", article.getArticleTitle());
        status.put("processStatus", article.getProcessStatus());  // 0-处理中, 1-成功, 2-失败
        status.put("errorMessage", article.getErrorMessage());
        status.put("agentTaskId", article.getAgentTaskId());
        
        // 只有成功时才返回内容长度信息
        if (article.getProcessStatus() == 1) {
            status.put("hasOptimizedContent", article.getOptimizedContent() != null);
            status.put("hasModel1Content", article.getModel1Content() != null);
            status.put("hasModel2Content", article.getModel2Content() != null);
            status.put("hasModel3Content", article.getModel3Content() != null);
        }
        
        return success(status);
    }

    /**
     * 新增日更助手文章
     */
    @Log(title = "日更助手文章", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody DailyArticle dailyArticle)
    {
        dailyArticle.setCreateBy(getUsername());
        return toAjax(dailyArticleService.insertDailyArticle(dailyArticle));
    }

    /**
     * 创建文章并调用智能体优化（异步处理）
     * 
     * 返回后文章处理状态为 0（处理中），前端需要轮询 /status/{id} 接口查询处理结果
     */
    @Log(title = "日更助手-创建优化文章", businessType = BusinessType.INSERT)
    @PostMapping("/createAndOptimize")
    public AjaxResult createAndOptimize(@RequestBody Map<String, String> params)
    {
        String articleTitle = params.get("articleTitle");
        if (articleTitle == null || articleTitle.trim().isEmpty()) {
            return error("文章标题不能为空");
        }

        String selectedModels = params.get("selectedModels");
        if (selectedModels == null || selectedModels.trim().isEmpty()) {
            selectedModels = "1,2,3"; // 默认全选
        }

        Long userId = getUserId();
        DailyArticle article = dailyArticleService.createArticleAndOptimize(userId, articleTitle, selectedModels);
        
        // 异步处理：文章已创建，processStatus=0（处理中），后台正在调用AI生成内容
        // 前端可通过 GET /system/daily-article/status/{id} 接口轮询查询处理状态
        Map<String, Object> result = new HashMap<>();
        result.put("id", article.getId());
        result.put("articleTitle", article.getArticleTitle());
        result.put("processStatus", article.getProcessStatus());
        result.put("message", "文章创建成功，AI内容正在生成中，请稍后查询处理状态");
        
        return success(result);
    }

    /**
     * 保存大模型生成的文章内容
     * 注意：此接口供工作流回调使用，无需用户认证，因此不记录操作日志
     */
    @PostMapping("/saveModelContent")
    public AjaxResult saveModelContent(@RequestBody Map<String, Object> params)
    {
        try {
            logger.info("收到保存模型内容请求，参数: {}", params);
            
            // 参数校验
            if (params.get("articleId") == null) {
                return error("缺少参数: articleId");
            }
            if (params.get("modelName") == null) {
                return error("缺少参数: modelName");
            }
            if (params.get("content") == null) {
                return error("缺少参数: content");
            }
            if (params.get("modelIndex") == null) {
                return error("缺少参数: modelIndex");
            }
            
            Long articleId = Long.parseLong(params.get("articleId").toString());
            String modelName = params.get("modelName").toString();
            String content = params.get("content").toString();
            int modelIndex = Integer.parseInt(params.get("modelIndex").toString());

            logger.info("解析参数成功 - articleId: {}, modelName: {}, modelIndex: {}, contentLength: {}", 
                       articleId, modelName, modelIndex, content.length());

            int result = dailyArticleService.saveModelContent(articleId, modelName, content, modelIndex);
            
            logger.info("保存模型内容完成 - result: {}", result);
            return toAjax(result);
            
        } catch (NumberFormatException e) {
            logger.error("参数格式错误", e);
            return error("参数格式错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("保存模型内容异常", e);
            return error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 更新优化后的文章内容
     * 注意：此接口供工作流回调使用，无需用户认证，因此不记录操作日志
     */
    @PostMapping("/updateOptimizedContent")
    public AjaxResult updateOptimizedContent(@RequestBody Map<String, Object> params)
    {
        try {
            logger.info("收到更新优化内容请求，参数: {}", params);
            
            // 参数校验
            if (params.get("articleId") == null) {
                return error("缺少参数: articleId");
            }
            if (params.get("optimizedContent") == null) {
                return error("缺少参数: optimizedContent");
            }
            
            Long articleId = Long.parseLong(params.get("articleId").toString());
            String optimizedContent = params.get("optimizedContent").toString();

            logger.info("解析参数成功 - articleId: {}, contentLength: {}", 
                       articleId, optimizedContent.length());

            int result = dailyArticleService.updateOptimizedContent(articleId, optimizedContent);
            
            // 显式更新状态为已完成
            if (result > 0) {
                dailyArticleService.updateProcessStatus(articleId, 1);
                logger.info("优化内容更新成功，状态已设置为已完成 - articleId: {}", articleId);
            }
            
            logger.info("更新优化内容完成 - result: {}", result);
            return toAjax(result);
            
        } catch (NumberFormatException e) {
            logger.error("参数格式错误", e);
            return error("参数格式错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("更新优化内容异常", e);
            return error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 修改日更助手文章
     */
    @Log(title = "日更助手文章", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody DailyArticle dailyArticle)
    {
        dailyArticle.setUpdateBy(getUsername());
        return toAjax(dailyArticleService.updateDailyArticle(dailyArticle));
    }

    /**
     * 删除日更助手文章
     */
    @Log(title = "日更助手文章", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dailyArticleService.deleteDailyArticleByIds(ids));
    }

    /**
     * 投递文章到微信公众号草稿箱
     * 只支持投递排版后的文章
     *
     * @param params 请求参数（articleId, contentType, layoutedContent）
     * @return 投递结果
     */
    @Log(title = "日更助手-投递公众号", businessType = BusinessType.OTHER)
    @PostMapping("/publishToWechat")
    public AjaxResult publishToWechat(@RequestBody Map<String, Object> params)
    {
        try {
            // 1. 获取参数
            Long articleId = Long.parseLong(params.get("articleId").toString());
            String contentType = params.get("contentType").toString();

            // 只支持排版后的文章投递
            if (!"layout".equals(contentType)) {
                return error("仅支持投递排版后的文章");
            }

            log.info("开始投递排版文章到公众号 - articleId: {}", articleId);

            // 2. 查询文章（验证文章是否存在）
            DailyArticle article = dailyArticleService.selectDailyArticleById(articleId);
            if (article == null) {
                return error("文章不存在");
            }

            // 3. 获取用户UnionId
            Long userId = getUserId();
            String unionId = userInfoMapper.getUnionIdByUserId(String.valueOf(userId));
            if (unionId == null || unionId.trim().isEmpty()) {
                return error("未找到用户信息，请先登录微信");
            }

            // 4. 获取排版后的内容
            Object layoutedContentObj = params.get("layoutedContent");
            if (layoutedContentObj == null) {
                return error("排版内容为空，无法投递");
            }
            String originalContent = layoutedContentObj.toString();
            
            log.info("接收到的排版内容（前500字符）: {}", originalContent.substring(0, Math.min(500, originalContent.length())));
            
            // 5. 从排版后的HTML内容中自动提取文章标题
            String baseTitle = article.getArticleTitle();
            String extractedTitle = extractTitleFromLayoutContent(originalContent);
            String finalTitle = (extractedTitle != null && !extractedTitle.trim().isEmpty())
                    ? extractedTitle.trim()
                    : baseTitle;
            
            // 6. 去掉正文开头的标题（避免重复）
            String content = removeTitleFromContent(originalContent);
            
            // 7. 将纯文本转换为HTML格式（如果内容中没有足够的HTML标签）
            content = convertTextToHtml(content);
            
            log.info("处理后的内容（前500字符）: {}", content.substring(0, Math.min(500, content.length())));

            if (content == null || content.trim().isEmpty()) {
                return error("内容为空，无法投递");
            }

            // 8. 构建投递参数
            Map<String, Object> publishParams = new HashMap<>();
            publishParams.put("unionId", unionId);
            publishParams.put("title", finalTitle);
            publishParams.put("contentText", content);
            publishParams.put("shareUrl", ""); // 可选：生成文章分享链接
            publishParams.put("thumbMediaId", null); // 使用用户默认封面图

            log.info("调用公众号投递服务 - title: {}", finalTitle);

            // 9. 调用公众号投递服务
            ResultBody result = wechatMpService.publishToOffice(publishParams);

            // 10. 处理投递结果
            if (result.getCode() == 200) {
                log.info("排版文章投递成功 - articleId: {}", articleId);

                // 更新文章发布次数
                Integer publishCount = article.getPublishCount();
                if (publishCount == null) {
                    publishCount = 0;
                }
                article.setPublishCount(publishCount + 1);
                dailyArticleService.updateDailyArticle(article);

                return success(result.getData());
            } else {
                log.error("文章投递失败 - articleId: {}, error: {}", articleId, result.getMessages());
                return error(result.getMessages());
            }

        } catch (NumberFormatException e) {
            log.error("参数格式错误", e);
            return error("参数格式错误");
        } catch (Exception e) {
            log.error("投递文章到公众号失败", e);
            return error("投递失败：" + e.getMessage());
        }
    }

    /**
     * 从排版后的HTML内容中提取文章标题
     * 优先使用第一个<h1>标签的文本，其次尝试匹配开头的《标题》样式
     */
    private String extractTitleFromLayoutContent(String content) {
        if (content == null) {
            return null;
        }

        String lower = content.toLowerCase();
        int h1Start = lower.indexOf("<h1");
        if (h1Start >= 0) {
            int gt = lower.indexOf('>', h1Start);
            int end = lower.indexOf("</h1>", gt + 1);
            if (gt >= 0 && end > gt) {
                String h1Inner = content.substring(gt + 1, end);
                // 去掉内部可能还有的标签，同时去掉书名号《》
                String title = h1Inner.replaceAll("<[^>]+>", "").trim();
                // 去掉书名号
                if (title.startsWith("《") && title.endsWith("》")) {
                    title = title.substring(1, title.length() - 1).trim();
                }
                return title;
            }
        }

        // 尝试匹配类似：《xxx》开头的标题
        String plain = content.replaceAll("<[^>]+>", "");
        plain = plain.trim();
        if (plain.startsWith("《")) {
            int end = plain.indexOf('》');
            if (end > 1) {
                return plain.substring(1, end).trim();
            }
        }

        // 退化：使用第一行非空文本作为标题
        String[] lines = plain.split("\r?\n");
        for (String line : lines) {
            if (line != null && !line.trim().isEmpty()) {
                return line.trim();
            }
        }

        return null;
    }

    /**
     * 从排版后的内容中移除开头的标题行（避免正文和公众号标题重复）
     * 优先移除第一个<h1>...</h1>标签，其次移除开头包含《》的段落
     */
    private String removeTitleFromContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }

        String result = content;
        String lower = content.toLowerCase();
        
        // 方案1：移除第一个<h1>...</h1>标签
        int h1Start = lower.indexOf("<h1");
        if (h1Start >= 0) {
            int h1End = lower.indexOf("</h1>", h1Start);
            if (h1End > h1Start) {
                // 找到完整的 </h1> 结束标签位置
                h1End += "</h1>".length();
                // 删除整个 <h1>...</h1>
                result = content.substring(0, h1Start) + content.substring(h1End);
                return result.trim();
            }
        }

        // 方案2：移除开头包含《》的段落（通常在<p>标签里）
        // 先去掉前面的空白和标签，找到第一个实质内容
        int firstPStart = lower.indexOf("<p");
        if (firstPStart >= 0) {
            int firstPGt = lower.indexOf('>', firstPStart);
            int firstPEnd = lower.indexOf("</p>", firstPGt);
            if (firstPGt >= 0 && firstPEnd > firstPGt) {
                String firstPContent = content.substring(firstPGt + 1, firstPEnd);
                // 去掉标签后检查是否以《开头
                String plainText = firstPContent.replaceAll("<[^>]+>", "").trim();
                if (plainText.startsWith("《") && plainText.contains("》")) {
                    // 删除这个<p>...</p>
                    firstPEnd += "</p>".length();
                    result = content.substring(0, firstPStart) + content.substring(firstPEnd);
                    return result.trim();
                }
            }
        }

        return result;
    }


    /**
     * 测试接口 - 检查后端是否正常
     */
    @GetMapping("/test")
    public AjaxResult test()
    {
        log.info("=== 测试接口被调用 ===");
        return success("后端正常运行");
    }

    /**
     * 调用元器工作流对文章内容进行智能排版（同步返回结果）
     * 排版结果不保存到数据库，仅返回给前端显示
     * 
     * @param params 请求参数（包含content）
     * @return 排版后的内容
     */
    @Log(title = "日更助手-智能排版", businessType = BusinessType.OTHER)
    @PostMapping("/layoutArticle")
    public AjaxResult layoutArticle(@RequestBody Map<String, Object> params)
    {
        log.info("=== 收到排版请求 === params: {}", params);
        
        try {
            // 1. 获取参数
            String content = params.get("content").toString();

            if (content == null || content.trim().isEmpty()) {
                log.warn("排版内容为空");
                return error("排版内容不能为空");
            }

            log.info("开始排版文章 - contentLength: {}", content.length());

            // 2. 调用Service进行排版（同步返回排版结果，不保存到数据库）
            Long userId = getUserId();
            String layoutedContent = dailyArticleService.layoutArticleSync(userId, content);

            // 3. 返回排版后的内容
            Map<String, Object> result = new HashMap<>();
            result.put("layoutedContent", layoutedContent);
            result.put("message", "排版完成");

            return success(result);

        } catch (Exception e) {
            log.error("排版文章失败", e);
            return error("排版失败：" + e.getMessage());
        }
    }

    /**
     * 将纯文本内容转换为HTML格式
     * 识别段落、小标题，添加相应的HTML标签
     * 
     * @param content 原始内容
     * @return HTML格式的内容
     */
    private String convertTextToHtml(String content) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }
        
        // 检查是否已经是HTML格式（包含足够多的HTML标签）
        long htmlTagCount = content.chars().filter(ch -> ch == '<').count();
        if (htmlTagCount > 10) {
            // 已经包含较多HTML标签，直接返回
            return content;
        }
        
        StringBuilder html = new StringBuilder();
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            
            if (line.isEmpty()) {
                // 空行跳过
                continue;
            }
            
            // 识别小标题（通常是较短的行，不含句号，或以冒号结尾）
            if (line.length() < 30 && (line.endsWith("：") || line.endsWith(":") || !line.contains("。"))) {
                // 小标题用 h3 标签，加粗
                html.append("<h3><strong>").append(line).append("</strong></h3>\n");
            } else if (line.startsWith("<")) {
                // 已经是HTML标签，保持原样
                html.append(line).append("\n");
            } else {
                // 普通段落用 p 标签
                html.append("<p>").append(line).append("</p>\n");
            }
        }
        
        return html.toString();
    }

}
