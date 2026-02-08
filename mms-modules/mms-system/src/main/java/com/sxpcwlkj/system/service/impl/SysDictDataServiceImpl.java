package com.sxpcwlkj.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.system.entity.SysDictData;
import com.sxpcwlkj.system.entity.bo.SysDictDataBo;
import com.sxpcwlkj.system.entity.vo.SysDictDataVo;
import com.sxpcwlkj.system.mapper.SysDictDataMapper;
import com.sxpcwlkj.system.service.SysDictDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 字典数据
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@RequiredArgsConstructor
@Service("sysDictData")
@Slf4j
public class SysDictDataServiceImpl implements SysDictDataService {

    private final SysDictDataMapper baseMapper;
    @Override
    public TableDataInfo<SysDictDataVo> selectPageList(SysDictDataBo bo, PageQuery pageQuery) {
        Page<SysDictDataVo> page = baseMapper.selectByPage(pageQuery.build(), this.buildQueryWrapper(bo));

        return TableDataInfo.build(page);
    }

    private Wrapper<SysDictData> buildQueryWrapper(SysDictDataBo bo) {

        QueryWrapper<SysDictData> wrapper = Wrappers.query();
        wrapper.eq(ObjectUtil.isNotNull(bo.getStatus()), "status", SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
        wrapper.like(ObjectUtil.isNotNull(bo.getFieldName()), "name", bo.getFieldName());

        wrapper.orderByAsc("sort");
        return wrapper;
    }


    @Override
    public SysDictDataVo selectVoById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public Boolean updateByIdBase(SysDictDataBo bo) {
        int row;
        SysDictData convert = MapstructUtil.convert(bo, SysDictData.class);
        return baseMapper.updateById(convert)>0;
    }

    @Override
    public Boolean insert(SysDictDataBo bo) {
        SysDictData convert = MapstructUtil.convert(bo, SysDictData.class);
        return baseMapper.insert(convert)>0;
    }

    @Override
    public Boolean deleteById(Serializable ids) {
        String[] array = DataUtil.getCatStr(ids.toString(), ",");
        return baseMapper.deleteByIds(new ArrayList<>(List.of(array)))>0;
    }
}
