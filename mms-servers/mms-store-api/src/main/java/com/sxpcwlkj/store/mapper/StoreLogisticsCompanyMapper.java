package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreLogisticsCompany;
import com.sxpcwlkj.store.entity.vo.StoreLogisticsCompanyVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 快递公司名称-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreLogisticsCompanyMapper extends BaseMapperPlus<StoreLogisticsCompany, StoreLogisticsCompanyVo> {

}
