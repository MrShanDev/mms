package com.sxpcwlkj.websocket.controller;

import com.sxpcwlkj.websocket.utils.PerformanceUtil;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author mmsAdmin
 * @ClassName MonitorController
 * @description: WebSocket服务监控控制器
 * @date 2025年1月18日
 * @version: 1.0
 */
@RestController
@RequestMapping("/monitor")
@Tag(name = "WebSocket监控", description = "WebSocket服务状态监控接口")
@RequiredArgsConstructor
public class MonitorController {

    /**
     * 获取系统性能指标
     */
    @GetMapping("/performance")
    @Operation(summary = "获取系统性能指标")
    public Map<String, Object> getPerformanceMetrics() {
        return PerformanceUtil.getPerformanceMetrics();
    }

    /**
     * 获取WebSocket服务状态
     */
    @GetMapping("/status")
    @Operation(summary = "获取WebSocket服务状态")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new java.util.HashMap<>();
        
        status.put("websocket_enabled", true); // 这里应该从配置获取
        status.put("online_users_count", WebSocketUtil.getOnlineUsers().size());
        status.put("server_time", System.currentTimeMillis());
        status.put("active_sessions", WebSocketUtil.SESSION_POOL.size());
        
        return status;
    }

    /**
     * 获取在线用户列表
     */
    @GetMapping("/online-users")
    @Operation(summary = "获取在线用户列表及详情")
    public Map<String, Object> getOnlineUsersDetail() {
        Map<String, Object> result = new java.util.HashMap<>();
        
        var onlineUsers = WebSocketUtil.getOnlineUsers();
        result.put("count", onlineUsers.size());
        result.put("users", onlineUsers);
        
        return result;
    }
}