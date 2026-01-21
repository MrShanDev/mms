package com.sxpcwlkj.bbs.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关注作者
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("bbs_attention")
@EqualsAndHashCode(callSuper = true)
public class BbsAttention  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 发布者
	*/
	private String memberId;
	/**
	* 被关注者
	*/
	private String attentionId;
}
