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
import com.sxpcwlkj.store.entity.StoreRefundOperationLog;
import com.sxpcwlkj.store.entity.bo.StoreRefundOperationLogBo;
import com.sxpcwlkj.store.entity.export.StoreRefundOperationLogExport;
import com.sxpcwlkj.store.entity.vo.StoreRefundOperationLogVo;
import com.sxpcwlkj.store.mapper.StoreRefundOperationLogMapper;
import com.sxpcwlkj.store.service.StoreRefundOperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 退款日志-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_refund_operation_log")
@RequiredArgsConstructor
public class StoreRefundOperationLogServiceImpl extends BaseServiceImpl<StoreRefundOperationLog, StoreRefundOperationLogVo, StoreRefundOperationLogBo> implements StoreRefundOperationLogService {

   private final StoreRefundOperationLogMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreRefundOperationLog, StoreRefundOperationLogVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreRefundOperationLogBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreRefundOperationLog obj = MapstructUtil.convert(bo, StoreRefundOperationLog.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("退款日志,insert 操作失败", e);
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
            log.error("退款日志,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreRefundOperationLogBo bo) {
        try {
            int row;
            StoreRefundOperationLog obj = MapstructUtil.convert(bo, StoreRefundOperationLog.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("退款日志,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreRefundOperationLogVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreRefundOperationLogVo> selectListVoPage(StoreRefundOperationLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreRefundOperationLog> lqw = buildQueryWrapper(bo);
        Page<StoreRefundOperationLogVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreRefundOperationLog> buildQueryWrapper(StoreRefundOperationLogBo query){
        if(query==null){
            query=new StoreRefundOperationLogBo();
        }
        LambdaQueryWrapper<StoreRefundOperationLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getRefundApplyId()), StoreRefundOperationLog::getRefundApplyId, query.getRefundApplyId());
        wrapper.eq(StringUtil.isNotEmpty(query.getOperationType()), StoreRefundOperationLog::getOperationType, query.getOperationType());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreRefundOperationLogExport> list) {
        return true;
    }
}
