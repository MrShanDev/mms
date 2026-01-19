package com.sxpcwlkj.store.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.store.entity.StoreReviewReplies;
import com.sxpcwlkj.store.entity.vo.StoreReviewRepliesVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 评论回复表-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface StoreReviewRepliesMapper extends BaseMapperPlus<StoreReviewReplies, StoreReviewRepliesVo> {

}
