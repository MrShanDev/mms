package com.sxpcwlkj.gen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.gen.entity.TableEntity;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 数据表
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Mapper
@Repository
public interface TableMapper extends BaseMapper<TableEntity> {

}
