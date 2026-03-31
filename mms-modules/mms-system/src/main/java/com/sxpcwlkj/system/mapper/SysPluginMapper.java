package com.sxpcwlkj.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.system.entity.SysPlugin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysPluginMapper extends BaseMapper<SysPlugin> {
}
