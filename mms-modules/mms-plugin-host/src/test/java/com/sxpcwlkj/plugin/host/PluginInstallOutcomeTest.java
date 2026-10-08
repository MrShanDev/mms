package com.sxpcwlkj.plugin.host;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.plugin.*;
import com.sxpcwlkj.plugin.host.web.PluginMvcExecutionGuard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PluginInstallOutcomeTest {
    private final PluginLifecycleManager lifecycle = mock(PluginLifecycleManager.class);
    private final BundledPluginSchemaExecutor sql = mock(BundledPluginSchemaExecutor.class);
    private PluginHostController controller() {
        var beans = new DefaultListableBeanFactory(); beans.registerSingleton("sql", sql);
        return new PluginHostController(lifecycle, new PluginHostProperties(), beans.getBeanProvider(PluginHostDbBridge.class),
                new ObjectMapper(), beans.getBeanProvider(HostServices.class), beans.getBeanProvider(HostDataService.class),
                mock(PluginMvcExecutionGuard.class), beans.getBeanProvider(BundledPluginSchemaExecutor.class), beans.getBeanProvider(JdbcTemplate.class));
    }
    private String installCode(boolean skip) throws Exception {
        var method = Arrays.stream(PluginHostController.class.getDeclaredMethods()).filter(m -> m.getName().equals("runPluginInstall")).findFirst().orElseThrow();
        method.setAccessible(true);
        Object result = method.invoke(controller(), Path.of("audit.jar"), skip, null);
        var code = result.getClass().getDeclaredMethod("code"); code.setAccessible(true);
        return (String) code.invoke(result);
    }
    @Test void invalidPackageNeverExecutesSql() throws Exception {
        when(lifecycle.validateJarForInstall(any())).thenThrow(new PluginException("invalid"));
        assertEquals(PluginInstallStreamResultCode.JAR_VALIDATION_FAILED, installCode(false));
        verifyNoInteractions(sql);
        verify(lifecycle, never()).installJarFromUpload(any());
    }
    @Test void unloadedPackageIsNotReportedSuccessful() throws Exception {
        var descriptor = new PluginDescriptor(); descriptor.setId("audit.plugin"); descriptor.setVersion("1.0.0");
        when(lifecycle.validateJarForInstall(any())).thenReturn(descriptor);
        when(lifecycle.installJarFromUpload(any())).thenReturn(descriptor);
        when(lifecycle.listSummaries()).thenReturn(List.of());
        assertEquals(PluginInstallStreamResultCode.PLUGIN_LOAD_FAILED, installCode(true));
    }
}
