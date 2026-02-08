package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统功能资源
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@TableName("sys_function")
public class SysFunction extends BaseEntity {

    /**
     * 菜单ID
     */
    @TableId(value = "id")
    private String id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * i18n编码
     */
    private String languageCode;
    /**
     * 权限标识
     */
    private String permission;
    /**
     * 菜单类型
     */
    private Integer type;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 组件名
     */
    private String componentName;
    /**
     * 状态;0正常 1停用
     */
    private Integer status;
    /**
     * 是否可见
     */
    private Integer visible;
    /**
     * 是否缓存
     */
    private Integer keepAlive;
    /**
     * 链接地址
     */
    private String isLink;
    /**
     * 是否外链
     */
    private Integer isOpenLink;
    /**
     * 是否内嵌 true
     * 外链path=内嵌url
     */
    private Integer isIframe;
    /**
     * 是否总是显示
     */
    private Integer alwaysShow;
    /**
     * 是否快捷菜单
     */
    private Integer isFast;
}
