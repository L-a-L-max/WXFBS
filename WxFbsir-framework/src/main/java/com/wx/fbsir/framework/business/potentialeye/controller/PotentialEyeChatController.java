package com.wx.fbsir.framework.business.potentialeye.controller;

import cn.hutool.core.util.ObjectUtil;
import com.wx.fbsir.business.point.service.IPointsService;
import com.wx.fbsir.business.potentialeye.domain.ChatFile;
import com.wx.fbsir.business.potentialeye.domain.ChatMessage;
import com.wx.fbsir.business.potentialeye.service.IChatFileService;
import com.wx.fbsir.business.potentialeye.service.IChatMessageService;
import com.wx.fbsir.common.annotation.Anonymous;
import com.wx.fbsir.common.config.WxFbsirConfig;
import com.wx.fbsir.common.core.domain.AjaxResult;
import com.wx.fbsir.common.core.domain.model.LoginUser;
import com.wx.fbsir.common.core.redis.RedisCache;
import com.wx.fbsir.common.utils.file.FileUploadUtils;
import com.wx.fbsir.framework.business.potentialeye.utils.PotentialEyeChatUtil;
import com.wx.fbsir.framework.web.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class PotentialEyeChatController {

    private static final Logger log = LoggerFactory.getLogger(PotentialEyeChatController.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IPointsService pointsService;

    @Autowired
    private IChatFileService chatFileService;

    @Autowired
    private PotentialEyeChatUtil potentialEyeChatUtil;

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 发送消息到元器AI
     */
    @PostMapping("/send")
    public AjaxResult send(HttpServletRequest request,
                           @RequestParam(value = "message", required = true) String message,
                           @RequestParam(value = "sessionId", required = false) String sessionId,
                           @RequestParam(value = "files", required = false) MultipartFile[] files) {

        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return AjaxResult.error("未登录");
        }

        if (isMessageInvalid(message)) {
            log.warn("[潜力眼对话] 消息为空 - username: {}", loginUser.getUsername());
            return AjaxResult.error("消息不能为空");
        }

        try {
            List<ChatFile> chatFileList = handleFiles(files, loginUser);
            if (chatFileList == null) {
                return AjaxResult.error("文件处理失败");
            }

            int requiredPoints = calculateRequiredPoints(chatFileList);
            if (!hasSufficientPoints(loginUser.getUserId(), requiredPoints)) {
                return AjaxResult.error("您的积分不足");
            }

            asyncSaveMessage(chatFileList, sessionId, message);
            String reply = sendToPotentialEye(message, sessionId, chatFileList);
            deductPoints(loginUser.getUserId(), requiredPoints);

            return buildSuccessResponse(reply);

        } catch (Exception e) {
            log.error("[潜力眼对话] 发送消息失败 - sessionId: {}, username: {}", sessionId, loginUser.getUsername(), e);
            return AjaxResult.error("发送失败: " + e.getMessage());
        }
    }

    /**
     * 验证消息是否有效
     */
    private boolean isMessageInvalid(String message) {
        return message == null || message.trim().isEmpty();
    }

    /**
     * 处理文件上传或获取缓存文件
     */
    private List<ChatFile> handleFiles(MultipartFile[] files, LoginUser loginUser) {
        if (ObjectUtil.isEmpty(files)) {
            return getCachedChatFiles();
        } else {
            return uploadFiles(files);
        }
    }

    /**
     * 获取缓存的聊天文件
     */
    private List<ChatFile> getCachedChatFiles() {
        String messageId = redisCache.getCacheObject("messageId");
        if (ObjectUtil.isEmpty(messageId)) {
            return null;
        }

        ChatFile query = new ChatFile();
        query.setMessageId(messageId);
        return chatFileService.selectChatFileList(query);
    }

    private List<ChatFile> uploadFiles(MultipartFile[] files) {
        try {
            // 使用WxFbsirConfig配置的基础路径
            String baseDir = WxFbsirConfig.getProfile();
            return uploadFilesToChatFileList(files, baseDir, "files");
        } catch (Exception e) {
            log.error("[潜力眼对话] 文件上传失败", e);
            return new ArrayList<>(); // 返回空列表而不是null
        }
    }

    public List<ChatFile> uploadFilesToChatFileList(MultipartFile[] files, String baseDir, String relativePath) {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("文件列表不能为空");
        }
        List<ChatFile> chatFileList = new ArrayList<>();
        List<String> uploadedFilePaths = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }
                String uuidFilename = FileUploadUtils.uuidFilename(file);
                String fullUploadDir = baseDir + File.separator + relativePath;
                String relativeFilePath = uuidFilename;
                // 确保目录存在
                File targetDir = new File(fullUploadDir);
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }
                // 创建目标文件
                File targetFile = new File(fullUploadDir, uuidFilename);
                // 确保父目录存在
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                }
                // 保存文件
                file.transferTo(targetFile);
                String absoluteFilePath = targetFile.getAbsolutePath();
                // 构建访问URL
                String domain = WxFbsirConfig.getDomain();
                if (domain.endsWith("/")) {
                    domain = domain.substring(0, domain.length() - 1);
                }
                String fileUrl = domain + "/" + relativePath + "/" + uuidFilename;
                fileUrl = fileUrl.replace("//", "/").replace(":/", "://");
                ChatFile chatFile = new ChatFile();
                chatFile.setFileName(file.getOriginalFilename());
                chatFile.setFilePath(absoluteFilePath);
                chatFile.setFileUrl(fileUrl);
                chatFileList.add(chatFile);
                uploadedFilePaths.add(absoluteFilePath);
                log.info("文件上传成功: {}", absoluteFilePath);
            }

            return chatFileList;
        } catch (Exception e) {
            // 回滚：删除已上传的文件
            deleteUploadedFiles(uploadedFilePaths);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除已上传的文件（错误回滚）[6](@ref)
     */
    private void deleteUploadedFiles(List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            return;
        }

        for (String filePath : filePaths) {
            try {
                // 将相对路径转换为绝对路径
                String absolutePath = FileUploadUtils.getDefaultBaseDir() + File.separator + filePath;
                File file = new File(absolutePath);
                if (file.exists()) {
                    if (file.delete()) {
                        log.info("已删除上传失败的文件: {}", absolutePath);
                    } else {
                        log.warn("文件删除失败: {}", absolutePath);
                    }
                }
            } catch (Exception ex) {
                log.error("删除文件时发生错误: {}", filePath, ex);
            }
        }
    }


//    /**
//     * 上传文件到OSS
//     */
//    private List<ChatFile> uploadFiles(MultipartFile[] files) {
//        try {
//            return aliyunOss.uploadFilesToChatFileList(files, "files");
//        } catch (Exception e) {
//            log.error("[潜力眼对话] 文件上传失败", e);
//            return null;
//        }
//    }

    /**
     * 计算所需积分
     */
    private int calculateRequiredPoints(List<ChatFile> chatFileList) {
        return chatFileList.size() * 20;
    }

    /**
     * 检查用户积分是否充足
     */
    private boolean hasSufficientPoints(Long userId, int requiredPoints) {
        Integer userPoints = pointsService.getUserPoints(userId);
        return userPoints >= requiredPoints;
    }

    /**
     * 异步保存消息到数据库
     */
    private void asyncSaveMessage(List<ChatFile> chatFileList, String sessionId, String message) {
        CompletableFuture.runAsync(() -> {
            try {
                log.info("[AI对话] 开始保存用户消息到数据库...");
                insertData(chatFileList, sessionId, message);
                log.info("[AI对话] 用户消息已保存到数据库");
            } catch (Exception e) {
                log.error("[AI对话] 保存用户消息失败", e);
            }
        });
    }

    /**
     * 发送消息到PotentialEye服务
     */
    private String sendToPotentialEye(String message, String sessionId, List<ChatFile> chatFileList) {
        List<String> fileUrls = chatFileList.stream()
                .map(ChatFile::getFileUrl)
                .collect(Collectors.toList());

        log.info("[AI对话] 开始转发消息到元器...");
        String reply = potentialEyeChatUtil.sendMessage(message, sessionId, fileUrls);
        log.info("[AI对话] 元器请求完成，返回: {}", reply);

        return reply;
    }

    /**
     * 扣除用户积分
     */
    private void deductPoints(Long userId, int points) {
        pointsService.changePoints(userId, "POTENTIAL_EYE_ANALYZE", -points);
    }

    /**
     * 构建成功响应
     */
    private AjaxResult buildSuccessResponse(String reply) {
        return AjaxResult.success(Map.of(
                "reply", reply,
                "timestamp", System.currentTimeMillis()
        ));
    }

    @Transactional
    public void insertData(List<ChatFile> chatFileList,String sessionId, String message) {
        // 将用户发送的消息和文件链接持久化
        String messageId = UUID.randomUUID().toString();
        chatFileList.forEach(file -> {
            ChatFile chatFile = new ChatFile();
            chatFile.setFileName(file.getFileName());
            chatFile.setFilePath(file.getFilePath());
            chatFile.setFileUrl(file.getFileUrl());
            chatFile.setMessageId(messageId);
            chatFileService.insertChatFile(chatFile);
        });
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSessionId(sessionId);
        chatMessage.setMessageId(messageId);
        chatMessage.setContent(message);
        chatMessage.setRole("user");
        chatMessage.setPointCost(chatFileList.size()*20);
        chatMessage.setIsRead(1);
        chatMessage.setCreatedTime(new Date());
        chatMessageService.insertChatMessage(chatMessage);
        redisCache.setCacheObject("messageId",messageId);
    }


    /**
     * 轮询获取AI回复（从数据库读取未读消息）
     */
    @Anonymous
    @GetMapping("/poll")
    public AjaxResult pollReply(@RequestParam("sessionId") String sessionId) {
        try {
            log.debug("[AI对话] 轮询用户消息 - sessionId: {}",sessionId);

            // 从数据库获取未读的AI回复
            List<ChatMessage> unreadMessages = potentialEyeChatUtil.getUnreadMessages(sessionId);

            if (unreadMessages != null && !unreadMessages.isEmpty()) {
                // 拼接所有未读消息
                // 使用StringBuilder高效拼接消息内容[6,7](@ref)
                StringBuilder replyBuilder = new StringBuilder();
                for (ChatMessage message : unreadMessages) {
                    if (replyBuilder.length() > 0) {
                        replyBuilder.append("\n");
                    }
                    replyBuilder.append(message.getContent());
                }
                String reply = replyBuilder.toString();
                log.debug("[AI对话] 生成回复: {}", reply);
                // 标记为已读（只标记处理成功的消息）
                List<Long> chatIdList = unreadMessages.stream()
                        .map(ChatMessage::getChatId)
                        .collect(Collectors.toList());
                potentialEyeChatUtil.markAsRead(sessionId, chatIdList);
                return AjaxResult.success(Map.of(
                        "hasReply", true,
                        "reply", reply
                ));
            } else {
                return AjaxResult.success(Map.of("hasReply", false));
            }
        } catch (Exception e) {
            log.error("[AI对话] 轮询失败 - sessionId: {}", sessionId, e);
            return AjaxResult.error("轮询失败: " + e.getMessage());
        }
    }


    /**
     * 工作流回调接口 - 接收元器工作流发送的AI回复（JSON格式）
     */
    @Anonymous
    @PostMapping("/workflow/callback")
    public AjaxResult handleWorkflowCallback(@RequestBody Map<String, Object> request) {
        try {
            String sessionId = (String) request.get("sessionId");
            String messageId = (String) request.get("messageId");
            String content = (String) request.get("result");
            log.info("[工作流回调] 收到AI回复 - sessionId: {}, 内容长度: {}", sessionId, content != null ? content.length() : 0);
            if (sessionId == null || sessionId.isEmpty()) {
                return AjaxResult.error("sessionId不能为空");
            }
            if (content == null || content.isEmpty()) {
                return AjaxResult.error("content不能为空");
            }
            potentialEyeChatUtil.saveAssistantMessage(sessionId,content,messageId);
            log.info("[工作流回调] AI回复已保存 - sessionId: {}", sessionId);
            return AjaxResult.success("OK");

        } catch (Exception e) {
            log.error("[工作流回调] 处理失败", e);
            return AjaxResult.error("处理失败: " + e.getMessage());
        }
    }

}
