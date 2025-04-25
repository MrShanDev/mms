package com.sxpcwlkj.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.code.entity.ConfigEntity;
import com.sxpcwlkj.common.constant.CacheConstants;
import com.sxpcwlkj.common.enums.ConfigKeyNum;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.BeanCopyUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.system.entity.SysConfig;
import com.sxpcwlkj.system.entity.bo.SysConfigBo;
import com.sxpcwlkj.system.entity.vo.SysConfigVo;
import com.sxpcwlkj.system.mapper.SysConfigMapper;
import com.sxpcwlkj.system.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统配置
 *
 * @author XIjue
 * @since 1.0.0 2024-06-10
 */
@Slf4j
@Service("sys_config")
@RequiredArgsConstructor
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig, SysConfigVo, SysConfigBo> implements SysConfigService {

    private final SysConfigMapper baseMapper;
    private final ApplicationContext applicationContext;

    @Override
    public BaseMapperPlus<SysConfig, SysConfigVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    public Boolean insert(SysConfigBo bo) {
        int row;
        SysConfig obj = MapstructUtil.convert(bo, SysConfig.class);
        if (obj != null && obj.getValueType() != null && !obj.getValueType().isEmpty()) {
            if ("Array".equals(obj.getValueType())) {
                List<String> list = bo.getConfigValues();
                String result = DataUtil.getListToString(list);
                obj.setConfigValue(result);
            }
        }
        assert obj != null;
        SysConfigVo sysConfigVo = this.getBaseMapper().selectVoOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, obj.getConfigKey()).last("LIMIT 1"));
        if(sysConfigVo!=null){
            throw new MmsException("配置key已存在");
        }
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
    public Boolean updateById(SysConfigBo bo) {
        int row = 0;
        SysConfig obj = MapstructUtil.convert(bo, SysConfig.class);
        if (obj != null && obj.getValueType() != null && !obj.getValueType().isEmpty()) {
            if ("Array".equals(obj.getValueType())) {
                List<String> list = bo.getConfigValues();
                String result = DataUtil.getListToString(list);
                obj.setConfigValue(result);
            }
        }
        if (obj != null && obj.getConfigKey() != null && !obj.getConfigKey().isEmpty()) {
            SysConfig sysConfig = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, obj.getConfigKey()).last("LIMIT 1"));
            if (sysConfig != null) {
                obj.setId(sysConfig.getId());
                obj.setRevision(sysConfig.getRevision());
            }
        }
        row = this.getBaseMapper().updateById(obj);
        return row > 0;
    }

    @Override
    public SysConfigVo selectVoById(Serializable id) {
        SysConfigVo sysConfigVo = this.getBaseMapper().selectVoById(id);
        if (sysConfigVo != null && !sysConfigVo.getConfigValue().isEmpty()) {
            List<String> list = Arrays.asList(sysConfigVo.getConfigValue().split(","));
            sysConfigVo.setConfigValues(list);
            if (!list.isEmpty()) {
                sysConfigVo.setValueType("Array");
            } else {
                sysConfigVo.setValueType("String");
            }
        }
        return sysConfigVo;
    }


    @Override
    public TableDataInfo<SysConfigVo> selectListVoPage(SysConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysConfig> lqw = buildQueryWrapper(bo);
        Page<SysConfigVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }


    private LambdaQueryWrapper<SysConfig> buildQueryWrapper(SysConfigBo query) {
        if (query == null) {
            query = new SysConfigBo();
        }
        //Map<String, Object> params = query.getParams();
        LambdaQueryWrapper<SysConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtil.isNotEmpty(query.getConfigName()), SysConfig::getConfigName, query.getConfigName());
        wrapper.eq(StringUtil.isNotEmpty(query.getConfigKey()), SysConfig::getConfigKey, query.getConfigKey());
        wrapper.eq(StringUtil.isNotEmpty(query.getConfigType()), SysConfig::getConfigType, query.getConfigType());
        wrapper.eq(StringUtil.isNotEmpty(query.getStatus()), SysConfig::getStatus, query.getStatus());
        return wrapper;
    }


    @Override
    public Boolean updateByCodes(List<SysConfigBo> bos) {
        if (bos != null && !bos.isEmpty()) {
            for (SysConfigBo bo : bos) {
                if (bo.getConfigKey() == null || bo.getConfigKey().isEmpty()) {
                    continue;
                }

                this.updateById(bo);
                this.initBase();
                this.initSms();
                this.initEmail();
                applicationContext.publishEvent(new ContextRefreshedEvent(applicationContext));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<SysConfigVo> selectByCodes(List<SysConfigBo> bos) {
        List<SysConfigVo> endList = new ArrayList<>();
        for (SysConfigBo bo : bos) {
            SysConfigVo vo = baseMapper.selectVoOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, bo.getConfigKey()).last("LIMIT 1"));
            if (vo == null) {
                if (bo.getConfigKey() == null || bo.getConfigKey().isEmpty()) {
                    continue;
                }
                bo.setStatus(0);
                bo.setConfigType(1);
                this.insert(bo);
            } else {
                bo.setConfigValue(vo.getConfigValue());
                if (bo.getValueType() != null && !bo.getValueType().isEmpty()) {
                    List<String> list = Arrays.asList(vo.getConfigValue().split(","));
                    if (!list.isEmpty()) {
                        bo.setValueType("Array");
                        //去除空字符串
                        list = list.stream().filter(str -> !str.isEmpty()).collect(Collectors.toList());
                        //去除空格
                        list = list.stream().map(String::trim).collect(Collectors.toList());
                        //去除重复
                        list = list.stream().distinct().collect(Collectors.toList());
                    } else {
                        bo.setValueType("String");
                    }
                    bo.setConfigValues(list);
                }
                bo.setConfigName(vo.getConfigName());
                bo.setConfigType(vo.getConfigType());
                bo.setStatus(vo.getStatus());
                bo.setId(vo.getId());
                bo.setRevision(vo.getRevision());
            }
            SysConfigVo obj = MapstructUtil.convert(bo, SysConfigVo.class);
            endList.add(obj);
        }
        return endList;
    }

    @Override
    public Boolean initSms() {
        List<SysConfig> sysSms = baseMapper.selectList(new LambdaQueryWrapper<SysConfig>().likeRight(SysConfig::getConfigKey, "sys_sms_"));
        //转为Map集合
        List<ConfigEntity> convert = BeanCopyUtil.convert(sysSms, ConfigEntity.class);
        RedisUtil.deleteObject(ConfigKeyNum.config_sms.getKey());
        RedisUtil.setCacheList(ConfigKeyNum.config_sms.getKey(), convert);
        RedisUtil.expire(ConfigKeyNum.config_sms.getKey(), Duration.ofDays(99999999));
        return true;
    }

    @Override
    public Boolean initBase() {
        List<SysConfig> sysSms = baseMapper.selectList(new LambdaQueryWrapper<SysConfig>().likeRight(SysConfig::getConfigKey, "sys_base_"));
        //转为Map集合
        List<ConfigEntity> convert = BeanCopyUtil.convert(sysSms, ConfigEntity.class);
        RedisUtil.deleteObject(ConfigKeyNum.config_base.getKey());
        RedisUtil.setCacheList(ConfigKeyNum.config_base.getKey(), convert);
        RedisUtil.expire(ConfigKeyNum.config_base.getKey(), Duration.ofDays(99999999));
        return true;
    }

    @Override
    public Boolean getIsOpenCaptcha() {
        SysConfigVo config = baseMapper.selectVoOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, "sys_base_captcha_state").last("limit 1"));
        return config != null && Objects.equals(Convert.toInt(config.getConfigValue()), SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
    }

    @Override
    public Boolean initEmail() {
        List<SysConfig> sysSms = baseMapper.selectList(new LambdaQueryWrapper<SysConfig>().likeRight(SysConfig::getConfigKey, "sys_email_"));
        //转为Map集合
        List<ConfigEntity> convert = BeanCopyUtil.convert(sysSms, ConfigEntity.class);
        RedisUtil.deleteObject(ConfigKeyNum.config_email.getKey());
        RedisUtil.setCacheList(ConfigKeyNum.config_email.getKey(), convert);
        RedisUtil.expire(ConfigKeyNum.config_email.getKey(), Duration.ofDays(99999999));
        return true;
    }

    @Override
    public Boolean initWx() {
        List<SysConfig> sysSms = baseMapper.selectList(new LambdaQueryWrapper<SysConfig>().likeRight(SysConfig::getConfigKey, "sys_wx_"));
        //转为Map集合
        List<ConfigEntity> convert = BeanCopyUtil.convert(sysSms, ConfigEntity.class);
        RedisUtil.deleteObject(ConfigKeyNum.config_wx.getKey());
        RedisUtil.setCacheList(ConfigKeyNum.config_wx.getKey(), convert);
        RedisUtil.expire(ConfigKeyNum.config_wx.getKey(), Duration.ofDays(99999999));
        return true;
    }
}
