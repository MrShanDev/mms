package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreOrderStatusLog;
import com.sxpcwlkj.store.entity.bo.StoreOrderStatusLogBo;
import com.sxpcwlkj.store.entity.vo.StoreOrderStatusLogVo;
import com.sxpcwlkj.store.entity.export.StoreOrderStatusLogExport;
import com.sxpcwlkj.store.mapper.StoreOrderStatusLogMapper;
import com.sxpcwlkj.store.service.StoreOrderStatusLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单状态流水表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_order_status_log")
@RequiredArgsConstructor
public class StoreOrderStatusLogServiceImpl extends BaseServiceImpl<StoreOrderStatusLog, StoreOrderStatusLogVo, StoreOrderStatusLogBo> implements StoreOrderStatusLogService {

   private final StoreOrderStatusLogMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreOrderStatusLog, StoreOrderStatusLogVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreOrderStatusLogBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreOrderStatusLog obj = MapstructUtil.convert(bo, StoreOrderStatusLog.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("订单状态流水表,insert 操作失败", e);
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
            log.error("订单状态流水表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreOrderStatusLogBo bo) {
        try {
            int row;
            StoreOrderStatusLog obj = MapstructUtil.convert(bo, StoreOrderStatusLog.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("订单状态流水表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreOrderStatusLogVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreOrderStatusLogVo> selectListVoPage(StoreOrderStatusLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreOrderStatusLog> lqw = buildQueryWrapper(bo);
        Page<StoreOrderStatusLogVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreOrderStatusLog> buildQueryWrapper(StoreOrderStatusLogBo query){
        if(query==null){
            query=new StoreOrderStatusLogBo();
        }
        LambdaQueryWrapper<StoreOrderStatusLog> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreOrderStatusLogExport> list) {
        return true;
    }

    @Override
    public StoreOrderStatusLogVo selectLastStatus(String orderId, Integer orderState) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<StoreOrderStatusLog>()
            .eq(StoreOrderStatusLog::getOrderId,orderId)
            .eq(StoreOrderStatusLog::getNewStatus,orderState)
            .orderByDesc(StoreOrderStatusLog::getCreatedTime)
            .last("LIMIT 1")
        );
    }
}
