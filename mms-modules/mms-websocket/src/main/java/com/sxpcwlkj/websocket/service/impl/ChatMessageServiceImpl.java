package com.sxpcwlkj.websocket.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.ChatMessage;
import com.sxpcwlkj.websocket.mapper.ChatMessageMapper;
import com.sxpcwlkj.websocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 聊天消息服务实现类
 * 
 * 功能特点：
 * - 支持消息持久化到数据库
 * - Redis缓存优化
 * - 异步批量处理
 * - 消息历史查询
 * - 消息清理策略
 * 
 * @author mmsAdmin
 * @since 2025年1月19日
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
    
    private static final int MAX_CACHE_SIZE = 1000; // 每个聊天室的最大缓存消息数
    private static final int BATCH_INSERT_SIZE = 100; // 批量插入大小
    
    /**
     * 保存消息到数据库和缓存
     * 
     * @param message 消息对象
     */
    @Override
    @Async("virtualThreadExecutor")
    public CompletableFuture<Void> saveMessageAsync(ChatMessage message) {
        try {
            // 设置默认值
            message.setCreateTime(LocalDateTime.now());
            message.setUpdateTime(LocalDateTime.now());
            message.setStatus("normal");
            if (message.getContentLength() == null && message.getContent() != null) {
                message.setContentLength(message.getContent().length());
            }
            
            // 保存到数据库
            this.save(message);
            
            // 更新缓存
            updateCache(message);
            
            log.debug("消息保存成功 - ID: {}, Type: {}", message.getId(), message.getMessageType());
        } catch (Exception e) {
            log.error("异步保存消息失败", e);
        }
        
        return CompletableFuture.completedFuture(null);
    }
    
    /**
     * 批量保存消息
     * 
     * @param messages 消息列表
     */
    @Override
    @Async("virtualThreadExecutor")
    public CompletableFuture<Void> saveMessagesBatchAsync(List<ChatMessage> messages) {
        try {
            if (CollUtil.isEmpty(messages)) {
                return CompletableFuture.completedFuture(null);
            }
            
            // 批量处理消息
            List<ChatMessage> processedMessages = messages.stream()
                    .peek(msg -> {
                        msg.setCreateTime(LocalDateTime.now());
                        msg.setUpdateTime(LocalDateTime.now());
                        msg.setStatus("normal");
                        if (msg.getContentLength() == null && msg.getContent() != null) {
                            msg.setContentLength(msg.getContent().length());
                        }
                    })
                    .collect(Collectors.toList());
            
            // 分批保存到数据库
            for (int i = 0; i < processedMessages.size(); i += BATCH_INSERT_SIZE) {
                int endIndex = Math.min(i + BATCH_INSERT_SIZE, processedMessages.size());
                List<ChatMessage> batch = processedMessages.subList(i, endIndex);
                
                // 批量插入
                this.saveBatch(batch, BATCH_INSERT_SIZE);
                
                // 更新缓存
                for (ChatMessage message : batch) {
                    updateCache(message);
                }
            }
            
            log.debug("批量保存消息完成 - 总数: {}", messages.size());
        } catch (Exception e) {
            log.error("异步批量保存消息失败", e);
        }
        
        return CompletableFuture.completedFuture(null);
    }
    
    /**
     * 获取私聊消息历史
     * 
     * @param userId1 用户ID1
     * @param userId2 用户ID2
     * @param limit 限制数量
     * @return 消息列表
     */
    @Override
    public List<ChatMessage> getPrivateChatHistory(String userId1, String userId2, int limit) {
        try {
            // 尝试从缓存获取
            String cacheKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + 
                "private:" + getSortedUserIdPair(userId1, userId2);
            
            List<ChatMessage> cachedMessages = getCachedMessages(cacheKey, limit);
            if (CollUtil.isNotEmpty(cachedMessages)) {
                return cachedMessages;
            }
            
            // 从数据库获取
            List<ChatMessage> dbMessages = baseMapper.selectPrivateHistory(
                userId1, userId2, null, LocalDateTime.now(), limit);
            
            // 更新缓存
            if (CollUtil.isNotEmpty(dbMessages)) {
                setCachedMessages(cacheKey, dbMessages);
            }
            
            return dbMessages;
        } catch (Exception e) {
            log.error("获取私聊历史消息失败 - userId1: {}, userId2: {}", userId1, userId2, e);
            return List.of();
        }
    }
    
    /**
     * 获取群聊消息历史
     * 
     * @param chatRoomId 聊天室ID
     * @param limit 限制数量
     * @return 消息列表
     */
    @Override
    public List<ChatMessage> getGroupChatHistory(String chatRoomId, int limit) {
        try {
            // 尝试从缓存获取
            String cacheKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "group:" + chatRoomId;
            
            List<ChatMessage> cachedMessages = getCachedMessages(cacheKey, limit);
            if (CollUtil.isNotEmpty(cachedMessages)) {
                return cachedMessages;
            }
            
            // 从数据库获取
            List<ChatMessage> dbMessages = baseMapper.selectGroupHistory(
                chatRoomId, null, LocalDateTime.now(), limit);
            
            // 更新缓存
            if (CollUtil.isNotEmpty(dbMessages)) {
                setCachedMessages(cacheKey, dbMessages);
            }
            
            return dbMessages;
        } catch (Exception e) {
            log.error("获取群聊历史消息失败 - chatRoomId: {}", chatRoomId, e);
            return List.of();
        }
    }
    
    /**
     * 获取用户消息历史
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 消息列表
     */
    @Override
    public List<ChatMessage> getUserMessages(String userId, int limit) {
        try {
            // 尝试从缓存获取
            String cacheKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "user:" + userId;
            
            List<ChatMessage> cachedMessages = getCachedMessages(cacheKey, limit);
            if (CollUtil.isNotEmpty(cachedMessages)) {
                return cachedMessages;
            }
            
            // 从数据库获取
            List<ChatMessage> dbMessages = baseMapper.selectByUserId(
                userId, null, LocalDateTime.now(), limit);
            
            // 更新缓存
            if (CollUtil.isNotEmpty(dbMessages)) {
                setCachedMessages(cacheKey, dbMessages);
            }
            
            return dbMessages;
        } catch (Exception e) {
            log.error("获取用户消息历史失败 - userId: {}", userId, e);
            return List.of();
        }
    }
    
    /**
     * 清理过期消息
     * 
     * @param chatRoomId 聊天室ID
     * @param daysBefore 多少天前的消息
     */
    @Override
    public void cleanupExpiredMessages(String chatRoomId, int daysBefore) {
        try {
            LocalDateTime beforeTime = LocalDateTime.now().minusDays(daysBefore);
            int deletedCount = baseMapper.deleteByTimeBefore(chatRoomId, beforeTime);
            
            // 清理对应的缓存
            String cacheKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "group:" + chatRoomId;
            RedisUtil.delCacheObject(cacheKey);
            
            log.info("清理过期消息完成 - 聊天室: {}, 天数: {}, 删除消息数: {}", 
                chatRoomId, daysBefore, deletedCount);
        } catch (Exception e) {
            log.error("清理过期消息失败 - chatRoomId: {}", chatRoomId, e);
        }
    }
    
    /**
     * 统计聊天室消息数量
     * 
     * @param chatRoomId 聊天室ID
     * @return 消息数量
     */
    @Override
    public Long countChatRoomMessages(String chatRoomId) {
        try {
            // 尝试从缓存获取
            String countKey = SocketConstant.SOCKET_MESSAGE_COUNT_PREFIX + "group:" + chatRoomId;
            Long cachedCount = RedisUtil.getCacheObject(countKey);
            if (cachedCount != null) {
                return cachedCount;
            }
            
            // 从数据库获取
            Long dbCount = baseMapper.countByChatRoomId(chatRoomId);
            
            // 更新缓存（缓存1小时）
            RedisUtil.setCacheObject(countKey, dbCount, java.time.Duration.ofHours(1));
            
            return dbCount;
        } catch (Exception e) {
            log.error("统计聊天室消息数量失败 - chatRoomId: {}", chatRoomId, e);
            return 0L;
        }
    }
    
    /**
     * 更新消息缓存
     * 
     * @param message 消息对象
     */
    private void updateCache(ChatMessage message) {
        try {
            String cacheKey = null;
            
            if ("private".equals(message.getMessageType()) && 
                StrUtil.isAllNotBlank(message.getSenderId(), message.getReceiverId())) {
                cacheKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + 
                    "private:" + getSortedUserIdPair(message.getSenderId(), message.getReceiverId());
            } else if ("group".equals(message.getMessageType()) && 
                       StrUtil.isNotBlank(message.getChatRoomId())) {
                cacheKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + 
                    "group:" + message.getChatRoomId();
            } else if ("broadcast".equals(message.getMessageType())) {
                cacheKey = SocketConstant.SOCKET_MESSAGE_HISTORY_PREFIX + "broadcast";
            }
            
            if (cacheKey != null) {
                // 添加消息到缓存列表
                RedisUtil.lRightPush(cacheKey, message);
                
                // 限制缓存大小
                RedisUtil.lTrim(cacheKey, -MAX_CACHE_SIZE, -1);
                
                // 设置缓存过期时间（7天），防止内存泄漏
                RedisUtil.expire(cacheKey, 7, java.util.concurrent.TimeUnit.DAYS);
                
                log.debug("消息已添加到缓存 - Key: {}", cacheKey);
            }
        } catch (Exception e) {
            log.error("更新消息缓存失败", e);
        }
    }
    
    /**
     * 获取缓存中的消息
     * 
     * @param cacheKey 缓存键
     * @param limit 限制数量
     * @return 消息列表
     */
    private List<ChatMessage> getCachedMessages(String cacheKey, int limit) {
        try {
            List<Object> cachedObjects = RedisUtil.lGet(cacheKey, -limit, -1);
            if (CollUtil.isEmpty(cachedObjects)) {
                return List.of();
            }
            
            return cachedObjects.stream()
                    .filter(obj -> obj instanceof ChatMessage)
                    .map(obj -> (ChatMessage) obj)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取缓存消息失败 - Key: {}", cacheKey, e);
            return List.of();
        }
    }
    
    /**
     * 设置缓存消息
     * 
     * @param cacheKey 缓存键
     * @param messages 消息列表
     */
    private void setCachedMessages(String cacheKey, List<ChatMessage> messages) {
        try {
            // 清空旧缓存
            RedisUtil.delCacheObject(cacheKey);
            
            // 批量添加新消息
            if (CollUtil.isNotEmpty(messages)) {
                for (ChatMessage message : messages) {
                    RedisUtil.lRightPush(cacheKey, message);
                }
                
                // 限制缓存大小
                RedisUtil.lTrim(cacheKey, -MAX_CACHE_SIZE, -1);
            }
            
            log.debug("缓存消息设置完成 - Key: {}, Count: {}", cacheKey, messages.size());
        } catch (Exception e) {
            log.error("设置缓存消息失败 - Key: {}", cacheKey, e);
        }
    }
    
    /**
     * 获取排序后的用户ID对
     * 
     * @param userId1 用户ID1
     * @param userId2 用户ID2
     * @return 排序后的ID对
     */
    private String getSortedUserIdPair(String userId1, String userId2) {
        if (userId1.compareTo(userId2) <= 0) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }
}