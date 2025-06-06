package com.sxpcwlkj.docApi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.docApi.entity.DocOrder;
import com.sxpcwlkj.docApi.entity.bo.DocOrderBo;
import com.sxpcwlkj.docApi.entity.vo.DocOrderVo;
import com.sxpcwlkj.docApi.entity.vo.DocUserVo;
import com.sxpcwlkj.docApi.mapper.DocOrderMapper;
import com.sxpcwlkj.docApi.service.DocOrderService;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author shanpengnian
 */
@Slf4j
@Transactional
@Service("doc_order")
@RequiredArgsConstructor
public class DocOrderServiceImpl extends BaseServiceImpl<DocOrder, DocOrderVo, DocOrderBo> implements DocOrderService {

    private final DocOrderMapper baseMapper;

    @Override
    public BaseMapperPlus<DocOrder, DocOrderVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(DocOrderBo bo) {
        try {
            int row;
            bo.setOrderId(null);
            DocOrder obj = MapstructUtil.convert(bo, DocOrder.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setOrderId(obj.getOrderId());
            return row > 0;
        } catch (Exception e) {
            log.error("文档订单,insert 操作失败", e);
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
            log.error("文档订单,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(DocOrderBo bo) {
        try {
            int row;
            DocOrder obj = MapstructUtil.convert(bo, DocOrder.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("文档订单,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public DocOrderVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<DocOrderVo> selectListVoPage(DocOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DocOrder> lqw = buildQueryWrapper(bo);
        Page<DocOrderVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<DocOrder> buildQueryWrapper(DocOrderBo query){
        if(query==null){
            query=new DocOrderBo();
        }
        LambdaQueryWrapper<DocOrder> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean create(DocUserVo docUserVo, Map<String, Object> orderInfo) {
        DocOrderBo docOrderBo= new DocOrderBo();
        docOrderBo.setUid(docUserVo.getUid());
        docOrderBo.setTxnAmt(new BigDecimal(orderInfo.get("payPrice").toString()));
        docOrderBo.setProdId(orderInfo.get("productId").toString());
        docOrderBo.setProdName(orderInfo.get("productTitle").toString());
        docOrderBo.setProdPrice(new BigDecimal(orderInfo.get("payPrice").toString()));
        docOrderBo.setProdType(orderInfo.get("productType").toString());
        docOrderBo.setStatus(0);
        docOrderBo.setPayNo(orderInfo.get("orderNo").toString());
        docOrderBo.setPayTimeout(orderInfo.get("expireTime").toString());
        docOrderBo.setCtime(new Date());
        docOrderBo.setMtime(new Date());

       return this.insert(docOrderBo);
    }

    @Override
    public String selectPayState(String orderNo,String uid) {
        DocOrder docOrder= baseMapper.selectOne(new LambdaQueryWrapper<DocOrder>()
            .eq(DocOrder::getPayNo,orderNo).eq(DocOrder::getUid,uid)
            .eq(DocOrder::getStatus,1)
            .orderByDesc(DocOrder::getCtime)
        );
        if(docOrder!=null){
            return "finish";
        }
        return "unpaid";
    }

    @Override
    public Boolean updateByOrderNo(String transactionId) {
        DocOrder docOrder= baseMapper.selectOne(new LambdaQueryWrapper<DocOrder>().eq(DocOrder::getPayNo,transactionId));
        if(docOrder!=null){
            docOrder.setStatus(1);
            docOrder.setMtime(new Date());
            return baseMapper.updateById(docOrder)>0;
        }
        return false;
    }
}
