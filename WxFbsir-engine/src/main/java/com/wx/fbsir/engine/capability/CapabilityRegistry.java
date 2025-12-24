package com.wx.fbsir.engine.capability;

import com.wx.fbsir.engine.capability.annotation.OnceCapability;
import com.wx.fbsir.engine.capability.annotation.StreamCapability;
import com.wx.fbsir.engine.websocket.message.EngineMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * 消息路由注册中心（注解驱动 - 完全自动配置）
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 📌 新手指南：如何添加新的消息处理器
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 步骤1: 在Controller方法上添加注解
 * ─────────────────────────────────────────
 * 
 * 流式任务（支持进度推送）:
 *   @StreamCapability(
 *       type = "AI_CHAT",
 *       description = "AI对话请求",
 *       progressInterval = 3000  // 可选：自动进度推送间隔（毫秒）
 *   )
 *   public void handleChat(EngineMessage message) { }
 *   
 * 单次任务（只返回最终结果）:
 *   @OnceCapability(
 *       type = "TASK_EXECUTE",
 *       description = "任务执行",
 *       requireAuth = true  // 可选：是否需要认证
 *   )
 *   public void handleTask(EngineMessage message) { }
 * 
 * 步骤2: 无需配置！
 * ─────────────────────────────────────────
 * 系统会在启动时自动扫描所有@Controller类中的注解并注册
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * ⚠️ 注意事项
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 
 * 1. Controller类必须添加@Controller注解
 * 2. 处理方法签名必须是: public void methodName(EngineMessage message)
 * 3. type值必须与MessageType枚举中的code一致
 * 4. 系统启动时会输出所有注册的处理器，请检查日志确认
 * 
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *
 * @author wxfbsir
 * @date 2025-12-18
 */
@Component
public class CapabilityRegistry {

    private static final Logger log = LoggerFactory.getLogger(CapabilityRegistry.class);

    @Autowired
    private ApplicationContext context;

    private final Map<String, MessageHandler> handlers = new LinkedHashMap<>();

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 📌 自动注册区域 - 无需手动配置
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    @PostConstruct
    public void registerHandlers() {
        // ━━━━━━━━━━━━━━━━ 自动扫描注解并注册 ━━━━━━━━━━━━━━━━
        // 扫描所有@Controller中的@StreamCapability和@OnceCapability注解
        // 无需手动配置，系统自动发现和注册
        autoRegisterFromAnnotations();
        
        // ━━━━━━━━━━━━━━━━ 输出注册结果 ━━━━━━━━━━━━━━━━
        log.info("[CapabilityRegistry] 已注册 {} 个消息处理器", handlers.size());
    }
    
    /**
     * 自动扫描注解并注册
     * 
     * 扫描所有@Controller类中的@StreamCapability和@OnceCapability注解
     * 并自动注册到handlers中
     */
    private void autoRegisterFromAnnotations() {
        // 获取所有标记了@Controller的Bean
        Map<String, Object> controllers = context.getBeansWithAnnotation(Controller.class);
        
        int streamCount = 0;
        int onceCount = 0;
        
        for (Map.Entry<String, Object> entry : controllers.entrySet()) {
            String beanName = entry.getKey();
            Object controller = entry.getValue();
            Class<?> controllerClass = controller.getClass();
            
            // 遍历所有public方法
            for (Method method : controllerClass.getMethods()) {
                // 检查方法参数是否为EngineMessage
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length != 1 || !paramTypes[0].equals(EngineMessage.class)) {
                    continue; // 方法签名不匹配，跳过
                }
                
                // 检查@StreamCapability注解
                StreamCapability streamAnnotation = method.getAnnotation(StreamCapability.class);
                if (streamAnnotation != null) {
                    String type = streamAnnotation.type();
                    String description = streamAnnotation.description();
                    
                    // 注册流式处理器（如果已存在则跳过）
                    if (!handlers.containsKey(type)) {
                        register(type, description, beanName, method.getName(), true);
                        streamCount++;
                    }
                }
                
                // 检查@OnceCapability注解
                OnceCapability onceAnnotation = method.getAnnotation(OnceCapability.class);
                if (onceAnnotation != null) {
                    String type = onceAnnotation.type();
                    String description = onceAnnotation.description();
                    
                    // 注册单次处理器（如果已存在则跳过）
                    if (!handlers.containsKey(type)) {
                        register(type, description, beanName, method.getName(), false);
                        onceCount++;
                    }
                }
            }
        }
        
        if (streamCount > 0 || onceCount > 0) {
            log.info("[CapabilityRegistry] 自动注册完成: 流式={}, 单次={}", streamCount, onceCount);
        }
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 框架内部方法（无需关注）
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

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
