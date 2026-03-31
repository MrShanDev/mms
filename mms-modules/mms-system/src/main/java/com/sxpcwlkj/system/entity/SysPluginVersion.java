package com.sxpcwlkj.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_plugin_version")
public class SysPluginVersion extends BaseEntity {

    @TableId("id")
    private String id;

    @TableField("plugin_id")
    private String pluginId;

    private String version;

    @TableField("is_active")
    private Integer isActive;
}
