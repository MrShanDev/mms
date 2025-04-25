package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 系统用户
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "user_id")
    private String userId;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String department;
    /**
     * 岗位编号数组
     */
    private String postIds;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 微信id
     */
    private String wxOpenid;
    /**
     * 微信开发者id
     */
    private String wxUnOpenId;
    /**
     * 性别;0：保密 1：男2：女
     */
    private Integer sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 密码
     */
    @TableField(
            insertStrategy = FieldStrategy.NOT_EMPTY,
            updateStrategy = FieldStrategy.NOT_EMPTY,
            whereStrategy = FieldStrategy.NOT_EMPTY
    )
    private String password;

    /**
     * 密码强度
     */
    private String passwordStrength;
    /**
     * 状态;0正常 1停用
     */
    private Integer status;
    /**
     * 删除标志;0代表存在 2代表删除
     */
    private Integer delFlag;
    /**
     * 最后登录ip
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private Date loginDate;
    /**
     * 对称性秘钥
     */
    @TableField("aes_key")
    private String aesKey;
    /**
     * 角色编码
     */
    @TableField(exist = false)
    private String[] roleCodes;
    /**
     * 角色id
     */
    @TableField(exist = false)
    private String roleName;

}
