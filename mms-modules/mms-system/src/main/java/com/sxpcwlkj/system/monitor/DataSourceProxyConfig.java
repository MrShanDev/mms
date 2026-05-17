package com.sxpcwlkj.system.monitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.QueryCountHolder;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.sql.DataSource;
import java.util.List;

/**
 * DataSource 代理：采集慢 SQL（不依赖具体数据库）
 */
@Configuration
@ConditionalOnBean(DataSource.class)
@RequiredArgsConstructor
@Slf4j
public class DataSourceProxyConfig {

    private final SlowSqlRecorder slowSqlRecorder;

    @Value("${mms.monitor.slow-sql-threshold-ms:500}")
    private long slowSqlThresholdMs;

    @Value("${mms.monitor.slow-sql-max-size:200}")
    private int slowSqlMaxSize;

    @org.springframework.context.annotation.Bean
    public BeanPostProcessor dataSourceProxyBeanPostProcessor() {
        slowSqlRecorder.setThresholdMs(slowSqlThresholdMs);
        slowSqlRecorder.setMaxSize(slowSqlMaxSize);

        // 代理 DataSource：监听 SQL 执行耗时
        QueryExecutionListener listener = new QueryExecutionListener() {
            @Override
            public void beforeQuery(net.ttddyy.dsproxy.ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
                // no-op
            }

            @Override
            public void afterQuery(net.ttddyy.dsproxy.ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
                long elapsed = execInfo.getElapsedTime();
                if (elapsed < slowSqlRecorder.getThresholdMs()) return;

                String dsName = execInfo.getDataSourceName();
                String sql = concatSql(queryInfoList);
                slowSqlRecorder.record(dsName == null ? "default" : dsName, elapsed, sql);
            }
        };

        // 统计 QueryCount（可选）：后续可扩展 QPS/平均耗时等
        try {
            QueryCountHolder.clear();
        } catch (Throwable ignore) {
        }

        log.info("DataSourceProxy 已启用：slow-sql-threshold={}ms maxSize={}", slowSqlThresholdMs, slowSqlMaxSize);

        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                if (!(bean instanceof DataSource ds)) return bean;
                String lower = bean.getClass().getName().toLowerCase();
                if (lower.contains("proxydatasource")) return bean; // 避免重复包装

                return ProxyDataSourceBuilder
                        .create(ds)
                        .name(beanName == null ? "datasource" : beanName)
                        .listener(listener)
                        .countQuery()
                        .buildProxy();
            }
        };
    }

    private String concatSql(List<QueryInfo> queryInfoList) {
        if (queryInfoList == null || queryInfoList.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (QueryInfo qi : queryInfoList) {
            String q = qi.getQuery();
            if (q == null || q.trim().isEmpty()) continue;
            if (sb.length() > 0) sb.append(";\n");
            sb.append(q.trim());
        }
        return sb.toString();
    }
}
