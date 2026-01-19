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
import com.sxpcwlkj.store.entity.StoreProductBrand;
import com.sxpcwlkj.store.entity.bo.StoreProductBrandBo;
import com.sxpcwlkj.store.entity.export.StoreProductBrandExport;
import com.sxpcwlkj.store.entity.vo.StoreProductBrandVo;
import com.sxpcwlkj.store.mapper.StoreProductBrandMapper;
import com.sxpcwlkj.store.service.StoreProductBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 商品品牌-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_product_brand")
@RequiredArgsConstructor
public class StoreProductBrandServiceImpl extends BaseServiceImpl<StoreProductBrand, StoreProductBrandVo, StoreProductBrandBo> implements StoreProductBrandService {

   private final StoreProductBrandMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreProductBrand, StoreProductBrandVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreProductBrandBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreProductBrand obj = MapstructUtil.convert(bo, StoreProductBrand.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("商品品牌,insert 操作失败", e);
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
            log.error("商品品牌,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreProductBrandBo bo) {
        try {
            int row;
            StoreProductBrand obj = MapstructUtil.convert(bo, StoreProductBrand.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("商品品牌,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreProductBrandVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreProductBrandVo> selectListVoPage(StoreProductBrandBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreProductBrand> lqw = buildQueryWrapper(bo);
        Page<StoreProductBrandVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreProductBrand> buildQueryWrapper(StoreProductBrandBo query){
        if(query==null){
            query=new StoreProductBrandBo();
        }
        LambdaQueryWrapper<StoreProductBrand> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreProductBrandExport> list) {
        return true;
    }
}
