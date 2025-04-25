package com.sxpcwlkj.gen.service;


import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.common.service.BaseService;
import com.sxpcwlkj.gen.config.GenDataSource;
import com.sxpcwlkj.gen.entity.DataSourceEntity;

import java.util.List;

/**
 * 数据源管理
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
public interface DataSourceService extends BaseService<DataSourceEntity> {

    TableDataInfo<DataSourceEntity> page(GenQueryBo query);

    List<DataSourceEntity> getList();

    /**
     * 获取数据库产品名，如：MySQL
     *
     * @param datasourceId 数据源ID
     * @return 返回产品名
     */
    String getDatabaseProductName(Long datasourceId);

    /**
     * 根据数据源ID，获取数据源
     *
     * @param datasourceId 数据源ID
     */
    GenDataSource get(Long datasourceId);
}
