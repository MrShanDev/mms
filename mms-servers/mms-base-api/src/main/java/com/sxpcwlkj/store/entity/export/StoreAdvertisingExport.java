package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 广告Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAdvertisingVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAdvertisingExport  extends BaseEntityVo{
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
	 * 广告位ID
	 */
    @ExcelProperty("广告位ID")
	@PrintColumn(title = "广告位ID", type = PrintTypeEnum.TEXT)
	private  String advertisingId;
	/**
	 * 开始时间
	 */
    @ExcelProperty("开始时间")
	@PrintColumn(title = "开始时间", type = PrintTypeEnum.TEXT)
	private  String startTime;
	/**
	 * 到期时间
	 */
    @ExcelProperty("到期时间")
	@PrintColumn(title = "到期时间", type = PrintTypeEnum.TEXT)
	private  String endTime;
	/**
	 * 路由地址
	 */
    @ExcelProperty("路由地址")
	@PrintColumn(title = "路由地址", type = PrintTypeEnum.TEXT)
	private  String routeUrl;
	/**
	 * 图片地址
	 */
    @ExcelProperty("图片地址")
	@PrintColumn(title = "图片地址", type = PrintTypeEnum.TEXT)
	private  String imageUrl;
	/**
	 * 路由参数
	 */
    @ExcelProperty("路由参数")
	@PrintColumn(title = "路由参数", type = PrintTypeEnum.TEXT)
	private  String routeParameter;
}
