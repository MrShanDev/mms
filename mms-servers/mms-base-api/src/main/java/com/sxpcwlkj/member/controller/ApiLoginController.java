package com.sxpcwlkj.member.controller;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.code.entity.WxCodeBo;
import com.sxpcwlkj.common.enums.DeviceEnum;
import com.sxpcwlkj.common.enums.HttpStatusEnum;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.enums.WxCodeStatusEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.*;
import com.sxpcwlkj.framework.utils.AddressUtil;
import com.sxpcwlkj.member.entity.StoreMember;
import com.sxpcwlkj.member.entity.bo.StoreMemberBo;
import com.sxpcwlkj.member.entity.bo.StoreMemberLoginBo;
import com.sxpcwlkj.member.entity.bo.StoreMemberRegisterBo;
import com.sxpcwlkj.store.entity.bo.WxCodeLogoBo;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.store.enums.DefStaticEnum;
import com.sxpcwlkj.member.service.StoreMemberService;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import com.sxpcwlkj.wx.service.WxCodeService;
import com.sxpcwlkj.wx.service.WxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 会员登录
 * @author mmsAdmin
 */
@Tag(name = "🌳商城模块-登录注册",description = "登录注册等一些基础功能")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("mall-api/v1/common/login")
public class ApiLoginController extends BaseController {

    private final StoreMemberService apiMemberService;
    private final WxService wxService;
    private final WxCodeService wxCodeService;
    private final Environment environment;

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
    @Operation(summary = "token自动登录", description = "token自动登录")
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
    @Operation(summary = "微信静默登录", description = "微信静默登录")
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
    @Operation(summary = "微信注册/登录", description = "微信注册/登录")
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
                storeMemberBo.setSex(0);
                storeMemberBo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
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
    @Operation(summary = "账号会员注册", description = "账号会员注册")
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
        storeMemberBo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
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
     * @param bo bo
     */
    @SaIgnore
    @Operation(summary = "账号密码登录", description = "账号密码登录")
    @PostMapping("/accountLogin")
    public R<StoreMemberVo> accountLogin(@RequestBody @Validated StoreMemberLoginBo bo, HttpServletRequest request) {
        StoreMemberVo storeMemberVo=null;
        if(bo.getType()==1){
            if (StringUtil.isEmpty(bo.getAccount())) {
                return R.fail("请输入账号!");
            }
            if (StringUtil.isEmpty(bo.getPassword())) {
                return R.fail("请输入密码!");
            }
            storeMemberVo = apiMemberService.selectVoByAccount(bo.getAccount());
        }else {
            if (StringUtil.isEmpty(bo.getPhone())) {
                return R.fail("请输入手机号!");
            }
            if (StringUtil.isEmpty(bo.getSmsCode())) {
                return R.fail("请输入短信验证码!");
            }
            storeMemberVo = apiMemberService.selectVoByPhone(bo.getPhone());
        }
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
     * @param bo bo
     */
    @SaIgnore
    @Operation(summary = "手机号登录", description = "手机号登录")
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
     * @param bo bo
     */
    @SaIgnore
    @Operation(summary = "找回密码", description = "找回密码")
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
     */
    @SaIgnore
    @Operation(summary = "退出登录", description = "退出登录")
    @PostMapping("/logout")
    public R<Void> logout() {
        LoginObject.logout();
        return R.success("退出成功");
    }



    /**
     * 登录二维码
     * @return 二维码
     */
    @SaIgnore
    @PostMapping("/oauth-authorize")
    public R<Map<String,String>> oauthAuthorize(){
        Map<String,String> data= new HashMap<>();
        String uuid= RandomUtil.getRandomUUID();
        String codeUrl=null;
        if(Objects.equals(environment.getProperty("spring.profiles.active"), "dev")){
            codeUrl="123456";
        }else {
            codeUrl= wxCodeService.getCode(new WxCodeBo(uuid)
                .typeDocLogin()
                .expireTime(1000*60)
                .paramData(uuid));
            // 生成公众号的二维码，带参数，mms-wx 模块接收到参数后，进行登录操作
            // 公众号开发设置需要配置服务器地址，否则无法接收消息
        }

        data.put("url",codeUrl);
        data.put("uuid",uuid);
        return R.success(data);
    }

    /**
     * 登录二维码轮询，进行注册并登录
     * @param bo 请求
     * @return 登录状态
     */
    @SaIgnore
    @PostMapping("/oauth-polling")
    public R<StoreMemberVo> oauthPolling(@RequestBody WxCodeLogoBo bo, HttpServletRequest request, HttpServletResponse response){
        String uuid = bo.getUuid();
        if(uuid==null){
            return R.fail("state不能为空！");
        }
        //登录二维码
        WxCodeBo wxCodeBo= wxCodeService.getCodeState(new WxCodeBo(uuid).typeDocLogin());

        // dev环境下模拟设置openid用于测试
        if(Objects.equals(environment.getProperty("spring.profiles.active"), "dev")){
            if(wxCodeBo.getOpenId() == null || wxCodeBo.getOpenId().isEmpty()){
                wxCodeBo.setOpenId("osJ0d2P0nFMQfywdpDrr06elFW0Q");
                wxCodeBo.setState(WxCodeStatusEnum.SUCCEED.getValue());
                log.info("[DEV环境] 模拟设置登录用户openid: {}", wxCodeBo.getOpenId());
            }
        }

        if(Objects.equals(wxCodeBo.getState(), WxCodeStatusEnum.SUCCEED.getValue())){
            log.info(wxCodeBo.getOpenId());
            StoreMemberVo vov = apiMemberService.selectVoByOpenId(wxCodeBo.getOpenId());
            if(vov!=null){
                CookieUtil.setCookie(response,"docToken",vov.getToken(),1000*60*60*24*7);
                return getLoginMemberInfo(request, vov);
            }
            StoreMemberBo storeMemberBo = new StoreMemberBo();
            storeMemberBo.setAccount(null);
            storeMemberBo.setPhone(null);
            storeMemberBo.setNickname("");
            storeMemberBo.setWxOpenid(wxCodeBo.getOpenId());
            storeMemberBo.setHeadPortrait(DefStaticEnum.MEMBER_DEF_HEADER_IMG.getValue());
            storeMemberBo.setPassword(SecureUtil.md5(UUID.randomUUID().toString()));
            storeMemberBo.setSex(3);
            storeMemberBo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
            storeMemberBo.setReputationScore(100);
            storeMemberBo.setLevel(1);
            storeMemberBo.setInvitationCode(RandomUtil.getRandomNumber(12));
            String address= AddressUtil.getCityInfo(IPUtil.getIp(request));
            storeMemberBo.setLastLoginIp(IPUtil.getIp(request));
            storeMemberBo.setIPAddressInfo(address);
            Boolean aBoolean = apiMemberService.insert(storeMemberBo);
            if (aBoolean) {
                StoreMemberVo vo = apiMemberService.selectVoByOpenId(wxCodeBo.getOpenId());
                CookieUtil.setCookie(response,"docToken",vo.getToken(),1000*60*60*24*7);
                return getLoginMemberInfo(request, vo);
            }
        }
        return fail("登录失败,请尝试其他方式！");
    }


}
