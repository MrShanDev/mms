package com.sxpcwlkj.websocket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户会话实体类
 * 用于管理用户的聊天会话列表，支持置顶、免打扰等功能
 * 
 * @author mmsAdmin
 * @since 2025年1月22日
 */
@Data
@Accessors(chain = true)
@TableName("chat_user_conversation")
public class UserConversation {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 会话ID（对于私聊是对方用户ID，对于群聊是群组ID）
     */
    private String conversationId;
    
    /**
     * 会话类型：private私聊、group群聊
     */
    private String conversationType;
    
    /**
     * 是否置顶：0否 1是
     */
    private Integer isPinned;
    
    /**
     * 置顶时间
     */
    private LocalDateTime pinnedTime;
    
    /**
     * 是否免打扰：0否 1是
     */
    private Integer isMuted;
    
    /**
     * 最后一条消息时间
     */
    private LocalDateTime lastMessageTime;
    
    /**
     * 最后一条消息内容
     */
    private String lastMessageContent;
    
    /**
     * 未读消息数
     */
    private Integer unreadCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 扩展字段JSON
     */
    private String extra;
}
