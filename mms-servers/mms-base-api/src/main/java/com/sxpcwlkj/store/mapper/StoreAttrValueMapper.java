package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreAttrValue;
import com.sxpcwlkj.store.entity.vo.StoreAttrValueVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
* 属性值表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreAttrValueMapper extends BaseMapperPlus<StoreAttrValue, StoreAttrValueVo> {

    List<StoreAttrValue> selectListSpuId(@Param("spuId") Serializable keyId, @Param("attrKeyId") String attrKeyId);
}
