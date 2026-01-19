package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreShipmentItem;
import com.sxpcwlkj.store.entity.bo.StoreShipmentItemBo;
import com.sxpcwlkj.store.entity.export.StoreShipmentItemExport;
import com.sxpcwlkj.store.entity.vo.StoreOrderItemVo;
import com.sxpcwlkj.store.entity.vo.StoreShipmentItemVo;
import com.sxpcwlkj.store.mapper.StoreShipmentItemMapper;
import com.sxpcwlkj.store.service.StoreOrderItemService;
import com.sxpcwlkj.store.service.StoreShipmentItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 发货商品明细表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_shipment_item")
@RequiredArgsConstructor
public class StoreShipmentItemServiceImpl extends BaseServiceImpl<StoreShipmentItem, StoreShipmentItemVo, StoreShipmentItemBo> implements StoreShipmentItemService {

   private final StoreShipmentItemMapper baseMapper;

   private final StoreOrderItemService storeOrderItemService;

    @Override
    public BaseMapperPlus<StoreShipmentItem, StoreShipmentItemVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreShipmentItemBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreShipmentItem obj = MapstructUtil.convert(bo, StoreShipmentItem.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("发货商品明细表,insert 操作失败", e);
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
            log.error("发货商品明细表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreShipmentItemBo bo) {
        try {
            int row;
            StoreShipmentItem obj = MapstructUtil.convert(bo, StoreShipmentItem.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("发货商品明细表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreShipmentItemVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreShipmentItemVo> selectListVoPage(StoreShipmentItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreShipmentItem> lqw = buildQueryWrapper(bo);
        Page<StoreShipmentItemVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreShipmentItem> buildQueryWrapper(StoreShipmentItemBo query){
        if(query==null){
            query=new StoreShipmentItemBo();
        }
        LambdaQueryWrapper<StoreShipmentItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getShipmentId()), StoreShipmentItem::getShipmentId, query.getShipmentId());
        wrapper.eq(StringUtil.isNotEmpty(query.getOrderItemId()), StoreShipmentItem::getOrderItemId, query.getOrderItemId());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreShipmentItemExport> list) {
        return true;
    }

    @Override
    public Boolean orderDelivery(List<String> itemIds,String shipmentId,Integer shippedQuantity,String batchNo) {

        for (String itemId : itemIds) {
            StoreShipmentItemBo bo = new StoreShipmentItemBo();
            bo.setShipmentId(shipmentId);
            StoreOrderItemVo vo = storeOrderItemService.selectVoById(itemId);
            bo.setOrderItemId(itemId);
            bo.setSkuId(vo.getSkuId());
            bo.setSkuName(vo.getSkuName());
            bo.setSkuCode(vo.getSkuCode());
            bo.setOrderQuantity(vo.getQuantity());
//            bo.setShippedQuantity(shippedQuantity);
            bo.setBatchNo(batchNo);
            this.insert(bo);
        }

        return Boolean.TRUE;
    }
}
