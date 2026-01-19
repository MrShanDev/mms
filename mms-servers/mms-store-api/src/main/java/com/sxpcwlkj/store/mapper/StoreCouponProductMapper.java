package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreCouponProduct;
import com.sxpcwlkj.store.entity.vo.StoreCouponProductVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 优惠券适用商品关系表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreCouponProductMapper extends BaseMapperPlus<StoreCouponProduct, StoreCouponProductVo> {

}
