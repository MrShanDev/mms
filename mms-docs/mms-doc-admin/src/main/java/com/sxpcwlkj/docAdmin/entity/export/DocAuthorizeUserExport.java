package com.sxpcwlkj.docAdmin.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.docAdmin.entity.vo.DocAuthorizeUserVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 文档授权用户Export
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocAuthorizeUserVo.class)
@EqualsAndHashCode(callSuper=false)
public class DocAuthorizeUserExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ExcelIgnore
    @ExcelProperty("ID")
	@PrintColumn(title = "ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 用户编号
	 */
    @ExcelProperty("用户编号")
	@PrintColumn(title = "用户编号", type = PrintTypeEnum.TEXT)
	private  String uid;
	/**
	 * 授权平台
	 */
    @ExcelProperty("授权平台")
	@PrintColumn(title = "授权平台", type = PrintTypeEnum.TEXT)
	private  String chan;
	/**
	 * 授权平台标识
	 */
    @ExcelProperty("授权平台标识")
	@PrintColumn(title = "授权平台标识", type = PrintTypeEnum.TEXT)
	private  String appid;
	/**
	 * 授权平台用户ID
	 */
    @ExcelProperty("授权平台用户ID")
	@PrintColumn(title = "授权平台用户ID", type = PrintTypeEnum.TEXT)
	private  String openid;
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
