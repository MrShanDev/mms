package com.sxpcwlkj.store.entity.vo;


import java.io.Serial;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.store.entity.Store;
import com.sxpcwlkj.framework.entity.BaseEntityVo;

import java.math.BigDecimal;

/**
 * 店铺Vo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */

@Data
@AutoMapper(target = Store.class)
@EqualsAndHashCode(callSuper=false)
public class StoreVo  extends BaseEntityVo{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;
    /**
     * 店铺类型
     */
    private String storeTypeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺性质
     */
    private Integer storeType;
    /**
     * 营业状态
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
