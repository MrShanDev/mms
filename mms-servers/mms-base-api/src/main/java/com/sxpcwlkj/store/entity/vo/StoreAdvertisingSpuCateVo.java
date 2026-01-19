package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreAdvertisingSpuCate;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
* 广告商品分类组合表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreAdvertisingSpuCate.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAdvertisingSpuCateVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 广告ID
	 */
	private String advertisementId;
    private String advertisementName;
	/**
	 * 商品分类ID
	 */
	private String spuCateId;
    private String spuCateName;
    private String[] cateIds;
	/**
	 * 商品数量
	 */
	private Integer productNum;
	/**
	 * 组合类型
	 */
	private String typeCode;

    private List<StoreAdvertisingSpuCateVo> spuCateList;

}
