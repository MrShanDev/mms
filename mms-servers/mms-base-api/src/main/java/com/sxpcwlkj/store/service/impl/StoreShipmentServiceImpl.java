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
import com.sxpcwlkj.store.entity.StoreShipment;
import com.sxpcwlkj.store.entity.bo.StoreShipmentBo;
import com.sxpcwlkj.store.entity.export.StoreShipmentExport;
import com.sxpcwlkj.store.entity.vo.StoreShipmentVo;
import com.sxpcwlkj.store.mapper.StoreShipmentMapper;
import com.sxpcwlkj.store.service.StoreShipmentItemService;
import com.sxpcwlkj.store.service.StoreShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 发货明细-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_shipment")
@RequiredArgsConstructor
public class StoreShipmentServiceImpl extends BaseServiceImpl<StoreShipment, StoreShipmentVo, StoreShipmentBo> implements StoreShipmentService {

   private final StoreShipmentMapper baseMapper;
   private final StoreShipmentItemService storeShipmentItemService;

    @Override
    public BaseMapperPlus<StoreShipment, StoreShipmentVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreShipmentBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreShipment obj = MapstructUtil.convert(bo, StoreShipment.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("发货明细,insert 操作失败", e);
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
            log.error("发货明细,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreShipmentBo bo) {
        try {
            int row;
            StoreShipment obj = MapstructUtil.convert(bo, StoreShipment.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("发货明细,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreShipmentVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreShipmentVo> selectListVoPage(StoreShipmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreShipment> lqw = buildQueryWrapper(bo);
        Page<StoreShipmentVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreShipment> buildQueryWrapper(StoreShipmentBo query){
        if(query==null){
            query=new StoreShipmentBo();
        }
        LambdaQueryWrapper<StoreShipment> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getOrderId()), StoreShipment::getOrderId, query.getOrderId());
        wrapper.eq(StringUtil.isNotEmpty(query.getLogisticsNo()), StoreShipment::getLogisticsNo, query.getLogisticsNo());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreShipmentExport> list) {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean orderDelivery(StoreShipmentBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreShipment obj = MapstructUtil.convert(bo, StoreShipment.class);
            assert obj != null;

            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            // 发货信息完善 发货商品，发货单号ID 发货数量，批次号
            Boolean fag= storeShipmentItemService.orderDelivery(bo.getItemIds(),obj.getId(),0,null);
            return row > 0;
        } catch (Exception e) {
            log.error("发货明细,insert 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreShipmentVo selectVoByOrderId(String orderId) {
        return baseMapper.selectVoOne(Wrappers.<StoreShipment>lambdaQuery().eq(StoreShipment::getOrderId,orderId));
    }
}
