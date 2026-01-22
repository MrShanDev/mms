package com.sxpcwlkj.ad.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.ad.entity.StoreAdvertising;
import com.sxpcwlkj.ad.entity.bo.StoreAdvertisingBo;
import com.sxpcwlkj.ad.entity.export.StoreAdvertisingExport;
import com.sxpcwlkj.ad.entity.vo.StoreAdvertisingVo;

import java.util.Set;

/**
 * 广告-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreAdvertisingService extends BaseService<StoreAdvertising, StoreAdvertisingVo, StoreAdvertisingBo> {
    /**
    * 导出广告
    * @param list 广告列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreAdvertisingExport> list);
}
