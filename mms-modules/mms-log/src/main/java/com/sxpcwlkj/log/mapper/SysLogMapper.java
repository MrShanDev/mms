package com.sxpcwlkj.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.log.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper
 *
 * @author mmsAdmin
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysOperLog> {

}
