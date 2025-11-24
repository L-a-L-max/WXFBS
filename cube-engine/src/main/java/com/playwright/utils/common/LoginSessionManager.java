package com.playwright.utils.common;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

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
    
    // 🔥 会话超时时间（30秒）
    private static final long SESSION_TIMEOUT_MS = 30 * 1000;
    
    // 🔥 只存储登录检测会话，不包含AI咨询会话
    private final ConcurrentHashMap<String, LoginSession> activeSessions = new ConcurrentHashMap<>();
    
    // 🔥 用户级别的锁，确保单用户同时只能有一个登录操作
    private final ConcurrentHashMap<String, ReentrantLock> userLocks = new ConcurrentHashMap<>();
    
    /**
     * 登录会话信息
     */
    public static class LoginSession {
        private final String userId;
        private final String aiType;
        private final BrowserContext context;
        private final Page page;
        private final AtomicBoolean isActive;  // 🔥 唯一状态：活跃/失效
        private final long createTime;
        private final boolean isPersistent;  // 🔥 是否是持久化浏览器上下文
        
        public LoginSession(String userId, String aiType, BrowserContext context, Page page, boolean isPersistent) {
            this.userId = userId;
            this.aiType = aiType;
            this.context = context;
            this.page = page;
            this.isActive = new AtomicBoolean(true);
            this.createTime = System.currentTimeMillis();
            this.isPersistent = isPersistent;
        }
        
        public String getUserId() { return userId; }
        public String getAiType() { return aiType; }
        public BrowserContext getContext() { return context; }
        public Page getPage() { return page; }
        public boolean isActive() { return isActive.get(); }
        public void setInactive() { isActive.set(false); }
        public long getCreateTime() { return createTime; }
        public boolean isPersistent() { return isPersistent; }
    }
    
    /**
     * 获取用户锁
     * @param userId 用户ID
     * @return 用户专属的锁
     */
    private ReentrantLock getUserLock(String userId) {
        return userLocks.computeIfAbsent(userId, k -> new ReentrantLock());
    }
    
    /**
     * 🔥【重构】准备登录会话（在创建BrowserContext之前调用）
     * 
     * 核心原则：简单粗暴，绝不误判
     * 1. 每次新请求到来，强制清理该用户的**所有**旧会话
     * 2. 不管旧会话是什么状态，一律清理并失效（setInactive）
     * 3. 旧会话的后台线程会检测到失效，立即退出
     * 4. 用户锁保证串行执行，不会有并发问题
     * 
     * @param userId 用户ID
     * @param aiType AI类型
     * @return 会话键，总是返回非null值
     */
    public String prepareLoginSession(String userId, String aiType) {
        String sessionKey = userId + "-" + aiType;
        ReentrantLock userLock = getUserLock(userId);
        
        // 准备登录会话
        userLock.lock();
        
        try {
            // 强制清理该用户的所有旧会话
            long userSessionCount = activeSessions.values().stream()
                .filter(session -> session.getUserId().equals(userId))
                .count();
            
            if (userSessionCount > 0) {
                
                // 清理该用户的所有会话
                activeSessions.values().stream()
                    .filter(session -> session.getUserId().equals(userId))
                    .forEach(session -> {
                        // 标记失效
                        session.setInactive();
                        
                        // 持久化AI：保持Page和Context开启
                        if (session.isPersistent()) {
                            // 持久化AI不关闭资源
                        } else {
                            // 非持久化AI：关闭资源
                            try {
                                if (session.getPage() != null && !session.getPage().isClosed()) {
                                    session.getPage().close();
                                }
                            } catch (Exception e) {
                                // 忽略
                            }
                            
                            try {
                                if (session.getContext() != null) {
                                    session.getContext().close();
                                }
                            } catch (Exception e) {
                                // 忽略
                            }
                        }
                    });
                
                // 从Map中移除
                activeSessions.entrySet().removeIf(entry -> 
                    entry.getValue().getUserId().equals(userId)
                );
                
                // 已清理旧会话
            }
            
            return sessionKey;
        } finally {
            userLock.unlock();
        }
    }
    
    /**
     * 🔥【第2步】开始登录会话（在创建BrowserContext和Page后调用）
     * 
     * 核心原则：简单直接，无需状态追踪
     * 1. 直接创建新会话，默认活跃状态
     * 2. 不需要“已初始化”标记，因为prepareLoginSession已清理所有旧会话
     * 3. 如果有新请求，prepareLoginSession会清理这个会话并标记失效
     * 4. 后台线程通过isSessionActive()实时检测失效并退出
     * 
     * @param userId 用户ID
     * @param aiType AI类型
     * @param context 浏览器上下文
     * @param page 页面对象
     * @param isPersistent 是否是持久化浏览器上下文（元宝为true，其他为false）
     * @return 会话键
     */
    public String startLoginSession(String userId, String aiType, BrowserContext context, Page page, boolean isPersistent) {
        String sessionKey = userId + "-" + aiType;
        LoginSession session = new LoginSession(userId, aiType, context, page, isPersistent);
        activeSessions.put(sessionKey, session);
        // 会话注册成功
        return sessionKey;
    }
    
    /**
     * 🔥 兼容旧代码的重载方法（默认非持久化）
     */
    public String startLoginSession(String userId, String aiType, BrowserContext context, Page page) {
        return startLoginSession(userId, aiType, context, page, false);
    }
    
    /**
     * 检查会话是否仍然活跃（加入超时检查）
     * @param sessionKey 会话键
     * @return 是否活跃
     */
    public boolean isSessionActive(String sessionKey) {
        LoginSession session = activeSessions.get(sessionKey);
        if (session == null || !session.isActive()) {
            return false;
        }
        
        // 🔥 检查会话是否超时（30秒）
        long sessionAge = System.currentTimeMillis() - session.getCreateTime();
        if (sessionAge > SESSION_TIMEOUT_MS) {
            // 会话超时，标记为失效
            session.setInactive();
            return false;
        }
        
        return true;
    }
    
    /**
     * 🔥 【核心方法】验证返回前的身份
     * 
     * 📌 作用：
     *   防止AI登录重试逻辑发送错误的二维码
     *   确保返回的结果属于正确的用户和AI
     * 
     * 📌 使用场景：
     *   在发送二维码、登录状态等关键信息前调用此方法验证
     *   如果验证失败，应该拒绝发送并终止当前登录流程
     * 
     * 📌 验证原理：
     *   1. 检查会话本身是否活跃（是否已被清理）
     *   2. 检查用户是否只有这一个AI的活跃会话
     *   3. 如果发现用户有其他AI的活跃会话，说明用户已切换，拒绝
     * 
     * @param userId 用户ID
     * @param aiType AI类型（Baidu、Doubao等）
     * @return 如果当前用户的活跃会话是这个AI，返回true；否则返回false
     */
    public boolean validateCurrentSession(String userId, String aiType) {
        String sessionKey = userId + "-" + aiType;
        
        // 第1步：检查这个会话本身是否活跃
        if (!isSessionActive(sessionKey)) {
            return false;
        }
        
        // 第2步：检查用户是否只有这一个活跃会话
        for (Map.Entry<String, LoginSession> entry : activeSessions.entrySet()) {
            String key = entry.getKey();
            LoginSession session = entry.getValue();
            
            if (session.getUserId().equals(userId) && session.isActive()) {
                if (!key.equals(sessionKey)) {
                    // 发现用户有其他活跃会话，说明用户已切换
                    return false;
                }
            }
        }
        
        return true;
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
     * 🔥 【错误恢复】强制清空用户所有登录会话
     * 
     * 📌 使用场景：
     *   1. 当检测到异常状态（如串码、重试失败等）
     *   2. 用户频繁切换导致混乱
     *   3. 前端主动调用清理（关闭窗口、切换AI等）
     *   4. 系统检测到不一致状态需要重置
     * 
     * 📌 特点：
     *   - 只清理指定用户的登录会话
     *   - 不影响其他用户
     *   - 不影响咨询进程（只清理LoginSessionManager中的会话）
     *   - 元宝AI特殊处理（不关闭浏览器实例）
     *   - 释放用户锁
     *   - 确保资源完全清理，为下次登录做好准备
     * 
     * @param userId 用户ID
     */
    public void clearAllUserLoginSessions(String userId) {
        // 统计该用户的会话数量
        long userSessionCount = activeSessions.values().stream()
            .filter(session -> session.getUserId().equals(userId))
            .count();
        
        ReentrantLock userLock = getUserLock(userId);
        userLock.lock();
        try {
            AtomicInteger clearedCount = new AtomicInteger(0);
            
            // 清理该用户的所有登录会话
            activeSessions.entrySet().removeIf(entry -> {
                String sessionKey = entry.getKey();
                LoginSession session = entry.getValue();
                
                if (session.getUserId().equals(userId)) {
                    clearedCount.incrementAndGet();
                    
                    // 🔥 关闭浏览器资源（持久化AI保持Page和Context开启）
                    if (session.isPersistent()) {
                    } else {
                        // 🔥 非持久化AI：立即关闭Page和Context
                        try {
                            if (session.getPage() != null && !session.getPage().isClosed()) {
                                session.getPage().close();
                            }
                        } catch (Exception e) {
                        }
                        
                        try {
                            if (session.getContext() != null) {
                                session.getContext().close();
                            }
                        } catch (Exception e) {
                        }
                    }
                    
                    session.setInactive();
                    return true;  // 移除此会话
                }
                return false;
            });
            
        } finally {
            userLock.unlock();
        }
    }
    
    /**
     * 结束登录会话
     * 
     * 🔥 重要：不仅标记会话不活跃，还要关闭浏览器资源
     * 
     * @param sessionKey 会话键
     */
    public void endLoginSession(String sessionKey) {
        
        LoginSession session = activeSessions.remove(sessionKey);
        if (session != null) {
            
            // 🔥 关闭浏览器资源（持久化AI保持Page和Context开启）
            if (session.isPersistent()) {
            } else {
                // 🔥 非持久化AI：关闭Page和Context
                try {
                    if (session.getPage() != null && !session.getPage().isClosed()) {
                        session.getPage().close();
                    } else {
                    }
                } catch (Exception e) {
                }
                
                try {
                    if (session.getContext() != null) {
                        session.getContext().close();
                    }
                } catch (Exception e) {
                }
            }
            
            session.setInactive();
        } else {
        }
    }
    
    /**
     * 清理用户的所有登录会话
     * @param userId 用户ID
     */
    public void cleanupUserSessions(String userId) {
        ReentrantLock userLock = getUserLock(userId);
        userLock.lock();
        try {
            cleanupUserSessionsExcept(userId, null);
        } finally {
            userLock.unlock();
        }
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
        int cleanedCount = 0;
        
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
                            } else {
                            }
                        } catch (Exception e) {
                        }
                    }
                    
                    // 再检查并关闭上下文
                    if (session.getContext() != null) {
                        try {
                            // BrowserContext没有isClosed方法，直接尝试关闭
                            session.getContext().close();
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
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
    
    /**
     * 🔥 【调试工具】获取用户的所有活跃会话信息
     * 
     * 用于调试和监控，帮助识别会话状态
     * 
     * @param userId 用户ID
     * @return 用户的活跃会话列表
     */
    public java.util.List<String> getUserActiveSessions(String userId) {
        java.util.List<String> sessions = new java.util.ArrayList<>();
        for (Map.Entry<String, LoginSession> entry : activeSessions.entrySet()) {
            LoginSession session = entry.getValue();
            if (session.getUserId().equals(userId) && session.isActive()) {
                sessions.add(String.format("%s (AI:%s, 创建时间:%d秒前)", 
                    entry.getKey(), 
                    session.getAiType(),
                    (System.currentTimeMillis() - session.getCreateTime()) / 1000
                ));
            }
        }
        return sessions;
    }
    
    /**
     * 🔥 【强制清理】清理指定AI类型的所有用户会话
     * 
     * 用于系统维护或紧急情况，清理某个AI的所有登录会话
     * 注意：这会影响所有用户，慎用！
     * 
     * @param aiType AI类型
     * @return 清理的会话数量
     */
    public int forceCleanupByAiType(String aiType) {
        AtomicInteger clearedCount = new AtomicInteger(0);
        
        activeSessions.entrySet().removeIf(entry -> {
            LoginSession session = entry.getValue();
            if (session.getAiType().equals(aiType)) {
                System.out.println("   清理: 用户" + session.getUserId() + "的" + aiType + "会话");
                try {
                    if (session.getPage() != null && !session.getPage().isClosed()) {
                        session.getPage().close();
                    }
                } catch (Exception e) {
                    // 忽略
                }
                try {
                    if (session.getContext() != null) {
                        session.getContext().close();
                    }
                } catch (Exception e) {
                    // 忽略
                }
                session.setInactive();
                clearedCount.incrementAndGet();
                return true;
            }
            return false;
        });
        
        return clearedCount.get();
    }
}
