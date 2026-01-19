package com.sxpcwlkj.system.service;

import com.sxpcwlkj.common.code.entity.FileStorageConfig;
import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.system.entity.SysOssConfig;
import com.sxpcwlkj.system.entity.bo.SysOssConfigBo;
import com.sxpcwlkj.system.entity.vo.SysOssConfigVo;

import java.util.List;

/**
 * 对象存储配置表
 *
 * @author mmsAdmin 942879858@qq.com
 * @since 1.0.0 2024-07-07
 */
public interface SysOssConfigService extends BaseService<SysOssConfig, SysOssConfigVo, SysOssConfigBo> {

    /**
     * 查询默认存储
     * @return
     */
    String queryDef();

    /**
     * 根据 Key 查询配置
     * @param key
     * @return
     */
    SysOssConfigVo selectVoByKey(String key);

    /**
     * 设置配置
     * @param bo
     * @return
     */
    Boolean updateByIdXml(SysOssConfigBo bo);

    /**
     * 查询配置列表
     * @return
     */
    List<FileStorageConfig> getOss();

    /**
     * 初始化配置
     * @return
     */
    Boolean initOss();

}
