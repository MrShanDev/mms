package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评论回复表
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_review_replies")
@EqualsAndHashCode(callSuper = true)
public class StoreReviewReplies  extends BaseEntity {
	/**
	* 回复ID，雪花算法
	*/
	@TableId
	private String id;
	/**
	* 评论ID
	*/
	private String reviewId;
	/**
	* 回复用户ID
	*/
	private String userId;
	/**
	* 被回复用户ID(针对回复的回复)
	*/
	private String targetUserId;
	/**
	* 回复内容
	*/
	private String content;
}
