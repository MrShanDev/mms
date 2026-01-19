package com.sxpcwlkj.store.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.ijpay.core.enums.TradeType;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.member.entity.vo.StoreMemberAddressVo;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.member.service.StoreMemberAddressService;
import com.sxpcwlkj.member.service.StoreMemberService;
import com.sxpcwlkj.store.enums.AfterSaleOrderStatusEnum;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.*;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.export.StoreOrderExport;
import com.sxpcwlkj.store.enums.PaymentTypeEnum;
import com.sxpcwlkj.store.enums.StoreOrderStatusEnum;
import com.sxpcwlkj.store.mapper.StoreInvoiceMapper;
import com.sxpcwlkj.store.mapper.StoreOrderCartMapper;
import com.sxpcwlkj.store.mapper.StoreOrderMapper;

import com.sxpcwlkj.base.utils.KdniaoAPIUtils;
import com.sxpcwlkj.base.utils.OrderNoUtil;
import com.sxpcwlkj.store.entity.*;
import com.sxpcwlkj.store.entity.bo.*;
import com.sxpcwlkj.store.entity.vo.*;
import com.sxpcwlkj.store.service.*;
import com.sxpcwlkj.wx.service.WxOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 订单主表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_order")
@RequiredArgsConstructor
public class StoreOrderServiceImpl extends BaseServiceImpl<StoreOrder, StoreOrderVo, StoreOrderBo> implements StoreOrderService {

    private final StoreOrderMapper baseMapper;
    private final StoreOrderCartMapper storeOrderCartMapper;
    private final StoreProductSpuService storeProductSpuService;
    private final StoreProductSkuService storeProductSkuService;
    private final StoreOrderItemService storeOrderItemService;
    private final StoreOrderStatusLogService storeOrderStatusLogService;
    private final WxOrderService wxOrderService;
    private final StoreMemberService storeMemberService;
    private final StoreMemberAddressService storeMemberAddressService;
    private final StoreService storeService;
    private final StoreShipmentService storeShipmentService;
    private final StoreRefundApplyService storeRefundApplyService;
    private final StoreRefundOperationLogService storeRefundOperationLogService;
    private final StoreRefundLogisticsService storeRefundLogisticsService;
    private final StoreLogisticsCompanyService storeLogisticsCompanyService;
    private final StoreInvoiceMapper storeInvoiceMapper;
    private final StoreCouponService storeCouponService;


    @Override
    public BaseMapperPlus<StoreOrder, StoreOrderVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreOrderBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreOrder obj = MapstructUtil.convert(bo, StoreOrder.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("订单主表,insert 操作失败", e);
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
            log.error("订单主表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreOrderBo bo) {
        try {
            int row;
            StoreOrder obj = MapstructUtil.convert(bo, StoreOrder.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("订单主表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreOrderVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreOrderVo> selectListVoPage(StoreOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreOrder> lqw = buildQueryWrapper(bo);
        if (!StringUtil.isNotEmpty(bo.getStatus())){
            lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.NONE.getCode());
//            lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.EXPIRED.getCode());
//            lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.CANCELLED.getCode());
//            lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.CLOSED.getCode());
        }
        Page<StoreOrderVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        for(StoreOrderVo vo : page.getRecords()){
            List<StoreOrderSpuVo> spuList = storeOrderItemService.getStoreOrderSpuVoList(vo.getId());
            vo.setSpuList(spuList);
            vo.setStartTime(vo.getCreatedTime().getTime());
            vo.setEndTime(vo.getCreatedTime().getTime()+30 * 60 * 1000);
            vo.setStoreOrderStateVo(new StoreOrderStateVo(vo.getStatus()));
            StoreMemberVo memberVo = storeMemberService.selectVoById(vo.getUserId());
            if(memberVo!=null) {
                if(StringUtil.isNotEmpty(memberVo.getNickname())){
                    vo.setNickname(memberVo.getNickname());
                }else {
                    vo.setNickname("暂无昵称");
                }
            }
        }
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<StoreOrderVo> selectListVoPageAdmin(StoreOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreOrder> lqw = buildQueryWrapper(bo);
        if (!StringUtil.isNotEmpty(bo.getStatus())){
            lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.NONE.getCode());
            lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.EXPIRED.getCode());
            lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.CANCELLED.getCode());
            lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.CLOSED.getCode());
        }
        Page<StoreOrderVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        for(StoreOrderVo vo : page.getRecords()){
            List<StoreOrderSpuVo> spuList = storeOrderItemService.getStoreOrderSpuVoList(vo.getId());
            vo.setSpuList(spuList);
            vo.setStartTime(vo.getCreatedTime().getTime());
            vo.setEndTime(vo.getCreatedTime().getTime()+30 * 60 * 1000);
            vo.setStoreOrderStateVo(new StoreOrderStateVo(vo.getStatus()));
            StoreMemberVo memberVo = storeMemberService.selectVoById(vo.getUserId());
            if(memberVo!=null) {
                if(StringUtil.isNotEmpty(memberVo.getNickname())){
                    vo.setNickname(memberVo.getNickname());
                }else {
                    vo.setNickname("暂无昵称");
                }
            }
        }
        return TableDataInfo.build(page);
    }


    private LambdaQueryWrapper<StoreOrder> buildQueryWrapper(StoreOrderBo query){
        if(query==null){
            query=new StoreOrderBo();
        }
        LambdaQueryWrapper<StoreOrder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getOrderNo()), StoreOrder::getOrderNo, query.getOrderNo());

        if(StringUtil.isNotEmpty(query.getUserId())){
            if (ValidatorUtil.isMobile(query.getUserId())){
                StoreMemberVo memberVo =  storeMemberService.selectVoByPhone(query.getUserId());
                if(memberVo!=null){
                    wrapper.eq(StoreOrder::getUserId, memberVo.getId());
                }else {
                    wrapper.eq(StoreOrder::getUserId, -1);
                }
            }else {
                wrapper.eq(StoreOrder::getUserId, query.getUserId());
            }
        }
        wrapper.eq(StringUtil.isNotEmpty(query.getPayType()), StoreOrder::getPayType, query.getPayType());
        wrapper.eq(StringUtil.isNotEmpty(query.getStatus()),StoreOrder::getStatus, query.getStatus());
        wrapper.orderByDesc(StoreOrder::getCreatedTime);

        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreOrderExport> list) {
        return true;
    }

    @Override
    public String createOrder(AddOrderBo bo) {

        //验证商品是否满足下单条件；
        List<String> cartIds = bo.getCardIds();
        String tenantId=null;
        List<String> spuIds = new ArrayList<>();
        for (String id : cartIds) {
            StoreOrderCartVo cart = storeOrderCartMapper.selectVoById(id);
            if (cart == null) {
                throw new RuntimeException("购物车商品已不存在");
            }
            if (tenantId != null && !tenantId.equals(cart.getTenantId())) {
                throw new RuntimeException("存在多个店铺的商品,请分开下单!");
            }
            tenantId = cart.getTenantId();
            spuIds.add(cart.getSpuId());
            storeProductSpuService.checkOrder(cart.getSpuId(), cart.getSkuId(), cart.getQuantity());
        }

         //商品总价
         BigDecimal  totalPrice = new BigDecimal(0);
         //计算运费
         BigDecimal freight= new BigDecimal(0);
         if(bo.getDeliveryType()!=1){
             //不进行物流，运费
             freight= new BigDecimal(0);
         }
         //计算优惠金额
         BigDecimal discountAmount= new BigDecimal(0);
         //计算应付金额
         BigDecimal payAmount= new BigDecimal(0);
         //计算保险费
         BigDecimal insuranceAmount= new BigDecimal(0);

        for (String cartId : cartIds) {
            StoreOrderCartVo cartVo = storeOrderCartMapper.selectVoById(cartId);
            //商品单价
            StoreProductSkuVo sku = storeProductSkuService.selectVoById(cartVo.getSkuId());
             //商品总价
            totalPrice = totalPrice.add(sku.getCostPrice().multiply(new BigDecimal(cartVo.getQuantity())));
        }
         //运费
         freight = new BigDecimal(0);

         // 验证和使用优惠券
         StoreCouponVo couponVo = null;
         if (StringUtil.isNotEmpty(bo.getCouponId())) {
             // 验证优惠券
             couponVo = storeCouponService.validateCoupon(bo.getCouponId(), totalPrice, spuIds);
             // 计算优惠金额（优惠券面额）
             discountAmount = couponVo.getFaceValue();
             // 防止优惠金额超过订单总价
             if (discountAmount.compareTo(totalPrice) > 0) {
                 discountAmount = totalPrice;
             }
         } else {
             //优惠金额
             discountAmount = new BigDecimal(0);
         }

         //应付金额
         payAmount = totalPrice.add(freight).add(insuranceAmount).subtract(discountAmount);

         //判断bo中的金额是否与计算金额一致
         if(!(bo.getTotalAmount().compareTo(totalPrice)==0)){
             throw new RuntimeException("商品总价与计算金额不一致");
         }
         if(!(bo.getFreightAmount().compareTo(freight)==0)){
             throw new RuntimeException("运费与计算运费不一致");
         }
         if(!(bo.getDiscountAmount().compareTo(discountAmount)==0)){
             throw new RuntimeException("优惠金额与计算优惠金额不一致");
         }
         if(!(bo.getPayAmount().compareTo(payAmount)==0)){
             throw new RuntimeException("应付金额与计算应付金额不一致");
         }

        //生成订单号
         String orderNo = OrderNoUtil.generate("");

         StoreOrder order = new StoreOrder();
         order.setOrderNo(orderNo);
         order.setUserId(bo.getUserId());
         // 商品总价
         order.setTotalAmount(bo.getTotalAmount());
         // 保险费
         order.setInsuranceAmount(new BigDecimal(0));
         // 优惠金额
         order.setDiscountAmount(bo.getDiscountAmount());
         // 运费
         order.setFreightAmount(bo.getFreightAmount());
         // 订单最终费用
         order.setOrderEndPrice(bo.getPayAmount());
         // 代付款
         order.setStatus(StoreOrderStatusEnum.WAITING_PAYMENT.getCode());
         // 配送方式
         order.setDeliveryType(bo.getDeliveryType());
         order.setTenantId(tenantId);
         // 优惠券ID
         if (StringUtil.isNotEmpty(bo.getCouponId())) {
             order.setCouponId(bo.getCouponId());
         }
        if(bo.getDeliveryType()==1) {
            if (StringUtil.isNotEmpty(bo.getAddressId())) {
                StoreMemberAddressVo addressVo = storeMemberAddressService.selectVoById(bo.getAddressId());
                if (addressVo == null) {
                    throw new RuntimeException("收货地址已不存在");
                }
                order.setDeliveryName(addressVo.getName());
                order.setDeliveryPhone(addressVo.getPhone());
                order.setDeliveryAddress(addressVo.getProvince() + addressVo.getCity() + addressVo.getDistrict() + addressVo.getAddress());
            } else {
                throw new RuntimeException("收货地址不能为空");
            }
        }

         baseMapper.insert(order);

            StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
            log.setOrderId(order.getId());
            log.setNewStatus(StoreOrderStatusEnum.WAITING_PAYMENT.getCode());
            log.setOldStatus(StoreOrderStatusEnum.NONE.getCode());
            log.setTenantId(tenantId);
            storeOrderStatusLogService.insert(log);

        for (String cartId : cartIds) {
            StoreOrderCartVo cartVo = storeOrderCartMapper.selectVoById(cartId);
            //商品单价
            StoreProductSkuVo sku = storeProductSkuService.selectVoById(cartVo.getSkuId());
            StoreOrderItemBo itemBo = new StoreOrderItemBo();
             itemBo.setOrderId(order.getId());
             itemBo.setSpuId(sku.getSpuId());
             itemBo.setSkuId(sku.getId());
             itemBo.setSkuName(sku.getSkuName());
             itemBo.setSkuCode(sku.getSkuCode());
             itemBo.setQuantity(cartVo.getQuantity());
             itemBo.setPrice(sku.getCostPrice());
             itemBo.setTotalPrice(sku.getCostPrice().multiply(new BigDecimal(cartVo.getQuantity())));
             itemBo.setMainImage(sku.getMainImage());
             itemBo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
             itemBo.setTenantId(tenantId);
             storeOrderItemService.insert(itemBo);
        }

        cartIds.forEach(cartId->{
            //更新库存
            StoreOrderCartVo cartVo = storeOrderCartMapper.selectVoById(cartId);
            StoreProductSkuVo sku = storeProductSkuService.selectVoById(cartVo.getSkuId());
            storeProductSkuService.update(new LambdaUpdateWrapper<StoreProductSku>()
                .eq(StoreProductSku::getId,sku.getId())
                .set(StoreProductSku::getStock,sku.getStock()-cartVo.getQuantity())
                .set(StoreProductSku::getUpdatedTime,new  Date())
            );
        });

         //删除购物车
         storeOrderCartMapper.delete(new LambdaQueryWrapper<StoreOrderCart>()
              .eq(StoreOrderCart::getUserId,bo.getUserId())
              .in(StoreOrderCart::getId,cartIds));

         // 使用优惠券（更新已使用次数）
         if (StringUtil.isNotEmpty(bo.getCouponId())) {
             storeCouponService.useCoupon(bo.getCouponId());
         }

        return orderNo;
    }

    @Override
    public Map<String, Object> payOrder(String orderNo, String payType, String loginId, String clientType, String price,HttpServletRequest request) {
        StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,orderNo)
            .eq(StoreOrder::getStatus,StoreOrderStatusEnum.WAITING_PAYMENT.getCode())
            .last(SystemCommonEnum.LIMIT_ONE.getCode()));
         if(order==null){
             throw new MmsException("订单不存在或已支付");
         }
         baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
             .eq(StoreOrder::getOrderNo,orderNo)
             .set(StoreOrder::getPayType, PaymentTypeEnum.getByName(payType).getCode())
         );
        StoreMemberVo memberVo = storeMemberService.selectVoById(loginId);
         if(memberVo==null||!memberVo.getId().equals(order.getUserId())){
              throw new MmsException("订单用户与登录用户不一致");
         }

         // 微信支付
         if (StringUtil.isEmpty(memberVo.getWxOpenid())){
              throw new MmsException("请先绑定微信后再支付");
         }
        Map<String,Object> orderInfo= new HashMap<>();
        orderInfo.put("openId",memberVo.getWxOpenid());
        orderInfo.put("orderNo",orderNo);
        orderInfo.put("productTitle",orderNo);
        //元转分
        Integer payPrice = order.getOrderEndPrice().multiply(new BigDecimal(100)).intValue();
        orderInfo.put("payPrice",payPrice);
        orderInfo.put("ip", IPUtil.getIp(request));
        orderInfo.put("tradeType", TradeType.NATIVE.getTradeType());
        orderInfo.put("mchId","");
        orderInfo.put("expireTime",System.currentTimeMillis()+1000*60*3);
        R<Object> r= wxOrderService.createPay(orderInfo);
        Map<String,Object> map = new HashMap<>();
        map.put("orderNo",orderNo);
        if(r!=null&&r.getData()!=null){
            map.put("payCode",r.getData().toString());
            map.put("msg",r.getMsg());
            map.put("code",r.getCode());
        }else {
            assert r != null;
            map.put("msg",r.getMsg());
            map.put("code",r.getCode());
        }
        return map;
    }

    @Override
    public StoreOrderVo getOrderDetail(String orderNo, String loginId,String ifReturnInfo,Boolean ifAdmin) {
        LambdaQueryWrapper<StoreOrder> queryWrapper = new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,orderNo);
        if(!ifAdmin){
            queryWrapper.eq(StoreOrder::getOrderNo,orderNo);
        }
        StoreOrderVo vo = baseMapper.selectVoOne(queryWrapper);
         if(vo==null){
              throw new MmsException("订单不存在");
         }
         List<StoreOrderSpuVo> spuList = storeOrderItemService.getStoreOrderSpuVoList(vo.getId());
         Integer totalQuantity = 0;

         for (StoreOrderSpuVo spuVo : spuList) {
             totalQuantity+=spuVo.getQuantity();
         }
         vo.setTotalQuantity(totalQuantity);
         vo.setSpuList(spuList);
         vo.setStartTime(vo.getCreatedTime().getTime());
         vo.setEndTime(vo.getCreatedTime().getTime()+30 * 60 * 1000);
         vo.setStoreOrderStateVo(new StoreOrderStateVo(vo.getStatus()));
         if("true".equals(ifReturnInfo)){
             //订单状态
             List<StoreOrderStatusLogVo> stateVos = storeOrderStatusLogService.selectVoListByLqw(new LambdaQueryWrapper<StoreOrderStatusLog>()
                 .eq(StoreOrderStatusLog::getOrderId,vo.getId())
             );
             vo.setStateVos(stateVos);
             StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(vo.getStatus());

             if(storeOrderStatusEnum.isShipped()&&vo.getDeliveryType()==1){

                 StoreShipmentVo shipmentVo = storeShipmentService.selectVoByOrderId(vo.getId());
                 if(shipmentVo!=null){

                     String shipperCode = shipmentVo.getLogisticsCompany();
                     // 物流单号
                     String logisticCode = shipmentVo.getLogisticsNo();
                     // 手机号\座机号后四位
                     String orderCode = shipmentVo.getReceiverPhone().substring(shipmentVo.getReceiverPhone().length()-4);

                     KdniaoAPIUtils kdniaoAPI = new KdniaoAPIUtils();
                     // 调用查询方法
                     String result = kdniaoAPI.getOrderTraces(shipperCode, logisticCode, orderCode);
                     if(result!=null){
                         //转json对象
                         JSONObject jsonObject = JSONObject.parseObject(result);
                         if("true".equals(jsonObject.getString("Success"))){
                             vo.setLogistics(jsonObject);
                         }else {
                             vo.setLogistics(null);
                         }
                     }else {
                         vo.setLogistics(null);
                     }
                 }
             }

         }
         //TODO 售后订单信息
         StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(vo.getStatus());
         if(storeOrderStatusEnum.isAfterSale()){
           StoreRefundApplyVo refundApplyVo=  storeRefundApplyService.selectVoByOrderNo(vo.getOrderNo());
           vo.setRefundApplyVo(refundApplyVo);
           if(refundApplyVo!=null&&refundApplyVo.getRefundStatus()>=4){
              StoreRefundLogisticsVo storeRefundLogisticsVo = storeRefundLogisticsService.selectVoByrefundApplyId(refundApplyVo.getId());
              StoreLogisticsCompanyVo storeLogisticsCompanyVo = storeLogisticsCompanyService.selectVoById(storeRefundLogisticsVo.getLogisticsCompany());
              if(storeLogisticsCompanyVo!=null){
                  storeRefundLogisticsVo.setLogisticsCompany(storeLogisticsCompanyVo.getName());
              }
              vo.setStoreRefundLogisticsVo(storeRefundLogisticsVo);
           }
         }
         StoreInvoiceVo invoiceVo = storeInvoiceMapper.selectVoOne(new LambdaQueryWrapper<StoreInvoice>()
                .eq(StoreInvoice::getOrderId,vo.getOrderNo())
                .orderByDesc(StoreInvoice::getCreatedTime)
                .last("LIMIT 1")
         );
         vo.setStoreInvoiceVo(invoiceVo);
        return  vo;
    }

    @Override
    public TableDataInfo<StoreOrderVo> getOrderList(String loginId, Integer orderStatus, Integer pageNum, Integer pageSize,String keywords) {
         StoreOrderBo bo = new StoreOrderBo();
         bo.setUserId(loginId);
         bo.setStatus(orderStatus);
         bo.setPageSize(pageSize);
         bo.setPageNum(pageNum);
         bo.setOrderNo(keywords);
        LambdaQueryWrapper<StoreOrder> lqw = buildQueryWrapper(bo);
        lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.NONE.getCode());
        lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.EXPIRED.getCode());
        lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.CANCELLED.getCode());
        lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.CLOSED.getCode());
        Page<StoreOrderVo> page = baseMapper.selectVoPage(bo.build(),lqw);
        for (StoreOrderVo orderVo : page.getRecords()) {
             List<StoreOrderSpuVo> spuList = storeOrderItemService.getStoreOrderSpuVoList(orderVo.getId());
             orderVo.setStartTime(orderVo.getCreatedTime().getTime());
             orderVo.setEndTime(orderVo.getCreatedTime().getTime()+30 * 60 * 1000);
             orderVo.setSpuList(spuList);
             StoreProductSpuVo spuVo = storeProductSpuService.selectVoById(spuList.get(0).getId());
             if(spuVo!=null) {
                 orderVo.setStoreId(spuVo.getStoreId());
                 StoreVo storeVo = storeService.selectVoById(spuVo.getStoreId());
                 if(storeVo!=null) {
                     orderVo.setStoreName(storeVo.getStoreName());
                 }
             }
             orderVo.setStoreOrderStateVo(new StoreOrderStateVo(orderVo.getStatus()));
                StoreInvoiceVo invoiceVo = storeInvoiceMapper.selectVoOne(new LambdaQueryWrapper<StoreInvoice>()
                    .eq(StoreInvoice::getOrderId,orderVo.getOrderNo())
                    .orderByDesc(StoreInvoice::getCreatedTime)
                    .last("LIMIT 1")
                );
             orderVo.setStoreInvoiceVo(invoiceVo);
        }
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<StoreOrderVo> getOrderListPc(String loginId, Integer orderStatus, Integer pageNum, Integer pageSize,String keywords) {
        StoreOrderBo bo = new StoreOrderBo();
        bo.setUserId(loginId);
        bo.setStatus(orderStatus);
        bo.setPageSize(pageSize);
        bo.setPageNum(pageNum);
        bo.setOrderNo(keywords);
        LambdaQueryWrapper<StoreOrder> lqw = buildQueryWrapper(bo);
        lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.AFTER_SALE_PROCESSING.getCode());
        lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.RETURN_PROCESSING.getCode());
        lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.EXCHANGE_PROCESSING.getCode());
        lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.REFUND_PROCESSING.getCode());
        lqw.ne(StoreOrder::getStatus, StoreOrderStatusEnum.NONE.getCode());
        Page<StoreOrderVo> page = baseMapper.selectVoPage(bo.build(),lqw);
        for (StoreOrderVo orderVo : page.getRecords()) {
            List<StoreOrderSpuVo> spuList = storeOrderItemService.getStoreOrderSpuVoList(orderVo.getId());
            orderVo.setStartTime(orderVo.getCreatedTime().getTime());
            orderVo.setEndTime(orderVo.getCreatedTime().getTime()+30 * 60 * 1000);
            orderVo.setSpuList(spuList);
            StoreProductSpuVo spuVo = storeProductSpuService.selectVoById(spuList.get(0).getId());
            if(spuVo!=null) {
                orderVo.setStoreId(spuVo.getStoreId());
                StoreVo storeVo = storeService.selectVoById(spuVo.getStoreId());
                if(storeVo!=null) {
                    orderVo.setStoreName(storeVo.getStoreName());
                }
            }
            orderVo.setStoreOrderStateVo(new StoreOrderStateVo(orderVo.getStatus()));
            //storeInvoiceService
            StoreInvoiceVo invoiceVo = storeInvoiceMapper.selectVoOne(new LambdaQueryWrapper<StoreInvoice>()
                    .eq(StoreInvoice::getOrderId,orderVo.getOrderNo())
                    .orderByDesc(StoreInvoice::getCreatedTime)
                    .last("LIMIT 1")
            );
            orderVo.setStoreInvoiceVo(invoiceVo);
        }
        return TableDataInfo.build(page);
    }

    @Override
    public Boolean updateSuccessPayByOrderNo(String outTradeNo, String transactionId, String mchId,BigDecimal payAmount,Date payTime) {
        StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,outTradeNo)
            .eq(StoreOrder::getStatus,StoreOrderStatusEnum.WAITING_PAYMENT.getCode())
            .last(SystemCommonEnum.LIMIT_ONE.getCode()));
        if(order==null){
            throw new MmsException("订单不存在或已支付");
        }
        baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
            .eq(StoreOrder::getId,order.getId())
            .set(StoreOrder::getTransactionNumber,transactionId)
            .set(StoreOrder::getPayTime,payTime)
            .set(StoreOrder::getPayAmount,payAmount)
            .set(StoreOrder::getStatus,StoreOrderStatusEnum.WAITING_CONFIRMATION.getCode())
            .set(StoreOrder::getUpdatedTime,new Date())
            .set(StoreOrder::getMchId,mchId)
            );

        StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
        log.setOrderId(order.getId());
        log.setNewStatus(StoreOrderStatusEnum.WAITING_CONFIRMATION.getCode());
        log.setOldStatus(order.getStatus());
        log.setTenantId(order.getTenantId());
        storeOrderStatusLogService.insert(log);
        return Boolean.TRUE;
    }

    @Override
    public Boolean cancelOrder(String orderNo, String loginId) {
         StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>()
              .eq(StoreOrder::getOrderNo,orderNo).last(SystemCommonEnum.LIMIT_ONE.getCode()));
         if(order==null){
              throw new MmsException("订单不存在或已支付");
         }
        StoreMemberVo memberVo = storeMemberService.selectVoById(loginId);
        if(memberVo==null||!memberVo.getId().equals(order.getUserId())){
            throw new MmsException("订单用户与登录用户不一致");
        }
         StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(order.getStatus());
         if(!storeOrderStatusEnum.canCancel()){
              throw new MmsException("订单状态不允许取消");
         }
         baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
              .eq(StoreOrder::getId,order.getId())
              .set(StoreOrder::getStatus,StoreOrderStatusEnum.CANCELLED.getCode())
              .set(StoreOrder::getUpdatedTime,new Date())
              );
           StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
           log.setOrderId(order.getId());
           log.setNewStatus(StoreOrderStatusEnum.CANCELLED.getCode());
           log.setOldStatus(order.getStatus());
           log.setTenantId(order.getTenantId());
           storeOrderStatusLogService.insert(log);
           return Boolean.TRUE;
    }

    @Override
    public Boolean deleteOrder(String orderNo, String loginId) {
         StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>()
              .eq(StoreOrder::getOrderNo,orderNo)
              .last(SystemCommonEnum.LIMIT_ONE.getCode()));
         if(order==null){
              throw new MmsException("订单不存在或已支付");
         }
         StoreMemberVo memberVo = storeMemberService.selectVoById(loginId);
         if(memberVo==null||!memberVo.getId().equals(order.getUserId())){
              throw new MmsException("订单用户与登录用户不一致");
         }
        StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(order.getStatus());
        if(!storeOrderStatusEnum.canDelete()){
            throw new MmsException("订单状态不允许删除");
        }
         baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
              .eq(StoreOrder::getId,order.getId())
              .set(StoreOrder::getStatus,StoreOrderStatusEnum.NONE.getCode())
              .set(StoreOrder::getUpdatedTime,new Date())
              );
           StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
           log.setOrderId(order.getId());
           log.setNewStatus(StoreOrderStatusEnum.NONE.getCode());
           log.setOldStatus(order.getStatus());
           log.setTenantId(order.getTenantId());
           storeOrderStatusLogService.insert(log);
           return Boolean.TRUE;
    }

    @Override
    public Boolean expiredOrder(String orderNo) {
        StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>()
            .eq(StoreOrder::getOrderNo,orderNo)
            .last(SystemCommonEnum.LIMIT_ONE.getCode()));
        if(order==null){
            throw new MmsException("订单不存在或已支付");
        }
        StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(order.getStatus());
        if(!storeOrderStatusEnum.canCancel()){
            throw new MmsException("订单状态不允许取消");
        }
        baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
            .eq(StoreOrder::getId,order.getId())
            .set(StoreOrder::getStatus,StoreOrderStatusEnum.EXPIRED.getCode())
            .set(StoreOrder::getUpdatedTime,new Date())
        );
        StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
        log.setOrderId(order.getId());
        log.setNewStatus(StoreOrderStatusEnum.EXPIRED.getCode());
        log.setOldStatus(order.getStatus());
        log.setCreatedBy(0L);
        log.setCreatedTime(new Date());
        log.setTenantId(order.getTenantId());
        storeOrderStatusLogService.insert(log);
        return Boolean.TRUE;
    }

    @Override
    public StoreOrderSpuVo afterSalesInfo(String orderNo, String loginId, String itemId) {
        StoreOrderVo orderVo = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,orderNo)
            .eq(StoreOrder::getUserId,loginId));
         List<StoreOrderSpuVo> spuList = storeOrderItemService.getStoreOrderSpuVoList(orderVo.getId());
         for (StoreOrderSpuVo spuVo : spuList) {
              if(spuVo.getItemId().equals(itemId)){
                  return spuVo;
              }
         }
        return null;
    }

    @Override
    public Boolean afterSalesForm(OrderAfterSalesBo bo, String loginId) {
        StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,bo.getOrderNo())
            .eq(StoreOrder::getUserId,loginId));
        StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(order.getStatus());
        if(!storeOrderStatusEnum.canApplyAfterSale()){
            throw new MmsException("订单状态不允许退款");
        }
        //
        StoreOrderItemVo itemVo =storeOrderItemService.selectVoById(bo.getItemId());
        if(itemVo==null){
             throw new MmsException("订单商品不存在");
        }
        if(itemVo.getQuantity()<bo.getNum()){
             throw new MmsException("退款数量不能大于购买数量");
        }
        // 添加售后订单
        Boolean fag =storeRefundApplyService.afterSalesForm(bo,itemVo);
        if(!fag){
             throw new MmsException("售后申请失败");
        }
        baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
            .eq(StoreOrder::getId,order.getId())
            .set(StoreOrder::getStatus,StoreOrderStatusEnum.AFTER_SALE_PROCESSING.getCode())
            .set(StoreOrder::getUpdatedTime,new Date())
        );
        StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
        log.setOrderId(order.getId());
        log.setNewStatus(StoreOrderStatusEnum.AFTER_SALE_PROCESSING.getCode());
        log.setOldStatus(order.getStatus());
        log.setTenantId(order.getTenantId());
        storeOrderStatusLogService.insert(log);
        return Boolean.TRUE;
    }

    @Override
    public Boolean receipt(String orderNo, String loginId) {
         StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,orderNo)
              .eq(StoreOrder::getUserId,loginId));
          StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(order.getStatus());
           if(!storeOrderStatusEnum.canConfirmReceipt()){
                throw new MmsException("订单状态不允许确认收货");
           }
            baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
                 .eq(StoreOrder::getId,order.getId())
                 .set(StoreOrder::getStatus,StoreOrderStatusEnum.COMPLETED.getCode())
                 .set(StoreOrder::getUpdatedTime,new Date())
                 );
            StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
             log.setOrderId(order.getId());
             log.setNewStatus(StoreOrderStatusEnum.COMPLETED.getCode());
             log.setOldStatus(order.getStatus());
             log.setTenantId(order.getTenantId());
             storeOrderStatusLogService.insert(log);
             return Boolean.TRUE;
    }

    @Override
    public TableDataInfo<StoreRefundApplyVo> getSalesOrderList(String loginId, Integer orderStatus, Integer pageNum, Integer pageSize, String keywords) {
        Page<StoreRefundApplyVo> page = storeRefundApplyService.selectListVoPageXml(pageNum,pageSize,loginId,orderStatus,keywords);
        for (StoreRefundApplyVo applyVo : page.getRecords()) {
            AfterSaleOrderStatusEnum afterSaleOrderStatusEnum=AfterSaleOrderStatusEnum.getByCode(applyVo.getRefundStatus());
            applyVo.setIsCanCancel(afterSaleOrderStatusEnum.canCancel());
            applyVo.setIsWaitingBuyerReturn(afterSaleOrderStatusEnum.isWaitingBuyerReturn());
        }
        return TableDataInfo.build(page);
    }

    @Override
    public Boolean addOrderRefundLogistics(String refundNo, String loginId) {

        StoreRefundApplyVo applyVo = storeRefundApplyService.selectVoByRefundNo(refundNo,loginId);
        if(applyVo==null){
            throw new MmsException("售后申请不存在");
        }
        AfterSaleOrderStatusEnum afterSaleOrderStatusEnum = AfterSaleOrderStatusEnum.getByCode(applyVo.getRefundStatus());
        if(afterSaleOrderStatusEnum.isFinalized()){
            throw new MmsException("售后已关闭");
        }
        // 更新售后申请状态为待卖家收货
        storeRefundApplyService.update(new LambdaUpdateWrapper<StoreRefundApply>()
             .eq(StoreRefundApply::getRefundNo,refundNo)
             .set(StoreRefundApply::getRefundStatus,AfterSaleOrderStatusEnum.WAITING_SELLER_RECEIVE.getCode())
              .set(StoreRefundApply::getStatus,2)
             .set(StoreRefundApply::getUpdatedTime,new Date())
        );

        StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,applyVo.getOrderNo())
            .eq(StoreOrder::getUserId,loginId));
        StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(order.getStatus());

        baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
            .eq(StoreOrder::getId,order.getId())
            .set(StoreOrder::getStatus,StoreOrderStatusEnum.RETURN_PROCESSING.getCode())
            .set(StoreOrder::getUpdatedTime,new Date())
        );
        StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
        log.setOrderId(order.getId());
        log.setNewStatus(StoreOrderStatusEnum.RETURN_PROCESSING.getCode());
        log.setOldStatus(order.getStatus());
        log.setTenantId(order.getTenantId());
        storeOrderStatusLogService.insert(log);
        return Boolean.TRUE;
    }

    @Override
    public Boolean scanTimeoutOrders(String params) {
        // 计算30分钟前的时间点
        LocalDateTime timeoutPoint = LocalDateTime.now().minusMinutes(30);

        // 查询超时订单 (使用分页避免一次查出太多)
        PageQuery pageQuery = new PageQuery(1,50);
        Page<StoreOrderVo> page = baseMapper.selectVoPage(pageQuery.build(),new LambdaQueryWrapper<StoreOrder>()
            .eq(StoreOrder::getStatus,StoreOrderStatusEnum.WAITING_PAYMENT.getCode())
            .lt(StoreOrder::getCreatedTime,timeoutPoint)
            .orderByDesc(StoreOrder::getCreatedTime)
        );
            // 处理这一批订单
            for (StoreOrderVo order : page.getRecords()) {
                try {
                    expiredOrder(order.getOrderNo());
                } catch (Exception e) {
                    // 记录日志，防止单个订单失败影响批量处理
                    log.error("处理超时订单失败：{}", order.getId(), e);
                }
            }

        return Boolean.TRUE;
    }

    @Override
    public Boolean orderConfirm(String orderNo, Integer type) {
        StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,orderNo));
        int newStatus = StoreOrderStatusEnum.REFUND_PROCESSING.getCode();
        if(type==0){
            //进行退款
            if(this.refund(order.getTransactionNumber(),order.getOrderNo(),order.getOrderEndPrice(),order.getOrderEndPrice())){
                //更新为退款中
                baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
                    .eq(StoreOrder::getId,order.getId())
                    .set(StoreOrder::getStatus,newStatus)
                    .set(StoreOrder::getUpdatedTime,new Date())
                );
                StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
                log.setOrderId(order.getId());
                log.setNewStatus(newStatus);
                log.setOldStatus(order.getStatus());
                log.setOperator(LoginObject.getLoginId());
                log.setTenantId(order.getTenantId());
                storeOrderStatusLogService.insert(log);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        if(type==1){
            //待发货状态
            newStatus = StoreOrderStatusEnum.WAITING_SHIPMENT.getCode();
            baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
                .eq(StoreOrder::getId,order.getId())
                .set(StoreOrder::getStatus,newStatus)
                .set(StoreOrder::getUpdatedTime,new Date())
            );
            StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
            log.setOrderId(order.getId());
            log.setNewStatus(newStatus);
            log.setOldStatus(order.getStatus());
            log.setOperator(LoginObject.getLoginId());
            log.setTenantId(order.getTenantId());
            storeOrderStatusLogService.insert(log);
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public Boolean refund(String transactionNumber,String orderNo,BigDecimal totalFee,BigDecimal refundFee) {
        StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,orderNo));
        StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(order.getStatus());
        if(!storeOrderStatusEnum.isPaid()){
            throw new MmsException("订单未支付");
        }
        if(storeOrderStatusEnum.isRefundCompleted()){
            throw new MmsException("订单已退款");
        }
        R<Object> r=  wxOrderService.refund(order.getTransactionNumber(),order.getOrderNo(),order.getOrderEndPrice());
        return r.getStatus();
    }


    @Override
    public Boolean orderDelivery(StoreShipmentBo bo) {
        StoreOrderVo order = baseMapper.selectVoById(bo.getOrderId());
        StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(order.getStatus());
        if(!storeOrderStatusEnum.canShip()){
            throw new MmsException("订单状态不允许发货");
        }
        bo.setShipmentStatus(2);
        Boolean b = storeShipmentService.orderDelivery(bo);
        if(b){
            int newStatus = StoreOrderStatusEnum.SHIPPED.getCode();
            if(bo.getShipmentType()==2){
                newStatus = StoreOrderStatusEnum.PARTIALLY_SHIPPED.getCode();
            }
            baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
                .eq(StoreOrder::getId,order.getId())
                .set(StoreOrder::getStatus,newStatus)
                .set(StoreOrder::getUpdatedTime,new Date())
            );
            StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
            log.setOrderId(order.getId());
            log.setNewStatus(newStatus);
            log.setOldStatus(order.getStatus());
            log.setOperator(LoginObject.getLoginId());
            log.setTenantId(order.getTenantId());
            storeOrderStatusLogService.insert(log);
        }
        return b;
    }

    @Override
    public StoreOrderVo selectOrderByOrderNo(String orderNo) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,orderNo));
    }

    @Override
    public Boolean afterServer(StoreRefundApplyBo bo) {
        StoreOrderVo order = selectOrderByOrderNo(bo.getOrderNo());
        bo.setAuditTime(new Date());
        bo.setAuditBy(LoginObject.getLoginId());
        // 1:待审核
        // 2:审核通过           会员可以提交退款订单
        // 3:审核拒绝
        // 4:买家退货，待卖家收货 会员完成提交退货订单
        // 5:卖家确认收货        商家收到退货
        // 6:卖家终止售后
        // 7:买家确认收货
        // 8:买家取消售后
        // 9:平台退款中
        // 10:完成售后退款完成
        if(bo.getRefundStatus()==2){
            // 售后日志
            StoreRefundOperationLogBo log2=new StoreRefundOperationLogBo();
            log2.setRefundApplyId(bo.getId());
            log2.setOperationType(AfterSaleOrderStatusEnum.REVIEW_APPROVED.getCode());
            log2.setOperationDesc(bo.getAuditRemark());
            log2.setOperatorId(LoginObject.getLoginId());
            log2.setOperatorRole(2);
            log2.setOperationTime(new Date());
            log2.setTenantId(order.getTenantId());
            storeRefundOperationLogService.insert(log2);
            // 同意申请
            baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
                .eq(StoreOrder::getId,order.getId())
                .set(StoreOrder::getStatus,StoreOrderStatusEnum.RETURN_PROCESSING.getCode())
                .set(StoreOrder::getUpdatedTime,new Date())
            );
            StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
            log.setOrderId(order.getId());
            log.setNewStatus(StoreOrderStatusEnum.RETURN_PROCESSING.getCode());
            log.setOldStatus(order.getStatus());
            log.setOperator(LoginObject.getLoginId());
            log.setTenantId(order.getTenantId());
            storeOrderStatusLogService.insert(log);
            bo.setStatus(1);
            bo.setAuditRemark(null);
            storeRefundApplyService.updateById(bo);
            return Boolean.TRUE;
        }

        if(bo.getRefundStatus()==3||bo.getRefundStatus()==6){
            if(bo.getRefundStatus()==3){
                // 售后日志
                StoreRefundOperationLogBo log2=new StoreRefundOperationLogBo();
                log2.setRefundApplyId(bo.getId());
                log2.setOperationType(AfterSaleOrderStatusEnum.REVIEW_REJECTED.getCode());
                log2.setOperationDesc(bo.getAuditRemark());
                log2.setOperatorId(LoginObject.getLoginId());
                log2.setOperatorRole(2);
                log2.setOperationTime(new Date());
                log2.setTenantId(order.getTenantId());
                storeRefundOperationLogService.insert(log2);
            }
            if(bo.getRefundStatus()==6){
                // 售后日志
                StoreRefundOperationLogBo log2=new StoreRefundOperationLogBo();
                log2.setRefundApplyId(bo.getId());
                log2.setOperationType(AfterSaleOrderStatusEnum.SELLER_TERMINATED.getCode());
                log2.setOperationDesc(bo.getAuditRemark());
                log2.setOperatorId(LoginObject.getLoginId());
                log2.setOperatorRole(2);
                log2.setOperationTime(new Date());
                log2.setTenantId(order.getTenantId());
                storeRefundOperationLogService.insert(log2);
            }
            if(bo.getRefundStatus()==3){
                bo.setStatus(1);
            }
            if(bo.getRefundStatus()==6){
                bo.setStatus(3);
            }
            //查询订单申请售后之前的最新状态
            StoreOrderStatusLogVo lastStatus = storeOrderStatusLogService.selectLastStatus(order.getId(),StoreOrderStatusEnum.AFTER_SALE_PROCESSING.getCode());
            if(lastStatus==null){
                throw new MmsException("订单售后状态已不支持取消");
            }

            baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
                .eq(StoreOrder::getId,order.getId())
                .set(StoreOrder::getStatus,lastStatus.getOldStatus())
                .set(StoreOrder::getUpdatedTime,new Date())
            );
            StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
            log.setOrderId(order.getId());
            log.setNewStatus(lastStatus.getOldStatus());
            log.setOldStatus(order.getStatus());
            log.setOperator(LoginObject.getLoginId());
            log.setTenantId(order.getTenantId());
            storeOrderStatusLogService.insert(log);
            bo.setAuditRemark(null);
            storeRefundApplyService.updateById(bo);


            return Boolean.TRUE;
        }

        // 卖家确认收货
        if(bo.getRefundStatus()==5){
            bo.setStatus(3);
            baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
                .eq(StoreOrder::getId,order.getId())
                .set(StoreOrder::getStatus,StoreOrderStatusEnum.REFUND_PROCESSING.getCode())
                .set(StoreOrder::getUpdatedTime,new Date())
            );
            StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
            log.setOrderId(order.getId());
            log.setNewStatus(StoreOrderStatusEnum.REFUND_PROCESSING.getCode());
            log.setOldStatus(order.getStatus());
            log.setOperator(LoginObject.getLoginId());
            log.setTenantId(order.getTenantId());
            storeOrderStatusLogService.insert(log);

            storeRefundApplyService.updateById(bo);

            // 售后日志
            StoreRefundOperationLogBo log2=new StoreRefundOperationLogBo();
            log2.setRefundApplyId(bo.getId());
            log2.setOperationType(AfterSaleOrderStatusEnum.SELLER_CONFIRMED_RECEIPT.getCode());
            log2.setOperationDesc(bo.getAuditRemark());
            log2.setOperatorId(LoginObject.getLoginId());
            log2.setOperatorRole(1);
            log2.setOperationTime(new Date());
            log2.setTenantId(order.getTenantId());
            storeRefundOperationLogService.insert(log2);
            return Boolean.TRUE;
        }

        if(bo.getRefundStatus()==9){
            bo.setStatus(4);

            // 售后日志
            StoreRefundOperationLogBo log2=new StoreRefundOperationLogBo();
            log2.setRefundApplyId(bo.getId());
            log2.setOperationType(AfterSaleOrderStatusEnum.WAITING_PLATFORM_REFUND.getCode());
            log2.setOperationDesc(bo.getAuditRemark());
            log2.setOperatorId(LoginObject.getLoginId());
            log2.setOperatorRole(2);
            log2.setOperationTime(new Date());
            log2.setTenantId(order.getTenantId());
            storeRefundOperationLogService.insert(log2);

            if(this.refund(order.getTransactionNumber(),order.getOrderNo(),order.getOrderEndPrice(),order.getOrderEndPrice())){
                //更新为退款中
                baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
                    .eq(StoreOrder::getId,order.getId())
                    .set(StoreOrder::getStatus,StoreOrderStatusEnum.REFUND_PROCESSING.getCode())
                    .set(StoreOrder::getUpdatedTime,new Date())
                );
                StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
                log.setOrderId(order.getId());
                log.setNewStatus(StoreOrderStatusEnum.REFUND_PROCESSING.getCode());
                log.setOldStatus(order.getStatus());
                log.setOperator(LoginObject.getLoginId());
                log.setTenantId(order.getTenantId());
                storeOrderStatusLogService.insert(log);

                bo.setAuditRemark(null);
                storeRefundApplyService.updateById(bo);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        return Boolean.FALSE;
    }



    @Override
    public Boolean untAfterServer(String refundNo, String loginId){
        StoreRefundApplyVo applyVo = storeRefundApplyService.selectVoByRefundNo(refundNo,loginId);
        if(applyVo==null){
            throw new MmsException("售后申请不存在");
        }
        AfterSaleOrderStatusEnum afterSaleOrderStatusEnum = AfterSaleOrderStatusEnum.getByCode(applyVo.getRefundStatus());
        if(!afterSaleOrderStatusEnum.canCancel()){
            throw new MmsException("售后状态已不支持取消");
        }

        //查询订单申请售后之前的最新状态
        StoreOrderStatusLogVo lastStatus = storeOrderStatusLogService.selectLastStatus(applyVo.getOrderId(),StoreOrderStatusEnum.AFTER_SALE_PROCESSING.getCode());
        if(lastStatus==null){
            throw new MmsException("订单售后状态已不支持取消");
        }

        baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
            .eq(StoreOrder::getId,applyVo.getOrderId())
            .set(StoreOrder::getStatus,lastStatus.getOldStatus())
            .set(StoreOrder::getUpdatedTime,new Date())
        );
        StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
        log.setOrderId(applyVo.getOrderId());
        log.setNewStatus(lastStatus.getOldStatus());
        log.setOldStatus(StoreOrderStatusEnum.AFTER_SALE_PROCESSING.getCode());
        log.setOperator(LoginObject.getLoginId());
        log.setTenantId(applyVo.getTenantId());
        storeOrderStatusLogService.insert(log);

        storeRefundApplyService.update(new LambdaUpdateWrapper<StoreRefundApply>()
            .eq(StoreRefundApply::getRefundNo,refundNo)
            .set(StoreRefundApply::getRefundStatus,AfterSaleOrderStatusEnum.BUYER_CANCELLED.getCode())
            .set(StoreRefundApply::getAuditTime,new Date())
            .set(StoreRefundApply::getAuditBy,loginId)
            .set(StoreRefundApply::getAuditRemark,"取消售后申请")
        );
        // 售后日志
        StoreRefundOperationLogBo log2=new StoreRefundOperationLogBo();
        log2.setRefundApplyId(applyVo.getId());
        log2.setOperationType(AfterSaleOrderStatusEnum.BUYER_CANCELLED.getCode());
        log2.setOperationDesc("取消售后申请");
        log2.setOperatorId(LoginObject.getLoginId());
        log2.setOperatorRole(1);
        log2.setOperationTime(new Date());
        log2.setTenantId(applyVo.getTenantId());
        storeRefundOperationLogService.insert(log2);
        return Boolean.TRUE;
    };

    /**
     * 售后申请处理详情
     * @param refundNo 售后单号
     * @param loginId 登录ID
     * @return  详情
     */
    @Override
    public StoreRefundApplyVo afterServerInfo(String refundNo, String loginId){
        StoreRefundApplyVo vo =storeRefundApplyService.selectVoByRefundNo(refundNo,loginId);
        AfterSaleOrderStatusEnum  afterSaleOrderStatusEnum = AfterSaleOrderStatusEnum.getByCode(vo.getRefundStatus());
        vo.setIsCanCancel(afterSaleOrderStatusEnum.canCancel());
        vo.setIsWaitingBuyerReturn(afterSaleOrderStatusEnum.isWaitingBuyerReturn());
        return vo;
    };
    @Override
    public  Boolean refundFee(String refundNo,BigDecimal refundFee) {
        StoreRefundApplyVo vo = storeRefundApplyService.selectVoByOrderNo(refundNo);
        StoreOrderVo order = selectOrderByOrderNo(vo.getOrderNo());
        if(vo.getRefundStatus()!=AfterSaleOrderStatusEnum.SELLER_CONFIRMED_RECEIPT.getCode()){
            throw new MmsException("售后状态不支持退款");
        }
        storeRefundApplyService.update(new LambdaUpdateWrapper<StoreRefundApply>()
            .eq(StoreRefundApply::getRefundNo,refundNo)
            .set(StoreRefundApply::getRefundStatus,AfterSaleOrderStatusEnum.WAITING_PLATFORM_REFUND.getCode())
        );
        return this.refund(order.getTransactionNumber(),order.getOrderNo(),order.getOrderEndPrice(),refundFee);
    }

    @Override
    public Boolean notifyOrder(WxPayRefundNotifyResult.ReqInfo reqInfo) {
        if(reqInfo==null){
            return Boolean.FALSE;
        }
        StoreOrderVo order = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderNo,reqInfo.getOutTradeNo()));
        if(order==null){
            log.info("订单不存在");
            return Boolean.FALSE;
        }
        StoreOrderStatusEnum storeOrderStatusEnum =StoreOrderStatusEnum.getByCode(order.getStatus());
        if(!storeOrderStatusEnum.isPaid()){
            log.info("订单未支付");
            return Boolean.FALSE;
        }
        if(storeOrderStatusEnum.isRefundCompleted()){
            log.info("订单已退款");
            return Boolean.FALSE;
        }
        if("SUCCESS".equals(reqInfo.getRefundStatus())){
            //订单金额
            Integer totalFee=reqInfo.getTotalFee();
            Integer refundFee=reqInfo.getRefundFee();

            //部分退款
            int newStatus = StoreOrderStatusEnum.PARTIALLY_REFUNDED.getCode();
            if(Objects.equals(totalFee, refundFee)){
                //退全款
                newStatus = StoreOrderStatusEnum.REFUNDED.getCode();
            }

            baseMapper.update(null,new LambdaUpdateWrapper<StoreOrder>()
                .eq(StoreOrder::getId,order.getId())
                .set(StoreOrder::getStatus,newStatus)
                .set(StoreOrder::getUpdatedTime,new Date())
            );
            StoreOrderStatusLogBo log = new StoreOrderStatusLogBo();
            log.setOrderId(order.getId());
            log.setNewStatus(newStatus);
            log.setOldStatus(order.getStatus());
            log.setOperator("");
            log.setTenantId(order.getTenantId());
            storeOrderStatusLogService.insert(log);

            //查找订单是否是在售后状态
            StoreRefundApplyVo applyVo = storeRefundApplyService.selectVoByOrderNo(order.getOrderNo());
            if(applyVo!=null){
                storeRefundApplyService.update(new LambdaUpdateWrapper<StoreRefundApply>()
                    .eq(StoreRefundApply::getRefundNo,applyVo.getRefundNo())
                    .set(StoreRefundApply::getRefundStatus,AfterSaleOrderStatusEnum.COMPLETED.getCode())
                );
                // 售后日志
                StoreRefundOperationLogBo log2=new StoreRefundOperationLogBo();
                log2.setRefundApplyId(applyVo.getId());
                log2.setOperationType(AfterSaleOrderStatusEnum.WAITING_PLATFORM_REFUND.getCode());
                log2.setOperationDesc("完成退款");
                log2.setOperatorId("");
                log2.setOperatorRole(4);
                log2.setOperationTime(new Date());
                log2.setTenantId(order.getTenantId());
                storeRefundOperationLogService.insert(log2);
            }

            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
