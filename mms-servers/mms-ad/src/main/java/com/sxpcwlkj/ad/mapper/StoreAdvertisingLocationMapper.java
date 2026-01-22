package com.sxpcwlkj.ad.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.ad.entity.StoreAdvertisingLocation;
import com.sxpcwlkj.ad.entity.vo.StoreAdvertisingLocationVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 广告位-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreAdvertisingLocationMapper extends BaseMapperPlus<StoreAdvertisingLocation, StoreAdvertisingLocationVo> {

}
