package com.sxpcwlkj.docAdmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文档配置
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("doc_config")
@EqualsAndHashCode(callSuper = true)
public class DocConfig  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* KEY
	*/
    @TableField("`key`")
	private String key;
	/**
	* 值
	*/
	private String value;
	/**
	* 创建时间
	*/
	private Date ctime;
	/**
	* 更新时间
	*/
	private Date mtime;
}
