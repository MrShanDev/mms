package com.sxpcwlkj.websocket.utils;

import cn.hutool.core.date.DateUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * WebSocket性能监控工具类
 * 
 * 功能特点：
 * - 实时监控连接数
 * - 消息发送统计
 * - 性能指标收集
 * - 资源使用监控
 * 
 * @author mmsAdmin
 * @since 2025年1月19日
 */
@Slf4j
public class WebSocketPerformanceMonitor {
    
    // 消息发送计数器
    private static final AtomicLong MESSAGE_SENT_COUNTER = new AtomicLong(0);
    private static final AtomicLong MESSAGE_RECEIVED_COUNTER = new AtomicLong(0);
    private static final AtomicLong CONNECTION_COUNTER = new AtomicLong(0);
    private static final AtomicLong DISCONNECTION_COUNTER = new AtomicLong(0);
    
    /**
     * 记录消息发送
     */
    public static void recordMessageSent() {
        long count = MESSAGE_SENT_COUNTER.incrementAndGet();
        
        // 每1000条消息记录一次日志
        if (count % 1000 == 0) {
            log.info("累计发送消息数: {}", count);
        }
        
        // 更新Redis中的统计数据
        updateRedisStats("total_messages_sent", count);
    }
    
    /**
     * 记录消息接收
     */
    public static void recordMessageReceived() {
        long count = MESSAGE_RECEIVED_COUNTER.incrementAndGet();
        
        // 每1000条消息记录一次日志
        if (count % 1000 == 0) {
            log.info("累计接收消息数: {}", count);
        }
        
        // 更新Redis中的统计数据
        updateRedisStats("total_messages_received", count);
    }
    
    /**
     * 记录连接建立
     */
    public static void recordConnectionEstablished() {
        long count = CONNECTION_COUNTER.incrementAndGet();
        long currentConnections = getCurrentConnectionCount();
        
        log.info("连接建立 - 累计连接数: {}, 当前连接数: {}", count, currentConnections);
        
        // 更新Redis中的统计数据
        updateRedisStats("total_connections", count);
        updateRedisStats("current_connections", currentConnections);
    }
    
    /**
     * 记录连接断开
     */
    public static void recordConnectionClosed() {
        long count = DISCONNECTION_COUNTER.incrementAndGet();
        long currentConnections = getCurrentConnectionCount();
        
        log.info("连接断开 - 累计断开数: {}, 当前连接数: {}", count, currentConnections);
        
        // 更新Redis中的统计数据
        updateRedisStats("total_disconnections", count);
        updateRedisStats("current_connections", currentConnections);
    }
    
    /**
     * 获取当前连接数
     */
    public static long getCurrentConnectionCount() {
        try {
            // 从Redis获取在线用户数量
            return RedisUtil.sSize(SocketConstant.SOCKET_ONLINE_USERS);
        } catch (Exception e) {
            log.error("获取当前连接数失败", e);
            return 0;
        }
    }
    
    /**
     * 获取总发送消息数
     */
    public static long getTotalMessagesSent() {
        return MESSAGE_SENT_COUNTER.get();
    }
    
    /**
     * 获取总接收消息数
     */
    public static long getTotalMessagesReceived() {
        return MESSAGE_RECEIVED_COUNTER.get();
    }
    
    /**
     * 获取总连接数
     */
    public static long getTotalConnections() {
        return CONNECTION_COUNTER.get();
    }
    
    /**
     * 获取总断开连接数
     */
    public static long getTotalDisconnections() {
        return DISCONNECTION_COUNTER.get();
    }
    
    /**
     * 获取性能统计摘要
     */
    public static String getPerformanceSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== WebSocket 性能统计 ===\n");
        sb.append("时间: ").append(DateUtil.now()).append("\n");
        sb.append("当前连接数: ").append(getCurrentConnectionCount()).append("\n");
        sb.append("累计连接数: ").append(getTotalConnections()).append("\n");
        sb.append("累计断开数: ").append(getTotalDisconnections()).append("\n");
        sb.append("累计发送消息: ").append(getTotalMessagesSent()).append("\n");
        sb.append("累计接收消息: ").append(getTotalMessagesReceived()).append("\n");
        sb.append("=========================\n");
        
        return sb.toString();
    }
    
    /**
     * 更新Redis中的统计信息
     */
    private static void updateRedisStats(String key, long value) {
        try {
            String statsKey = SocketConstant.SOCKET_PERFORMANCE_STATS_PREFIX + key;
            RedisUtil.setCacheObject(statsKey, value);
            
            // 设置过期时间（例如24小时）
            RedisUtil.expire(statsKey, 24 * 60 * 60); // 24小时
        } catch (Exception e) {
            log.error("更新Redis统计信息失败 - Key: {}, Value: {}", key, value, e);
        }
    }
    
    /**
     * 记录用户活跃时间
     */
    public static void recordUserActivity(String userId) {
        try {
            String activityKey = SocketConstant.SOCKET_USER_LAST_ACTIVE + userId;
            RedisUtil.setCacheObject(activityKey, LocalDateTime.now());
            
            // 设置过期时间（例如24小时）
            RedisUtil.expire(activityKey, 24 * 60 * 60);
        } catch (Exception e) {
            log.error("记录用户活跃时间失败 - UserId: {}", userId, e);
        }
    }
    
    /**
     * 检查用户是否活跃
     */
    public static boolean isUserActive(String userId) {
        try {
            String activityKey = SocketConstant.SOCKET_USER_LAST_ACTIVE + userId;
            LocalDateTime lastActive = RedisUtil.getCacheObject(activityKey);
            
            if (lastActive != null) {
                // 检查是否在过去30分钟内活跃
                return lastActive.isAfter(LocalDateTime.now().minusMinutes(30));
            }
            
            return false;
        } catch (Exception e) {
            log.error("检查用户活跃状态失败 - UserId: {}", userId, e);
            return false;
        }
    }
}