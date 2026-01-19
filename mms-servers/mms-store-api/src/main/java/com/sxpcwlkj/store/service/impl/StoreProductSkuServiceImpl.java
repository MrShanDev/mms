package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreProductSku;
import com.sxpcwlkj.store.entity.bo.StoreProductSkuBo;
import com.sxpcwlkj.store.entity.export.StoreProductSkuExport;
import com.sxpcwlkj.store.entity.vo.StoreProductSkuVo;
import com.sxpcwlkj.store.mapper.StoreProductSkuMapper;
import com.sxpcwlkj.store.service.StoreProductSkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 商品存量价格-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_product_sku")
@RequiredArgsConstructor
public class StoreProductSkuServiceImpl extends BaseServiceImpl<StoreProductSku, StoreProductSkuVo, StoreProductSkuBo> implements StoreProductSkuService {

   private final StoreProductSkuMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreProductSku, StoreProductSkuVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreProductSkuBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreProductSku obj = MapstructUtil.convert(bo, StoreProductSku.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("商品存量价格,insert 操作失败", e);
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
            log.error("商品存量价格,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreProductSkuBo bo) {
        try {
            int row;
            StoreProductSku obj = MapstructUtil.convert(bo, StoreProductSku.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("商品存量价格,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreProductSkuVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreProductSkuVo> selectListVoPage(StoreProductSkuBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreProductSku> lqw = buildQueryWrapper(bo);
        Page<StoreProductSkuVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreProductSku> buildQueryWrapper(StoreProductSkuBo query){
        if(query==null){
            query=new StoreProductSkuBo();
        }
        LambdaQueryWrapper<StoreProductSku> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreProductSkuExport> list) {
        return true;
    }

    @Override
    public StoreProductSkuVo selectByCode(String code,String spuId) {
        return baseMapper.selectVoOne(new  LambdaQueryWrapper<StoreProductSku>()
            .eq(StoreProductSku::getSkuCode,code)
            .eq(StoreProductSku::getSpuId,spuId)
            .last(SystemCommonEnum.LIMIT_ONE.getCode()));
    }
}
