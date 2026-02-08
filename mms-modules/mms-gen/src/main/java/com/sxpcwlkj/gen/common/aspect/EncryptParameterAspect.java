package com.sxpcwlkj.gen.common.aspect;

import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.common.code.entity.PageResult;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.framework.entity.AesKeyEntity;
import com.sxpcwlkj.framework.entity.SysSign;
import com.sxpcwlkj.framework.service.SysSignService;
import com.sxpcwlkj.framework.utils.SignUtil;
import com.sxpcwlkj.gen.common.annotation.EncryptParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 处理参数加密解密切面
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class EncryptParameterAspect {
    private final SysSignService sysSignService;
    /**
     * 切面方法：page、list、get、save、update、tableList
     *
     */
    @Around("execution(* com.sxpcwlkj.gen.controller.DataSourceController.*(..))")
    public Object doProcess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 处理请求入参
        List<Object> methodArgs = this.getMethodArgs(proceedingJoinPoint);
        for (Object item : methodArgs) {
            handleItem(item, true);
        }
        Object result = proceedingJoinPoint.proceed();

        // 处理返回值
        handleObject(result);
        return result;
    }

    /**
     * 获取方法的请求参数
     */
    private List<Object> getMethodArgs(ProceedingJoinPoint proceedingJoinPoint) {
        List<Object> methodArgs = new ArrayList<>();
        for (Object arg : proceedingJoinPoint.getArgs()) {
            if (Objects.nonNull(arg)) {
                methodArgs.add(arg);
            }
        }
        return methodArgs;
    }

    /**
     * 加密返回结果中的字段
     *
     */
    private void handleObject(Object object) throws Exception {
        // 仅处理类型是Result的返回对象
        if (!(object instanceof R) || Objects.isNull(((R<?>) object).getData())) {
            return;
        }

        Object data = ((R<?>) object).getData();
        if (data instanceof List || data instanceof TableDataInfo || data instanceof PageResult) {
            List<?> itemList = data instanceof List ? (List<?>) data : (data instanceof TableDataInfo ? ((TableDataInfo<?>) data).getRows() : ((PageResult<?>) data).getRows());
            itemList.forEach(f ->
                    handleItem(f, false)
            );
        } else {
            handleItem(data, false);
        }
    }

    /**
     * 加密/解密具体对象下的字段
     *
     * @param item      需要加解密的对象
     * @param isDecrypt true：解密，false：加密
     */
    private void handleItem(Object item, boolean isDecrypt) {

        // 只处理在entity包下面的对象
        if (Objects.isNull(item.getClass().getPackage()) || !item.getClass().getPackage().getName().startsWith("com.sxpcwlkj.gen.entity;")) {
            return;
        }

        // 遍历所有字段
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 若该字段被EncryptParameter注解,则进行解密/加密
            Class<?> fieldType = field.getType();
            if (fieldType == String.class && Objects.nonNull(AnnotationUtils.findAnnotation(field, EncryptParameter.class))) {
                // 设置private类型允许访问
                field.setAccessible(Boolean.TRUE);
                try {
                    SysSign sysSign = sysSignService.getSign();
                    if (sysSign == null) {
                        throw new MmsException("秘钥不存在/请重新登录");
                    }
                    AesKeyEntity aes= new AesKeyEntity(sysSign.getAppId(), sysSign.getSecretKey());
                    String newFieldValue = isDecrypt ?  SignUtil.decryptAesCryptoJs((String) field.get(item),aes):SignUtil.encryptAesCryptoJs((String) field.get(item),aes);
                    field.set(item, newFieldValue);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
                field.setAccessible(Boolean.FALSE);
            }
        }
    }

}
