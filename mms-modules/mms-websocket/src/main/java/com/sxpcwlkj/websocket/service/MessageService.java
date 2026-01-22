package com.sxpcwlkj.websocket.service;

import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.Message;
import com.sxpcwlkj.websocket.entity.ChatMessage;
import com.sxpcwlkj.websocket.utils.PermissionUtil;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mmsAdmin
 * @ClassName MessageService
 * @description: 消息服务类，处理私聊、群聊、权限验证等功能
 * @date 2025年1月18日
 * @version: 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    
    private final ChatMessageService chatMessageService;
    private final OfflineMessageService offlineMessageService;
    
    /**
     * 消息可撤回的最大时间（分钟）
     */
    private static final int MAX_RECALL_MINUTES = 2;

    /**
     * 发送私聊消息
     *
     * @param message 消息对象
     * @return 是否发送成功
     */
    public boolean sendPrivateMessage(Message message) {
        try {
            // 验证权限
            if (!PermissionUtil.canSendPrivateMessage(message.getSenderId(), message.getReceiverId())) {
                log.warn("用户 {} 无权限向用户 {} 发送私聊消息", message.getSenderId(), message.getReceiverId());
                return false;
            }

            // 检查接收者是否在线
            boolean isReceiverOnline = WebSocketUtil.isOnline(message.getReceiverId());

            // 保存消息到历史记录
            saveMessageHistory(message);

            // 发送消息给接收者（在线直接发送，离线存储）
            boolean sent = offlineMessageService.sendOrStore(message.getReceiverId(), message);
            
            if (sent) {
                message.markAsDelivered();
                log.debug("私聊消息已发送给用户 {}: {}", message.getReceiverId(), message.getContent());
            } else {
                log.debug("用户 {} 不在线，消息已存入离线队列", message.getReceiverId());
            }
            return true;
        } catch (Exception e) {
            log.error("发送私聊消息异常", e);
            return false;
        }
    }

    /**
     * 发送群聊消息
     *
     * @param message 消息对象
     * @return 是否发送成功
     */
    public boolean sendGroupMessage(Message message) {
        try {
            // 验证权限
            if (!PermissionUtil.canSendGroupMessage(message.getSenderId(), message.getChatRoomId())) {
                log.warn("用户 {} 无权限在群组 {} 中发送消息", message.getSenderId(), message.getChatRoomId());
                return false;
            }

            // 保存消息到历史记录
            saveMessageHistory(message);

            // 发送消息给群组所有成员（除了发送者自己）
            Set<String> members = WebSocketUtil.getChatRoomMembers(message.getChatRoomId());
            members.remove(message.getSenderId()); // 不发送给自己
            
            WebSocketUtil.sendToUsers(members.stream().toList(), JSONUtil.toJsonStr(message));

            log.debug("群聊消息已发送到群组 {}: {}", message.getChatRoomId(), message.getContent());
            return true;
        } catch (Exception e) {
            log.error("发送群聊消息异常", e);
            return false;
        }
    }

    /**
     * 发送广播消息
     *
     * @param message 消息对象
     * @return 是否发送成功
     */
    public boolean sendBroadcastMessage(Message message) {
        try {
            // 验证权限（通常只有系统管理员可以发送广播消息）
            if (!PermissionUtil.canManageChat(message.getSenderId())) {
                log.warn("用户 {} 无权限发送广播消息", message.getSenderId());
                return false;
            }

            // 保存消息到历史记录
            saveMessageHistory(message);

            // 广播消息给所有在线用户
            WebSocketUtil.broadcastMessage(JSONUtil.toJsonStr(message));

            log.debug("广播消息已发送: {}", message.getContent());
            return true;
        } catch (Exception e) {
            log.error("发送广播消息异常", e);
            return false;
        }
    }

    /**
     * 保存消息到历史记录
     *
     * @param message 消息对象
     */
    private void saveMessageHistory(Message message) {
        try {
            String historyKey;
            if (Message.class.getSimpleName().equals("private")) {
                // 私聊消息按双方ID排序生成键名
                String[] ids = new String[]{message.getSenderId(), message.getReceiverId()};
                java.util.Arrays.sort(ids);
                historyKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "private:" + ids[0] + "_" + ids[1];
            } else if ("group".equals(message.getMessageType())) {
                historyKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "group:" + message.getChatRoomId();
            } else {
                historyKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "broadcast";
            }

            // 添加消息到列表，限制历史记录数量（例如保留最近1000条消息）
            RedisUtil.lRightPush(historyKey, message);
            RedisUtil.lTrim(historyKey, -1000, -1); // 只保留最後1000條消息
        } catch (Exception e) {
            log.error("保存消息历史记录异常", e);
        }
    }

    /**
     * 获取私聊消息历史记录
     *
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @param count   获取消息数量
     * @return 消息列表
     */
    public List<Message> getPrivateChatHistory(String userId1, String userId2, int count) {
        try {
            String[] ids = new String[]{userId1, userId2};
            java.util.Arrays.sort(ids);
            String historyKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "private:" + ids[0] + "_" + ids[1];
            
            java.util.List<Object> rawMessages = RedisUtil.lGet(historyKey, -count, -1); // 获取最后count条消息
            if (rawMessages == null) {
                rawMessages = new java.util.ArrayList<>();
            }
            
            List<Message> messages = new java.util.ArrayList<>();
            for (Object obj : rawMessages) {
                if (obj instanceof Message) {
                    messages.add((Message) obj);
                }
            }
            return messages;
        } catch (Exception e) {
            log.error("获取私聊历史记录异常", e);
            return List.of();
        }
    }

    /**
     * 获取群聊消息历史记录
     *
     * @param chatRoomId 群组ID
     * @param count      获取消息数量
     * @return 消息列表
     */
    public List<Message> getGroupChatHistory(String chatRoomId, int count) {
        try {
            String historyKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "group:" + chatRoomId;
            java.util.List<Object> rawMessages = RedisUtil.lGet(historyKey, -count, -1); // 获取最后count条消息
            if (rawMessages == null) {
                rawMessages = new java.util.ArrayList<>();
            }
            
            List<Message> messages = new java.util.ArrayList<>();
            for (Object obj : rawMessages) {
                if (obj instanceof Message) {
                    messages.add((Message) obj);
                }
            }
            return messages;
        } catch (Exception e) {
            log.error("获取群聊历史记录异常", e);
            return List.of();
        }
    }

    /**
     * 创建群聊
     *
     * @param chatRoomId 群聊ID
     * @param roomName   群聊名称
     * @param creatorId  创建者ID
     * @return 是否创建成功
     */
    public boolean createGroupChat(String chatRoomId, String roomName, String creatorId) {
        try {
            // 验证创建权限
            if (!PermissionUtil.canCreateChat(creatorId)) {
                log.warn("用户 {} 无权限创建群聊", creatorId);
                return false;
            }

            // 添加创建者到群聊
            WebSocketUtil.addUserToChatRoom(chatRoomId, creatorId);

            // 保存群组信息
            String groupInfoKey = SocketConstant.SOCKET_GROUP_INFO_PREFIX + chatRoomId;
            var groupInfo = new java.util.HashMap<String, Object>();
            groupInfo.put("name", roomName);
            groupInfo.put("creator", creatorId);
            groupInfo.put("createTime", System.currentTimeMillis());
            groupInfo.put("memberCount", 1);
            RedisUtil.setCacheObject(groupInfoKey, groupInfo);

            log.info("群聊 {} 创建成功，创建者: {}", chatRoomId, creatorId);
            return true;
        } catch (Exception e) {
            log.error("创建群聊异常", e);
            return false;
        }
    }

    /**
     * 用户加入群聊
     *
     * @param chatRoomId 群聊ID
     * @param userId     用户ID
     * @return 是否加入成功
     */
    public boolean joinGroupChat(String chatRoomId, String userId) {
        try {
            // 验证加入权限
            if (!PermissionUtil.canJoinChatRoom(userId, chatRoomId)) {
                log.warn("用户 {} 无权限加入群聊 {}", userId, chatRoomId);
                return false;
            }

            // 检查用户是否已经在群聊中
            if (WebSocketUtil.isUserInChatRoom(chatRoomId, userId)) {
                log.debug("用户 {} 已在群聊 {} 中", userId, chatRoomId);
                return true;
            }

            // 添加用户到群聊
            WebSocketUtil.addUserToChatRoom(chatRoomId, userId);

            // 更新群组信息中的成员数量
            String groupInfoKey = SocketConstant.SOCKET_GROUP_INFO_PREFIX + chatRoomId;
            var groupInfo = (java.util.Map<String, Object>) RedisUtil.getCacheObject(groupInfoKey);
            if (groupInfo != null) {
                groupInfo.put("memberCount", WebSocketUtil.getChatRoomMemberCount(chatRoomId));
                RedisUtil.setCacheObject(groupInfoKey, groupInfo);
            }

            log.info("用户 {} 成功加入群聊 {}", userId, chatRoomId);
            return true;
        } catch (Exception e) {
            log.error("用户加入群聊异常", e);
            return false;
        }
    }

    /**
     * 用户离开群聊
     *
     * @param chatRoomId 群聊ID
     * @param userId     用户ID
     * @return 是否离开成功
     */
    public boolean leaveGroupChat(String chatRoomId, String userId) {
        try {
            // 移除用户从群聊
            WebSocketUtil.removeUserFromChatRoom(chatRoomId, userId);

            // 更新群组信息中的成员数量
            String groupInfoKey = SocketConstant.SOCKET_GROUP_INFO_PREFIX + chatRoomId;
            var groupInfo = (java.util.Map<String, Object>) RedisUtil.getCacheObject(groupInfoKey);
            if (groupInfo != null) {
                groupInfo.put("memberCount", WebSocketUtil.getChatRoomMemberCount(chatRoomId));
                RedisUtil.setCacheObject(groupInfoKey, groupInfo);
            }

            log.info("用户 {} 已离开群聊 {}", userId, chatRoomId);
            return true;
        } catch (Exception e) {
            log.error("用户离开群聊异常", e);
            return false;
        }
    }

    /**
     * 获取群聊成员列表
     *
     * @param chatRoomId 群聊ID
     * @return 成员列表
     */
    public Set<String> getGroupMembers(String chatRoomId) {
        try {
            return WebSocketUtil.getChatRoomMembers(chatRoomId);
        } catch (Exception e) {
            log.error("获取群聊成员列表异常", e);
            return Set.of();
        }
    }
    
    /**
     * 撤回消息
     * 
     * @param messageId 消息ID
     * @param userId 操作用户ID（必须是消息发送者）
     * @return 是否撤回成功
     */
    public boolean recallMessage(String messageId, String userId) {
        try {
            // 1. 从数据库查询消息
            ChatMessage chatMessage = chatMessageService.getById(messageId);
            if (chatMessage == null) {
                log.warn("消息 {} 不存在", messageId);
                return false;
            }
            
            // 2. 验证是否是消息发送者
            if (!userId.equals(chatMessage.getSenderId())) {
                log.warn("用户 {} 无权撤回消息 {}，消息发送者为 {}", userId, messageId, chatMessage.getSenderId());
                return false;
            }
            
            // 3. 检查是否在可撤回时间范围内
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(chatMessage.getCreateTime(), now);
            if (duration.toMinutes() > MAX_RECALL_MINUTES) {
                log.warn("消息 {} 已超过可撤回时间（{}分钟）", messageId, MAX_RECALL_MINUTES);
                return false;
            }
            
            // 4. 检查消息是否已经被撤回
            if ("recall".equals(chatMessage.getStatus())) {
                log.warn("消息 {} 已经被撤回", messageId);
                return true; // 已撤回，返回成功
            }
            
            // 5. 更新数据库状态为撤回
            chatMessage.setStatus("recall");
            chatMessage.setUpdateTime(now);
            chatMessageService.updateById(chatMessage);
            
            // 6. 通知相关用户消息已撤回
            Message recallNotice = new Message();
            recallNotice.setMessageId(messageId);
            recallNotice.setSenderId(userId);
            recallNotice.setContent("撤回了一条消息");
            recallNotice.setContentType("recall");
            recallNotice.setStatus("recall");
            
            if ("private".equals(chatMessage.getMessageType())) {
                // 私聊：通知接收者
                recallNotice.setMessageType("private");
                recallNotice.setReceiverId(chatMessage.getReceiverId());
                WebSocketUtil.sendToUser(chatMessage.getReceiverId(), JSONUtil.toJsonStr(recallNotice));
            } else if ("group".equals(chatMessage.getMessageType())) {
                // 群聊：通知群组所有成员
                recallNotice.setMessageType("group");
                recallNotice.setChatRoomId(chatMessage.getChatRoomId());
                WebSocketUtil.sendToChatRoom(chatMessage.getChatRoomId(), JSONUtil.toJsonStr(recallNotice));
            }
            
            log.info("用户 {} 成功撤回消息 {}", userId, messageId);
            return true;
        } catch (Exception e) {
            log.error("撤回消息异常 - messageId: {}, userId: {}", messageId, userId, e);
            return false;
        }
    }
    
    /**
     * 标记消息为已读
     * 
     * @param messageId 消息ID
     * @param userId 用户ID（必须是消息接收者）
     * @return 是否标记成功
     */
    public boolean markMessageAsRead(String messageId, String userId) {
        try {
            ChatMessage chatMessage = chatMessageService.getById(messageId);
            if (chatMessage == null) {
                log.warn("消息 {} 不存在", messageId);
                return false;
            }
            
            // 验证是否是消息接收者
            if (!userId.equals(chatMessage.getReceiverId()) && 
                !WebSocketUtil.isUserInChatRoom(chatMessage.getChatRoomId(), userId)) {
                log.warn("用户 {} 无权标记消息 {} 为已读", userId, messageId);
                return false;
            }
            
            // 在 Redis 中记录已读状态
            String readKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "read:" + userId + ":" + messageId;
            RedisUtil.setCacheObject(readKey, true, Duration.ofDays(30));
            
            // 发送已读回执给发送者
            Message readReceipt = new Message();
            readReceipt.setMessageId(messageId);
            readReceipt.setReceiverId(userId);
            readReceipt.setContentType("read_receipt");
            readReceipt.setStatus("read");
            
            WebSocketUtil.sendToUser(chatMessage.getSenderId(), JSONUtil.toJsonStr(readReceipt));
            
            log.debug("用户 {} 标记消息 {} 为已读", userId, messageId);
            return true;
        } catch (Exception e) {
            log.error("标记消息已读异常", e);
            return false;
        }
    }
    
    /**
     * 批量标记消息为已读
     * 
     * @param messageIds 消息ID列表
     * @param userId 用户ID
     * @return 成功标记的数量
     */
    public int markMessagesAsRead(List<String> messageIds, String userId) {
        int successCount = 0;
        for (String messageId : messageIds) {
            if (markMessageAsRead(messageId, userId)) {
                successCount++;
            }
        }
        return successCount;
    }
    
    /**
     * 获取用户未读消息数量
     * 
     * @param userId 用户ID
     * @return 未读消息数
     */
    public long getUnreadMessageCount(String userId) {
        try {
            // 从数据库查询用户的所有消息
            List<ChatMessage> messages = chatMessageService.getUserMessages(userId, 1000);
            
            long unreadCount = 0;
            for (ChatMessage msg : messages) {
                String readKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "read:" + userId + ":" + msg.getId();
                Boolean isRead = RedisUtil.getCacheObject(readKey);
                if (isRead == null || !isRead) {
                    unreadCount++;
                }
            }
            
            return unreadCount;
        } catch (Exception e) {
            log.error("获取未读消息数量异常", e);
            return 0;
        }
    }
    
    /**
     * 获取与指定用户的未读消息数（私聊）
     * 
     * @param userId 当前用户ID
     * @param otherUserId 对方用户ID
     * @return 未读消息数
     */
    public long getUnreadPrivateMessageCount(String userId, String otherUserId) {
        try {
            List<ChatMessage> messages = chatMessageService.getPrivateChatHistory(userId, otherUserId, 500);
            
            long unreadCount = 0;
            for (ChatMessage msg : messages) {
                // 只统计对方发给自己的消息
                if (msg.getSenderId().equals(otherUserId)) {
                    String readKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "read:" + userId + ":" + msg.getId();
                    Boolean isRead = RedisUtil.getCacheObject(readKey);
                    if (isRead == null || !isRead) {
                        unreadCount++;
                    }
                }
            }
            
            return unreadCount;
        } catch (Exception e) {
            log.error("获取私聊未读消息数异常", e);
            return 0;
        }
    }
}