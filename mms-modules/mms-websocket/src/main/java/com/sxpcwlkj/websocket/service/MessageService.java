package com.sxpcwlkj.websocket.service;

import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.Message;
import com.sxpcwlkj.websocket.utils.PermissionUtil;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class MessageService {

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

            // 发送消息给接收者
            boolean sent = WebSocketUtil.sendToUser(message.getReceiverId(), JSONUtil.toJsonStr(message));
            
            if (sent) {
                message.markAsDelivered();
                // 如果接收者在线，标记为已送达
                if (isReceiverOnline) {
                    log.debug("私聊消息已发送给用户 {}: {}", message.getReceiverId(), message.getContent());
                } else {
                    log.debug("私聊消息已保存，用户 {} 不在线", message.getReceiverId());
                }
                return true;
            } else {
                log.warn("发送私聊消息失败给用户 {}: {}", message.getReceiverId(), message.getContent());
                return false;
            }
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
}