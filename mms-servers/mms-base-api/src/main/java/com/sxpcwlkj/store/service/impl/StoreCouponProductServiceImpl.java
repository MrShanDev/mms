package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreCouponProduct;
import com.sxpcwlkj.store.entity.bo.StoreCouponProductBo;
import com.sxpcwlkj.store.entity.vo.StoreCouponProductVo;
import com.sxpcwlkj.store.entity.export.StoreCouponProductExport;
import com.sxpcwlkj.store.mapper.StoreCouponProductMapper;
import com.sxpcwlkj.store.service.StoreCouponProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券适用商品关系表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_coupon_product")
@RequiredArgsConstructor
public class StoreCouponProductServiceImpl extends BaseServiceImpl<StoreCouponProduct, StoreCouponProductVo,StoreCouponProductBo> implements StoreCouponProductService {

   private final StoreCouponProductMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreCouponProduct, StoreCouponProductVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreCouponProductBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreCouponProduct obj = MapstructUtil.convert(bo, StoreCouponProduct.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("优惠券适用商品关系表,insert 操作失败", e);
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
            log.error("优惠券适用商品关系表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreCouponProductBo bo) {
        try {
            int row;
            StoreCouponProduct obj = MapstructUtil.convert(bo, StoreCouponProduct.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("优惠券适用商品关系表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreCouponProductVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreCouponProductVo> selectListVoPage(StoreCouponProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreCouponProduct> lqw = buildQueryWrapper(bo);
        Page<StoreCouponProductVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreCouponProduct> buildQueryWrapper(StoreCouponProductBo query){
        if(query==null){
            query=new StoreCouponProductBo();
        }
        LambdaQueryWrapper<StoreCouponProduct> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreCouponProductExport> list) {
        return true;
    }
}
