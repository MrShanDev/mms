package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreNavMenu;
import com.sxpcwlkj.store.entity.vo.StoreNavMenuVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 网站导航菜单-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreNavMenuMapper extends BaseMapperPlus<StoreNavMenu, StoreNavMenuVo> {

}
