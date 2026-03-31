package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 从 JSON 文本、普通文件或 JAR 中读取 {@link PluginDescriptor}。
 */
public final class PluginDescriptorReader {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private PluginDescriptorReader() {
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }

    public static PluginDescriptor readJson(String json) throws IOException {
        return normalize(MAPPER.readValue(json, PluginDescriptor.class));
    }

    public static PluginDescriptor readStream(InputStream in) throws IOException {
        return normalize(MAPPER.readValue(in, PluginDescriptor.class));
    }

    public static PluginDescriptor readPath(Path jsonFile) throws IOException {
        try (InputStream in = Files.newInputStream(jsonFile)) {
            return readStream(in);
        }
    }

    /**
     * 自插件 JAR 解析描述：优先 {@link PluginConstants#DESCRIPTOR_PATH_IN_JAR}，否则根目录
     * {@link PluginConstants#DESCRIPTOR_FALLBACK_PATH_IN_JAR}。
     */
    public static PluginDescriptor readFromJar(Path jarPath) {
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            JarEntry primary = jar.getJarEntry(PluginConstants.DESCRIPTOR_PATH_IN_JAR);
            JarEntry fallback = jar.getJarEntry(PluginConstants.DESCRIPTOR_FALLBACK_PATH_IN_JAR);
            if (primary == null && fallback == null) {
                throw new PluginException("JAR 中未找到 "
                        + PluginConstants.DESCRIPTOR_PATH_IN_JAR
                        + " 或 "
                        + PluginConstants.DESCRIPTOR_FALLBACK_PATH_IN_JAR
                        + " : " + jarPath);
            }
            JarEntry use = primary != null ? primary : fallback;
            try (InputStream in = jar.getInputStream(use)) {
                return readStream(in);
            }
        } catch (IOException e) {
            throw new PluginException("读取插件 JAR 描述失败: " + jarPath, e);
        }
    }

    private static PluginDescriptor normalize(PluginDescriptor d) {
        if (d.getKind() == null) {
            d.setKind(PluginKind.EXTENSION);
        }
        if (d.getDependencies() == null) {
            d.setDependencies(new java.util.ArrayList<>());
        }
        if (d.getFrontend() != null && d.getFrontend().getRoutePrefixes() == null) {
            d.getFrontend().setRoutePrefixes(new java.util.ArrayList<>());
        }
        return d;
    }
}
