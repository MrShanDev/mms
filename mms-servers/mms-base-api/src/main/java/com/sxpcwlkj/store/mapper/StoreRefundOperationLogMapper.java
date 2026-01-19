package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreRefundOperationLog;
import com.sxpcwlkj.store.entity.vo.StoreRefundOperationLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 退款日志-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreRefundOperationLogMapper extends BaseMapperPlus<StoreRefundOperationLog, StoreRefundOperationLogVo> {

}
