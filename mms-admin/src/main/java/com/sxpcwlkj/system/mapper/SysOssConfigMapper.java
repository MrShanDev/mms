package com.sxpcwlkj.system.mapper;


import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysOssConfig;
import com.sxpcwlkj.system.entity.vo.SysOssConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
* 对象存储配置表
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-07-07
*/
@Mapper
@Repository
public interface SysOssConfigMapper extends BaseMapperPlus<SysOssConfig, SysOssConfigVo> {

}