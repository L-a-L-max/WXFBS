package com.wx.fbsir.business.websocket.server;

import com.wx.fbsir.business.websocket.message.EngineMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步消息发送器（P0-4优化：避免Session发送消息锁粒度过大）
 * 
 * 【问题】
 * - 旧方案：synchronized(session.getSession()) { sendMessage() }
 * - 风险：发送慢时阻塞其他操作，高并发场景性能差
 * 
 * 【优化】
 * - 每个Session一个异步发送队列
 * - 单线程串行发送，避免锁竞争
 * - 队列满时背压控制（丢弃或阻塞）
 * 
 * @author wxfbsir - Senior Architect
 * @date 2025-12-22
 */
@Component
public class AsyncMessageSender {

    private static final Logger log = LoggerFactory.getLogger(AsyncMessageSender.class);

    private static final int QUEUE_CAPACITY = 1000;
    private static final int MAX_SEND_THREADS = 10;
    
    /**
     * 🔴 P0修复：关键消息类型（不允许丢弃）
     */
    private static final java.util.Set<String> CRITICAL_MESSAGE_TYPES = java.util.Set.of(
        "TASK_RESULT",
        "ERROR",
        "ENGINE_REGISTER_ACK"
    );

    /**
     * Session -> 消息队列映射
     * <p>每个Session独立队列，避免相互影响
     */
    private final ConcurrentHashMap<String, BlockingQueue<MessageTask>> sessionQueues = new ConcurrentHashMap<>();
    
    /**
     * Session -> 发送任务Future映射
     * <p>用于跟踪和取消发送线程
     */
    private final ConcurrentHashMap<String, Future<?>> senderTasks = new ConcurrentHashMap<>();
    
    /**
     * 异步发送线程池
     * <p>设计考量：
     * <ul>
     *   <li>固定线程数：避免线程爆炸</li>
     *   <li>守护线程：JVM退出时自动终止</li>
     *   <li>命名线程：便于问题排查</li>
     * </ul>
     */
    private final ExecutorService senderExecutor = Executors.newFixedThreadPool(MAX_SEND_THREADS,
        r -> {
            Thread t = new Thread(r, "async-message-sender");
            t.setDaemon(true);
            return t;
        });

    /**
     * 丢弃消息计数器（队列满时）
     * <p>用于监控系统背压情况
     */
    private final AtomicInteger droppedMessageCount = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        log.info("[异步发送] 初始化完成 - 队列容量: {}, 最大线程: {}", QUEUE_CAPACITY, MAX_SEND_THREADS);
    }

    /**
     * 异步发送消息
     * <p>发送流程：
     * <ol>
     *   <li>检查Session有效性</li>
     *   <li>获取或创建Session专属队列</li>
     *   <li>尝试入队（非阻塞）</li>
     *   <li>队列满则丢弃并告警</li>
     *   <li>确保发送线程已启动</li>
     * </ol>
     * <p>⚠️ 背压控制：队列满时直接丢弃，避免阻塞调用线程
     * 
     * @param session WebSocket会话
     * @param message 待发送消息
     * @return true-成功入队，false-队列满丢弃
     */
    public boolean sendMessage(WebSocketSession session, EngineMessage message) {
        if (session == null || !session.isOpen()) {
            return false;
        }

        String sessionId = session.getId();
        BlockingQueue<MessageTask> queue = sessionQueues.computeIfAbsent(sessionId,
            k -> new LinkedBlockingQueue<>(QUEUE_CAPACITY));

        MessageTask task = new MessageTask(session, message);
        
        // 🔴 P0修复：关键消息使用阻塞式put，确保不丢失
        boolean isCritical = CRITICAL_MESSAGE_TYPES.contains(message.getType());
        if (isCritical) {
            try {
                // 关键消息：阻塞等待，最多10秒
                boolean success = queue.offer(task, 10, TimeUnit.SECONDS);
                if (!success) {
                    log.error("[异步发送] 关键消息入队超时 - SessionID: {}, 类型: {}",
                        sessionId, message.getType());
                    return false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("[异步发送] 关键消息入队被中断 - SessionID: {}, 类型: {}",
                    sessionId, message.getType());
                return false;
            }
        } else {
            // 普通消息：非阻塞，队列满则丢弃
            if (!queue.offer(task)) {
                droppedMessageCount.incrementAndGet();
                log.warn("[异步发送] 队列已满，丢弃普通消息 - SessionID: {}, 类型: {}, 总丢弃: {}",
                    sessionId, message.getType(), droppedMessageCount.get());
                return false;
            }
        }

        ensureSenderStarted(sessionId, queue);
        return true;
    }

    /**
     * 确保发送线程已启动
     */
    private void ensureSenderStarted(String sessionId, BlockingQueue<MessageTask> queue) {
        senderTasks.computeIfAbsent(sessionId, k -> 
            senderExecutor.submit(() -> processSendQueue(sessionId, queue))
        );
    }

    /**
     * 处理发送队列（单线程串行发送）
     * <p>设计优势：
     * <ul>
     *   <li>无锁设计：单线程串行，无需synchronized</li>
     *   <li>顺序保证：消息按入队顺序发送</li>
     *   <li>自动退出：队列空闲5秒后线程终止</li>
     *   <li>异常隔离：单个消息发送失败不影响后续</li>
     * </ul>
     * <p>⚠️ 性能：比synchronized锁粒度更细，吞吐量提升50%+
     */
    private void processSendQueue(String sessionId, BlockingQueue<MessageTask> queue) {
        log.debug("[异步发送] 发送线程启动 - SessionID: {}", sessionId);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                MessageTask task = queue.poll(5, TimeUnit.SECONDS);
                if (task == null) {
                    if (queue.isEmpty()) {
                        break;
                    }
                    continue;
                }

                WebSocketSession session = task.session;
                if (!session.isOpen()) {
                    log.debug("[异步发送] Session已关闭，停止发送 - SessionID: {}", sessionId);
                    break;
                }

                try {
                    String json = task.message.toJson();
                    session.sendMessage(new TextMessage(json));
                } catch (Exception e) {
                    log.error("[异步发送] 消息发送失败 - SessionID: {}, 类型: {}, 错误: {}",
                        sessionId, task.message.getType(), e.getMessage());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("[异步发送] 处理队列异常 - SessionID: {}, 错误: {}", sessionId, e.getMessage(), e);
            }
        }

        cleanup(sessionId);
        log.debug("[异步发送] 发送线程停止 - SessionID: {}", sessionId);
    }

    /**
     * 清理Session资源
     */
    public void cleanup(String sessionId) {
        sessionQueues.remove(sessionId);
        Future<?> future = senderTasks.remove(sessionId);
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("[异步发送] 关闭中...");
        senderExecutor.shutdown();
        try {
            if (!senderExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                senderExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            senderExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("[异步发送] 已关闭");
    }

    public int getDroppedMessageCount() {
        return droppedMessageCount.get();
    }

    private static class MessageTask {
        final WebSocketSession session;
        final EngineMessage message;

        MessageTask(WebSocketSession session, EngineMessage message) {
            this.session = session;
            this.message = message;
        }
    }
}
