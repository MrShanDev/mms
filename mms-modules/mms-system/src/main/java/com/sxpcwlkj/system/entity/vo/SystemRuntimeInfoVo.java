package com.sxpcwlkj.system.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 系统运行信息（实时）
 */
@Data
public class SystemRuntimeInfoVo implements Serializable {
    private App app;
    private Os os;
    private Cpu cpu;
    private Mem memory;
    private Mem jvmMemory;
    private List<Disk> disks;
    private Db db;
    private DbPool dbPool;
    private List<SlowSql> slowSqlRecent;

    @Data
    public static class App implements Serializable {
        private String name;
        private String version;
        private String organization;
        private String docUrl;
        private String describe;

        private Long serverTimeMs;
        private Long startTimeMs;
        private Long uptimeMs;
        private Long pid;

        private String jdkVersion;
        private String jvmName;
        private String javaHome;
    }

    @Data
    public static class Os implements Serializable {
        private String name;
        private String version;
        private String arch;
        private String hostname;
    }

    @Data
    public static class Cpu implements Serializable {
        private Integer cores;
        /** 0~1 */
        private Double systemLoad;
        /** 0~1 */
        private Double processLoad;
        private Double[] loadAverage;
    }

    @Data
    public static class Mem implements Serializable {
        /** bytes */
        private Long total;
        /** bytes */
        private Long used;
        /** bytes */
        private Long free;
        /** 0~1 */
        private Double usedPercent;
    }

    @Data
    public static class Disk implements Serializable {
        private String name;
        private String mount;
        private String type;
        /** bytes */
        private Long total;
        /** bytes */
        private Long used;
        /** bytes */
        private Long free;
        /** 0~1 */
        private Double usedPercent;
    }

    @Data
    public static class Db implements Serializable {
        private Boolean available;
        private String productName;
        private String productVersion;
        private String driverName;
        private String driverVersion;
        private String url;
        private String username;
    }

    @Data
    public static class DbPool implements Serializable {
        private String type;
        private Integer active;
        private Integer idle;
        private Integer total;
        private Integer pending;
        private Integer max;
    }

    @Data
    public static class SlowSql implements Serializable {
        private Long timeMs;
        private Long elapsedMs;
        private String datasource;
        private String sql;
    }
}
