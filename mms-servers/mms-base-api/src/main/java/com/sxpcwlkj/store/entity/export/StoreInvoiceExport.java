package com.sxpcwlkj.store.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.vo.StoreInvoiceVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import java.util.Date;

/**
* 订单发票表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreInvoiceVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreInvoiceExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ExcelIgnore
    @ExcelProperty("主键ID")
	@PrintColumn(title = "主键ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 订单ID
	 */
    @ExcelProperty("订单ID")
	@PrintColumn(title = "订单ID", type = PrintTypeEnum.TEXT)
	private  String orderId;
	/**
	 * 发票号码
	 */
    @ExcelProperty("发票号码")
	@PrintColumn(title = "发票号码", type = PrintTypeEnum.TEXT)
	private  String invoiceNumber;
	/**
	 * 发票类型
	 */
	@Dict("invoice_type")
	@ExcelProperty(value ="发票类型",converter = DictExcelConverter.class)
	@PrintColumn(title = "发票类型", type = PrintTypeEnum.TEXT)
	private  Integer invoiceType;
	/**
	 * 抬头类型
	 */
	@Dict("invoice_header_type")
	@ExcelProperty(value ="抬头类型",converter = DictExcelConverter.class)
	@PrintColumn(title = "抬头类型", type = PrintTypeEnum.TEXT)
	private  Integer invoiceHeaderType;
	/**
	 * 发票抬头
	 */
    @ExcelProperty("发票抬头")
	@PrintColumn(title = "发票抬头", type = PrintTypeEnum.TEXT)
	private  String invoiceHeader;
	/**
	 * 纳税人识别号(企业必填)
	 */
    @ExcelProperty("纳税人识别号(企业必填)")
	@PrintColumn(title = "纳税人识别号(企业必填)", type = PrintTypeEnum.TEXT)
	private  String taxNumber;
	/**
	 * 开户银行(专票必填)
	 */
    @ExcelProperty("开户银行(专票必填)")
	@PrintColumn(title = "开户银行(专票必填)", type = PrintTypeEnum.TEXT)
	private  String bankName;
	/**
	 * 银行账号(专票必填)
	 */
    @ExcelProperty("银行账号(专票必填)")
	@PrintColumn(title = "银行账号(专票必填)", type = PrintTypeEnum.TEXT)
	private  String bankAccount;
	/**
	 * 公司地址(专票必填)
	 */
    @ExcelProperty("公司地址(专票必填)")
	@PrintColumn(title = "公司地址(专票必填)", type = PrintTypeEnum.TEXT)
	private  String companyAddress;
	/**
	 * 公司电话(专票必填)
	 */
    @ExcelProperty("公司电话(专票必填)")
	@PrintColumn(title = "公司电话(专票必填)", type = PrintTypeEnum.TEXT)
	private  String companyPhone;
	/**
	 * 开票金额
	 */
    @ExcelProperty("开票金额")
	@PrintColumn(title = "开票金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal invoiceAmount;
	/**
	 * 开票商品明细
	 */
    @ExcelProperty("开票商品明细")
	@PrintColumn(title = "开票商品明细", type = PrintTypeEnum.TEXT)
	private  Object invoiceItems;
	/**
	 * 商品摘要(用于发票显示)
	 */
    @ExcelProperty("商品摘要(用于发票显示)")
	@PrintColumn(title = "商品摘要(用于发票显示)", type = PrintTypeEnum.TEXT)
	private  String productSummary;
	/**
	 * 商品总数量
	 */
    @ExcelProperty("商品总数量")
	@PrintColumn(title = "商品总数量", type = PrintTypeEnum.TEXT)
	private  Integer totalQuantity;
	/**
	 * 开票状态
	 */
	@Dict("invoice_status")
	@ExcelProperty(value ="开票状态",converter = DictExcelConverter.class)
	@PrintColumn(title = "开票状态", type = PrintTypeEnum.TEXT)
	private  Integer invoiceStatus;
	/**
	 * 申请时间
	 */
    @ExcelProperty("申请时间")
	@PrintColumn(title = "申请时间", type = PrintTypeEnum.TEXT)
	private  Date applyTime;
	/**
	 * 开票时间
	 */
    @ExcelProperty("开票时间")
	@PrintColumn(title = "开票时间", type = PrintTypeEnum.TEXT)
	private  Date invoiceTime;
	/**
	 * 发票下载地址
	 */
    @ExcelProperty("发票下载地址")
	@PrintColumn(title = "发票下载地址", type = PrintTypeEnum.TEXT)
	private  String downloadUrl;
	/**
	 * 文件存储路径
	 */
    @ExcelProperty("文件存储路径")
	@PrintColumn(title = "文件存储路径", type = PrintTypeEnum.TEXT)
	private  String filePath;
	/**
	 * 文件名称
	 */
    @ExcelProperty("文件名称")
	@PrintColumn(title = "文件名称", type = PrintTypeEnum.TEXT)
	private  String fileName;
	/**
	 * 开票失败原因
	 */
    @ExcelProperty("开票失败原因")
	@PrintColumn(title = "开票失败原因", type = PrintTypeEnum.TEXT)
	private  String failReason;
}
