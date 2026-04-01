package com.sxpcwlkj.plugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginVersionConstraintTest {

    @Test
    void blankRangeAccepts() {
        assertTrue(PluginVersionConstraint.satisfiedBy(null, "1.0.0"));
        assertTrue(PluginVersionConstraint.satisfiedBy("", "1.0.0"));
    }

    @Test
    void exactAndStar() {
        assertTrue(PluginVersionConstraint.satisfiedBy("1.0.0", "1.0.0"));
        assertFalse(PluginVersionConstraint.satisfiedBy("1.0.1", "1.0.0"));
        assertTrue(PluginVersionConstraint.satisfiedBy("*", "9.9.9"));
    }

    @Test
    void prefixPlus() {
        assertTrue(PluginVersionConstraint.satisfiedBy("1.0.+", "1.0.1"));
        assertTrue(PluginVersionConstraint.satisfiedBy("1.0.+", "1.0.0"));
        assertFalse(PluginVersionConstraint.satisfiedBy("1.0.+", "1.1.0"));
    }

    @Test
    void mavenIntervalInclusive() {
        assertTrue(PluginVersionConstraint.satisfiedBy("[1.0.0,2.0.0]", "1.0.0"));
        assertTrue(PluginVersionConstraint.satisfiedBy("[1.0.0,2.0.0]", "2.0.0"));
        assertFalse(PluginVersionConstraint.satisfiedBy("[1.0.0,2.0.0]", "0.9.0"));
        assertFalse(PluginVersionConstraint.satisfiedBy("[1.0.0,2.0.0]", "2.0.1"));
    }

    @Test
    void mavenIntervalExclusiveBound() {
        assertFalse(PluginVersionConstraint.satisfiedBy("(1.0.0,2.0.0)", "1.0.0"));
        assertTrue(PluginVersionConstraint.satisfiedBy("(1.0.0,2.0.0)", "1.0.1"));
        assertFalse(PluginVersionConstraint.satisfiedBy("(1.0.0,2.0.0)", "2.0.0"));
    }

    @Test
    void mavenOpenEnded() {
        assertTrue(PluginVersionConstraint.satisfiedBy("[1.0.0,)", "99.0.0"));
        assertFalse(PluginVersionConstraint.satisfiedBy("[1.0.0,)", "0.9.0"));
        assertTrue(PluginVersionConstraint.satisfiedBy("(,2.0.0]", "1.5.0"));
        assertTrue(PluginVersionConstraint.satisfiedBy("(,2.0.0]", "2.0.0"));
        assertFalse(PluginVersionConstraint.satisfiedBy("(,2.0.0]", "2.0.1"));
    }
}
