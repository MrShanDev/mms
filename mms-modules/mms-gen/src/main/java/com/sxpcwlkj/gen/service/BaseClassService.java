package com.sxpcwlkj.gen.service;

import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.common.service.BaseService;
import com.sxpcwlkj.gen.entity.BaseClassEntity;

import java.util.List;

/**
 * 基类管理
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
public interface BaseClassService extends BaseService<BaseClassEntity> {

    TableDataInfo<BaseClassEntity> page(GenQueryBo query);

    List<BaseClassEntity> getList();
}
