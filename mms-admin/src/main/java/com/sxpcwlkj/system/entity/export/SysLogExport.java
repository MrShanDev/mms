package com.sxpcwlkj.system.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.system.entity.vo.SysLogVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 操作日志记录表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = SysLogVo.class)
@EqualsAndHashCode(callSuper=false)
public class SysLogExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 日志主键
	 */
	@ExcelIgnore
    @ExcelProperty("日志主键")
	@PrintColumn(title = "日志主键", type = PrintTypeEnum.TEXT)
	private  Long operId;
	/**
	 * 模块名称
	 */
    @ExcelProperty("模块名称")
	@PrintColumn(title = "模块名称", type = PrintTypeEnum.TEXT)
	private  String module;
	/**
	 * 操作类型
	 */
	@Dict("operType")
	@ExcelProperty(value ="操作类型",converter = DictExcelConverter.class)
	@PrintColumn(title = "操作类型", type = PrintTypeEnum.TEXT)
	private  Integer operType;
	/**
	 * 操作描述
	 */
    @ExcelProperty("操作描述")
	@PrintColumn(title = "操作描述", type = PrintTypeEnum.TEXT)
	private  String description;
	/**
	 * 请求方法
	 */
    @ExcelProperty("请求方法")
	@PrintColumn(title = "请求方法", type = PrintTypeEnum.TEXT)
	private  String requestMethod;
	/**
	 * 操作方法(类名.方法名)
	 */
    @ExcelProperty("操作方法(类名.方法名)")
	@PrintColumn(title = "操作方法(类名.方法名)", type = PrintTypeEnum.TEXT)
	private  String method;
	/**
	 * 请求URL
	 */
    @ExcelProperty("请求URL")
	@PrintColumn(title = "请求URL", type = PrintTypeEnum.TEXT)
	private  String operUrl;
	/**
	 * 操作人员ID
	 */
    @ExcelProperty("操作人员ID")
	@PrintColumn(title = "操作人员ID", type = PrintTypeEnum.TEXT)
	private  Long userId;
	/**
	 * 操作人员账号
	 */
    @ExcelProperty("操作人员账号")
	@PrintColumn(title = "操作人员账号", type = PrintTypeEnum.TEXT)
	private  String userName;
	/**
	 * 操作人员角色
	 */
    @ExcelProperty("操作人员角色")
	@PrintColumn(title = "操作人员角色", type = PrintTypeEnum.TEXT)
	private  String userRoles;
	/**
	 * 主机地址
	 */
    @ExcelProperty("主机地址")
	@PrintColumn(title = "主机地址", type = PrintTypeEnum.TEXT)
	private  String operIp;
	/**
	 * 操作地点
	 */
    @ExcelProperty("操作地点")
	@PrintColumn(title = "操作地点", type = PrintTypeEnum.TEXT)
	private  String operLocation;
	/**
	 * 请求参数
	 */
    @ExcelProperty("请求参数")
	@PrintColumn(title = "请求参数", type = PrintTypeEnum.TEXT)
	private  String operParam;
	/**
	 * 操作前数据
	 */
    @ExcelProperty("操作前数据")
	@PrintColumn(title = "操作前数据", type = PrintTypeEnum.TEXT)
	private  String beforeData;
	/**
	 * 返回结果
	 */
    @ExcelProperty("返回结果")
	@PrintColumn(title = "返回结果", type = PrintTypeEnum.TEXT)
	private  String jsonResult;
	/**
	 * 错误消息
	 */
    @ExcelProperty("错误消息")
	@PrintColumn(title = "错误消息", type = PrintTypeEnum.TEXT)
	private  String errorMsg;
	/**
	 * 操作时间
	 */
    @ExcelProperty("操作时间")
	@PrintColumn(title = "操作时间", type = PrintTypeEnum.TEXT)
	private  Date operTime;
	/**
	 * 消耗时间(毫秒)
	 */
    @ExcelProperty("消耗时间(毫秒)")
	@PrintColumn(title = "消耗时间(毫秒)", type = PrintTypeEnum.TEXT)
	private  Long costTime;
	/**
	 * 用户代理
	 */
    @ExcelProperty("用户代理")
	@PrintColumn(title = "用户代理", type = PrintTypeEnum.TEXT)
	private  String userAgent;
	/**
	 * 浏览器类型
	 */
    @ExcelProperty("浏览器类型")
	@PrintColumn(title = "浏览器类型", type = PrintTypeEnum.TEXT)
	private  String browser;
	/**
	 * 操作系统
	 */
    @ExcelProperty("操作系统")
	@PrintColumn(title = "操作系统", type = PrintTypeEnum.TEXT)
	private  String os;
}
