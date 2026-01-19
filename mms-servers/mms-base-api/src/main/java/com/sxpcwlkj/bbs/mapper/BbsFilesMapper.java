package com.sxpcwlkj.bbs.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.bbs.entity.BbsFiles;
import com.sxpcwlkj.bbs.entity.vo.BbsFilesVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 话题附件-Mapper
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface BbsFilesMapper extends BaseMapperPlus<BbsFiles, BbsFilesVo> {

}
