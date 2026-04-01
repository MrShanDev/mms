package com.sxpcwlkj.plugin.host;

import com.sxpcwlkj.plugin.PluginConstants;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 插件兼容性集中校验（依赖指纹等）。{@code requiresMms} 等仍由 {@link com.sxpcwlkj.plugin.PluginDescriptorValidator} 处理。
 * <p>指纹清单格式见 {@link PluginConstants#DEPS_FINGERPRINT_MANIFEST}（GAV 行 + 排序 + 原始字节 SHA-256）。构建侧建议由 CI 校验
 * 清单与锁定的依赖图一致，避免手工改版本号漏更新摘要。</p>
 */
public final class PluginCompatibilityChecker {

    private PluginCompatibilityChecker() {
    }

    /**
     * 若描述符声明了 {@link PluginDescriptor#getDependencyFingerprintSha256()}，
     * 则 JAR 内必须存在 {@link PluginConstants#DEPS_FINGERPRINT_MANIFEST}，且其字节内容的 SHA-256（小写十六进制）与声明一致。
     */
    public static void verifyDependencyFingerprintOrThrow(Path pluginJar, PluginDescriptor descriptor) {
        String declared = descriptor.getDependencyFingerprintSha256();
        if (declared == null || declared.isBlank()) {
            return;
        }
        if (!Files.isRegularFile(pluginJar)) {
            throw new PluginException("指纹校验：无法读取插件 JAR: " + pluginJar);
        }
        try (ZipFile zip = new ZipFile(pluginJar.toFile())) {
            ZipEntry entry = zip.getEntry(PluginConstants.DEPS_FINGERPRINT_MANIFEST);
            if (entry == null) {
                throw new PluginException(
                        "插件 " + descriptor.getId() + " 声明了 dependencyFingerprintSha256 但 JAR 内缺少 "
                                + PluginConstants.DEPS_FINGERPRINT_MANIFEST);
            }
            byte[] raw;
            try (InputStream in = zip.getInputStream(entry)) {
                raw = in.readAllBytes();
            }
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw);
            String actual = HexFormat.of().formatHex(digest);
            if (!actual.equalsIgnoreCase(declared.trim())) {
                throw new PluginException(
                        "插件依赖指纹不匹配: 期望 " + declared.trim().toLowerCase() + " 实际 " + actual);
            }
        } catch (PluginException e) {
            throw e;
        } catch (Exception e) {
            throw new PluginException("指纹校验失败: " + e.getMessage(), e);
        }
    }
}
