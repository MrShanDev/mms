package com.sxpcwlkj.framework.advice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sxpcwlkj.common.annotation.MssSafety;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.entity.AesKeyEntity;
import com.sxpcwlkj.framework.entity.SysSign;
import com.sxpcwlkj.framework.sercice.SysSignService;
import com.sxpcwlkj.framework.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xijue
 * 添加 @MssSafety 接口响应数据加密
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ResponseResultBodyAdvice implements ResponseBodyAdvice<Object> {
    private final SysSignService sysSignService;
    /**
     * 判断类或者方法是否使用了 @MssSafety
     */
    @Override
    public boolean supports(MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        if (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), MssSafety.class) || returnType.hasMethodAnnotation(MssSafety.class)) {
            MssSafety apiSecurity = returnType.getMethodAnnotation(MssSafety.class);
            if (apiSecurity == null) {
                return false;
            }
            return  apiSecurity.encryptResponse();
        }
        return false;
    }

    /**
     * 当类或者方法使用了 @ResponseResultBody 就会调用这个方法
     * 如果返回类型是string，那么springmvc是直接返回的，此时需要手动转化为json
     * 因为当body都为null时，下面的非加密下的if判断参数类型的条件都不满足，如果接口返回类似为String，
     * 会报错com.shepherd.fast.global.ResponseVO cannot be cast to java.lang.String
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request,
                                  @NotNull ServerHttpResponse response) {
        MssSafety apiSecurity = getApiSecurity(returnType);
        boolean isEncrypt=  apiSecurity.encryptResponse();

        Method method = returnType.getMethod();
        assert method != null;
        Class<?> returnClass = method.getReturnType();
        int code=200;
        long total=0;
        String msg="操作成功";
        Object data=body;
        if(apiSecurity.encryptType().getType().equals(SignUtil.ENCRYPTION_TYPE_AES)){
            SysSign sysSign = sysSignService.getSign();
            if (sysSign == null) {
                throw new MmsException("秘钥不存在/请重新登录");
            }
            if(returnClass.equals(TableDataInfo.class)){
                data = ((TableDataInfo<?>) body).getRows();
                data = JSONArray.toJSONString(data);
                code = ((TableDataInfo<?>) body).getCode();
                msg = ((TableDataInfo<?>) body).getMsg();
                total = ((TableDataInfo<?>) body).getTotal();
            }
            if(returnClass.equals(R.class)){
                assert body instanceof R<?>;
                data = ((R<?>) body).getData();
                data = JSONObject.toJSONString(data);
                code = ((R<?>) body).getCode();
                msg = ((R<?>) body).getMsg();
            }
            if(code==200){
                // 加密返回数据
                ApiSecurityParam param1 = new ApiSecurityParam()
                    .setAppid(sysSign.getAppId())
                    .addData(data.toString());
                ApiSecurityParam  apiSecurityParam1 = SignUtil.encryptAesCryptoJs(param1,new AesKeyEntity(sysSign.getAppId(),sysSign.getSecretKey()));
                data = apiSecurityParam1.getSign();
                if(body instanceof TableDataInfo<?>){
                    Map<String,String> map=new HashMap<>();
                    map.put("total",total+"");
                    map.put("rows",data.toString());
                    data = map;
                }
                return R.security(code,msg,data);
            }

        }
        return body;
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

    /**
     * 当类或者方法使用了 @ResponseResultBody 就会调用这个方法
     * 如果返回类型是string，那么springmvc是直接返回的，此时需要手动转化为json
     * 因为当body都为null时，下面的非加密下的if判断参数类型的条件都不满足，如果接口返回类似为String，
     * 会报错com.shepherd.fast.global.ResponseVO cannot be cast to java.lang.String
     */
//    @SneakyThrows
//    @Override
//    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        Method method = returnType.getMethod();
//        Class<?> returnClass = method.getReturnType();
//        Boolean enable = apiSecurityProperties.getEnable();
//        MssSafety apiSecurity = method.getAnnotation(MssSafety.class);
//        if (Objects.isNull(apiSecurity)) {
//            apiSecurity = method.getDeclaringClass().getAnnotation(MssSafety.class);
//        }
//        if (enable && Objects.nonNull(apiSecurity) && apiSecurity.encryptResponse() && Objects.nonNull(body)) {
//            // 只需要加密返回data数据内容
//            if (body instanceof ResponseVO) {
//                body = ((ResponseVO) body).getData();
//            }
//            JSONObject jsonObject = encryptResponse(body);
//            body = jsonObject;
//        } else {
//            if (body instanceof String || Objects.equals(returnClass, String.class)) {
//                String value = objectMapper.writeValueAsString(ResponseVO.success(body));
//                return value;
//            }
//            // 防止重复包裹的问题出现
//            if (body instanceof ResponseVO) {
//                return body;
//            }
//        }
//        return ResponseVO.success(body);
//    }

    JSONObject encryptResponse(Object result) {
        String aseKey = "aseKey";
        String content = JSONObject.toJSONString(result);
        String data = "";
        String key = "key";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", key);
        jsonObject.put("data", data);
        return jsonObject;
    }
}
