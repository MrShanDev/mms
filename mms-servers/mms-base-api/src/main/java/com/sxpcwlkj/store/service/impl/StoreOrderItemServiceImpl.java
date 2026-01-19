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
import com.sxpcwlkj.store.entity.StoreOrderItem;
import com.sxpcwlkj.store.entity.bo.StoreOrderItemBo;
import com.sxpcwlkj.store.entity.export.StoreOrderItemExport;
import com.sxpcwlkj.store.entity.vo.StoreOrderItemVo;
import com.sxpcwlkj.store.entity.vo.StoreOrderSpuVo;
import com.sxpcwlkj.store.mapper.StoreOrderItemMapper;
import com.sxpcwlkj.store.service.StoreOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 订单商品明细表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_order_item")
@RequiredArgsConstructor
public class StoreOrderItemServiceImpl extends BaseServiceImpl<StoreOrderItem, StoreOrderItemVo, StoreOrderItemBo> implements StoreOrderItemService {

   private final StoreOrderItemMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreOrderItem, StoreOrderItemVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreOrderItemBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreOrderItem obj = MapstructUtil.convert(bo, StoreOrderItem.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("订单商品明细表,insert 操作失败", e);
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
            log.error("订单商品明细表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreOrderItemBo bo) {
        try {
            int row;
            StoreOrderItem obj = MapstructUtil.convert(bo, StoreOrderItem.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("订单商品明细表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreOrderItemVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreOrderItemVo> selectListVoPage(StoreOrderItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreOrderItem> lqw = buildQueryWrapper(bo);
        Page<StoreOrderItemVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreOrderItem> buildQueryWrapper(StoreOrderItemBo query){
        if(query==null){
            query=new StoreOrderItemBo();
        }
        LambdaQueryWrapper<StoreOrderItem> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreOrderItemExport> list) {
        return true;
    }

    @Override
    public List<StoreOrderSpuVo> getStoreOrderSpuVoList(String orderId) {
        return baseMapper.getStoreOrderSpuVoList(orderId);
    }
}
