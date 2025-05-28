package com.sxpcwlkj.mobile.entity.bo;


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
public class StoreAdvertisingBo extends BaseEntity {
	/**
	 * ID
	 */
	private String id;
	/**
	 * 广告位ID
	 */
	private String advertisingLocationId;
	/**
	 * 广告名称
	 */
	private String name;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 广告图
	 */
	private String image;
	/**
	 * 是否跳转
	 */
	private Integer isOpen;
	/**
	 * 打开方式
	 */
	private Integer openWay;
	/**
	 * 路由地址
	 */
	private String openPath;
	/**
	 * 路由参数
	 */
	private String routeParameter;
}
