package com.sxpcwlkj.websocket.controller;

import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.websocket.entity.Message;
import com.sxpcwlkj.websocket.entity.UserConversation;
import com.sxpcwlkj.websocket.service.MessageService;
import com.sxpcwlkj.websocket.service.UserConversationService;
import com.sxpcwlkj.websocket.service.OfflineMessageService;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final UserConversationService userConversationService;
    private final OfflineMessageService offlineMessageService;

    /**
     * 获取在线用户数量
     */
    @GetMapping("/online-count")
    @Operation(summary = "获取在线用户数量")
    public R<Long> getOnlineUserCount() {
        return R.success(DataUtil.getLong(WebSocketUtil.getOnlineUsers().size()));
    }

    /**
     * 获取所有在线用户ID列表
     */
    @GetMapping("/online-users")
    @Operation(summary = "获取在线用户列表")
    public R<Set<String>> getOnlineUsers() {
        return R.success(WebSocketUtil.getOnlineUsers());
    }

    /**
     * 发送私聊消息（通过HTTP API）
     */
    @PostMapping("/send-private-msg")
    @Operation(summary = "发送私聊消息")
    public R<Boolean> sendPrivateMessage(@RequestBody Message message) {
        return R.success(messageService.sendPrivateMessage(message));
    }

    /**
     * 发送群聊消息（通过HTTP API）
     */
    @PostMapping("/send-group-msg")
    @Operation(summary = "发送群聊消息")
    public R<Boolean> sendGroupMessage(@RequestBody Message message) {
        return R.success(messageService.sendGroupMessage(message));
    }

    /**
     * 创建群聊
     */
    @PostMapping("/create-group")
    @Operation(summary = "创建群聊")
    public R<Boolean> createGroupChat(@RequestParam String chatRoomId,
                                   @RequestParam String roomName,
                                   @RequestParam String creatorId) {
        return R.success(messageService.createGroupChat(chatRoomId, roomName, creatorId));
    }

    /**
     * 加入群聊
     */
    @PostMapping("/join-group")
    @Operation(summary = "加入群聊")
    public R<Boolean> joinGroupChat(@RequestParam String chatRoomId,
                                 @RequestParam String userId) {
        return R.success(messageService.joinGroupChat(chatRoomId, userId));
    }

    /**
     * 离开群聊
     */
    @PostMapping("/leave-group")
    @Operation(summary = "离开群聊")
    public R<Boolean> leaveGroupChat(@RequestParam String chatRoomId,
                                  @RequestParam String userId) {
        return R.success(messageService.leaveGroupChat(chatRoomId, userId));
    }

    /**
     * 获取群聊成员列表
     */
    @GetMapping("/group-members/{chatRoomId}")
    @Operation(summary = "获取群聊成员列表")
    public R<Set<String>> getGroupMembers(@PathVariable String chatRoomId) {
        return R.success(messageService.getGroupMembers(chatRoomId));
    }

    /**
     * 获取私聊消息历史记录
     */
    @GetMapping("/chat-history/private")
    @Operation(summary = "获取私聊消息历史记录")
    public R<List<Message>> getPrivateChatHistory(@RequestParam String userId1,
                                               @RequestParam String userId2,
                                               @RequestParam(defaultValue = "50") int count) {
        return R.success(messageService.getPrivateChatHistory(userId1, userId2, count));
    }

    /**
     * 获取群聊消息历史记录
     */
    @GetMapping("/chat-history/group/{chatRoomId}")
    @Operation(summary = "获取群聊消息历史记录")
    public R<List<Message>> getGroupChatHistory(@PathVariable String chatRoomId,
                                             @RequestParam(defaultValue = "50") int count) {
        return R.success(messageService.getGroupChatHistory(chatRoomId, count));
    }

    /**
     * 检查用户是否在线
     */
    @GetMapping("/is-online/{userId}")
    @Operation(summary = "检查用户是否在线")
    public R<Boolean> isUserOnline(@PathVariable String userId) {
        return R.success(WebSocketUtil.isOnline(userId));
    }

    /**
     * 向指定用户发送系统消息
     */
    @PostMapping("/send-system-msg")
    @Operation(summary = "发送系统消息给指定用户")
    public R<Boolean> sendSystemMessage(@RequestParam String userId,
                                     @RequestParam String content) {
        return R.success(WebSocketUtil.sendToUser(userId, content));
    }
    
    /**
     * 撤回消息
     */
    @PostMapping("/recall-message")
    @Operation(summary = "撤回消息")
    public R<Boolean> recallMessage(@RequestParam String messageId,
                                 @RequestParam String userId) {
        return R.success(messageService.recallMessage(messageId, userId));
    }
    
    /**
     * 标记消息为已读
     */
    @PostMapping("/mark-read")
    @Operation(summary = "标记消息为已读")
    public R<Boolean> markMessageAsRead(@RequestParam String messageId,
                                     @RequestParam String userId) {
        return R.success(messageService.markMessageAsRead(messageId, userId));
    }
    
    /**
     * 批量标记消息为已读
     */
    @PostMapping("/mark-read-batch")
    @Operation(summary = "批量标记消息为已读")
    public R<Integer> markMessagesAsRead(@RequestParam String userId,
                                  @RequestBody List<String> messageIds) {
        return R.success(messageService.markMessagesAsRead(messageIds, userId));
    }
    
    /**
     * 获取用户未读消息数
     */
    @GetMapping("/unread-count/{userId}")
    @Operation(summary = "获取用户未读消息数")
    public R<Long> getUnreadCount(@PathVariable String userId) {
        return R.success(messageService.getUnreadMessageCount(userId));
    }
    
    /**
     * 获取私聊未读消息数
     */
    @GetMapping("/unread-count/private")
    @Operation(summary = "获取私聊未读消息数")
    public R<Long> getUnreadPrivateCount(@RequestParam String userId,
                                      @RequestParam String otherUserId) {
        return R.success(messageService.getUnreadPrivateMessageCount(userId, otherUserId));
    }
    
    // ==================== 会话管理接口 ====================
    
    /**
     * 置顶会话
     */
    @PostMapping("/conversation/pin")
    @Operation(summary = "置顶会话")
    public R<Boolean> pinConversation(@RequestParam String userId,
                                   @RequestParam String conversationId,
                                   @RequestParam String conversationType) {
        return R.success(userConversationService.pinConversation(userId, conversationId, conversationType));
    }
    
    /**
     * 取消置顶
     */
    @PostMapping("/conversation/unpin")
    @Operation(summary = "取消置顶会话")
    public R<Boolean> unpinConversation(@RequestParam String userId,
                                     @RequestParam String conversationId,
                                     @RequestParam String conversationType) {
        return R.success(userConversationService.unpinConversation(userId, conversationId, conversationType));
    }
    
    /**
     * 设置免打扰
     */
    @PostMapping("/conversation/mute")
    @Operation(summary = "设置会话免打扰")
    public R<Boolean> setConversationMute(@RequestParam String userId,
                                       @RequestParam String conversationId,
                                       @RequestParam String conversationType,
                                       @RequestParam boolean muted) {
        return R.success(userConversationService.setMute(userId, conversationId, conversationType, muted));
    }
    
    /**
     * 获取用户会话列表
     */
    @GetMapping("/conversation/list/{userId}")
    @Operation(summary = "获取用户会话列表")
    public R<List<UserConversation>> getConversationList(@PathVariable String userId) {
        return R.success(userConversationService.getUserConversations(userId));
    }
    
    /**
     * 删除会话
     */
    @PostMapping("/conversation/delete")
    @Operation(summary = "删除会话")
    public R<Boolean> deleteConversation(@RequestParam String userId,
                                      @RequestParam String conversationId,
                                      @RequestParam String conversationType) {
        return R.success(userConversationService.deleteConversation(userId, conversationId, conversationType));
    }
    
    /**
     * 清空会话未读数
     */
    @PostMapping("/conversation/clear-unread")
    @Operation(summary = "清空会话未读数")
    public R<Boolean> clearConversationUnread(@RequestParam String userId,
                                        @RequestParam String conversationId,
                                        @RequestParam String conversationType) {
        userConversationService.clearUnreadCount(userId, conversationId, conversationType);
        return R.success(true);
    }
    
    // ==================== 离线消息管理接口 ====================
    
    /**
     * 获取离线消息列表
     */
    @GetMapping("/offline-messages/{userId}")
    @Operation(summary = "获取用户离线消息")
    public R<List<Message>> getOfflineMessages(@PathVariable String userId) {
        return R.success(offlineMessageService.getOfflineMessages(userId));
    }
    
    /**
     * 获取离线消息数量
     */
    @GetMapping("/offline-messages/count/{userId}")
    @Operation(summary = "获取离线消息数量")
    public R<Long> getOfflineMessageCount(@PathVariable String userId) {
        return R.success(offlineMessageService.getOfflineMessageCount(userId));
    }
    
    /**
     * 手动推送离线消息
     */
    @PostMapping("/offline-messages/push")
    @Operation(summary = "推送离线消息给用户")
    public R<Integer> pushOfflineMessages(@RequestParam String userId) {
        return R.success(offlineMessageService.pushOfflineMessages(userId));
    }
    
    /**
     * 清空离线消息
     */
    @PostMapping("/offline-messages/clear")
    @Operation(summary = "清空离线消息")
    public R<Boolean> clearOfflineMessages(@RequestParam String userId) {
        offlineMessageService.clearOfflineMessages(userId);
        return R.success(true);
    }
}
