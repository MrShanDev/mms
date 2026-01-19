package com.sxpcwlkj.store.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * 商品SKU
 */
@Data
public class SkuBo {

    private  String id;

    private List<SkuValuesBo> values;

    private SkuDataBo data;
}
