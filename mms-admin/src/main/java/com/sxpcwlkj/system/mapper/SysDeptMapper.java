package com.sxpcwlkj.system.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysDept;
import com.sxpcwlkj.system.entity.vo.SysDeptVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 系统部门-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface SysDeptMapper extends BaseMapperPlus<SysDept, SysDeptVo> {

}
