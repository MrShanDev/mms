package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreAttrValueSpu;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 商品规格值关系表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreAttrValueSpu.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAttrValueSpuVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 商品ID
	 */
	private String spuId;
	/**
	 * 属性值ID
	 */
	private String attrValueId;
	/**
	 * 规格项ID
	 */
	private String attrKeyId;
	/**
	 * 自定义属性值
	 */
	private String attrValueText;
	/**
	 * 属性值图片
	 */
	private String attrImage;

}
