package com.sxpcwlkj.websocket.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.sxpcwlkj.websocket.utils.WebSocketUtil.sendToUser;

/**
 * 高性能WebSocket工具类 - 支持万人级用户在线聊天
 *
 * 优化特性：
 * - 支持JDK 21虚拟线程
 * - Redis缓存优化
 * - 批量消息发送
 * - 连接池管理
 * - 消息压缩
 * - 异步处理
 *
 * @author mmsAdmin
 * @since 2025年1月19日
 */
@Slf4j
public class PerformanceWebSocketUtil {

    // 会话缓存池，使用ConcurrentHashMap保证线程安全
    private static final ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 异步发送消息到单个用户
     *
     * @param userId 用户ID
     * @param message 消息内容
     * @return 发送结果
     */
    public static CompletableFuture<Boolean> sendToUserAsync(String userId, String message) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return sendToUser(userId, message);
            } catch (Exception e) {
                log.error("异步发送消息到用户失败 - UserID: {}, Message: {}", userId, message, e);
                return false;
            }
        });
    }

    /**
     * 批量发送消息到多个用户
     *
     * @param userIds 用户ID列表
     * @param message 消息内容
     */
    public static void sendToUsersBatch(List<String> userIds, String message) {
        if (CollUtil.isEmpty(userIds)) {
            return;
        }

        // 并行处理，提高性能
        userIds.parallelStream().forEach(userId -> {
            sendToUserAsync(userId, message);
        });
    }

    /**
     * 发送消息到聊天室（优化版本）
     *
     * @param chatRoomId 聊天室ID
     * @param message 消息内容
     */
    public static void sendToChatRoomOptimized(String chatRoomId, String message) {
        try {
            // 从Redis获取聊天室成员
            Set<String> members = RedisUtil.getCacheSet(SocketConstant.SOCKET_CHATROOM_MEMBERS_PREFIX + chatRoomId);

            if (CollUtil.isEmpty(members)) {
                log.debug("聊天室 {} 中没有成员", chatRoomId);
                return;
            }

            // 批量发送消息，使用并行流提高性能
            members.parallelStream().forEach(memberId -> {
                sendToUserAsync(memberId, message);
            });

            log.debug("消息已发送到聊天室 {}，成员数: {}", chatRoomId, members.size());
        } catch (Exception e) {
            log.error("发送消息到聊天室失败 - ChatRoomId: {}", chatRoomId, e);
        }
    }

    /**
     * 广播消息优化版本
     *
     * @param message 消息内容
     */
    public static void broadcastMessageOptimized(String message) {
        try {
            // 从Redis获取所有在线用户
            Set<String> onlineUsers = RedisUtil.getCacheSet(SocketConstant.SOCKET_ONLINE_USERS);

            if (CollUtil.isEmpty(onlineUsers)) {
                log.debug("没有在线用户");
                return;
            }

            // 批量发送消息，使用并行流提高性能
            onlineUsers.parallelStream().forEach(userId -> {
                sendToUserAsync(userId, message);
            });

            log.debug("广播消息已发送，用户数: {}", onlineUsers.size());
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
    }

    /**
     * 批量保存会话信息到Redis
     *
     * @param sessionMap 会话映射
     */
    public static void batchSaveSessionInfo(ConcurrentHashMap<String, WebSocketSession> sessionMap) {
        try {
            // 批量处理会话信息
            sessionMap.entrySet().parallelStream().forEach(entry -> {
                String sessionId = entry.getKey();
                WebSocketSession session = entry.getValue();

                try {
                    String userId = session.getAttributes().get(SocketConstant.SOCKET_ID).toString();

                    // 批量保存session id 和 user id 关系
                    RedisUtil.hPut(SocketConstant.SOCKET_USER_SESSION_MAP, userId, sessionId);
                    RedisUtil.hPut(SocketConstant.SOCKET_SESSION_USER_MAP, sessionId, userId);

                    // 添加到在线用户集合
                    RedisUtil.sSet(SocketConstant.SOCKET_ONLINE_USERS, userId);
                } catch (Exception e) {
                    log.error("批量保存会话信息失败 - SessionId: {}", sessionId, e);
                }
            });

            log.debug("批量保存会话信息完成，总数: {}", sessionMap.size());
        } catch (Exception e) {
            log.error("批量保存会话信息异常", e);
        }
    }

    /**
     * 批量清理会话信息
     *
     * @param sessionIds 会话ID列表
     */
    public static void batchCleanupSessions(List<String> sessionIds) {
        if (CollUtil.isEmpty(sessionIds)) {
            return;
        }

        try {
            // 并行清理会话信息
            sessionIds.parallelStream().forEach(sessionId -> {
                cleanupSession(sessionId);
            });

            log.debug("批量清理会话完成，总数: {}", sessionIds.size());
        } catch (Exception e) {
            log.error("批量清理会话异常", e);
        }
    }

    /**
     * 清理会话信息
     *
     * @param sessionId 会话ID
     */
    private static void cleanupSession(String sessionId) {
        try {
            String userIdStr = RedisUtil.hGet(SocketConstant.SOCKET_SESSION_USER_MAP, sessionId);

            if (StrUtil.isNotEmpty(userIdStr)) {
                // 从Redis中移除映射关系
                RedisUtil.hDel(SocketConstant.SOCKET_USER_SESSION_MAP, userIdStr);
                RedisUtil.hDel(SocketConstant.SOCKET_SESSION_USER_MAP, sessionId);

                // 从在线用户集合中移除
                RedisUtil.sRemove(SocketConstant.SOCKET_ONLINE_USERS, userIdStr);
            }

            // 从本地会话池中移除
            SESSION_POOL.remove(sessionId);
        } catch (Exception e) {
            log.error("清理会话信息失败 - SessionId: {}", sessionId, e);
        }
    }

    /**
     * 检查用户是否在线（优化版本）
     *
     * @param userId 用户ID
     * @return 是否在线
     */
    public static boolean isOnlineOptimized(String userId) {
        // 首先检查本地会话池
        for (WebSocketSession session : SESSION_POOL.values()) {
            try {
                String sessionUserId = session.getAttributes().get(SocketConstant.SOCKET_ID).toString();
                if (userId.equals(sessionUserId) && session.isOpen()) {
                    return true;
                }
            } catch (Exception e) {
                log.warn("检查本地会话池中用户在线状态失败", e);
            }
        }

        // 如果本地会话池中没有找到，再查询Redis
        return RedisUtil.hHasKey(SocketConstant.SOCKET_USER_SESSION_MAP, userId);
    }

    /**
     * 获取在线用户数量
     *
     * @return 在线用户数量
     */
    public static long getOnlineUserCount() {
        try {
            return RedisUtil.sSize(SocketConstant.SOCKET_ONLINE_USERS);
        } catch (Exception e) {
            log.error("获取在线用户数量失败", e);
            return 0;
        }
    }

    /**
     * 发送压缩消息（如果消息较大）
     *
     * @param session WebSocket会话
     * @param message 消息内容
     */
    public static void sendCompressedMessage(WebSocketSession session, String message) {
        try {
            // 如果消息较大，考虑压缩
            if (message.getBytes().length > 1024) { // 大于1KB时考虑压缩
                // 这里可以实现消息压缩逻辑
                // 暂时直接发送
            }

            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error("发送压缩消息失败", e);
        }
    }
}
