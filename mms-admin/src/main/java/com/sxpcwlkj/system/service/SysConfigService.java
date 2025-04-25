package com.sxpcwlkj.system.service;

import com.sxpcwlkj.framework.sercice.BaseService;
import com.sxpcwlkj.system.entity.SysConfig;
import com.sxpcwlkj.system.entity.bo.SysConfigBo;
import com.sxpcwlkj.system.entity.vo.SysConfigVo;

import java.util.List;

/**
 * 系统配置
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-06-10
 */
public interface SysConfigService extends BaseService<SysConfig, SysConfigVo, SysConfigBo> {
    Boolean updateByCodes(List<SysConfigBo> bos);

    List<SysConfigVo> selectByCodes(List<SysConfigBo> bos);

    Boolean initSms();

    Boolean initBase();

    Boolean getIsOpenCaptcha();

    Boolean initEmail();

    Boolean initWx();
}
