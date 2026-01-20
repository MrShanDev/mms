package com.sxpcwlkj.member.entity;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
* 配置表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreSysConfig.class)
@EqualsAndHashCode(callSuper=false)
public class StoreSysConfigVo implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 配置名称
	 */
	private String configName;
	/**
	 * 配置键
	 */
	private String configKey;
	/**
	 * 配置值
	 */
	private String configValue;
	/**
	 * 配置类型;1：系统内置 2：用户自定义
	 */
	private Integer configType;

}
