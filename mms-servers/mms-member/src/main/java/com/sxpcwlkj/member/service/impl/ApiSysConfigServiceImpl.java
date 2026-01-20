package com.sxpcwlkj.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.member.entity.ApiSysConfig;
import com.sxpcwlkj.member.entity.ApiSysConfigVo;
import com.sxpcwlkj.member.entity.bo.ApiSysConfigBo;
import com.sxpcwlkj.member.mapper.ApiSysConfigMapper;
import com.sxpcwlkj.member.service.ApiSysConfigService;
import com.sxpcwlkj.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 配置表-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_sys_config")
@RequiredArgsConstructor
public class ApiSysConfigServiceImpl extends BaseServiceImpl<ApiSysConfig, ApiSysConfigVo, ApiSysConfigBo> implements ApiSysConfigService {

   private final ApiSysConfigMapper baseMapper;

    @Override
    public BaseMapperPlus<ApiSysConfig, ApiSysConfigVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    public ApiSysConfigVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<ApiSysConfigVo> selectListVoPage(ApiSysConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ApiSysConfig> lqw = buildQueryWrapper(bo);
        Page<ApiSysConfigVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<ApiSysConfig> buildQueryWrapper(ApiSysConfigBo query){
        if(query==null){
            query=new ApiSysConfigBo();
        }
        LambdaQueryWrapper<ApiSysConfig> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }


    @Override
    public void initStoreSysConfig() {
       ApiSysConfigVo idVo =  baseMapper.selectVoOne(new LambdaQueryWrapper<ApiSysConfig>()
            .eq(ApiSysConfig::getConfigKey,"kdniao:id")
            .eq(ApiSysConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .eq(ApiSysConfig::getConfigType,2)
            .last("LIMIT 1"));
       if(idVo!=null) {
           RedisUtil.setCacheObject("kdniao:id",idVo.getConfigValue());
       }
        ApiSysConfigVo keyVo =  baseMapper.selectVoOne(new LambdaQueryWrapper<ApiSysConfig>()
            .eq(ApiSysConfig::getConfigKey,"kdniao:key")
            .eq(ApiSysConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .eq(ApiSysConfig::getConfigType,2)
            .last("LIMIT 1"));
       if (keyVo!=null){
           RedisUtil.setCacheObject("kdniao:key",keyVo.getConfigValue());
       }
    }

    @Override
    public List<ApiSysConfigVo> selectWebsiteConfigList() {
        return baseMapper.selectVoList(new LambdaQueryWrapper<ApiSysConfig>()
            .likeRight(ApiSysConfig::getConfigKey,"website_")
            .eq(ApiSysConfig::getConfigType,2)
            .eq(ApiSysConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByAsc(ApiSysConfig::getSort)
        );
    }

    @Override
    public ApiSysConfigVo selectVoByKey(String key) {
        //前缀是website_
        if(key.startsWith("website_")){
            return baseMapper.selectVoOne(new LambdaQueryWrapper<ApiSysConfig>()
                .eq(ApiSysConfig::getConfigKey,key)
                .eq(ApiSysConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
                .eq(ApiSysConfig::getConfigType,2)
                .last("LIMIT 1"));
        }
        return null;
    }
}
