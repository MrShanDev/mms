package com.sxpcwlkj.system.service;

import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.system.entity.bo.SysRoleBo;
import com.sxpcwlkj.system.entity.vo.SysRoleVo;

/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */

public interface SysRoleService {
    TableDataInfo<SysRoleVo> selectPageUserList(SysRoleBo bo, PageQuery pageQuery);

    /**
     * 查询
     * @param id ID
     * @return vo
     */
    SysRoleVo selectVoById(String id);

    /**
     * 更新
     * @param bo 对象
     * @return vo
     */
    Boolean updateByIdBase(SysRoleBo bo);

    /**
     * 新增
     * @param bo 对象
     * @return vo
     */
    Boolean insert(SysRoleBo bo);
    /**
     * 删除
     * @param
     * @return vo
     */
    Boolean deleteById(String id);
}
