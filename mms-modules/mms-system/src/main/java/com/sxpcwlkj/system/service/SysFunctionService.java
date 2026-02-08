package com.sxpcwlkj.system.service;

import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.system.entity.AdminMenuTree;
import com.sxpcwlkj.system.entity.bo.SysFunctionBo;
import com.sxpcwlkj.system.entity.vo.SysFunctionVo;

import java.util.List;

/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
public interface SysFunctionService {

    List<SysFunctionVo> selectPageList(SysFunctionBo bo, PageQuery pageQuery);

    /**
     * 查询
     * @param id ID
     * @return vo
     */
    SysFunctionVo selectVoById(String id);

    /**
     * 更新
     * @param bo 对象
     * @return vo
     */
    Boolean updateByIdBase(SysFunctionBo bo);

    /**
     * 新增
     * @param bo 对象
     * @return vo
     */
    Boolean insert(SysFunctionBo bo);
    /**
     * 删除
     * @param
     * @return vo
     */
    Boolean deleteById(String id);

    List<AdminMenuTree> getAllMenuTree();

    /**
     * 快捷菜单
     * @param size 查询数量
     * @return 数据
     */
    List<SysFunctionVo> selectIsFast(int size);
}
