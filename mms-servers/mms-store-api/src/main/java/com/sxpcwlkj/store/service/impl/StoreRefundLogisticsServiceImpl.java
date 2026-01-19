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
import com.sxpcwlkj.store.entity.StoreRefundLogistics;
import com.sxpcwlkj.store.entity.bo.StoreRefundLogisticsBo;
import com.sxpcwlkj.store.entity.bo.StoreRefundOperationLogBo;
import com.sxpcwlkj.store.entity.export.StoreRefundLogisticsExport;
import com.sxpcwlkj.store.entity.vo.StoreRefundLogisticsVo;
import com.sxpcwlkj.store.enums.AfterSaleOrderStatusEnum;
import com.sxpcwlkj.store.mapper.StoreRefundLogisticsMapper;
import com.sxpcwlkj.store.service.StoreRefundApplyService;
import com.sxpcwlkj.store.service.StoreRefundLogisticsService;
import com.sxpcwlkj.store.service.StoreRefundOperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 退货物流信息表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_refund_logistics")
@RequiredArgsConstructor
public class StoreRefundLogisticsServiceImpl extends BaseServiceImpl<StoreRefundLogistics, StoreRefundLogisticsVo, StoreRefundLogisticsBo> implements StoreRefundLogisticsService {

   private final StoreRefundLogisticsMapper baseMapper;

   private final StoreRefundOperationLogService storeRefundOperationLogService;

   private final StoreRefundApplyService storeRefundApplyService;

    @Override
    public BaseMapperPlus<StoreRefundLogistics, StoreRefundLogisticsVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreRefundLogisticsBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreRefundLogistics obj = MapstructUtil.convert(bo, StoreRefundLogistics.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("退货物流信息表,insert 操作失败", e);
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
            log.error("退货物流信息表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreRefundLogisticsBo bo) {
        try {
            int row;
            StoreRefundLogistics obj = MapstructUtil.convert(bo, StoreRefundLogistics.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("退货物流信息表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreRefundLogisticsVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreRefundLogisticsVo> selectListVoPage(StoreRefundLogisticsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreRefundLogistics> lqw = buildQueryWrapper(bo);
        Page<StoreRefundLogisticsVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreRefundLogistics> buildQueryWrapper(StoreRefundLogisticsBo query){
        if(query==null){
            query=new StoreRefundLogisticsBo();
        }
        LambdaQueryWrapper<StoreRefundLogistics> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getRefundApplyId()), StoreRefundLogistics::getRefundApplyId, query.getRefundApplyId());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreRefundLogisticsExport> list) {
        return true;
    }

    @Override
    public Boolean insertXml(StoreRefundLogisticsBo bo, String loginId) {
        bo.setCreatedBy(DataUtil.getLong(loginId));

        //更新售后订单状态
        Boolean bag= storeRefundApplyService.updateByIdState(bo.getRefundNo(),AfterSaleOrderStatusEnum.WAITING_SELLER_RECEIVE.getCode());
        if(!bag){
            return false;
        }
        // 售后日志
        StoreRefundOperationLogBo log=new StoreRefundOperationLogBo();
        log.setRefundApplyId(bo.getRefundApplyId());
        log.setOperationType(AfterSaleOrderStatusEnum.WAITING_SELLER_RECEIVE.getCode());
        log.setOperationDesc("提交退货物流信息");
        log.setOperatorId(loginId);
        log.setOperatorRole(1);
        log.setOperationTime(new Date());
        log.setTenantId(bo.getTenantId());
        storeRefundOperationLogService.insert(log);
        // 记录退货物流信息
        return this.insert(bo);
    }

    @Override
    public StoreRefundLogisticsVo selectVoByrefundApplyId(String id) {
        return baseMapper.selectVoOne(Wrappers.<StoreRefundLogistics>lambdaQuery().eq(StoreRefundLogistics::getRefundApplyId,id)
            .orderByDesc(StoreRefundLogistics::getCreatedTime)
            .last("LIMIT 1")
        );
    }


}
