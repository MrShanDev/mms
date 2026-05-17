package com.sxpcwlkj.system.service;

import com.sxpcwlkj.system.entity.vo.SystemRuntimeTrendVo;

/**
 * 系统运行趋势（CPU/内存/JVM 内存）
 */
public interface SystemRuntimeTrendService {
    SystemRuntimeTrendVo getTrend(int limit);
}

