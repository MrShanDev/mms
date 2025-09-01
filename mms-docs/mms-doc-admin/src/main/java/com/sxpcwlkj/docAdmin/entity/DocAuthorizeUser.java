package com.sxpcwlkj.docAdmin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文档授权用户
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("doc_authorize_user")
@EqualsAndHashCode(callSuper = true)
public class DocAuthorizeUser  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 用户编号
	*/
	private String uid;
	/**
	* 授权平台
	*/
	private String chan;
	/**
	* 授权平台标识
	*/
	private String appid;
	/**
	* 授权平台用户ID
	*/
	private String openid;
	/**
	* 创建时间
	*/
	private Date ctime;
	/**
	* 更新时间
	*/
	private Date mtime;
}
