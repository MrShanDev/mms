package com.sxpcwlkj.authority;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotWebContextException;
import cn.dev33.satoken.exception.SaTokenContextException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Console;
import com.sxpcwlkj.common.enums.DeviceEnum;
import com.sxpcwlkj.common.enums.ErrorCodeEnum;
import com.sxpcwlkj.common.exception.LoginException;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.redis.RedisUtil;
import com.sxpcwlkj.redis.constant.RedisConstant;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description 登录对象
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Setter
@Getter
@Component
@Slf4j
@RequiredArgsConstructor
public class LoginObject<T> {

    private Class<T> clazz;

    public final static String SUPER_ID = "1";

    /** Redis 会话多为 Map，未必有 MapStruct 声明；convertValue 与 doc 等 VO 字段名对齐即可 */
    private static final ObjectMapper LOGIN_SESSION_MAP_OM = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * 是否登录
     *
     * @return 是否登录
     */
    public static Boolean isLogin() {
        try {
            return StpUtil.isLogin();
        } catch (NotWebContextException | SaTokenContextException e) {
            // 非请求线程（如插件生命周期监听 CompletableFuture.runAsync、定时任务）无 Sa-Token Web 上下文
            return false;
        } catch (NotLoginException e) {
            return false;
        } catch (Exception e) {
            log.error("获取登录状态失败！", e);
            return false;
        }
    }

    /**
     * 获取当前登录用户ID
     * @return 当前登录用户ID
     */
    public static String getLoginId() {
        try {
            SaSession session = StpUtil.getSession();
            return session != null ? session.getLoginId().toString() : null;
        } catch (NotWebContextException | SaTokenContextException e) {
            // 无 Web 上下文或非请求线程（如应用就绪后加载插件、定时任务）无法初始化 SaToken 上下文
            return null;
        } catch (NotLoginException e) {
            // 有 Web 上下文但未登录（如文档站 /doc/v1/oauth-polling 等 @SaIgnore 接口仍走 MyBatis 多租户）
            return null;
        }

    }

    /**
     * 获取租户号
     * @return
     */
    public static String getLoginTenant() {
        String id = getLoginId();
        if (id == null) {
            return "000000";
        }
        String tenantId = RedisUtil.getCacheObject(RedisConstant.ADMIN_TENANT_KEY + id);
        return tenantId != null && !tenantId.isBlank() ? tenantId : "000000";
    }

    /**
     * 当前登录对象是否超级管理员
     * @return 当前登录对象是否超级管理员
     */
    public static Boolean getLoginSuper() {
        if (Objects.equals(getLoginId(), "1")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    /**
     * 获取当前登录用户
     * @param clazz 对象
     * @return 当前登录用户
     */
    public static <T> T getLoginObject(Class<T> clazz) {

        if (isLogin()) {
            try {
                if (getLoginId() != null) {
                    Object object = null;
                    String device = StpUtil.getLoginDeviceType();
                    // 须与写入 Redis 时一致；登录 id 可能为 UUID 字符串，不能用 getLoginIdAsLong()
                    String loginIdKey = String.valueOf(StpUtil.getLoginId());
                    if (DeviceEnum.MOBILE.getType().equals(device)) {
                        object = RedisUtil.getCacheObject(RedisConstant.MOBILE_KEY + loginIdKey);
                    } else if (DeviceEnum.ADMIN.getType().equals(device)) {
                        object = RedisUtil.getCacheObject(RedisConstant.ADMIN_KEY + loginIdKey);
                    } else if (DeviceEnum.DOC.getType().equals(device)) {
                        object = RedisUtil.getLoginSessionMap(RedisConstant.DOC_KEY + loginIdKey);
                    } else if (DeviceEnum.PC.getType().equals(device)) {
                        object = RedisUtil.getLoginSessionMap(RedisConstant.PC_KEY + loginIdKey);
                    } else {
                        log.info("[mms-doc LoginObject] unknown device type for session cache: device={} loginIdKey={}", device, loginIdKey);
                    }

                    if (object == null) {
                        if (DeviceEnum.DOC.getType().equals(device)) {
                            log.info("[mms-doc LoginObject] redis session null key={}{}", RedisConstant.DOC_KEY, loginIdKey);
                        } else if (DeviceEnum.PC.getType().equals(device)) {
                            log.info("[mms-doc LoginObject] redis session null key={}{}", RedisConstant.PC_KEY, loginIdKey);
                        }
                        return null;
                    }
                    if (object instanceof Map<?, ?> raw) {
                        Map<String, Object> sm = new LinkedHashMap<>();
                        for (Map.Entry<?, ?> e : raw.entrySet()) {
                            if (e.getKey() != null) {
                                sm.put(e.getKey().toString(), e.getValue());
                            }
                        }
                        coerceEpochMillisToDate(sm, "ctime", "mtime", "createdTime", "updatedTime");
                        T converted = convertMapSessionToBean(sm, clazz, device);
                        if (converted == null) {
                            log.warn("[mms-doc LoginObject] map to {} null device={} mapKeys={}", clazz.getSimpleName(), device, sm.keySet());
                        }
                        return converted;
                    }
                    T converted;
                    try {
                        converted = MapstructUtil.convert(object, clazz);
                    } catch (Exception e) {
                        if (object instanceof Map<?, ?> m2) {
                            Map<String, Object> sm2 = new LinkedHashMap<>();
                            for (Map.Entry<?, ?> e2 : m2.entrySet()) {
                                if (e2.getKey() != null) {
                                    sm2.put(e2.getKey().toString(), e2.getValue());
                                }
                            }
                            coerceEpochMillisToDate(sm2, "ctime", "mtime", "createdTime", "updatedTime");
                            converted = convertMapSessionToBean(sm2, clazz, device);
                        } else {
                            throw e;
                        }
                    }
                    if (converted == null) {
                        log.warn("[mms-doc LoginObject] object to {} null device={} objType={}", clazz.getSimpleName(), device, object.getClass().getName());
                    }
                    return converted;
                }
            } catch (Exception ex) {
                String dev = null;
                String lid = null;
                try {
                    dev = StpUtil.getLoginDeviceType();
                    lid = StpUtil.getLoginId() != null ? String.valueOf(StpUtil.getLoginId()) : null;
                } catch (Exception ignored) {
                    // ignore
                }
                log.warn("[mms-doc LoginObject] getLoginObject exception clazz={} device={} loginId={} msg={}", clazz.getSimpleName(), dev, lid, ex.getMessage());
            }

        }
        //请先登录
          throw new MmsException(ErrorCodeEnum.USER_NOT_LOGIN.getValue(),ErrorCodeEnum.USER_NOT_LOGIN.getKey());
//        throw new NotLoginException(ErrorCodeEnum.USER_NOT_LOGIN.getValue(), DeviceEnum.MOBILE.getType(), "0");
    }

    /**
     * 登录
     *
     * @param id       标识ID
     * @param device   登录设备
     * @param timeout  过期时间/秒
     * @param jwtKey   JWL key
     * @param jwtValue JWT value
     * @return 登录token
     */
    public static String loginToken(String id, String device, Long timeout, String jwtKey, String jwtValue) {
        Console.log("当前会话TokenName", StpUtil.getTokenName());
        if (StpUtil.isLogin()) {
            String curDevice = StpUtil.getLoginDeviceType();
            String curId = StpUtil.getLoginId() != null ? String.valueOf(StpUtil.getLoginId()) : "";
            boolean sameAccount = Objects.equals(curId, id);
            boolean sameDevice = curDevice != null && device != null && curDevice.equals(device);
            if (sameAccount && sameDevice) {
                Console.log("当前会话已登录，无需重复登录", StpUtil.getTokenName());
                return StpUtil.getTokenValue();
            }
            // 同浏览器先有 PC 商城等会话再扫文档码：若直接复用 token，device 仍为 PC，会读 pc:member 而文档写的是 doc:member
            log.info("[mms-doc LoginObject] loginToken 换票: curId={} reqId={} curDevice={} reqDevice={}", curId, id, curDevice, device);
            StpUtil.logout();
        }
        //根据用户id，进行登录
        SaLoginModel saLoginModel = new SaLoginModel();
        if (StringUtil.isNotEmpty(device)) {
            saLoginModel.setDeviceType(device);
        }
        if (StringUtil.isNotEmpty(timeout) && timeout > 0) {
            saLoginModel.setTimeout(timeout);
        }
        if (StringUtil.isNotEmpty(jwtKey)) {
            saLoginModel.setExtra(jwtKey, jwtValue);
        }
        StpUtil.login(id, saLoginModel);

        // 获取当前会话的token值
        return StpUtil.getTokenValue();
    }

    /**
     * 退出当前登录
     */
    public static void logout() {
        try {
            // 当前会话注销登录
            StpUtil.logout();
        } catch (NotLoginException exception) {
            throw new LoginException("Not Log In : " + exception.getMessage());
        }
    }


    public static String getLoginUserName() {
        try {
            String id=getLoginId();
            if(id==null) {
                return null;
            }
            return RedisUtil.getCacheObject(RedisConstant.ADMIN_NAME + id);
        }catch (NotWebContextException e){
            return null;
        }
    }

    private static <T> T convertMapSessionToBean(Map<String, Object> sm, Class<T> clazz, String device) {
        try {
            T t = MapstructUtil.convert(sm, clazz);
            if (t != null) {
                return t;
            }
        } catch (Exception ignored) {
        }
        try {
            return LOGIN_SESSION_MAP_OM.convertValue(sm, clazz);
        } catch (Exception e2) {
            log.warn("[mms-doc LoginObject] map->{} device={} mapKeys={} err={}", clazz.getSimpleName(), device, sm.keySet(), e2.getMessage());
            return null;
        }
    }

    /** 文档站等场景 Redis 中日期存为 epoch 毫秒，反序列化为 Map 后便于转为 VO */
    private static void coerceEpochMillisToDate(Map<String, Object> sm, String... keys) {
        if (sm == null || keys == null) {
            return;
        }
        for (String k : keys) {
            Object v = sm.get(k);
            if (v instanceof Number n) {
                sm.put(k, new Date(n.longValue()));
            }
        }
    }
}
