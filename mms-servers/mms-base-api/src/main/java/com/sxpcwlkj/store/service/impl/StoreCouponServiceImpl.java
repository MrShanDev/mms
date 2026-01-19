package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreCoupon;
import com.sxpcwlkj.store.entity.StoreCouponProduct;
import com.sxpcwlkj.store.entity.bo.StoreCouponBo;
import com.sxpcwlkj.store.entity.export.StoreCouponExport;
import com.sxpcwlkj.store.entity.vo.StoreCouponVo;
import com.sxpcwlkj.store.mapper.StoreCouponMapper;
import com.sxpcwlkj.store.mapper.StoreCouponProductMapper;
import com.sxpcwlkj.store.service.StoreCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 优惠券管理-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_coupon")
@RequiredArgsConstructor
public class StoreCouponServiceImpl extends BaseServiceImpl<StoreCoupon, StoreCouponVo,StoreCouponBo> implements StoreCouponService {

   private final StoreCouponMapper baseMapper;
   private final StoreCouponProductMapper storeCouponProductMapper;

    @Override
    public BaseMapperPlus<StoreCoupon, StoreCouponVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreCouponBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreCoupon obj = MapstructUtil.convert(bo, StoreCoupon.class);
            assert obj != null;
            List<String> spuIds = bo.getSpuIds();
            if(spuIds!=null){
                assert obj != null;
                storeCouponProductMapper.delete(Wrappers.<StoreCouponProduct>lambdaQuery().eq(StoreCouponProduct::getCouponId,obj.getId()));
                for (String spuId : spuIds) {
                    StoreCouponProduct storeCouponProduct = new StoreCouponProduct();
                    storeCouponProduct.setCouponId(obj.getId());
                    storeCouponProduct.setProductId(spuId);
                    storeCouponProductMapper.insert(storeCouponProduct);
                }
            }
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("优惠券管理,insert 操作失败", e);
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
            log.error("优惠券管理,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreCouponBo bo) {
        try {
            int row;
            StoreCoupon obj = MapstructUtil.convert(bo, StoreCoupon.class);
            List<String> spuIds = bo.getSpuIds();
            if(spuIds!=null){
                assert obj != null;
                storeCouponProductMapper.delete(Wrappers.<StoreCouponProduct>lambdaQuery().eq(StoreCouponProduct::getCouponId,obj.getId()));
                for (String spuId : spuIds) {
                    StoreCouponProduct storeCouponProduct = new StoreCouponProduct();
                    storeCouponProduct.setCouponId(obj.getId());
                    storeCouponProduct.setProductId(spuId);
                    storeCouponProductMapper.insert(storeCouponProduct);
                }
            }
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("优惠券管理,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreCouponVo selectVoById(Serializable id) {
        StoreCouponVo vo= this.getBaseMapper().selectVoById(id);
        if(vo!=null){
            List<StoreCouponProduct> storeCouponProducts = storeCouponProductMapper.selectList(Wrappers.<StoreCouponProduct>lambdaQuery().eq(StoreCouponProduct::getCouponId,vo.getId()));
            List<String> spuIds = new ArrayList<>();
            for (StoreCouponProduct storeCouponProduct : storeCouponProducts) {
                spuIds.add(storeCouponProduct.getProductId());
            }
            vo.setSpuIds(spuIds);
        }
        return vo;

    }
    @Override
    public TableDataInfo<StoreCouponVo> selectListVoPage(StoreCouponBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreCoupon> lqw = buildQueryWrapper(bo);
        Page<StoreCouponVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreCoupon> buildQueryWrapper(StoreCouponBo query){
        if(query==null){
            query=new StoreCouponBo();
        }
        LambdaQueryWrapper<StoreCoupon> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getCouponCode()), StoreCoupon::getCouponCode, query.getCouponCode());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreCouponExport> list) {
        return true;
    }

    @Override
    public StoreCouponVo validateCoupon(String couponId, BigDecimal totalAmount, List<String> productIds) {
        // 1. 查询优惠券
        StoreCouponVo coupon = this.selectVoById(couponId);
        if (coupon == null) {
            throw new MmsException("优惠券不存在");
        }

        // 2. 验证优惠券是否过期
        if (coupon.getExpiryTime() != null && coupon.getExpiryTime().before(new Date())) {
            throw new MmsException("优惠券已过期");
        }

        // 3. 验证使用次数
        if (coupon.getUsageLimit() != null && coupon.getUsedCount() != null) {
            if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
                throw new MmsException("优惠券已达使用上限");
            }
        }

        // 4. 验证最低使用金额
        if (coupon.getMinOrderAmount() != null && totalAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new MmsException("订单金额未达到优惠券最低使用金额：" + coupon.getMinOrderAmount() + "元");
        }

        // 5. 验证优惠券类型：1-通用 2-指定商品
        if (coupon.getCouponType() != null && coupon.getCouponType() == 2) {
            // 指定商品优惠券，需要验证商品是否在适用范围内
            List<String> couponProductIds = storeCouponProductMapper.selectList(
                Wrappers.<StoreCouponProduct>lambdaQuery()
                    .eq(StoreCouponProduct::getCouponId, couponId)
            ).stream().map(StoreCouponProduct::getProductId).collect(Collectors.toList());

            if (couponProductIds.isEmpty()) {
                throw new MmsException("优惠券未配置适用商品");
            }

            // 检查订单中是否有适用商品
            boolean hasApplicableProduct = productIds.stream()
                .anyMatch(couponProductIds::contains);

            if (!hasApplicableProduct) {
                throw new MmsException("订单中没有适用该优惠券的商品");
            }
        }

        // 6. 验证通过，返回优惠券信息
        return coupon;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean useCoupon(String couponId) {
        try {
            // 更新优惠券使用次数
            int rows = baseMapper.update(null, new LambdaUpdateWrapper<StoreCoupon>()
                .eq(StoreCoupon::getId, couponId)
                .setSql("used_count = used_count + 1")
            );
            return rows > 0;
        } catch (Exception e) {
            log.error("使用优惠券失败, couponId: {}", couponId, e);
            throw new MmsException("使用优惠券失败");
        }
    }
}
