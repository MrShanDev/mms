package com.sxpcwlkj.system.entity.vo;

import com.sxpcwlkj.system.entity.AdminMenuTree;
import com.sxpcwlkj.system.entity.SysRole;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户角色
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */

@Data
@AutoMapper(target = SysRole.class)
public class SysRoleVo {


    /**
     * 角色ID
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色权限字符串
     */
    private String code;

    /**
     * 显示顺序
     */
    private Long sort;

    /**
     * 状态;0正常 1停用
     */
    private String status;
    /**
     * 级别
     */
    private Integer level;


    /**
     * 角色拥有的资源
     */
    private List<SysFunctionVo> sysFunctionVoList;
    /**
     * 用户角色资源集合
     */
    private String[] permissions;

    private Date createdTime;

    private String remark;

    private String revision;
    /**
     * 系统资源
     */
    private List<AdminMenuTree> functionTree;
    /**
     * 默认选中的节点
     */
    private String[] defChecked;
}
