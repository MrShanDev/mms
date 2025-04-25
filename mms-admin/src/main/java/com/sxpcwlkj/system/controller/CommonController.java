package com.sxpcwlkj.system.controller;
import com.sxpcwlkj.common.code.entity.WxCodeBo;
import com.sxpcwlkj.common.constant.Constants;
import com.sxpcwlkj.common.enums.DeviceEnum;
import com.sxpcwlkj.common.enums.WxCodeStatusEnum;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.framework.sercice.SysSignService;
import com.sxpcwlkj.redis.constant.RedisConstant;
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.service.SysLoginService;
import com.sxpcwlkj.wx.service.WxCodeService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.ConfigEntity;
import com.sxpcwlkj.common.enums.ConfigKeyNum;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.email.service.EmailService;
import com.sxpcwlkj.common.utils.IPUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.sms.service.SmsService;
import com.sxpcwlkj.system.entity.AdminMenuTree;
import com.sxpcwlkj.system.entity.bo.EmailBo;
import com.sxpcwlkj.system.entity.bo.SmsBo;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import com.sxpcwlkj.system.service.SysDictService;
import com.sxpcwlkj.system.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.*;

/**
 * mms公共接口
 * @author xijue
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("common")
public class CommonController extends BaseController {
    private final SmsService smsService;
    private final EmailService emailService;
    private final SysUserService sysUserService;
    private final SysLoginService sysLoginService;
    private final Environment environment;
    private final SysDictService sysDictService;
    private final WxCodeService wxCodeService;
    private  final SysSignService sysSignService;

    @Value("${server.port}")
    private String port;
    /**
     * 获取系统基础配置
     */
    @PostMapping("/startBase")
    @SaIgnore
    public R<Object> startBase(HttpServletRequest request, HttpServletResponse response,String key){
        List<ConfigEntity> convert = RedisUtil.getCacheList(ConfigKeyNum.config_base.getKey());
        Map<String, Object> map = new HashMap<>();
        if(convert.isEmpty()){
            List<String> list=new ArrayList<>();
            list.add("1");
            map.put("loginType",list);
        }
        convert.forEach(smsConfigEntity -> {
            if("sys_base_title".equals(smsConfigEntity.getConfigKey())){
                map.put("globalTitle",smsConfigEntity.getConfigValue());
            }
            if("sys_base_description".equals(smsConfigEntity.getConfigKey())){
                map.put("globalDescription",smsConfigEntity.getConfigValue());
            }
            if("sys_base_logo".equals(smsConfigEntity.getConfigKey())){
                map.put("logo",smsConfigEntity.getConfigValue());
            }
            if("sys_base_captcha_state".equals(smsConfigEntity.getConfigKey())){
                map.put("captchaState", Objects.equals(Convert.toInt(smsConfigEntity.getConfigValue()), SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()));
            }
            if("sys_base_login_type".equals(smsConfigEntity.getConfigKey())){
                map.put("loginType", DataUtil.getStringToList(smsConfigEntity.getConfigValue()));
            }
            if("sys_base_tenant_state".equals(smsConfigEntity.getConfigKey())){
                map.put("tenantState", Convert.toInt(smsConfigEntity.getConfigValue()) == 1);
            }
            if("sys_base_login_bg".equals(smsConfigEntity.getConfigKey())){
                map.put("loginBg",smsConfigEntity.getConfigValue());
            }
            if("sys_base_tenant_exclusion_table".equals(smsConfigEntity.getConfigKey())){
                map.put("tenantExclusionTable", DataUtil.getStringToList(smsConfigEntity.getConfigValue()));
            }

        });

        return R.success("base system config.",map);
    }

    /**
     * 获取二维码
     * @param key 二维码key
     * @return 二维码
     */
    @SaIgnore
    @PostMapping("/getWxCode")
    public R<Object> getWxCode(String key) {
        if(key==null){
            return R.fail("key不能为空！");
        }
        //登录二维码
        String codeUrl= wxCodeService.getCode(new WxCodeBo(key)
            .typeLogin()
            .paramData(key));
        return success("二维码获取成功",codeUrl);
    }

    /**
     * 查询二维码状态
     * @param key 二维码key
     */
    @SaIgnore
    @PostMapping("/queryWxCodeState")
    public R<Object> queryWxCodeState(String key,HttpServletRequest request,
                                      HttpServletResponse response) {
        if(key==null){
            return R.fail("key不能为空！");
        }
        //登录二维码
        WxCodeBo wxCodeBo= wxCodeService.getCodeState(new WxCodeBo(key).typeLogin());
        if(Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.WAITING.getValue())){
            return fail("等待扫码中...");
        }
        if(Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.SCANNED.getValue())){
            return okFail("二维码已被扫！");
        }
        if(Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.FAILING.getValue())){
            return okFail("扫码失败！");
        }
        if(Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.SUCCEED.getValue())){
            log.info(wxCodeBo.getOpenId());
            SysUserVo sysUserVo= sysUserService.selectOpenId(wxCodeBo.getOpenId());
            if(sysUserVo==null){
                return okFail("该微信未绑定账号！");
            }
            if(!Objects.equals(sysUserVo.getStatus(), SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())){
                return okFail("该账号已被禁用！");
            }
            //进行登记登记
            SysUser user = MapstructUtil.convert(sysUserVo, SysUser.class);
            sysLoginService.updateSysUser(request,user);
            // 获取当前会话的token值
            assert user != null;
            String token = LoginObject.loginToken(user.getUserId(), DeviceEnum.ADMIN.getType(), 10000000L, "id", user.getUserId());
            Map<String, Object> ajax = new HashMap<>(16);
            ajax.put(Constants.TOKEN, token);
            SysUserVo sysUser = sysUserService.getUserRoleAnfFunctionInfo(LoginObject.getLoginId());
            ajax.put(Constants.USERINFO, sysUserService.getUserInfo(sysUser));
            RedisUtil.setCacheObject(RedisConstant.ADMIN_KEY+sysUser.getUserId(),sysUser, Duration.ofHours(24));
            if(LoginObject.isLogin()){
                //给浏览器端设置一个 Cookie 的 clientKey值 3天
                sysSignService.loginSetCookie(request,response,1000*60*60*24*3);
            }
            return success(ajax);
        }
        return R.fail("系统异常！");

    }


    /**
     * 发送验证码
     * @param bo 验证码业务对象
     */
    @SaIgnore
    @PostMapping("/smsCode")
    public R<Object> smsCode(@RequestBody SmsBo bo, HttpServletRequest request) {

        Integer type = bo.getType();
        String phone = bo.getPhone();

        String keytype = "register:";
        if (type == 1) {
            keytype = "register:";
        }
        if (type == 2) {
            List<ConfigEntity> convert = RedisUtil.getCacheList(ConfigKeyNum.config_base.getKey());
            for (ConfigEntity smsConfigEntity : convert) {
                if("sys_base_login_type".equals(smsConfigEntity.getConfigKey())){
                    List<String> toList = DataUtil.getStringToList(smsConfigEntity.getConfigValue());
                    if(toList.contains("1")){
                       return R.fail("当前系统不支持手机号登录！");
                    }
                }
            }
            keytype = "login:";
        }
        if (type == 3) {
            keytype = "password:";
        }
        if (type == 4) {
            keytype = "passwordPay:";
        }
        if (type == 5) {
            keytype = "updatePhone:";
        }
        if (Objects.equals(environment.getProperty("spring.profiles.active"), "prod")) {
            try {
                String ip = null;
                ip = IPUtil.getIp(request);
                log.info("ips:" + ip);
                Object s = RedisUtil.getCacheObject("ip:phone:" + ip);
                int num = Convert.toInt(s == null ? 0 : Convert.toInt(s), 0);
                if (num >= 5) {
                    log.info("ips>=5,拦截:" + ip);
                    return R.fail("当前IP发送频繁,请一天后再发送哦！");
                }
                num++;
                RedisUtil.setCacheObject("ip:phone:" + ip, num);
                RedisUtil.setCacheObject("ip:phone:" + ip, num, Duration.ofDays(1));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        SysUserVo userVo=null;
        if (type == 1) {
            userVo = sysUserService.selectVoByPhone(phone);
            if (userVo != null) {
                return R.fail("该手机号已注册！");
            }
        }
        if (type == 2 || type == 3) {
            userVo = sysUserService.selectVoByPhone(phone);
            if (userVo == null) {
                return R.fail("该手机号账号不存在！");
            }
        }
        if(type==4||type==5){
            userVo=LoginObject.getLoginObject(SysUserVo.class);
            if (userVo == null) {
                return R.fail("登录失效请重新登录！");
            }
            if(type==5){
                userVo = sysUserService.selectVoByPhone(phone);
                if (userVo != null) {
                    return R.fail("该手机号已被绑定！");
                }
            }
        }
        if(LoginObject.isLogin()){
            if (!Objects.equals(Convert.toInt(LoginObject.getLoginObject(SysUserVo.class).getStatus()), SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())) {
                throw new MmsException("账号状态不正常！");
            }
        }

        String key = RedisUtil.PHONE_CODES_KEY + keytype + phone;
        Object object = RedisUtil.getCacheObject(key);
        if (object != null) {
            RedisUtil.deleteObject(key);
            return R.fail("发送频繁,请稍后再发送哦！");
        }
        String code = RandomUtil.randomNumbers(6);
        R<Object> result = smsService.sendSms(phone, code);

        if (result.getStatus()!=0) {
            log.error("验证码短信发送异常 => {}", result.getMsg());
            return result;
        }
        RedisUtil.setCacheObject(key, code);
        RedisUtil.expire(key, Duration.ofMinutes(1));
        return R.success("手机短信码已发送！");
    }

    /**
     * 获取所有字典列表
     * @return 字典列表
     */
    @SaCheckLogin
    @PostMapping("/listDictAll")
    public R<Object> listDictAll() {
        return R.success(sysDictService.selectAll());
    }

    /**
     * 登录用户系统菜单
     * @return 菜单列表
     */
    @SaCheckLogin
    @GetMapping("/getMenu")
    public R<Object> getMenu() {
        List<AdminMenuTree> menuTree = sysUserService.getAdminMenuTree(LoginObject.getLoginId());
        return success(menuTree);
    }


    @SaCheckLogin
    @PostMapping("/emailCode")
    public R<Object> emailCode(@RequestBody EmailBo bo, HttpServletRequest request) {
        Integer type = bo.getType();
        String emali = bo.getEmail();
        String keytype = "updatePhone:";
        if (Objects.equals(environment.getProperty("spring.profiles.active"), "prod")) {
            try {
                String ip = null;
                ip = IPUtil.getIp(request);
                log.info("ips:" + ip);
                Object s = RedisUtil.getCacheObject("ip:email:" + ip);
                int num = Convert.toInt(s == null ? 0 : Convert.toInt(s), 0);
                if (num >= 5) {
                    log.info("ips>=5,拦截:" + ip);
                    return R.fail("当前IP发送频繁,请一天后再发送哦！");
                }
                num++;
                RedisUtil.setCacheObject("ip:email:" + ip, num);
                RedisUtil.setCacheObject("ip:email:" + ip, num, Duration.ofDays(1));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        String key = RedisUtil.EMAIL_CODES_KEY + keytype + emali;
        Object object = RedisUtil.getCacheObject(key);
        if (object != null) {
            RedisUtil.deleteObject(key);
            return R.fail("发送频繁,请稍后再发送哦！");
        }
        String code = RandomUtil.randomNumbers(6);
        R<Object> result = emailService.sendEmailCode(bo.getEmail(), code);

        if (result.getStatus()!=0) {
            log.error("验证码发送异常 => {}", result.getMsg());
            return result;
        }
        RedisUtil.setCacheObject(key, code);
        RedisUtil.expire(key, Duration.ofMinutes(10));
        return R.success("邮箱验证码已发送！");
    }

}
