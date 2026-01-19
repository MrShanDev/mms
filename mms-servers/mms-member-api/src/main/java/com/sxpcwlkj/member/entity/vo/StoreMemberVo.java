package com.sxpcwlkj.member.entity.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.sxpcwlkj.common.annotation.SensitivityEncrypt;
import com.sxpcwlkj.common.enums.SensitivityTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.member.entity.StoreMember;
import com.sxpcwlkj.member.entity.StoreMemberAuthentication;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
* 店铺会员;
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-01-30
*/
@EqualsAndHashCode(callSuper=false)
@Data
@AutoMapper(target = StoreMember.class)
public class StoreMemberVo extends BaseEntityVo {
	/**
	 * ID
	 */
	@TableId
	private String id;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 手机号
	 */
	@SensitivityEncrypt(type = SensitivityTypeEnum.PHONE)
	private String phone;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 头像
	 */
	private String headPortrait;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 微信openid
	 */
	private String wxOpenid;
	/**
	 * 信用分
	 */
	private Integer reputationScore;
	/**
	 * 级别
	 */
	private Integer level;
	/**
	 * 邀请码
	 */
	private String invitationCode;
	/**
	 * 支付密码
	 */
	private String payPassword;
	/**
	 * 秘钥
	 */
	private String privateKey;
	/**
	 * 支付宝OPENID
	 */
	private String alipayOpenid;
	/**
	 * 抖音OPENID
	 */
	private String douyinOpenid;
	/**
	 * 最后登录IP
	 */
	private String lastLoginIp;
	/**
	 * 认证信息
	 */
	private StoreMemberAuthentication authentication;
	/**
	 * Token
	 */
	private String token ;



}
