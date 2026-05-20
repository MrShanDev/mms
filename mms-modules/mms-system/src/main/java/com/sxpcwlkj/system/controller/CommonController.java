package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.ConfigEntity;
import com.sxpcwlkj.common.code.entity.WxCodeBo;
import com.sxpcwlkj.common.constant.Constants;
import com.sxpcwlkj.common.enums.ConfigKeyNum;
import com.sxpcwlkj.common.enums.DeviceEnum;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.enums.WxCodeStatusEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.properties.DemoModeProperties;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.IPUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.email.service.EmailService;
import com.sxpcwlkj.framework.service.SysSignService;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import com.sxpcwlkj.sms.service.SmsService;
import com.sxpcwlkj.system.entity.AdminMenuTree;
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.entity.bo.EmailBo;
import com.sxpcwlkj.system.entity.bo.SysSmsBo;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import com.sxpcwlkj.system.service.SysDictService;
import com.sxpcwlkj.system.service.SysLoginService;
import com.sxpcwlkj.system.service.SysUserService;
import com.sxpcwlkj.wx.service.WxCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * mms公共接口
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-公共接口",description = "系统管理模块-公共接口")
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
    private final DemoModeProperties demoModeProperties;

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
                map.put("tenantState", Convert.toInt(smsConfigEntity.getConfigValue()).equals(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()));
            }
            if("sys_base_login_bg".equals(smsConfigEntity.getConfigKey())){
                map.put("loginBg",smsConfigEntity.getConfigValue());
            }
            if("sys_base_tenant_exclusion_table".equals(smsConfigEntity.getConfigKey())){
                map.put("tenantExclusionTable", DataUtil.getStringToList(smsConfigEntity.getConfigValue()));
            }

        });
        map.put("demoMode",demoModeProperties.isEnabled());
        map.put("demoAccount",demoModeProperties.isEnabled()?"mms":"");
        map.put("demoPassword",demoModeProperties.isEnabled()?"123456":"");
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
        try {
            //登录二维码
            String codeUrl = wxCodeService.getCode(new WxCodeBo(key)
                .typeLogin()
                .paramData(key));
            return R.success("二维码获取成功", codeUrl);
        } catch (Exception e) {
            log.error("获取微信登录二维码失败", e);
            return R.fail("获取微信二维码失败，请稍后重试");
        }
    }

    /**
     * 查询登录扫码状态（与 {@code /common/wxLogin/stream} 并行保留）。
     * <p>用途：短轮询、Apifox/排障、未接 SSE 的客户端；返回体与历史行为一致，扫码成功时仍在此接口内完成登录与 Cookie。</p>
     * @param key 与 {@code /common/getWxCode} 一致的二维码 key
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
            return R.fail("等待扫码中...");
        }
        if(Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.SCANNED.getValue())){
            return R.okFail("二维码已被扫！");
        }
        if(Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.FAILING.getValue())){
            return R.okFail("扫码失败！");
        }
        if(Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.SUCCEED.getValue())){
            log.info(wxCodeBo.getOpenId());
            SysUserVo sysUserVo= sysUserService.selectOpenId(wxCodeBo.getOpenId());
            if(sysUserVo==null){
                return R.okFail("该微信未绑定账号！");
            }
            if(!Objects.equals(sysUserVo.getStatus(), SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())){
                return R.okFail("该账号已被禁用！");
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
            return R.success(ajax);
        }
        return R.fail("系统异常！");

    }

    /**
     * 微信扫码登录：扫码结果 SSE，替代浏览器短轮询 {@code /common/queryWxCodeState}。
     * <p>终态为「扫码成功」时只推 {@code {"ok":true,"login":true}}，客户端须再请求一次
     * {@code POST /common/queryWxCodeState} 完成会话写入、Cookie（避免在已 chunked 的 SSE 响应上追加 Set-Cookie）。</p>
     */
    @SaIgnore
    @GetMapping(value = "/wxLogin/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter wxLoginStream(@RequestParam("key") String key) {
        if (key == null || key.isBlank()) {
            throw new MmsException("key不能为空");
        }
        SseEmitter emitter = new SseEmitter(0L);
        AtomicBoolean disconnected = new AtomicBoolean(false);
        emitter.onCompletion(() -> disconnected.set(true));
        emitter.onTimeout(() -> disconnected.set(true));
        emitter.onError(e -> disconnected.set(true));

        CompletableFuture.runAsync(() -> {
            final long tickMs = 2500L;
            final long maxMs = 70_000L;
            final long deadline = System.currentTimeMillis() + maxMs;
            while (!disconnected.get() && System.currentTimeMillis() < deadline) {
                try {
                    WxCodeBo wxCodeBo = wxCodeService.getCodeState(new WxCodeBo(key).typeLogin());
                    Integer st = wxCodeBo.getState();
                    if (Objects.equals(st, WxCodeStatusEnum.WAITING.getValue())
                        || Objects.equals(st, WxCodeStatusEnum.SCANNED.getValue())) {
                        Map<String, Object> tick = new LinkedHashMap<>();
                        tick.put("phase", Objects.equals(st, WxCodeStatusEnum.WAITING.getValue()) ? "waiting" : "scanned");
                        sendCommonWxSse(emitter, "tick", tick);
                        sleepQuietCommon(tickMs);
                        continue;
                    }
                    if (Objects.equals(st, WxCodeStatusEnum.FAILING.getValue())) {
                        sendCommonWxSse(emitter, "done", wxLoginStreamDone(false, false, "扫码失败！"));
                        emitter.complete();
                        return;
                    }
                    if (Objects.equals(st, WxCodeStatusEnum.SUCCEED.getValue())) {
                        sendCommonWxSse(emitter, "done", wxLoginStreamDone(true, true, null));
                        emitter.complete();
                        return;
                    }
                    sendCommonWxSse(emitter, "done", wxLoginStreamDone(false, false, "系统异常！"));
                    emitter.complete();
                    return;
                } catch (Exception e) {
                    log.error("wxLogin stream error, key={}", key, e);
                    try {
                        sendCommonWxSse(emitter, "done", wxLoginStreamDone(false, false, "系统异常！"));
                        emitter.completeWithError(e);
                    } catch (Exception ignored) {
                        try {
                            emitter.complete();
                        } catch (Exception ignored2) {
                            // ignore
                        }
                    }
                    return;
                }
            }
            if (!disconnected.get()) {
                try {
                    sendCommonWxSse(emitter, "done", wxLoginStreamDone(false, false, "二维码已失效"));
                    emitter.complete();
                } catch (Exception ignored) {
                    // ignore
                }
            }
        });

        return emitter;
    }

    private static Map<String, Object> wxLoginStreamDone(boolean ok, boolean login, String msg) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("ok", ok);
        m.put("login", login);
        if (msg != null) {
            m.put("msg", msg);
        }
        return m;
    }

    private static void sendCommonWxSse(SseEmitter emitter, String eventName, Map<String, Object> payload)
        throws IOException {
        emitter.send(SseEmitter.event().name(eventName).data(JSONUtil.toJsonStr(payload)));
    }

    private static void sleepQuietCommon(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * 发送验证码
     * @param bo 验证码业务对象
     */
    @SaIgnore
    @PostMapping("/smsCode")
    public R<Object> smsCode(@RequestBody SysSmsBo bo, HttpServletRequest request) {

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

        if (!result.getStatus()) {
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
        return R.success(menuTree);
    }


    @SaCheckLogin
    @PostMapping("/emailCode")
    public R<Object> emailCode(@RequestBody EmailBo bo, HttpServletRequest request) {
        Integer type = bo.getType();
        String keytype = "updatePhone:";
        String emali = bo.getEmail();
        SysUserVo pwdResetCaller = null;
        // 重置密码验证码：必须使用当前账号数据库中的真实绑定邮箱发送，且不依赖前端传入的明文/脱敏地址
        if (Objects.equals(type, 3)) {
            keytype = "password:";
            SysUserVo full = sysUserService.selectVoById(LoginObject.getLoginId());
            if (full == null || StrUtil.isBlank(full.getEmail())) {
                return R.fail("请先绑定邮箱");
            }
            emali = full.getEmail().trim();
            pwdResetCaller = full;
        } else {
            if (StrUtil.isBlank(emali)) {
                return R.fail("邮箱不能为空");
            }
            emali = emali.trim();
        }
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
        R<Object> result;
        if (Objects.equals(type, 3)) {
            String displayName = "用户";
            if (pwdResetCaller != null) {
                String n = StrUtil.blankToDefault(pwdResetCaller.getNickName(), pwdResetCaller.getUserName());
                if (StrUtil.isNotBlank(n)) {
                    displayName = StrUtil.trim(n);
                }
            }
            result = emailService.sendPasswordReset(emali, displayName, code);
        } else if (Objects.equals(type, 1)) {
            result = emailService.sendBindEmailCode(emali, code);
        } else {
            result = emailService.sendRegisterCode(emali, code);
        }
        if (!result.getStatus()) {
            log.error("验证码发送异常 => {}", result.getMsg());
            return result;
        }
        RedisUtil.setCacheObject(key, code);
        RedisUtil.expire(key, Duration.ofMinutes(10));
        return R.success("邮箱验证码已发送！");
    }

}
