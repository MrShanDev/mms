package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreCouponProduct;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 优惠券适用商品关系表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreCouponProduct.class)
@EqualsAndHashCode(callSuper=false)
public class StoreCouponProductVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 优惠券ID
	 */
	private String couponId;
	/**
	 * 商品ID
	 */
	private String productId;
	/**
	 * 商品名称(冗余字段)
	 */
	private String productName;

}
