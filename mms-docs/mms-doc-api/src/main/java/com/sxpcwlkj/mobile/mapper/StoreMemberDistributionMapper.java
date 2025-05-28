package com.sxpcwlkj.mobile.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.mobile.entity.StoreMemberDistribution;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberDistributionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
* 会员分销
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-02-01
*/
@Mapper
@Repository
public interface StoreMemberDistributionMapper extends BaseMapperPlus<StoreMemberDistribution, StoreMemberDistributionVo> {

    /**
    *  列表分页条件查询
    * @param page  分页构造
    * @param sql   sql条件拼接
    * @return
    */
    Page<StoreMemberDistributionVo> listPageXml(@Param("page")Page<StoreMemberDistribution> page, @Param("sql")String sql);

}
