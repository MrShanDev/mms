package com.sxpcwlkj.docAdmin.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.docAdmin.entity.vo.DocConfigVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 文档配置Export
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocConfigVo.class)
@EqualsAndHashCode(callSuper=false)
public class DocConfigExport  extends BaseEntityVo{
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
	 * KEY
	 */
    @ExcelProperty("KEY")
	@PrintColumn(title = "KEY", type = PrintTypeEnum.TEXT)
	private  String key;
	/**
	 * 值
	 */
    @ExcelProperty("值")
	@PrintColumn(title = "值", type = PrintTypeEnum.TEXT)
	private  String value;
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
