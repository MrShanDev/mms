package com.sxpcwlkj.datasource.config;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MybatisPlusConfigNormalizeTest {

    @Test
    void dedupesAndLowercases() {
        List<String> raw = Arrays.asList("SYS_User", " sys_user ", "Sys_User");
        Set<String> out = MybatisPlusConfig.normalizeTenantExclusions(raw);
        assertEquals(1, out.size());
        assertTrue(out.contains("sys_user"));
    }
}
