package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreProductSku;
import com.sxpcwlkj.store.entity.vo.StoreProductSkuVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
* 商品存量价格-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreProductSkuMapper extends BaseMapperPlus<StoreProductSku, StoreProductSkuVo> {

    List<StoreProductSku> selectListSpuId(@Param("spuId") Serializable spuId);
}
