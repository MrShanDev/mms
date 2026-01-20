package com.sxpcwlkj.bbs.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.bbs.entity.BbsCate;
import com.sxpcwlkj.bbs.entity.vo.BbsCateVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 话题分类-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface BbsCateMapper extends BaseMapperPlus<BbsCate, BbsCateVo> {

}
