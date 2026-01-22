package com.sxpcwlkj.ad.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.ad.entity.StoreAdvertising;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 广告Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAdvertising.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreAdvertisingBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 广告位ID
	 */
	@NotBlank(message = "广告位ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
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
