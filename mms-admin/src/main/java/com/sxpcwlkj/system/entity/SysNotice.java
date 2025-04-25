package com.sxpcwlkj.system.entity;



import lombok.Data;
import lombok.EqualsAndHashCode;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;


/**
 * 系统公告
 *
 * @author 西决
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
