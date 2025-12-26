package com.demo.wxchat.controller;

import com.demo.wxchat.domain.AiChatMessage;
import com.demo.wxchat.domain.AjaxResult;
import com.demo.wxchat.service.AiChatMessageService;
import com.demo.wxchat.service.AiChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI对话控制器
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private AiChatService aiChatService;

    @Autowired
    private AiChatMessageService aiChatMessageService;

    /**
     * 发送消息到元器AI
     */
    @PostMapping("/send")
    public AjaxResult sendMessage(@RequestBody Map<String, Object> request) {
        String sessionId = null;
        try {
            String message = (String) request.get("message");
            sessionId = (String) request.get("sessionId");

            if (sessionId == null || sessionId.isBlank()) {
                sessionId = "user_" + System.currentTimeMillis();
            }

            if (message == null || message.trim().isEmpty()) {
                log.warn("[AI对话] 消息为空 - sessionId: {}", sessionId);
                return AjaxResult.error("消息不能为空");
            }

            log.info("[AI对话] 用户发送消息 - sessionId: {}, message: {}", sessionId, message);

            // 保存用户消息到数据库
            log.info("[AI对话] 开始保存用户消息到数据库...");
            aiChatMessageService.saveUserMessage(sessionId, message);
            log.info("[AI对话] 用户消息已保存到数据库");

            // 发送到元器
            log.info("[AI对话] 开始转发消息到元器...");
            String reply = aiChatService.sendMessage(message, sessionId);
            log.info("[AI对话] 元器请求完成，返回: {}", reply);

            return AjaxResult.success(Map.of(
                    "reply", reply,
                    "timestamp", System.currentTimeMillis()
            ));

        } catch (Exception e) {
            log.error("[AI对话] 发送消息失败 - sessionId: {}, 错误: {}", sessionId, e.getMessage(), e);
            return AjaxResult.error("发送失败: " + e.getMessage());
        }
    }

    /**
     * 轮询获取AI回复（从数据库读取未读消息）
     */
    @GetMapping("/poll")
    public AjaxResult pollReply(@RequestParam String sessionId) {
        try {
            // 从数据库获取未读的AI回复
            List<AiChatMessage> unreadMessages = aiChatMessageService.getUnreadMessages(sessionId);

            if (unreadMessages != null && !unreadMessages.isEmpty()) {
                // 拼接所有未读消息
                String reply = unreadMessages.stream()
                        .map(AiChatMessage::getContent)
                        .collect(Collectors.joining("\n"));

                // 标记为已读
                List<Long> ids = unreadMessages.stream()
                        .map(AiChatMessage::getId)
                        .collect(Collectors.toList());
                aiChatMessageService.markAsRead(sessionId, ids);

                return AjaxResult.success(Map.of(
                        "hasReply", true,
                        "reply", reply
                ));
            } else {
                return AjaxResult.success(Map.of(
                        "hasReply", false
                ));
            }

        } catch (Exception e) {
            log.error("[AI对话] 轮询失败", e);
            return AjaxResult.error("轮询失败: " + e.getMessage());
        }
    }

    /**
     * 工作流回调接口 - 接收元器工作流发送的AI回复（JSON格式）
     * 元器工作流中配置HTTP节点调用此接口
     */
    @PostMapping("/workflow/callback")
    public AjaxResult handleWorkflowCallback(@RequestBody Map<String, Object> request) {
        try {
            String sessionId = (String) request.get("sessionId");
            String content = (String) request.get("content");
            String messageId = (String) request.get("messageId");

            log.info("[工作流回调] 收到AI回复 - sessionId: {}, 内容长度: {}", sessionId, content != null ? content.length() : 0);

            if (sessionId == null || sessionId.isEmpty()) {
                return AjaxResult.error("sessionId不能为空");
            }
            if (content == null || content.isEmpty()) {
                return AjaxResult.error("content不能为空");
            }

            // 保存AI回复到数据库
            AiChatMessage message = aiChatMessageService.saveAssistantMessage(sessionId, content, messageId);

            log.info("[工作流回调] AI回复已保存 - id: {}, sessionId: {}", message.getId(), sessionId);

            return AjaxResult.success("OK");

        } catch (Exception e) {
            log.error("[工作流回调] 处理失败", e);
            return AjaxResult.error("处理失败: " + e.getMessage());
        }
    }
}
