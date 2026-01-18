package com.sxpcwlkj.websocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxpcwlkj.websocket.entity.ChatMessage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 聊天消息服务接口
 * 
 * 功能特点：
 * - 消息持久化
 * - 缓存优化
 * - 异步处理
 * - 历史消息查询
 * - 消息清理
 * 
 * @author mmsAdmin
 * @since 2025年1月19日
 */
public interface ChatMessageService extends IService<ChatMessage> {
    
    /**
     * 异步保存消息
     * 
     * @param message 消息对象
     * @return CompletableFuture
     */
    CompletableFuture<Void> saveMessageAsync(ChatMessage message);
    
    /**
     * 异步批量保存消息
     * 
     * @param messages 消息列表
     * @return CompletableFuture
     */
    CompletableFuture<Void> saveMessagesBatchAsync(List<ChatMessage> messages);
    
    /**
     * 获取私聊消息历史
     * 
     * @param userId1 用户ID1
     * @param userId2 用户ID2
     * @param limit 限制数量
     * @return 消息列表
     */
    List<ChatMessage> getPrivateChatHistory(String userId1, String userId2, int limit);
    
    /**
     * 获取群聊消息历史
     * 
     * @param chatRoomId 聊天室ID
     * @param limit 限制数量
     * @return 消息列表
     */
    List<ChatMessage> getGroupChatHistory(String chatRoomId, int limit);
    
    /**
     * 获取用户消息历史
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 消息列表
     */
    List<ChatMessage> getUserMessages(String userId, int limit);
    
    /**
     * 清理过期消息
     * 
     * @param chatRoomId 聊天室ID
     * @param daysBefore 多少天前的消息
     */
    void cleanupExpiredMessages(String chatRoomId, int daysBefore);
    
    /**
     * 统计聊天室消息数量
     * 
     * @param chatRoomId 聊天室ID
     * @return 消息数量
     */
    Long countChatRoomMessages(String chatRoomId);
}