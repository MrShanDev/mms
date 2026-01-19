package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreAdvertisingSpuCate;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 广告商品分类组合表Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreAdvertisingSpuCate.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreAdvertisingSpuCateBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 广告ID
	 */
	@NotBlank(message = "广告ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String advertisementId;
	/**
	 * 商品分类ID
	 */
	@NotBlank(message = "商品分类ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String spuCateId;
	/**
	 * 商品数量
	 */
	@NotNull(message = "商品数量不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer productNum;
	/**
	 * 组合类型
	 */
	@NotBlank(message = "组合类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String typeCode;
}
