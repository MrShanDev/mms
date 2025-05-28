package com.sxpcwlkj.mobile.controller;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.enums.DeviceEnum;
import com.sxpcwlkj.common.enums.HttpStatusEnum;
import com.sxpcwlkj.common.exception.MmsException;

import com.sxpcwlkj.common.utils.IPUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.common.utils.StringUtil;

import com.sxpcwlkj.mobile.entity.StoreMember;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberLoginBo;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberRegisterBo;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberBo;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberVo;
import com.sxpcwlkj.mobile.enums.DefStaticEnum;
import com.sxpcwlkj.mobile.service.StoreMemberService;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import com.sxpcwlkj.wx.service.WxService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * 会员登录
 * 登录注册等一些基础功能
 *
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/login")
public class ApiLoginController extends BaseController {

    private final StoreMemberService apiMemberService;
    private final WxService wxService;


    /**
     * 【公共部分】获取登录对象信息
     * @param request 请求
     * @param vo 会员信息
     * @return 会员信息
     */
    @NotNull
    private R<StoreMemberVo> getLoginMemberInfo(HttpServletRequest request, StoreMemberVo vo) {
        if(vo==null){
            return R.fail("登录失败！");
        }
        String token = LoginObject.loginToken(vo.getId(), DeviceEnum.MOBILE.getType(), 10000000L, "id", vo.getId());
        vo.setToken(token);
        String ip = IPUtil.getIp(request);
        apiMemberService.update(new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId, vo.getId()).set(StoreMember::getLastLoginIp, ip));
        RedisUtil.setCacheObject(RedisConstant.MOBILE_KEY+vo.getId(),vo, Duration.ofHours(24));
        return R.response(Boolean.TRUE, vo);
    }

    /**
     * token自动登录
     * token传递在header
     *
     * @return 会员信息
     */
    @SaIgnore
    @GetMapping("/tokenLogin")
    public R<StoreMemberVo> tokenLogin(HttpServletRequest request) {
        if (LoginObject.isLogin()) {
            StoreMemberVo vo = apiMemberService.selectVoById(LoginObject.getLoginId());
            return getLoginMemberInfo(request, vo);
        }
        throw new MmsException("请先登录！", HttpStatusEnum.FORBIDDEN.getCode());
    }

    /**
     * 微信根据code，获取code进行静默登录
     * token传递在header
     * @param code 微信获取的code
     * @return 会员信息
     */
    @SaIgnore
    @GetMapping("/codeGetOpenIdLogin")
    public R<StoreMemberVo> codeGetOpenIdLogin(@RequestParam("code")String code, HttpServletRequest request) {
        // 调用微信 API 获取用户的 openid 和 session_key
        WxMaJscode2SessionResult session = null;
        try {
            session = wxService.getWxMaService().getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            throw new MmsException("微信登录失败！");
        }
        if (session==null) {
            throw new MmsException("获取微信登录信息为空！");
        }
        StoreMemberVo vo = apiMemberService.selectVoByOpenId(session.getOpenid());
        return getLoginMemberInfo(request, vo);
    }

    /**
     * 微信注册/登录
     *
     * @param code          微信获取的code
     * @param encryptedData 微信获取的加密数据
     * @param iv            微信获取的iv
     * @return 会员信息
     */
    @SaIgnore
    @GetMapping("/codeGetPhoneRegisterOrLogin")
    public R<StoreMemberVo> codeGetPhoneRegisterOrLogin(@RequestParam("code") String code, @RequestParam("encryptedData") String encryptedData, @RequestParam("iv") String iv, HttpServletRequest request) throws WxErrorException {
            // 调用微信 API 获取用户的 openid 和 session_key
            WxMaJscode2SessionResult session = wxService.getWxMaService().getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            // 调用微信 API 获取用户的手机号
            WxMaPhoneNumberInfo phoneInfo = wxService.getWxMaService().getUserService().getPhoneNoInfo(session.getSessionKey(), encryptedData, iv);
            String phoneNumber = phoneInfo.getPhoneNumber();
            if (StringUtil.isEmpty(phoneNumber)) {
                return R.fail("获取手机号失败！");
            }
            StoreMemberVo vo = apiMemberService.selectVoByPhone(phoneNumber);
            if (vo==null) {
                StoreMemberBo storeMemberBo = new StoreMemberBo();
                storeMemberBo.setAccount(phoneNumber);
                storeMemberBo.setPhone(phoneNumber);
                storeMemberBo.setNickname("");
                storeMemberBo.setHeadPortrait(DefStaticEnum.MEMBER_DEF_HEADER_IMG.getValue());
                storeMemberBo.setPassword(SecureUtil.md5(phoneNumber));
                storeMemberBo.setSex(3);
                storeMemberBo.setStatus(1);
                storeMemberBo.setReputationScore(100);
                storeMemberBo.setLevel(1);
                storeMemberBo.setInvitationCode("");
                storeMemberBo.setWxOpenid(openid);
                apiMemberService.insert(storeMemberBo);
                vo = apiMemberService.selectVoByPhone(phoneNumber);
            }
            if (vo.getWxOpenid()==null|| vo.getWxOpenid().isEmpty()) {
                apiMemberService.update(new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId, vo.getId()).set(StoreMember::getWxOpenid, openid));
            }
            return getLoginMemberInfo(request, vo);
    }


    /**
     * 账号会员注册
     *
     * @param bo bo
     * @return 会员信息
     */
    @SaIgnore
    @PostMapping("/accountRegister")
    public R<StoreMemberVo> accountRegister(@RequestBody StoreMemberRegisterBo bo, HttpServletRequest request) {

        if (StringUtil.isEmpty(bo.getPhone())) {
            return R.fail("请输入手机号!");
        }
        if (StringUtil.isEmpty(bo.getSmsCode())) {
            return R.fail("请输入短信验证码!");
        }
        if (StringUtil.isEmpty(bo.getPassword())) {
            return R.fail("请输入密码!");
        }
        if (bo.getPassword().length() < 6 || bo.getPassword().length() > 16) {
            return R.fail("密码长度应该在6~16位之间!");
        }
        StoreMemberVo storeMemberVo = apiMemberService.selectVoByPhone(bo.getPhone());
        if (StringUtil.isNotEmpty(storeMemberVo)) {
            return R.fail("该手机号已注册！");
        }

        String key = RedisUtil.CAPTCHA_CODE_KEY + "_register_" + bo.getPhone();
        Object object = RedisUtil.getCacheObject(key);
        if (object == null) {
            return R.fail("短信验证码失效！");
        } else {
            if (!bo.getSmsCode().equals(object.toString())) {
                return R.fail("短信验证码有误！");
            }
        }
        StoreMemberBo storeMemberBo = new StoreMemberBo();
        storeMemberBo.setAccount(bo.getPhone());
        storeMemberBo.setPhone(bo.getPhone());
        storeMemberBo.setNickname("");
        storeMemberBo.setHeadPortrait(DefStaticEnum.MEMBER_DEF_HEADER_IMG.getValue());
        storeMemberBo.setPassword(SecureUtil.md5(bo.getPassword()));
        storeMemberBo.setSex(3);
        storeMemberBo.setStatus(1);
        storeMemberBo.setReputationScore(100);
        storeMemberBo.setLevel(1);
        storeMemberBo.setInvitationCode(bo.getInvitationCode());
        Boolean aBoolean = apiMemberService.insert(storeMemberBo);
        if (aBoolean) {
            StoreMemberVo vo = apiMemberService.selectVoByPhone(storeMemberBo.getPhone());
            return getLoginMemberInfo(request, vo);
        }
        return R.fail("注册失败!");
    }

    /**
     * 账号密码登录
     *
     * @param bo
     * @return
     */
    @SaIgnore
    @PostMapping("/accountLogin")
    public R<StoreMemberVo> accountLogin(@RequestBody @Validated StoreMemberLoginBo bo, HttpServletRequest request) {
        log.info(SecureUtil.md5(bo.getPassword()));
        StoreMemberVo storeMemberVo = apiMemberService.selectVoByAccount(bo.getPhone());
        if (storeMemberVo == null) {
            return R.fail("账号不存在！");
        }
        if (storeMemberVo.getPassword().equals(SecureUtil.md5(bo.getPassword()))) {
            return getLoginMemberInfo(request, storeMemberVo);
        }
        return R.fail("登录密码错误!");
    }

    /**
     * 手机号登录
     *
     * @param bo
     * @return
     */
    @SaIgnore
    @PostMapping("/login")
    public R<StoreMemberVo> login(@RequestBody StoreMemberLoginBo bo, HttpServletRequest request) {

        if (StringUtil.isEmpty(bo.getPhone())) {
            return R.fail("请输入手机号!");
        }
        if (StringUtil.isEmpty(bo.getSmsCode())) {
            return R.fail("请输入短信验证码!");
        }
        StoreMemberVo storeMemberVo = apiMemberService.selectVoByPhone(bo.getPhone());
        if (storeMemberVo==null) {
            return R.fail("账号不存在！");
        }
        String key = RedisUtil.CAPTCHA_CODE_KEY + "_login_" + bo.getPhone();
        Object object = RedisUtil.getCacheObject(key);
        if (object == null) {
            return R.fail("短信验证码失效！");
        } else {
            if (!bo.getSmsCode().equals(object.toString())) {
                return R.fail("短信验证码有误！");
            }
        }
        return getLoginMemberInfo(request, storeMemberVo);
    }

    /**
     * 找回密码
     *
     * @param bo
     * @return
     */
    @SaIgnore
    @PostMapping("/findPassword")
    public R<StoreMemberVo> findPassword(@RequestBody StoreMemberLoginBo bo, HttpServletRequest request) {

        if (StringUtil.isEmpty(bo.getPhone())) {
            return R.fail("请输入手机号!");
        }
        if (StringUtil.isEmpty(bo.getSmsCode())) {
            return R.fail("请输入短信验证码!");
        }
        if (StringUtil.isEmpty(bo.getPassword())) {
            return R.fail("请输入密码!");
        }
        if (bo.getPassword().length() < 6 || bo.getPassword().length() > 16) {
            return R.fail("密码长度应该在6~16位之间!");
        }
        StoreMemberVo storeMemberVo = apiMemberService.selectVoByPhone(bo.getPhone());
        if (storeMemberVo==null) {
            return R.fail("账号不存在！");
        }
        String key = RedisUtil.CAPTCHA_CODE_KEY + "_password_" + bo.getPhone();
        Object object = RedisUtil.getCacheObject(key);
        if (object == null) {
            return R.fail("短信验证码失效！");
        } else {
            if (!bo.getSmsCode().equals(object.toString())) {
                return R.fail("短信验证码有误！");
            }
        }
        apiMemberService.update(new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getPhone, bo.getPhone()).set(StoreMember::getPassword, SecureUtil.md5(bo.getPassword())));
        return getLoginMemberInfo(request, storeMemberVo);

    }

    /**
     * 退出登录
     *
     * @return
     */
    @SaIgnore
    @PostMapping("/logout")
    public R<Void> logout() {
        LoginObject.logout();
        return R.success("退出成功");
    }



}
