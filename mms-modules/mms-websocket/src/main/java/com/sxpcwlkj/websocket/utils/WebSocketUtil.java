package com.sxpcwlkj.websocket.utils;

import cn.hutool.core.convert.Convert;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.MsgUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xijue
 * @ClassName WebSocketSessionManager
 * @description: session信息维护类
 * @date 2024年10月23日
 * @version: 1.0
 */
@Slf4j
public class WebSocketUtil {

    /**
     * 保存连接 session 的地方
     */
    public static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加 session
     * 验证 session对象是否有userId
     */
    public static void add(WebSocketSession session) {

        Map<String, Object> attributes = session.getAttributes();
        //是否存在userId
        if (attributes.containsKey(SocketConstant.SOCKET_ID)) {
            //握手时保存的值
            Object userId = attributes.get(SocketConstant.SOCKET_ID);
            if (userId != null) {
                //保存session id 和 user id 关系(知道userId，可以给用户发送信息)
                RedisUtil.setCacheObject(SocketConstant.SOCKET_USER_ID + userId, session.getId());
                RedisUtil.setCacheObject(SocketConstant.SOCKET_SESSION_ID + session.getId(), userId);
                MsgUser msgUser = new MsgUser();
                msgUser.setUserId(Convert.toLong(userId));
                msgUser.setSessionId(session.getId());
                msgUser.setName("未设置");
                msgUser.setHeadPortrait("https://picsum.photos/30/30");
                RedisUtil.setCacheObject(SocketConstant.SOCKET_USER+msgUser.getUserId(),msgUser);
                // 添加 session
                SESSION_POOL.put(session.getId(), session);
            }
        }
    }

    /**
     * 删除并同步关闭连接
     */
    public static void close(String sessionId) {
        WebSocketSession session =null;
        WebSocketSession socketSession = SESSION_POOL.get(sessionId);
        if (socketSession != null) {
            // 删除 session
            session= SESSION_POOL.remove(sessionId);
        }

        if (session != null) {
            try {
                String userId = getUserId(sessionId);
                RedisUtil.deleteObject(SocketConstant.SOCKET_SESSION_ID + sessionId);
                if (StringUtils.isNoneEmpty(userId)) {
                    RedisUtil.deleteObject(SocketConstant.SOCKET_USER_ID + userId);
                }
                // 关闭连接
                session.close();
            } catch (IOException e) {
                // todo: 关闭出现异常处理,添加错误处理逻辑
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得 session
     */
    public static WebSocketSession get(String sessionId) {
        // 获得 session
        return SESSION_POOL.get(sessionId);
    }


    /**
     * 根据sessionId 获取 用户ID
     */
    public static String getUserId(String sessionId) {
        return RedisUtil.getCacheObject(SocketConstant.SOCKET_SESSION_ID + sessionId);
    }

    /**
     * 根据用户ID 获取 sessionId
     */
    public static String getSessionId(String userId) {
        return RedisUtil.getCacheObject(SocketConstant.SOCKET_USER_ID + userId);
    }

//    =====================================发送消息==================================
    /**
     * 发送消息
     */
    public static void sendMsg(WebSocketSession session, String data) throws IOException {
        try {
            session.sendMessage(new TextMessage(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发消息/广播消息
     */
    public static void sendMessage(String text) {
        try {
            for (WebSocketSession session : WebSocketUtil.SESSION_POOL.values()) {
                session.sendMessage(new TextMessage(text));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 指定发消息
     */
    public static void sendMessage(String userName, String text) {
        WebSocketSession webSocketSession = WebSocketUtil.get(userName);
        if (webSocketSession == null || !webSocketSession.isOpen()) return;
        try {
            webSocketSession.sendMessage(new TextMessage(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
