package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.system.entity.SysTenant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysTenantMapper extends BaseMapper<SysTenant> {
}
