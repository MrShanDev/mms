package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.HostServices;
import com.sxpcwlkj.plugin.PluginBeanRegistrar;
import com.sxpcwlkj.plugin.PluginConstants;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginRuntimeContext;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public final class DefaultPluginRuntimeContext implements PluginRuntimeContext {

    private final int hostMmsRevisionVal;
    private final String springBootVersion;
    private final Path installRoot;
    private final PluginDescriptor descriptor;
    private final HostServices hostServices;
    private final PluginBeanRegistrar pluginBeanRegistrar;
    private final PluginPeerInvoker peerInvoker;
    private final CopyOnWriteArrayList<Runnable> unloadHooks = new CopyOnWriteArrayList<>();

    public DefaultPluginRuntimeContext(
            int hostMmsRevisionVal,
            String springBootVersion,
            Path installRoot,
            PluginDescriptor descriptor,
            HostServices hostServices,
            PluginBeanRegistrar pluginBeanRegistrar,
            PluginPeerInvoker peerInvoker) {
        this.hostMmsRevisionVal = hostMmsRevisionVal;
        this.springBootVersion = springBootVersion;
        this.installRoot = installRoot;
        this.descriptor = descriptor;
        this.hostServices = hostServices;
        this.pluginBeanRegistrar = pluginBeanRegistrar;
        this.peerInvoker = peerInvoker != null ? peerInvoker : (id, m, a) -> Optional.empty();
    }

    @Override
    public PluginDescriptor pluginDescriptor() {
        return descriptor;
    }

    @Override
    public HostServices hostServices() {
        return hostServices;
    }

    @Override
    public PluginBeanRegistrar pluginBeanRegistrar() {
        return pluginBeanRegistrar;
    }

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

    @Override
    public Optional<Object> tryInvokePeerPlugin(String targetPluginId, String methodName, Object... args) {
        return peerInvoker.invokePeer(targetPluginId, methodName, args != null ? args : new Object[0]);
    }

    @Override
    public void addUnloadHook(Runnable hook) {
        if (hook != null) {
            unloadHooks.add(hook);
        }
    }

    /**
     * onLoad 结束后由宿主取出并清空，避免重复执行。
     */
    public List<Runnable> takeUnloadHooksSnapshot() {
        List<Runnable> snap = new ArrayList<>(unloadHooks);
        unloadHooks.clear();
        return snap;
    }
}
