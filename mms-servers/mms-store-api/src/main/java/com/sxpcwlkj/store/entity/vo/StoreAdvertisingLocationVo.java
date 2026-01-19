package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreAdvertisingLocation;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 广告位Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreAdvertisingLocation.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAdvertisingLocationVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 广告位名称
	 */
	private String name;
	/**
	 * 广告位高度
	 */
	private String height;
	/**
	 * 广告位宽度
	 */
	private String width;
	/**
	 * 广告位编码
	 */
	private String code;
	/**
	 * 最大显示数量
	 */
	private Integer maxNum;

}
