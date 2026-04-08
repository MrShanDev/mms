package com.sxpcwlkj.common.context;

/**
 * 演示模式相关线程上下文。
 * <ul>
 *   <li>{@link #setDemoModeDisabled}：显式关闭 MyBatis 演示写拦截（慎用）。</li>
 *   <li>{@link #setPropagatedDemoPrincipal}：由<strong>有 Sa 上下文的请求线程</strong>捕获登录主体后，在 Servlet 异步 /
 *   线程池线程上还原，使 {@code DemoModeInterceptor} 能按 {@code demo.mode.allowed-users} 与超级管理员规则放行。</li>
 * </ul>
 */
public final class DemoModeContextHolder {

    private static final ThreadLocal<Boolean> DEMO_MODE_DISABLED = new ThreadLocal<>();
    private static final ThreadLocal<String> PROPAGATED_USERNAME = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> PROPAGATED_SUPER_ADMIN = new ThreadLocal<>();

    private DemoModeContextHolder() {}

    public static void setDemoModeDisabled(boolean disabled) {
        DEMO_MODE_DISABLED.set(disabled);
    }

    public static Boolean isDemoModeDisabled() {
        return DEMO_MODE_DISABLED.get();
    }

    /**
     * 在目标线程（如 StreamingResponseBody 异步线程）执行写库前调用；{@code username} 用于匹配 {@code demo.mode.allowed-users}。
     */
    public static void setPropagatedDemoPrincipal(String username, boolean superAdmin) {
        if (username != null && !username.isBlank()) {
            PROPAGATED_USERNAME.set(username.trim());
        }
        if (superAdmin) {
            PROPAGATED_SUPER_ADMIN.set(true);
        }
    }

    public static String getPropagatedUsername() {
        return PROPAGATED_USERNAME.get();
    }

    public static Boolean getPropagatedSuperAdmin() {
        return PROPAGATED_SUPER_ADMIN.get();
    }

    public static void clearPropagatedDemoPrincipal() {
        PROPAGATED_USERNAME.remove();
        PROPAGATED_SUPER_ADMIN.remove();
    }

    public static void clear() {
        DEMO_MODE_DISABLED.remove();
        PROPAGATED_USERNAME.remove();
        PROPAGATED_SUPER_ADMIN.remove();
    }
}
