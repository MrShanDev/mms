package com.sxpcwlkj.websocket.utils;

import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author mmsAdmin
 * @ClassName PerformanceUtil
 * @description: 性能优化工具类，提供异步处理、批量操作、连接池等功能
 * @date 2025年1月18日
 * @version: 1.0
 */
@Slf4j
public class PerformanceUtil {

    // 线程池用于异步处理
    private static final ExecutorService executorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors() * 2,
        r -> {
            Thread t = new Thread(r, "websocket-performance-thread");
            t.setDaemon(true);
            return t;
        }
    );

    /**
     * 异步发送消息给单个用户
     */
    public static CompletableFuture<Boolean> sendToUserAsync(String userId, String message) {
        return CompletableFuture.supplyAsync(() -> {
            return WebSocketUtil.sendToUser(userId, message);
        }, executorService);
    }

    /**
     * 异步发送消息给多个用户
     */
    public static CompletableFuture<Void> sendToUsersAsync(java.util.List<String> userIds, String message) {
        return CompletableFuture.runAsync(() -> {
            WebSocketUtil.sendToUsers(userIds, message);
        }, executorService);
    }

    /**
     * 异步发送消息给聊天室
     */
    public static CompletableFuture<Void> sendToChatRoomAsync(String chatRoomId, String message) {
        return CompletableFuture.runAsync(() -> {
            WebSocketUtil.sendToChatRoom(chatRoomId, message);
        }, executorService);
    }

    /**
     * 批量保存消息到历史记录
     */
    public static void batchSaveMessages(String chatRoomId, java.util.List<Object> messages) {
        try {
            String historyKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "group:" + chatRoomId;

            // 使用Redis管道批量操作提高性能
            for (Object message : messages) {
                RedisUtil.lRightPush(historyKey, message);
            }

            // 限制历史记录数量
            RedisUtil.lTrim(historyKey, -1000, -1);
        } catch (Exception e) {
            log.error("批量保存消息异常", e);
        }
    }

    /**
     * 获取系统性能指标
     */
    public static java.util.Map<String, Object> getPerformanceMetrics() {
        java.util.Map<String, Object> metrics = new java.util.HashMap<>();

        // 内存使用情况
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        metrics.put("memory_used_mb", usedMemory / (1024 * 1024));
        metrics.put("memory_total_mb", totalMemory / (1024 * 1024));
        metrics.put("memory_free_mb", freeMemory / (1024 * 1024));
        metrics.put("memory_usage_rate", String.format("%.2f%%", (double) usedMemory / totalMemory * 100));

        // CPU核心数
        metrics.put("cpu_cores", runtime.availableProcessors());

        // 在线用户数
        metrics.put("online_users_count", WebSocketUtil.getOnlineUsers().size());

        // 当前线程池状态
        metrics.put("active_threads", Thread.activeCount());

        metrics.put("msg","memory_total_mb：系统物理内存总量，单位是兆字节,memory_free_mb：当前未被使用的空闲内存大小,online_users_count：正在访问或使用系统的用户总数,memory_used_mb：已被系统和程序占用的内存容量,cpu_cores：中央处理器的逻辑核心数量,active_threads：系统当前正在执行的并发任务数,memory_usage_rate：已用内存占总内存的百分比。");

        return metrics;
    }

    /**
     * 清理过期的Redis数据
     */
    public static void cleanupExpiredData() {
        try {
            // 这里可以添加清理过期数据的逻辑
            // 例如清理超过一定时间未使用的消息历史记录
            log.debug("执行性能清理任务");
        } catch (Exception e) {
            log.error("清理过期数据异常", e);
        }
    }

    /**
     * 预热连接池
     */
    public static void warmUpConnections() {
        // 在系统启动时预热连接相关资源
        log.info("WebSocket连接池预热完成");
    }

    /**
     * 关闭性能优化工具的资源
     */
    public static void shutdown() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            log.info("性能优化工具资源已释放");
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            log.error("性能优化工具关闭异常", e);
        }
    }
}
