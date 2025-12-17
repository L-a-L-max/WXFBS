package com.demo.wxchat.service;

import com.demo.wxchat.config.YuanqiChatConfig;
import com.demo.wxchat.util.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * AI对话服务
 */
@Service
public class AiChatService {

    private static final Logger log = LoggerFactory.getLogger(AiChatService.class);

    @Autowired
    private YuanqiChatConfig yuanqiChatConfig;

    private final RestTemplate restTemplate;

    public AiChatService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(15000);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * 发送消息到元器
     */
    public String sendMessage(String message, String sessionId) {
        String targetUrl = yuanqiChatConfig.getTargetUrl();
        String token = yuanqiChatConfig.getToken();
        String encodingAesKey = yuanqiChatConfig.getEncodingAesKey();
        String corpId = yuanqiChatConfig.getCorpId();
        String agentId = yuanqiChatConfig.getAgentId();

        try {
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, encodingAesKey, corpId);

            long timestamp = System.currentTimeMillis() / 1000;
            long msgId = System.currentTimeMillis();
            String timestampStr = String.valueOf(timestamp);
            String nonce = generateNonce();

            // 把sessionId嵌入消息内容，格式：[SID:xxx]实际消息
            // 工作流可以从SYS.UserQuery中提取sessionId
            String wrappedMessage = "[SID:" + sessionId + "]" + message;

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
                    msgId,
                    agentId != null ? agentId : "1000011"
            );

            // AES加密
            String encryptedXml = crypt.encryptMsg(xmlMessage, timestampStr, nonce);
            String msgSignature = extractMsgSignature(encryptedXml);

            // 构建请求URL
            String requestUrl = targetUrl + "?msg_signature=" + msgSignature
                    + "&timestamp=" + timestampStr
                    + "&nonce=" + nonce;

            log.info("[AI对话] 发送消息到元器 - sessionId: {}, message: {}", sessionId, message);

            // 发送HTTP请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            HttpEntity<String> requestEntity = new HttpEntity<>(encryptedXml, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            String result = response.getBody();
            if (result == null) {
                result = "";
            }

            log.info("[AI对话] 元器响应 - 状态码: {}, 响应长度: {}", response.getStatusCode().value(), result.length());

            if (result.isEmpty()) {
                return "消息已发送，元器正在处理中...";
            }

            return parseYuanqiResponse(result, crypt, timestampStr, nonce);
        } catch (Exception e) {
            log.error("[AI对话] 发送消息失败", e);
            return "抱歉，服务暂时不可用，请稍后重试";
        }
    }

    private String parseYuanqiResponse(String response, WXBizMsgCrypt crypt, String timestamp, String nonce) {
        try {
            if (response.contains("<Encrypt>")) {
                String msgSignature = extractMsgSignature(response);
                String decrypted = crypt.decryptMsg(msgSignature, timestamp, nonce, response);
                return extractContent(decrypted);
            }
            return extractContent(response);
        } catch (Exception e) {
            log.warn("[AI对话] 响应解析失败: {}", e.getMessage());
            if (response.contains("<Content>")) {
                return extractContent(response);
            }
            return "收到响应，解析中...";
        }
    }

    private String extractContent(String xml) {
        try {
            int start = xml.indexOf("<Content><![CDATA[");
            int end = xml.indexOf("]]></Content>");
            if (start != -1 && end > start) {
                return xml.substring(start + 18, end);
            }

            start = xml.indexOf("<Content>");
            end = xml.indexOf("</Content>");
            if (start != -1 && end > start) {
                return xml.substring(start + 9, end);
            }
        } catch (Exception e) {
            log.warn("[AI对话] Content提取失败: {}", e.getMessage());
        }
        return "消息已发送";
    }

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

    private String generateNonce() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
