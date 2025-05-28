package com.sxpcwlkj.mobile.mapper;


import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.mobile.entity.StoreAdvertising;
import com.sxpcwlkj.mobile.entity.vo.StoreAdvertisingVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
* 广告;
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-05-13
*/
@Mapper
@Repository
public interface StoreAdvertisingMapper extends BaseMapperPlus<StoreAdvertising, StoreAdvertisingVo> {

}
