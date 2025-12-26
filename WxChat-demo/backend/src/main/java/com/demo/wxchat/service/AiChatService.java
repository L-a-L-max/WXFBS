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
        log.info("[元器请求] 开始处理 - sessionId: {}, message: {}", sessionId, message);
        
        String targetUrl = yuanqiChatConfig.getTargetUrl();
        String token = yuanqiChatConfig.getToken();
        String encodingAesKey = yuanqiChatConfig.getEncodingAesKey();
        String corpId = yuanqiChatConfig.getCorpId();
        String agentId = yuanqiChatConfig.getAgentId();

        log.info("[元器配置] targetUrl: {}", targetUrl);
        log.info("[元器配置] corpId: {}, agentId: {}", corpId, agentId);

        try {
            log.info("[加密处理] 开始初始化加密器");
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, encodingAesKey, corpId);
            log.info("[加密处理] 加密器初始化成功");

            long timestamp = System.currentTimeMillis() / 1000;
            long msgId = System.currentTimeMillis();
            String timestampStr = String.valueOf(timestamp);
            String nonce = generateNonce();

            // 构建消息内容
            String wrappedMessage = "[SID:" + sessionId + "]" + message;
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
                    msgId,
                    agentId != null ? agentId : "1000011"
            );
            
            log.info("[消息构建] XML消息构建完成，长度: {}", xmlMessage.length());
            log.debug("[消息详情] XML内容: {}", xmlMessage);

            // AES加密
            log.info("[加密处理] 开始AES加密");
            String encryptedXml = crypt.encryptMsg(xmlMessage, timestampStr, nonce);
            log.info("[加密处理] 加密完成，密文长度: {}", encryptedXml.length());
            
            String msgSignature = extractMsgSignature(encryptedXml);
            log.info("[签名验证] 消息签名: {}", msgSignature);

            // 构建请求URL
            String requestUrl = targetUrl + "?msg_signature=" + msgSignature
                    + "&timestamp=" + timestampStr
                    + "&nonce=" + nonce;

            log.info("[HTTP请求] 准备发送 - URL: {}", requestUrl);
            log.info("[HTTP请求] 请求体长度: {} bytes", encryptedXml.getBytes().length);

            // 发送HTTP请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            HttpEntity<String> requestEntity = new HttpEntity<>(encryptedXml, headers);

            log.info("[HTTP请求] 开始发送POST请求...");
            long startTime = System.currentTimeMillis();
            
            ResponseEntity<String> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("[HTTP响应] 请求完成 - 耗时: {}ms, 状态码: {}", elapsedTime, response.getStatusCode().value());

            String result = response.getBody();
            if (result == null || result.isEmpty()) {
                log.info("[HTTP响应] 元器返回空响应体，消息已成功发送");
                return "消息已发送";
            }

            log.info("[HTTP响应] 响应体长度: {} bytes", result.length());
            log.debug("[HTTP响应] 响应内容: {}", result);

            // 元器通常只返回200状态码，不需要复杂解析
            return "消息已发送";
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("[HTTP错误] 客户端错误 - 状态码: {}, 错误信息: {}", e.getStatusCode(), e.getMessage());
            log.error("[HTTP错误] 响应体: {}", e.getResponseBodyAsString());
            return "发送失败：" + e.getStatusCode();
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            log.error("[HTTP错误] 服务器错误 - 状态码: {}, 错误信息: {}", e.getStatusCode(), e.getMessage());
            log.error("[HTTP错误] 响应体: {}", e.getResponseBodyAsString());
            return "元器服务异常";
        } catch (Exception e) {
            log.error("[系统错误] 发送消息失败 - 异常类型: {}, 错误信息: {}", e.getClass().getName(), e.getMessage(), e);
            return "系统异常：" + e.getMessage();
        }
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
