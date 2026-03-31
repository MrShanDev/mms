package com.sxpcwlkj.plugin.host;

import com.sxpcwlkj.plugin.PluginDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 将插件宿主与业务库（安装记录、激活版本）衔接。未注册 Bean 时宿主按磁盘全量加载（兼容旧环境）。
 */
public interface PluginHostDbBridge {

    /**
     * 库中已有安装/版本记录的插件 ID（任意版本一行即视为纳入管理）。<br>
     * 若返回空集，宿主对磁盘做<strong>全量</strong>扫描（兼容纯磁盘旧环境）。
     */
    default Set<String> pluginIdsManagedInDatabase() {
        return Collections.emptySet();
    }

    /**
     * 启动时应加载的激活版本（每插件通常一条）。
     */
    default List<PluginVersionCoordinate> activeVersionsForStartup() {
        return Collections.emptyList();
    }

    /**
     * JAR 已写入磁盘且已校验通过后调用；应写入版本记录并标记为当前激活版本。
     */
    void onInstallSuccess(PluginDescriptor descriptor);

    /**
     * 磁盘目录已删除后调用；{@code version} 为空表示该插件全部版本已卸载。
     */
    void onUninstallDiskFinished(String pluginId, String versionOrNullMeansAll);

    /**
     * 将激活版本切换为磁盘上已存在的某一版本（不写磁盘，仅更新库表后由宿主 reload）。
     */
    void activateInstalledVersion(String pluginId, String version);
}
