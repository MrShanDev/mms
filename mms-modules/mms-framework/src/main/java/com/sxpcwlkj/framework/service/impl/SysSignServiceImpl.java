package com.sxpcwlkj.framework.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.constant.Constants;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.CookieUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.framework.entity.SysSign;
import com.sxpcwlkj.framework.mapper.SysSignMapper;
import com.sxpcwlkj.framework.service.SysSignService;
import com.sxpcwlkj.framework.utils.SignUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

/**
 * 系统加签
 * @author: mmsAdmin
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysSignServiceImpl implements SysSignService {


    private final SysSignMapper sysSignMapper;

    @Override
    public void loginSetCookie(HttpServletRequest request, HttpServletResponse response, int timeout) {
        String appId=request.getHeader(Constants.APPID);
        if(StringUtil.isBlank(appId)){
            throw  new MmsException("客户端请求头缺少["+Constants.APPID+"]参数。");
        }
        // 持久化
        SysSign sysSign = initSign(appId, timeout);
        // 设置Cookie
        CookieUtil.setCookie(response,Constants.APPID, sysSign.getAppId(), timeout/1000);
        CookieUtil.setCookie(response,Constants.SECRET_KEY, sysSign.getSecretKey(), timeout/1000);
    }
    @Override
    public SysSign initSign(String appid, int timeOut) {
        SysSign sysSign = getSign();
        if(sysSign==null){
            sysSign=new SysSign();
            sysSign.setId(IdUtil.getSnowflakeNextId());
            sysSign.setAppId(appid);
            sysSign.setSecretKey(Objects.requireNonNull(SignUtil.getAesKey()).getSecretKey());
            sysSign.setTimeOut(DateUtil.offset(new Date(), DateField.SECOND, timeOut));
            sysSign.setUserId(LoginObject.getLoginId());
            sysSign.setCreatedBy(LoginObject.isLogin()?Long.parseLong(Objects.requireNonNull(LoginObject.getLoginId())):0);
            sysSign.setCreatedTime(new Date());
            boolean flag = sysSignMapper.initSign(sysSign)>0;
            if(flag){
                RedisUtil.setCacheObject(RedisConstant.ENCRYPTION_APP_ID+LoginObject.getLoginId(), sysSign);
                RedisUtil.expire(RedisConstant.ENCRYPTION_APP_ID+LoginObject.getLoginId(), Duration.ofSeconds(10));
            }
        }
        return sysSign;
    }

    @Override
    public Boolean initSign(String appid, String publicKey, String privateKey, int timeOut) {
        SysSign sysSign=new SysSign();
        sysSign.setAppId(appid);
        sysSign.setPublicKey(publicKey);
        sysSign.setPrivateKey(privateKey);
        sysSign.setTimeOut(DateUtil.offset(new Date(), DateField.SECOND, timeOut));
        sysSign.setUserId(LoginObject.getLoginId());
        Boolean flag = sysSignMapper.insert(sysSign)>0;
        if(flag){
            RedisUtil.setCacheObject(RedisConstant.ENCRYPTION_APP_ID+LoginObject.getLoginId(), sysSign);
            RedisUtil.expire(RedisConstant.ENCRYPTION_APP_ID+LoginObject.getLoginId(), Duration.ofSeconds(10));
        }
        return flag;
    }

    @Override
    public SysSign getSign() {
        SysSign sysSign = RedisUtil.getCacheObject(RedisConstant.ENCRYPTION_APP_ID+LoginObject.getLoginId());
        if(sysSign==null){
            sysSign = sysSignMapper.selectOne(new LambdaQueryWrapper<SysSign>().eq(SysSign::getUserId,LoginObject.getLoginId()).last("LIMIT 1"));
            RedisUtil.setCacheObject(RedisConstant.ENCRYPTION_APP_ID+LoginObject.getLoginId(), sysSign);
            RedisUtil.expire(RedisConstant.ENCRYPTION_APP_ID+LoginObject.getLoginId(), Duration.ofSeconds(10));
        }
        if(sysSign!=null){
            if(sysSign.getTimeOut().getTime()>new Date().getTime()){
                return sysSign;
            }
        }
        return sysSign;
    }


}
