package com.sxpcwlkj.websocket.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.websocket.WebSocketService;
import com.sxpcwlkj.websocket.entity.DataMsg;
import com.sxpcwlkj.websocket.entity.DataMsgVo;
import com.sxpcwlkj.websocket.enums.CmdEnum;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * @author xijue
 * @ClassName SocketHandler
 * @description: websocket消息处理类
 * @date 2024年10月23日
 * @version: 1.0
 */
@Component
@Slf4j
public class SocketHandler extends AbstractWebSocketHandler  {


    /**
     * 连接成功后
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        //socket连接成功后触发
        log.info("建立websocket连接");
        WebSocketUtil.add(session);
        //返回自己的sessionId
        WebSocketUtil.sendMsg(session, new DataMsg(CmdEnum.SUCCEED).setData(session.getId()));
    }

    /**
     * 客户端发送文本信息触发
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
        // 客户端发送普通文件信息时触发
//        log.info("发送文本消息");
        // 获得客户端传来的消息
        String payload = message.getPayload();
        log.info("服务端接收到消息 " + payload);
        DataMsgVo dataMsg = BeanUtil.toBean(JSONUtil.toBean(payload, JSONObject.class), DataMsgVo.class);
        WebSocketService.receiverMsg(session,dataMsg);
    }

    /**
     * 客户端发送二进信息是触发
     */
    @Override
    protected void handleBinaryMessage(@NotNull WebSocketSession session, @NotNull BinaryMessage message) throws Exception {
        //客户端发送二进信息是触发
        log.info("发送二进制消息");
    }

    /**
     * 心跳监测的回复
     * 针对ping帧的恢复，非text形式的消息接收
     */
    @Override
    protected void handlePongMessage(@NotNull WebSocketSession session, @NotNull PongMessage message) throws Exception {

    }


    /**
     * 异常处理
     */
    @Override
    public void handleTransportError(WebSocketSession session, @NotNull Throwable exception) throws Exception {
        //异常时触发
        log.error("异常处理");
        WebSocketUtil.close(session.getId());
    }

    /**
     * 关闭websocket连接
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        // socket连接关闭后触发
        log.info("关闭websocket连接");
        WebSocketUtil.close(session.getId());
    }


    /**
     * 是否支持分片消息
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
