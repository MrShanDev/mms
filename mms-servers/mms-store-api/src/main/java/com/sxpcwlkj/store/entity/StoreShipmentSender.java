package com.sxpcwlkj.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发货人信息
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("store_shipment_sender")
@EqualsAndHashCode(callSuper = true)
public class StoreShipmentSender  extends BaseEntity {
	/**
	* 发件人ID
	*/
	@TableId
	private String id;
	/**
	* 发件人姓名
	*/
	private String senderName;
	/**
	* 发件人电话
	*/
	private String senderPhone;
	/**
	* 发件地址
	*/
	private String senderAddress;
	/**
	* 公司名称
	*/
	private String companyName;
	/**
	* 是否默认发件人
	*/
	private Integer isDefault;
}
