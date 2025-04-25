package com.sxpcwlkj.framework.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.entity.SysSign;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 系统加签
 * @author sxpcwlkj
 */
@Mapper
@Repository
public interface SysSignMapper extends BaseMapperPlus<SysSign,SysSign> {

}
