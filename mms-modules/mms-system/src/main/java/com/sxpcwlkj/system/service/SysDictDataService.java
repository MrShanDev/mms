package com.sxpcwlkj.system.service;

import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.system.entity.bo.SysDictDataBo;
import com.sxpcwlkj.system.entity.vo.SysDictDataVo;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
public interface SysDictDataService {
    TableDataInfo<SysDictDataVo> selectPageList(SysDictDataBo bo, PageQuery pageQuery);

    /**
     * 查询
     * @param id ID
     * @return vo
     */
    SysDictDataVo selectVoById(Long id);

    /**
     * 更新
     * @param bo 对象
     * @return vo
     */
    Boolean updateByIdBase(SysDictDataBo bo);

    /**
     * 新增
     * @param bo 对象
     * @return vo
     */
    Boolean insert(SysDictDataBo bo);
    /**
     * 删除
     * @param
     * @return vo
     */
    Boolean deleteById(Serializable id);
}
