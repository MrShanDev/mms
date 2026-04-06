package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 插件在安装时同步到 {@code sys_function} 的一行（主键 {@link #id} 固定，保证幂等）。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluginMenuItemDef {

    private String id;
    private String parentId;
    private String path;
    private String name;
    private String component;
    private String componentName;
    private String languageCode;
    private String permission;
    private Integer type;
    private Integer sort;
    private String icon;
    private Integer status;
    /**
     * 与 {@code mms-ui} 侧栏 {@code meta.isHide} 对应：后端为 {@code isHide = (visible == 1)}。
     * <p>目录菜单（{@code type=1}）要默认展示请用 {@code -1} 或 {@code 0}；{@code 1} 表示隐藏。</p>
     */
    private Integer visible;
    private Integer isIframe;
    private Integer isOpenLink;
    private String isLink;
    private Integer keepAlive;
    private Integer alwaysShow;
    private Integer isFast;
}
