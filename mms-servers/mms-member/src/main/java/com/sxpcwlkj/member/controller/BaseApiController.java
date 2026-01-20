package com.sxpcwlkj.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.email.service.EmailService;
import com.sxpcwlkj.member.entity.StoreSmsBo;
import com.sxpcwlkj.member.entity.StoreToolArea;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.member.mapper.ApiToolAreaMapper;
import com.sxpcwlkj.member.service.StoreMemberService;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.IPUtil;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.member.entity.StoreSysConfigVo;
import com.sxpcwlkj.member.service.ApiSysConfigService;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.sms.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * 基础接口
 * @author mmsAdmin
 */
@Tag(name = "🌳基础接口",description = "公共接口")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("base-api/v1")
public class BaseApiController extends BaseController {

    private final FileStorageService fileStorageService;
    private final StoreMemberService apiMemberService;
    private final Environment env;
    private final ApiToolAreaMapper storeToolAreaMapper;
    private final SmsService smsService;
    private final ApiSysConfigService sysConfigService;
    private final EmailService emailService;
    /**
     * 网站信息
     * @return 网站信息
     */
    @SaIgnore
    @Operation(summary = "网站信息", description = "返回自定义类型,系统配置前缀：website_")
    @GetMapping("/getWebsite")
    public R<List<StoreSysConfigVo>> getWebsite(){
        return R.success(sysConfigService.selectWebsiteConfigList());
    }

    /**
     * 根据key查询单个配置
     * @param key 配置key
     * @return 配置
     */
    @SaIgnore
    @Operation(summary = "根据key查询单个系统配置", description = "website_开头的系统变量")
    @GetMapping("/getConfigByKey/{key}")
    public R<StoreSysConfigVo> getConfig(@PathVariable String key){
        return R.success(sysConfigService.selectVoByKey(key));
    }

    /**
     * 发送短信验证码
     * @param bo 报参对象
     * @param request  request
     * @return  R<Void>
     */
    @Operation(summary = "发送短信验证码", description = "1:注册 2:登录 3:修改密码 4:支付密码 5:更换手机号")
    @SaIgnore
    @PostMapping("/smsCode")
    public R<Void> smsCode(@RequestBody StoreSmsBo bo, HttpServletRequest request) {

        Integer type = bo.getType();

        String phone = bo.getPhone();

        String keytype = "_register_";
        if (type == 1) {
            keytype = "_register_";
        }
        if (type == 2) {
            keytype = "_login_";
        }
        if (type == 3) {
            keytype = "_password_";
        }
        if (type == 4) {
            keytype = "_passwordPay_";
        }
        if (type == 5) {
            keytype = "_updatePhone_";
        }
        if (Objects.equals(env.getProperty("spring.profiles.active"), "prod")) {
            try {
                String ip = null;
                ip = IPUtil.getIp(request);
                log.info("ips:" + ip);
                Object s = RedisUtil.getCacheObject("ip:" + ip);
                int num = Convert.toInt(s == null ? 0 : Convert.toInt(s), 0);
                if (num >= 10) {
                    log.info("ips>=10,拦截:" + ip);
                    return R.fail("当前IP发送频繁,请一天后再发送哦！");
                }
                num++;
                RedisUtil.setCacheObject("ip:" + ip, num);
                RedisUtil.setCacheObject("ip:" + ip, num, Duration.ofDays(1));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        StoreMemberVo storeMemberVos = apiMemberService.selectVoByPhone(phone);
        if (type == 1) {
            if (storeMemberVos != null) {
                return R.fail("该手机号已注册！");
            }
        }
        if (type == 2 || type == 3) {

            if (storeMemberVos == null) {
                return R.fail("该手机号账号不存在！");
            }
            if (!Objects.equals(storeMemberVos.getStatus(), SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())) {
                throw new MmsException("会员状态不正常！");
            }
        }

        String key = RedisUtil.CAPTCHA_CODE_KEY + keytype + phone;
        Object object = RedisUtil.getCacheObject(key);
        if (object != null) {
            RedisUtil.deleteObject(key);
            return R.fail("发送频繁,请稍后再发送哦！");
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        String code = RandomUtil.randomNumbers(6);
        smsService.sendSms(phone, code);
        RedisUtil.setCacheObject(key, code);
        RedisUtil.expire(key, Duration.ofMinutes(1));
        if (Objects.equals(env.getProperty("spring.profiles.active"), "prod")) {
            return R.success("手机短信码已发送！");
        }
        return R.success("手机短信码已发送！"+code);
    }

    /**
     * 上传文件
     * 成功返回文件 url
     */
    @SaCheckLogin
    @Operation(summary = "上传文件", description = "上传文件")
    @PostMapping("/uploadFile")
    public R<String> uploadFile(MultipartFile file) {
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath("upload/") //保存到相对路径下，为了方便管理，不需要可以不写
                .setObjectId("0")   //关联对象id，为了方便管理，不需要可以不写
                .setObjectType("0") //关联对象类型，为了方便管理，不需要可以不写
                .putAttr("role", "admin") //保存一些属性，可以在切面、保存上传记录、自定义存储平台等地方获取使用，不需要可以不写
                .upload();  //将文件上传到对应地方
        return fileInfo == null ? R.fail("上传失败") : R.success("上传成功", fileInfo.getUrl());
    }
    /**
     * 获取区域
     * @param provincialName 不传 默认排序第一个省
     * @param cityName       不传 默认排序第一个市
     */
    @Deprecated
    @SaIgnore
    @Operation(summary = "获取区域", description = "获取区域")
    @GetMapping("/storeToolAreaList")
    public R<List<Object>> storeToolAreaList(String provincialName, String cityName) {

        List<StoreToolArea> areasOne = storeToolAreaMapper.selectList(new LambdaQueryWrapper<StoreToolArea>()
                .eq(StoreToolArea::getParentCode, "0").orderByAsc(StoreToolArea::getSort));
        String[] array = areasOne.stream().map(StoreToolArea::getName).toArray(String[]::new);

        List<StoreToolArea> areasTwo = new ArrayList<>();
        List<StoreToolArea> areasThree = new ArrayList<>();

        if (!areasOne.isEmpty()) {
            //获取第一个
            String provincialCode = areasOne.get(0).getCode();
            // 指定省
            if (StringUtil.isNotEmpty(provincialName)) {
                StoreToolArea areaClick = storeToolAreaMapper.selectOne(new LambdaQueryWrapper<StoreToolArea>()
                        .eq(StoreToolArea::getParentCode, 0)
                        .eq(StoreToolArea::getName, provincialName)
                        .last("LIMIT 1"));
                if (areaClick != null) {
                    provincialCode = areaClick.getCode();
                    areasTwo = storeToolAreaMapper.selectList(new LambdaQueryWrapper<StoreToolArea>()
                            .eq(StoreToolArea::getParentCode, provincialCode)
                            .orderByAsc(StoreToolArea::getSort));
                    if (!areasTwo.isEmpty()) {
                        String cityCode = areasTwo.get(0).getCode();
                        // 指定市
                        if (StringUtil.isNotEmpty(cityName)) {
                            StoreToolArea area = storeToolAreaMapper.selectOne(new LambdaQueryWrapper<StoreToolArea>()
                                    .eq(StoreToolArea::getParentCode, provincialCode)
                                    .eq(StoreToolArea::getName, cityName)
                                    .last("LIMIT 1"));
                            if (area != null) {
                                cityCode = area.getCode();
                            }
                        }
                        areasThree = storeToolAreaMapper.selectList(new LambdaQueryWrapper<StoreToolArea>()
                                .eq(StoreToolArea::getParentCode, cityCode).orderByAsc(StoreToolArea::getSort));

                    }
                }
                else{
                    areasTwo = storeToolAreaMapper.selectList(new LambdaQueryWrapper<StoreToolArea>()
                            .eq(StoreToolArea::getParentCode, provincialCode).orderByAsc(StoreToolArea::getSort));
                    if (!areasTwo.isEmpty()) {
                        String cityCode = areasTwo.get(0).getCode();
                        // 指定市
                        if (StringUtil.isNotEmpty(cityName)) {
                            StoreToolArea area = storeToolAreaMapper.selectOne(new LambdaQueryWrapper<StoreToolArea>()
                                    .eq(StoreToolArea::getParentCode, provincialCode)
                                    .eq(StoreToolArea::getName, cityName)
                                    .last("LIMIT 1"));
                            if (area != null) {
                                cityCode = area.getCode();
                            }
                        }
                        areasThree = storeToolAreaMapper.selectList(new LambdaQueryWrapper<StoreToolArea>()
                                .eq(StoreToolArea::getParentCode, cityCode).orderByAsc(StoreToolArea::getSort));
                    }
                }
            }else {
                areasTwo = storeToolAreaMapper.selectList(new LambdaQueryWrapper<StoreToolArea>()
                        .eq(StoreToolArea::getParentCode, provincialCode).orderByAsc(StoreToolArea::getSort));
                if (!areasTwo.isEmpty()) {
                    String cityCode = areasTwo.get(0).getCode();
                    // 指定市
                    if (StringUtil.isNotEmpty(cityName)) {
                        StoreToolArea area = storeToolAreaMapper.selectOne(new LambdaQueryWrapper<StoreToolArea>()
                                .eq(StoreToolArea::getParentCode, provincialCode)
                                .eq(StoreToolArea::getName, cityName)
                                .last("LIMIT 1"));
                        if (area != null) {
                            cityCode = area.getCode();
                        }
                    }
                    areasThree = storeToolAreaMapper.selectList(new LambdaQueryWrapper<StoreToolArea>()
                            .eq(StoreToolArea::getParentCode, cityCode).orderByAsc(StoreToolArea::getSort));

                }
            }

        }

        List<Object> list = new ArrayList<>();

        String[] arrayTwo = areasTwo.stream().map(StoreToolArea::getName).toArray(String[]::new);
        String[] arrayThree = areasThree.stream().map(StoreToolArea::getName).toArray(String[]::new);

        list.add(array);
        list.add(arrayTwo);
        list.add(arrayThree);

        return R.success("获取成功", list);

    }

    /**
     * 发送邮件
     */
    @SaIgnore
    @Operation(summary = "发送邮件", description = "发送邮件")
    @PostMapping("/sendEmail")
    public R<Void> sendEmail(@RequestParam String email) {
        return R.success("邮件已发送，请查收！");
    }

    /**
     * 发送邮箱
     */
    @SaCheckLogin
    @Operation(summary = "会员设置邮箱", description = "会员设置邮箱")
    @PostMapping("/setEmail")
    public R<Object> setEmail(@Validated @NotNull(message = "邮箱不能为空") String email) {
        String code = RandomUtil.randomNumbers(6);
        return emailService.sendRegisterCode(email, code);
    }
}
