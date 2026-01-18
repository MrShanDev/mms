package com.sxpcwlkj.websocket.entity;

import cn.hutool.core.util.IdUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author mmsAdmin
 * @ClassName Message
 * @description: 优化后的消息实体类，支持私聊、群聊、权限验证等功能
 * @date 2025年1月18日
 * @version: 2.0
 */
@Data
public class Message {

    /**
     * 消息唯一ID
     */
    private String messageId;

    /**
     * 发送者用户ID
     */
    private String senderId;

    /**
     * 接收者用户ID（私聊时使用）
     */
    private String receiverId;

    /**
     * 聊天室/群组ID
     */
    private String chatRoomId;

    /**
     * 消息类型：private(私聊), group(群聊), broadcast(广播)
     */
    private String messageType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型：text, image, file, audio, video等
     */
    private String contentType;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 是否已读
     */
    private Boolean isRead = false;

    /**
     * 消息扩展属性
     */
    private Map<String, Object> extra;

    /**
     * 消息状态：sent, delivered, read
     */
    private String status;

    public Message() {
        this.messageId = IdUtil.simpleUUID();
        this.sendTime = LocalDateTime.now();
        this.status = "sent";
    }

    /**
     * 创建私聊消息
     */
    public static Message createPrivateMessage(String senderId, String receiverId, String content) {
        Message message = new Message();
        message.setMessageType("private");
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        return message;
    }

    /**
     * 创建群聊消息
     */
    public static Message createGroupMessage(String senderId, String chatRoomId, String content) {
        Message message = new Message();
        message.setMessageType("group");
        message.setSenderId(senderId);
        message.setChatRoomId(chatRoomId);
        message.setContent(content);
        return message;
    }

    /**
     * 创建广播消息
     */
    public static Message createBroadcastMessage(String senderId, String content) {
        Message message = new Message();
        message.setMessageType("broadcast");
        message.setSenderId(senderId);
        message.setContent(content);
        return message;
    }

    /**
     * 标记消息为已读
     */
    public void markAsRead() {
        this.isRead = true;
        this.status = "read";
    }

    /**
     * 标记消息为已投递
     */
    public void markAsDelivered() {
        this.status = "delivered";
    }
}