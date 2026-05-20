package com.sxpcwlkj.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.*;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.DataBaseHelper;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.framework.entity.RsaKeyEntity;
import com.sxpcwlkj.framework.utils.AddressUtil;
import com.sxpcwlkj.framework.utils.SignUtil;
import com.sxpcwlkj.system.entity.*;
import com.sxpcwlkj.system.entity.bo.ResetPwdBo;
import com.sxpcwlkj.system.entity.bo.ResetPwdSuperBo;
import com.sxpcwlkj.system.entity.bo.SetUserRoleSuperBo;
import com.sxpcwlkj.system.entity.bo.SysUserBo;
import com.sxpcwlkj.system.entity.export.SysUserExportVo;
import com.sxpcwlkj.system.entity.vo.SysDeptVo;
import com.sxpcwlkj.system.entity.vo.SysFunctionVo;
import com.sxpcwlkj.system.entity.vo.SysRoleVo;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import com.sxpcwlkj.system.mapper.*;
import com.sxpcwlkj.system.service.SysDeptService;
import com.sxpcwlkj.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mmsAdmin
 */
@RequiredArgsConstructor
@Service("sysUser")
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper baseMapper;
    private final SysDeptMapper sysDeptMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleFunctionMapper sysUserFunctionMapper;
    private final SysFunctionMapper sysFunctionMapper;
    private final SysDeptService sysDeptService;

    @Override
    public TableDataInfo<SysUserVo> selectPageUserList(SysUserBo bo, PageQuery pageQuery) {
        Page<SysUserVo> page = baseMapper.selectPageUserList(pageQuery.build(), this.buildQueryWrapper(bo));
        for (SysUserVo s : page.getRecords()) {
            List<SysDeptVo> list = new ArrayList<>();
            sysDeptService.queryListSon(s.getDeptId(), list);
            Collections.reverse(list);
            s.setDeptIds(list.stream().map(SysDeptVo::getDeptId).toArray(String[]::new));
            // 角色
            List<SysRoleVo> sysRolesVo = sysRoleMapper.selectByUserIdList(s.getUserId());
            if (!sysRolesVo.isEmpty()) {
                s.setRoleCodes(sysRolesVo.stream().map(SysRoleVo::getCode).toArray(String[]::new));
                s.setRoleName(sysRolesVo.stream().map(SysRoleVo::getName).collect(Collectors.joining(", ")));
            }else {
                s.setRoleName("无");
                s.setRoleCodes(new String[0]);
            }
        }
        return TableDataInfo.build(page);
    }

    @Override
    public Boolean insert(SysUserBo bo) {
        // 初始化头像
        bo.setAvatar(SystemCommonEnum.SYS_USER_AVATAR.getCode());
        // 加盐
        bo.setAesKey(Objects.requireNonNull(SignUtil.getAesKey()).getSecretKey());
        //加密密码
        String encryptRsa = SignUtil.pressWord(bo.getPassword(), bo.getAesKey());
        bo.setPassword(encryptRsa);
        // 检查账号是否去重
        SysUser sysUserName = baseMapper.selectByUserName(bo.getUserName());
        if (sysUserName != null) {
            throw new MmsException("账号已存在!");
        }
        // 检查账号是否去重
        SysUser sysUserPhone = baseMapper.selectByUserPhone(bo.getPhoneNumber());
        if (sysUserPhone != null) {
            throw new MmsException("手机号已存在!");
        }
        // 检查账号是否去重
        SysUser sysUserEmail = baseMapper.selectByUserName(bo.getEmail());
        if (sysUserEmail != null) {
            throw new MmsException("邮箱已存在!");
        }

        SysUser sysUser = MapstructUtil.convert(bo, SysUser.class);
        assert sysUser != null;
        sysUser.setPasswordStrength(PasswordStrengthCheckerUtil.checkStrength(bo.getPassword()));
        ValidatorUtil.validateEntity(sysUser, ValidatedGroupConfig.insert.class);
        // 新增用户信息
        int rows = baseMapper.insert(sysUser);
        if (rows > 0) {
            return Boolean.TRUE;
        }
        throw new MmsException("操作失败");
    }

    @Override
    public SysUserVo selectVoById(String userId) {
        // 等级过滤：等级低的用户，不能查看比他等级高的用户
        if (!LoginObject.getLoginSuper()) {
            List<SysRoleVo> targetRoles = sysRoleMapper.selectByUserIdList(userId);
            // 1. 禁止非超级管理员查看拥有 super_admin 角色的账号详情
            if (targetRoles.stream()
                .filter(Objects::nonNull)
                .map(SysRoleVo::getCode)
                .anyMatch(SystemCommonEnum.SUPER_ADMIN.getCode()::equals)) {
                return null;
            }

            Integer minLevel = getCurrentUserMinLevel();
            // 2. 如果目标用户拥有任何职级高于当前用户(level更小)的角色，则禁止查看
            boolean hasHigherLevel = targetRoles.stream()
                .anyMatch(r -> r.getLevel() != null && r.getLevel() < minLevel);
            if (hasHigherLevel) {
                return null;
            }
        }

        SysUserVo userVo = baseMapper.selectVoById(userId);
        if (userVo != null) {
            // 部门
            List<SysDeptVo> list = new ArrayList<>();
            sysDeptService.queryListSon(userVo.getDeptId(), list);
            Collections.reverse(list);
            userVo.setDeptIds(list.stream().map(SysDeptVo::getDeptId).toArray(String[]::new));
            // 角色
            List<SysRoleVo> sysRolesVo = sysRoleMapper.selectByUserIdList(userId);
            if (!sysRolesVo.isEmpty()) {
                userVo.setRoleCodes(sysRolesVo.stream().map(SysRoleVo::getCode).toArray(String[]::new));
                userVo.setRoleName(sysRolesVo.stream().map(SysRoleVo::getName).collect(Collectors.joining(", ")));
            }else {
                userVo.setRoleName("无");
                userVo.setRoleCodes(new String[0]);
            }
            userVo.setPassword("");
        }
        return userVo;
    }

    @Override
    public Boolean updateByIdBase(SysUserBo bo) {
        // 检查账号是否去重
        SysUser sysUserName = baseMapper.selectByUserName(bo.getUserName());
        if (sysUserName != null) {
            if (!bo.getUserId().equals(sysUserName.getUserId())) {
                throw new MmsException("账号已存在!");
            }
        }
        // 检查账号是否去重
        SysUser sysUserPhone = baseMapper.selectByUserPhone(bo.getPhoneNumber());
        if (sysUserPhone != null) {
            if (!bo.getUserId().equals(sysUserPhone.getUserId())) {
                throw new MmsException("手机号已存在!");
            }
        }
        // 检查账号是否去重
        SysUser sysUserEmail = baseMapper.selectByUserName(bo.getEmail());
        if (sysUserEmail != null) {
            if (!bo.getUserId().equals(sysUserEmail.getUserId())) {
                throw new MmsException("邮箱已存在!");
            }
        }
        SysUser user = MapstructUtil.convert(bo, SysUser.class);
        ValidatorUtil.validateEntity(bo, ValidatedGroupConfig.update.class);
        int rows = baseMapper.updateById(user);
        if (rows > 0) {
            return Boolean.TRUE;
        }
        throw new MmsException("操作失败");
    }

    @Override
    public Boolean deleteById(String ids) {
        String[] array = DataUtil.getCatStr(ids, ",");
        int rows = 0;
        for (String id:array){
            if (userHasSuperAdminRole(id)) {
                throw new MmsException("超级管理员不能删除!");
            }
            SysUser sysUser = baseMapper.selectById(id);
            if (sysUser != null) {
                //删除角色
                sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
            }
            rows = baseMapper.deleteById(id);
        }
        return rows>0;
    }

    @Override
    public Boolean imports(Set<SysUserExportVo> list) {
        return true;
    }

    private Wrapper<SysUser> buildQueryWrapper(SysUserBo bo) {
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        // 用户ID
        wrapper.eq(StringUtil.isNotEmpty(bo.getUserId()), "u.user_id", bo.getUserId())
            // 账号查询
            .like(StringUtil.isNotEmpty(bo.getUserName()), "u.user_name", bo.getUserName())
            // 账号状态
//            .eq(StringUtil.isNotEmpty(bo.getStatus()), "u.status", bo.getStatus())
            // 手机号
            .like(StringUtil.isNotEmpty(bo.getPhoneNumber()), "u.phonenumber", bo.getPhoneNumber())
            .and(ObjectUtil.isNotNull(bo.getDeptId()), w -> {
                List<SysDept> deptList = sysDeptMapper.selectList(new LambdaQueryWrapper<SysDept>()
                    .select(SysDept::getDeptId)
                    .apply(DataBaseHelper.findInSet(bo.getDeptId(), "ancestors")));
                List<String> ids = StreamUtil.toList(deptList, SysDept::getDeptId);
                ids.add(bo.getDeptId());
                w.in("u.dept_id", ids);
            });

        // 等级过滤：等级低的用户，看不到比他等级高的用户 (等级值越小，职级越高)
        if (!LoginObject.getLoginSuper()) {
            Integer minLevel = getCurrentUserMinLevel();
            // 1. 排除拥有 super_admin 角色的用户
            wrapper.apply(
                "NOT EXISTS (SELECT 1 FROM sys_user_role sur_sa JOIN sys_role sr_sa ON sur_sa.role_id = sr_sa.id WHERE sur_sa.user_id = u.user_id AND sr_sa.code = {0})",
                SystemCommonEnum.SUPER_ADMIN.getCode());
            // 2. 排除掉 拥有 “比当前用户最高职级(minLevel) 还要高(level < minLevel)” 的角色的用户
            wrapper.apply("NOT EXISTS (SELECT 1 FROM sys_user_role sur JOIN sys_role sr ON sur.role_id = sr.id WHERE sur.user_id = u.user_id AND sr.level < {0})", minLevel);
        }

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
        List<SysRoleVo> roles = sysRoleMapper.selectByUserIdList(loginId);
        if (roles == null || roles.isEmpty()) {
            return 999999; // 无角色的用户等级设为极低
        }
        return roles.stream()
            .map(SysRoleVo::getLevel)
            .filter(Objects::nonNull)
            .min(Integer::compare)
            .orElse(999999);
    }

    @Override
    public SysUser selectByUserName(String tenantId, String userName) {
        return baseMapper.selectOne(new QueryWrapper<SysUser>().eq("user_name", userName).last("LIMIT 1"));
    }

    @Override
    public Boolean updateRsa(String tenantId, String userId) {
        RsaKeyEntity key = SignUtil.getRsaKey();
        log.info("密码对：'{}','{}','{}','{}'", tenantId, userId, key.getPrivateKey(), key.getPrivateKey());
        return baseMapper.updateRsa(tenantId, userId, key.getPrivateKey(), key.getPrivateKey()) > 0;
    }

    @Override
    public SysUserVo getUserRoleAnfFunctionInfo(String userId) {
        SysUserVo userVo = null;
        SysUser user = baseMapper.selectById(userId);

        if (user != null) {
            userVo = BeanCopyUtil.convert(user, SysUserVo.class);
            //1.角色
            assert userVo != null;
            List<SysRole> sysRoles = sysUserRoleMapper.roleList(userVo.getUserId());
            List<SysRoleVo> sysRoleVos = BeanCopyUtil.convert(sysRoles, SysRoleVo.class);

            //2.资源
            assert sysRoleVos != null;
            //角色资源
            List<String> list = new ArrayList<>();
            for (SysRoleVo r : sysRoleVos) {
                userVo.setRoleName(sysRoleVos.get(0).getName());
                List<SysFunction> fList = sysUserFunctionMapper.selectByRoleId(r.getId());
                List<SysFunctionVo> roleFunctionVoList = BeanCopyUtil.convert(fList, SysFunctionVo.class);
                r.setSysFunctionVoList(roleFunctionVoList);
                assert roleFunctionVoList != null;


                for (SysFunctionVo functionVo : roleFunctionVoList) {
                    if (StringUtil.isNotEmpty(functionVo.getPermission())) {
                        list.add(functionVo.getPermission());
                    }
                }
                r.setPermissions(list.toArray(String[]::new));
            }
            //用户角色列表
            userVo.setRoleVoList(sysRoleVos);
            userVo.setRoleCodes(sysRoleVos.stream().map(SysRoleVo::getCode).toArray(String[]::new));
            //资源集
            userVo.setButCodes(list.toArray(String[]::new));
            //===================具备 super_admin 角色的账号拉全量启用菜单（不按 user_id / 登录名）==========================
            boolean loginIsSuperAdmin =
                sysRoleVos.stream()
                    .filter(Objects::nonNull)
                    .map(SysRoleVo::getCode)
                    .filter(Objects::nonNull)
                    .anyMatch(SystemCommonEnum.SUPER_ADMIN.getCode()::equals);
            if (loginIsSuperAdmin) {
                sysRoles = sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
                    .eq(SysRole::getCode, SystemCommonEnum.SUPER_ADMIN.getCode())
                    .orderByAsc(SysRole::getSort).last("LIMIT 1"));
                sysRoleVos = BeanCopyUtil.convert(sysRoles, SysRoleVo.class);
                if (sysRoleVos == null || sysRoleVos.isEmpty()) {
                    log.warn("未找到启用的 super_admin 角色行，使用占位角色装配全量菜单 userId={}", userVo.getUserId());
                    SysRoleVo holder = new SysRoleVo();
                    holder.setCode(SystemCommonEnum.SUPER_ADMIN.getCode());
                    holder.setName("超级管理员");
                    sysRoleVos = new ArrayList<>();
                    sysRoleVos.add(holder);
                }

                List<SysFunction> functionList = sysFunctionMapper.selectList(new LambdaQueryWrapper<SysFunction>().eq(SysFunction::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()).orderByAsc(SysFunction::getSort));
                List<SysFunctionVo> roleFunctionVoList = BeanCopyUtil.convert(functionList, SysFunctionVo.class);
                sysRoleVos.get(0).setSysFunctionVoList(roleFunctionVoList);
                assert roleFunctionVoList != null;
                list = new ArrayList<>();
                for (int i = 0; i < roleFunctionVoList.size(); i++) {
                    SysFunctionVo functionVo = roleFunctionVoList.get(i);
                    if (StringUtil.isNotEmpty(functionVo.getPermission())) {
                        list.add(functionVo.getPermission());
                    }
                }
                sysRoleVos.get(0).setPermissions(list.toArray(String[]::new));
                //用户角色列表
                userVo.setRoleVoList(sysRoleVos);

                //角色集
                userVo.setRoleCodes(sysRoleVos.stream().map(SysRoleVo::getCode).toArray(String[]::new));
                //资源集
                userVo.setButCodes(list.toArray(String[]::new));
            }


        }

        return userVo;
    }

    @Override
    public Boolean updateHeaderImgById(String headerImg) {
        return baseMapper.update(new LambdaUpdateWrapper<SysUser>().set(SysUser::getAvatar, headerImg).eq(SysUser::getUserId, LoginObject.getLoginId())) > 0;
    }

    @Override
    public SysUserVo selectVoByPhone(String phone) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhoneNumber, phone).last("LIMIT 1"));
    }

    @Override
    public SysUserVo selectVoByEmail(String email) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, email).last("LIMIT 1"));
    }

    @Override
    public List<SysUserVo> selectAll() {
        Page<SysUserVo> page = baseMapper.selectPageUserList(null, this.buildQueryWrapper(new SysUserBo()));
        return page != null ? page.getRecords() : new ArrayList<>();
    }

    @Override
    public Boolean resetPwd(ResetPwdBo bo) {

        if (bo.getPassword().length() < 6) {
            throw new MmsException("密码至少6位！");
        }
        SysUserVo userVo = baseMapper.selectVoById(LoginObject.getLoginId());
        if (StringUtil.isEmpty(userVo.getAesKey())) {
            throw new MmsException("缺少加密KEY!");
        }
        //加密密码

        String encryptRsa = SignUtil.pressWord(bo.getOldPassword(), userVo.getAesKey());
        log.info("加密密码:{}", encryptRsa);
        log.info("数据库密码:{}", userVo.getPassword());
        if (!userVo.getPassword().equals(encryptRsa)) {
            throw new MmsException("原密码错误！");
        }
        userVo.setPassword(SignUtil.pressWord(bo.getPassword(), userVo.getAesKey()));
        userVo.setPasswordStrength(PasswordStrengthCheckerUtil.checkStrength(bo.getPassword()));
        int row = baseMapper.update(null, new LambdaUpdateWrapper<SysUser>()
            .set(SysUser::getPassword, userVo.getPassword())
            .set(SysUser::getPasswordStrength, userVo.getPasswordStrength())
            .eq(SysUser::getUserId, LoginObject.getLoginId()));
        return row > 0;
    }

    @Override
    public Boolean resetPwdWithoutOld(String newPassword) {

        if (newPassword.length() < 6) {
            throw new MmsException("密码至少6位！");
        }
        SysUserVo userVo = baseMapper.selectVoById(LoginObject.getLoginId());
        if (StringUtil.isEmpty(userVo.getAesKey())) {
            throw new MmsException("缺少加密KEY!");
        }
        userVo.setPassword(SignUtil.pressWord(newPassword, userVo.getAesKey()));
        userVo.setPasswordStrength(PasswordStrengthCheckerUtil.checkStrength(newPassword));
        int row = baseMapper.update(null, new LambdaUpdateWrapper<SysUser>()
            .set(SysUser::getPassword, userVo.getPassword())
            .set(SysUser::getPasswordStrength, userVo.getPasswordStrength())
            .eq(SysUser::getUserId, LoginObject.getLoginId()));
        return row > 0;
    }

    @Override
    public Boolean resetPwdSuper(ResetPwdSuperBo bo) {
        SysUserVo userVo = baseMapper.selectVoById(LoginObject.getLoginId());
        //更新key
        userVo.setAesKey(Objects.requireNonNull(SignUtil.getAesKey()).getSecretKey());
        //加密密码
        userVo.setPassword(SignUtil.pressWord(bo.getPassword(), userVo.getAesKey()));
        userVo.setPasswordStrength(PasswordStrengthCheckerUtil.checkStrength(bo.getPassword()));
        int rows = baseMapper.update(null, new LambdaUpdateWrapper<SysUser>()
            .eq(SysUser::getUserId, bo.getUserId())
            .set(SysUser::getPassword, userVo.getPassword())
            .set(SysUser::getPasswordStrength, userVo.getPasswordStrength())
            .set(SysUser::getAesKey, userVo.getAesKey())
        );
        if (rows > 0) {
            return Boolean.TRUE;
        }
        throw new MmsException("操作失败");
    }

    @Override
    public Boolean setUserRoleSuper(SetUserRoleSuperBo bo) {

        List<String> result = bo.getRoleCodes().stream().filter(roleCode -> SystemCommonEnum.SUPER_ADMIN.getCode().equals(roleCode)).toList();
        if (!result.isEmpty()) {
            throw new MmsException("超级管理员不可被修改！");
        }
        //删除用户与角色管理
        int rows = sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, bo.getUserId()));
        for (String roleCode : bo.getRoleCodes()) {
            // 新增用户与角色管理
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(bo.getUserId());
            SysRoleVo sysRoleVo = sysRoleMapper.selectByCode(roleCode);
            sysUserRole.setRoleId(sysRoleVo.getId());
            rows = sysUserRoleMapper.insert(sysUserRole);
            if (rows == 0) {
                throw new MmsException("操作失败");
            }
        }
        if (rows > 0) {
            return Boolean.TRUE;
        }
        throw new MmsException("操作失败");
    }

    @Override
    public Boolean unbind(int type) {
        if(type==1){
            return baseMapper.update(null, new LambdaUpdateWrapper<SysUser>().set(SysUser::getPhoneNumber, "").eq(SysUser::getUserId, LoginObject.getLoginId())) > 0;
        }
        if(type==2){
            return baseMapper.update(null, new LambdaUpdateWrapper<SysUser>().set(SysUser::getEmail, "").eq(SysUser::getUserId, LoginObject.getLoginId())) > 0;
        }
        if(type==3){
            return baseMapper.update(null, new LambdaUpdateWrapper<SysUser>().set(SysUser::getWxOpenid, "").eq(SysUser::getUserId, LoginObject.getLoginId())) > 0;
        }
       return false;
    }

    @Override
    public Boolean bindingPhone(String phone) {
        SysUserVo userVo = this.selectVoByPhone(phone);
        if (userVo != null) {
            throw new MmsException("该手机号已被绑定！");
        }
        return baseMapper.update(null, new LambdaUpdateWrapper<SysUser>().set(SysUser::getPhoneNumber, phone).eq(SysUser::getUserId, LoginObject.getLoginId())) > 0;
    }

    @Override
    public Boolean bindingEmail(String email) {
        SysUserVo userVo = this.selectVoByEmail(email);
        if (userVo != null) {
            throw new MmsException("该邮箱已被绑定！");
        }
        return baseMapper.update(null, new LambdaUpdateWrapper<SysUser>().set(SysUser::getEmail, email).eq(SysUser::getUserId, LoginObject.getLoginId())) > 0;
    }

    @Override
    public Map<String, Object> getUserInfo(SysUserVo sysUser) {
        Map<String,Object> userInfo=new HashMap<>();
        // 缓存登录对象 authBtnList
        userInfo.put("roles", sysUser.getRoleCodes());
        userInfo.put("authBtnList", sysUser.getButCodes());
        userInfo.put("userName", Convert.toStr(sysUser.getUserName(), ""));
        userInfo.put("photo",Convert.toStr(sysUser.getAvatar(), ""));
        userInfo.put("nickName",Convert.toStr(sysUser.getNickName(), ""));
        userInfo.put("email",DesensitizedUtil.email(Convert.toStr(sysUser.getEmail(), "")));
        userInfo.put("phoneNumber", DesensitizedUtil.mobilePhone(Convert.toStr(sysUser.getPhoneNumber(), "")));
        userInfo.put("sex",Convert.toStr(sysUser.getSex(), ""));
        String loginIpStr = Convert.toStr(sysUser.getLoginIp(), "");
        userInfo.put("loginIp", loginIpStr);
        userInfo.put("loginRegion", formatLoginIpRegion(loginIpStr));
        userInfo.put("loginDate",Convert.toStr(sysUser.getLoginDate(), ""));
        userInfo.put("roleName",Convert.toStr(sysUser.getRoleName(), ""));
        userInfo.put("passwordStrength",Convert.toStr(sysUser.getPasswordStrength(), ""));
        userInfo.put("wxOpenid", DesensitizedUtil.idCardNum(Convert.toStr(sysUser.getWxOpenid(), ""),2,2));
        return userInfo;
    }

    @Override
    public SysUserVo selectOpenId(String openId) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getWxOpenid, openId).last("LIMIT 1"));
    }

    @Override
    public boolean bindingOpenId(String openId) {
        return baseMapper.update(null, new LambdaUpdateWrapper<SysUser>().set(SysUser::getWxOpenid, openId).eq(SysUser::getUserId, LoginObject.getLoginId())) > 0;
    }

    @Override
    public Long selectTool() {
        return baseMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()));
    }

    @Override
    public List<AdminMenuTree> getAdminMenuTree(String userId) {

        SysUserVo userVo = this.getUserRoleAnfFunctionInfo(userId);
        // 用户角色
        List<SysRoleVo> roleVoList = userVo.getRoleVoList();
        //所有角色
        List<SysFunctionVo> functionVos = new ArrayList<>();
        for (SysRoleVo role : roleVoList) {
            if (role.getSysFunctionVoList() != null) {
                functionVos.addAll(role.getSysFunctionVoList());
            }
        }
        // 多角色合并时同一菜单会出现多条；超级管理员一次拉全量菜单时，展示名 name 也可能与其它菜单重复。
        // Vue Router 要求路由 name 全局唯一，否则注册/跳转异常（普通账号菜单少不易触发）。
        functionVos = dedupeFunctionVosById(functionVos);

        return getAdminMenuTree(functionVos, "1");
    }

    /** 按资源主键去重，保留首次出现顺序。 */
    private static List<SysFunctionVo> dedupeFunctionVosById(List<SysFunctionVo> list) {
        if (list == null || list.isEmpty()) {
            return list == null ? new ArrayList<>() : list;
        }
        Map<String, SysFunctionVo> map = new LinkedHashMap<>();
        for (SysFunctionVo vo : list) {
            if (vo == null || vo.getId() == null) {
                continue;
            }
            String id = vo.getId().trim();
            if (id.isEmpty()) {
                continue;
            }
            map.putIfAbsent(id, vo);
        }
        return new ArrayList<>(map.values());
    }

    /** 与 {@link SysFunctionServiceImpl} 保存校验一致，避免历史脏数据拖垮 getMenu。 */
    private static boolean isAdminMenuRouteRowValid(SysFunctionVo vo) {
        if (vo == null) {
            return false;
        }
        if (StringUtil.isEmpty(vo.getPath())) {
            return false;
        }
        return StringUtil.isNotEmpty(vo.getComponent()) || StringUtil.isNotEmpty(vo.getRedirectPath());
    }

    private List<AdminMenuTree> getAdminMenuTree(List<SysFunctionVo> functionVos, String funId) {
        // 构建parentId到子节点列表的映射，只包含类型为1的菜单项，提高查找效率
        Map<String, List<SysFunctionVo>> parentChildMap = new HashMap<>();
        for (SysFunctionVo vo : functionVos) {
            // 只处理类型为1的菜单项
            if (vo.getType() == 1) {
                if (!isAdminMenuRouteRowValid(vo)) {
                    log.warn(
                            "getAdminMenuTree 跳过无效目录菜单行 id={} path={} component={} redirectPath={}（须非空 path 且 component/redirect 至少其一，否则管理端动态路由异常）",
                            vo.getId(),
                            vo.getPath(),
                            vo.getComponent(),
                            vo.getRedirectPath());
                    continue;
                }
                String parentId = vo.getParentId();
                parentChildMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(vo);
            }
        }

        return buildAdminMenuTreeWithMap(parentChildMap, funId);
    }

    private List<AdminMenuTree> buildAdminMenuTreeWithMap(Map<String, List<SysFunctionVo>> parentChildMap, String parentId) {
        List<AdminMenuTree> result = new ArrayList<>();
        List<SysFunctionVo> children = parentChildMap.get(parentId);

        if (children == null || children.isEmpty()) {
            return result;
        }

        for (SysFunctionVo child : children) {
            AdminMenuTree menu = new AdminMenuTree();
            menu.setId(child.getId());
            menu.setPath(child.getPath());
            // 路由 name 须全局唯一；sys_function.name 多为中文展示名，在全量菜单下极易重复导致 Vue Router 异常
            menu.setName(child.getId());
            menu.setComponent(child.getComponent());

            Map<String, Object> meta = new HashMap<>();

            meta.put("title", child.getLanguageCode());
            meta.put("isLink", child.getIsLink());
            meta.put("isHide", child.getVisible() == 1);
            meta.put("isKeepAlive", child.getKeepAlive() == 1);
            meta.put("isAffix", child.getAlwaysShow() == 1);
            meta.put("isIframe", child.getIsIframe() == 1);
            if (!StringUtil.isEmpty(child.getPermission())) {
                meta.put("roles", child.getPermission().split(","));
            }
            meta.put("icon", child.getIcon());
            menu.setMeta(meta);

            // 递归构建子树
            List<AdminMenuTree> grandchildren = buildAdminMenuTreeWithMap(parentChildMap, child.getId());
            menu.setChildren(grandchildren);
            // 勿用 component_name 作为 redirect：若误填中文标题，Vue Router 会按相对路径解析成 /父路径/中文，导致 404
            // 父级已配置页面组件时勿设 redirect：否则访问父 path（如 /index）会被 Vue Router 直接跳到首个子菜单（如挂在其下的 /personal）
            if (!grandchildren.isEmpty()) {
                AdminMenuTree first = grandchildren.get(0);
                if (first != null && StringUtil.isNotEmpty(first.getPath()) && StringUtil.isEmpty(child.getComponent())) {
                    menu.setRedirect(first.getPath());
                }
            }
            result.add(menu);
        }

        return result;
    }

    /** 目标用户是否拥有 {@link SystemCommonEnum#SUPER_ADMIN} 角色（与菜单全量、删除保护等口径一致）。 */
    private boolean userHasSuperAdminRole(String userId) {
        List<SysRoleVo> roles = sysRoleMapper.selectByUserIdList(userId);
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        return roles.stream()
            .filter(Objects::nonNull)
            .map(SysRoleVo::getCode)
            .filter(Objects::nonNull)
            .anyMatch(SystemCommonEnum.SUPER_ADMIN.getCode()::equals);
    }

    /**
     * 个人中心展示：根据最后登录 IP 解析归属地（沿用 {@link AddressUtil} / ip2region）。
     * 本机、内网或无法解析时返回「未知」。
     */
    private static String formatLoginIpRegion(String loginIp) {
        if (StrUtil.isBlank(loginIp)) {
            return "未知";
        }
        String ip = loginIp.trim();
        if (isNonPublicOrLocalIp(ip)) {
            return "未知";
        }
        String raw = AddressUtil.getCityInfo(ip);
        if (StrUtil.isBlank(raw) || "未知".equals(raw) || raw.contains("内网")) {
            return "未知";
        }
        return compactIpRegionLabel(raw);
    }

    private static boolean isNonPublicOrLocalIp(String ip) {
        String lower = ip.toLowerCase(Locale.ROOT);
        if ("127.0.0.1".equals(lower)
            || "localhost".equals(lower)
            || "::1".equals(lower)
            || "0:0:0:0:0:0:0:1".equals(lower)) {
            return true;
        }
        if (lower.startsWith("10.")) {
            return true;
        }
        if (lower.startsWith("192.168.")) {
            return true;
        }
        if (lower.startsWith("172.")) {
            int o2 = secondIpv4Octet(lower);
            return o2 >= 16 && o2 <= 31;
        }
        return false;
    }

    private static int secondIpv4Octet(String ipv4) {
        try {
            String[] p = ipv4.split("\\.");
            if (p.length >= 2) {
                return Integer.parseInt(p[1]);
            }
        } catch (Exception ignored) {
            // ignore
        }
        return -1;
    }

    /** 将 ip2region 管线字段压成「省-市」样式 */
    private static String compactIpRegionLabel(String raw) {
        String[] seg = raw.split("\\|");
        List<String> parts = new ArrayList<>();
        for (String s : seg) {
            if (StrUtil.isBlank(s) || "0".equals(s) || "中国".equals(s)) {
                continue;
            }
            parts.add(s);
        }
        if (parts.isEmpty()) {
            return "未知";
        }
        if (parts.size() >= 2) {
            return parts.get(0) + "-" + parts.get(1);
        }
        return parts.get(0);
    }

}
