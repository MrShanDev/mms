package com.sxpcwlkj.docAdmin.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.docAdmin.entity.vo.DocOrderVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import java.util.Date;

/**
* 文档订单Export
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocOrderVo.class)
@EqualsAndHashCode(callSuper=false)
public class DocOrderExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 订单编号
	 */
	@ExcelIgnore
    @ExcelProperty("订单编号")
	@PrintColumn(title = "订单编号", type = PrintTypeEnum.TEXT)
	private  String orderId;
	/**
	 * 用户编号
	 */
    @ExcelProperty("用户编号")
	@PrintColumn(title = "用户编号", type = PrintTypeEnum.TEXT)
	private  String uid;
	/**
	 * 订单金额
	 */
    @ExcelProperty("订单金额")
	@PrintColumn(title = "订单金额", type = PrintTypeEnum.TEXT)
	private  BigDecimal txnAmt;
	/**
	 * 支付商户号
	 */
    @ExcelProperty("支付商户号")
	@PrintColumn(title = "支付商户号", type = PrintTypeEnum.TEXT)
	private  String payMchid;
	/**
	 * 支付平台流水号
	 */
    @ExcelProperty("支付平台流水号")
	@PrintColumn(title = "支付平台流水号", type = PrintTypeEnum.TEXT)
	private  String payNo;
	/**
	 * 支付超时时间
	 */
    @ExcelProperty("支付超时时间")
	@PrintColumn(title = "支付超时时间", type = PrintTypeEnum.TEXT)
	private  String payTimeout;
	/**
	 * 产品编号
	 */
    @ExcelProperty("产品编号")
	@PrintColumn(title = "产品编号", type = PrintTypeEnum.TEXT)
	private  String prodId;
	/**
	 * 产品名称
	 */
    @ExcelProperty("产品名称")
	@PrintColumn(title = "产品名称", type = PrintTypeEnum.TEXT)
	private  String prodName;
	/**
	 * 产品价格
	 */
    @ExcelProperty("产品价格")
	@PrintColumn(title = "产品价格", type = PrintTypeEnum.TEXT)
	private  BigDecimal prodPrice;
	/**
	 * 产品类型
	 */
    @ExcelProperty("产品类型")
	@PrintColumn(title = "产品类型", type = PrintTypeEnum.TEXT)
	private  String prodType;
	/**
	 * 创建时间
	 */
    @ExcelProperty("创建时间")
	@PrintColumn(title = "创建时间", type = PrintTypeEnum.TEXT)
	private  String ctime;
	/**
	 * 更新时间
	 */
    @ExcelProperty("更新时间")
	@PrintColumn(title = "更新时间", type = PrintTypeEnum.TEXT)
	private  String mtime;
}
