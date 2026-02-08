package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统配置
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("sys_config")
public class SysConfig extends BaseEntity {
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
	 * 配置类型
	 */
	private Integer configType;
	/**
	* 配置值
	*/
	private String configValue;
	/**
	 * 配置值
	 */
	@TableField(exist = false)
	private List<String> configValues;
	/**
	 * 配置类型
	 */
	@TableField(exist = false)
	private String valueType;
	/**
	 * 序号
	 */
	@TableField(exist = false)
	private Integer index;

}
