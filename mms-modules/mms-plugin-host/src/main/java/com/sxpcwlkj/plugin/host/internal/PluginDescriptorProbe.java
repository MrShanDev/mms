package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginDescriptorReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 从插件版本目录 {@code lib/*.jar} 中解析第一份含 {@code plugin.json} 的描述符（与加载逻辑一致）。
 */
public final class PluginDescriptorProbe {

    public record ProbeResult(PluginDescriptor descriptor, Path descriptorJar) {}

    private PluginDescriptorProbe() {
    }

    public static Optional<ProbeResult> tryRead(Path versionDir) throws Exception {
        Path libDir = versionDir.resolve("lib");
        if (!Files.isDirectory(libDir)) {
            return Optional.empty();
        }
        try (Stream<Path> st = Files.list(libDir)) {
            for (Path jar :
                    st.filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar"))
                            .sorted()
                            .toList()) {
                try {
                    PluginDescriptor d = PluginDescriptorReader.readFromJar(jar);
                    return Optional.of(new ProbeResult(d, jar));
                } catch (PluginException ignored) {
                    // 尝试下一个 jar
                }
            }
        }
        return Optional.empty();
    }
}
