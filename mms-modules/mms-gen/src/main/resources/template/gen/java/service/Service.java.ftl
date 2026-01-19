package ${package}.${moduleName}.service;

import com.sxpcwlkj.framework.service.BaseService;
import ${package}.${moduleName}.entity.${ClassName};
import ${package}.${moduleName}.entity.bo.${ClassName}Bo;
import ${package}.${moduleName}.entity.vo.${ClassName}Vo;
import ${package}.${moduleName}.entity.export.${ClassName}Export;
import java.util.List;
import java.util.Set;

/**
 * ${tableComment}-接口
 *
 * @author ${author}
 * @Doc ${website}
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface ${ClassName}Service extends BaseService<${ClassName}, ${ClassName}Vo, ${ClassName}Bo> {
    <#if formLayout==2 >
    /**
    * ${tableComment}列表
    * @param isAll true：全部数据 false：有效数据(status=0)
    * @param showLevel 显示级别，0：全部 1：一级 2：二级 3：三级
    * @return ${tableComment}数结构列表
    */
    List<${ClassName}Vo> queryTree(boolean isAll,int showLevel);

    /**
    * 按照${tableComment}ID，查询下级所有${tableComment}
    *
    * @param id      ${tableComment}ID
    * @param endList 最终的数据
    */
    void queryListSon(String id, List<${ClassName}Vo> endList);
    </#if>
    /**
    * 导出${tableComment}
    * @param list ${tableComment}列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<${ClassName}Export> list);
}
