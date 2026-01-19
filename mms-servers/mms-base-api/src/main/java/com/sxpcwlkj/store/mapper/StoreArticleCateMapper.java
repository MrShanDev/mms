package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreArticleCate;
import com.sxpcwlkj.store.entity.vo.StoreArticleCateVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 店铺文章分类-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreArticleCateMapper extends BaseMapperPlus<StoreArticleCate, StoreArticleCateVo> {

}
