package com.sxpcwlkj.mobile.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.mobile.entity.StoreMember;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
* 店铺会员;
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-01-30
*/
@Mapper
@Repository
public interface StoreMemberMapper extends BaseMapperPlus<StoreMember, StoreMemberVo> {

    /**
    *  列表分页条件查询
    * @param page  分页构造
    * @param sql   sql条件拼接
    * @return
    */
    Page<StoreMemberVo> listPageXml(@Param("page")Page<StoreMember> page, @Param("sql")String sql);

}
