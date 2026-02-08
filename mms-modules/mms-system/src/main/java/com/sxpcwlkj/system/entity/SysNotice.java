package com.sxpcwlkj.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 系统公告
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("sys_notice")
@EqualsAndHashCode(callSuper = true)
public class SysNotice  extends BaseEntity {
	/**
	* 公告ID
	*/
	@TableId
	private String id;
	/**
	* 公告标题
	*/
	private String title;
	/**
	* 公告内容
	*/
	private String content;
	/**
	* 公告类型
	*/
	private Integer type;

}
