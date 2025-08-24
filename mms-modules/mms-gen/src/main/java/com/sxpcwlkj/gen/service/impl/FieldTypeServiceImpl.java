package com.sxpcwlkj.gen.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.common.service.impl.BaseServiceImpl;
import com.sxpcwlkj.gen.entity.FieldTypeEntity;
import com.sxpcwlkj.gen.mapper.FieldTypeMapper;
import com.sxpcwlkj.gen.service.FieldTypeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 字段类型管理
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Service
public class FieldTypeServiceImpl extends BaseServiceImpl<FieldTypeMapper, FieldTypeEntity> implements FieldTypeService {

    @Override
    public TableDataInfo<FieldTypeEntity> page(GenQueryBo query) {
        IPage<FieldTypeEntity> page = baseMapper.selectPage(
                getPage(query),
                getWrapper(query)
        );
        return TableDataInfo.build(page);
    }

    @Override
    public Map<String, FieldTypeEntity> getMap() {
        List<FieldTypeEntity> list = baseMapper.selectList(null);
        Map<String, FieldTypeEntity> map = new LinkedHashMap<>(list.size());
        for (FieldTypeEntity entity : list) {
            map.put(entity.getColumnType().toLowerCase(), entity);
        }
        return map;
    }

    @Override
    public Set<String> getPackageByTableId(Long tableId) {
        Set<String> importList = baseMapper.getPackageByTableId(tableId);

        return importList.stream().filter(StrUtil::isNotBlank).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getList() {
        return baseMapper.list();
    }

    @Override
    public boolean save(FieldTypeEntity entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}
