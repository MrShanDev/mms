package com.sxpcwlkj.docAdmin.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.docAdmin.entity.vo.DocUserVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 文档用户Export
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocUserVo.class)
@EqualsAndHashCode(callSuper=false)
public class DocUserExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	@ExcelIgnore
    @ExcelProperty("用户编号")
	@PrintColumn(title = "用户编号", type = PrintTypeEnum.TEXT)
	private  String uid;
	/**
	 * 昵称
	 */
    @ExcelProperty("昵称")
	@PrintColumn(title = "昵称", type = PrintTypeEnum.TEXT)
	private  String nickname;
	/**
	 * 头像
	 */
    @ExcelProperty("头像")
	@PrintColumn(title = "头像", type = PrintTypeEnum.TEXT)
	private  String avatar;
	/**
	 * 用户类型
	 */
    @ExcelProperty("用户类型")
	@PrintColumn(title = "用户类型", type = PrintTypeEnum.TEXT)
	private  String type;
	/**
	 * 创建时间
	 */
    @ExcelProperty("创建时间")
	@PrintColumn(title = "创建时间", type = PrintTypeEnum.TEXT)
	private  Date ctime;
	/**
	 * 更新时间
	 */
    @ExcelProperty("更新时间")
	@PrintColumn(title = "更新时间", type = PrintTypeEnum.TEXT)
	private  Date mtime;
}
