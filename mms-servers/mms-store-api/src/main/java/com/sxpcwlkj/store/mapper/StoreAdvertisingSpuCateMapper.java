package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreAdvertisingSpuCate;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingSpuCateVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 广告商品分类组合表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreAdvertisingSpuCateMapper extends BaseMapperPlus<StoreAdvertisingSpuCate, StoreAdvertisingSpuCateVo> {

}
