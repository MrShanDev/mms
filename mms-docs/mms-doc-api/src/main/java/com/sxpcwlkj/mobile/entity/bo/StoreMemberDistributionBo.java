package com.sxpcwlkj.mobile.entity.bo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.mobile.entity.StoreMemberDistribution;
import com.sxpcwlkj.datasource.entity.BaseEntity;
/**
* 会员分销
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-02-01
*/
@EqualsAndHashCode(callSuper=false)
@Data
@AutoMapper(target = StoreMemberDistribution.class)
public class StoreMemberDistributionBo extends BaseEntity {
	/**
	 * ID
	 */
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
