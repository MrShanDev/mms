package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.StoreShipmentItem;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* 发货商品明细表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreShipmentItem.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreShipmentItemBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@NotBlank(message = "主键不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 发货单ID
	 */
	@NotBlank(message = "发货单ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String shipmentId;
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
	 * 订单数量
	 */
	@NotNull(message = "订单数量不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer orderQuantity;
	/**
	 * 本次发货数量
	 */
	@NotNull(message = "本次发货数量不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer shippedQuantity;
	/**
	 * 批次号
	 */
	@NotBlank(message = "批次号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String batchNo;
	/**
	 * 生产日期
	 */
	@NotNull(message = "生产日期不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date productionDate;
	/**
	 * 有效期至
	 */
	@NotNull(message = "有效期至不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date expiryDate;
}
