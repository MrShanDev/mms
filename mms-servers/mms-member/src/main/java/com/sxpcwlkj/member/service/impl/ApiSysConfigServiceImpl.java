package com.sxpcwlkj.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.member.entity.StoreSysConfig;
import com.sxpcwlkj.member.entity.StoreSysConfigVo;
import com.sxpcwlkj.member.entity.bo.StoreSysConfigBo;
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
public class ApiSysConfigServiceImpl extends BaseServiceImpl<StoreSysConfig, StoreSysConfigVo, StoreSysConfigBo> implements ApiSysConfigService {

   private final ApiSysConfigMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreSysConfig, StoreSysConfigVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    public StoreSysConfigVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreSysConfigVo> selectListVoPage(StoreSysConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreSysConfig> lqw = buildQueryWrapper(bo);
        Page<StoreSysConfigVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreSysConfig> buildQueryWrapper(StoreSysConfigBo query){
        if(query==null){
            query=new StoreSysConfigBo();
        }
        LambdaQueryWrapper<StoreSysConfig> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }


    @Override
    public void initStoreSysConfig() {
       StoreSysConfigVo idVo =  baseMapper.selectVoOne(new LambdaQueryWrapper<StoreSysConfig>()
            .eq(StoreSysConfig::getConfigKey,"kdniao:id")
            .eq(StoreSysConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .eq(StoreSysConfig::getConfigType,2)
            .last("LIMIT 1"));
       if(idVo!=null) {
           RedisUtil.setCacheObject("kdniao:id",idVo.getConfigValue());
       }
        StoreSysConfigVo keyVo =  baseMapper.selectVoOne(new LambdaQueryWrapper<StoreSysConfig>()
            .eq(StoreSysConfig::getConfigKey,"kdniao:key")
            .eq(StoreSysConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .eq(StoreSysConfig::getConfigType,2)
            .last("LIMIT 1"));
       if (keyVo!=null){
           RedisUtil.setCacheObject("kdniao:key",keyVo.getConfigValue());
       }
    }

    @Override
    public List<StoreSysConfigVo> selectWebsiteConfigList() {
        return baseMapper.selectVoList(new LambdaQueryWrapper<StoreSysConfig>()
            .likeRight(StoreSysConfig::getConfigKey,"website_")
            .eq(StoreSysConfig::getConfigType,2)
            .eq(StoreSysConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByAsc(StoreSysConfig::getSort)
        );
    }

    @Override
    public StoreSysConfigVo selectVoByKey(String key) {
        //前缀是website_
        if(key.startsWith("website_")){
            return baseMapper.selectVoOne(new LambdaQueryWrapper<StoreSysConfig>()
                .eq(StoreSysConfig::getConfigKey,key)
                .eq(StoreSysConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
                .eq(StoreSysConfig::getConfigType,2)
                .last("LIMIT 1"));
        }
        return null;
    }
}
