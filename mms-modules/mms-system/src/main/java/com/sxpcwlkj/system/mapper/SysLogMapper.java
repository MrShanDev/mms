package com.sxpcwlkj.system.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysLog;
import com.sxpcwlkj.system.entity.vo.SysLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 操作日志记录表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface SysLogMapper extends BaseMapperPlus<SysLog, SysLogVo> {

}
