package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统租户
 * @author xijue
 * @Doc mmsadmin.cn
 */

@Data
@NoArgsConstructor

@TableName("sys_tenant")
public class SysTenant{

    /**
     * 租户编号
     */
    @TableId(value = "tenant_id")
    private String tenantId;
    /**
     * 租户名
     */
    private String name;
    /**
     * 联系人的用户编号
     */
    private String contactUserId;
    /**
     * 联系人
     */
    private String contactName;
    /**
     * 联系手机
     */
    private String contactMobile;
    /**
     * 租户状态;0正常 1停用
     */
    private Integer status;
    /**
     * 绑定域名
     */
    private String domain;
    /**
     * 租户套餐编号
     */
    private String packageId;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 账号数量
     */
    private Integer accountCount;

    /**
     * 备注
     */
    @TableField(fill = FieldFill.INSERT)
    private String remark;


    /**
     * 乐观锁
     */
    @TableField(fill = FieldFill.INSERT)
    private String revision;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;

    /**
     * 搜索值
     */
    @JsonIgnore
    @TableField(exist = false)
    private String searchValue;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
