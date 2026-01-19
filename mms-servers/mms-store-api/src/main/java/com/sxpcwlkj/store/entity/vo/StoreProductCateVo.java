package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreProductCate;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
* 商品分类Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreProductCate.class)
@EqualsAndHashCode(callSuper=false)
public class StoreProductCateVo  extends BaseEntityVo{
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
	private String name;
	/**
	 * 店铺ID
	 */
	private String storeId;
	/**
	 * 分类图标
	 */
	private String cateIcon;
	/**
	 * 分类背景图片
	 */
	private String cateBgImg;
	/**
	 * 层级
	 */
	private Integer level;

    private String[] ids;
    private List<StoreProductCateVo> children;

    private List<StoreProductSpuVo>  supList;

}
