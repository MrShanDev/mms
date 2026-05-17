package com.sxpcwlkj.system.service.impl;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.util.Util;
import com.sxpcwlkj.system.entity.vo.SystemRuntimeTrendVo;
import com.sxpcwlkj.system.service.SystemRuntimeTrendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 运行趋势采样：默认每 5 秒采样一次，保留约 15 分钟数据
 */
@Service
@EnableScheduling
@Slf4j
public class SystemRuntimeTrendServiceImpl implements SystemRuntimeTrendService {

    private static final int SAMPLE_MS = 5000;
    private static final int MAX_POINTS = 180; // 180 * 5s = 15min

    private final Deque<SystemRuntimeTrendVo.Point> deque = new ArrayDeque<>();

    private final SystemInfo si = new SystemInfo();
    private final CentralProcessor processor = si.getHardware().getProcessor();
    private final GlobalMemory memory = si.getHardware().getMemory();

    private volatile long[] lastCpuTicks;

    @Scheduled(fixedDelay = SAMPLE_MS, initialDelay = 2000)
    public void sample() {
        try {
            SystemRuntimeTrendVo.Point p = new SystemRuntimeTrendVo.Point();
            p.setTimeMs(System.currentTimeMillis());

            // CPU（0~1）
            try {
                long[] prev = lastCpuTicks;
                if (prev == null) {
                    prev = processor.getSystemCpuLoadTicks();
                    lastCpuTicks = prev;
                    Util.sleep(200);
                }
                double cpu = processor.getSystemCpuLoadBetweenTicks(prev);
                lastCpuTicks = processor.getSystemCpuLoadTicks();
                p.setCpu(clamp01(cpu));
            } catch (Throwable ignore) {
                p.setCpu(null);
            }

            // 物理内存（0~1）
            try {
                long total = memory.getTotal();
                long free = memory.getAvailable();
                long used = Math.max(0, total - free);
                p.setMem(total <= 0 ? null : clamp01((double) used / total));
            } catch (Throwable ignore) {
                p.setMem(null);
            }

            // JVM 内存（0~1）
            try {
                Runtime r = Runtime.getRuntime();
                long max = r.maxMemory();
                long total = max > 0 ? max : r.totalMemory();
                long used = Math.max(0, r.totalMemory() - r.freeMemory());
                p.setJvm(total <= 0 ? null : clamp01((double) used / total));
            } catch (Throwable ignore) {
                p.setJvm(null);
            }

            synchronized (deque) {
                deque.addLast(p);
                while (deque.size() > MAX_POINTS) deque.removeFirst();
            }
        } catch (Exception e) {
            log.debug("runtime trend sample failed", e);
        }
    }

    @Override
    public SystemRuntimeTrendVo getTrend(int limit) {
        int n = Math.max(10, Math.min(limit, MAX_POINTS));
        List<SystemRuntimeTrendVo.Point> list = new ArrayList<>(n);
        synchronized (deque) {
            int skip = Math.max(0, deque.size() - n);
            int i = 0;
            for (SystemRuntimeTrendVo.Point p : deque) {
                if (i++ < skip) continue;
                list.add(p);
            }
        }
        SystemRuntimeTrendVo vo = new SystemRuntimeTrendVo();
        vo.setPoints(list);
        return vo;
    }

    private Double clamp01(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) return null;
        if (v < 0) return 0D;
        if (v > 1) return 1D;
        return v;
    }
}
