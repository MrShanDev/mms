package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysDictData;
import com.sxpcwlkj.system.entity.vo.SysDictDataVo;
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
public interface SysDictDataMapper extends BaseMapperPlus<SysDictData, SysDictDataVo> {
    @InterceptorIgnore(tenantLine = "true")
    Page<SysDictDataVo> selectByPage(@Param("page") Page<SysDictData> page, @Param(Constants.WRAPPER) Wrapper<SysDictData> queryWrapper);
}
