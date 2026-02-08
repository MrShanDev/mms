package ${package}.${moduleName}.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import ${package}.${moduleName}.entity.${ClassName};
import ${package}.${moduleName}.entity.bo.${ClassName}Bo;
import ${package}.${moduleName}.entity.vo.${ClassName}Vo;
import ${package}.${moduleName}.entity.export.${ClassName}Export;
import ${package}.${moduleName}.mapper.${ClassName}Mapper;
import ${package}.${moduleName}.service.${ClassName}Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
/**
 * ${tableComment}-接口实现
 *
* @author ${author}
* @Doc ${website}
 */
@Slf4j
@Transactional
@Service("${tableName}")
@RequiredArgsConstructor
public class ${ClassName}ServiceImpl extends BaseServiceImpl<${ClassName}, ${ClassName}Vo,${ClassName}Bo> implements ${ClassName}Service {

   private final ${ClassName}Mapper baseMapper;

    @Override
    public BaseMapperPlus<${ClassName}, ${ClassName}Vo> getBaseMapper() {
        return baseMapper;
    }

    <#if formLayout==2 >
    @Override
    public List<${ClassName}Vo> queryTree(boolean isAll,int showLevel) {
    List<${ClassName}Vo> queryTrees = baseMapper.selectVoList(new LambdaQueryWrapper<${ClassName}>()
            .eq(!isAll,${ClassName}::getStatus, SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue())
            .orderByAsc(${ClassName}::getSort));
        return formatTree(queryTrees, "0",showLevel,0);
    }

    @Override
    public void queryListSon(String id, List<${ClassName}Vo> endList) {
        ${ClassName}Vo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            endList.add(vo);
            queryListSon(String.valueOf(vo.getParentId()), endList);
        }
    }
    private List<${ClassName}Vo> formatTree(List<${ClassName}Vo> vos, String fid, int level,int currentLevel) {
        // 构建parentId到子节点列表的映射，提高查找效率
        java.util.Map<String, List<${ClassName}Vo>> parentChildMap = new java.util.HashMap<>();
        for (${ClassName}Vo vo : vos) {
            String parentId = String.valueOf(vo.getParentId());
            parentChildMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(vo);
        }

        return buildTreeWithMap(parentChildMap, fid, level, currentLevel);
    }

    private List<${ClassName}Vo> buildTreeWithMap(java.util.Map<String, List<${ClassName}Vo>> parentChildMap, String parentId, int maxLevel, int currentLevel) {
        List<${ClassName}Vo> result = new ArrayList<>();
        List<${ClassName}Vo> children = parentChildMap.get(parentId);

        if (children == null || children.isEmpty()) {
            return result;
        }

        for (${ClassName}Vo child : children) {
            // 检查层级限制
            if (maxLevel > currentLevel || maxLevel == 0) {
                // 递归构建子树
                List<${ClassName}Vo> grandchildren = buildTreeWithMap(parentChildMap, String.valueOf(child.get${TableId}()), maxLevel, currentLevel + 1);
                child.setChildren(grandchildren);
                result.add(child);
            }
        }

        return result;
    }
    </#if>

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(${ClassName}Bo bo) {
        try {
            int row;
            bo.set${TableId}(null);
            ${ClassName} obj = MapstructUtil.convert(bo, ${ClassName}.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.set${TableId}(obj.get${TableId}());
            return row > 0;
        } catch (Exception e) {
            log.error("${tableComment},insert 操作失败", e);
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
            log.error("${tableComment},deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateByIdBase(${ClassName}Bo bo) {
        try {
            int row;
            ${ClassName} obj = MapstructUtil.convert(bo, ${ClassName}.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("${tableComment},updateByIdBase 操作失败", e);
            throw e;
        }
    }

    @Override
    public ${ClassName}Vo selectVoById(Serializable id) {
        <#if formLayout==2 >
        ${ClassName}Vo vo= this.getBaseMapper().selectVoById(id);
        List<String> end= new ArrayList<>();
            getIds(end,String.valueOf(vo.get${TableId}()));
            Collections.reverse(end);
            vo.set${TableId}s(end.toArray(new String[]{}));
        return vo;
        <#else>
        return this.getBaseMapper().selectVoById(id);
        </#if>

    }
    <#if formLayout==2 >
    private void getIds(List<String> end, String id) {
        // 获取所有可能需要的节点，避免递归查询
        List<${ClassName}Vo> allNodes = baseMapper.selectVoList(new LambdaQueryWrapper<${ClassName}>()
                .orderByAsc(${ClassName}::getSort));

        // 构建ID到节点的映射
        java.util.Map<String, ${ClassName}Vo> idNodeMap = new java.util.HashMap<>();
        for (${ClassName}Vo node : allNodes) {
            idNodeMap.put(String.valueOf(node.get${TableId}()), node);
        }

        // 通过循环而不是递归获取上级节点
        String currentId = id;
        while (currentId != null && !"0".equals(currentId)) {
            ${ClassName}Vo currentNode = idNodeMap.get(currentId);
            if (currentNode != null) {
                end.add(String.valueOf(currentNode.get${TableId}()));
                currentId = String.valueOf(currentNode.get${TableParentId}());
            } else {
                break; // 节点不存在，跳出循环
            }
        }
    }
    </#if>
    @Override
    public TableDataInfo<${ClassName}Vo> selectListVoPage(${ClassName}Bo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<${ClassName}> lqw = buildQueryWrapper(bo);
        Page<${ClassName}Vo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<${ClassName}> buildQueryWrapper(${ClassName}Bo query){
        if(query==null){
            query=new ${ClassName}Bo();
        }
        LambdaQueryWrapper<${ClassName}> wrapper = Wrappers.lambdaQuery();
        <#list queryList as field>
            <#if field.queryType == '='>
        wrapper.eq(StringUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, query.get${field.attrName?cap_first}());
            <#elseif field.queryType == '!='>
        wrapper.ne(StringUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, query.get${field.attrName?cap_first}());
            <#elseif field.queryType == '>'>
        wrapper.gt(StringUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, query.get${field.attrName?cap_first}());
            <#elseif field.queryType == '>='>
        wrapper.ge(StringUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, query.get${field.attrName?cap_first}());
            <#elseif field.queryType == '<'>
        wrapper.lt(StringUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, query.get${field.attrName?cap_first}());
            <#elseif field.queryType == '<='>
        wrapper.le(StringUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, query.get${field.attrName?cap_first}());
            <#elseif field.queryType == 'like'>
        wrapper.like(StringUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, query.get${field.attrName?cap_first}());
            <#elseif field.queryType == 'left like'>
        wrapper.likeLeft(StringUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, query.get${field.attrName?cap_first}());
            <#elseif field.queryType == 'right like'>
        wrapper.likeRight(StringUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, query.get${field.attrName?cap_first}());
            <#elseif field.queryType == 'between'>
        wrapper.between(ArrayUtil.isNotEmpty(query.get${field.attrName?cap_first}()), ${ClassName}::get${field.attrName?cap_first}, ArrayUtil.isNotEmpty(query.get${field.attrName?cap_first}()) ? query.get${field.attrName?cap_first}()[0] : null, ArrayUtil.isNotEmpty(query.get${field.attrName?cap_first}()) ? query.get${field.attrName?cap_first}()[1] : null);
            </#if>
        </#list>
        return wrapper;
    }

    @Override
    public Boolean imports(Set<${ClassName}Export> list) {
        return true;
    }
}
