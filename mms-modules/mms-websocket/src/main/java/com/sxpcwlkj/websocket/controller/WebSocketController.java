package com.sxpcwlkj.websocket.controller;

import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.websocket.entity.Message;
import com.sxpcwlkj.websocket.service.MessageService;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author mmsAdmin
 * @ClassName WebSocketController
 * @description: WebSocket管理控制器，提供API接口用于管理聊天、用户状态等
 * @date 2025年1月18日
 * @version: 1.0
 */
@RestController
@RequestMapping("/websocket")
@Tag(name = "WebSocket管理", description = "WebSocket连接管理、消息发送等接口")
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageService messageService;

    /**
     * 获取在线用户数量
     */
    @GetMapping("/online-count")
    @Operation(summary = "获取在线用户数量")
    public Long getOnlineUserCount() {
        return DataUtil.getLong(WebSocketUtil.getOnlineUsers().size());
    }

    /**
     * 获取所有在线用户ID列表
     */
    @GetMapping("/online-users")
    @Operation(summary = "获取在线用户列表")
    public Set<String> getOnlineUsers() {
        return WebSocketUtil.getOnlineUsers();
    }

    /**
     * 发送私聊消息（通过HTTP API）
     */
    @PostMapping("/send-private-msg")
    @Operation(summary = "发送私聊消息")
    public boolean sendPrivateMessage(@RequestBody Message message) {
        return messageService.sendPrivateMessage(message);
    }

    /**
     * 发送群聊消息（通过HTTP API）
     */
    @PostMapping("/send-group-msg")
    @Operation(summary = "发送群聊消息")
    public boolean sendGroupMessage(@RequestBody Message message) {
        return messageService.sendGroupMessage(message);
    }

    /**
     * 创建群聊
     */
    @PostMapping("/create-group")
    @Operation(summary = "创建群聊")
    public boolean createGroupChat(@RequestParam String chatRoomId,
                                   @RequestParam String roomName,
                                   @RequestParam String creatorId) {
        return messageService.createGroupChat(chatRoomId, roomName, creatorId);
    }

    /**
     * 加入群聊
     */
    @PostMapping("/join-group")
    @Operation(summary = "加入群聊")
    public boolean joinGroupChat(@RequestParam String chatRoomId,
                                 @RequestParam String userId) {
        return messageService.joinGroupChat(chatRoomId, userId);
    }

    /**
     * 离开群聊
     */
    @PostMapping("/leave-group")
    @Operation(summary = "离开群聊")
    public boolean leaveGroupChat(@RequestParam String chatRoomId,
                                  @RequestParam String userId) {
        return messageService.leaveGroupChat(chatRoomId, userId);
    }

    /**
     * 获取群聊成员列表
     */
    @GetMapping("/group-members/{chatRoomId}")
    @Operation(summary = "获取群聊成员列表")
    public Set<String> getGroupMembers(@PathVariable String chatRoomId) {
        return messageService.getGroupMembers(chatRoomId);
    }

    /**
     * 获取私聊消息历史记录
     */
    @GetMapping("/chat-history/private")
    @Operation(summary = "获取私聊消息历史记录")
    public List<Message> getPrivateChatHistory(@RequestParam String userId1,
                                               @RequestParam String userId2,
                                               @RequestParam(defaultValue = "50") int count) {
        return messageService.getPrivateChatHistory(userId1, userId2, count);
    }

    /**
     * 获取群聊消息历史记录
     */
    @GetMapping("/chat-history/group/{chatRoomId}")
    @Operation(summary = "获取群聊消息历史记录")
    public List<Message> getGroupChatHistory(@PathVariable String chatRoomId,
                                             @RequestParam(defaultValue = "50") int count) {
        return messageService.getGroupChatHistory(chatRoomId, count);
    }

    /**
     * 检查用户是否在线
     */
    @GetMapping("/is-online/{userId}")
    @Operation(summary = "检查用户是否在线")
    public boolean isUserOnline(@PathVariable String userId) {
        return WebSocketUtil.isOnline(userId);
    }

    /**
     * 向指定用户发送系统消息
     */
    @PostMapping("/send-system-msg")
    @Operation(summary = "发送系统消息给指定用户")
    public boolean sendSystemMessage(@RequestParam String userId,
                                     @RequestParam String content) {
        return WebSocketUtil.sendToUser(userId, content);
    }
}
