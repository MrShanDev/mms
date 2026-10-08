package com.sxpcwlkj.system.service.impl;
import com.sxpcwlkj.common.properties.TenantProperties;
import com.sxpcwlkj.common.exception.LoginException;
import com.sxpcwlkj.common.code.entity.CaptchaEntity;
import com.sxpcwlkj.system.mapper.SysUserMapper;
import com.sxpcwlkj.system.service.*;
import com.sxpcwlkj.redis.RedisUtil;
import org.redisson.api.RedissonClient;
import org.redisson.api.RBucket;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class CaptchaConsumptionTest {
    @Test void captchaIsSingleUseAndCannotDeleteOtherRedisNamespaces() {
        SysConfigService config=mock(SysConfigService.class);
        when(config.getIsOpenCaptcha()).thenReturn(true);
        var service=new SysLoginServiceImpl(mock(SysTenantService.class),mock(SysTenantPackageService.class),mock(SysUserService.class),mock(SysUserMapper.class),new TenantProperties(),mock(TenantAdministrationService.class),config);
        RedissonClient client=mock(RedissonClient.class);
        RBucket<Map<String,Object>> bucket=mock(RBucket.class);
        String uuid="0123456789abcdef0123456789abcdef";
        when(client.<Map<String,Object>>getBucket("mms:captcha:"+uuid)).thenReturn(bucket);
        when(bucket.getAndDelete()).thenReturn(Map.of(CaptchaEntity.CODE.CAPTCHA_KEY.getValue(),"login",CaptchaEntity.CODE.CAPTCHA_VALUE.getValue(),"ABCD")).thenReturn(null);
        var factory=new org.springframework.beans.factory.support.DefaultListableBeanFactory();
        factory.registerSingleton("redissonClient",client);
        factory.registerSingleton("converter",mock(io.github.linpeilie.Converter.class));
        new com.sxpcwlkj.common.utils.SpringUtil().postProcessBeanFactory(factory);
        try (var redis=mockStatic(RedisUtil.class)) {
            redis.when(RedisUtil::getClient).thenReturn(client);
            service.verfyCaptcha("user","login","abcd",uuid);
            assertThrows(LoginException.class,()->service.verfyCaptcha("user","login","abcd",uuid));
            assertThrows(LoginException.class,()->service.verfyCaptcha("user","login","abcd","mms:admin:1"));
        }
        verify(client,times(2)).getBucket("mms:captcha:"+uuid);
        verify(bucket,times(2)).getAndDelete();
    }
}
