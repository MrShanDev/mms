package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreTypeVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 店铺类型Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreTypeVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreTypeExport  extends BaseEntityVo{
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
	 * 名称
	 */
    @ExcelProperty("名称")
	@PrintColumn(title = "名称", type = PrintTypeEnum.TEXT)
	private  String storeTypeName;
	/**
	 * 图标片
	 */
    @ExcelProperty("图标片")
	@PrintColumn(title = "图标片", type = PrintTypeEnum.TEXT)
	private  String storeTypeIcon;
	/**
	 * 小图标
	 */
    @ExcelProperty("小图标")
	@PrintColumn(title = "小图标", type = PrintTypeEnum.TEXT)
	private  String storeTypeIco;
}
