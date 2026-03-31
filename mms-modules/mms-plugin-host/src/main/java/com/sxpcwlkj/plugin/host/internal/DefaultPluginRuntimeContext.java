package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginConstants;
import com.sxpcwlkj.plugin.PluginRuntimeContext;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor
public final class DefaultPluginRuntimeContext implements PluginRuntimeContext {

    private final int hostMmsRevisionVal;
    private final String springBootVersion;
    private final Path installRoot;

    @Override
    public int hostMmsRevision() {
        return hostMmsRevisionVal;
    }

    @Override
    public String hostSpringBootVersion() {
        return springBootVersion;
    }

    @Override
    public Path pluginInstallRoot() {
        return installRoot;
    }

    @Override
    public Path pluginDataDirectory() {
        return installRoot.resolve(PluginConstants.SUBDIR_DATA);
    }

    @Override
    public Path pluginTemporaryDirectory() {
        return installRoot.resolve(PluginConstants.SUBDIR_TMP);
    }
}
