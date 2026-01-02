package com.sxpcwlkj.docAdmin.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.docAdmin.entity.DocConfig;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 文档配置Vo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = DocConfig.class)
@EqualsAndHashCode(callSuper=false)
public class DocConfigVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * KEY
	 */
	private String key;
	/**
	 * 值
	 */
	private String value;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date ctime;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date mtime;

}
