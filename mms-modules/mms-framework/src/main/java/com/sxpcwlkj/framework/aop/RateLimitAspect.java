package com.sxpcwlkj.framework.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.sxpcwlkj.common.annotation.RateLimit;
import com.sxpcwlkj.common.exception.MmsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流切面
 * @author mmsAdmin
 *
 */
@Aspect
@Component
public class RateLimitAspect {

    // 使用 ConcurrentHashMap 缓存每个方法的 RateLimiter
    private final ConcurrentHashMap<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)") // 拦截带有 @RateLimit 注解的方法
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        // 获取方法的唯一标识（类名 + 方法名）
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodKey = signature.getDeclaringTypeName() + "." + signature.getName();

        // 获取或创建 RateLimiter
        RateLimiter rateLimiter = rateLimiterMap.computeIfAbsent(methodKey, k -> RateLimiter.create(rateLimit.permitsPerSecond()));

        // 尝试获取令牌
        if (rateLimiter.tryAcquire()) {
            // 执行业务逻辑
            return joinPoint.proceed();
        } else {
            // 限流处理
            throw new MmsException("请求过快，请稍后再试!");
        }
    }
}
