package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreProductSku;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 商品存量价格Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreProductSku.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreProductSkuBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 商品ID
	 */
	@NotBlank(message = "商品ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String spuId;
	/**
	 * KU编码，唯一
	 */
	@NotBlank(message = "KU编码，唯一不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String skuCode;
	/**
	 * SKU名称(SPU名称+规格值)
	 */
	@NotBlank(message = "SKU名称(SPU名称+规格值)不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String skuName;
	/**
	 * 价格
	 */
	@NotNull(message = "价格不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal price;
	/**
	 * 成交价
	 */
	@NotNull(message = "成交价不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal costPrice;
	/**
	 * 库存
	 */
	@NotNull(message = "库存不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer stock;
	/**
	 * 库存预警值
	 */
	@NotNull(message = "库存预警值不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer stockWarning;
	/**
	 * 主图
	 */
	@NotBlank(message = "主图不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String mainImage;
}
