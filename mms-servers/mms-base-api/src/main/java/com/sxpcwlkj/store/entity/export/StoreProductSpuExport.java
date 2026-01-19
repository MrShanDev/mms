package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreProductSpuVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 店铺商品Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreProductSpuVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreProductSpuExport  extends BaseEntityVo{
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
	 * 店铺ID
	 */
    @ExcelProperty("店铺ID")
	@PrintColumn(title = "店铺ID", type = PrintTypeEnum.TEXT)
	private  String storeId;
	/**
	 * 分类
	 */
    @ExcelProperty("分类")
	@PrintColumn(title = "分类", type = PrintTypeEnum.TEXT)
	private  String cateId;
	/**
	 * 品牌ID
	 */
    @ExcelProperty("品牌ID")
	@PrintColumn(title = "品牌ID", type = PrintTypeEnum.TEXT)
	private  String brandId;
	/**
	 * 标题
	 */
    @ExcelProperty("标题")
	@PrintColumn(title = "标题", type = PrintTypeEnum.TEXT)
	private  String title;
	/**
	 * 副标题
	 */
    @ExcelProperty("副标题")
	@PrintColumn(title = "副标题", type = PrintTypeEnum.TEXT)
	private  String subTitle;
	/**
	 * 主图
	 */
    @ExcelProperty("主图")
	@PrintColumn(title = "主图", type = PrintTypeEnum.TEXT)
	private  String mainImage;
	/**
	 * 图片集
	 */
    @ExcelProperty("图片集")
	@PrintColumn(title = "图片集", type = PrintTypeEnum.TEXT)
	private  String listImages;
	/**
	 * 标签集
	 */
    @ExcelProperty("标签集")
	@PrintColumn(title = "标签集", type = PrintTypeEnum.TEXT)
	private  String tagIds;
	/**
	 * 商品描述1
	 */
    @ExcelProperty("商品描述1")
	@PrintColumn(title = "商品描述1", type = PrintTypeEnum.TEXT)
	private  String describeOne;
	/**
	 * 商品描述2
	 */
    @ExcelProperty("商品描述2")
	@PrintColumn(title = "商品描述2", type = PrintTypeEnum.TEXT)
	private  String describeTwo;
	/**
	 * 商品描述3
	 */
    @ExcelProperty("商品描述3")
	@PrintColumn(title = "商品描述3", type = PrintTypeEnum.TEXT)
	private  String describeThree;
	/**
	 * 商品描述4
	 */
    @ExcelProperty("商品描述4")
	@PrintColumn(title = "商品描述4", type = PrintTypeEnum.TEXT)
	private  String describeFour;
	/**
	 * 商品描述5
	 */
    @ExcelProperty("商品描述5")
	@PrintColumn(title = "商品描述5", type = PrintTypeEnum.TEXT)
	private  String describeFive;
	/**
	 * 详情介绍
	 */
    @ExcelProperty("详情介绍")
	@PrintColumn(title = "详情介绍", type = PrintTypeEnum.TEXT)
	private  String detailHtml;
}
