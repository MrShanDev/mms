package com.sxpcwlkj.mobile.entity.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.sxpcwlkj.common.annotation.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.mobile.entity.StoreAdvertising;
import java.util.Date;
	import com.sxpcwlkj.datasource.entity.BaseEntity;


/**
* 广告;
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-05-13
*/
@EqualsAndHashCode(callSuper=false)
@Data
@AutoMapper(target = StoreAdvertising.class)
public class StoreAdvertisingVo  {
	/**
	 * ID
	 */
	private String id;
	/**
	 * 广告位ID
	 */
	private String advertisingId;
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
}
