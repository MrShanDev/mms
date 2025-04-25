package com.sxpcwlkj.system.mapper;


import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysConfig;
import com.sxpcwlkj.system.entity.vo.SysConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
* 系统配置
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-06-10
*/
@Mapper
@Repository
public interface SysConfigMapper extends BaseMapperPlus<SysConfig, SysConfigVo> {

}
