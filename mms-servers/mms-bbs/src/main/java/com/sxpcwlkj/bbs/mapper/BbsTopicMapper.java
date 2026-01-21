package com.sxpcwlkj.bbs.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.bbs.entity.BbsTopic;
import com.sxpcwlkj.bbs.entity.vo.BbsTopicVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 话题-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface BbsTopicMapper extends BaseMapperPlus<BbsTopic, BbsTopicVo> {

    Page<BbsTopicVo> selectVoPageXml(@Param("page") Page<?> page, 
                                      @Param("cateId") String cateId, 
                                      @Param("keyWord") String keyWord, 
                                      @Param("memberId") String memberId,
                                      @Param("attentionList") List<String> attentionList,
                                      @Param("latitude") Double latitude,
                                      @Param("longitude") Double longitude,
                                      @Param("topicIds") List<String> topicIds);
}
