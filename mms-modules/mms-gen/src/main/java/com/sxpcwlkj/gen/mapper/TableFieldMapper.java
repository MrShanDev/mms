package com.sxpcwlkj.gen.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.gen.entity.TableFieldEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 表字段
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface TableFieldMapper extends BaseMapper<TableFieldEntity> {

    List<TableFieldEntity> getByTableId(Long tableId);

    void deleteBatchTableIds(Long[] tableIds);
}
