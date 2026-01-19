package com.sxpcwlkj.store.entity.vo;


import java.io.Serial;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.StoreArticleCate;
import com.sxpcwlkj.framework.entity.BaseEntityVo;

import java.util.List;

/**
* 店铺文章分类Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreArticleCate.class)
@EqualsAndHashCode(callSuper=false)
public class StoreArticleCateVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
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
    private String[] ids;
    List<StoreArticleCateVo> children;

}
