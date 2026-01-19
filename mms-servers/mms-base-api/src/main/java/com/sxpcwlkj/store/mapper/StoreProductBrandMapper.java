package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreProductBrand;
import com.sxpcwlkj.store.entity.vo.StoreProductBrandVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 商品品牌-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreProductBrandMapper extends BaseMapperPlus<StoreProductBrand, StoreProductBrandVo> {

}
