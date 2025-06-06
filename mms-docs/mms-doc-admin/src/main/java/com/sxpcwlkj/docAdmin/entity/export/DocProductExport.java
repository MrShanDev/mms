package com.sxpcwlkj.docAdmin.entity.export;

import com.sxpcwlkj.common.annotation.Dict;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.sxpcwlkj.common.annotation.PrintColumn;
import com.sxpcwlkj.common.enums.PrintTypeEnum;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.docAdmin.entity.vo.DocProductVo;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.framework.interceptor.DictExcelConverter;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;

/**
* 文档商品Export
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocProductVo.class)
@EqualsAndHashCode(callSuper=false)
public class DocProductExport  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

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
	 * 销售单价
	 */
    @ExcelProperty("销售单价")
	@PrintColumn(title = "销售单价", type = PrintTypeEnum.TEXT)
	private  String unitPrice;
	/**
	 * 市场价格
	 */
    @ExcelProperty("市场价格")
	@PrintColumn(title = "市场价格", type = PrintTypeEnum.TEXT)
	private  String markPrice;
	/**
	 * 产品类型
	 */
    @ExcelProperty("产品类型")
	@PrintColumn(title = "产品类型", type = PrintTypeEnum.TEXT)
	private  String type;
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
