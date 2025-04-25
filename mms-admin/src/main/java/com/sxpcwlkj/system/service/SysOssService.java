package com.sxpcwlkj.system.service;

import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.system.entity.bo.SysOssBo;
import com.sxpcwlkj.system.entity.vo.SysOssVo;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
public interface SysOssService {
    TableDataInfo<SysOssVo> selectPageList(SysOssBo bo, PageQuery pageQuery);

    /**
     * 查询
     * @param id ID
     * @return vo
     */
    SysOssVo selectVoById(Long id) ;

    /**
     * 更新
     * @param bo 对象
     * @return vo
     */
    Boolean updateById(SysOssBo bo);

    /**
     * 新增
     * @param bo 对象
     * @return vo
     */
    Boolean insert(SysOssBo bo);
    /**
     * 删除
     * @param
     * @return vo
     */
    Boolean deleteById(Serializable id);
}
