package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreOrderCart;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 购物车表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreOrderCart.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderCartBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 购物车项ID
	 */
	@NotBlank(message = "购物车项ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 用户ID
	 */
	@NotBlank(message = "用户ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String userId;
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
	 * 是否选中（0:未选中 1:已选中）
	 */
	@NotBlank(message = "是否选中（0:未选中 1:已选中）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer selected;
	/**
	 * 商品主图
	 */
	@NotBlank(message = "商品主图不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String mainImage;
}
