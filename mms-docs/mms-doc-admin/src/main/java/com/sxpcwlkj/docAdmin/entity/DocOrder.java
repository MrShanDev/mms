package com.sxpcwlkj.docAdmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 文档订单
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("doc_order")
@EqualsAndHashCode(callSuper = true)
public class DocOrder  extends BaseEntity {
	/**
	* 订单编号
	*/
	@TableId
	private String orderId;
	/**
	* 用户编号
	*/
	private String uid;
	/**
	* 订单金额
	*/
	private BigDecimal txnAmt;
	/**
	* 支付商户号
	*/
	private String payMchid;
	/**
	* 支付平台流水号
	*/
	private String payNo;
	/**
	* 支付超时时间
	*/
	private String payTimeout;
	/**
	* 产品编号
	*/
	private String prodId;
	/**
	* 产品名称
	*/
	private String prodName;
	/**
	* 产品价格
	*/
	private BigDecimal prodPrice;
	/**
	* 产品类型
	*/
	private String prodType;
	/**
	* 创建时间
	*/
	private String ctime;
	/**
	* 更新时间
	*/
	private String mtime;
}
