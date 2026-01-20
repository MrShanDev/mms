package com.sxpcwlkj.bbs.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 话题操作
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("bbs_leve")
@EqualsAndHashCode(callSuper = true)
public class BbsLeve  extends BaseEntity {
	/**
	* ID
	*/
	private String id;
	/**
	* 话题ID 评论ID
	*/
	private String bbsId;
	/**
	* 会员ID
	*/
	private String memberId;
	/**
	* 类型
	*/
	private Integer type;
}
