package com.sxpcwlkj.websocket.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxpcwlkj.websocket.entity.UserConversation;
import com.sxpcwlkj.websocket.mapper.UserConversationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户会话服务类
 * 
 * @author mmsAdmin
 * @since 2025年1月22日
 */
@Service
@Slf4j
public class UserConversationService extends ServiceImpl<UserConversationMapper, UserConversation> {
    
    /**
     * 获取或创建会话
     * 
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param conversationType 会话类型
     * @return 会话对象
     */
    public UserConversation getOrCreateConversation(String userId, String conversationId, String conversationType) {
        UserConversation conversation = baseMapper.selectByUserAndConversation(userId, conversationId, conversationType);
        
        if (conversation == null) {
            conversation = new UserConversation();
            conversation.setUserId(userId);
            conversation.setConversationId(conversationId);
            conversation.setConversationType(conversationType);
            conversation.setIsPinned(0);
            conversation.setIsMuted(0);
            conversation.setUnreadCount(0);
            conversation.setCreateTime(LocalDateTime.now());
            conversation.setUpdateTime(LocalDateTime.now());
            this.save(conversation);
        }
        
        return conversation;
    }
    
    /**
     * 置顶会话
     * 
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param conversationType 会话类型
     * @return 是否成功
     */
    public boolean pinConversation(String userId, String conversationId, String conversationType) {
        try {
            UserConversation conversation = getOrCreateConversation(userId, conversationId, conversationType);
            conversation.setIsPinned(1);
            conversation.setPinnedTime(LocalDateTime.now());
            conversation.setUpdateTime(LocalDateTime.now());
            this.updateById(conversation);
            
            log.info("用户 {} 置顶会话 {}", userId, conversationId);
            return true;
        } catch (Exception e) {
            log.error("置顶会话失败", e);
            return false;
        }
    }
    
    /**
     * 取消置顶
     * 
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param conversationType 会话类型
     * @return 是否成功
     */
    public boolean unpinConversation(String userId, String conversationId, String conversationType) {
        try {
            UserConversation conversation = baseMapper.selectByUserAndConversation(userId, conversationId, conversationType);
            if (conversation != null) {
                conversation.setIsPinned(0);
                conversation.setPinnedTime(null);
                conversation.setUpdateTime(LocalDateTime.now());
                this.updateById(conversation);
            }
            
            log.info("用户 {} 取消置顶会话 {}", userId, conversationId);
            return true;
        } catch (Exception e) {
            log.error("取消置顶失败", e);
            return false;
        }
    }
    
    /**
     * 设置免打扰
     * 
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param conversationType 会话类型
     * @param muted 是否免打扰
     * @return 是否成功
     */
    public boolean setMute(String userId, String conversationId, String conversationType, boolean muted) {
        try {
            UserConversation conversation = getOrCreateConversation(userId, conversationId, conversationType);
            conversation.setIsMuted(muted ? 1 : 0);
            conversation.setUpdateTime(LocalDateTime.now());
            this.updateById(conversation);
            
            log.info("用户 {} {} 会话 {} 免打扰", userId, muted ? "开启" : "关闭", conversationId);
            return true;
        } catch (Exception e) {
            log.error("设置免打扰失败", e);
            return false;
        }
    }
    
    /**
     * 更新会话最后消息
     * 
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param conversationType 会话类型
     * @param lastMessageContent 最后消息内容
     */
    public void updateLastMessage(String userId, String conversationId, String conversationType, String lastMessageContent) {
        try {
            UserConversation conversation = getOrCreateConversation(userId, conversationId, conversationType);
            conversation.setLastMessageTime(LocalDateTime.now());
            conversation.setLastMessageContent(lastMessageContent);
            conversation.setUpdateTime(LocalDateTime.now());
            this.updateById(conversation);
        } catch (Exception e) {
            log.error("更新会话最后消息失败", e);
        }
    }
    
    /**
     * 增加未读数
     * 
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param conversationType 会话类型
     */
    public void incrementUnreadCount(String userId, String conversationId, String conversationType) {
        try {
            UserConversation conversation = getOrCreateConversation(userId, conversationId, conversationType);
            conversation.setUnreadCount(conversation.getUnreadCount() + 1);
            conversation.setUpdateTime(LocalDateTime.now());
            this.updateById(conversation);
        } catch (Exception e) {
            log.error("增加未读数失败", e);
        }
    }
    
    /**
     * 清空未读数
     * 
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param conversationType 会话类型
     */
    public void clearUnreadCount(String userId, String conversationId, String conversationType) {
        try {
            UserConversation conversation = baseMapper.selectByUserAndConversation(userId, conversationId, conversationType);
            if (conversation != null) {
                conversation.setUnreadCount(0);
                conversation.setUpdateTime(LocalDateTime.now());
                this.updateById(conversation);
            }
        } catch (Exception e) {
            log.error("清空未读数失败", e);
        }
    }
    
    /**
     * 获取用户会话列表（按置顶和时间排序）
     * 
     * @param userId 用户ID
     * @return 会话列表
     */
    public List<UserConversation> getUserConversations(String userId) {
        return baseMapper.selectUserConversations(userId);
    }
    
    /**
     * 删除会话
     * 
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param conversationType 会话类型
     * @return 是否成功
     */
    public boolean deleteConversation(String userId, String conversationId, String conversationType) {
        try {
            LambdaQueryWrapper<UserConversation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserConversation::getUserId, userId)
                   .eq(UserConversation::getConversationId, conversationId)
                   .eq(UserConversation::getConversationType, conversationType);
            this.remove(wrapper);
            
            log.info("用户 {} 删除会话 {}", userId, conversationId);
            return true;
        } catch (Exception e) {
            log.error("删除会话失败", e);
            return false;
        }
    }
}
