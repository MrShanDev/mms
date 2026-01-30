package com.sxpcwlkj.member.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.member.entity.StoreToolArea;
import com.sxpcwlkj.member.entity.vo.StoreToolAreaVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 行政区域-Mapper
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Mapper
@Repository
public interface StoreToolAreaMapper extends BaseMapperPlus<StoreToolArea, StoreToolAreaVo> {

}

