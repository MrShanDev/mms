package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
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
    public Boolean updateById(SysFunctionBo bo) {
        SysFunction convert = MapstructUtil.convert(bo, SysFunction.class);
        return baseMapper.updateById(convert) > 0;
    }

    @Override
    public Boolean insert(SysFunctionBo bo) {
        bo.setId(null);
        SysFunction convert = MapstructUtil.convert(bo, SysFunction.class);
        return baseMapper.insert(convert) > 0;
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

    /**
     * 资源格式化
     *
     * @param functionVos 资源集合
     * @param funId       上级资源ID
     * @return AdminMenuTree
     */
    private List<SysFunctionVo> getSysFunctionVo(List<SysFunctionVo> functionVos, String funId, int level,int currentLevel) {
        List<SysFunctionVo> endList = new ArrayList<>();
        for (SysFunctionVo f : functionVos) {
            //是否是属于funId
            if (f.getParentId() != null && funId.equals(f.getParentId())) {
                if(level > currentLevel||level==0) {
                    List<SysFunctionVo> children = getSysFunctionVo(functionVos, f.getId(), level, currentLevel + 1);
                    f.setChildren(children);
                    endList.add(f);
                }
            }
        }
        return endList;

    }

    /**
     * 菜单格式化
     *
     * @param functionVos 资源集合
     * @param funId       上级资源ID
     * @return AdminMenuTree
     */
    private List<AdminMenuTree> getAdminMenuTree(List<SysFunctionVo> functionVos, String funId) {

        List<AdminMenuTree> endList = new ArrayList<>();
        for (SysFunctionVo f : functionVos) {
            //是否是属于funId
            if (f.getParentId() != null && funId.equals(f.getParentId())) {
                AdminMenuTree menu = new AdminMenuTree();
                menu.setId(f.getId());
                menu.setPath(f.getPath());
                menu.setName(f.getName());
                menu.setComponent(f.getComponent());
                menu.setRedirect(f.getComponentName());

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

                meta.put("title", f.getLanguageCode());
                meta.put("isLink", f.getIsLink());
                meta.put("isHide", f.getVisible().equals("true"));
                meta.put("isKeepAlive", f.getKeepAlive().equals("true"));
                meta.put("isAffix", f.getAlwaysShow().equals("true"));
                meta.put("isIframe", f.getIsIframe().equals("true"));
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
