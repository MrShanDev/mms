package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginDescriptorReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 从插件版本目录 {@code lib/*.jar} 中解析含 {@code plugin.json} 的描述符。
 * <p>
 * {@code lib} 下常有主插件 JAR + 依赖 JAR；若依赖包误含或蹭用了 {@code META-INF/mms/plugin.json}，
 * 按字典序「第一个可解析的 JAR」会错拿描述符，导致市场页 {@code sysConfig} 为空。提供
 * {@link #tryReadForPlugin(Path, String)}：在给出期望 {@code id} 时优先匹配
 * {@link PluginDescriptor#getId()} 与之一致的 JAR，再回退为历史行为（首个成功解析的 JAR）。
 * </p>
 */
public final class PluginDescriptorProbe {

    public record ProbeResult(PluginDescriptor descriptor, Path descriptorJar) {}

    private PluginDescriptorProbe() {
    }

    /**
     * 兼容旧调用：无期望 id，取 lib 下首个成功解析的 JAR（字典序）。
     */
    public static Optional<ProbeResult> tryRead(Path versionDir) throws Exception {
        return tryReadForPlugin(versionDir, null);
    }

    /**
     * @param preferredPluginId 非空时优先选用 {@code descriptor.id} 与之相等的 JAR；无任何匹配时回退为 {@link #tryRead}。
     */
    public static Optional<ProbeResult> tryReadForPlugin(Path versionDir, String preferredPluginId) throws Exception {
        Path libDir = versionDir.resolve("lib");
        if (!Files.isDirectory(libDir)) {
            return Optional.empty();
        }
        List<Path> jars;
        try (Stream<Path> st = Files.list(libDir)) {
            jars = st.filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar"))
                    .sorted()
                    .toList();
        }
        String want =
                preferredPluginId != null && !preferredPluginId.isBlank()
                        ? preferredPluginId.trim()
                        : null;
        if (want != null) {
            for (Path jar : jars) {
                try {
                    PluginDescriptor d = PluginDescriptorReader.readFromJar(jar);
                    if (d.getId() != null && want.equals(d.getId().trim())) {
                        return Optional.of(new ProbeResult(d, jar));
                    }
                } catch (PluginException ignored) {
                    // 尝试下一个 jar
                }
            }
        }
        for (Path jar : jars) {
            try {
                PluginDescriptor d = PluginDescriptorReader.readFromJar(jar);
                return Optional.of(new ProbeResult(d, jar));
            } catch (PluginException ignored) {
                // 尝试下一个 jar
            }
        }
        return Optional.empty();
    }
}
