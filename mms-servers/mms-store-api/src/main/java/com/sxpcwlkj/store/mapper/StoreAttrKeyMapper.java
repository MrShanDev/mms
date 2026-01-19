package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreAttrKey;
import com.sxpcwlkj.store.entity.vo.StoreAttrKeyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
* 属性键表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreAttrKeyMapper extends BaseMapperPlus<StoreAttrKey, StoreAttrKeyVo> {

    List<StoreAttrKey> selectListSpuId(@Param("spuId") Serializable spuId);
}
