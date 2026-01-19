package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreLogisticsCompany;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 快递公司名称Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreLogisticsCompany.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreLogisticsCompanyBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 快递公司简称
	 */
	@NotBlank(message = "快递公司简称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String code;
	/**
	 * 快递公司名称
	 */
	@NotBlank(message = "快递公司名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String name;
	/**
	 * 快递公司类型
	 */
	@NotBlank(message = "快递公司类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String type;
	/**
	 * 快递公司编号
	 */
	@NotBlank(message = "快递公司编号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String number;
}
