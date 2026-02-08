package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.system.entity.SysLog;
import com.sxpcwlkj.system.entity.bo.SysLogBo;
import com.sxpcwlkj.system.entity.vo.SysLogVo;
import com.sxpcwlkj.system.entity.export.SysLogExport;
import com.sxpcwlkj.system.mapper.SysLogMapper;
import com.sxpcwlkj.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志记录表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("sys_log")
@RequiredArgsConstructor
public class SysLogServiceImpl extends BaseServiceImpl<SysLog, SysLogVo,SysLogBo> implements SysLogService {

   private final SysLogMapper baseMapper;

    @Override
    public BaseMapperPlus<SysLog, SysLogVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(SysLogBo bo) {
        try {
            int row;
            bo.setOperId(null);
            SysLog obj = MapstructUtil.convert(bo, SysLog.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setOperId(obj.getOperId());
            return row > 0;
        } catch (Exception e) {
            log.error("操作日志记录表,insert 操作失败", e);
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
            log.error("操作日志记录表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateByIdBase(SysLogBo bo) {
        try {
            int row;
            SysLog obj = MapstructUtil.convert(bo, SysLog.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("操作日志记录表,updateByIdBase 操作失败", e);
            throw e;
        }
    }

    @Override
    public SysLogVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<SysLogVo> selectListVoPage(SysLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysLog> lqw = buildQueryWrapper(bo);
        Page<SysLogVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<SysLog> buildQueryWrapper(SysLogBo query){
        if(query==null){
            query=new SysLogBo();
        }
        LambdaQueryWrapper<SysLog> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(SysLog::getOperTime);
        return wrapper;
    }

    @Override
    public Boolean imports(Set<SysLogExport> list) {
        return true;
    }
}
