package com.sxpcwlkj.member.entity.bo;


import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.member.entity.StoreMemberAddress;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 会员收件地址;
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-05-11
*/
@EqualsAndHashCode(callSuper=false)
@Data
@AutoMapper(target = StoreMemberAddress.class)
public class StoreMemberAddressBo extends BaseEntity{
	/**
	 * ID
	 */
	private String id;
	/**
	 * 会员ID 可不传后台自动获取
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
	 * 是否默认 0否 1是
	 */
	private String tolerant;
}
