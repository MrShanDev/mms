package com.sxpcwlkj.websocket.interceptor;


import cn.dev33.satoken.stp.StpUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author shanpengnian
 * @ClassName TextWebSocketHandler
 * @description: TODO
 * @date 2024年10月24日
 * @version: 1.0
 */
@Slf4j
@Component
public class SocketInterceptor implements HandshakeInterceptor {

    /**
     * 握手前
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request,
                                   @NotNull ServerHttpResponse response,
                                   @NotNull WebSocketHandler wsHandler,
                                   @NotNull Map<String, Object> attributes) throws Exception {
        // 获得请求参数
        String authToken = ((ServletServerHttpRequest) request).getServletRequest().getParameter("token");
        if (!StringUtils.isEmpty(authToken)) {
            Object objId = StpUtil.getLoginIdByToken(authToken);
            if (null == objId) {
                log.info("用户登录已失效");
                return Boolean.FALSE;
            }

            attributes.put(SocketConstant.SOCKET_TOKEN, authToken);
            attributes.put(SocketConstant.SOCKET_ID, objId.toString());
            log.info("用户id:{}ws握手成功", objId);
            return Boolean.TRUE;
        }
        log.info("握手失败，用户登录已失效");
        return Boolean.FALSE;
    }

    /**
     * 握手后
     */
    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {
        log.info("握手完成");
    }
}
