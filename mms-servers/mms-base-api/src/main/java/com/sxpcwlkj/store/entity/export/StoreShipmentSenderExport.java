package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.vo.StoreShipmentSenderVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 发货人信息Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreShipmentSenderVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreShipmentSenderExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 发件人ID
	 */
	@ExcelIgnore
    @ExcelProperty("发件人ID")
	@PrintColumn(title = "发件人ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 发件人姓名
	 */
    @ExcelProperty("发件人姓名")
	@PrintColumn(title = "发件人姓名", type = PrintTypeEnum.TEXT)
	private  String senderName;
	/**
	 * 发件人电话
	 */
    @ExcelProperty("发件人电话")
	@PrintColumn(title = "发件人电话", type = PrintTypeEnum.TEXT)
	private  String senderPhone;
	/**
	 * 发件地址
	 */
    @ExcelProperty("发件地址")
	@PrintColumn(title = "发件地址", type = PrintTypeEnum.TEXT)
	private  String senderAddress;
	/**
	 * 公司名称
	 */
    @ExcelProperty("公司名称")
	@PrintColumn(title = "公司名称", type = PrintTypeEnum.TEXT)
	private  String companyName;
	/**
	 * 是否默认发件人
	 */
    @ExcelProperty("是否默认发件人")
	@PrintColumn(title = "是否默认发件人", type = PrintTypeEnum.TEXT)
	private  Integer isDefault;
}
