package com.sxpcwlkj.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.sercice.BaseService;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import com.sxpcwlkj.mobile.entity.StoreAdvertising;
import com.sxpcwlkj.mobile.entity.bo.StoreAdvertisingBo;
import com.sxpcwlkj.mobile.entity.vo.StoreAdvertisingVo;
import com.sxpcwlkj.mobile.mapper.StoreAdvertisingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;


/**
 * 广告;
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-05-13
 */
@Slf4j
@Service("store_advertising")
@RequiredArgsConstructor
public class StoreAdvertisingServiceImpl extends BaseServiceImpl<StoreAdvertising, StoreAdvertisingVo,StoreAdvertisingBo> implements BaseService<StoreAdvertising, StoreAdvertisingVo, StoreAdvertisingBo> {

   private final StoreAdvertisingMapper baseMapper;


    @Override
    public BaseMapperPlus<StoreAdvertising, StoreAdvertisingVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    public Boolean insert(StoreAdvertisingBo bo) {
        int row = 0;
        StoreAdvertising obj = MapstructUtil.convert(bo, StoreAdvertising.class);
        row = this.getBaseMapper().insert(obj);
        return row > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable id) {
        return this.getBaseMapper().deleteById(id) > 0;
    }

    @Override
    public Boolean updateById(StoreAdvertisingBo bo) {
        int row = 0;
        StoreAdvertising obj = MapstructUtil.convert(bo, StoreAdvertising.class);
        row = this.getBaseMapper().updateById(obj);
        return row > 0;
    }

    @Override
    public StoreAdvertisingVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);
    }


    @Override
    public TableDataInfo<StoreAdvertisingVo> selectListVoPage(StoreAdvertisingBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAdvertising> lqw = buildQueryWrapper(bo);
        Page<StoreAdvertisingVo> page = null;
                //baseMapper.selectPageVo(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }


    private LambdaQueryWrapper<StoreAdvertising> buildQueryWrapper(StoreAdvertisingBo query){
        if(query==null){
            query=new StoreAdvertisingBo();
        }
        LambdaQueryWrapper<StoreAdvertising> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getAdvertisingLocationId()), StoreAdvertising::getAdvertisingId, query.getAdvertisingLocationId());
        wrapper.eq(StringUtil.isNotEmpty(query.getStartTime()), StoreAdvertising::getStartTime, query.getStartTime());
        wrapper.eq(StringUtil.isNotEmpty(query.getEndTime()), StoreAdvertising::getEndTime, query.getEndTime());
        return wrapper;
    }

}
