package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.store.entity.StoreOrder;
import com.sxpcwlkj.store.entity.StoreRefundItem;
import com.sxpcwlkj.store.entity.bo.OrderAfterSalesBo;
import com.sxpcwlkj.store.entity.bo.StoreRefundItemBo;
import com.sxpcwlkj.store.entity.bo.StoreRefundOperationLogBo;
import com.sxpcwlkj.store.enums.AfterSaleOrderStatusEnum;
import com.sxpcwlkj.store.mapper.StoreOrderItemMapper;
import com.sxpcwlkj.store.mapper.StoreOrderMapper;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.store.service.*;
import com.sxpcwlkj.base.utils.OrderNoUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreRefundApply;
import com.sxpcwlkj.store.entity.bo.StoreRefundApplyBo;
import com.sxpcwlkj.store.entity.export.StoreRefundApplyExport;
import com.sxpcwlkj.store.mapper.StoreRefundApplyMapper;
import com.sxpcwlkj.store.entity.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.*;

/**
 * 退款申请表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_refund_apply")
@RequiredArgsConstructor
public class StoreRefundApplyServiceImpl extends BaseServiceImpl<StoreRefundApply, StoreRefundApplyVo, StoreRefundApplyBo> implements StoreRefundApplyService {

   private final StoreRefundApplyMapper baseMapper;
   private final StoreOrderMapper storeOrderMapper;
   private final StoreRefundItemService storeRefundItemService;
   private final StoreOrderItemMapper storeOrderItemMapper;
   private final StoreProductSkuService storeProductSkuService;
   private final StoreProductSpuService storeProductSpuService;
   private final StoreService storeService;
   private final StoreRefundOperationLogService storeRefundOperationLogService;

    @Override
    public BaseMapperPlus<StoreRefundApply, StoreRefundApplyVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreRefundApplyBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreRefundApply obj = MapstructUtil.convert(bo, StoreRefundApply.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("退款申请表,insert 操作失败", e);
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
            log.error("退款申请表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreRefundApplyBo bo) {
        try {
            int row;
            StoreRefundApply obj = MapstructUtil.convert(bo, StoreRefundApply.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("退款申请表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreRefundApplyVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreRefundApplyVo> selectListVoPage(StoreRefundApplyBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreRefundApply> lqw = buildQueryWrapper(bo);
        Page<StoreRefundApplyVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreRefundApply> buildQueryWrapper(StoreRefundApplyBo query){
        if(query==null){
            query=new StoreRefundApplyBo();
        }
        LambdaQueryWrapper<StoreRefundApply> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreRefundApplyExport> list) {
        return true;
    }

    @Override
    public Boolean afterSalesForm(OrderAfterSalesBo bo, StoreOrderItemVo itemVo) {
        StoreOrderVo storeOrderVo =storeOrderMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,bo.getOrderNo()));
        StoreRefundApplyBo newBo = new StoreRefundApplyBo();
        //退款类型（1:仅退款 2:退货退款 3:换货）
        newBo.setRefundType(2);
        //退货退款
        if(bo.getServiceType().equals("RETURN_GOODS")){
            //退款类型（1:仅退款 2:退货退款 3:换货）
            newBo.setRefundType(2);
        }
        //仅退款
        if(bo.getServiceType().equals("RETURN_MONEY")){
            //退款类型（1:仅退款 2:退货退款 3:换货）
            newBo.setRefundType(1);
        }
        newBo.setOrderId(storeOrderVo.getId());
        newBo.setOrderNo(storeOrderVo.getOrderNo());
        newBo.setRefundNo(OrderNoUtil.generate(""));

        newBo.setRefundReason(bo.getReason());
        newBo.setRefundReasonDesc(bo.getProblemDesc());
        newBo.setRefundAmount(storeOrderVo.getPayAmount());
        newBo.setRefundStatus(AfterSaleOrderStatusEnum.WAITING_REVIEW.getCode());
        newBo.setApplyTime(new Date());
        newBo.setApplyBy(LoginObject.getLoginId());
        newBo.setRemark(bo.getImages());
        newBo.setTenantId(storeOrderVo.getTenantId());
        this.insert(newBo);
        List<StoreOrderSpuVo> spuList = storeOrderItemMapper.getStoreOrderSpuVoList(storeOrderVo.getId());
        for(StoreOrderSpuVo spu:spuList){
            if(!spu.getItemId().equals(itemVo.getId())){
                continue;
            }
            StoreRefundItemBo newItemBo = new StoreRefundItemBo();
            newItemBo.setRefundApplyId(newBo.getId());
            newItemBo.setOrderItemId(spu.getItemId());
            newItemBo.setSkuId(spu.getSkuId());
            newItemBo.setSkuName(spu.getSkuName());
            newItemBo.setUnitPrice(spu.getPrice());
            newItemBo.setRefundQuantity(bo.getNum());
            newItemBo.setMainImage(spu.getMainImage());
            newItemBo.setRefundReason(bo.getReason());
            newItemBo.setEvidenceImages(bo.getImages());
            newItemBo.setRefundPrice(spu.getTotalPrice());
            newItemBo.setTenantId(storeOrderVo.getTenantId());
            storeRefundItemService.insert(newItemBo);
        }
        // 售后日志
        StoreRefundOperationLogBo log=new StoreRefundOperationLogBo();
        log.setRefundApplyId(newBo.getId());
        log.setOperationType(AfterSaleOrderStatusEnum.WAITING_REVIEW.getCode());
        log.setOperationDesc("提交售后申请");
        log.setOperatorId(LoginObject.getLoginId());
        log.setOperatorRole(1);
        log.setOperationTime(new Date());
        log.setTenantId(storeOrderVo.getTenantId());
        storeRefundOperationLogService.insert(log);
        return Boolean.TRUE;
    }

    @Override
    public StoreRefundApplyVo selectVoByOrderNo(String orderNo) {
        StoreRefundApplyVo vo =baseMapper.selectVoOne(new LambdaQueryWrapper<StoreRefundApply>()
            .eq(StoreRefundApply::getOrderNo,orderNo)
            .orderByDesc(StoreRefundApply::getCreatedTime)
            .last("LIMIT 1")
        );
        if(vo==null){
            return null;
        }
        List<StoreRefundItemVo> itemVos= storeRefundItemService.selectVoListByLqw(new LambdaQueryWrapper<StoreRefundItem>().eq(StoreRefundItem::getRefundApplyId,vo.getId()));
        vo.setItemVos(itemVos);
        return vo;
    }

    @Override
    public Page<StoreRefundApplyVo> selectListVoPageXml(Integer pageNum, Integer pageSize, String loginId, Integer orderStatus, String keywords) {
        Page<StoreRefundApplyVo> page = new Page<>(pageNum, pageSize);
        if (keywords!=null&& keywords.isEmpty()){
            keywords=null;
        }
        page= baseMapper.selectVoPageAfterSever(page,loginId,orderStatus,keywords);
        for (StoreRefundApplyVo vo:page.getRecords()){
            List<StoreRefundItemVo> itemVos= storeRefundItemService.selectVoListByLqw(new LambdaQueryWrapper<StoreRefundItem>().eq(StoreRefundItem::getRefundApplyId,vo.getId()));
            for(StoreRefundItemVo itemVo:itemVos){
                StoreProductSkuVo skuVo = storeProductSkuService.selectVoById(itemVo.getSkuId());
                if(skuVo!=null){
                    itemVo.setSpuId(skuVo.getSpuId());
                }
                StoreProductSpuVo spuVo = storeProductSpuService.selectVoById(itemVo.getSpuId());
                if(spuVo!=null) {
                    vo.setStoreId(spuVo.getStoreId());
                    StoreVo storeVo = storeService.selectVoById(spuVo.getStoreId());
                    if(storeVo!=null) {
                        vo.setStoreName(storeVo.getStoreName());
                    }
                }
            }
            vo.setItemVos(itemVos);
        }
        return page;
    }

    @Override
    public StoreRefundApplyVo selectVoByRefundNo(String refundNo, String loginId) {
        StoreRefundApplyVo vo = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreRefundApply>().eq(StoreRefundApply::getRefundNo,refundNo));
        List<StoreRefundItemVo> itemVos= storeRefundItemService.selectVoListByLqw(new LambdaQueryWrapper<StoreRefundItem>().eq(StoreRefundItem::getRefundApplyId,vo.getId()));
        vo.setItemVos(itemVos);
        return vo;
    }

    @Override
    public Boolean updateByIdState(String refundNo, int code) {
        return baseMapper.update(null,new LambdaUpdateWrapper<StoreRefundApply>()
            .eq(StoreRefundApply::getRefundNo,refundNo)
            .set(StoreRefundApply::getRefundStatus,code)
            .set(StoreRefundApply::getStatus,2)
        )>0;
    }
}
