package com.sxpcwlkj.system.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.entity.CaptchaEntity;
import com.sxpcwlkj.common.enums.DeviceEnum;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.LoginException;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.exception.TenantException;
import com.sxpcwlkj.common.properties.TenantProperties;
import com.sxpcwlkj.common.utils.IPUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.framework.utils.SignUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.system.entity.SysTenant;
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.entity.bo.LoginBodyBo;
import com.sxpcwlkj.system.mapper.SysUserMapper;
import com.sxpcwlkj.system.entity.SysTenantPackage;
import com.sxpcwlkj.system.service.SysConfigService;
import com.sxpcwlkj.system.service.SysLoginService;
import com.sxpcwlkj.system.service.SysTenantPackageService;
import com.sxpcwlkj.system.service.SysTenantService;
import com.sxpcwlkj.system.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 系统登录
 * @Author mmsAdmin
 * @Date 2022/12/25 12:06
 */
@RequiredArgsConstructor
@Service("sysLogin")
@Slf4j
public class SysLoginServiceImpl implements SysLoginService {

    private final SysTenantService sysTenantService;

    private final SysTenantPackageService sysTenantPackageService;

    private final SysUserService sysUserService;

    private final SysUserMapper sysUserMapper;

    private final TenantProperties tenantProperties;

    private final SysConfigService sysConfigService;

    @Override
    public String login(HttpServletRequest request,LoginBodyBo loginBodyBo) {
        //验证验证码
        verfyCaptcha(loginBodyBo.getUsername(), loginBodyBo.getCodeKey(), loginBodyBo.getCode(), loginBodyBo.getUuid());

        //用户名与密码验证
        SysUser sysUser = sysUserService.selectByUserName(loginBodyBo.getTenantId(), loginBodyBo.getUsername());
        // 效验
        if (null == sysUser) {
            throw new LoginException("账号不存在!");
        }
        // 账号是否禁用
        if (!sysUser.getStatus().equals(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())) {
            throw new LoginException("账号已禁用!");
        }
        //解密数据库的密码
        String pressWord = SignUtil.pressWord(loginBodyBo.getPassword(), sysUser.getAesKey());
        try {
            Assert.equals(pressWord, sysUser.getPassword());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new LoginException("账号与密码有误!");
        }
        //验证租户
        verfyTenement(sysUser);
        //进行登记登记
        updateSysUser(request,sysUser);
        //获取当前会话的token值
        return LoginObject.loginToken(sysUser.getUserId(), DeviceEnum.ADMIN.getType(), 10000000L, "id", sysUser.getUserId());
    }

    @Override
    public void logout() {
        LoginObject.logout();
    }


    /**
     * 验证码验证
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid uuid
     */
    @Override
    public void verfyCaptcha(String username, String codeKey, String code, String uuid) {

        if(sysConfigService.getIsOpenCaptcha()){
            if(StringUtil.isEmpty(code)){
                throw new MmsException("The verification code cannot be empty");
            }
            Map<String, Object> data = RedisUtil.getCacheObject(uuid);
            if (ObjectUtils.isEmpty(data)) {
                log.info("登录：验证码失效！");
                throw new LoginException("Verification code failure");
            }
            if (!codeKey.equals(data.get(CaptchaEntity.CODE.CAPTCHA_KEY.getValue()))) {
                log.info("登录：验证码与验证对象不匹配！");
                throw new LoginException("Verification code does not match the verification object");
            }
            if (!code.equalsIgnoreCase((String) data.get(CaptchaEntity.CODE.CAPTCHA_VALUE.getValue()))) {
                log.info("登录：验证码不匹配！");
                throw new LoginException("Verification code does not match");
            }
        }
        return;
    }

    /**
     * 验证租户
     *
     * @param sysUser 用户
     */
    @Override
    public void verfyTenement(SysUser sysUser) {
        if (tenantProperties.getEnable()) {
            //租户
            SysTenant sysTenant = sysTenantService.selectById(sysUser.getTenantId());
            if (ObjectUtils.isEmpty(sysTenant)) {
                log.info("登录用户ID：{} 租户不存在.", sysUser.getUserId());
                throw new TenantException("tenant.not.exists");
            }
            if (!sysTenant.getStatus().equals(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())) {
                log.info("登录用户ID：{} 租户状态为禁用.", sysUser.getUserId());
                throw new TenantException("tenant off state");
            }
            if (sysTenant.getExpireTime() != null && !sysTenant.getExpireTime().after(new Date())) {
                log.info("登录用户ID：{} 租户已到过期时间.", sysUser.getUserId());
                throw new TenantException("租户已过期，请联系管理员续期");
            }
            if (StringUtil.isNotEmpty(sysTenant.getPackageId())) {
                SysTenantPackage pkg = sysTenantPackageService.findById(sysTenant.getPackageId()).orElse(null);
                if (pkg == null) {
                    log.info("登录用户ID：{} 关联套餐不存在: {}", sysUser.getUserId(), sysTenant.getPackageId());
                    throw new TenantException("租户套餐不存在或已删除");
                }
                if (pkg.getStatus() != null
                        && !pkg.getStatus().equals(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())) {
                    log.info("登录用户ID：{} 租户套餐已停用: {}", sysUser.getUserId(), sysTenant.getPackageId());
                    throw new TenantException("租户套餐已停用");
                }
            }

        }

    }

    @Override
    public void updateSysUser(HttpServletRequest request,SysUser sysUser) {
        sysUser.setLoginIp(IPUtil.getIp(request));
        sysUser.setLoginDate(new Date());
        sysUserMapper.updateIpById(sysUser);


    }


}
