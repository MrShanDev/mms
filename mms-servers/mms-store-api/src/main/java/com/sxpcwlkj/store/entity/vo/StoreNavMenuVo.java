package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreNavMenu;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
* 网站导航菜单Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreNavMenu.class)
@EqualsAndHashCode(callSuper=false)
public class StoreNavMenuVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 名称
	 */
	private String name;
    /**
     * 父ID
     */
    private String parentId;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 路由地址
	 */
	private String routingPath;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 样式
	 */
	private String css;
    private String[] ids;
    List<StoreNavMenuVo> children;
}
