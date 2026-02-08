package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统租户套餐
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@TableName("sys_tenant_package")
public class SysTenantPackage extends BaseEntity {

    /**
     * 套餐编号
     */
    @TableId(value = "id")
    private String id;
    /**
     * 套餐名
     */
    private String name;
    /**
     * 租户状态;0正常 1停用
     */
    private Integer status;
    /**
     * 关联的菜单编号
     */
    private String functionIds;

}
