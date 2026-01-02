package com.wx.fbsir.business.potentialeye.controller;


import com.wx.fbsir.business.potentialeye.config.YuanqiChatConfig;
import com.wx.fbsir.business.potentialeye.utils.WXBizMsgCrypt;
import com.wx.fbsir.common.annotation.Anonymous;
import com.wx.fbsir.business.potentialeye.utils.PotentialEyeChatUtil;
import com.wx.fbsir.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 企业微信通道回调控制器
 * 
 * 用于接收元器的回调消息，拦截AI回复并存储到数据库
 * 
 * 元器配置的回调地址应该是：https://你的域名/api/v2/channel/callback/wecom/message/{channelId}
 */
@RestController
@RequestMapping("/api/v2/channel/callback/wecom/message")
public class ChannelCallbackController {

    private static final Logger log = LoggerFactory.getLogger(ChannelCallbackController.class);

    @Autowired
    private YuanqiChatConfig yuanqiChatConfig;

    @Autowired
    private PotentialEyeChatUtil potentialEyeChatUtil;

    @Autowired
    private ISysUserService userService;

    private final RestTemplate restTemplate;

    public ChannelCallbackController() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(15000);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * URL验证接口（GET请求）
     * 元器配置回调地址时会先发GET请求验证
     */
    @Anonymous
    @GetMapping("/{channelId}")
    public String handleVerification(
            @PathVariable String channelId,
            @RequestParam("msg_signature") String msgSignature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam(value = "echostr",required = false) String echostr) {

        log.info("[企微通道] 收到URL验证请求 - channelId: {}, signature: {}", channelId, msgSignature);

        // 转发验证请求到元器
        String url = buildGetUrl(resolveUpstreamUrl(channelId), msgSignature, timestamp, nonce, echostr);
        log.info("[企微通道] 转发验证到元器: {}", url);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            String body = response.getBody();
            log.info("[企微通道] 元器验证响应 - 状态码: {}, 内容: {}", response.getStatusCode(), body);
            return body == null ? "" : body;
        } catch (Exception e) {
            log.error("[企微通道] URL验证转发失败 - channelId: {}", channelId, e);
            return "";
        }
    }

    /**
     * 消息回调接口（POST请求）
     * 接收元器发送的AI回复消息
     */
    @Anonymous
    @PostMapping(
            value = "/{channelId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE, MediaType.ALL_VALUE},
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public String handleMessage(
            @PathVariable String channelId,
            @RequestParam("msg_signature") String msgSignature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestBody(required = false) String requestBody) {

        if (requestBody == null || requestBody.isEmpty()) {
            return "";
        }

        log.info("[企微通道] 收到消息回调 - channelId: {}, signature: {}, bodyLen: {}", channelId, msgSignature, requestBody.length());

        try {
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(
                    yuanqiChatConfig.getToken(),
                    yuanqiChatConfig.getEncodingAesKey(),
                    yuanqiChatConfig.getCorpId()
            );

            String plainXml = requestBody;
            if (requestBody.contains("<Encrypt>")) {
                plainXml = crypt.decryptMsg(msgSignature, timestamp, nonce, requestBody);
            }

            String fromUser = extractXmlValue(plainXml, "FromUserName");
            String toUser = extractXmlValue(plainXml, "ToUserName");

            String msgType = extractXmlValue(plainXml, "MsgType");
            String content = extractXmlValue(plainXml, "Content");

            log.info("[企微通道] 消息解析 - From: {}, To: {}, Type: {}, ContentLen: {}",
                    fromUser, toUser, msgType, content == null ? 0 : content.length());

            // 判断是否是AI回复消息（发送给真实用户的消息）
            boolean isFromApp = fromUser != null && (fromUser.startsWith("ww") || fromUser.equals("sys") || fromUser.contains("@"));
            boolean isToRealUser = toUser != null && !toUser.isEmpty()
                    && !toUser.startsWith("ww")
                    && !"yuanqi".equalsIgnoreCase(toUser);

            if (isToRealUser && content != null && !content.isEmpty() && (msgType == null || "text".equalsIgnoreCase(msgType))) {
                log.info("[企微通道] 拦截到回复消息 - toUser: {}, fromUser: {}", toUser, fromUser);
                // 保存AI回复到数据库
                potentialEyeChatUtil.saveAssistantMessage(toUser, content, null);

                return "";
            }

            // 按FromUser策略再判断一次
            if (isFromApp && toUser != null && !toUser.isEmpty() && content != null && !content.isEmpty()) {
                log.info("[企微通道] 按FromUser策略拦截到回复消息 - toUser: {}, fromUser: {}", toUser, fromUser);
                potentialEyeChatUtil.saveAssistantMessage(toUser, content, null);
                return "";
            }
        } catch (Exception e) {
            log.error("[企微通道] 回调解析失败 - channelId: {}", channelId, e);
        }

        // 如果不是回复消息，转发到元器
        String upstreamUrl = buildPostUrl(resolveUpstreamUrl(channelId), msgSignature, timestamp, nonce);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(upstreamUrl, HttpMethod.POST, entity, String.class);
            String body = response.getBody();
            return body == null ? "" : body;
        } catch (Exception e) {
            log.error("[企微通道] 回调转发失败 - channelId: {}", channelId, e);
            return "";
        }
    }

    /**
     * 根据channelId解析上游元器URL
     */
    private String resolveUpstreamUrl(String channelId) {
        String targetUrl = yuanqiChatConfig.getTargetUrl();
        if (targetUrl == null || targetUrl.isEmpty()) {
            return "https://yuanqi.tencent.com/api/v2/channel/callback/wecom/message/" + channelId;
        }
        String marker = "/api/v2/channel/callback/wecom/message/";
        int idx = targetUrl.indexOf(marker);
        if (idx == -1) {
            return targetUrl;
        }

        String prefix = targetUrl.substring(0, idx + marker.length());
        return prefix + channelId;
    }

    private String buildGetUrl(String baseUrl, String msgSignature, String timestamp, String nonce, String echostr) {
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append("?msg_signature=").append(encode(msgSignature));
        sb.append("&timestamp=").append(encode(timestamp));
        sb.append("&nonce=").append(encode(nonce));
        sb.append("&echostr=").append(encode(echostr));
        return sb.toString();
    }

    private String buildPostUrl(String baseUrl, String msgSignature, String timestamp, String nonce) {
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append("?msg_signature=").append(encode(msgSignature));
        sb.append("&timestamp=").append(encode(timestamp));
        sb.append("&nonce=").append(encode(nonce));
        return sb.toString();
    }

    private String encode(String value) {
        if (value == null) return "";
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String extractXmlValue(String xml, String tagName) {
        try {
            String startTag = "<" + tagName + "><![CDATA[";
            String endTag = "]]></" + tagName + ">";
            int start = xml.indexOf(startTag);
            int end = xml.indexOf(endTag);
            if (start != -1 && end > start) {
                return xml.substring(start + startTag.length(), end);
            }

            startTag = "<" + tagName + ">";
            endTag = "</" + tagName + ">";
            start = xml.indexOf(startTag);
            end = xml.indexOf(endTag);
            if (start != -1 && end > start) {
                return xml.substring(start + startTag.length(), end);
            }
        } catch (Exception e) {
            log.warn("[企微通道] XML解析失败 - tag: {}", tagName);
        }
        return null;
    }
}
