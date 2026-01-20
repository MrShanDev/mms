package com.sxpcwlkj.bbs.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 话题
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("bbs_topic")
@EqualsAndHashCode(callSuper = true)
public class BbsTopic  extends BaseEntity {
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
	* 分类ID
	*/
	private String cateId;
	/**
	* 标题
	*/
	private String title;
	/**
	* 内容
	*/
	private String contentHtml;
}
