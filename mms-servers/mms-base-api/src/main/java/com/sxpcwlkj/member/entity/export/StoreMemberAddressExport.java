package com.sxpcwlkj.member.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.member.entity.vo.StoreMemberAddressVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 会员收货地址Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreMemberAddressVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreMemberAddressExport  extends BaseEntityVo{
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
	 * 会员ID
	 */
    @ExcelProperty("会员ID")
	@PrintColumn(title = "会员ID", type = PrintTypeEnum.TEXT)
	private  String memberId;
	/**
	 * 收货人
	 */
    @ExcelProperty("收货人")
	@PrintColumn(title = "收货人", type = PrintTypeEnum.TEXT)
	private  String name;
	/**
	 * 收货手机号
	 */
    @ExcelProperty("收货手机号")
	@PrintColumn(title = "收货手机号", type = PrintTypeEnum.TEXT)
	private  String phone;
	/**
	 * 国家
	 */
    @ExcelProperty("国家")
	@PrintColumn(title = "国家", type = PrintTypeEnum.TEXT)
	private  String country;
	/**
	 * 省
	 */
    @ExcelProperty("省")
	@PrintColumn(title = "省", type = PrintTypeEnum.TEXT)
	private  String province;
	/**
	 * 市
	 */
    @ExcelProperty("市")
	@PrintColumn(title = "市", type = PrintTypeEnum.TEXT)
	private  String city;
	/**
	 * 区/县
	 */
    @ExcelProperty("区/县")
	@PrintColumn(title = "区/县", type = PrintTypeEnum.TEXT)
	private  String district;
	/**
	 * 详细地址
	 */
    @ExcelProperty("详细地址")
	@PrintColumn(title = "详细地址", type = PrintTypeEnum.TEXT)
	private  String address;
	/**
	 * 是否默认
	 */
    @ExcelProperty("是否默认")
	@PrintColumn(title = "是否默认", type = PrintTypeEnum.TEXT)
	private  String isDef;
}
