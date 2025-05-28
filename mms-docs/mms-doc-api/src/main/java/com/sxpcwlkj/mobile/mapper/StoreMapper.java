package com.sxpcwlkj.mobile.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.mobile.entity.Store;
import com.sxpcwlkj.mobile.entity.vo.StoreVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
* 店铺
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-01-26
*/
@Mapper
@Repository
public interface StoreMapper extends BaseMapperPlus<Store, StoreVo> {

    /**
    *  列表分页条件查询
    * @param page  分页构造
    * @param sql   sql条件拼接
    * @return
    */
    Page<StoreVo> listPageXml(@Param("page")Page<Object> page, @Param("sql")String sql);

}
