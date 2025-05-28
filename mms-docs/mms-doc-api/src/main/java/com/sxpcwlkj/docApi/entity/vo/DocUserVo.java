package com.sxpcwlkj.docApi.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import com.sxpcwlkj.docApi.entity.DocUser;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 文档用户Vo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = DocUser.class)
@EqualsAndHashCode(callSuper=false)
public class DocUserVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	private String uid;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 用户类型
	 */
	private String type;
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

    /**
     * 用户等级到期时间
     */
    private String vip_date;

}
