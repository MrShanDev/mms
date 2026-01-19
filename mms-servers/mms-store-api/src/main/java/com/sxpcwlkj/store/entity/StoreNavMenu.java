package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网站导航菜单
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_nav_menu")
@EqualsAndHashCode(callSuper = true)
public class StoreNavMenu  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
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
}
