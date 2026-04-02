package com.sxpcwlkj.redis.constant;

/**
 * @Description  Redis常量
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
public class RedisConstant {

    // user换成前缀key
    public static final String ADMIN_KEY="admin:";
    public static final String ADMIN_NAME="admin:name:";
    public static final String MOBILE_KEY="mobile:member:";
    /** mms-unix {@code store_member} 等 PC 端会员会话缓存 */
    public static final String PC_KEY="pc:member:";
    /** MMS-DOC 文档站用户（{@link com.sxpcwlkj.common.enums.DeviceEnum#DOC}） */
    public static final String DOC_KEY="doc:member:";
    public static final String ENCRYPTION_SERVER_PORT="encryption:server:";
    public static final String ENCRYPTION_APP_ID="encryption:";
    public static final String COOKIE_APP_ID="cookie:";
    public static final String ADMIN_TENANT_KEY="sys:tenant:";
    public static final String WX_OPENID_KEY="wx:openid:";
}
