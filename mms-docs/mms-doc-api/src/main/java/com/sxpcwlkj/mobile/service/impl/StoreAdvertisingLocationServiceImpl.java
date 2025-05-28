package com.sxpcwlkj.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import com.sxpcwlkj.mobile.entity.StoreAdvertising;
import com.sxpcwlkj.mobile.entity.StoreAdvertisingLocation;
import com.sxpcwlkj.mobile.entity.bo.StoreAdvertisingLocationBo;
import com.sxpcwlkj.mobile.entity.vo.StoreAdvertisingLocationVo;
import com.sxpcwlkj.mobile.entity.vo.StoreAdvertisingVo;
import com.sxpcwlkj.mobile.mapper.StoreAdvertisingLocationMapper;
import com.sxpcwlkj.mobile.mapper.StoreAdvertisingMapper;
import com.sxpcwlkj.mobile.service.StoreAdvertisingLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 广告位;
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-05-13
 */
@Slf4j
@Service("store_advertising_location")
@RequiredArgsConstructor
public class StoreAdvertisingLocationServiceImpl extends BaseServiceImpl<StoreAdvertisingLocation, StoreAdvertisingLocationVo, StoreAdvertisingLocationBo> implements StoreAdvertisingLocationService {

    private final StoreAdvertisingLocationMapper baseMapper;
    private final StoreAdvertisingMapper storeAdvertisingMapper;


    @Override
    public BaseMapperPlus<StoreAdvertisingLocation, StoreAdvertisingLocationVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    public Boolean insert(StoreAdvertisingLocationBo bo) {
        int row = 0;
        StoreAdvertisingLocation obj = MapstructUtil.convert(bo, StoreAdvertisingLocation.class);
        row = this.getBaseMapper().insert(obj);
        return row > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable id) {
        return this.getBaseMapper().deleteById(id) > 0;
    }

    @Override
    public Boolean updateById(StoreAdvertisingLocationBo bo) {
        int row = 0;
        StoreAdvertisingLocation obj = MapstructUtil.convert(bo, StoreAdvertisingLocation.class);
        row = this.getBaseMapper().updateById(obj);
        return row > 0;
    }

    @Override
    public StoreAdvertisingLocationVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);
    }


    @Override
    public TableDataInfo<StoreAdvertisingLocationVo> selectListVoPage(StoreAdvertisingLocationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAdvertisingLocation> lqw = buildQueryWrapper(bo);
        Page<StoreAdvertisingLocationVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    private LambdaQueryWrapper<StoreAdvertisingLocation> buildQueryWrapper(StoreAdvertisingLocationBo query) {
        if (query == null) {
            query = new StoreAdvertisingLocationBo();
        }
        //Map<String, Object> params = query.getParams();
        LambdaQueryWrapper<StoreAdvertisingLocation> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public StoreAdvertisingLocationVo selectVoListByCode(String code) {
        StoreAdvertisingLocationVo vo = null;
        if (StringUtil.isNotBlank(code)) {
            LambdaQueryWrapper<StoreAdvertisingLocation> lqw = Wrappers.lambdaQuery();
            lqw.eq(StoreAdvertisingLocation::getCode, code);
            vo = this.getBaseMapper().selectVoOne(lqw);
            if (vo != null) {
               List<StoreAdvertisingVo> ads= storeAdvertisingMapper.selectVoList(new LambdaQueryWrapper<StoreAdvertising>()
                        .eq(StoreAdvertising::getAdvertisingId, vo.getId())
                        .eq(StoreAdvertising::getStatus, 0)
                        .le(StoreAdvertising::getStartTime, new Date())
                        .ge(StoreAdvertising::getEndTime, new Date())
                        .orderByDesc(StoreAdvertising::getSort)
                        .last("limit "+vo.getMaxNum())
                );
               vo.setStoreAdvertisingList(ads);
            }
        }
        return vo;
    }

    @Override
    public List<StoreAdvertisingLocationVo> selectVoList(LambdaQueryWrapper<StoreAdvertisingLocation> storeAdvertisingLocationLambdaQueryWrapper) {
        return selectVoList(storeAdvertisingLocationLambdaQueryWrapper);
    }
}
