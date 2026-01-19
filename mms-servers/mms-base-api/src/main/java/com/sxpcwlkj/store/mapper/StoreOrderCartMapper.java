package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreOrderCart;
import com.sxpcwlkj.store.entity.vo.StoreOrderCartVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 购物车表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreOrderCartMapper extends BaseMapperPlus<StoreOrderCart, StoreOrderCartVo> {

}
