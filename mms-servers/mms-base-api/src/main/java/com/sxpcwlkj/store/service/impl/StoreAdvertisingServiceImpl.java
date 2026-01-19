package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreAdvertising;
import com.sxpcwlkj.store.entity.StoreAdvertisingLocation;
import com.sxpcwlkj.store.entity.bo.StoreAdvertisingBo;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingVo;
import com.sxpcwlkj.store.entity.export.StoreAdvertisingExport;
import com.sxpcwlkj.store.mapper.StoreAdvertisingLocationMapper;
import com.sxpcwlkj.store.mapper.StoreAdvertisingMapper;
import com.sxpcwlkj.store.service.StoreAdvertisingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * 广告-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_advertising")
@RequiredArgsConstructor
public class StoreAdvertisingServiceImpl extends BaseServiceImpl<StoreAdvertising, StoreAdvertisingVo, StoreAdvertisingBo> implements StoreAdvertisingService {

   private final StoreAdvertisingMapper baseMapper;
   private final StoreAdvertisingLocationMapper storeAdvertisingLocationMapper;

    @Override
    public BaseMapperPlus<StoreAdvertising, StoreAdvertisingVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreAdvertisingBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreAdvertising obj = MapstructUtil.convert(bo, StoreAdvertising.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("广告,insert 操作失败", e);
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
            log.error("广告,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreAdvertisingBo bo) {
        try {
            int row;
            StoreAdvertising obj = MapstructUtil.convert(bo, StoreAdvertising.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("广告,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreAdvertisingVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreAdvertisingVo> selectListVoPage(StoreAdvertisingBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAdvertising> lqw = buildQueryWrapper(bo);
        Page<StoreAdvertisingVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        for (StoreAdvertisingVo vo : page.getRecords()) {
            StoreAdvertisingLocation storeAdvertisingLocation= storeAdvertisingLocationMapper.selectById(vo.getAdvertisingId());
            vo.setAdvertisingName(storeAdvertisingLocation!=null?storeAdvertisingLocation.getName():"");
        }
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreAdvertising> buildQueryWrapper(StoreAdvertisingBo query){
        if(query==null){
            query=new StoreAdvertisingBo();
        }
        LambdaQueryWrapper<StoreAdvertising> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getAdvertisingId()), StoreAdvertising::getAdvertisingId, query.getAdvertisingId());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreAdvertisingExport> list) {
        return true;
    }
}
