package com.sxpcwlkj.system.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 后端菜单
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
public class AdminMenuTree {

    // ID
    private String id;
    // 路由路径
    private String path;
    // 路由名称
    private String name;
    // 组件路径
    private String component;
    // 组件路径别名
    private String componentAlias;
    // 路由重定向，有子集 children 时
    private String redirect;
    // 菜单排序
    //private Integer menuSort;
    // 是否外链
    //private Boolean isLink;
    // 菜单类型为按钮时，权限标识
    //private String btnPower;

    private Map<String,Object> meta;

    private List<AdminMenuTree> children;

}
