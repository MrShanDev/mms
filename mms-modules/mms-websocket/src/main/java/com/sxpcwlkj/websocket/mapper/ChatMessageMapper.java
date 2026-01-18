package com.sxpcwlkj.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.websocket.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天消息数据访问层
 * 
 * @author mmsAdmin
 * @since 2025年1月19日
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    
    /**
     * 批量插入消息
     * 
     * @param messages 消息列表
     * @return 影响行数
     */
    int insertBatch(@Param("messages") List<ChatMessage> messages);
    
    /**
     * 查询私聊消息历史
     * 
     * @param userId1 用户ID1
     * @param userId2 用户ID2
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 消息列表
     */
    List<ChatMessage> selectPrivateHistory(@Param("userId1") String userId1, 
                                          @Param("userId2") String userId2,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime,
                                          @Param("limit") Integer limit);
    
    /**
     * 查询群聊消息历史
     * 
     * @param chatRoomId 聊天室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 消息列表
     */
    List<ChatMessage> selectGroupHistory(@Param("chatRoomId") String chatRoomId,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime,
                                        @Param("limit") Integer limit);
    
    /**
     * 根据时间段删除消息（用于清理过期消息）
     * 
     * @param chatRoomId 聊天室ID
     * @param beforeTime 删除此时间之前的消息
     * @return 影响行数
     */
    int deleteByTimeBefore(@Param("chatRoomId") String chatRoomId,
                           @Param("beforeTime") LocalDateTime beforeTime);
    
    /**
     * 统计聊天室消息数量
     * 
     * @param chatRoomId 聊天室ID
     * @return 消息数量
     */
    Long countByChatRoomId(@Param("chatRoomId") String chatRoomId);
    
    /**
     * 查询指定用户的消息
     * 
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 消息列表
     */
    List<ChatMessage> selectByUserId(@Param("userId") String userId,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime,
                                    @Param("limit") Integer limit);
}