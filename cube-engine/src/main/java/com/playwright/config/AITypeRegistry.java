package com.playwright.config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI类型注册表 - 避免硬编码AI类型
 * 统一管理所有AI的配置信息
 * 
 * @author 优立方
 * @date 2025-01-21
 */
public class AITypeRegistry {
    
    /**
     * AI配置类
     */
    public static class AIConfig {
        private final String code;           // AI代码（唯一标识）
        private final String name;           // AI显示名称
        private final String messageType;    // WebSocket消息类型
        private final String chatIdType;     // 会话ID消息类型
        
        public AIConfig(String code, String name, String messageType, String chatIdType) {
            this.code = code;
            this.name = name;
            this.messageType = messageType;
            this.chatIdType = chatIdType;
        }
        
        public String getCode() { return code; }
        public String getName() { return name; }
        public String getMessageType() { return messageType; }
        public String getChatIdType() { return chatIdType; }
    }
    
    // AI配置映射表（代码 -> 配置）
    private final Map<String, AIConfig> codeConfigMap = new ConcurrentHashMap<>();
    
    // AI配置映射表（名称 -> 配置）
    private final Map<String, AIConfig> nameConfigMap = new ConcurrentHashMap<>();
    
    // AI配置映射表（方法名关键字 -> 配置）
    private final Map<String, AIConfig> methodKeywordMap = new ConcurrentHashMap<>();

    /**
     * 初始化AI注册表
     */
    public AITypeRegistry() {
        registerAI("baidu", "百度AI", "RETURN_BAIDU_RES", "RETURN_BAIDU_CHATID", 
            Arrays.asList("Baidu", "baidu"));
        registerAI("doubao", "豆包", "RETURN_DB_RES", "RETURN_DB_CHATID", 
            Arrays.asList("DB", "DouBao", "doubao"));
        registerAI("yuanbao", "腾讯元宝", "RETURN_YB_RES", "RETURN_YB_CHATID", 
            Arrays.asList("YB", "YuanBao", "Tencent", "yuanbao", "hunyuan", "znpb"));
        registerAI("yuanbao_t1", "腾讯元宝T1", "RETURN_YBT1_RES", "RETURN_YBT1_CHATID", 
            Arrays.asList("YBT1", "hunyuan"));
        registerAI("yuanbao_ds", "腾讯元宝DS", "RETURN_YBDS_RES", "RETURN_YBDS_CHATID", 
            Arrays.asList("YBDS", "yuanbao_deepseek"));
        registerAI("znpb", "智能排版", "RETURN_ZNPB_RES", "RETURN_ZNPB_CHATID", 
            Arrays.asList("ZNPB", "znpb"));
        registerAI("deepseek", "DeepSeek", "RETURN_DEEPSEEK_RES", "RETURN_DEEPSEEK_CHATID", 
            Arrays.asList("DeepSeek", "DS", "deepseek"));
        registerAI("metaso", "秘塔", "RETURN_METASO_RES", "RETURN_METASO_CHATID", 
            Arrays.asList("Metaso", "metaso", "mita"));
        registerAI("zhihu", "知乎直答", "RETURN_ZHZD_RES", "RETURN_ZHZD_CHATID", 
            Arrays.asList("Zhihu", "ZHZD", "zhihu", "zhzd"));
        registerAI("tongyi", "通义千问", "RETURN_TONGYI_RES", "RETURN_TONGYI_CHATID", 
            Arrays.asList("TongYi", "tongyi", "QW", "qianwen"));
        registerAI("kimi", "Kimi", "RETURN_KIMI_RES", "RETURN_KIMI_CHATID", 
            Arrays.asList("Kimi", "kimi"));
    }

    /**
     * 注册AI配置
     * 
     * @param code AI代码
     * @param name AI名称
     * @param messageType WebSocket消息类型
     * @param chatIdType 会话ID消息类型
     * @param methodKeywords 方法名关键字列表
     */
    private void registerAI(String code, String name, String messageType, String chatIdType, 
                           List<String> methodKeywords) {
        AIConfig config = new AIConfig(code, name, messageType, chatIdType);
        codeConfigMap.put(code.toLowerCase(), config);
        nameConfigMap.put(name, config);
        
        // 注册方法关键字映射
        if (methodKeywords != null) {
            for (String keyword : methodKeywords) {
                methodKeywordMap.put(keyword.toLowerCase(), config);
            }
        }
    }

    /**
     * 根据代码获取AI配置
     */
    public AIConfig getByCode(String code) {
        if (code == null) return null;
        return codeConfigMap.get(code.toLowerCase());
    }

    /**
     * 根据名称获取AI配置
     */
    public AIConfig getByName(String name) {
        if (name == null) return null;
        return nameConfigMap.get(name);
    }

    /**
     * 根据方法名获取AI配置
     * 支持从方法名中提取AI关键字，如 startBaidu -> baidu
     */
    public AIConfig getByMethodName(String methodName) {
        if (methodName == null) return null;
        
        // 尝试直接匹配方法关键字
        for (Map.Entry<String, AIConfig> entry : methodKeywordMap.entrySet()) {
            if (methodName.toLowerCase().contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return null;
    }

    /**
     * 获取所有已注册的AI配置
     */
    public Collection<AIConfig> getAllConfigs() {
        return codeConfigMap.values();
    }

    /**
     * 获取所有AI代码
     */
    public Set<String> getAllCodes() {
        return codeConfigMap.keySet();
    }

    /**
     * 获取所有AI名称
     */
    public Set<String> getAllNames() {
        return nameConfigMap.keySet();
    }

    /**
     * 检查AI是否已注册
     */
    public boolean isRegistered(String codeOrName) {
        if (codeOrName == null) return false;
        return codeConfigMap.containsKey(codeOrName.toLowerCase()) 
            || nameConfigMap.containsKey(codeOrName);
    }
}
