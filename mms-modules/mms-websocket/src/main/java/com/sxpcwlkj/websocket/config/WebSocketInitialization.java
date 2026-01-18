package com.sxpcwlkj.websocket.config;

import com.sxpcwlkj.websocket.utils.PerformanceUtil;
import com.sxpcwlkj.websocket.utils.WebSocketUtil;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * @author mmsAdmin
 * @ClassName WebSocketInitialization
 * @description: WebSocket系统初始化和销毁配置
 * @date 2025年1月18日
 * @version: 1.0
 */
@Component
@Slf4j
public class WebSocketInitialization {

    /**
     * 应用启动完成后初始化WebSocket相关资源
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        log.info("开始初始化WebSocket系统资源...");

        try {
            // 预热连接池
            PerformanceUtil.warmUpConnections();

            // 初始化WebSocket工具类相关资源
            log.info("WebSocket系统资源初始化完成");
        } catch (Exception e) {
            log.error("WebSocket系统资源初始化失败", e);
        }
    }

    /**
     * 应用关闭前清理WebSocket相关资源
     */
    @PreDestroy
    public void destroy() {
        log.info("开始清理WebSocket系统资源...");

        try {
            // 关闭性能优化工具的资源
            PerformanceUtil.shutdown();

            // 关闭所有WebSocket连接
            WebSocketUtil.SESSION_POOL.forEach((sessionId, session) -> {
                try {
                    if (session.isOpen()) {
                        session.close();
                    }
                } catch (Exception e) {
                    log.warn("关闭WebSocket会话失败: {}", sessionId, e);
                }
            });

            log.info("WebSocket系统资源清理完成，共清理 {} 个会话", WebSocketUtil.SESSION_POOL.size());
        } catch (Exception e) {
            log.error("WebSocket系统资源清理失败", e);
        }
    }
}
