package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreShipmentItem;
import com.sxpcwlkj.store.entity.vo.StoreShipmentItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 发货商品明细表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreShipmentItemMapper extends BaseMapperPlus<StoreShipmentItem, StoreShipmentItemVo> {

}
