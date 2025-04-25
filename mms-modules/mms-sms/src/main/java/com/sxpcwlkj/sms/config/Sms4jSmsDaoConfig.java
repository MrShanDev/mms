package com.sxpcwlkj.sms.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Configuration
@Slf4j
public class Sms4jSmsDaoConfig implements SmsDao {

    // 过期时间保存
    @Override
    public void set(String key, Object value, long cacheTime) {
        log.info("key:{},value:{},time:{}",key,value,cacheTime);
    }

    // 保存
    @Override
    public void set(String key, Object value) {
        log.info("key:{},value:{}",key,value);
    }

    // 获取
    @Override
    public Object get(String key) {
        log.info("get:{}",key);
        return null;
    }

    // 移除
    @Override
    public Object remove(String key) {
        log.info("remove:{}",key);
        return null;
    }

    // 清理
    @Override
    public void clean() {
        log.info("clean");
    }
}
