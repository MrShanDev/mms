package com.sxpcwlkj.ai.utils;

import com.sxpcwlkj.ai.service.OllamaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 上下文对话队列管理工具类
 * 使用说明：
 * 1. 自动从配置读取 ollama.maxContextTurns 属性
 * 2. 使用 getContext(sessionId) 获取指定会话的上下文队列
 * 3. 自动每30秒持久化上下文
 * @author shanpengnian
 */
@Component
public class DequeUtil {
    /**
     * 上下文存储器（线程安全）
     * Key: 会话ID
     * Value: 对话消息队列（双端队列）
     */
    private final Map<String, Deque<OllamaService.ChatMessage>> contextStore = new ConcurrentHashMap<>();

    /**
     * 最大对话轮次（从配置读取）
     * 每轮包含一问一答两个消息
     */
    private final int maxContextTurns;

    /**
     * 队列容量 = 最大轮次 * 2（每个轮次包含两个消息）
     */
    private final int queueCapacity;

    public DequeUtil(@Value("${mms.ai.context.max-turns:10}") int maxContextTurns) {
        this.maxContextTurns = maxContextTurns;
        this.queueCapacity = maxContextTurns * 2;
    }
    /**
     * 获取或创建指定会话的上下文队列
     * @param sessionId 会话ID
     * @return 线程安全的双端队列
     */
    public Deque<OllamaService.ChatMessage> getContext(String sessionId) {
        return contextStore.computeIfAbsent(sessionId, k -> new LinkedBlockingDeque<>(queueCapacity));
    }

    /**
     * 添加 上下文管理方法（线程安全）
     * @param context 上下文
     * @param message 消息
     */
    public synchronized void addMessageToContext(Deque<OllamaService.ChatMessage> context, OllamaService.ChatMessage message) {
        // 移除最旧的消息当达到容量限制
        if (context.size() >= maxContextTurns) {
            //移除第一条消息记录
            context.removeFirst();
        }
        // 智能过滤，添加新消息
        this.smartTrimContext(context);
        context.addLast(message);
    }

    /**
     * 上下文构建工具方法
     * 需要将之前的历史聊天问题和回复内容添加进去
     * @param context 上下文
     * @return 构建后的上下文
     */
    public String buildContextPrompt(Deque<OllamaService.ChatMessage> context) {
        StringBuilder prompt = new StringBuilder();
        context.forEach(msg -> prompt.append(msg.role()).append(": ").append(msg.content()).append("\n"));
        return prompt.toString();
    }

    /**
     * 定期持久化上下文（每30秒执行）
     */
    @Scheduled(fixedRate = 30_000)
    public void persistContexts() {
        contextStore.forEach((sessionId, context) -> {
            // 实际持久化逻辑应替换此输出
            System.out.println("Persisting context for session: " + sessionId);
            // Redis替换为实际持久化逻辑
            //redisTemplate.opsForValue().set(
            //    "chat:context:" + sessionId,
            //    new ArrayList<>(context),
            //    1, TimeUnit.HOURS // 设置合理过期时间
            //);
        });
        System.out.println("Total sessions persisted: " + contextStore.size());
    }

    /**
     * 清理指定会话的上下文
     * @param sessionId 要清理的会话ID
     */
    public void cleanupContext(String sessionId) {
        contextStore.remove(sessionId);
        System.out.println("Cleaned up context for session: " + sessionId);
    }
    // 添加定期清理过期会话的定时任务
    @Scheduled(fixedRate = 60_000)
    public void cleanupExpiredSessions() {
        // 实现会话过期逻辑（如最近访问时间超过阈值）
    }

    /**
     * 获取当前存储的所有会话ID
     * @return 会话ID集合
     */
    public Set<String> getActiveSessions() {
        return Collections.unmodifiableSet(contextStore.keySet());
    }

    /**
     * Token计数
     * @param text text
     * @return 数量
     */
    private int calculateTokens(String text) {
        // 实现简单的token估算,按照空格截取计算
        return text.split(" ").length;
    }
    /**
     * 上下文是否已满
     * @param context 上下文
     * @return 是否满
     */
    private boolean isContextFull(Deque<OllamaService.ChatMessage> context) {
        int totalTokens = context.stream()
            .mapToInt(msg -> calculateTokens(msg.content()))
            .sum();
        // 根据模型max_tokens调整
        return totalTokens > 2000;
    }
    /**
     * 智能裁剪上下文
     *
     * @param context 上下文
     */
    public void smartTrimContext(Deque<OllamaService.ChatMessage> context) {
        while (isContextFull(context)) {
            // 同时移除配对的user/assistant消息
            context.removeFirst();
            context.removeFirst();
        }
    }
}
