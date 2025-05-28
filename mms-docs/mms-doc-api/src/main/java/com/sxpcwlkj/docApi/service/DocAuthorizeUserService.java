package com.sxpcwlkj.docApi.service;

import com.sxpcwlkj.docApi.entity.DocAuthorizeUser;
import com.sxpcwlkj.docApi.entity.bo.DocAuthorizeUserBo;
import com.sxpcwlkj.docApi.entity.vo.DocAuthorizeUserVo;
import com.sxpcwlkj.framework.sercice.BaseService;

import java.util.Set;

/**
 * 文档授权用户-接口
 *
 * @author 西决
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface DocAuthorizeUserService extends BaseService<DocAuthorizeUser, DocAuthorizeUserVo, DocAuthorizeUserBo> {

}
