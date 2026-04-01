package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortManagerAllocateTest {

    @Test
    void allocateRejectsInvertedRange() {
        assertThrows(PluginException.class, () -> PortManager.allocateFirstFreePort(30000, 20000));
    }

    @Test
    void allocatesSomePortInRange() throws Exception {
        int p = PortManager.allocateFirstFreePort(40100, 40199);
        assertTrue(p >= 40100 && p <= 40199);
    }
}
