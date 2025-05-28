package com.sxpcwlkj.docAdmin.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.docAdmin.entity.DocOrder;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;

/**
* 文档订单Vo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = DocOrder.class)
@EqualsAndHashCode(callSuper=false)
public class DocOrderVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 订单编号
	 */
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
