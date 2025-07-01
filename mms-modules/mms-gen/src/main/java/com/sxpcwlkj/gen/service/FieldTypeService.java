package com.sxpcwlkj.gen.service;

import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.common.service.BaseService;
import com.sxpcwlkj.gen.entity.FieldTypeEntity;

import java.util.Map;
import java.util.Set;

/**
 * 字段类型管理
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
public interface FieldTypeService extends BaseService<FieldTypeEntity> {
    TableDataInfo<FieldTypeEntity> page(GenQueryBo query);

    Map<String, FieldTypeEntity> getMap();

    /**
     * 根据tableId，获取包列表
     *
     * @param tableId 表ID
     * @return 返回包列表
     */
    Set<String> getPackageByTableId(Long tableId);

    Set<String> getList();
}
