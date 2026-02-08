package com.sxpcwlkj.system.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.system.entity.SysLog;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
* 操作日志记录表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = SysLog.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class SysLogBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 日志主键
	 */
	@NotBlank(message = "日志主键不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private Long operId;
	/**
	 * 模块名称
	 */
	@NotBlank(message = "模块名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String module;
	/**
	 * 操作类型
	 */
	@NotNull(message = "操作类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer operType;
	/**
	 * 操作描述
	 */
	@NotBlank(message = "操作描述不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String description;
	/**
	 * 请求方法
	 */
	@NotBlank(message = "请求方法不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String requestMethod;
	/**
	 * 操作方法(类名.方法名)
	 */
	@NotBlank(message = "操作方法(类名.方法名)不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String method;
	/**
	 * 请求URL
	 */
	@NotBlank(message = "请求URL不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String operUrl;
	/**
	 * 操作人员ID
	 */
	@NotBlank(message = "操作人员ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Long userId;
	/**
	 * 操作人员账号
	 */
	@NotBlank(message = "操作人员账号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String userName;
	/**
	 * 操作人员角色
	 */
	@NotBlank(message = "操作人员角色不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String userRoles;
	/**
	 * 主机地址
	 */
	@NotBlank(message = "主机地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String operIp;
	/**
	 * 操作地点
	 */
	@NotBlank(message = "操作地点不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String operLocation;
	/**
	 * 请求参数
	 */
	@NotBlank(message = "请求参数不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String operParam;
	/**
	 * 操作前数据
	 */
	@NotBlank(message = "操作前数据不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String beforeData;
	/**
	 * 返回结果
	 */
	@NotBlank(message = "返回结果不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String jsonResult;
	/**
	 * 错误消息
	 */
	@NotBlank(message = "错误消息不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String errorMsg;
	/**
	 * 操作时间
	 */
	@NotNull(message = "操作时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Date operTime;
	/**
	 * 消耗时间(毫秒)
	 */
	@NotBlank(message = "消耗时间(毫秒)不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Long costTime;
	/**
	 * 用户代理
	 */
	@NotBlank(message = "用户代理不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String userAgent;
	/**
	 * 浏览器类型
	 */
	@NotBlank(message = "浏览器类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String browser;
	/**
	 * 操作系统
	 */
	@NotBlank(message = "操作系统不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String os;
}
