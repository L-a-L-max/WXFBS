package com.playwright.utils.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.Page;
import com.playwright.websocket.WebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WebSocket日志消息工具类，用于发送不同业务场景的日志数据
 * @author 优立方
 * @version JDK 17
 * @date 2025年05月27日 10:21
 */
@Component
public class  LogMsgUtil {

    @Autowired
    private ScreenshotUtil screenshotUtil;


    // WebSocket客户端服务，用于实际的消息发送
    private final WebSocketClientService webSocketClientService;

    /**
     * 构造函数注入WebSocket客户端服务
     * @param webSocketClientService WebSocket客户端服务实例
     */
    public LogMsgUtil(WebSocketClientService webSocketClientService) {
        this.webSocketClientService = webSocketClientService;
    }

    /**
     * 发送图片数据消息
     * @param page Playwright页面对象
     * @param imageName 图片名称（自动添加.png后缀）
     * @param userId 用户ID
     */
    public void sendImgData(Page page, String imageName, String userId){
        try {
        // 截图并上传到指定存储服务
        String url = screenshotUtil.screenshotAndUpload(page,imageName+".png");

        JSONObject imgData = new JSONObject();
        imgData.put("url",url);
        imgData.put("userId",userId);
        imgData.put("type","RETURN_PC_TASK_IMG");
        webSocketClientService.sendMessage(imgData.toJSONString());
        } catch (Exception e) {
            // 🔥 使用ExceptionLogger记录详细异常
            ExceptionLogger.logScreenshotException(imageName, userId, e);
            // 静默处理，不影响主要业务流程
        }
    }

    /**
     * 发送图片数据消息（带任务ID）
     * @param page Playwright页面对象
     * @param imageName 图片名称（自动添加.png后缀）
     * @param userId 用户ID
     * @param taskId 任务ID
     */
    public void sendImgData(Page page, String imageName, String userId, String taskId){
        try {
        // 截图并上传到指定存储服务
        String url = screenshotUtil.screenshotAndUpload(page,imageName+".png");

        JSONObject imgData = new JSONObject();
        imgData.put("url",url);
        imgData.put("userId",userId);
        imgData.put("taskId",taskId);
        imgData.put("type","RETURN_PC_TASK_IMG");
        webSocketClientService.sendMessage(imgData.toJSONString());
        } catch (Exception e) {
            // 🔥 使用ExceptionLogger记录详细异常
            ExceptionLogger.logScreenshotException(imageName + " (taskId:" + taskId + ")", userId, e);
            // 静默处理，不影响主要业务流程
        }
    }

    /**
     * 发送任务日志消息
     * @param taskNode 任务节点描述信息
     * @param userId 用户ID
     * @param aiName AI服务名称
     */
    public void sendTaskLog(String taskNode,String userId,String aiName){
        sendTaskLog(taskNode, userId, aiName, null);
    }
    
    /**
     * 发送任务日志消息（带taskId）
     * @param taskNode 任务节点描述信息
     * @param userId 用户ID
     * @param aiName AI服务名称
     * @param taskId 任务ID
     */
    public void sendTaskLog(String taskNode,String userId,String aiName,String taskId){

        JSONObject logData = new JSONObject();
        logData.put("content",taskNode);
        logData.put("userId",userId);
        logData.put("type","RETURN_PC_TASK_LOG");
        logData.put("aiName",aiName);
        
        // 🔥 使用统一的消息增强工具
        logData = MessageValidationUtil.enhanceMessage(logData, userId, taskId);
        
        // 记录消息发送日志
        MessageValidationUtil.logMessageSent("RETURN_PC_TASK_LOG", userId, taskId, aiName, taskNode);
        
        webSocketClientService.sendMessage(logData.toJSONString());
    }

    /**
     * 发送结果数据消息
     * @param copiedText 文本内容（如剪贴板内容）
     * @param userId 用户ID
     * @param aiName AI服务名称
     * @param type 消息类型标识
     * @param shareUrl 分享链接
     * @param shareImgUrl 分享图片URL
     */
    public void sendResData(String copiedText,String userId,String aiName,String type,String shareUrl,String shareImgUrl){
        sendResData(copiedText, userId, aiName, type, shareUrl, shareImgUrl, null);
    }

    /**
     * 发送结果数据消息（带taskId）
     * @param copiedText 文本内容（如剪贴板内容）
     * @param userId 用户ID
     * @param aiName AI服务名称
     * @param type 消息类型标识
     * @param shareUrl 分享链接
     * @param shareImgUrl 分享图片URL
     * @param taskId 任务ID
     */
    public void sendResData(String copiedText,String userId,String aiName,String type,String shareUrl,String shareImgUrl,String taskId){

        JSONObject resData = new JSONObject();
        resData.put("draftContent",copiedText);
        resData.put("shareUrl",shareUrl);
        resData.put("shareImgUrl",shareImgUrl);
        resData.put("aiName",aiName);
        resData.put("type", type);
        resData.put("userId",userId);
        
        // 🔥 修复前端错误：添加 aiResponses 字段以兼容前端期望的数据格式
        JSONObject aiResponse = new JSONObject();
        aiResponse.put("content", copiedText);
        aiResponse.put("shareUrl", shareUrl);
        aiResponse.put("shareImgUrl", shareImgUrl);
        aiResponse.put("aiName", aiName);
        
        JSONArray aiResponses = new JSONArray();
        aiResponses.add(aiResponse);
        resData.put("aiResponses", aiResponses);
        
        // 🔥 使用统一的消息增强工具
        resData = MessageValidationUtil.enhanceMessage(resData, userId, taskId);
        
        // 🔥 添加消息唯一ID，防止前端去重或覆盖
        String messageId = System.currentTimeMillis() + "_" + aiName + "_" + userId;
        resData.put("messageId", messageId);
        
        // 🔥 添加AI标识，便于前端识别
        resData.put("aiCode", aiName.toLowerCase().replaceAll("[^a-z0-9]", ""));
        
        // 🔥 只在发送结果消息时输出日志，chatId消息静默
        if (type != null && type.contains("_RES")) {
            String chatId = extractChatIdFromShareUrl(shareUrl);
            MessageValidationUtil.logCompleteAIResult(userId, aiName, copiedText, shareUrl, shareImgUrl, chatId);
        }
        
        webSocketClientService.sendMessage(resData.toJSONString());
    }

    /**
     * 从分享链接中提取会话ID
     */
    private String extractChatIdFromShareUrl(String shareUrl) {
        if (shareUrl == null || shareUrl.isEmpty()) {
            return null;
        }
        try {
            // 提取URL中的ID部分（通常在最后一个/之后）
            String[] parts = shareUrl.split("/");
            if (parts.length > 0) {
                String lastPart = parts[parts.length - 1];
                // 如果包含查询参数，只取ID部分
                if (lastPart.contains("?")) {
                    lastPart = lastPart.split("\\?")[0];
                }
                return lastPart;
            }
        } catch (Exception e) {
            // 静默处理异常
        }
        return null;
    }

    /**
     * 发送聊天数据消息（从页面URL提取参数）
     * @param page Playwright页面对象
     * @param filterCode URL参数匹配正则表达式
     * @param userId 用户ID
     * @param type 消息类型标识
     * @param count 正则匹配组序号（从1开始）
     */
    public void sendChatData(Page page,String filterCode,String userId,String type,int count){
        String currentUrl = page.url();
        Pattern pattern = Pattern.compile(filterCode);
        Matcher matcher = pattern.matcher(currentUrl);
        JSONObject chatData = new JSONObject();
        chatData.put("type", type);
        if (matcher.find()) {
            String param = matcher.group(count);
            chatData.put("chatId", param);
            chatData.put("userId", userId);
            webSocketClientService.sendMessage(chatData.toJSONString());
        }
    }
    
    /**
     * 直接发送聊天ID数据到WebSocket
     * @param chatId 聊天会话ID
     * @param userId 用户ID
     * @param type 消息类型标识  
     */
    public void sendChatDataDirect(String chatId, String userId, String type) {
        if (chatId != null && !chatId.trim().isEmpty()) {
            JSONObject chatData = new JSONObject();
            chatData.put("type", type);
            chatData.put("chatId", chatId);
            chatData.put("userId", userId);
            webSocketClientService.sendMessage(chatData.toJSONString());
        }
    }
    /**
     * 检查是否需要变换AI任务名称，处理评分和排版
     *
     * @param type UserInfoReq中的类型
     * @param defaultValue 没有特殊检查时获得的默认值
     */
    public String checkDynamicAiName(String type, String defaultValue){
        if(type.contains("AI排版")){
            return "智能排版";
        }
        if(type.contains("AI评分")){
            return "智能评分";
        }
        return defaultValue;
    }

    /**
     * 检查是否需要变换AI结果类型，处理评分和排版
     *
     * @param type UserInfoReq中的类型
     * @param defaultValue 没有特殊检查时获得的默认值
     */
    public String checkDynamicType(String type,String defaultValue){
        if(type.contains("AI排版")){
            return "RETURN_ZNPB_RES";
        }
        if(type.contains("AI评分")){
            return "RETURN_WKPF_RES";
        }
        return defaultValue;
    }
    /**
     * 发送投递到媒体任务日志消息
     * @param taskNode 任务节点描述信息
     * @param userId 用户ID
     * @param mediaName 媒体名称
     */
    public void sendMediaTaskLog(String taskNode,String userId,String mediaName){

        JSONObject logData = new JSONObject();
        logData.put("content",taskNode);
        logData.put("userId",userId);
        logData.put("type","RETURN_MEDIA_TASK_LOG");
        logData.put("aiName",mediaName);
        webSocketClientService.sendMessage(logData.toJSONString());
    }

    /**
     * 发送 投递到微头条的流程
     */
    public void sendTTHFlow(String taskNode, String userId){
        JSONObject flowData = new JSONObject();
        flowData.put("content", taskNode);
        flowData.put("type", "RETURN_TTH_FLOW");
        flowData.put("userId", userId);
        webSocketClientService.sendMessage(flowData.toJSONString());
    }

}
