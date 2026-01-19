package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreSearchRes;
import com.sxpcwlkj.store.entity.bo.StoreSearchResBo;
import com.sxpcwlkj.store.entity.export.StoreSearchResExport;
import com.sxpcwlkj.store.entity.vo.StoreSearchResVo;

import java.util.Set;

/**
 * 搜索记录-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreSearchResService extends BaseService<StoreSearchRes, StoreSearchResVo, StoreSearchResBo> {
    /**
    * 导出搜索记录
    * @param list 搜索记录列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreSearchResExport> list);

    /**
     * 添加搜索记录
     * @param keywords 关键字
     * @param userId 用户ID
     */
    void addSearchRes(String keywords, String userId);
}
