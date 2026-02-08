package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.system.entity.SysDept;
import com.sxpcwlkj.system.entity.bo.SysDeptBo;
import com.sxpcwlkj.system.entity.export.SysDeptExport;
import com.sxpcwlkj.system.entity.vo.SysDeptVo;
import com.sxpcwlkj.system.mapper.SysDeptMapper;
import com.sxpcwlkj.system.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * 系统部门-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Service("sys_dept")
@RequiredArgsConstructor
public class SysDeptServiceImpl extends BaseServiceImpl<SysDept, SysDeptVo,SysDeptBo> implements SysDeptService {

   private final SysDeptMapper baseMapper;

    @Override
    public BaseMapperPlus<SysDept, SysDeptVo> getBaseMapper() {
        return baseMapper;
    }

    @Override
    public List<SysDeptVo> queryTree(boolean isAll,int showLevel) {
    List<SysDeptVo> queryTrees = baseMapper.selectVoList(new LambdaQueryWrapper<SysDept>()
            .eq(!isAll,SysDept::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByAsc(SysDept::getSort));
        return formatTree(queryTrees, "0",showLevel,0);
    }

    @Override
    public void queryListSon(String id, List<SysDeptVo> endList) {
        SysDeptVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            endList.add(vo);
            queryListSon(vo.getParentId(), endList);
        }
    }
    private List<SysDeptVo> formatTree(List<SysDeptVo> vos, String fid, int level,int currentLevel) {
        // 构建parentId到子节点列表的映射，提高查找效率
        Map<String, List<SysDeptVo>> parentChildMap = new HashMap<>();
        for (SysDeptVo vo : vos) {
            String parentId = vo.getParentId();
            parentChildMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(vo);
        }

        return buildTreeWithMap(parentChildMap, fid, level, currentLevel);
    }

    private List<SysDeptVo> buildTreeWithMap(Map<String, List<SysDeptVo>> parentChildMap, String parentId, int maxLevel, int currentLevel) {
        List<SysDeptVo> result = new ArrayList<>();
        List<SysDeptVo> children = parentChildMap.get(parentId);

        if (children == null || children.isEmpty()) {
            return result;
        }

        for (SysDeptVo child : children) {
            // 检查层级限制
            if (maxLevel > currentLevel || maxLevel == 0) {
                // 递归构建子树
                List<SysDeptVo> grandchildren = buildTreeWithMap(parentChildMap, child.getDeptId(), maxLevel, currentLevel + 1);
                child.setChildren(grandchildren);
                result.add(child);
            }
        }

        return result;
    }

    @Override
    public Boolean insert(SysDeptBo bo) {
        int row;
        bo.setDeptId(null);
        SysDept obj = MapstructUtil.convert(bo, SysDept.class);
        row = this.getBaseMapper().insert(obj);
        return row > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        String[] array = DataUtil.getCatStr(ids.toString(), ",");
        return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
    }

    @Override
    public Boolean updateByIdBase(SysDeptBo bo) {
        int row;
        SysDept obj = MapstructUtil.convert(bo, SysDept.class);
        row = this.getBaseMapper().updateById(obj);
        return row > 0;
    }

    @Override
    public SysDeptVo selectVoById(Serializable id) {
        SysDeptVo vo= this.getBaseMapper().selectVoById(id);
        List<String> end= new ArrayList<>();
            getIds(end,vo.getDeptId());
            Collections.reverse(end);
            vo.setDeptIds(end.toArray(new String[]{}));
        return vo;

    }
    private void getIds(List<String> end, String id) {
        SysDeptVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            end.add(vo.getDeptId());
            getIds(end, vo.getParentId());
        }
    }
    @Override
    public TableDataInfo<SysDeptVo> selectListVoPage(SysDeptBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysDept> lqw = buildQueryWrapper(bo);
        Page<SysDeptVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<SysDept> buildQueryWrapper(SysDeptBo query){
        if(query==null){
            query=new SysDeptBo();
        }
        LambdaQueryWrapper<SysDept> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getDeptId()), SysDept::getDeptId, query.getDeptId());
        wrapper.eq(StringUtil.isNotEmpty(query.getParentId()), SysDept::getParentId, query.getParentId());
        wrapper.eq(StringUtil.isNotEmpty(query.getDeptName()), SysDept::getDeptName, query.getDeptName());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<SysDeptExport> list) {
        return true;
    }
}
