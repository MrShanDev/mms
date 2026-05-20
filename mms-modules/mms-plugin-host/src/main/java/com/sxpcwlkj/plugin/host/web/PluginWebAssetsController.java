package com.sxpcwlkj.plugin.host.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.sxpcwlkj.plugin.PluginInstallationLayout;
import com.sxpcwlkj.plugin.host.DiskPluginSlot;
import com.sxpcwlkj.plugin.host.PluginHostProperties;
import com.sxpcwlkj.plugin.host.PluginLifecycleManager;
import com.sxpcwlkj.plugin.host.PluginManifestView;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 插件联邦前端静态资源：与 mms-ui {@code /plugin-assets/{pluginId}/{version}/...} 及 JAR 内 {@code META-INF/mms/web/} 解压目录一致。
 */
@RestController
@RequiredArgsConstructor
public class PluginWebAssetsController {

    private static final String CURRENT_ALIAS = "current";

    private final PluginHostProperties pluginHostProperties;
    private final PluginLifecycleManager pluginLifecycleManager;

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
        String resolvedVersion = resolvePluginVersion(pluginId, pluginVersion, resourcePath, root);
        if (resolvedVersion == null || resolvedVersion.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        String idSeg = PluginInstallationLayout.safeSegment(pluginId);
        String verSeg = PluginInstallationLayout.safeSegment(resolvedVersion);
        Path base = PluginInstallationLayout.webDirectory(root, idSeg, verSeg).normalize();
        Path rel = normalizeRelativePath(resourcePath);
        if (rel == null) {
            return ResponseEntity.notFound().build();
        }
        Path file = resolveExistingFile(base, rel);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource body = new FileSystemResource(file);
        MediaType mediaType = probeMediaType(file);
        CacheControl cc = resolveCacheControl(rel);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(cc)
                .header("X-Content-Type-Options", "nosniff")
                .body(body);
    }

    private String resolvePluginVersion(String pluginId, String pluginVersion, String resourcePath, Path root) {
        if (pluginVersion == null || pluginVersion.isBlank()) {
            return null;
        }
        String requested = pluginVersion.trim();
        if (!CURRENT_ALIAS.equalsIgnoreCase(requested)) {
            return requested;
        }
        String pid = pluginId == null ? "" : pluginId.trim();
        if (pid.isBlank()) {
            return null;
        }

        Set<String> candidateVersions = new LinkedHashSet<>();
        for (PluginManifestView m : pluginLifecycleManager.listManifests()) {
            if (pid.equals(m.id()) && m.version() != null && !m.version().isBlank()) {
                candidateVersions.add(m.version().trim());
            }
        }
        pluginLifecycleManager.listDiskSlots().stream()
                .filter(s -> pid.equals(s.pluginId()) && s.libHasJars())
                .sorted(Comparator.comparing(DiskPluginSlot::version, String.CASE_INSENSITIVE_ORDER).reversed())
                .map(DiskPluginSlot::version)
                .filter(v -> v != null && !v.isBlank())
                .map(String::trim)
                .forEach(candidateVersions::add);

        String idSeg = PluginInstallationLayout.safeSegment(pid);
        Path rel = normalizeRelativePath(resourcePath);
        if (rel == null) {
            return null;
        }

        List<String> ordered = new ArrayList<>(candidateVersions);
        for (String version : ordered) {
            Path base = PluginInstallationLayout.webDirectory(root, idSeg, PluginInstallationLayout.safeSegment(version)).normalize();
            Path file = base.resolve(rel).normalize();
            if (file.startsWith(base) && Files.isRegularFile(file)) {
                return version;
            }
        }
        return ordered.isEmpty() ? null : ordered.get(0);
    }

    private static Path normalizeRelativePath(String resourcePath) {
        if (resourcePath == null) {
            return null;
        }
        String normalized = resourcePath.replace("\\", "/").trim();
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        if (normalized.isBlank()) {
            return null;
        }
        Path rel = Path.of(normalized);
        return rel.isAbsolute() ? null : rel;
    }

    private static Path resolveExistingFile(Path base, Path rel) {
        Path direct = base.resolve(rel).normalize();
        if (direct.startsWith(base) && Files.isRegularFile(direct)) {
            return direct;
        }

        String relText = rel.toString().replace("\\", "/");
        String doubledAssetsPrefix = "assets/assets/";
        if (relText.startsWith(doubledAssetsPrefix)) {
            String fallbackText = "assets/" + relText.substring(doubledAssetsPrefix.length());
            Path fallbackRel = Path.of(fallbackText);
            Path fallback = base.resolve(fallbackRel).normalize();
            if (fallback.startsWith(base) && Files.isRegularFile(fallback)) {
                return fallback;
            }
        }
        return null;
    }

    private static CacheControl resolveCacheControl(Path rel) {
        String relText = rel.toString().replace("\\", "/").toLowerCase();
        if (relText.endsWith("/remoteentry.js") || relText.equals("remoteentry.js")) {
            return CacheControl.noCache().mustRevalidate();
        }
        return CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic();
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
        Path userDir = Path.of(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        Path direct = userDir.resolve("plugins").normalize();

        Path parent = userDir.getParent();
        Path sibling = parent == null ? null : parent.resolve("plugins").normalize();

        String rd = pluginHostProperties.getRootDir();
        if (rd != null && !rd.isBlank()) {
            Path configured = Path.of(rd.trim()).toAbsolutePath().normalize();
            if (Files.isDirectory(configured)) {
                return configured;
            }
            if (Files.isDirectory(direct)) {
                return direct;
            }
            if (sibling != null && Files.isDirectory(sibling)) {
                return sibling;
            }
            return configured;
        }

        if (Files.isDirectory(direct)) {
            return direct;
        }
        if (sibling != null && Files.isDirectory(sibling)) {
            return sibling;
        }
        return direct;
    }
}
