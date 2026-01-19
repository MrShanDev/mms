package com.sxpcwlkj.member.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员分销
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-02-01
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("store_member_distribution")
public class StoreMemberDistribution extends BaseEntity {
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
	* 上级ID
	*/
	private String fatherIdOne;
	/**
	* 上上级ID
	*/
	private String fatherIdTwo;
	/**
	* 上上上级ID
	*/
	private String fatherIdThree;
	/**
	* 推荐注册人ID
	*/
	private String referrerId;
}
