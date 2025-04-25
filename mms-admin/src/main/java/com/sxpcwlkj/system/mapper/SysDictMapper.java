package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysDict;
import com.sxpcwlkj.system.entity.vo.SysDictVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Mapper
@Repository
public interface SysDictMapper extends BaseMapperPlus<SysDict, SysDictVo> {
    @InterceptorIgnore(tenantLine = "true")
    Page<SysDictVo> selectByPage(@Param("page") Page<SysDict> page, @Param(Constants.WRAPPER) Wrapper<SysDict> queryWrapper);
}
