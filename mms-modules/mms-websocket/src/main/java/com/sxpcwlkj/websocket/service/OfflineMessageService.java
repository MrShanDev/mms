package com.sxpcwlkj.websocket.service;

import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.Message;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 离线消息服务类
 * 
 * @author mmsAdmin
 * @since 2025年1月22日
 */
@Service
@Slf4j
public class OfflineMessageService {
    
    /**
     * 离线消息最大保留数量
     */
    private static final int MAX_OFFLINE_MESSAGE_COUNT = 1000;
    
    /**
     * 离线消息过期时间（天）
     */
    private static final int OFFLINE_MESSAGE_EXPIRE_DAYS = 7;
    
    /**
     * 存储离线消息
     * 
     * @param userId 接收者用户ID
     * @param message 消息对象
     */
    public void saveOfflineMessage(String userId, Message message) {
        try {
            String queueKey = SocketConstant.SOCKET_OFFLINE_MESSAGE_QUEUE_PREFIX + userId;
            
            // 将消息添加到队列尾部
            RedisUtil.lRightPush(queueKey, message);
            
            // 限制队列长度
            long size = RedisUtil.lSize(queueKey);
            if (size > MAX_OFFLINE_MESSAGE_COUNT) {
                // 删除最早的消息
                RedisUtil.lLeftPop(queueKey);
            }
            
            // 设置过期时间
            RedisUtil.expire(queueKey, OFFLINE_MESSAGE_EXPIRE_DAYS, java.util.concurrent.TimeUnit.DAYS);
            
            log.debug("离线消息已保存 - 用户: {}, 消息ID: {}", userId, message.getMessageId());
        } catch (Exception e) {
            log.error("保存离线消息失败 - 用户: {}", userId, e);
        }
    }
    
    /**
     * 发送消息（在线直接发送，离线存储）
     * 
     * @param userId 接收者用户ID
     * @param message 消息对象
     * @return 是否在线发送成功
     */
    public boolean sendOrStore(String userId, Message message) {
        // 尝试在线发送
        boolean sendSuccess = WebSocketUtil.sendToUser(userId, JSONUtil.toJsonStr(message));
        
        if (!sendSuccess) {
            // 用户不在线，存储为离线消息
            saveOfflineMessage(userId, message);
            log.debug("用户 {} 不在线，消息已存入离线队列", userId);
        }
        
        return sendSuccess;
    }
    
    /**
     * 获取用户的离线消息
     * 
     * @param userId 用户ID
     * @return 离线消息列表
     */
    public List<Message> getOfflineMessages(String userId) {
        try {
            String queueKey = SocketConstant.SOCKET_OFFLINE_MESSAGE_QUEUE_PREFIX + userId;
            List<Object> rawMessages = RedisUtil.lGet(queueKey, 0, -1);
            
            if (rawMessages == null || rawMessages.isEmpty()) {
                return new ArrayList<>();
            }
            
            List<Message> messages = new ArrayList<>();
            for (Object obj : rawMessages) {
                if (obj instanceof Message) {
                    messages.add((Message) obj);
                }
            }
            
            return messages;
        } catch (Exception e) {
            log.error("获取离线消息失败 - 用户: {}", userId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取离线消息数量
     * 
     * @param userId 用户ID
     * @return 离线消息数
     */
    public long getOfflineMessageCount(String userId) {
        try {
            String queueKey = SocketConstant.SOCKET_OFFLINE_MESSAGE_QUEUE_PREFIX + userId;
            return RedisUtil.lSize(queueKey);
        } catch (Exception e) {
            log.error("获取离线消息数量失败 - 用户: {}", userId, e);
            return 0;
        }
    }
    
    /**
     * 推送离线消息给用户
     * 
     * @param userId 用户ID
     * @return 推送的消息数量
     */
    public int pushOfflineMessages(String userId) {
        try {
            List<Message> offlineMessages = getOfflineMessages(userId);
            if (offlineMessages.isEmpty()) {
                return 0;
            }
            
            int successCount = 0;
            for (Message message : offlineMessages) {
                boolean sendSuccess = WebSocketUtil.sendToUser(userId, JSONUtil.toJsonStr(message));
                if (sendSuccess) {
                    successCount++;
                } else {
                    // 如果发送失败，停止推送
                    log.warn("推送离线消息失败，用户可能已断开连接 - 用户: {}", userId);
                    break;
                }
            }
            
            // 清空已推送的离线消息
            if (successCount > 0) {
                clearOfflineMessages(userId);
            }
            
            log.info("推送离线消息完成 - 用户: {}, 推送数量: {}/{}", userId, successCount, offlineMessages.size());
            return successCount;
        } catch (Exception e) {
            log.error("推送离线消息失败 - 用户: {}", userId, e);
            return 0;
        }
    }
    
    /**
     * 清空用户的离线消息
     * 
     * @param userId 用户ID
     */
    public void clearOfflineMessages(String userId) {
        try {
            String queueKey = SocketConstant.SOCKET_OFFLINE_MESSAGE_QUEUE_PREFIX + userId;
            RedisUtil.delCacheObject(queueKey);
            log.debug("离线消息已清空 - 用户: {}", userId);
        } catch (Exception e) {
            log.error("清空离线消息失败 - 用户: {}", userId, e);
        }
    }
    
    /**
     * 删除指定的离线消息
     * 
     * @param userId 用户ID
     * @param messageId 消息ID
     * @return 是否删除成功
     */
    public boolean deleteOfflineMessage(String userId, String messageId) {
        try {
            List<Message> messages = getOfflineMessages(userId);
            boolean removed = messages.removeIf(msg -> msg.getMessageId().equals(messageId));
            
            if (removed) {
                // 重新保存消息列表
                String queueKey = SocketConstant.SOCKET_OFFLINE_MESSAGE_QUEUE_PREFIX + userId;
                RedisUtil.delCacheObject(queueKey);
                
                for (Message msg : messages) {
                    RedisUtil.lRightPush(queueKey, msg);
                }
                
                // 设置过期时间
                RedisUtil.expire(queueKey, OFFLINE_MESSAGE_EXPIRE_DAYS, java.util.concurrent.TimeUnit.DAYS);
                
                log.debug("离线消息已删除 - 用户: {}, 消息ID: {}", userId, messageId);
            }
            
            return removed;
        } catch (Exception e) {
            log.error("删除离线消息失败 - 用户: {}, 消息ID: {}", userId, messageId, e);
            return false;
        }
    }
}
