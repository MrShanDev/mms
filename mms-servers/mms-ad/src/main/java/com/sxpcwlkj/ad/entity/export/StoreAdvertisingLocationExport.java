package com.sxpcwlkj.ad.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.ad.entity.vo.StoreAdvertisingLocationVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 广告位Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAdvertisingLocationVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAdvertisingLocationExport  extends BaseEntityVo{
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
	 * 广告位名称
	 */
    @ExcelProperty("广告位名称")
	@PrintColumn(title = "广告位名称", type = PrintTypeEnum.TEXT)
	private  String name;
	/**
	 * 广告位高度
	 */
    @ExcelProperty("广告位高度")
	@PrintColumn(title = "广告位高度", type = PrintTypeEnum.TEXT)
	private  String height;
	/**
	 * 广告位宽度
	 */
    @ExcelProperty("广告位宽度")
	@PrintColumn(title = "广告位宽度", type = PrintTypeEnum.TEXT)
	private  String width;
	/**
	 * 广告位编码
	 */
    @ExcelProperty("广告位编码")
	@PrintColumn(title = "广告位编码", type = PrintTypeEnum.TEXT)
	private  String code;
	/**
	 * 最大显示数量
	 */
    @ExcelProperty("最大显示数量")
	@PrintColumn(title = "最大显示数量", type = PrintTypeEnum.TEXT)
	private  Integer maxNum;
}
