package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreProductBrand;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 商品品牌Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreProductBrand.class)
@EqualsAndHashCode(callSuper=false)
public class StoreProductBrandVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 品牌名称
	 */
	private String name;
	/**
	 * 品牌LOGO
	 */
	private String logo;
	/**
	 * 首字符
	 */
	private String firstLetter;

}
