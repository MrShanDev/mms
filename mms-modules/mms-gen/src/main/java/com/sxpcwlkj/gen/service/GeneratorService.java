package com.sxpcwlkj.gen.service;


import com.sxpcwlkj.gen.entity.Preview;

import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
public interface GeneratorService {

    void downloadCode(Long tableId, ZipOutputStream zip);

    void generatorCode(Long tableId);

    List<Preview> preview(Long tableId);

    /**
     * 执行生成的SQL
     * @param tableId 表ID（可选）
     * @param datasourceId 数据源ID（可选，与tableId二选一）
     * @param sql SQL内容
     * @return 执行结果或数据列表
     */
    Object executeSql(Long tableId, Long datasourceId, String sql);
}
