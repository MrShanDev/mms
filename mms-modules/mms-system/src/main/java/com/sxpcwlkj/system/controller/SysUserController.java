package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.authority.LoginObject;
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
import com.sxpcwlkj.wx.service.WxCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 系统用户
 * @module 系统管理模块
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Tag(name = "系统管理模块-系统用户",description = "系统管理模块-系统用户")
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
        ExcelUtil.download(response, SysUserExportVo.class, "系统用户");
    }

    /**
     * 导入系统用户
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
     * 导出系统用户
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
        ExcelUtil.safeExport(response, SysUserExportVo.class, "系统用户", data, pageQuery);
    }

    /**
     * 打印系统用户
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
            .setTitle("系统用户")
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
     * 重置密码:管理员
     *
     * @return vo
     */
    @MmsLog(
        module = "用户管理",
        operType = OperationType.UPDATE,
        description = "管理员重置密码"
    )
    @SaCheckLogin
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
        if(uuid==null){
            return R.fail("uuid不能为空！");
        }
        //获取微信绑定的二维码
        String codeUrl= wxCodeService.getCode(new WxCodeBo(uuid)
            .typeBinding()
            .expireTime(1000*60)
            .paramData(LoginObject.getLoginId()));
        return R.success("二维码获取成功",codeUrl);
    }

    /**
     * 查询二维码状态
     * @param uuid 二维码key
     * @return
     */
    @SaIgnore
    @GetMapping("/queryWxCodeState")
    public R<Object> queryWxCodeState(String uuid) {
        if(uuid==null){
            return R.fail("key不能为空！");
        }
        //登录二维码
        WxCodeBo wxCodeBo= wxCodeService.getCodeState(new WxCodeBo(uuid).typeBinding());
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
            SysUserVo sysUserVo= baseService.selectOpenId(wxCodeBo.getOpenId());
            if(sysUserVo!=null){
                return R.okFail("该微信已绑定账号！");
            }
            return R.success(baseService.bindingOpenId(wxCodeBo.getOpenId())?"绑定微信成功":"绑定微信失败");
        }
        return R.fail("系统异常！");

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
