package com.sxpcwlkj.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("sys_config")
@EqualsAndHashCode(callSuper = true)
public class StoreSysConfig extends BaseEntity {
	/**
	* 主键ID
	*/
	@TableId
	private String id;
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
