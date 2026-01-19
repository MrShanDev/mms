package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreReviewReplies;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 评论回复表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreReviewReplies.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreReviewRepliesBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 回复ID，雪花算法
	 */
	@NotBlank(message = "回复ID，雪花算法不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 评论ID
	 */
	@NotBlank(message = "评论ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String reviewId;
	/**
	 * 回复用户ID
	 */
	@NotBlank(message = "回复用户ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String userId;
	/**
	 * 被回复用户ID(针对回复的回复)
	 */
	@NotBlank(message = "被回复用户ID(针对回复的回复)不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String targetUserId;
	/**
	 * 回复内容
	 */
	@NotBlank(message = "回复内容不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String content;
}
