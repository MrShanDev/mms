package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreType;
import com.sxpcwlkj.store.entity.bo.StoreTypeBo;
import com.sxpcwlkj.store.entity.export.StoreTypeExport;
import com.sxpcwlkj.store.entity.vo.StoreTypeVo;
import com.sxpcwlkj.store.mapper.StoreTypeMapper;
import com.sxpcwlkj.store.service.StoreTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 店铺类型-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_type")
@RequiredArgsConstructor
public class StoreTypeServiceImpl extends BaseServiceImpl<StoreType, StoreTypeVo, StoreTypeBo> implements StoreTypeService {

   private final StoreTypeMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreType, StoreTypeVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreTypeBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreType obj = MapstructUtil.convert(bo, StoreType.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("店铺类型,insert 操作失败", e);
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
            log.error("店铺类型,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreTypeBo bo) {
        try {
            int row;
            StoreType obj = MapstructUtil.convert(bo, StoreType.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("店铺类型,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreTypeVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreTypeVo> selectListVoPage(StoreTypeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreType> lqw = buildQueryWrapper(bo);
        Page<StoreTypeVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreType> buildQueryWrapper(StoreTypeBo query){
        if(query==null){
            query=new StoreTypeBo();
        }
        LambdaQueryWrapper<StoreType> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreTypeExport> list) {
        return true;
    }
}
