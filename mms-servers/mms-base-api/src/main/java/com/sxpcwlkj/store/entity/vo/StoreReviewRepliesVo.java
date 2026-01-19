package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreReviewReplies;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 评论回复表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreReviewReplies.class)
@EqualsAndHashCode(callSuper=false)
public class StoreReviewRepliesVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 回复ID，雪花算法
	 */
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
