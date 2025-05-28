package com.sxpcwlkj.docAdmin.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docAdmin.entity.DocAuthorizeUser;
import com.sxpcwlkj.docAdmin.entity.vo.DocAuthorizeUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 文档授权用户-Mapper
*
* @author 西决
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Mapper
@Repository
public interface DocAuthorizeUserMapper extends BaseMapperPlus<DocAuthorizeUser, DocAuthorizeUserVo> {

}
