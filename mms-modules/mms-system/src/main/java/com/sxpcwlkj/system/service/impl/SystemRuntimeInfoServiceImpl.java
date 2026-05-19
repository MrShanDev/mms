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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * 系统运行信息（实时）
 */
@Service
@RequiredArgsConstructor
public class SystemRuntimeInfoServiceImpl implements SystemRuntimeInfoService {

    private final MmsAdminProperties mmsAdminProperties;
    private final Environment environment;
    private final DataSource dataSource;
    private final SlowSqlRecorder slowSqlRecorder;

    /** 采样间隔（ms）：用于计算 CPU 使用率 */
    private static final int CPU_SAMPLE_MS = 300;

    /** 占位：未发现 Docker 独立卷时第三条磁盘的 mount（与前端常量一致） */
    private static final String SYNTH_MOUNT_DOCKER_UNAVAILABLE = "--docker-slot-empty--";

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
        app.setServerPort(readConfiguredServerPort());
        return app;
    }

    /** 取自 Spring {@code server.port}（与实际随机端口可能不一致，仅作运维参考） */
    private Integer readConfiguredServerPort() {
        try {
            String raw = environment.getProperty("server.port");
            if (raw == null || raw.isBlank()) {
                return null;
            }
            int p = Integer.parseInt(raw.trim());
            return p > 0 ? p : null;
        } catch (Throwable ignore) {
            return null;
        }
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

    /**
     * 首页磁盘三块表盘数据源（固定语义顺序）：
     * 1）系统根卷（{@code /} 或 Windows 盘符根）；
     * 2）macOS VM 分区（{@code /System/Volumes/VM}）；
     * 3）容器存储（默认检测 {@code /var/lib/docker} 等，或由 DOCKER_* 指定）。
     */
    private List<SystemRuntimeInfoVo.Disk> buildDisks(FileSystem fs) {
        LinkedHashMap<String, SystemRuntimeInfoVo.Disk> map = scanFileStoresNormalized(fs);

        List<SystemRuntimeInfoVo.Disk> out = new ArrayList<>(3);
        SystemRuntimeInfoVo.Disk system = resolveSystemRootDisk(map);
        SystemRuntimeInfoVo.Disk vmVol = resolveMacVmVolumeDisk(map);
        SystemRuntimeInfoVo.Disk dockerVol = resolveDockerDataDisk(map, system, vmVol);

        if (system != null) {
            out.add(system);
        }
        if (vmVol != null) {
            out.add(vmVol);
        }
        if (dockerVol != null) {
            out.add(dockerVol);
        } else if (!out.isEmpty()) {
            out.add(syntheticDockerUnavailableDisk());
        }

        if (out.isEmpty()) {
            map.values().stream().limit(3).forEach(out::add);
        }
        return out;
    }

    private LinkedHashMap<String, SystemRuntimeInfoVo.Disk> scanFileStoresNormalized(FileSystem fs) {
        LinkedHashMap<String, SystemRuntimeInfoVo.Disk> byNk = new LinkedHashMap<>();
        try {
            for (OSFileStore s : fs.getFileStores()) {
                SystemRuntimeInfoVo.Disk d = toDiskVoFromStore(s);
                if (d == null || d.getMount() == null || d.getMount().isBlank()) {
                    continue;
                }
                byNk.putIfAbsent(normMountKey(d.getMount()), d);
            }
        } catch (Throwable ignore) {
        }
        return byNk;
    }

    private SystemRuntimeInfoVo.Disk toDiskVoFromStore(OSFileStore s) {
        try {
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
            return d;
        } catch (Throwable ignore) {
            return null;
        }
    }

    private static String normMountKey(String mount) {
        if (mount == null) {
            return "";
        }
        String raw = mount.trim().replace('\\', '/');
        if (raw.isEmpty()) {
            return "";
        }
        if ("/".equals(raw)) {
            return "/";
        }
        if (raw.matches("(?i)[a-z]:/?")) {
            return raw.substring(0, 1).toLowerCase(Locale.ROOT) + ":";
        }
        Path p = Paths.get(raw).normalize();
        String s = p.toString().replace('\\', '/');
        while (s.length() > 1 && s.endsWith("/")) {
            s = s.substring(0, s.length() - 1);
        }
        return s.toLowerCase(Locale.ROOT);
    }

    /** 系统根：优先 Unix `/`；Windows 取首个形如 `c:` 的根挂载 */
    private SystemRuntimeInfoVo.Disk resolveSystemRootDisk(LinkedHashMap<String, SystemRuntimeInfoVo.Disk> map) {
        SystemRuntimeInfoVo.Disk d = map.get("/");
        if (d != null) {
            return d;
        }
        for (SystemRuntimeInfoVo.Disk x : map.values()) {
            String nk = normMountKey(x.getMount());
            if (nk.length() == 2 && nk.endsWith(":")) {
                return x;
            }
        }
        return map.values().stream().findFirst().orElse(null);
    }

    /** macOS 内置 VM/APFS swap 语义卷 */
    private SystemRuntimeInfoVo.Disk resolveMacVmVolumeDisk(LinkedHashMap<String, SystemRuntimeInfoVo.Disk> map) {
        SystemRuntimeInfoVo.Disk d = map.get("/system/volumes/vm");
        if (d != null) {
            return d;
        }
        final String vmNk = normMountKey("/System/Volumes/VM");
        for (SystemRuntimeInfoVo.Disk x : map.values()) {
            if (normMountKey(x.getMount()).equals(vmNk)) {
                return x;
            }
        }
        return null;
    }

    /** Docker/Podman 数据目录（Linux 服务端常见）；支持 DOCKER_DATA_ROOT */
    private SystemRuntimeInfoVo.Disk resolveDockerDataDisk(
            LinkedHashMap<String, SystemRuntimeInfoVo.Disk> map,
            SystemRuntimeInfoVo.Disk system,
            SystemRuntimeInfoVo.Disk vmVol) {

        HashSet<String> forbid = nkSetMounts(system, vmVol);
        SystemRuntimeInfoVo.Disk best = pickDockerFromOshiStores(map, forbid);

        for (Path probe : dockerProbeDirectories()) {
            SystemRuntimeInfoVo.Disk probed = diskFromProbePath(probe);
            if (probed == null) {
                continue;
            }
            String pk = normMountKey(probed.getMount());
            if (forbid.contains(pk)) {
                continue;
            }
            if (best == null || dockerRank(probed) < dockerRank(best)) {
                best = probed;
            }
        }
        return best;
    }

    private static Path[] dockerProbeDirectories() {
        List<Path> paths = new ArrayList<>();
        String env = readEnvDockerRoot();
        if (env != null) {
            paths.add(Paths.get(env));
        }
        paths.add(Paths.get("/var/lib/docker"));
        paths.add(Paths.get("/var/lib/docker-desktop"));
        paths.add(Paths.get("/var/lib/podman"));
        return paths.toArray(new Path[0]);
    }

    private static String readEnvDockerRoot() {
        for (String k : new String[] {"DOCKER_DATA_ROOT", "DOCKER_STORAGE_PATH", "DOCKER_ROOT"}) {
            try {
                String v = System.getenv(k);
                if (v != null && !v.isBlank()) {
                    return v.trim();
                }
            } catch (Throwable ignore) {
            }
        }
        return null;
    }

    private static HashSet<String> nkSetMounts(SystemRuntimeInfoVo.Disk... xs) {
        HashSet<String> s = new HashSet<>();
        for (SystemRuntimeInfoVo.Disk x : xs) {
            if (x != null && x.getMount() != null && !x.getMount().isBlank()) {
                s.add(normMountKey(x.getMount()));
            }
        }
        return s;
    }

    private SystemRuntimeInfoVo.Disk pickDockerFromOshiStores(LinkedHashMap<String, SystemRuntimeInfoVo.Disk> map, HashSet<String> forbid) {
        SystemRuntimeInfoVo.Disk best = null;
        int bestRank = Integer.MAX_VALUE;

        for (SystemRuntimeInfoVo.Disk x : map.values()) {
            String nk = normMountKey(x.getMount());
            if (forbid.contains(nk)) {
                continue;
            }
            if (nk.endsWith("/vm") || nk.contains("/system/volumes/vm")) {
                continue;
            }

            boolean pathHit = nk.equals("/var/lib/docker")
                    || nk.startsWith("/var/lib/docker")
                    || nk.contains("/podman/")
                    || nk.contains("/containers/storage");
            String nameLow = Objects.toString(x.getName(), "").toLowerCase(Locale.ROOT);
            boolean nameHit = nameLow.contains("docker") || nameLow.contains("podman");
            boolean loosePath = nk.contains("docker");

            if (!pathHit && !nameHit && !loosePath) {
                continue;
            }

            int rank = dockerRank(x);
            if (!pathHit && !nameHit && rank >= 25) {
                continue;
            }
            if (best == null || rank < bestRank) {
                best = x;
                bestRank = rank;
            }
        }
        return best;
    }

    /** Docker 未发现独立挂载时占位：容量为 0、占用率 0，仅占位第三块表盘 */
    private SystemRuntimeInfoVo.Disk syntheticDockerUnavailableDisk() {
        SystemRuntimeInfoVo.Disk d = new SystemRuntimeInfoVo.Disk();
        d.setMount(SYNTH_MOUNT_DOCKER_UNAVAILABLE);
        d.setName("Docker");
        d.setType("-");
        d.setTotal(0L);
        d.setFree(0L);
        d.setUsed(0L);
        d.setUsedPercent(0d);
        return d;
    }

    private static int dockerRank(SystemRuntimeInfoVo.Disk d) {
        String nk = normMountKey(d.getMount());
        String n = Objects.toString(d.getName(), "").toLowerCase(Locale.ROOT);
        String blob = nk + "|" + n;

        if (nk.equals("/var/lib/docker")) {
            return 0;
        }
        if (nk.startsWith("/var/lib/docker")) {
            return 1;
        }
        if (nk.contains("/var/lib/podman") || nk.contains("/containers/storage")) {
            return 2;
        }
        if (blob.contains("docker-desktop")) {
            return 10;
        }
        if (blob.contains("docker")) {
            return 20;
        }
        return 99;
    }

    /** 对已存在路径做 FileStore 探测（独立于 OSHI 枚举是否在列表中出现） */
    private SystemRuntimeInfoVo.Disk diskFromProbePath(Path probe) {
        try {
            Path p = probe.toAbsolutePath().normalize();
            if (!Files.exists(p)) {
                return null;
            }
            FileStore fst = Files.getFileStore(p);
            long total = fst.getTotalSpace();
            long usable = fst.getUsableSpace();
            if (total <= 0 && usable <= 0) {
                return null;
            }
            long free = fst.getUsableSpace();
            long used = Math.max(0, total - free);

            SystemRuntimeInfoVo.Disk d = new SystemRuntimeInfoVo.Disk();
            d.setName(String.valueOf(fst.name()));
            d.setMount(p.toString());
            try {
                d.setType(String.valueOf(fst.type()));
            } catch (Throwable ignore) {
                d.setType(null);
            }
            d.setTotal(total);
            d.setFree(free);
            d.setUsed(used);
            d.setUsedPercent(total <= 0 ? null : clamp01((double) used / total));
            return d;
        } catch (Throwable ignore) {
            return null;
        }
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
