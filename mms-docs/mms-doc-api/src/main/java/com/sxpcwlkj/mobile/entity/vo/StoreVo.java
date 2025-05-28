package com.sxpcwlkj.mobile.entity.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.mobile.entity.Store;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
* 店铺
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-01-26
*/
@EqualsAndHashCode(callSuper=false)
@Data
@AutoMapper(target = Store.class)
public class StoreVo  {
	/**
	 * ID
	 */
	private String storeId;
	/**
	 * 店铺类型
	 */
	private String storeTypeId;
	/**
	 * 店铺名称
	 */
	private String storeName;
	/**
	 * 店铺类型;1:企业  2:个人
	 */
	private Integer storeType;
	/**
	 * 营业状态;0.禁用 1.营业  2.休业
	 */
	private Integer businessState;
	/**
	 * 店铺地址
	 */
	private String storeAddress;
	/**
	 * 店铺经度
	 */
	private String storeLongitude;
	/**
	 * 店铺维度
	 */
	private String storeLatitude;
	/**
	 * 店铺logo
	 */
	private String storeLogo;
	/**
	 * 店铺门头
	 */
	private String storeBgImg;
	/**
	 * 店铺简介
	 */
	private String storeIntroduction;
	/**
	 * 营业时间
	 */
	private String storeBusinessTime;
	/**
	 * 店铺电话
	 */
	private String storePhone;
	/**
	 * 服务社区
	 */
	private String storeCommunity;
	/**
	 * 社区描述
	 */
	private String storeCommunityMark;
	/**
	 * 月销售额
	 */
	private BigDecimal monthlySales;
	/**
	 * 配送费用
	 */
	private BigDecimal distributionPrice;
	/**
	 * 店铺星级
	 */
	private String storeRank;
	/**
	 * 店铺评分
	 */
	private String storeScore;
	/**
	 * 起送订单金额
	 */
	private BigDecimal startingPrice;
	/**
	 * 审核状态
	 */
	private Integer storeAuditState;
	/**
	 * 审核原因
	 */
	private String storeAuditWhy;
	/**
	 * 店铺备用装修图片1
	 */
	private String storeImgOne;
	/**
	 * 店铺备用装修图片2
	 */
	private String storeImgTwo;
	/**
	 * 店铺备用装修图片3
	 */
	private String storeImgThree;

}
