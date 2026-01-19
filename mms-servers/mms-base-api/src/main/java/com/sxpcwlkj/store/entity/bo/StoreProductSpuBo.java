package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreProductSpu;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

/**
* 店铺商品Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreProductSpu.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreProductSpuBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
	/**
	 * 店铺ID
	 */
	private String storeId;
	/**
	 * 分类
	 */
	@NotBlank(message = "分类不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String cateId;
	/**
	 * 品牌ID
	 */
	@NotBlank(message = "品牌不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String brandId;
	/**
	 * 搜索关键字
	 */
	@NotBlank(message = "标题不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String title;
	/**
	 * 副标题
	 */
	private String subTitle;

    /**
     *  单位
     */
    @NotBlank(message = "单位不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String unit;
    /**
     *  市场价格
     */
    @NotNull(message = "市场价格不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private BigDecimal marketPrice;
	/**
	 * 主图
	 */
	@NotBlank(message = "主图不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String mainImage;
	/**
	 * 图片集
	 */
	private String listImages;
	/**
	 * 标签集
	 */
	private String tagIds;
	/**
	 * 商品描述1
	 */
	private String describeOne;
	/**
	 * 商品描述2
	 */
	private String describeTwo;
	/**
	 * 商品描述3
	 */
	private String describeThree;
	/**
	 * 商品描述4
	 */
	private String describeFour;
	/**
	 * 商品描述5
	 */
	private String describeFive;
	/**
	 * 详情介绍
	 */
	private String detailHtml;
    /**
     *  商品SKU组合
     */
    private List<SkuBo> skuList;
}
