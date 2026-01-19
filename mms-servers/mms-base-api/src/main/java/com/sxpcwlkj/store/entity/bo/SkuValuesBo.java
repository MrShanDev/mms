package com.sxpcwlkj.store.entity.bo;

import lombok.Data;

/**
 * 商品SKU 组合值
 */
@Data
public class SkuValuesBo {
    /**
     *    suk项ID
     */
    private  String keyId;
    /**
     *    suk值ID
     */
    private String valueId;
    /**
     *    suk项名称
     */
    private  String keyName;
    /**
     *    suk值名称
     */
    private String valueName;
    /**
     *     suk项图片
     */
    private String color;
}
