package com.sxpcwlkj.websocket.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.websocket.WebSocketService;
import com.sxpcwlkj.websocket.entity.DataMsg;
import com.sxpcwlkj.websocket.entity.DataMsgVo;
import com.sxpcwlkj.websocket.enums.CmdEnum;
import com.sxpcwlkj.websocket.utils.WebSocketPerformanceMonitor;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * 高性能WebSocket处理器 - 支持万人级用户在线聊天
 * 
 * 优化特性:
 * - 支持JDK 21虚拟线程
 * - 消息持久化到数据库
 * - Redis缓存优化
 * - 异步消息处理
 * - 消息安全验证
 * - 高效连接管理
 * - 性能监控
 * 
 * @author mmsAdmin
 * @since 2025年1月19日
 */
@Component
@Slf4j
public class SocketHandler extends AbstractWebSocketHandler  {
    
    @Autowired
    private WebSocketService webSocketService;

    /**
     * 连接成功后
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        //socket连接成功后触发
        String userId = WebSocketUtil.getUserId(session.getId());
        log.info("用户 {} 建立websocket连接", userId);
        WebSocketUtil.add(session);
        
        // 记录性能指标
        WebSocketPerformanceMonitor.recordConnectionEstablished();
        
        //返回自己的sessionId
        WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED).setData(session.getId()));
        
        // 设置用户默认权限
        // 注意：实际项目中应根据用户角色动态设置权限
        // 这里仅为演示目的设置默认权限
        // com.sxpcwlkj.websocket.utils.PermissionUtil.setDefaultPermissions(userId);
    }

    /**
     * 客户端发送文本信息触发
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        log.debug("服务端接收到消息: {}", payload);
        
        try {
            DataMsgVo dataMsg = BeanUtil.toBean(JSONUtil.toBean(payload, JSONObject.class), DataMsgVo.class);
            if (dataMsg != null) {
                webSocketService.receiverMsg(session, dataMsg);
                
                // 记录消息接收性能指标
                WebSocketPerformanceMonitor.recordMessageReceived();
            } else {
                log.warn("无法解析的消息格式: {}", payload);
            }
        } catch (Exception e) {
            log.error("处理消息异常: {}", payload, e);
            // 发送错误消息给客户端
            WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED).setData("消息格式错误"));
        }
    }

    /**
     * 客户端发送二进制信息时触发
     */
    @Override
    protected void handleBinaryMessage(@NotNull WebSocketSession session, @NotNull BinaryMessage message) throws Exception {
        //客户端发送二进制信息时触发
        log.info("接收二进制消息");
        // 可以在此处处理文件上传等二进制数据
    }

    /**
     * 心跳监测的回复
     * 针对ping帧的恢复，非text形式的消息接收
     */
    @Override
    protected void handlePongMessage(@NotNull WebSocketSession session, @NotNull PongMessage message) throws Exception {
        log.debug("收到PONG消息");
    }


    /**
     * 异常处理
     */
    @Override
    public void handleTransportError(WebSocketSession session, @NotNull Throwable exception) throws Exception {
        //异常时触发
        String userId = WebSocketUtil.getUserId(session.getId());
        log.error("用户 {} WebSocket连接发生异常", userId, exception);
        WebSocketUtil.close(session.getId());
    }

    /**
     * 关闭websocket连接
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        // socket连接关闭后触发
        String userId = WebSocketUtil.getUserId(session.getId());
        log.info("用户 {} 关闭websocket连接, 状态: {}", userId, status);
        WebSocketUtil.close(session.getId());
        
        // 记录性能指标
        WebSocketPerformanceMonitor.recordConnectionClosed();
    }


    /**
     * 是否支持分片消息
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
