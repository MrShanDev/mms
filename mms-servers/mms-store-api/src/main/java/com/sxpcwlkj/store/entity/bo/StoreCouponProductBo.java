package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreCouponProduct;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 优惠券适用商品关系表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreCouponProduct.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreCouponProductBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@NotBlank(message = "主键ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 优惠券ID
	 */
	@NotBlank(message = "优惠券ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String couponId;
	/**
	 * 商品ID
	 */
	@NotBlank(message = "商品ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String productId;
	/**
	 * 商品名称(冗余字段)
	 */
	@NotBlank(message = "商品名称(冗余字段)不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String productName;
}
