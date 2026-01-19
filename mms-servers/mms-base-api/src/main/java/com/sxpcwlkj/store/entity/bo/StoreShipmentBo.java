package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreShipment;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* 发货明细Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreShipment.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreShipmentBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 发货ID
	 */
	@NotBlank(message = "发货ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 订单ID
	 */
	@NotBlank(message = "订单ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String orderId;
    /**
     * 发货类型
     */
    @NotNull(message = "发货类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer deliveryType;
	/**
	 * 发货单号
	 */
	//@NotBlank(message = "发货单号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String shipmentNo;
	/**
	 * 发货类型
	 */
	@NotNull(message = "发货类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer shipmentType;
	/**
	 * 物流公司
	 */
	//@NotBlank(message = "物流公司不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String logisticsCompany;
	/**
	 * 物流单号
	 */
	//@NotBlank(message = "物流单号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String logisticsNo;
	/**
	 * 发货时间
	 */
	//@NotNull(message = "发货时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date shipmentTime;
	/**
	 * 预计送达时间
	 */
	//@NotNull(message = "预计送达时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date deliveryTime;
	/**
	 * 发件人ID
	 */
	//@NotBlank(message = "发件人ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderId;
	/**
	 * 发件人姓名（冗余）
	 */
	//@NotBlank(message = "发件人姓名（冗余）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderName;
	/**
	 * 发件人电话（冗余）
	 */
	//@NotBlank(message = "发件人电话（冗余）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderPhone;
	/**
	 * 发件地址（冗余）
	 */
	//@NotBlank(message = "发件地址（冗余）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderAddress;
	/**
	 * 收货人姓名
	 */
	//@NotBlank(message = "收货人姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String receiverName;
	/**
	 * 收货人电话
	 */
	//@NotBlank(message = "收货人电话不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String receiverPhone;
	/**
	 * 收货地址
	 */
	//@NotBlank(message = "收货地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String receiverAddress;
	/**
	 * 包裹重量（kg）
	 */
	//@NotNull(message = "包裹重量（kg）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal shipmentWeight;
	/**
	 * 包裹体积（m³）
	 */
	//@NotNull(message = "包裹体积（m³）不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal shipmentVolume;
	/**
	 * 实际运费
	 */
	//@NotNull(message = "实际运费不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal freightAmount;
	/**
	 * 保价金额
	 */
	//@NotNull(message = "保价金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal insuranceAmount;
	/**
	 * 包裹数量
	 */
	//@NotNull(message = "包裹数量不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer packageCount;
	/**
	 * 发货状态
	 */
	//@NotNull(message = "发货状态不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer shipmentStatus;
	/**
	 * 异常原因
	 */
	//@NotBlank(message = "异常原因不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String exceptionReason;
	/**
	 * 签收人姓名
	 */
	//@NotBlank(message = "签收人姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String signerName;
	/**
	 * 签收时间
	 */
	//@NotNull(message = "签收时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date signTime;
	/**
	 * 签收备注
	 */
	//@NotBlank(message = "签收备注不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String signRemark;

    /**
     * 商品IDs
     */
    private List<String> itemIds;
}
