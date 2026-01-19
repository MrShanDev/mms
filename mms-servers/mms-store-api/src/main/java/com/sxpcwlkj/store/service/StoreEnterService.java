package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreEnter;
import com.sxpcwlkj.store.entity.bo.StoreEnterBo;
import com.sxpcwlkj.store.entity.export.StoreEnterExport;
import com.sxpcwlkj.store.entity.vo.StoreEnterVo;

import java.util.Set;

/**
 * 店铺入驻-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreEnterService extends BaseService<StoreEnter, StoreEnterVo, StoreEnterBo> {
    /**
    * 导出店铺入驻
    * @param list 店铺入驻列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreEnterExport> list);

    /**
     *  根据会员ID查询店铺入驻信息
     * @param mId  会员ID
     * @return  StoreEnterVo
     */
    StoreEnterVo selectVoByMemberId(String mId);
}
