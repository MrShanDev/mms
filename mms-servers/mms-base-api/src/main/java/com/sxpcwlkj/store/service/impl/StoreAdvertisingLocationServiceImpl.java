package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreAdvertising;
import com.sxpcwlkj.store.entity.StoreAdvertisingLocation;
import com.sxpcwlkj.store.entity.bo.StoreAdvertisingLocationBo;
import com.sxpcwlkj.store.entity.export.StoreAdvertisingLocationExport;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingLocationVo;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingVo;
import com.sxpcwlkj.store.mapper.StoreAdvertisingLocationMapper;
import com.sxpcwlkj.store.mapper.StoreAdvertisingMapper;
import com.sxpcwlkj.store.service.StoreAdvertisingLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 广告位-接口实现
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreAdvertisingLocationBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreAdvertisingLocation obj = MapstructUtil.convert(bo, StoreAdvertisingLocation.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("广告位,insert 操作失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        try {
            String[] array = DataUtil.getCatStr(ids.toString(), ",");
            return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
        } catch (Exception e) {
            log.error("广告位,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreAdvertisingLocationBo bo) {
        try {
            int row;
            StoreAdvertisingLocation obj = MapstructUtil.convert(bo, StoreAdvertisingLocation.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("广告,updateById 操作失败", e);
            throw e;
        }
    }


    @Override
    public StoreAdvertisingLocationVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreAdvertisingLocationVo> selectListVoPage(StoreAdvertisingLocationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAdvertisingLocation> lqw = buildQueryWrapper(bo);
        Page<StoreAdvertisingLocationVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreAdvertisingLocation> buildQueryWrapper(StoreAdvertisingLocationBo query){
        if(query==null){
            query=new StoreAdvertisingLocationBo();
        }
        LambdaQueryWrapper<StoreAdvertisingLocation> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getName()), StoreAdvertisingLocation::getName, query.getName());
        wrapper.eq(StringUtil.isNotEmpty(query.getCode()), StoreAdvertisingLocation::getCode, query.getCode());
        return wrapper;
    }

    @Override
    public StoreAdvertisingLocationVo selectVoByCode(String advertisingCode) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<StoreAdvertisingLocation>()
            .eq(StoreAdvertisingLocation::getCode,advertisingCode).last("LIMIT 1"));
    }

    @Override
    public List<StoreAdvertisingLocationVo> selectVoList(LambdaQueryWrapper<StoreAdvertisingLocation> storeAdvertisingLocationLambdaQueryWrapper) {
        return baseMapper.selectVoList(storeAdvertisingLocationLambdaQueryWrapper);
    }

    @Override
    public List<StoreAdvertisingVo> selectVoListByCode(String code) {
        return selectVoListByCode(code,100);
    }

    @Override
    public List<StoreAdvertisingVo> selectVoListByCode(String code, Integer size) {
        StoreAdvertisingLocationVo vo = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreAdvertisingLocation>()
            .eq(StoreAdvertisingLocation::getCode,code)
            .eq(StoreAdvertisingLocation::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .last("LIMIT 1"));
        if(vo==null){
            throw new RuntimeException("广告位不存在！");
        }
        if(size<vo.getMaxNum()){
            vo.setMaxNum(size);
        }
        return storeAdvertisingMapper.selectVoList(new LambdaQueryWrapper<StoreAdvertising>()
            .eq(StoreAdvertising::getAdvertisingId,vo.getId())
            .eq(StoreAdvertising::getStatus,SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .le(StoreAdvertising::getStartTime,new Date())
            .ge(StoreAdvertising::getEndTime,new Date())
            .orderByAsc(StoreAdvertising::getSort)
            .last("LIMIT "+vo.getMaxNum()+" OFFSET 0")
        );
    }

    @Override
    public Boolean imports(Set<StoreAdvertisingLocationExport> list) {
        return true;
    }
}
