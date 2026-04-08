package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginConstants;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * 从插件 JAR 解压 {@link PluginConstants#WEB_BUNDLE_PREFIX_IN_JAR} 到安装目录 {@code web/}。
 */
public final class PluginWebBundleExtractor {

    private PluginWebBundleExtractor() {
    }

    /**
     * 将 JAR 内联邦前端文件同步到 {@code webRoot}：先清空已有 {@code webRoot} 再写入（与当前 JAR 一致）。
     */
    public static void extractFromJar(Path jarFile, Path webRoot) throws IOException {
        deleteRecursiveIfExists(webRoot);
        Files.createDirectories(webRoot);
        try (JarFile jf = new JarFile(jarFile.toFile(), false)) {
            Enumeration<JarEntry> en = jf.entries();
            while (en.hasMoreElements()) {
                JarEntry e = en.nextElement();
                String name = e.getName();
                if (!name.startsWith(PluginConstants.WEB_BUNDLE_PREFIX_IN_JAR)
                        || name.length() <= PluginConstants.WEB_BUNDLE_PREFIX_IN_JAR.length()) {
                    continue;
                }
                String rel = name.substring(PluginConstants.WEB_BUNDLE_PREFIX_IN_JAR.length());
                if (rel.isBlank()) {
                    continue;
                }
                Path out = webRoot.resolve(rel).normalize();
                if (!out.startsWith(webRoot.normalize())) {
                    throw new IOException("非法 JAR 路径: " + name);
                }
                if (e.isDirectory()) {
                    Files.createDirectories(out);
                } else {
                    if (out.getParent() != null) {
                        Files.createDirectories(out.getParent());
                    }
                    try (InputStream is = jf.getInputStream(e)) {
                        Files.copy(is, out);
                    }
                }
            }
        }
    }

    private static void deleteRecursiveIfExists(Path root) throws IOException {
        if (root == null || !Files.exists(root)) {
            return;
        }
        try (Stream<Path> walk = Files.walk(root)) {
            for (Path p : walk.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(p);
            }
        }
    }
}
