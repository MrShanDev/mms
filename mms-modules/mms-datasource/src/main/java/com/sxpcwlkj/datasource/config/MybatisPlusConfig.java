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

import java.util.List;
import java.util.Objects;

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
        if (tenantProperties.getEnable()) {
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
                @Override
                public Expression getTenantId() {
                    //获得当前登录用户的租户id
                    return new StringValue(Objects.requireNonNull(LoginObject.getLoginTenant()));
                }
                // 租户字段名（对应数据库字段）
                @Override
                public String getTenantIdColumn() {
                    return tenantProperties.getColumn();
                }

                // 忽略多租户的表或SQL（如系统表）
                @Override
                public boolean ignoreTable(String tableName) {
                    List<String> exclusionList= tenantProperties.getExclusionTable();
                    boolean state=Boolean.FALSE;
                    if(exclusionList==null|| exclusionList.isEmpty()){
                        return state;
                    }
                    for (int i = 0; i < exclusionList.size(); i++) {
                        String s=exclusionList.get(i);
                        if(s.equalsIgnoreCase(tableName)||s.startsWith("sys_gen_")){
                            state= Boolean.TRUE;
                            break;
                        }
                    }
                    return state;
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

