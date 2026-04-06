package com.sxpcwlkj.plugin.host;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.regex.Pattern;

/**
 * 读取与 {@code logback-spring.xml} 中 {@code plugin_sift} 一致的 {@code logs/plugins/{pluginKey}.log}。
 */
public final class PluginLogFileSupport {

    private static final Pattern SAFE_ID_VER = Pattern.compile("^[a-zA-Z0-9._-]+$");

    private PluginLogFileSupport() {}

    public static Path defaultPluginsLogDirectory() {
        return Path.of(System.getProperty("user.dir", "."), "logs", "plugins").toAbsolutePath().normalize();
    }

    public static void validateSegments(String pluginId, String version) {
        if (pluginId == null || pluginId.isBlank() || version == null || version.isBlank()) {
            throw new IllegalArgumentException("pluginId 与 version 不能为空");
        }
        if (!SAFE_ID_VER.matcher(pluginId).matches() || !SAFE_ID_VER.matcher(version).matches()) {
            throw new IllegalArgumentException("pluginId 或 version 含有非法字符");
        }
    }

    public static String buildPluginKey(String pluginId, String version) {
        validateSegments(pluginId, version);
        return pluginId.trim() + "@" + version.trim();
    }

    /**
     * @param pluginsDir 已通过调用方 canonical 的 plugins 根目录
     * @param pluginKey  形如 {@code id@version}
     */
    public static Path resolveLogFile(Path pluginsDir, String pluginKey) throws IOException {
        if (pluginKey == null || !pluginKey.contains("@")) {
            throw new IllegalArgumentException("pluginKey 须为 pluginId@version");
        }
        String[] parts = pluginKey.split("@", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("pluginKey 格式错误");
        }
        validateSegments(parts[0], parts[1]);
        Path base = pluginsDir.toAbsolutePath().normalize();
        Path f = base.resolve(pluginKey + ".log").normalize();
        if (!f.startsWith(base)) {
            throw new IllegalArgumentException("日志路径越界");
        }
        return f;
    }

    /**
     * 读取文件末尾至多 {@code maxBytes} 字节，按 UTF-8 解码；若截断则尽量从下一整行开始展示。
     */
    public static TailResult readTailUtf8(Path file, int maxBytes) throws IOException {
        if (maxBytes <= 0) {
            return new TailResult("", false);
        }
        if (!Files.isRegularFile(file)) {
            return new TailResult("", false);
        }
        long len = Files.size(file);
        if (len <= maxBytes) {
            return new TailResult(Files.readString(file, StandardCharsets.UTF_8), false);
        }
        ByteBuffer bb = ByteBuffer.allocate(maxBytes);
        try (var ch = Files.newByteChannel(file)) {
            ch.position(len - maxBytes);
            while (bb.hasRemaining()) {
                int n = ch.read(bb);
                if (n <= 0) {
                    break;
                }
            }
        }
        bb.flip();
        byte[] buf = new byte[bb.remaining()];
        bb.get(buf);
        int start = 0;
        while (start < buf.length && buf[start] != '\n') {
            start++;
        }
        if (start < buf.length) {
            start++;
        }
        String text = new String(buf, start, buf.length - start, StandardCharsets.UTF_8);
        return new TailResult(text, true);
    }

    /**
     * 将插件日志截断为 0 字节（logback 仍会向同一文件继续追加）。
     * 若父目录存在但文件不存在，则创建空文件。
     */
    public static void truncateLogFile(Path file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("file 不能为空");
        }
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(file)) {
            Files.createFile(file);
            return;
        }
        if (!Files.isRegularFile(file)) {
            throw new IOException("路径不是常规文件: " + file);
        }
        try (FileChannel ch = FileChannel.open(file, StandardOpenOption.WRITE)) {
            ch.truncate(0);
        }
    }

    public record TailResult(String text, boolean truncated) {}
}
