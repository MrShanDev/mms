package com.sxpcwlkj.store.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.StoreCoupon;
import com.sxpcwlkj.framework.entity.BaseEntityVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* 优惠券管理Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreCoupon.class)
@EqualsAndHashCode(callSuper=false)
public class StoreCouponVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 优惠券码
	 */
	private String couponCode;
	/**
	 * 优惠券名称
	 */
	private String couponName;
	/**
	 * 面额
	 */
	private BigDecimal faceValue;
	/**
	 * 到期时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date expiryTime;
	/**
	 * 优惠券类型
	 */
	private Integer couponType;
	/**
	 * 最低使用金额
	 */
	private BigDecimal minOrderAmount;
	/**
	 * 使用次数限制
	 */
	private Integer usageLimit;
	/**
	 * 已使用次数
	 */
	private Integer usedCount;

    private List<String> spuIds;
}
