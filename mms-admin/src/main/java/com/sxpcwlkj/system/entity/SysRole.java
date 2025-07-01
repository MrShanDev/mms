package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统角色
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    /**
     * 角色ID
     */
    @TableId(value = "id")
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
     * 级别
     */
    private Integer level;
}
