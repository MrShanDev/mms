package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginRuntimeMode;
import com.sxpcwlkj.plugin.host.PluginHostProperties;
import com.sxpcwlkj.plugin.host.PluginSubprocessSnapshot;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * INDEPENDENT_PROCESS 模式下可选的子进程生命周期（受 {@link PluginHostProperties#isSubprocessLaunchEnabled()} 控制）。
 */
@Slf4j
public final class PluginSubprocessManager {

    private final Map<String, Process> byKey = new ConcurrentHashMap<>();
    private final Map<String, Integer> effectivePortByKey = new ConcurrentHashMap<>();
    private final Map<String, String> lastErrorByKey = new ConcurrentHashMap<>();
    private final Map<String, Path> versionDirByKey = new ConcurrentHashMap<>();

    public List<PluginSubprocessSnapshot> listSnapshots() {
        Set<String> keys = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        keys.addAll(byKey.keySet());
        keys.addAll(lastErrorByKey.keySet());
        keys.addAll(effectivePortByKey.keySet());
        List<PluginSubprocessSnapshot> list = new ArrayList<>();
        for (String key : keys) {
            list.add(buildSnapshot(key));
        }
        return Collections.unmodifiableList(list);
    }

    public PluginSubprocessSnapshot snapshotFor(String pluginId, String version) {
        if (pluginId == null || version == null) {
            return null;
        }
        String key = pluginId.trim() + "@" + version.trim();
        Process p = byKey.get(key);
        String err = lastErrorByKey.get(key);
        if (p == null && err == null) {
            return null;
        }
        return buildSnapshot(key);
    }

    private PluginSubprocessSnapshot buildSnapshot(String key) {
        Process p = byKey.get(key);
        ParsedKey pk = parseKey(key);
        int port = effectivePortByKey.getOrDefault(key, 0);
        boolean alive = p != null && p.isAlive();
        Long pid = null;
        try {
            if (p != null) {
                pid = p.pid();
            }
        } catch (Exception ignored) {
            // ignore
        }
        return new PluginSubprocessSnapshot(
                key, pk.pluginId(), pk.version(), port, pid, alive, lastErrorByKey.get(key), 0, null);
    }

    public synchronized void startIfEnabled(
            PluginHostProperties props,
            PluginDescriptor desc,
            Path versionDir,
            String pluginKey,
            int effectivePort) {
        lastErrorByKey.remove(pluginKey);
        effectivePortByKey.remove(pluginKey);
        Path absVer = versionDir.toAbsolutePath().normalize();
        if (!props.isSubprocessLaunchEnabled()) {
            if (desc.runtimeModeOrDefault() == PluginRuntimeMode.INDEPENDENT_PROCESS) {
                versionDirByKey.put(pluginKey, absVer);
            }
            return;
        }
        if (desc.runtimeModeOrDefault() != PluginRuntimeMode.INDEPENDENT_PROCESS) {
            return;
        }
        stop(pluginKey, props);
        versionDirByKey.put(pluginKey, absVer);
        String main = desc.getMainClass();
        if (main == null || main.isBlank()) {
            log.warn("子进程启动已开启但插件未配置 mainClass，跳过: {}", pluginKey);
            lastErrorByKey.put(pluginKey, "未配置 mainClass");
            return;
        }
        if (effectivePort < 1) {
            log.warn("子进程有效端口非法，跳过: {} port={}", pluginKey, effectivePort);
            lastErrorByKey.put(pluginKey, "有效端口非法");
            return;
        }
        if (props.isSubprocessPortDoubleCheckBeforeLaunch() && effectivePort > 0) {
            if (!PortManager.isTcpPortAvailable(effectivePort)) {
                String msg = "子进程启动前检测到端口已被占用（非空闲）: " + effectivePort;
                log.warn("{} pluginKey={}", msg, pluginKey);
                lastErrorByKey.put(pluginKey, msg);
                return;
            }
        }
        Path lib = versionDir.resolve("lib").toAbsolutePath().normalize();
        if (!lib.toFile().isDirectory()) {
            log.warn("子进程启动失败，lib 不存在: {}", lib);
            lastErrorByKey.put(pluginKey, "lib 目录不存在");
            return;
        }
        String cp = lib + File.separator + "*";
        List<String> cmd = new ArrayList<>();
        cmd.add(props.getSubprocessJavaBinary());
        cmd.add("-cp");
        cmd.add(cp);
        cmd.add(main.trim());
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(versionDir.toFile());
        Map<String, String> env = pb.environment();
        env.put("MMS_PLUGIN_ID", desc.getId());
        env.put("MMS_PLUGIN_PORT", String.valueOf(effectivePort));
        env.put("MMS_PLUGIN_GRACEFUL_STOP_POLICY", "SIGTERM_THEN_FORCIBLE");
        String token = props.getSubprocessAdminToken();
        if (token != null && !token.isBlank()) {
            env.put("MMS_PLUGIN_SUBPROCESS_TOKEN", token);
        }
        String base = props.getSubprocessPeerContextBaseUrl();
        if (base != null && !base.isBlank()) {
            env.put("MMS_PLUGIN_HOST_CONTEXT_BASE", base.trim());
        }
        Path shutdownFile = versionDir.resolve(".mms-plugin-shutdown");
        if (props.isSubprocessShutdownSignalFileEnabled()) {
            try {
                Files.deleteIfExists(shutdownFile);
                env.put("MMS_PLUGIN_SHUTDOWN_FILE", shutdownFile.toAbsolutePath().toString());
            } catch (Exception e) {
                log.debug("清理 shutdown 标记失败: {}", shutdownFile, e);
            }
        }
        pb.redirectErrorStream(true);
        Path logFile = versionDir.resolve("subprocess.log");
        try {
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile.toFile()));
            Process p = pb.start();
            byKey.put(pluginKey, p);
            effectivePortByKey.put(pluginKey, effectivePort);
            log.info("插件子进程已启动 key={} pid={} port={} 日志追加至 {}", pluginKey, p.pid(), effectivePort, logFile);
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            log.warn("插件子进程启动失败 {}: {}", pluginKey, msg);
            lastErrorByKey.put(pluginKey, msg);
        }
    }

    public synchronized void stop(String pluginKey, PluginHostProperties props) {
        int graceSeconds =
                props != null ? props.getSubprocessShutdownGracePeriodSeconds() : 15;
        Process p = byKey.remove(pluginKey);
        effectivePortByKey.remove(pluginKey);
        Path versionDir = versionDirByKey.get(pluginKey);
        if (props != null
                && props.isSubprocessShutdownSignalFileEnabled()
                && versionDir != null) {
            Path shutdownFile = versionDir.resolve(".mms-plugin-shutdown");
            try {
                Files.writeString(
                        shutdownFile,
                        "host requested shutdown at " + System.nanoTime() + "\n",
                        StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.debug("写入 shutdown 标记失败: {}", shutdownFile, e);
            }
        }
        if (p == null) {
            versionDirByKey.remove(pluginKey);
            return;
        }
        try {
            p.destroy();
            long grace = Math.max(1, graceSeconds);
            if (p.waitFor(grace, TimeUnit.SECONDS)) {
                log.info("插件子进程已优雅退出 key={}", pluginKey);
            } else {
                log.warn("插件子进程未在 {}s 内退出，强制终止: {}", grace, pluginKey);
                p.destroyForcibly();
                p.waitFor(5, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.debug("停止子进程异常 {}: {}", pluginKey, e.getMessage());
            try {
                p.destroyForcibly();
            } catch (Exception ignored) {
                // ignore
            }
        }
        if (props != null
                && props.isSubprocessShutdownSignalFileEnabled()
                && versionDir != null) {
            try {
                Files.deleteIfExists(versionDir.resolve(".mms-plugin-shutdown"));
            } catch (Exception ignored) {
                // ignore
            }
        }
        versionDirByKey.remove(pluginKey);
        lastErrorByKey.remove(pluginKey);
    }

    public synchronized void stopAll(PluginHostProperties props) {
        for (String k : new ArrayList<>(byKey.keySet())) {
            stop(k, props);
        }
    }

    private static ParsedKey parseKey(String pluginKey) {
        int at = pluginKey.indexOf('@');
        if (at <= 0 || at >= pluginKey.length() - 1) {
            return new ParsedKey(pluginKey, "");
        }
        return new ParsedKey(pluginKey.substring(0, at), pluginKey.substring(at + 1));
    }

    private record ParsedKey(String pluginId, String version) {}
}
