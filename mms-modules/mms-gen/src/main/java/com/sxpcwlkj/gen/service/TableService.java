package com.sxpcwlkj.gen.service;

import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.common.service.BaseService;
import com.sxpcwlkj.gen.entity.TableEntity;

/**
 * 数据表
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
public interface TableService extends BaseService<TableEntity> {

    TableDataInfo<TableEntity> page(GenQueryBo query);

    TableEntity getByTableName(String tableName);

    void deleteBatchIds(Long[] ids);

    /**
     * 导入表
     *
     * @param datasourceId 数据源ID
     * @param tableName    表名
     */
    void tableImport(Long datasourceId, String tableName);

    /**
     * 同步数据库表
     *
     * @param id 表ID
     */
    void sync(Long id);

    TableEntity selectVoById(Long id);
}
