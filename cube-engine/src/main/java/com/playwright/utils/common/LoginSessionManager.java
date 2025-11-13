package com.playwright.utils.common;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 登录会话管理器，解决扫码登录截图传台问题
 * 
 * 🔥 重要说明：
 * 1. 本管理器只管理【登录检测窗口】，不影响【AI咨询窗口】
 * 2. 只清理当前用户的登录会话，不影响其他用户
 * 3. 登录会话通过 startLoginSession() 方法注册
 * 4. AI咨询窗口使用不同的浏览器上下文，不在此管理范围内
 * 
 * @author 优立方
 * @version JDK 17
 * @date 2025年11月13日
 */
@Component
public class LoginSessionManager {
    
    // 🔥 只存储登录检测会话，不包含AI咨询会话
    private final ConcurrentHashMap<String, LoginSession> activeSessions = new ConcurrentHashMap<>();
    
    /**
     * 登录会话信息
     */
    public static class LoginSession {
        private final String userId;
        private final String aiType;
        private final BrowserContext context;
        private final Page page;
        private final AtomicBoolean isActive;
        private final long createTime;
        
        public LoginSession(String userId, String aiType, BrowserContext context, Page page) {
            this.userId = userId;
            this.aiType = aiType;
            this.context = context;
            this.page = page;
            this.isActive = new AtomicBoolean(true);
            this.createTime = System.currentTimeMillis();
        }
        
        public String getUserId() { return userId; }
        public String getAiType() { return aiType; }
        public BrowserContext getContext() { return context; }
        public Page getPage() { return page; }
        public boolean isActive() { return isActive.get(); }
        public void setInactive() { isActive.set(false); }
        public long getCreateTime() { return createTime; }
    }
    
    /**
     * 开始新的登录会话
     * 
     * 🔥 智能会话管理：
     * 1. 如果是同一个AI的重复登录（连续点击），复用现有会话，不清理
     * 2. 如果是切换到不同AI的登录，清理旧会话，创建新会话
     * 
     * @param userId 用户ID
     * @param aiType AI类型
     * @param context 浏览器上下文
     * @param page 页面对象
     * @return 会话键
     */
    public String startLoginSession(String userId, String aiType, BrowserContext context, Page page) {
        String sessionKey = userId + "-" + aiType;
        
        // 🔥 检查是否已存在相同的会话（连续点击同一个AI）
        LoginSession existingSession = activeSessions.get(sessionKey);
        if (existingSession != null && existingSession.isActive()) {
            // 连续点击同一个AI，复用现有会话，不清理
            // 只更新页面引用（如果需要）
            return sessionKey;
        }
        
        // 🔥 切换到不同AI或首次登录：清理该用户的其他AI会话
        cleanupUserSessionsExcept(userId, sessionKey);
        
        // 创建新会话
        LoginSession session = new LoginSession(userId, aiType, context, page);
        activeSessions.put(sessionKey, session);
        
        return sessionKey;
    }
    
    /**
     * 检查会话是否仍然活跃
     * @param sessionKey 会话键
     * @return 是否活跃
     */
    public boolean isSessionActive(String sessionKey) {
        LoginSession session = activeSessions.get(sessionKey);
        if (session == null) {
            return false;
        }
        
        // 检查页面是否已关闭
        try {
            if (session.getPage().isClosed()) {
                session.setInactive();
                activeSessions.remove(sessionKey);
                return false;
            }
        } catch (Exception e) {
            session.setInactive();
            activeSessions.remove(sessionKey);
            return false;
        }
        
        return session.isActive();
    }
    
    /**
     * 获取现有的登录会话
     * @param sessionKey 会话键
     * @return 登录会话，如果不存在或已失效则返回null
     */
    public LoginSession getSession(String sessionKey) {
        LoginSession session = activeSessions.get(sessionKey);
        if (session != null && session.isActive()) {
            // 检查页面是否仍然有效
            try {
                if (!session.getPage().isClosed()) {
                    return session;
                }
            } catch (Exception e) {
                // 页面已关闭
            }
        }
        return null;
    }
    
    /**
     * 结束登录会话
     * @param sessionKey 会话键
     */
    public void endLoginSession(String sessionKey) {
        LoginSession session = activeSessions.remove(sessionKey);
        if (session != null) {
            session.setInactive();
        }
    }
    
    /**
     * 清理用户的所有登录会话
     * @param userId 用户ID
     */
    public void cleanupUserSessions(String userId) {
        cleanupUserSessionsExcept(userId, null);
    }
    
    /**
     * 清理用户的所有登录会话，但排除指定的会话
     * 
     * 🔥 安全保证：
     * 1. 只清理指定userId的登录会话
     * 2. 不影响其他用户的任何会话
     * 3. 不影响当前用户的AI咨询窗口（咨询窗口不在此管理范围内）
     * 4. 只清理登录检测窗口（通过startLoginSession注册的会话）
     * 
     * @param userId 用户ID - 只清理该用户的登录会话
     * @param exceptSessionKey 要排除的会话键（不清理这个会话）
     */
    private void cleanupUserSessionsExcept(String userId, String exceptSessionKey) {
        activeSessions.entrySet().removeIf(entry -> {
            String sessionKey = entry.getKey();
            LoginSession session = entry.getValue();
            
            // 🔥 第一层保护：只处理指定用户的会话
            if (session.getUserId().equals(userId)) {
                // 🔥 第二层保护：不清理指定排除的会话（新创建的会话）
                if (exceptSessionKey != null && sessionKey.equals(exceptSessionKey)) {
                    return false;
                }
                
                try {
                    // 🔥 安全关闭：只关闭登录检测窗口的页面和上下文
                    // AI咨询窗口不在activeSessions中，不会被影响
                    
                    // 先检查并关闭页面
                    if (session.getPage() != null) {
                        try {
                            if (!session.getPage().isClosed()) {
                                session.getPage().close();
                            }
                        } catch (Exception e) {
                            // 页面可能已经被外部关闭，忽略错误
                        }
                    }
                    
                    // 再检查并关闭上下文
                    if (session.getContext() != null) {
                        try {
                            // BrowserContext没有isClosed方法，直接尝试关闭
                            session.getContext().close();
                        } catch (Exception e) {
                            // 上下文可能已经被外部关闭（如用户关闭窗口），忽略错误
                        }
                    }
                } catch (Exception e) {
                    // 静默处理清理错误
                }
                
                session.setInactive();
                return true;
            }
            // 🔥 第三层保护：其他用户的会话完全不处理
            return false;
        });
    }
    
    /**
     * 清理超时的会话（超过5分钟）
     */
    public void cleanupTimeoutSessions() {
        long currentTime = System.currentTimeMillis();
        long timeout = 5 * 60 * 1000; // 5分钟超时
        
        activeSessions.entrySet().removeIf(entry -> {
            LoginSession session = entry.getValue();
            if (currentTime - session.getCreateTime() > timeout) {
                try {
                    // 先检查并关闭页面
                    if (session.getPage() != null) {
                        try {
                            if (!session.getPage().isClosed()) {
                                session.getPage().close();
                            }
                        } catch (Exception e) {
                            // 页面可能已经被外部关闭，忽略错误
                        }
                    }
                    
                    // 再检查并关闭上下文
                    if (session.getContext() != null) {
                        try {
                            session.getContext().close();
                        } catch (Exception e) {
                            // 上下文可能已经被外部关闭，忽略错误
                        }
                    }
                } catch (Exception e) {
                    // 静默处理清理错误
                }
                
                session.setInactive();
                return true;
            }
            return false;
        });
    }
    
    /**
     * 获取活跃会话数量
     * @return 活跃会话数量
     */
    public int getActiveSessionCount() {
        return activeSessions.size();
    }
}
