package com.sxpcwlkj.websocket;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.*;
import com.sxpcwlkj.websocket.enums.ChatRoomTypeEnum;
import com.sxpcwlkj.websocket.enums.CmdEnum;
import com.sxpcwlkj.websocket.service.MessageService;
import com.sxpcwlkj.websocket.service.ChatMessageService;
import com.sxpcwlkj.websocket.utils.PermissionUtil;
import com.sxpcwlkj.websocket.utils.SecurityUtil;
import com.sxpcwlkj.websocket.utils.WebSocketPerformanceMonitor;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 高性能WebSocket业务处理类 - 支持万人级用户在线聊天
 *
 * 优化特性:
 * - 支持JDK 21虚拟线程
 * - 消息持久化到数据库
 * - Redis缓存优化
 * - 异步消息处理
 * - 消息安全验证
 * - 高效连接管理
 *
 * @author mmsAdmin
 * @since 2025年1月19日
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private final MessageService messageService;
    private final ChatMessageService chatMessageService;

    /**
     * 接收到消息
     *
     * @param session 发送人session对象
     * @param dataMsg 发送人发送的内容
     */
    public void receiverMsg(WebSocketSession session, DataMsgVo dataMsg) {
        try {
            if (dataMsg == null) {
                return;
            }

            String cmd = dataMsg.getCmd().toString();
            String userId = WebSocketUtil.getUserId(session.getId());

            //记录性能指标
            WebSocketPerformanceMonitor.recordMessageReceived();

            //心跳,返回sessionId
            if (cmd.equals(CmdEnum.SYS_PING.getValue().toString())) {
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SYS_PING).setData(Boolean.TRUE.toString()));
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            // 获取在线用户列表
            else if (cmd.equals(CmdEnum.USER_LIST.getValue().toString())) {
                //聊天列表
                List<ChatRoom> chatRoomList = new ArrayList<>();

                //=====================AI聊天室===================
                ChatRoom chatRoom = new ChatRoom()
                        //创建聊天室
                        .createdRoom("0", session, "MMS-AI客服")
                        //设置聊天室类型,获取在线人员
                        .setChatRoomType(ChatRoomTypeEnum.SYSTEM);
                // 根据聊天室ID获取历史缓存数据
                List<MsgInfoVo> msgInfoVos = getChatRoomMsg(chatRoom.getChatRoomId());
                //如果空，新增第一条信息
                if (msgInfoVos.isEmpty()) {
                    chatRoomList.add(
                            chatRoom.addMsg(new MsgInfo(chatRoom.getChatRoomId())
                                    .setUse(0L, Convert.toLong(WebSocketUtil.getUserId(session.getId())))
                                    .setMsgContent("您好,欢迎使用MMS聊天室！"))
                    );
                } else {
                    // 返回历史缓存信息
                    chatRoom.setMsgInfoList(msgInfoVos);
                    chatRoomList.add(chatRoom);
                }
                //=====================AI聊天室===================

                //TODO  添加其他在线聊天室
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.USER_LIST)
                        .setData(chatRoomList));
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            // 发送消息
            else if (cmd.equals(CmdEnum.SEND_MSG.getValue().toString())) {
                handleSendMessage(session, dataMsg, userId);
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            // 订阅消息
            else if (cmd.equals(SocketConstant.CMD_SUBSCRIBE)) {
                handleSubscribe(session, dataMsg, userId);
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            // 取消订阅
            else if (cmd.equals(SocketConstant.CMD_UNSUBSCRIBE)) {
                handleUnsubscribe(session, dataMsg, userId);
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            // 加入群组
            else if (cmd.equals(SocketConstant.CMD_JOIN_GROUP)) {
                handleJoinGroup(session, dataMsg, userId);
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            // 离开群组
            else if (cmd.equals(SocketConstant.CMD_LEAVE_GROUP)) {
                handleLeaveGroup(session, dataMsg, userId);
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            // 获取在线用户
            else if (cmd.equals(SocketConstant.CMD_GET_ONLINE_USERS)) {
                handleGetOnlineUsers(session, userId);
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            // 发送私聊消息
            else if (cmd.equals(SocketConstant.CMD_SEND_PRIVATE_MSG)) {
                handleSendPrivateMessage(session, dataMsg, userId);
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            // 发送群聊消息
            else if (cmd.equals(SocketConstant.CMD_SEND_GROUP_MSG)) {
                handleSendGroupMessage(session, dataMsg, userId);
                WebSocketPerformanceMonitor.recordMessageSent();
            }
            //其他
            else {
                log.warn("未知命令: {}", cmd);
                WebSocketPerformanceMonitor.recordMessageSent();
            }
        } catch (Exception e) {
            log.error("处理WebSocket消息异常", e);
        }
    }

    /**
     * 处理发送消息
     */
    private void handleSendMessage(WebSocketSession session, DataMsgVo dataMsg, String userId) {
        try {
            String msgData = dataMsg.getData();
            DataMsgInfoVo convert = BeanUtil.toBean(JSONUtil.toBean(msgData, JSONObject.class), DataMsgInfoVo.class);
            if (convert != null) {
                MsgInfoVo msgInfoVo = convert.getData();
                MsgUser sendUser = msgInfoVo.getSendUser();
                Object objId = StpUtil.getLoginIdByToken(sendUser.getToken());
                sendUser.setUserId(Convert.toLong(objId));
                sendUser.setSessionId(session.getId());
                sendUser.setToken("");
                msgInfoVo.setEveryone(false);
                msgInfoVo.setRecipientUser(new MsgUser());
                msgInfoVo.setSendUser(sendUser);
                List<MsgInfoVo> msgInfoVos = new ArrayList<>();
                msgInfoVos.add(msgInfoVo);

                setChatRoomMsg(msgInfoVo.getChatRoomId(), msgInfoVos);
            }
        } catch (Exception e) {
            log.error("处理发送消息异常", e);
        }
    }

    /**
     * 处理订阅消息
     */
    private void handleSubscribe(WebSocketSession session, DataMsgVo dataMsg, String userId) {
        try {
            // 解析订阅参数
            String subscribeData = dataMsg.getData();
            JSONObject json = JSONUtil.parseObj(subscribeData);
            String channel = json.getStr("channel");

            // 将用户添加到订阅频道
            String subscriptionKey = SocketConstant.SOCKET_USER_SUBSCRIPTIONS_PREFIX + userId;
            RedisUtil.setCacheSet(subscriptionKey, java.util.Collections.singleton(channel));

            // 发送确认消息
            WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                    .setData("订阅成功: " + channel));

            log.info("用户 {} 订阅频道: {}", userId, channel);
        } catch (Exception e) {
            log.error("处理订阅消息异常", e);
        }
    }

    /**
     * 处理取消订阅消息
     */
    private void handleUnsubscribe(WebSocketSession session, DataMsgVo dataMsg, String userId) {
        try {
            // 解析取消订阅参数
            String unsubscribeData = dataMsg.getData();
            JSONObject json = JSONUtil.parseObj(unsubscribeData);
            String channel = json.getStr("channel");

            // 从订阅频道移除用户
            String subscriptionKey = SocketConstant.SOCKET_USER_SUBSCRIPTIONS_PREFIX + userId;
            java.util.Set<String> currentChannels = RedisUtil.getCacheSet(subscriptionKey);
            if (currentChannels != null && currentChannels.contains(channel)) {
                currentChannels.remove(channel);
                if (!currentChannels.isEmpty()) {
                    RedisUtil.setCacheSet(subscriptionKey, currentChannels);
                } else {
                    // 如果集合为空，可以删除key
                    RedisUtil.deleteObject(subscriptionKey);
                }
            }

            // 发送确认消息
            WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                    .setData("取消订阅成功: " + channel));

            log.info("用户 {} 取消订阅频道: {}", userId, channel);
        } catch (Exception e) {
            log.error("处理取消订阅消息异常", e);
        }
    }

    /**
     * 处理加入群组
     */
    private void handleJoinGroup(WebSocketSession session, DataMsgVo dataMsg, String userId) {
        try {
            // 解析群组ID
            String groupData = dataMsg.getData();
            JSONObject json = JSONUtil.parseObj(groupData);
            String chatRoomId = json.getStr("chatRoomId");

            // 验证权限并加入群组
            boolean joined = messageService.joinGroupChat(chatRoomId, userId);

            // 发送确认消息
            WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                    .setData(joined ? "加入群组成功" : "加入群组失败"));

            if (joined) {
                log.info("用户 {} 加入群组: {}", userId, chatRoomId);
            }
        } catch (Exception e) {
            log.error("处理加入群组消息异常", e);
        }
    }

    /**
     * 处理离开群组
     */
    private void handleLeaveGroup(WebSocketSession session, DataMsgVo dataMsg, String userId) {
        try {
            // 解析群组ID
            String groupData = dataMsg.getData();
            JSONObject json = JSONUtil.parseObj(groupData);
            String chatRoomId = json.getStr("chatRoomId");

            // 离开群组
            boolean left = messageService.leaveGroupChat(chatRoomId, userId);

            // 发送确认消息
            WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                    .setData(left ? "离开群组成功" : "离开群组失败"));

            if (left) {
                log.info("用户 {} 离开群组: {}", userId, chatRoomId);
            }
        } catch (Exception e) {
            log.error("处理离开群组消息异常", e);
        }
    }

    /**
     * 处理获取在线用户
     */
    private void handleGetOnlineUsers(WebSocketSession session, String userId) {
        try {
            // 检查权限
            if (!PermissionUtil.canReadMessage(userId)) {
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                        .setData("权限不足"));
                return;
            }

            // 获取在线用户列表
            java.util.Set<String> onlineUsers = WebSocketUtil.getOnlineUsers();

            // 发送在线用户列表
            WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                    .setData(new ArrayList<>(onlineUsers)));

            log.debug("用户 {} 请求在线用户列表，返回 {} 个用户", userId, onlineUsers.size());
        } catch (Exception e) {
            log.error("处理获取在线用户消息异常", e);
        }
    }

    /**
     * 处理发送私聊消息
     */
    private void handleSendPrivateMessage(WebSocketSession session, DataMsgVo dataMsg, String userId) {
        try {
            // 解析消息内容
            String messageData = dataMsg.getData();
            JSONObject json = JSONUtil.parseObj(messageData);
            String receiverId = json.getStr("receiverId");
            String content = json.getStr("content");
            String contentType = json.getStr("contentType", "text");

            // 安全验证
            if (!SecurityUtil.validateUserId(userId) || !SecurityUtil.validateUserId(receiverId)) {
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                        .setData("用户ID格式不正确"));
                log.warn("用户 {} 尝试发送私聊消息，但用户ID格式无效", userId);
                return;
            }

            if (!SecurityUtil.validateMessageContent(content)) {
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                        .setData("消息内容包含非法字符"));
                log.warn("用户 {} 发送的消息包含非法内容", userId);
                return;
            }

            // 检查速率限制
            if (SecurityUtil.isUserRateLimited(userId)) {
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                        .setData("消息发送频率过高"));
                log.warn("用户 {} 发送消息频率过高", userId);
                return;
            }

            // 清理消息内容
            content = SecurityUtil.sanitizeMessageContent(content);

            // 记录用户活动
            SecurityUtil.logUserActivity(userId, "SEND_PRIVATE_MESSAGE",
                    String.format("发送私聊消息给用户 %s, 内容: %s", receiverId, content));

            // 创建消息对象
            Message message = Message.createPrivateMessage(userId, receiverId, content);
            message.setContentType(contentType);

            // 发送私聊消息
            boolean sent = messageService.sendPrivateMessage(message);

            // 发送发送结果确认
            WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                    .setData(sent ? "私聊消息发送成功" : "私聊消息发送失败"));

            if (sent) {
                log.info("用户 {} 向用户 {} 发送私聊消息: {}", userId, receiverId, content);

                // 异步保存消息到数据库和缓存
                saveMessageToDatabase("private", userId, receiverId, null, content, contentType);
            }
        } catch (Exception e) {
            log.error("处理发送私聊消息异常", e);
        }
    }

    /**
     * 处理发送群聊消息
     */
    private void handleSendGroupMessage(WebSocketSession session, DataMsgVo dataMsg, String userId) {
        try {
            // 解析消息内容
            String messageData = dataMsg.getData();
            JSONObject json = JSONUtil.parseObj(messageData);
            String chatRoomId = json.getStr("chatRoomId");
            String content = json.getStr("content");
            String contentType = json.getStr("contentType", "text");

            // 安全验证
            if (!SecurityUtil.validateUserId(userId) || !SecurityUtil.validateChatRoomId(chatRoomId)) {
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                        .setData("用户ID或聊天室ID格式不正确"));
                log.warn("用户 {} 尝试发送群聊消息，但ID格式无效", userId);
                return;
            }

            if (!SecurityUtil.validateMessageContent(content)) {
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                        .setData("消息内容包含非法字符"));
                log.warn("用户 {} 发送的消息包含非法内容", userId);
                return;
            }

            // 检查速率限制
            if (SecurityUtil.isUserRateLimited(userId)) {
                WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                        .setData("消息发送频率过高"));
                log.warn("用户 {} 发送消息频率过高", userId);
                return;
            }

            // 清理消息内容
            content = SecurityUtil.sanitizeMessageContent(content);

            // 记录用户活动
            SecurityUtil.logUserActivity(userId, "SEND_GROUP_MESSAGE",
                    String.format("在群组 %s 发送消息, 内容: %s", chatRoomId, content));

            // 创建消息对象
            Message message = Message.createGroupMessage(userId, chatRoomId, content);
            message.setContentType(contentType);

            // 发送群聊消息
            boolean sent = messageService.sendGroupMessage(message);

            // 发送发送结果确认
            WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED)
                    .setData(sent ? "群聊消息发送成功" : "群聊消息发送失败"));

            if (sent) {
                log.info("用户 {} 在群组 {} 发送群聊消息: {}", userId, chatRoomId, content);

                // 异步保存消息到数据库和缓存
                saveMessageToDatabase("group", userId, null, chatRoomId, content, contentType);
            }
        } catch (Exception e) {
            log.error("处理发送群聊消息异常", e);
        }
    }


    /**
     * 获取聊天室消息
     *
     * @param chatRoomId 聊天室id
     * @return 聊天室消息
     */
    public static List<MsgInfoVo> getChatRoomMsg(Long chatRoomId) {
        List<MsgInfoVo> list = new ArrayList<>();
        try {
            list = RedisUtil.getCacheList(SocketConstant.SOCKET_ROOM + chatRoomId.toString());
        } catch (Exception e) {
            log.error("获取聊天室消息异常", e);
        }
        return list;
    }

    /**
     * 设置聊天室消息
     *
     * @param chatRoomId  聊天室id
     * @param msgInfoList 聊天室消息
     */
    public static void setChatRoomMsg(Long chatRoomId, List<MsgInfoVo> msgInfoList) {
        RedisUtil.setCacheList(SocketConstant.SOCKET_ROOM + chatRoomId.toString(), msgInfoList);
    }

    /**
     * 异步保存消息到数据库
     */
    @Async("virtualThreadExecutor")
    private CompletableFuture<Void> saveMessageToDatabase(String messageType, String senderId, String receiverId,
                                     String chatRoomId, String content, String contentType) {
        try {
            com.sxpcwlkj.websocket.entity.ChatMessage chatMessage = new com.sxpcwlkj.websocket.entity.ChatMessage()
                .setSenderId(senderId)
                .setReceiverId(receiverId)
                .setChatRoomId(chatRoomId)
                .setMessageType(messageType)
                .setContent(content)
                .setContentType(contentType)
                .setStatus("normal")
                .setCreateTime(LocalDateTime.now());

            // 异步保存到数据库和缓存
            chatMessageService.saveMessageAsync(chatMessage);

            log.debug("消息已异步保存到数据库: Type={}, From={}, To={}", messageType, senderId, receiverId);
        } catch (Exception e) {
            log.error("异步保存消息失败", e);
        }

        return CompletableFuture.completedFuture(null);
    }
}
