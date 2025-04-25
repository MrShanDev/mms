package com.sxpcwlkj.system.entity.bo;

import java.io.Serial;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.system.entity.SysDept;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* 系统部门Bo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = SysDept.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class SysDeptBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 部门编号
	 */
    @NotBlank(message = "部门编号不能为空",groups = {ValidatedGroupConfig.update.class})
	private String deptId;

	/**
	 * 父级编号
	 */
    @NotBlank(message = "父级编号不能为空",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String parentId;

	/**
	 * 部门名称
	 */
    @NotBlank(message = "部门名称不能为空",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String deptName;

	/**
	 * 负责人
	 */
    @NotBlank(message = "负责人不能为空",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String leader;

	/**
	 * 联系方式
	 */
    @NotBlank(message = "联系方式不能为空",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    @Pattern(regexp = "^1[3456789]\\d{9}$",message = "手机号格式不正确",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String phone;

	/**
	 * 邮箱
	 */
    @NotBlank(message = "邮箱不能为空",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    @Email(message = "邮箱格式不正确",groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String email;

	/**
	 * 详细地址
	 */
    @NotBlank(message = "详细地址不能为空")
	private String address;

}
