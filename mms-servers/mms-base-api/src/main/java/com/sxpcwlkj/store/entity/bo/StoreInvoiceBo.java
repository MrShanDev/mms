package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.StoreInvoice;
import java.math.BigDecimal;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* 订单发票表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreInvoice.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreInvoiceBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@NotBlank(message = "主键ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 订单编号
	 */
	@NotBlank(message = "订单编号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String orderId;
	/**
	 * 发票号码
	 */
	private String invoiceNumber;
	/**
	 * 发票类型（1:电子发票 2:纸质发票）
	 */
    @NotNull(message = "发票类型不能为空" ,groups = {ValidatedGroupConfig.insert.class})
	private Integer invoiceType;
	/**
	 * 抬头类型（1:个人 2:企业）
	 */
    @NotNull(message = "抬头类型不能为空" ,groups = {ValidatedGroupConfig.insert.class})
	private Integer invoiceHeaderType;
	/**
	 * 发票抬头
	 */
	@NotBlank(message = "发票抬头不能为空" ,groups = {ValidatedGroupConfig.insert.class})
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
	@NotNull(message = "开票金额不能为空" ,groups = {ValidatedGroupConfig.insert.class})
	@DecimalMin(value = "0.01", message = "开票金额必须大于0")
	private BigDecimal invoiceAmount;
	/**
	 * 开票商品明细
	 */
	@NotNull(message = "开票商品明细不能为空" ,groups = {ValidatedGroupConfig.insert.class})
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
	 * 开票状态（1:待开票 2:已开票 3:开票失败）
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
