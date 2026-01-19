package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreArticleCateVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 店铺文章分类Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreArticleCateVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreArticleCateExport  extends BaseEntityVo{
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
	private  String cateName;
	/**
	 * 级别
	 */
    @ExcelProperty("级别")
	@PrintColumn(title = "级别", type = PrintTypeEnum.TEXT)
	private  Integer level;
	/**
	 * 图标
	 */
    @ExcelProperty("图标")
	@PrintColumn(title = "图标", type = PrintTypeEnum.TEXT)
	private  String icon;
}
