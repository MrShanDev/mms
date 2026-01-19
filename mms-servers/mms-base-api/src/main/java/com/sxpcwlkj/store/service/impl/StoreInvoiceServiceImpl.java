package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreInvoice;
import com.sxpcwlkj.store.entity.bo.StoreInvoiceBo;
import com.sxpcwlkj.store.entity.vo.StoreInvoiceVo;
import com.sxpcwlkj.store.entity.export.StoreInvoiceExport;
import com.sxpcwlkj.store.mapper.StoreInvoiceMapper;
import com.sxpcwlkj.store.service.StoreInvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单发票表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_invoice")
@RequiredArgsConstructor
public class StoreInvoiceServiceImpl extends BaseServiceImpl<StoreInvoice, StoreInvoiceVo,StoreInvoiceBo> implements StoreInvoiceService {

   private final StoreInvoiceMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreInvoice, StoreInvoiceVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreInvoiceBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreInvoice obj = MapstructUtil.convert(bo, StoreInvoice.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("订单发票表,insert 操作失败", e);
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
            log.error("订单发票表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreInvoiceBo bo) {
        try {
            int row;
            if(bo.getDownloadUrl()!=null&& !bo.getDownloadUrl().isEmpty()){
                bo.setInvoiceTime(new Date());
                bo.setStatus(2);
            }
            StoreInvoice obj = MapstructUtil.convert(bo, StoreInvoice.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("订单发票表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreInvoiceVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreInvoiceVo> selectListVoPage(StoreInvoiceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreInvoice> lqw = buildQueryWrapper(bo);
        Page<StoreInvoiceVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreInvoice> buildQueryWrapper(StoreInvoiceBo query){
        if(query==null){
            query=new StoreInvoiceBo();
        }
        LambdaQueryWrapper<StoreInvoice> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getOrderId()), StoreInvoice::getOrderId, query.getOrderId());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreInvoiceExport> list) {
        return true;
    }
}
