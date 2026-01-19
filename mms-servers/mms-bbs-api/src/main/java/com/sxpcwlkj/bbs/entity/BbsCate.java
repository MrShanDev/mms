package com.sxpcwlkj.bbs.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 话题分类
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("bbs_cate")
@EqualsAndHashCode(callSuper = true)
public class BbsCate  extends BaseEntity {
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
	* 名称
	*/
	private String name;
	/**
	* 图标
	*/
	private String icon;
}
