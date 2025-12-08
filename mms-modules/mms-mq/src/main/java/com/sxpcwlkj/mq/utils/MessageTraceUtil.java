package com.sxpcwlkj.mq.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 消息追踪工具类
 */
@Slf4j
@Component
public class MessageTraceUtil {

    private static final String TRACE_ID = "traceId";

    /**
     * 生成追踪ID
     */
    public static String generateTraceId() {
        return "TRACE_" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 设置追踪ID
     */
    public static void setTraceId(String traceId) {
        if (traceId == null || traceId.isEmpty()) {
            traceId = generateTraceId();
        }
        MDC.put(TRACE_ID, traceId);
        log.debug("设置消息追踪ID: {}", traceId);
    }

    /**
     * 获取当前追踪ID
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    /**
     * 清理追踪ID
     */
    public static void clearTraceId() {
        MDC.remove(TRACE_ID);
        log.debug("清理消息追踪ID");
    }

    /**
     * 在现有追踪上下文中执行任务
     */
    public static void executeWithTrace(Runnable task) {
        String originalTraceId = getTraceId();
        try {
            if (originalTraceId == null) {
                setTraceId(generateTraceId());
            }
            task.run();
        } finally {
            if (originalTraceId == null) {
                clearTraceId();
            }
        }
    }
}
