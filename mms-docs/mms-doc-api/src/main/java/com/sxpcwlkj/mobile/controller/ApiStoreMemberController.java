package com.sxpcwlkj.mobile.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.util.StringUtils;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.utils.BeanCopyUtil;
import com.sxpcwlkj.common.utils.R;

import com.sxpcwlkj.mobile.entity.StoreMember;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberUpdateBo;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberVo;
import com.sxpcwlkj.mobile.service.StoreMemberService;
import com.sxpcwlkj.redis.RedisUtil;
import com.xkzhangsan.time.utils.StringUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 会员中心
 * @author sxpcwlkj
 * @date 2020/11/17
 * @description
 */

@SuppressWarnings("all")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/member")
public class ApiStoreMemberController extends BaseController {

    private final StoreMemberService apiMemberService;


    /**
     * 获取会员信息
     * 已登录用户自动获取
     * @return
     */
    @SaCheckLogin
    @GetMapping("/info")
    public R<StoreMemberVo> getMemberInfo() {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        StoreMemberVo vo = BeanCopyUtil.convert(storeMember, StoreMemberVo.class);
        return R.success("获取成功",vo);
    }

    /**
     * 更新会员信息
     * @param type 1:手机号 2:密码 3:昵称 4:头像 5:性别 6:账号 7:生日
     */
    @SaCheckLogin
    @SuppressWarnings("unchecked")
    @PostMapping("/updateMember")
    public R<String> updateMember(@RequestBody StoreMemberUpdateBo bo) {

        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        bo.setMemberId(storeMember.getId());
        //更换手机号
        if (bo.getType() == 1) {
            if (StringUtil.isEmpty(bo.getPhone())) {
                return R.fail("请输入手机号!");
            }
            if (StringUtils.isEmpty(bo.getSmsCode())) {
                return R.fail("请输入验证码!");
            }
            StoreMemberVo storeMemberVo = apiMemberService.selectVoByPhone(bo.getPhone());
            if (storeMemberVo!=null) {
                return R.fail("该手机号已被占用！");
            }
            String key = RedisUtil.CAPTCHA_CODE_KEY + "_updatePhone_" + bo.getPhone();
            Object object = RedisUtil.getCacheObject(key);
            if (object == null) {
                return R.fail("短信验证码失效！");
            } else {
                if (!bo.getSmsCode().equals(object.toString())) {
                    return R.fail("短信验证码有误！");
                }
            }

        } else if (bo.getType() == 2) {
            if (StringUtils.isEmpty(bo.getPassword())) {
                return R.fail("请输入密码!");
            }
            if (bo.getPassword().length() < 6||bo.getPassword().length() >16) {
                return R.fail("密码长度应该在6~16位之间!");
            }
            bo.setPassword(SecureUtil.md5(bo.getPassword()));
        } else if (bo.getType() == 3) {
            if (StringUtils.isEmpty(bo.getNickname())) {
                return R.fail("请传入昵称!");
            }
        } else if (bo.getType() == 4) {
            if (StringUtils.isEmpty(bo.getHeadPortrait())) {
                return R.fail("请传入头像地址!");
            }
        }else if (bo.getType() == 5) {
            if (!(bo.getSex()==1||bo.getSex()==2||bo.getSex()==3)) {
                return R.fail("请传入正确的性别！");
            }
        }else if (bo.getType() == 6) {
            if (StringUtils.isEmpty(bo.getAccount())) {
                return R.fail("请传入账号!");
            }
            StoreMemberVo storeMemberVo = apiMemberService.selectVoByAccount(bo.getAccount());
            if (storeMemberVo!=null) {
                return R.fail("该账号已被占用！");
            }
        }else if (bo.getType() == 7) {
            if (bo.getBirthday()==null) {
                return R.fail("请传入生日（yyyy-MM-dd）!");
            }
        }
        return apiMemberService.updateMember(bo);
    }


    /**
     * 实名认证
     * @param a 身份证正面网络地址
     * @param b 身份证反面网络地址
     * @return
     */
    @SaCheckLogin
    @GetMapping("/authentication")
    public R<Map<String, String>> authentication(@Validated @NotNull(message = "身份证正面不能为空") String one,@Validated @NotNull(message = "身份证背面不能为空") String two) {
        return apiMemberService.authentication(one,two);
    }


}
