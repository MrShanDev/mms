package com.sxpcwlkj.websocket.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.MsgUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author mmsAdmin
 * @ClassName WebSocketSessionManager
 * @description: 优化后的session信息维护类，支持分布式环境
 * @date 2024年10月23日
 * @version: 2.0
 */
@Slf4j
public class WebSocketUtil {

    /**
     * 本地缓存session（主要用于当前节点的快速查找）
     */
    public static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加 session
     * 验证 session对象是否有userId
     */
    public static void add(WebSocketSession session) {
        try {
            Map<String, Object> attributes = session.getAttributes();
            //是否存在userId
            if (attributes.containsKey(SocketConstant.SOCKET_ID)) {
                //握手时保存的值
                Object userId = attributes.get(SocketConstant.SOCKET_ID);
                if (userId != null) {
                    String sessionId = session.getId();
                    Long userIdLong = Convert.toLong(userId);
                    
                    // 保存session id 和 user id 关系(知道userId，可以给用户发送信息)
                    RedisUtil.hPut(SocketConstant.SOCKET_USER_SESSION_MAP, userIdLong.toString(), sessionId);
                    RedisUtil.hPut(SocketConstant.SOCKET_SESSION_USER_MAP, sessionId, userIdLong.toString());
                    
                    // 保存当前节点的session信息
                    SESSION_POOL.put(sessionId, session);
                    
                    // 将用户添加到在线用户集合
                    RedisUtil.sSet(SocketConstant.SOCKET_ONLINE_USERS, userIdLong.toString());
                    
                    // 更新用户信息
                    MsgUser msgUser = new MsgUser();
                    msgUser.setUserId(userIdLong);
                    msgUser.setSessionId(sessionId);
                    msgUser.setName("未设置");
                    msgUser.setHeadPortrait("https://picsum.photos/30/30");
                    RedisUtil.setCacheObject(SocketConstant.SOCKET_USER + userIdLong, msgUser);
                    
                    log.debug("用户 {} 连接到WebSocket", userIdLong);
                }
            }
        } catch (Exception e) {
            log.error("添加WebSocket会话失败", e);
        }
    }

    /**
     * 删除并同步关闭连接
     */
    public static void close(String sessionId) {
        try {
            WebSocketSession session = SESSION_POOL.remove(sessionId);
            if (session != null) {
                String userIdStr = RedisUtil.hGet(SocketConstant.SOCKET_SESSION_USER_MAP, sessionId);
                
                // 从Redis中移除映射关系
                RedisUtil.hDel(SocketConstant.SOCKET_USER_SESSION_MAP, userIdStr);
                RedisUtil.hDel(SocketConstant.SOCKET_SESSION_USER_MAP, sessionId);
                
                // 从在线用户集合中移除
                if (StringUtils.isNotEmpty(userIdStr)) {
                    RedisUtil.sRemove(SocketConstant.SOCKET_ONLINE_USERS, userIdStr);
                }
                
                // 关闭连接
                if (session.isOpen()) {
                    session.close();
                }
                
                log.debug("用户 {} 断开WebSocket连接", userIdStr);
            }
        } catch (Exception e) {
            log.error("关闭WebSocket会话失败", e);
        }
    }

    /**
     * 获得 session
     */
    public static WebSocketSession get(String sessionId) {
        // 优先从本地缓存获取
        WebSocketSession session = SESSION_POOL.get(sessionId);
        if (session != null && session.isOpen()) {
            return session;
        }
        return null;
    }

    /**
     * 根据sessionId 获取 用户ID
     */
    public static String getUserId(String sessionId) {
        return RedisUtil.hGet(SocketConstant.SOCKET_SESSION_USER_MAP, sessionId);
    }

    /**
     * 根据用户ID 获取 sessionId
     */
    public static String getSessionId(String userId) {
        return RedisUtil.hGet(SocketConstant.SOCKET_USER_SESSION_MAP, userId);
    }
    
    /**
     * 检查用户是否在线
     */
    public static boolean isOnline(String userId) {
        return RedisUtil.hHasKey(SocketConstant.SOCKET_USER_SESSION_MAP, userId);
    }
    
    /**
     * 获取所有在线用户ID
     */
    public static Set<String> getOnlineUsers() {
        return RedisUtil.sGet(SocketConstant.SOCKET_ONLINE_USERS);
    }

//    =====================================发送消息==================================
    /**
     * 发送消息
     */
    public static void sendMsg(WebSocketSession session, String data) throws IOException {
        try {
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(data));
            }
        } catch (Exception e) {
            log.error("发送消息失败", e);
            throw e;
        }
    }

    /**
     * 群发消息/广播消息
     */
    public static void broadcastMessage(String text) {
        try {
            // 从Redis获取所有在线用户
            Set<String> onlineUsers = getOnlineUsers();
            for (String userId : onlineUsers) {
                String sessionId = getSessionId(userId);
                if (sessionId != null) {
                    WebSocketSession session = get(sessionId);
                    if (session != null && session.isOpen()) {
                        sendMsg(session, text);
                    }
                }
            }
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
    }

    /**
     * 发送给指定用户
     */
    public static boolean sendToUser(String userId, String text) {
        try {
            String sessionId = getSessionId(userId);
            if (sessionId != null) {
                WebSocketSession session = get(sessionId);
                if (session != null && session.isOpen()) {
                    sendMsg(session, text);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("发送消息给用户 {} 失败", userId, e);
        }
        return false;
    }
    
    /**
     * 发送给多个用户
     */
    public static void sendToUsers(List<String> userIds, String text) {
        for (String userId : userIds) {
            sendToUser(userId, text);
        }
    }
    
    /**
     * 发送给聊天室所有成员
     */
    public static void sendToChatRoom(String chatRoomId, String text) {
        try {
            // 获取聊天室成员列表
            Set<String> members = RedisUtil.sGet(SocketConstant.SOCKET_CHATROOM_MEMBERS_PREFIX + chatRoomId);
            for (String memberId : members) {
                sendToUser(memberId, text);
            }
        } catch (Exception e) {
            log.error("发送消息到聊天室 {} 失败", chatRoomId, e);
        }
    }
    
    /**
     * 添加用户到聊天室
     */
    public static void addUserToChatRoom(String chatRoomId, String userId) {
        RedisUtil.sSet(SocketConstant.SOCKET_CHATROOM_MEMBERS_PREFIX + chatRoomId, userId);
    }
    
    /**
     * 从聊天室移除用户
     */
    public static void removeUserFromChatRoom(String chatRoomId, String userId) {
        RedisUtil.sRemove(SocketConstant.SOCKET_CHATROOM_MEMBERS_PREFIX + chatRoomId, userId);
    }
    
    /**
     * 检查用户是否在聊天室中
     */
    public static boolean isUserInChatRoom(String chatRoomId, String userId) {
        return RedisUtil.sHasKey(SocketConstant.SOCKET_CHATROOM_MEMBERS_PREFIX + chatRoomId, userId);
    }
    
    /**
     * 获取聊天室成员数量
     */
    public static long getChatRoomMemberCount(String chatRoomId) {
        return RedisUtil.sSize(SocketConstant.SOCKET_CHATROOM_MEMBERS_PREFIX + chatRoomId);
    }
    
    /**
     * 获取聊天室所有成员
     */
    public static Set<String> getChatRoomMembers(String chatRoomId) {
        return RedisUtil.sGet(SocketConstant.SOCKET_CHATROOM_MEMBERS_PREFIX + chatRoomId);
    }
    
    /**
     * 生成唯一的聊天室ID
     */
    public static String generateChatRoomId(List<String> memberIds) {
        // 将成员ID排序后拼接生成唯一ID
        List<String> sortedIds = memberIds.stream().sorted().collect(Collectors.toList());
        String combined = String.join(",", sortedIds);
        return String.valueOf(combined.hashCode());
    }
    
    /**
     * 生成全局唯一的消息ID
     */
    public static String generateMessageId() {
        return IdUtil.simpleUUID();
    }
}
