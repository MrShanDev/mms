package com.sxpcwlkj.system.service;

import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.system.entity.AdminMenuTree;
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.entity.bo.ResetPwdBo;
import com.sxpcwlkj.system.entity.bo.ResetPwdSuperBo;
import com.sxpcwlkj.system.entity.bo.SetUserRoleSuperBo;
import com.sxpcwlkj.system.entity.bo.SysUserBo;
import com.sxpcwlkj.system.entity.export.SysUserExportVo;
import com.sxpcwlkj.system.entity.vo.SysUserVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mmsAdmin
 */
public interface SysUserService {

    /**
     * 分页查询用户列表
     * @param user 用户信息
     * @param pageQuery 分页参数
     * @return 用户列表
     */
    TableDataInfo<SysUserVo> selectPageUserList(SysUserBo user, PageQuery pageQuery);

    /**
     * 新增用户
     * @param bo 对象
     * @return vo
     */
    Boolean insert(SysUserBo bo);

    /**
     * 根据用户Id
     * @param userId 用户id
     * @return vo
     */
    SysUserVo selectVoById(String userId);

    /**
     * 编辑用户
     * @param bo 对象
     * @return true：成功 false ：失败
     */
    Boolean updateById(SysUserBo bo);

    /**
     * 删除用户
     * @param ids 用户id[]
     * @return vo
     */
    Boolean deleteById(String ids);

    /**
     * 导出用户
     * @param list 用户列表
     * @return true：成功 false ：失败
     */
    Boolean imports(Set<SysUserExportVo> list);

    /**
     * 根据用户名查询用户
     * @param tenantId 租户id
     * @param userName 用户名
     * @return 用户
     */
    SysUser selectByUserName(String tenantId, String userName);

    /**
     * 用户更新秘钥对
     * @param tenantId 租户id
     * @param userId 用户id
     * @return  true：成功 false ：失败
     */
    Boolean updateRsa(String tenantId, String userId);

    /**
     * 获取用户的角色，权限，基本信息
     * @param userId 用户id
     * @return vo
     */
    SysUserVo getUserRoleAnfFunctionInfo(String userId);

    /**
     * 获取当前登录对象的菜单
     * @param userId 用户id
     * @return 菜单列表
     */
    List<AdminMenuTree> getAdminMenuTree(String userId);

    /**
     * 更新头像
     * @param headerImg 头像
     * @return true：成功 false ：失败
     */
    Boolean updateHeaderImgById(String headerImg);

    /**
     * 重置密码
     * @param bo 对象
     * @return true：成功 false ：失败
     */
    Boolean resetPwd(ResetPwdBo bo);

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户
     */
    SysUserVo selectVoByPhone(String phone);

    /**
     * 根据手机号查询用户
     * @param email email
     * @return 用户
     */
    SysUserVo selectVoByEmail(String email);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<SysUserVo> selectAll();

    /**
     * 超级管理员重置用户密码
     * @param bo 对象
     * @return true：成功 false ：失败
     */
    Boolean resetPwdSuper(ResetPwdSuperBo bo);

    /**
     * 设置用户角色
     * @param bo 对象
     * @return true：成功 false ：失败
     */
    Boolean setUserRoleSuper(SetUserRoleSuperBo bo);

    /**
     * 解绑手机号
     * @param  1：解绑手机号 2：解绑邮箱  3：解绑微信
     * @return  true：成功 false ：失败
     */
    Boolean unbind(int type);

    /**
     * 绑定手机号
     * @param phone 绑定的新手机号
     * @return  true：成功 false ：失败
     */
    Boolean bindingPhone(String phone);

    /**
     * 绑定手机号
     * @param email 绑定的email
     * @return  true：成功 false ：失败
     */
    Boolean bindingEmail(String email);


    /**
     * 获取登录对象信息
     * @return 用户信息
     */
    Map<String,Object> getUserInfo(SysUserVo sysUser);

    /**
     * 根据openId查询用户
     * @param openId 微信openId
     * @return 用户信息
     */
    SysUserVo selectOpenId(String openId);

    /**
     * 绑定微信openId
     * @param openId 微信openId
     * @return  true：成功 false ：失败
     */
    boolean bindingOpenId(String openId);

    Long selectTool();
}
