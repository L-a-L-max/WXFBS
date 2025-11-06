package com.cube.wechat.selfapp.app.service.impl;

import com.cube.wechat.selfapp.app.config.WechatMpConfig;
import com.cube.wechat.selfapp.app.constants.WxExceptionConstants;
import com.cube.wechat.selfapp.app.domain.Item;
import com.cube.wechat.selfapp.app.domain.WcOfficeAccount;
import com.cube.wechat.selfapp.app.mapper.UserInfoMapper;
import com.cube.wechat.selfapp.app.service.WechatMpService;
import com.cube.wechat.selfapp.app.util.WeChatUtils;
import com.cube.wechat.selfapp.corpchat.util.ResultBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.draft.WxMpAddDraft;
import me.chanjar.weixin.mp.bean.draft.WxMpDraftArticles;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author muyou
 * dateStart 2024/8/4 9:34
 * dateNow   2025/8/31 17:32
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WechatMpServiceImpl implements WechatMpService {
    private final WeChatUtils weChatUtils;
    private final UserInfoMapper userInfoMapper;
    private final WechatMpConfig wechatMpConfig;
    
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
        
        // 1. 移除Markdown代码块标记（```）及其内容
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
    public ResultBody publishToOffice(Map map) {
        try {
            String unionId = map.get("unionId").toString();
            String title = map.get("title").toString();
            String contentText = map.get("contentText").toString();
            String shareUrl = map.get("shareUrl").toString();
            Object thumbMediaId = map.get("thumbMediaId");
            String userId = userInfoMapper.getUserIdByUnionId(unionId);
            WcOfficeAccount wo = weChatUtils.getOfficeAccountByUserId(userId);
            if(unionId == null || title == null || contentText == null || shareUrl == null) {
                throw new RuntimeException(WxExceptionConstants.WX_PARAMETER_EXCEPTION);
            }
            if (wo == null) {
                throw new RuntimeException(WxExceptionConstants.WX_AUTH_EXCEPTION);
            }
            
            // 提取标题（如果标题为空或"标题待定"，尝试从内容中提取）
            if (title == null || title.trim().isEmpty() || title.equals("标题待定")) {
                int first = contentText.indexOf("《");
                int second = contentText.indexOf("》", first + 1);
                if (first >= 0 && second > first) {
                    title = contentText.substring(first + 1, second);
                    // 移除标题行
                    contentText = contentText.substring(second + 1).trim();
                    while (contentText.startsWith("\r\n") || contentText.startsWith("\n")) {
                        contentText = contentText.replaceFirst("^[\r\n]+", "");
                    }
                }
            }
            
            // 清洗HTML内容，确保符合微信草稿箱API要求
            contentText = sanitizeHtmlContent(contentText);
            
            // 清理多余的换行
            contentText = contentText.replaceAll("\r\n\r\n", "");
            
            // 添加原文链接
            if (shareUrl != null && !shareUrl.trim().isEmpty()) {
                String shareUrlHtml = "<p>原文链接：" + shareUrl + "</p>";
                contentText = shareUrlHtml + contentText;
            }
            
            log.info("📝 标题: {}", title);
            log.info("📝 内容长度: {}", contentText.length());
            
            WxMpService wxMpService = wechatMpConfig.getWxMpService(unionId);
            WxMpDraftArticles draft = new WxMpDraftArticles();
            draft.setTitle(title);
            draft.setContent(contentText); // 使用清理后的内容
            if(thumbMediaId == null) {
                draft.setThumbMediaId(wo.getMediaId());
            } else {
                draft.setThumbMediaId(thumbMediaId.toString()); // 直接使用已有封面图media_id
            }
            draft.setShowCoverPic(1); // 显示封面
            draft.setContentSourceUrl(shareUrl); // 添加文章来源链接
            WxMpAddDraft wxMpAddDraft = WxMpAddDraft.builder().articles(List.of(draft)).build();
            // 3. 调用微信接口上传草稿
            String mediaId = wxMpService.getDraftService().addDraft(wxMpAddDraft);
            String publishedArticleUrl = weChatUtils.getPublishedArticleUrl(mediaId, unionId);
            if (publishedArticleUrl == null || publishedArticleUrl.isEmpty()) {
                return ResultBody.error(204, "获取文章链接失败");
            } else {
                return ResultBody.success(publishedArticleUrl);
            }
        } catch (Exception e) {
            log.error("❌ 微信草稿箱发布失败", e);
            
            // 🔥 增强的错误提示 - 根据微信API错误码提供友好提示
            String errorMsg = e.getMessage();
            String userFriendlyMsg = parseWeChatError(errorMsg);
            
            return ResultBody.error(500, userFriendlyMsg);
        }
    }

    @Override
    public ResultBody getMaterial(Map map) {
        try {
            String unionId = null;
            String type = null;
            try {
                unionId = map.get("unionId").toString();
                type = map.get("type").toString();
            } catch (Exception e) {
                throw new RuntimeException(WxExceptionConstants.WX_PARAMETER_EXCEPTION);
            }
            List<Item> itemList = Collections.emptyList();
            // TODO 后续增添更多类型
            if(type.equals("image")) {
                itemList = weChatUtils.getImgMaterial("image", unionId);
            }
            return ResultBody.success(itemList);
        } catch (Exception e) {
            return ResultBody.error(204, e.getMessage());
        }
    }

    @Override
    public ResultBody uploadMaterial(String type, String unionId, String imgDescription, MultipartFile multipartFile) {
        try {
            // TODO 后续增加其他类型上传
            if(type.equals("image")) {
                InputStream inputStream = multipartFile.getInputStream();
                String url = weChatUtils.uploadImgMaterial(unionId, inputStream, imgDescription);
                if(url == null || url.isEmpty()) {
                    return ResultBody.error(204, "url为空");
                }
                return ResultBody.success(url);
            }
            return ResultBody.error(204, "暂不支持该类型上传");
        } catch (Exception e) {
            return ResultBody.error(500, e.getMessage());
        }
    }

    @Override
    public ResultBody uploadCoverImgMaterial(Map map) {
        return null;
    }
    
    /**
     * 🔥 解析微信API错误，提供友好的用户提示
     * 
     * @param errorMsg 微信API返回的原始错误信息
     * @return 友好的错误提示信息
     */
    private String parseWeChatError(String errorMsg) {
        if (errorMsg == null || errorMsg.isEmpty()) {
            return "发布失败，请重试";
        }
        
        // 🔥 错误码53402：封面裁剪失败
        if (errorMsg.contains("53402") || errorMsg.contains("封面裁剪失败")) {
            return "❌ 封面图片处理失败（错误码53402）\n" +
                   "📌 可能原因：\n" +
                   "  1. 封面图片尺寸不符合要求（建议：900x383像素或2:1比例）\n" +
                   "  2. 图片格式不支持（仅支持JPG、PNG）\n" +
                   "  3. 图片文件损坏或过大（限制1MB以内）\n" +
                   "💡 解决方案：请重新上传符合要求的封面图片";
        }
        
        // 🔥 错误码53403：封面图片media_id无效
        if (errorMsg.contains("53403") || errorMsg.contains("media_id")) {
            return "❌ 封面图片ID无效（错误码53403）\n" +
                   "📌 可能原因：\n" +
                   "  1. 封面图片已过期（有效期3天）\n" +
                   "  2. 图片ID不存在或已被删除\n" +
                   "💡 解决方案：请重新上传封面图片";
        }
        
        // 🔥 错误码40001：access_token过期
        if (errorMsg.contains("40001") || errorMsg.contains("access_token")) {
            return "❌ 微信授权已过期（错误码40001）\n" +
                   "📌 请重新登录微信公众号授权";
        }
        
        // 🔥 错误码40125/40155：AppID或AppSecret无效
        if (errorMsg.contains("40125") || errorMsg.contains("40155") || 
            errorMsg.contains("AppID") || errorMsg.contains("AppSecret")) {
            return "❌ 微信公众号配置错误\n" +
                   "📌 请联系管理员检查AppID和AppSecret配置";
        }
        
        // 🔥 错误码45009：接口调用超过限制
        if (errorMsg.contains("45009") || errorMsg.contains("调用超过")) {
            return "❌ 接口调用次数超限（错误码45009）\n" +
                   "📌 微信API每日调用次数有限制\n" +
                   "💡 解决方案：请稍后再试或联系管理员";
        }
        
        // 🔥 错误码40007：无效的media_id（封面图）
        if (errorMsg.contains("40007")) {
            return "❌ 封面图片无效（错误码40007）\n" +
                   "📌 可能原因：图片已过期或格式不正确\n" +
                   "💡 解决方案：请重新上传封面图片";
        }
        
        // 🔥 内容格式错误
        if (errorMsg.contains("内容格式") || errorMsg.contains("HTML")) {
            return "❌ 内容格式不符合微信要求\n" +
                   "📌 可能原因：\n" +
                   "  1. HTML标签不符合微信规范\n" +
                   "  2. 包含不支持的标签或属性\n" +
                   "  3. 内容为空或格式错误\n" +
                   "💡 解决方案：请检查AI生成的内容格式";
        }
        
        // 🔥 内容包含思考过程提示
        if (errorMsg.contains("让我") || errorMsg.contains("首先，我") || 
            errorMsg.contains("接下来，") || errorMsg.contains("需要先")) {
            return "⚠️ 检测到AI思考过程内容\n" +
                   "📌 当前内容可能包含AI的思考过程，而不是最终答案\n" +
                   "💡 解决方案：\n" +
                   "  1. 请重新生成内容\n" +
                   "  2. 确保获取的是AI的最终回答\n" +
                   "  3. 或手动编辑去除思考过程内容";
        }
        
        // 🔥 标题相关错误
        if (errorMsg.contains("标题")) {
            return "❌ 标题格式错误\n" +
                   "📌 可能原因：标题为空、过长（限制64字符）或格式不正确\n" +
                   "💡 解决方案：请检查内容中是否包含《标题》格式";
        }
        
        // 🔥 默认错误提示
        return "❌ 发布失败：" + errorMsg + "\n" +
               "💡 如问题持续，请联系技术支持";
    }
}
