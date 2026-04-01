package com.sxpcwlkj.plugin;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginDescriptorValidatorTest {

    @Test
    void structureValid() {
        PluginDescriptor d = minimalDescriptor("com.acme.x", "1.0.0", 21, 21);
        assertTrue(PluginDescriptorValidator.validateStructure(d).isEmpty());
    }

    @Test
    void structureInvalidId() {
        PluginDescriptor d = minimalDescriptor("no-segments", "1.0.0", 21, 21);
        List<String> e = PluginDescriptorValidator.validateStructure(d);
        assertFalse(e.isEmpty());
    }

    @Test
    void hostRevisionRange() {
        PluginDescriptor d = minimalDescriptor("com.acme.x", "1.0.0", 20, 25);
        assertTrue(PluginDescriptorValidator.satisfiesHost(d, 21, "3.5.8"));
        assertFalse(PluginDescriptorValidator.satisfiesHost(d, 19, "3.5.8"));
        assertFalse(PluginDescriptorValidator.satisfiesHost(d, 26, "3.5.8"));
    }

    @Test
    void springBootHint() {
        PluginDescriptor d = minimalDescriptor("com.acme.x", "1.0.0", 21, 21);
        d.getRequiresMms().setSpringBoot("3.5.8");
        assertTrue(PluginDescriptorValidator.satisfiesHost(d, 21, "3.5.8"));
        assertFalse(PluginDescriptorValidator.satisfiesHost(d, 21, "3.4.0"));
    }

    @Test
    void librarySkipsEntryClass() {
        PluginDescriptor d = minimalDescriptor("com.acme.lib", "1.0.0", 21, 21);
        d.setKind(PluginKind.LIBRARY);
        d.setEntryClass(null);
        assertTrue(PluginDescriptorValidator.validateStructure(d).isEmpty());
    }

    @Test
    void spiOnlyMustNotDeclarePort() {
        PluginDescriptor d = minimalDescriptor("com.acme.x", "1.0.0", 21, 21);
        d.setRuntimeMode(PluginRuntimeMode.SPI_ONLY);
        d.setIndependentPort(8082);
        assertFalse(PluginDescriptorValidator.validateStructure(d).isEmpty());
    }

    @Test
    void independentProcessRequiresPortAndMainClass() {
        PluginDescriptor d = minimalDescriptor("com.acme.x", "1.0.0", 21, 21);
        d.setRuntimeMode(PluginRuntimeMode.INDEPENDENT_PROCESS);
        assertFalse(PluginDescriptorValidator.validateStructure(d).isEmpty());
        d.setIndependentPort(8082);
        d.setMainClass("com.acme.Boot");
        assertTrue(PluginDescriptorValidator.validateStructure(d).isEmpty());
    }

    @Test
    void hostServicesContractRejectsLowHost() {
        PluginDescriptor d = minimalDescriptor("com.acme.x", "1.0.0", 21, 21);
        d.setHostServicesContractVersion(2);
        assertThrows(PluginException.class, () ->
                PluginDescriptorValidator.validateHostServicesContractOrThrow(d, 1));
        PluginDescriptorValidator.validateHostServicesContractOrThrow(d, 2);
    }

    private static PluginDescriptor minimalDescriptor(String id, String ver, int min, Integer max) {
        PluginDescriptor d = new PluginDescriptor();
        d.setId(id);
        d.setVersion(ver);
        RequiresMmsDescriptor r = new RequiresMmsDescriptor();
        r.setRevisionMin(min);
        r.setRevisionMax(max);
        d.setRequiresMms(r);
        d.setEntryClass("com.acme.X");
        d.setKind(PluginKind.EXTENSION);
        return d;
    }
}
