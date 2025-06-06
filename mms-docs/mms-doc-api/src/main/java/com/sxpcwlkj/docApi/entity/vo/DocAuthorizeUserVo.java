package com.sxpcwlkj.docApi.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.docApi.entity.DocAuthorizeUser;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 文档授权用户Vo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = DocAuthorizeUser.class)
@EqualsAndHashCode(callSuper=false)
public class DocAuthorizeUserVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 用户编号
	 */
	private String uid;
	/**
	 * 授权平台
	 */
	private String chan;
	/**
	 * 授权平台标识
	 */
	private String appid;
	/**
	 * 授权平台用户ID
	 */
	private String openid;
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
