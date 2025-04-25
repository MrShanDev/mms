package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统角色功能
 * @author xijue
 * @Doc mmsadmin.cn
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_function")

public class SysRoleFunction extends BaseEntity {

    /**
     * 主键编码
     */
    @TableId(value = "id")
    private String id;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 功能ID
     */
    private String functionId;

}
