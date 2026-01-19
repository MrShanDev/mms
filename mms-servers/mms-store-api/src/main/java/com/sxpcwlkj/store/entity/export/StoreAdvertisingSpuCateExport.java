package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingSpuCateVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 广告商品分类组合表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAdvertisingSpuCateVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAdvertisingSpuCateExport  extends BaseEntityVo{
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
	 * 广告ID
	 */
    @ExcelProperty("广告ID")
	@PrintColumn(title = "广告ID", type = PrintTypeEnum.TEXT)
	private  String advertisementId;
	/**
	 * 商品分类ID
	 */
    @ExcelProperty("商品分类ID")
	@PrintColumn(title = "商品分类ID", type = PrintTypeEnum.TEXT)
	private  String spuCateId;
	/**
	 * 商品数量
	 */
    @ExcelProperty("商品数量")
	@PrintColumn(title = "商品数量", type = PrintTypeEnum.TEXT)
	private  Integer productNum;
	/**
	 * 组合类型
	 */
    @ExcelProperty("组合类型")
	@PrintColumn(title = "组合类型", type = PrintTypeEnum.TEXT)
	private  String typeCode;
}
