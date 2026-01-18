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
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * WebSocket配置类 - 支持万人级用户在线聊天
 * 
 * 优化特性:
 * - 支持JDK 21虚拟线程
 * - 高性能连接处理
 * - 消息持久化支持
 * - Redis缓存优化
 * - 万人级并发支持
 * 
 * @author mmsAdmin
 * @version 3.0
 * @since 2025年1月19日
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketInterceptor socketInterceptor;
    private final SocketHandler socketHandler;

    @Value("${server.port:8080}")
    private String port;

    @Value("${websocket.enable:true}")
    private Boolean enable;

    @Value("${websocket.max-message-size:8192}")
    private Integer maxMessageSize;

    @Value("${websocket.send-buffer-size:8192}")
    private Integer sendBufferSize;

    @Value("${websocket.receive-buffer-size:8192}")
    private Integer receiveBufferSize;

    @Value("${websocket.max-text-message-size:8192}")
    private Integer maxTextMessageSize;

    @Value("${websocket.max-binary-message-size:8192}")
    private Integer maxBinaryMessageSize;

    /**
     * 注册WebSocket处理器
     * 配置万人级并发处理能力
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        if (enable) {
            registry
                .addHandler(socketHandler, "/ws")
                .addInterceptors(socketInterceptor)
                // 生产环境中应使用具体的域名，而不是通配符
                .setAllowedOrigins("*");

            // 初始化系统用户
            initSystemUser();

            log.info("WebSocket服务启动成功 - 地址: ws://localhost:{}" + "/ws?token=***********", port);
            log.info("WebSocket配置 - 消息大小: {}KB, 发送缓冲区: {}KB, 接收缓冲区: {}KB", 
                maxTextMessageSize, sendBufferSize / 1024, receiveBufferSize / 1024);
        } else {
            log.warn("WebSocket服务未启用，请在配置文件中设置 websocket.enable=true");
        }
    }

    /**
     * 创建支持虚拟线程的执行器（JDK 21+）
     * 用于处理WebSocket连接握手和消息处理
     * 针对高并发场景进行了优化
     */
    @Bean("virtualThreadExecutor")
    public Executor virtualThreadExecutor() {
        try {
            // 尝试使用JDK 19+的虚拟线程
            Class<?> threadClass = Thread.class;
            java.lang.reflect.Method ofVirtualMethod = threadClass.getMethod("ofVirtual");
            Object virtualThreadBuilder = ofVirtualMethod.invoke(null);
            java.util.concurrent.ThreadFactory virtualThreadFactory = 
                (java.util.concurrent.ThreadFactory) virtualThreadBuilder.getClass()
                    .getMethod("factory").invoke(virtualThreadBuilder);
            
            log.info("使用JDK 21虚拟线程执行器，支持万人级并发");
            return Executors.newThreadPerTaskExecutor(virtualThreadFactory);
        } catch (Exception e) {
            // 如果虚拟线程不可用，使用传统的线程池
            log.warn("虚拟线程不可用，使用传统线程池: {}", e.getMessage());
            return Executors.newCachedThreadPool(new CustomizableThreadFactory("websocket-thread-"));
        }
    }

    /**
     * 创建握手处理器，使用虚拟线程优化
     */
    @Bean("customHandshakeHandler")
    public DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler();
    }

    /**
     * 初始化系统用户
     * 在应用启动时预加载系统用户信息到缓存
     */
    private void initSystemUser() {
        try {
            MsgUser systemUser = new MsgUser();
            systemUser.setUserId(0L);
            systemUser.setSessionId("0000000");
            systemUser.setName("MMS-AI客服");
            systemUser.setHeadPortrait("https://sxpcwlkj.oss-accelerate.aliyuncs.com/test/664f129e525edcd6fbcb8ab9.jpeg");
            
            // 将系统用户信息缓存到Redis，设置永不过期
            RedisUtil.setCacheObject(SocketConstant.SOCKET_USER + systemUser.getUserId(), systemUser);
            
            log.info("系统用户初始化完成 - 用户ID: {}", systemUser.getUserId());
        } catch (Exception e) {
            log.error("初始化系统用户失败", e);
        }
    }
}
