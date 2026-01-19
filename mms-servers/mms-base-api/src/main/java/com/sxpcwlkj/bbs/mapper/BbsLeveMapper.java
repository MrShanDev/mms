package com.sxpcwlkj.bbs.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.bbs.entity.BbsLeve;
import com.sxpcwlkj.bbs.entity.vo.BbsLeveVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 话题操作-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface BbsLeveMapper extends BaseMapperPlus<BbsLeve, BbsLeveVo> {

}
