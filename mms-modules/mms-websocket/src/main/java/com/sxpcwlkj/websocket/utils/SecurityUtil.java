package com.sxpcwlkj.websocket.utils;

import cn.hutool.core.util.StrUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * @author mmsAdmin
 * @ClassName SecurityUtil
 * @description: 安全防护工具类，提供输入验证、防注入、频率限制等功能
 * @date 2025年1月18日
 * @version: 1.0
 */
@Slf4j
public class SecurityUtil {

    // 消息内容长度限制
    private static final int MAX_MESSAGE_LENGTH = 10000; // 10KB
    
    // 用户ID长度限制
    private static final int MAX_USER_ID_LENGTH = 50;
    
    // 聊天室ID长度限制
    private static final int MAX_CHAT_ROOM_ID_LENGTH = 100;
    
    // 正则表达式：验证用户ID格式（字母数字下划线横线）
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{1," + MAX_USER_ID_LENGTH + "}$");
    
    // 正则表达式：验证聊天室ID格式
    private static final Pattern CHAT_ROOM_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{1," + MAX_CHAT_ROOM_ID_LENGTH + "}$");
    
    // SQL注入检测模式
    private static final String[] SQL_INJECTION_PATTERNS = {
        "(?i)(union\\s+select)",
        "(?i)(insert\\s+into)",
        "(?i)(update\\s+\\w+\\s+set)",
        "(?i)(delete\\s+from)",
        "(?i)(drop\\s+(table|database|view))",
        "(?i)(create\\s+(table|database|view))",
        "(?i)(alter\\s+\\w+)",
        "(?i)(exec\\s+\\()",
        "(?i)(execute\\s+\\()",
        "'\\s*(or|and)\\s*1\\s*=\\s*1"
    };

    /**
     * 验证用户ID格式
     */
    public static boolean validateUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            return false;
        }
        return USER_ID_PATTERN.matcher(userId).matches();
    }

    /**
     * 验证聊天室ID格式
     */
    public static boolean validateChatRoomId(String chatRoomId) {
        if (StrUtil.isBlank(chatRoomId)) {
            return false;
        }
        return CHAT_ROOM_ID_PATTERN.matcher(chatRoomId).matches();
    }

    /**
     * 验证消息内容安全性
     */
    public static boolean validateMessageContent(String content) {
        if (StrUtil.isBlank(content)) {
            return true; // 空消息可以接受
        }
        
        // 检查消息长度
        if (content.length() > MAX_MESSAGE_LENGTH) {
            log.warn("消息内容超出长度限制: {} > {}", content.length(), MAX_MESSAGE_LENGTH);
            return false;
        }
        
        // 检查是否包含SQL注入模式
        for (String pattern : SQL_INJECTION_PATTERNS) {
            if (java.util.regex.Pattern.compile(pattern).matcher(content).find()) {
                log.warn("检测到潜在的SQL注入攻击: {}", content);
                return false;
            }
        }
        
        // 检查是否包含脚本标签（XSS防护）
        if (content.toLowerCase().contains("<script") || content.toLowerCase().contains("javascript:")) {
            log.warn("检测到潜在的XSS攻击: {}", content);
            return false;
        }
        
        return true;
    }

    /**
     * 清理消息内容（移除潜在危险字符）
     */
    public static String sanitizeMessageContent(String content) {
        if (StrUtil.isBlank(content)) {
            return content;
        }
        
        // 移除或转义HTML标签
        content = content.replaceAll("<script[^>]*>[\\s\\S]*?</script>", "");
        content = content.replaceAll("<iframe[^>]*>[\\s\\S]*?</iframe>", "");
        content = content.replaceAll("javascript:", "");
        
        // 限制长度
        if (content.length() > MAX_MESSAGE_LENGTH) {
            content = content.substring(0, MAX_MESSAGE_LENGTH);
        }
        
        return content;
    }

    /**
     * 检查用户是否被限制（如频繁发送消息）
     */
    public static boolean isUserRateLimited(String userId) {
        String rateLimitKey = SocketConstant.SOCKET_USER_SUBSCRIPTIONS_PREFIX + "rate_limit:" + userId;
        
        // 这里可以实现具体的频率限制逻辑，例如每分钟最多发送多少条消息
        // 使用Redis计数器实现
        try {
            // 示例：每分钟最多100条消息
            long currentTime = System.currentTimeMillis();
            long oneMinuteAgo = currentTime - 60000; // 一分钟前的时间戳
            
            // 在实际应用中，这里应该使用Redis的有序集合或计数器来跟踪消息频率
            // 由于我们不能直接访问RedisUtil的高级功能，这里仅作示意
            return false; // 暂时不实施速率限制
        } catch (Exception e) {
            log.error("检查用户速率限制异常", e);
            return false; // 出错时默认不禁用
        }
    }

    /**
     * 记录用户活动（用于安全审计）
     */
    public static void logUserActivity(String userId, String action, String details) {
        log.info("用户活动记录 - 用户ID: {}, 操作: {}, 详情: {}", userId, action, details);
        
        // 在实际应用中，这里可以将活动记录保存到安全日志中
        // 例如保存到数据库或专门的安全审计系统
    }

    /**
     * 检查是否为恶意IP地址（简单的IP黑名单检查）
     */
    public static boolean isMaliciousIP(String ip) {
        // 在实际应用中，这里可以从Redis或数据库中获取IP黑名单
        // 为了示例，我们返回false
        return false;
    }

    /**
     * 检查消息是否包含敏感词
     */
    public static boolean containsSensitiveWords(String content) {
        // 在实际应用中，这里可以检查消息是否包含敏感词
        // 从配置或数据库中加载敏感词列表
        // 为了示例，我们返回false
        return false;
    }

    /**
     * 生成安全的消息ID
     */
    public static String generateSecureMessageId() {
        // 使用安全的随机数生成器生成消息ID
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}