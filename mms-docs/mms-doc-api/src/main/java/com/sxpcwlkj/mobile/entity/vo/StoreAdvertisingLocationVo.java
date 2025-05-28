package com.sxpcwlkj.mobile.entity.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.mobile.entity.StoreAdvertisingLocation;
import java.util.Date;
import java.util.List;

import com.sxpcwlkj.datasource.entity.BaseEntity;


/**
* 广告位;
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-05-13
*/
@EqualsAndHashCode(callSuper=false)
@Data
@AutoMapper(target = StoreAdvertisingLocation.class)
public class StoreAdvertisingLocationVo{
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
	private String maxNum;

	private List<StoreAdvertisingVo>  storeAdvertisingList;
}
