package com.sxpcwlkj.docApi.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.docApi.entity.DocOrder;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
* 文档订单Bo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocOrder.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class DocOrderBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 订单编号
	 */
	@NotBlank(message = "订单编号不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String orderId;
	/**
	 * 用户编号
	 */
	@NotBlank(message = "用户编号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String uid;
	/**
	 * 订单金额
	 */
	@NotNull(message = "订单金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal txnAmt;
	/**
	 * 支付商户号
	 */
	@NotBlank(message = "支付商户号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String payMchid;
	/**
	 * 支付平台流水号
	 */
	@NotBlank(message = "支付平台流水号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String payNo;
	/**
	 * 支付超时时间
	 */
	@NotBlank(message = "支付超时时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String payTimeout;
	/**
	 * 产品编号
	 */
	@NotBlank(message = "产品编号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String prodId;
	/**
	 * 产品名称
	 */
	@NotBlank(message = "产品名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String prodName;
	/**
	 * 产品价格
	 */
	@NotNull(message = "产品价格不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal prodPrice;
	/**
	 * 产品类型
	 */
	@NotBlank(message = "产品类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String prodType;
	/**
	 * 创建时间
	 */
	@NotBlank(message = "创建时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date ctime;
	/**
	 * 更新时间
	 */
	@NotBlank(message = "更新时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date mtime;
}
