package com.sxpcwlkj.member.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.member.entity.StoreMemberAuthentication;
import com.sxpcwlkj.member.entity.bo.StoreMemberAuthenticationBo;
import com.sxpcwlkj.member.entity.export.StoreMemberAuthenticationExport;
import com.sxpcwlkj.member.entity.vo.StoreMemberAuthenticationVo;

import java.util.List;
import java.util.Set;

/**
 * 会员认证-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreMemberAuthenticationService extends BaseService<StoreMemberAuthentication, StoreMemberAuthenticationVo, StoreMemberAuthenticationBo> {
    /**
     * 导出会员认证
     * @param list 会员认证列表
     * @return true：成功 false ：失败
     */
    Boolean imports(Set<StoreMemberAuthenticationExport> list);
}
