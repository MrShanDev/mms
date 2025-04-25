package com.sxpcwlkj.system.service;

import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.system.entity.bo.SysDictBo;
import com.sxpcwlkj.system.entity.vo.SysDictDataVo;
import com.sxpcwlkj.system.entity.vo.SysDictVo;

import java.util.List;
import java.util.Map;


/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
public interface SysDictService {
    TableDataInfo<SysDictVo> selectPageList(SysDictBo bo, PageQuery pageQuery);

    /**
     * 查询
     * @param id ID
     * @return vo
     */
    SysDictVo selectVoById(Long id);

    /**
     * 更新
     * @param bo 对象
     * @return vo
     */
    Boolean updateById(SysDictBo bo);

    /**
     * 新增
     * @param bo 对象
     * @return vo
     */
    Boolean insert(SysDictBo bo);
    /**
     * 删除
     * @param
     * @return vo
     */
    Boolean deleteById(Long id);

    /**
     * 初始化字典
     * @param code
     * @return
     */
    Integer initSysDict(String code);

    /**
     * 获取字典值列表
     * @param code
     * @return
     */
    List<SysDictDataVo> getSysDictByCode(String code);

    /**
     * 获取所有字典列表
     * @return
     */
    List<Map<String,Object>> selectAll();
}
