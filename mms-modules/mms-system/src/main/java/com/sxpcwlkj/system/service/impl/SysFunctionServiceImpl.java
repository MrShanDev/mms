package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.system.entity.AdminMenuTree;
import com.sxpcwlkj.system.entity.SysFunction;
import com.sxpcwlkj.system.entity.bo.SysFunctionBo;
import com.sxpcwlkj.system.entity.vo.SysFunctionVo;
import com.sxpcwlkj.system.mapper.SysFunctionMapper;
import com.sxpcwlkj.system.service.SysFunctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 系统菜单
 */
@RequiredArgsConstructor
@Service("sysFunction")
@Slf4j
public class SysFunctionServiceImpl implements SysFunctionService {

    private final SysFunctionMapper baseMapper;

    @Override
    public List<SysFunctionVo> selectPageList(SysFunctionBo bo, PageQuery pageQuery) {
        List<SysFunctionVo> functionVos = baseMapper.selectVoList(new LambdaQueryWrapper<SysFunction>().orderByAsc(SysFunction::getSort));
        return getSysFunctionVo(functionVos, "0",bo.getLevel(),0);
    }

    @Override
    public SysFunctionVo selectVoById(String id) {
        SysFunctionVo vo = baseMapper.selectVoById(id);
        List<SysFunctionVo> list = new ArrayList<>();
        if (vo != null) {
            getIds(list, vo.getParentId());
        }
        Collections.reverse(list);
        assert vo != null;
        vo.setMenuSuperior(list.stream().map(SysFunctionVo::getId).toArray(String[]::new));
        return vo;
    }


    private List<SysFunctionVo> getIds(List<SysFunctionVo> end, String id) {
        SysFunctionVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            end.add(vo);
            getIds(end, vo.getParentId());
        }
        return end;
    }


    @Override
    public Boolean updateByIdBase(SysFunctionBo bo) {
        SysFunction convert = MapstructUtil.convert(bo, SysFunction.class);
        assertType1MenuRouteComplete(convert);
        return baseMapper.updateById(convert) > 0;
    }

    @Override
    public Boolean insert(SysFunctionBo bo) {
        bo.setId(null);
        SysFunction convert = MapstructUtil.convert(bo, SysFunction.class);
        assertType1MenuRouteComplete(convert);
        return baseMapper.insert(convert) > 0;
    }

    /**
     * 管理端动态路由（getMenu）要求：类型=目录(1) 时 path 非空，且 component 与 redirect 至少填其一，
     * 否则 Vue Router 注册异常（超级管理员全量菜单时尤易暴露）。
     */
    private static void assertType1MenuRouteComplete(SysFunction f) {
        if (f == null || f.getType() == null || f.getType() != 1) {
            return;
        }
        if (StringUtil.isEmpty(f.getPath())) {
            throw new MmsException("目录/菜单须填写路由地址 path，否则管理端无法生成动态路由");
        }
        boolean hasComp = StringUtil.isNotEmpty(f.getComponent());
        boolean hasRedirect = StringUtil.isNotEmpty(f.getRedirectPath());
        if (!hasComp && !hasRedirect) {
            throw new MmsException("目录/菜单须填写组件路径 component，或填写默认跳转 redirect（与菜单管理中「路由重定向」一致）");
        }
    }

    @Override
    public Boolean deleteById(String id) {
        List<SysFunctionVo> functionVos = baseMapper.selectVoList(new LambdaQueryWrapper<SysFunction>().eq(SysFunction::getParentId, id));
        if (!functionVos.isEmpty()) {
           throw new RuntimeException("该资菜单存在子菜单，请先删除子菜单");
        }
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public List<AdminMenuTree> getAllMenuTree() {
        List<SysFunctionVo> functionVos = baseMapper.selectVoList(new LambdaQueryWrapper<SysFunction>().orderByAsc(SysFunction::getSort));
        return getAdminMenuTree(functionVos, "0");
    }

    @Override
    public List<SysFunctionVo> selectIsFast(int size) {
        return baseMapper.selectVoList(new LambdaQueryWrapper<SysFunction>()
            .eq(SysFunction::getIsFast, 1)
            .ne(SysFunction::getComponent,"")
            .last("LIMIT "+size)
        );
    }

    /**
     * 资源格式化
     *
     * @param functionVos 资源集合
     * @param funId       上级资源ID
     * @return AdminMenuTree
     */
    private List<SysFunctionVo> getSysFunctionVo(List<SysFunctionVo> functionVos, String funId, int level,int currentLevel) {
        // 构建parentId到子节点列表的映射，提高查找效率
        Map<String, List<SysFunctionVo>> parentChildMap = new HashMap<>();
        for (SysFunctionVo vo : functionVos) {
            String parentId = vo.getParentId();
            parentChildMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(vo);
        }

        return buildFunctionTreeWithMap(parentChildMap, funId, level, currentLevel);
    }

    private List<SysFunctionVo> buildFunctionTreeWithMap(Map<String, List<SysFunctionVo>> parentChildMap, String parentId, int maxLevel, int currentLevel) {
        List<SysFunctionVo> result = new ArrayList<>();
        List<SysFunctionVo> children = parentChildMap.get(parentId);

        if (children == null || children.isEmpty()) {
            return result;
        }

        for (SysFunctionVo child : children) {
            // 检查层级限制
            if (maxLevel > currentLevel || maxLevel == 0) {
                // 递归构建子树
                List<SysFunctionVo> grandchildren = buildFunctionTreeWithMap(parentChildMap, child.getId(), maxLevel, currentLevel + 1);
                child.setChildren(grandchildren);
                result.add(child);
            }
        }

        return result;
    }

    /**
     * 菜单格式化
     *
     * @param functionVos 资源集合
     * @param funId       上级资源ID
     * @return AdminMenuTree
     */
    private List<AdminMenuTree> getAdminMenuTree(List<SysFunctionVo> functionVos, String funId) {
        // 构建parentId到子节点列表的映射，提高查找效率
        Map<String, List<SysFunctionVo>> parentChildMap = new HashMap<>();
        for (SysFunctionVo vo : functionVos) {
            String parentId = vo.getParentId();
            parentChildMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(vo);
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
            menu.setName(child.getName());
            menu.setComponent(child.getComponent());

            // 菜单名称
//                private String title;
//                // 外链/内嵌时链接地址（http:xxx.com），开启外链条件，`1、isLink: 链接地址不为空`
//                private String isLink;
//                // 是否隐藏
//                private Boolean isHide;
//                // 是否缓存
//                private Boolean isKeepAlive;
//                // 是否固定
//                private Boolean isAffix;
//                // 是否内嵌，开启条件，`1、isIframe:true 2、isLink：链接地址不为空`
//                private Boolean isIframe;
//                // 权限标识，取角色管理
//                private String[] roles;
//                // 菜单图标
//                private String icon;

            Map<String, Object> meta = new HashMap<>();

            meta.put("title", child.getLanguageCode());
            meta.put("isLink", child.getIsLink());
            meta.put("isHide", child.getVisible().equals("true"));
            meta.put("isKeepAlive", child.getKeepAlive().equals("true"));
            meta.put("isAffix", child.getAlwaysShow().equals("true"));
            meta.put("isIframe", child.getIsIframe().equals("true"));
            if (!StringUtil.isEmpty(child.getPermission())) {
                meta.put("roles", child.getPermission().split(","));
            }

            meta.put("icon", child.getIcon());
            menu.setMeta(meta);

            // 递归构建子树
            List<AdminMenuTree> grandchildren = buildAdminMenuTreeWithMap(parentChildMap, child.getId());
            menu.setChildren(grandchildren);
            // 父级已配置页面组件时勿设 redirect，避免进入父路由时被重定向到第一个子菜单
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

}
