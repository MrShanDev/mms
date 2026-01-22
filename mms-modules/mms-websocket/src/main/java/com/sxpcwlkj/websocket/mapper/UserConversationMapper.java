package com.sxpcwlkj.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.websocket.entity.UserConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户会话数据访问层
 * 
 * @author mmsAdmin
 * @since 2025年1月22日
 */
@Mapper
public interface UserConversationMapper extends BaseMapper<UserConversation> {
    
    /**
     * 查询用户的会话列表（按置顶和最后消息时间排序）
     * 
     * @param userId 用户ID
     * @return 会话列表
     */
    List<UserConversation> selectUserConversations(@Param("userId") String userId);
    
    /**
     * 查询指定会话
     * 
     * @param userId 用户ID
     * @param conversationId 会话ID
     * @param conversationType 会话类型
     * @return 会话对象
     */
    UserConversation selectByUserAndConversation(@Param("userId") String userId,
                                                  @Param("conversationId") String conversationId,
                                                  @Param("conversationType") String conversationType);
}
