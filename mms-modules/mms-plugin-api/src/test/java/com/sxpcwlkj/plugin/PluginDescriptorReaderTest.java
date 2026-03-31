package com.sxpcwlkj.plugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginDescriptorReaderTest {

    @Test
    void readFixtureFromClasspath() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/fixtures/sample-plugin.json")) {
            PluginDescriptor d = PluginDescriptorReader.readStream(in);
            assertEquals("com.acme.sample", d.getId());
            assertEquals("1.0.0", d.getVersion());
            assertEquals(PluginKind.EXTENSION, d.getKind());
            assertEquals("com.acme.sample.SamplePlugin", d.getEntryClass());
            assertEquals(21, d.getRequiresMms().getRevisionMin());
        }
    }

    @Test
    void readExampleFromModuleResources() throws Exception {
        try (InputStream in = PluginDescriptorReader.class.getResourceAsStream("/META-INF/mms/plugin.example.json")) {
            assertTrue(in != null);
            PluginDescriptor d = PluginDescriptorReader.readStream(in);
            PluginDescriptorValidator.validateStructureOrThrow(d);
        }
    }

    @Test
    void readFromJarUsesMetaInfPath(@TempDir Path dir) throws Exception {
        byte[] json;
        try (InputStream in = getClass().getResourceAsStream("/fixtures/sample-plugin.json")) {
            assertTrue(in != null);
            json = in.readAllBytes();
        }
        Path jar = dir.resolve("fake-plugin.jar");
        try (OutputStream os = Files.newOutputStream(jar);
             JarOutputStream jos = new JarOutputStream(os)) {
            JarEntry je = new JarEntry(PluginConstants.DESCRIPTOR_PATH_IN_JAR);
            jos.putNextEntry(je);
            jos.write(json);
            jos.closeEntry();
        }
        PluginDescriptor d = PluginDescriptorReader.readFromJar(jar);
        assertEquals("com.acme.sample", d.getId());
    }

}
