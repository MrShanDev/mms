package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreCoupon;
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
* 优惠券管理Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreCoupon.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreCouponBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@NotBlank(message = "主键ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 优惠券码
	 */
	@NotBlank(message = "优惠券码不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String couponCode;
	/**
	 * 优惠券名称
	 */
	@NotBlank(message = "优惠券名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String couponName;
	/**
	 * 面额
	 */
	@NotNull(message = "面额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal faceValue;
	/**
	 * 到期时间
	 */
	@NotNull(message = "到期时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date expiryTime;
	/**
	 * 优惠券类型
	 */
	@NotNull(message = "优惠券类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer couponType;
	/**
	 * 最低使用金额
	 */
	@NotNull(message = "最低使用金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private BigDecimal minOrderAmount;
	/**
	 * 使用次数限制
	 */
	@NotNull(message = "使用次数限制不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer usageLimit;
	/**
	 * 已使用次数
	 */
	@NotNull(message = "已使用次数不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer usedCount;


    private List<String> spuIds;
}
