package com.sxpcwlkj.plugin;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PluginInstallationLayoutTest {

    @Test
    void paths() {
        Path root = Path.of("/tmp/plugins");
        Path pr = PluginInstallationLayout.pluginRoot(root, "com.acme.a", "1.0.0");
        assertEquals(Path.of("/tmp/plugins/com.acme.a/1.0.0"), pr);
        assertEquals(pr.resolve("lib"), PluginInstallationLayout.libDirectory(root, "com.acme.a", "1.0.0"));
        assertEquals(pr.resolve("web"), PluginInstallationLayout.webDirectory(root, "com.acme.a", "1.0.0"));
    }

    @Test
    void safeSegment() {
        assertEquals("a_b", PluginInstallationLayout.safeSegment("a/b"));
    }
}
