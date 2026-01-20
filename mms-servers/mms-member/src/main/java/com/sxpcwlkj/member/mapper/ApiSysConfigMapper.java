package com.sxpcwlkj.member.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.member.entity.ApiSysConfig;
import com.sxpcwlkj.member.entity.ApiSysConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 配置表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface ApiSysConfigMapper extends BaseMapperPlus<ApiSysConfig, ApiSysConfigVo> {

}
