package com.sxpcwlkj.docApi.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.docApi.entity.DocProduct;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 文档商品Bo
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = DocProduct.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class DocProductBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 产品编号
	 */
	@NotBlank(message = "产品编号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String prodId;
	/**
	 * 产品名称
	 */
	@NotBlank(message = "产品名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String prodName;
	/**
	 * 销售单价
	 */
	@NotBlank(message = "销售单价不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String unitPrice;
	/**
	 * 市场价格
	 */
	@NotBlank(message = "市场价格不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String markPrice;
	/**
	 * 产品类型
	 */
	@NotBlank(message = "产品类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String type;
	/**
	 * 创建时间
	 */
	@NotBlank(message = "创建时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String ctime;
	/**
	 * 更新时间
	 */
	@NotBlank(message = "更新时间不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String mtime;
}
