package com.sxpcwlkj.websocket.config;

import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.service.ChatMessageService;
import com.sxpcwlkj.websocket.utils.WebSocketPerformanceMonitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * WebSocket定时任务配置
 *
 * 功能特点：
 * - 定期清理过期消息
 * - 清理离线用户会话
 * - 优化Redis缓存
 * - 性能监控统计
 *
 * @author mmsAdmin
 * @since 2025年1月19日
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class WebSocketScheduleConfig {

    private final ChatMessageService chatMessageService;

    /**
     * 定期清理过期消息（每天凌晨2点执行）
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredMessages() {
        try {
            log.info("开始执行消息清理任务");

            // 获取所有聊天室ID并清理过期消息（30天前的消息）
            // 由于我们无法直接获取所有聊天室ID，可以使用Redis模式匹配
            Collection<String> chatRoomKeys = RedisUtil.keys(SocketConstant.SOCKET_CHATROOM_MEMBERS_PREFIX + "*");
            for (String key : chatRoomKeys) {
                String chatRoomId = key.replace(SocketConstant.SOCKET_CHATROOM_MEMBERS_PREFIX, "");
                chatMessageService.cleanupExpiredMessages(chatRoomId, 30);
            }

            log.info("消息清理任务完成");
        } catch (Exception e) {
            log.error("执行消息清理任务失败", e);
        }
    }

    /**
     * 清理离线用户会话（每小时执行）
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void cleanupOfflineSessions() {
        try {
            log.info("开始执行离线会话清理任务");

            // 获取所有在线用户
            Set<String> onlineUsers = RedisUtil.getCacheSet(SocketConstant.SOCKET_ONLINE_USERS);

            for (String userId : onlineUsers) {
                // 检查用户是否仍然活跃
                if (!WebSocketPerformanceMonitor.isUserActive(userId)) {
                    // 如果用户不活跃，则尝试从在线用户集合中移除
                    RedisUtil.sRemove(SocketConstant.SOCKET_ONLINE_USERS, userId);
                    log.info("移除不活跃用户: {}", userId);
                }
            }

            log.info("离线会话清理任务完成");
        } catch (Exception e) {
            log.error("执行离线会话清理任务失败", e);
        }
    }

    /**
     * 优化Redis缓存（每30分钟执行）
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void optimizeRedisCache() {
        try {
            log.info("开始执行Redis缓存优化任务");

            // 清理过期的历史消息缓存
            Collection<String> historyKeys = RedisUtil.keys(SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "*");
            for (String key : historyKeys) {
                // 检查列表长度，如果超过限制则截取
                long size = RedisUtil.lSize(key);
                if (size > 1000) { // 超过1000条消息时进行截取
                    RedisUtil.lTrim(key, -1000, -1); // 保留最新的1000条
                }
            }

            log.info("Redis缓存优化任务完成");
        } catch (Exception e) {
            log.error("执行Redis缓存优化任务失败", e);
        }
    }

    /**
     * 输出性能统计报告（每小时执行）
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void printPerformanceReport() {
        try {
            log.info(WebSocketPerformanceMonitor.getPerformanceSummary());
        } catch (Exception e) {
            log.error("输出性能报告失败", e);
        }
    }
}
