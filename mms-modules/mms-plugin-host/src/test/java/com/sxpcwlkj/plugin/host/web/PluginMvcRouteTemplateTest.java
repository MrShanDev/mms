package com.sxpcwlkj.plugin.host.web;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginMvcRouteTemplateTest {

    @Test
    void matchLiterals() {
        var t = PluginMvcRouteTemplate.parse("demo/ping");
        var m = t.match(List.of("demo", "ping"));
        assertTrue(m.isPresent());
    }

    @Test
    void matchPathVars() {
        var t = PluginMvcRouteTemplate.parse("demo/echo/{msg}");
        var m = t.match(List.of("demo", "echo", "Hi"));
        assertTrue(m.isPresent());
        assertEquals(Map.of("msg", "Hi"), m.get());
        assertEquals(List.of("msg"), t.variableNamesInOrder());
    }

    @Test
    void caseInsensitiveLiterals() {
        var t = PluginMvcRouteTemplate.parse("Demo/Ping");
        assertTrue(t.match(List.of("demo", "ping")).isPresent());
    }
}
