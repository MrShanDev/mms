package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreAttrKeySpuVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 商品规格项关系表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAttrKeySpuVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAttrKeySpuExport  extends BaseEntityVo{
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
	 * 属性ID
	 */
    @ExcelProperty("属性ID")
	@PrintColumn(title = "属性ID", type = PrintTypeEnum.TEXT)
	private  String attrKeyId;
}
