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
import com.sxpcwlkj.system.entity.SysUser;
import com.sxpcwlkj.system.mapper.SysUserMapper;
import com.sxpcwlkj.common.exception.MmsException;
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
   private final SysUserMapper sysUserMapper;

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
        if (vo != null && endList.stream().noneMatch(item -> Objects.equals(item.getDeptId(), vo.getDeptId()))) {
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
        validateParent(null, bo.getParentId());
        bo.setDeptId(null);
        SysDept obj = MapstructUtil.convert(bo, SysDept.class);
        row = this.getBaseMapper().insert(obj);
        return row > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        String[] array = DataUtil.getCatStr(ids.toString(), ",");
        for (String id : array) {
            if (baseMapper.selectCount(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id)) > 0) {
                throw new MmsException("请先处理下级部门");
            }
            if (sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getDeptId, id)) > 0) {
                throw new MmsException("部门仍有关联用户，不能删除");
            }
        }
        return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
    }

    @Override
    public Boolean updateByIdBase(SysDeptBo bo) {
        validateParent(bo.getDeptId(), bo.getParentId());
        int row;
        SysDept obj = MapstructUtil.convert(bo, SysDept.class);
        row = this.getBaseMapper().updateById(obj);
        return row > 0;
    }

    private void validateParent(String deptId, String parentId) {
        Set<String> visited = new HashSet<>();
        String cursor = parentId;
        while (cursor != null && !cursor.isBlank() && !"0".equals(cursor)) {
            if (Objects.equals(deptId, cursor) || !visited.add(cursor)) {
                throw new MmsException("上级部门不能是自身或下级部门");
            }
            SysDept parent = baseMapper.selectById(cursor);
            if (parent == null) throw new MmsException("上级部门不存在");
            cursor = parent.getParentId();
        }
    }

    @Override
    public SysDeptVo selectVoById(Serializable id) {
        SysDeptVo vo= this.getBaseMapper().selectVoById(id);
        if (vo == null) return null;
        List<String> end= new ArrayList<>();
            getIds(end,vo.getDeptId());
            Collections.reverse(end);
            vo.setDeptIds(end.toArray(new String[]{}));
        return vo;

    }
    private void getIds(List<String> end, String id) {
        SysDeptVo vo = baseMapper.selectVoById(id);
        if (vo != null && !end.contains(vo.getDeptId())) {
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean imports(Set<SysDeptExport> list) {
        if (list == null || list.isEmpty() || list.size() > 1000) throw new MmsException("请导入 1 至 1000 条部门");
        Map<String, SysDeptExport> pending = new java.util.LinkedHashMap<>();
        for (var row : list) {
            if (row == null || row.getDeptId() == null || !row.getDeptId().matches("[0-9]{1,19}") || "0".equals(row.getDeptId())
                || row.getDeptName() == null || row.getDeptName().isBlank()) throw new MmsException("部门编号及名称必须有效");
            if (pending.putIfAbsent(row.getDeptId(), row) != null || baseMapper.selectById(row.getDeptId()) != null)
                throw new MmsException("部门编号重复或已存在: " + row.getDeptId());
        }
        // 校验整批父链，包括同一文件中尚未落库的父节点。
        for (var row : list) {
            Set<String> visited = new HashSet<>(); visited.add(row.getDeptId());
            String parent = row.getParentId();
            while (parent != null && !parent.isBlank() && !"0".equals(parent)) {
                if (!visited.add(parent)) throw new MmsException("导入部门存在父子循环");
                if (pending.containsKey(parent)) parent = pending.get(parent).getParentId();
                else {
                    SysDept existing = baseMapper.selectById(parent);
                    if (existing == null) throw new MmsException("上级部门不存在: " + parent);
                    parent = existing.getParentId();
                }
            }
        }
        for (var row : list) {
            SysDept obj = new SysDept(); obj.setDeptId(row.getDeptId()); obj.setDeptName(row.getDeptName());
            obj.setParentId(row.getParentId() == null || row.getParentId().isBlank() ? "0" : row.getParentId());
            obj.setLeader(row.getLeader()); obj.setPhone(row.getPhone()); obj.setEmail(row.getEmail()); obj.setAddress(row.getAddress());
            if (baseMapper.insert(obj) != 1) throw new MmsException("部门导入失败");
        }
        return true;
    }
}
