package com.sxpcwlkj.system.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.system.entity.SysNotice;
import com.sxpcwlkj.system.entity.vo.SysNoticeVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 系统公告-Mapper
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface SysNoticeMapper extends BaseMapperPlus<SysNotice, SysNoticeVo> {

}
