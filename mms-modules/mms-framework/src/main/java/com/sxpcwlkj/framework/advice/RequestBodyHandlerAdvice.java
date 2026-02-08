package com.sxpcwlkj.framework.advice;

import cn.hutool.core.io.IoUtil;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.constant.Constants;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.exception.ParamsException;
import com.sxpcwlkj.common.properties.EncryptionProperties;
import com.sxpcwlkj.common.utils.JsonUtil;
import com.sxpcwlkj.framework.entity.AesKeyEntity;
import com.sxpcwlkj.framework.entity.RsaKeyEntity;
import com.sxpcwlkj.framework.entity.SysSign;
import com.sxpcwlkj.framework.service.SysSignService;
import com.sxpcwlkj.framework.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * @author mmsAdmin
 * 添加 @MssSafety 接口请求参数解密
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RequestBodyHandlerAdvice implements RequestBodyAdvice {

    private final StringRedisTemplate stringRedisTemplate;

    private final EncryptionProperties encryptionProperties;
    private final SysSignService sysSignService;

    private static final String SIGN_KEY = "X-Sign";
    private static final String NONCE_KEY = "X-Nonce";
    private static final String TIMESTAMP_KEY = "X-Timestamp";
    private static final String ENCRYPT_TYPE = "Encrypt-Type";
    private static final String APPID = "App-Id";


    /**
     * @param methodParameter 包含控制器方法的参数信息
     * @param targetType      目标类型，即请求体将要转换成的 Java 类型
     * @param converterType   将要使用的消息转换器的类型
     * @return 返回true表示支持，false表示不支持
     */
    @Override
    public boolean supports(MethodParameter methodParameter,
                            @NotNull Type targetType,
                            @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        if (AnnotatedElementUtils.hasAnnotation(methodParameter.getContainingClass(), MssSafety.class) || methodParameter.hasMethodAnnotation(MssSafety.class)) {
            MssSafety apiSecurity = methodParameter.getMethodAnnotation(MssSafety.class);
            if (apiSecurity == null) {
                return false;
            }
            return apiSecurity.decryptRequest() || apiSecurity.isRepetition();
        }
        return false;
    }

    /**
     * 接口入参 处理前
     * 将请求需要的请求体脱离出来
     *
     * @param inputMessage  包含 HTTP 请求的头和体
     * @param parameter     包含控制器方法的参数信息
     * @param targetType    目标类型，即请求体将要转换成的 Java 类型
     * @param converterType 将要使用的消息转换器的类型
     * @return 返回新的流
     * @throws IOException 如果发生 I/O 错误
     */
    @Override
    public @NotNull HttpInputMessage beforeBodyRead(@NotNull HttpInputMessage inputMessage,
                                                    @NotNull MethodParameter parameter,
                                                    @NotNull Type targetType,
                                                    @NotNull Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        MssSafety apiSecurity = getApiSecurity(parameter);
        InputStream inputStream = inputMessage.getBody();
        String body = IoUtil.read(inputStream, StandardCharsets.UTF_8);
        if (StringUtils.isBlank(body)) {
            throw new ParamsException("请求参数body不能为空");
        }
        //记录nonce 防止重复提交
        String nonce = parameter.getParameterName()+":"+parameter.getExecutable().getName();
        // 加密传参格式固定为ApiSecurityParam
        JSONObject bodyJson = JsonUtil.parseObject(body, JSONObject.class);
        assert bodyJson != null;
        ApiSecurityParam apiSecurityParam = JSON.parseObject(bodyJson.toJSONString(), ApiSecurityParam.class);
        // 加密传参ApiSecurityParam可以接收签名参数，这里把签名参数放到header里面，方便在后面afterBodyRead中验签
        HttpHeaders headers = inputMessage.getHeaders();
        //防重幂等
        if(apiSecurity.isRepetition()){
            boolean nonceExists = Boolean.TRUE.equals(stringRedisTemplate.hasKey(NONCE_KEY + nonce));
            if (nonceExists) {
                //请求重复
                throw new MmsException("请求过快，请稍后再试！");
            }
            stringRedisTemplate.opsForValue().set(NONCE_KEY + nonce, "1", encryptionProperties.getRepeatedTime(), TimeUnit.MILLISECONDS);
        }
        //解密
        if(apiSecurity.decryptRequest()){
            String timestamp = apiSecurityParam.getTimestamp();
            if (StringUtils.isNotBlank(timestamp)) {
                headers.set(TIMESTAMP_KEY, timestamp);
            }
            String sign = apiSecurityParam.getSign();
            if (StringUtils.isNotBlank(sign)) {
                headers.set(SIGN_KEY, sign);
            }
            if (apiSecurityParam.getAppId() == null){
                apiSecurityParam.setAppid(headers.getFirst(APPID));
            }
            // 验证签名sign
            // 获取aes秘钥
            SysSign sysSign = sysSignService.getSign();
            if (sysSign == null) {
                throw new MmsException("秘钥不存在/请重新登录");
            }
            commonVerify(inputMessage.getHeaders());
            if (apiSecurity.encryptType().getType().equalsIgnoreCase(SignUtil.ENCRYPTION_TYPE_AES)) {
                SignUtil.verifyAesCryptoJs(apiSecurityParam, new AesKeyEntity(sysSign.getAppId(), sysSign.getSecretKey()));
            }
            //非对称解密验签
            if (apiSecurity.encryptType().getType().equalsIgnoreCase(SignUtil.ENCRYPTION_TYPE_RSA)) {
                SignUtil.verifyRsa(apiSecurityParam, new RsaKeyEntity(sysSign.getPublicKey(), sysSign.getPrivateKey()));
            } else {
                throw new MmsException("加密方式不匹配");
            }
            // 使用解密后的数据构造新的读取流, Spring MVC后续读取解析转换为接口
            JSONObject dataJson = JsonUtil.parseObject(apiSecurityParam.getData(), JSONObject.class);
            return new MssHttpInputMessage(headers, new ByteArrayInputStream(JSON.toJSONString(dataJson).getBytes()));
        }
        return new MssHttpInputMessage(headers, new ByteArrayInputStream(bodyJson.toString().getBytes()));
    }

    /**
     * 接口入参 处理后
     *
     * @param body 已转换的 Java 对象，表示请求体的数据
     *             其余参数和上面的{@link #beforeBodyRead(HttpInputMessage, MethodParameter, Type, Class)} 一样
     */
    @Override
    public @NotNull Object afterBodyRead(@NotNull Object body,
                                         @NotNull HttpInputMessage inputMessage,
                                         @NotNull MethodParameter parameter,
                                         @NotNull Type targetType,
                                         @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        //MssSafety apiSecurity = getApiSecurity(parameter);
        //boolean isSign = apiSecurity.isSign() && apiSecurity.encryptResponse();
        // TODO 这里可以对body进行处理
        return body;
    }

    /**
     * body为空的这种情况，比如当body位空时，返回一个默认对象啥的
     *
     * @param inputMessage  包含 HTTP 请求的头和体
     * @param parameter     包含控制器方法的参数信息
     * @param targetType    目标类型，即请求体将要转换成的 Java 类型
     * @param converterType 将要使用的消息转换器的类型
     * @return 返回新的流
     */
    @Override
    public Object handleEmptyBody(Object body,
                                  @NotNull HttpInputMessage inputMessage,
                                  @NotNull MethodParameter parameter,
                                  @NotNull Type targetType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return inputMessage;
    }


    /**
     * 基础验证签名
     * @param headers 请求头
     */
    void commonVerify(HttpHeaders headers){

        String sign = headers.getFirst(SIGN_KEY);
        if (StringUtils.isBlank(sign)) {
            throw new ParamsException("签名["+SIGN_KEY+"]不能为空");
        }
        String timestamp = headers.getFirst(TIMESTAMP_KEY);
        if (StringUtils.isBlank(timestamp)) {
            throw new ParamsException("请求头缺少:"+TIMESTAMP_KEY+"参数");
        }
        try {
            long time = Long.parseLong(timestamp);
            // 判断timestamp时间戳与当前时间是否超过签名有效时长（过期时间根据业务情况进行配置）,如果超过了就提示签名过期
            long now = System.currentTimeMillis();
            log.info("当前时间戳：{}", now - time);
            if (now - time > encryptionProperties.getValidTime()) {
                throw new MmsException("签名已过期");
            }
        } catch (Exception e) {
            throw new MmsException("签名已过期");
        }
        String encryptType = headers.getFirst(ENCRYPT_TYPE);
        if (StringUtils.isBlank(encryptType)) {
            throw new ParamsException("请求头缺少:"+ENCRYPT_TYPE+"参数");
        }
        String appId = headers.getFirst(Constants.APPID);
        if (StringUtils.isBlank(appId)) {
            throw new ParamsException("请求头缺少:"+Constants.APPID+"参数");
        }

    }

    /**
     * 获取接口注解
     *
     * @param methodParameter 包含控制器方法的参数信息
     * @return 接口注解
     */
    MssSafety getApiSecurity(MethodParameter methodParameter) {
        return methodParameter.getMethodAnnotation(MssSafety.class);
    }

}
