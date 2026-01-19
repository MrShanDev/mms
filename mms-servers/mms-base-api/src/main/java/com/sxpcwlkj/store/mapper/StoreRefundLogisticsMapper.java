package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreRefundLogistics;
import com.sxpcwlkj.store.entity.vo.StoreRefundLogisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 退货物流信息表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreRefundLogisticsMapper extends BaseMapperPlus<StoreRefundLogistics, StoreRefundLogisticsVo> {

}
