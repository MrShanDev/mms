package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreShipmentSender;
import com.sxpcwlkj.store.entity.vo.StoreShipmentSenderVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 发货人信息-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreShipmentSenderMapper extends BaseMapperPlus<StoreShipmentSender, StoreShipmentSenderVo> {

}
