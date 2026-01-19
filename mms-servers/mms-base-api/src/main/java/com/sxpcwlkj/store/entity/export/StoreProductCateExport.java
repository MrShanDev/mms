package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreProductCateVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 商品分类Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreProductCateVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreProductCateExport  extends BaseEntityVo{
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
	private  String parentId;
	/**
	 * 分类名称
	 */
    @ExcelProperty("分类名称")
	@PrintColumn(title = "分类名称", type = PrintTypeEnum.TEXT)
	private  String name;
	/**
	 * 店铺ID
	 */
    @ExcelProperty("店铺ID")
	@PrintColumn(title = "店铺ID", type = PrintTypeEnum.TEXT)
	private  String storeId;
	/**
	 * 分类图标
	 */
    @ExcelProperty("分类图标")
	@PrintColumn(title = "分类图标", type = PrintTypeEnum.TEXT)
	private  String cateIcon;
	/**
	 * 分类背景图片
	 */
    @ExcelProperty("分类背景图片")
	@PrintColumn(title = "分类背景图片", type = PrintTypeEnum.TEXT)
	private  String cateBgImg;
	/**
	 * 层级
	 */
    @ExcelProperty("层级")
	@PrintColumn(title = "层级", type = PrintTypeEnum.TEXT)
	private  String level;
}
