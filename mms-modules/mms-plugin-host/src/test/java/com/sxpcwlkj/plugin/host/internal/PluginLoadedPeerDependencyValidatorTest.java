package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginDependencyDescriptor;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginLoadedPeerDependencyValidatorTest {

    @Test
    void passesWhenNoDependencies() {
        PluginDescriptor d = new PluginDescriptor();
        d.setId("com.example.a");
        d.setVersion("1.0.0");
        assertDoesNotThrow(() -> PluginLoadedPeerDependencyValidator.validateOrThrow(d, new ConcurrentHashMap<>()));
    }

    @Test
    void failsWhenRequiredPeerNotLoaded() {
        PluginDescriptor d = new PluginDescriptor();
        d.setId("com.example.child");
        d.setVersion("1.0.0");
        PluginDependencyDescriptor dep = new PluginDependencyDescriptor();
        dep.setId("com.example.parent");
        dep.setOptional(false);
        d.setDependencies(List.of(dep));
        PluginException ex =
                assertThrows(PluginException.class, () -> PluginLoadedPeerDependencyValidator.validateOrThrow(d, new ConcurrentHashMap<>()));
        assertTrue(ex.getMessage().contains("com.example.parent"));
        assertTrue(ex.getMessage().contains("需要先成功加载"));
    }

    @Test
    void passesWhenPeerLoadedAndVersionInRange() {
        PluginDescriptor parentDesc = new PluginDescriptor();
        parentDesc.setId("com.example.parent");
        parentDesc.setVersion("1.2.0");
        LoadedPluginInstance parent =
                new LoadedPluginInstance(parentDesc, null, List.of(), List.of(), List.of(), () -> {});

        PluginDescriptor child = new PluginDescriptor();
        child.setId("com.example.child");
        child.setVersion("1.0.0");
        PluginDependencyDescriptor dep = new PluginDependencyDescriptor();
        dep.setId("com.example.parent");
        dep.setVersionRange("[1.0.0,2.0.0)");
        dep.setOptional(false);
        child.setDependencies(List.of(dep));

        ConcurrentHashMap<String, LoadedPluginInstance> loaded = new ConcurrentHashMap<>();
        loaded.put("com.example.parent@1.2.0", parent);
        assertDoesNotThrow(() -> PluginLoadedPeerDependencyValidator.validateOrThrow(child, loaded));
    }

    @Test
    void skipsOptionalDependency() {
        PluginDescriptor d = new PluginDescriptor();
        d.setId("com.example.child");
        d.setVersion("1.0.0");
        PluginDependencyDescriptor dep = new PluginDependencyDescriptor();
        dep.setId("com.example.missing");
        dep.setOptional(true);
        d.setDependencies(List.of(dep));
        assertDoesNotThrow(() -> PluginLoadedPeerDependencyValidator.validateOrThrow(d, new ConcurrentHashMap<>()));
    }
}
