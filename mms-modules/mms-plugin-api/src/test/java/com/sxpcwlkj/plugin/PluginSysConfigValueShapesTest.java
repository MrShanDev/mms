package com.sxpcwlkj.plugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PluginSysConfigValueShapesTest {

    @Test
    void normalizeDeclaredCardinality() {
        assertEquals(PluginSysConfigValueShapes.SCALAR, PluginSysConfigValueShapes.normalizeDeclaredCardinality(null));
        assertEquals(PluginSysConfigValueShapes.SCALAR, PluginSysConfigValueShapes.normalizeDeclaredCardinality(" "));
        assertEquals(PluginSysConfigValueShapes.LIST, PluginSysConfigValueShapes.normalizeDeclaredCardinality("list"));
        assertEquals(PluginSysConfigValueShapes.LIST, PluginSysConfigValueShapes.normalizeDeclaredCardinality("array"));
        assertEquals(PluginSysConfigValueShapes.OBJECT, PluginSysConfigValueShapes.normalizeDeclaredCardinality("object"));
        assertThrows(PluginException.class, () -> PluginSysConfigValueShapes.normalizeDeclaredCardinality("map"));
    }
}
