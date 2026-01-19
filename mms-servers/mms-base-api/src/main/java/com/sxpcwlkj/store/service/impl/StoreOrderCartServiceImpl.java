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
import com.sxpcwlkj.store.entity.StoreOrderCart;
import com.sxpcwlkj.store.entity.bo.AddCartBo;
import com.sxpcwlkj.store.entity.bo.StoreOrderCartBo;
import com.sxpcwlkj.store.entity.export.StoreOrderCartExport;
import com.sxpcwlkj.store.entity.vo.StoreOrderCartVo;
import com.sxpcwlkj.store.entity.vo.StoreProductSkuVo;
import com.sxpcwlkj.store.entity.vo.StoreProductSpuVo;
import com.sxpcwlkj.store.mapper.StoreOrderCartMapper;
import com.sxpcwlkj.store.service.StoreOrderCartService;
import com.sxpcwlkj.store.service.StoreProductSkuService;
import com.sxpcwlkj.store.service.StoreProductSpuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 购物车表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_order_cart")
@RequiredArgsConstructor
public class StoreOrderCartServiceImpl extends BaseServiceImpl<StoreOrderCart, StoreOrderCartVo, StoreOrderCartBo> implements StoreOrderCartService {

   private final StoreOrderCartMapper baseMapper;
   private final StoreProductSpuService  storeProductSpuService;
   private final StoreProductSkuService storeProductSkuService;

    @Override
    public BaseMapperPlus<StoreOrderCart, StoreOrderCartVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreOrderCartBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreOrderCart obj = MapstructUtil.convert(bo, StoreOrderCart.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("购物车表,insert 操作失败", e);
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
            log.error("购物车表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreOrderCartBo bo) {
        try {
            int row;
            StoreOrderCart obj = MapstructUtil.convert(bo, StoreOrderCart.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("购物车表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreOrderCartVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreOrderCartVo> selectListVoPage(StoreOrderCartBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreOrderCart> lqw = buildQueryWrapper(bo);
        Page<StoreOrderCartVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreOrderCart> buildQueryWrapper(StoreOrderCartBo query){
        if(query==null){
            query=new StoreOrderCartBo();
        }
        LambdaQueryWrapper<StoreOrderCart> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getUserId()), StoreOrderCart::getUserId, query.getUserId());
        wrapper.eq(StringUtil.isNotEmpty(query.getSkuId()), StoreOrderCart::getSkuId, query.getSkuId());
        wrapper.eq(StringUtil.isNotEmpty(query.getSkuCode()), StoreOrderCart::getSkuCode, query.getSkuCode());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreOrderCartExport> list) {
        return true;
    }

    @Override
    public Boolean addCart(AddCartBo bo) {
        //商品下单验证
        StoreProductSkuVo sku = storeProductSkuService.selectByCode(bo.getSkuCode(),bo.getSpuId());
        if(sku==null){
            log.error("商品不存在");
            throw new MmsException("添加购物车，商品规格编码不存在");
        }
        storeProductSpuService.checkOrder(bo.getSpuId(),sku.getId(),bo.getNum());
        StoreProductSpuVo spu = storeProductSpuService.selectVoById(bo.getSpuId());
        StoreOrderCartVo cartVo =  baseMapper.selectVoOne(new  LambdaQueryWrapper<StoreOrderCart>()
            .eq(StoreOrderCart::getSkuCode,bo.getSkuCode())
            .eq(StoreOrderCart::getUserId,bo.getUserId())
            .last("LIMIT 1")
        );
        if(cartVo!=null&&bo.getType()!=1){
            bo.setId(cartVo.getId());
            //购物车重复检查：如果购物车中已存在相同商品，则更新数量。
            //更新购物车数量
            if(bo.getNum()<=0){
              return   baseMapper.deleteById(cartVo.getId())>0;
            }else {
                int num = cartVo.getQuantity();
                if(bo.getType()==3){
                    num = bo.getNum();
                }else {
                    num = num+bo.getNum();
                }
                return  baseMapper.update(new LambdaUpdateWrapper<StoreOrderCart>()
                    .eq(StoreOrderCart::getSkuCode,bo.getSkuCode())
                    .set(StoreOrderCart::getQuantity,num)
                )>0;
            }
        }
        else {
            if(bo.getNum()<=0){
                log.error("商品数量不能小于0");
                throw new MmsException("添加购物车，商品数量不能小于1");
            }
            if(bo.getType()==1){
                baseMapper.delete(new LambdaQueryWrapper<StoreOrderCart>()
                    .eq(StoreOrderCart::getUserId,bo.getUserId())
//                    .eq(StoreOrderCart::getSpuId,bo.getSpuId())
                );
            }
            //新增购物车
            StoreOrderCart cart = new StoreOrderCart();
            cart.setSkuId(sku.getId());
            cart.setSkuCode(sku.getSkuCode());
            cart.setSkuName(sku.getSkuName());
            cart.setPrice(sku.getCostPrice());
            cart.setSpuId(bo.getSpuId());
            cart.setUserId(bo.getUserId());
            cart.setQuantity(bo.getNum());
            cart.setMainImage(spu.getMainImage());
            cart.setSpuName(spu.getTitle());
            cart.setTenantId(spu.getTenantId());
            Boolean b= baseMapper.insert(cart)>0;
            bo.setId(cart.getId());
            return b;
        }
    }

    @Override
    public List<StoreOrderCartVo> getCartList(String loginId) {
        return baseMapper.selectVoList(new LambdaQueryWrapper<StoreOrderCart>()
            .eq(StoreOrderCart::getUserId,loginId)
        );
    }

    @Override
    public Boolean delCart(AddCartBo bo) {
        return baseMapper.delete(new LambdaQueryWrapper<StoreOrderCart>()
            .in(StoreOrderCart::getId,bo.getIds())
            .eq(StoreOrderCart::getUserId,bo.getUserId()))>0;
    }
}
