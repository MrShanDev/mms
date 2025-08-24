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
}
