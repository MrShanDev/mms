package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.system.entity.*;
import com.sxpcwlkj.system.entity.bo.SysRoleBo;
import com.sxpcwlkj.system.entity.vo.SysFunctionVo;
import com.sxpcwlkj.system.entity.vo.SysRoleVo;
import com.sxpcwlkj.system.mapper.SysFunctionMapper;
import com.sxpcwlkj.system.mapper.SysDeptMapper;
import com.sxpcwlkj.system.mapper.SysRoleFunctionMapper;
import com.sxpcwlkj.system.mapper.SysRoleMapper;
import com.sxpcwlkj.system.mapper.SysUserRoleMapper;
import com.sxpcwlkj.system.service.SysFunctionService;
import com.sxpcwlkj.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 系统角色
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@RequiredArgsConstructor
@Service("sysRole")
@Slf4j
@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl implements SysRoleService {

    private final AdminLoginPermissionCacheService adminLoginPermissionCacheService;
    private final SysRoleMapper baseMapper;
    private final SysDeptMapper sysDeptMapper;
    private final SysFunctionMapper sysFunctionMapper;
    private final SysRoleFunctionMapper sysRoleFunctionMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysFunctionService sysFunctionService;

    @Override
    public TableDataInfo<SysRoleVo> selectPageUserList(SysRoleBo bo, PageQuery pageQuery) {
        Page<SysRoleVo> page = baseMapper.selectPageList(pageQuery.build(), this.buildQueryWrapper(bo));

        return TableDataInfo.build(page);
    }

    private Wrapper<SysRole> buildQueryWrapper(SysRoleBo bo) {

        QueryWrapper<SysRole> wrapper = Wrappers.query();
        wrapper.eq(StringUtil.isNotEmpty(bo.getStatus()), "status", bo.getStatus());
        wrapper.like(StringUtil.isNotEmpty(bo.getName()), "name", bo.getName());
        wrapper.like(StringUtil.isNotEmpty(bo.getCode()), "code", bo.getCode());

        // 等级过滤：只能看到职级不高于自己的角色 (level 越大职级越低)
        if (!LoginObject.getLoginSuper()) {
            Integer minLevel = getCurrentUserMinLevel();
            // 1. 只能看到 level >= 自己职级的角色
            wrapper.ge("level", minLevel);
            // 2. 显式排除超级管理员角色编码
            wrapper.ne("code", SystemCommonEnum.SUPER_ADMIN.getCode());
        }

        wrapper.orderByAsc("level");
        return wrapper;
    }

    /**
     * 获取当前登录用户的最高职级 (level越小职级越高)
     */
    private Integer getCurrentUserMinLevel() {
        String loginId = LoginObject.getLoginId();
        if (StringUtil.isEmpty(loginId)) {
            return 999999;
        }
        List<SysRoleVo> roles = baseMapper.selectByUserIdList(loginId);
        if (roles == null || roles.isEmpty()) {
            return 999999;
        }
        return roles.stream()
            .filter(role -> "1".equals(role.getStatus()))
            .map(SysRoleVo::getLevel)
            .filter(java.util.Objects::nonNull)
            .min(Integer::compare)
            .orElse(999999);
    }

    @Override
    public SysRoleVo selectVoById(String id) {
        SysRoleVo vo = baseMapper.selectVoById(id);
        if (vo == null) return null;
        assertManageableRole(vo);
        // 系统菜单
        List<AdminMenuTree> functionTree = sysFunctionService.getAllMenuTree();
        vo.setFunctionTree(functionTree);
        String[] defChecked;
        if(LoginObject.getLoginSuper()&&vo.getCode().equals(SystemCommonEnum.SUPER_ADMIN.getCode())){
            defChecked = functionTree.stream().map(AdminMenuTree::getId).toArray(String[]::new);
        }else {
            // 当前角色拥有的节点
            List<SysFunction> ids = sysFunctionMapper.selectByRoleForIds(id);
            defChecked = ids.stream().map(SysFunction::getId).toArray(String[]::new);
        }
        vo.setDefChecked(defChecked);
        return vo;
    }

    private void assertManageableRole(SysRoleVo role) {
        if (role == null) throw new MmsException("角色不存在");
        if (!LoginObject.getLoginSuper() && ("super_admin".equals(role.getCode()) || role.getLevel() == null
                || role.getLevel() < getCurrentUserMinLevel())) throw new MmsException("无权操作该角色");
    }

    private java.util.Set<String> validateGrant(SysRoleBo bo) {
        if (!LoginObject.getLoginSuper() && ("super_admin".equals(bo.getCode()) || bo.getLevel() == null
                || bo.getLevel() < getCurrentUserMinLevel())) throw new MmsException("不能创建或提升高于自己的角色");
        if (bo.getDataScope() == null) bo.setDataScope("5");
        if (!java.util.Set.of("1","2","3","4","5").contains(bo.getDataScope())) throw new MmsException("数据范围无效");
        if (!LoginObject.getLoginSuper()) {
            // Only the tenant super administrator may expand department/all data access.
            if (!"5".equals(bo.getDataScope())) throw new MmsException("数据范围授权需超级管理员操作");
        }
        if ("2".equals(bo.getDataScope())) {
            if (bo.getDataScopeDeptIds() == null || bo.getDataScopeDeptIds().isBlank()) throw new MmsException("自定义数据范围必须选择部门");
            java.util.Set<String> departments = new java.util.LinkedHashSet<>();
            for (String id : bo.getDataScopeDeptIds().split(",")) {
                id = id.trim();
                if (id.isEmpty() || sysDeptMapper.selectById(id) == null) throw new MmsException("数据范围部门不存在或不属于当前租户");
                departments.add(id);
            }
            bo.setDataScopeDeptIds(String.join(",", departments));
        } else bo.setDataScopeDeptIds(null);
        java.util.Set<String> grants = new java.util.LinkedHashSet<>();
        if (bo.getDefChecked() != null) for (String selected : bo.getDefChecked()) {
            String id = selected; java.util.Set<String> path = new java.util.HashSet<>();
            while (id != null && !id.isBlank() && !"0".equals(id)) {
                if (!path.add(id)) throw new MmsException("菜单父链存在循环");
                SysFunctionVo menu = sysFunctionMapper.selectVoById(id);
                if (menu == null) throw new MmsException("菜单不存在或不属于当前租户");
                if (!LoginObject.getLoginSuper() && StringUtil.isNotEmpty(menu.getPermission())
                        && !cn.dev33.satoken.stp.StpUtil.hasPermission(menu.getPermission()))
                    throw new MmsException("不能授予自己没有的权限");
                grants.add(id); id = menu.getParentId();
            }
        }
        return grants;
    }

    private void replaceGrants(String roleId, java.util.Set<String> grants) {
        sysRoleFunctionMapper.delete(new LambdaQueryWrapper<SysRoleFunction>().eq(SysRoleFunction::getRoleId, roleId));
        for (String id : grants) {
            SysRoleFunction relation = new SysRoleFunction(); relation.setRoleId(roleId); relation.setFunctionId(id); relation.setStatus(1);
            if (sysRoleFunctionMapper.insert(relation) != 1) throw new MmsException("授权失败");
        }
    }

    private java.util.List<String> assignedUsers(String roleId) {
        return sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId))
                .stream().map(SysUserRole::getUserId).distinct().toList();
    }

    @Override
    public Boolean updateByIdBase(SysRoleBo bo) {
        SysRoleVo current = baseMapper.selectVoById(bo.getId()); assertManageableRole(current);
        if ("super_admin".equals(current.getCode()) && (!"super_admin".equals(bo.getCode()) || !java.util.Objects.equals(bo.getStatus(),1)))
            throw new MmsException("超级管理员角色不能改名或禁用");
        java.util.Set<String> grants = validateGrant(bo);
        SysRole duplicate = baseMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, bo.getCode()).last("LIMIT 1"));
        if (duplicate != null && !bo.getId().equals(duplicate.getId())) throw new MmsException("角色标识重复");
        SysRole role = MapstructUtil.convert(bo, SysRole.class); role.setTenantId(null);
        if (baseMapper.updateById(role) != 1) throw new MmsException("角色修改失败或版本已变化");
        replaceGrants(bo.getId(), grants);
        adminLoginPermissionCacheService.refreshUsers(assignedUsers(bo.getId()));
        return true;
    }

    @Override
    public Boolean insert(SysRoleBo bo) {
        if ("super_admin".equals(bo.getCode())) throw new MmsException("不能新增超级管理员角色");
        java.util.Set<String> grants = validateGrant(bo);
        if (baseMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, bo.getCode()).last("LIMIT 1")) != null)
            throw new MmsException("角色标识重复");
        SysRole role = MapstructUtil.convert(bo, SysRole.class); role.setId(null);
        if (baseMapper.insert(role) != 1) throw new MmsException("角色新增失败");
        replaceGrants(role.getId(), grants); return true;
    }

    @Override
    public Boolean deleteById(String ids) {
        String[] array = DataUtil.getCatStr(ids, ",");
        java.util.Set<String> users = new java.util.LinkedHashSet<>();
        for (String id : array) {
            SysRoleVo role = baseMapper.selectVoById(id); assertManageableRole(role);
            if ("super_admin".equals(role.getCode())) throw new MmsException("超级管理员角色不能删除");
            users.addAll(assignedUsers(id));
        }
        for (String id : array) {
            sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId,id));
            sysRoleFunctionMapper.delete(new LambdaQueryWrapper<SysRoleFunction>().eq(SysRoleFunction::getRoleId,id));
            if (baseMapper.deleteById(id) != 1) throw new MmsException("角色删除失败");
        }
        adminLoginPermissionCacheService.refreshUsers(users); return true;
    }
}
