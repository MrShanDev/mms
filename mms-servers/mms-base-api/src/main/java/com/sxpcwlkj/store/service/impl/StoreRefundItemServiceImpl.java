package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreRefundItem;
import com.sxpcwlkj.store.entity.bo.StoreRefundItemBo;
import com.sxpcwlkj.store.entity.export.StoreRefundItemExport;
import com.sxpcwlkj.store.entity.vo.StoreRefundItemVo;
import com.sxpcwlkj.store.mapper.StoreRefundItemMapper;
import com.sxpcwlkj.store.service.StoreRefundItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 退款商品明细表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_refund_item")
@RequiredArgsConstructor
public class StoreRefundItemServiceImpl extends BaseServiceImpl<StoreRefundItem, StoreRefundItemVo, StoreRefundItemBo> implements StoreRefundItemService {

   private final StoreRefundItemMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreRefundItem, StoreRefundItemVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreRefundItemBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreRefundItem obj = MapstructUtil.convert(bo, StoreRefundItem.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("退款商品明细表,insert 操作失败", e);
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
            log.error("退款商品明细表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreRefundItemBo bo) {
        try {
            int row;
            StoreRefundItem obj = MapstructUtil.convert(bo, StoreRefundItem.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("退款商品明细表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreRefundItemVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreRefundItemVo> selectListVoPage(StoreRefundItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreRefundItem> lqw = buildQueryWrapper(bo);
        Page<StoreRefundItemVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreRefundItem> buildQueryWrapper(StoreRefundItemBo query){
        if(query==null){
            query=new StoreRefundItemBo();
        }
        LambdaQueryWrapper<StoreRefundItem> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreRefundItemExport> list) {
        return true;
    }
}
