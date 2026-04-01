package com.sxpcwlkj.plugin.host.internal;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.sxpcwlkj.plugin.HostDataService;
import com.sxpcwlkj.plugin.HostServices;
import com.sxpcwlkj.plugin.PluginDataAccess;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.data.EmptyHostDataService;
import com.sxpcwlkj.plugin.host.PluginHostProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 默认宿主能力：缓存、{@link HostDataService} 桥接、{@link PluginDataAccess}（受控 JDBC）、事务与 Web 权限探测。
 */
@RequiredArgsConstructor
public final class DefaultHostServices implements HostServices {

    /**
     * 与当前代码实现一致；破坏性变更时递增并在发行说明中说明（2：事务与 hasWebPermission）。
     */
    public static final int HOST_IMPLEMENTED_CONTRACT_VERSION = 2;

    private final ObjectProvider<StringRedisTemplate> stringRedisTemplate;
    private final ObjectProvider<HostDataService> hostDataService;
    private final ObjectProvider<DataSource> dataSource;
    private final ObjectProvider<PlatformTransactionManager> transactionManager;
    private final PluginHostProperties pluginHostProperties;

    @Override
    public int hostImplementedContractVersion() {
        return HOST_IMPLEMENTED_CONTRACT_VERSION;
    }

    @Override
    public HostDataService hostData() {
        HostDataService svc = hostDataService.getIfAvailable();
        return svc != null ? svc : EmptyHostDataService.INSTANCE;
    }

    @Override
    public PluginDataAccess pluginDataAccess(PluginDescriptor forPlugin) {
        if (forPlugin == null) {
            throw new PluginException("pluginDescriptor 不能为空");
        }
        String prefix = forPlugin.getPluginTablePrefix();
        if (prefix == null || prefix.isBlank()) {
            throw new PluginException("使用 pluginDataAccess 须在 plugin.json 声明 pluginTablePrefix");
        }
        DataSource ds = dataSource.getIfAvailable();
        if (ds == null) {
            throw new PluginException("宿主未配置 DataSource，无法使用 pluginDataAccess");
        }
        Set<String> allowed = null;
        if (forPlugin.getPluginDataTables() != null && !forPlugin.getPluginDataTables().isEmpty()) {
            allowed = new HashSet<>();
            String pfx = prefix.trim().toLowerCase(Locale.ROOT);
            for (String rel : forPlugin.getPluginDataTables()) {
                allowed.add(pfx + rel.trim().toLowerCase(Locale.ROOT));
            }
        }
        int tmo = pluginHostProperties != null ? pluginHostProperties.getDataAccessQueryTimeoutSeconds() : 0;
        boolean audit =
                pluginHostProperties == null || pluginHostProperties.isPluginDataAccessAuditLogEnabled();
        boolean autoTenant =
                pluginHostProperties != null && pluginHostProperties.isPluginDataAccessAutoTenantEnabled();
        String tenantCol =
                pluginHostProperties != null && pluginHostProperties.getPluginDataAccessTenantColumn() != null
                        ? pluginHostProperties.getPluginDataAccessTenantColumn().trim()
                        : "tenant_id";
        Optional<String> tenant = hostData().tryCurrentTenantId();
        return new JdbcPluginDataAccess(
                new JdbcTemplate(ds),
                forPlugin.getId(),
                forPlugin.getVersion(),
                prefix.trim(),
                allowed,
                tmo,
                audit,
                autoTenant,
                tenantCol,
                tenant);
    }

    @Override
    public Optional<String> cacheGetString(String key) {
        if (key == null || key.isEmpty()) {
            return Optional.empty();
        }
        StringRedisTemplate redis = stringRedisTemplate.getIfAvailable();
        if (redis == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(redis.opsForValue().get(key));
    }

    @Override
    public void cachePutString(String key, String value, int ttlSeconds) {
        if (key == null || key.isEmpty()) {
            return;
        }
        StringRedisTemplate redis = stringRedisTemplate.getIfAvailable();
        if (redis == null) {
            return;
        }
        if (ttlSeconds > 0) {
            redis.opsForValue().set(key, value == null ? "" : value, ttlSeconds, TimeUnit.SECONDS);
        } else {
            redis.opsForValue().set(key, value == null ? "" : value);
        }
    }

    @Override
    public void runInWritableTransaction(Runnable work) {
        if (work == null) {
            return;
        }
        PlatformTransactionManager ptm = transactionManager.getIfAvailable();
        if (ptm == null) {
            throw new PluginException("宿主未配置事务管理器，无法使用 runInWritableTransaction");
        }
        TransactionTemplate tt = new TransactionTemplate(ptm);
        tt.executeWithoutResult(status -> work.run());
    }

    @Override
    public void runInReadOnlyTransaction(Runnable work) {
        if (work == null) {
            return;
        }
        PlatformTransactionManager ptm = transactionManager.getIfAvailable();
        if (ptm == null) {
            work.run();
            return;
        }
        TransactionTemplate tt = new TransactionTemplate(ptm);
        tt.setReadOnly(true);
        tt.executeWithoutResult(status -> work.run());
    }

    @Override
    public boolean hasWebPermission(String permissionCode) {
        if (permissionCode == null || permissionCode.isBlank()) {
            return false;
        }
        try {
            StpUtil.checkPermission(permissionCode.trim());
            return true;
        } catch (NotLoginException | NotPermissionException e) {
            return false;
        }
    }
}
