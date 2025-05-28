package com.sxpcwlkj.mobile.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.sxpcwlkj.framework.sercice.BaseService;
import com.sxpcwlkj.mobile.entity.StoreAdvertisingLocation;
import com.sxpcwlkj.mobile.entity.bo.StoreAdvertisingLocationBo;
import com.sxpcwlkj.mobile.entity.vo.StoreAdvertisingLocationVo;

import java.util.List;

/**
 * 广告位;
 * 支持自定义扩展,已继承接口：insert、deleteById、updateById、selectById、getByEntityListPage（更多查看BaseService接口）
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-05-13
 */
public interface StoreAdvertisingLocationService extends BaseService<StoreAdvertisingLocation, StoreAdvertisingLocationVo, StoreAdvertisingLocationBo> {


    StoreAdvertisingLocationVo selectVoListByCode(String code);

    List<StoreAdvertisingLocationVo> selectVoList(LambdaQueryWrapper<StoreAdvertisingLocation> storeAdvertisingLocationLambdaQueryWrapper);
}
