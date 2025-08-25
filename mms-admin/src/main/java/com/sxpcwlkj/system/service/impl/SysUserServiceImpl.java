package com.sxpcwlkj.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
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
    public Boolean updateById(SysUserBo bo) {
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
            .eq(StringUtil.isNotEmpty(bo.getStatus()), "u.status", bo.getStatus())
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
        return wrapper;
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
            //===================超级管理员拥有所有==========================
            if (userVo.getUserId().toString().equals(SystemCommonEnum.SUPER_ADMIN.getValue().toString())) {
                sysRoles = sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()).like(SysRole::getCode, SystemCommonEnum.SUPER_ADMIN.getCode()).orderByAsc(SysRole::getSort).last("LIMIT 1"));
                sysRoleVos = BeanCopyUtil.convert(sysRoles, SysRoleVo.class);

                List<SysFunction> functionList = sysFunctionMapper.selectList(new LambdaQueryWrapper<SysFunction>().eq(SysFunction::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue()).orderByAsc(SysFunction::getSort));
                List<SysFunctionVo> roleFunctionVoList = BeanCopyUtil.convert(functionList, SysFunctionVo.class);
                assert sysRoleVos != null;
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
        return baseMapper.selectVoList();
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
        userInfo.put("loginIp",Convert.toStr(sysUser.getLoginIp(), ""));
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
        return baseMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getStatus, 0));
    }

    @Override
    public List<AdminMenuTree> getAdminMenuTree(String userId) {

        SysUserVo userVo = this.getUserRoleAnfFunctionInfo(userId);
        // 用户角色
        List<SysRoleVo> roleVoList = userVo.getRoleVoList();
        //所有角色
        List<SysFunctionVo> functionVos = new ArrayList<>();
        for (SysRoleVo role : roleVoList) {
            functionVos.addAll(role.getSysFunctionVoList());
        }

        return getAdminMenuTree(functionVos, "1");
    }

    private List<AdminMenuTree> getAdminMenuTree(List<SysFunctionVo> functionVos, String funId) {

        List<AdminMenuTree> endList = new ArrayList<>();
        for (SysFunctionVo f : functionVos) {
            //是否是属于funId
            if (f.getParentId() != null && funId.equals(f.getParentId())) {
                if (f.getType() != 1) continue;
                AdminMenuTree menu = new AdminMenuTree();
                menu.setId(f.getId());
                menu.setPath(f.getPath());
                menu.setName(f.getName());
                menu.setComponent(f.getComponent());
                menu.setRedirect(f.getComponentName());

                Map<String, Object> meta = new HashMap<>();

                meta.put("title", f.getLanguageCode());
                meta.put("isLink", f.getIsLink());
                meta.put("isHide", f.getVisible() == 1);
                meta.put("isKeepAlive", f.getKeepAlive() == 1);
                meta.put("isAffix", f.getAlwaysShow() == 1);
                meta.put("isIframe", f.getIsIframe() == 1);
                if (!StringUtil.isEmpty(f.getPermission())) {
                    meta.put("roles", f.getPermission().split(","));
                }
                meta.put("icon", f.getIcon());
                menu.setMeta(meta);
                List<AdminMenuTree> children = getAdminMenuTree(functionVos, f.getId());
                menu.setChildren(children);
                endList.add(menu);
            }
        }

        return endList;

    }


}
