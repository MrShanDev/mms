package com.sxpcwlkj.mobile.entity.bo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.mobile.entity.StoreAdvertisingLocation;
import java.util.Date;
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
public class StoreAdvertisingLocationBo extends BaseEntity {
	/**
	 * ID
	 */
	private String id;
	/**
	 * 广告位名称
	 */
	private String name;
	/**
	 * 广告位编码
	 */
	private String code;
	/**
	 * 宽度
	 */
	private Integer width;
	/**
	 * 高度
	 */
	private Integer height;
	/**
	 * 最大显示数量
	 */
	private Integer maxNum;
}
