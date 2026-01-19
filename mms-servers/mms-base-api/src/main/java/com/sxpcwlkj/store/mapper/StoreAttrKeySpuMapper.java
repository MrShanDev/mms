package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreAttrKeySpu;
import com.sxpcwlkj.store.entity.vo.StoreAttrKeySpuVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 商品规格项关系表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreAttrKeySpuMapper extends BaseMapperPlus<StoreAttrKeySpu, StoreAttrKeySpuVo> {

}
