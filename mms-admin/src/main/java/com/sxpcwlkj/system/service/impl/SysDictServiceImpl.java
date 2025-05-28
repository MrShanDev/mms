package com.sxpcwlkj.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.constant.CacheConstants;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.system.entity.SysDict;
import com.sxpcwlkj.system.entity.SysDictData;
import com.sxpcwlkj.system.entity.bo.SysDictBo;
import com.sxpcwlkj.system.entity.bo.SysDictDataBo;
import com.sxpcwlkj.system.entity.vo.SysDictDataVo;
import com.sxpcwlkj.system.entity.vo.SysDictVo;
import com.sxpcwlkj.system.mapper.SysDictDataMapper;
import com.sxpcwlkj.system.mapper.SysDictMapper;
import com.sxpcwlkj.system.service.SysDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@RequiredArgsConstructor
@Service("sysDict")
@Slf4j
public class SysDictServiceImpl implements SysDictService {

    private final SysDictMapper baseMapper;
    private final SysDictDataMapper sysDictDataMapper;

    @Override
    public TableDataInfo<SysDictVo> selectPageList(SysDictBo bo, PageQuery pageQuery) {
        Page<SysDictVo> page = baseMapper.selectByPage(pageQuery.build(), this.buildQueryWrapper(bo));

        return TableDataInfo.build(page);
    }

    private Wrapper<SysDict> buildQueryWrapper(SysDictBo bo) {
        //Map<String, Object> params = bo.getParams();
        QueryWrapper<SysDict> wrapper = Wrappers.query();
        wrapper.like(ObjectUtil.isNotNull(bo.getName()), "name", bo.getName());
        wrapper.or(ObjectUtil.isNotNull(bo.getName()));
        wrapper.like(ObjectUtil.isNotNull(bo.getName()), "field_name", bo.getName());
        wrapper.orderByAsc("created_time");
        wrapper.orderByAsc("sort");
        return wrapper;
    }

    @Override
    public SysDictVo selectVoById(Long id) {
        SysDictVo sysDictVo = baseMapper.selectVoById(id);
        List<SysDictDataVo> vos = sysDictDataMapper.selectVoList(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getFieldName, sysDictVo.getFieldName()).orderByAsc(SysDictData::getSort));
        if (vos == null) {
            vos = new ArrayList<>();
            SysDictDataVo sysDictDataVo = new SysDictDataVo();
            sysDictDataVo.setDictType("0");
            sysDictDataVo.setStatus("0");
            vos.add(sysDictDataVo);
        }
        sysDictVo.setList(vos);
        return sysDictVo;
    }

    @Override
    public Boolean updateById(SysDictBo bo) {
        int row;
        SysDict convert = MapstructUtil.convert(bo, SysDict.class);

        // 字典值维护
        List<SysDictDataBo> list = bo.getList();
        if (list != null && !list.isEmpty()) {
            sysDictDataMapper.delete(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getFieldName, bo.getFieldName()));
            for (SysDictDataBo data : list) {
                SysDictData convertData = MapstructUtil.convert(data, SysDictData.class);
                assert convertData != null;
                convertData.setId(null);
                convertData.setFieldName(bo.getFieldName());
                sysDictDataMapper.insert(convertData);
            }
        }
        return baseMapper.updateById(convert) > 0;
    }

    @Override
    public Boolean insert(SysDictBo bo) {
        SysDict convert = MapstructUtil.convert(bo, SysDict.class);
        int row = baseMapper.insert(convert);
        // 字典值维护
        List<SysDictDataBo> list = bo.getList();
        if (list != null && !list.isEmpty()) {
            for (SysDictDataBo data : list) {
                SysDictData convertData = MapstructUtil.convert(data, SysDictData.class);
                assert convertData != null;
                convertData.setId(null);
                convertData.setFieldName(bo.getFieldName());
                sysDictDataMapper.insert(convertData);
            }
        }

        return row > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        SysDict bo = baseMapper.selectById(id);
        sysDictDataMapper.delete(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getFieldName, bo.getFieldName()));
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public Integer initSysDict(String code) {
        List<SysDict> dicts = baseMapper.selectList(new LambdaQueryWrapper<SysDict>().eq(SysDict::getStatus, 0).orderByAsc(SysDict::getSort));
        int i = 0;
        for (SysDict d : dicts) {
            List<SysDictData> data = sysDictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                    .eq(SysDictData::getFieldName, d.getFieldName())
                    .eq(SysDictData::getStatus, 0)
                    .orderByAsc(SysDictData::getSort)
            );
            RedisUtil.deleteObject(CacheConstants.SYS_DICT_KEY + d.getFieldName());
            RedisUtil.setCacheObject(CacheConstants.SYS_DICT_KEY + d.getFieldName(), data);
            for (SysDictData v : data) {
                i++;
                RedisUtil.deleteObject(CacheConstants.SYS_DICT_KEY + v.getFieldName() + ":" + v.getValue());
                RedisUtil.setCacheObject(CacheConstants.SYS_DICT_KEY + v.getFieldName() + ":" + v.getValue(), v.getLabel());
            }
        }
        return i;
    }

    @Override
    public List<SysDictDataVo> getSysDictByCode(String code) {
        return sysDictDataMapper.selectVoList(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getFieldName, code)
                .eq(SysDictData::getStatus, 0).orderByAsc(SysDictData::getSort));
    }

    @Override
    public List<Map<String, Object>> selectAll() {
        List<SysDictVo> dataVos = baseMapper.selectVoList(new LambdaQueryWrapper<SysDict>().eq(SysDict::getStatus, 0).orderByAsc(SysDict::getSort));
        List<Map<String, Object>> end = new ArrayList<>();

        for (SysDictVo dict : dataVos) {
            Map<String, Object> map = new HashMap<>();
            map.put("dictType", dict.getFieldName());
            List<SysDictDataVo> dictData = getSysDictByCode(dict.getFieldName());
            List<Map<String, Object>> item = new ArrayList<>();
            for (SysDictDataVo vo : dictData) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("dictLabel", vo.getLabel());
                itemMap.put("dictValue", vo.getValue());
                itemMap.put("labelClass", vo.getColorType());
                item.add(itemMap);
            }
            map.put("dataList", item);

            end.add(map);
        }
        return end;
    }
}

