package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreSearchRes;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 搜索记录Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreSearchRes.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreSearchResBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@NotBlank(message = "不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 *
	 */
	@NotBlank(message = "不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Long memberId;
	/**
	 *
	 */
	@NotBlank(message = "不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String searchKeyword;
	/**
	 *
	 */
	@NotNull(message = "不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer searchNum;
}
