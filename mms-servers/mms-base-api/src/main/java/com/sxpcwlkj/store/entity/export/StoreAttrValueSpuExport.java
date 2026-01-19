package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreAttrValueSpuVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 商品规格值关系表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAttrValueSpuVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAttrValueSpuExport  extends BaseEntityVo{
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
	 * 商品ID
	 */
    @ExcelProperty("商品ID")
	@PrintColumn(title = "商品ID", type = PrintTypeEnum.TEXT)
	private  String spuId;
	/**
	 * 属性值ID
	 */
    @ExcelProperty("属性值ID")
	@PrintColumn(title = "属性值ID", type = PrintTypeEnum.TEXT)
	private  String attrValueId;
	/**
	 * 规格项ID
	 */
    @ExcelProperty("规格项ID")
	@PrintColumn(title = "规格项ID", type = PrintTypeEnum.TEXT)
	private  String attrKeyId;
	/**
	 * 自定义属性值
	 */
    @ExcelProperty("自定义属性值")
	@PrintColumn(title = "自定义属性值", type = PrintTypeEnum.TEXT)
	private  String attrValueText;
	/**
	 * 属性值图片
	 */
    @ExcelProperty("属性值图片")
	@PrintColumn(title = "属性值图片", type = PrintTypeEnum.TEXT)
	private  String attrImage;
}
