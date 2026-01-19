package com.sxpcwlkj.store.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.StoreRefundApply;
import com.sxpcwlkj.framework.entity.BaseEntityVo;

import java.util.List;
import java.math.BigDecimal;
import java.util.Date;

/**
* 退款申请表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreRefundApply.class)
@EqualsAndHashCode(callSuper=false)
public class StoreRefundApplyVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 退款申请ID
	 */
	private String id;
	/**
	 * 订单ID
	 */
	private String orderId;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 退款单号（唯一）
	 */
	private String refundNo;
	/**
	 * 退款类型
	 */
	private Integer refundType;
	/**
	 * 退款原因
	 */
	private String refundReason;
	/**
	 * 退款原因描述
	 */
	private String refundReasonDesc;
	/**
	 * 申请退款金额
	 */
	private BigDecimal refundAmount;
	/**
	 * 实际退款金额
	 */
	private BigDecimal actualRefundAmount;
	/**
	 * 申请时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date applyTime;
	/**
	 * 申请人
	 */
	private String applyBy;
	/**
	 * 退款状态
	 */
	private Integer refundStatus;
	/**
	 * 审核时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date auditTime;
	/**
	 * 审核人
	 */
	private String auditBy;
	/**
	 * 审核备注
	 */
	private String auditRemark;
	/**
	 * 退款时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date refundTime;
	/**
	 * 退款备注
	 */
	private String refundRemark;
	/**
	 * 退款失败原因
	 */
	private String failReason;


    List<StoreRefundItemVo> itemVos;
    /**
     * 是否可以取消
     */
    Boolean isCanCancel;
    /**
     * 是否等待买家退货
     */
    Boolean isWaitingBuyerReturn;

    private String spuId;

    private String storeId;

    private String storeName;

}
