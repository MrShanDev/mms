package com.sxpcwlkj.member.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
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
@TableName("store_member")
public class StoreMember extends BaseEntity {
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
	* 账号/邮箱
	*/
	private String account;
	/**
	* 性别
	*/
	private Integer sex;
	/**
	* 手机号
	*/
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
	 * 微信openid
	 */
	private String wxOpenid;
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
	 * 纬度
	 */
	private Double latitude;
	/**
	 * 经度
	 */
	private Double longitude;
	/**
	 * 所在城市（根据IP解析）
	 */
	private String city;
	/**
	 * 个性签名
	 */
	private String signature;
	/**
	 * 用户标签（多个标签用逗号分隔）
	 */
	private String tags;
}
