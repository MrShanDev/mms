package com.sxpcwlkj.websocket.utils;

import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author mmsAdmin
 * @ClassName PermissionUtil
 * @description: 权限验证工具类
 * @date 2025年1月18日
 * @version: 1.0
 */
@Slf4j
public class PermissionUtil {

    /**
     * 检查用户是否具有特定权限
     *
     * @param userId   用户ID
     * @param permission 权限标识
     * @return 是否有权限
     */
    public static boolean hasPermission(String userId, String permission) {
        try {
            String permissionKey = SocketConstant.SOCKET_USER_PERMISSIONS_PREFIX + userId;
            Set<Object> permissions = RedisUtil.sGet(permissionKey);
            return permissions != null && permissions.contains(permission);
        } catch (Exception e) {
            log.error("检查用户权限失败: userId={}, permission={}", userId, permission, e);
            return false; // 默认无权限
        }
    }

    /**
     * 检查用户是否具有读取消息权限
     */
    public static boolean canReadMessage(String userId) {
        return hasPermission(userId, SocketConstant.PERMISSION_CHAT_READ);
    }

    /**
     * 检查用户是否具有发送消息权限
     */
    public static boolean canWriteMessage(String userId) {
        return hasPermission(userId, SocketConstant.PERMISSION_CHAT_WRITE);
    }

    /**
     * 检查用户是否具有创建聊天权限
     */
    public static boolean canCreateChat(String userId) {
        return hasPermission(userId, SocketConstant.PERMISSION_CHAT_CREATE);
    }

    /**
     * 检查用户是否具有管理聊天权限
     */
    public static boolean canManageChat(String userId) {
        return hasPermission(userId, SocketConstant.PERMISSION_CHAT_MANAGE);
    }

    /**
     * 为用户添加权限
     */
    public static void addPermission(String userId, String permission) {
        try {
            String permissionKey = SocketConstant.SOCKET_USER_PERMISSIONS_PREFIX + userId;
            RedisUtil.sSet(permissionKey, permission);
            log.debug("为用户 {} 添加权限: {}", userId, permission);
        } catch (Exception e) {
            log.error("添加用户权限失败: userId={}, permission={}", userId, permission, e);
        }
    }

    /**
     * 为用户移除权限
     */
    public static void removePermission(String userId, String permission) {
        try {
            String permissionKey = SocketConstant.SOCKET_USER_PERMISSIONS_PREFIX + userId;
            RedisUtil.sRemove(permissionKey, permission);
            log.debug("为用户 {} 移除权限: {}", userId, permission);
        } catch (Exception e) {
            log.error("移除用户权限失败: userId={}, permission={}", userId, permission, e);
        }
    }

    /**
     * 为用户设置默认权限
     */
    public static void setDefaultPermissions(String userId) {
        addPermission(userId, SocketConstant.PERMISSION_CHAT_READ);
        addPermission(userId, SocketConstant.PERMISSION_CHAT_WRITE);
    }

    /**
     * 检查用户是否可以加入聊天室
     */
    public static boolean canJoinChatRoom(String userId, String chatRoomId) {
        // 检查基本读取权限
        if (!canReadMessage(userId)) {
            return false;
        }

        // 这里可以添加更复杂的权限检查逻辑，比如检查用户是否被邀请加入特定群组
        // 或者检查群组的访问控制列表
        return true;
    }

    /**
     * 检查用户是否可以发送私聊消息给另一个用户
     */
    public static boolean canSendPrivateMessage(String senderId, String receiverId) {
        // 检查发送者是否有发送权限
        if (!canWriteMessage(senderId)) {
            return false;
        }

        // 可以在这里添加额外的检查，例如检查接收者是否屏蔽了发送者
        return true;
    }

    /**
     * 检查用户是否可以发送群聊消息
     */
    public static boolean canSendGroupMessage(String userId, String chatRoomId) {
        // 检查用户是否有发送权限
        if (!canWriteMessage(userId)) {
            return false;
        }

        // 检查用户是否在群组中
        return WebSocketUtil.isUserInChatRoom(chatRoomId, userId);
    }
}