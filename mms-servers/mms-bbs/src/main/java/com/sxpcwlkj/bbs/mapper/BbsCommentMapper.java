package com.sxpcwlkj.bbs.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.bbs.entity.BbsComment;
import com.sxpcwlkj.bbs.entity.vo.BbsCommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 话题评论-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface BbsCommentMapper extends BaseMapperPlus<BbsComment, BbsCommentVo> {

}
