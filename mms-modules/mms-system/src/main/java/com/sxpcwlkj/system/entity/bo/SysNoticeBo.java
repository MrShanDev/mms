package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.system.entity.SysNotice;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 系统公告Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = SysNotice.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class SysNoticeBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 公告ID
	 */
    @NotBlank(message = "公告ID不能为空")
	private String id;

	/**
	 * 公告标题
	 */
    @NotBlank(message = "公告标题不能为空")
	private String title;

	/**
	 * 公告内容
	 */
    @NotBlank(message = "公告内容不能为空")
	private String content;

	/**
	 * 公告类型
	 */
    @NotNull(message = "公告类型不能为空")
	private Integer type;

}
