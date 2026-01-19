package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单发票表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_invoice")
@EqualsAndHashCode(callSuper = true)
public class StoreInvoice  extends BaseEntity {
	/**
	* 主键ID
	*/
	@TableId
	private String id;
	/**
	* 订单编号
	*/
	private String orderId;
	/**
	* 发票号码
	*/
	private String invoiceNumber;
	/**
	* 发票类型
	*/
	private Integer invoiceType;
	/**
	* 抬头类型
	*/
	private Integer invoiceHeaderType;
	/**
	* 发票抬头
	*/
	private String invoiceHeader;
	/**
	* 纳税人识别号(企业必填)
	*/
	private String taxNumber;
	/**
	* 开户银行(专票必填)
	*/
	private String bankName;
	/**
	* 银行账号(专票必填)
	*/
	private String bankAccount;
	/**
	* 公司地址(专票必填)
	*/
	private String companyAddress;
	/**
	* 公司电话(专票必填)
	*/
	private String companyPhone;
	/**
	* 开票金额
	*/
	private BigDecimal invoiceAmount;
	/**
	* 开票商品明细
	*/
	private Object invoiceItems;
	/**
	* 商品摘要(用于发票显示)
	*/
	private String productSummary;
	/**
	* 商品总数量
	*/
	private Integer totalQuantity;
	/**
	* 开票状态
	*/
	private Integer invoiceStatus;
	/**
	* 申请时间
	*/
	private Date applyTime;
	/**
	* 开票时间
	*/
	private Date invoiceTime;
	/**
	* 发票下载地址
	*/
	private String downloadUrl;
	/**
	* 文件存储路径
	*/
	private String filePath;
	/**
	* 文件名称
	*/
	private String fileName;
	/**
	* 开票失败原因
	*/
	private String failReason;
}
