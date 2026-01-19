package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.Store;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 店铺Bo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@AutoMapper(target = Store.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreBo  extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
    private String id;

    private String memberId;
    /**
     * 店铺类型
     */
    //@NotBlank(message = "店铺类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeTypeId;
    /**
     * 店铺名称
     */
    @NotBlank(message = "店铺名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeName;
    /**
     * 店铺性质
     */
    //@NotNull(message = "店铺性质不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer storeType;
    /**
     * 营业状态
     */
    @NotNull(message = "营业状态不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer businessState;
    /**
     * 店铺地址
     */
    @NotBlank(message = "店铺地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeAddress;
    /**
     * 店铺经度
     */
    //@NotBlank(message = "店铺经度不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeLongitude;
    /**
     * 店铺维度
     */
    //@NotBlank(message = "店铺维度不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeLatitude;
    /**
     * 店铺logo
     */
    @NotBlank(message = "店铺logo不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeLogo;
    /**
     * 店铺门头
     */
    //@NotBlank(message = "店铺门头不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeBgImg;
    /**
     * 店铺简介
     */
    @NotBlank(message = "店铺简介不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeIntroduction;
    /**
     * 营业时间
     */
    //@NotBlank(message = "营业时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeBusinessTime;
    /**
     * 店铺电话
     */
    //@NotBlank(message = "店铺电话不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storePhone;
    /**
     * 服务社区
     */
    //@NotBlank(message = "服务社区不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeCommunity;
    /**
     * 社区描述
     */
    //@NotBlank(message = "社区描述不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeCommunityMark;
    /**
     * 月销售额
     */
    //@NotNull(message = "月销售额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private BigDecimal monthlySales;
    /**
     * 配送费用
     */
    //@NotNull(message = "配送费用不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private BigDecimal distributionPrice;
    /**
     * 店铺星级
     */
    //@NotBlank(message = "店铺星级不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeRank;
    /**
     * 店铺评分
     */
    //@NotBlank(message = "店铺评分不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeScore;
    /**
     * 起送订单金额
     */
    //@NotNull(message = "起送订单金额不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private BigDecimal startingPrice;
    /**
     * 审核状态
     */
    //@NotNull(message = "审核状态不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer storeAuditState;
    /**
     * 审核原因
     */
    //@NotBlank(message = "审核原因不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeAuditWhy;
    /**
     * 店铺备用装修图片1
     */
    //@NotBlank(message = "店铺备用装修图片1不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeImgOne;
    /**
     * 店铺备用装修图片2
     */
    //@NotBlank(message = "店铺备用装修图片2不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeImgTwo;
    /**
     * 店铺备用装修图片3
     */
    //@NotBlank(message = "店铺备用装修图片3不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String storeImgThree;
}
