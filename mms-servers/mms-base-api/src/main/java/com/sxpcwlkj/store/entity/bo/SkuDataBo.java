package com.sxpcwlkj.store.entity.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  商品SKI 最终组合的数据
 */
@Data
public class SkuDataBo {
    /**
     * 组合标题
     */
    private String title;
    /**
     *  组合价格
     */
    private BigDecimal price;
    /**
     *   重量
     */
    private String weight;
    /**
     *     库存
     */
    private Integer inventory;
    /**
     *    货号
     */
    private String code;
    /**
     *    sku图片
     */
    private String others;
}
