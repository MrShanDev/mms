package com.sxpcwlkj.plugin.host;

import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginCompatibilityCheckerTest {

    @Test
    void mismatchThrows(@TempDir Path tmp) throws Exception {
        Path jar = tmp.resolve("p.jar");
        byte[] manifestBytes = "a:g:v:1.0\nb:h:u:2.0\n".getBytes(StandardCharsets.UTF_8);
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jar))) {
            jos.putNextEntry(new JarEntry("META-INF/mms/deps-fingerprint.manifest"));
            jos.write(manifestBytes);
            jos.closeEntry();
        }
        PluginDescriptor d = new PluginDescriptor();
        d.setId("com.test.a");
        d.setDependencyFingerprintSha256("ab".repeat(32));
        assertThrows(PluginException.class, () -> PluginCompatibilityChecker.verifyDependencyFingerprintOrThrow(jar, d));
    }

    @Test
    void matchPasses(@TempDir Path tmp) throws Exception {
        Path jar = tmp.resolve("ok.jar");
        byte[] manifestBytes = "a:g:v:1.0\n".getBytes(StandardCharsets.UTF_8);
        String hex = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(manifestBytes));
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jar))) {
            jos.putNextEntry(new JarEntry("META-INF/mms/deps-fingerprint.manifest"));
            jos.write(manifestBytes);
            jos.closeEntry();
        }
        PluginDescriptor d = new PluginDescriptor();
        d.setId("com.test.b");
        d.setDependencyFingerprintSha256(hex);
        PluginCompatibilityChecker.verifyDependencyFingerprintOrThrow(jar, d);
        try (ZipFile zf = new ZipFile(jar.toFile())) {
            ZipEntry e = zf.getEntry("META-INF/mms/deps-fingerprint.manifest");
            assertTrue(e != null);
            try (InputStream in = zf.getInputStream(e)) {
                assertTrue(new String(in.readAllBytes(), StandardCharsets.UTF_8).contains("a:g:v"));
            }
        }
    }
}
