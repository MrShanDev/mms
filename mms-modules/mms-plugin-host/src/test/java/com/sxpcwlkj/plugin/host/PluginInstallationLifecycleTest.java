package com.sxpcwlkj.plugin.host;

import com.sxpcwlkj.plugin.*;
import com.sxpcwlkj.plugin.host.internal.PluginSpringBeanAttachment;
import com.sxpcwlkj.plugin.host.web.PluginMvcRegistrar;
import com.sxpcwlkj.plugin.host.web.PluginMvcExecutorRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.jar.*;
import javax.tools.ToolProvider;
import static org.junit.jupiter.api.Assertions.*;

class PluginInstallationLifecycleTest {
    @TempDir Path dir;
    private PluginLifecycleManager manager() {
        var props = new PluginHostProperties(); props.setEnabled(true); props.setHostMmsRevision(21); props.setRootDir(dir.resolve("plugins").toString());
        var factory = new DefaultListableBeanFactory();
        return new PluginLifecycleManager(props, factory.getBeanProvider(PluginHostDbBridge.class), factory.getBeanProvider(HostServices.class),
            factory.getBeanProvider(PluginMvcRegistrar.class), factory.getBeanProvider(PluginSpringBeanAttachment.class),
            factory.getBeanProvider(PluginMvcExecutorRegistry.class), factory.getBeanProvider(PluginLifecycleEventListener.class));
    }
    private Path jar(String version, boolean fail) throws Exception {
        Path classes = Files.createDirectories(dir.resolve("classes-" + version));
        Path source = classes.resolve("Entry.java");
        Files.writeString(source, "package audit; public class Entry implements com.sxpcwlkj.plugin.MmsPlugin {"
            + "public String version(){return \"" + version + "\";} public void onLoad(com.sxpcwlkj.plugin.PluginRuntimeContext ctx){"
            + "ctx.addUnloadHook(() -> System.setProperty(\"mms.audit.cleaned\", \"yes\"));"
            + (fail ? "throw new NoClassDefFoundError(\"audit dependency\");" : "") + "}}" );
        assertEquals(0, ToolProvider.getSystemJavaCompiler().run(null, null, null, "-classpath", System.getProperty("java.class.path"), "-d", classes.toString(), source.toString()));
        Path jar = dir.resolve(version + ".jar");
        try(var out = new JarOutputStream(Files.newOutputStream(jar))) {
            write(out, "audit/Entry.class", Files.readAllBytes(classes.resolve("audit/Entry.class")));
            write(out, "META-INF/services/com.sxpcwlkj.plugin.MmsPlugin", "audit.Entry".getBytes(StandardCharsets.UTF_8));
            String json = "{\"id\":\"audit.plugin\",\"version\":\"" + version + "\",\"name\":\"Audit\",\"description\":\"Audit\",\"kind\":\"extension\",\"entryClass\":\"audit.Entry\",\"runtimeMode\":\"SPI_ONLY\",\"requiresMms\":{\"revisionMin\":21}}";
            write(out, "META-INF/mms/plugin.json", json.getBytes(StandardCharsets.UTF_8));
        }
        return jar;
    }
    private void write(JarOutputStream out, String name, byte[] bytes) throws Exception {out.putNextEntry(new JarEntry(name));out.write(bytes);out.closeEntry();}
    @Test void installLoadUpgradeUnloadAndReinstall() throws Exception {
        var manager = manager(); Path first = jar("1.0.0", false);
        manager.installJarFromUpload(first); manager.reload();
        assertEquals(1, manager.listSummaries().size());
        assertThrows(PluginException.class, () -> manager.installJarFromUpload(first));
        manager.installJarFromUpload(jar("1.1.0", false)); manager.reload();
        assertTrue(manager.listSummaries().stream().anyMatch(s -> s.version().equals("1.1.0")));
        manager.unloadAllVersionsOfPlugin("audit.plugin"); manager.uninstallFromDisk("audit.plugin", null);
        assertTrue(manager.listSummaries().isEmpty()); assertEquals("yes", System.getProperty("mms.audit.cleaned"));
        manager.installJarFromUpload(first); manager.reload(); assertEquals(1, manager.listSummaries().size());
        manager.unloadAllVersionsOfPlugin("audit.plugin"); System.clearProperty("mms.audit.cleaned");
    }
    @Test void linkageFailureRunsUnloadHooks() throws Exception {
        var manager = manager(); manager.installJarFromUpload(jar("2.0.0", true));
        manager.reload(); assertTrue(manager.listSummaries().isEmpty());
        assertEquals("yes", System.getProperty("mms.audit.cleaned")); System.clearProperty("mms.audit.cleaned");
    }
}
