package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.MmsPlugin;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PluginReflectionSupportTest {

    public static class P implements MmsPlugin {
        public String hello(String name) {
            return "hi " + name;
        }
    }

    @Test
    void findsPublicInstanceMethodByArity() {
        Method m = PluginReflectionSupport.findPublicMethod(P.class, "hello", 1);
        assertNotNull(m);
        assertNull(PluginReflectionSupport.findPublicMethod(P.class, "hello", 0));
        assertNull(PluginReflectionSupport.findPublicMethod(P.class, "missing", 1));
    }
}
