package com.sxpcwlkj.system.entity.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.annotation.SensitivityEncrypt;
import com.sxpcwlkj.common.enums.SensitivityTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.system.entity.SysUser;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = SysUser.class)
public class SysUserVo extends BaseEntityVo {

    /**
     * 主键ID
     */
    @ExcelProperty("用户ID")
    private String userId;
    /**
     * 部门ID
     */
    private String deptId;
    private String[] deptIds;
    /**
     * 部门名称
     */
    @ExcelProperty("部门名称")
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
    @SensitivityEncrypt(type = SensitivityTypeEnum.PHONE)
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

    @Dict("SYS_SEX")
    private String sex;

    /**
     * 头像
     */

    private String avatar;

    /**
     * 密码
     */
    private String password;
    /**
     * 密码强度
     */
    private String passwordStrength;

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
    private String aesKey;

    /**
     * 用户拥有角色
     */
    private List<SysRoleVo> roleVoList;
    /**
     * 用户角色集合
     */
    private String[] roleCodes;

    private String[] butCodes;
    /**
     * 角色id
     */
    private String roleName;
    /**
     * 用户状态
     */
    private Integer status;

}
