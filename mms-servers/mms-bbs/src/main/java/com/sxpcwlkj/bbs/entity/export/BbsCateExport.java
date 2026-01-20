package com.sxpcwlkj.bbs.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.bbs.entity.vo.BbsCateVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 话题分类Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = BbsCateVo.class)
@EqualsAndHashCode(callSuper=false)
public class BbsCateExport  extends BaseEntityVo{
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
	 * 父ID
	 */
    @ExcelProperty("父ID")
	@PrintColumn(title = "父ID", type = PrintTypeEnum.TEXT)
	private  String fatherId;
	/**
	 * 名称
	 */
    @ExcelProperty("名称")
	@PrintColumn(title = "名称", type = PrintTypeEnum.TEXT)
	private  String name;
	/**
	 * 图标
	 */
    @ExcelProperty("图标")
	@PrintColumn(title = "图标", type = PrintTypeEnum.TEXT)
	private  String icon;
}
