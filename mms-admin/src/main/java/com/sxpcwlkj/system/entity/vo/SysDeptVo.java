package com.sxpcwlkj.system.entity.vo;


import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.system.entity.SysDept;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
import java.util.Date;

/**
* 系统部门Vo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = SysDept.class)
@EqualsAndHashCode(callSuper=false)
public class SysDeptVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 部门编号
	 */
    @ExcelProperty("部门编号")
	private String deptId;
	/**
	 * 父级编号
	 */
    @ExcelProperty("父级编号")
	private String parentId;
	/**
	 * 部门名称
	 */
    @ExcelProperty("部门名称")
	private String deptName;
	/**
	 * 负责人
	 */
    @ExcelProperty("负责人")
	private String leader;
	/**
	 * 联系方式
	 */
    @ExcelProperty("联系方式")
	private String phone;
	/**
	 * 邮箱
	 */
    @ExcelProperty("邮箱")
	private String email;
	/**
	 * 详细地址
	 */
    @ExcelProperty("详细地址")
	private String address;
    private String[] deptIds;
    List<SysDeptVo> children;

}
