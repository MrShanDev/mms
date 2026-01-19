package com.sxpcwlkj.store.entity.vo;


import cn.hutool.core.util.IdUtil;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreAttrValue;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 属性值表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreAttrValue.class)
@EqualsAndHashCode(callSuper=false)
public class StoreAttrValueVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 属性键ID
	 */
	private String attrKeyId;
	/**
	 * 属性值
	 */
	private String value;
	/**
	 * 颜色值
	 */
	private String color;


    public  StoreAttrValueVo(String attrKeyId) {
        this.attrKeyId = attrKeyId;
        this.id = IdUtil.objectId();
        this.value = "";
        this.color = "";
    }

}
