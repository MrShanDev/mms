package com.sxpcwlkj.system.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.system.entity.SysLog;
import com.sxpcwlkj.framework.entity.BaseEntityVo;

import java.util.Date;

/**
* 操作日志记录表Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = SysLog.class)
@EqualsAndHashCode(callSuper=false)
public class SysLogVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 日志主键
	 */
	private Long operId;
	/**
	 * 模块名称
	 */
	private String module;
	/**
	 * 操作类型
	 */
	private Integer operType;
	/**
	 * 操作描述
	 */
	private String description;
	/**
	 * 请求方法
	 */
	private String requestMethod;
	/**
	 * 操作方法(类名.方法名)
	 */
	private String method;
	/**
	 * 请求URL
	 */
	private String operUrl;
	/**
	 * 操作人员ID
	 */
	private Long userId;
	/**
	 * 操作人员账号
	 */
	private String userName;
	/**
	 * 操作人员角色
	 */
	private String userRoles;
	/**
	 * 主机地址
	 */
	private String operIp;
	/**
	 * 操作地点
	 */
	private String operLocation;
	/**
	 * 请求参数
	 */
	private String operParam;
	/**
	 * 操作前数据
	 */
	private String beforeData;
	/**
	 * 返回结果
	 */
	private String jsonResult;
	/**
	 * 错误消息
	 */
	private String errorMsg;
	/**
	 * 操作时间
	 */
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date operTime;
	/**
	 * 消耗时间(毫秒)
	 */
	private Long costTime;
	/**
	 * 用户代理
	 */
	private String userAgent;
	/**
	 * 浏览器类型
	 */
	private String browser;
	/**
	 * 操作系统
	 */
	private String os;

}
