package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreSearchRes;
import com.sxpcwlkj.store.entity.vo.StoreSearchResVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 搜索记录-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreSearchResMapper extends BaseMapperPlus<StoreSearchRes, StoreSearchResVo> {

}
