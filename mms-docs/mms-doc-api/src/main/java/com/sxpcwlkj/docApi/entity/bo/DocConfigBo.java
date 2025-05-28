package com.sxpcwlkj.docApi.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.docApi.entity.DocConfig;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 文档配置Bo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocConfig.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class DocConfigBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * KEY
	 */
	@NotBlank(message = "KEY不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String key;
	/**
	 * 值
	 */
	@NotBlank(message = "值不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String value;
	/**
	 * 创建时间
	 */
	@NotNull(message = "创建时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date ctime;
	/**
	 * 更新时间
	 */
	@NotNull(message = "更新时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date mtime;
}
