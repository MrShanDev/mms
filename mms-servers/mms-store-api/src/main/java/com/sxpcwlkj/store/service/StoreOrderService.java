package com.sxpcwlkj.store.service;

import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.store.entity.bo.*;
import com.sxpcwlkj.store.entity.vo.StoreRefundApplyVo;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreOrder;
import com.sxpcwlkj.store.entity.vo.StoreOrderSpuVo;
import com.sxpcwlkj.store.entity.vo.StoreOrderVo;
import com.sxpcwlkj.store.entity.export.StoreOrderExport;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 订单主表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreOrderService extends BaseService<StoreOrder, StoreOrderVo, StoreOrderBo> {
    /**
    * 导出订单主表
    * @param list 订单主表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreOrderExport> list);

    /**
     *   创建订单
     * @param bo bo
     * @return 订单ID
     */
    String createOrder(AddOrderBo bo);

    /**
     *    支付订单
     * @param orderNo      订单编号
     * @param payType      支付类型
     * @param loginId      登录ID
     * @return  Map<String, Object>
     */
    Map<String, Object> payOrder(String orderNo, String payType, String loginId, String clientType, String price,HttpServletRequest request);

    /**
     *     订单详情
     * @param orderNo       订单编号
     * @param loginId        登录ID
     * @return  StoreOrderCartVo
     */
    StoreOrderVo getOrderDetail(String orderNo, String loginId,String ifReturnInfo,Boolean ifAdmin);

    /**
     *       订单列表
     * @param loginId        登录ID
     * @param orderStatus      订单状态
     * @param pageNum            页码
     * @param pageSize         每页条数
     * @return   List<StoreOrderCartVo>
     */
    TableDataInfo<StoreOrderVo> getOrderList(String loginId, Integer orderStatus, Integer pageNum, Integer pageSize,String keywords);

    /**
     *       订单列表
     * @param loginId        登录ID
     * @param orderStatus      订单状态
     * @param pageNum            页码
     * @param pageSize         每页条数
     * @return   List<StoreOrderCartVo>
     */
    TableDataInfo<StoreOrderVo> getOrderListPc(String loginId, Integer orderStatus, Integer pageNum, Integer pageSize,String keywords);

    /**
     * 支付成功更新订单状态
     * @param outTradeNo 订单编号
     * @param transactionId 支付平台流水号
     * @param mchId 商户号
     * @param payAmount 支付金额
     * @return true：成功 false ：失败
     */
    Boolean updateSuccessPayByOrderNo(String outTradeNo, String transactionId, String mchId, BigDecimal payAmount, Date payTime);

    /**
     *    取消订单
     * @param orderNo 订单编号
     * @param loginId 登录ID
     * @return true：成功 false ：失败
     */
    Boolean cancelOrder(String orderNo, String loginId);

    /**
     *    删除订单
     * @param orderNo 订单编号
     * @param loginId 登录ID
     * @return true：成功 false ：失败
     */
    Boolean deleteOrder(String orderNo, String loginId);

    /**
     * 订单超时
     * @param orderNo 订单编号
     * @return true：成功 false ：失败
     */
    Boolean expiredOrder(String orderNo);

    /**
     *    订单售后信息
     * @param orderNo 订单编号
     * @param loginId 登录ID
     * @param itemId  订单商品ID
     * @return List<StoreOrderSpuVo>
     */
    StoreOrderSpuVo afterSalesInfo(String orderNo, String loginId, String itemId);

    /**
     *    售后申请
     * @param bo bo
     * @return true：成功 false ：失败
     */
    Boolean afterSalesForm(OrderAfterSalesBo bo, String loginId);

    /**
     * 收货
     * @param orderNo 订单编号
     * @param loginId 登录ID
     * @return true：成功 false ：失败
     */
    Boolean receipt(String orderNo, String loginId);

    /**
     * 查询售后订单列表
     * @param loginId 登录ID
     * @param orderStatus 订单状态
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param keywords 关键字
     * @return TableDataInfo<StoreOrderVo>
     */
    TableDataInfo<StoreRefundApplyVo> getSalesOrderList(String loginId, Integer orderStatus, Integer pageNum, Integer pageSize, String keywords);

    /**
     * 添加订退货单物流信息
     * @param refundNo 售后订单编号
     * @param loginId 登录ID
     * @return true：成功 false ：失败
     */
    Boolean addOrderRefundLogistics(String refundNo, String loginId);

    /**
     * 扫描超时订单
     * @return  true：成功 false ：失败
     */
    Boolean scanTimeoutOrders(String params);
    /**
     * 订单确认
     * @param orderNo 订单
     * @param type  0 拒绝 1 通过（进入待发货）
     * @return   true：成功 false ：失败
     */
    Boolean orderConfirm(String orderNo, Integer type);

    /**
     * 退款
     * @param orderNo 订单编号
     * @return  true：成功 false ：失败
     */
    Boolean refund(String transactionNumber,String orderNo,BigDecimal totalFee,BigDecimal refundFee);

    /**
     * 退款通知
     * @param reqInfo
     * @return
     */
    Boolean notifyOrder(WxPayRefundNotifyResult.ReqInfo reqInfo);

    /**
     * 订单发货
     * @param bo 订单
     * @return  true：成功 false ：失败
     */
    Boolean orderDelivery(StoreShipmentBo bo);

    /**
     * 商品订单
     */
    StoreOrderVo selectOrderByOrderNo(String orderNo);

    /**
     * 售后申请处理
     * @param bo bo
     * @return true：成功 false ：失败
     */
    Boolean afterServer(StoreRefundApplyBo bo);

    /**
     * 取消售后申请处理
     * @param refundNo 售后单号
     * @param loginId 登录ID
     * @return  true：成功 false ：失败
     */
    Boolean untAfterServer(String refundNo, String loginId);

    /**
     * 售后申请处理详情
     * @param refundNo 售后单号
     * @param loginId 登录ID
     * @return  详情
     */
    StoreRefundApplyVo afterServerInfo(String refundNo, String loginId);

    /**
     * 退款售后订单
     * @param refundNo 订单编号
     * @param refundFee 退款金额
     * @return  true：成功 false ：失败
     */
    Boolean refundFee(String refundNo,BigDecimal refundFee);
    /**
     * 订单列表
     * @param bo bo
     * @param pageQuery 分页
     * @return ""
     */
    TableDataInfo<StoreOrderVo> selectListVoPageAdmin(StoreOrderBo bo, PageQuery pageQuery);
}
