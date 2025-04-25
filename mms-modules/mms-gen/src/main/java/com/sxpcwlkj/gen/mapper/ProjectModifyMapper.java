package com.sxpcwlkj.gen.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.gen.entity.ProjectModifyEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名变更
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ProjectModifyMapper extends BaseMapper<ProjectModifyEntity> {

}
