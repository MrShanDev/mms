package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreRefundItem;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 退款商品明细表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreRefundItem.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundItemBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@NotBlank(message = "主键不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 退款申请ID
	 */
	@NotBlank(message = "退款申请ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String refundApplyId;
	/**
	 * 订单明细ID
	 */
	@NotBlank(message = "订单明细ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String orderItemId;
	/**
	 * 商品SKU ID
	 */
	@NotBlank(message = "商品SKU ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String skuId;
	/**
	 * SKU编码
	 */
	@NotBlank(message = "SKU编码不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String skuCode;
	/**
	 * SKU名称
	 */
	@NotBlank(message = "SKU名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String skuName;
	/**
	 * 商品单价
	 */
	@NotNull(message = "商品单价不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal unitPrice;
	/**
	 * 退款数量
	 */
	@NotNull(message = "退款数量不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer refundQuantity;
	/**
	 * 退款金额
	 */
	@NotNull(message = "退款金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal refundPrice;
	/**
	 * 商品主图
	 */
	@NotBlank(message = "商品主图不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String mainImage;
	/**
	 * 商品退款原因
	 */
	@NotBlank(message = "商品退款原因不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String refundReason;
	/**
	 * 证据图片
	 */
	@NotBlank(message = "证据图片不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Object evidenceImages;
}
