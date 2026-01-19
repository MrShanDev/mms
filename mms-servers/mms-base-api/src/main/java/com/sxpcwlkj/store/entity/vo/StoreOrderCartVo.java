package com.sxpcwlkj.store.entity.vo;


import java.io.Serial;

import com.sxpcwlkj.store.entity.StoreOrderCart;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.linpeilie.annotations.AutoMapper;

import com.sxpcwlkj.framework.entity.BaseEntityVo;

import java.math.BigDecimal;

/**
 * 购物车表Vo
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */

@Data
@AutoMapper(target = StoreOrderCart.class)
@EqualsAndHashCode(callSuper=false)
public class StoreOrderCartVo  extends BaseEntityVo{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 购物车项ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 商品SKU ID
     */
    private String skuId;
    /**
     * SKU编码
     */
    private String skuCode;
    /**
     * SKU名称
     */
    private String skuName;
    /**
     * SPU ID
     */
    private String spuId;
    /**
     *   SPU名称
     */
    private String spuName;
    /**
     * 商品单价
     */
    private BigDecimal price;
    /**
     * 购买数量
     */
    private Integer quantity;
    /**
     * 是否选中（0:未选中 1:已选中）
     */
    private Integer selected;
    /**
     * 商品主图
     */
    private String mainImage;

}
