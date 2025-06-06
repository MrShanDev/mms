package com.sxpcwlkj.docAdmin.entity.vo;


import java.io.Serial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxpcwlkj.common.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;
import com.sxpcwlkj.docAdmin.entity.DocProduct;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.List;
import java.util.Date;

/**
* 文档商品Vo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = DocProduct.class)
@EqualsAndHashCode(callSuper=false)
public class DocProductVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 产品编号
	 */
	private String prodId;
	/**
	 * 产品名称
	 */
	private String prodName;
	/**
	 * 销售单价
	 */
	private String unitPrice;
	/**
	 * 市场价格
	 */
	private String markPrice;
	/**
	 * 产品类型
	 */
	private String type;
	/**
	 * 创建时间
	 */
	private String ctime;
	/**
	 * 更新时间
	 */
	private String mtime;

}
