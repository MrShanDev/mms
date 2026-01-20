package com.sxpcwlkj.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.BeanCopyUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.member.entity.StoreMember;
import com.sxpcwlkj.member.entity.StoreMemberAddress;

import com.sxpcwlkj.member.entity.bo.StoreMemberAddressBo;
import com.sxpcwlkj.member.entity.bo.StoreMemberUpdateBo;
import com.sxpcwlkj.member.entity.vo.StoreMemberAddressVo;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.member.service.StoreMemberAddressService;
import com.sxpcwlkj.member.service.StoreMemberService;
import com.sxpcwlkj.redis.RedisUtil;
import com.xkzhangsan.time.utils.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 会员中心
 * @author mmsAdmin
 */

@Tag(name = "🌳会员中心",description = "会员等一些基础功能")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("mms-api/v1/member")
public class ApiStoreMemberController extends BaseController {

    private final StoreMemberService apiMemberService;
    private final StoreMemberAddressService apiMemberAddressService;

    /**
     * 已登录会员信息
     */

    @SaCheckLogin
    @Operation(summary = "已登录会员信息", description = "已登录会员信息")
    @GetMapping("/info")
    public R<StoreMemberVo> getMemberInfo() {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        StoreMemberVo vo = BeanCopyUtil.convert(storeMember, StoreMemberVo.class);
        return R.success("获取成功",vo);
    }

    /**
     * 会员信息更新
     */
    @SaCheckLogin
    @Operation(summary = "会员信息更新", description = "会员信息更新")
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

        }
        else if (bo.getType() == 2) {
            if (StringUtils.isEmpty(bo.getPassword())) {
                return R.fail("请输入密码!");
            }
            if (bo.getPassword().length() < 6||bo.getPassword().length() >16) {
                return R.fail("密码长度应该在6~16位之间!");
            }
            bo.setPassword(SecureUtil.md5(bo.getPassword()));
        }
        else if (bo.getType() == 3) {
            if (StringUtils.isEmpty(bo.getNickname())) {
                return R.fail("请传入昵称!");
            }
        }
        else if (bo.getType() == 4) {
            if (StringUtils.isEmpty(bo.getHeadPortrait())) {
                return R.fail("请传入头像地址!");
            }
        }
        else if (bo.getType() == 5) {
            if (!(bo.getSex()==0||bo.getSex()==1||bo.getSex()==2)) {
                return R.fail("请传入正确的性别！");
            }
        }
        else if (bo.getType() == 6) {
            if (StringUtils.isEmpty(bo.getAccount())) {
                return R.fail("请传入账号!");
            }
            StoreMemberVo storeMemberVo = apiMemberService.selectVoByAccount(bo.getAccount());
            if (storeMemberVo!=null) {
                return R.fail("该账号已被占用！");
            }
        }
        else if (bo.getType() == 7) {
            if (bo.getBirthday()==null) {
                return R.fail("请传入生日（yyyy-MM-dd）!");
            }
        }
        return apiMemberService.updateMember(bo);
    }


    /**
     * 会员实名认证
     */
    @SaCheckLogin
    @Operation(summary = "会员实名认证", description = "会员实名认证")
    @GetMapping("/authentication")
    public R<Map<String, String>> authentication(@Validated @NotNull(message = "身份证正面不能为空") String one,@Validated @NotNull(message = "身份证背面不能为空") String two) {
        return apiMemberService.authentication(one,two);
    }



    /**
     * 会员地址列表-分页查询
     */
    @SaCheckLogin
    @Operation(summary = "会员地址列表", description = "会员地址列表")
    @PostMapping("/address/list")
    public R<TableDataInfo<StoreMemberAddressVo>> listPage(PageQuery pageQuery){
        StoreMemberAddressBo bo=new StoreMemberAddressBo();
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        bo.setMemberId(storeMember.getId());
        return R.success(apiMemberAddressService.selectListVoPage(bo, pageQuery));
    }

    /**
     * 会员地址详情
     * @param id 主键ID
     */
    @SaCheckLogin
    @Operation(summary = "会员地址详情", description = "会员地址详情")
    @GetMapping("/address/{id}")
    public R<StoreMemberAddressVo> queryById(@PathVariable String id) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        return success(apiMemberAddressService.selectVoByIdAndMid(id, storeMember.getId()));
    }

    /**
     * 会员地址编辑
     */
    @SaCheckLogin
    @Operation(summary = "会员地址编辑", description = "会员地址编辑")
    @PostMapping("/address/edit")
    public R<Boolean> edit(@Validated @RequestBody(required = false) StoreMemberAddressBo bo) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        bo.setMemberId(storeMember.getId());
        //解决 revision  问题
        StoreMemberAddressVo storeMemberAddressVo = apiMemberAddressService.selectVoByIdMid(bo.getId(), storeMember.getId());
        if(storeMemberAddressVo==null){
            return fail("地址不存在！");
        }
        bo.setRevision(storeMemberAddressVo.getRevision());
        bo.setUpdatedTime(new Date());
        // 如果是默认地址，则将之前的默认地址设为非默认
        if(bo.getTolerant().equals("1")){
            apiMemberAddressService.update(new LambdaUpdateWrapper<StoreMemberAddress>()
                .eq(StoreMemberAddress::getMemberId,bo.getMemberId())
                .set(StoreMemberAddress::getTolerant,0));
        }
        return success(apiMemberAddressService.updateById(bo));
    }

    /**
     * 会员地址设为默认
     * @param id 地址ID
     */
    @SaCheckLogin
    @Operation(summary = "会员地址设为默认", description = "会员地址设为默认")
    @GetMapping("/address/updateDef")
    public R<Boolean> updateDef( String id) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        StoreMemberAddressVo storeMemberAddressVo = apiMemberAddressService.selectVoByIdAndMid(id, storeMember.getId());
        if(storeMemberAddressVo!=null){

            apiMemberAddressService.update(new LambdaUpdateWrapper<StoreMemberAddress>()
                .eq(StoreMemberAddress::getMemberId, storeMember.getId())
                .set(StoreMemberAddress::getTolerant,0));

            apiMemberAddressService.update(new LambdaUpdateWrapper<StoreMemberAddress>()
                .eq(StoreMemberAddress::getId,id)
                .set(StoreMemberAddress::getTolerant,1));
            return success(true);
        }
        return fail("修改失败");
    }

    /**
     * 会员地址新增
     */
    @SaCheckLogin
    @Operation(summary = "会员地址新增", description = "会员地址新增")
    @PostMapping("/address/insert")
    public R<Boolean> insert(@Validated @RequestBody(required = false) StoreMemberAddressBo bo) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        bo.setMemberId(storeMember.getId());
        bo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
        bo.setId(null);
        bo.setCountry("中国");
        bo.setMemberId(storeMember.getId());
        // 如果是默认地址，则将之前的默认地址设为非默认
        if(bo.getTolerant().equals("1")){
            apiMemberAddressService.update(new LambdaUpdateWrapper<StoreMemberAddress>()
                .eq(StoreMemberAddress::getMemberId,bo.getMemberId())
                .set(StoreMemberAddress::getTolerant,0));
        }
        return success(apiMemberAddressService.insert(bo));
    }

    /**
     * 会员地址删除
     * @param id 主键ID
     */
    @SaCheckLogin
    @Operation(summary = "会员地址删除", description = "会员地址删除")
    @GetMapping("/address/delete/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        return success(apiMemberAddressService.deleteByIdAndMid(id, storeMember.getId()));
    }

}
