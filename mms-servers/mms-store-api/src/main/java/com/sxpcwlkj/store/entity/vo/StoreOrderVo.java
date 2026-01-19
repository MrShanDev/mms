package com.sxpcwlkj.store.entity.vo;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.store.entity.StoreOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* 订单主表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreOrder.class)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	private String id;
    /**
     * 店铺ID
     */
    private String storeId;
    /**
     * 店铺名称
     */
    private String storeName;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 用户ID
	 */
	private String userId;
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    /**
     * 订单运费
     */
    private BigDecimal freightAmount;
    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private Date payTime;
    /**
     * 保险费用
     */
    private BigDecimal insuranceAmount;

    /**
     * 订单最后金额
     */
    private BigDecimal orderEndPrice;

    /**
     * 交易单号
     */
    private String transactionNumber;
    /**
     * 收款商户账号
     */
    private String mchId;
	/**
	 * 支付方式
	 */
    @Dict("PAY-TYPE")
	private Integer payType;
    /**
     * 配送方式
     */
    @Dict("deliveryType")
    private Integer deliveryType;
	/**
	 * 收货人姓名
	 */
	private String deliveryName;
	/**
	 * 收货人电话
	 */
	private String deliveryPhone;
	/**
	 * 收货地址
	 */
	private String deliveryAddress;

    private  Long startTime;

    private  Long  endTime;

    private  List<StoreOrderSpuVo> spuList;

    private StoreOrderStateVo storeOrderStateVo;

    private List<StoreOrderStatusLogVo> stateVos;

    private JSONObject logistics;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 会员信息
     */
    private StoreMemberVo storeMemberVo;
    /**
     * 退款申请信息
     */
    private StoreRefundApplyVo refundApplyVo;
    /**
     * 退款物流信息
     */
    private StoreRefundLogisticsVo storeRefundLogisticsVo;

    /**
     * 商品总数量
     */
    private Integer totalQuantity;
    /**
     * 发票信息
     */
    private StoreInvoiceVo storeInvoiceVo;

}
