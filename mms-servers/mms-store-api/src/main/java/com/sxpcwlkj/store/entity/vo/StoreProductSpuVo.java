package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreProductSpu;
import com.sxpcwlkj.store.entity.bo.SkuBo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* 店铺商品Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreProductSpu.class)
@EqualsAndHashCode(callSuper=false)
public class StoreProductSpuVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 店铺ID
	 */
	private String storeId;
	/**
	 * 分类
	 */
	private String cateId;
    private String[] cateIds;
	/**
	 * 品牌ID
	 */
	private String brandId;
    private String brandName;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 副标题
	 */
	private String subTitle;
    /**
     *     单位
     */
    @Dict("MALL_UNIT")
    private String unit;
    /**
     *  市场价格
     */
    private BigDecimal marketPrice;
    /**
     * 最低价
     */
    private BigDecimal minPrice;
	/**
	 * 主图
	 */
	private String mainImage;

    /**
     * 图片集
     */
    private String listImages;
	/**
	 * 图片集
	 */
	private List<SpuFileVo> fileList;
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
     *    SKU数据
     */
    private  List<StoreAttrKeyVo> attrValueList;
    /**
     *  SKU数据
     */
    private List<SkuBo> skuList;
    /**
     * 店铺信息
     */
    private Map<String, Object> storeInfo;

    /**
     * 是否自营
     */
    private Boolean ifSelfSupport;

}
