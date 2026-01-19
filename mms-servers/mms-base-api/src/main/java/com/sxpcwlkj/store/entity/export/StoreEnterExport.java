package com.sxpcwlkj.store.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.vo.StoreEnterVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;

/**
* 店铺入驻Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreEnterVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreEnterExport  extends BaseEntityVo{
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
	 * 公司名称
	 */
    @ExcelProperty("公司名称")
	@PrintColumn(title = "公司名称", type = PrintTypeEnum.TEXT)
	private  String companyAme;
	/**
	 * 公司详细地址
	 */
    @ExcelProperty("公司详细地址")
	@PrintColumn(title = "公司详细地址", type = PrintTypeEnum.TEXT)
	private  String companyAddress;
	/**
	 * 员工总数
	 */
    @ExcelProperty("员工总数")
	@PrintColumn(title = "员工总数", type = PrintTypeEnum.TEXT)
	private  Integer employeeNum;
	/**
	 * 公司电话
	 */
    @ExcelProperty("公司电话")
	@PrintColumn(title = "公司电话", type = PrintTypeEnum.TEXT)
	private  String companyPhone;
	/**
	 * 注册资金
	 */
    @ExcelProperty("注册资金")
	@PrintColumn(title = "注册资金", type = PrintTypeEnum.TEXT)
	private  Integer registeredCapital;
	/**
	 * 联系人姓名
	 */
    @ExcelProperty("联系人姓名")
	@PrintColumn(title = "联系人姓名", type = PrintTypeEnum.TEXT)
	private  String linkName;
	/**
	 * 联系人电话
	 */
    @ExcelProperty("联系人电话")
	@PrintColumn(title = "联系人电话", type = PrintTypeEnum.TEXT)
	private  String linkPhone;
	/**
	 * 电子邮箱
	 */
    @ExcelProperty("电子邮箱")
	@PrintColumn(title = "电子邮箱", type = PrintTypeEnum.TEXT)
	private  String companyEmail;
	/**
	 * 营业执照号
	 */
    @ExcelProperty("营业执照号")
	@PrintColumn(title = "营业执照号", type = PrintTypeEnum.TEXT)
	private  String licenseNum;
	/**
	 * 法定经营范围
	 */
    @ExcelProperty("法定经营范围")
	@PrintColumn(title = "法定经营范围", type = PrintTypeEnum.TEXT)
	private  String scope;
	/**
	 * 营业执照电子版
	 */
    @ExcelProperty("营业执照电子版")
	@PrintColumn(title = "营业执照电子版", type = PrintTypeEnum.TEXT)
	private  String licencePhoto;
	/**
	 * 法人姓名
	 */
    @ExcelProperty("法人姓名")
	@PrintColumn(title = "法人姓名", type = PrintTypeEnum.TEXT)
	private  String legalName;
	/**
	 * 法人证件号
	 */
    @ExcelProperty("法人证件号")
	@PrintColumn(title = "法人证件号", type = PrintTypeEnum.TEXT)
	private  String legalId;
	/**
	 * 法人证件电子版
	 */
    @ExcelProperty("法人证件电子版")
	@PrintColumn(title = "法人证件电子版", type = PrintTypeEnum.TEXT)
	private  String legalPhoto;
	/**
	 * 银行开户名
	 */
    @ExcelProperty("银行开户名")
	@PrintColumn(title = "银行开户名", type = PrintTypeEnum.TEXT)
	private  String settlementBankAccountName;
	/**
	 * 银行账号
	 */
    @ExcelProperty("银行账号")
	@PrintColumn(title = "银行账号", type = PrintTypeEnum.TEXT)
	private  String settlementBankAccountNum;
	/**
	 * 开户银行支行名称
	 */
    @ExcelProperty("开户银行支行名称")
	@PrintColumn(title = "开户银行支行名称", type = PrintTypeEnum.TEXT)
	private  String settlementBankBranchName;
	/**
	 * 支行联行号
	 */
    @ExcelProperty("支行联行号")
	@PrintColumn(title = "支行联行号", type = PrintTypeEnum.TEXT)
	private  String settlementBankjointName;
	/**
	 * 店铺名称
	 */
    @ExcelProperty("店铺名称")
	@PrintColumn(title = "店铺名称", type = PrintTypeEnum.TEXT)
	private  String storeName;
	/**
	 * 店铺logo
	 */
    @ExcelProperty("店铺logo")
	@PrintColumn(title = "店铺logo", type = PrintTypeEnum.TEXT)
	private  String storeLogo;
	/**
	 * 店铺简介
	 */
    @ExcelProperty("店铺简介")
	@PrintColumn(title = "店铺简介", type = PrintTypeEnum.TEXT)
	private  String storeDesc;
	/**
	 * 店铺经营类目
	 */
	@Dict("store_type")
	@ExcelProperty(value ="店铺经营类目",converter = DictExcelConverter.class)
	@PrintColumn(title = "店铺经营类目", type = PrintTypeEnum.TEXT)
	private  String goodsManagementCategory;
	/**
	 * 店铺所在地
	 */
    @ExcelProperty("店铺所在地")
	@PrintColumn(title = "店铺所在地", type = PrintTypeEnum.TEXT)
	private  String storeAddressIdPath;
	/**
	 * 店铺详细地址
	 */
    @ExcelProperty("店铺详细地址")
	@PrintColumn(title = "店铺详细地址", type = PrintTypeEnum.TEXT)
	private  String storeAddressDetail;
}
