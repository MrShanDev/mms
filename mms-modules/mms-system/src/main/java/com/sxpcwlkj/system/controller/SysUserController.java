package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.enums.ErrorCodeEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.common.code.entity.PrintObject;
import com.sxpcwlkj.common.code.entity.WxCodeBo;
import com.sxpcwlkj.common.enums.WxCodeStatusEnum;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.framework.utils.ExcelUtil;
import com.sxpcwlkj.log.annotation.MmsLog;
import com.sxpcwlkj.log.enums.OperationType;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.system.entity.bo.*;
import com.sxpcwlkj.system.entity.export.SysUserExportVo;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import com.sxpcwlkj.system.service.SysUserService;
import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.wx.service.WxCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 用户管理
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-用户管理",description = "系统管理模块-用户管理")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("system/user")
public class SysUserController extends BaseController {

    private final SysUserService baseService;
    private final WxCodeService wxCodeService;
    /**
     * 获取用户分页列表
     *
     * @return 用户列表
     */
    @MssSafety(decryptRequest = true)
    @SaCheckPermission("system:user:list")
    @PostMapping("/list")
    public R<PageResult<SysUserVo>> listPage(@RequestBody @Validated(ValidatedGroupConfig.query.class)  SysUserBo user) {
        return R.success(baseService.selectPageUserList(user, user.getPageQuery()).toPageResult());
    }

    /**
     * 获取用户详细信息
     *
     * @return PageDataInfo
     */
    @SaCheckPermission("system:user:query")
    @GetMapping("/{id}")
    public R<SysUserVo> queryById(@PathVariable String id) {
        return R.success(baseService.selectVoById(id));
    }

    /**
     * 编辑用户
     *
     * @return true/false
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.UPDATE,
        description = "修改用户信息",
        saveBeforeData = true,
        saveResponseData = true  // 开启响应数据记录
    )
    @MssSafety
    @Transactional
    @SaCheckPermission("system:user:edit")
    @PutMapping
    public R<Boolean> edit(@RequestBody @Validated(ValidatedGroupConfig.update.class) SysUserBo bo) {
        return R.success(baseService.updateByIdBase(bo));
    }

    /**
     * 新增用户
     *
     * @return vo
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.INSERT,
        description = "新增用户"
    )
    @MssSafety
    @Transactional
    @SaCheckPermission("system:user:insert")
    @PostMapping
    public R<Boolean> insert(@RequestBody @Validated(ValidatedGroupConfig.insert.class) SysUserBo bo) {
        return R.success(baseService.insert(bo));
    }

    /**
     * 删除用户
     *
     * @return vo
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.DELETE,
        description = "删除用户"
    )
    @MssSafety
    @Transactional
    @SaCheckPermission("system:user:delete")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable String ids) {
        return R.success(baseService.deleteById(ids));
    }


    /**
     * 模版下载
     */
    @SaCheckPermission("system:user:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil.download(response, SysUserExportVo.class, "用户管理");
    }

    /**
     * 导入用户管理
     * @param file 模版文件
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.IMPORT,
        description = "导入用户数据"
    )
    @MssSafety
    @Transactional
    @SaCheckPermission("system:user:import")
    @PostMapping("/import")
    public R<Boolean> imports(@RequestParam("file") MultipartFile file) throws Exception {
       Set<SysUserExportVo> list=  ExcelUtil.imports(file, SysUserExportVo.class);
       Boolean state= baseService.imports(list);
       return R.ok(state,state?"数据导入成功!":"数据导入失败!");
    }

    /**
     * 导出用户管理
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.EXPORT,
        description = "导出用户数据"
    )
    @MssSafety
    @SaCheckPermission("system:user:export")
    @PostMapping("/export")
    public void export(SysUserBo user, PageQuery pageQuery, HttpServletResponse response) throws IOException {
        List<SysUserVo> list = baseService.selectPageUserList(user, pageQuery).getRows();
        List<SysUserExportVo> data = MapstructUtil.convert(list, SysUserExportVo.class);

        // 使用封装后的安全导出方法
        ExcelUtil.safeExport(response, SysUserExportVo.class, "用户管理", data, pageQuery);
    }

    /**
     * 打印用户管理
     * @param user 查询条件
     * @param pageQuery 分页条件
     */
    @MssSafety
    @Transactional
    @SaCheckPermission("system:user:print")
    @PostMapping("/print")
    public R<PrintObject<SysUserExportVo>> print(SysUserBo user, PageQuery pageQuery) throws Exception {
        List<SysUserVo> list= baseService.selectPageUserList(user, pageQuery).getRows();
        List<SysUserExportVo> data= MapstructUtil.convert(list,SysUserExportVo.class);
        PrintObject<SysUserExportVo>  printObject=   new PrintObject<SysUserExportVo>()
            .setTitle("用户管理")
            .setData(data);
        return R.response(Boolean.TRUE,printObject);
    }

   //====================================个人信息=====================================

    /**
     * 重置密码
     *
     * @return vo
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.UPDATE,
        description = "重置密码"
    )
    @SaCheckLogin
    @PostMapping("/resetPwd")
    public R<Boolean> resetPwd(@Validated @RequestBody(required = false)ResetPwdBo bo) {
        return R.success(baseService.resetPwd(bo));
    }

    /**
     * 邮箱验证码重置当前用户密码（不校验旧密码）
     *
     * @param bo 验证码与新密码
     * @return 是否成功
     */
    @MmsLog(
        module = "个人中心",
        operType = OperationType.UPDATE,
        description = "邮箱验证码重置密码"
    )
    @SaCheckLogin
    @PostMapping("/resetPwdEmail")
    public R<Boolean> resetPwdEmail(@Validated @RequestBody ResetPwdEmailBo bo) {
        SysUserVo full = baseService.selectVoById(LoginObject.getLoginId());
        if (full == null || full.getEmail() == null || full.getEmail().isBlank()) {
            return R.fail("请先绑定邮箱");
        }
        String emailTrim = full.getEmail().trim();
        String key = RedisUtil.EMAIL_CODES_KEY + "password:" + emailTrim;
        Object code = RedisUtil.getCacheObject(key);
        if (code == null) {
            return R.fail("验证码已过期！");
        }
        if (!Objects.equals(bo.getCode(), code)) {
            return R.fail("验证码不正确！");
        }
        RedisUtil.deleteObject(key);
        return R.success(baseService.resetPwdWithoutOld(bo.getPassword()));
    }


    /**
     * 重置密码:管理员
     *
     * @return vo
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.UPDATE,
        description = "管理员重置密码"
    )
    @SaCheckPermission("system:user:edit")
    @Transactional
    @PostMapping("/resetPwdSuper")
    public R<Boolean> resetPwdSuper(@Validated @RequestBody  ResetPwdSuperBo bo) {
        return R.success(baseService.resetPwdSuper(bo));
    }

    /**
     * 修改头像
     *
     * @param avatar 图片
     * @return true/false
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.UPDATE,
        description = "修改用户头像"
    )
    @SaCheckLogin
    @GetMapping(value = "/editHeaderImg")
    public R<Boolean> editHeaderImg(@Validated @NotBlank(message = "头像不能为空")  String avatar) {
        return R.success("修改头像成功",baseService.updateHeaderImgById(avatar));
    }

    /**
     * 设置用户角色
     * @param bo bo
     * @return true/false
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.UPDATE,
        description = "设置用户角色"
    )
    @SaCheckPermission("system:user:edit")
    @Transactional
    @PostMapping("/setUserRole")
    public R<Boolean> setUserRole(@Validated @RequestBody SetUserRoleSuperBo bo) {
        return R.success("设置用户角色成功",baseService.setUserRoleSuper(bo));
    }

    /**
     * 解绑手机号 邮箱 微信
     *
     * @return vo
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.UPDATE,
        description = "解绑手机号邮箱微信"
    )
    @SaCheckLogin
    @GetMapping("/unbind")
    public R<Boolean> unbind(int type) {
        return R.success(baseService.unbind(type)?"解绑成功":"解绑失败");
    }

    /**
     * 绑定手机号
     *
     * @return vo
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.UPDATE,
        description = "绑定手机号"
    )
    @SaCheckLogin
    @PostMapping("/bindingPhone")
    public R<Boolean> bindingPhone(@Validated({ValidatedGroupConfig.update.class}) @RequestBody SysSmsBo bo) {
        String keyType = "updatePhone:";
        String phone = bo.getPhone();
        String key = RedisUtil.PHONE_CODES_KEY + keyType + phone;
        Object code = RedisUtil.getCacheObject(key);
        if (code == null) {
            return R.fail("验证码已过期！");
        }
        if (!bo.getCode().equals(code)) {
            return R.fail("验证码不正确！");
        }
        RedisUtil.deleteObject(key);
        return R.success(baseService.bindingPhone(phone)? "绑定手机号成功":"绑定手机号失败");
    }


    /**
     * 绑定邮件
     *
     * @return vo
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.UPDATE,
        description = "绑定邮箱"
    )
    @SaCheckLogin
    @PostMapping("/bindingEmail")
    public R<Boolean> bindingEmail(@Validated({ValidatedGroupConfig.update.class}) @RequestBody EmailBo bo) {
        String keyType = "updatePhone:";
        String email = bo.getEmail();
        String key = RedisUtil.EMAIL_CODES_KEY + keyType + email;
        Object code = RedisUtil.getCacheObject(key);
        if (code == null) {
            return R.fail("验证码已过期！");
        }
        if (!bo.getCode().equals(code)) {
            return R.fail("验证码不正确！");
        }
        RedisUtil.deleteObject(key);
        return R.success(baseService.bindingEmail(email)? "绑定邮箱成功":"绑定邮箱失败");
    }


    @SaCheckLogin
    @GetMapping("/getWxCode")
    public R<String> getWxCode(String uuid) {
        if (uuid == null) {
            return R.fail("uuid不能为空！");
        }
        try {
            String codeUrl = wxCodeService.getCode(new WxCodeBo(uuid)
                .typeBinding()
                .expireTime(1000 * 60)
                .paramData(LoginObject.getLoginId()));
            return R.success("二维码获取成功", codeUrl);
        } catch (Exception e) {
            log.error("获取微信绑定二维码失败", e);
            return R.fail("获取微信二维码失败，请稍后重试或联系管理员");
        }
    }

    /**
     * 查询绑定扫码状态（与 {@code /system/user/wxBind/stream} 并行保留）。
     * <p>用途：短轮询、接口调试、按需查询 Redis 中该 uuid 的状态；业务逻辑与 SSE 内轮询共用 {@link #resolveWxBindPhase}。</p>
     * @param uuid 与 {@code /system/user/getWxCode} 一致的二维码 key
     */
    @SaIgnore
    @GetMapping("/queryWxCodeState")
    public R<Object> queryWxCodeState(String uuid) {
        if (uuid == null) {
            return R.fail("key不能为空！");
        }
        WxCodeBo wxCodeBo = wxCodeService.getCodeState(new WxCodeBo(uuid).typeBinding());
        return resolveWxBindPhase(wxCodeBo);
    }

    /**
     * 微信绑定扫码状态（SSE）：个人中心绑定/更换绑定时监听扫码结果，减少浏览器短轮询。
     * <p>EventSource 无法携带与 axios 一致的 Header，故通过 query 传递 {@code Authorization}，
     * 与 {@code /system/home/runtimeSse} 约定一致。</p>
     */
    @SaIgnore
    @GetMapping(value = "/wxBind/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter wxBindStream(
        @RequestParam("uuid") String uuid,
        @RequestParam("Authorization") String authorization) {
        if (uuid == null || uuid.isBlank()) {
            throw new MmsException("uuid不能为空");
        }
        if (authorization == null || authorization.isBlank()
            || LoginObject.getLoginIdByToken(authorization) == null) {
            throw new MmsException(ErrorCodeEnum.USER_NOT_LOGIN.getValue(), ErrorCodeEnum.USER_NOT_LOGIN.getKey());
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
                    WxCodeBo wxCodeBo = wxCodeService.getCodeState(new WxCodeBo(uuid).typeBinding());
                    Integer st = wxCodeBo.getState();
                    if (Objects.equals(st, WxCodeStatusEnum.WAITING.getValue())
                        || Objects.equals(st, WxCodeStatusEnum.SCANNED.getValue())) {
                        Map<String, Object> tick = new LinkedHashMap<>();
                        tick.put("phase", Objects.equals(st, WxCodeStatusEnum.WAITING.getValue()) ? "waiting" : "scanned");
                        sendWxBindSseEvent(emitter, "tick", tick);
                        sleepQuiet(tickMs);
                        continue;
                    }
                    if (Objects.equals(st, WxCodeStatusEnum.FAILING.getValue())) {
                        sendWxBindSseEvent(emitter, "done", wxBindDonePayload(false, "扫码失败！"));
                        emitter.complete();
                        return;
                    }
                    if (Objects.equals(st, WxCodeStatusEnum.SUCCEED.getValue())) {
                        log.info("wx bind stream succeed openId={}", wxCodeBo.getOpenId());
                        SysUserVo sysUserVo = baseService.selectOpenId(wxCodeBo.getOpenId());
                        if (sysUserVo != null) {
                            sendWxBindSseEvent(emitter, "done", wxBindDonePayload(false, "该微信已绑定账号！"));
                        } else {
                            boolean bound = baseService.bindingOpenId(wxCodeBo.getOpenId());
                            String msg = bound ? "绑定微信成功" : "绑定微信失败";
                            sendWxBindSseEvent(emitter, "done", wxBindDonePayload(bound, msg));
                        }
                        emitter.complete();
                        return;
                    }
                    sendWxBindSseEvent(emitter, "done", wxBindDonePayload(false, "系统异常！"));
                    emitter.complete();
                    return;
                } catch (Exception e) {
                    log.error("wxBind stream error, uuid={}", uuid, e);
                    try {
                        sendWxBindSseEvent(emitter, "done", wxBindDonePayload(false, "系统异常！"));
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
                    sendWxBindSseEvent(emitter, "done", wxBindDonePayload(false, "二维码已失效"));
                    emitter.complete();
                } catch (Exception ignored) {
                    // ignore
                }
            }
        });

        return emitter;
    }

    /**
     * 与 {@link #queryWxCodeState} 共用：根据 Redis 中二维码状态得到绑定结果。
     */
    private R<Object> resolveWxBindPhase(WxCodeBo wxCodeBo) {
        if (Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.WAITING.getValue())) {
            return R.fail("等待扫码中...");
        }
        if (Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.SCANNED.getValue())) {
            return R.okFail("二维码已被扫！");
        }
        if (Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.FAILING.getValue())) {
            return R.okFail("扫码失败！");
        }
        if (Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.SUCCEED.getValue())) {
            log.info(wxCodeBo.getOpenId());
            SysUserVo sysUserVo = baseService.selectOpenId(wxCodeBo.getOpenId());
            if (sysUserVo != null) {
                return R.okFail("该微信已绑定账号！");
            }
            return R.success(baseService.bindingOpenId(wxCodeBo.getOpenId()) ? "绑定微信成功" : "绑定微信失败");
        }
        return R.fail("系统异常！");
    }

    private static Map<String, Object> wxBindDonePayload(boolean ok, String msg) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("ok", ok);
        m.put("msg", msg);
        return m;
    }

    private static void sendWxBindSseEvent(SseEmitter emitter, String eventName, Map<String, Object> payload)
        throws IOException {
        emitter.send(SseEmitter.event().name(eventName).data(JSONUtil.toJsonStr(payload)));
    }

    private static void sleepQuiet(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * 获取最新登录对象信息
     * @return 登录对象
     */
    @SaCheckLogin
    @GetMapping("/getUserInfo")
    public R<Map<String, Object>> getUserInfo(){
        SysUserVo sysUser = baseService.getUserRoleAnfFunctionInfo(LoginObject.getLoginId());
        return R.success(baseService.getUserInfo(sysUser));
    }

}
