package com.sxpcwlkj.framework.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description BaseService
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
public interface BaseService<T, V, B> {
    //=================基础的增删改查操作========================

    /**
     * 插入一个实体类
     *
     * @param entity
     * @return
     */
    Boolean insert(B entity);

    /**
     * 根据实体类的主键删除一个实体
     *
     */
    Boolean deleteById(Serializable ids);

    /**
     * 通过实体类删除
     *
     * @param lqw
     */
    Boolean delete(LambdaQueryWrapper<T> lqw);

    /**
     * 通过map删除
     *
     * @param map
     */
    Boolean deleteByMap(Map<String, Object> map);

    /**
     * 更新一个实体类
     *
     * @param lqw
     */
    Boolean update(LambdaUpdateWrapper<T> lqw);

    /**
     * 通过实体类的id来修改
     *
     * @param entity
     */
    Boolean updateById(B entity);

    /**
     * 根据参数查询
     *
     * @param map
     * @return
     */
    List<V> selectVoListByMap(Map<String, Object> map);

    /**
     * 查询所有
     *
     * @return
     */
    List<V> selectVoAll();

    /**
     * 查询所有的实体，根据实体的属性值，属性值作为条件
     *
     * @param entity
     * @return
     */
    List<V> selectVoAllByEntity(B entity);


    /**
     * 根据主键获取一个实体
     *
     * @param id
     * @return
     */
    V selectVoById(Serializable id);


    /**
     * 通过对象查询----不分页
     *
     * @param lqw
     * @return
     */
    List<V> selectVoListByLqw(LambdaQueryWrapper<T> lqw);

    //=========================分页查询中的操作=====================================

    /**
     * 通过对象查询----分页
     *
     * @param current  当前页
     * @param pageSize 每页显示提搜书
     * @return
     */
    TableDataInfo<T> selectVoListPage(Integer current, Integer pageSize);

    /**
     * 通过对象查询----分页
     *
     * @param entity
     * @param pageQuery
     * @return
     */
    TableDataInfo<V> selectListVoPage(B entity, PageQuery pageQuery);

    //=========================批量的操作=====================================

    /**
     * 批量新增
     *
     * @param list
     */
    Boolean insertBatch(List<B> list);

    /**
     * 批量修改
     *
     * @param list
     */
    Boolean updateBatchById(List<B> list);

    //=============================封装纯sql语句==================================

    /**
     * 通过sql 查询一个对象返回map集合
     *
     * @param sql
     * @return
     */
    Map<String, Object> getMapBySql(@Param("sql") String sql);

    /**
     * 通过sql查询一个对象返回实体类
     *
     * @param sql
     * @return
     */
    V getVoBySql(@Param("sql") String sql);

    /**
     * 通过sql查询到的map集合封装成List集合返回
     *
     * @param sql
     * @return
     */
    List<Map<String, Object>> getListMapBySql(@Param("sql") String sql);

    /**
     * 通过sql查询到的结果封装为List集合
     *
     * @param sql
     * @return
     */
    List<V> getListVoBySql(@Param("sql") String sql);

    /**
     * 查询分页
     *
     * @param sql
     * @return
     */
    TableDataInfo<V> getPageVoBySql(@Param("sql") String sql);

    /**
     * 通过sql查询到的结果修改
     *
     * @param sql
     */
    Boolean updateBysql(@Param("sql") String sql);

    /**
     * 通过sql删除
     *
     * @param sql
     */
    Boolean deleteBySql(@Param("sql") String sql);

}
