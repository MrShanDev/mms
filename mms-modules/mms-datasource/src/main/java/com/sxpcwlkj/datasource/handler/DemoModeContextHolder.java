package com.sxpcwlkj.datasource.handler;

/**
 * 创建上下文持有者
 * @author mmsAdmin
 */
public class DemoModeContextHolder {
    private static final ThreadLocal<Boolean> DEMO_MODE_DISABLED = new ThreadLocal<>();

    public static void setDemoModeDisabled(boolean disabled) {
        DEMO_MODE_DISABLED.set(disabled);
    }

    public static Boolean isDemoModeDisabled() {
        return DEMO_MODE_DISABLED.get();
    }

    public static void clear() {
        DEMO_MODE_DISABLED.remove();
    }
}
