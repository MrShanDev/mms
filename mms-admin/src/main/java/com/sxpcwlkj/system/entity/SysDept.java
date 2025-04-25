package com.sxpcwlkj.system.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;


/**
 * 系统部门
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("sys_dept")
@EqualsAndHashCode(callSuper = true)
public class SysDept  extends BaseEntity {
	/**
	* 部门编号
	*/
	@TableId
	private String deptId;
	/**
	* 父级编号
	*/
	private String parentId;
	/**
	* 部门名称
	*/
	private String deptName;
	/**
	* 负责人
	*/
	private String leader;
	/**
	* 联系方式
	*/
	private String phone;
	/**
	* 邮箱
	*/
	private String email;
	/**
	* 详细地址
	*/
	private String address;
}
