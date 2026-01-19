package com.sxpcwlkj.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreRefundApply;
import com.sxpcwlkj.store.entity.bo.OrderAfterSalesBo;
import com.sxpcwlkj.store.entity.bo.StoreRefundApplyBo;
import com.sxpcwlkj.store.entity.export.StoreRefundApplyExport;
import com.sxpcwlkj.store.entity.vo.StoreOrderItemVo;
import com.sxpcwlkj.store.entity.vo.StoreRefundApplyVo;

import java.util.Set;

/**
 * 退款申请表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreRefundApplyService extends BaseService<StoreRefundApply, StoreRefundApplyVo, StoreRefundApplyBo> {
    /**
    * 导出退款申请表
    * @param list 退款申请表列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreRefundApplyExport> list);

    /**
     * 售后申请
     * @param bo bo
     * @return true：成功 false ：失败
     */
    Boolean afterSalesForm(OrderAfterSalesBo bo, StoreOrderItemVo itemVo);

    /**
     * 退款申请
     * @param orderNo 订单编号
     * @return 退款申请
     */
    StoreRefundApplyVo selectVoByOrderNo(String orderNo);

    /**
     * 退款申请列表
     * @param loginId 用户ID
     * @param orderStatus 订单状态
     * @param keywords 关键字
     * @return 退款申请列表
     */
    Page<StoreRefundApplyVo> selectListVoPageXml(Integer pageNum, Integer pageSize, String loginId, Integer orderStatus, String keywords);

    /**
     * 退款申请列表
     */
    StoreRefundApplyVo selectVoByRefundNo(String refundNo,String loginId);
    /**
     * 售后订单提交退货信息
     */
    Boolean updateByIdState(String refundNo, int code);
}
