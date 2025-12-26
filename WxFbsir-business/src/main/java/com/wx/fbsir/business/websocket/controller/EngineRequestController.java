package com.wx.fbsir.business.websocket.controller;

import com.wx.fbsir.business.websocket.message.EngineMessage;
import com.wx.fbsir.business.websocket.server.EngineSession;
import com.wx.fbsir.business.websocket.server.EngineSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Engine请求控制器
 * 
 * 提供通用的HTTP接口，用于调用Engine能力并等待单次返回结果
 * 
 * 使用场景：
 * - 网页端需要获取Engine的执行结果（非流式）
 * - 例如：健康检查、任务状态查询、配置获取等
 * 
 * 工作流程：
 * 1. 接收HTTP请求，生成requestId
 * 2. 将请求通过WebSocket转发给Engine
 * 3. 等待Engine返回结果（阻塞等待，有超时）
 * 4. 将结果返回给HTTP请求方
 * 
 * 说明：
 * - 适用于单次返回的请求（once类型），不适用于流式返回（stream类型）
 * - 流式返回请仍需使用WebSocket直接连接
 *
 * @author wxfbsir
 * @date 2025-12-23
 */
@RestController
@RequestMapping("/ws/engine")
public class EngineRequestController {

    private static final Logger log = LoggerFactory.getLogger(EngineRequestController.class);

    private final EngineSessionManager sessionManager;
    
    /**
     * 请求ID -> CompletableFuture 映射
     * 用于阻塞等待Engine返回结果
     */
    private final Map<String, CompletableFuture<Map<String, Object>>> pendingRequests = new ConcurrentHashMap<>();
    
    /**
     * 默认超时时间（秒）
     */
    private static final int DEFAULT_TIMEOUT = 30;

    public EngineRequestController(EngineSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * 通用Engine请求接口
     * 
     * 请求示例：
     * POST /ws/engine/request
     * {
     *   "engineId": "engine-001",      // 必填：目标Engine ID
     *   "type": "HEALTH_CHECK",         // 必填：消息类型
     *   "userId": "user-001",           // 可选：用户ID
     *   "timeout": 30,                  // 可选：超时时间（秒），默认30秒
     *   "payload": {                    // 可选：业务数据
     *     "key1": "value1"
     *   }
     * }
     * 
     * 响应示例：
     * {
     *   "success": true,
     *   "requestId": "req-12345678",
     *   "data": {...},                  // Engine返回的数据
     *   "executionTime": 1234           // 执行耗时（毫秒）
     * }
     * 
     * @param requestData 请求数据
     * @return 响应结果
     */
    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> sendRequest(@RequestBody Map<String, Object> requestData) {
        long startTime = System.currentTimeMillis();
        
        // 1. 提取必填参数
        String engineId = (String) requestData.get("engineId");
        String type = (String) requestData.get("type");
        
        if (engineId == null || engineId.isEmpty()) {
            return ResponseEntity.ok(buildError("INVALID_PARAM", "缺少 engineId 参数"));
        }
        if (type == null || type.isEmpty()) {
            return ResponseEntity.ok(buildError("INVALID_PARAM", "缺少 type 参数"));
        }
        
        // 2. 检查Engine是否在线
        EngineSession session = sessionManager.getSessionByEngineId(engineId);
        if (session == null || session.getStatus() != EngineSession.SessionStatus.REGISTERED) {
            return ResponseEntity.ok(buildError("ENGINE_OFFLINE", 
                "Engine [" + engineId + "] 不在线或未注册"));
        }
        
        // 3. 检查Engine是否具有该能力
        if (!session.hasCapability(type)) {
            return ResponseEntity.ok(buildError("CAPABILITY_NOT_FOUND", 
                "Engine [" + engineId + "] 没有 [" + type + "] 能力"));
        }
        
        // 4. 提取可选参数
        String userId = (String) requestData.getOrDefault("userId", "system");
        
        // 5. 生成requestId（格式：userId_timestamp_messageType_sequence）
        String requestId = com.wx.fbsir.business.websocket.util.RequestIdGenerator.generate(userId, type);
        
        // 6. 获取超时时间
        int timeout = requestData.containsKey("timeout") ? 
            ((Number) requestData.get("timeout")).intValue() : DEFAULT_TIMEOUT;
        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) requestData.getOrDefault("payload", new HashMap<>());
        
        // 7. 构建EngineMessage
        EngineMessage.Builder builder = EngineMessage.builder()
            .type(type)
            .engineId(engineId)
            .userId(userId);
        
        // 添加 requestId 到 payload
        payload.put("requestId", requestId);
        
        // 🔴 关键修复：标记请求来源为 HTTP
        payload.put("sourceType", "HTTP");
        
        // 添加所有 payload 字段
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            builder.payload(entry.getKey(), entry.getValue());
        }
        
        EngineMessage message = builder.build();
        
        // 8. 创建 CompletableFuture，用于等待结果
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);
        
        // 9. 发送消息给Engine
        boolean sent = sessionManager.sendMessage(engineId, message);
        if (!sent) {
            pendingRequests.remove(requestId);
            return ResponseEntity.ok(buildError("SEND_FAILED", "消息发送失败"));
        }
        
        log.info("[Engine请求] 已发送 - Engine: {}, 类型: {}, 请求ID: {}", engineId, type, requestId);
        
        // 10. 等待Engine返回结果（阻塞等待）
        try {
            Map<String, Object> result = future.get(timeout, TimeUnit.SECONDS);
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.info("[Engine请求] 完成 - 请求ID: {}, 耗时: {}ms", requestId, executionTime);
            
            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("requestId", requestId);
            response.put("data", result);
            response.put("executionTime", executionTime);
            
            return ResponseEntity.ok(response);
            
        } catch (java.util.concurrent.TimeoutException e) {
            pendingRequests.remove(requestId);
            log.warn("[Engine请求] 超时 - 请求ID: {}, 超时时间: {}秒", requestId, timeout);
            return ResponseEntity.ok(buildError("TIMEOUT", 
                "请求超时（" + timeout + "秒），Engine可能处理时间过长或未响应"));
            
        } catch (Exception e) {
            pendingRequests.remove(requestId);
            log.error("[Engine请求] 异常 - 请求ID: {}, 错误: {}", requestId, e.getMessage(), e);
            return ResponseEntity.ok(buildError("EXECUTION_ERROR", "请求执行异常: " + e.getMessage()));
        }
    }

    /**
     * 接收Engine返回的结果
     * 
     * 该方法由 EngineWebSocketHandler 或消息路由器调用
     * 当Engine返回结果时，完成对应的 CompletableFuture
     * 
     * @param requestId 请求ID
     * @param result    结果数据
     */
    public void completeRequest(String requestId, Map<String, Object> result) {
        CompletableFuture<Map<String, Object>> future = pendingRequests.remove(requestId);
        if (future != null) {
            future.complete(result);
            log.debug("[Engine请求] 收到结果 - 请求ID: {}", requestId);
        } else {
            log.warn("[Engine请求] 收到未知请求的结果 - 请求ID: {}", requestId);
        }
    }

    /**
     * 获取Engine列表（快捷方法）
     * 
     * GET /ws/engine/list
     * 
     * @return Engine列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listEngines() {
        List<EngineSession> sessions = sessionManager.getRegisteredSessions();
        
        List<Map<String, Object>> engineList = new ArrayList<>();
        for (EngineSession session : sessions) {
            Map<String, Object> info = new HashMap<>();
            info.put("engineId", session.getEngineId());
            info.put("version", session.getVersion());
            info.put("status", session.getStatus().name());
            
            // 能力列表（只返回type）
            if (session.getCapabilities() != null) {
                List<String> capabilities = new ArrayList<>();
                for (Map<String, Object> cap : session.getCapabilities()) {
                    capabilities.add((String) cap.get("type"));
                }
                info.put("capabilities", capabilities);
            }
            
            engineList.add(info);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("total", engineList.size());
        result.put("engines", engineList);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 构建错误响应
     */
    private Map<String, Object> buildError(String errorCode, String errorMessage) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("errorCode", errorCode);
        error.put("errorMessage", errorMessage);
        error.put("timestamp", System.currentTimeMillis());
        return error;
    }
}
