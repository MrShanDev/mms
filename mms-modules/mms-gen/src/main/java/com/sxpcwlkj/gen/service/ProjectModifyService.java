package com.sxpcwlkj.gen.service;

import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.gen.common.GenQueryBo;
import com.sxpcwlkj.gen.common.service.BaseService;
import com.sxpcwlkj.gen.entity.ProjectModifyEntity;

import java.io.IOException;

/**
 * 项目名变更
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
public interface ProjectModifyService extends BaseService<ProjectModifyEntity> {

    TableDataInfo<ProjectModifyEntity> page(GenQueryBo query);

    byte[] download(ProjectModifyEntity project) throws IOException;

}
