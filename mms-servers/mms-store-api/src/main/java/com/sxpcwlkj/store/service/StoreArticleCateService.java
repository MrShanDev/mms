package com.sxpcwlkj.store.service;

import com.sxpcwlkj.framework.service.BaseService;
import com.sxpcwlkj.store.entity.StoreArticleCate;
import com.sxpcwlkj.store.entity.bo.StoreArticleCateBo;
import com.sxpcwlkj.store.entity.export.StoreArticleCateExport;
import com.sxpcwlkj.store.entity.vo.StoreArticleCateVo;

import java.util.List;
import java.util.Set;

/**
 * 店铺文章分类-接口
 *
 * @author mmsAdmin
 * @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 * @describe  支持自定义扩展,已继承接口：{insert、deleteById、updateById、selectById、getByEntityListPage}（更多查看BaseService接口）
 */
public interface StoreArticleCateService extends BaseService<StoreArticleCate, StoreArticleCateVo, StoreArticleCateBo> {
    /**
    * 店铺文章分类列表
    * @param isAll true：全部数据 false：有效数据(status=0)
    * @param showLevel 显示级别，0：全部 1：一级 2：二级 3：三级
    * @return 店铺文章分类数结构列表
    */
    List<StoreArticleCateVo> queryTree(boolean isAll,int showLevel);

    /**
    * 按照店铺文章分类ID，查询下级所有店铺文章分类
    *
    * @param id      店铺文章分类ID
    * @param endList 最终的数据
    */
    void queryListSon(String id, List<StoreArticleCateVo> endList);
    /**
    * 导出店铺文章分类
    * @param list 店铺文章分类列表
    * @return true：成功 false ：失败
    */
    Boolean imports(Set<StoreArticleCateExport> list);
}
