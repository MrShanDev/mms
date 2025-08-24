package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.entity.vo.SysRoleVo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

/**
 * 系统用户bo
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = SysUser.class, reverseConvertGenerate = false)
public class SysUserBo extends BaseEntity {
    /**
     * 主键ID
     */
    @NotBlank(message = "主键ID不能为空", groups = {ValidatedGroupConfig.update.class})
    private String userId;

    /**
     * 部门ID
     */
    private String deptId;

    @NotEmpty(message = "部门不能为空", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    private String[] deptIds;
    /**
     * 部门名称
     */
    private String department;
    /**
     * 岗位编号数组
     */
    private String postIds;

    /**
     * 用户账号
     */
    @NotBlank(message = "账号不能为空", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    @Size(min = 4, max = 20, message = "账号长度在5到20之间", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    private String userName;

    /**
     * 用户昵称
     */
    @NotBlank(message = "账号昵称不能为空", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    @Size(min = 2, max = 20, message = "账号昵称长度在2到20之间", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    private String nickName;

    /**
     * 用户类型
     */
    @NotBlank(message = "账号类型不能为空", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    private String userType;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    @Email(message = "请正确填写邮箱", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    private String email;

    /**
     * 手机号
     */
    @Length(min = 11, max = 11, message = "手机号格式不正确", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
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
    @NotBlank(message = "性别不能为空", groups = {ValidatedGroupConfig.insert.class, ValidatedGroupConfig.update.class})
    private String sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {ValidatedGroupConfig.insert.class})
    @Size(min = 6, max = 32, message = "密码长度在{min}-{max}位之间", groups = {ValidatedGroupConfig.insert.class})
    private String password;

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
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 角色id
     */
    private String roleName;

}
