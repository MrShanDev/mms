package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreRefundItem;
import com.sxpcwlkj.store.entity.vo.StoreRefundItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 退款商品明细表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreRefundItemMapper extends BaseMapperPlus<StoreRefundItem, StoreRefundItemVo> {

}
