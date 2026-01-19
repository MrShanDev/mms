package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreProductBrandVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 商品品牌Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreProductBrandVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreProductBrandExport  extends BaseEntityVo{
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
	 * 品牌名称
	 */
    @ExcelProperty("品牌名称")
	@PrintColumn(title = "品牌名称", type = PrintTypeEnum.TEXT)
	private  String name;
	/**
	 * 品牌LOGO
	 */
    @ExcelProperty("品牌LOGO")
	@PrintColumn(title = "品牌LOGO", type = PrintTypeEnum.TEXT)
	private  String logo;
	/**
	 * 首字符
	 */
    @ExcelProperty("首字符")
	@PrintColumn(title = "首字符", type = PrintTypeEnum.TEXT)
	private  String firstLetter;
}
