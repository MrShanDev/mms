package com.sxpcwlkj.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 抽象类  ----通过子类来完成实例化
 */
public abstract class BaseServiceImpl<T, V, B> implements BaseService<T, V, B> {

    Class<T> clazz;

    public void testNew(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getTClass() {
        return clazz;
    }

    public abstract BaseMapperPlus<T, V> getBaseMapper();

    @Override
    public Boolean insert(B entity) {
        int row;
        T obj = MapstructUtil.convert(entity, getTClass());
        row = getBaseMapper().insert(obj);
        return row > 0;

    }

    @Override
    public Boolean deleteById(Serializable id) {
        return this.getBaseMapper().deleteById(id) > 0;
    }

    @Override
    public Boolean delete(LambdaQueryWrapper<T> lqw) {
        int row;
        row = getBaseMapper().delete(lqw);
        return row > 0;
    }

    @Override
    public Boolean deleteByMap(Map<String, Object> map) {
        return this.getBaseMapper().deleteByMap(map) > 0;
    }

    @Override
    public Boolean update(LambdaUpdateWrapper<T> lqw) {
        int row;
        row = getBaseMapper().update(lqw);
        return row > 0;
    }

    @Override
    public Boolean updateById(B entity) {
        int row;
        T obj = MapstructUtil.convert(entity, getTClass());
        row = getBaseMapper().updateById(obj);
        return row > 0;
    }

    @Override
    public List<V> selectVoListByMap(Map<String, Object> map) {
        return getBaseMapper().selectVoByMap(map);
    }

    @Override
    public List<V> selectVoAll() {
        return getBaseMapper().selectVoList();
    }

    @Override
    public List<V> selectVoAllByEntity(B entity) {
        return getBaseMapper().selectVoList();
    }

    @Override
    public V selectVoById(Serializable id) {
        return getBaseMapper().selectVoById(id);
    }

    @Override
    public List<V> selectVoListByLqw(LambdaQueryWrapper<T> lqw) {
        return getBaseMapper().selectVoList(lqw);
    }

    //=========================分页查询中的操作=======================

    @Override
    public TableDataInfo<T> selectVoListPage(Integer current, Integer pageSize) {
        //参数一是当前页，参数二是每页个数
        IPage<T> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>(current, pageSize);
        page = getBaseMapper().selectPage(page, null);
        return TableDataInfo.build(page);
    }

    // public TableDataInfo<V> getVoListPageXml(B entity, PageQuery pageQuery);

    // ========================批量操作============================

    @Override
    public Boolean insertBatch(List<B> list) {
        List<T> obj = MapstructUtil.convert(list, getTClass());
        return getBaseMapper().insertBatch(obj);
    }

    @Override
    public Boolean updateBatchById(List<B> list) {
        List<T> obj = MapstructUtil.convert(list, getTClass());
        return getBaseMapper().updateBatchById(obj);
    }

    // =========================sql==============================

    @Override
    public Map<String, Object> getMapBySql(@Param("sql") String sql) {
        return getBaseMapper().getMapBySql(sql);
    }

    @Override
    public V getVoBySql(@Param("sql") String sql) {
        return getBaseMapper().getVoBySql(sql);
    }

    @Override
    public List<Map<String, Object>> getListMapBySql(@Param("sql") String sql) {
        return getBaseMapper().getListMapBySql(sql);
    }

    @Override
    public List<V> getListVoBySql(@Param("sql") String sql) {
        return getBaseMapper().getListVoBySql(sql);
    }

    @Override
    public TableDataInfo<V> getPageVoBySql(@Param("sql") String sql) {
        return getBaseMapper().getPageVoBySql(sql);
    }

    @Override
    public Boolean updateBysql(String sql) {
        return getBaseMapper().updatemysql(sql) > 0;
    }

    @Override
    public Boolean deleteBySql(String sql) {
        return getBaseMapper().deleteBySql(sql) > 0;
    }


    //判断空
    public boolean isEmpty(String str) {
        return (null == str) || (str.trim().length() <= 0);
    }

    public boolean isEmpty(Character cha) {
        return (null == cha) || cha.equals(' ');
    }

    public boolean isEmpty(Object obj) {
        return (null == obj);
    }

    public boolean isEmpty(Object[] objs) {
        return (null == objs) || (objs.length <= 0);
    }

    public boolean isEmpty(Collection<?> obj) {
        return (null == obj) || obj.isEmpty();
    }

    public boolean isEmpty(Set<?> set) {
        return (null == set) || set.isEmpty();
    }

    public boolean isEmpty(Serializable obj) {
        return null == obj;
    }

    public boolean isEmpty(Map<?, ?> map) {
        return (null == map) || map.isEmpty();
    }
}

