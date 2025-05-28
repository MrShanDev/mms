package com.sxpcwlkj.mobile.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.mobile.entity.StoreMemberAddress;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberAddressVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
* 会员收件地址;
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-05-11
*/
@Mapper
@Repository
public interface StoreMemberAddressMapper extends BaseMapperPlus<StoreMemberAddress, StoreMemberAddressVo> {

}
