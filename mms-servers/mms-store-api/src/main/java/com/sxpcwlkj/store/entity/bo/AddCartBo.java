package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 *  添加购物车bo
 */
@Data
public class AddCartBo {

    /**
     *  购物车id
     */
    private String id;
    /**
     *  商品id
     */
    @NotBlank(message = "商品id不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private  String  spuId;

    /**
     * SKU编码
     */
    @NotBlank(message = "SKU编码不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private String skuCode;

    /**
     * 下单数量(当数量为0时，删除该条购物车)
     */
    @NotNull(message = "下单数量不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private  Integer  num;

    /**
     *  用户id
     */
    private String  userId;

    /**
     *   0:加入购物车 1:立即购买 2:购物车数量+ /-
     */
    @NotNull(message = "下单类型不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
    private Integer  type=0;

    @NotNull(message = "购物车id不能为空" ,groups = {ValidatedGroupConfig.del.class})
    private List<String> ids;
}
