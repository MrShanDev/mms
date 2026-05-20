package com.sxpcwlkj.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 插件市场展示元数据（与磁盘安装、内存加载状态由接口合并）。
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_plugins")
public class SysPlugin extends BaseEntity {

    @TableId(value = "id")
    private String id;

    @TableField("plugin_id")
    private String pluginId;

    private String name;

    @TableField("icon_url")
    private String iconUrl;

    private String description;

    /**
     * 上架来源：0 官方 1 用户安装登记 2 预留；旧库可能尚未加 listing_source 列，先做兼容不参与 ORM 持久化。
     */
    @TableField(exist = false)
    private Integer listingSource;
}
