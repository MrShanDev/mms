package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统用户角色
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {

    /**
     * 主键编码
     */
    @TableId(value = "id")
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 角色ID
     */
    private String roleId;
}
