package com.sxpcwlkj.system.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.lang.Console;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.system.entity.vo.SysRoleVo;
import com.sxpcwlkj.system.entity.vo.SysUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 * 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
 * @author xijue
 */
@Component
@RequiredArgsConstructor
public class SaTokenStpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     * @param loginId：账号id
     * @param loginType：账号体系标识
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<>();

        SysUserVo sysUserVo = LoginObject.getLoginObject(SysUserVo.class);
        assert sysUserVo != null;
        List<SysRoleVo> roleVoList = sysUserVo.getRoleVoList();
        for (SysRoleVo r:roleVoList) {
            list.addAll(Arrays.asList(r.getPermissions()));
        }

        Console.log(list.toString());
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     * @param loginId：账号id
     * @param loginType：账号体系标识
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<>();

        SysUserVo userVo = LoginObject.getLoginObject(SysUserVo.class);
        SysUserVo sysUserVo = MapstructUtil.convert(userVo, SysUserVo.class);
        assert sysUserVo != null;
        String[] roleCodes = sysUserVo.getRoleCodes();
        list= Arrays.asList(roleCodes);

        return list;
    }

}

