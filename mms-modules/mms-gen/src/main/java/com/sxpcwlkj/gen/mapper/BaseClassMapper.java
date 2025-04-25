package com.sxpcwlkj.gen.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.gen.entity.BaseClassEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基类管理
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface BaseClassMapper extends BaseMapper<BaseClassEntity> {

}
