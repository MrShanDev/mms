package com.sxpcwlkj.system.service;

import com.sxpcwlkj.framework.sercice.BaseService;
import com.sxpcwlkj.system.entity.SysDept;
import com.sxpcwlkj.system.entity.bo.SysDeptBo;
import com.sxpcwlkj.system.entity.export.SysDeptExport;
import com.sxpcwlkj.system.entity.vo.SysDeptVo;

import java.util.List;
import java.util.Set;

/**
 * 系统部门-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface SysDeptService extends BaseService<SysDept, SysDeptVo, SysDeptBo> {
    /**
    * 系统部门列表
    * @param isAll true：全部数据 false：有效数据(status=0)
    * @param showLevel 显示级别，0：全部 1：一级 2：二级 3：三级
    * @return 系统部门数结构列表
    */
    List<SysDeptVo> queryTree(boolean isAll,int showLevel);

    /**
    * 按照系统部门ID，查询下级所有系统部门
    *
    * @param id      系统部门ID
    * @param endList 最终的数据
    */
    void queryListSon(String id, List<SysDeptVo> endList);
    /**
    * 导出系统部门
    * @param list 系统部门列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<SysDeptExport> list);
}
