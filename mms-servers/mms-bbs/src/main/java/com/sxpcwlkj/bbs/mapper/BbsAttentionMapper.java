package com.sxpcwlkj.bbs.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.bbs.entity.BbsAttention;
import com.sxpcwlkj.bbs.entity.vo.BbsAttentionVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 关注作者-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface BbsAttentionMapper extends BaseMapperPlus<BbsAttention, BbsAttentionVo> {

}
