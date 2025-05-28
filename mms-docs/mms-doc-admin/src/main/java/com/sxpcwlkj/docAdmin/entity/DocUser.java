package com.sxpcwlkj.docAdmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文档用户
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("doc_user")
@EqualsAndHashCode(callSuper = true)
public class DocUser  extends BaseEntity {
	/**
	* 用户编号
	*/
	@TableId
	private String uid;
	/**
	* 昵称
	*/
	private String nickname;
	/**
	* 头像
	*/
	private String avatar;
	/**
	* 用户类型
	*/
	private String type;
	/**
	* 创建时间
	*/
	private Date ctime;
	/**
	* 更新时间
	*/
	private Date mtime;
}
