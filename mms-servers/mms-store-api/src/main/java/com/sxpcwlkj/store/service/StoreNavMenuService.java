package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreNavMenu;
import com.sxpcwlkj.store.entity.bo.StoreNavMenuBo;
import com.sxpcwlkj.store.entity.export.StoreNavMenuExport;
import com.sxpcwlkj.store.entity.vo.StoreNavMenuVo;

import java.util.List;
import java.util.Set;

/**
 * 网站导航菜单-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreNavMenuService extends BaseService<StoreNavMenu, StoreNavMenuVo, StoreNavMenuBo> {
    /**
     * 网站导航菜单列表
     * @param isAll true：全部数据 false：有效数据(status=0)
     * @param showLevel 显示级别，0：全部 1：一级 2：二级 3：三级
     * @return 网站导航菜单数结构列表
     */
    List<StoreNavMenuVo> queryTree(boolean isAll, int showLevel);

    /**
     * 按照网站导航菜单ID，查询下级所有网站导航菜单
     *
     * @param id      网站导航菜单ID
     * @param endList 最终的数据
     */
    void queryListSon(String id, List<StoreNavMenuVo> endList);
    /**
     * 导出网站导航菜单
     * @param list 网站导航菜单列表
     * @return true：成功 false ：失败
     */
    Boolean imports(Set<StoreNavMenuExport> list);
}
