package com.sxpcwlkj.article.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺文章分类
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_article_cate")
@EqualsAndHashCode(callSuper = true)
public class StoreArticleCate  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 父ID
	*/
	private String parentId;
	/**
	* 分类名称
	*/
	private String cateName;
	/**
	* 级别
	*/
	private Integer level;
	/**
	* 图标
	*/
	private String icon;
}
