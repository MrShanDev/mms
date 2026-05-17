package com.sxpcwlkj.system.monitor;

import com.sxpcwlkj.system.entity.vo.SystemRuntimeInfoVo;
import com.sxpcwlkj.system.service.SystemRuntimeInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * SSE 推送任务：定时把 runtimeInfo + 当前点位推给前端
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SystemRuntimeSsePusher {

    private final SystemRuntimeSseHub hub;
    private final SystemRuntimeInfoService runtimeInfoService;

    /** 推送频率：2 秒一次（可按需调整） */
    @Scheduled(fixedDelayString = "${mms.monitor.sse-push-ms:2000}", initialDelay = 1500)
    public void push() {
        try {
            SystemRuntimeInfoVo info = runtimeInfoService.getRuntimeInfo();
            Map<String, Object> payload = new HashMap<>();
            payload.put("info", info);
            payload.put("point", buildPoint(info));
            hub.broadcast(payload);
        } catch (Exception e) {
            log.debug("runtime sse push failed: {}", e.getMessage());
        }
    }

    private Map<String, Object> buildPoint(SystemRuntimeInfoVo info) {
        Map<String, Object> p = new HashMap<>();
        p.put("timeMs", System.currentTimeMillis());
        try {
            p.put("cpu", info.getCpu() != null ? info.getCpu().getSystemLoad() : null);
        } catch (Exception ignored) {
            p.put("cpu", null);
        }
        try {
            p.put("mem", info.getMemory() != null ? info.getMemory().getUsedPercent() : null);
        } catch (Exception ignored) {
            p.put("mem", null);
        }
        try {
            p.put("jvm", info.getJvmMemory() != null ? info.getJvmMemory().getUsedPercent() : null);
        } catch (Exception ignored) {
            p.put("jvm", null);
        }
        return p;
    }
}

