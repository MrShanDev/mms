package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreAdvertising;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 广告Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreAdvertising.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAdvertisingVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 广告位ID
	 */
	private String advertisingId;
    private String advertisingName;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 到期时间
	 */
	private String endTime;
	/**
	 * 路由地址
	 */
	private String routeUrl;
	/**
	 * 图片地址
	 */
	private String imageUrl;
	/**
	 * 路由参数
	 */
	private String routeParameter;
	/**
	 * 扩展参数一
	 */
	private String extendedParameterOne;
	/**
	 * 扩展参数三
	 */
	private String extendedParameterThree;
	/**
	 * 扩展参数二
	 */
	private String extendedParameterTwo;
	/**
	 * 扩展参数四
	 */
	private String extendedParameterFour;
	/**
	 * 扩展参数五
	 */
	private String extendedParameterFive;

}
