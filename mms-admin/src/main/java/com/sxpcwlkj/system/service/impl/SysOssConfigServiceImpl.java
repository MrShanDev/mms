package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.code.entity.FileStorageConfig;
import com.sxpcwlkj.common.enums.ConfigKeyNum;
import com.sxpcwlkj.common.enums.OssTypeEnum;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import com.sxpcwlkj.oss.service.MyFileStorageService;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.system.entity.SysOssConfig;
import com.sxpcwlkj.system.entity.bo.SysOssConfigBo;
import com.sxpcwlkj.system.entity.vo.SysOssConfigVo;
import com.sxpcwlkj.system.mapper.SysOssConfigMapper;
import com.sxpcwlkj.system.service.SysOssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


/**
 * 对象存储配置表
 *
 * @author mmsAdmin 942879858@qq.com
 * @since 1.0.0 2024-07-07
 */
@Slf4j
@Service("sys_oss_config")
@RequiredArgsConstructor
public class SysOssConfigServiceImpl extends BaseServiceImpl<SysOssConfig, SysOssConfigVo, SysOssConfigBo> implements SysOssConfigService {

    private final SysOssConfigMapper baseMapper;

    private final MyFileStorageService myFileStorageService;

    @Override
    public BaseMapperPlus<SysOssConfig, SysOssConfigVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    public Boolean insert(SysOssConfigBo bo) {
        int row;
        SysOssConfig obj = MapstructUtil.convert(bo, SysOssConfig.class);
        row = this.getBaseMapper().insert(obj);
        return row > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        String[] array = DataUtil.getCatStr(ids.toString(), ",");
        return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
    }

    @Override
    public Boolean updateById(SysOssConfigBo bo) {
        int row;
        SysOssConfig obj = MapstructUtil.convert(bo, SysOssConfig.class);
        row = this.getBaseMapper().updateById(obj);
        return row > 0;
    }

    @Override
    public SysOssConfigVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);
    }


    @Override
    public TableDataInfo<SysOssConfigVo> selectListVoPage(SysOssConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOssConfig> lqw = buildQueryWrapper(bo);
        Page<SysOssConfigVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    private LambdaQueryWrapper<SysOssConfig> buildQueryWrapper(SysOssConfigBo query) {
        if (query == null) {
            query = new SysOssConfigBo();
        }
        //Map<String, Object> params = query.getParams();
        LambdaQueryWrapper<SysOssConfig> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public String queryDef() {
        SysOssConfigVo configVo = baseMapper.selectVoOne(new LambdaQueryWrapper<SysOssConfig>().eq(SysOssConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()).last("LIMIT 1"));
        if(configVo!=null){
            return configVo.getConfigKey();
        }
        return OssTypeEnum.local.getType();
    }

    @Override
    public SysOssConfigVo selectVoByKey(String key) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<SysOssConfig>().eq(SysOssConfig::getConfigKey, key));
    }


    @Override
    public Boolean updateByIdXml(SysOssConfigBo bo) {
        int row;
        SysOssConfigVo configVo = baseMapper.selectVoOne(new LambdaQueryWrapper<SysOssConfig>().eq(SysOssConfig::getConfigKey, bo.getConfigKey()));
        if (configVo != null) {
            if (bo.getStatus().toString().equals(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue().toString()) ) {
                baseMapper.update(null, new LambdaUpdateWrapper<SysOssConfig>().set(SysOssConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_CLOSE.getValue()).eq(SysOssConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()));
            }
            bo.setId(configVo.getId());
            bo.setRevision(configVo.getRevision());
            row = baseMapper.updateById(MapstructUtil.convert(bo, SysOssConfig.class));
        } else {
            if (bo.getStatus().toString().equals(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue().toString()) ) {
                baseMapper.update(null, new LambdaUpdateWrapper<SysOssConfig>().set(SysOssConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_CLOSE.getValue()).eq(SysOssConfig::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()));
            }
            row = baseMapper.insert(MapstructUtil.convert(bo, SysOssConfig.class));
        }
        if (row > 0) {
            this.getOss();
            return myFileStorageService.initFileService();
        }

        return false;
    }

    @Override
    public List<FileStorageConfig> getOss() {
        //
        List<SysOssConfigVo> vos = baseMapper.selectVoList(new LambdaQueryWrapper<SysOssConfig>().orderByDesc(SysOssConfig::getStatus));
        List<FileStorageConfig> list = new ArrayList<>();
        for (SysOssConfigVo configVo : vos) {
            if (configVo != null) {
                FileStorageConfig config = new FileStorageConfig();
                config.setPlatform(configVo.getConfigKey());
                config.setAccessKey(configVo.getAccessKey());
                config.setSecretKey(configVo.getSecretKey());
                config.setBucketName(configVo.getBucketName());
                config.setEndPoint(configVo.getEndpoint());
                config.setDomain(configVo.getDomain());
                config.setBasePath(configVo.getPrefix());
                config.setRegion(configVo.getRegion());
                config.setStoragePath(configVo.getExt1());
                list.add(config);
            }
        }

        RedisUtil.deleteObject(ConfigKeyNum.config_oss.getKey());
        RedisUtil.setCacheList(ConfigKeyNum.config_oss.getKey(), list);
        RedisUtil.expire(ConfigKeyNum.config_oss.getKey(), Duration.ofDays(99999999));

        return list;
    }

    @Override
    public Boolean initOss() {
        this.getOss();
        return myFileStorageService.initFileService();
    }




}
