package com.sxpcwlkj.websocket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 * 用于消息持久化存储
 * 
 * @author mmsAdmin
 * @since 2025年1月19日
 */
@Data
@Accessors(chain = true)
@TableName("chat_message")
public class ChatMessage {
    
    /**
     * 消息ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 发送者ID
     */
    private String senderId;
    
    /**
     * 接收者ID（私聊时）
     */
    private String receiverId;
    
    /**
     * 聊天室ID（群聊时）
     */
    private String chatRoomId;
    
    /**
     * 消息类型：private私聊、group群聊、broadcast广播
     */
    private String messageType;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 内容类型：text文本、image图片、video视频、file文件等
     */
    private String contentType;
    
    /**
     * 消息状态：normal正常、recall撤回、delete删除
     */
    private String status;
    
    /**
     * 消息发送时间
     */
    private LocalDateTime createTime;
    
    /**
     * 消息更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 扩展字段JSON格式
     */
    private String extra;
    
    /**
     * 消息长度
     */
    private Integer contentLength;
}