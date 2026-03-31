package com.sxpcwlkj.datasource.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.properties.TenantProperties;
import com.sxpcwlkj.datasource.handler.DemoModeInterceptor;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * MybatisPlus配置
 *
 * @name: MybatisPlusConfig
 * @author: mmsAdmin
 * @date: 2022/12/01
 **/


@EnableTransactionManagement
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Configuration
public class MybatisPlusConfig {
    private final TenantProperties tenantProperties;
    private final DemoModeInterceptor demoModeInterceptor;

    //插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加演示模式拦截器
        interceptor.addInnerInterceptor(demoModeInterceptor);

        //多租户插件
        if (Boolean.TRUE.equals(tenantProperties.getEnable())) {
            final Set<String> tenantExclusions = normalizeTenantExclusions(tenantProperties.getExclusionTable());
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
                @Override
                public Expression getTenantId() {
                    String tenantId = LoginObject.getLoginTenant();
                    if (tenantId == null || tenantId.isBlank()) {
                        tenantId = "000000";
                    }
                    return new StringValue(tenantId);
                }
                // 租户字段名（对应数据库字段）
                @Override
                public String getTenantIdColumn() {
                    return tenantProperties.getColumn();
                }

                // 忽略多租户的表或SQL（如系统表）
                @Override
                public boolean ignoreTable(String tableName) {
                    if (tableName == null || tableName.isEmpty()) {
                        return false;
                    }
                    String tn = tableName.toLowerCase(Locale.ROOT);
                    if (tn.startsWith("sys_gen_")) {
                        return true;
                    }
                    return tenantExclusions.contains(tn);
                }
            }));
        }

        // 防止全表更新与删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        // 添加乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // 如果配置多个插件, 【切记分页最后添加】
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 启动期归一化排除表名：去空白、小写、去重，避免运行期对 exclusionTable 线性扫描。
     */
    static Set<String> normalizeTenantExclusions(java.util.List<String> exclusionList) {
        if (exclusionList == null || exclusionList.isEmpty()) {
            return Set.of();
        }
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (String s : exclusionList) {
            if (s != null && !s.isBlank()) {
                set.add(s.trim().toLowerCase(Locale.ROOT));
            }
        }
        return Set.copyOf(set);
    }


}

/**
 * PaginationInnerInterceptor 分页插件，自动识别数据库类型
 * https://baomidou.com/pages/97710a/
 * OptimisticLockerInnerInterceptor 乐观锁插件
 * https://baomidou.com/pages/0d93c0/
 * MetaObjectHandler 元对象字段填充控制器
 * https://baomidou.com/pages/4c6bcf/
 * ISqlInjector sql注入器
 * https://baomidou.com/pages/42ea4a/
 * BlockAttackInnerInterceptor 如果是对全表的删除或更新操作，就会终止该操作
 * https://baomidou.com/pages/f9a237/
 * IllegalSQLInnerInterceptor sql性能规范插件(垃圾SQL拦截)
 * IdentifierGenerator 自定义主键策略
 * https://baomidou.com/pages/568eb2/
 * TenantLineInnerInterceptor 多租户插件
 * https://baomidou.com/pages/aef2f2/
 * DynamicTableNameInnerInterceptor 动态表名插件
 * https://baomidou.com/pages/2a45ff/
 */

