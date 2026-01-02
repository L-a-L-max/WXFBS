package com.wx.fbsir.business.potentialeye.utils;

import com.wx.fbsir.business.potentialeye.config.YuanqiChatConfig;
import com.wx.fbsir.business.potentialeye.domain.ChatMessage;
import com.wx.fbsir.business.potentialeye.service.IChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class PotentialEyeChatUtil {

    private static final Logger log = LoggerFactory.getLogger(PotentialEyeChatUtil.class);

    @Autowired
    private YuanqiChatConfig yuanqiChatConfig;

    @Autowired
    private IChatMessageService chatMessageService;

    private final RestTemplate restTemplate;

    public PotentialEyeChatUtil() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(15000);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * 发送消息到元器平台（包含简历URL列表）
     *
     * @param message 用户消息内容
     * @param sessionId 用户名/会话ID
     * @param chatFileUrlList 简历URL列表
     * @return 处理结果
     */
    public String sendMessage(String message, String sessionId, List<String> chatFileUrlList) {
        log.info("[元器请求] 开始处理 - sessionId: {}, message: {}", sessionId, message);

        String targetUrl = yuanqiChatConfig.getTargetUrl();
        String token = yuanqiChatConfig.getToken();
        String encodingAesKey = yuanqiChatConfig.getEncodingAesKey();
        String corpId = yuanqiChatConfig.getCorpId();
        String agentId = yuanqiChatConfig.getAgentId();

        log.info("[元器配置] targetUrl: {}", targetUrl);
        log.info("[企业微信认证] corpId: {}", corpId);

        try {
            log.info("[加密处理] 开始初始化加密器");
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, encodingAesKey, corpId);
            log.info("[加密处理] 加密器初始化成功");

            long timestamp = System.currentTimeMillis() / 1000;
            String timestampStr = String.valueOf(timestamp);
            String nonce = generateNonce();

            // 构建消息体，将resumeUrlList信息编码到消息中
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("[SID:").append(sessionId).append("]");
            messageBuilder.append(message);

            if (chatFileUrlList != null && !chatFileUrlList.isEmpty()) {
                messageBuilder.append(" [RESUMES:");
                for (int i = 0; i < chatFileUrlList.size(); i++) {
                    if (i > 0) {
                        messageBuilder.append(",");
                    }
                    messageBuilder.append(chatFileUrlList.get(i));
                }
                messageBuilder.append("]");
            }

            String wrappedMessage = messageBuilder.toString();
            log.info("[消息构建] 包装后消息: {}", wrappedMessage);

            // 构建企业微信XML格式消息
            String xmlMessage = String.format(
                    "<xml>" +
                            "<ToUserName><![CDATA[yuanqi]]></ToUserName>" +
                            "<FromUserName><![CDATA[%s]]></FromUserName>" +
                            "<CreateTime>%d</CreateTime>" +
                            "<MsgType><![CDATA[text]]></MsgType>" +
                            "<Content><![CDATA[%s]]></Content>" +
                            "<MsgId>%d</MsgId>" +
                            "<AgentID>%s</AgentID>" +
                            "</xml>",
                    sessionId,
                    timestamp,
                    wrappedMessage,
                    System.currentTimeMillis(),
                    agentId != null ? agentId : "1000011"
            );

            log.info("[消息构建] XML消息构建完成，长度: {}", xmlMessage.length());

            // AES加密
            log.info("[加密处理] 开始AES加密");
            String encryptedXml = crypt.encryptMsg(xmlMessage, timestampStr, nonce);
            log.info("[加密处理] 加密完成，密文长度: {}", encryptedXml.length());

            // 从加密后的XML中提取MsgSignature
            String msgSignature = extractMsgSignature(encryptedXml);
            log.info("[签名验证] 消息签名: {}", msgSignature);

            // 构建请求URL
            String requestUrl = buildRequestUrl(targetUrl, msgSignature, timestampStr, nonce);
            // 构建请求实体
            HttpEntity<String> requestEntity = buildSimpleRequestEntity(encryptedXml);

            log.info("[HTTP请求] 准备发送 - URL: {}", requestUrl);
            log.info("[HTTP请求] 请求体长度: {} bytes", encryptedXml.getBytes().length);
            log.info("[HTTP请求] file URL数量: {}", chatFileUrlList != null ? chatFileUrlList.size() : 0);

            long startTime = System.currentTimeMillis();
            ResponseEntity<String> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("[HTTP响应] 请求完成 - 耗时: {}ms, 状态码: {}",
                    elapsedTime, response.getStatusCodeValue());

            return processSimpleResponse(response);

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("[HTTP错误] 客户端错误 - 状态码: {}, 错误信息: {}",
                    e.getStatusCode(), e.getMessage());
            return "发送失败：" + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            log.error("[HTTP错误] 服务器错误 - 状态码: {}, 错误信息: {}",
                    e.getStatusCode(), e.getMessage());
            return "元器服务异常：" + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            log.error("[系统错误] 发送消息失败 - 异常类型: {}, 错误信息: {}",
                    e.getClass().getName(), e.getMessage(), e);
            return "系统异常：" + e.getMessage();
        }
    }

    /**
     * 提取MsgSignature
     */
    private String extractMsgSignature(String xml) {
        try {
            int start = xml.indexOf("<MsgSignature><![CDATA[");
            int end = xml.indexOf("]]></MsgSignature>");
            if (start != -1 && end > start) {
                return xml.substring(start + 23, end);
            }

            start = xml.indexOf("<MsgSignature>");
            end = xml.indexOf("</MsgSignature>");
            if (start != -1 && end > start) {
                return xml.substring(start + 14, end);
            }
        } catch (Exception e) {
            log.warn("[AI对话] MsgSignature提取失败: {}", e.getMessage());
        }
        return "";
    }

    /**
     * 构建请求URL（包含签名）
     */
    private String buildRequestUrl(String targetUrl, String msgSignature, String timestamp, String nonce) {
        return targetUrl +
                "?msg_signature=" + msgSignature +
                "&timestamp=" + timestamp +
                "&nonce=" + nonce;
    }

    /**
     * 构建简单请求实体（XML格式）
     */
    private HttpEntity<String> buildSimpleRequestEntity(String encryptedXml) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        return new HttpEntity<>(encryptedXml, headers);
    }

    /**
     * 构建请求实体 - 多部分表单格式（备用方案）
     */
    private HttpEntity<MultiValueMap<String, Object>> buildMultipartRequestEntity(
            String message, String sessionId, List<String> chatFileUrlList,
            String corpId, String token, String encodingAesKey, String timestamp, String nonce) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 1. 企业微信认证信息（必需）
        body.add("corpId", corpId);
        body.add("token", token);
        body.add("encodingAesKey", encodingAesKey);

        // 2. 会话和消息内容
        body.add("sessionId", sessionId);
        body.add("message", message);

        // 3. Resume URL列表
        if (chatFileUrlList != null && !chatFileUrlList.isEmpty()) {
            log.info("[file处理] 开始处理file URL列表，数量: {}", chatFileUrlList.size());

            for (int i = 0; i < chatFileUrlList.size(); i++) {
                String fileUrl = chatFileUrlList.get(i);
                if (fileUrl != null && !fileUrl.trim().isEmpty()) {
                    // 为每个简历URL添加一个参数
                    String key = "fileUrl_" + i;
                    body.add(key, fileUrl);
                    log.info("[Resume处理] 添加第{}个简历URL: {}", i + 1, fileUrl);
                }
            }

            // 同时添加一个包含所有Resume URL的数组参数
            for (String fileUrl : chatFileUrlList) {
                if (fileUrl != null && !fileUrl.trim().isEmpty()) {
                    body.add("fileUrls", fileUrl);
                }
            }
            log.info("[file处理] file URL列表添加完成，共{}个URL", chatFileUrlList.size());
        } else {
            log.info("[file处理] 无file URL需要处理");
        }

        // 4. 其他参数
        body.add("timestamp", timestamp);
        body.add("nonce", nonce);

        return new HttpEntity<>(body, headers);
    }

    /**
     * 处理响应
     */
    private String processSimpleResponse(ResponseEntity<String> response) {
        String result = response.getBody();
        if (result == null || result.isEmpty()) {
            log.info("[HTTP响应] 元器返回空响应体");
            return "消息已发送";
        }
        log.info("[HTTP响应] 响应体: {}", result);
        return result;
    }

    /**
     * 生成随机数
     */
    private String generateNonce() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 保存AI回复消息（供工作流回调使用）
     */
    public ChatMessage saveAssistantMessage(String sessionId, String content, String messageId) {
        // 检查是否已存在（去重）
        if (messageId != null) {
            ChatMessage chatMessage = chatMessageService.selectChatMessageByMessageId(messageId);
            if (chatMessage != null) {
                log.info("[AI对话消息] 消息已存在，跳过 - messageId: {}", messageId);
                return chatMessage;
            }
        }


        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setRole("AI");
        message.setContent(content);
        message.setMessageId(messageId != null ? messageId : UUID.randomUUID().toString());
        message.setIsRead(0);  // AI回复默认未读
        message.setCreateTime(new Date());

        chatMessageService.insertChatMessage(message);
        log.info("[AI对话消息] 保存AI回复 - sessionId: {}, 内容长度: {}", sessionId, content.length());

        return message;
    }

    /**
     * 获取未读的AI回复消息
     */
    public List<ChatMessage> getUnreadMessages(String sessionId) {
        return chatMessageService.selectUnreadMessages(sessionId);
    }

    /**
     * 标记消息为已读
     */
    public void markAsRead(String sessionId, List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            chatMessageService.markAsRead(sessionId, ids);
            log.debug("[AI对话消息] 标记已读 - sessionId: {}, 数量: {}", sessionId, ids.size());
        }
    }
}