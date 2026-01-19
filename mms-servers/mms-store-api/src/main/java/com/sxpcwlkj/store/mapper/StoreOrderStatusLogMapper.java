package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreOrderStatusLog;
import com.sxpcwlkj.store.entity.vo.StoreOrderStatusLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 订单状态流水表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreOrderStatusLogMapper extends BaseMapperPlus<StoreOrderStatusLog, StoreOrderStatusLogVo> {

}
