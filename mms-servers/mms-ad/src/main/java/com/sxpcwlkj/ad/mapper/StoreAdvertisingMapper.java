package com.sxpcwlkj.ad.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.ad.entity.StoreAdvertising;
import com.sxpcwlkj.ad.entity.vo.StoreAdvertisingVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 广告-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreAdvertisingMapper extends BaseMapperPlus<StoreAdvertising, StoreAdvertisingVo> {


}
