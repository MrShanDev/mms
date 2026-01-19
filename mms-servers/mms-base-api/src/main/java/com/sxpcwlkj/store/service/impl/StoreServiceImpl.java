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
import com.sxpcwlkj.store.entity.Store;
import com.sxpcwlkj.store.entity.bo.StoreBo;
import com.sxpcwlkj.store.entity.export.StoreExport;
import com.sxpcwlkj.store.entity.vo.StoreVo;
import com.sxpcwlkj.store.mapper.StoreMapper;
import com.sxpcwlkj.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 店铺-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store")
@RequiredArgsConstructor
public class StoreServiceImpl extends BaseServiceImpl<Store, StoreVo, StoreBo> implements StoreService {

   private final StoreMapper baseMapper;

    @Override
    public BaseMapperPlus<Store, StoreVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreBo bo) {
        try {
            int row;
            //bo.setId(null);
            Store obj = MapstructUtil.convert(bo, Store.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("店铺,insert 操作失败", e);
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
            log.error("店铺,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreBo bo) {
        try {
            int row;
            Store obj = MapstructUtil.convert(bo, Store.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("店铺,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreVo> selectListVoPage(StoreBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Store> lqw = buildQueryWrapper(bo);
        Page<StoreVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<Store> buildQueryWrapper(StoreBo query){
        if(query==null){
            query=new StoreBo();
        }
        LambdaQueryWrapper<Store> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreExport> list) {
        return true;
    }
}
