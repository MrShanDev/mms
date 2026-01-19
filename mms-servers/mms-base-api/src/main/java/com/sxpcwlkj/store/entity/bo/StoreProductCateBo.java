package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreProductCate;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 商品分类Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreProductCate.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreProductCateBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 父ID
	 */
	@NotBlank(message = "父ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String parentId;
	/**
	 * 分类名称
	 */
	@NotBlank(message = "分类名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String name;
	/**
	 * 店铺ID
	 */
	//@NotBlank(message = "店铺ID不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String storeId;
	/**
	 * 分类图标
	 */
	//@NotBlank(message = "分类图标不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String cateIcon;
	/**
	 * 分类背景图片
	 */
	//@NotBlank(message = "分类背景图片不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String cateBgImg;
	/**
	 * 层级
	 */
	//@NotBlank(message = "层级不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String level;
    /**
     *  是否显示在首页
     */
    private Integer pushIndex;
}
