package com.sxpcwlkj.store.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.sxpcwlkj.store.entity.vo.StoreVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
* 店铺Export
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreVo.class)
@EqualsAndHashCode(callSuper=false)
public class StoreExport  extends BaseEntityVo{
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
	 * 店铺类型
	 */
    @ExcelProperty("店铺类型")
	@PrintColumn(title = "店铺类型", type = PrintTypeEnum.TEXT)
	private  String storeTypeId;
	/**
	 * 店铺名称
	 */
    @ExcelProperty("店铺名称")
	@PrintColumn(title = "店铺名称", type = PrintTypeEnum.TEXT)
	private  String storeName;
	/**
	 * 店铺性质
	 */
	@Dict("store_type")
	@ExcelProperty(value ="店铺性质",converter = DictExcelConverter.class)
	@PrintColumn(title = "店铺性质", type = PrintTypeEnum.TEXT)
	private  Integer storeType;
	/**
	 * 营业状态
	 */
	@Dict("business_state")
	@ExcelProperty(value ="营业状态",converter = DictExcelConverter.class)
	@PrintColumn(title = "营业状态", type = PrintTypeEnum.TEXT)
	private  Integer businessState;
	/**
	 * 店铺地址
	 */
    @ExcelProperty("店铺地址")
	@PrintColumn(title = "店铺地址", type = PrintTypeEnum.TEXT)
	private  String storeAddress;
	/**
	 * 店铺经度
	 */
    @ExcelProperty("店铺经度")
	@PrintColumn(title = "店铺经度", type = PrintTypeEnum.TEXT)
	private  String storeLongitude;
	/**
	 * 店铺维度
	 */
    @ExcelProperty("店铺维度")
	@PrintColumn(title = "店铺维度", type = PrintTypeEnum.TEXT)
	private  String storeLatitude;
	/**
	 * 店铺logo
	 */
    @ExcelProperty("店铺logo")
	@PrintColumn(title = "店铺logo", type = PrintTypeEnum.TEXT)
	private  String storeLogo;
	/**
	 * 店铺门头
	 */
    @ExcelProperty("店铺门头")
	@PrintColumn(title = "店铺门头", type = PrintTypeEnum.TEXT)
	private  String storeBgImg;
	/**
	 * 店铺简介
	 */
    @ExcelProperty("店铺简介")
	@PrintColumn(title = "店铺简介", type = PrintTypeEnum.TEXT)
	private  String storeIntroduction;
	/**
	 * 营业时间
	 */
    @ExcelProperty("营业时间")
	@PrintColumn(title = "营业时间", type = PrintTypeEnum.TEXT)
	private  String storeBusinessTime;
	/**
	 * 店铺电话
	 */
    @ExcelProperty("店铺电话")
	@PrintColumn(title = "店铺电话", type = PrintTypeEnum.TEXT)
	private  String storePhone;
	/**
	 * 服务社区
	 */
    @ExcelProperty("服务社区")
	@PrintColumn(title = "服务社区", type = PrintTypeEnum.TEXT)
	private  String storeCommunity;
	/**
	 * 社区描述
	 */
    @ExcelProperty("社区描述")
	@PrintColumn(title = "社区描述", type = PrintTypeEnum.TEXT)
	private  String storeCommunityMark;
	/**
	 * 月销售额
	 */
    @ExcelProperty("月销售额")
	@PrintColumn(title = "月销售额", type = PrintTypeEnum.TEXT)
	private  BigDecimal monthlySales;
	/**
	 * 配送费用
	 */
    @ExcelProperty("配送费用")
	@PrintColumn(title = "配送费用", type = PrintTypeEnum.TEXT)
	private  BigDecimal distributionPrice;
	/**
	 * 店铺星级
	 */
    @ExcelProperty("店铺星级")
	@PrintColumn(title = "店铺星级", type = PrintTypeEnum.TEXT)
	private  String storeRank;
	/**
	 * 店铺评分
	 */
    @ExcelProperty("店铺评分")
	@PrintColumn(title = "店铺评分", type = PrintTypeEnum.TEXT)
	private  String storeScore;
	/**
	 * 起送订单金额
	 */
    @ExcelProperty("起送订单金额")
	@PrintColumn(title = "起送订单金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal startingPrice;
	/**
	 * 审核状态
	 */
	@Dict("store_audit_state")
	@ExcelProperty(value ="审核状态",converter = DictExcelConverter.class)
	@PrintColumn(title = "审核状态", type = PrintTypeEnum.TEXT)
	private  Integer storeAuditState;
	/**
	 * 审核原因
	 */
    @ExcelProperty("审核原因")
	@PrintColumn(title = "审核原因", type = PrintTypeEnum.TEXT)
	private  String storeAuditWhy;
	/**
	 * 店铺备用装修图片1
	 */
    @ExcelProperty("店铺备用装修图片1")
	@PrintColumn(title = "店铺备用装修图片1", type = PrintTypeEnum.TEXT)
	private  String storeImgOne;
	/**
	 * 店铺备用装修图片2
	 */
    @ExcelProperty("店铺备用装修图片2")
	@PrintColumn(title = "店铺备用装修图片2", type = PrintTypeEnum.TEXT)
	private  String storeImgTwo;
	/**
	 * 店铺备用装修图片3
	 */
    @ExcelProperty("店铺备用装修图片3")
	@PrintColumn(title = "店铺备用装修图片3", type = PrintTypeEnum.TEXT)
	private  String storeImgThree;
}
