package com.sxpcwlkj.store.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.bo.*;
import com.sxpcwlkj.store.entity.vo.*;
import com.sxpcwlkj.store.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 *  店铺订单接口
 *
 */
@Tag(name = "🌳商城模块-订单接口",description = "店铺订单一些基础功能")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("mall-api/v1/common/order")
public class ApiStoreOrderController {

    private  final StoreOrderCartService storeOrderCartService;
    private final StoreOrderService storeOrderService;
    private final StoreLogisticsCompanyService storeLogisticsCompanyService;
    private final StoreRefundLogisticsService storeRefundLogisticsService;
    private final StoreRefundApplyService storeRefundApplyService;
    private final StoreInvoiceService storeInvoiceService;
    /**
     *  添加购物车
     * @param bo bo
     * @return  R
     */
    @SaCheckLogin
    @Operation(summary = "添加/删除购物车", description = "下单数量为0时，删除购物车")
    @PostMapping("/addCart")
    public R<String> addCart(@RequestBody @Validated(ValidatedGroupConfig.insert.class) AddCartBo bo) {
        bo.setUserId(LoginObject.getLoginId());
        Boolean  flag=storeOrderCartService.addCart(bo);
        return  R.ok(flag,flag?"添加成功":"添加失败", bo.getId());
    }

    @SaCheckLogin
    @Operation(summary = "删除购物车", description = "下单数量为0时，删除购物车")
    @PostMapping("/deleteCart")
    public R<String> deleteCart(@RequestBody @Validated(ValidatedGroupConfig.del.class) AddCartBo bo) {
        bo.setUserId(LoginObject.getLoginId());
        Boolean  flag=storeOrderCartService.delCart(bo);
        return  R.ok(flag,flag?"删除成功":"删除失败", bo.getId());
    }

    /**
     *   获取购物车列表
     * @return  R
     */
    @SaCheckLogin
    @Operation(summary = "获取购物车列表", description = "获取当前登录用户购物车列表")
    @PostMapping("/getCartList")
    public R<List<StoreOrderCartVo>> getCartList() {
        return R.success(storeOrderCartService.getCartList(LoginObject.getLoginId()));
    }


    /**
     *  创建订单
     */
    @SaCheckLogin
    @Operation(summary = "创建订单", description = "创建订单")
    @PostMapping("/createOrder")
    public R<String> createOrder(@RequestBody AddOrderBo bo) {
        if(bo.getDeliveryType()==1){
            if(StringUtil.isEmpty(bo.getAddressId())){
                return R.fail("请选择收货地址");
            }
        }
        bo.setUserId(LoginObject.getLoginId());
        String  orderId= storeOrderService.createOrder(bo);
        return R.ok(orderId != null,orderId!=null?"创建成功":"创建失败", orderId);
    }

    /**
     * 支持的付款方式
     * @param type 网站：website，小程序：miniapp
     * @return List
     */
    @SaCheckLogin
    @Operation(summary = "支持的付款方式", description = "支持的付款方式")
    @GetMapping("/getPayType")
    public R<List<Map<String,Object>>> getPayType(
        @RequestParam(defaultValue = "website") String  type) {
        List<Map<String,Object>> data=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("id",1);
        map.put("name","微信支付");
        map.put("code","WECHAT");
        map.put("icon","https://dss1.bdstatic.com/6OF1bjeh1BF3odCf/it/u=3774939867,2826752539&fm=74");
        map.put("status",1);
        data.add(map);
        Map<String,Object> map2=new HashMap<>();
        map2.put("id",2);
        map2.put("name","支付宝支付");
        map2.put("code","ALIPAY");
        map2.put("icon","https://ss3.bdstatic.com/yrwDcj7w0QhBkMak8IuT_XF5ehU5bvGh7c50/logopic/a9936a369e82e0c6c42112674a5220e8_fullsize.jpg");
        map2.put("status",0);
        data.add(map2);

        return R.success(data);
    }

    /**
     *  订单支付
     * @param orderNo 订单编号
     * @param clientType  支付方式  （微信支付：wxpay，支付宝支付：alipay，余额：balance）
     * @return 付款二维码，订单信息
     */
    @SaCheckLogin
    @Operation(summary = "订单支付", description = "订单支付")
    @GetMapping("/payOrder")
    public R<Map<String,Object>> payOrder(
        String  orderNo,
        String  paymentMethod,
        String  clientType,
        String  price,
        HttpServletRequest request) {
        return R.success(storeOrderService.payOrder(orderNo,paymentMethod,LoginObject.getLoginId(),clientType,price,request));
    }

    /**
     * 查询订单详情
     * ifReturnInfo  是否返回订单商品信息(true：返回，false：不返回)
     */
    @SaCheckLogin
    @Operation(summary = "查询订单详情", description = "查询订单详情")
    @GetMapping("/getOrderDetail")
    public R<StoreOrderVo> getOrderDetail(String  orderNo,
                                          @Parameter(description = "是否返回订单商品信息", required = false, example = "false") String  ifReturnInfo
    ) {
        return R.success(storeOrderService.getOrderDetail(orderNo,LoginObject.getLoginId(),ifReturnInfo,false));
    }

    /**
     * 我的订单列表
     */
    @SaCheckLogin
    @Operation(summary = "我的订单列表", description = "我的订单列表")
    @GetMapping("/getOrderList")
    public TableDataInfo<StoreOrderVo> getOrderList(Integer pageNum, Integer pageSize, Integer orderStatus,String keywords) {
        return storeOrderService.getOrderListPc(LoginObject.getLoginId(),orderStatus,pageNum,pageSize,keywords);
    }

    /**
     * 取消订单
     */
    @SaCheckLogin
    @Operation(summary = "取消我的订单", description = "取消订单")
    @GetMapping("/cancelOrder")
    public R<String> cancelOrder(String  orderNo) {
        Boolean flag = storeOrderService.cancelOrder(orderNo,LoginObject.getLoginId());
        return R.ok(flag,flag?"取消成功":"取消失败");
    }

    /**
     * 删除订单
     */
    @SaCheckLogin
    @Operation(summary = "删除我的订单", description = "删除订单")
    @GetMapping("/deleteOrder")
    public R<String> deleteOrder(String  orderNo) {
        Boolean flag = storeOrderService.deleteOrder(orderNo,LoginObject.getLoginId());
        return R.ok(flag,flag?"删除成功":"删除失败");
    }

    /**
     * 申请售后详情
     */
    @SaCheckLogin
    @Operation(summary = "申请售后商品详情", description = "申请售后商品详情")
    @GetMapping("/afterSalesInfo")
    public R<Map<String,Object>> afterSalesInfo(String  orderNo,String  itemId) {
        StoreOrderSpuVo spu= storeOrderService.afterSalesInfo(orderNo,LoginObject.getLoginId(),itemId);
        Map<String,Object> map=new HashMap<>();
        //ORIGINAL 原始售后  RETURN 退货售后
        map.put("refundWay","ORIGINAL");
        map.put("afterSalesSpu",spu);
        //退货
        map.put("returnGoods",true);
        //退款
        map.put("returnMoney",true);
        //退款方式 BANK_TRANSFER 银行转账  ALIPAY 支付宝  WECHATPAY 微信  BALANCE 余额 ROUTE 原路返回
        map.put("accountType","ROUTE");

        map.put("orderNo",orderNo);

        map.put("itemId",itemId);
        return R.success(map);
    }

    /**
     * 申请售后理由
     */
    @SaCheckLogin
    @Operation(summary = "申请售后理由", description = "申请售后理由")
    @GetMapping("/afterSaleReason")
    public R<List<Map<String,Object>>> afterSaleReason(String  type) {
        List<Map<String,Object>> data=new ArrayList<>();
        Map<String,Object> map0=new HashMap<>();
        map0.put("id",0);
        map0.put("reason","七天无理由退货");
        data.add(map0);
        Map<String,Object> map=new HashMap<>();
        map.put("id",1);
        map.put("reason","不喜欢/不想要");
        data.add(map);
        Map<String,Object> map2=new HashMap<>();
        map2.put("id",2);
        map2.put("reason","商品质量问题");
        data.add(map2);

        Map<String,Object> map3=new HashMap<>();
        map3.put("id",3);
        map3.put("reason","发货延迟");
        data.add(map3);

        Map<String,Object> map4=new HashMap<>();
        map4.put("id",4);
        map4.put("reason","商品描述不符");
        data.add(map4);

        Map<String,Object> map5=new HashMap<>();
        map5.put("id",5);
        map5.put("reason","其他");
        data.add(map5);
        return R.success(data);
    }


    /**
     * 申请售后提交
     */
    @SaCheckLogin
    @Operation(summary = "申请售后提交", description = "申请售后提交")
    @PostMapping("/afterSalesForm")
    public R<Boolean> afterSalesForm(@RequestBody @Validated(ValidatedGroupConfig.insert.class) OrderAfterSalesBo bo) {
        Boolean flag = storeOrderService.afterSalesForm(bo,LoginObject.getLoginId());
        return R.ok(flag,flag?"申请成功":"申请失败");
    }
    /**
     * 我的售后订单列表
     */
    @SaCheckLogin
    @Operation(summary = "我的售后订单列表", description = "我的售后订单列表")
    @GetMapping("/getSalesOrderList")
    public TableDataInfo<StoreRefundApplyVo> getSalesOrderList(Integer pageNum, Integer pageSize, Integer orderStatus, String keywords) {
        return storeOrderService.getSalesOrderList(LoginObject.getLoginId(),orderStatus,pageNum,pageSize,keywords);
    }
    /**
     * 取消售后申请处理
     */
    @SaCheckLogin
    @Operation(summary = "取消售后", description = "取消售后")
    @GetMapping("/untAfterServer")
    public R<Boolean> untAfterServer(String  refundNo) {
        Boolean flag = storeOrderService.untAfterServer(refundNo,LoginObject.getLoginId());
        return R.ok(flag,flag?"操作成功":"操作失败");
    }
    /**
     * 售后详情
     */
    @SaCheckLogin
    @Operation(summary = "售后详情", description = "售后详情")
    @GetMapping("/afterServerInfo")
    public R<StoreRefundApplyVo> afterServerInfo(String  refundNo) {
        return R.success("售后详情",storeOrderService.afterServerInfo(refundNo,LoginObject.getLoginId()));
    }

    /**
     * 确认收货
     */
    @SaCheckLogin
    @Operation(summary = "确认收货", description = "确认收货")
    @GetMapping("/receipt")
    public R<Boolean> receipt(String  orderNo) {
        Boolean flag = storeOrderService.receipt(orderNo,LoginObject.getLoginId());
        return R.ok(flag,flag?"操作成功":"操作失败");
    }

    /**
     * 物流公司列表
     */
    @Operation(summary = "物流公司列表", description = "物流公司列表")
    @GetMapping("/logisticsCompany")
    public R<List<StoreLogisticsCompanyVo>> logisticsCompany() {
        StoreLogisticsCompanyBo bo=new StoreLogisticsCompanyBo();
        bo.setPageSize(1000);
        bo.setPageNum(1);
        TableDataInfo<StoreLogisticsCompanyVo> page = storeLogisticsCompanyService.selectListVoPage(bo, bo.getPageQuery());
        return R.success(page.getRows());
    }

    @SaCheckLogin
    @Operation(summary = "退货订单提交物流信息", description = "退货订单提交物流信息")
    @PostMapping("/orderRefundLogistics")
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) StoreRefundLogisticsBo bo) {
        StoreRefundApplyVo vo= storeRefundApplyService.selectVoByRefundNo(bo.getRefundNo(),LoginObject.getLoginId());
        if(vo==null){
            return R.fail("退货订单不存在");
        }
        if(vo.getStatus()==2){
            return R.fail("当前状态不允许操作");
        }
        bo.setRefundApplyId(vo.getId());
        bo.setTenantId(vo.getTenantId());
        return R.success(storeRefundLogisticsService.insertXml(bo,LoginObject.getLoginId()));
    }


    /**
     * 申请开票
     */
    @SaCheckLogin
    @Operation(summary = "申请开票", description = "申请开票")
    @PostMapping("/addStoreInvoice")
    public R<Boolean> addStoreInvoice(@RequestBody @Validated(ValidatedGroupConfig.insert.class) StoreInvoiceBo bo) {
        StoreOrderVo vo= storeOrderService.selectVoById(bo.getOrderId());
        if(vo==null){
            return R.fail("订单不存在");
        }
        bo.setTenantId(vo.getTenantId());
        bo.setApplyTime(new Date());
        bo.setInvoiceStatus(1);
        Boolean flag = storeInvoiceService.insert(bo);
        return R.ok(flag,flag?"申请成功":"申请失败");
    }
}
