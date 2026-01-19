package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreOrderItem;
import com.sxpcwlkj.store.entity.vo.StoreOrderItemVo;
import com.sxpcwlkj.store.entity.vo.StoreOrderSpuVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 订单商品明细表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreOrderItemMapper extends BaseMapperPlus<StoreOrderItem, StoreOrderItemVo> {
    /**
     * 根据订单编号获取订单商品明细表
     * @param orderId 订单编号
     * @return List<StoreOrderSpuVo>
     */
    List<StoreOrderSpuVo> getStoreOrderSpuVoList(@Param("orderId") String orderId);
}
