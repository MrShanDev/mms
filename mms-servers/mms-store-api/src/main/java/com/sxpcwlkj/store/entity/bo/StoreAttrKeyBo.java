package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreAttrKey;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 属性键表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAttrKey.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreAttrKeyBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 属性名
	 */
	@NotBlank(message = "属性名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String name;
	/**
	 * 是否是销售属性;0-否,1-是
	 */
	@NotBlank(message = "是否是销售属性不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String isSale;
	/**
	 * 输入类型;1-下拉框,2-单行文本,3-多行文本
	 */
	@NotBlank(message = "输入类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String inputType;
	/**
	 * 是否必填;0-否,1-是
	 */
	@NotBlank(message = "是否必填不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String isRequired;
}
