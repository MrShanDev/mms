package com.sxpcwlkj.system.entity.bo;

import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.system.entity.SysNotice;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

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
