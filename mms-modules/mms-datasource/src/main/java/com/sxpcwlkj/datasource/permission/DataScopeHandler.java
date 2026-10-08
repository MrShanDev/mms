package com.sxpcwlkj.datasource.permission;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.sxpcwlkj.common.annotation.DataScope;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataScopeHandler implements MultiDataPermissionHandler {
    private final ObjectProvider<DataScopeProvider> provider;
    @Override public Expression getSqlSegment(Table table, Expression where, String mappedId) {
        try {
            String className = mappedId.substring(0, mappedId.lastIndexOf('.'));
            Class<?> mapper = Class.forName(className);
            DataScope scope = mapper.getAnnotation(DataScope.class);
            String methodName = mappedId.substring(mappedId.lastIndexOf('.') + 1);
            for (var method : mapper.getMethods()) if (method.getName().equals(methodName) && method.isAnnotationPresent(DataScope.class))
                scope = method.getAnnotation(DataScope.class);
            if (scope == null || !scope.table().equalsIgnoreCase(table.getName().replace("`",""))) return null;
            String qualifier = table.getAlias() == null ? table.getName() : table.getAlias().getName();
            if (!qualifier.matches("`?[A-Za-z_][A-Za-z0-9_]*`?")) throw new IllegalArgumentException("Invalid qualifier");
            DataScopeProvider implementation = provider.getIfAvailable();
            // A module opting in must never become unrestricted when no policy provider is installed.
            String condition = implementation == null ? "1=0" : implementation.condition(scope, qualifier);
            return condition == null ? null : CCJSqlParserUtil.parseCondExpression(condition);
        } catch (ClassNotFoundException e) { return null; }
        catch (Exception e) { throw new com.sxpcwlkj.common.exception.MmsException("数据权限校验失败", e); }
    }
}
