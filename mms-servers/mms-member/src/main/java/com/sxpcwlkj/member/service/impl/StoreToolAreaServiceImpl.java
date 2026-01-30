package com.sxpcwlkj.member.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.member.entity.StoreToolArea;
import com.sxpcwlkj.member.entity.bo.StoreToolAreaBo;
import com.sxpcwlkj.member.entity.vo.StoreToolAreaVo;
import com.sxpcwlkj.member.entity.export.StoreToolAreaExport;
import com.sxpcwlkj.member.mapper.StoreToolAreaMapper;
import com.sxpcwlkj.member.service.StoreToolAreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
/**
 * 行政区域-接口实现
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_tool_area")
@RequiredArgsConstructor
public class StoreToolAreaServiceImpl extends BaseServiceImpl<StoreToolArea, StoreToolAreaVo,StoreToolAreaBo> implements StoreToolAreaService {

    private final StoreToolAreaMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreToolArea, StoreToolAreaVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreToolAreaBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreToolArea obj = MapstructUtil.convert(bo, StoreToolArea.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("行政区域,insert 操作失败", e);
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
            log.error("行政区域,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreToolAreaBo bo) {
        try {
            int row;
            StoreToolArea obj = MapstructUtil.convert(bo, StoreToolArea.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("行政区域,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreToolAreaVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreToolAreaVo> selectListVoPage(StoreToolAreaBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreToolArea> lqw = buildQueryWrapper(bo);
        Page<StoreToolAreaVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreToolArea> buildQueryWrapper(StoreToolAreaBo query){
        if(query==null){
            query=new StoreToolAreaBo();
        }
        LambdaQueryWrapper<StoreToolArea> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreToolAreaExport> list) {
        return true;
    }
}

