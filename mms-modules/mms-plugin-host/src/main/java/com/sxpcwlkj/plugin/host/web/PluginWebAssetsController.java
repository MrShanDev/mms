package com.sxpcwlkj.plugin.host.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.sxpcwlkj.plugin.PluginInstallationLayout;
import com.sxpcwlkj.plugin.host.PluginHostProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * 插件联邦前端静态资源：与 mms-ui {@code /plugin-assets/{pluginId}/{version}/...} 及 JAR 内 {@code META-INF/mms/web/} 解压目录一致。
 */
@RestController
@RequiredArgsConstructor
public class PluginWebAssetsController {

    private final PluginHostProperties pluginHostProperties;

    /**
     * @param resourcePath Spring 6 {@code {*}} 捕获剩余路径段，如 {@code assets/remoteEntry.js}
     */
    @SaCheckLogin
    @GetMapping("/plugin-assets/{pluginId}/{pluginVersion}/{*resourcePath}")
    public ResponseEntity<Resource> serve(
            @PathVariable String pluginId,
            @PathVariable String pluginVersion,
            @PathVariable String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        Path root = resolvePluginsRoot();
        String idSeg = PluginInstallationLayout.safeSegment(pluginId);
        String verSeg = PluginInstallationLayout.safeSegment(pluginVersion);
        Path base = PluginInstallationLayout.webDirectory(root, idSeg, verSeg).normalize();
        Path rel = Path.of(resourcePath.replace("\\", "/"));
        if (rel.isAbsolute()) {
            return ResponseEntity.notFound().build();
        }
        Path file = base.resolve(rel).normalize();
        if (!file.startsWith(base) || !Files.isRegularFile(file)) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource body = new FileSystemResource(file);
        MediaType mediaType = probeMediaType(file);
        CacheControl cc = CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic();
        return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(cc)
                .header("X-Content-Type-Options", "nosniff")
                .body(body);
    }

    private static MediaType probeMediaType(Path file) {
        try {
            String probed = Files.probeContentType(file);
            if (probed != null && !probed.isBlank()) {
                return MediaType.parseMediaType(probed);
            }
        } catch (Exception ignored) {
            // fall through
        }
        String name = file.getFileName().toString().toLowerCase();
        if (name.endsWith(".js")) {
            return new MediaType("text", "javascript", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (name.endsWith(".mjs")) {
            return new MediaType("text", "javascript", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (name.endsWith(".css")) {
            return new MediaType("text", "css", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (name.endsWith(".json")) {
            return MediaType.APPLICATION_JSON;
        }
        if (name.endsWith(".html")) {
            return MediaType.TEXT_HTML;
        }
        if (name.endsWith(".svg")) {
            return MediaType.valueOf("image/svg+xml");
        }
        if (name.endsWith(".woff2")) {
            return new MediaType("font", "woff2");
        }
        if (name.endsWith(".woff")) {
            return new MediaType("font", "woff");
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    private Path resolvePluginsRoot() {
        String rd = pluginHostProperties.getRootDir();
        if (rd != null && !rd.isBlank()) {
            return Path.of(rd.trim()).toAbsolutePath().normalize();
        }
        return Path.of(System.getProperty("user.dir", "."), "mms-plugins").toAbsolutePath().normalize();
    }
}
