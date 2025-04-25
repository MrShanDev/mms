package com.sxpcwlkj.gen.service;


import com.sxpcwlkj.gen.common.service.BaseService;
import com.sxpcwlkj.gen.entity.TableFieldEntity;

import java.util.List;

/**
 * 表字段
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
public interface TableFieldService extends BaseService<TableFieldEntity> {

    List<TableFieldEntity> getByTableId(Long tableId);

    void deleteBatchTableIds(Long[] tableIds);

    /**
     * 修改表字段数据
     *
     * @param tableId        表ID
     * @param tableFieldList 字段列表
     */
    void updateTableField(Long tableId, List<TableFieldEntity> tableFieldList);

    /**
     * 初始化字段数据
     */
    void initFieldList(List<TableFieldEntity> tableFieldList);
}
