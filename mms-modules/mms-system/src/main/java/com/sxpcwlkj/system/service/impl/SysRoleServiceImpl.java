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
public class SysRoleServiceImpl implements SysRoleService {

    private final AdminLoginPermissionCacheService adminLoginPermissionCacheService;
    private final SysRoleMapper baseMapper;
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
            .map(SysRoleVo::getLevel)
            .filter(java.util.Objects::nonNull)
            .min(Integer::compare)
            .orElse(999999);
    }

    @Override
    public SysRoleVo selectVoById(String id) {
        SysRoleVo vo = baseMapper.selectVoById(id);
        if (vo == null) return null;
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

    @Override
    public Boolean updateByIdBase(SysRoleBo bo) {
        int row;
        SysRole sysRole = baseMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, bo.getCode()).last("LIMIT 1"));
        if (sysRole != null) {
            if (!bo.getId().equals(sysRole.getId())) {
                throw new MmsException("角色标识重复!");
            }
        }

        // 角色资源

        String[] defChecked = bo.getDefChecked();

        if (defChecked != null) {
            // 先清理删除
            sysRoleFunctionMapper.delete(new LambdaQueryWrapper<SysRoleFunction>().eq(SysRoleFunction::getRoleId, bo.getId()));
            for (String aLong : defChecked) {
                //这里需要处理一下，el-tree 的选择数据，如果子项数不全部选择，是不返回父节点的
                checkFunction(aLong, bo.getId());
                SysRoleFunction roleFunction = new SysRoleFunction();
                roleFunction.setRoleId(bo.getId());
                roleFunction.setFunctionId(aLong);
                roleFunction.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
                sysRoleFunctionMapper.insert(roleFunction);
            }
        }

        SysRole base = MapstructUtil.convert(bo, SysRole.class);
        row = baseMapper.updateById(base);
        if (row > 0) {
            adminLoginPermissionCacheService.refreshAllCachedAdminUsers();
            return Boolean.TRUE;
        }
        throw new MmsException("操作失败");
    }

    private void checkFunction(String funId,String roleId){
        //当前资源
        SysFunctionVo sysRoleFunction= sysFunctionMapper.selectVoById(funId);
        if(sysRoleFunction!=null){
            //查看当前这个资源的父资源，是否被记录进来
            SysRoleFunction funRole= sysRoleFunctionMapper.selectOne(new LambdaQueryWrapper<SysRoleFunction>()
                .eq(SysRoleFunction::getRoleId,roleId)
                .eq(SysRoleFunction::getFunctionId,sysRoleFunction.getParentId())
            );
            if(funRole==null){
                SysRoleFunction role = new SysRoleFunction();
                role.setRoleId(roleId);
                role.setFunctionId(sysRoleFunction.getParentId());
                role.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
                sysRoleFunctionMapper.insert(role);
                checkFunction(sysRoleFunction.getParentId(), roleId);
            }
        }

    }

    @Override
    public Boolean insert(SysRoleBo bo) {
        SysRole sysRole = baseMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, bo.getCode()).last("LIMIT 1"));
        if (sysRole != null) {
            throw new MmsException("角色标识重复!");
        }
        int row;
        SysRole base = MapstructUtil.convert(bo, SysRole.class);
        row = baseMapper.insert(base);
        if (row > 0) {
            adminLoginPermissionCacheService.refreshAllCachedAdminUsers();
            return Boolean.TRUE;
        }
        throw new MmsException("操作失败");
    }

    @Override
    public Boolean deleteById(String ids) {
        String[] array = DataUtil.getCatStr(ids, ",");
        int rows=0;
        for (String id : array) {
            SysRole sysRole = baseMapper.selectById(id);
            if (sysRole == null) {
                throw new MmsException("数据有误!");
            }
            if (sysRole.getCode().equals(SystemCommonEnum.SUPER_ADMIN.getCode())) {
                throw new MmsException("超级管理员不能删除!");
            }
            // 删除 用户-角色
            sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
            // 删除 角色-资源
            sysRoleFunctionMapper.delete(new LambdaQueryWrapper<SysRoleFunction>().eq(SysRoleFunction::getRoleId, id));
            // 删除角色
            rows = baseMapper.deleteById(id);
        }
        if (rows > 0) {
            adminLoginPermissionCacheService.refreshAllCachedAdminUsers();
            return Boolean.TRUE;
        }
        throw new MmsException("操作失败");
    }
}
