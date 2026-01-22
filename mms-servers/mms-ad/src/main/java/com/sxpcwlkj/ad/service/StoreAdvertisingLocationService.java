package com.sxpcwlkj.ad.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.ad.entity.StoreAdvertisingLocation;
import com.sxpcwlkj.ad.entity.bo.StoreAdvertisingLocationBo;
import com.sxpcwlkj.ad.entity.export.StoreAdvertisingLocationExport;
import com.sxpcwlkj.ad.entity.vo.StoreAdvertisingLocationVo;
import com.sxpcwlkj.ad.entity.vo.StoreAdvertisingVo;

import java.util.List;
import java.util.Set;

/**
 * 广告位-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreAdvertisingLocationService extends BaseService<StoreAdvertisingLocation, StoreAdvertisingLocationVo, StoreAdvertisingLocationBo> {
    /**
     * 导出广告位
     * @param list 广告位列表
     * @return true：成功 false ：失败
     */
    Boolean imports(Set<StoreAdvertisingLocationExport> list);
    /**
     * 根据广告位编码查询广告位
     * @param advertisingCode 广告位编码
     * @return 广告位
     */
    StoreAdvertisingLocationVo selectVoByCode(String advertisingCode);

    /**
     *  根据广告位编码查询广告位
     */
    List<StoreAdvertisingLocationVo> selectVoList(LambdaQueryWrapper<StoreAdvertisingLocation> storeAdvertisingLocationLambdaQueryWrapper);

    /**
     *  根据广告位编码查询广告
     * @param code  广告位编码
     * @return  广告列表
     */
    List<StoreAdvertisingVo> selectVoListByCode(String code);

    /**
     *  根据广告位编码查询广告
     * @param code  广告位编码
     * @param size  查询条数
     * @return  广告列表
     */
    List<StoreAdvertisingVo> selectVoListByCode(String code, Integer size);
}
