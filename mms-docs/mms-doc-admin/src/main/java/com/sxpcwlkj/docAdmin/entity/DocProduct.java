package com.sxpcwlkj.docAdmin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxpcwlkj.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文档商品
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Data
@TableName("doc_product")
@EqualsAndHashCode(callSuper = true)
public class DocProduct  extends BaseEntity {
	/**
	* 产品编号
	*/
    @TableId
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
