package com.sxpcwlkj.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.entity.StoreNavMenu;
import com.sxpcwlkj.store.entity.bo.StoreNavMenuBo;
import com.sxpcwlkj.store.entity.export.StoreNavMenuExport;
import com.sxpcwlkj.store.entity.vo.StoreNavMenuVo;
import com.sxpcwlkj.store.mapper.StoreNavMenuMapper;
import com.sxpcwlkj.store.service.StoreNavMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
/**
 * 网站导航菜单-接口实现
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Transactional
@Service("store_nav_menu")
@RequiredArgsConstructor
public class StoreNavMenuServiceImpl extends BaseServiceImpl<StoreNavMenu, StoreNavMenuVo, StoreNavMenuBo> implements StoreNavMenuService {

    private final StoreNavMenuMapper baseMapper;

    @Override
    public BaseMapperPlus<StoreNavMenu, StoreNavMenuVo> getBaseMapper() {
        return baseMapper;
    }

    @Override
    public List<StoreNavMenuVo> queryTree(boolean isAll,int showLevel) {
        List<StoreNavMenuVo> queryTrees = baseMapper.selectVoList(new LambdaQueryWrapper<StoreNavMenu>()
            .eq(!isAll,StoreNavMenu::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByAsc(StoreNavMenu::getSort));
        return formatTree(queryTrees, "0",showLevel,0);
    }

    @Override
    public void queryListSon(String id, List<StoreNavMenuVo> endList) {
        StoreNavMenuVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            endList.add(vo);
            queryListSon(vo.getParentId(), endList);
        }
    }
    private List<StoreNavMenuVo> formatTree(List<StoreNavMenuVo> vos, String fid, int level,int currentLevel) {
        List<StoreNavMenuVo> endList = new ArrayList<>();
        for (StoreNavMenuVo s : vos) {
            if (fid.equals(s.getParentId())) {
                if(level > currentLevel||level==0) {
                    List<StoreNavMenuVo> vo = formatTree(vos, s.getId(),level,currentLevel+1);
                    s.setChildren(vo);
                    endList.add(s);
                }
            }
        }
        return endList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreNavMenuBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreNavMenu obj = MapstructUtil.convert(bo, StoreNavMenu.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("网站导航菜单,insert 操作失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        try {
            String[] array = DataUtil.getCatStr(ids.toString(), ",");
            return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
        } catch (Exception e) {
            log.error("网站导航菜单,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreNavMenuBo bo) {
        try {
            int row;
            StoreNavMenu obj = MapstructUtil.convert(bo, StoreNavMenu.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("网站导航菜单,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreNavMenuVo selectVoById(Serializable id) {
        StoreNavMenuVo vo= this.getBaseMapper().selectVoById(id);
        List<String> end= new ArrayList<>();
        getIds(end,vo.getId());
        Collections.reverse(end);
        vo.setIds(end.toArray(new String[]{}));
        return vo;

    }
    private void getIds(List<String> end, String id) {
        StoreNavMenuVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            end.add(vo.getId());
            getIds(end, vo.getParentId());
        }
    }
    @Override
    public TableDataInfo<StoreNavMenuVo> selectListVoPage(StoreNavMenuBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreNavMenu> lqw = buildQueryWrapper(bo);
        Page<StoreNavMenuVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreNavMenu> buildQueryWrapper(StoreNavMenuBo query){
        if(query==null){
            query=new StoreNavMenuBo();
        }
        LambdaQueryWrapper<StoreNavMenu> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreNavMenuExport> list) {
        return true;
    }
}
