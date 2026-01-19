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
import com.sxpcwlkj.store.entity.StoreAdvertisingSpuCate;
import com.sxpcwlkj.store.entity.bo.StoreAdvertisingSpuCateBo;
import com.sxpcwlkj.store.entity.export.StoreAdvertisingSpuCateExport;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingSpuCateVo;
import com.sxpcwlkj.store.entity.vo.StoreAdvertisingVo;
import com.sxpcwlkj.store.entity.vo.StoreProductCateVo;
import com.sxpcwlkj.store.mapper.StoreAdvertisingSpuCateMapper;
import com.sxpcwlkj.store.mapper.StoreProductCateMapper;
import com.sxpcwlkj.store.service.StoreAdvertisingService;
import com.sxpcwlkj.store.service.StoreAdvertisingSpuCateService;
import com.sxpcwlkj.store.service.StoreProductCateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 广告商品分类组合表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_advertising_spu_cate")
@RequiredArgsConstructor
public class StoreAdvertisingSpuCateServiceImpl extends BaseServiceImpl<StoreAdvertisingSpuCate, StoreAdvertisingSpuCateVo, StoreAdvertisingSpuCateBo> implements StoreAdvertisingSpuCateService {

   private final StoreAdvertisingSpuCateMapper baseMapper;
   private final StoreAdvertisingService storeAdvertisingService;
   private final StoreProductCateService storeProductCateService;
    private final StoreProductCateMapper storeProductCateMapper;
    @Override
    public BaseMapperPlus<StoreAdvertisingSpuCate, StoreAdvertisingSpuCateVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreAdvertisingSpuCateBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreAdvertisingSpuCate obj = MapstructUtil.convert(bo, StoreAdvertisingSpuCate.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("广告商品分类组合表,insert 操作失败", e);
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
            log.error("广告商品分类组合表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreAdvertisingSpuCateBo bo) {
        try {
            int row;
            StoreAdvertisingSpuCate obj = MapstructUtil.convert(bo, StoreAdvertisingSpuCate.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("广告商品分类组合表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreAdvertisingSpuCateVo selectVoById(Serializable id) {
        StoreAdvertisingSpuCateVo vo = this.getBaseMapper().selectVoById(id);
        List<String> end= new ArrayList<>();
        getIds(end,vo.getSpuCateId());
        Collections.reverse(end);
        vo.setCateIds(end.toArray(new String[]{}));
        return vo;
    }
    private void getIds(List<String> end, String id) {
        StoreProductCateVo vo = storeProductCateMapper.selectVoById(id);
        if (vo != null) {
            end.add(vo.getId());
            getIds(end, vo.getParentId());
        }
    }
    @Override
    public TableDataInfo<StoreAdvertisingSpuCateVo> selectListVoPage(StoreAdvertisingSpuCateBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAdvertisingSpuCate> lqw = buildQueryWrapper(bo);
        Page<StoreAdvertisingSpuCateVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        List<StoreAdvertisingSpuCateVo> list = new ArrayList<>();
        for (StoreAdvertisingSpuCateVo vo : page.getRecords()) {

            StoreAdvertisingVo storeAdvertising =storeAdvertisingService.selectVoById(vo.getAdvertisementId());
            if (storeAdvertising!=null){
                vo.setAdvertisementName(storeAdvertising.getExtendedParameterTwo());
            }
            StoreProductCateVo storeProductCate = storeProductCateService.selectVoById(vo.getSpuCateId());
            if (storeProductCate!=null){
                vo.setSpuCateName(storeProductCate.getName());
            }

           List<StoreAdvertisingSpuCateVo> vos = baseMapper.selectVoList(new LambdaQueryWrapper<StoreAdvertisingSpuCate>()
                .eq(StoreAdvertisingSpuCate::getAdvertisementId,vo.getAdvertisementId())
                .orderByAsc(StoreAdvertisingSpuCate::getSort)
            );

            for (StoreAdvertisingSpuCateVo v : vos) {
                //TODO 业务处理
                StoreAdvertisingVo storeAdvertising2 =storeAdvertisingService.selectVoById(v.getAdvertisementId());
                if (storeAdvertising2!=null){
                    v.setAdvertisementName(storeAdvertising2.getExtendedParameterTwo());
                }
                StoreProductCateVo storeProductCate2 = storeProductCateService.selectVoById(v.getSpuCateId());
                if (storeProductCate2!=null){
                    v.setSpuCateName(storeProductCate2.getName());
                }
//                list.add(v);
            }
            vo.setSpuCateList(vos);

        }
//        page.setRecords(list);
//        page.setTotal(list.size());
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreAdvertisingSpuCate> buildQueryWrapper(StoreAdvertisingSpuCateBo query){
        if(query==null){
            query=new StoreAdvertisingSpuCateBo();
        }
        LambdaQueryWrapper<StoreAdvertisingSpuCate> wrapper = Wrappers.lambdaQuery();
        wrapper.groupBy(StoreAdvertisingSpuCate::getAdvertisementId);
//        wrapper.orderByAsc(StoreAdvertisingSpuCate::getSort);
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreAdvertisingSpuCateExport> list) {
        return true;
    }
}
