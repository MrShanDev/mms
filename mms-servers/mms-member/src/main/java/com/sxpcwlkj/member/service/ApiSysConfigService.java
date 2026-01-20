package com.sxpcwlkj.member.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.member.entity.ApiSysConfig;
import com.sxpcwlkj.member.entity.ApiSysConfigVo;
import com.sxpcwlkj.member.entity.bo.ApiSysConfigBo;

import java.util.List;


/**
 * 配置表-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface ApiSysConfigService extends BaseService<ApiSysConfig, ApiSysConfigVo, ApiSysConfigBo> {
    /**
     * 初始化系统配置
     */
    void initStoreSysConfig();
    /**
     *  查询网站配置
     * @return  网站配置
     */
    List<ApiSysConfigVo> selectWebsiteConfigList();
    /**
     * 根据key查询自定义配置
     * @param key 配置key
     * @return 配置
     */
    ApiSysConfigVo selectVoByKey(String key);

}
