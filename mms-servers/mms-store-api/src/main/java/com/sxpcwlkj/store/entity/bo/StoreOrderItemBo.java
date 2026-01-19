package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreOrderItem;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 订单商品明细表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreOrderItem.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderItemBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@NotBlank(message = "主键不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 订单ID
	 */
	@NotBlank(message = "订单ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String orderId;
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
	 * SPU ID
	 */
	@NotBlank(message = "SPU ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String spuId;
	/**
	 * 商品单价
	 */
	@NotNull(message = "商品单价不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal price;
	/**
	 * 购买数量
	 */
	@NotNull(message = "购买数量不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer quantity;
	/**
	 * 商品总价
	 */
	@NotNull(message = "商品总价不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal totalPrice;
	/**
	 * 商品主图
	 */
	@NotBlank(message = "商品主图不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String mainImage;
}
