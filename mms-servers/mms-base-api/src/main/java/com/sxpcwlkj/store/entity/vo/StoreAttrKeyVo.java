package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreAttrKey;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
* 属性键表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreAttrKey.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAttrKeyVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 属性名
	 */
	private String name;
	/**
	 * 是否是销售属性;0-否,1-是
	 */
	private Integer isSale;
	/**
	 * 输入类型;1-下拉框,2-单行文本,3-多行文本
	 */
	private String inputType;
	/**
	 * 是否必填;0-否,1-是
	 */
	private String isRequired;
    /**
     *  是否图片;0-否,1-是
     */
    private Integer isImage;
    /**
     *  属性值列表
     */
    private List<StoreAttrValueVo> attrValueList;
}
