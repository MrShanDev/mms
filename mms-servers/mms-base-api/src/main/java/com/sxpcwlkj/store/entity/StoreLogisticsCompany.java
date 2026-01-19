package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 快递公司名称
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_logistics_company")
@EqualsAndHashCode(callSuper = true)
public class StoreLogisticsCompany  extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 快递公司简称
	*/
	private String code;
	/**
	* 快递公司名称
	*/
	private String name;
	/**
	* 快递公司类型
	*/
	private String type;
	/**
	* 快递公司编号
	*/
	private String number;
}
