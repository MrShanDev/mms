package com.sxpcwlkj.websocket.listener;

import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.Message;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author mmsAdmin
 * @ClassName MessageEventListener
 * @description: 消息事件监听器，用于异步处理消息事件和通知
 * @date 2025年1月18日
 * @version: 1.0
 */
@Component
@Slf4j
public class MessageEventListener {

    @PostConstruct
    public void init() {
        log.info("消息事件监听器初始化完成");
        // 启动异步消息处理线程
        startMessageProcessingThread();
    }

    /**
     * 启动消息处理线程
     */
    private void startMessageProcessingThread() {
        CompletableFuture.runAsync(() -> {
            log.info("消息处理线程启动");
            // 这里可以实现从Redis队列或其他消息中间件中消费消息的逻辑
            // 例如监听Redis发布/订阅频道
            // 监听聊天室消息、系统通知等
        });
    }

    /**
     * 处理消息发送事件
     */
    public void onMessageSent(Message message) {
        try {
            log.debug("消息已发送: ID={}, Type={}, Sender={}, Content={}",
                    message.getMessageId(), message.getMessageType(), message.getSenderId(), message.getContent());

            // 根据消息类型执行不同的处理逻辑
            switch (message.getMessageType()) {
                case "private":
                    handlePrivateMessageEvent(message);
                    break;
                case "group":
                    handleGroupMessageEvent(message);
                    break;
                case "broadcast":
                    handleBroadcastMessageEvent(message);
                    break;
                default:
                    log.warn("未知消息类型: {}", message.getMessageType());
            }

            // 更新消息状态
            updateMessageStatus(message.getMessageId(), "delivered");

        } catch (Exception e) {
            log.error("处理消息发送事件异常", e);
        }
    }

    /**
     * 处理私聊消息事件
     */
    private void handlePrivateMessageEvent(Message message) {
        // 记录私聊消息统计
        String statsKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "stats:private";
        RedisUtil.hIncrBy(statsKey, "total", 1L);
        RedisUtil.hIncrBy(statsKey, "today:" + java.time.LocalDate.now(), 1L);

        // 发送回执给发送方（可选）
        // WebSocketUtil.sendToUser(message.getSenderId(), "消息已送达");
    }

    /**
     * 处理群聊消息事件
     */
    private void handleGroupMessageEvent(Message message) {
        // 记录群聊消息统计
        String statsKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "stats:group";
        RedisUtil.hIncrBy(statsKey, "total", 1L);
        RedisUtil.hIncrBy(statsKey, "today:" + java.time.LocalDate.now(), 1L);

        // 记录群组特定统计
        String groupStatsKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "stats:group:" + message.getChatRoomId();
        RedisUtil.hIncrBy(groupStatsKey, "total", 1L);
        RedisUtil.hIncrBy(groupStatsKey, "today:" + java.time.LocalDate.now(), 1L);
    }

    /**
     * 处理广播消息事件
     */
    private void handleBroadcastMessageEvent(Message message) {
        // 记录广播消息统计
        String statsKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "stats:broadcast";
        RedisUtil.hIncrBy(statsKey, "total", 1L);
        RedisUtil.hIncrBy(statsKey, "today:" + java.time.LocalDate.now(), 1L);
    }

    /**
     * 更新消息状态
     */
    private void updateMessageStatus(String messageId, String status) {
        // 这里可以将消息状态更新到Redis或其他存储中
        String statusKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "status:" + messageId;
        RedisUtil.setCacheObject(statusKey, status);
    }

    /**
     * 处理消息已读事件
     */
    public void onMessageRead(String messageId, String userId) {
        try {
            // 更新消息为已读状态
            String statusKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "status:" + messageId;
            RedisUtil.setCacheObject(statusKey, "read");

            log.debug("消息 {} 已被用户 {} 标记为已读", messageId, userId);
        } catch (Exception e) {
            log.error("处理消息已读事件异常", e);
        }
    }

    /**
     * 处理用户上线事件
     */
    public void onUserOnline(String userId) {
        try {
            log.info("用户 {} 上线", userId);

            // 检查是否有离线消息需要推送
            checkAndSendOfflineMessages(userId);
        } catch (Exception e) {
            log.error("处理用户上线事件异常", e);
        }
    }

    /**
     * 处理用户下线事件
     */
    public void onUserOffline(String userId) {
        try {
            log.info("用户 {} 下线", userId);
        } catch (Exception e) {
            log.error("处理用户下线事件异常", e);
        }
    }

    /**
     * 检查并发送离线消息
     */
    private void checkAndSendOfflineMessages(String userId) {
        try {
            // 这里可以实现检查用户离线消息的逻辑
            // 例如从Redis中获取该用户的离线消息并推送给用户
            log.debug("检查用户 {} 的离线消息", userId);
        } catch (Exception e) {
            log.error("检查离线消息异常", e);
        }
    }

    /**
     * 处理聊天室成员变更事件
     */
    public void onChatRoomMemberChanged(String chatRoomId, String userId, String action) {
        try {
            log.info("聊天室 {} 成员变更: 用户 {}, 动作 {}", chatRoomId, userId, action);

            // 可以向聊天室其他成员发送通知
            String notification = String.format("用户 %s 已%s聊天室", userId,
                "join".equals(action) ? "加入" : "离开");

            // 创建系统消息并发送给聊天室成员
            Message systemMessage = new Message();
            systemMessage.setMessageType("group");
            systemMessage.setChatRoomId(chatRoomId);
            systemMessage.setSenderId("SYSTEM");
            systemMessage.setContent(notification);
            systemMessage.setContentType("system");

            WebSocketUtil.sendToChatRoom(chatRoomId, com.sxpcwlkj.common.utils.JsonUtil.toJsonString(systemMessage));
        } catch (Exception e) {
            log.error("处理聊天室成员变更事件异常", e);
        }
    }
}
