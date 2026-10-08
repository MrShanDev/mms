package com.sxpcwlkj.system.service.impl;

import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.annotation.DataScope;
import com.sxpcwlkj.datasource.permission.DataScopeProvider;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import com.sxpcwlkj.system.entity.SysDept;
import com.sxpcwlkj.system.mapper.SysDeptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SystemDataScopeProvider implements DataScopeProvider {
    private final SysDeptMapper departments;
    @Override public String condition(DataScope scope, String qualifier) {
        if (!LoginObject.isLogin()) return "1=0";
        if (LoginObject.getLoginSuper()) return null;
        SysUserVo user = LoginObject.getLoginObject(SysUserVo.class);
        if (user == null) return "1=0";
        Set<String> clauses = new LinkedHashSet<>();
        List<SysDept> tree = null;
        if (user.getRoleVoList() != null) for (var role : user.getRoleVoList()) {
            if ("1".equals(role.getDataScope())) return null;
            String dataScope = role.getDataScope();
            if (Set.of("2","3","4").contains(dataScope == null ? "5" : dataScope) && !scope.deptColumn().isBlank()) {
                Set<String> ids = new LinkedHashSet<>();
                if ("2".equals(dataScope) && role.getDataScopeDeptIds() != null) {
                    for (String id : role.getDataScopeDeptIds().split(",")) if (!id.isBlank()) ids.add(id.trim());
                } else if (user.getDeptId() != null) ids.add(user.getDeptId());
                if ("4".equals(dataScope)) {
                    if (tree == null) tree = departments.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>());
                    boolean changed; do { changed = false;
                        for (SysDept dept : tree) if (ids.contains(dept.getParentId())) changed |= ids.add(dept.getDeptId());
                    } while (changed);
                }
                if (!ids.isEmpty()) clauses.add(column(qualifier, scope.deptColumn()) + " IN (" + ids.stream().map(SystemDataScopeProvider::literal).collect(java.util.stream.Collectors.joining(",")) + ")");
            } else if (!scope.userColumn().isBlank()) {
                clauses.add(column(qualifier,scope.userColumn()) + "=" + literal(user.getUserId()));
            }
        }
        // A role-less account can view only its own row; it still requires endpoint permissions.
        if (clauses.isEmpty()) return scope.userColumn().isBlank() ? "1=0" : column(qualifier,scope.userColumn()) + "=" + literal(user.getUserId());
        return "(" + String.join(" OR ",clauses) + ")";
    }
    private static String column(String qualifier, String name) {
        if (!name.matches("[A-Za-z_][A-Za-z0-9_]*")) throw new IllegalArgumentException("Invalid scope column");
        return qualifier + ".`" + name + "`";
    }
    private static String literal(String value) { return "'" + value.replace("'", "''") + "'"; }
}
