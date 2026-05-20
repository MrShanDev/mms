package com.sxpcwlkj.common.constants;

/**
 * {@code sys_plugins.listing_source}：上架来源，用于扩展与卸载策略（如官方不可删）。
 */
public final class PluginListingSource {

    /** 官方上架（随发行版/手工登记，禁止 purge / 删库表登记中的「卸载」） */
    public static final int OFFICIAL = 0;

    /** 用户通过本系统安装向导写入登记 */
    public static final int USER = 1;

    /** 预留 */
    public static final int RESERVED = 2;

    private PluginListingSource() {}
}
