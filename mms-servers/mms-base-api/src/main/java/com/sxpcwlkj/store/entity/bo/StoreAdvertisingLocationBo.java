package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreAdvertisingLocation;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 广告位Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAdvertisingLocation.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreAdvertisingLocationBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 广告位名称
	 */
	@NotBlank(message = "广告位名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String name;
	/**
	 * 广告位高度
	 */
	@NotBlank(message = "广告位高度不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String height;
	/**
	 * 广告位宽度
	 */
	@NotBlank(message = "广告位宽度不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String width;
	/**
	 * 广告位编码
	 */
	@NotBlank(message = "广告位编码不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String code;
	/**
	 * 最大显示数量
	 */
	@NotNull(message = "最大显示数量不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer maxNum;
}
