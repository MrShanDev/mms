package com.sxpcwlkj.system.service.impl;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;
import com.sxpcwlkj.common.properties.MmsAdminProperties;
import com.sxpcwlkj.system.monitor.SlowSqlRecorder;
import com.sxpcwlkj.system.entity.vo.SystemRuntimeInfoVo;
import com.sxpcwlkj.system.service.SystemRuntimeInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统运行信息（实时）
 */
@Service
@RequiredArgsConstructor
public class SystemRuntimeInfoServiceImpl implements SystemRuntimeInfoService {

    private final MmsAdminProperties mmsAdminProperties;
    private final DataSource dataSource;
    private final SlowSqlRecorder slowSqlRecorder;

    /** 采样间隔（ms）：用于计算 CPU 使用率 */
    private static final int CPU_SAMPLE_MS = 300;

    @Override
    public SystemRuntimeInfoVo getRuntimeInfo() {
        SystemRuntimeInfoVo vo = new SystemRuntimeInfoVo();

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        var hal = si.getHardware();
        CentralProcessor processor = hal.getProcessor();
        GlobalMemory memory = hal.getMemory();
        FileSystem fs = os.getFileSystem();

        vo.setApp(buildApp());
        vo.setOs(buildOs(os));
        vo.setCpu(buildCpu(processor));
        vo.setMemory(buildMemory(memory));
        vo.setJvmMemory(buildJvmMemory());
        vo.setDisks(buildDisks(fs));
        vo.setDb(buildDb());
        vo.setDbPool(buildDbPool());
        vo.setSlowSqlRecent(buildSlowSqlRecent());

        return vo;
    }

    private SystemRuntimeInfoVo.App buildApp() {
        SystemRuntimeInfoVo.App app = new SystemRuntimeInfoVo.App();
        app.setName(mmsAdminProperties.getName());
        app.setVersion(mmsAdminProperties.getVersion());
        app.setOrganization(mmsAdminProperties.getOrganization());
        app.setDocUrl(mmsAdminProperties.getDocUrl());
        app.setDescribe(mmsAdminProperties.getDescribe());

        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        app.setServerTimeMs(System.currentTimeMillis());
        app.setStartTimeMs(rb.getStartTime());
        app.setUptimeMs(rb.getUptime());

        // JDK/JVM 信息
        app.setJdkVersion(System.getProperty("java.version"));
        app.setJvmName(System.getProperty("java.vm.name"));
        app.setJavaHome(System.getProperty("java.home"));

        // pid
        try {
            app.setPid(ProcessHandle.current().pid());
        } catch (Throwable ignore) {
            app.setPid(null);
        }
        return app;
    }

    private SystemRuntimeInfoVo.Os buildOs(OperatingSystem os) {
        SystemRuntimeInfoVo.Os o = new SystemRuntimeInfoVo.Os();
        o.setName(os.getFamily());
        o.setVersion(os.getVersionInfo().getVersion());
        o.setArch(System.getProperty("os.arch"));
        try {
            o.setHostname(os.getNetworkParams().getHostName());
        } catch (Throwable ignore) {
            o.setHostname(null);
        }
        return o;
    }

    private SystemRuntimeInfoVo.Cpu buildCpu(CentralProcessor processor) {
        SystemRuntimeInfoVo.Cpu cpu = new SystemRuntimeInfoVo.Cpu();
        cpu.setCores(processor.getLogicalProcessorCount());

        // 系统 / 进程 CPU（优先使用 JDK MXBean；没有则回退 OSHI 采样）
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean mx) {
            try {
                cpu.setSystemLoad(clamp01(mx.getSystemCpuLoad()));
            } catch (Throwable ignore) {
                cpu.setSystemLoad(null);
            }
            try {
                cpu.setProcessLoad(clamp01(mx.getProcessCpuLoad()));
            } catch (Throwable ignore) {
                cpu.setProcessLoad(null);
            }
        } else {
            // 采样计算 0~1 的系统 CPU 使用率
            try {
                long[] prevTicks = processor.getSystemCpuLoadTicks();
                Util.sleep(CPU_SAMPLE_MS);
                double systemLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks);
                cpu.setSystemLoad(clamp01(systemLoad));
            } catch (Throwable ignore) {
                cpu.setSystemLoad(null);
            }
            cpu.setProcessLoad(null);
        }

        try {
            double[] la = processor.getSystemLoadAverage(3);
            cpu.setLoadAverage(new Double[]{
                    la.length > 0 && la[0] >= 0 ? la[0] : null,
                    la.length > 1 && la[1] >= 0 ? la[1] : null,
                    la.length > 2 && la[2] >= 0 ? la[2] : null,
            });
        } catch (Throwable ignore) {
            cpu.setLoadAverage(null);
        }
        return cpu;
    }

    private SystemRuntimeInfoVo.Mem buildMemory(GlobalMemory memory) {
        SystemRuntimeInfoVo.Mem m = new SystemRuntimeInfoVo.Mem();
        long total = memory.getTotal();
        long free = memory.getAvailable();
        long used = Math.max(0, total - free);
        m.setTotal(total);
        m.setFree(free);
        m.setUsed(used);
        m.setUsedPercent(total <= 0 ? null : clamp01((double) used / total));
        return m;
    }

    private SystemRuntimeInfoVo.Mem buildJvmMemory() {
        SystemRuntimeInfoVo.Mem m = new SystemRuntimeInfoVo.Mem();
        Runtime r = Runtime.getRuntime();
        long total = r.totalMemory();
        long free = r.freeMemory();
        long used = Math.max(0, total - free);
        // max 可能为 -1（不限制）；这里优先用 max 作为 total 展示
        long max = r.maxMemory();
        long showTotal = max > 0 ? max : total;
        m.setTotal(showTotal);
        m.setFree(Math.max(0, showTotal - used));
        m.setUsed(used);
        m.setUsedPercent(showTotal <= 0 ? null : clamp01((double) used / showTotal));
        return m;
    }

    private List<SystemRuntimeInfoVo.Disk> buildDisks(FileSystem fs) {
        List<SystemRuntimeInfoVo.Disk> list = new ArrayList<>();
        try {
            for (OSFileStore s : fs.getFileStores()) {
                SystemRuntimeInfoVo.Disk d = new SystemRuntimeInfoVo.Disk();
                d.setName(s.getName());
                d.setMount(s.getMount());
                d.setType(s.getType());
                long total = s.getTotalSpace();
                long free = s.getUsableSpace();
                long used = Math.max(0, total - free);
                d.setTotal(total);
                d.setFree(free);
                d.setUsed(used);
                d.setUsedPercent(total <= 0 ? null : clamp01((double) used / total));
                list.add(d);
                // 首页只展示前几个，避免过长
                if (list.size() >= 6) break;
            }
        } catch (Throwable ignore) {
            return list;
        }
        return list;
    }

    private SystemRuntimeInfoVo.Db buildDb() {
        SystemRuntimeInfoVo.Db db = new SystemRuntimeInfoVo.Db();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            db.setAvailable(true);
            db.setProductName(meta.getDatabaseProductName());
            db.setProductVersion(meta.getDatabaseProductVersion());
            db.setDriverName(meta.getDriverName());
            db.setDriverVersion(meta.getDriverVersion());
            db.setUrl(meta.getURL());
            db.setUsername(meta.getUserName());
        } catch (Throwable e) {
            db.setAvailable(false);
        }
        return db;
    }

    private SystemRuntimeInfoVo.DbPool buildDbPool() {
        SystemRuntimeInfoVo.DbPool pool = new SystemRuntimeInfoVo.DbPool();
        Object hikari = tryUnwrapHikari(dataSource);
        if (hikari != null && hikari.getClass().getName().contains("HikariDataSource")) {
            pool.setType("HikariCP");
            try {
                Object mx = hikari.getClass().getMethod("getHikariPoolMXBean").invoke(hikari);
                if (mx != null) {
                    pool.setActive(intValue(mx, "getActiveConnections"));
                    pool.setIdle(intValue(mx, "getIdleConnections"));
                    pool.setTotal(intValue(mx, "getTotalConnections"));
                    pool.setPending(intValue(mx, "getThreadsAwaitingConnection"));
                }
            } catch (Throwable ignore) {
            }
            try {
                Object v = hikari.getClass().getMethod("getMaximumPoolSize").invoke(hikari);
                pool.setMax(v == null ? null : Integer.parseInt(String.valueOf(v)));
            } catch (Throwable ignore) {
            }
            return pool;
        }
        pool.setType(dataSource == null ? "-" : dataSource.getClass().getSimpleName());
        return pool;
    }

    private List<SystemRuntimeInfoVo.SlowSql> buildSlowSqlRecent() {
        List<SlowSqlRecorder.Entry> list = slowSqlRecorder.latest(20);
        List<SystemRuntimeInfoVo.SlowSql> out = new ArrayList<>(list.size());
        for (SlowSqlRecorder.Entry e : list) {
            SystemRuntimeInfoVo.SlowSql s = new SystemRuntimeInfoVo.SlowSql();
            s.setTimeMs(e.getTimeMs());
            s.setElapsedMs(e.getElapsedMs());
            s.setDatasource(e.getDatasource());
            s.setSql(e.getSql());
            out.add(s);
        }
        return out;
    }

    /**
     * dynamic-datasource / p6spy 等场景下尝试解包，找到真正的 HikariDataSource
     */
    private Object tryUnwrapHikari(Object ds) {
        if (ds == null) return null;
        if (ds.getClass().getName().contains("HikariDataSource")) return ds;

        // dynamic datasource：com.baomidou.dynamic.datasource.DynamicRoutingDataSource
        try {
            Class<?> clazz = ds.getClass();
            if (clazz.getName().contains("DynamicRoutingDataSource")) {
                // getPrimary()
                String primary = null;
                try {
                    var m = clazz.getMethod("getPrimary");
                    Object v = m.invoke(ds);
                    if (v != null) primary = String.valueOf(v);
                } catch (Throwable ignore) {
                }
                // getDataSources()
                try {
                    var m = clazz.getMethod("getDataSources");
                    Object v = m.invoke(ds);
                    if (v instanceof Map<?, ?> map) {
                        Object target = primary != null && map.containsKey(primary) ? map.get(primary) : map.values().stream().findFirst().orElse(null);
                        return tryUnwrapHikari(target);
                    }
                } catch (Throwable ignore) {
                }
            }
        } catch (Throwable ignore) {
        }

        // p6spy：com.p6spy.engine.spy.P6DataSource（字段 realDataSource）
        try {
            Class<?> clazz = ds.getClass();
            if (clazz.getName().contains("p6spy") || clazz.getName().contains("P6DataSource")) {
                var f = clazz.getDeclaredField("realDataSource");
                f.setAccessible(true);
                Object real = f.get(ds);
                return tryUnwrapHikari(real);
            }
        } catch (Throwable ignore) {
        }

        // 通用 unwrap（JDBC 4.0）
        try {
            if (ds instanceof javax.sql.DataSource d) {
                try {
                    Class<?> hikariClass = Class.forName("com.zaxxer.hikari.HikariDataSource");
                    if (d.isWrapperFor(hikariClass)) {
                        return d.unwrap(hikariClass);
                    }
                } catch (Throwable ignore) {
                }
            }
        } catch (Throwable ignore) {
        }

        return null;
    }

    private Integer intValue(Object target, String method) {
        try {
            Object v = target.getClass().getMethod(method).invoke(target);
            return v == null ? null : Integer.parseInt(String.valueOf(v));
        } catch (Throwable ignore) {
            return null;
        }
    }

    private Double clamp01(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) return null;
        if (v < 0) return 0D;
        if (v > 1) return 1D;
        return v;
    }
}
