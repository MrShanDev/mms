package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreShipmentSender;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 发货人信息Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreShipmentSender.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreShipmentSenderBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 发件人ID
	 */
	@NotBlank(message = "发件人ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 发件人姓名
	 */
	@NotBlank(message = "发件人姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderName;
	/**
	 * 发件人电话
	 */
	@NotBlank(message = "发件人电话不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderPhone;
	/**
	 * 发件地址
	 */
	@NotBlank(message = "发件地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String senderAddress;
	/**
	 * 公司名称
	 */
	@NotBlank(message = "公司名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String companyName;
	/**
	 * 是否默认发件人
	 */
	@NotBlank(message = "是否默认发件人不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer isDefault;
}
