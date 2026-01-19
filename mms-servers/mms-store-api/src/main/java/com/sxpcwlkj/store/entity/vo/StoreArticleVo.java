package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreArticle;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 店铺文章Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreArticle.class)
@EqualsAndHashCode(callSuper=false)
public class StoreArticleVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
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
