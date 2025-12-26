package com.wx.fbsir.engine.controller.base;

import com.wx.fbsir.engine.websocket.client.WebSocketClientManager;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import com.wx.fbsir.engine.websocket.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 流式任务控制器基类
 * 
 * 提供通用的流式任务处理能力：
 * 1. 自动生成和传递requestId
 * 2. 可配置的进度推送间隔
 * 3. 统一的消息发送方法
 * 4. 优雅的错误处理
 * 5. 资源自动清理
 * 
 * 使用方式：
 * <pre>
 * @Controller
 * public class MyController extends BaseStreamController {
 *     
 *     public void handleMyTask(EngineMessage message) {
 *         // 1. 提取参数
 *         String userId = message.getUserId();
 *         String requestId = extractRequestId(message);
 *         
 *         // 2. 开始流式任务（每5秒推送一次进度）
 *         StreamTask task = startStreamTask(userId, requestId, 5000);
 *         
 *         try {
 *             // 3. 执行业务逻辑
 *             task.sendProgress("步骤1完成", 1, 3);
 *             // ... 业务代码 ...
 *             task.sendProgress("步骤2完成", 2, 3);
 *             // ... 业务代码 ...
 *             
 *             // 4. 发送最终结果
 *             task.sendSuccess("任务完成", resultData);
 *             
 *         } catch (Exception e) {
 *             task.sendError("任务失败: " + e.getMessage());
 *         } finally {
 *             task.stop(); // 停止定时任务
 *         }
 *     }
 * }
 * </pre>
 *
 * @author wxfbsir
 * @date 2025-12-23
 */
public abstract class BaseStreamController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * WebSocket客户端管理器（延迟注入避免循环依赖）
     */
    @Autowired
    @Lazy
    protected WebSocketClientManager webSocketClientManager;

    /**
     * 从消息中提取requestId
     * 
     * @param message 消息对象
     * @return requestId，如果不存在则返回null
     */
    protected String extractRequestId(EngineMessage message) {
        return message.getPayloadValue("requestId");
    }

    /**
     * 🔥 标准化payload提取方法
     * 
     * 统一从EngineMessage中提取业务payload对象
     * 
     * 消息结构说明：
     * Business层转发的消息结构为：
     * {
     *   "type": "CAPABILITY_TYPE",
     *   "userId": "1",
     *   "payload": {
     *     "requestId": "xxx",
     *     "userId": "1",
     *     "sourceType": "WEBSOCKET",
     *     "payload": {          // <-- 这是业务真正的payload
     *       "query": "xxx",
     *       "param1": "value1",
     *       "param2": true
     *     }
     *   }
     * }
     * 
     * 本方法提取内层的业务payload对象，返回JSONObject供能力方法解析
     * 
     * @param message Engine消息对象
     * @return 业务payload的JSONObject，如果不存在或类型错误则返回null
     */
    protected com.alibaba.fastjson2.JSONObject extractPayload(EngineMessage message) {
        try {
            log.info("[Payload解析] 开始提取 - message.getPayload()所有key: {}", message.getPayload().keySet());
            Object payloadObj = message.getPayloadValue("payload");
            log.info("[Payload解析] 提取'payload'字段 - 值类型: {}, 值: {}", 
                payloadObj != null ? payloadObj.getClass().getName() : "null",
                payloadObj);
            
            if (payloadObj instanceof com.alibaba.fastjson2.JSONObject) {
                com.alibaba.fastjson2.JSONObject result = (com.alibaba.fastjson2.JSONObject) payloadObj;
                log.info("[Payload解析] ✅ 提取成功 - 包含字段: {}", result.keySet());
                log.info("[Payload解析] ✅ 详细内容: {}", result.toJSONString());
                return result;
            } else {
                log.warn("[Payload解析] payload不是JSONObject - 类型: {}", 
                    payloadObj != null ? payloadObj.getClass().getName() : "null");
                return null;
            }
        } catch (Exception e) {
            log.error("[Payload解析] 提取失败", e);
            return null;
        }
    }

    /**
     * 从payload中安全提取String字段
     * 
     * @param payload JSONObject
     * @param key 字段名
     * @return 字段值，不存在则返回null
     */
    protected String getStringFromPayload(com.alibaba.fastjson2.JSONObject payload, String key) {
        if (payload == null) {
            log.warn("[Payload解析] payload为null，无法提取字段: {}", key);
            return null;
        }
        String value = payload.getString(key);
        log.info("[Payload解析] 提取字段 '{}' - 值: {}", key, value);
        return value;
    }

    /**
     * 从payload中安全提取Boolean字段（支持Boolean和String类型）
     * 
     * @param payload JSONObject
     * @param key 字段名
     * @param defaultValue 默认值
     * @return 布尔值
     */
    protected boolean getBooleanFromPayload(com.alibaba.fastjson2.JSONObject payload, String key, boolean defaultValue) {
        if (payload == null) {
            log.warn("[Payload解析] payload为null，返回默认值: {}", defaultValue);
            return defaultValue;
        }
        
        Object value = payload.get(key);
        log.info("[Payload解析] 提取字段 '{}' - 值: {}, 类型: {}", 
            key, value, value != null ? value.getClass().getSimpleName() : "null");
        
        if (value instanceof Boolean) {
            log.info("[Payload解析] '{}' 解析为Boolean: {}", key, value);
            return (Boolean) value;
        } else if (value instanceof String) {
            boolean result = Boolean.parseBoolean((String) value);
            log.info("[Payload解析] '{}' 从String解析为Boolean: {}", key, result);
            return result;
        }
        
        log.warn("[Payload解析] '{}' 无法解析为Boolean，返回默认值: {}", key, defaultValue);
        return defaultValue;
    }

    /**
     * 从payload中安全提取Integer字段
     * 
     * @param payload JSONObject
     * @param key 字段名
     * @param defaultValue 默认值
     * @return 整数值
     */
    protected int getIntFromPayload(com.alibaba.fastjson2.JSONObject payload, String key, int defaultValue) {
        if (payload == null) {
            return defaultValue;
        }
        
        Integer value = payload.getInteger(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 开始流式任务（使用默认间隔5秒）
     * 
     * @param userId 用户ID
     * @param requestId 请求ID
     * @return 流式任务对象
     */
    protected StreamTask startStreamTask(String userId, String requestId) {
        return startStreamTask(userId, requestId, 5000);
    }

    /**
     * 开始流式任务（自定义推送间隔）
     * 
     * @param userId 用户ID
     * @param requestId 请求ID
     * @param intervalMillis 进度推送间隔（毫秒）
     * @return 流式任务对象
     */
    protected StreamTask startStreamTask(String userId, String requestId, long intervalMillis) {
        return new StreamTask(userId, requestId, intervalMillis);
    }

    /**
     * 发送单次进度通知（无需创建StreamTask）
     * 
     * @param userId 用户ID
     * @param requestId 请求ID
     * @param message 进度消息
     * @param current 当前步骤
     * @param total 总步骤数
     */
    protected void sendProgress(String userId, String requestId, String message, int current, int total) {
        if (!isConnected()) {
            return;
        }

        EngineMessage progressMsg = EngineMessage.builder()
            .type(MessageType.TASK_PROGRESS.getCode())
            .userId(userId)
            .payload("requestId", requestId)
            .payload("message", message)
            .payload("current", current)
            .payload("total", total)
            .payload("timestamp", System.currentTimeMillis())
            .build();

        webSocketClientManager.sendMessage(progressMsg);
    }

    /**
     * 发送成功结果
     * 
     * @param userId 用户ID
     * @param requestId 请求ID
     * @param message 结果消息
     * @param data 结果数据
     */
    protected void sendSuccess(String userId, String requestId, String message, Object data) {
        if (!isConnected()) {
            return;
        }

        EngineMessage.Builder builder = EngineMessage.builder()
            .type(MessageType.TASK_RESULT.getCode())
            .userId(userId)
            .payload("requestId", requestId)
            .payload("success", true)
            .payload("message", message)
            .payload("timestamp", System.currentTimeMillis());

        if (data != null) {
            builder.payload("data", data);
        }

        webSocketClientManager.sendMessage(builder.build());
    }

    /**
     * 发送错误结果
     * 
     * @param userId 用户ID
     * @param requestId 请求ID
     * @param errorMessage 错误消息
     */
    protected void sendError(String userId, String requestId, String errorMessage) {
        if (!isConnected()) {
            return;
        }

        EngineMessage errorMsg = EngineMessage.builder()
            .type(MessageType.TASK_RESULT.getCode())
            .userId(userId)
            .payload("requestId", requestId)
            .payload("success", false)
            .payload("errorCode", "TASK_ERROR")
            .payload("errorMessage", errorMessage)
            .payload("timestamp", System.currentTimeMillis())
            .build();

        webSocketClientManager.sendMessage(errorMsg);
    }

    /**
     * 检查WebSocket是否已连接
     */
    protected boolean isConnected() {
        return webSocketClientManager != null && webSocketClientManager.isConnected();
    }

    /**
     * 流式任务包装类
     * 
     * 提供自动化的进度推送和消息发送功能
     */
    public class StreamTask {
        private final String userId;
        private final String requestId;
        private final long intervalMillis;
        private final AtomicInteger progressCount = new AtomicInteger(0);
        private final AtomicBoolean stopped = new AtomicBoolean(false);
        private ScheduledExecutorService scheduler;
        private ScheduledFuture<?> progressFuture;

        /**
         * 构造函数
         * 
         * @param userId 用户ID
         * @param requestId 请求ID（全链路唯一标识）
         * @param intervalMillis 进度推送间隔（毫秒）
         */
        public StreamTask(String userId, String requestId, long intervalMillis) {
            this.userId = userId;
            this.requestId = requestId;
            this.intervalMillis = intervalMillis;
        }

        /**
         * 启动定时进度推送
         * 
         * @param progressMessage 进度消息生成器（参数：当前计数）
         */
        public void startAutoProgress(java.util.function.Function<Integer, String> progressMessage) {
            if (scheduler != null) {
                log.warn("[StreamTask] 定时任务已启动，请勿重复启动 - 用户: {}, 请求ID: {}", userId, requestId);
                return;
            }

            scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread thread = new Thread(r);
                thread.setName("StreamTask-" + userId + "-" + System.currentTimeMillis());
                thread.setDaemon(true);
                return thread;
            });

            progressFuture = scheduler.scheduleAtFixedRate(() -> {
                if (stopped.get()) {
                    return;
                }

                try {
                    int count = progressCount.incrementAndGet();
                    String message = progressMessage.apply(count);
                    sendProgress(message);
                } catch (Exception e) {
                    log.error("[StreamTask] 自动进度推送失败 - 用户: {}, 请求ID: {}, 错误: {}", 
                        userId, requestId, e.getMessage());
                }
            }, intervalMillis, intervalMillis, TimeUnit.MILLISECONDS);

            log.debug("[StreamTask] 已启动自动进度推送 - 用户: {}, 请求ID: {}, 间隔: {}ms", 
                userId, requestId, intervalMillis);
        }


        /**
         * 发送进度通知
         * 
         * @param message 进度消息
         */
        public void sendProgress(String message) {
            BaseStreamController.this.sendProgress(userId, requestId, message, 0, 0);
        }

        /**
         * 发送进度通知（带进度百分比）
         * 
         * @param message 进度消息
         * @param current 当前步骤
         * @param total 总步骤数
         */
        public void sendProgress(String message, int current, int total) {
            BaseStreamController.this.sendProgress(userId, requestId, message, current, total);
        }

        /**
         * 发送文本日志消息（参考老项目 logInfo.sendTaskLog）
         * 
         * 用于显示执行进度文本，如"页面加载完成"、"二维码加载中"等
         * 前端会将这些日志添加到 progressLogs 数组中显示
         * 
         * @param message 日志消息内容
         */
        public void sendLog(String message) {
            if (!isConnected()) {
                return;
            }

            EngineMessage.Builder builder = EngineMessage.builder()
                .type(MessageType.TASK_LOG.getCode())
                .userId(userId)
                .payload("requestId", requestId)
                .payload("message", message)
                .payload("timestamp", System.currentTimeMillis());

            BaseStreamController.this.webSocketClientManager.sendMessage(builder.build());
            log.debug("[StreamTask] 发送日志 - 用户: {}, 消息: {}", userId, message);
        }

        /**
         * 发送截图消息
         * 
         * 用于发送截图URL，前端会将截图添加到 screenshots 数组中轮播显示
         * 
         * @param screenshotUrl 截图URL
         */
        public void sendScreenshot(String screenshotUrl) {
            if (!isConnected() || screenshotUrl == null || screenshotUrl.isEmpty()) {
                return;
            }

            EngineMessage.Builder builder = EngineMessage.builder()
                .type(MessageType.TASK_SCREENSHOT.getCode())
                .userId(userId)
                .payload("requestId", requestId)
                .payload("screenshotUrl", screenshotUrl)
                .payload("timestamp", System.currentTimeMillis());

            BaseStreamController.this.webSocketClientManager.sendMessage(builder.build());
            log.debug("[StreamTask] 发送截图 - 用户: {}, URL: {}", userId, screenshotUrl);
        }

        /**
         * 发送进度通知（带额外数据）
         * 
         * @param message 进度消息
         * @param extraData 额外数据
         */
        public void sendProgress(String message, java.util.Map<String, Object> extraData) {
            if (!isConnected()) {
                return;
            }

            EngineMessage.Builder builder = EngineMessage.builder()
                .type(MessageType.TASK_PROGRESS.getCode())
                .userId(userId)
                .payload("requestId", requestId)
                .payload("message", message)
                .payload("timestamp", System.currentTimeMillis());

            if (extraData != null) {
                extraData.forEach(builder::payload);
            }

            BaseStreamController.this.webSocketClientManager.sendMessage(builder.build());
        }

        /**
         * 发送成功结果
         * 
         * @param message 结果消息
         * @param data 结果数据
         */
        public void sendSuccess(String message, Object data) {
            stop(); // 自动停止定时任务
            BaseStreamController.this.sendSuccess(userId, requestId, message, data);
        }

        /**
         * 发送错误结果
         * 
         * @param errorMessage 错误消息
         */
        public void sendError(String errorMessage) {
            stop(); // 自动停止定时任务
            BaseStreamController.this.sendError(userId, requestId, errorMessage);
        }

        /**
         * 停止定时任务
         * 🔴 P0修复：确保异常时也能正确关闭线程池
         */
        public void stop() {
            if (stopped.getAndSet(true)) {
                return; // 已经停止
            }

            try {
                if (progressFuture != null) {
                    progressFuture.cancel(false);
                }
            } finally {
                // 🔴 P0修复：确保任何情况下都关闭线程池
                if (scheduler != null) {
                    scheduler.shutdown();
                    try {
                        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                            scheduler.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        scheduler.shutdownNow();
                        Thread.currentThread().interrupt();
                    }
                }
            }

            log.debug("[StreamTask] 已停止 - 用户: {}, 请求ID: {}", userId, requestId);
        }

        /**
         * 获取请求ID
         */
        public String getRequestId() {
            return requestId;
        }

        /**
         * 获取用户ID
         */
        public String getUserId() {
            return userId;
        }
    }
}
