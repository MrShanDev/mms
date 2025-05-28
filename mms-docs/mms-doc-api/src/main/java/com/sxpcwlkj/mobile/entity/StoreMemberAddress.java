package com.sxpcwlkj.mobile.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;
import com.sxpcwlkj.datasource.entity.BaseEntity;

/**
 * 会员收件地址;
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-05-11
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("store_member_address")
public class StoreMemberAddress extends BaseEntity {
	/**
	* ID
	*/
	@TableId
	private String id;
	/**
	* 会员ID
	*/
	private String memberId;
	/**
	* 收件人名称
	*/
	private String name;
	/**
	* 收件人手机号
	*/
	private String phone;
	/**
	* 国家
	*/
	private String country;
	/**
	* 省
	*/
	private String province;
	/**
	* 市
	*/
	private String city;
	/**
	* 区/县
	*/
	private String district;
	/**
	* 详细地址
	*/
	private String address;
	/**
	* 是否默认
	*/
	@TableField(value = "is_def")
	private String tolerant;
}
