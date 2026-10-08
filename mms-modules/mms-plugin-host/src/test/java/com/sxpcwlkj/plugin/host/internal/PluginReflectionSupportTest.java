package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.MmsPlugin;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PluginReflectionSupportTest {

    public static class P implements MmsPlugin {
        public String hello(String name) {
            return "hi " + name;
        }
    }

    public static class Overloaded implements MmsPlugin {
        public String call(String value) { return value; }
        public String call(Integer value) { return value.toString(); }
    }
    @Test void rejectsAmbiguousOverloads() {
        assertThrows(IllegalArgumentException.class, () -> PluginReflectionSupport.findPublicMethod(Overloaded.class, "call", 1));
    }

    @Test
    void findsPublicInstanceMethodByArity() {
        Method m = PluginReflectionSupport.findPublicMethod(P.class, "hello", 1);
        assertNotNull(m);
        assertNull(PluginReflectionSupport.findPublicMethod(P.class, "hello", 0));
        assertNull(PluginReflectionSupport.findPublicMethod(P.class, "missing", 1));
    }
}
