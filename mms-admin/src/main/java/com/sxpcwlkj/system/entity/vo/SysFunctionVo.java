package com.sxpcwlkj.system.entity.vo;

import com.sxpcwlkj.system.entity.SysFunction;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 系统资源
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
@AutoMapper(target = SysFunction.class)
public class SysFunctionVo {

    private String id;
    /**
     * 父菜单ID
     */
    private String parentId;
    //上级菜单
    private String[] menuSuperior;
    /**
     * 路由名称
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

    // 路由重定向，有子集 children 时
    private String redirectPath;
    /**
     * 路由路径
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
     * isLink
     */
    private Integer isIframe;
    /**
     * 是否总是显示
     */
    private Integer alwaysShow;


    private String revision;

    private Map<String,Object> meta;

    private List<SysFunctionVo> children;
}
