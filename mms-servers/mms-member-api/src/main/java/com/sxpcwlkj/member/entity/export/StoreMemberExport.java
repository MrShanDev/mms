package com.sxpcwlkj.member.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
* 会员列表Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreMemberVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreMemberExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ExcelIgnore
    @ExcelProperty("ID")
	@PrintColumn(title = "ID", type = PrintTypeEnum.TEXT)
	private  String id;
	/**
	 * 昵称
	 */
    @ExcelProperty("昵称")
	@PrintColumn(title = "昵称", type = PrintTypeEnum.TEXT)
	private  String nickname;
	/**
	 * 账号
	 */
    @ExcelProperty("账号")
	@PrintColumn(title = "账号", type = PrintTypeEnum.TEXT)
	private  String account;
	/**
	 * 性别
	 */
	@Dict("SYS_SEX")
	@ExcelProperty(value ="性别",converter = DictExcelConverter.class)
	@PrintColumn(title = "性别", type = PrintTypeEnum.TEXT)
	private  Integer sex;
	/**
	 * 手机号
	 */
    @ExcelProperty("手机号")
	@PrintColumn(title = "手机号", type = PrintTypeEnum.TEXT)
	private  String phone;
	/**
	 * 密码
	 */
    @ExcelProperty("密码")
	@PrintColumn(title = "密码", type = PrintTypeEnum.TEXT)
	private  String password;
	/**
	 * 头像
	 */
    @ExcelProperty("头像")
	@PrintColumn(title = "头像", type = PrintTypeEnum.TEXT)
	private  String headPortrait;
	/**
	 * 生日
	 */
    @ExcelProperty("生日")
	@PrintColumn(title = "生日", type = PrintTypeEnum.TEXT)
	private  Date birthday;
	/**
	 * 信用分
	 */
    @ExcelProperty("信用分")
	@PrintColumn(title = "信用分", type = PrintTypeEnum.TEXT)
	private  Integer reputationScore;
	/**
	 * 级别
	 */
    @ExcelProperty("级别")
	@PrintColumn(title = "级别", type = PrintTypeEnum.TEXT)
	private  Integer level;
	/**
	 * 邀请码
	 */
    @ExcelProperty("邀请码")
	@PrintColumn(title = "邀请码", type = PrintTypeEnum.TEXT)
	private  String invitationCode;
	/**
	 * 秘钥
	 */
    @ExcelProperty("秘钥")
	@PrintColumn(title = "秘钥", type = PrintTypeEnum.TEXT)
	private  String privateKey;
	/**
	 * 微信openid
	 */
    @ExcelProperty("微信openid")
	@PrintColumn(title = "微信openid", type = PrintTypeEnum.TEXT)
	private  String wxOpenid;
	/**
	 * 支付宝openId
	 */
    @ExcelProperty("支付宝openId")
	@PrintColumn(title = "支付宝openId", type = PrintTypeEnum.TEXT)
	private  String alipayOpenid;
	/**
	 * 抖音openId
	 */
    @ExcelProperty("抖音openId")
	@PrintColumn(title = "抖音openId", type = PrintTypeEnum.TEXT)
	private  String douyinOpenid;
	/**
	 * 最后登录IP
	 */
    @ExcelProperty("最后登录IP")
	@PrintColumn(title = "最后登录IP", type = PrintTypeEnum.TEXT)
	private  String lastLoginIp;
	/**
	 * 支付密码
	 */
    @ExcelProperty("支付密码")
	@PrintColumn(title = "支付密码", type = PrintTypeEnum.TEXT)
	private  String payPassword;
}
