package com.sxpcwlkj.bbs.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 话题评论
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("bbs_comment")
@EqualsAndHashCode(callSuper = true)
public class BbsComment  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 父ID
	*/
	private String fatherId;
	/**
	* 话题ID
	*/
	private String bbsId;
	/**
	* 会员ID
	*/
	private String memberId;
	/**
	* 评论
	*/
	private String comment;
    /**
     * 评论等级
     */
    private Integer level;
}
