package com.sxpcwlkj.websocket.config;

import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.websocket.constant.SocketConstant;
import com.sxpcwlkj.websocket.entity.MsgUser;
import com.sxpcwlkj.websocket.handler.SocketHandler;
import com.sxpcwlkj.websocket.interceptor.SocketInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * @author xijue
 * @ClassName WebSocketConfig
 * @description: 注册 webSocket
 * @date 2024年10月23日
 * @version: 1.0
 */
@Configuration //告诉SpringBoot这是一个配置类，让SpringBoot加载配置
@EnableWebSocket //用于开启注解接收和发送消息
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketInterceptor socketInterceptor;

    private final SocketHandler socketHandler;

    @Value("${server.port}")
    private String port;

    @Value("${websocket.enable}")
    private Boolean enable;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        if(enable){
            // 注册 socket处理器
            registry
                    //websocket连接地址 ws://localhost:8080/ws
                    .addHandler(socketHandler, "/ws")
                    //添加拦截器
                    .addInterceptors(socketInterceptor)
                    //允许跨域，方便本地调试，生产建议去掉
                    .setAllowedOrigins("*");
            //需要登录验证

            //系统管理员信息
            MsgUser msgUser = new MsgUser();
            msgUser.setUserId(0L);
            msgUser.setSessionId("0000000");
            msgUser.setName("MMS-AI客服");
            msgUser.setHeadPortrait("https://sxpcwlkj.oss-accelerate.aliyuncs.com/test/664f129e525edcd6fbcb8ab9.jpeg");
            RedisUtil.setCacheObject(SocketConstant.SOCKET_USER+msgUser.getUserId(),msgUser);

            log.info("webSocket服务启动:\tws://localhost:" + port + "/ws?token=***********");
        }
        log.error("webSocket服务关闭状态!");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        // 消息缓冲区大小的大小
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }


}
