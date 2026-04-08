package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.system.entity.SysFunction;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统资源bo
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = SysFunction.class)
public class SysFunctionBo extends BaseEntity {
    /**
     * 菜单ID
     * */
    @NotBlank(message = "主键ID不能为空",groups = {ValidatedGroupConfig.update.class})
    private String id;
    /**
     * 父菜单ID
     */
    @NotBlank(message = "主键ID不能为空",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String parentId;
    /**
     * 菜单名称
     */
    @NotBlank(message = "主键ID不能为空",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
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
    @NotBlank(message = "菜单类型不能为空",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer type;
    /**
     * 显示顺序
     */
    @Min(message = "显示顺序小于1",value = 1 ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer sort;

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
     * 默认跳转（纯目录无组件时填写）
     */
    private String redirectPath;
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
     * 级别
     */
    private Integer level=0;
    /**
     * 是否快捷菜单
     */
    private Integer isFast;
}
