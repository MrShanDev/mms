package com.sxpcwlkj.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象存储配置表
 * @author xijue
 * @Doc mmsadmin.cn
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("sys_oss_config")
public class SysOssConfig extends BaseEntity {
	/**
	* 主建
	*/
	@TableId
	private Long id;
	/**
	* 配置key
	*/
	private String configKey;
	/**
	* accessKey
	*/
	private String accessKey;
	/**
	* 秘钥
	*/
	private String secretKey;
	/**
	* 桶名称
	*/
	private String bucketName;
	/**
	* 前缀
	*/
	private String prefix;
	/**
	* 访问站点
	*/
	private String endpoint;
	/**
	* 自定义域名
	*/
	private String domain;
	/**
	* 是否https
	*/
	private String isHttps;
	/**
	* 域
	*/
	private String region;
	/**
	* 桶权限类型
	*/
	private String accessPolicy;
	/**
	* 扩展字段
	*/
	private String ext1;
}
