package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreOrder;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
* 订单主表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreOrder.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	@NotBlank(message = "订单ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 订单编号
	 */
	@NotBlank(message = "订单编号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String orderNo;
	/**
	 * 用户ID
	 */
	@NotBlank(message = "用户ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String userId;
	/**
	 * 订单总金额
	 */
	@NotNull(message = "订单总金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal totalAmount;
	/**
	 * 优惠金额
	 */
	@NotNull(message = "优惠金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal discountAmount;
	/**
	 * 实际支付金额
	 */
	@NotNull(message = "实际支付金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal payAmount;
	/**
	 * 支付时间
	 */
	@NotNull(message = "支付时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date payTime;
	/**
	 * 支付方式
	 */
	@NotNull(message = "支付方式不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer payType;
	/**
	 * 收货人姓名
	 */
	@NotBlank(message = "收货人姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String deliveryName;
	/**
	 * 收货人电话
	 */
	@NotBlank(message = "收货人电话不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String deliveryPhone;
	/**
	 * 收货地址
	 */
	@NotBlank(message = "收货地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String deliveryAddress;
}
