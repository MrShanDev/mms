package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysOss;
import com.sxpcwlkj.system.entity.vo.SysOssVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @Description oss
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Mapper
@Repository
public interface SysOssMapper extends BaseMapperPlus<SysOss, SysOssVo> {
    @InterceptorIgnore(tenantLine = "true")
    Page<SysOssVo> selectByPage(@Param("page") Page<SysOss> page, @Param(Constants.WRAPPER) Wrapper<SysOss> queryWrapper);
}
