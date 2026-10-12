package com.sxpcwlkj.system.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.plugin.host.PluginHostController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 固定发行版目录的服务端安装入口。客户端只提交 pluginId，下载位置与摘要均来自随宿主发布的白名单。
 */
@Service
@RequiredArgsConstructor
public class OfficialPluginReleaseService {
    private static final String CATALOG_PATH = "plugin-releases/plugins-2026.10.12.json";
    private static final String API_ROOT = "https://gitee.com/api/v5/repos/MrShanDev/mms/releases/";
    private static final long MAX_JAR_BYTES = 300L * 1024 * 1024;
    private static final ObjectMapper JSON = new ObjectMapper();

    private final PluginHostController pluginHostController;
    private Map<String, ReleasePlugin> catalog = Map.of();
    private String releaseUrl;
    private long releaseId;
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build();

    @PostConstruct
    void loadCatalog() throws IOException {
        try (InputStream in = new ClassPathResource(CATALOG_PATH).getInputStream()) {
            JsonNode root = JSON.readTree(in);
            releaseId = root.path("releaseId").asLong();
            releaseUrl = root.path("releaseUrl").asText();
            if (releaseId <= 0 || !"https://gitee.com/MrShanDev/mms/releases/tag/plugins-2026.10.12".equals(releaseUrl)) {
                throw new IOException("官方发行版目录不合法");
            }
            Map<String, ReleasePlugin> entries = new LinkedHashMap<>();
            for (JsonNode row : root.path("plugins")) {
                String pluginId = row.path("pluginId").asText();
                String version = row.path("version").asText();
                String fileName = row.path("fileName").asText();
                String sha256 = row.path("sha256").asText();
                long size = row.path("size").asLong();
                java.util.ArrayList<ReleaseAsset> assets = new java.util.ArrayList<>();
                for (JsonNode a : row.path("assets")) {
                    ReleaseAsset asset = new ReleaseAsset(a.path("id").asLong(), a.path("name").asText(),
                            a.path("size").asLong(), a.path("sha256").asText());
                    if (asset.id() <= 0 || asset.size() <= 0 || !validSha(asset.sha256())) {
                        throw new IOException("插件附件目录不合法: " + pluginId);
                    }
                    assets.add(asset);
                }
                if (pluginId.isBlank() || version.isBlank() || !fileName.endsWith(".jar")
                        || size <= 0 || size > MAX_JAR_BYTES || !validSha(sha256) || assets.isEmpty()
                        || assets.stream().mapToLong(ReleaseAsset::size).sum() != size) {
                    throw new IOException("插件发行版目录不合法: " + pluginId);
                }
                if (entries.putIfAbsent(pluginId, new ReleasePlugin(pluginId, version, fileName,
                        size, sha256, List.copyOf(assets))) != null) {
                    throw new IOException("插件发行版目录重复: " + pluginId);
                }
            }
            catalog = Map.copyOf(entries);
        }
    }

    public String releaseVersion(String pluginId) {
        ReleasePlugin entry = catalog.get(pluginId);
        return entry == null ? null : entry.version();
    }

    public String releaseUrl(String pluginId) {
        return catalog.containsKey(pluginId) ? releaseUrl : null;
    }

    public synchronized R<Map<String, Object>> install(String pluginId) throws Exception {
        ReleasePlugin entry = catalog.get(pluginId);
        if (entry == null) {
            return R.fail("该插件没有预置的官方发行版");
        }
        Path jar = Files.createTempFile("mms-official-release-", ".jar");
        try {
            downloadVerified(entry, jar);
            return pluginHostController.install(new PathMultipartFile(jar, entry.fileName()), false);
        } finally {
            Files.deleteIfExists(jar);
        }
    }

    private void downloadVerified(ReleasePlugin plugin, Path target) throws Exception {
        MessageDigest whole = sha256();
        long total = 0;
        try (OutputStream out = Files.newOutputStream(target)) {
            for (ReleaseAsset asset : plugin.assets()) {
                URI endpoint = URI.create(API_ROOT + releaseId + "/attach_files/" + asset.id() + "/download");
                HttpRequest request = HttpRequest.newBuilder(endpoint)
                        .timeout(Duration.ofMinutes(3)).GET().build();
                HttpResponse<InputStream> response = http.send(request, HttpResponse.BodyHandlers.ofInputStream());
                if (response.statusCode() == 302) {
                    URI redirect = endpoint.resolve(response.headers().firstValue("Location")
                            .orElseThrow(() -> new IOException("发行版附件缺少下载地址")));
                    response.body().close();
                    if (!"https".equalsIgnoreCase(redirect.getScheme())
                            || !"foruda.gitee.com".equalsIgnoreCase(redirect.getHost())) {
                        throw new IOException("发行版附件跳转到了不受信任的地址");
                    }
                    response = http.send(HttpRequest.newBuilder(redirect).timeout(Duration.ofMinutes(3))
                            .GET().build(), HttpResponse.BodyHandlers.ofInputStream());
                }
                if (response.statusCode() != 200) {
                    response.body().close();
                    throw new IOException("发行版附件下载失败，HTTP " + response.statusCode());
                }
                MessageDigest segment = sha256();
                long read = 0;
                try (InputStream in = response.body()) {
                    byte[] buf = new byte[64 * 1024];
                    int n;
                    while ((n = in.read(buf)) != -1) {
                        read += n;
                        total += n;
                        if (read > asset.size() || total > plugin.size() || total > MAX_JAR_BYTES) {
                            throw new IOException("发行版附件超过预期大小");
                        }
                        segment.update(buf, 0, n);
                        whole.update(buf, 0, n);
                        out.write(buf, 0, n);
                    }
                }
                if (read != asset.size() || !HexFormat.of().formatHex(segment.digest()).equalsIgnoreCase(asset.sha256())) {
                    throw new IOException("发行版附件校验失败: " + asset.name());
                }
            }
        }
        if (total != plugin.size() || !HexFormat.of().formatHex(whole.digest()).equalsIgnoreCase(plugin.sha256())) {
            throw new IOException("插件 JAR 校验失败");
        }
    }

    private static boolean validSha(String value) {
        return value != null && value.matches("[0-9a-fA-F]{64}");
    }

    private static MessageDigest sha256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private record ReleaseAsset(long id, String name, long size, String sha256) {}
    private record ReleasePlugin(String pluginId, String version, String fileName, long size,
                                 String sha256, List<ReleaseAsset> assets) {}

    private record PathMultipartFile(Path path, String originalFilename) implements MultipartFile {
        @Override public String getName() { return "file"; }
        @Override public String getOriginalFilename() { return originalFilename; }
        @Override public String getContentType() { return "application/java-archive"; }
        @Override public boolean isEmpty() { return size() == 0; }
        @Override public long getSize() { return size(); }
        @Override public byte[] getBytes() throws IOException { return Files.readAllBytes(path); }
        @Override public InputStream getInputStream() throws IOException { return Files.newInputStream(path); }
        @Override public void transferTo(File dest) throws IOException { Files.copy(path, dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING); }
        private long size() {
            try { return Files.size(path); } catch (IOException e) { throw new IllegalStateException(e); }
        }
    }
}
