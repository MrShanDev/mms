package com.sxpcwlkj.member.mapper;


import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.member.entity.StoreMemberAddress;
import com.sxpcwlkj.member.entity.vo.StoreMemberAddressVo;
import org.apache.ibatis.annotations.Mapper;
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
