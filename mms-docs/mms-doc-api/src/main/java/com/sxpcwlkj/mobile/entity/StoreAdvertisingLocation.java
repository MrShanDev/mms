package com.sxpcwlkj.mobile.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
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
@TableName("store_advertising_location")
public class StoreAdvertisingLocation extends BaseEntity {
	/**
	 * ID
	 */
	@TableId
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
}
