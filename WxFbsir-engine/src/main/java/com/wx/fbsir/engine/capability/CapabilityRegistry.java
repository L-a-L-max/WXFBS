package com.wx.fbsir.engine.capability;

import com.wx.fbsir.engine.websocket.message.EngineMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.function.Consumer;

/**
 * 消息路由注册中心
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 新增消息类型（只需添加一行，Controller 自动装配）
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 在 registerHandlers() 方法中添加一行即可：
 * 
 *   stream("AI_CHAT",      "AI对话请求",     "aiController",   "handleChat");
 *   once("TASK_EXECUTE",   "任务执行",       "taskController", "handleTask");
 * 
 * 参数说明：
 *   - 消息类型：精准匹配的消息类型字符串
 *   - 描述：能力描述，用于 Admin 展示
 *   - Controller名：Spring Bean 名称（类名首字母小写）
 *   - 方法名：处理方法名，参数必须是 EngineMessage
 * 
 * ⚠️ 重要：只支持精准匹配，避免 yb_deepseek 和 deepseek 等误匹配问题
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *
 * @author wxfbsir
 * @date 2025-12-18
 */
@Component
public class CapabilityRegistry {

    @Autowired
    private ApplicationContext context;

    private final Map<String, MessageHandler> handlers = new LinkedHashMap<>();

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 📌 消息路由配置区域 - 新增消息类型只需添加一行
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    @PostConstruct
    public void registerHandlers() {
        // ━━━━━━━━━━━━━━━━ 在此添加消息处理器（一行一个）━━━━━━━━━━━━━━━━
        // stream("AI_CHAT",      "AI对话请求",     "aiController",   "handleChat");
        // once("TASK_EXECUTE",   "任务执行",       "taskController", "handleTask");
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 框架方法（无需修改）
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    /**
     * 注册流式消息处理器（支持中间状态回传）
     * Controller 通过 Spring 自动装配
     */
    private void stream(String type, String description, String beanName, String methodName) {
        register(type, description, beanName, methodName, true);
    }

    /**
     * 注册单次消息处理器（只返回最终结果）
     * Controller 通过 Spring 自动装配
     */
    private void once(String type, String description, String beanName, String methodName) {
        register(type, description, beanName, methodName, false);
    }

    private void register(String type, String description, String beanName, String methodName, boolean streaming) {
        try {
            Object bean = context.getBean(beanName);
            var method = bean.getClass().getMethod(methodName, EngineMessage.class);
            Consumer<EngineMessage> handler = msg -> {
                try {
                    method.invoke(bean, msg);
                } catch (Exception e) {
                    throw new RuntimeException("调用 " + beanName + "." + methodName + " 失败: " + e.getMessage(), e);
                }
            };
            handlers.put(type, new MessageHandler(type, description, handler, streaming));
        } catch (Exception e) {
            System.err.println("[能力注册] 注册失败: " + type + " -> " + beanName + "." + methodName + ", 错误: " + e.getMessage());
        }
    }

    public MessageHandler getHandler(String type) {
        return handlers.get(type);
    }

    /**
     * 精准匹配消息类型（不支持模糊匹配，避免误调用）
     * 
     * @param type 消息类型，必须完全匹配
     * @return 处理器，未找到返回 null
     */
    public MessageHandler findHandler(String type) {
        // 只支持精准匹配，避免 yb_deepseek 误匹配 deepseek 等问题
        return handlers.get(type);
    }

    public boolean hasHandler(String type) {
        return handlers.containsKey(type);
    }

    public List<Map<String, Object>> getCapabilityList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MessageHandler h : handlers.values()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("type", h.type());
            map.put("description", h.description());
            map.put("streaming", h.streaming());
            list.add(map);
        }
        return list;
    }

    public int size() {
        return handlers.size();
    }

    /**
     * 消息处理器
     * 
     * @param type        消息类型
     * @param description 描述
     * @param handler     处理方法
     * @param streaming   是否流式（支持中间状态回传）
     */
    public record MessageHandler(
        String type,
        String description,
        Consumer<EngineMessage> handler,
        boolean streaming
    ) {
        public void handle(EngineMessage message) {
            handler.accept(message);
        }
    }
}
