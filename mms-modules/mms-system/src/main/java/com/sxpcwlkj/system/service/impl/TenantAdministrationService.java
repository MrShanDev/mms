package com.sxpcwlkj.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.properties.TenantProperties;
import com.sxpcwlkj.common.tenant.TenantRequestPolicy;
import com.sxpcwlkj.framework.utils.PasswordHashUtil;
import com.sxpcwlkj.system.entity.*;
import com.sxpcwlkj.system.entity.bo.*;
import com.sxpcwlkj.system.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TenantAdministrationService implements TenantRequestPolicy {
    private final SysTenantMapper tenants;
    private final SysTenantPackageMapper packages;
    private final SysFunctionMapper functions;
    private final TenantAdministrationMapper administration;
    private final TenantProperties properties;
    private final org.springframework.beans.factory.ObjectProvider<AdminLoginPermissionCacheService> cache;

    public boolean enabled() {
        String value=administration.mode();
        return value==null ? Boolean.TRUE.equals(properties.getEnable()) : Boolean.parseBoolean(value);
    }
    @Transactional(rollbackFor=Exception.class)
    public void mode(boolean enabled) {
        administration.saveMode(Boolean.toString(enabled));
        if(!enabled) for(SysTenant t:tenants.selectList(new LambdaQueryWrapper<SysTenant>().ne(SysTenant::getTenantId,"000000"))) cache.getObject().revokeUsersForTenant(t.getTenantId());
    }
    @Override public void check() {
        if (LoginObject.isLogin()) checkTenant(LoginObject.getLoginTenant());
    }
    public void checkTenant(String id) {
        if ("000000".equals(id)) return;
        if (!enabled()) throw new MmsException("当前为单应用模式，不允许租户访问",403);
        SysTenant t=tenants.selectById(id);
        if(t==null || !Objects.equals(t.getStatus(),1)) throw new MmsException("租户不存在或已停用",403);
        if(t.getExpireTime()!=null && !t.getExpireTime().after(new Date())) throw new MmsException("租户已到期，请联系平台续期",403);
        if(t.getPackageId()!=null && !t.getPackageId().isBlank()) activePackage(t.getPackageId());
    }
    private SysTenantPackage activePackage(String id) {
        SysTenantPackage p=packages.selectById(id);
        if(p==null || !Objects.equals(p.getStatus(),1)) throw new MmsException("租户套餐不存在或已停用",403);
        return p;
    }
    public SysTenant query(String id) {
        SysTenant t=tenants.selectById(id);
        if(t==null) throw new MmsException("租户不存在");
        return t;
    }
    public List<SysTenantPackage> packageList() { return packages.selectList(new LambdaQueryWrapper<SysTenantPackage>().orderByAsc(SysTenantPackage::getName)); }
    public List<SysFunction> packageMenus() { return functions.selectForSessionTenant("000000").stream().filter(TenantAdministrationService::tenantResource).toList(); }
    static boolean tenantResource(SysFunction f) {
        String p=Objects.toString(f.getPermission(),"");
        String c=Objects.toString(f.getComponent(),"");
        String path=Objects.toString(f.getPath(),"");
        return !p.startsWith("system:tenant") && !p.startsWith("system:config") && !p.startsWith("system:dict")
            && !p.startsWith("gen:") && !p.startsWith("system:generate") && !p.startsWith("plugin:")
            && !c.contains("config") && !c.contains("generate") && !c.contains("tenant") && !c.contains("plugin")
            && !path.contains("config") && !path.contains("generate") && !path.contains("tenant") && !path.contains("plugin")
            && (f.getRemark()==null || !f.getRemark().startsWith("plugin:"));
    }
    public List<SysFunction> allowedFunctions(String tenant) {
        SysTenant t=query(tenant);
        List<SysFunction> eligible=packageMenus();
        if(t.getPackageId()!=null && !t.getPackageId().isBlank()) {
            Set<String> ids=new HashSet<>(Arrays.asList(activePackage(t.getPackageId()).getFunctionIds().split(",")));
            eligible=eligible.stream().filter(f->ids.contains(f.getId())).toList();
        }
        return eligible;
    }
    public Set<String> allowedPermissions(String tenant) {
        if("000000".equals(tenant)) return null;
        List<SysFunction> eligible=allowedFunctions(tenant);
        Set<String> permissions=new HashSet<>();
        eligible.forEach(f->{if(f.getPermission()!=null && !f.getPermission().isBlank()) permissions.add(f.getPermission());});
        return permissions;
    }
    public void checkPermission(String permission) {
        String tenant=LoginObject.getLoginTenant();
        if(!"000000".equals(tenant) && !allowedPermissions(tenant).contains(permission)) throw new MmsException("当前套餐未授权此功能",403);
    }
    @Transactional(rollbackFor=Exception.class)
    public void savePackage(TenantPackageSaveBo bo) {
        Set<String> allowed=new HashSet<>(); packageMenus().forEach(f->allowed.add(f.getId()));
        if(!allowed.containsAll(bo.getFunctionIds())) throw new MmsException("套餐包含平台专属或无效菜单");
        Set<String> ids=new LinkedHashSet<>(bo.getFunctionIds());
        // Include ancestors so a selected page always has a reachable route.
        Map<String,SysFunction> menu=new HashMap<>();packageMenus().forEach(f->menu.put(f.getId(),f));
        for(String id:new ArrayList<>(ids)) {
            Set<String> visited=new HashSet<>(); SysFunction f=menu.get(id);
            while(f!=null && f.getParentId()!=null && f.getParentId()!=0 && visited.add(f.getId())) {
                String parent=f.getParentId().toString(); if(!menu.containsKey(parent)) throw new MmsException("菜单父级不可用于租户"); ids.add(parent);f=menu.get(parent);
            }
        }
        SysTenantPackage p;
        if(bo.getId()==null || bo.getId().isBlank()) {p=new SysTenantPackage();p.setId(IdUtil.getSnowflakeNextIdStr());}
        else {p=packages.selectById(bo.getId());if(p==null) throw new MmsException("套餐不存在");}
        p.setName(bo.getName());p.setStatus(bo.getStatus());p.setFunctionIds(String.join(",",ids));
        if(bo.getId()==null || bo.getId().isBlank()) packages.insert(p);else packages.updateById(p);
        for(SysTenant t:tenants.selectList(new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getPackageId,p.getId()))) {
            if(Objects.equals(p.getStatus(),1)) provisionMenus(t);
            cache.getObject().revokeUsersForTenant(t.getTenantId());
        }
    }
    @Transactional(rollbackFor=Exception.class)
    public void saveTenant(TenantSaveBo bo,boolean create) {
        if("000000".equals(bo.getTenantId())) throw new MmsException("平台默认租户不允许修改");
        SysTenant old=tenants.selectById(bo.getTenantId());
        if(create && old!=null) throw new MmsException("租户编号已存在");
        if(!create && old==null) throw new MmsException("租户不存在");
        if(bo.getPackageId()==null || bo.getPackageId().isBlank()) throw new MmsException("请选择有效套餐");
        activePackage(bo.getPackageId());
        if(bo.getAccountCount()<administration.userCount(bo.getTenantId())) throw new MmsException("账号额度不能小于现有账号数量");
        if(create && (bo.getAdminUsername()==null || bo.getAdminUsername().isBlank() || bo.getAdminPassword()==null)) throw new MmsException("请填写初始管理员账号和密码");
        SysTenant t=create?new SysTenant():old;
        t.setTenantId(bo.getTenantId());t.setName(bo.getName());t.setStatus(bo.getStatus());t.setContactName(bo.getContactName());t.setContactMobile(bo.getContactMobile());t.setDomain(bo.getDomain());t.setPackageId(bo.getPackageId());t.setExpireTime(bo.getExpireTime());t.setAccountCount(bo.getAccountCount());t.setRemark(bo.getRemark());t.setUpdatedTime(new Date());
        if(create) {
            t.setId(t.getTenantId());
            t.setCreatedTime(new Date());tenants.insert(t);
            String dept=IdUtil.getSnowflakeNextIdStr(),role=IdUtil.getSnowflakeNextIdStr(),user=IdUtil.getSnowflakeNextIdStr();
            administration.createDepartment(dept,t.getTenantId());administration.createRole(role,t.getTenantId());
            administration.createUser(user,t.getTenantId(),bo.getAdminUsername(),PasswordHashUtil.hash(bo.getAdminPassword()),dept);
            administration.assignRole(IdUtil.getSnowflakeNextIdStr(),user,role,t.getTenantId());t.setContactUserId(user);tenants.updateById(t);
        } else tenants.updateById(t);
        provisionMenus(t);
        if(!create) cache.getObject().revokeUsersForTenant(t.getTenantId());
    }
    private void provisionMenus(SysTenant t) {
        List<String> ids=Arrays.asList(activePackage(t.getPackageId()).getFunctionIds().split(","));
        List<SysFunction> sources=packageMenus().stream().filter(f->ids.contains(f.getId())).toList();
        Map<String,String> mapped=new HashMap<>();
        List<SysFunction> existing=functions.selectForSessionTenant(t.getTenantId());
        for(SysFunction source:sources) {
            SysFunction match=existing.stream().filter(f->(Objects.equals(source.getType(),1) || Objects.equals(f.getPermission(),source.getPermission())) && Objects.equals(f.getType(),source.getType()) && Objects.equals(f.getPath(),source.getPath()) && Objects.equals(f.getName(),source.getName())).findFirst().orElse(null);
            mapped.put(source.getId(),match==null?IdUtil.getSnowflakeNextIdStr():match.getId());
        }
        Set<String> current=new HashSet<>();existing.forEach(f->current.add(f.getId()));
        for(SysFunction source:sources) {
            String id=mapped.get(source.getId());
            if(current.contains(id))continue;
            Long parent=source.getParentId()==null || source.getParentId()==0?0L:Long.valueOf(mapped.get(source.getParentId().toString()));
            administration.cloneMenu(source.getId(),id,parent,t.getTenantId());
            administration.assignMenu(IdUtil.getSnowflakeNextIdStr(),id,t.getTenantId());
        }
    }
    /** Lock the tenant row until the surrounding user creation transaction commits. */
    public void checkAccountQuota() {
        String id=LoginObject.getLoginTenant();if("000000".equals(id)) return;
        SysTenant t=tenants.lockById(id);checkTenant(id);
        if(t.getAccountCount()!=null && administration.userCount(id)>=t.getAccountCount()) throw new MmsException("租户账号额度已用完");
    }
}
