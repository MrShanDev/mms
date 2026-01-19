package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreShipmentSender;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 发货人信息Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreShipmentSender.class)
@EqualsAndHashCode(callSuper=false)
public class StoreShipmentSenderVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 发件人ID
	 */
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
