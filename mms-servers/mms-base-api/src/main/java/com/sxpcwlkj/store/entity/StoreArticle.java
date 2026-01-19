package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺文章
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_article")
@EqualsAndHashCode(callSuper = true)
public class StoreArticle  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 文章标题
	*/
	private String title;
	/**
	* 封面图片
	*/
	private String coverImg;
	/**
	* 标签
	*/
	private String tag;
	/**
	* 作者
	*/
	private String author;
	/**
	* 分类ID
	*/
	private String articleCateId;
	/**
	* 内容
	*/
	private String content;
    /**
    * 会员ID
    */
    private String memberId;
}
