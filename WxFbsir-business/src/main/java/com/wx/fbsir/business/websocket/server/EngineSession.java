package com.wx.fbsir.business.websocket.server;

import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Engine 会话信息
 * 
 * 封装 WebSocket 会话和 Engine 元数据
 * 解决老项目会话管理混乱的问题
 *
 * @author wxfbsir
 * @date 2025-12-15
 */
public class EngineSession {

    /**
     * Engine 唯一标识
     */
    private final String engineId;

    /**
     * WebSocket 会话
     */
    private final WebSocketSession session;

    /**
     * Engine 版本
     */
    private String version;

    /**
     * Engine 能力列表（包含详细信息）
     */
    private List<Map<String, Object>> capabilities;

    /**
     * 设备信息
     */
    private Map<String, Object> deviceInfo;

    /**
     * 连接时间
     */
    private final Instant connectedAt;

    /**
     * 最后活跃时间
     */
    private volatile Instant lastActiveAt;

    /**
     * 最后心跳时间
     */
    private volatile Instant lastHeartbeatAt;

    /**
     * 会话状态
     */
    private volatile SessionStatus status;

    /**
     * 额外属性
     */
    private final Map<String, Object> attributes;

    // ========== 统计信息（存储在内存中，断开时写入数据库） ==========
    
    /**
     * 发送消息数
     */
    private volatile long messageSent = 0;

    /**
     * 接收消息数
     */
    private volatile long messageReceived = 0;

    /**
     * 心跳次数
     */
    private volatile int heartbeatCount = 0;

    /**
     * 错误次数
     */
    private volatile int errorCount = 0;

    /**
     * 最后错误信息
     */
    private volatile String lastError;

    /**
     * 会话状态枚举
     */
    public enum SessionStatus {
        /**
         * 已连接，等待注册
         */
        CONNECTED,
        
        /**
         * 已注册，正常工作
         */
        REGISTERED,
        
        /**
         * 暂停（心跳超时警告）
         */
        SUSPENDED,
        
        /**
         * 已断开
         */
        DISCONNECTED
    }

    public EngineSession(String engineId, WebSocketSession session) {
        this.engineId = engineId;
        this.session = session;
        this.connectedAt = Instant.now();
        this.lastActiveAt = Instant.now();
        this.status = SessionStatus.CONNECTED;
        this.attributes = new ConcurrentHashMap<>();
    }

    /**
     * 更新活跃时间
     */
    public void updateActiveTime() {
        this.lastActiveAt = Instant.now();
    }

    /**
     * 更新心跳时间并增加计数
     */
    public void updateHeartbeatTime() {
        this.lastHeartbeatAt = Instant.now();
        this.lastActiveAt = Instant.now();
        this.heartbeatCount++;
    }

    /**
     * 增加发送消息计数
     */
    public void incrementMessageSent() {
        this.messageSent++;
    }

    /**
     * 增加接收消息计数
     */
    public void incrementMessageReceived() {
        this.messageReceived++;
        this.lastActiveAt = Instant.now();
    }

    /**
     * 记录错误
     */
    public void recordError(String error) {
        this.errorCount++;
        this.lastError = error;
    }

    /**
     * 检查会话是否有效
     */
    public boolean isValid() {
        return session != null && session.isOpen() && status != SessionStatus.DISCONNECTED;
    }

    /**
     * 检查心跳是否超时
     *
     * @param timeoutSeconds 超时秒数
     * @return true 已超时
     */
    public boolean isHeartbeatTimeout(int timeoutSeconds) {
        if (lastHeartbeatAt == null) {
            // 如果从未收到心跳，使用连接时间
            return Instant.now().minusSeconds(timeoutSeconds).isAfter(connectedAt);
        }
        return Instant.now().minusSeconds(timeoutSeconds).isAfter(lastHeartbeatAt);
    }

    /**
     * 获取会话持续时间（秒）
     */
    public long getDurationSeconds() {
        return Instant.now().getEpochSecond() - connectedAt.getEpochSecond();
    }

    /**
     * 获取空闲时间（秒）
     */
    public long getIdleSeconds() {
        return Instant.now().getEpochSecond() - lastActiveAt.getEpochSecond();
    }

    /**
     * 设置属性
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * 获取属性
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    // ========== Getter/Setter ==========

    public String getEngineId() {
        return engineId;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public String getSessionId() {
        return session != null ? session.getId() : null;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Map<String, Object>> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Map<String, Object>> capabilities) {
        this.capabilities = capabilities;
    }

    /**
     * 检查是否具有指定能力
     */
    public boolean hasCapability(String type) {
        if (capabilities == null || capabilities.isEmpty()) {
            return false;
        }
        return capabilities.stream()
            .anyMatch(cap -> type.equals(cap.get("type")));
    }

    public Map<String, Object> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(Map<String, Object> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public Instant getConnectedAt() {
        return connectedAt;
    }

    public Instant getLastActiveAt() {
        return lastActiveAt;
    }

    public Instant getLastHeartbeatAt() {
        return lastHeartbeatAt;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public long getMessageSent() {
        return messageSent;
    }

    public long getMessageReceived() {
        return messageReceived;
    }

    public int getHeartbeatCount() {
        return heartbeatCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public String getLastError() {
        return lastError;
    }

    @Override
    public String toString() {
        return "EngineSession{" +
                "engineId='" + engineId + '\'' +
                ", sessionId='" + getSessionId() + '\'' +
                ", status=" + status +
                ", version='" + version + '\'' +
                '}';
    }
}
