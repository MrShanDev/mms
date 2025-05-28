package com.sxpcwlkj.docAdmin.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docAdmin.entity.DocUser;
import com.sxpcwlkj.docAdmin.entity.vo.DocUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 文档用户-Mapper
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface DocUserMapper extends BaseMapperPlus<DocUser, DocUserVo> {

}
