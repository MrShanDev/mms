package com.sxpcwlkj.system.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 系统运行趋势（实时采样）
 */
@Data
public class SystemRuntimeTrendVo implements Serializable {
    private List<Point> points;

    @Data
    public static class Point implements Serializable {
        private Long timeMs;
        /** 0~1 */
        private Double cpu;
        /** 0~1 */
        private Double mem;
        /** 0~1 */
        private Double jvm;
    }
}

