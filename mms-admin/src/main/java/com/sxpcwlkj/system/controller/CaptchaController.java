package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.CaptchaEntity;
import com.sxpcwlkj.common.properties.MsProperties;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.system.service.CaptchaService;
import com.sxpcwlkj.system.service.SysTenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码处理类
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("system/visitor")
public class CaptchaController extends BaseController {
    private final SysTenantService sysTenantService;
    private final CaptchaService captchaService;
    private final MsProperties msProperties;

    /**
     * 系统配置
     */
    @SaIgnore
    @GetMapping("/sysInfo")
    public R<Map<String,Object>> sysInfo() {
        Map<String,Object> map=new HashMap<>();
        map.put("sysInfo",msProperties);
        map.put("tenantList",sysTenantService.selectOpenList());
        return success(map);
    }

    /**
     * 登录验证码
     *
     */
    @SaIgnore
    @GetMapping("/{code}")
    public R<Object> captcha(@PathVariable String code) {

        if(StringUtils.isEmpty(code)){
            return R.fail("缺少验证[code]参数");
        }
        Map<String, Object> ajax = captchaService.getCaptcha(CaptchaEntity.TYPE.系统登录.getValue(), code);
        DataUtil.getString(ajax.get(CaptchaEntity.CODE.CAPTCHA.getValue()));
        return R.success(ajax);
    }

}
